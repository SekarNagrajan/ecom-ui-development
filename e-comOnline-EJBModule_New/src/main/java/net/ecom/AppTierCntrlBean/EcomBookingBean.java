/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.json.SvmJsonList;
import com.svm.lrp.nfr.multiAgencyUtil.MultiAgencyUtil;
import com.svm.lrp.nfr.query.cbean.VDSInvoker;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import net.ecom.common.BookingSummaryComparator;
import net.ecom.common.DateConversion_New;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.ZipFileConversion;
import net.ecom.utils.JDBCServiceLocatorFactory;
import net.ecom.v1.cntlbean.EcomV1BookingBeanRemote;
import net.ecom.vds.service.EcomBKGVdsService;
import net.ecom.vds.service.EcomBKGSpsService;
import net.ecom.vds.service.EcomGeneralVDSService;
import net.ecom.vds.serviceVo.Allschedules;
import net.ecom.vds.serviceVo.Booking;
import net.ecom.vds.serviceVo.EcomBKGCargo;
import net.ecom.vds.serviceVo.EcomBKGCommDG;
import net.ecom.vds.serviceVo.EcomBKGCommodity;
import net.ecom.vds.serviceVo.EcomBKGCommonModel;
import net.ecom.vds.serviceVo.EcomBKGConf;
import net.ecom.vds.serviceVo.EcomCommonModel;
import net.ecom.vds.serviceVo.EcomBKGPrintVO;
import net.ecom.vds.serviceVo.EcomBKGPrintCommonModel;
import net.ecom.vds.serviceVo.EcomBKGCustomer;
import net.ecom.vds.serviceVo.EcomBKGEDIDocStatus;
import net.ecom.vds.serviceVo.EcomBKGHaulage;
import net.ecom.vds.serviceVo.EcomBKGReaderMaster;

import net.ecom.vds.serviceVo.EcomBKGLeg;
import net.ecom.vds.serviceVo.EcomBKGMasterVO;
import net.ecom.vds.serviceVo.EcomBKGReference;
import net.ecom.vds.serviceVo.EcomBKGRemarks;
import net.ecom.vds.serviceVo.EcomBKGRouting;
import net.ecom.vds.serviceVo.EcomInsuranceVo;
import net.ecom.vds.serviceVo.Editempbooking_ics_new;
import net.ecom.vds.serviceVo.RouteVo;
import net.ecom.vds.serviceVo.Routedetail;
import net.ecom.vds.serviceVo.RoutingVo;
import net.ecom.vds.serviceVo.ScheduleDataModel;
import net.ecom.vo.Agencydto;
import net.ecom.vo.AutoLegVo;
import net.ecom.vo.BetPortsSortVo;
import net.ecom.vo.BkgContainerHaulageDetails;
import net.ecom.vo.BookingHaulagedetailsVo;
import net.ecom.vo.BoundConfigVo;
import net.ecom.vo.BoundDefultVo;
import net.ecom.vo.CargoProtectVo;
import net.ecom.vo.ColumnList;
import net.ecom.vo.CommoditySearchVO;
import net.ecom.vo.CommonBKGJaserModel;
import net.ecom.vo.CustomerDetailsVO;
import net.ecom.vo.CustomerProfile;
import net.ecom.vo.CustomerSearch;
import net.ecom.vo.CutoffConfigVO;
import net.ecom.vo.DropDownVO;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.EbookingValidatorFormBeanNew;
import net.ecom.vo.EbookingViewAll;
import net.ecom.vo.EcomBkgPrintBookingSaveVo;
import net.ecom.vo.FileDetailsVO;
import net.ecom.vo.LayoverTimeVo;
import net.ecom.vo.MailFormatVO;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.Route;
import net.ecom.vo.RouteAvailVo;
import net.ecom.vo.SurveyList;
import net.ecom.vo.TransScheduleLegVo;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

@Stateless(name = "EcomBookingBean")
@Remote(EcomBookingBeanRemote.class)
@Resources({
        @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS"),
        @Resource(name = "jdbc/MASTERDB", mappedName = "java:/MASTERDS"),
        @Resource(name = "jdbc/VSSDB", mappedName = "java:/VSSDS"),
        @Resource(name = "jdbc/ReportDB", mappedName = "java:/REPORTDS"),
        @Resource(name = "jdbc/DOCDB", mappedName = "java:/DOCDS")
})
// @Startup
// @TransactionManagement(TransactionManagementType.BEAN)
public class EcomBookingBean implements EcomBookingBeanRemote {

    @Resource
    private SessionContext context;

    /**
     * ESLGEN-8237: Cache haulage transit hours by Origin|Delivery|TransportMode.
     * Value is hours (Days*24), or -1 when no matching row. Cleared each getSchdule_Trust call.
     */
    private static final ConcurrentHashMap<String, Integer> HAULAGE_TRANSIT_HOURS_CACHE = new ConcurrentHashMap<String, Integer>();

    private static String haulageTransitHoursCacheKey(String originPort, String deliveryPort, String transportMode) {
        String o = originPort == null ? "" : originPort.trim().toUpperCase();
        String d = deliveryPort == null ? "" : deliveryPort.trim().toUpperCase();
        String m = transportMode == null ? "" : transportMode.trim().toUpperCase();
        return o + '\u001F' + d + '\u001F' + m;
    }

    protected static final String bookingsummaryquery = "exec usp_getecombookingsummary ?,?,?,?,?,?";

    protected static final String getBookingColumnsquery = "select Comp_Name as companyName,First_Name as firstName,Last_Name as lastname,control_location as locationCode,User_Email as Email,address from ecom_reg_Details cu(nolock) where 2=2";
    protected static final String getCustomerDataquery = "select top 100 Comp_Name as companyName,First_Name as firstName,Last_Name as lastname,c.cityName,User_Email as Email,address from ecom_reg_Details cu(nolock) left outer join city c(nolock) on cu.locationcode=c.citydescription";

    protected static final String QrygetEDIID = "select ediid from edidocconfig(nolock) where ediname='" + EcomConsts.ONLINE_BOOKING + "'";

    protected static final String checkTemplateqry = "select templatename from ecombooking where templatename=? and usercr=? and ebook_status ='T'";
    protected static final String TEMPLATE_BOOKING = "delete from ecombooking where ebookno=?";
    protected static final String TEMPLATE_BOOKING_CUST = "delete from ecombooking_cust where ebookno=?";
    protected static final String TEMPLATE_BOOKING_CARGO = "delete from ecombooking_cargo where ebookno=?";
    protected static final String TEMPLATE_BOOKING_COMM = "delete from ecombooking_commodity where ebookno=?";
    protected static final String TEMPLATE_BOOKING_DG = "delete from ecombooking_dg where ebookno=?";
    protected static final String TEMPLATE_BOOKING_OOG = "delete from ecombooking_oog where ebookno=?";
    protected static final String TEMPLATE_BOOKING_REFER = "delete from ecombooking_refer where ebookno=?";
    protected static final String TEMPLATE_BOOKING_FILES = "delete from ecombooking_files where ebookno=?";
    protected static final String TEMPLATE_BOOKING_LEG = "delete from  ecombooking_leg where ebookno=?";
    //
    protected static final String BOOKING_SUMMARY_COUNT = "exec usp_getecombookingsummary ?,?,?,?,?,?";
    //
    protected static final String GET_BOOKING_MASTEREDIT = "select agencyid,originPortId,"
            + "finalPortId,loadPortId, dischargePortId,bookDate, bookReady, preCarriage,TemplateName, onCarriage,  ebook_status,intransitport,"
            + "remarks ,rateno,routeId,AgencyRefNo,isamendment,emptypickup,emptypickupdate,oceanfreight,"
            + "trptsvccode,nat_code,pickup_point,salesagreedrate,ccmailid,haulercode,haulagetype,pickupdate,dropdate,exportRef,releasetype,custPO,customerreference,ACID,cargoprotectid,ShipperReference,ensfiling,t2lfiling,sisubmitparty,enstype from ecombooking(nolock) where ebookno = ? ";
    protected static final String GET_BOOKING_CUSTVIEW = "select custTypeId, custId, countryId,name, address1,address2, city, "
            + "zipCode, telephone, fax, email,cid,flag,toorder,stateid,eori,persontype from ecombooking_cust(nolock) "
            + "where ebookno = ?";
    protected static final String GET_BOOKING_CUSTEDIT = "select bc.custTypeId,bc.custId,bc.countryId,bc.name,bc.address1,bc.address2,bc.city,bc.zipCode,bc.telephone,bc.fax,"
            + "bc.email,bc.cid,bc.flag,bc.toorder,bc.stateid,bc.eori,bc.persontype from ecombooking_cust bc(nolock) left join customermaster cm on cm.custcode = bc.custId and cm.status='A' and cm.inactiveflag='A' where bc.ebookno = ?";
    protected static final String GET_BOOKING_CUSTTEMPLATE = "select bc.custTypeId,bc.custId,cm.countryId,cm.custname as name,cm.address1,cm.address2,cm.city,cm.zipCode,cm.phone as telephone,cm.fax,cm.email,bc.cid,bc.flag,bc.toorder,bc.stateid,bc.eori,bc.persontype from ecombooking_cust bc(nolock) inner join customermaster cm on cm.custcode = bc.custId and cm.status='A' and cm.inactiveflag='A' where bc.ebookno = ?\n"
            + "union\n"
            + "select bc.custTypeId,bc.custId,bc.countryId,bc.name,bc.address1,bc.address2,bc.city,bc.zipCode,bc.telephone,bc.fax,bc.email,bc.cid,bc.flag,bc.toorder,bc.stateid,bc.eori,bc.persontype from ecombooking_cust bc(nolock) where (bc.custId is null  or len(bc.custId)=0)and bc.ebookno = ?";
    protected static final String GET_BOOKING_FILES = "select filecatgeory,filename from ecombooking_files where ebookno=?";
    protected static final String GET_BOOKING_EXTBOOKNO = "select extBookNo from ecombooking where ebookno=?";
    protected static final String BOOKING_SUMMARY_GENERATION = "insert into ecombookingsummarygeneration(id,book_no,originPortId,finalPortId,datecr,"
            + "dateup,status,ecomlrpstatus,agencyrefno,appversion,dgstatus,confirmeddate) values(?,?,?,?,?,?,?,?,?,?,?,?)";

    protected static final String FORWARD_MESSAGE_QUERY = "select message from ecom_messageformat(nolock) where Status='a' and Type ='msg' "
            + "and criteria =? and Module =? and usertype=? ";
    protected static final String FORWARD_MESSAGE_LANG_QUERY = "select message from ecom_messageformatlang(nolock) where Status='a' and Type ='msg'"
            + "and criteria =? and Module =? and usertype=? and lang=? ";

    protected static final String GET_CUTOFFTERMINAL = "select arrdate from allschedules where groupcallid=?";
    protected static final String Routingfromleg_qry = "select leg_no,bound,servicecode,vesselcode,voyagecode,loadportid,dischargeportid,etddate,etadate,legtypeid,groupcallid,callIdLoading,callIdDischarge,loadterminal,dischargeTerminal,dbound from ecombooking_leg(nolock) where ebookno =?";
    private static final String BOOK_NO_GEN = "select mdlid,mdlprefix,mdlindex from ecomMdlIndex where mdlid = ?";
    private static final String BOOK_NO_UPDATE = "update ecomMdlIndex set mdlindex=? where mdlid =?";
    protected static final String GetLoginCustomerquery = "select comp.countryid as Country,comp.custname as comp_name,comp.address1 as Address,comp.address2 as Address1,City,um.email as User_Email,um.contactname as name,"
            + " um.fax as fax,um.phone as phone,comp.postalcode as Pin_Code"
            + " from customercontacts  um (nolock) inner join customermaster comp(nolock) on um.custcode=comp.custcode"
            + " where  comp.custcode =? and um.webid=?";
    protected static final String GET_MAIL_CONTENT = "select message,from_mailid,subject,activitycode from ecom_messageformat(nolock) where Status='a' and Type ='mail' and criteria = ? and Module =? and usertype = ?";

    protected static final String MoreCustomerDetailsQry = "exec usp_getEcomLastestCustomerFromBooking ?,'366','MC','',?";
    protected static final String SAVE_EBOOKING_CUST_QRY = "insert into ecombooking_cust(ebookno, custTypeId, custId, "
            + "countryId, name, address1, city, zipCode, telephone, fax, email, address2,flag,toorder,stateId,eori,persontype) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";
    protected static final String SAVE_NEW_EBOOKING_MASTER_QRY = "insert into ecombooking (ebookno, agencyid,"
            + "originPortId,finalPortId, loadPortId, dischargePortId,bookDate,bookReady,preCarriage, onCarriage,remarks, "
            + " usercr,datecr,ebook_status,cust_code,templatename,rateno, "
            + " routeId,agencyRefNo,oceanfreight,trptsvccode,emptypickup,emptypickupdate,version,intransitport,submitted_date,dateup,userup,nat_code,pickup_point,appversion,salesagreedrate,ccmailid,haulercode,haulagetype,pickupdate,dropdate,exportRef,releasetype,custPO,customerreference,ACID,cargoprotectid,ShipperReference,ensfiling,t2lfiling,sisubmitparty,enstype) "
            + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,getdate(),?,?,?,?,?,?,?,?,?,?,?,?,getdate(),getdate(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    protected static final String SAVE_EBOOKING_FILE = "insert into ecombooking_files (ebookno,FileName,FileCatgeory) values(?,?,?)";
    protected static final String DELETE_FILES = "delete ecombooking_files where ebookno=?";
    protected static final String ECOMBOOKING_LEG = "insert into ecombooking_leg(ebookno,leg_no,bound,servicecode,vesselcode,loadportid,dischargeportid,etddate,etadate,legTypeId,loadtype,dischtype,voyagecode,groupcallid,loadTerminal,dischargeTerminal,callIdLoading,callIdDischarge,dbound)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    protected static final String BOOKING_CARGO = "select ebookno,conid,containerno,eqpType, eqpSize, quantity, soc,ref_nor,dgFlag,tareWeight,grossWeight,cargoWeight,weightUnit,eqpstatus,oog from ecombooking_cargo(nolock)where ebookno=?";
    protected static final String BOOKING_COMMODITY = "select ebookno,conid, itemno, commodityFlag, commodity, HSCommodity,package,packageQty,cargoVolume,volumnUnit,nettwt,descr,bycode,byname,slcode,slname,cmdty_hblno from ecombooking_commodity(nolock)where ebookno=?";
    protected static final String BOOKING_DG = "select ebookno,conid,itemno,MarinePollutant,unNo,class,FlashPt,FlashPtUnit from ecombooking_dg(nolock)where ebookno=?";
    protected static final String BOOKING_OOG = "select ebookno,conid,containerno,leftft,rightft,forward,alt,topft,unit,comItemNo from ecombooking_oog(nolock)where ebookno=?";
    protected static final String BOOKING_REFER = "select ebookno,itemno,containerno,ref_setTemp,ref_minTemp,ref_maxTemp,ref_tempUnit,humidity,humidity_flag,drains,co2value,o2value,ventpercentage from ecombooking_refer(nolock)where ebookno=?";
    protected static final String GET_BOOKING_CUSTOMERSEARCH_QUERY = "select CustCode,Custname,Email from customermaster(nolock) where 2=2";
    protected static final String UPDATE_BOOKING_MASTER = "update ecombooking set agencyid=?,"
            + "originPortId=?,finalPortId=?, loadPortId=?, dischargePortId=?,bookDate=?,bookReady=?,preCarriage=?, onCarriage=?,remarks=?, "
            + " userup=?,dateup=getdate(),ebook_status=?,cust_code=?,templatename=?,rateno=?, "
            + " routeId=?,agencyRefNo=?,oceanfreight=?,trptsvccode=?,emptypickup=?,emptypickupdate=?,intransitport=?,nat_code=?,pickup_point=?,appversion=?,salesagreedrate=?,ccmailid=?,haulercode=?,haulagetype=?,pickupdate=?,dropdate=?,exportRef=?,releasetype=?,custPO=?,customerreference=?,ACID=?,cargoprotectid=?,ShipperReference=?,ensfiling=?,t2lfiling=?,sisubmitparty=?,enstype=? where ebookno=?";
    protected static final String DELETE_ECOM_BOOKING_LEG = "delete from ecombooking_leg where ebookno=?";
    protected static final String INSERT_ECOM_BOOKING_LEG = "insert into ecombooking_leg(ebookno,leg_no,bound,servicecode,vesselcode,loadportid,dischargeportid,etddate,etadate,legTypeId,loadtype,dischtype,voyagecode,groupcallid,loadTerminal,dischargeTerminal,callIdLoading,callIdDischarge,dbound)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    protected static final String DELETE_ECOM_BKG_CUSTDELETE = "delete from ecombooking_cust where ebookno=?";
    protected static final String DELETE_ECOM_BKG_CARGO = "delete ecombooking_cargo where ebookno=?";
    protected static final String DELETE_ECOM_BKG_COMMODITY = "delete ecombooking_commodity where ebookno=?";
    protected static final String DELETE_ECOM_BKG_DG = "delete ecombooking_dg where ebookno=?";
    protected static final String DELETE_ECOM_BKG_OOG = "delete ecombooking_oog where ebookno=?";
    protected static final String DELETE_ECOM_BKG_REFER = "delete ecombooking_refer where ebookno=?";
    protected static final String SAVE_EBOOKING_CONT_QRY_NEW = "insert into ecombooking_cargo (ebookno,conid, eqpType, eqpSize, quantity, soc,ref_nor,dgFlag,tareWeight,grossWeight,cargoWeight,weightUnit,eqpstatus,oog) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    protected static final String SAVE_EBOOKING_COMMODITY_QRY_NEW = "insert into ecombooking_commodity(ebookno,conid, itemno, commodityFlag, commodity, HSCommodity,package,packageQty,cargoVolume,volumnUnit,nettwt,descr,bycode,byname,slcode,slname,cmdty_hblno) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    protected static final String SAVE_EBOOKING_DG_QRY_NEW = "insert into ecombooking_dg(ebookno,conid, itemno, MarinePollutant, unNo, class,FlashPt,FlashPtUnit) values(?,?,?,?,?,?,?,?)";
    protected static final String SAVE_EBOOKING_OOG_QRY_NEW = "insert into ecombooking_oog(ebookno,conid,leftft, rightft, forward,alt,topft,unit,comitemno) values(?,?,?,?,?,?,?,?,?)";
    protected static final String SAVE_EBOOKING_REFER_QRY_NEW = "insert into ecombooking_refer (ebookno,conid,ref_setTemp, ref_minTemp, ref_maxTemp, ref_tempUnit, Humidity, Humidity_flag, Drains, CO2Value, O2Value, itemno,VentPercentage) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SAVE_CCMAIN_USERDET = "update ecomuserdetails set ccmailid=? where emailid=?";
    private static final String UPDATE_ECOM_CANCELBKG = "update ecombooking set ebook_status=? where ebookno=?";
    private static final String GET_ECOM_BOOKING_STATUS = "select ebook_status from ecombooking where ebookno=?";
    // V1 Query
    private static final String V1_LRP_BOOKING_QRY = "exec usp_getecombooking ?,?,?,?";

    // Haulage details
    private static final String SAVE_ECOM_BKG_HAULAGE = "insert into ecombooking_haulage (ebookno,eqprowid,eqptypeid,loadLocation,loadlocationname,contactPerson,contactAddress,contactPhone,contactEmail,referenceNumber,dropDate,remarks,docType,overWeight,stopSequence,TMSClientCode,eqpId) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String GET_ECOM_BKG_HAULAGE_DETAILS = "select ebookno,eqprowid,eqpId,eqptypeid,loadLocation,loadlocationname,contactPerson,contactAddress,contactPhone,contactEmail,referenceNumber,dropDate,remarks,docType,overWeight,stopSequence,TMSClientCode from ecombooking_haulage where ebookno=?";
    private static final String DELETE_ECOM_BKG_HAULAGE = "delete from ecombooking_haulage where ebookno=?";
    private static final String INSERT_ECOMBOOKNG_CONF = "insert into ecombooking_conf (ebookno,bkgCutoff,docCutoff,vgmCutoff,bbroroCutoff,hazCutoff,portentryCutoff) values(?,?,?,?,?,?,?)";
    private static final String DELETE_ECOMBOOKNG_CONF = "delete from ecombooking_conf where ebookno=?";
    private static final String GET_ECOMBOOKNG_CONF = "select ebookno,bkgcutoff,doccutoff,vgmcutoff,bbrorocutoff,hazcutoff,portentrycutoff from ecombooking_conf where ebookno=?";
    protected static final String GET_MAIL_CONTENT_LANG = "select message,from_mailid,subject,activitycode from ecom_messageformatlang(nolock) where Status='a' and Type ='mail' and criteria = ? and Module =? and usertype = ? and lang=?";

    private static final String INSERT_BOOKING_CANCEL = "INSERT INTO ecombooking_cancel "
            + "(eBookNo, extBookNo, reason, remarks, status, usercr, datecr) "
            + "VALUES (?, ?, ?, ?, ?, ?, GETDATE())";

    // ENS Filling
    private static final String INSERT_BOOKING_ENSDEC = "INSERT INTO ecombooking_ics_new "
            + "(ebookno, eucustom, bill_type, ens_type, payment_mode, cust_name, "
            + "address, address1, country, state, telephone, email, enseori, filing_type, city,zipcode) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
    private static final String DELETE_BOOKING_ENSDEC = "DELETE FROM ecombooking_ics_new WHERE ebookno = ?";
    private static final String GET_BOOKING_ENSDEC = "SELECT ebookno, eucustom, bill_type, ens_type, payment_mode, cust_name, address, address1, country, state, telephone, email, enseori, filing_type, city,zipcode FROM ecombooking_ics_new WHERE ebookno = ?";
    //
    protected static final String bookingHaulageDetails = "exec usp_getbookingHaulageTracking ?";

    @Inject
    VDSInvoker vds;

    EcomBKGVdsService ecomvdsservice;

    EcomBKGSpsService ecomvdspservice;
    EcomGeneralVDSService ecomGenService;

    @EJB(mappedName = "java:global/e-comOnline-EJBModule_New/EcomV1BookingBean!net.ecom.v1.cntlbean.EcomV1BookingBeanRemote")
    EcomV1BookingBeanRemote v1BKGBeanObj;

    @EJB(mappedName = "java:global/e-comOnline-EJBModule_New/EComCntrlBean!net.ecom.AppTierCntrlBean.EComCntrlBeanRemote")
    EComCntrlBeanRemote cntrlBeanObj;

    @EJB(mappedName = "java:global/e-comOnline-EJBModule_New/EcomScheduleBean!net.ecom.AppTierCntrlBean.EcomScheduleBeanRemote")
    EcomScheduleBeanRemote scheduleBeanObj;

    @PostConstruct
    public void inti() {
        ecomvdsservice = vds.create(EcomBKGVdsService.class);
        ecomvdspservice = vds.create(EcomBKGSpsService.class);
        ecomGenService = vds.create(EcomGeneralVDSService.class);
    }

    @Override
    public ArrayList getBookingDetail(String usercode, String fromDate, String toDate, String type, String custType,
            String v1DataEnableStatus) throws RemoteException {
        ArrayList view = new ArrayList();
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement ps = null;
        EbookingViewAll eBooking = null;
        String randomid = null, appversion = null, dgstatus = null;
        try {

            con = new lrp.serverutils.refs.ServerObject("ReportDB").getConnection();
            // randomid = getBookingSummaryDetails(usercode, fromDate, toDate, type,
            // custType, v1DataEnableStatus);
            ps = con.prepareStatement(bookingsummaryquery);
            ps.setString(1, usercode);
            ps.setString(2, "");
            ps.setString(3, type);
            ps.setString(4, fromDate);
            ps.setString(5, toDate);
            ps.setString(6, "S");
            System.out.println("[ECOM] EcomBooingBean usercode: " + usercode + ", randomid: " + randomid + ", type: "
                    + type + ", fromDate: " + fromDate + ", toDate: " + toDate);

            rs = ps.executeQuery();
            while (rs.next()) {
                eBooking = new EbookingViewAll();
                eBooking.setBookNo(checkNull(rs.getString("onlinebookno")));
                eBooking.setLrpEcomStatus(checkNull(rs.getString("ecomlrpstatus")));
                eBooking.setOriginPortId(checkNull(rs.getString("originPortId")));
                eBooking.setFinalPortId(checkNull(rs.getString("finalPortId")));
                eBooking.setStatus(checkNull(rs.getString("status")));
                eBooking.setTrust_book_no(checkNull(rs.getString("trust_book_no")));
                eBooking.setTrust_action(checkNull(rs.getString("trust_book_no")));
                eBooking.setCruser(checkNull(rs.getString("submittedby")));
                eBooking.setCrdate(rs.getString("datecr"));
                eBooking.setSubmitdate(checkNull(rs.getString("dateup")));
                eBooking.setLrpstatus(checkNull(rs.getString("status")));
                eBooking.setAgencyrefno(checkNull(rs.getString("agencyrefno")));
                eBooking.setAgencyId(checkNull(rs.getString("agencyid")));
                eBooking.setHaulageflag(checkNull(rs.getString("haulageflag")));
                appversion = checkNull(rs.getString("appversion"));
                dgstatus = checkNull(rs.getString("dgstatus"));
                eBooking.setTuesCount(checkNull(rs.getString("tuescount")));

                if ("1".equalsIgnoreCase(appversion)) {
                    eBooking.setAppVersion("1");
                } else {
                    eBooking.setAppVersion("2");
                }
                if ("A".equalsIgnoreCase(dgstatus)) {
                    eBooking.setDgStatus("DG Approved");
                } else if ("D".equalsIgnoreCase(dgstatus)) {
                    eBooking.setDgStatus("DG Approval Pending");
                } else {
                    eBooking.setDgStatus("");
                }
                eBooking.setConfirmedDate(checkNull(rs.getString("confirmeddate")));
                eBooking.setStatusDesc(checkNull(rs.getString("statusdesc")));
                eBooking.setShowExtBookNo(checkNull(rs.getString("showrefno")));
                eBooking.setLrpusercr(checkNull(rs.getString("lrpusercr")));
                eBooking.setPoletd(checkNull(rs.getString("poletd")));
                eBooking.setPayAmountUsd(checkNull(rs.getString("pay_amount_usd")));
                eBooking.setPayCustomerType(checkNull(rs.getString("pay_customer_type")));
                eBooking.setPayAgency(checkNull(rs.getString("payagency")));
                eBooking.setPaymentEligible(checkNull(rs.getString("is_payment_eligible")));
                eBooking.setPaymentCompleted(checkNull(rs.getString("is_payment_completed")));
                eBooking.setPaymentRequestId(checkNull(rs.getString("paymentRequestId")));
                view.add(eBooking);
            }
            ArrayList v1bkglis = getV1BookingSummaryDetails(usercode, fromDate, toDate, type, custType,
                    v1DataEnableStatus);
            view.addAll(v1bkglis);
            System.out.println("[ECOM] EcomBooingBean view size-> " + view.size());
            if (view != null && view.size() > 0) {
                BookingSummaryComparator objcmp = new BookingSummaryComparator();
                Collections.sort(view, objcmp);
            }
            System.out.println("[ECOM] EcomBooingBean view size after comparator-> " + view.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return view;
    }

    private ArrayList getV1BookingSummaryDetails(String custcode, String fromDate, String toDate, String type,
            String custType, String v1DataEnableStatus) {
        ResultSet rs = null;
        Connection con = null, v1Con = null;
        PreparedStatement ps = null;
        ArrayList v1view = new ArrayList();
        EbookingViewAll eBooking = null;
        try {
            //
            String book_no = null, agencyrefno = null, originPort = null, finalPort = null, status = null,
                    ecomLrpStatus = null, dateCreate = null, dateUpdate = null;

            // V1 lrp booking data insertion
            if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
                v1Con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
                ps = v1Con.prepareStatement(V1_LRP_BOOKING_QRY);
                ps.setString(1, custcode);
                ps.setString(2, fromDate);
                ps.setString(3, toDate);
                ps.setString(4, type);
                rs = ps.executeQuery();
                while (rs.next()) {
                    book_no = checkNull(rs.getString("book_no"));
                    originPort = checkNull(rs.getString("originPortId"));
                    finalPort = checkNull(rs.getString("finalPortId"));
                    status = checkNull(rs.getString("status"));
                    ecomLrpStatus = checkNull(rs.getString("ecomlrpstatus"));
                    dateCreate = checkNull(rs.getString("datecr"));
                    dateUpdate = checkNull(rs.getString("dateup"));
                    agencyrefno = checkNull(rs.getString("agencyrefno"));

                    eBooking = new EbookingViewAll();
                    eBooking.setBookNo("");
                    eBooking.setLrpEcomStatus(checkNull(ecomLrpStatus));
                    eBooking.setOriginPortId(checkNull(originPort));
                    eBooking.setFinalPortId(checkNull(finalPort));
                    eBooking.setStatus(checkNull(status));
                    eBooking.setTrust_book_no(checkNull(book_no));
                    eBooking.setTrust_action(checkNull(book_no));
                    // eBooking.setCruser(checkNull(rs.getString("submittedby")));
                    eBooking.setCrdate(dateCreate);
                    eBooking.setSubmitdate(checkNull(dateUpdate));
                    eBooking.setLrpstatus(checkNull(status));
                    eBooking.setAgencyrefno(checkNull(agencyrefno));
                    // eBooking.setAgencyId(checkNull(rs.getString("agencyid")));
                    eBooking.setShowExtBookNo("Y");
                    eBooking.setAppVersion("1");
                    eBooking.setDgStatus("");

                    eBooking.setStatusDesc(("C".equalsIgnoreCase(status) ? "Confirmed" : "Draft"));

                    v1view.add(eBooking);
                }
                System.out.println("[ECOM] EcomBooingBean v1view size -> " + v1view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
                if (v1Con != null) {
                    v1Con.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return v1view;
    }

    private String getBookingSummaryDetails(String custcode, String fromDate, String toDate, String type,
            String custType, String v1DataEnableStatus) {
        ResultSet rs = null;
        Connection con = null, v1Con = null;
        PreparedStatement ps = null;
        String randomid = null, dgstatus = null;
        SvmJsonList alist = null;
        List<Booking> lst = null;
        Gson gson = new Gson();
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            //
            String book_no = null, agencyrefno = null, originPort = null, finalPort = null, status = null,
                    ecomLrpStatus = null, dateCreate = null, dateUpdate = null;
            String confirmedDate;
            int iRandomNo = 0;
            iRandomNo = get4DigitRandomNumber();
            //
            randomid = String.valueOf(iRandomNo);
            alist = new SvmJsonList();
            alist.add(custcode);
            alist.add(fromDate);
            alist.add(toDate);
            alist.add(type);
            alist.add(custType);
            // VDS Service for Booking Summary

            // BookingSummaryVo hs = (BookingSummaryVo)
            // ecomvdsservice.getecombookingsummarydetails(custcode, date, custType);
            //////
            ps = con.prepareStatement(BOOKING_SUMMARY_GENERATION);
            Map resultMap = ecomvdspservice.getEcomLRPBookings(alist);
            if (resultMap != null && resultMap.size() > 0) {
                resultMap = (Map) resultMap.get("rs");
                if (resultMap != null && resultMap.size() > 0) {
                    lst = gson.fromJson(gson.toJson((ArrayList) resultMap.get("rs0")), new TypeToken<List<Booking>>() {
                    }.getType());
                    if (lst != null && lst.size() > 0) {
                        for (int i = 0; i < lst.size(); i++) {
                            Booking bkg = lst.get(i);
                            book_no = checkNull(bkg.getBook_no());
                            originPort = checkNull(bkg.getOriginportid());
                            finalPort = checkNull(bkg.getFinalportid());
                            status = checkNull(bkg.getStatus());
                            dateCreate = checkNull(bkg.getDatecr());
                            dateUpdate = checkNull(bkg.getDateup());
                            agencyrefno = checkNull(bkg.getAgencyrefno());
                            ecomLrpStatus = "L";
                            dgstatus = checkNull(bkg.getDgstatus());
                            confirmedDate = checkNull(bkg.getConfirmedDate());
                            //
                            ps.setString(1, randomid);
                            ps.setString(2, book_no);
                            ps.setString(3, originPort);
                            ps.setString(4, finalPort);
                            ps.setString(5, dateCreate);
                            ps.setString(6, dateUpdate);
                            ps.setString(7, status);
                            ps.setString(8, ecomLrpStatus);
                            ps.setString(9, agencyrefno);
                            ps.setInt(10, 2);
                            ps.setString(11, dgstatus);
                            ps.setString(12, confirmedDate);
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }

            }

            // V1 lrp booking data insertion
            if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
                v1Con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
                ps = v1Con.prepareStatement(V1_LRP_BOOKING_QRY);
                ps.setString(1, custcode);
                ps.setString(2, fromDate);
                ps.setString(3, toDate);
                ps.setString(4, type);
                rs = ps.executeQuery();
                ps = con.prepareStatement(BOOKING_SUMMARY_GENERATION);
                while (rs.next()) {
                    book_no = checkNull(rs.getString("book_no"));
                    originPort = checkNull(rs.getString("originPortId"));
                    finalPort = checkNull(rs.getString("finalPortId"));
                    status = checkNull(rs.getString("status"));
                    ecomLrpStatus = checkNull(rs.getString("ecomlrpstatus"));
                    dateCreate = checkNull(rs.getString("datecr"));
                    dateUpdate = checkNull(rs.getString("dateup"));
                    agencyrefno = checkNull(rs.getString("agencyrefno"));
                    ps.setString(1, randomid);
                    ps.setString(2, book_no);
                    ps.setString(3, originPort);
                    ps.setString(4, finalPort);
                    ps.setString(5, dateCreate);
                    ps.setString(6, dateUpdate);
                    ps.setString(7, status);
                    ps.setString(8, ecomLrpStatus);
                    ps.setString(9, agencyrefno);
                    ps.setInt(10, 1);
                    ps.setString(11, null);
                    ps.setString(12, null);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
                if (v1Con != null) {
                    v1Con.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return randomid;
    }

    @Override
    public HashMap getBookingCount(String custno, String custtypeconfig, String v1DataEnableStatus, String monthyr) throws RemoteException {
        HashMap bookdetails = new HashMap();
        ResultSet rs = null;
        String randomid = null;
        Connection con = null;
        PreparedStatement ps = null;
        int dc = 0, sa = 0, draft = 0, cf = 0, a = 0;
        try {
            String fromDate = "", toDate = "",type="A";
            if (checkNull(monthyr).length() > 0 && monthyr.contains("-")) {
                String str[] = monthyr.split("-");
                int month = Integer.parseInt(str[0]);
                int year = Integer.parseInt(str[1]);

                YearMonth yearMonth = YearMonth.of(year, month);
                // Get the first and last day of the month
                LocalDate startOfMonth = yearMonth.atDay(1);
                LocalDate endOfMonth = yearMonth.atEndOfMonth();
                fromDate = startOfMonth.toString();
                toDate = endOfMonth.toString();
                type="D";
            } 
            con = new lrp.serverutils.refs.ServerObject("ReportDB").getConnection();
            randomid = getBookingSummaryDetails(custno, "", "", "A", custtypeconfig, v1DataEnableStatus);
            ps = con.prepareStatement(BOOKING_SUMMARY_COUNT);
            ps.setString(1, custno);
            ps.setString(2, randomid);
            ps.setString(3, type);
            ps.setString(4, fromDate);
            ps.setString(5, toDate);
            ps.setString(6, "D");

            rs = ps.executeQuery();
            while (rs.next()) {
                if ("D".equalsIgnoreCase(rs.getString("status"))) {
                    draft = rs.getInt("countval");
                } else if ("S".equalsIgnoreCase(rs.getString("status"))) {
                    sa = rs.getInt("countval");
                } else if ("V".equalsIgnoreCase(rs.getString("status"))) {
                    dc = rs.getInt("countval");
                } else if ("P".equalsIgnoreCase(rs.getString("status"))) {
                    a = rs.getInt("countval");
                } else if ("C".equalsIgnoreCase(rs.getString("status"))) {
                    cf = rs.getInt("countval");
                }
            }
            bookdetails.put("Draft", String.valueOf(draft));
            bookdetails.put("Send to Agency", String.valueOf(sa));
            bookdetails.put("Declined", String.valueOf(dc));
            bookdetails.put("Approved", String.valueOf(a));
            bookdetails.put("Confirmed", String.valueOf(cf));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return bookdetails;
    }

    // booking routing info
    @Override
    public Map<String, List> geteBookRoutingInfo(String origin, String delivery, String gateinDate, String preCarriage,
            String onCarriage, String strDateFormat, String porttype, String pol, String pod, String fromdate,
            String todate, String origintype, String deliverytype, List<String> servList, List<String> feederlis,
            String def_route_plug, String search_type, Map ecomplugin) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList alroute = new ArrayList();
        System.out.println("[ECOM] EcomBooingBean gateinDate: " + gateinDate + ", fromdate: " + fromdate + ", todate: "
                + todate + ", origin: " + origin
                + ", delivery: " + delivery + ", pol: " + pol + ", pod: " + pod);
        List routeList = new ArrayList();
        List addlst = new ArrayList();
        Route routeDTO = null;
        String targetdate = "60", Status = "N", service = null;
        RoutingVo hs = null;
        boolean check = false;
        boolean checkNearestSeaPort = true;
        boolean isOriPOLDiff = false;
        boolean isDelPODDiff = false;
        Map pluginconfig = null;
        int transshipmentCou = 0;
        //
        List<String> oriPortList = null;
        List<String> delPortList = null;
        RouteAvailVo routeAvlObj = null;
        List<RouteAvailVo> lstAltRoute = null;
        Map<String, List> returnmap = new HashMap();
        String enablepolpod = null, isLCTEnable = null, Schdsorttype = null, removeSameVessel = null,
                originhalmode = null, deliveryhalmode = null;
        int origintopolarrhaulagehour = 5;
        int polarrtopoldephaulagehour = 5;
        String routing_transtime = "N";
        HashSet<String> legports = new HashSet<String>();
        //
        try {
            enablepolpod = (String) ecomplugin.get("Enable_Routing_Pol_Pod_Search");
            isLCTEnable = (String) ecomplugin.get("Display_LCT_CUTOFF");
            Schdsorttype = (String) ecomplugin.get(GeneralConf.Sort_Schedule_Based_On_Depdate);
            removeSameVessel = (String) ecomplugin.get(GeneralConf.REMOVE_SAME_VESSEL_DETAILS);
            if (ecomplugin.get(GeneralConf.Schedule_Ori_Dep_POL_Arr_Haulage_Hours) != null && ecomplugin
                    .get(GeneralConf.Schedule_Ori_Dep_POL_Arr_Haulage_Hours).toString().trim().length() > 0) {
                origintopolarrhaulagehour = Integer.parseInt(
                        checkNull((String) ecomplugin.get(GeneralConf.Schedule_Ori_Dep_POL_Arr_Haulage_Hours)));
            }
            if (ecomplugin.get(GeneralConf.Schedule_POL_Arr_POL_Dep_Haulage_Hours) != null && ecomplugin
                    .get(GeneralConf.Schedule_POL_Arr_POL_Dep_Haulage_Hours).toString().trim().length() > 0) {
                polarrtopoldephaulagehour = Integer.parseInt(
                        checkNull((String) ecomplugin.get(GeneralConf.Schedule_POL_Arr_POL_Dep_Haulage_Hours)));
            }
            if (checkNull((String) ecomplugin.get(GeneralConf.Schedule_POL_Arr_POL_Dep_Haulage_Hours)).length() > 0) {
                routing_transtime = (String) ecomplugin.get(GeneralConf.Refer_Routing_Transtime);
            }
            pluginconfig = ecomvdsservice.getEcomBookingPluginConfig("BRI");
            List lis = (List) pluginconfig.get("pluginconfiguration");
            if (lis != null && lis.size() > 0) {
                Map configval = (Map) lis.get(0);
                if (configval.containsValue("BRI")) {
                    checkNearestSeaPort = false;
                }
            }

            hs = (RoutingVo) ("Y".equalsIgnoreCase(checkNull(def_route_plug))
                    ? ecomvdsservice.getecomroutinginformation(origin, delivery, gateinDate, gateinDate, def_route_plug)
                    : ecomvdsservice.getecomroutinginformation(origin, delivery, gateinDate, gateinDate));

            List lst = hs.getRoutedetail();
            if (lst.size() > 0) {
                Status = "Y";
                addlst.add(lst);
            }
            if ("Y".equalsIgnoreCase(porttype) && checkNearestSeaPort && (!"Y".equalsIgnoreCase(enablepolpod))) {
                if ("N".equalsIgnoreCase(Status)) {
                    hs = (RoutingVo) ("Y".equalsIgnoreCase(checkNull(def_route_plug))
                            ? ecomvdsservice.getecomroutinginformation(pol, delivery, gateinDate, gateinDate,
                                    def_route_plug)
                            : ecomvdsservice.getecomroutinginformation(pol, delivery, gateinDate, gateinDate));
                    lst = hs.getRoutedetail();
                    if (lst.size() > 0) {
                        Status = "Y";
                        addlst.add(lst);
                        isOriPOLDiff = true;
                    }
                }
                if ("N".equalsIgnoreCase(Status)) {
                    hs = (RoutingVo) ("Y".equalsIgnoreCase(checkNull(def_route_plug))
                            ? ecomvdsservice.getecomroutinginformation(origin, pod, gateinDate, gateinDate,
                                    def_route_plug)
                            : ecomvdsservice.getecomroutinginformation(origin, pod, gateinDate, gateinDate));
                    lst = hs.getRoutedetail();
                    if (lst.size() > 0) {
                        Status = "Y";
                        addlst.add(lst);
                        isDelPODDiff = true;
                    }
                }

            }
            if ("N".equalsIgnoreCase(Status) && "Y".equalsIgnoreCase(porttype)
                    && (checkNearestSeaPort || "Y".equalsIgnoreCase(enablepolpod))) {
                hs = (RoutingVo) ("Y".equalsIgnoreCase(checkNull(def_route_plug))
                        ? ecomvdsservice.getecomroutinginformation(pol, pod, gateinDate, gateinDate, def_route_plug)
                        : ecomvdsservice.getecomroutinginformation(pol, pod, gateinDate, gateinDate));
                lst = hs.getRoutedetail();
                if (lst.size() > 0) {
                    Status = "Y";
                    addlst.add(lst);
                    isDelPODDiff = true;
                    isOriPOLDiff = true;
                }
            }
            originhalmode = getbkgporthlgmode(origin);
            deliveryhalmode = getbkgporthlgmode(delivery);
            logPrinter(">>servList>>" + servList);
            if (addlst.size() > 0) {
                for (int i = 0; i < addlst.size(); i++) {
                    List alst = (List) addlst.get(i);
                    for (int j = 0; j < alst.size(); j++) {
                        transshipmentCou = 0;
                        check = false;
                        Routedetail rst = (Routedetail) alst.get(j);
                        service = checkNull(rst.getMainsvc()).trim().toUpperCase();
                        routeDTO = new Route();
                        routeDTO.setBlpol(rst.getBlpol());
                        routeDTO.setBlpolTer(rst.getBlpolter());
                        routeDTO.setBlpolMode(rst.getMode());
                        routeDTO.setBlpolSVC(rst.getBlpolsvc());
                        routeDTO.setTrans1(rst.getOpspol1());
                        routeDTO.setTrans1Ter(rst.getOpspol1ter());
                        routeDTO.setTrans1Mode(rst.getOpsmode1());
                        routeDTO.setTrans1SVC(rst.getOpspolsvc1());
                        routeDTO.setTrans2(rst.getOpspol2());
                        routeDTO.setTrans2Mode(rst.getOpsmode2());
                        routeDTO.setTrans2SVC(rst.getOpspolsvc2());
                        routeDTO.setTrans2Ter(rst.getOpspol2ter());
                        routeDTO.setTrans3(rst.getOpspol3());
                        routeDTO.setTrans3Mode(rst.getOpsmode3());
                        routeDTO.setTrans3SVC(rst.getOpspolsvc3());
                        routeDTO.setTrans3Ter(rst.getOpspol3ter());
                        routeDTO.setBlpod(rst.getBlpod());
                        routeDTO.setBlpodTer(rst.getBlpodter());
                        routeDTO.setDefRoute(rst.getDefroute());
                        routeDTO.setMainSVC(rst.getMainsvc());
                        routeDTO.setHauMode(rst.getOrgmode());
                        routeDTO.setDisHauMode(rst.getDelmode());
                        if (isOriPOLDiff) {
                            if (checkNull(originhalmode).length() > 0) {
                                routeDTO.setHauMode(originhalmode);
                            } else {
                                routeDTO.setHauMode("T");
                            }
                        }
                        if (isDelPODDiff) {
                            if (checkNull(deliveryhalmode).length() > 0) {
                                routeDTO.setDisHauMode(deliveryhalmode);
                            } else {
                                routeDTO.setDisHauMode("T");
                            }
                        }
                        routeDTO.setRouteId(String.valueOf(rst.getId()));
                        //
                        routeDTO.setGateInDate(checkNull(gateinDate));
                        routeDTO.setOriginVen("");
                        routeDTO.setRoute(String.valueOf(rst.getId()));
                        routeDTO.setBlopodMode("");
                        routeDTO.setBlpodSVC("");
                        routeDTO.setOrginTer(rst.getBlpolter());//
                        routeDTO.setOrigin(checkNull(origin));//
                        routeDTO.setDelivery(checkNull(delivery));//
                        routeDTO.setDeliveryTer(rst.getBlpodter());//
                        routeDTO.setPreCarriage(preCarriage);
                        routeDTO.setOnCarriage(onCarriage);
                        routeDTO.setTargetdate(checkNull(targetdate));
                        routeDTO.setService(rst.getMainsvc());
                        routeDTO.setOpstrans1mode(checkNull(rst.getOpstrans1mde()));
                        routeDTO.setOpstrans2mode(checkNull(rst.getOpstrans2mde()));
                        //
                        routeDTO.setPoltranstime(checkNull(rst.getPoltranstime()));
                        routeDTO.setPodtranstime(checkNull(rst.getPodtranstime()));
                        //
                        if (checkNull(routeDTO.getTrans1()).length() > 0) {
                            transshipmentCou = transshipmentCou + 1;
                        }
                        if (checkNull(routeDTO.getTrans2()).length() > 0) {
                            transshipmentCou = transshipmentCou + 1;
                        }
                        if (checkNull(routeDTO.getTrans3()).length() > 0) {
                            transshipmentCou = transshipmentCou + 1;
                        }
                        routeDTO.setTranshipment(transshipmentCou);
                        //
                        if (servList != null && servList.contains(service)) {
                            check = true;
                        }
                        if (feederlis != null && feederlis.size() > 0) {
                            if (feederlis.contains(checkNull(rst.getBlpolsvc()))
                                    || feederlis.contains(checkNull(rst.getOpspolsvc1()))
                                    || feederlis.contains(checkNull(rst.getMainsvc()))
                                    || feederlis.contains(checkNull(rst.getOpspolsvc2()))
                                    || feederlis.contains(checkNull(rst.getOpspolsvc3()))) {
                                check = true;
                            }
                        }
                        if (!check) {
                            alroute.add(routeDTO);
                        }
                        if (!check) {
                            if (routeDTO.getTrans1() != null && routeDTO.getTrans1().length() > 0) {
                                legports.add(routeDTO.getBlpol() + "~~" + routeDTO.getTrans1());
                                if (routeDTO.getTrans2() != null && routeDTO.getTrans2().length() > 0) {
                                    legports.add(routeDTO.getTrans1() + "~~" + routeDTO.getTrans2());
                                    if (routeDTO.getTrans3() != null && routeDTO.getTrans3().length() > 0) {
                                        legports.add(routeDTO.getTrans2() + "~~" + routeDTO.getTrans3());
                                        legports.add(routeDTO.getTrans3() + "~~" + routeDTO.getBlpod());
                                    } else {
                                        legports.add(routeDTO.getTrans2() + "~~" + routeDTO.getBlpod());
                                    }
                                } else {
                                    legports.add(routeDTO.getTrans1() + "~~" + routeDTO.getBlpod());
                                }
                            } else {
                                legports.add(routeDTO.getBlpol() + "~~" + routeDTO.getBlpod());
                            }
                        }
                    }
                }
            }
            System.out.println("[ECOM] EcomBooingBean  ==>alroute" + alroute.size());
            routeList = getSchdule_Trust(alroute, fromdate, todate, origintype, deliverytype, origin, delivery,
                    isLCTEnable, origintopolarrhaulagehour, polarrtopoldephaulagehour, Schdsorttype, legports,
                    routing_transtime, removeSameVessel, ecomplugin);
            //
            returnmap.put("actualschedule", routeList);
            if (routeList.size() == 0 && "schedule".equalsIgnoreCase(search_type)) {
                oriPortList = getPortListByAgency(origin);
                delPortList = getPortListByAgency(delivery);
                lstAltRoute = new ArrayList<RouteAvailVo>();
                //
                System.out.println("[ECOM] EcomBooingBean Check for Agency port pair");
                for (String oriPort : oriPortList) {
                    for (String delPort : delPortList) {
                        logPrinter(" origin : " + oriPort + " delivery : " + delPort);
                        if (!(oriPort.equalsIgnoreCase(origin) && delPort.equalsIgnoreCase(delivery))) {
                            routeList = scheduleBeanObj.geteBookRoutingInfo(oriPort, delPort, fromdate, todate,
                                    def_route_plug, servList, feederlis);
                            if (routeList.size() > 0) {
                                logPrinter(" Result Agency Avail");
                                routeAvlObj = new RouteAvailVo();
                                routeAvlObj.setOrigin(oriPort);
                                routeAvlObj.setDelivery(delPort);
                                lstAltRoute.add(routeAvlObj);
                            }
                        }
                    }
                }
                //
                if (lstAltRoute.size() == 0) {
                    // check for country
                    System.out.println("[ECOM] EcomBooingBean Check for country port pair");
                    oriPortList = getPortListByCountry(origin);
                    delPortList = getPortListByCountry(delivery);
                    for (String oriPort : oriPortList) {
                        for (String delPort : delPortList) {
                            if (!(oriPort.equalsIgnoreCase(origin) && delPort.equalsIgnoreCase(delivery))) {
                                routeList = scheduleBeanObj.geteBookRoutingInfo(oriPort, delPort, fromdate, todate,
                                        def_route_plug, servList, feederlis);
                                if (routeList.size() > 0) {
                                    routeAvlObj = new RouteAvailVo();
                                    routeAvlObj.setOrigin(oriPort);
                                    routeAvlObj.setDelivery(delPort);
                                    lstAltRoute.add(routeAvlObj);
                                }
                            }
                        }
                    }
                }
            }
            returnmap.put("altchedule", lstAltRoute);
            //
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("[ECOM] EcomBooingBean returnList " + returnmap);
        return returnmap;
    }

    private String getbkgporthlgmode(String portcode) {
        String hlgmode = "";
        try {
            Map vmap = ecomvdsservice.getecomporthlgmode(portcode);
            List<Map> vlist = (List) vmap.get("ports");
            if (vlist.size() > 0) {
                Map rs = vlist.get(0);
                hlgmode = (rs.get("hlgmode") != null) ? rs.get("hlgmode").toString() : "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hlgmode;
    }

    private List getSchdule_Trust(ArrayList alroute, String fromdate, String todate, String origintype,
            String deliverytype, String origin, String delivery, String isLCTEnable, int origintopolarrhaulagehour,
            int polarrtopoldephaulagehour, String Schdsorttype, HashSet<String> legports, String routing_transtime,
            String removeSameVessel, Map ecomplugin) {

        // ESLGEN-8237: clear per-request so cache only dedupes lookups within this call
        HAULAGE_TRANSIT_HOURS_CACHE.clear();

        Connection con = null;
        PreparedStatement ps = null, ps1 = null;
        ResultSet rs = null, rs1 = null;
        String etd_calc = null, eta_calc = null;
        Route routeDTO = null, routeDTO1 = null;
        int count = 0;
        Route frou = null, route1 = null;
        List finallist = new ArrayList();
        List transList = null;
        boolean isRouting = false, isDichargeBound = false;
        String polTerminal = null, podTerminal = null;
        Map defbound = null, pluginconfig = null;
        BoundConfigVo configbound = null;
        String strBoundThereLoad = null;
        String configBLG = "N", configBFL = "N";
        String isBkgLegFormationReq = "N";
        String strMainRestriction = null;
        String strFeederRestriction = null;
        Map legtranshs = null;
        int transtime = 0;
        String istranstime = "N";
        //
        String isMainSVCReq = null;
        String isMainVoyReq = null;
        String isMainBoundReq = null;
        //
        String isFeederSvcReq = null;
        String isFeederVoyReq = null;
        String isFeederBoundReq = null;
        //
        String strPOLTerminalOnly = "N";
        String strPODTerminalOnly = "N";
        String strTransTerminalOnly = "N";
        //
        String isSvcReq = null;
        String isVoyReq = null;
        String isBoundReq = null;
        String strSerType = null;
        boolean pluginBTD = false;
        boolean pluginBLN = false;
        int poltranstime = 0, podtranstime = 0;
        //
        List<BoundConfigVo> bcList = null;
        try {
            logPrinter("[getSchdule_Trust START] fromdate:" + fromdate + ", todate:" + todate + ", origin:" + origin + ", delivery:" + delivery + ", origintype:" + origintype + ", deliverytype:" + deliverytype + ", isLCTEnable:" + isLCTEnable + ", Schdsorttype:" + Schdsorttype + ", origintopolarrhaulagehour:" + origintopolarrhaulagehour + ", polarrtopoldephaulagehour:" + polarrtopoldephaulagehour + ", routing_transtime:" + routing_transtime + ", removeSameVessel:" + removeSameVessel + ", TotalRoutes:" + (alroute != null ? alroute.size() : 0) + ", legports:" + legports);

            pluginconfig = ecomvdsservice.getEcomBookingPluginConfig("BLF,BTD,BLN");
            List lis = (List) pluginconfig.get("pluginconfiguration");
            if (lis != null && lis.size() > 0) {
                for (int i = 0; i < lis.size(); i++) {
                    Map configval = (Map) lis.get(i);
                    if (configval.containsValue("BLF")) {
                        isBkgLegFormationReq = "Y";
                    } else if (configval.containsValue("BTD")) {
                        pluginBTD = true;
                    } else if (configval.containsValue("BLN")) {
                        pluginBLN = true;
                    }
                }
            }
            logPrinter("[PLUGIN CONFIG] BLF:" + isBkgLegFormationReq + ", BTD:" + pluginBTD + ", BLN:" + pluginBLN);

            if (pluginBTD) {
                legtranshs = getBKGLegTransitDays(legports);
                logPrinter("[TRANSIT DAYS] legports:" + legports + ", mapSize:" + (legtranshs != null ? legtranshs.size() : 0));
            }

            con = new lrp.serverutils.refs.ServerObject("VSSDB").getConnection();
            
            if ("Y".equalsIgnoreCase(isBkgLegFormationReq)) {
                ps = con.prepareStatement(
                        "select attribute,value from bkgconfig (NOLOCK) where attribute in ('BLF','BLM','BPT','BDT','BTT')"
                        + " and status='A'",
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = ps.executeQuery();
                String strColumnName = null;
                while (rs.next()) {
                    strColumnName = rs.getString("attribute").trim();
                    if ("BLF".equalsIgnoreCase(strColumnName)) {
                        strFeederRestriction = rs.getString("value").trim();
                    } else if ("BLM".equalsIgnoreCase(strColumnName)) {
                        strMainRestriction = rs.getString("value").trim();
                    } else if ("BPT".equalsIgnoreCase(strColumnName)) {
                        strPOLTerminalOnly = checkNull(rs.getString("value"));
                    } else if ("BDT".equalsIgnoreCase(strColumnName)) {
                        strPODTerminalOnly = checkNull(rs.getString("value"));
                    } else if ("BTT".equalsIgnoreCase(strColumnName)) {
                        strTransTerminalOnly = checkNull(rs.getString("value"));
                    }
                }
                logPrinter("[BKG CONFIG] BLF(Feeder):" + strFeederRestriction + ", BLM(Main):" + strMainRestriction + ", BPT(POLTer):" + strPOLTerminalOnly + ", BDT(PODTer):" + strPODTerminalOnly + ", BTT(TransTer):" + strTransTerminalOnly);
            }
            
            strFeederRestriction = strFeederRestriction == null ? "" : strFeederRestriction;
            strMainRestriction = strMainRestriction == null ? "" : strMainRestriction;
            // SVC-VOY-BND
            isMainSVCReq = strMainRestriction.contains("SVC") ? "N" : "Y";
            isMainVoyReq = strMainRestriction.contains("VOY") ? "N" : "Y";
            isMainBoundReq = strMainRestriction.contains("BND") ? "N" : "Y";
            isFeederSvcReq = strFeederRestriction.contains("SVC") ? "N" : "Y";
            isFeederVoyReq = strFeederRestriction.contains("VOY") ? "N" : "Y";
            isFeederBoundReq = strFeederRestriction.contains("BND") ? "N" : "Y";
            
            logPrinter("[RESTRICTION] MainLine(SVC,VOY,BND):" + isMainSVCReq + "," + isMainVoyReq + "," + isMainBoundReq + " | Feeder(SVC,VOY,BND):" + isFeederSvcReq + "," + isFeederVoyReq + "," + isFeederBoundReq + " | Terminal(POL,POD,Trans):" + strPOLTerminalOnly + "," + strPODTerminalOnly + "," + strTransTerminalOnly);
            
            for (int j = 0; j < alroute.size(); j++) {
                routeDTO = (Route) alroute.get(j);
                String blpol = routeDTO.getBlpol();
                String blpolSVC = routeDTO.getBlpolSVC();
                String trans1 = routeDTO.getTrans1();
                String trans1SVC = routeDTO.getTrans1SVC();
                String trans2 = routeDTO.getTrans2();
                String trans2SVC = routeDTO.getTrans2SVC();
                String trans3 = routeDTO.getTrans3();
                String trans3SVC = routeDTO.getTrans3SVC();
                String blpod = routeDTO.getBlpod();
                String routeid = routeDTO.getRouteId();
                String trans1Ter = routeDTO.getTrans1Ter();
                String trans2Ter = routeDTO.getTrans2Ter();
                String trans3Ter = routeDTO.getTrans3Ter();
                String blpolter = routeDTO.getBlpolTer();
                String blpodter = routeDTO.getBlpodTer();
                String defaultroute = routeDTO.getDefRoute();
                String opstrans1Mode = routeDTO.getOpstrans1mode();
                String opstrans2Mode = routeDTO.getOpstrans2mode();
                //
                System.out.println("[ECOM] EcomBooingBean Routing Info - Index: " + j + ", Plugin: " + pluginBLN
                        + " | RouteId: " + routeid + ", DefRoute: " + defaultroute
                        + " | BLPOL: " + blpol + " (SVC: " + blpolSVC + ", Ter: " + blpolter + ")"
                        + " | Trans1: " + trans1 + " (SVC: " + trans1SVC + ", Ter: " + trans1Ter + ", Mode: " + opstrans1Mode + ")"
                        + " | Trans2: " + trans2 + " (SVC: " + trans2SVC + ", Ter: " + trans2Ter + ", Mode: " + opstrans2Mode + ")"
                        + " | Trans3: " + trans3 + " (SVC: " + trans3SVC + ", Ter: " + trans3Ter + ")"
                        + " | BLPOD: " + blpod + " (Ter: " + blpodter + ")");
                //
                poltranstime = origintopolarrhaulagehour;
                podtranstime = origintopolarrhaulagehour;
                
                if ("Y".equalsIgnoreCase(routing_transtime)) {
                    if (checkNull((routeDTO.getPoltranstime())).matches("\\d+")) {
                        poltranstime = Integer.parseInt(checkNull(routeDTO.getPoltranstime()));
                    }
                    if (checkNull((routeDTO.getPodtranstime())).matches("\\d+")) {
                        podtranstime = Integer.parseInt(checkNull(routeDTO.getPodtranstime()));
                    }
                    logPrinter("[TRANS TIME] routing_transtime:Y, POL:" + poltranstime + "hrs, POD:" + podtranstime + "hrs");
                }

                // ESLGEN-8237: Override haulage transit from Ecom_haulage_transitdays when available
                // Priority: port pair + TransportMode, then port pair without mode, else existing logic
                if (origin != null && !origin.equalsIgnoreCase(blpol)) {
                    String originHauMode = checkNull(routeDTO.getHauMode()).length() > 0 ? routeDTO.getHauMode() : "T";
                    int originHaulageHours = getHaulageTransitHours(origin, blpol, originHauMode);
                    if (originHaulageHours >= 0) {
                        poltranstime = originHaulageHours;
                        logPrinter("[TRANS TIME] ESLGEN-8237 override POL haulage:" + origin + "=>" + blpol
                                + ", Mode:" + originHauMode + ", Hours:" + poltranstime);
                    }
                }
                if (delivery != null && !delivery.equalsIgnoreCase(blpod)) {
                    String deliveryHauMode = checkNull(routeDTO.getDisHauMode()).length() > 0 ? routeDTO.getDisHauMode() : "T";
                    int deliveryHaulageHours = getHaulageTransitHours(blpod, delivery, deliveryHauMode);
                    if (deliveryHaulageHours >= 0) {
                        podtranstime = deliveryHaulageHours;
                        logPrinter("[TRANS TIME] ESLGEN-8237 override POD haulage:" + blpod + "=>" + delivery
                                + ", Mode:" + deliveryHauMode + ", Hours:" + podtranstime);
                    }
                }
                
                defbound = getDefaultBound(blpol, blpolSVC, trans1, trans1SVC, trans2, trans2SVC, trans3, trans3SVC, blpod, pluginBLN);
                Set<String> keys = defbound.keySet();
                count = 0;
                for (String k : keys) {
                    if (count == 0) {
                        bcList = (List<BoundConfigVo>) defbound.get(k);
                    }
                    count++;
                }
                AutoLegVo autoLegVo = determineStrBoundThereLoad(bcList);
                strBoundThereLoad = autoLegVo.getStrBoundThereLoad();
                boolean hasVesselVoyageInfo = autoLegVo.isHasVesselVoyageInfo();
                configbound = autoLegVo.getBoundConfig();
                logPrinter("[BOUND CONFIG] defboundKeys:" + defbound.keySet() + ", strBoundThereLoad:" + strBoundThereLoad + ", hasVesselVoyageInfo:" + hasVesselVoyageInfo + ", Lbound:" + (configbound != null ? configbound.getLbound() : "null") + ", Dbound:" + (configbound != null ? configbound.getDbound() : "null"));
                
                String date = fromdate + "," + todate;
                ScheduleDataModel hs = null;
                System.out.println("[ECOM] EcomBooingBean strBoundThereLoad=> " + strBoundThereLoad + ","
                        + routeDTO.getBlpol() + "," + blpolSVC + "," + date + "," + blpolter + ","
                        + configbound.getLbound() + "," + configbound.getDbound());
                if ("Y".equalsIgnoreCase(isLCTEnable)) {
                    if ("Y".equalsIgnoreCase(strBoundThereLoad)) {
                        hs = (ScheduleDataModel) ecomvdsservice.getecomscheduleforroutingforlct(routeDTO.getBlpol(), blpolSVC, date, blpolter, configbound.getLbound());
                        logPrinter("[SCHEDULE FETCH] LCT:Y, BoundFilter:" + configbound.getLbound() + ", POL:" + routeDTO.getBlpol() + ", SVC:" + blpolSVC + ", Date:" + date + ", Ter:" + blpolter);
                    } else {
                        hs = (ScheduleDataModel) ecomvdsservice.getecomscheduleforroutingforlct(routeDTO.getBlpol(), blpolSVC, date, blpolter);
                        logPrinter("[SCHEDULE FETCH] LCT:Y, BoundFilter:N, POL:" + routeDTO.getBlpol() + ", SVC:" + blpolSVC + ", Date:" + date + ", Ter:" + blpolter);
                    }
                } else {
                    if ("Y".equalsIgnoreCase(strBoundThereLoad)) {
                        hs = (ScheduleDataModel) ecomvdsservice.getecomscheduleforrouting(routeDTO.getBlpol(), blpolSVC, date, blpolter, configbound.getLbound());
                        logPrinter("[SCHEDULE FETCH] LCT:N, BoundFilter:" + configbound.getLbound() + ", POL:" + routeDTO.getBlpol() + ", SVC:" + blpolSVC + ", Date:" + date + ", Ter:" + blpolter);
                    } else {
                        hs = (ScheduleDataModel) ecomvdsservice.getecomscheduleforrouting(routeDTO.getBlpol(), blpolSVC, date, blpolter);
                        logPrinter("[SCHEDULE FETCH] LCT:N, BoundFilter:N, POL:" + routeDTO.getBlpol() + ", SVC:" + blpolSVC + ", Date:" + date + ", Ter:" + blpolter);
                    }
                }
                logPrinter(" ===>>" + hs);
                //
                List lst = hs.getAllschedules();
                int beforeFilterCount = (lst != null ? lst.size() : 0);
                if (hasVesselVoyageInfo) {
                    logPrinter(" hasVesselVoyageInfo= lst > " + lst);
                    logPrinter(" hasVesselVoyageInfo= bcList > " + bcList);
                    lst = filterSchedulesByBoundConfig(lst, bcList);
                }
                logPrinter("[SCHEDULE RESULT] POLSchedules:" + beforeFilterCount + ", AfterFilter:" + (lst != null ? lst.size() : 0) + ", hasVesselVoyageInfo:" + hasVesselVoyageInfo);

                if (lst != null & lst.size() > 0) {
                    for (int m = 0; m < lst.size(); m++) {
                        Allschedules all = (Allschedules) lst.get(m);
                        transList = new ArrayList();
                        List tst = new ArrayList();
                        count = count + 1;
                        String setVessel = all.getVessel();
                        String setVoyage = all.getVoyage();
                        String setBound = all.getBound();
                        String disbound = all.getBound();
                        String setService = all.getService();
                        String setETD1 = all.getDepdate();
                        String setETA1 = all.getArrdate();
                        polTerminal = all.getTerminal();
                        String setGrpCallId = all.getGroupcallid();
                        String type = all.getType();
                        String lct = all.getClosingtime();
                        logPrinter("[SCHEDULE " + m + "/" + lst.size() + "] Vessel:" + setVessel + ", Voyage:" + setVoyage + ", Bound:" + setBound + ", Service:" + setService + ", Type:" + type + ", ETD:" + setETD1 + ", ETA:" + setETA1 + ", Terminal:" + polTerminal + ", LCT:" + lct);

                        String setTrans1 = checkNull(trans1);
                        String setTrans1SVC = checkNull(trans1SVC);
                        String setTrans2 = checkNull(trans2);
                        String setTrans2SVC = checkNull(trans2SVC);
                        String setTrans3 = checkNull(trans3);
                        String setTrans3SVC = checkNull(trans3SVC);
                        String setTrans1Ter = checkNull(trans1Ter);
                        String setTrans2Ter = checkNull(trans2Ter);
                        String setTrans3Ter = checkNull(trans3Ter);
                        String setpolter = checkNull(blpolter);
                        String setpodter = checkNull(blpodter);
                        String setBlpod = checkNull(blpod);
                        isDichargeBound = false;
                        if (hasVesselVoyageInfo) {
                            configbound = all.getBoundConfig();
                        }
                        if (configbound != null && (!"N/A".equalsIgnoreCase(configbound.getDbound()))) {
                            disbound = checkNull(configbound.getDbound());
                            isDichargeBound = true;
                        }
                        
                        logPrinter("[ROUTE TYPE] Trans1:'" + setTrans1 + "' => " + (setTrans1.length() > 0 ? "TRANSSHIPMENT" : "DIRECT") + ", isDichargeBound:" + isDichargeBound + ", disbound:" + disbound);
                        if (setTrans1.length() > 0) {
                            logPrinter("[TRANSSHIPMENT] " + blpol + "=>" + setTrans1 + "=>" + setTrans2 + "=>" + setTrans3 + "=>" + setBlpod + " | Vessel:" + setVessel + ", Voyage:" + setVoyage + ", Bound:" + setBound + ", Service:" + setService);
                            tst = GetAutoLeg(blpol, setService, setVessel, setVoyage, setBound, setETD1, setTrans1,
                                    setTrans1SVC, setTrans2, setTrans2SVC, setTrans3, setTrans3SVC, setBlpod,
                                    setGrpCallId, routeid, setpolter, setpodter, setTrans1Ter, setTrans2Ter,
                                    setTrans3Ter, defaultroute, setETA1, setpolter, defbound, configBLG, configBFL,
                                    opstrans1Mode, opstrans2Mode, strMainRestriction, strFeederRestriction, type,
                                    strTransTerminalOnly, strPODTerminalOnly, lct, disbound, isDichargeBound,
                                    legtranshs, configbound, ecomplugin, removeSameVessel);
                            logPrinter("[TRANSSHIPMENT RESULT] GetAutoLeg returned " + (tst != null ? tst.size() : 0) + " legs");
                        } else {
                            ArrayList al = new ArrayList();
                            frou = new Route();
                            frou.service = setService;
                            frou.vessel = setVessel;
                            frou.voyage = setVoyage;
                            frou.bound = setBound;
                            frou.blpolTer = polTerminal;
                            frou.loadport = blpol;
                            frou.legType = type;
                            frou.loadcallid = setGrpCallId;
                            frou.grpCallId = setGrpCallId;
                            frou.ETD1 = setETD1;
                            eta_calc = setETD1;
                            frou.disport = setBlpod;
                            frou.routeId = routeid;
                            frou.defRoute = defaultroute;
                            frou.cutoff_polterminal = setpolter;
                            frou.cutoff_polarrdate = setETA1;
                            frou.setClosingtime(lct);
                            // change to vds

                            transtime = getTranstTime(blpol, setBlpod, legtranshs);
                            istranstime = (transtime > 0) ? "Y" : "N";
                            
                            String str1 = "select top 1  a.arrdate,a.terminal,a.groupcallid,b.vendor_code,c.vendor_name,a.bound from allschedules  a(NOLOCK)"
                                    + " left join  feederscheduleVendors b(nolock)on a.service=b.Service and a.voyage=b.voyage and a.vessel=b.vessel and a.bound=b.bound "
                                    + " left join VendorDetails c(nolock) on c.vendor_code=b.vendor_code "
                                    + " where  (? = 'N' or a.service=?) and a.vessel=?  and (? = 'N' or a.bound=?) and a.port=? "
                                    + " and a.arrdate > ? and (? = 'N' or a.voyage=?) and (?='N' or a.terminal=?) and (?='N' or a.arrdate < DATEADD(DAY, ?,?)) order by a.arrdate asc";
                            ps1 = con.prepareStatement(str1, 1004, 1007);
                            
                            strSerType = type;
                            if ("F".equalsIgnoreCase(strSerType)) {
                                isSvcReq = isFeederSvcReq;
                                isVoyReq = isFeederVoyReq;
                                isBoundReq = isFeederBoundReq;
                            } else {
                                isSvcReq = isMainSVCReq;
                                isVoyReq = isMainVoyReq;
                                isBoundReq = isMainBoundReq;
                            }
                            /* if (configbound!=null && checkNull(configbound.getVoyage()).length() > 0) {
                                isVoyReq = "Y";
                            } */
                            // y
                            logPrinter(" isDichargeBound=> " + isDichargeBound);
                            isBoundReq = ((isDichargeBound) ? "Y" : isBoundReq);
                            
                            logPrinter("[DIRECT ROUTE] " + blpol + "=>" + setBlpod + " | TransitDays:" + transtime + ", ServiceType:" + strSerType + "(" + ("F".equalsIgnoreCase(strSerType) ? "FEEDER" : "MAINLINE") + ") | Restrictions(SVC,VOY,BND):" + isSvcReq + "," + isVoyReq + "," + isBoundReq + " | Vessel:" + setVessel + ", Voyage:" + setVoyage + ", Bound:" + setBound + ", DisBound:" + disbound + ", ETD:" + setETD1 + ", PODTer:" + blpodter);
                            ps1.setString(1, isSvcReq);
                            ps1.setString(2, setService);
                            ps1.setString(3, setVessel);
                            ps1.setString(4, isBoundReq);
                            ps1.setString(5, disbound);
                            ps1.setString(6, setBlpod);
                            ps1.setString(7, setETD1);
                            ps1.setString(8, isVoyReq);
                            ps1.setString(9, setVoyage);
                            ps1.setString(10, "Y");
                            ps1.setString(11, blpodter);
                            ps1.setString(12, istranstime);
                            ps1.setInt(13, transtime);
                            ps1.setString(14, setETD1);
                            rs1 = ps1.executeQuery();
                            rs1.last();
                            int rcnt = rs1.getRow();
                            rs1.beforeFirst();
                            logPrinter(" rcnt=> " + rcnt);
                            if (rcnt <= 0 && "N".equalsIgnoreCase(strPODTerminalOnly)) {
                                ps1 = con.prepareStatement(str1, 1004, 1007);
                                ps1.setString(1, isSvcReq);
                                ps1.setString(2, setService);
                                ps1.setString(3, setVessel);
                                ps1.setString(4, isBoundReq);
                                ps1.setString(5, disbound);
                                ps1.setString(6, setBlpod);
                                ps1.setString(7, setETD1);
                                ps1.setString(8, isVoyReq);
                                ps1.setString(9, setVoyage);
                                ps1.setString(10, strPODTerminalOnly);
                                ps1.setString(11, blpodter);
                                ps1.setString(12, istranstime);
                                ps1.setInt(13, transtime);
                                ps1.setString(14, setETD1);
                                rs1 = ps1.executeQuery();
                                rs1.last();
                                rcnt = rs1.getRow();
                                rs1.beforeFirst();
                                logPrinter(" rcnt 2=> " + rcnt);
                            }
                            
                            while (rs1.next()) {
                                frou.ETA1 = checkNull(rs1.getString("arrdate"));
                                etd_calc = checkNull(rs1.getString("arrdate"));
                                frou.dischargecallid = checkNull(rs1.getString("groupcallid"));
                                frou.blpodTer = checkNull(rs1.getString("terminal"));
                                logPrinter(" frou.blpodTer=>" + frou.blpodTer);
                                frou.dischargebound = checkNull(rs1.getString("bound"));
                                isRouting = true;
                            }
                              /*
                             * if ((!isRouting) && "N".equalsIgnoreCase(strPODTerminalOnly)) {
                             * frou = getAlternativeSchedule(frou, isSvcReq, setService, setVessel,
                             * isBoundReq, disbound, setBlpod, setETD1, isVoyReq, setVoyage, "N", blpodter,
                             * istranstime, transtime, "0");
                             * isRouting = frou.isIsRouting();
                             * }
                             */
                            logPrinter("[POD RESULT] isRouting:" + isRouting + ", ETA:" + frou.ETA1 + ", Terminal:" + frou.blpodTer + ", CallId:" + frou.dischargecallid + ", DisBound:" + frou.dischargebound);
                            
                            if (isRouting) {
                                isRouting = false;
                                if (origin != null && !origin.equalsIgnoreCase(blpol)) {
                                    route1 = new Route();
                                    route1.loadport = origin;
                                    route1.disport = blpol;
                                    route1.service = "";
                                    route1.vessel = "";
                                    route1.voyage = "";
                                    route1.legType = checkNull(routeDTO.getHauMode()).length()>0?routeDTO.getHauMode():"T";
                                    route1.routeId = routeid;
                                    route1.bound = "";
                                    route1.ETA1 = timeDiffForRouting(eta_calc, "origin", polarrtopoldephaulagehour);
                                    route1.ETD1 = timeDiffForRouting(route1.ETA1, "origin", poltranstime);
                                    route1.defRoute = defaultroute;
                                    al.add(route1);
                                    logPrinter("[ORIGIN HAULAGE] " + origin + "=>" + blpol + ", LegType:" + route1.legType + ", ETD:" + route1.ETD1 + ", ETA:" + route1.ETA1);
                                }
                                al.add(frou);
                                if (delivery != null && !delivery.equalsIgnoreCase(setBlpod)) {
                                    frou = new Route();
                                    frou.loadport = setBlpod;
                                    frou.disport = delivery;
                                    frou.service = "";
                                    frou.vessel = "";
                                    frou.voyage = "";
                                    frou.legType = checkNull(routeDTO.getDisHauMode()).length()>0?routeDTO.getDisHauMode():"T";
                                    frou.bound = "";
                                    frou.ETD1 = timeDiffForRouting(etd_calc, "delivery", polarrtopoldephaulagehour);
                                    frou.ETA1 = timeDiffForRouting(frou.ETD1, "delivery", podtranstime);
                                    frou.routeId = routeid;
                                    frou.defRoute = defaultroute;
                                    al.add(frou);
                                    logPrinter("[DELIVERY HAULAGE] " + setBlpod + "=>" + delivery + ", LegType:" + frou.legType + ", ETD:" + frou.ETD1 + ", ETA:" + frou.ETA1);
                                }
                                logPrinter("[DIRECT ROUTE COMPLETE] TotalLegs:" + al.size());
                                finallist.add(al);
                            } else {
                                logPrinter("[DIRECT ROUTE SKIPPED] No POD arrival found");
                            }
                        }
                        if (tst != null && tst.size() > 0) {
                            if (origin != null && !origin.equalsIgnoreCase(blpol)) {
                                routeDTO1 = (Route) tst.get(0);
                                etd_calc = routeDTO1.getETD1();
                                route1 = new Route();
                                route1.loadport = origin;
                                route1.disport = blpol;
                                route1.service = "";
                                route1.vessel = "";
                                route1.voyage = "";
                                route1.legType = checkNull(routeDTO.getHauMode()).length()>0?routeDTO.getHauMode():"T";
                                route1.routeId = routeDTO1.getRouteId();
                                route1.bound = "";
                                route1.ETA1 = timeDiffForRouting(etd_calc, "origin", polarrtopoldephaulagehour);
                                route1.ETD1 = timeDiffForRouting(route1.ETA1, "origin", poltranstime);
                                route1.defRoute = defaultroute;
                                transList.add(route1);
                                logPrinter("[TRANS ORIGIN HAULAGE] " + origin + "=>" + blpol + ", LegType:" + route1.legType + ", ETD:" + route1.ETD1 + ", ETA:" + route1.ETA1);
                            }
                            for (int i = 0; i < tst.size(); i++) {
                                Route legRoute = (Route) tst.get(i);
                                logPrinter("[TRANS SEA LEG " + i + "] " + legRoute.loadport + "=>" + legRoute.disport + ", Vessel:" + legRoute.vessel + ", Voyage:" + legRoute.voyage);
                                transList.add(tst.get(i));
                            }
                            
                            if (delivery != null && !delivery.equalsIgnoreCase(setBlpod)) {
                                frou = new Route();
                                routeDTO1 = (Route) tst.get(tst.size() - 1);
                                etd_calc = routeDTO1.getETA1();
                                frou.loadport = setBlpod;
                                frou.disport = delivery;
                                frou.service = "";
                                frou.vessel = "";
                                frou.voyage = "";
                                frou.legType = checkNull(routeDTO.getDisHauMode()).length()>0?routeDTO.getDisHauMode():"T";
                                frou.bound = "";
                                frou.ETD1 = timeDiffForRouting(etd_calc, "delivery", polarrtopoldephaulagehour);
                                frou.ETA1 = timeDiffForRouting(frou.ETD1, "delivery", podtranstime);
                                frou.routeId = routeDTO1.getRouteId();
                                frou.defRoute = defaultroute;
                                transList.add(frou);
                                logPrinter("[TRANS DELIVERY HAULAGE] " + setBlpod + "=>" + delivery + ", LegType:" + frou.legType + ", ETD:" + frou.ETD1 + ", ETA:" + frou.ETA1);
                            }
                            logPrinter("[TRANSSHIPMENT COMPLETE] TotalLegs:" + transList.size());
                            finallist.add(transList);
                        }
                    }
                }
            }
            
            int beforeDupRemoval = finallist.size();
            finallist = removeRoutingDuplicate(finallist);
            int afterDupRemoval = finallist.size();
            // TRANS_SAME_VESSEL_SKIP: Same-vessel routes are now avoided at leg-selection time inside
            // GetAutoLeg (skip-and-find-next-different-vessel), so this post-hoc whole-route removal
            // is no longer needed.
            // if ("Y".equalsIgnoreCase(removeSameVessel)) {
            //     finallist = removeSameVesselDetails(finallist);
            // }
            finallist = sortByDepDate(finallist, Schdsorttype);
            logPrinter("[POST PROCESS] BeforeDupRemoval:" + beforeDupRemoval + ", AfterDupRemoval:" + afterDupRemoval + ", removeSameVessel:" + removeSameVessel + ", Schdsorttype:" + Schdsorttype + ", FinalCount:" + finallist.size());
            logPrinter("[getSchdule_Trust END] FinalRouteCount:" + finallist.size());
        } catch (Exception e) {
            logPrinter("[ERROR] getSchdule_Trust Exception:" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (ps1 != null) {
                    ps1.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return finallist;
    }

    private LinkedHashMap getDefaultBound(String blpol, String blpolsvc, String opspol1, String opspolsvc1,
            String opspol2, String opspolsvc2, String opspol3, String opspolsvc3, String blpod, boolean pluginBLN) {

        LinkedHashMap lnkhsmap = null;
        BoundDefultVo defbouvo = null;
        List<BoundConfigVo> boundConfig = null;
        try {

            lnkhsmap = new LinkedHashMap();

            if ((blpol != null) && (blpol.trim().length() > 0)) {

                defbouvo = new BoundDefultVo();
                //
                defbouvo.setPol(blpol.trim());
                if ((opspol1 != null) && (opspol1.trim().length() > 0)) {
                    defbouvo.setPod(opspol1.trim());
                } else {
                    defbouvo.setPod(blpod.trim());
                }
                defbouvo.setSerive(blpolsvc.trim());

                boundConfig = getautolegconfig(defbouvo, pluginBLN);

                lnkhsmap.put(defbouvo.getPol() + "~~" + defbouvo.getPod() + "~~" + defbouvo.getSerive(), boundConfig);
                //
            }

            // To check First Transhipment
            if ((opspol1 != null) && (opspol1.trim().length() > 0)) {

                defbouvo = new BoundDefultVo();

                defbouvo.setPol(opspol1.trim());
                if ((opspol2 != null) && (opspol2.trim().length() > 0)) {
                    defbouvo.setPod(opspol2.trim());
                } else {
                    defbouvo.setPod(blpod.trim());
                }

                defbouvo.setSerive(opspolsvc1.trim());
                boundConfig = getautolegconfig(defbouvo, pluginBLN);
                lnkhsmap.put(defbouvo.getPol() + "~~" + defbouvo.getPod() + "~~" + defbouvo.getSerive(), boundConfig);
            }

            // To check second Transhipment
            if ((opspol2 != null) && (opspol2.trim().length() > 0)) {

                defbouvo = new BoundDefultVo();

                defbouvo.setPol(opspol2.trim());
                if ((opspol3 != null) && (opspol3.trim().length() > 0)) {
                    defbouvo.setPod(opspol3.trim());
                } else {
                    defbouvo.setPod(blpod.trim());
                }
                defbouvo.setSerive(opspolsvc2.trim());
                boundConfig = getautolegconfig(defbouvo, pluginBLN);
                lnkhsmap.put(defbouvo.getPol() + "~~" + defbouvo.getPod() + "~~" + defbouvo.getSerive(), boundConfig);
            }

            // To check Third Transhipment
            if ((opspol3 != null) && (opspol3.trim().length() > 0)) {
                defbouvo = new BoundDefultVo();
                defbouvo.setPol(opspol3.trim());
                defbouvo.setPod(blpod.trim());
                defbouvo.setSerive(opspolsvc3.trim());
                boundConfig = getautolegconfig(defbouvo, pluginBLN);
                lnkhsmap.put(defbouvo.getPol() + "~~" + defbouvo.getPod() + "~~" + defbouvo.getSerive(), boundConfig);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lnkhsmap;
    }

    private List<BoundConfigVo> getautolegconfig(BoundDefultVo defbouvo, boolean pluginBLN) {
        String lbound = null, dbound = null, vessel = null, voyage = null;
        BoundConfigVo bc = null;
        List<BoundConfigVo> bcList = new ArrayList<>();
        try {

            String pol = defbouvo.getPol();
            String pod = defbouvo.getPod();
            String service = defbouvo.getSerive();
            Map hs = (Map) ecomvdsservice.getecombkgportcombination(pol, pod, service);
            List lst = (List) hs.get("bkgportcombination");
            System.out.println("[ECOM] EcomBooingBean getautolegconfig lst=> " + lst);
            if (lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    bc = new BoundConfigVo();
                    Map rs = (Map) lst.get(i);
                    lbound = checkNull((String) rs.get("bound"));
                    dbound = checkNull((String) rs.get("dbound"));
                    vessel = checkNull((String) rs.get("vessel"));
                    voyage = checkNull((String) rs.get("voyage"));
                    if (lbound != null && lbound.length() > 0) {
                        bc.setLbound(lbound);
                    } else {
                        bc.setLbound("N/A");
                    }
                    if (dbound != null && dbound.length() > 0) {
                        bc.setDbound(dbound);
                    } else {
                        bc.setDbound("N/A");
                    }
                    bc.setVessel(vessel);
                    bc.setVoyage(voyage);
                    if (pluginBLN) {
                        bcList.add(bc);
                    } else {
                        if (checkNull(vessel).length() == 0 && checkNull(voyage).length() == 0) {
                            bcList.add(bc);
                        }
                    }
                }
            }
            System.out.println("[ECOM] EcomBooingBean getautolegconfig bcList=> " + bcList);

        } catch (Exception e) {
            System.out.println("[ECOM] EcomBooingBean Exception in getautolegconfig " + e);
            e.printStackTrace();
        }
        return bcList;
    }

    private List removeSameVesselDetails(List resultList) {

        ArrayList removeList = new ArrayList();
        Route route = null;
        List routeList = null;
        String strCurrVessel = null;
        String strPrevVessel = null;
        try {
            int listSize = resultList.size();
            for (int i = 0; i < listSize; i++) {
                strCurrVessel = null;
                strPrevVessel = null;
                routeList = (List) resultList.get(i);
                int routeSize = routeList.size();
                for (int j = 0; j < routeSize; j++) {
                    route = (Route) routeList.get(j);
                    strCurrVessel = route.getVessel();
                    if (strCurrVessel.equalsIgnoreCase(strPrevVessel)) {
                        removeList.add(i);
                        break;
                    }
                    strPrevVessel = strCurrVessel;
                }

            }
            for (int z = removeList.size() - 1; z >= 0; z--) {
                int remove = (Integer) removeList.get(z);
                resultList.remove(remove);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }


    private void addUsedVessel(Set<String> usedVessels, String vessel) {
        String vsl = (vessel == null) ? "" : vessel.trim();
        if (usedVessels != null && vsl.length() > 0) {
            usedVessels.add(vsl.toUpperCase());
        }
    }

    private boolean isVesselAlreadyUsed(Set<String> usedVessels, String vessel) {
        String vsl = (vessel == null) ? "" : vessel.trim();
        return usedVessels != null && vsl.length() > 0 && usedVessels.contains(vsl.toUpperCase());
    }

    private List removeRoutingDuplicate(List resultList) {

        ArrayList removeList = new ArrayList();
        Route route = null;
        Set duplicateSet = new HashSet();
        List routeList = new ArrayList();
        String strResult = null;
        try {
            int listSize = resultList.size();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < listSize; i++) {
                // strCurrVessel = null;
                // strPrevVessel = null;
                routeList = (List) resultList.get(i);
                int routeSize = routeList.size();
                for (int j = 0; j < routeSize; j++) {
                    route = (Route) routeList.get(j);
                    sb.append(checkNull(route.getLoadport()));
                    sb.append(checkNull(route.getDisport()));
                    sb.append(checkNull(route.getService()));
                    sb.append(checkNull(route.getVessel()));
                    sb.append(checkNull(route.getVoyage()));
                    sb.append(checkNull(route.getBound()));
                    sb.append(checkNull(route.getBlpolTer()));
                    sb.append(checkNull(route.getBlpodTer()));
                    sb.append(checkNull(route.getETA1()));
                    sb.append(checkNull(route.getETD1()));
                    sb.append(checkNull(route.getLegType()));
                    // strCurrVessel = route.getVessel();
                    // if (strCurrVessel.equalsIgnorecase(strPrevVessel){
                    // removeList.add(i);
                    // break;
                    // }
                    // strPrevVessel = strCurrVessel;
                }
                strResult = sb.toString().trim();
                //
                if (duplicateSet.contains(strResult)) {

                    removeList.add(i);
                } else {
                    duplicateSet.add(strResult);
                }
                sb.delete(0, sb.length());
            }
            for (int z = removeList.size() - 1; z >= 0; z--) {
                int remove = (Integer) removeList.get(z);
                resultList.remove(remove);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private String timeDiffForRouting(String convertDate, String port, int hours) {
        String finalDate = null;
        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date();
        try {
            SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentDate = source.parse(convertDate);
            cal.setTime(currentDate);
            if ("origin".equalsIgnoreCase(port)) {
                cal.add(Calendar.HOUR, -hours);
            } else if ("delivery".equalsIgnoreCase(port)) {
                cal.add(Calendar.HOUR, hours);
            }
            Date oneHourBack = cal.getTime();
            finalDate = source.format(oneHourBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalDate;
    }

    /**
     * ESLGEN-8237: Lookup haulage transit hours from Ecom_haulage_transitdays.
     * Priority: port pair + TransportMode, then port pair without TransportMode.
     * Returns hours (Days * 24), or -1 if not found.
     */
    private int getHaulageTransitHours(String originPort, String deliveryPort, String transportMode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int hours = -1;
        try {
            if (checkNull(originPort).length() == 0 || checkNull(deliveryPort).length() == 0) {
                return -1;
            }
            String mode = checkNull(transportMode);
            String cacheKey = haulageTransitHoursCacheKey(originPort, deliveryPort, mode);
            Integer cached = HAULAGE_TRANSIT_HOURS_CACHE.get(cacheKey);
            if (cached != null) {
                logPrinter("[HAULAGE TRANSIT LOOKUP CACHE HIT] Origin:" + originPort + ", Delivery:" + deliveryPort
                        + ", Mode:" + mode + ", Hours:" + cached);
                return cached.intValue();
            }
            con = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();
            String sql = "SELECT TOP 1 Days FROM Ecom_haulage_transitdays (NOLOCK) "
                    + "WHERE Origin = ? AND Delivery = ? AND Status = 'A' "
                    + "AND ( "
                    + "  (TransportMode = ?) "
                    + "  OR (TransportMode IS NULL OR LTRIM(RTRIM(TransportMode)) = '') "
                    + ") "
                    + "ORDER BY CASE WHEN TransportMode = ? AND TransportMode IS NOT NULL "
                    + "AND LTRIM(RTRIM(TransportMode)) <> '' THEN 0 ELSE 1 END";
            ps = con.prepareStatement(sql);
            ps.setString(1, originPort);
            ps.setString(2, deliveryPort);
            ps.setString(3, mode);
            ps.setString(4, mode);
            rs = ps.executeQuery();
            if (rs.next()) {
                int days = rs.getInt("Days");
                if (!rs.wasNull() && days >= 0) {
                    hours = days * 24;
                }
            }
            HAULAGE_TRANSIT_HOURS_CACHE.putIfAbsent(cacheKey, Integer.valueOf(hours));
            logPrinter("[HAULAGE TRANSIT LOOKUP] Origin:" + originPort + ", Delivery:" + deliveryPort
                    + ", Mode:" + mode + ", Hours:" + hours);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return hours;
    }

    private List sortByDepDate(List arr, String Schdsorttype) {
        List arrRes = null;
        List arrResNonDefault = null;
        List sortedList = null;
        int size = 0;
        String depDate = null;
        BetPortsSortVo berPortVo = null;
        List innerList = null;
        Route frou = null;
        try {
            size = arr.size();
            arrRes = new ArrayList();
            arrResNonDefault = new ArrayList();
            for (int i = 0; i < size; i++) {
                berPortVo = new BetPortsSortVo();
                innerList = (List) arr.get(i);
                frou = (Route) innerList.get(0);
                depDate = frou.getETD1();
                berPortVo.setDepDate(depDate);
                berPortVo.setLstRow(innerList);
                if ("Y".equalsIgnoreCase(frou.getDefRoute()) && (!"Y".equalsIgnoreCase(Schdsorttype))) {
                    arrRes.add(berPortVo);
                } else {
                    arrResNonDefault.add(berPortVo);
                }
            }
            Collections.sort(arrRes, new BetPortsSortVo());
            Collections.sort(arrResNonDefault, new BetPortsSortVo());
            //
            sortedList = new ArrayList();
            size = arrRes.size();
            for (int i = 0; i < size; i++) {
                berPortVo = (BetPortsSortVo) arrRes.get(i);
                sortedList.add(berPortVo.getLstRow());
            }
            size = arrResNonDefault.size();
            for (int i = 0; i < size; i++) {
                berPortVo = (BetPortsSortVo) arrResNonDefault.get(i);
                sortedList.add(berPortVo.getLstRow());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sortedList;
    }

    public List GetAutoLeg(String blpol, String setService, String setVessel, String setVoyage, String setBound,
            String setETD1, String setTrans1, String setTrans1SVC, String setTrans2, String setTrans2SVC,
            String setTrans3, String setTrans3SVC, String setBlpod, String setGrpCallId, String routeid,
            String setpolter, String setpodter, String setTrans1Ter, String setTrans2Ter, String setTrans3Ter,
            String defaultroute, String ETA, String BLpolter, Map defbound, String configBLG, String configBFL,
            String opstrans1Mode, String opstrans2Mode, String strMainRestriction, String strFeederRestriction,
            String strLegType, String strTransTerminalOnly, String strPODTerminalOnly, String lct, String disbound,
            boolean isDichargeBound, Map legtranshs, BoundConfigVo firstLegConfigbound, Map ecomplugin,
            String removeSameVessel) {
        String Sql = "";
        Hashtable hts = new Hashtable();
        ArrayList lstarry = new ArrayList();
        logPrinter("[GetAutoLeg START] BLPOL:" + blpol + ", BLPOD:" + setBlpod + ", Trans1:" + setTrans1 + "(SVC:" + setTrans1SVC + ",Ter:" + setTrans1Ter + "), Trans2:" + setTrans2 + "(SVC:" + setTrans2SVC + ",Ter:" + setTrans2Ter + "), Trans3:" + setTrans3 + "(SVC:" + setTrans3SVC + ",Ter:" + setTrans3Ter + ") | Vessel:" + setVessel + ", Voyage:" + setVoyage + ", Bound:" + setBound + ", Service:" + setService + ", ETD:" + setETD1 + ", LegType:" + strLegType + ", DisBound:" + disbound + ", isDichargeBound:" + isDichargeBound);
        ArrayList al = null;
        String rcdBnd = "";
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Route frou = null;
        String resbound = null;
        BoundConfigVo boundres = null;
        List<BoundConfigVo> bcList = null;
        String isMainSVCReq = null;
        String isMainVoyReq = null;
        String isMainBoundReq = null;
        //
        String isFeederSvcReq = null;
        String isFeederVoyReq = null;
        String isFeederBoundReq = null;
        //
        String isSvcReq = null;
        String isVoyReq = null;
        String isBoundReq = null;
        String strSerType = null;
        //
        List<TransScheduleLegVo> translis = null;
        TransScheduleLegVo translegvo = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("VSSDB").getConnection();
            String service = "", vessel = "", voyage = "", bound = "", lport = "", ETD = "", POLM = "", CALLID = "";
            String TS1 = "", TS1S = "", TS2 = "", TS2S = "", TS3 = "", TS3S = "", POD = "", POL = "";
            String TempDate = "", Port = "", polter = "", podter = "", TS1T = "", TS2T = "", TS3T = "",
                    tempDepDate = "", bft = "N", blt = "N", legformdate = "";
            int flag1 = 0, flag2 = 0, flag3 = 0, flag4 = 0, opsT1mde1 = 0, opsT2mde1 = 0;
            String tmpport = "", tmpservice = "", tmpter = "";
            String ntmpport = "", ntmpservice = "",ittPrevLegArrDate="";
            int transtime = 0;
            String istranstime = "N";
            String polTerminal = null;
            boolean isFullRouting = false;
            String strBoundThereLoad = null;
            boolean ishaulfinished = false;
            // TRANS_LAYOVER_PORT_BASED: Store previous scheduled leg's service, terminal and type
            // (Haulage moves don't have service, so we need to preserve the scheduled leg's values)
            String prevScheduledLegService = "";
            String prevScheduledLegTerminal = "";
            String prevScheduledLegType = "";
            // TRANS_SAME_VESSEL_SKIP: Track ALL vessels used on prior legs so connecting legs
            // skip A-A and A-B-A reuse and pick the next available different vessel.
            Set<String> usedVessels = new HashSet<String>();
            boolean isRemoveSameVessel = "Y".equalsIgnoreCase(removeSameVessel);
            // SVC-VOY-BND Restriction Parsing
            isMainSVCReq = strMainRestriction.contains("SVC") ? "N" : "Y";
            isMainVoyReq = strMainRestriction.contains("VOY") ? "N" : "Y";
            isMainBoundReq = strMainRestriction.contains("BND") ? "N" : "Y";
            isFeederSvcReq = strFeederRestriction.contains("SVC") ? "N" : "Y";
            isFeederVoyReq = strFeederRestriction.contains("VOY") ? "N" : "Y";
            isFeederBoundReq = strFeederRestriction.contains("BND") ? "N" : "Y";
            strSerType = strLegType;
            if ("F".equalsIgnoreCase(strSerType)) {
                isSvcReq = isFeederSvcReq;
                isVoyReq = isFeederVoyReq;
                isBoundReq = isFeederBoundReq;
            } else {
                isSvcReq = isMainSVCReq;
                isVoyReq = isMainVoyReq;
                isBoundReq = isMainBoundReq;
            }
            isBoundReq = ((isDichargeBound) ? "Y" : isBoundReq);
            logPrinter("[AUTOLEG CONFIG] LegType:" + strSerType + "(" + ("F".equalsIgnoreCase(strSerType) ? "FEEDER" : "MAINLINE") + "), Restrictions(SVC,VOY,BND):" + isSvcReq + "," + isVoyReq + "," + isBoundReq + ", isDichargeBound:" + isDichargeBound + " | MainLine(SVC,VOY,BND):" + isMainSVCReq + "," + isMainVoyReq + "," + isMainBoundReq + " | Feeder(SVC,VOY,BND):" + isFeederSvcReq + "," + isFeederVoyReq + "," + isFeederBoundReq);
            //
            if (blpol != null && blpol.toString().trim().length() > 0) {
                POL = blpol.toString().trim();
            }
            //
            if (setpolter != null && setpolter.toString().trim().length() > 0) {
                polter = setpolter.toString().trim();
            }
            //
            if (setpodter != null && setpodter.toString().trim().length() > 0) {
                podter = setpodter.toString().trim();
            }
            //
            if (setTrans1Ter != null && setTrans1Ter.toString().trim().length() > 0) {
                TS1T = setTrans1Ter.toString().trim();
            }
            //
            if (setTrans2Ter != null && setTrans2Ter.toString().trim().length() > 0) {
                TS2T = setTrans2Ter.toString().trim();
            }
            //
            if (setTrans3Ter != null && setTrans3Ter.toString().trim().length() > 0) {
                TS3T = setTrans3Ter.toString().trim();
            }
            //
            if (setService != null && setService.toString().trim().length() > 0) {
                service = setService.toString().trim();
            }
            //
            if (setVessel != null && setVessel.toString().trim().length() > 0) {
                vessel = setVessel.toString().trim();
            }
            //
            if (setVoyage != null && setVoyage.toString().trim().length() > 0) {
                voyage = setVoyage.toString().trim();
            }
            //
            if (setBound != null && setBound.toString().trim().length() > 0) {
                bound = setBound.toString().trim();
            }
            //
            if (setETD1 != null && setETD1.toString().trim().length() > 0) {
                ETD = setETD1.toString().trim();
            }
            //
            // System.out.println("[ECOM] EcomBooingBean setGrpCallId-> " + setGrpCallId);
            if (setGrpCallId != null && setGrpCallId.toString().trim().length() > 0) {
                CALLID = setGrpCallId.toString().trim();
            }
            //
            if (setTrans1 != null && setTrans1.toString().trim().length() > 0) {
                TS1 = setTrans1.toString().trim();
            }
            //
            if (setTrans1SVC != null && setTrans1SVC.toString().trim().length() > 0) {
                TS1S = setTrans1SVC.toString().trim();
            }
            //
            if (setTrans2 != null && setTrans2.toString().trim().length() > 0) {
                TS2 = setTrans2.toString().trim();
            }
            //
            if (setTrans2SVC != null && setTrans2SVC.toString().trim().length() > 0) {
                TS2S = setTrans2SVC.toString().trim();
            }
            //
            if (setTrans3 != null && setTrans3.toString().trim().length() > 0) {
                TS3 = setTrans3.toString().trim();
            }
            //
            if (setTrans3SVC != null && setTrans3SVC.toString().trim().length() > 0) {
                TS3S = setTrans3SVC.toString().trim();
            }
            //
            if (setBlpod != null && setBlpod.toString().trim().length() > 0) {
                POD = setBlpod.toString().trim();
            }
            //
            if (opstrans1Mode != null && opstrans1Mode.toString().trim().length() > 0) {
                opstrans1Mode = opstrans1Mode.toString().trim();
            }
            //
            if (opstrans2Mode != null && opstrans2Mode.toString().trim().length() > 0) {
                opstrans2Mode = opstrans2Mode.toString().trim();
            }
            //
            logPrinter("[FLAG_LOGIC_INPUT] TS1:" + TS1 + ", TS2:" + TS2 + ", TS2S:" + TS2S + ", TS3:" + TS3 + ", TS3S:" + TS3S + ", POD:" + POD + ", opstrans1Mode:" + opstrans1Mode + ", opstrans2Mode:" + opstrans2Mode);
            if (TS1.trim().length() > 0) {
                flag1 = 1;
            }
            if (TS2.trim().length() > 0 && TS2S.trim().length() > 0) {
                flag2 = 1;
                if ((opstrans1Mode != null) && (opstrans1Mode.trim().length() > 0)
                        && (!"V".equalsIgnoreCase(opstrans1Mode.trim()))) {
                    opsT1mde1 = 1;
                }
            } else if ((opstrans1Mode != null) && (opstrans1Mode.trim().length() > 0)
                    && (!"V".equalsIgnoreCase(opstrans1Mode.trim()))) {
                flag2 = 1;
                opsT1mde1 = 1;
               // logPrinter("[FLAG2_OPST1MDE1_SET] opstrans1Mode is not V (no TS2/TS2S), flag2=1, opsT1mde1=1");
            } /*else if (TS2.trim().length() > 0 && TS2S.trim().length() == 0 && TS3.trim().length() > 0 && (opstrans2Mode != null) && (opstrans2Mode.trim().length() > 0)
                && (!"V".equalsIgnoreCase(opstrans2Mode.trim()))) {
                flag2 = 1;
            }*/
            if (TS3.trim().length() > 0 && TS3S.trim().length() > 0) {
                flag3 = 1;
                if ((opstrans2Mode != null) && (opstrans2Mode.trim().length() > 0)
                        && (!"V".equalsIgnoreCase(opstrans2Mode.trim()))) {
                    opsT2mde1 = 1;
                    flag2 = 1;
                }
            } else if ((opstrans2Mode != null) && (opstrans2Mode.trim().length() > 0)
                    && (!"V".equalsIgnoreCase(opstrans2Mode.trim()))) {
                flag2 = 1;
                opsT2mde1 = 1;
            }
            if (POD.trim().length() > 0) {
                flag4 = 1;
            }
            logPrinter("[TRANS FLAGS] flag1(TS1):" + flag1 + ", flag2(TS2/OpsMode1):" + flag2 + ", flag3(TS3/OpsMode2):" + flag3 + ", flag4(POD):" + flag4 + ", opsT1mde1:" + opsT1mde1 + ", opsT2mde1:" + opsT2mde1);
            
            String hrs = "1", ithhrs = "1";
            String btwPortHrs = "0";
            String fallbackRTH = "1";
            String ffhrs = "";
            boolean isTransLayoverPortBased = false;
            Map<String, List<LayoverTimeVo>> layoverConfigCache = new HashMap<String, List<LayoverTimeVo>>();
            
            // Load RTH (Routing Transit Hours) config
            try {
                pst = con.prepareStatement(" select value from bkgconfig (NOLOCK) where attribute='RTH' and status='A'",
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = pst.executeQuery();
                if (rs.next()) {
                    hrs = rs.getString("value").trim();
                }
                fallbackRTH = hrs;
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Load FFH (Feeder Layover Hours) config
            try {
                pst = con.prepareStatement(" select value from bkgconfig (NOLOCK) where attribute='FFH' and status='A'",
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = pst.executeQuery();
                if (rs.next()) {
                    ffhrs = checkNull(rs.getString("value")).trim();
                }
                if (ffhrs.isEmpty()) {
                    ffhrs = fallbackRTH;
                }
            } catch (Exception e) {
                ffhrs = fallbackRTH;
                e.printStackTrace();
            }
            
            // Check TRANS_LAYOVER_PORT_BASED plugin
            try {
                isTransLayoverPortBased = "Y".equalsIgnoreCase((String) ecomplugin.get("SCHEDULE_TRANS_LAYOVER_PORT_BASED"));
            } catch (Exception e) {
                isTransLayoverPortBased = false;
            }
            
            // Load layover configs for transshipment ports
            if (isTransLayoverPortBased) {
                try {
                    loadLayoverConfigsForPorts(layoverConfigCache, TS1, TS2, TS3, POD);
                } catch (Exception e) {
                    logPrinter("[LAYOVER ERROR] Failed to load layover configs: " + e.getMessage());
                }
            }
            
            // Load ITH (Inter Terminal Hours) config
            try {
                pst = con.prepareStatement(" select value from bkgconfig (NOLOCK) where attribute='ITH' and status='A'",
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = pst.executeQuery();
                if (rs.next()) {
                    ithhrs = rs.getString("value").trim();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Get ETD from schedule
            pst = con.prepareStatement(" select depdate,terminal from allschedules (NOLOCK) where groupcallid=? ",
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, CALLID);
            rs = pst.executeQuery();
            if (rs.next()) {
                ETD = rs.getString("depdate").trim();
                polTerminal = checkNull(rs.getString("terminal"));
            }
            
            // Load BFT (Before First Trans) config
            try {
                pst = con.prepareStatement(" select value from bkgconfig (NOLOCK) where attribute='BFT' and status='A'",
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = pst.executeQuery();
                if (rs.next()) {
                    bft = rs.getString("value").trim();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Load BLT (Before Last Trans) config
            try {
                pst = con.prepareStatement(" select value from bkgconfig (NOLOCK) where attribute='BLT' and status='A'",
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = pst.executeQuery();
                if (rs.next()) {
                    blt = rs.getString("value").trim();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            logPrinter("[LAYOVER CONFIG] RTH(RoutingTransitHrs):" + hrs + ", FFH(FeederLayoverHrs):" + ffhrs + ", ITH(InterTerminalHrs):" + ithhrs + ", BFT(BeforeFirstTrans):" + bft + ", BLT(BeforeLastTrans):" + blt + ", TRANS_LAYOVER_PORT_BASED:" + isTransLayoverPortBased + ", ETD:" + ETD + ", POLTerminal:" + polTerminal);
            //
            // ================ FIRST LEG: POL => Trans1 ================
            if (flag1 == 1 && ETD != null && ETD.trim().length() > 0) {
                isFullRouting = false;
                tmpport = TS1;
                tmpservice = TS1S;
                tmpter = TS1T;
                transtime = getTranstTime(POL, tmpport, legtranshs);
                istranstime = (transtime > 0) ? "Y" : "N";
                
                logPrinter("[FIRST LEG] POL:" + POL + "=>TS1:" + tmpport + " | Vessel:" + vessel + ", Voyage:" + voyage + ", Bound:" + disbound + ", Service:" + service + ", ETD:" + ETD + ", Terminal:" + TS1T + ", TransitDays:" + transtime + " | Restrictions(SVC,VOY,BND,TER):" + isSvcReq + "," + isVoyReq + "," + isBoundReq + "," + strTransTerminalOnly);
                
                Sql = "select top 1 a.service, a.vessel, a.voyage,a.terminal, a.bound, a.port, a.arrdate, a.type, a.groupcallid,b.vendor_code,c.vendor_name ,a.depdate  from allschedules a(NOLOCK) "
                        + "left join  feederscheduleVendors b(nolock) on a.service=b.Service and a.voyage=b.voyage and  "
                        + " a.vessel=b.vessel and a.bound=b.bound left join VendorDetails c(nolock) on  c.vendor_code=b.vendor_code "
                        + " where a.vessel=?  and  a.port=?  and a.arrdate > ? "
                        + "and (? = 'N' or a.service=?) and (? = 'N' or a.bound=?) and  (? = 'N' or a.voyage=?) and (?='N' or a.terminal=?) and (?='N' or a.arrdate < DATEADD(DAY, ?,?)) "
                        + "order by a.arrdate asc ";
                pst = con.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                pst.setString(1, vessel);
                pst.setString(2, tmpport);
                pst.setString(3, ETD);
                pst.setString(4, isSvcReq);
                pst.setString(5, service);
                pst.setString(6, isBoundReq);
                pst.setString(7, disbound);
                pst.setString(8, isVoyReq);
                pst.setString(9, voyage);
                pst.setString(10, strTransTerminalOnly);
                pst.setString(11, TS1T);
                pst.setString(12, istranstime);
                pst.setInt(13, transtime);
                pst.setString(14, ETD);
                rs = pst.executeQuery();
                rs.last();
                int rcnt = rs.getRow();
                rs.beforeFirst();
                logPrinter("[FIRST LEG QUERY] ResultCount:" + rcnt);
                if (rcnt <= 0 && "Y".equalsIgnoreCase(strTransTerminalOnly)) {
                    logPrinter("[FIRST LEG FALLBACK] Retrying without terminal restriction");
                    pst = con.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    pst.setString(1, vessel);
                    pst.setString(2, tmpport);
                    pst.setString(3, ETD);
                    pst.setString(4, isSvcReq);
                    pst.setString(5, service);
                    pst.setString(6, isBoundReq);
                    pst.setString(7, disbound);
                    pst.setString(8, isVoyReq);
                    pst.setString(9, voyage);
                    pst.setString(10, "N");
                    pst.setString(11, TS1T);
                    pst.setString(12, istranstime);
                    pst.setInt(13, transtime);
                    pst.setString(14, ETD);
                    rs = pst.executeQuery();
                    rs.last();
                    rcnt = rs.getRow();
                    rs.beforeFirst();
                    logPrinter("[FIRST LEG FALLBACK] ResultCount:" + rcnt);
                }
                if (rs.next()) {
                    isFullRouting = true;
                    logPrinter("[FIRST LEG FOUND] Port:" + rs.getString("port") + ", ArrDate:" + rs.getString("arrdate") + ", Type:" + rs.getString("type") + ", Terminal:" + rs.getString("terminal"));
                    frou = new Route();
                    al = new ArrayList();
                    //
                    al.add(rs.getString("service"));
                    al.add(rs.getString("vessel"));
                    al.add(voyage);
                    // bound = rs.getString("bound");
                    // al.add(bound);
                    al.add(rs.getString("bound"));
                    Port = rs.getString("port");
                    al.add(Port);
                    TempDate = rs.getString("arrdate").trim();
                    al.add(TempDate);
                    al.add(rs.getString("type"));
                    al.add(rs.getString("groupcallid"));
                    al.add("N");
                    al.add(ETD);
                    //
                    tempDepDate = rs.getString("depdate");
                    //

                    frou.service = rs.getString("service");
                    frou.vessel = rs.getString("vessel");

                    frou.voyage = voyage;
                    frou.dischargebound = rs.getString("bound");
                    frou.bound = bound;
                    frou.loadport = POL;
                    frou.ETA1 = TempDate;
                    frou.legType = rs.getString("type");
                    frou.dischargecallid = rs.getString("groupcallid");
                    frou.loadcallid = setGrpCallId;
                    frou.grpCallId = setGrpCallId;
                    frou.ETD1 = ETD;
                    frou.disport = tmpport;
                    frou.routeId = routeid;
                    frou.blpolTer = polTerminal;
                    frou.blpodTer = rs.getString("terminal");
                    frou.defRoute = defaultroute;
                    frou.cutoff_polterminal = BLpolter.trim();
                    frou.cutoff_polarrdate = ETA;
                    frou.setClosingtime(lct);
                    // TRANS_LAYOVER_PORT_BASED: Store previous scheduled leg's service, terminal and type for layover lookup
                    // (Haulage moves don't have service, so we need to preserve the scheduled leg's values)
                    prevScheduledLegService = frou.service;
                    prevScheduledLegTerminal = frou.blpodTer;
                    prevScheduledLegType = frou.legType;
                    addUsedVessel(usedVessels, frou.vessel);
                }
                /*
                 * if ((!isFullRouting) && "Y".equalsIgnoreCase(strTransTerminalOnly)) {
                 * frou = getAlternativeSchedule(null, isSvcReq, service, vessel, isBoundReq,
                 * disbound, tmpport, ETD, isVoyReq, voyage, "N", TS1T, istranstime, transtime,
                 * "0");
                 * if (frou != null) {
                 * isFullRouting = frou.isIsRouting();
                 * al = new ArrayList();
                 * //frou.service = rs.getString("service");
                 * frou.service = service;
                 * //frou.vessel = rs.getString("vessel");
                 * frou.vessel = vessel;
                 * 
                 * frou.voyage = voyage;
                 * //frou.dischargebound = rs.getString("bound");
                 * frou.bound = bound;
                 * frou.loadport = POL;
                 * TempDate = frou.ETA1;
                 * //frou.legType = rs.getString("type");
                 * //frou.dischargecallid = rs.getString("groupcallid");
                 * frou.loadcallid = setGrpCallId;
                 * frou.grpCallId = setGrpCallId;
                 * frou.ETD1 = ETD;
                 * frou.disport = tmpport;
                 * frou.routeId = routeid;
                 * frou.blpolTer = polTerminal;
                 * //frou.blpodTer = rs.getString("terminal");
                 * frou.defRoute = defaultroute;
                 * frou.cutoff_polterminal = BLpolter.trim();
                 * frou.cutoff_polarrdate = ETA;
                 * frou.setClosingtime(lct);
                 * //
                 * tempDepDate = frou.getDepdatefornexttrans();
                 * //
                 * }
                 * }
                 */
                if (frou != null) {
                    lstarry.add(frou);
                    hts.put("1", al);
                }
            }
            //
            logPrinter("TS1: " + TS1 + ", TS1S: " + TS1S + ", TS1T: " + TS1T + ", TS2: " + TS2 + ", TS2S: " + TS2S + ", TS3: " + TS3);
            if (TS1.trim().length() > 0 ) {
                tmpport = TS1.trim();
                tmpter = TS1T;
            }
            if (TS1.trim().length() > 0 && TS1S.trim().length() > 0) {
                tmpservice = TS1S.trim();
            }
            if (TS2.trim().length() > 0 ){
                ntmpport = TS2.trim();
            }
            if(TS2.trim().length() > 0 && TS2S.trim().length() > 0) {
                ntmpservice = TS2S.trim();
            }
            if ("Y".equalsIgnoreCase(bft)) {
                legformdate = tempDepDate;
            } else {
                legformdate = TempDate;
            }
      //      logPrinter("[LEG FORMATION DATE] BFT:" + bft + ", legformdate:" + legformdate + ", TempDate:" + TempDate + ", tempDepDate:" + tempDepDate);
            
            // ================ TRANSSHIPMENT 1: Trans1 => Trans2/POD ================
            logPrinter("[LEG FORMATION DATE] BFT:" + bft + ", flag1: " + flag1 + ", flag2: " + flag2 + ", legformdate: " + legformdate + ", TempDate: " + TempDate + ", tempDepDate: " + tempDepDate);
            if (flag1 == 1 && flag2 == 1 && legformdate != null && legformdate.trim().length() > 0) {
                logPrinter("[TRANSSHIPMENT 1 START] TS1:" + tmpport + "=>TS2/POD:" + (ntmpport.length() > 0 ? ntmpport : POD) + " | Service:" + tmpservice + ", Terminal:" + tmpter + ", isFullRouting:" + isFullRouting);
                isFullRouting = false;
                al = new ArrayList();
                frou = new Route();
                //
                if (checkNull(TS1).length() > 0 && checkNull(TS2).length() > 0
                        && !TS1.trim().equalsIgnoreCase(TS2.trim())) {
                    if (opsT1mde1 == 1) {
                        // ITT (Inter Terminal Transfer) / Haulage Mode for Trans1
                        isFullRouting = true;
                        String tmpops1date = "";
                        String tmpops2date = "";
                        tmpops1date = addHours("5", legformdate);
                        tmpops2date = addHours("5", tmpops1date);
                        logPrinter("[TRANS1 ITT/HAULAGE] Mode:" + opstrans1Mode + ", FromPort:" + tmpport + ", ToPort:" + (TS2 != null && TS2.length() > 0 ? TS2 : POD) + ", ETD:" + tmpops1date + ", ETA:" + tmpops2date);
                        al.add("");
                        al.add("");
                        al.add("");
                        al.add("");
                        al.add(tmpport.trim());
                        al.add(tmpops1date);
                        al.add(opstrans1Mode.trim());
                        al.add("");
                        al.add(((TS2 != null && TS2.toString().trim().length() > 0) ? TS2.toString().trim()
                                : POD.trim()));

                        al.add(tmpops2date);
                        al.add(opstrans1Mode.trim());
                        al.add("");
                        TempDate = tmpops2date.toString();
                        tempDepDate = tmpops2date.toString();
                        al.add("Y");
                        al.add("");
                        al.add("");

                        if (!(TS2 != null && TS2.toString().trim().length() > 0)) {
                            ishaulfinished = true;
                        }

                        frou.service = "";
                        frou.vessel = "";
                        frou.voyage = "";
                        frou.bound = "";
                        frou.loadport = tmpport;
                        frou.ETA1 = tmpops2date;
                        frou.legType = opstrans1Mode.trim();
                        frou.dischargecallid = "";
                        frou.loadcallid = "";
                        frou.grpCallId = "";
                        frou.ETD1 = tmpops1date;
                        frou.disport = ((TS2 != null && TS2.toString().trim().length() > 0) ? TS2.toString().trim()
                                : POD.trim());
                        frou.routeId = routeid;
                        frou.blpolTer = TS1T;
                        frou.blpodTer = TS2T;
                        frou.defRoute = defaultroute;
                    } else {
                        // Vessel Leg for Transshipment 1
                        resbound = tmpport + "~~" + ntmpport + "~~" + tmpservice;
                        bcList = (List<BoundConfigVo>) defbound.get(resbound);
                        AutoLegVo autoLegVo = determineStrBoundThereLoad(bcList);
                        strBoundThereLoad = autoLegVo.getStrBoundThereLoad();
                        boolean hasVesselVoyageInfo = autoLegVo.isHasVesselVoyageInfo();
                        boundres = autoLegVo.getBoundConfig();
                        logPrinter("[TRANS1 BOUND CONFIG] Key:" + resbound + ", strBoundThereLoad:" + strBoundThereLoad + ", hasVesselVoyageInfo:" + hasVesselVoyageInfo + ", Lbound:" + (boundres != null ? boundres.getLbound() : "null") + ", Dbound:" + (boundres != null ? boundres.getDbound() : "null"));
                        
                        // LAYOVER: Calculate for Transshipment 1 load
                        String trans1LayoverHrs = hrs;
                        LayoverTimeVo trans1LayoverConfig = null;
                        String prevLegArrDate = legformdate;
                        if (isTransLayoverPortBased) {
                            if ("F".equalsIgnoreCase(prevScheduledLegType)) {
                                trans1LayoverHrs = ffhrs;
                                logPrinter("[TRANS1 LAYOVER] PrevLeg is Feeder, using FFH:" + ffhrs);
                            } else {
                                trans1LayoverConfig = getLayoverTimeConfigFromCache(layoverConfigCache, tmpport, prevScheduledLegService, prevScheduledLegTerminal, fallbackRTH);
                                trans1LayoverHrs = getInitialLayoverHoursForQuery(trans1LayoverConfig, fallbackRTH, ffhrs);
                            }
                            logPrinter("[TRANS1 LAYOVER] Port:" + tmpport + ", PrevService:" + prevScheduledLegService + ", PrevTerminal:" + prevScheduledLegTerminal + ", PrevType:" + prevScheduledLegType + ", PrevArrDate:" + prevLegArrDate + ", LayoverHrs:" + trans1LayoverHrs);
                        }
                        logPrinter("[TRANS1 LOAD QUERY] Service:" + tmpservice + ", Port:" + tmpport + ", BoundFilter:" + strBoundThereLoad + ", Lbound:" + boundres.getLbound() + ", BLT:" + blt + ", LegFormDate:" + legformdate + ", TerminalOnly:" + strTransTerminalOnly + ", Terminal:" + TS1T);
                        Sql = "select service, vessel, voyage, bound, port, depdate, arrdate, type, groupcallid, terminal  from allschedules (NOLOCK) where service=? "
                                + " and port=? and ('N'=? or bound=?) and ('Y'=? or depdate > DATEADD(hh, CONVERT(int, '" + trans1LayoverHrs + "'), ? )) "
                                + "and ('N'=? or arrdate > DATEADD(hh, CONVERT(int, '" + trans1LayoverHrs + "'), ? )) and (?='N' or terminal=?) order by arrdate  ";
                        pst = con.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        pst.setString(1, tmpservice);
                        pst.setString(2, tmpport);
                        pst.setString(3, strBoundThereLoad);
                        pst.setString(4, boundres.getLbound());
                        pst.setString(5, blt);
                        pst.setString(6, legformdate);
                        pst.setString(7, blt);
                        pst.setString(8, legformdate);
                        pst.setString(9, strTransTerminalOnly);
                        pst.setString(10, TS1T);
                        rs = pst.executeQuery();
                        rs.last();
                        int rcnt = rs.getRow();
                        rs.beforeFirst();
                        logPrinter("[TRANS1 LOAD RESULT] QueryCount:" + rcnt);
                        translis = setTransLoadLegDetails(rs, null, boundres, bcList, hasVesselVoyageInfo);
                        if ("Y".equalsIgnoreCase(strTransTerminalOnly)) {
                            pst = con.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            pst.setString(1, tmpservice);
                            pst.setString(2, tmpport);
                            pst.setString(3, strBoundThereLoad);
                            pst.setString(4, boundres.getLbound());
                            pst.setString(5, blt);
                            pst.setString(6, legformdate);
                            pst.setString(7, blt);
                            pst.setString(8, legformdate);
                            pst.setString(9, "N");
                            pst.setString(10, TS1T);
                            rs = pst.executeQuery();
                            rs.last();
                            rcnt = rs.getRow();
                            rs.beforeFirst();
                            logPrinter("[TRANS1 LOAD FALLBACK] QueryCount:" + rcnt);
                            translis = setTransLoadLegDetails(rs, translis, boundres, bcList, hasVesselVoyageInfo);
                        }
                        logPrinter("[TRANS1 LOAD SCHEDULES] TotalSchedules:" + (translis != null ? translis.size() : 0));
                        /*
                         * String[] ser = new String[rcnt];
                         * String[] vsl = new String[rcnt];
                         * String[] voy = new String[rcnt];
                         * String[] bnd = new String[rcnt];
                         * String[] dis_bnd = new String[rcnt];
                         * String[] dep = new String[rcnt];
                         * String[] typ = new String[rcnt];
                         * String[] gcallid = new String[rcnt];
                         * String[] terminal = new String[rcnt];
                         * int i = 0;
                         * while (rs.next()) {
                         * ser[i] = rs.getString("service");
                         * vsl[i] = rs.getString("vessel");
                         * voy[i] = rs.getString("voyage");
                         * bnd[i] = rs.getString("bound");
                         * dis_bnd[i] = rs.getString("bound");
                         * dep[i] = rs.getString("depdate");
                         * typ[i] = rs.getString("type");
                         * gcallid[i] = rs.getString("groupcallid");
                         * terminal[i] = rs.getString("terminal");
                         * isDichargeBound = false;
                         * if (!"N/A".equalsIgnoreCase(boundres.getDbound())) {
                         * dis_bnd[i] = boundres.getDbound();
                         * isDichargeBound = true;
                         * }
                         * i++;
                         * }
                         */

                        // Query discharge leg for each load schedule
                        for (int k = 0; k < translis.size(); k++) {
                            translegvo = translis.get(k);
                            // TRANS_SAME_VESSEL_SKIP: This candidate's discharge leg would run on the same
                            // vessel as the previous leg (POL=>TS1) - skip it and try the next available
                            // candidate with a different vessel instead of forming a same-vessel connection.
                            if (isRemoveSameVessel && isVesselAlreadyUsed(usedVessels, translegvo.getVessel())) {
                                logPrinter("[TRANS1 SAME VESSEL SKIP] Candidate:" + k + ", Vessel:" + translegvo.getVessel() + " already used on prior leg - trying next schedule");
                                continue;
                            }
                            boundres = translegvo.getBoundConfig();
                            isDichargeBound = false;
                            if (boundres != null && (!"N/A".equalsIgnoreCase(boundres.getDbound()))) {
                                isDichargeBound = true;
                            }
                            logPrinter("[TRANS1 DISCH QUERY " + k + "/" + translis.size() + "] LoadPort:" + tmpport + ", DischPort:" + ntmpport + " | Vessel:" + translegvo.getVessel() + ", Voyage:" + translegvo.getVoyage() + ", Service:" + translegvo.getService() + ", Type:" + translegvo.getType() + ", DepDate:" + translegvo.getDepatureDate() + ", isDichargeBound:" + isDichargeBound);
                            
                            Sql = "select a.service, a.vessel, a.voyage, a.bound, a.port, a.arrdate, a.type, a.groupcallid,b.vendor_code,c.vendor_name, a.depdate,a.terminal  from allschedules a(NOLOCK) "
                                    + "left join  feederscheduleVendors b(nolock) on a.service=b.Service and a.voyage=b.voyage and "
                                    + "a.vessel=b.vessel and a.bound=b.bound left join VendorDetails c(nolock) on    c.vendor_code=b.vendor_code "
                                    + "where (? = 'N' or a.service=?) and a.vessel=? "
                                    + "and (? = 'N' or a.voyage=?) and (? = 'N' or a.bound=?) and  a.port=?  and a.arrdate > DATEADD(hh, CONVERT(int, '"
                                    + btwPortHrs + "'), '" + translegvo.getDepatureDate()
                                    + "') and (?='N' or a.terminal=?) and (?='N' or a.arrdate < DATEADD(DAY, ?,?)) order by a.arrdate asc ";
                            pst = con.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                            strSerType = translegvo.getType();
                            if ("F".equalsIgnoreCase(strSerType)) {
                                isSvcReq = isFeederSvcReq;
                                isVoyReq = isFeederVoyReq;
                                isBoundReq = isFeederBoundReq;
                            } else {
                                isSvcReq = isMainSVCReq;
                                isVoyReq = isMainVoyReq;
                                isBoundReq = isMainBoundReq;
                            }
                            isBoundReq = ((isDichargeBound) ? "Y" : isBoundReq);
                            transtime = getTranstTime(tmpport, ntmpport, legtranshs);
                            istranstime = (transtime > 0) ? "Y" : "N";

                            pst.setString(1, isSvcReq);
                            pst.setString(2, translegvo.getService());// ser[k].trim()
                            pst.setString(3, translegvo.getVessel());// vsl[k].trim()
                            pst.setString(4, isVoyReq);
                            pst.setString(5, translegvo.getVoyage());// voy[k].trim()
                            pst.setString(6, isBoundReq);
                            pst.setString(7, translegvo.getDischargeBound());// dis_bnd[k].trim()
                            pst.setString(8, ntmpport.trim());
                            pst.setString(9, strTransTerminalOnly);
                            pst.setString(10, TS2T);
                            pst.setString(11, istranstime);
                            pst.setInt(12, transtime);
                            pst.setString(13, translegvo.getDepatureDate());// dep[k]
                            rs = pst.executeQuery();
                            rs.last();
                            rcnt = rs.getRow();
                            rs.beforeFirst();
                            logPrinter(" rcnt=> " + rcnt);
                            if (rcnt <= 0 && "Y".equalsIgnoreCase(strTransTerminalOnly)) {
                                pst = con.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);
                                pst.setString(1, isSvcReq);
                                pst.setString(2, translegvo.getService());// ser[k].trim()
                                pst.setString(3, translegvo.getVessel());// vsl[k].trim()
                                pst.setString(4, isVoyReq);
                                pst.setString(5, translegvo.getVoyage());// voy[k].trim()
                                pst.setString(6, isBoundReq);
                                pst.setString(7, translegvo.getDischargeBound());// dis_bnd[k].trim()
                                pst.setString(8, ntmpport.trim());
                                pst.setString(9, "N");
                                pst.setString(10, TS2T);
                                pst.setString(11, istranstime);
                                pst.setInt(12, transtime);
                                pst.setString(13, translegvo.getDepatureDate());// dep[k]
                                rs = pst.executeQuery();
                                rs.last();
                                rcnt = rs.getRow();
                                rs.beforeFirst();
                                logPrinter(" rcnt 2=> " + rcnt);
                            }
                            while (rs.next()) {
                                // LAYOVER VALIDATION for Trans1
                                if (isTransLayoverPortBased) {
                                    String currentLegType = translegvo.getType();
                                    String actualLayover;
                                    if ("F".equalsIgnoreCase(prevScheduledLegType) || "F".equalsIgnoreCase(currentLegType)) {
                                        actualLayover = ffhrs;
                                    } else if (trans1LayoverConfig != null) {
                                        String nextLegLoadTerminal = translegvo.getTerminal();
                                        actualLayover = calculateTransLayoverHours(trans1LayoverConfig, 
                                                                    prevScheduledLegTerminal, nextLegLoadTerminal, fallbackRTH);
                                    } else {
                                        actualLayover = fallbackRTH;
                                    }
                                    String scheduleDepDate = translegvo.getDepatureDate();
                                    String scheduleArrDate = translegvo.getArrivalDate();
                                    
                                    if (!isScheduleLayoverValid(prevLegArrDate, scheduleDepDate, scheduleArrDate, blt, actualLayover)) {
                                        logPrinter("[TRANS1 LAYOVER SKIP] PrevType:" + prevScheduledLegType + ", CurrType:" + currentLegType + ", PrevDisch:" + prevScheduledLegTerminal + ", NextLoad:" + translegvo.getTerminal() + ", ActualLayoverHrs:" + actualLayover + " - Schedule skipped");
                                        continue;
                                    }
                                    logPrinter("[TRANS1 LAYOVER VALID] PrevType:" + prevScheduledLegType + ", CurrType:" + currentLegType + ", PrevDisch:" + prevScheduledLegTerminal + ", NextLoad:" + translegvo.getTerminal() + ", ActualLayoverHrs:" + actualLayover);
                                }
                                
                                isFullRouting = true;
                                service = translegvo.getService();
                                al.add(service);
                                al.add(rs.getString("vessel"));
                                al.add(translegvo.getVoyage());
                                bound = translegvo.getBound();
                                al.add(bound);
                                POL = tmpport.trim();
                                al.add(tmpport.trim());
                                al.add(translegvo.getDepatureDate());
                                al.add(translegvo.getType());
                                al.add(translegvo.getGroupCallId());
                                Port = rs.getString("port");
                                al.add(Port);
                                al.add(rs.getString("arrdate"));
                                al.add(rs.getString("type"));
                                al.add(rs.getString("groupcallid"));
                                TempDate = rs.getString("arrdate");
                                logPrinter("[TRANS1 LEG FOUND] LoadPort:" + tmpport + "=>DischPort:" + Port + " | Vessel:" + rs.getString("vessel") + ", Voyage:" + translegvo.getVoyage() + ", ArrDate:" + TempDate + ", Type:" + rs.getString("type"));
                                if (rcdBnd.equalsIgnoreCase("N/A")) {
                                    al.add("Y");
                                } else {
                                    al.add("N");
                                }
                                //
                                tempDepDate = rs.getString("depdate");
                                //
                                frou.service = translegvo.getService();//ser[k].trim();
                                frou.vessel = rs.getString("vessel");
                                frou.voyage = translegvo.getVoyage();//voy[k].trim();
                                frou.bound = translegvo.getBound();//bnd[k].trim();
                                frou.dischargebound = rs.getString("bound");
                                frou.loadport = tmpport;
                                frou.ETA1 = TempDate;
                                frou.legType = rs.getString("type");
                                frou.dischargecallid = rs.getString("groupcallid");
                                frou.loadcallid = translegvo.getGroupCallId();//gcallid[k].trim();
                                frou.grpCallId = translegvo.getGroupCallId();//gcallid[k].trim();
                                frou.ETD1 = translegvo.getDepatureDate();//dep[k].trim();
                                frou.disport = Port;
                                frou.routeId = routeid;
                                frou.blpolTer = translegvo.getTerminal();//terminal[k];
                                frou.blpodTer = rs.getString("terminal");
                                frou.defRoute = defaultroute;
                                // TRANS_LAYOVER_PORT_BASED: Update stored values for next leg's layover lookup
                                prevScheduledLegService = frou.service;
                                prevScheduledLegTerminal = frou.blpodTer;
                                prevScheduledLegType = frou.legType;
                                addUsedVessel(usedVessels, frou.vessel);

                                break;
                            }
                            /*   if ((!isFullRouting) && "Y".equalsIgnoreCase(strTransTerminalOnly)) {

                                frou = getAlternativeSchedule(null, isSvcReq, ser[k].trim(), vsl[k].trim(), isBoundReq, dis_bnd[k].trim(), ntmpport.trim(), dep[k], isVoyReq, voy[k].trim(), "N", TS2T, istranstime, transtime, btwPortHrs);
                                if (frou != null) {
                                    isFullRouting = frou.isIsRouting();
                                    al = new ArrayList();
                                    frou.service = ser[k].trim();
                                    frou.vessel = vsl[k].trim();
                                    frou.voyage = voy[k].trim();
                                    frou.bound = bnd[k].trim();
                                    frou.loadport = tmpport;
                                    TempDate = frou.ETA1;
                                    frou.loadcallid = gcallid[k].trim();
                                    frou.grpCallId = gcallid[k].trim();
                                    frou.ETD1 = dep[k].trim();
                                    frou.disport = ntmpport;
                                    frou.routeId = routeid;
                                    frou.blpolTer = terminal[k];
                                    frou.defRoute = defaultroute;
                                    //
                                    tempDepDate = frou.getDepdatefornexttrans();
                                    //
                                    break;
                        }

                            }*/
                        if (isFullRouting) {
                            break;
                        }
                        }

                    }
                } else {
                    // ITT (Internal Terminal Transfer) - Same port, different terminal
                    isFullRouting = true;
                    String tmpops1date = "";
                    String tmpops2date = "";
                    tmpops1date = addHours(hrs, legformdate);
                    tmpops2date = addHours(ithhrs, tmpops1date);
                    String destPort = (TS2 != null && TS2.toString().trim().length() > 0) ? TS2.toString().trim() : POD.trim();
                    logPrinter("[TRANS1 ITT SAME-PORT] FromPort:" + tmpport + ", ToPort:" + destPort + ", ETD:" + tmpops1date + ", ETA:" + tmpops2date + ", RTH:" + hrs + ", ITH:" + ithhrs);
                    ittPrevLegArrDate = legformdate;
                    al.add("");
                    al.add("");
                    al.add("");
                    al.add("");
                    al.add(tmpport.trim());
                    al.add(tmpops1date);
                    al.add("I");
                    al.add("");
                    al.add(destPort);
                    al.add(tmpops2date);
                    al.add("I");
                    al.add("");
                    TempDate = tmpops2date.toString();
                    tempDepDate = tmpops2date.toString();
                    al.add("Y");
                    al.add("");
                    al.add("");

                    if (!(TS2 != null && TS2.toString().trim().length() > 0)) {
                        ishaulfinished = true;
                        logPrinter("[TRANS1 ITT] ishaulfinished:true - TS2 not present, route complete at ITT");
                    }

                    frou.service = "";
                    frou.vessel = "";
                    frou.voyage = "";
                    frou.bound = "";
                    frou.loadport = tmpport;
                    frou.ETA1 = tmpops2date;
                    frou.legType = "I";
                    frou.dischargecallid = "";
                    frou.loadcallid = "";
                    frou.grpCallId = "";
                    frou.ETD1 = tmpops1date;
                    frou.disport = destPort;
                    frou.routeId = routeid;
                    frou.blpolTer = TS1T;
                    frou.blpodTer = TS2T;
                    frou.defRoute = defaultroute;
                    
                }
                hts.put("2", al);
                lstarry.add(frou);
                logPrinter("[TRANSSHIPMENT 1 END] isFullRouting:" + isFullRouting + ", LegsCount:" + lstarry.size());
            }
            if (TS2.trim().length() > 0 ) {
                //flag3=1;
                tmpport = TS2.trim();
                tmpter = TS2T;
            }
            if (TS2.trim().length() > 0 && TS2S.trim().length() > 0) {
                //flag3=1;
                tmpservice = TS2S.trim();
            }
            if (TS3.trim().length() > 0 && TS3S.trim().length() > 0) {
                ntmpport = TS3.trim();
                ntmpservice = TS3S.trim();
            }
            //
            if ("Y".equalsIgnoreCase(bft)) {
                legformdate = tempDepDate;
            } else {
                legformdate = TempDate;
            }
            //
            // ================ TRANSSHIPMENT 2: Trans2 => Trans3/POD ================
            logPrinter("[TRANSSHIPMENT 2 CHECK] isFullRouting:" + isFullRouting + ", flag1:" + flag1 + ", flag3:" + flag3 + ", legformdate:" + legformdate);
            if (isFullRouting && flag1 == 1 && flag3 == 1 && legformdate != null && legformdate.trim().length() > 0) {
                isFullRouting = false;
                logPrinter("[TRANSSHIPMENT 2 START] TS2:" + tmpport + "=>TS3/POD:" + (ntmpport.length() > 0 ? ntmpport : POD) + " | Service:" + tmpservice + ", Terminal:" + tmpter);
                frou = new Route();
                //
                if (checkNull(TS2).length() > 0 && checkNull(TS3).length() > 0 && !TS2.trim().equalsIgnoreCase(TS3.trim())) {
                    if (opsT2mde1 == 1) {
                        // ITT (Inter Terminal Transfer) / Haulage Mode for Trans2
                        isFullRouting = true;
                        String tmpops1date = "";
                        String tmpops2date = "";
                        tmpops1date = addHours("5", legformdate);
                        tmpops2date = addHours("5", tmpops1date);
                        logPrinter("[TRANS2 ITT/HAULAGE] Mode:" + opstrans2Mode + ", FromPort:" + tmpport + ", ToPort:" + (TS3 != null && TS3.length() > 0 ? TS3 : POD) + ", ETD:" + tmpops1date + ", ETA:" + tmpops2date);
                        al.add("");
                        al.add("");
                        al.add("");
                        al.add("");
                        al.add(tmpport.trim());
                        al.add(tmpops1date);
                        al.add(opstrans2Mode.trim());
                        al.add("");
                        al.add(((TS3 != null && TS3.toString().trim().length() > 0) ? TS3.toString().trim() : POD.trim()));

                        al.add(tmpops2date);
                        al.add(opstrans2Mode.trim());
                        al.add("");
                        TempDate = tmpops2date.toString();
                        tempDepDate = tmpops2date.toString();
                        al.add("Y");
                        al.add("");
                        al.add("");

                        if (!(TS3 != null && TS3.toString().trim().length() > 0)) {
                            ishaulfinished = true;
                        }

                        frou.service = "";
                        frou.vessel = "";
                        frou.voyage = "";
                        frou.bound = "";
                        frou.loadport = tmpport;
                        frou.ETA1 = tmpops2date;
                        frou.legType = opstrans2Mode.trim();
                        frou.dischargecallid = "";
                        frou.loadcallid = "";
                        frou.grpCallId = "";
                        frou.ETD1 = tmpops1date;
                        frou.disport = ((TS3 != null && TS3.toString().trim().length() > 0) ? TS3.toString().trim()
                                : POD.trim());
                        frou.routeId = routeid;
                        frou.blpolTer = TS2T;
                        frou.blpodTer = TS3T;
                        frou.defRoute = defaultroute;
                    } else {
                        // Vessel Leg for Transshipment 2
                        resbound = tmpport + "~~" + ntmpport + "~~" + tmpservice;
                        bcList = (List<BoundConfigVo>) defbound.get(resbound);
                        AutoLegVo autoLegVo = determineStrBoundThereLoad(bcList);
                        strBoundThereLoad = autoLegVo.getStrBoundThereLoad();
                        boolean hasVesselVoyageInfo = autoLegVo.isHasVesselVoyageInfo();
                        boundres = autoLegVo.getBoundConfig();
                        logPrinter("[TRANS2 BOUND CONFIG] Key:" + resbound + ", strBoundThereLoad:" + strBoundThereLoad + ", hasVesselVoyageInfo:" + hasVesselVoyageInfo + ", Lbound:" + (boundres != null ? boundres.getLbound() : "null") + ", Dbound:" + (boundres != null ? boundres.getDbound() : "null"));
                        
                        // LAYOVER: Calculate for Transshipment 2 load
                        String trans2LayoverHrs = hrs;
                        LayoverTimeVo trans2LayoverConfig = null;
                        String prevLeg2ArrDate = legformdate;
                        if (isTransLayoverPortBased) {
                            if ("F".equalsIgnoreCase(prevScheduledLegType)) {
                                trans2LayoverHrs = ffhrs;
                                logPrinter("[TRANS2 LAYOVER] PrevLeg is Feeder, using FFH:" + ffhrs);
                            } else {
                                trans2LayoverConfig = getLayoverTimeConfigFromCache(layoverConfigCache, tmpport, prevScheduledLegService, prevScheduledLegTerminal, fallbackRTH);
                                trans2LayoverHrs = getInitialLayoverHoursForQuery(trans2LayoverConfig, fallbackRTH, ffhrs);
                            }
                            if((checkNull(TS1).length() > 0 && checkNull(TS1).equalsIgnoreCase(checkNull(TS2)))){
                            // Check if previous leg was ITT move and adjust legformdate accordingly
                            String[] ittAdjustResult = adjustLegformdateForITTPreviousLeg(ittPrevLegArrDate, legformdate, trans2LayoverHrs);
                            legformdate = ittAdjustResult[0];
                            trans2LayoverHrs = ittAdjustResult[1];
                            prevLeg2ArrDate= ittPrevLegArrDate;
                            ittPrevLegArrDate="";
                            
                            logPrinter("[TRANS2 LAYOVER] Port:" + tmpport + ", PrevService:" + prevScheduledLegService + ", PrevTerminal:" + prevScheduledLegTerminal + ", PrevType:" + prevScheduledLegType + ", PrevArrDate:" + prevLeg2ArrDate + ", LayoverHrs:" + trans2LayoverHrs);
                            }
                        }
                        logPrinter("[TRANS2 LOAD QUERY] Service:" + tmpservice + ", Port:" + tmpport + ", BoundFilter:" + strBoundThereLoad + ", Lbound:" + boundres.getLbound() + ", BLT:" + blt + ", LegFormDate:" + legformdate + ", TerminalOnly:" + strTransTerminalOnly + ", Terminal:" + TS2T);
                        
                        Sql = "select service, vessel, voyage, bound, port, depdate, arrdate, type, groupcallid ,terminal from allschedules (NOLOCK) where service=? "
                                + " and port=? and ('N'=? or bound=?) and ('Y'=? or depdate > DATEADD(hh, CONVERT(int, '" + trans2LayoverHrs + "'), ? )) and ('N'=? or arrdate > DATEADD(hh, CONVERT(int, '" + trans2LayoverHrs + "'), ? ))  and (?='N' or terminal=?) order by arrdate ";

                        pst = con.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        pst.setString(1, tmpservice);
                        pst.setString(2, tmpport);
                        pst.setString(3, strBoundThereLoad);
                        pst.setString(4, boundres.getLbound());
                        pst.setString(5, blt);
                        pst.setString(6, legformdate);
                        pst.setString(7, blt);
                        pst.setString(8, legformdate);
                        pst.setString(9, strTransTerminalOnly);
                        pst.setString(10, TS2T);
                        rs = pst.executeQuery();
                        rs.last();
                        int rcnt = rs.getRow();
                        rs.beforeFirst();
                        logPrinter("[TRANS2 LOAD RESULT] QueryCount:" + rcnt);
                        translis = setTransLoadLegDetails(rs, null, boundres, bcList, hasVesselVoyageInfo);
                        if ("Y".equalsIgnoreCase(strTransTerminalOnly)) {
                            pst = con.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            pst.setString(1, tmpservice);
                            pst.setString(2, tmpport);
                            pst.setString(3, strBoundThereLoad);
                            pst.setString(4, boundres.getLbound());
                            pst.setString(5, blt);
                            pst.setString(6, legformdate);
                            pst.setString(7, blt);
                            pst.setString(8, legformdate);
                            pst.setString(9, "N");
                            pst.setString(10, TS2T);
                            rs = pst.executeQuery();
                            rs.last();
                            rcnt = rs.getRow();
                            rs.beforeFirst();
                            logPrinter("[TRANS2 LOAD FALLBACK] QueryCount:" + rcnt);
                            translis = setTransLoadLegDetails(rs, translis, boundres, bcList, hasVesselVoyageInfo);
                        }
                        logPrinter("[TRANS2 LOAD SCHEDULES] TotalSchedules:" + (translis != null ? translis.size() : 0));
                        /*
                        String[] ser = new String[rcnt];
                        String[] vsl = new String[rcnt];
                        String[] voy = new String[rcnt];
                        String[] bnd = new String[rcnt];
                        String[] dis_bnd = new String[rcnt];
                        String[] dep = new String[rcnt];
                        String[] typ = new String[rcnt];
                        String[] gcallid = new String[rcnt];
                        String[] terminal = new String[rcnt];
                        int i = 0;
                        while (rs.next()) {
                            ser[i] = rs.getString("service");
                            vsl[i] = rs.getString("vessel");
                            voy[i] = rs.getString("voyage");
                            bnd[i] = rs.getString("bound");
                            dis_bnd[i] = rs.getString("bound");
                            dep[i] = rs.getString("depdate");
                            typ[i] = rs.getString("type");
                            gcallid[i] = rs.getString("groupcallid");
                            terminal[i] = rs.getString("terminal");
                        isDichargeBound = false;
                        if (!"N/A".equalsIgnoreCase(boundres.getDbound())) {
                                dis_bnd[i] = boundres.getDbound();
                            isDichargeBound = true;
                        }
                            i++;
                        }*/

                        // Query discharge leg for each load schedule
                        al = new ArrayList();
                        for (int k = 0; k < translis.size(); k++) {
                            translegvo = translis.get(k);
                            // TRANS_SAME_VESSEL_SKIP: This candidate's discharge leg would run on the same
                            // vessel as the previous leg (TS1=>TS2) - skip it and try the next available
                            // candidate with a different vessel instead of forming a same-vessel connection.
                            if (isRemoveSameVessel && isVesselAlreadyUsed(usedVessels, translegvo.getVessel())) {
                                logPrinter("[TRANS2 SAME VESSEL SKIP] Candidate:" + k + ", Vessel:" + translegvo.getVessel() + " already used on prior leg - trying next schedule");
                                continue;
                            }
                            boundres = translegvo.getBoundConfig();
                            isDichargeBound = false;
                            if (boundres != null && (!"N/A".equalsIgnoreCase(boundres.getDbound()))) {
                                isDichargeBound = true;
                            }
                            logPrinter("[TRANS2 DISCH QUERY " + k + "/" + translis.size() + "] LoadPort:" + tmpport + ", DischPort:" + ntmpport + " | Vessel:" + translegvo.getVessel() + ", Voyage:" + translegvo.getVoyage() + ", Service:" + translegvo.getService() + ", Type:" + translegvo.getType() + ", DepDate:" + translegvo.getDepatureDate() + ", isDichargeBound:" + isDichargeBound);
                            Sql = "select a.service, a.vessel, a.voyage, a.bound, a.port, a.arrdate, a.type, a.groupcallid,b.vendor_code,c.vendor_name, a.depdate, a.terminal from allschedules a(NOLOCK) "
                                    + "left join  feederscheduleVendors b(nolock) on a.service=b.Service and a.voyage=b.voyage and "
                                    + "a.vessel=b.vessel and a.bound=b.bound left join VendorDetails c(nolock) on    c.vendor_code=b.vendor_code "
                                    + "where (? = 'N' or a.service =?) and a.vessel=?  and (? = 'N' or a.voyage =?)"
                                    + " and (? = 'N' or a.bound=?) and a.port=? and a.arrdate > DATEADD(hh, CONVERT(int, '" + btwPortHrs + "'), '" + translegvo.getDepatureDate() + "') and (?='N' or a.terminal=?) and (?='N' or a.arrdate < DATEADD(DAY, ?,?)) order by a.arrdate asc ";
                            pst = con.prepareStatement(Sql, 1004, 1007);
                            //
                            strSerType = translegvo.getType();//typ[k];
                            if ("F".equalsIgnoreCase(strSerType)) {
                                isSvcReq = isFeederSvcReq;
                                isVoyReq = isFeederVoyReq;
                                isBoundReq = isFeederBoundReq;
                            } else {
                                isSvcReq = isMainSVCReq;
                                isVoyReq = isMainVoyReq;
                                isBoundReq = isMainBoundReq;
                            }
                            isBoundReq = ((isDichargeBound) ? "Y" : isBoundReq);
                            transtime = getTranstTime(tmpport, ntmpport, legtranshs);
                            istranstime = (transtime > 0) ? "Y" : "N";

                            pst.setString(1, isSvcReq);
                            pst.setString(2, translegvo.getService());
                            pst.setString(3, translegvo.getVessel());
                            pst.setString(4, isVoyReq);
                            pst.setString(5, translegvo.getVoyage());
                            pst.setString(6, isBoundReq);
                            pst.setString(7, translegvo.getBound());
                            pst.setString(8, ntmpport.trim());
                            pst.setString(9, strTransTerminalOnly);
                            pst.setString(10, TS3T);
                            pst.setString(11, istranstime);
                            pst.setInt(12, transtime);
                            pst.setString(13, translegvo.getDepatureDate());
                            rs = pst.executeQuery();
                            rs.last();
                            rcnt = rs.getRow();
                            rs.beforeFirst();
                            logPrinter("[TRANS2 DISCH RESULT] QueryCount:" + rcnt);
                            if (rcnt <= 0 && "Y".equalsIgnoreCase(strTransTerminalOnly)) {
                                logPrinter("[TRANS2 DISCH FALLBACK] Retrying without terminal restriction");
                                pst = con.prepareStatement(Sql, 1004, 1007);
                                pst.setString(1, isSvcReq);
                                pst.setString(2, translegvo.getService());
                                pst.setString(3, translegvo.getVessel());
                                pst.setString(4, isVoyReq);
                                pst.setString(5, translegvo.getVoyage());
                                pst.setString(6, isBoundReq);
                                pst.setString(7, translegvo.getDischargeBound());
                                pst.setString(8, ntmpport.trim());
                                pst.setString(9, "N");
                                pst.setString(10, TS3T);
                                pst.setString(11, istranstime);
                                pst.setInt(12, transtime);
                                pst.setString(13, translegvo.getDepatureDate());
                                rs = pst.executeQuery();
                                rs.last();
                                rcnt = rs.getRow();
                                rs.beforeFirst();
                                logPrinter("[TRANS2 DISCH FALLBACK] QueryCount:" + rcnt);
                            }
                            while (rs.next()) {
                                // LAYOVER VALIDATION for Trans2
                                if (isTransLayoverPortBased) {
                                    String currentLeg2Type = translegvo.getType();
                                    String actualLayover2;
                                    if ("F".equalsIgnoreCase(prevScheduledLegType) || "F".equalsIgnoreCase(currentLeg2Type)) {
                                        actualLayover2 = ffhrs;
                                    } else if (trans2LayoverConfig != null) {
                                        String nextLeg2LoadTerminal = translegvo.getTerminal();
                                        actualLayover2 = calculateTransLayoverHours(trans2LayoverConfig, 
                                                                    prevScheduledLegTerminal, nextLeg2LoadTerminal, fallbackRTH);
                                    } else {
                                        actualLayover2 = fallbackRTH;
                                    }
                                    String scheduleDepDate2 = translegvo.getDepatureDate();
                                    String scheduleArrDate2 = translegvo.getArrivalDate();
                                    
                                    if (!isScheduleLayoverValid(prevLeg2ArrDate, scheduleDepDate2, scheduleArrDate2, blt, actualLayover2)) {
                                        logPrinter("[TRANS2 LAYOVER SKIP] PrevType:" + prevScheduledLegType + ", CurrType:" + currentLeg2Type + ", PrevDisch:" + prevScheduledLegTerminal + ", NextLoad:" + translegvo.getTerminal() + ", ActualLayoverHrs:" + actualLayover2 + " - Schedule skipped");
                                        continue;
                                    }
                                    logPrinter("[TRANS2 LAYOVER VALID] PrevType:" + prevScheduledLegType + ", CurrType:" + currentLeg2Type + ", PrevDisch:" + prevScheduledLegTerminal + ", NextLoad:" + translegvo.getTerminal() + ", ActualLayoverHrs:" + actualLayover2);
                                }
                                
                                isFullRouting = true;
                                logPrinter("[TRANS2 LEG FOUND] LoadPort:" + tmpport + "=>DischPort:" + rs.getString("port") + " | Vessel:" + rs.getString("vessel") + ", Voyage:" + translegvo.getVoyage() + ", ArrDate:" + rs.getString("arrdate") + ", Type:" + rs.getString("type"));
                                service = translegvo.getService();//ser[k].trim();
                                al.add(service);
                                al.add(translegvo.getVessel());//rs.getString("vessel")
                                al.add(translegvo.getVoyage());//voy[k].trim()
                                bound = translegvo.getBound();//bnd[k].trim();
                                al.add(bound);
                                al.add(tmpport.trim());
                                al.add(translegvo.getDepatureDate());//dep[k].trim()
                                al.add(translegvo.getType());//typ[k].trim()
                                al.add(translegvo.getGroupCallId());//gcallid[k].trim()
                                Port = rs.getString("port");
                                al.add(Port);
                                al.add(rs.getString("arrdate"));
                                al.add(rs.getString("type"));
                                al.add(rs.getString("groupcallid"));
                                TempDate = rs.getString("arrdate");
                                //
                                if (rcdBnd.equalsIgnoreCase("N/A")) {
                                    al.add("Y");
                                } else {
                                    al.add("N");
                                }
                                //
                                tempDepDate = rs.getString("depdate");
                                //
                                frou.service = translegvo.getService();// ser[k].trim();
                                frou.vessel = rs.getString("vessel");
                                frou.voyage = translegvo.getVoyage();// voy[k].trim();
                                frou.bound = translegvo.getBound();// bnd[k].trim();
                                frou.dischargebound = rs.getString("bound");
                                frou.loadport = tmpport;
                                frou.ETA1 = TempDate;
                                frou.legType = rs.getString("type");
                                frou.dischargecallid = rs.getString("groupcallid");
                                frou.loadcallid = translegvo.getGroupCallId();//gcallid[k].trim();
                                frou.grpCallId = translegvo.getGroupCallId();//gcallid[k].trim();
                                frou.ETD1 = translegvo.getDepatureDate();//dep[k].trim();
                                frou.disport = Port;
                                frou.routeId = routeid;
                                frou.blpolTer = translegvo.getTerminal();//terminal[k];
                                frou.blpodTer = rs.getString("terminal");
                                frou.defRoute = defaultroute;
                                // TRANS_LAYOVER_PORT_BASED: Update stored values for next leg's layover lookup
                                prevScheduledLegService = frou.service;
                                prevScheduledLegTerminal = frou.blpodTer;
                                prevScheduledLegType = frou.legType;
                                addUsedVessel(usedVessels, frou.vessel);

                                break;
                            }
                            /* if ((!isFullRouting) && "Y".equalsIgnoreCase(strTransTerminalOnly)) {

                                frou = getAlternativeSchedule(null, isSvcReq, ser[k].trim(), vsl[k].trim(), isBoundReq, dis_bnd[k].trim(), ntmpport.trim(), dep[k], isVoyReq, voy[k].trim(), "N", TS3T, istranstime, transtime, btwPortHrs);
                                if (frou != null) {
                                    isFullRouting = frou.isIsRouting();
                                    al = new ArrayList();
                                    frou.service = ser[k].trim();
                                    frou.vessel = vsl[k].trim();
                                    frou.voyage = voy[k].trim();
                                    frou.bound = bnd[k].trim();
                                    frou.loadport = tmpport;
                                    TempDate = frou.ETA1;
                                    frou.loadcallid = gcallid[k].trim();
                                    frou.grpCallId = gcallid[k].trim();
                                    frou.ETD1 = dep[k].trim();
                                    frou.disport = ntmpport;
                                    frou.routeId = routeid;
                                    frou.blpolTer = terminal[k];
                                    frou.defRoute = defaultroute;
                                    //
                                    tempDepDate = frou.getDepdatefornexttrans();
                                    //
                                    break;
                        }

                            }*/
                            // Break outer for loop if schedule found
                            if (isFullRouting) {
                                break;
                            }
                        }

                    }
                } else {
                    // ITT (Internal Terminal Transfer) - Same port, different terminal
                    logPrinter("[TRANS2 ITT SAME-PORT] FromPort:" + tmpport + ", RTH:" + hrs + ", ITH:" + ithhrs);
                    isFullRouting = true;
                    String tmpops1date = "";
                    String tmpops2date = "";
                    tmpops1date = addHours(hrs, legformdate);
                    tmpops2date = addHours(ithhrs, tmpops1date);
                    String destPort = (TS3 != null && TS3.toString().trim().length() > 0) ? TS3.toString().trim() : POD.trim();
                    logPrinter("[TRANS2 ITT SAME-PORT] FromPort:" + tmpport + ", ToPort:" + destPort + ", ETD:" + tmpops1date + ", ETA:" + tmpops2date + ", RTH:" + hrs + ", ITH:" + ithhrs);
                    ittPrevLegArrDate = legformdate;
                    al.add("");
                    al.add("");
                    al.add("");
                    al.add("");
                    al.add(tmpport.trim());
                    al.add(tmpops1date);
                    al.add("I");
                    al.add("");
                    al.add(destPort);
                    al.add(tmpops2date);
                    al.add("I");
                    al.add("");
                    TempDate = tmpops2date.toString();
                    tempDepDate = tmpops2date.toString();
                    al.add("Y");
                    al.add("");
                    al.add("");

                    if (!(TS3 != null && TS3.toString().trim().length() > 0)) {
                        ishaulfinished = true;
                        logPrinter("[TRANS2 ITT] ishaulfinished:true - TS3 not present, route complete at ITT");
                    }

                    frou.service = "";
                    frou.vessel = "";
                    frou.voyage = "";
                    frou.bound = "";
                    frou.loadport = tmpport;
                    frou.ETA1 = tmpops2date;
                    frou.legType = "I";
                    frou.dischargecallid = "";
                    frou.loadcallid = "";
                    frou.grpCallId = "";
                    frou.ETD1 = tmpops1date;
                    frou.disport = destPort;
                    frou.routeId = routeid;
                    frou.blpolTer = TS2T;
                    frou.blpodTer = TS3T;
                    frou.defRoute = defaultroute;
                }
                hts.put("3", al);
                lstarry.add(frou);
                logPrinter("[TRANSSHIPMENT 2 END] isFullRouting:" + isFullRouting + ", LegsCount:" + lstarry.size());
            }
            if (TS3.trim().length() > 0 && TS3S.trim().length() > 0) {
                tmpport = TS3.trim();
                tmpservice = TS3S.trim();
                tmpter = TS3T;
            }
            if (POD.trim().length() > 0) {
                ntmpport = POD.trim();
                ntmpservice = TS3S.trim();
            }
            if ("Y".equalsIgnoreCase(bft)) {
                legformdate = tempDepDate;
            } else {
                legformdate = TempDate;
            }
            
            // ================ FINAL LEG: Trans3/LastPort => POD ================
            logPrinter("[FINAL LEG CHECK] isFullRouting:" + isFullRouting + ", ishaulfinished:" + ishaulfinished + ", hasFirstLeg:" + (hts != null && hts.containsKey("1")) + ", POD:" + POD + ", legformdate:" + legformdate + ", TS3T:" + TS3T + ", tmpter:" + tmpter);
            if (isFullRouting && (!ishaulfinished) && hts != null && hts.containsKey("1") && POD.trim().length() > 0 && legformdate != null && legformdate.trim().length() > 0) {
                logPrinter("[FINAL LEG START] FromPort:" + tmpport + "=>POD:" + POD + " | Service:" + tmpservice);
                isFullRouting = false;
                al = new ArrayList();
                frou = new Route();
                //
                resbound = tmpport + "~~" + ntmpport + "~~" + tmpservice;
                bcList = (List<BoundConfigVo>) defbound.get(resbound);
                AutoLegVo autoLegVo = determineStrBoundThereLoad(bcList);
                strBoundThereLoad = autoLegVo.getStrBoundThereLoad();
                boolean hasVesselVoyageInfo = autoLegVo.isHasVesselVoyageInfo();
                boundres = autoLegVo.getBoundConfig();
                logPrinter("[FINAL BOUND CONFIG] Key:" + resbound + ", strBoundThereLoad:" + strBoundThereLoad + ", hasVesselVoyageInfo:" + hasVesselVoyageInfo + ", Lbound:" + (boundres != null ? boundres.getLbound() : "null") + ", Dbound:" + (boundres != null ? boundres.getDbound() : "null"));
                
                // LAYOVER: Calculate for Final leg load
                String trans3LayoverHrs = hrs;
                LayoverTimeVo trans3LayoverConfig = null;
                String prevLeg3ArrDate = legformdate;
                if (isTransLayoverPortBased) {
                    if ("F".equalsIgnoreCase(prevScheduledLegType)) {
                        trans3LayoverHrs = ffhrs;
                        logPrinter("[FINAL LAYOVER] PrevLeg is Feeder, using FFH:" + ffhrs);
                    } else {
                        trans3LayoverConfig = getLayoverTimeConfigFromCache(layoverConfigCache, tmpport, prevScheduledLegService, prevScheduledLegTerminal, fallbackRTH);
                        trans3LayoverHrs = getInitialLayoverHoursForQuery(trans3LayoverConfig, fallbackRTH, ffhrs);
                    }
                    
                    if((checkNull(TS2).length() > 0 && checkNull(TS2).equalsIgnoreCase(checkNull(TS3)))){
                    // Check if previous leg was ITT move and adjust legformdate accordingly
                    String[] ittAdjustResult = adjustLegformdateForITTPreviousLeg(ittPrevLegArrDate, legformdate, trans3LayoverHrs);
                    legformdate = ittAdjustResult[0];
                    trans3LayoverHrs = ittAdjustResult[1];
                    prevLeg3ArrDate= ittPrevLegArrDate;
                    ittPrevLegArrDate="";
                    
                    logPrinter("[FINAL LAYOVER] Port:" + tmpport + ", PrevService:" + prevScheduledLegService + ", PrevTerminal:" + prevScheduledLegTerminal + ", PrevType:" + prevScheduledLegType + ", PrevArrDate:" + prevLeg3ArrDate + ", LayoverHrs:" + trans3LayoverHrs);
                    }
                }
                logPrinter("[FINAL LOAD QUERY] Service:" + tmpservice + ", Port:" + tmpport + ", BoundFilter:" + strBoundThereLoad + ", Lbound:" + boundres.getLbound() + ", BLT:" + blt + ", LegFormDate:" + legformdate + ", TerminalOnly:" + strTransTerminalOnly + ", Terminal:" + tmpter);
                Sql = "select service, vessel, voyage, bound, port, depdate, arrdate, type, groupcallid, terminal from allschedules (NOLOCK) where service=? "
                        + " and port=? and ('N'=? or bound=?) and ('Y'=? or depdate > DATEADD(hh, CONVERT(int, '" + trans3LayoverHrs + "'), ? )) and ('N'=? or arrdate > DATEADD(hh, CONVERT(int, '" + trans3LayoverHrs + "'), ? )) and (?='N' or terminal=?) order by arrdate ";
                pst = con.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                pst.setString(1, tmpservice);
                pst.setString(2, tmpport);
                pst.setString(3, strBoundThereLoad);
                pst.setString(4, boundres.getLbound());
                pst.setString(5, blt);
                pst.setString(6, legformdate);
                pst.setString(7, blt);
                pst.setString(8, legformdate);
                pst.setString(9, strTransTerminalOnly);
                pst.setString(10, tmpter);
                rs = pst.executeQuery();
                rs.last();
                int rcnt = rs.getRow();
                rs.beforeFirst();
                logPrinter("[FINAL LOAD RESULT] QueryCount:" + rcnt);
                translis = setTransLoadLegDetails(rs, null, boundres, bcList, hasVesselVoyageInfo);
                if ("Y".equalsIgnoreCase(strTransTerminalOnly)) {
                    pst = con.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    pst.setString(1, tmpservice);
                    pst.setString(2, tmpport);
                    pst.setString(3, strBoundThereLoad);
                    pst.setString(4, boundres.getLbound());
                    pst.setString(5, blt);
                    pst.setString(6, legformdate);
                    pst.setString(7, blt);
                    pst.setString(8, legformdate);
                    pst.setString(9, "N");
                    pst.setString(10, tmpter);
                    rs = pst.executeQuery();
                    rs.last();
                    rcnt = rs.getRow();
                    rs.beforeFirst();
                    logPrinter("[FINAL LOAD FALLBACK] QueryCount:" + rcnt);
                    translis = setTransLoadLegDetails(rs, translis, boundres, bcList, hasVesselVoyageInfo);
                }
                logPrinter("[FINAL LOAD SCHEDULES] TotalSchedules:" + (translis != null ? translis.size() : 0));

                // Query POD arrival for each load schedule
                for (int k = 0; k < translis.size(); k++) {
                    translegvo = translis.get(k);
                    // TRANS_SAME_VESSEL_SKIP: This candidate's POD arrival leg would run on the same
                    // vessel as the previous leg - skip it and try the next available candidate with a
                    // different vessel instead of forming a same-vessel connection.
                    if (isRemoveSameVessel && isVesselAlreadyUsed(usedVessels, translegvo.getVessel())) {
                        logPrinter("[FINAL SAME VESSEL SKIP] Candidate:" + k + ", Vessel:" + translegvo.getVessel() + " already used on prior leg - trying next schedule");
                        continue;
                    }
                    boundres = translegvo.getBoundConfig();
                    isDichargeBound = false;
                    if (boundres != null && (!"N/A".equalsIgnoreCase(boundres.getDbound()))) {
                        isDichargeBound = true;
                    }
                    logPrinter("[FINAL POD QUERY " + k + "/" + translis.size() + "] LoadPort:" + tmpport + ", POD:" + POD + " | Vessel:" + translegvo.getVessel() + ", Voyage:" + translegvo.getVoyage() + ", Service:" + translegvo.getService() + ", Type:" + translegvo.getType() + ", DepDate:" + translegvo.getDepatureDate() + ", isDichargeBound:" + isDichargeBound);
                    Sql = "select a.service, a.vessel, a.voyage, a.bound, a.port,a.terminal, a.arrdate, a.type, a.groupcallid,b.vendor_code,c.vendor_name from allschedules a(NOLOCK) "
                            + "left join  feederscheduleVendors b(nolock) on a.service=b.Service and a.voyage=b.voyage and "
                            + "a.vessel=b.vessel and a.bound=b.bound left join VendorDetails c(nolock) on    c.vendor_code=b.vendor_code "
                            + " where (? = 'N' or a.service=?) and a.vessel=? and (? = 'N' or a.voyage =?)"
                            + " and (? = 'N' or a.bound=?) and a.port=? and a.arrdate > DATEADD(hh, CONVERT(int, '" + btwPortHrs + "'), '" + translegvo.getDepatureDate() + "') and (?='N' or a.terminal=?)  and (?='N' or a.arrdate < DATEADD(DAY, ?,?)) order by a.arrdate asc";

                    pst = con.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    //
                    strSerType = translegvo.getType();//typ[k];
                    if ("F".equalsIgnoreCase(strSerType)) {
                        isSvcReq = isFeederSvcReq;
                        isVoyReq = isFeederVoyReq;
                        isBoundReq = isFeederBoundReq;

                    } else {
                        isSvcReq = isMainSVCReq;
                        isVoyReq = isMainVoyReq;
                        isBoundReq = isMainBoundReq;
                    }
                    isBoundReq = ((isDichargeBound) ? "Y" : isBoundReq);
                    transtime = getTranstTime(tmpport, POD, legtranshs);
                    istranstime = (transtime > 0) ? "Y" : "N";
                    
                    pst.setString(1, isSvcReq);
                    pst.setString(2, translegvo.getService());
                    pst.setString(3, translegvo.getVessel());
                    pst.setString(4, isVoyReq);
                    pst.setString(5, translegvo.getVoyage());
                    pst.setString(6, isBoundReq);
                    pst.setString(7, translegvo.getDischargeBound());
                    pst.setString(8, POD.trim());
                    pst.setString(9, "Y");
                    pst.setString(10, podter);
                    pst.setString(11, istranstime);
                    pst.setInt(12, transtime);
                    pst.setString(13, translegvo.getDepatureDate());
                    rs = pst.executeQuery();
                    rs.last();
                    rcnt = rs.getRow();
                    rs.beforeFirst();
                    logPrinter("[FINAL POD RESULT] QueryCount:" + rcnt + ", TransitDays:" + transtime + ", Restrictions(SVC,VOY,BND):" + isSvcReq + "," + isVoyReq + "," + isBoundReq);
                    if (rcnt <= 0 && "N".equalsIgnoreCase(strPODTerminalOnly)) {
                        logPrinter("[FINAL POD FALLBACK] Retrying without terminal restriction");
                        pst = con.prepareStatement(Sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        pst.setString(1, isSvcReq);
                        pst.setString(2, translegvo.getService());
                        pst.setString(3, translegvo.getVessel());
                        pst.setString(4, isVoyReq);
                        pst.setString(5, translegvo.getVoyage());
                        pst.setString(6, isBoundReq);
                        pst.setString(7, translegvo.getDischargeBound());
                        pst.setString(8, POD.trim());
                        pst.setString(9, strPODTerminalOnly);
                        pst.setString(10, podter);
                        pst.setString(11, istranstime);
                        pst.setInt(12, transtime);
                        pst.setString(13, translegvo.getDepatureDate());
                        rs = pst.executeQuery();
                        rs.last();
                        rcnt = rs.getRow();
                        rs.beforeFirst();
                        logPrinter("[FINAL POD FALLBACK] QueryCount:" + rcnt);
                    }
                    while (rs.next()) {
                        // LAYOVER VALIDATION for Final Leg
                        if (isTransLayoverPortBased) {
                            String currentLeg3Type = translegvo.getType();
                            String actualLayover3;
                            if ("F".equalsIgnoreCase(prevScheduledLegType) || "F".equalsIgnoreCase(currentLeg3Type)) {
                                actualLayover3 = ffhrs;
                            } else if (trans3LayoverConfig != null) {
                                String nextLeg3LoadTerminal = translegvo.getTerminal();
                                actualLayover3 = calculateTransLayoverHours(trans3LayoverConfig, 
                                                            prevScheduledLegTerminal, nextLeg3LoadTerminal, fallbackRTH);
                            } else {
                                actualLayover3 = fallbackRTH;
                            }
                            String scheduleDepDate3 = translegvo.getDepatureDate();
                            String scheduleArrDate3 = translegvo.getArrivalDate();
                            
                            if (!isScheduleLayoverValid(prevLeg3ArrDate, scheduleDepDate3, scheduleArrDate3, blt, actualLayover3)) {
                                logPrinter("[FINAL LAYOVER SKIP] PrevType:" + prevScheduledLegType + ", CurrType:" + currentLeg3Type + ", PrevDisch:" + prevScheduledLegTerminal + ", NextLoad:" + translegvo.getTerminal() + ", ActualLayoverHrs:" + actualLayover3 + " - Schedule skipped");
                                continue;
                            }
                            logPrinter("[FINAL LAYOVER VALID] PrevType:" + prevScheduledLegType + ", CurrType:" + currentLeg3Type + ", PrevDisch:" + prevScheduledLegTerminal + ", NextLoad:" + translegvo.getTerminal() + ", ActualLayoverHrs:" + actualLayover3);
                        }
                        
                        isFullRouting = true;
                        logPrinter("[FINAL LEG FOUND] LoadPort:" + tmpport + "=>POD:" + rs.getString("port") + " | Vessel:" + rs.getString("vessel") + ", Voyage:" + translegvo.getVoyage() + ", ArrDate:" + rs.getString("arrdate") + ", Type:" + rs.getString("type") + ", Terminal:" + rs.getString("terminal"));
                        service = translegvo.getService();//ser[k].trim();
                        al.add(service);
                        al.add(rs.getString("vessel"));
                        al.add(translegvo.getVoyage());//voy[k].trim()
                        bound = translegvo.getBound();//bnd[k].trim();
                        al.add(bound);
                        al.add(tmpport.trim());
                        al.add(translegvo.getDepatureDate());// dep[k].trim()
                        al.add(translegvo.getType());// typ[k].trim()
                        al.add(translegvo.getGroupCallId());// gcallid[k].trim()
                        Port = rs.getString("port");
                        al.add(Port);
                        al.add(rs.getString("arrdate"));
                        al.add(rs.getString("type"));
                        al.add(rs.getString("groupcallid"));
                        TempDate = rs.getString("arrdate");
                        //
                        if (rcdBnd.equalsIgnoreCase("N/A")) {
                            al.add("Y");
                        } else {
                            al.add("N");
                        }
                        frou.service = translegvo.getService();//ser[k].trim();
                        frou.vessel = rs.getString("vessel");
                        frou.voyage = translegvo.getVoyage();//voy[k].trim();
                        frou.bound = translegvo.getBound();//bnd[k].trim();
                        frou.dischargebound = rs.getString("bound");
                        frou.loadport = tmpport;
                        frou.ETA1 = TempDate;
                        frou.legType = rs.getString("type");
                        frou.dischargecallid = rs.getString("groupcallid");
                        frou.loadcallid = translegvo.getGroupCallId();//gcallid[k].trim();
                        frou.grpCallId = translegvo.getGroupCallId(); //gcallid[k].trim();
                        frou.ETD1 = translegvo.getDepatureDate();//dep[k].trim();
                        frou.disport = Port;
                        frou.routeId = routeid;
                        frou.blpolTer = translegvo.getTerminal();//terminal[k];
                        frou.blpodTer = checkNull(rs.getString("terminal"));
                        frou.defRoute = defaultroute;

                        break;
                    }
                    /*if ((!isFullRouting) && "N".equalsIgnoreCase(strPODTerminalOnly)) {

                        frou = getAlternativeSchedule(null, isSvcReq, ser[k].trim(), vsl[k].trim(), isBoundReq, dis_bnd[k].trim(), POD, dep[k], isVoyReq, voy[k].trim(), "N", podter, istranstime, transtime, btwPortHrs);
                        if (frou != null) {
                            isFullRouting = frou.isIsRouting();
                            al = new ArrayList();
                            frou.service = ser[k].trim();
                            frou.vessel = vsl[k].trim();
                            frou.voyage = voy[k].trim();
                            frou.bound = bnd[k].trim();
                            frou.loadport = tmpport;
                            TempDate = frou.ETA1;
                            frou.loadcallid = gcallid[k].trim();
                            frou.grpCallId = gcallid[k].trim();
                            frou.ETD1 = dep[k].trim();
                            frou.disport = POD;
                            frou.routeId = routeid;
                            frou.blpolTer = terminal[k];
                            frou.defRoute = defaultroute;
                            //
                            tempDepDate = frou.getDepdatefornexttrans();
                            //
                            break;
                }

                    }*/
                    // Break outer for loop if schedule found
                    if (isFullRouting) {
                        break;
                    }
                }
                hts.put("4", al);
                lstarry.add(frou);
                logPrinter("[FINAL LEG END] isFullRouting:" + isFullRouting + ", LegsCount:" + lstarry.size());
            }
            if (!isFullRouting) {
                logPrinter("[GetAutoLeg INCOMPLETE] isFullRouting:false - Route not fully formed");
                lstarry = new ArrayList();
            } else {
                logPrinter("[GetAutoLeg COMPLETE] TotalLegs:" + lstarry.size());
            }
        } catch (Exception e) {
            logPrinter("[GetAutoLeg ERROR] Exception:" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    pst.close();
                    con.close();
                    rs = null;
                }
            } catch (Exception ex) {
                logPrinter("[GetAutoLeg CLEANUP] Database connection closed");
            }
        }
        logPrinter("[GetAutoLeg END] FinalLegCount:" + lstarry.size());
        return lstarry;
    }

    public ArrayList getEbookingDetails(String bookNo, String flag, String action) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        ResultSet rst = null;
        String stat = null;
        String book_Number = null;
        Connection con111 = null;
        PreparedStatement ps111 = null;
        ResultSet rs111 = null;
        ArrayList masterlist = new ArrayList();
        EbookingValidatorFormBeanNew eBookingmaster = null;
        Agencydto agencyVo = new Agencydto();
        String phone = null, fax = null;
        try {
            System.out.println("[ECOM] EcomBooingBean >>bookNo>>" + bookNo);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_BOOKING_MASTEREDIT);
            ps.setString(1, checkNull(bookNo));
            rs = ps.executeQuery();
            while (rs.next()) {
                eBookingmaster = new EbookingValidatorFormBeanNew();
                eBookingmaster.setBooking_refno(bookNo.trim());
                eBookingmaster.setEbook_date(rs.getString("bookDate"));
                eBookingmaster.setEbook_dateBut(rs.getString("bookDate"));
                eBookingmaster.setEmptyPickup(rs.getString("emptypickup"));
                eBookingmaster.setPodintransit(checkNull(rs.getString("intransitport")));
                eBookingmaster.setEbook_templatename(checkNull(rs.getString("templatename")));
                eBookingmaster.setEmptyPickUpDate(rs.getString("emptypickupdate"));
                eBookingmaster.setAmendment(checkNull(rs.getString("isamendment")));
                eBookingmaster.setEbook_portofloading(checkNull(rs.getString("loadPortId")));
                eBookingmaster.setEbook_portofdischarge(checkNull(rs.getString("dischargePortId")));
                eBookingmaster.setEbook_bookagency(checkNull(rs.getString("agencyid")));
                agencyVo = getAgencyEmail(eBookingmaster.getEbook_bookagency().trim(), "view");
                eBookingmaster.setEbook_bookagency_desc(agencyVo.getAgencyname());
                eBookingmaster.setEbook_ocean_freight(checkNull(rs.getString("oceanfreight")));
                eBookingmaster.setCarrierContact(checkNull(rs.getString("trptsvccode")));
                eBookingmaster.setPreCarriage(checkNull(rs.getString("preCarriage")));
                eBookingmaster.setOnCarriage(checkNull(rs.getString("onCarriage")));
                eBookingmaster.setEnsfiling(checkNull(rs.getString("ensfiling")));
                eBookingmaster.setEbook_origin(checkNull(rs.getString("originportid")));
                eBookingmaster.setEbook_pol(checkNull(rs.getString("loadPortId")));
                eBookingmaster.setEbook_pod(checkNull(rs.getString("dischargePortId")));
                eBookingmaster.setEbook_delivery(checkNull(rs.getString("finalPortId")));
                eBookingmaster.setEbook_userstatus(checkNull(rs.getString("ebook_status")));
                eBookingmaster.setEbook_status_remarks(checkNull(rs.getString("remarks")));
                eBookingmaster.setRemarks(checkNull(rs.getString("remarks")));
                eBookingmaster.setEbook_rate_reference(checkNull(rs.getString("rateno")));
                eBookingmaster.setEbook_routeid(checkNull(rs.getString("routeId")));
                eBookingmaster.setEbook_agency_reference(checkNullUpperCase(rs.getString("AgencyRefNo")));
                eBookingmaster.setEbook_natcode(checkNull(rs.getString("nat_code")));
                eBookingmaster.setEbook_pickup_point(rs.getString("pickup_point"));
                eBookingmaster.setSalesRateAgree(checkNull(rs.getString("salesagreedrate")));
                eBookingmaster.setCcMailId(checkNull(rs.getString("ccmailid")));
                eBookingmaster.setEbook_haulercode(checkNull(rs.getString("haulercode")));
           //     System.out.println("[ECOM] EcomBooingBean >>rs.getString(\"haulagetype\")>>>" + rs.getString("haulagetype"));
                eBookingmaster.setEbook_haulagetype(checkNull(rs.getString("haulagetype")));
                eBookingmaster.setEbook_pickup_date(checkNull(rs.getString("pickupdate")));
                eBookingmaster.setEbook_drop_date(checkNull(rs.getString("dropdate")));
                eBookingmaster.setEbook_exportref(checkNull(rs.getString("exportRef")));
                eBookingmaster.setEbook_reftype(checkNull(rs.getString("releasetype")));
                eBookingmaster.setEbook_custpo(checkNull(rs.getString("custPO")));
                eBookingmaster.setCustomerreference(checkNull(rs.getString("customerreference")));
                eBookingmaster.setACID(checkNull(rs.getString("ACID")));
                eBookingmaster.setCargoprotectid(rs.getString("cargoprotectid"));
                String shipperReference = checkNull(rs.getString("ShipperReference"));
                if ("nullnull".equalsIgnoreCase(shipperReference)) {
                    shipperReference = "";
                }
                if (shipperReference.length() > 0) {
                    eBookingmaster.setShipperReferenceType(checkNull(shipperReference.substring(0, 1)));
                    eBookingmaster.setShipperReference(checkNull(shipperReference.substring(1)));
                } else {
                    eBookingmaster.setShipperReferenceType("");
                    eBookingmaster.setShipperReference("");
                }

                eBookingmaster.setEbook_preferred_agency(rs.getString("agencyid"));
                eBookingmaster.setEbook_t2lfiling(rs.getString("t2lfiling"));
                eBookingmaster.setEbook_sisubmit_party(rs.getString("sisubmitparty"));
                eBookingmaster.setEbook_EnsDocType(rs.getString("enstype"));

                if ("edit".equalsIgnoreCase(action)) {
                    ps = con.prepareStatement(GET_BOOKING_CUSTEDIT);
                } else if ("template".equalsIgnoreCase(action)) {
                    ps = con.prepareStatement(GET_BOOKING_CUSTTEMPLATE);
                } else {
                    ps = con.prepareStatement(GET_BOOKING_CUSTVIEW);
                }

                ps.setString(1, bookNo);
                if ("template".equalsIgnoreCase(action)) {
                    ps.setString(2, bookNo);
                }
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (checkNull(rs.getString("custTypeId")).equalsIgnoreCase("BP")) {
                        eBookingmaster.setEbook_bookingparty_desc(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_bookingcity(checkNull(rs.getString("city")));
                        eBookingmaster.setEbook_bookingcity_desc(checkNull(rs.getString("city")));
                        eBookingmaster.setEbook_bookingcountry(checkNull(rs.getString("countryId")));
                        eBookingmaster.setEbook_bookingname(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_bookingcode(checkNull(rs.getString("custId")));
                        eBookingmaster.setEbook_bookingaddress(checkNull(rs.getString("address1")));
                        eBookingmaster.setEbook_bookingaddress1(checkNull(rs.getString("address2")));
                        eBookingmaster.setEbook_bookingemail(checkNull(rs.getString("email")));
                        eBookingmaster.setEbook_bookingstate(checkNull(rs.getString("stateid")));
                        eBookingmaster.setEbook_bookingzipcode(checkNull(rs.getString("zipcode")));
                        eBookingmaster.setEbook_bookingeori(checkNull(rs.getString("eori")));
                        eBookingmaster.setBookingtypeperson(checkNull(rs.getString("persontype")));
                        phone = checkNull(rs.getString("telephone"));
                        fax = checkNull(rs.getString("fax"));
                        if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_bookingphone(phone);
                        } else {
                            eBookingmaster.setEbook_bookingphone("");
                        }
                        if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_bookingfax(fax);
                        } else {
                            eBookingmaster.setEbook_bookingfax("");
                        }
                    }
                    if (checkNull(rs.getString("custTypeId")).equalsIgnoreCase("AP")) {
                        eBookingmaster.setEbook_agreecity(checkNull(rs.getString("city")));
                        if (checkNull(rs.getString("city")).equalsIgnoreCase("NA")) {
                            eBookingmaster.setEbook_agreecity_desc("No Cities Available");
                        }
                        eBookingmaster.setEbook_agreecountry(checkNull(rs.getString("countryId")));
                        eBookingmaster.setEbook_agreename(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_agreecode(checkNull(rs.getString("custId")));
                        eBookingmaster.setEbook_agreeaddress(checkNull(rs.getString("address1")));
                        eBookingmaster.setEbook_agreeaddress1(checkNull(rs.getString("address2")));
                        eBookingmaster.setEbook_agreeemail(checkNull(rs.getString("email")));
                        eBookingmaster.setEbook_agreestate(checkNull(rs.getString("stateid")));
                        eBookingmaster.setEbook_agree_id(checkNull(rs.getString("cid")));
                        eBookingmaster.setEbook_agreeeori(checkNull(rs.getString("eori")));
                        eBookingmaster.setEbook_agreezipcode(checkNull(rs.getString("zipcode")));
                        eBookingmaster.setAgreetypeperson(checkNull(rs.getString("persontype")));
                        phone = checkNull(rs.getString("telephone"));
                        fax = checkNull(rs.getString("fax"));
                        if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_agreephone(phone);
                        } else {
                            eBookingmaster.setEbook_agreephone("");
                        }
                        if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_agreefax(fax);
                        } else {
                            eBookingmaster.setEbook_agreefax("");
                        }
                    }
                    if (checkNull(rs.getString("custTypeId")).equalsIgnoreCase("SH")) {
                        eBookingmaster.setEbook_shippercity(checkNull(rs.getString("city")));
                        if (checkNull(rs.getString("city")).equalsIgnoreCase("NA")) {
                            eBookingmaster.setEbook_shippercity_desc("No Cities Available");
                        }
                        eBookingmaster.setEbook_shippercountry(checkNull(rs.getString("countryId")));
                        eBookingmaster.setEbook_shippername(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_shippercode(checkNull(rs.getString("custId")));
                        eBookingmaster.setEbook_shipperaddress(checkNull(rs.getString("address1")));
                        eBookingmaster.setEbook_shipperaddress1(checkNull(rs.getString("address2")));
                        eBookingmaster.setEbook_shipperemail(checkNull(rs.getString("email")));
                        eBookingmaster.setEbook_shipperstate(checkNull(rs.getString("stateid")));
                        eBookingmaster.setEbook_shipper_id(checkNull(rs.getString("cid")));
                        eBookingmaster.setEbook_shippereori(checkNull(rs.getString("eori")));
                        eBookingmaster.setEbook_shipperzipcode(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setShippertypeperson(checkNull(rs.getString("persontype")));
                        eBookingmaster.setShipperCheck(checkNull(rs.getString("flag")));
                        phone = checkNull(rs.getString("telephone"));
                        fax = checkNull(rs.getString("fax"));
                        if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_shipperphone(phone);
                        } else {
                            eBookingmaster.setEbook_shipperphone("");
                        }
                        if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_shipperfax(fax);
                        } else {
                            eBookingmaster.setEbook_shipperfax("");
                        }

                    }
                    if (checkNull(rs.getString("custTypeId")).equalsIgnoreCase("CN")) {
                        eBookingmaster.setEbook_consigneecity(checkNull(rs.getString("city")));
                        if (checkNull(rs.getString("city")).equalsIgnoreCase("NA")) {
                            eBookingmaster.setEbook_consigneecity_desc("No Cities Available");
                        }
                        eBookingmaster.setEbook_consigneecountry(checkNull(rs.getString("countryId")));
                        eBookingmaster.setEbook_consigneename(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_consigneecode(checkNull(rs.getString("custId")));
                        eBookingmaster.setEbook_consigneeaddress(checkNull(rs.getString("address1")));
                        eBookingmaster.setEbook_consigneeaddress1(checkNull(rs.getString("address2")));
                        eBookingmaster.setEbook_consigneeemail(checkNull(rs.getString("email")));
                        eBookingmaster.setEbook_consigneestate(checkNull(rs.getString("stateid")));
                        eBookingmaster.setEbook_consigne_id((checkNull(rs.getString("cid"))));
                        eBookingmaster.setEbook_consigneezipcode(checkNull(rs.getString("zipcode")));
                        eBookingmaster.setHidCNToOrder(checkNull(rs.getString("toorder")));
                        eBookingmaster.setEbook_consigneeeori(checkNull(rs.getString("eori")));
                        eBookingmaster.setConsigneetypeperson(checkNull(rs.getString("persontype")));
                        phone = checkNull(rs.getString("telephone"));
                        fax = checkNull(rs.getString("fax"));
                        if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_consigneephone(phone);
                        } else {
                            eBookingmaster.setEbook_consigneephone("");
                        }
                        if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_consigneefax(fax);
                        } else {
                            eBookingmaster.setEbook_consigneefax("");
                        }
                    }
                    if (checkNull(rs.getString("custTypeId")).equalsIgnoreCase("NT")) {
                        eBookingmaster.setEbook_notifycity(checkNull(rs.getString("city")));
                        if (checkNull(rs.getString("city")).equalsIgnoreCase("NA")) {
                            eBookingmaster.setEbook_notifycity_desc("No Cities Available");
                        }
                        eBookingmaster.setEbook_notifycountry(checkNull(rs.getString("countryId")));
                        eBookingmaster.setEbook_notifyname(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_notifycode(checkNull(rs.getString("custId")));
                        eBookingmaster.setEbook_notifyaddress(checkNull(rs.getString("address1")));
                        eBookingmaster.setEbook_notifyaddress1(checkNull(rs.getString("address2")));
                        eBookingmaster.setEbook_notifyemail(checkNull(rs.getString("email")));
                        eBookingmaster.setEbook_notifystate(checkNull(rs.getString("stateid")));
                        eBookingmaster.setEbook_notify_id((checkNull(rs.getString("cid"))));
                        eBookingmaster.setNotifyCheck(checkNull(rs.getString("flag")));
                        eBookingmaster.setEbook_notifyeori(checkNull(rs.getString("eori")));
                        eBookingmaster.setEbook_notifyzipcode(checkNull(rs.getString("zipcode")));
                        eBookingmaster.setNotifytypeperson(checkNull(rs.getString("persontype")));
                        eBookingmaster.setHidNTToOrder(checkNull(rs.getString("toorder")));
                        phone = checkNull(rs.getString("telephone"));
                        fax = checkNull(rs.getString("fax"));
                        if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_notifyphone(phone);
                        } else {
                            eBookingmaster.setEbook_notifyphone("");
                        }
                        if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_notifyfax(fax);
                        } else {
                            eBookingmaster.setEbook_notifyfax("");
                        }
                    }

                    if (checkNull(rs.getString("custTypeId")).equalsIgnoreCase("N1")) {
                        eBookingmaster.setEbook_notifycity_n1(checkNull(rs.getString("city")));
                        if (checkNull(rs.getString("city")).equalsIgnoreCase("NA")) {
                            eBookingmaster.setEbook_notifycity_desc_n1("No Cities Available");
                        }
                        eBookingmaster.setEbook_notifycountry_n1(checkNull(rs.getString("countryId")));
                        eBookingmaster.setEbook_notifyname_n1(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_notifycode_n1(checkNull(rs.getString("custId")));
                        eBookingmaster.setEbook_notifyaddress_n1(checkNull(rs.getString("address1")));
                        eBookingmaster.setEbook_notifyaddress1_n1(checkNull(rs.getString("address2")));
                        eBookingmaster.setEbook_notifyemail_n1(checkNull(rs.getString("email")));
                        eBookingmaster.setEbook_notifystate_n1(checkNull(rs.getString("stateid")));
                        eBookingmaster.setEbook_notify_id_n1((checkNull(rs.getString("cid"))));
                        eBookingmaster.setHidNT1ToOrder(checkNull(rs.getString("toorder")));
                        eBookingmaster.setEbook_notifyeori_n1(checkNull(rs.getString("eori")));
                        eBookingmaster.setEbook_notifyzipcode_n1(checkNull(rs.getString("zipcode")));
                        eBookingmaster.setNotifytypeperson_n1(checkNull(rs.getString("persontype")));
                        eBookingmaster.setNotify1Check(checkNull(rs.getString("flag")));
                        phone = checkNull(rs.getString("telephone"));
                        fax = checkNull(rs.getString("fax"));
                        if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_notifyphone_n1(phone);
                        } else {
                            eBookingmaster.setEbook_notifyphone_n1("");
                        }
                        if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_notifyfax_n1(fax);
                        } else {
                            eBookingmaster.setEbook_notifyfax_n1("");
                        }
                    }
                    //FW
                    if (checkNull(rs.getString("custTypeId")).equalsIgnoreCase("FW")) {
                        eBookingmaster.setEbook_forwardercity(checkNull(rs.getString("city")));
                        if (checkNull(rs.getString("city")).equalsIgnoreCase("NA")) {
                            eBookingmaster.setEbook_forwardercity_desc("No Cities Available");
                        }
                        eBookingmaster.setEbook_forwardercountry(checkNull(rs.getString("countryId")));
                        eBookingmaster.setEbook_forwardername(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_forwardercode(checkNull(rs.getString("custId")));
                        eBookingmaster.setEbook_forwarderaddress(checkNull(rs.getString("address1")));
                        eBookingmaster.setEbook_forwarderaddress1(checkNull(rs.getString("address2")));
                        eBookingmaster.setEbook_forwarderemail(checkNull(rs.getString("email")));
                        eBookingmaster.setEbook_forwarderstate(checkNull(rs.getString("stateid")));
                        eBookingmaster.setEbook_forwarder_id(checkNull(rs.getString("cid")));
                        eBookingmaster.setEbook_forwardereori(checkNull(rs.getString("eori")));
                        eBookingmaster.setEbook_forwarderzipcode(checkNull(rs.getString("zipcode")));
                        eBookingmaster.setForwardertypeperson(checkNull(rs.getString("persontype")));
                        // eBookingmaster.setforwarderCheck(checkNull(rs.getString("flag")));
                        phone = checkNull(rs.getString("telephone"));
                        fax = checkNull(rs.getString("fax"));
                        if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_forwarderphone(phone);
                        } else {
                            eBookingmaster.setEbook_forwarderphone("");
                        }
                        if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_forwarderfax(fax);
                        } else {
                            eBookingmaster.setEbook_forwarderfax("");
                        }

                    }
                    //WH
                    if (checkNull(rs.getString("custTypeId")).equalsIgnoreCase("WH")) {
                        eBookingmaster.setEbook_Warehousecity(checkNull(rs.getString("city")));
                        if (checkNull(rs.getString("city")).equalsIgnoreCase("NA")) {
                            eBookingmaster.setEbook_Warehousecity_desc("No Cities Available");
                        }
                        eBookingmaster.setEbook_Warehousecountry(checkNull(rs.getString("countryId")));
                        eBookingmaster.setEbook_Warehousename(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_Warehousecode(checkNull(rs.getString("custId")));
                        eBookingmaster.setEbook_Warehouseaddress(checkNull(rs.getString("address1")));
                        eBookingmaster.setEbook_Warehouseaddress1(checkNull(rs.getString("address2")));
                        eBookingmaster.setEbook_Warehouseemail(checkNull(rs.getString("email")));
                        eBookingmaster.setEbook_Warehousestate(checkNull(rs.getString("stateid")));
                        eBookingmaster.setEbook_Warehouse_id(checkNull(rs.getString("cid")));
                        eBookingmaster.setEbook_Warehouseeori(checkNull(rs.getString("eori")));
                        eBookingmaster.setEbook_Warehousezipcode(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setWarehousetypeperson(checkNull(rs.getString("persontype")));
                        // eBookingmaster.setforwarderCheck(checkNull(rs.getString("flag")));
                        phone = checkNull(rs.getString("telephone"));
                        fax = checkNull(rs.getString("fax"));
                        if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_Warehousephone(phone);
                        } else {
                            eBookingmaster.setEbook_Warehousephone("");
                        }
                        if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_Warehousefax(fax);
                        } else {
                            eBookingmaster.setEbook_Warehousefax("");
                        }

                    }
                    if (checkNull(rs.getString("custTypeId")).equalsIgnoreCase("NC")) {
                        eBookingmaster.setEbook_NamedCustomercity(checkNull(rs.getString("city")));
                        if (checkNull(rs.getString("city")).equalsIgnoreCase("NA")) {
                            eBookingmaster.setEbook_NamedCustomercity_desc("No Cities Available");
                        }
                        eBookingmaster.setEbook_NamedCustomercountry(checkNull(rs.getString("countryId")));
                        eBookingmaster.setEbook_NamedCustomername(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_NamedCustomercode(checkNull(rs.getString("custId")));
                        eBookingmaster.setEbook_NamedCustomeraddress(checkNull(rs.getString("address1")));
                        eBookingmaster.setEbook_NamedCustomeraddress1(checkNull(rs.getString("address2")));
                        eBookingmaster.setEbook_NamedCustomeremail(checkNull(rs.getString("email")));
                        eBookingmaster.setEbook_NamedCustomerstate(checkNull(rs.getString("stateid")));
                        eBookingmaster.setEbook_NamedCustomer_id(checkNull(rs.getString("cid")));
                        eBookingmaster.setEbook_NamedCustomereori(checkNull(rs.getString("eori")));
                        eBookingmaster.setEbook_NamedCustomerzipcode(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setEbook_NamedCustomerpersontype(checkNull(rs.getString("persontype")));
                        // eBookingmaster.setforwarderCheck(checkNull(rs.getString("flag")));
                        phone = checkNull(rs.getString("telephone"));
                        fax = checkNull(rs.getString("fax"));
                        if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_NamedCustomerphone(phone);
                        } else {
                            eBookingmaster.setEbook_NamedCustomerphone("");
                        }
                        if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_NamedCustomerfax(fax);
                        } else {
                            eBookingmaster.setEbook_NamedCustomerfax("");
                        }

                    }
                    //Seller
                    if (checkNull(rs.getString("custTypeId")).equalsIgnoreCase("SL")) {
                        eBookingmaster.setEbook_Sellercity(checkNull(rs.getString("city")));
                        if (checkNull(rs.getString("city")).equalsIgnoreCase("NA")) {
                            eBookingmaster.setEbook_Sellercity_desc("No Cities Available");
                        }
                        eBookingmaster.setEbook_Sellercountry(checkNull(rs.getString("countryId")));
                        System.out.println("[ECOM] EcomBooingBean Seller==>" + eBookingmaster.getEbook_Sellercountry());
                        eBookingmaster.setEbook_Sellername(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_Sellercode(checkNull(rs.getString("custId")));
                        eBookingmaster.setEbook_Selleraddress(checkNull(rs.getString("address1")));
                        eBookingmaster.setEbook_Selleraddress1(checkNull(rs.getString("address2")));
                        eBookingmaster.setEbook_Selleremail(checkNull(rs.getString("email")));
                        eBookingmaster.setEbook_Sellerstate(checkNull(rs.getString("stateid")));
                        eBookingmaster.setEbook_Seller_id(checkNull(rs.getString("cid")));
                        eBookingmaster.setEbook_Sellereori(checkNull(rs.getString("eori")));
                        eBookingmaster.setEbook_Sellerzipcode(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setEbook_Sellerpersontype(checkNull(rs.getString("persontype")));
                        // eBookingmaster.setforwarderCheck(checkNull(rs.getString("flag")));
                        phone = checkNull(rs.getString("telephone"));
                        fax = checkNull(rs.getString("fax"));
                        if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_Sellerphone(phone);
                        } else {
                            eBookingmaster.setEbook_Sellerphone("");
                        }
                        if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_Sellerfax(fax);
                        } else {
                            eBookingmaster.setEbook_Sellerfax("");
                        }

                    }
                    //Buyer
                    if (checkNull(rs.getString("custTypeId")).equalsIgnoreCase("BY")) {
                        eBookingmaster.setEbook_Buyercity(checkNull(rs.getString("city")));
                        if (checkNull(rs.getString("city")).equalsIgnoreCase("NA")) {
                            eBookingmaster.setEbook_Buyercity_desc("No Cities Available");
                        }
                        eBookingmaster.setEbook_Buyercountry(checkNull(rs.getString("countryId")));
                        System.out.println("[ECOM] EcomBooingBean Buyer==>" + eBookingmaster.getEbook_Buyercountry());
                        eBookingmaster.setEbook_Buyername(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_Buyercode(checkNull(rs.getString("custId")));
                        eBookingmaster.setEbook_Buyeraddress(checkNull(rs.getString("address1")));
                        eBookingmaster.setEbook_Buyeraddress1(checkNull(rs.getString("address2")));
                        eBookingmaster.setEbook_Buyeremail(checkNull(rs.getString("email")));
                        eBookingmaster.setEbook_Buyerstate(checkNull(rs.getString("stateid")));
                        eBookingmaster.setEbook_Buyer_id(checkNull(rs.getString("cid")));
                        eBookingmaster.setEbook_Buyereori(checkNull(rs.getString("eori")));
                        eBookingmaster.setEbook_Buyerzipcode(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setEbook_Buyerpersontype(checkNull(rs.getString("persontype")));
                        // eBookingmaster.setforwarderCheck(checkNull(rs.getString("flag")));
                        phone = checkNull(rs.getString("telephone"));
                        fax = checkNull(rs.getString("fax"));
                        if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_Buyerphone(phone);
                        } else {
                            eBookingmaster.setEbook_Buyerphone("");
                        }
                        if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                            eBookingmaster.setEbook_Buyerfax(fax);
                        } else {
                            eBookingmaster.setEbook_Buyerfax("");
                        }

                    }
                }
                //

                ps = con.prepareStatement(GET_BOOKING_FILES);
                ps.setString(1, bookNo);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String FileCatgeory = rs.getString("filecatgeory");
                    String FileName = rs.getString("filename");
                    if (EcomConsts.FILETYPE_VMG.equalsIgnoreCase(FileCatgeory)) {
                        eBookingmaster.setFilename1(FileName);
                    }
                    if (EcomConsts.FILETYPE_DGC.equalsIgnoreCase(FileCatgeory)) {
                        eBookingmaster.setFilename2(FileName);
                    }
                    if (EcomConsts.FILETYPE_LOL.equalsIgnoreCase(FileCatgeory)) {
                        eBookingmaster.setFilename3(FileName);
                    }
                    if (EcomConsts.FILETYPE_MSD.equalsIgnoreCase(FileCatgeory)) {
                        eBookingmaster.setFilename4(FileName);
                    }
                    if (EcomConsts.FILETYPE_OTHERS.equalsIgnoreCase(FileCatgeory)) {
                        eBookingmaster.setFilename5(FileName);
                    }
                }
                //

                pst = con.prepareStatement(GET_BOOKING_EXTBOOKNO);
                pst.setString(1, bookNo);
                rst = pst.executeQuery();
                if (rst.next()) {
                    book_Number = checkNull(rst.getString("extBookNo"));
                }
                stat = "";
                if ((!book_Number.trim().equalsIgnoreCase("N/A")) && (!book_Number.trim().equalsIgnoreCase("")) && (!book_Number.trim().equalsIgnoreCase("null"))) {
                    Map vmap = (Map) ecomvdsservice.getecombookingprintstatus(book_Number);
                    List vlist = (List) vmap.get("booking");
                    for (int i = 0; i < vlist.size(); i++) {
                        Map reslt = (Map) vlist.get(i);
                        stat = reslt.get("status").toString();
                    }
                }
                masterlist.add(eBookingmaster);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (rs111 != null) {
                    rs111.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (ps111 != null) {
                    ps111.close();
                }
                if (con != null) {
                    con.close();

                }
                if (con111 != null) {
                    con111.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

        }
        return masterlist;
    }

    @SuppressWarnings("static-access")
    private Agencydto getAgencyEmail(String ebook_agencycode, String view) {

        Agencydto agencyVO = new Agencydto();
        try {
            Map hs = (Map) ecomvdsservice.getecomagencydetails(ebook_agencycode);
            //////
            List lst = (List) hs.get("agency");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                agencyVO.setAgencyEmailId(checkNull((String) rs.get("email")));
                agencyVO.setAgencyname(checkNull((String) rs.get("agencyname")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agencyVO;
    }

    @Override
    @SuppressWarnings("static-access")
    public List getEbookingCargoDetails(String bookNo, String flag, String clearTemperatureWhileCopy) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String weight = null, volume = null, netweight = null;
        String unitvalues = null;
        List commlist = null;
        Map commhs = null, insmap = null;
        int i = 1;
        String qouteno = null;
        EbookingValidatorFormBeanNew eBookingcargo = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            insmap = getInsuranceDetils(bookNo, "E");

            ps = con.prepareStatement(BOOKING_CARGO);
            ps.setString(1, checkNull(bookNo));
            rs = ps.executeQuery();
            System.out.println("[ECOM] EcomBooingBean ==>>>>" + bookNo);
            commlist = new ArrayList();
            commhs = new HashMap();
            while (rs.next()) {
                eBookingcargo = new EbookingValidatorFormBeanNew();
                // ecombooking_cargo
                eBookingcargo.setCargoid_new(String.valueOf(i));
                eBookingcargo.setConid(rs.getInt("conid"));
                eBookingcargo.setContainerNo(checkNull(rs.getString("containerno")));
                String eqp_type = checkNull(rs.getString("eqpType"));
                String setTypedesc = checkNull(rs.getString("eqpSize")) + eqp_type;
                eBookingcargo.setEbook_hazardous(checkNull(rs.getString("dgFlag")));
                eBookingcargo.setEbook_eqp(checkNull(rs.getString("eqpSize")));
                eBookingcargo.setEbook_eqp_type(checkNull(rs.getString("eqpType")));
                eBookingcargo.setEbook_eqp_type_desc(setTypedesc);
                eBookingcargo.setEbook_eqp_qty(checkNull(rs.getString("quantity")));
                eBookingcargo.setEbook_nor(checkNull(rs.getString("ref_nor")));
                if ("Y".equalsIgnoreCase(clearTemperatureWhileCopy) && "T".equalsIgnoreCase(flag)) {
                    eBookingcargo.setEbook_nor("");
                }
                eBookingcargo.setHzd(checkNull(rs.getString("dgFlag")));
                weight = rs.getString("cargoWeight") != null && !rs.getString("cargoWeight").isEmpty() ? rs.getString("cargoWeight") : "0.0";
                weight = String.valueOf(new BigDecimal(weight).toPlainString());
                weight = checkPointValue(weight);
                eBookingcargo.setEbook_cargoweight(ConvertDecimalFormat(weight));
                eBookingcargo.setEbook_weightunit(checkNull(rs.getString("weightUnit")));
                weight = rs.getString("tareWeight") != null && !rs.getString("tareWeight").isEmpty() ? rs.getString("tareWeight") : "0.0";
                eBookingcargo.setEbook_tareweight(ConvertDecimalFormat(String.valueOf(new BigDecimal(weight).toPlainString())));
                weight = rs.getString("grossWeight") != null && !rs.getString("grossWeight").isEmpty() ? rs.getString("grossWeight") : "0.0";
                eBookingcargo.setEbook_grossweight(ConvertDecimalFormat(String.valueOf(new BigDecimal(weight).toPlainString())));
                eBookingcargo.setSoc(checkNull(rs.getString("soc")));
                eBookingcargo.setHidden_soc(checkNull(rs.getString("soc")));
                eBookingcargo.setEqpstatus(checkNull(rs.getString("eqpstatus")));
                eBookingcargo.setOog(checkNull(rs.getString("oog")));
                commhs.put(rs.getInt("conid"), eBookingcargo);
                // end ecombooking_cargo
            }

            // ecombooking_commodity
            ps = con.prepareStatement(BOOKING_COMMODITY);
            ps.setString(1, checkNull(bookNo));
            rs = ps.executeQuery();
            while (rs.next()) {
                eBookingcargo = new EbookingValidatorFormBeanNew();
                int conid = (rs.getInt("conid"));
                eBookingcargo.setEbook_commoditycode(checkNull(rs.getString("commodity")));
                eBookingcargo.setEbook_conid(checkNull(rs.getString("conid")));
                String cargoVol, netWt;
                String VolumeUnt = checkNull(rs.getString("volumnUnit"));
                volume = rs.getString("cargoVolume") != null && !rs.getString("cargoVolume").isEmpty() && !"0.0".equalsIgnoreCase(rs.getString("cargoVolume")) ? rs.getString("cargoVolume") : "";
                if (rs.getString("cargoVolume") != null && !rs.getString("cargoVolume").isEmpty() && !"0.0".equalsIgnoreCase(rs.getString("cargoVolume"))) {
                    System.out.println("[ECOM] EcomBooingBean ========>>>>cargoVolume" + rs.getString("cargoVolume"));
                    cargoVol = String.valueOf(new BigDecimal(volume).toPlainString());
                    //cargoVol = checkPointValue(cargoVol);
                    if (checkNull(cargoVol).endsWith(".0")) {
                        cargoVol = checkNull(cargoVol).substring(0, checkNull(cargoVol).indexOf(".0"));
                    }
                } else {
                    cargoVol = checkNull(rs.getString("cargoVolume"));
                }
                if (cargoVol == null || "0".equalsIgnoreCase(cargoVol) || "0.0".equalsIgnoreCase(cargoVol) || "".equalsIgnoreCase(cargoVol) || Float.parseFloat(cargoVol) < 0) {
                    cargoVol = "";
                }
                System.out.println("[ECOM] EcomBooingBean cargoVol---" + cargoVol);
                String eqp_qantity = checkNull(rs.getString("packageQty"));
                if ("0".equalsIgnoreCase(eqp_qantity)) {
                    eqp_qantity = "";
                }
                eBookingcargo.setEbook_packagecode(checkNull(rs.getString("package")));
                eBookingcargo.setEbook_cargovolume(cargoVol);
                eBookingcargo.setEbook_volumeunit(VolumeUnt);
                eBookingcargo.setCommodityothers(checkNull(rs.getString("commodityFlag")));
                eBookingcargo.setEbook_packquantity(eqp_qantity);
                eBookingcargo.setHscode(checkNull(rs.getString("HSCommodity")));
                netweight = rs.getString("nettwt") != null && !rs.getString("nettwt").isEmpty() && !"0.0".equalsIgnoreCase(rs.getString("nettwt")) ? rs.getString("nettwt") : "";
                if (rs.getString("nettwt") != null && !rs.getString("nettwt").isEmpty() && !"0.0".equalsIgnoreCase(rs.getString("nettwt"))) {
                    System.out.println("[ECOM] EcomBooingBean ========>>>>netweight" + rs.getString("nettwt"));
                    netWt = String.valueOf(new BigDecimal(netweight).toPlainString());
                    netWt = checkPointValue(netWt);
                } else {
                    netWt = checkNull(rs.getString("nettwt"));
                }
                if ("0".equalsIgnoreCase(netWt) || "0.0".equalsIgnoreCase(netWt)) {
                    netWt = "";
                }
                eBookingcargo.setEbook_netweight(netWt);
                eBookingcargo.setEbook_actCommoName(checkNull(rs.getString("descr")));
                eBookingcargo.setEbook_cmdtyhblno(checkNull(rs.getString("cmdty_hblno")));
                eBookingcargo.setEbook_cmdtysellercode(checkNull(rs.getString("slcode")));
                eBookingcargo.setEbook_cmdtysellerName(checkNull(rs.getString("slname")));
                eBookingcargo.setEbook_cmdtybuyercode(checkNull(rs.getString("bycode")));
                eBookingcargo.setEbook_cmdtybuyerName(checkNull(rs.getString("byname")));
                // String strPattern = "^0+";
                System.out.println("[ECOM] EcomBooingBean ----> " + eBookingcargo.getEbook_commoditycode() + "----> "
                        + (insmap.containsKey((eBookingcargo.getEbook_commoditycode()))));// .replaceAll(strPattern, "")
                if (insmap != null) {
                    eBookingcargo.setInsuredemail(checkNull((String) insmap.get("insuredemail")));
                    if ((insmap.containsKey((eBookingcargo.getEbook_commoditycode())) || insmap.containsKey((eBookingcargo.getHscode())))) {// .replaceAll(strPattern, "")
                        qouteno = checkNull((String) insmap.get((eBookingcargo.getEbook_commoditycode())));
                        if (qouteno.length() == 0) {
                            qouteno = checkNull((String) insmap.get((eBookingcargo.getHscode())));
                        }
                        eBookingcargo.setIsinsuranceavailable("Y");
                        eBookingcargo.setQuoteno(qouteno);
                        System.out.println("[ECOM] EcomBooingBean qouteno==> " + qouteno);
                    }
                }
                commhs = setCommodityInfo(commhs, eBookingcargo, conid);
            }
            // end
            // ecombooking_dg - Load Multiple DG Details
            ps = con.prepareStatement(BOOKING_DG);
            ps.setString(1, checkNull(bookNo));
            rs = ps.executeQuery();
            
            // Group DG entries by conid
            Map<Integer, List<EbookingValidatorFormBeanNew>> dgEntriesMap = new HashMap<>();
            
            while (rs.next()) {
                int conid = rs.getInt("conid");
                int itemno = rs.getInt("itemno");
                
                eBookingcargo = new EbookingValidatorFormBeanNew();
                String CargoFlash = "";
                if (checkNull(rs.getString("FlashPt")).length() > 0) {
                    unitvalues = rs.getString("FlashPt") != null && !rs.getString("FlashPt").isEmpty() ? rs.getString("FlashPt") : "0.0";
                    CargoFlash = checkNull(String.valueOf(new BigDecimal(unitvalues).toPlainString()));
                    CargoFlash = checkPointValue(CargoFlash);
                }
                String FlashUnt = checkNull(rs.getString("FlashPtUnit"));
                if ("0.0".equalsIgnoreCase(CargoFlash)) {
                    CargoFlash = "";
                    FlashUnt = "";
                }
                eBookingcargo.setEbook_flashpoint(CargoFlash);
                eBookingcargo.setEbook_flashunit(FlashUnt);
                eBookingcargo.setEbook_unno(checkNull(rs.getString("unNo")));
                eBookingcargo.setEbook_poluttunit(checkNull(rs.getString("MarinePollutant")));
                eBookingcargo.setEbook_dgclass(checkNull(rs.getString("class")));
                eBookingcargo.setEbook_comid(String.valueOf(itemno)); // Store itemno
                
                // Add to grouped map
                if (!dgEntriesMap.containsKey(conid)) {
                    dgEntriesMap.put(conid, new ArrayList<EbookingValidatorFormBeanNew>());
                }
                dgEntriesMap.get(conid).add(eBookingcargo);
                
                System.out.println("[ECOM] EcomBookingBean Load DG: conid=" + conid + ", itemno=" + itemno + ", unNo=" + eBookingcargo.getEbook_unno());
            }
            
            // Set dgDetailsList for each container
            for (Map.Entry<Integer, List<EbookingValidatorFormBeanNew>> entry : dgEntriesMap.entrySet()) {
                int conid = entry.getKey();
                List<EbookingValidatorFormBeanNew> dgList = entry.getValue();
                
                EbookingValidatorFormBeanNew commVo = (EbookingValidatorFormBeanNew) commhs.get(conid);
                if (commVo != null) {
                    commVo.setDgDetailsList(dgList);
                    commVo.setDgCount(dgList.size());
                    
                    // Set first DG entry fields for backward compatibility
                    if (!dgList.isEmpty()) {
                        EbookingValidatorFormBeanNew firstDG = dgList.get(0);
                        commVo.setEbook_flashpoint(firstDG.getEbook_flashpoint());
                        commVo.setEbook_flashunit(firstDG.getEbook_flashunit());
                        commVo.setEbook_unno(firstDG.getEbook_unno());
                        commVo.setEbook_poluttunit(firstDG.getEbook_poluttunit());
                        commVo.setEbook_dgclass(firstDG.getEbook_dgclass());
                    }
                    
                    commhs.put(conid, commVo);
                    System.out.println("[ECOM] EcomBookingBean Set dgDetailsList for conid=" + conid + ", count=" + dgList.size());
                }
            }
            // end
            // ecombooking_oog
            ps = con.prepareStatement(BOOKING_OOG);
            ps.setString(1, checkNull(bookNo));
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("[ECOM] EcomBooingBean ===>BOOKING_OOG<===");
                eBookingcargo = new EbookingValidatorFormBeanNew();
                int comItemNo = (rs.getInt("comItemNo"));
                unitvalues = rs.getString("forward") != null && !rs.getString("forward").isEmpty() ? rs.getString("forward") : "0.0";
                String olfwd = checkNull(String.valueOf(new BigDecimal(unitvalues).toPlainString()));
                olfwd = checkPointValue(olfwd);
                unitvalues = rs.getString("leftft") != null && !rs.getString("leftft").isEmpty() ? rs.getString("leftft") : "0.0";
                String owlft = checkNull(String.valueOf(new BigDecimal(unitvalues).toPlainString()));
                owlft = checkPointValue(owlft);
                unitvalues = rs.getString("topft") != null && !rs.getString("topft").isEmpty() ? rs.getString("topft") : "0.0";
                String oh = checkNull(String.valueOf(new BigDecimal(unitvalues).toPlainString()));
                oh = checkPointValue(oh);
                unitvalues = rs.getString("alt") != null && !rs.getString("alt").isEmpty() ? rs.getString("alt") : "0.0";
                String olaft = checkNull(String.valueOf(new BigDecimal(unitvalues).toPlainString()));
                olaft = checkPointValue(olaft);
                unitvalues = rs.getString("rightft") != null && !rs.getString("rightft").isEmpty() ? rs.getString("rightft") : "0.0";
                String owrgt = checkNull(String.valueOf(new BigDecimal(unitvalues).toPlainString()));
                owrgt = checkPointValue(owrgt);
                String dimenUnt = checkNull(rs.getString("unit"));

                eBookingcargo.setEbook_olforward(olfwd);
                eBookingcargo.setEbook_owleft(owlft);
                eBookingcargo.setEbook_oh(oh);
                eBookingcargo.setEbook_olaft(olaft);
                eBookingcargo.setEbook_owright(owrgt);
                eBookingcargo.setEbook_dimensionunit(dimenUnt);
                commhs = setCommodityOOG(commhs, eBookingcargo, comItemNo);
            }
            // end
            // ecombooking_refer
            ps = con.prepareStatement(BOOKING_REFER);
            ps.setString(1, checkNull(bookNo));
            rs = ps.executeQuery();
            while (rs.next()) {
                eBookingcargo = new EbookingValidatorFormBeanNew();
                int itemno = (rs.getInt("itemno"));
                String settemp = checkNull(rs.getString("ref_setTemp"));
                String tempunit = checkNull(rs.getString("ref_tempUnit"));
                String mintemp = checkNull(rs.getString("ref_minTemp"));
                String maxtemp = checkNull(rs.getString("ref_maxTemp"));
                String ventilation = checkNull(rs.getString("ventpercentage"));
                if ("0.0".equalsIgnoreCase(settemp)) {
                    settemp = "";
                    tempunit = "";
                    mintemp = "";
                    maxtemp = "";
                }

                if ("Y".equalsIgnoreCase(clearTemperatureWhileCopy) && "T".equalsIgnoreCase(flag)) {
                    settemp = "";
                    tempunit = "";
                    mintemp = "";
                    maxtemp = "";
                    ventilation = "";
                }
                eBookingcargo.setEbook_presetemp(settemp);
                eBookingcargo.setEbook_temperunit(tempunit);
                eBookingcargo.setEbook_mintemp(mintemp);
                eBookingcargo.setEbook_maxtemp(maxtemp);
                eBookingcargo.setHumidity(checkNull(rs.getString("humidity")));
                eBookingcargo.setHumidityflag(checkNull(rs.getString("humidity_flag")));
                eBookingcargo.setDrains(checkNull(rs.getString("drains")));
                eBookingcargo.setCo2(checkNull(rs.getString("co2value")));
                eBookingcargo.setO2(checkNull(rs.getString("o2value")));
                eBookingcargo.setVentilation(ventilation);
                commhs = setCommodityRefer(commhs, eBookingcargo, itemno);
            }
            // end
            Iterator it = commhs.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                // EbookingValidatorFormBeanNew crg = (EbookingValidatorFormBeanNew)
                // pairs.getValue();
                // int size = Integer.parseInt(crg.getEbook_eqp_qty());
                // for (int k = 0; k < size; k++) {
                // crg.setEbook_eqp_qty("1");
                commlist.add(pairs.getValue());
                // }
            }
            System.out.println("[ECOM] EcomBooingBean  ===>>commlist" + commlist.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return commlist;
    }

    /*
     * public Route getAlternativeSchedule(Route route, String isSvcReq, String
     * setService, String setVessel, String isBoundReq, String disbound, String
     * setBlpod, String setETD1, String isVoyReq, String setVoyage, String
     * strPODTerminalOnly, String blpodter, String istranstime, int transtime,
     * String btwntwoports) {
     * Connection con = null;
     * PreparedStatement ps = null;
     * ResultSet rs = null;
     * try {
     * con = new lrp.serverutils.refs.ServerObject("VSSDB").getConnection();
     * //
     * String str1 =
     * "select top 1  a.service, a.vessel, a.voyage, a.bound, a.port, a.arrdate, a.type, a.groupcallid,b.vendor_code,c.vendor_name, "
     * + " a.depdate,a.terminal from allschedules  a(NOLOCK)"
     * +
     * " left join  feederscheduleVendors b(nolock)on a.service=b.Service and a.voyage=b.voyage and a.vessel=b.vessel and a.bound=b.bound "
     * + " left join VendorDetails c(nolock) on c.vendor_code=b.vendor_code "
     * +
     * " where  (? = 'N' or a.service=?) and a.vessel=?  and (? = 'N' or a.bound=?) and a.port=? "
     * +
     * " and a.arrdate > DATEADD(hh, CONVERT(int, ?), ?) and (? = 'N' or a.voyage=?) and (?='N' or a.terminal=?) and (?='N' or a.arrdate < DATEADD(DAY, ?,?)) order by a.arrdate asc"
     * ;
     * ps = con.prepareStatement(str1, 1004, 1007);
     * //
     * System.out.println("[ECOM] EcomBooingBean s q l:" + str1);
     * //
     * System.out.println("[ECOM] EcomBooingBean getAlternativeSchedule=> " +
     * isSvcReq + "," + setService + "," + setVessel + "," + isBoundReq + "," +
     * setBlpod + "," + setETD1 + "," + isVoyReq + "," + setVoyage + "," +
     * strPODTerminalOnly + "," + blpodter + "," + disbound + "," + btwntwoports);
     * ps.setString(1, isSvcReq);
     * ps.setString(2, setService);
     * ps.setString(3, setVessel);
     * ps.setString(4, isBoundReq);
     * ps.setString(5, disbound);
     * ps.setString(6, setBlpod);
     * ps.setString(7, btwntwoports);
     * ps.setString(8, setETD1);
     * ps.setString(9, isVoyReq);
     * ps.setString(10, setVoyage);
     * ps.setString(11, strPODTerminalOnly);
     * ps.setString(12, blpodter);
     * ps.setString(13, istranstime);
     * ps.setInt(14, transtime);
     * ps.setString(15, setETD1);
     * rs = ps.executeQuery();
     * while (rs.next()) {
     * if (route == null) {
     * route = new Route();
     * }
     * System.out.println("[ECOM] EcomBooingBean arrdate::>>" +
     * checkNull(rs.getString("arrdate")));
     * route.ETA1 = checkNull(rs.getString("arrdate"));
     * //etd_calc = checkNull(rs.getString("arrdate"));
     * route.dischargecallid = checkNull(rs.getString("groupcallid"));
     * route.blpodTer = checkNull(rs.getString("terminal"));
     * route.legType = rs.getString("type");
     * System.out.println("[ECOM] EcomBooingBean frou.blpodTer=>" + route.blpodTer);
     * route.dischargebound = checkNull(rs.getString("bound"));
     * route.setDepdatefornexttrans(rs.getString("depdate"));
     * System.out.println("[ECOM] EcomBooingBean getDepdatefornexttrans=> " +
     * route.getDepdatefornexttrans());
     * //isRouting = true;
     * route.setIsRouting(true);
     * }
     * 
     * } catch (Exception e) {
     * e.printStackTrace();
     * } finally {
     * try {
     * if (rs != null) {
     * rs.close();
     * }
     * if (ps != null) {
     * ps.close();
     * }
     * if (con != null) {
     * con.close();
     * }
     * } catch (Exception e) {
     * 
     * e.printStackTrace();
     * }
     * }
     * return route;
     * }
     */
    private Map setCommodityInfo(Map commlist, EbookingValidatorFormBeanNew eBookingcargo, int itemno) {
        EbookingValidatorFormBeanNew commVo = (EbookingValidatorFormBeanNew) commlist.get(itemno);
        //
        try {
            System.out.println("[ECOM] EcomBooingBean ===>setCommodityInfo<===" + itemno);
            commVo.setEbook_commoditycode(eBookingcargo.getEbook_commoditycode());
            commVo.setEbook_conid(checkNull(eBookingcargo.getEbook_conid()));
            commVo.setEbook_packagecode(checkNull(eBookingcargo.getEbook_packagecode()));
            commVo.setEbook_cargovolume(eBookingcargo.getEbook_cargovolume());
            commVo.setEbook_volumeunit(eBookingcargo.getEbook_volumeunit());
            commVo.setCommodityothers(checkNull(eBookingcargo.getCommodityothers()));
            commVo.setEbook_packquantity(eBookingcargo.getEbook_packquantity());
            commVo.setHscode(checkNull(eBookingcargo.getHscode()));
            commVo.setEbook_netweight(eBookingcargo.getEbook_netweight());
            commVo.setEbook_actCommoName(eBookingcargo.getEbook_actCommoName());
            commVo.setIsinsuranceavailable(eBookingcargo.getIsinsuranceavailable());
            commVo.setQuoteno(eBookingcargo.getQuoteno());
            commVo.setInsuredemail(eBookingcargo.getInsuredemail());
            commVo.setEbook_cmdtyhblno(eBookingcargo.getEbook_cmdtyhblno());
            commVo.setEbook_cmdtysellercode(eBookingcargo.getEbook_cmdtysellercode());
            commVo.setEbook_cmdtysellerName(eBookingcargo.getEbook_cmdtysellerName());
            commVo.setEbook_cmdtybuyercode(eBookingcargo.getEbook_cmdtybuyercode());
            commVo.setEbook_cmdtybuyerName(eBookingcargo.getEbook_cmdtybuyerName());

        } catch (Exception e) {
            e.printStackTrace();
        }
        commlist.remove(itemno);
        commlist.put(itemno, commVo);
        return commlist;
    }

    private Map setCommodityDG(Map commlist, EbookingValidatorFormBeanNew eBookingcargo, int itemno) {
        EbookingValidatorFormBeanNew commVo = (EbookingValidatorFormBeanNew) commlist.get(itemno);
        //
        try {
            System.out.println("[ECOM] EcomBooingBean ===>setCommodityDG<===" + itemno);
            commVo.setEbook_flashpoint(eBookingcargo.getEbook_flashpoint());
            commVo.setEbook_flashunit(eBookingcargo.getEbook_flashunit());
            commVo.setEbook_unno(eBookingcargo.getEbook_unno());
            commVo.setEbook_poluttunit(eBookingcargo.getEbook_poluttunit());
            commVo.setEbook_dgclass(eBookingcargo.getEbook_dgclass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        commlist.remove(itemno);
        commlist.put(itemno, commVo);
        return commlist;
    }

    private Map setCommodityOOG(Map commlist, EbookingValidatorFormBeanNew eBookingcargo, int itemno) {
        EbookingValidatorFormBeanNew commVo = (EbookingValidatorFormBeanNew) commlist.get(itemno);
        //
        try {
            System.out.println("[ECOM] EcomBooingBean ===>setCommodityOOG<===" + itemno);
            commVo.setEbook_olforward(eBookingcargo.getEbook_olforward());
            commVo.setEbook_owleft(eBookingcargo.getEbook_owleft());
            commVo.setEbook_oh(eBookingcargo.getEbook_oh());
            commVo.setEbook_olaft(eBookingcargo.getEbook_olaft());
            commVo.setEbook_owright(eBookingcargo.getEbook_owright());
            commVo.setEbook_dimensionunit(eBookingcargo.getEbook_dimensionunit());
        } catch (Exception e) {
            e.printStackTrace();
        }
        commlist.remove(itemno);
        commlist.put(itemno, commVo);
        return commlist;
    }

    private Map setCommodityRefer(Map commlist, EbookingValidatorFormBeanNew eBookingcargo, int itemno) {
        EbookingValidatorFormBeanNew commVo = (EbookingValidatorFormBeanNew) commlist.get(itemno);
        //
        try {
            commVo.setEbook_presetemp(eBookingcargo.getEbook_presetemp());
            commVo.setEbook_temperunit(eBookingcargo.getEbook_temperunit());
            commVo.setEbook_mintemp(eBookingcargo.getEbook_mintemp());
            commVo.setEbook_maxtemp(eBookingcargo.getEbook_maxtemp());
            commVo.setHumidity(eBookingcargo.getHumidity());
            commVo.setHumidityflag(eBookingcargo.getHumidityflag());
            commVo.setDrains(eBookingcargo.getDrains());
            commVo.setCo2(eBookingcargo.getCo2());
            commVo.setO2(eBookingcargo.getO2());
            commVo.setVentilation(eBookingcargo.getVentilation());
        } catch (Exception e) {
            e.printStackTrace();
        }
        commlist.remove(itemno);
        commlist.put(itemno, commVo);
        return commlist;
    }

    @Override
    @SuppressWarnings("static-access")
    public ArrayList getBookingColumns(String userCode) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ColumnList columnList = null;
        ArrayList column = new ArrayList();
        ResultSetMetaData rsmtadta;
        int columnCount = 0;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(getBookingColumnsquery, 1004, 1007);
            rs = ps.executeQuery();
            rsmtadta = rs.getMetaData();
            columnCount = rsmtadta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columnList = new ColumnList();
                columnList.setColumnName(rsmtadta.getColumnName(i));
                columnList.setColumnType(rsmtadta.getColumnTypeName(i));
                columnList.setColumnDisplaySize(rsmtadta.getColumnDisplaySize(i));
                column.add(columnList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();

                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ps != null) {
                try {
                    ps.close();

                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();

                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return column;
    }

    @Override
    @SuppressWarnings("static-access")
    public List getCustomerData(String columnName, String constraints, String columnValue) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CustomerSearch customerList = null;
        ArrayList column = new ArrayList();
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(getCustomerDataquery, 1004, 1007);
            rs = ps.executeQuery();
            while (rs.next()) {
                customerList = new CustomerSearch();
                customerList.setCompanyName(checkNull(rs.getString(1)));
                customerList.setFirstName(checkNull(rs.getString(2)));
                customerList.setLastName(checkNull(rs.getString(3)));
                customerList.setLocationCode(checkNull(rs.getString(4)));
                customerList.setMailID(checkNull(rs.getString(5)));
                customerList.setAddress(checkNull(rs.getString(6)));
                column.add(customerList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();

                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ps != null) {
                try {
                    ps.close();

                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();

                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return column;
    }

    @Override
    @SuppressWarnings("static-access")
    public List geteBookingSearch() throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ColumnList columnList = null;
        List column = new ArrayList();
        ResultSetMetaData rsmtadta;
        int columnCount = 0;
        try {
            con = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();
            ps = con.prepareStatement(GET_BOOKING_CUSTOMERSEARCH_QUERY, 1004, 1007);
            rs = ps.executeQuery();
            rsmtadta = rs.getMetaData();
            columnCount = rsmtadta.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columnList = new ColumnList();
                columnList.setColumnName(rsmtadta.getColumnName(i));
                columnList.setColumnType(rsmtadta.getColumnTypeName(i));
                columnList.setColumnDisplaySize(rsmtadta.getColumnDisplaySize(i));
                column.add(columnList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();

                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ps != null) {
                try {
                    ps.close();

                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();

                } catch (SQLException ex) {
                    Logger.getLogger(EComCntrlBean.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return column;
    }

    @Override
    @SuppressWarnings("static-access")
    public List geteBookingCustomerInfo(String columnName, String constraints, String columnValue, String custCode,
            String custTypeConfig, String fieldName) throws RemoteException {

        List parties = new ArrayList();
        String result = "";
        CustomerProfile profile = null;
        String phone = null, fax = null, email = null;
        SvmJsonList alist = null;
        Map rethm = null, ret2 = null;
        List vlist = null;
        try {
            result = setConstraints(columnName, constraints, columnValue);
            alist = new SvmJsonList();
            alist.add(custCode);
            alist.add(result);
            alist.add(custTypeConfig);
            alist.add(fieldName);
            rethm = ecomvdspservice.getecomBookingCustomerSearch(alist);
            if (rethm != null && rethm.size() > 0) {
                ret2 = (Map) rethm.get("rs");
                if (ret2 != null && ret2.size() > 0) {
                    vlist = (List) ret2.get("rs0");
                    if (vlist != null && vlist.size() > 0) {
                        for (int i = 0; i < vlist.size(); i++) {
                            Map rs = (Map) vlist.get(i);
                            phone = checkNull((String) rs.get("telephone"));
                            fax = checkNull((String) rs.get("fax"));
                            profile = new CustomerProfile();
                            profile.setCustId(checkNull((String) rs.get("custid")));
                            profile.setCustName(checkNull((String) rs.get("name")));
                            profile.setAddr1(checkNull((String) rs.get("address1")));
                            profile.setAddr2(checkNull((String) rs.get("address2")));
                            profile.setCity(checkNull((String) rs.get("city")).toUpperCase());
                            profile.setCountry(checkNull((String) rs.get("countryid")).toUpperCase());
                            profile.setCountryName(checkNull((String) rs.get("countryname")).toUpperCase());
                            if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                                profile.setTeleNo(checkNull(phone).replace("+", ""));
                            } else {
                                profile.setTeleNo("");
                            }
                            if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                                profile.setFax(checkNull(fax).replace("+", ""));
                            } else {
                                profile.setFax("");
                            }
                            email = checkNull((String) rs.get("email"));
                            if (email.length() > 0 && email.equalsIgnoreCase("-")) {
                                email = "";
                            }
                            profile.setEmailId(checkNull((String) rs.get("email")));
                            profile.setCitycode(checkNull((String) rs.get("city")).toUpperCase());
                            profile.setState(checkNull((String) rs.get("state")));
                            profile.setZipcode(checkNull((String) rs.get("zipcode")));
                            profile.setVatno(checkNull((String) rs.get("vatno")));
                            System.out.println("[ECOM] EcomBooingBean dyn tax no" + profile.getVatno());
                            //
                            profile.setEnseori(checkNull((String) rs.get("eori")));
                            profile.setTypeofperson(checkNull((String) rs.get("persontype")));

                            parties.add(profile);
                        }
                    }
                }
            } // END SH
        } catch (Exception e) {
            System.out.println("[ECOM] EcomBooingBean Exception" + e);
        }
        return parties;
    }

    // start
    @Override
    public ReturnVO saveToBooking(EbookingValidatorFormBeanNew eBookingMasterData, ArrayList all, String custcode,
            String custname, String bookno, String savetype, ArrayList voidCargoListObj, ArrayList voidComListObj,
            String eqpQuantity, String client_code, String strtempname, String slottype, String ApEmailID,
            FileDetailsVO fls, String custTypeConfig, String custCountry, Map cusMap) throws RemoteException {
        System.out.println("[ECOM] EcomBooingBean savetobookingcalled");
        // System.out.println("ensDec===>"+
        // eBookingMasterData.getEnsdecvo().getDeclarantAddress() );

        ReturnVO rvo = new ReturnVO();
        Connection con = null;
        // Connection con_new = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        custcode = checkNull(custcode);
        custname = checkNull(custname);
        savetype = checkNull(savetype);
        rvo.setStrSaveType(savetype);
        String loginCustName = checkNull(custname);
        String agencycode = "", contactName = null, contactPhone = null, submittedBy = null, polagencyid = null,
                custmapagency = null, documentagencyid = null, bookingagencyid = null, agencyissuedid = null;

        String msgtype = "";
        String Ecom_SaveType = "";
        String book_refno = checkNull(bookno);
        String ebook_status = "";
        String bookSAVETYPE = "", haulage_org = null, haulage_des = null, endfiling_des = null;
        String loadType = null, dischType = null;
        String appVersion = eBookingMasterData.getAppVersion();
        // appVersion="2";
        System.out
                .println("[ECOM] EcomBooingBean eBookingMasterData.getAppVersion() = appVersion======> " + appVersion);
        String strgetshipmentTerms = eBookingMasterData.getEbook_shipment();
        if (strgetshipmentTerms.trim().length() > 4) {
            String str1[] = strgetshipmentTerms.split("-");
            eBookingMasterData.setEbook_shipment(str1[0]);
        }
        boolean pluginBAG = false, pluginMAG = false;
        String multiAgencyid = null;
        EbookingValidatorFormBeanNew eBookingcargoData = null;
        BookingHaulagedetailsVo haulvo = null;
        List<BookingHaulagedetailsVo> haulagelist = eBookingMasterData.getHaulagelist();
        ArrayList routinglist = new ArrayList();
        Route eBookingrouting = null;
        ArrayList cargolist = new ArrayList();
        ArrayList<EbookingValidatorFormBeanNew> custList = new ArrayList<>();
        HashMap<String, ArrayList<EbookingValidatorFormBeanNew>> custMap = (HashMap<String, ArrayList<EbookingValidatorFormBeanNew>>) cusMap;
        String shipment_type = null;
        EbookingValidatorFormBeanNew eBookingcustData = null;
        try {
            shipment_type = eBookingMasterData.getShipment_type();
            Map pluginconfig = ecomvdsservice.getEcomBookingPluginConfig("BAG,MAG");
            System.out.println("[ECOM] EcomBooingBean >>bookno" + bookno + " shipment_type => " + shipment_type
                    + "getEcomBookingPluginConfig => " + pluginconfig);
            List lis = (List) pluginconfig.get("pluginconfiguration");
            if (lis != null && lis.size() > 0) {
                for (int i = 0; i < lis.size(); i++) {
                    Map configval = (Map) lis.get(i);
                    if (configval.containsValue("BAG")) {
                        pluginBAG = true;
                    }
                    if (configval.containsValue("MAG")) {
                        pluginMAG = true;
                    }
                }
            }
            System.out.println("[ECOM] EcomBooingBean pluginBAG = > " + pluginBAG + " pluginMAG = > " + pluginMAG);

            if (savetype.length() > 0 && eBookingMasterData != null) {
                if ("D".equalsIgnoreCase(savetype)) {
                    ebook_status = "D";
                    eBookingMasterData.setEbook_templatename("");
                } else if ("S".equalsIgnoreCase(savetype)) {
                    eBookingMasterData.setEbook_templatename("");
                    ebook_status = "S";
                    msgtype = "insert";
                    bookSAVETYPE = "S";
                } else {
                    ebook_status = "T";
                }
                System.out.println("[ECOM] EcomBooingBean >>savetype>>" + savetype);
                if ((!"TE".equalsIgnoreCase(savetype))
                        && checkNull(eBookingMasterData.getEbook_templatename()).length() > 0) {
                    System.out.println(
                            "[ECOM] EcomBooingBean >>template name>>" + eBookingMasterData.getEbook_templatename());
                    bookno = "";
                }
                if (bookno.length() > 0 && "TE".equalsIgnoreCase(savetype)) {
                    Ecom_SaveType = "DraftUpdate";
                    msgtype = "tempedit";
                    bookSAVETYPE = "TE";
                } else if (bookno.length() > 0) {
                    Ecom_SaveType = "DraftUpdate";
                    msgtype = "Draft_up";
                    bookSAVETYPE = "DU";
                } else {
                    Ecom_SaveType = "DraftInsert";
                    msgtype = "Draft_in";
                    bookSAVETYPE = "DI";
                }
                System.out.println("[ECOM] EcomBooingBean >>savetype??>>" + savetype);
                String service = "";
                String vessel = "";
                String voyage = "";
                String poleta = "";
                String bound = "";
                String polterminal = "";
                String podterminal = "";
                String poletd = "";
                String preCarriage = "";
                String onCarriage = "";
                String callid = "";
                String feeder = "", grpid = "";
                int size = 0;
                int itemno = 1;
                if (all != null && all.size() > 0) {
                    size = all.size();
                    System.out.println("size" + size);
                    for (int i = 0; i < size; i++) {
                        if (i == 0) {
                            cargolist = (ArrayList) all.get(i);
                        } else {
                            routinglist = (ArrayList) all.get(i);
                        }
                    }
                }
                // get Ecom Customer mapping Agency
                custmapagency = checkNull(getEcomCustMappingAgency(custcode));
                if (custmapagency != null && custmapagency.length() > 0) {
                    agencycode = custmapagency;
                } else {
                    System.out.println("[ECOM] EcomBooingBean eBookingMasterData.getEnableMultiAgency()==> "
                            + eBookingMasterData.getEnableMultiAgency());
                    if (pluginBAG && pluginMAG && ("Y".equalsIgnoreCase(eBookingMasterData.getEnableMultiAgency()))) {
                        System.out.println("[ECOM] EcomBooingBean routinglist=========> " + routinglist);
                        if (routinglist != null && routinglist.size() > 0) {
                            String oriportcode = null, servicecode = null, vesselcode = null, voyagecode = null,
                                    boundcode = null, pol_etd = null, ebookdate = null;
                            List<Map<String, String>> evaluateValueList = new ArrayList();
                            Map<String, String> resultData = new HashMap();
                            Map<String, String> valueMap = new HashMap();
                            for (int k = 0; k < routinglist.size(); k++) {
                                eBookingrouting = (Route) routinglist.get(k);
                                if (eBookingMasterData.getEbook_origin()
                                        .equalsIgnoreCase(eBookingrouting.getLoadport())) {
                                    oriportcode = eBookingrouting.getLoadport().trim();
                                    pol_etd = eBookingMasterData.getEbook_pol_etd();
                                    ebookdate = eBookingMasterData.getEbook_date();
                                    //
                                }
                                System.out.println("[ECOM] EcomBooingBean servicecode=========> "
                                        + checkNull(servicecode).length());
                                if (checkNull(servicecode).length() == 0) {
                                    servicecode = eBookingrouting.getService();
                                    vesselcode = eBookingrouting.getVessel();
                                    voyagecode = eBookingrouting.getVoyage();
                                    boundcode = eBookingrouting.getBound();
                                }
                            }
                            System.out
                                    .println("[ECOM] EcomBooingBean oriportcode=" + oriportcode + "&pol_etd=" + pol_etd
                                            + "&ebookdate" + ebookdate + "&servicecode=>" + servicecode + "&vesselcode="
                                            + vesselcode + "&voyagecode=" + voyagecode + "&boundcode=" + boundcode);
                            valueMap = new HashMap();
                            valueMap.put("portcode", oriportcode);
                            valueMap.put("type", "E");
                            valueMap.put("service", servicecode);
                            valueMap.put("vessel", vesselcode);
                            valueMap.put("voyage", voyagecode);
                            valueMap.put("bound", boundcode);
                            valueMap.put("saildate", pol_etd);
                            valueMap.put("bookdate", ebookdate);
                            valueMap.put("shipmenttype", "NR");
                            evaluateValueList.add(valueMap);
                            resultData = MultiAgencyUtil.getAgency(evaluateValueList);
                            System.out.println("[ECOM] EcomBooingBean resultData===>  " + resultData);
                            if (resultData != null) {
                                System.out.println(
                                        "[ECOM] EcomBooingBean Route PLA===>" + eBookingMasterData.getEbook_origin());
                                if (eBookingMasterData.getEbook_origin() != null
                                        && resultData.containsKey(eBookingMasterData.getEbook_origin().trim())) {
                                    multiAgencyid = resultData.get(eBookingMasterData.getEbook_origin().trim());
                                }
                            }
                            System.out.println("[ECOM] EcomBooingBean multiAgencyid==========> " + multiAgencyid
                                    + "preferred agency=>" + eBookingMasterData.getEbook_preferred_agency());
                        }
                    }
                    if (multiAgencyid != null && multiAgencyid.length() > 0) {
                        agencycode = multiAgencyid;
                    } else {
                        agencycode = getAgencydetails(checkNull(eBookingMasterData.getEbook_origin()));
                    }

                }
                bookingagencyid = agencycode;
                if (checkNull(eBookingMasterData.getEbook_preferred_agency()).length() > 0) {
                    bookingagencyid = checkNull(eBookingMasterData.getEbook_preferred_agency());

                }
                //
                System.out.println("[ECOM] EcomBooingBean  ==agencycode ===>" + bookingagencyid);
                documentagencyid = agencycode;
                polagencyid = agencycode;
                if (!"FC".equalsIgnoreCase(shipment_type)) {
                    Map docagency = ecomvdsservice.getecombookingdocagency(bookingagencyid);
                    List doclis = (List) docagency.get("backofficemapping");
                    if (doclis != null && doclis.size() > 0) {
                        docagency = (Map) doclis.get(0);
                        documentagencyid = checkNull(docagency.get("backofficeagency").toString());
                    }
                }
                agencyissuedid = agencycode;
                if (checkNull(eBookingMasterData.getEbook_preferred_agency()).length() > 0) {
                    documentagencyid = checkNull(eBookingMasterData.getEbook_preferred_agency());
                    agencyissuedid = checkNull(eBookingMasterData.getEbook_preferred_agency());
                }
                System.out.println("[ECOM] EcomBooingBean  documentagencyid =>" + documentagencyid);

                con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                // update cc mail id in Userdetails
                if ("YES".equalsIgnoreCase(eBookingMasterData.getIsCCMailId())) {
                    pstmt = con.prepareStatement(SAVE_CCMAIN_USERDET);
                    pstmt.setString(1, checkNull(eBookingMasterData.getCcMailId()));
                    pstmt.setString(2, checkNull(loginCustName));
                    pstmt.executeUpdate();
                }

                //
                haulage_org = checkNull(eBookingMasterData.getPreCarriage());
                haulage_des = checkNull(eBookingMasterData.getOnCarriage());
                endfiling_des = checkNull(eBookingMasterData.getEnsfiling());
                haulage_org = haulage_org != null && haulage_org.length() > 0 ? haulage_org : "N";
                haulage_des = haulage_des != null && haulage_des.length() > 0 ? haulage_des : "N";
                endfiling_des = endfiling_des != null && endfiling_des.length() > 0 ? endfiling_des : "N";
                //
                // State check
                eBookingMasterData.setEbook_bookingstate(getStateCode(eBookingMasterData.getEbook_bookingstate(),
                        eBookingMasterData.getEbook_bookingcountry(), book_refno));
                eBookingMasterData.setEbook_agreestate(getStateCode(eBookingMasterData.getEbook_agreestate(),
                        eBookingMasterData.getEbook_agreecountry(), book_refno));
                eBookingMasterData.setEbook_shipperstate(getStateCode(eBookingMasterData.getEbook_shipperstate(),
                        eBookingMasterData.getEbook_shippercountry(), book_refno));
                eBookingMasterData.setEbook_consigneestate(getStateCode(eBookingMasterData.getEbook_consigneestate(),
                        eBookingMasterData.getEbook_consigneecountry(), book_refno));
                eBookingMasterData.setEbook_notifystate(getStateCode(eBookingMasterData.getEbook_notifystate(),
                        eBookingMasterData.getEbook_notifycountry(), book_refno));
                eBookingMasterData.setEbook_notifystate_n1(getStateCode(eBookingMasterData.getEbook_notifystate_n1(),
                        eBookingMasterData.getEbook_notifycountry_n1(), book_refno));
                eBookingMasterData.setEbook_forwarderstate(getStateCode(eBookingMasterData.getEbook_forwarderstate(),
                        eBookingMasterData.getEbook_forwardercountry(), book_refno));
                eBookingMasterData.setEbook_Warehousestate(getStateCode(eBookingMasterData.getEbook_Warehousestate(),
                        eBookingMasterData.getEbook_Warehousecountry(), book_refno));
                eBookingMasterData
                        .setEbook_NamedCustomerstate(getStateCode(eBookingMasterData.getEbook_NamedCustomerstate(),
                                eBookingMasterData.getEbook_NamedCustomercountry(), book_refno));
                eBookingMasterData.setEbook_Sellerstate(getStateCode(eBookingMasterData.getEbook_Sellerstate(),
                        eBookingMasterData.getEbook_Sellercountry(), book_refno));
                eBookingMasterData.setEbook_Buyerstate(getStateCode(eBookingMasterData.getEbook_Buyerstate(),
                        eBookingMasterData.getEbook_Buyercountry(), book_refno));

                //
                // New Tabel changes
                if ("DraftInsert".equalsIgnoreCase(Ecom_SaveType)) {
                    book_refno = bookNoGenerate("BKG");
                    if (book_refno.length() > 0) {
                        pstmt = con.prepareStatement(SAVE_NEW_EBOOKING_MASTER_QRY);
                        // agencycode =
                        // getAgencydetails(checkNull(eBookingMasterData.getEbook_origin()));
                        // if (multiAgencyid != null && multiAgencyid.length() > 0) {
                        // agencycode = multiAgencyid;
                        // }
                        service = eBookingMasterData.getEbook_servicecode();
                        vessel = eBookingMasterData.getEbook_vesselcode();
                        voyage = eBookingMasterData.getEbook_voyage();
                        polterminal = eBookingMasterData.getEbook_terminal();
                        podterminal = eBookingMasterData.getEbook_terminalpod();
                        poleta = eBookingMasterData.getEbook_pol_eta();
                        poletd = eBookingMasterData.getEbook_pol_etd();
                        bound = eBookingMasterData.getEbook_bound_leg();
                        callid = eBookingMasterData.getGroupcallid();
                        // eBookingMasterData.setCargoprotectid((checkNull(eBookingMasterData.getCargoprotectid()).length()
                        // == 0 ? null : eBookingMasterData.getCargoprotectid()));

                        pstmt.setString(1, book_refno);
                        pstmt.setString(2, checkNull(bookingagencyid));
                        pstmt.setString(3, eBookingMasterData.getEbook_origin());
                        pstmt.setString(4, eBookingMasterData.getEbook_delivery());
                        pstmt.setString(5, eBookingMasterData.getEbook_portofloading());
                        pstmt.setString(6, eBookingMasterData.getEbook_portofdischarge());
                        if (eBookingMasterData.getEbook_date().length() > 0) {
                            pstmt.setString(7, eBookingMasterData.getEbook_date());
                        } else {
                            pstmt.setString(7, null);
                        }
                        if (eBookingMasterData.getEbook_date().length() > 0) {
                            pstmt.setString(8, eBookingMasterData.getEbook_date());
                        } else {
                            pstmt.setString(8, null);
                        }
                        pstmt.setString(9, haulage_org);
                        pstmt.setString(10, haulage_des);
                        pstmt.setString(11, eBookingMasterData.getRemarks());
                        pstmt.setString(12, loginCustName);
                        pstmt.setString(13, ebook_status);
                        pstmt.setString(14, custcode);
                        pstmt.setString(15, eBookingMasterData.getEbook_templatename());
                        pstmt.setString(16, eBookingMasterData.getEbook_rate_reference());
                        pstmt.setString(17, eBookingMasterData.getEbook_routeid());
                        pstmt.setString(18, checkNullUpperCase(eBookingMasterData.getEbook_agency_reference()));
                        pstmt.setString(19, eBookingMasterData.getEbook_ocean_freight());
                        pstmt.setString(20, eBookingMasterData.getCarrierContact());
                        pstmt.setString(21, eBookingMasterData.getEmptyPickup());
                        pstmt.setString(22, eBookingMasterData.getEmptyPickUpDate());
                        pstmt.setString(23, "0");
                        pstmt.setString(24, checkNull(eBookingMasterData.getPodintransit()));

                        if ("S".equalsIgnoreCase(savetype)) {
                            pstmt.setString(25, loginCustName);
                        } else {
                            pstmt.setString(25, null);
                        }
                        pstmt.setString(26, eBookingMasterData.getEbook_natcode());
                        String pickuppoint = eBookingMasterData.getEmptyPickup() != null
                                && eBookingMasterData.getEmptyPickup().length() > 0
                                ? eBookingMasterData.getEbook_pickup_point()
                                : "";
                        pstmt.setString(27, pickuppoint);
                        if ("1".equalsIgnoreCase(appVersion)) {
                            pstmt.setInt(28, 1);
                        } else {
                            pstmt.setInt(28, 2);
                        }
                        pstmt.setString(29, checkNull(eBookingMasterData.getSalesRateAgree()));
                        pstmt.setString(30, checkNull(eBookingMasterData.getCcMailId()));
                        pstmt.setString(31, checkNull(eBookingMasterData.getEbook_haulercode()));
                        pstmt.setString(32, checkNull(eBookingMasterData.getEbook_haulagetype()));
                        if (eBookingMasterData.getEbook_pickup_date() != null
                                && eBookingMasterData.getEbook_pickup_date().length() > 0) {
                            pstmt.setString(33, eBookingMasterData.getEbook_pickup_date());
                        } else {
                            pstmt.setString(33, null);
                        }
                        if (eBookingMasterData.getEbook_drop_date() != null
                                && eBookingMasterData.getEbook_drop_date().length() > 0) {
                            pstmt.setString(34, eBookingMasterData.getEbook_drop_date());
                        } else {
                            pstmt.setString(34, null);
                        }
                        pstmt.setString(35, eBookingMasterData.getEbook_exportref());
                        pstmt.setString(36, eBookingMasterData.getEbook_reftype());
                        pstmt.setString(37, eBookingMasterData.getEbook_custpo());
                        pstmt.setString(38, eBookingMasterData.getCustomerreference());
                        pstmt.setString(39, eBookingMasterData.getACID());

                        pstmt.setString(40, eBookingMasterData.getCargoprotectid());
                        String dpwShipperRef = checkNull(eBookingMasterData.getShipperReferenceType()) + checkNull(eBookingMasterData.getShipperReference());
                        if ("nullnull".equalsIgnoreCase(dpwShipperRef) || "ullnull".equalsIgnoreCase(checkNull(eBookingMasterData.getShipperReference()))) {
                            dpwShipperRef = "";
                        }
                        pstmt.setString(41, dpwShipperRef);
                        pstmt.setString(42, endfiling_des);
                        pstmt.setString(43, checkNull(eBookingMasterData.getEbook_t2lfiling()));
                        pstmt.setString(44, checkNull(eBookingMasterData.getEbook_sisubmit_party()));
                        pstmt.setString(45, checkNull(eBookingMasterData.getEbook_EnsDocType()));
                        System.out.println("[ECOM] EcomBooingBean eBookingMasterData.getACID()==>"
                                + eBookingMasterData.getACID()
                                + "eBookingMasterData.getCargoprotectid()==>" + eBookingMasterData.getCargoprotectid()
                                + "//" + eBookingMasterData.getEbook_sisubmit_party());
                        int master_Update = pstmt.executeUpdate();
                        //

                        // Save booking party
                        System.out.println("[ECOM] EcomBooingBean BP ==> " + eBookingMasterData.getEbook_bookingcountry()
                                + "~~" + eBookingMasterData.getEbook_bookingname() + "~~"
                                + eBookingMasterData.getEbook_bookingaddress() + "~~"
                                + eBookingMasterData.getEbook_bookingcity_desc() + "~~"
                                + eBookingMasterData.getEbook_bookingphone() + "~~"
                                + eBookingMasterData.getEbook_bookingfax() + "~~"
                                + eBookingMasterData.getEbook_bookingemail() + "~~"
                                + eBookingMasterData.getEbook_bookingaddress1() + "~~"
                                + eBookingMasterData.getEbook_bookingstate());
                        if ("BP".equalsIgnoreCase(custTypeConfig)) {
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "BP");
                            pstmt.setString(3, custcode);
                            pstmt.setString(4, eBookingMasterData.getEbook_bookingcountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_bookingname());
                            pstmt.setString(6, eBookingMasterData.getEbook_bookingaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_bookingcity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_bookingzipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_bookingphone());
                            pstmt.setString(10, eBookingMasterData.getEbook_bookingfax());
                            pstmt.setString(11, eBookingMasterData.getEbook_bookingemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_bookingaddress1());
                            pstmt.setString(13, null);
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_bookingstate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_bookingeori());
                            pstmt.setString(17, eBookingMasterData.getBookingtypeperson());
                            pstmt.executeUpdate();
                        } else {
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "BP");
                            pstmt.setString(3, eBookingMasterData.getEbook_agreecode());
                            pstmt.setString(4, eBookingMasterData.getEbook_agreecountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_agreename());
                            pstmt.setString(6, eBookingMasterData.getEbook_agreeaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_agreecity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_agreezipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_agreephone());
                            pstmt.setString(10, eBookingMasterData.getEbook_agreefax());
                            pstmt.setString(11, eBookingMasterData.getEbook_agreeemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_agreeaddress1());
                            pstmt.setString(13, null);
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_bookingstate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_agreeeori());
                            pstmt.setString(17, eBookingMasterData.getAgreetypeperson());
                            pstmt.executeUpdate();
                        }
                        String cust_name = "";
                        cust_name = eBookingMasterData.getEbook_agreename();
                        // Insert Login user as AGrement Party
                        System.out.println("[ECOM] EcomBooingBean AP==> " + eBookingMasterData.getEbook_agreename()
                                + "~~" + eBookingMasterData.getEbook_agreecountry() + "~~"
                                + eBookingMasterData.getEbook_agreestate());
                        if (cust_name.length() > 0) {
                            System.out.println("[ECOM] EcomBooingBean >>>>eBookingMasterData.getEbook_agreecode()>>>"
                                    + eBookingMasterData.getEbook_agreecode());
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "AP");
                            pstmt.setString(3, eBookingMasterData.getEbook_agreecode());
                            pstmt.setString(4, eBookingMasterData.getEbook_agreecountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_agreename());
                            pstmt.setString(6, eBookingMasterData.getEbook_agreeaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_agreecity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_agreezipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_agreephone());
                            pstmt.setString(10, eBookingMasterData.getEbook_agreefax());
                            pstmt.setString(11, eBookingMasterData.getEbook_agreeemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_agreeaddress1());
                            pstmt.setString(13, null);
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_agreestate()));
                            System.out.println("[ECOM] EcomBooingBean ===>" + eBookingMasterData.getEbook_agreeeori()
                                    + "///" + eBookingMasterData.getAgreetypeperson());
                            pstmt.setString(16, eBookingMasterData.getEbook_agreeeori());
                            pstmt.setString(17, eBookingMasterData.getAgreetypeperson());
                            pstmt.executeUpdate();
                        }

                        cust_name = eBookingMasterData.getEbook_shippername();
                        System.out.println("[ECOM] EcomBooingBean SH==> " + eBookingMasterData.getEbook_shippercountry()
                                + "~~" + eBookingMasterData.getEbook_shippercountry() + "~~"
                                + eBookingMasterData.getEbook_shipperstate());
                        if (cust_name.length() > 0) {
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "SH");
                            pstmt.setString(3, eBookingMasterData.getEbook_shippercode());
                            pstmt.setString(4, eBookingMasterData.getEbook_shippercountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_shippername());
                            pstmt.setString(6, eBookingMasterData.getEbook_shipperaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_shippercity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_shipperzipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_shipperphone());
                            pstmt.setString(10, eBookingMasterData.getEbook_shipperfax());
                            pstmt.setString(11, eBookingMasterData.getEbook_shipperemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_shipperaddress1());
                            pstmt.setString(13, eBookingMasterData.getShipperCheck());
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_shipperstate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_shippereori());
                            pstmt.setString(17, eBookingMasterData.getShippertypeperson());
                            pstmt.executeUpdate();
                        }
                        cust_name = "";
                        cust_name = eBookingMasterData.getEbook_consigneename();
                        System.out.println("[ECOM] EcomBooingBean CN==> " + eBookingMasterData.getEbook_consigneename()
                                + "~~" + eBookingMasterData.getEbook_consigneecountry() + "~~"
                                + eBookingMasterData.getEbook_consigneestate());
                        if (cust_name.length() > 0) {
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "CN");
                            pstmt.setString(3, eBookingMasterData.getEbook_consigneecode());
                            pstmt.setString(4, eBookingMasterData.getEbook_consigneecountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_consigneename());
                            pstmt.setString(6, eBookingMasterData.getEbook_consigneeaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_consigneecity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_consigneezipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_consigneephone());
                            pstmt.setString(10, eBookingMasterData.getEbook_consigneefax());
                            pstmt.setString(11, eBookingMasterData.getEbook_consigneeemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_consigneeaddress1());
                            pstmt.setString(13, eBookingMasterData.getNotifyCheck());
                            System.out.println("[ECOM] EcomBooingBean >>eBookingMasterData.getHidCNToOrder()>>"
                                    + eBookingMasterData.getHidCNToOrder());
                            pstmt.setString(14, eBookingMasterData.getHidCNToOrder());
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_consigneestate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_consigneeeori());
                            pstmt.setString(17, eBookingMasterData.getConsigneetypeperson());
                            pstmt.executeUpdate();
                        }
                        cust_name = "";
                        cust_name = eBookingMasterData.getEbook_notifyname();
                        System.out.println("[ECOM] EcomBooingBean NT==> " + eBookingMasterData.getEbook_notifyname()
                                + "~~" + eBookingMasterData.getEbook_notifycountry() + "~~"
                                + eBookingMasterData.getEbook_notifystate());
                        if (cust_name.length() > 0) {
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "NT");
                            pstmt.setString(3, eBookingMasterData.getEbook_notifycode());
                            pstmt.setString(4, eBookingMasterData.getEbook_notifycountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_notifyname());
                            pstmt.setString(6, eBookingMasterData.getEbook_notifyaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_notifycity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_notifyzipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_notifyphone());
                            pstmt.setString(10, eBookingMasterData.getEbook_notifyfax());
                            pstmt.setString(11, eBookingMasterData.getEbook_notifyemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_notifyaddress1());
                            pstmt.setString(13, eBookingMasterData.getNotifyCheck());
                            pstmt.setString(14, eBookingMasterData.getHidNTToOrder());
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_notifystate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_notifyeori());
                            pstmt.setString(17, eBookingMasterData.getNotifytypeperson());
                            pstmt.executeUpdate();
                        }
                        cust_name = "";
                        cust_name = eBookingMasterData.getEbook_notifyname_n1();

                        if (cust_name.length() > 0) {
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "N1");
                            pstmt.setString(3, eBookingMasterData.getEbook_notifycode_n1());
                            pstmt.setString(4, eBookingMasterData.getEbook_notifycountry_n1());
                            pstmt.setString(5, eBookingMasterData.getEbook_notifyname_n1());
                            pstmt.setString(6, eBookingMasterData.getEbook_notifyaddress_n1());
                            pstmt.setString(7, eBookingMasterData.getEbook_notifycity_desc_n1());
                            pstmt.setString(8, eBookingMasterData.getEbook_notifyzipcode_n1());
                            pstmt.setString(9, eBookingMasterData.getEbook_notifyphone_n1());
                            pstmt.setString(10, eBookingMasterData.getEbook_notifyfax_n1());
                            pstmt.setString(11, eBookingMasterData.getEbook_notifyemail_n1());
                            pstmt.setString(12, eBookingMasterData.getEbook_notifyaddress1_n1());
                            pstmt.setString(13, eBookingMasterData.getNotify1Check());
                            pstmt.setString(14, eBookingMasterData.getHidNT1ToOrder());
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_notifystate_n1()));
                            pstmt.setString(16, eBookingMasterData.getEbook_notifyeori_n1());
                            pstmt.setString(17, eBookingMasterData.getNotifytypeperson_n1());
                            pstmt.executeUpdate();
                        }
                        // FW
                        cust_name = eBookingMasterData.getEbook_forwardername();

                        if (cust_name.length() > 0) {
                            System.out.println("[ECOM] EcomBooingBean >>>>eBookingMasterData.getEbook_forwardercode()>>>" + eBookingMasterData.getEbook_forwardercode());
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "FW");
                            pstmt.setString(3, eBookingMasterData.getEbook_forwardercode());
                            pstmt.setString(4, eBookingMasterData.getEbook_forwardercountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_forwardername());
                            pstmt.setString(6, eBookingMasterData.getEbook_forwarderaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_forwardercity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_forwarderzipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_forwarderphone());
                            pstmt.setString(10, eBookingMasterData.getEbook_forwarderfax());
                            pstmt.setString(11, eBookingMasterData.getEbook_forwarderemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_forwarderaddress1());
                            pstmt.setString(13, null);
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_forwarderstate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_forwardereori());
                            pstmt.setString(17, eBookingMasterData.getForwardertypeperson());
                            pstmt.executeUpdate();
                        }
                        // WH
                        cust_name = eBookingMasterData.getEbook_Warehousename();

                        if (cust_name.length() > 0) {
                            System.out.println("[ECOM] EcomBooingBean >>>>eBookingMasterData.getEbook_Warehousecode()>>>" + eBookingMasterData.getEbook_Warehousecode());
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "WH");
                            pstmt.setString(3, eBookingMasterData.getEbook_Warehousecode());
                            pstmt.setString(4, eBookingMasterData.getEbook_Warehousecountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_Warehousename());
                            pstmt.setString(6, eBookingMasterData.getEbook_Warehouseaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_Warehousecity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_Warehousezipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_Warehousephone());
                            pstmt.setString(10, eBookingMasterData.getEbook_Warehousefax());
                            pstmt.setString(11, eBookingMasterData.getEbook_Warehouseemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_Warehouseaddress1());
                            pstmt.setString(13, null);
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_Warehousestate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_Warehouseeori());
                            pstmt.setString(17, eBookingMasterData.getWarehousetypeperson());
                            pstmt.executeUpdate();
                        }
                        // NC
                        cust_name = eBookingMasterData.getEbook_NamedCustomername();

                        if (cust_name.length() > 0) {
                            System.out.println("[ECOM] EcomBooingBean >>>>eBookingMasterData.getEbook_Named Customer code()>>>"
                                    + eBookingMasterData.getEbook_NamedCustomercode());
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "NC");
                            pstmt.setString(3, eBookingMasterData.getEbook_NamedCustomercode());
                            pstmt.setString(4, eBookingMasterData.getEbook_NamedCustomercountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_NamedCustomername());
                            pstmt.setString(6, eBookingMasterData.getEbook_NamedCustomeraddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_NamedCustomercity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_NamedCustomerzipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_NamedCustomerphone());
                            pstmt.setString(10, eBookingMasterData.getEbook_NamedCustomerfax());
                            pstmt.setString(11, eBookingMasterData.getEbook_NamedCustomeremail());
                            pstmt.setString(12, eBookingMasterData.getEbook_NamedCustomeraddress1());
                            pstmt.setString(13, null);
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_NamedCustomerstate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_NamedCustomereori());
                            pstmt.setString(17, eBookingMasterData.getEbook_NamedCustomerpersontype());
                            pstmt.executeUpdate();
                        }
                        // SL
                        if (custMap != null && !custMap.isEmpty()) {
                            saveCustDetails(custMap, book_refno);
                        } else {
                            cust_name = checkNull(eBookingMasterData.getEbook_Sellername());

                            if (cust_name.length() > 0) {
                                System.out.println("[ECOM] EcomBooingBean >>>>eBookingMasterData.getEbook_Sellercode Customer code()>>>"
                                        + eBookingMasterData.getEbook_Sellercode());
                                pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                                pstmt.setString(1, book_refno);
                                pstmt.setString(2, "SL");
                                pstmt.setString(3, eBookingMasterData.getEbook_Sellercode());
                                pstmt.setString(4, eBookingMasterData.getEbook_Sellercountry());
                                pstmt.setString(5, eBookingMasterData.getEbook_Sellername());
                                pstmt.setString(6, eBookingMasterData.getEbook_Selleraddress());
                                pstmt.setString(7, eBookingMasterData.getEbook_Sellercity_desc());
                                pstmt.setString(8, eBookingMasterData.getEbook_Sellerzipcode());
                                pstmt.setString(9, eBookingMasterData.getEbook_Sellerphone());
                                pstmt.setString(10, eBookingMasterData.getEbook_Sellerfax());
                                pstmt.setString(11, eBookingMasterData.getEbook_Selleremail());
                                pstmt.setString(12, eBookingMasterData.getEbook_Selleraddress1());
                                pstmt.setString(13, null);
                                pstmt.setString(14, null);
                                pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_Sellerstate()));
                                pstmt.setString(16, eBookingMasterData.getEbook_Sellereori());
                                pstmt.setString(17, eBookingMasterData.getEbook_Sellerpersontype());
                                pstmt.executeUpdate();
                            }
                            // BY
                            cust_name = checkNull(eBookingMasterData.getEbook_Buyername());

                            if (cust_name.length() > 0) {
                                System.out.println("[ECOM] EcomBooingBean >>>>eBookingMasterData.getEbook_Buyercode Customer code()>>>"
                                        + eBookingMasterData.getEbook_Buyercode());
                                pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                                pstmt.setString(1, book_refno);
                                pstmt.setString(2, "BY");
                                pstmt.setString(3, eBookingMasterData.getEbook_Buyercode());
                                pstmt.setString(4, eBookingMasterData.getEbook_Buyercountry());
                                pstmt.setString(5, eBookingMasterData.getEbook_Buyername());
                                pstmt.setString(6, eBookingMasterData.getEbook_Buyeraddress());
                                pstmt.setString(7, eBookingMasterData.getEbook_Buyercity_desc());
                                pstmt.setString(8, eBookingMasterData.getEbook_Buyerzipcode());
                                pstmt.setString(9, eBookingMasterData.getEbook_Buyerphone());
                                pstmt.setString(10, eBookingMasterData.getEbook_Buyerfax());
                                pstmt.setString(11, eBookingMasterData.getEbook_Buyeremail());
                                pstmt.setString(12, eBookingMasterData.getEbook_Buyeraddress1());
                                pstmt.setString(13, null);
                                pstmt.setString(14, null);
                                pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_Buyerstate()));
                                pstmt.setString(16, eBookingMasterData.getEbook_Buyereori());
                                pstmt.setString(17, eBookingMasterData.getEbook_Buyerpersontype());
                                pstmt.executeUpdate();
                            }
                        }

                        // Booking Leg
                        pstmt = con.prepareStatement(ECOMBOOKING_LEG);
                        if (routinglist != null && routinglist.size() > 0) {
                            int len = routinglist.size();
                            int leg_no = 1;
                            for (int k = 0; k < len; k++) {
                                eBookingrouting = (Route) routinglist.get(k);
                                String pol = eBookingrouting.getLoadport();
                                String pod = eBookingrouting.getDisport();
                                String loadTerminal = eBookingrouting.getBlpolTer();
                                String dischargeTerminal = eBookingrouting.getBlpodTer();
                                String pol_str = eBookingrouting.getLoadtype();
                                String pod_str = eBookingrouting.getDisctype();
                                String legTypeId = eBookingrouting.getLegType();
                                String disbound = eBookingrouting.getDischargebound();
                                service = eBookingrouting.getService();
                                vessel = eBookingrouting.getVessel();
                                voyage = eBookingrouting.getVoyage();
                                poleta = eBookingrouting.getETA1();
                                poletd = eBookingrouting.getETD1();
                                bound = eBookingrouting.getBound();
                                preCarriage = eBookingrouting.getPreCarriage();
                                onCarriage = eBookingrouting.getOnCarriage();

                                grpid = eBookingrouting.getGrpCallId();
                                String loadcallid = eBookingrouting.getLoadcallid();
                                String dischargcallid = eBookingrouting.getDischargecallid();
                                pstmt.setString(1, book_refno);
                                pstmt.setInt(2, leg_no);
                                pstmt.setString(3, bound);
                                pstmt.setString(4, service);
                                pstmt.setString(5, vessel);
                                pstmt.setString(6, pol);
                                pstmt.setString(7, pod);
                                pstmt.setString(8, poletd); //
                                pstmt.setString(9, poleta); //
                                pstmt.setString(10, legTypeId);
                                pstmt.setString(11, pol_str);
                                pstmt.setString(12, pod_str);
                                pstmt.setString(13, voyage);
                                pstmt.setString(14, grpid);
                                pstmt.setString(15, loadTerminal);
                                pstmt.setString(16, dischargeTerminal);
                                pstmt.setString(17, loadcallid);
                                pstmt.setString(18, dischargcallid);
                                pstmt.setString(19, disbound);
                                pstmt.executeUpdate();
                                leg_no++;
                            }
                        }
                        //

                        if (cargolist != null) {
                            int len = cargolist.size();

                            itemno = 1;
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CONT_QRY_NEW);
                            for (int i = 0; i < len; i++) {
                                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                if (eBookingcargoData != null) {
                                    String haz = "N";
                                    String hazflag = eBookingcargoData.getHzd();
                                    if ("Y".equalsIgnoreCase(checkNull(hazflag))) {
                                        haz = "Y";
                                    }
                                    pstmt.setString(1, book_refno);
                                    pstmt.setInt(2, itemno);
                                    pstmt.setString(3, eBookingcargoData.getEbook_eqp_type());
                                    pstmt.setString(4, eBookingcargoData.getEbook_eqp());
                                    pstmt.setString(5, eBookingcargoData.getEbook_eqp_qty());
                                    pstmt.setString(6, eBookingcargoData.getSoc());
                                    pstmt.setString(7, checkNull(eBookingcargoData.getEbook_nor()));
                                    pstmt.setString(8, haz);
                                    pstmt.setString(9, ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_tareweight())));
                                    pstmt.setString(10, ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_grossweight())));
                                    pstmt.setString(11, ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_cargoweight())));
                                    pstmt.setString(12, checkNull(eBookingcargoData.getEbook_weightunit()));
                                    pstmt.setString(13, checkNull(eBookingcargoData.getEqpstatus()));
                                    pstmt.setString(14, checkNull(eBookingcargoData.getOog()));
                                    pstmt.addBatch();
                                }
                                itemno = itemno + 1;
                            }
                            int[] Cargo_update = pstmt.executeBatch();
                            //

                            itemno = 1;
                            pstmt = con.prepareStatement(SAVE_EBOOKING_COMMODITY_QRY_NEW);
                            for (int i = 0; i < len; i++) {
                                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                if (eBookingcargoData != null) {
                                    pstmt.setString(1, book_refno);
                                    pstmt.setInt(2, itemno);
                                    pstmt.setInt(3, itemno);
                                    pstmt.setString(4, checkNull(eBookingcargoData.getCommodityothers()));
                                    pstmt.setString(5, checkNull(eBookingcargoData.getEbook_commoditycode()));
                                    System.out.println("[ECOM] EcomBooingBean >>checkNull(eBookingcargoData.getHscode()>>"
                                            + eBookingcargoData.getHscode());
                                    pstmt.setString(6, checkNull(eBookingcargoData.getHscode()));
                                    pstmt.setString(7, checkNull(eBookingcargoData.getEbook_packagecode()));
                                    pstmt.setString(8, checkNull(eBookingcargoData.getEbook_packquantity()));
                                    pstmt.setString(9, checkNull(eBookingcargoData.getEbook_cargovolume()));
                                    pstmt.setString(10, checkNull(eBookingcargoData.getEbook_volumeunit()));
                                    pstmt.setString(11, checkNull(eBookingcargoData.getEbook_netweight()));
                                    pstmt.setString(12, checkNull(eBookingcargoData.getEbook_actCommoName()));
                                    pstmt.setString(13, checkNull(eBookingcargoData.getEbook_cmdtybuyercode()));
                                    pstmt.setString(14, checkNull(eBookingcargoData.getEbook_cmdtybuyerName()));
                                    pstmt.setString(15, checkNull(eBookingcargoData.getEbook_cmdtysellercode()));
                                    pstmt.setString(16, checkNull(eBookingcargoData.getEbook_cmdtysellerName()));
                                    pstmt.setString(17, checkNull(eBookingcargoData.getEbook_cmdtyhblno()));

                                    pstmt.addBatch();
                                }
                                itemno = itemno + 1;
                            }
                            if (itemno > 0) {
                                int[] comm_update = pstmt.executeBatch();
                            }
                            //
                            //

                            itemno = 1;
                            int conid = 1;
                            pstmt = con.prepareStatement(SAVE_EBOOKING_DG_QRY_NEW);
                            for (int i = 0; i < len; i++) {
                                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                if (eBookingcargoData != null && "Y".equalsIgnoreCase(eBookingcargoData.getHzd())) {
                                    
                                    // Unified DG Save - Always use dgDetailsList (works for single & multiple)
                                    List<EbookingValidatorFormBeanNew> dgList = eBookingcargoData.getDgDetailsList();
                                    
                                    if (dgList != null && !dgList.isEmpty()) {
                                        // Insert all DG entries from list
                                        for (EbookingValidatorFormBeanNew dgEntry : dgList) {
                                            String flashpoint = checkNull(dgEntry.getEbook_flashpoint());
                                            String flashunit = checkNull(dgEntry.getEbook_flashunit());
                                            if (flashpoint.length() <= 0) {
                                                flashpoint = null;
                                                flashunit = null;
                                            }
                                            
                                            // Convert comid (String) to int for itemno
                                            int dgItemNo = 1;
                                            try {
                                                String comidStr = checkNull(dgEntry.getEbook_comid());
                                                if (comidStr.length() > 0) {
                                                    dgItemNo = Integer.parseInt(comidStr);
                                                }
                                            } catch (Exception e) {
                                                System.out.println("[ECOM] EcomBookingBean Error parsing comid: " + e.getMessage());
                                            }
                                            
                                            // Split UN number (format: "1100~~ALCOHOL" or "1100 ~~ ALCOHOL")
                                            String unno[] = checkNull(dgEntry.getEbook_unno()).split("~~");
                                            
                                            pstmt.setString(1, book_refno);
                                            pstmt.setInt(2, conid);
                                            pstmt.setInt(3, dgItemNo); // itemno from dgEntry
                                            pstmt.setString(4, checkNull(dgEntry.getEbook_poluttunit()));
                                            pstmt.setString(5, checkNull(unno[0]).trim());
                                            pstmt.setString(6, checkNull(dgEntry.getEbook_dgclass()));
                                            pstmt.setString(7, flashpoint);
                                            pstmt.setString(8, flashunit);
                                            pstmt.addBatch();
                                            System.out.println("[ECOM] EcomBookingBean DG Save: conid=" + conid + ", itemno=" + dgItemNo + ", unNo=" + unno[0]);
                                        }
                                        conid = conid + 1;
                                    }
                                    itemno = itemno + 1;
                                } else {
                                    itemno = itemno + 1;
                                }
                            }
                            if (itemno > 1) {
                                int[] dg_update = pstmt.executeBatch();
                            }
                            //
                            //

                            itemno = 1;
                            conid = 1;
                            pstmt = con.prepareStatement(SAVE_EBOOKING_OOG_QRY_NEW);
                            for (int i = 0; i < len; i++) {
                                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                if (eBookingcargoData != null && "O".equalsIgnoreCase(eBookingcargoData.getEbook_eqp_type())) {
                                    pstmt.setString(1, book_refno);
                                    pstmt.setInt(2, conid);
                                    pstmt.setString(3, checkNull(eBookingcargoData.getEbook_owleft()));
                                    pstmt.setString(4, checkNull(eBookingcargoData.getEbook_owright()));
                                    pstmt.setString(5, checkNull(eBookingcargoData.getEbook_olforward()));
                                    pstmt.setString(6, checkNull(eBookingcargoData.getEbook_olaft()));
                                    pstmt.setString(7, checkNull(eBookingcargoData.getEbook_oh()));
                                    pstmt.setString(8, checkNull(eBookingcargoData.getEbook_dimensionunit()));
                                    pstmt.setInt(9, itemno);
                                    pstmt.addBatch();
                                    conid = conid + 1;
                                    itemno = itemno + 1;
                                } else {
                                    itemno = itemno + 1;
                                }
                            }
                            if (itemno > 1) {
                                int[] oog_update = pstmt.executeBatch();
                            }
                            //
                            //

                            itemno = 1;
                            conid = 1;
                            pstmt = con.prepareStatement(SAVE_EBOOKING_REFER_QRY_NEW);
                            String mintemp, maxtemp, presenttemp;
                            for (int i = 0; i < len; i++) {
                                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                String nor = checkNull(eBookingcargoData.getEbook_nor());
                                if (eBookingcargoData != null && "R".equalsIgnoreCase(eBookingcargoData.getEbook_eqp_type())) {
                                    if ("N".equalsIgnoreCase(nor)) {
                                        mintemp = checkNull(eBookingcargoData.getEbook_mintemp());
                                        maxtemp = checkNull(eBookingcargoData.getEbook_maxtemp());
                                        presenttemp = checkNull(eBookingcargoData.getEbook_presetemp());
                                        mintemp = mintemp.length() > 0 ? mintemp : null;
                                        maxtemp = maxtemp.length() > 0 ? maxtemp : null;
                                        presenttemp = presenttemp.length() > 0 ? presenttemp : null;

                                        pstmt.setString(1, book_refno);
                                        pstmt.setInt(2, conid);
                                        pstmt.setString(3, presenttemp);
                                        pstmt.setString(4, mintemp);
                                        pstmt.setString(5, maxtemp);
                                        pstmt.setString(6, checkNull(eBookingcargoData.getEbook_temperunit()));
                                        pstmt.setString(7, checkNull("Y".equalsIgnoreCase(eBookingcargoData.getHumidityflag()) ? eBookingcargoData.getHumidity() : ""));
                                        pstmt.setString(8, checkNull("Y".equalsIgnoreCase(eBookingcargoData.getHumidityflag()) ? "1" : "0"));
                                        pstmt.setString(9, checkNull(eBookingcargoData.getDrains()));
                                        pstmt.setString(10, checkNull(eBookingcargoData.getCo2()));
                                        pstmt.setString(11, checkNull(eBookingcargoData.getO2()));
                                        pstmt.setInt(12, itemno);
                                        pstmt.setString(13, checkNull(eBookingcargoData.getVentilation()));

                                        pstmt.addBatch();
                                    }
                                    conid = conid + 1;
                                    itemno = itemno + 1;
                                } else {
                                    itemno = itemno + 1;
                                }
                            }
                            if (itemno > 1) {
                                int[] refer_update = pstmt.executeBatch();
                            }
                            //

                            // insert draft haulage
                            if (haulagelist != null && haulagelist.size() > 0) {
                                pstmt = con.prepareStatement(SAVE_ECOM_BKG_HAULAGE);
                                for (int i = 0; i < haulagelist.size(); i++) {
                                    haulvo = (BookingHaulagedetailsVo) haulagelist.get(i);
                                    pstmt.setString(1, book_refno);
                                    pstmt.setInt(2, haulvo.getEqprowid());
                                    pstmt.setString(3, checkNull(haulvo.getEqptype()));
                                    pstmt.setString(4, checkNull(haulvo.getPickloccode()));
                                    pstmt.setString(5, checkNull(haulvo.getPicklocname()));
                                    pstmt.setString(6, checkNull(haulvo.getContactperson()));
                                    pstmt.setString(7, checkNull(haulvo.getAddress()));
                                    pstmt.setString(8, checkNull(haulvo.getContactphone()));
                                    pstmt.setString(9, checkNull(haulvo.getContactemail()));
                                    pstmt.setString(10, checkNull(haulvo.getRefno()));
                                    pstmt.setString(11, checkNull(haulvo.getDropdate()));
                                    pstmt.setString(12, checkNull(haulvo.getRemarks()));
                                    pstmt.setString(13, checkNull(haulvo.getTwrsdoctype()));
                                    pstmt.setString(14, checkNull(haulvo.getTwrsoverweight()));
                                    pstmt.setInt(15, haulvo.getStopsequence());
                                    pstmt.setString(16, null);
                                    pstmt.setString(17, checkNull(haulvo.getEqptype()) + checkNull(haulvo.getDummyeqp()));
                                    pstmt.addBatch();
                                    itemno = itemno + 1;
                                }
                                if (itemno > 1) {
                                    int[] refer_update = pstmt.executeBatch();
                                }

                            }
                            //
                        }
                        if (checkIsEmpty(eBookingMasterData.getBkgcutoff())
                                || checkIsEmpty(eBookingMasterData.getDoccutoff())
                                || checkIsEmpty(eBookingMasterData.getVgmcutoff())
                                || checkIsEmpty(eBookingMasterData.getBbrorocutoff())
                                || checkIsEmpty(eBookingMasterData.getHazcutoff())
                                || checkIsEmpty(eBookingMasterData.getPortentrycutoff())) {
                            pstmt = con.prepareStatement(INSERT_ECOMBOOKNG_CONF);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, eBookingMasterData.getBkgcutoff());
                            pstmt.setString(3, eBookingMasterData.getDoccutoff());
                            pstmt.setString(4, eBookingMasterData.getVgmcutoff());
                            pstmt.setString(5, eBookingMasterData.getBbrorocutoff());
                            pstmt.setString(6, eBookingMasterData.getHazcutoff());
                            pstmt.setString(7, eBookingMasterData.getPortentrycutoff());
                            pstmt.executeUpdate();

                        }
                        // ENS Filling
                        ENSDeclarantVo ensDec = eBookingMasterData.getEnsdecvo();
                        if (ensDec != null && checkNull(ensDec.getEnsFillingType()).length() > 0) {
                            pstmt = con.prepareStatement(INSERT_BOOKING_ENSDEC);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, ensDec.getEuCustZone());
                            pstmt.setString(3, ensDec.getBlTypeENS());
                            pstmt.setString(4, ensDec.getEnsFillingType());
                            pstmt.setString(5, ensDec.getPaymentMethod());
                            pstmt.setString(6, ensDec.getDeclarantName());
                            pstmt.setString(7, ensDec.getDeclarantAddress());
                            pstmt.setString(8, ensDec.getDeclarantAddress1());
                            pstmt.setString(9, ensDec.getDeclarantCountry());
                            pstmt.setString(10, ensDec.getDeclarantState());
                            pstmt.setString(11, ensDec.getDeclarantTelephone());
                            pstmt.setString(12, ensDec.getDeclarantEmail());
                            pstmt.setString(13, ensDec.getDeclarantEORI());
                            pstmt.setString(14, ensDec.getDeclarantFillingType());
                            pstmt.setString(15, ensDec.getDeclarantCity());
                            pstmt.setString(16, ensDec.getDeclarantZipcode());

                            // Execute the insert statement
                            int rowsInserted = pstmt.executeUpdate();
                            if (rowsInserted > 0) {
                                System.out.println("[ECOM] EcomBookingBean Record inserted successfully into ecomsi_ics_new!");
                            }

                        }
                    }
                } else if ("DraftUpdate".equalsIgnoreCase(Ecom_SaveType)) {
                    if (bookno.length() > 0) {
                        pstmt = con.prepareStatement(UPDATE_BOOKING_MASTER);
                        // agencycode = getAgencydetails(eBookingMasterData.getEbook_origin());
                        // if (multiAgencyid != null && multiAgencyid.length() > 0) {
                        // agencycode = multiAgencyid;
                        // }

                        service = eBookingMasterData.getEbook_servicecode();
                        vessel = eBookingMasterData.getEbook_vesselcode();
                        voyage = eBookingMasterData.getEbook_voyage();
                        polterminal = eBookingMasterData.getEbook_terminal();
                        podterminal = eBookingMasterData.getEbook_terminalpod();
                        poleta = eBookingMasterData.getEbook_pol_eta();
                        poletd = eBookingMasterData.getEbook_pol_etd();
                        bound = eBookingMasterData.getEbook_bound_leg();
                        callid = eBookingMasterData.getGroupcallid();
                        preCarriage = eBookingMasterData.getPreCarriage();
                        onCarriage = eBookingMasterData.getOnCarriage();
                        //

                        pstmt.setString(1, checkNull(bookingagencyid));
                        pstmt.setString(2, eBookingMasterData.getEbook_origin());
                        pstmt.setString(3, eBookingMasterData.getEbook_delivery());
                        pstmt.setString(4, eBookingMasterData.getEbook_portofloading());
                        pstmt.setString(5, eBookingMasterData.getEbook_portofdischarge());
                        if (eBookingMasterData.getEbook_date().length() > 0) {
                            pstmt.setString(6, eBookingMasterData.getEbook_date());
                        } else {
                            pstmt.setString(6, null);
                        }
                        if (eBookingMasterData.getEbook_date().length() > 0) {
                            pstmt.setString(7, eBookingMasterData.getEbook_date());
                        } else {
                            pstmt.setString(7, null);
                        }
                        pstmt.setString(8, haulage_org);
                        pstmt.setString(9, haulage_des);
                        pstmt.setString(10, eBookingMasterData.getRemarks());
                        pstmt.setString(11, loginCustName);
                        pstmt.setString(12, ebook_status);
                        pstmt.setString(13, custcode);
                        pstmt.setString(14, eBookingMasterData.getEbook_templatename());
                        pstmt.setString(15, eBookingMasterData.getEbook_rate_reference());
                        pstmt.setString(16, eBookingMasterData.getEbook_routeid());
                        pstmt.setString(17, checkNullUpperCase(eBookingMasterData.getEbook_agency_reference()));
                        pstmt.setString(18, eBookingMasterData.getEbook_ocean_freight());
                        pstmt.setString(19, eBookingMasterData.getCarrierContact());
                        pstmt.setString(20, eBookingMasterData.getEmptyPickup());
                        pstmt.setString(21, eBookingMasterData.getEmptyPickUpDate());
                        pstmt.setString(22, checkNull(eBookingMasterData.getPodintransit()));
                        pstmt.setString(23, eBookingMasterData.getEbook_natcode());
                        String pickuppoint = eBookingMasterData.getEmptyPickup() != null && eBookingMasterData.getEmptyPickup().length() > 0 ? eBookingMasterData.getEbook_pickup_point() : "";
                        pstmt.setString(24, pickuppoint);
                        if ("1".equalsIgnoreCase(appVersion)) {
                            pstmt.setInt(25, 1);
                        } else {
                            pstmt.setInt(25, 2);
                        }
                        pstmt.setString(26, checkNull(eBookingMasterData.getSalesRateAgree()));
                        pstmt.setString(27, checkNull(eBookingMasterData.getCcMailId()));
                        pstmt.setString(28, checkNull(eBookingMasterData.getEbook_haulercode()));
                        pstmt.setString(29, checkNull(eBookingMasterData.getEbook_haulagetype()));
                        if (eBookingMasterData.getEbook_pickup_date() != null
                                && eBookingMasterData.getEbook_pickup_date().length() > 0) {
                            pstmt.setString(30, eBookingMasterData.getEbook_pickup_date());
                        } else {
                            pstmt.setString(30, null);
                        }
                        if (eBookingMasterData.getEbook_drop_date() != null && eBookingMasterData.getEbook_drop_date().length() > 0) {
                            pstmt.setString(31, eBookingMasterData.getEbook_drop_date());
                        } else {
                            pstmt.setString(31, null);
                        }

                        pstmt.setString(32, eBookingMasterData.getEbook_exportref());
                        pstmt.setString(33, eBookingMasterData.getEbook_reftype());
                        pstmt.setString(34, eBookingMasterData.getEbook_custpo());
                        pstmt.setString(35, eBookingMasterData.getCustomerreference());
                        pstmt.setString(36, eBookingMasterData.getACID());
                        pstmt.setString(37, eBookingMasterData.getCargoprotectid());
                        String dpwShipperRef = checkNull(eBookingMasterData.getShipperReferenceType()) + checkNull(eBookingMasterData.getShipperReference());
                        if ("nullnull".equalsIgnoreCase(dpwShipperRef) || "ullnull".equalsIgnoreCase(checkNull(eBookingMasterData.getShipperReference()))) {
                            dpwShipperRef = "";
                        }
                        pstmt.setString(38, dpwShipperRef);
                        pstmt.setString(39, eBookingMasterData.getEnsfiling());
                        pstmt.setString(40, eBookingMasterData.getEbook_t2lfiling());
                        pstmt.setString(41, eBookingMasterData.getEbook_sisubmit_party());
                        pstmt.setString(42, eBookingMasterData.getEbook_EnsDocType());
                        pstmt.setString(43, book_refno);
                        // System.out.println("[ECOM] EcomBooingBean expref" +
                        // eBookingMasterData.getEbook_exportref() +
                        // eBookingMasterData.getEbook_t2lfiling()+"//"+eBookingMasterData.getEnsShipmentType());
                        int master_Update = pstmt.executeUpdate();

                        //
                        // Booking Leg Delete
                        pstmt = con.prepareStatement(DELETE_ECOM_BOOKING_LEG);
                        pstmt.setString(1, book_refno);
                        pstmt.executeUpdate();
                        // End
                        // Insert Booking Leg

                        pstmt = con.prepareStatement(INSERT_ECOM_BOOKING_LEG);
                        if (routinglist != null && routinglist.size() > 0) {
                            int len = routinglist.size();
                            int leg_no = 1;
                            for (int k = 0; k < len; k++) {
                                eBookingrouting = (Route) routinglist.get(k);
                                String pol = eBookingrouting.getLoadport();
                                String pod = eBookingrouting.getDisport();
                                String loadTerminal = eBookingrouting.getBlpolTer();
                                String dischargeTerminal = eBookingrouting.getBlpodTer();
                                String pol_str = eBookingrouting.getLoadtype();
                                String pod_str = eBookingrouting.getDisctype();
                                String legTypeId = eBookingrouting.getLegType();
                                String disbound = eBookingrouting.getDischargebound();
                                service = eBookingrouting.getService();
                                vessel = eBookingrouting.getVessel();
                                voyage = eBookingrouting.getVoyage();
                                poleta = eBookingrouting.getETA1();
                                poletd = eBookingrouting.getETD1();
                                bound = eBookingrouting.getBound();
                                grpid = eBookingrouting.getGrpCallId();
                                String loadcallid = eBookingrouting.getLoadcallid();
                                String dischargcallid = eBookingrouting.getDischargecallid();
                                pstmt.setString(1, book_refno);
                                pstmt.setInt(2, leg_no);
                                pstmt.setString(3, bound);
                                pstmt.setString(4, service);
                                pstmt.setString(5, vessel);
                                pstmt.setString(6, pol);
                                pstmt.setString(7, pod);
                                pstmt.setString(8, poletd); //
                                pstmt.setString(9, poleta); //
                                pstmt.setString(10, legTypeId);
                                pstmt.setString(11, pol_str);
                                pstmt.setString(12, pod_str);
                                pstmt.setString(13, voyage);
                                pstmt.setString(14, grpid);
                                pstmt.setString(15, loadTerminal);
                                pstmt.setString(16, dischargeTerminal);
                                pstmt.setString(17, loadcallid);
                                pstmt.setString(18, dischargcallid);
                                pstmt.setString(19, disbound);
                                pstmt.executeUpdate();
                                leg_no++;
                            }
                        }
                        //
                        //
                        pstmt = con.prepareStatement(DELETE_ECOM_BKG_CUSTDELETE);
                        pstmt.setString(1, book_refno);
                        pstmt.executeUpdate();
                        // customer

                        if ("BP".equalsIgnoreCase(custTypeConfig)) {
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "BP");
                            pstmt.setString(3, custcode);
                            pstmt.setString(4, eBookingMasterData.getEbook_bookingcountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_bookingname());
                            pstmt.setString(6, eBookingMasterData.getEbook_bookingaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_bookingcity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_bookingzipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_bookingphone());
                            pstmt.setString(10, eBookingMasterData.getEbook_bookingfax());
                            pstmt.setString(11, eBookingMasterData.getEbook_bookingemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_bookingaddress1());
                            pstmt.setString(13, null);
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_bookingstate()));
                            pstmt.setString(16, null);
                            pstmt.setString(17, null);
                            pstmt.executeUpdate();
                        } else {
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "BP");
                            pstmt.setString(3, eBookingMasterData.getEbook_agreecode());
                            pstmt.setString(4, eBookingMasterData.getEbook_agreecountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_agreename());
                            pstmt.setString(6, eBookingMasterData.getEbook_agreeaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_agreecity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_agreezipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_agreephone());
                            pstmt.setString(10, eBookingMasterData.getEbook_agreefax());
                            pstmt.setString(11, eBookingMasterData.getEbook_agreeemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_agreeaddress1());
                            pstmt.setString(13, null);
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_agreestate()));
                            pstmt.setString(16, null);
                            pstmt.setString(17, null);
                            pstmt.executeUpdate();
                        }

                        String cust_name = "";
                        cust_name = eBookingMasterData.getEbook_agreename();
                        // Insert Login user as AGrement Party
                        if (cust_name.length() > 0) {
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "AP");
                            pstmt.setString(3, eBookingMasterData.getEbook_agreecode());
                            pstmt.setString(4, eBookingMasterData.getEbook_agreecountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_agreename());
                            pstmt.setString(6, eBookingMasterData.getEbook_agreeaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_agreecity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_agreezipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_agreephone());
                            pstmt.setString(10, eBookingMasterData.getEbook_agreefax());
                            pstmt.setString(11, eBookingMasterData.getEbook_agreeemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_agreeaddress1());
                            pstmt.setString(13, null);
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_agreestate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_agreeeori());
                            pstmt.setString(17, eBookingMasterData.getAgreetypeperson());
                            pstmt.executeUpdate();
                        }

                        cust_name = eBookingMasterData.getEbook_shippername();

                        if (cust_name.length() > 0) {
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "SH");
                            pstmt.setString(3, eBookingMasterData.getEbook_shippercode());
                            pstmt.setString(4, eBookingMasterData.getEbook_shippercountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_shippername());
                            pstmt.setString(6, eBookingMasterData.getEbook_shipperaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_shippercity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_shipperzipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_shipperphone());
                            pstmt.setString(10, eBookingMasterData.getEbook_shipperfax());
                            pstmt.setString(11, eBookingMasterData.getEbook_shipperemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_shipperaddress1());
                            pstmt.setString(13, eBookingMasterData.getShipperCheck());
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_shipperstate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_shippereori());
                            pstmt.setString(17, eBookingMasterData.getShippertypeperson());
                            pstmt.executeUpdate();
                        }
                        cust_name = "";
                        cust_name = eBookingMasterData.getEbook_consigneename();

                        if (cust_name.length() > 0) {
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "CN");
                            pstmt.setString(3, eBookingMasterData.getEbook_consigneecode());
                            pstmt.setString(4, eBookingMasterData.getEbook_consigneecountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_consigneename());
                            pstmt.setString(6, eBookingMasterData.getEbook_consigneeaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_consigneecity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_consigneezipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_consigneephone());
                            pstmt.setString(10, eBookingMasterData.getEbook_consigneefax());
                            pstmt.setString(11, eBookingMasterData.getEbook_consigneeemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_consigneeaddress1());
                            pstmt.setString(13, eBookingMasterData.getNotifyCheck());
                            System.out.println("[ECOM] EcomBooingBean >>eBookingMasterData.getHidCNToOrder()>>" + eBookingMasterData.getHidCNToOrder());
                            pstmt.setString(14, eBookingMasterData.getHidCNToOrder());
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_consigneestate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_consigneeeori());
                            pstmt.setString(17, eBookingMasterData.getConsigneetypeperson());
                            pstmt.executeUpdate();
                        }
                        cust_name = "";
                        cust_name = eBookingMasterData.getEbook_notifyname();
                        if (cust_name.length() > 0) {
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "NT");
                            pstmt.setString(3, eBookingMasterData.getEbook_notifycode());
                            pstmt.setString(4, eBookingMasterData.getEbook_notifycountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_notifyname());
                            pstmt.setString(6, eBookingMasterData.getEbook_notifyaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_notifycity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_notifyzipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_notifyphone());
                            pstmt.setString(10, eBookingMasterData.getEbook_notifyfax());
                            pstmt.setString(11, eBookingMasterData.getEbook_notifyemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_notifyaddress1());
                            pstmt.setString(13, eBookingMasterData.getNotifyCheck());
                            pstmt.setString(14, eBookingMasterData.getHidNTToOrder());
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_notifystate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_notifyeori());
                            pstmt.setString(17, eBookingMasterData.getNotifytypeperson());
                            pstmt.executeUpdate();
                        }
                        cust_name = "";
                        cust_name = eBookingMasterData.getEbook_notifyname_n1();
                        if (cust_name.length() > 0) {
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "N1");
                            pstmt.setString(3, eBookingMasterData.getEbook_notifycode_n1());
                            pstmt.setString(4, eBookingMasterData.getEbook_notifycountry_n1());
                            pstmt.setString(5, eBookingMasterData.getEbook_notifyname_n1());
                            pstmt.setString(6, eBookingMasterData.getEbook_notifyaddress_n1());
                            pstmt.setString(7, eBookingMasterData.getEbook_notifycity_desc_n1());
                            pstmt.setString(8, eBookingMasterData.getEbook_notifyzipcode_n1());
                            pstmt.setString(9, eBookingMasterData.getEbook_notifyphone_n1());
                            pstmt.setString(10, eBookingMasterData.getEbook_notifyfax_n1());
                            pstmt.setString(11, eBookingMasterData.getEbook_notifyemail_n1());
                            pstmt.setString(12, eBookingMasterData.getEbook_notifyaddress1_n1());
                            pstmt.setString(13, eBookingMasterData.getNotify1Check());
                            pstmt.setString(14, eBookingMasterData.getHidNT1ToOrder());
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_notifystate_n1()));
                            pstmt.setString(16, eBookingMasterData.getEbook_notifyeori_n1());
                            pstmt.setString(17, eBookingMasterData.getNotifytypeperson_n1());
                            pstmt.executeUpdate();
                        }
                        cust_name = "";
                        cust_name = eBookingMasterData.getEbook_forwardername();

                        if (cust_name.length() > 0) {
                            System.out
                                    .println("[ECOM] EcomBooingBean >>>>eBookingMasterData.getEbook_forwardercode()>>>"
                                            + eBookingMasterData.getEbook_forwardercode());
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "FW");
                            pstmt.setString(3, eBookingMasterData.getEbook_forwardercode());
                            pstmt.setString(4, eBookingMasterData.getEbook_forwardercountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_forwardername());
                            pstmt.setString(6, eBookingMasterData.getEbook_forwarderaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_forwardercity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_forwarderzipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_forwarderphone());
                            pstmt.setString(10, eBookingMasterData.getEbook_forwarderfax());
                            pstmt.setString(11, eBookingMasterData.getEbook_forwarderemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_forwarderaddress1());
                            pstmt.setString(13, null);
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_forwarderstate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_forwardereori());
                            pstmt.setString(17, eBookingMasterData.getForwardertypeperson());
                            pstmt.executeUpdate();
                        }
                        // WH
                        cust_name = "";
                        cust_name = eBookingMasterData.getEbook_Warehousename();

                        if (cust_name.length() > 0) {
                            System.out
                                    .println("[ECOM] EcomBooingBean >>>>eBookingMasterData.getEbook_Warehousecode()>>>"
                                            + eBookingMasterData.getEbook_Warehousecode());
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "WH");
                            pstmt.setString(3, eBookingMasterData.getEbook_Warehousecode());
                            pstmt.setString(4, eBookingMasterData.getEbook_Warehousecountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_Warehousename());
                            pstmt.setString(6, eBookingMasterData.getEbook_Warehouseaddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_Warehousecity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_Warehousezipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_Warehousephone());
                            pstmt.setString(10, eBookingMasterData.getEbook_Warehousefax());
                            pstmt.setString(11, eBookingMasterData.getEbook_Warehouseemail());
                            pstmt.setString(12, eBookingMasterData.getEbook_Warehouseaddress1());
                            pstmt.setString(13, null);
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_Warehousestate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_Warehouseeori());
                            pstmt.setString(17, eBookingMasterData.getWarehousetypeperson());
                            pstmt.executeUpdate();
                        }
                        // NC
                        cust_name = "";
                        cust_name = eBookingMasterData.getEbook_NamedCustomername();

                        if (cust_name.length() > 0) {
                            System.out
                                    .println("[ECOM] EcomBooingBean >>>>eBookingMasterData.getEbook_Warehousecode()>>>"
                                            + eBookingMasterData.getEbook_NamedCustomercode());
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, "NC");
                            pstmt.setString(3, eBookingMasterData.getEbook_NamedCustomercode());
                            pstmt.setString(4, eBookingMasterData.getEbook_NamedCustomercountry());
                            pstmt.setString(5, eBookingMasterData.getEbook_NamedCustomername());
                            pstmt.setString(6, eBookingMasterData.getEbook_NamedCustomeraddress());
                            pstmt.setString(7, eBookingMasterData.getEbook_NamedCustomercity_desc());
                            pstmt.setString(8, eBookingMasterData.getEbook_NamedCustomerzipcode());
                            pstmt.setString(9, eBookingMasterData.getEbook_NamedCustomerphone());
                            pstmt.setString(10, eBookingMasterData.getEbook_NamedCustomerfax());
                            pstmt.setString(11, eBookingMasterData.getEbook_NamedCustomeremail());
                            pstmt.setString(12, eBookingMasterData.getEbook_NamedCustomeraddress1());
                            pstmt.setString(13, null);
                            pstmt.setString(14, null);
                            pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_NamedCustomerstate()));
                            pstmt.setString(16, eBookingMasterData.getEbook_NamedCustomereori());
                            pstmt.setString(17, eBookingMasterData.getEbook_NamedCustomerpersontype());
                            pstmt.executeUpdate();
                        }
                        if (custMap != null && !custMap.isEmpty()) {
                            saveCustDetails(custMap, book_refno);
                        } else {
                            // SL
                            cust_name = "";
                            cust_name = checkNull(eBookingMasterData.getEbook_Sellername());

                            if (cust_name.length() > 0) {
                                System.out.println(
                                        "[ECOM] EcomBooingBean >>>>eBookingMasterData.getEbook_Sellercode Customer code()>>>"
                                        + eBookingMasterData.getEbook_Sellercode());
                                pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                                pstmt.setString(1, book_refno);
                                pstmt.setString(2, "SL");
                                pstmt.setString(3, eBookingMasterData.getEbook_Sellercode());
                                pstmt.setString(4, eBookingMasterData.getEbook_Sellercountry());
                                pstmt.setString(5, eBookingMasterData.getEbook_Sellername());
                                pstmt.setString(6, eBookingMasterData.getEbook_Selleraddress());
                                pstmt.setString(7, eBookingMasterData.getEbook_Sellercity_desc());
                                pstmt.setString(8, eBookingMasterData.getEbook_Sellerzipcode());
                                pstmt.setString(9, eBookingMasterData.getEbook_Sellerphone());
                                pstmt.setString(10, eBookingMasterData.getEbook_Sellerfax());
                                pstmt.setString(11, eBookingMasterData.getEbook_Selleremail());
                                pstmt.setString(12, eBookingMasterData.getEbook_Selleraddress1());
                                pstmt.setString(13, null);
                                pstmt.setString(14, null);
                                pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_Sellerstate()));
                                pstmt.setString(16, eBookingMasterData.getEbook_Sellereori());
                                pstmt.setString(17, eBookingMasterData.getEbook_Sellerpersontype());
                                pstmt.executeUpdate();
                            }
                            // BY
                            cust_name = "";
                            cust_name = checkNull(eBookingMasterData.getEbook_Buyername());

                            if (cust_name.length() > 0) {
                                System.out.println(
                                        "[ECOM] EcomBooingBean >>>>eBookingMasterData.getEbook_Buyercode Customer code()>>>"
                                        + eBookingMasterData.getEbook_Buyercode());
                                pstmt = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                                pstmt.setString(1, book_refno);
                                pstmt.setString(2, "BY");
                                pstmt.setString(3, eBookingMasterData.getEbook_Buyercode());
                                pstmt.setString(4, eBookingMasterData.getEbook_Buyercountry());
                                pstmt.setString(5, eBookingMasterData.getEbook_Buyername());
                                pstmt.setString(6, eBookingMasterData.getEbook_Buyeraddress());
                                pstmt.setString(7, eBookingMasterData.getEbook_Buyercity_desc());
                                pstmt.setString(8, eBookingMasterData.getEbook_Buyerzipcode());
                                pstmt.setString(9, eBookingMasterData.getEbook_Buyerphone());
                                pstmt.setString(10, eBookingMasterData.getEbook_Buyerfax());
                                pstmt.setString(11, eBookingMasterData.getEbook_Buyeremail());
                                pstmt.setString(12, eBookingMasterData.getEbook_Buyeraddress1());
                                pstmt.setString(13, null);
                                pstmt.setString(14, null);
                                pstmt.setString(15, splitMasterCode(eBookingMasterData.getEbook_Buyerstate()));
                                pstmt.setString(16, eBookingMasterData.getEbook_Buyereori());
                                pstmt.setString(17, eBookingMasterData.getEbook_Buyerpersontype());
                                pstmt.executeUpdate();
                            }
                        }

                        // End
                        // Get Max of item in Cargo for this Booking.
                        // Update Cargo(commodity) and Container(Eqpimnet type) Details
                        pstmt = con.prepareStatement(DELETE_ECOM_BKG_CARGO);
                        pstmt.setString(1, book_refno);
                        pstmt.executeUpdate();

                        pstmt = con.prepareStatement(DELETE_ECOM_BKG_COMMODITY);
                        pstmt.setString(1, book_refno);
                        pstmt.executeUpdate();

                        pstmt = con.prepareStatement(DELETE_ECOM_BKG_DG);
                        pstmt.setString(1, book_refno);
                        pstmt.executeUpdate();

                        pstmt = con.prepareStatement(DELETE_ECOM_BKG_OOG);
                        pstmt.setString(1, book_refno);
                        pstmt.executeUpdate();
                        pstmt = con.prepareStatement(DELETE_ECOM_BKG_REFER);
                        pstmt.setString(1, book_refno);
                        pstmt.executeUpdate();
                        //
                        int check = 0;
                        // Update Cargo(commodity) and Container(Eqpimnet type) Details
                        if (cargolist != null) {
                            int len = cargolist.size();
                            itemno = 1;
                            pstmt = con.prepareStatement(SAVE_EBOOKING_CONT_QRY_NEW);
                            for (int i = 0; i < len; i++) {
                                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                if (eBookingcargoData != null) {
                                    String haz = "N";
                                    String hazflag = eBookingcargoData.getHzd();
                                    if ("Y".equalsIgnoreCase(checkNull(hazflag))) {
                                        haz = "Y";
                                    }
                                    pstmt.setString(1, book_refno);
                                    pstmt.setInt(2, itemno);
                                    pstmt.setString(3, eBookingcargoData.getEbook_eqp_type());
                                    pstmt.setString(4, eBookingcargoData.getEbook_eqp());
                                    pstmt.setString(5, eBookingcargoData.getEbook_eqp_qty());
                                    pstmt.setString(6, eBookingcargoData.getSoc());
                                    pstmt.setString(7, checkNull(eBookingcargoData.getEbook_nor()));
                                    pstmt.setString(8, haz);
                                    pstmt.setString(9, ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_tareweight())));
                                    pstmt.setString(10, ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_grossweight())));
                                    pstmt.setString(11, ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_cargoweight())));
                                    pstmt.setString(12, checkNull(eBookingcargoData.getEbook_weightunit()));
                                    pstmt.setString(13, checkNull(eBookingcargoData.getEqpstatus()));
                                    pstmt.setString(14, checkNull(eBookingcargoData.getOog()));
                                    pstmt.addBatch();
                                }
                                itemno = itemno + 1;
                            }
                            int[] Cargo_update = pstmt.executeBatch();
                            //

                            itemno = 1;
                            pstmt = con.prepareStatement(SAVE_EBOOKING_COMMODITY_QRY_NEW);
                            for (int i = 0; i < len; i++) {
                                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                if (eBookingcargoData != null) {
                                    pstmt.setString(1, book_refno);
                                    pstmt.setInt(2, itemno);
                                    pstmt.setInt(3, itemno);
                                    pstmt.setString(4, checkNull(eBookingcargoData.getCommodityothers()));
                                    pstmt.setString(5, checkNull(eBookingcargoData.getEbook_commoditycode()));
                                    pstmt.setString(6, checkNull(eBookingcargoData.getHscode()));
                                    pstmt.setString(7, checkNull(eBookingcargoData.getEbook_packagecode()));
                                    pstmt.setString(8, checkNull(eBookingcargoData.getEbook_packquantity()));
                                    pstmt.setString(9, checkNull(eBookingcargoData.getEbook_cargovolume()));
                                    pstmt.setString(10, checkNull(eBookingcargoData.getEbook_volumeunit()));
                                    pstmt.setString(11, checkNull(eBookingcargoData.getEbook_netweight()));
                                    pstmt.setString(12, checkNull(eBookingcargoData.getEbook_actCommoName()));
                                    pstmt.setString(13, checkNull(eBookingcargoData.getEbook_cmdtybuyercode()));
                                    pstmt.setString(14, checkNull(eBookingcargoData.getEbook_cmdtybuyerName()));
                                    pstmt.setString(15, checkNull(eBookingcargoData.getEbook_cmdtysellercode()));
                                    pstmt.setString(16, checkNull(eBookingcargoData.getEbook_cmdtysellerName()));
                                    pstmt.setString(17, checkNull(eBookingcargoData.getEbook_cmdtyhblno()));

                                    pstmt.addBatch();
                                }
                                itemno = itemno + 1;
                            }
                            if (itemno > 0) {
                                int[] comm_update = pstmt.executeBatch();
                            }
                            //
                            //

                            itemno = 1;
                            int conid = 1;
                            pstmt = con.prepareStatement(SAVE_EBOOKING_DG_QRY_NEW);
                            for (int i = 0; i < len; i++) {
                                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                if (eBookingcargoData != null && "Y".equalsIgnoreCase(eBookingcargoData.getHzd())) {
                                    
                                    // Unified DG Save - Always use dgDetailsList (works for single & multiple)
                                    List<EbookingValidatorFormBeanNew> dgList = eBookingcargoData.getDgDetailsList();
                                    
                                    if (dgList != null && !dgList.isEmpty()) {
                                        // Insert all DG entries from list
                                        for (EbookingValidatorFormBeanNew dgEntry : dgList) {
                                            String flashpoint = checkNull(dgEntry.getEbook_flashpoint());
                                            String flashunit = checkNull(dgEntry.getEbook_flashunit());
                                            if (flashpoint.length() <= 0) {
                                                flashpoint = null;
                                                flashunit = null;
                                            }
                                            
                                            // Convert comid (String) to int for itemno
                                            int dgItemNo = 1;
                                            try {
                                                String comidStr = checkNull(dgEntry.getEbook_comid());
                                                if (comidStr.length() > 0) {
                                                    dgItemNo = Integer.parseInt(comidStr);
                                                }
                                            } catch (Exception e) {
                                                System.out.println("[ECOM] EcomBookingBean Error parsing comid: " + e.getMessage());
                                            }
                                            
                                            // Split UN number (format: "1100~~ALCOHOL" or "1100 ~~ ALCOHOL")
                                            String unno[] = checkNull(dgEntry.getEbook_unno()).split("~~");
                                            
                                            pstmt.setString(1, book_refno);
                                            pstmt.setInt(2, conid);
                                            pstmt.setInt(3, dgItemNo); // itemno from dgEntry
                                            pstmt.setString(4, checkNull(dgEntry.getEbook_poluttunit()));
                                            pstmt.setString(5, checkNull(unno[0]).trim());
                                            pstmt.setString(6, checkNull(dgEntry.getEbook_dgclass()));
                                            pstmt.setString(7, flashpoint);
                                            pstmt.setString(8, flashunit);
                                            pstmt.addBatch();
                                            System.out.println("[ECOM] EcomBookingBean DG Save: conid=" + conid + ", itemno=" + dgItemNo + ", unNo=" + unno[0]);
                                        }
                                        conid = conid + 1;
                                    }
                                    itemno = itemno + 1;
                                } else {
                                    itemno = itemno + 1;
                                }
                            }
                            if (itemno > 1) {
                                int[] dg_update = pstmt.executeBatch();
                            }
                            //
                            //

                            itemno = 1;
                            conid = 1;
                            pstmt = con.prepareStatement(SAVE_EBOOKING_OOG_QRY_NEW);
                            for (int i = 0; i < len; i++) {
                                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                if (eBookingcargoData != null && "O".equalsIgnoreCase(eBookingcargoData.getEbook_eqp_type())) {
                                    pstmt.setString(1, book_refno);
                                    pstmt.setInt(2, conid);
                                    pstmt.setString(3, checkNull(eBookingcargoData.getEbook_owleft()));
                                    pstmt.setString(4, checkNull(eBookingcargoData.getEbook_owright()));
                                    pstmt.setString(5, checkNull(eBookingcargoData.getEbook_olforward()));
                                    pstmt.setString(6, checkNull(eBookingcargoData.getEbook_olaft()));
                                    pstmt.setString(7, checkNull(eBookingcargoData.getEbook_oh()));
                                    pstmt.setString(8, checkNull(eBookingcargoData.getEbook_dimensionunit()));
                                    pstmt.setInt(9, itemno);
                                    pstmt.addBatch();
                                    conid = conid + 1;
                                    itemno = itemno + 1;
                                } else {
                                    itemno = itemno + 1;
                                }
                            }
                            if (itemno > 1) {
                                int[] oog_update = pstmt.executeBatch();
                            }
                            //
                            //

                            itemno = 1;
                            conid = 1;
                            pstmt = con.prepareStatement(SAVE_EBOOKING_REFER_QRY_NEW);
                            String mintemp, maxtemp, presenttemp;
                            for (int i = 0; i < len; i++) {
                                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                String nor = checkNull(eBookingcargoData.getEbook_nor());
                                if (eBookingcargoData != null && "R".equalsIgnoreCase(eBookingcargoData.getEbook_eqp_type())) {
                                    if ("N".equalsIgnoreCase(nor)) {
                                        mintemp = checkNull(eBookingcargoData.getEbook_mintemp());
                                        maxtemp = checkNull(eBookingcargoData.getEbook_maxtemp());
                                        presenttemp = checkNull(eBookingcargoData.getEbook_presetemp());
                                        mintemp = mintemp.length() > 0 ? mintemp : null;
                                        maxtemp = maxtemp.length() > 0 ? maxtemp : null;
                                        presenttemp = presenttemp.length() > 0 ? presenttemp : null;

                                        pstmt.setString(1, book_refno);
                                        pstmt.setInt(2, conid);
                                        pstmt.setString(3, presenttemp);
                                        pstmt.setString(4, mintemp);
                                        pstmt.setString(5, maxtemp);
                                        pstmt.setString(6, checkNull(eBookingcargoData.getEbook_temperunit()));
                                        pstmt.setString(7, checkNull("Y".equalsIgnoreCase(eBookingcargoData.getHumidityflag()) ? eBookingcargoData.getHumidity() : ""));
                                        pstmt.setString(8, checkNull("Y".equalsIgnoreCase(eBookingcargoData.getHumidityflag()) ? "1" : "0"));
                                        pstmt.setString(9, checkNull(eBookingcargoData.getDrains()));
                                        pstmt.setString(10, checkNull(eBookingcargoData.getCo2()));
                                        pstmt.setString(11, checkNull(eBookingcargoData.getO2()));
                                        pstmt.setInt(12, itemno);
                                        pstmt.setString(13, checkNull(eBookingcargoData.getVentilation()));
                                        pstmt.addBatch();
                                        conid = conid + 1;
                                        itemno = itemno + 1;
                                    }
                                } else {
                                    itemno = itemno + 1;
                                }
                            }
                            if (itemno > 1) {
                                int[] refer_update = pstmt.executeBatch();
                            }
                            //
                        } // end cargo
                        //
                        pstmt = con.prepareStatement(DELETE_FILES);
                        pstmt.setString(1, book_refno);
                        pstmt.executeUpdate();

                        pstmt = con.prepareStatement(DELETE_ECOM_BKG_HAULAGE);
                        pstmt.setString(1, book_refno);
                        pstmt.executeUpdate();

                        // insert draft haulage
                        if (haulagelist != null && haulagelist.size() > 0) {
                            pstmt = con.prepareStatement(SAVE_ECOM_BKG_HAULAGE);
                            for (int i = 0; i < haulagelist.size(); i++) {
                                haulvo = (BookingHaulagedetailsVo) haulagelist.get(i);
                                pstmt.setString(1, book_refno);
                                pstmt.setInt(2, haulvo.getEqprowid());
                                pstmt.setString(3, checkNull(haulvo.getEqptype()));
                                pstmt.setString(4, checkNull(haulvo.getPickloccode()));
                                pstmt.setString(5, checkNull(haulvo.getPicklocname()));
                                pstmt.setString(6, checkNull(haulvo.getContactperson()));
                                pstmt.setString(7, checkNull(haulvo.getAddress()));
                                pstmt.setString(8, checkNull(haulvo.getContactphone()));
                                pstmt.setString(9, checkNull(haulvo.getContactemail()));
                                pstmt.setString(10, checkNull(haulvo.getRefno()));
                                pstmt.setString(11, checkNull(haulvo.getDropdate()));
                                pstmt.setString(12, checkNull(haulvo.getRemarks()));
                                pstmt.setString(13, checkNull(haulvo.getTwrsdoctype()));
                                pstmt.setString(14, checkNull(haulvo.getTwrsoverweight()));
                                pstmt.setInt(15, haulvo.getStopsequence());
                                pstmt.setString(16, null);
                                pstmt.setString(17, checkNull(haulvo.getEqptype()) + checkNull(haulvo.getDummyeqp()));
                                pstmt.addBatch();
                                itemno = itemno + 1;
                            }
                            if (itemno > 1) {
                                int[] refer_update = pstmt.executeBatch();
                            }

                        }
                        //
                        pstmt = con.prepareStatement(DELETE_ECOMBOOKNG_CONF);
                        pstmt.setString(1, book_refno);
                        pstmt.executeUpdate();
                        if (checkIsEmpty(eBookingMasterData.getBkgcutoff())
                                || checkIsEmpty(eBookingMasterData.getDoccutoff())
                                || checkIsEmpty(eBookingMasterData.getVgmcutoff())
                                || checkIsEmpty(eBookingMasterData.getBbrorocutoff())
                                || checkIsEmpty(eBookingMasterData.getHazcutoff())
                                || checkIsEmpty(eBookingMasterData.getPortentrycutoff())) {
                            pstmt = con.prepareStatement(INSERT_ECOMBOOKNG_CONF);
                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, eBookingMasterData.getBkgcutoff());
                            pstmt.setString(3, eBookingMasterData.getDoccutoff());
                            pstmt.setString(4, eBookingMasterData.getVgmcutoff());
                            pstmt.setString(5, eBookingMasterData.getBbrorocutoff());
                            pstmt.setString(6, eBookingMasterData.getHazcutoff());
                            pstmt.setString(7, eBookingMasterData.getPortentrycutoff());
                            pstmt.executeUpdate();

                        }
                        // ENS Filling
                        pstmt = con.prepareStatement(DELETE_BOOKING_ENSDEC);
                        pstmt.setString(1, book_refno);
                        pstmt.executeUpdate();
                        ENSDeclarantVo ensDec = eBookingMasterData.getEnsdecvo();
                        if (ensDec != null && checkNull(ensDec.getEuCustZone()).length() > 0) {
                            pstmt = con.prepareStatement(INSERT_BOOKING_ENSDEC);

                            pstmt.setString(1, book_refno);
                            pstmt.setString(2, ensDec.getEuCustZone());
                            pstmt.setString(3, ensDec.getBlTypeENS());
                            pstmt.setString(4, ensDec.getEnsFillingType());
                            pstmt.setString(5, ensDec.getPaymentMethod());
                            pstmt.setString(6, ensDec.getDeclarantName());
                            pstmt.setString(7, ensDec.getDeclarantAddress());
                            pstmt.setString(8, ensDec.getDeclarantAddress1());
                            pstmt.setString(9, ensDec.getDeclarantCountry());
                            pstmt.setString(10, ensDec.getDeclarantState());
                            pstmt.setString(11, ensDec.getDeclarantTelephone());
                            pstmt.setString(12, ensDec.getDeclarantEmail());
                            pstmt.setString(13, ensDec.getDeclarantEORI());
                            pstmt.setString(14, ensDec.getDeclarantFillingType());
                            pstmt.setString(15, ensDec.getDeclarantCity());
                            pstmt.setString(16, ensDec.getDeclarantZipcode());

                            // Execute the insert statement
                            int rowsInserted = pstmt.executeUpdate();
                            if (rowsInserted > 0) {
                                System.out.println("[ECOM] EcomBookingBean Record inserted successfully into ecomsi_ics_new!");
                            }

                        }
                    }
                }
                // File concept
                FileOutputStream fos = null;
                boolean flag = false;
                String filepath = "";
                String zipFileName = "";
                ZipFileConversion zipconv = new ZipFileConversion();
                HashMap<String, byte[]> filehm = new HashMap<String, byte[]>();
                String fileUploadPath = null;
                boolean isconvertzip = ("Y".equalsIgnoreCase(eBookingMasterData.getEnableZipConversion()));
                System.out.println("[ECOM] EcomBooingBean isconvertzip=> " + isconvertzip + "  getEnableZipConversion=> " + eBookingMasterData.getEnableZipConversion());

                //
                String filename1 = fls.getVgmFileName();
                String filename2 = fls.getDgFileName();
                String filename3 = fls.getLolFileName();
                String filename4 = fls.getMsdsFileName();
                String filename5 = fls.getOthersFileName();
                String filename6 = fls.getVgmFileName1();
                String filename7 = fls.getVgmFileName2();
                String filename8 = fls.getDgFileName1();
                String filename9 = fls.getDgFileName2();
                String filename10 = fls.getLolFileName1();
                String filename11 = fls.getLolFileName2();
                String filename12 = fls.getMsdsFileName1();
                String filename13 = fls.getMsdsFileName2();
                String filename14 = fls.getOthersFileName1();
                String filename15 = fls.getOthersFileName2();

                fls.setFilePath(fls.getFilePath() + book_refno);
                fileUploadPath = (fls.getFilePath());
                System.out.println("[ECOM] EcomBooingBean fls.isFileAvailable()===> " + fls.isFileAvailable() + " fls.getFilePath()===> " + fls.getFilePath());

                if (fls.isFileAvailable()) {
                    File directory = new File(fls.getFilePath());
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                }
                String deletedfile = eBookingMasterData.getDeletedfilename();
                System.out.println("[ECOM] EcomBooingBean deletedfile==> " + deletedfile);
                if (deletedfile != null && deletedfile.length() > 0) {
                    deletedfile = deletedfile.substring(2, deletedfile.length());
                    ArrayList fileset = new ArrayList(Arrays.asList(deletedfile.split("~~")));
                    for (int i = 0; i < fileset.size(); i++) {
                        System.out.println("[ECOM] EcomBooingBean checkNull((String)iterator.next())==> " + checkNull((String) fileset.get(i)));
                        File dfile = new File(fls.getFilePath() + File.separator + checkNull((String) fileset.get(i)));
                        FileUtils.forceDelete(dfile);
                    }
                }
                if (fls.getVgmFileData() != null && fls.getVgmFileName() != null && fls.getVgmFileName().length() > 0) {
                    filename1 = EcomConsts.FILETYPE_VMG + "_" + fls.getVgmFileName();
                    if (isconvertzip) {
                        filehm.put(filename1, fls.getVgmFileData());
                    } else {
                        File Ffilepath1 = new File(fls.getFilePath(), filename1);
                        fos = new FileOutputStream(Ffilepath1);
                        fos.write(fls.getVgmFileData());
                    }
                    flag = true;
                }
                if (fls.getVgmFileData1() != null && fls.getVgmFileName1() != null && fls.getVgmFileName1().length() > 0) {
                    filename6 = EcomConsts.FILETYPE_VMG + "_" + fls.getVgmFileName1();
                    if (isconvertzip) {
                        filehm.put(filename6, fls.getVgmFileData1());
                    } else {
                        File Ffilepath1 = new File(fls.getFilePath(), filename6);
                        fos = new FileOutputStream(Ffilepath1);
                        fos.write(fls.getVgmFileData1());
                    }
                    flag = true;
                }
                if (fls.getVgmFileData2() != null && fls.getVgmFileName2() != null && fls.getVgmFileName2().length() > 0) {
                    filename7 = EcomConsts.FILETYPE_VMG + "_" + fls.getVgmFileName2();
                    if (isconvertzip) {
                        filehm.put(filename7, fls.getVgmFileData2());
                    } else {
                        File Ffilepath1 = new File(fls.getFilePath(), filename7);
                        fos = new FileOutputStream(Ffilepath1);
                        fos.write(fls.getVgmFileData2());
                    }
                    flag = true;
                }
                if (filehm != null && filehm.size() > 0) {
                    zipconv.convertZipFile(EcomConsts.FILETYPE_VMG + "_" + book_refno, fileUploadPath, filehm);
                    zipFileName = zipFileName + EcomConsts.FILETYPE_VMG + "_" + book_refno + ".zip" + "~~~";
                }
                filehm = new HashMap<String, byte[]>();
                if (fls.getDgFileData() != null && fls.getDgFileName() != null && fls.getDgFileName().length() > 0) {
                    filename2 = EcomConsts.FILETYPE_DGC + "_" + fls.getDgFileName();
                    if (isconvertzip) {
                        filehm.put(filename2, fls.getDgFileData());
                    } else {
                        File Ffilepath2 = new File(fls.getFilePath(), filename2);
                        fos = new FileOutputStream(Ffilepath2);
                        fos.write(fls.getDgFileData());
                    }
                    flag = true;
                }
                if (fls.getDgFileData1() != null && fls.getDgFileName1() != null && fls.getDgFileName1().length() > 0) {
                    filename8 = EcomConsts.FILETYPE_DGC + "_" + fls.getDgFileName1();
                    if (isconvertzip) {
                        filehm.put(filename8, fls.getDgFileData1());
                    } else {
                        File Ffilepath2 = new File(fls.getFilePath(), filename8);
                        fos = new FileOutputStream(Ffilepath2);
                        fos.write(fls.getDgFileData1());
                    }
                    flag = true;
                }
                if (fls.getDgFileData2() != null && fls.getDgFileName2() != null && fls.getDgFileName2().length() > 0) {
                    filename9 = EcomConsts.FILETYPE_DGC + "_" + fls.getDgFileName2();
                    if (isconvertzip) {
                        filehm.put(filename9, fls.getDgFileData2());
                    } else {
                        File Ffilepath2 = new File(fls.getFilePath(), filename9);
                        fos = new FileOutputStream(Ffilepath2);
                        fos.write(fls.getDgFileData2());
                    }
                    flag = true;
                }
                if (filehm != null && filehm.size() > 0) {
                    zipconv.convertZipFile(EcomConsts.FILETYPE_DGC + "_" + book_refno, fileUploadPath, filehm);
                    zipFileName = zipFileName + EcomConsts.FILETYPE_DGC + "_" + book_refno + ".zip" + "~~~";
                }
                filehm = new HashMap<String, byte[]>();
                if (fls.getLolFileData() != null && fls.getLolFileName() != null && fls.getLolFileName().length() > 0) {
                    filename3 = EcomConsts.FILETYPE_LOL + "_" + fls.getLolFileName();
                    if (isconvertzip) {
                        filehm.put(filename3, fls.getLolFileData());
                    } else {
                        File Ffilepath3 = new File(fls.getFilePath(), filename3);
                        fos = new FileOutputStream(Ffilepath3);
                        fos.write(fls.getLolFileData());
                    }
                    flag = true;

                }
                if (fls.getLolFileData1() != null && fls.getLolFileName1() != null && fls.getLolFileName1().length() > 0) {
                    filename10 = EcomConsts.FILETYPE_LOL + "_" + fls.getLolFileName1();
                    if (isconvertzip) {
                        filehm.put(filename10, fls.getLolFileData1());
                    } else {
                        File Ffilepath3 = new File(fls.getFilePath(), filename10);
                        fos = new FileOutputStream(Ffilepath3);
                        fos.write(fls.getLolFileData1());
                    }
                    flag = true;

                }
                if (fls.getLolFileData2() != null && fls.getLolFileName2() != null && fls.getLolFileName2().length() > 0) {
                    filename11 = EcomConsts.FILETYPE_LOL + "_" + fls.getLolFileName2();
                    if (isconvertzip) {
                        filehm.put(filename11, fls.getLolFileData2());
                    } else {
                        File Ffilepath3 = new File(fls.getFilePath(), filename11);
                        fos = new FileOutputStream(Ffilepath3);
                        fos.write(fls.getLolFileData2());
                    }
                    flag = true;

                }
                if (filehm != null && filehm.size() > 0) {
                    zipconv.convertZipFile(EcomConsts.FILETYPE_LOL + "_" + book_refno, fileUploadPath, filehm);
                    zipFileName = zipFileName + EcomConsts.FILETYPE_LOL + "_" + book_refno + ".zip" + "~~~";
                }
                filehm = new HashMap<String, byte[]>();
                if (fls.getMsdsFileData() != null && fls.getMsdsFileName() != null && fls.getMsdsFileName().length() > 0) {
                    filename4 = EcomConsts.FILETYPE_MSD + "_" + fls.getMsdsFileName();
                    if (isconvertzip) {
                        filehm.put(filename4, fls.getMsdsFileData());
                    } else {
                        File Ffilepath4 = new File(fls.getFilePath(), filename4);
                        fos = new FileOutputStream(Ffilepath4);
                        fos.write(fls.getMsdsFileData());
                    }
                    flag = true;
                }
                if (fls.getMsdsFileData1() != null && fls.getMsdsFileName1() != null && fls.getMsdsFileName1().length() > 0) {
                    filename12 = EcomConsts.FILETYPE_MSD + "_" + fls.getMsdsFileName1();
                    if (isconvertzip) {
                        filehm.put(filename12, fls.getMsdsFileData1());
                    } else {
                        File Ffilepath4 = new File(fls.getFilePath(), filename12);
                        fos = new FileOutputStream(Ffilepath4);
                        fos.write(fls.getMsdsFileData1());
                    }
                    flag = true;
                }
                if (fls.getMsdsFileData2() != null && fls.getMsdsFileName2() != null && fls.getMsdsFileName2().length() > 0) {
                    filename13 = EcomConsts.FILETYPE_MSD + "_" + fls.getMsdsFileName2();
                    if (isconvertzip) {
                        filehm.put(filename13, fls.getMsdsFileData2());
                    } else {
                        File Ffilepath4 = new File(fls.getFilePath(), filename13);
                        fos = new FileOutputStream(Ffilepath4);
                        fos.write(fls.getMsdsFileData2());
                    }
                    flag = true;
                }
                if (filehm != null && filehm.size() > 0) {
                    zipconv.convertZipFile(EcomConsts.FILETYPE_MSD + "_" + book_refno, fileUploadPath, filehm);
                    zipFileName = zipFileName + EcomConsts.FILETYPE_MSD + "_" + book_refno + ".zip" + "~~~";
                }
                filehm = new HashMap<String, byte[]>();
                if (fls.getOthersFileData() != null && fls.getOthersFileName() != null && fls.getOthersFileName().length() > 0) {
                    filename5 = EcomConsts.FILETYPE_OTHERS + "_" + fls.getOthersFileName();
                    if (isconvertzip) {
                        filehm.put(filename5, fls.getOthersFileData());
                    } else {
                        File Ffilepath5 = new File(fls.getFilePath(), filename5);
                        fos = new FileOutputStream(Ffilepath5);
                        fos.write(fls.getOthersFileData());
                    }
                    flag = true;
                }
                if (fls.getOthersFileData1() != null && fls.getOthersFileName1() != null && fls.getOthersFileName1().length() > 0) {
                    filename14 = EcomConsts.FILETYPE_OTHERS + "_" + fls.getOthersFileName1();
                    if (isconvertzip) {
                        filehm.put(filename14, fls.getOthersFileData1());
                    } else {
                        File Ffilepath5 = new File(fls.getFilePath(), filename14);
                        fos = new FileOutputStream(Ffilepath5);
                        fos.write(fls.getOthersFileData1());

                    }
                    flag = true;
                }
                if (fls.getOthersFileData2() != null && fls.getOthersFileName2() != null && fls.getOthersFileName2().length() > 0) {
                    filename15 = EcomConsts.FILETYPE_OTHERS + "_" + fls.getOthersFileName2();
                    if (isconvertzip) {
                        filehm.put(filename15, fls.getOthersFileData2());
                    } else {
                        File Ffilepath5 = new File(fls.getFilePath(), filename15);
                        fos = new FileOutputStream(Ffilepath5);
                        fos.write(fls.getOthersFileData2());
                    }
                    flag = true;
                }
                if (filehm != null && filehm.size() > 0) {
                    zipconv.convertZipFile(EcomConsts.FILETYPE_OTHERS + "_" + book_refno, fileUploadPath, filehm);
                    zipFileName = zipFileName + EcomConsts.FILETYPE_OTHERS + "_" + book_refno + ".zip" + "~~~";
                }

                if (flag) {
                    if (fos != null) {
                        fos.flush();
                        fos.close();
                    }
                }
                pstmt = con.prepareStatement(SAVE_EBOOKING_FILE);
                if (fls.getVgmFileName() != null && fls.getVgmFileName().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename1);
                    pstmt.setString(3, EcomConsts.FILETYPE_VMG);
                    pstmt.addBatch();
                    filepath += filename1 + "~~~";
                }
                if (fls.getVgmFileName1() != null && fls.getVgmFileName1().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename6);
                    pstmt.setString(3, EcomConsts.FILETYPE_VMG + "1");
                    pstmt.addBatch();
                    filepath += filename6 + "~~~";
                }
                if (fls.getVgmFileName2() != null && fls.getVgmFileName2().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename7);
                    pstmt.setString(3, EcomConsts.FILETYPE_VMG + "2");
                    pstmt.addBatch();
                    filepath += filename7 + "~~~";
                }
                //
                if (fls.getDgFileName() != null && fls.getDgFileName().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename2);
                    pstmt.setString(3, EcomConsts.FILETYPE_DGC);
                    pstmt.addBatch();
                    filepath += filename2 + "~~~";
                }
                if (fls.getDgFileName1() != null && fls.getDgFileName1().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename8);
                    pstmt.setString(3, EcomConsts.FILETYPE_DGC + "1");
                    pstmt.addBatch();
                    filepath += filename8 + "~~~";
                }
                if (fls.getDgFileName2() != null && fls.getDgFileName2().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename9);
                    pstmt.setString(3, EcomConsts.FILETYPE_DGC + "2");
                    pstmt.addBatch();
                    filepath += filename9 + "~~~";
                }
                //
                if (fls.getLolFileName() != null && fls.getLolFileName().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename3);
                    pstmt.setString(3, EcomConsts.FILETYPE_LOL);
                    pstmt.addBatch();
                    filepath += filename3 + "~~~";
                }
                if (fls.getLolFileName1() != null && fls.getLolFileName1().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename10);
                    pstmt.setString(3, EcomConsts.FILETYPE_LOL + "1");
                    pstmt.addBatch();
                    filepath += filename10 + "~~~";
                }
                if (fls.getLolFileName2() != null && fls.getLolFileName2().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename11);
                    pstmt.setString(3, EcomConsts.FILETYPE_LOL + "2");
                    pstmt.addBatch();
                    filepath += filename11 + "~~~";
                }
                //
                if (fls.getMsdsFileName() != null && fls.getMsdsFileName().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename4);
                    pstmt.setString(3, EcomConsts.FILETYPE_MSD);
                    pstmt.addBatch();
                    filepath += filename4 + "~~~";
                }
                if (fls.getMsdsFileName1() != null && fls.getMsdsFileName1().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename12);
                    pstmt.setString(3, EcomConsts.FILETYPE_MSD + "1");
                    pstmt.addBatch();
                    filepath += filename12 + "~~~";
                }
                if (fls.getMsdsFileName2() != null && fls.getMsdsFileName2().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename13);
                    pstmt.setString(3, EcomConsts.FILETYPE_MSD + "2");
                    pstmt.addBatch();
                    filepath += filename13 + "~~~";
                }
                //
                if (fls.getOthersFileName() != null && fls.getOthersFileName().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename5);
                    pstmt.setString(3, EcomConsts.FILETYPE_OTHERS);
                    pstmt.addBatch();
                    filepath += filename5 + "~~~";
                }
                if (fls.getOthersFileName1() != null && fls.getOthersFileName1().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename14);
                    pstmt.setString(3, EcomConsts.FILETYPE_OTHERS + "1");
                    pstmt.addBatch();
                    filepath += filename14 + "~~~";
                }
                if (fls.getOthersFileName2() != null && fls.getOthersFileName2().length() > 0) {
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, filename15);
                    pstmt.setString(3, EcomConsts.FILETYPE_OTHERS + "2");
                    pstmt.addBatch();
                    filepath += filename15 + "~~~";
                }
                if (isconvertzip) {
                    if (zipFileName != null && zipFileName.length() > 3) {
                        filepath = zipFileName.substring(0, zipFileName.length() - 3);
                    }
                }
                System.out.println("[ECOM] EcomBooingBean >>filepath>>" + filepath);
                // pstmt.executeBatch();
                //

                if (con != null) {
                    con.close();
                }
                StringBuffer hssb = new StringBuffer();
                StringBuffer otherssb = new StringBuffer();
                StringBuffer dgsb = new StringBuffer();

                if ("S".equalsIgnoreCase(savetype)) {
                    msgtype = "insert";
                    if ("1".equalsIgnoreCase(appVersion)) {
                        rvo = v1BKGBeanObj.saveToBooking(eBookingMasterData, all, custcode, custname, book_refno, savetype, voidCargoListObj, voidComListObj, eqpQuantity, client_code, slottype);
                    } else {
                        ebook_status = "S";
                        bookSAVETYPE = "S";
                        String ediid = null;
                        List lst = null;
                        EcomBKGMasterVO vc = null;
                        EcomBKGReference rf = null;
                        EcomCommonModel cm = null;
                        EcomBKGRemarks rm = null;
                        EcomBKGCustomer cust = null;
                        EcomBKGCommodity cmd = null;
                        EcomBKGReaderMaster edirm = null;
                        EcomBKGMasterVO mst = null;
                        EcomBKGHaulage haul = null;
                        EcomBKGConf conf = null;
                        con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                        String readerid = "";
                        // String lclflag = "NR"; //editempbooking - shipment_type
                        size = all.size();
                        if (all != null && size > 0) {
                            for (int i = 0; i < size; i++) {
                                if (i == 0) {
                                    cargolist = (ArrayList) all.get(i);
                                } else {
                                    routinglist = (ArrayList) all.get(i);
                                }
                            }
                        }
                        //
                        service = eBookingMasterData.getEbook_servicecode();
                        vessel = eBookingMasterData.getEbook_vesselcode();
                        voyage = eBookingMasterData.getEbook_voyage();
                        poleta = eBookingMasterData.getEbook_pol_eta();
                        poletd = eBookingMasterData.getEbook_pol_etd();
                        bound = eBookingMasterData.getEbook_bound_leg();
                        callid = eBookingMasterData.getGroupcallid();

                        // agencycode = getAgencydetails(eBookingMasterData.getEbook_origin());
                        // if (multiAgencyid != null && multiAgencyid.length() > 0) {
                        // agencycode = multiAgencyid;
                        // }
                        // System.out.println("[ECOM] EcomBooingBean =======agencycode======>" +
                        // agencycode);
                        //
                        readerid = bookNoGenerate("EDI");
                        System.out.println("[ECOM] EcomBooingBean >>readerid>>" + readerid);
                        if (readerid != null && readerid.length() > 0) {

                            // Rate reference
                            lst = new ArrayList();
                            rf = new EcomBKGReference();
                            cm = new EcomCommonModel();
                            rf.setBook_no(book_refno);
                            rf.setRefid("DAK");
                            rf.setRefval(book_refno);
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            lst.add(rf);
                            cm.setEditempbooking_reference(lst);
                            Map hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                            System.out.println("[ECOM] EcomBooingBean ===>>>" + hs);
                            //
                            String nat = checkNull(eBookingMasterData.getEbook_natcode()); // NAT Code
                            if (nat != null && nat.length() > 0) {
                                // NAT Code
                                lst = new ArrayList();
                                rf = new EcomBKGReference();
                                cm = new EcomCommonModel();
                                rf.setBook_no(book_refno);
                                rf.setRefid("NAT");
                                rf.setRefval(checkNull(eBookingMasterData.getEbook_natcode()));
                                rf.setIsedited("N");
                                rf.setEdireaderid(readerid);
                                rf.setVersion("0");
                                lst.add(rf);
                                cm.setEditempbooking_reference(lst);
                                hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                System.out.println("[ECOM] EcomBooingBean ===>>>" + hs);
                            }
                            String haulerCode = checkNull(eBookingMasterData.getEbook_haulercode()); // NAT Code
                            if (haulerCode != null && haulerCode.length() > 0) {
                                // NAT Code
                                lst = new ArrayList();
                                rf = new EcomBKGReference();
                                cm = new EcomCommonModel();
                                rf.setBook_no(book_refno);
                                rf.setRefid("HAU");
                                rf.setRefval(checkNull(haulerCode));
                                rf.setIsedited("N");
                                rf.setEdireaderid(readerid);
                                rf.setVersion("0");
                                lst.add(rf);
                                cm.setEditempbooking_reference(lst);
                                hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                System.out.println("[ECOM] EcomBooingBean ===>>>" + hs);
                            }
                            // Insert haulge type and picup&drop date in reference
                            String haulgeType = checkNull(eBookingMasterData.getEbook_haulagetype());
                            String pickUpDate = checkNull(eBookingMasterData.getEbook_pickup_date());
                            String dropDate = checkNull(eBookingMasterData.getEbook_drop_date());
                            String strDbFormatTime = "yyyy-MM-dd HH:mm:ss";
                            String strDbFormat = "yyyy-MM-dd";
                            SimpleDateFormat source = new SimpleDateFormat(strDbFormatTime);
                            SimpleDateFormat traget = new SimpleDateFormat(strDbFormat);
                            if (haulgeType != null && haulgeType.length() > 0) {
                                // NAT Code
                                lst = new ArrayList();
                                rf = new EcomBKGReference();
                                cm = new EcomCommonModel();
                                rf.setBook_no(book_refno);
                                rf.setRefid("ETH");
                                rf.setRefval(checkNull(haulgeType) + "~~" + checkNull(eBookingMasterData.getEbook_haulageDesc()));
                                rf.setIsedited("N");
                                rf.setEdireaderid(readerid);
                                rf.setVersion("0");
                                lst.add(rf);
                                cm.setEditempbooking_reference(lst);
                                hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                System.out.println("[ECOM] EcomBooingBean ===>>>" + hs);
                            }
                            if (pickUpDate != null && pickUpDate.length() > 0) {
                                pickUpDate = traget.format(source.parse(pickUpDate));
                                lst = new ArrayList();
                                rf = new EcomBKGReference();
                                cm = new EcomCommonModel();
                                rf.setBook_no(book_refno);
                                rf.setRefid("ELD");
                                rf.setRefval(checkNull(pickUpDate));
                                rf.setIsedited("N");
                                rf.setEdireaderid(readerid);
                                rf.setVersion("0");
                                lst.add(rf);
                                cm.setEditempbooking_reference(lst);
                                hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                System.out.println("[ECOM] EcomBooingBean ===>>>" + hs);
                            }
                            if (dropDate != null && dropDate.length() > 0) {
                                dropDate = traget.format(source.parse(dropDate));
                                lst = new ArrayList();
                                rf = new EcomBKGReference();
                                cm = new EcomCommonModel();
                                rf.setBook_no(book_refno);
                                rf.setRefid("EDD");
                                rf.setRefval(checkNull(dropDate));
                                rf.setIsedited("N");
                                rf.setEdireaderid(readerid);
                                rf.setVersion("0");
                                lst.add(rf);
                                cm.setEditempbooking_reference(lst);
                                hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                System.out.println("[ECOM] EcomBooingBean ===>>>" + hs);
                            }

                            if (checkNull(eBookingMasterData.getEbook_rate_reference()).length() > 0) {
                                //
                                lst = new ArrayList();
                                rf = new EcomBKGReference();
                                cm = new EcomCommonModel();
                                rf.setBook_no(book_refno);
                                rf.setRefid("EOR");
                                rf.setRefval(eBookingMasterData.getEbook_rate_reference());
                                rf.setIsedited("N");
                                rf.setEdireaderid(readerid);
                                rf.setVersion("0");
                                lst.add(rf);
                                cm.setEditempbooking_reference(lst);
                                hs = (Map) ecomvdsservice.seteditempbookingreference(cm);

                            }

                            if (checkNull(eBookingMasterData.getEbook_exportref()).length() > 0) {
                                //
                                lst = new ArrayList();
                                rf = new EcomBKGReference();
                                cm = new EcomCommonModel();
                                rf.setBook_no(book_refno);
                                System.out.println("[ECOM] EcomBooingBean exprefvalue" + EcomConsts.EXPORT_REF);
                                rf.setRefid(EcomConsts.EXPORT_REF);
                                rf.setRefval(eBookingMasterData.getEbook_exportref());
                                rf.setIsedited("N");
                                rf.setEdireaderid(readerid);
                                rf.setVersion("0");
                                lst.add(rf);
                                cm.setEditempbooking_reference(lst);
                                hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                System.out.println("[ECOM] EcomBooingBean ===>>>HSVALUE" + hs);
                            }
                            if (checkNull(eBookingMasterData.getEbook_custpo()).length() > 0) {
                                //
                                lst = new ArrayList();
                                rf = new EcomBKGReference();
                                cm = new EcomCommonModel();
                                rf.setBook_no(book_refno);
                                System.out.println("[ECOM] EcomBooingBean custpovalue" + EcomConsts.CUST_PO);
                                rf.setRefid(EcomConsts.CUST_PO);
                                rf.setRefval(eBookingMasterData.getEbook_custpo());
                                rf.setIsedited("N");
                                rf.setEdireaderid(readerid);
                                rf.setVersion("0");
                                lst.add(rf);
                                cm.setEditempbooking_reference(lst);
                                hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                System.out.println("[ECOM] EcomBooingBean ===>>>HSVALUE" + hs);
                            }
                            if (checkNull(eBookingMasterData.getCustomerreference()).length() > 0) {
                                //
                                lst = new ArrayList();
                                rf = new EcomBKGReference();
                                cm = new EcomCommonModel();
                                rf.setBook_no(book_refno);
                                System.out.println("[ECOM] EcomBooingBean getCustomerreference" + eBookingMasterData.getCustomerreference());
                                rf.setRefid(EcomConsts.CUST_REF);
                                rf.setRefval(eBookingMasterData.getCustomerreference());
                                rf.setIsedited("N");
                                rf.setEdireaderid(readerid);
                                rf.setVersion("0");
                                lst.add(rf);
                                cm.setEditempbooking_reference(lst);
                                hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                System.out.println("[ECOM] EcomBooingBean ===>>>CUST_REF" + hs);
                            }
                            if (checkNull(eBookingMasterData.getACID()).length() > 0) {
                                //
                                lst = new ArrayList();
                                rf = new EcomBKGReference();
                                cm = new EcomCommonModel();
                                rf.setBook_no(book_refno);
                                rf.setRefid(EcomConsts.ACID);
                                rf.setRefval(eBookingMasterData.getACID());
                                rf.setIsedited("N");
                                rf.setEdireaderid(readerid);
                                rf.setVersion("0");
                                lst.add(rf);
                                cm.setEditempbooking_reference(lst);
                                hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                System.out.println("[ECOM] EcomBooingBean ===>>>CUST_REF" + hs);
                            }
                            String dpwCode = checkNull(eBookingMasterData.getShipperReference());
                            String dpwType = checkNull(eBookingMasterData.getShipperReferenceType());
                            String dpwRefval = dpwType + dpwCode;
                            if (dpwCode.length() > 0 && !"ullnull".equalsIgnoreCase(dpwCode) && !"nullnull".equalsIgnoreCase(dpwRefval)) {
                                //
                                lst = new ArrayList();
                                rf = new EcomBKGReference();
                                cm = new EcomCommonModel();
                                rf.setBook_no(book_refno);
                                rf.setRefid("DPW");
                                rf.setRefval(dpwRefval);
                                rf.setIsedited("N");
                                rf.setEdireaderid(readerid);
                                rf.setVersion("0");
                                lst.add(rf);
                                cm.setEditempbooking_reference(lst);
                                hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                System.out.println("[ECOM] EcomBooingBean ===>>>CUST_REF" + hs);
                            }

                            String routeID = eBookingMasterData.getEbook_routeid();

                            System.out.println("[ECOM] EcomBooingBean ==>routeID==>" + routeID);
                            Map rout = null;
                            rout = (Map) ecomvdsservice.getecombkgroutingserivice(routeID);
                            Gson gson = new Gson();
                            List routlst = gson.fromJson(gson.toJson((ArrayList) rout.get("routedetail")), new TypeToken<List<RouteVo>>() {
                            }.getType());
                            String Origin = "", Blpol = "", BlpolTer = "", BlpolSVC = "", BlpolMode = "", Trans1 = "";
                            String Trans1Ter = "", Trans1SVC = "", Trans1Mode = "", Trans2 = "", Trans2Ter = "",
                                    Trans2SVC = "", Trans2Mode = "";
                            String Trans3 = "", Trans3Ter = "", Trans3SVC = "", Trans3Mode = "", Blpod = "",
                                    BlpodTer = "", Delivery = "", BlpodMode = "";
                            String Service = "", DefRoute = "", Routing_id = "", Feeder = "", originmoe = "",
                                    opstrans1Mode = "", opstrans2Mode = "";
                            if (routlst.size() > 0) {
                                RouteVo rst = (RouteVo) routlst.get(0);
                                Origin = rst.getOrigin();

                                originmoe = rst.getOrgmode();
                                System.out.println("[ECOM] EcomBooingBean >>>rst.getOrgmode()>>>" + rst.getOrgmode());
                                Blpol = rst.getBlpol();
                                BlpolTer = rst.getBlpolter();
                                BlpolSVC = rst.getBlpolsvc();
                                BlpolMode = rst.getMode();
                                System.out.println("[ECOM] EcomBooingBean BlpolMode>>" + BlpolMode);
                                Trans1 = rst.getOpspol1();
                                Trans1Ter = rst.getOpspol1ter();
                                Trans1SVC = rst.getOpspolsvc1();
                                Trans1Mode = rst.getOpsmode1();
                                Trans2 = rst.getOpspol2();
                                Trans2Ter = rst.getOpspol2ter();
                                Trans2SVC = rst.getOpspolsvc2();
                                Trans2Mode = rst.getOpsmode2();
                                Trans3 = rst.getOpspol3();
                                Trans3Ter = rst.getOpspol3ter();
                                Trans3SVC = rst.getOpspolsvc3();
                                Trans3Mode = rst.getOpsmode3();
                                Blpod = rst.getBlpod();
                                BlpodTer = rst.getBlpodter();
                                BlpodMode = rst.getDelmode();
                                Delivery = rst.getDelivery();
                                Service = rst.getMainsvc();
                                DefRoute = rst.getDefroute();
                                Routing_id = rst.getId();
                                opstrans1Mode = rst.getOpstrans1mde();
                                opstrans2Mode = rst.getOpstrans2mde();
                            }
                            //
                            polterminal = BlpolTer;
                            podterminal = BlpodTer;
                            //
                            System.out.println("[ECOM] EcomBooingBean  MainService " + Service);
                            String oceanVslCode = "", oceanVoyage = "", oceanSerCode = "", oceanBound = "",
                                    oceanSerCode_temp = "";
                            if (routinglist != null && routinglist.size() > 0) {
                                int len = routinglist.size();
                                for (int k = 0; k < len; k++) {
                                    eBookingrouting = (Route) routinglist.get(k);
                                    oceanSerCode_temp = checkNull(eBookingrouting.getService());
                                    if (oceanSerCode_temp != null && oceanSerCode_temp.length() > 0) {
                                        if (Service.equalsIgnoreCase(oceanSerCode_temp)) {
                                            oceanSerCode = checkNull(eBookingrouting.getService());
                                            oceanVslCode = eBookingrouting.getVessel();
                                            oceanVoyage = eBookingrouting.getVoyage();
                                            oceanBound = eBookingrouting.getBound();
                                            break;
                                        }
                                    }
                                }
                            }
                            //
                            String addremarks = "";
                            String salesRateAgreed = checkNull(eBookingMasterData.getSalesRateAgree());
                            if (salesRateAgreed.length() > 0) {
                                addremarks = "Sales Rep. & Rate Agreed : " + salesRateAgreed + "\n";
                            }
                            Map carrierMap = null;
                            List carrierTermList = null;
                            String carrierTermCode = null;
                            if (eBookingMasterData.getRemarks() != null) {
                                addremarks = addremarks + eBookingMasterData.getRemarks().replace("\r", "");
                            }
                            System.out.println("[ECOM] EcomBooingBean  addremarks " + addremarks);
                            // GET CARRIER TERM
                            if (eBookingMasterData.getCarrierContact() != null && eBookingMasterData.getCarrierContact().length() > 0) {
                                carrierMap = ecomvdsservice.getEcomCarrierTerm(eBookingMasterData.getCarrierContact());
                                System.out.println("[ECOM] EcomBooingBean  carrierMap " + carrierMap);
                                if (carrierMap != null) {
                                    carrierTermList = (List) carrierMap.get("transportservice");
                                    if (carrierTermList != null && carrierTermList.size() > 0) {
                                        carrierMap = (Map) carrierTermList.get(0);
                                        carrierTermCode = (String) carrierMap.get("trptcode");
                                    }
                                }
                            }

                            // QUERY- BOOKING_MASTER
                            feeder = getServiceName(service);
                            // Master Data saved
                            lst = new ArrayList();
                            mst = new EcomBKGMasterVO();
                            cm = new EcomCommonModel();
                            mst.setBook_no(book_refno);
                            mst.setAgencyid(bookingagencyid);
                            mst.setOceanvslcode(oceanVslCode);
                            mst.setOceanvoyage(oceanVoyage);
                            mst.setOceansercode(oceanSerCode);
                            mst.setOceanbound(oceanBound);
                            mst.setServicecode(service);
                            mst.setVesselcode(vessel);
                            mst.setVoyagecode(voyage);
                            mst.setBound(bound);
                            mst.setOriginportid(eBookingMasterData.getEbook_origin());
                            mst.setFinalportid(eBookingMasterData.getEbook_delivery());
                            mst.setLoadportid(eBookingMasterData.getEbook_portofloading());
                            mst.setDischargeportid(eBookingMasterData.getEbook_portofdischarge());
                            mst.setPolterminal(polterminal);
                            if (poletd != null && poletd.length() > 0 && (!poletd.equalsIgnoreCase("-"))) {
                                mst.setPolat(poletd);
                            } else {
                                mst.setPolat(null);
                            }
                            mst.setPodterminal(podterminal);
                            if (poleta != null && poleta.length() > 0 && (!poleta.equalsIgnoreCase("-"))) {
                                mst.setPodat(poleta);
                            } else {
                                mst.setPodat(null);
                            }
                            if (eBookingMasterData.getEbook_date().length() > 0) {
                                mst.setBookready(eBookingMasterData.getEbook_date());
                            } else {
                                mst.setBookready(null);
                            }
                            mst.setPrecarriage(haulage_org);
                            mst.setOncarriage(haulage_des);
                            mst.setStatus("D");
                            mst.setUsercr(EcomConsts.SET_CREATER_USER);
                            mst.setMessagetype(GeneralConf.EDI_MESSAGETYPE_BKG);
                            mst.setTranstype("9");
                            mst.setEdireaderid(readerid);
                            mst.setDocumentalagencyid(documentagencyid);
                            mst.setAgencyissueid(agencyissuedid);
                            mst.setAgencypolid(polagencyid);
                            mst.setRemarks("");
                            mst.setFirstsvctype(feeder);
                            mst.setBooktype(EcomConsts.SET_BOOK_TYPE);
                            mst.setCrossBook("");
                            String strOrigin = eBookingMasterData.getEbook_origin_country().substring(7, eBookingMasterData.getEbook_origin_country().length());
                            String strDeli = eBookingMasterData.getEbook_delivery_country().substring(7, eBookingMasterData.getEbook_delivery_country().length());
                            mst.setOriginportdesc(strOrigin.trim());
                            mst.setFinalportdesc(strDeli.trim());
                            mst.setShipment_type(shipment_type);
                            mst.setPaymenttermsid(eBookingMasterData.getEbook_shipment());
                            mst.setBooksource(EcomConsts.SET_BOOK_SOURCE);
                            mst.setMessagesender(EcomConsts.SET_MESSAGE_SENDER);
                            mst.setCarriercode(EcomConsts.SET_CARRIER_CODE);
                            mst.setCallid(slottype);
                            mst.setCustremarks(addremarks);
                            mst.setAgencyrefno(checkNullUpperCase(eBookingMasterData.getEbook_agency_reference()));
                            mst.setTransportserviceid((eBookingMasterData.getCarrierContact()));
                            mst.setTransporttermid(checkNull(carrierTermCode));
                            mst.setTbook_no("");
                            mst.setIntransitport(checkNull(eBookingMasterData.getPodintransit()));
                            mst.setIntransitportname(checkNull(eBookingMasterData.getHidPodIntransitName()));
                            if (eBookingMasterData.getCargoprotectid() != null && eBookingMasterData.getCargoprotectid().length() > 0) {
                                mst.setCargoprotectid(eBookingMasterData.getCargoprotectid());
                            }
                            mst.setVersion("0");
                            mst.setEnsfiling(checkNull(eBookingMasterData.getEnsfiling()));
                            mst.setT2lfiling(checkNull(eBookingMasterData.getEbook_t2lfiling()));
                            mst.setEnsdoctype(checkNull(eBookingMasterData.getEbook_EnsDocType()));
                            lst.add(mst);
                            cm.setEditempbooking(lst);
                            hs = (Map) ecomvdsservice.setecomeditempbooking(cm);
                            System.out.println("[ECOM] EcomBooingBean ===>>>" + hs);
                            // End the master data
                            // Query - BOOKING_ROUTING
                            lst = new ArrayList();
                            EcomBKGRouting rut = new EcomBKGRouting();
                            cm = new EcomCommonModel();
                            rut.setOrigin(eBookingMasterData.getEbook_origin());
                            rut.setOriginmode(originmoe);
                            rut.setPol(Blpol);
                            rut.setPolter(BlpolTer);
                            rut.setPolser(BlpolSVC);
                            rut.setPolmode(BlpolMode);
                            rut.setTrans1(Trans1);
                            rut.setTrans1ter(Trans1Ter);
                            rut.setTrans1ser(Trans1SVC);
                            rut.setTrans1mode(Trans1Mode);
                            rut.setTrans2(Trans2);
                            rut.setTrans2ter(Trans2Ter);
                            rut.setTrans2ser(Trans2SVC);
                            rut.setTrans2mode(Trans2Mode);
                            rut.setTrans3(Trans3);
                            rut.setTrans3ter(Trans3Ter);
                            rut.setTrans3ser(Trans3SVC);
                            rut.setTrans3mode(Trans3Mode);
                            rut.setPod(Blpod);
                            rut.setPodter(BlpodTer);
                            rut.setPodmode(BlpodMode);
                            rut.setDelivery(eBookingMasterData.getEbook_delivery());
                            rut.setSvccode(Service);
                            rut.setBook_no(book_refno);
                            rut.setEdireaderid(readerid);
                            rut.setDefrout(DefRoute);
                            rut.setOpstrans1mde(opstrans1Mode);
                            rut.setOpstrans2mde(opstrans2Mode);
                            if (checkNull(routeID).length() > 0) {
                                rut.setRouteid(Integer.parseInt(checkNull(routeID)));
                            }
                            lst.add(rut);
                            cm.setEditempbooking_routing(lst);
                            hs = (Map) ecomvdsservice.setecomeditempbkgroutinginfo(cm);

                            // Query - booking_leg_editemp
                            lst = new ArrayList();
                            EcomBKGLeg leg = null;
                            Boolean isInland = false;
                            Route finalRouteVO = null;
                            cm = new EcomCommonModel();
                            if (routinglist != null && routinglist.size() > 0) {
                                int len = routinglist.size();
                                int leg_no = 1, prevLeg = 0;
                                for (int k = 0; k < len; k++) {
                                    leg = new EcomBKGLeg();
                                    eBookingrouting = (Route) routinglist.get(k);
                                    finalRouteVO = (Route) routinglist.get(len - 1);
                                    String pol = eBookingrouting.getLoadport();
                                    String pod = eBookingrouting.getDisport();
                                    String legTypeId = eBookingrouting.getLegType();
                                    String dbound = eBookingrouting.getDischargebound();
                                    polterminal = eBookingrouting.getBlpolTer();
                                    String podTerminal = eBookingrouting.getBlpodTer();
                                    service = eBookingrouting.getService();
                                    vessel = eBookingrouting.getVessel();
                                    voyage = eBookingrouting.getVoyage();
                                    poleta = eBookingrouting.getETA1();
                                    poletd = eBookingrouting.getETD1();
                                    bound = eBookingrouting.getBound();
                                    preCarriage = eBookingMasterData.getPreCarriage();
                                    onCarriage = eBookingMasterData.getOnCarriage();
                                    String grp_CallId = eBookingrouting.getGrpCallId();
                                    String loadcallid = eBookingrouting.getLoadcallid();
                                    String dischargcallid = eBookingrouting.getDischargecallid();
                                    /*
                                     * if(checkNull(pod).equalsIgnoreCase(checkNull(eBookingMasterData.
                                     * getEbook_portofdischarge()))){
                                     * podTerminal=podterminal;
                                     * }
                                     */
                                    System.out.println("[ECOM] EcomBooingBean podTerminal: " + podTerminal
                                            + ", loadcallid: " + loadcallid + ", dischargcallid: " + dischargcallid
                                            + ", eBookingrouting LegType: " + eBookingrouting.getLegType()
                                            + ", load porttype: " + eBookingrouting.getLordporttype()
                                            + ", disch porttype: " + eBookingrouting.getDisporttype());
                                    // set load and discharge type
                                    if ("I".equalsIgnoreCase(eBookingrouting.getLordporttype()) || "I".equalsIgnoreCase(eBookingrouting.getDisporttype())) {
                                        loadType = "L";
                                        dischType = "L";
                                        isInland = true;
                                    } else if (k == 0 || (k == 1 && isInland)) {
                                        loadType = "L";
                                        dischType = "T";
                                    } else {
                                        loadType = "T";
                                        dischType = "T";
                                    }
                                    if (k == (len - 1) || ("I".equalsIgnoreCase(finalRouteVO.getDisporttype()) && k == (len - 2))) {
                                        dischType = "L";
                                    }
                                    //
                                    leg.setBook_no(book_refno);
                                    leg.setLeg_no(leg_no);
                                    leg.setBound(bound);
                                    leg.setServicecode(service);
                                    leg.setVesselcode(vessel);
                                    leg.setLoadportid(pol);
                                    leg.setDischargeportid(pod);
                                    leg.setEtddate(poletd); //
                                    leg.setEtadate(poleta); //
                                    leg.setLegtypeid(legTypeId);
                                    System.out.println("[ECOM] EcomBooingBean >>>eBookingMasterData.getOnCarriage()>>" + eBookingMasterData.getOnCarriage());
                                    leg.setPrecarriage(haulage_org);
                                    leg.setOncarriage(haulage_des);
                                    leg.setLoadtype(loadType);
                                    leg.setDischtype(dischType);
                                    leg.setVoyagecode(voyage);
                                    leg.setEdireaderid(readerid);
                                    leg.setVersion(0);
                                    leg.setPreviousleg(prevLeg);
                                    leg.setGroupcallid(grp_CallId);
                                    leg.setRouteid(routeID);
                                    leg.setCalliddischarge(dischargcallid);
                                    leg.setCallidloading(loadcallid);
                                    leg.setDbound(dbound);
                                    leg.setPolterminal(polterminal);
                                    leg.setPodterminal(podTerminal);
                                    lst.add(leg);
                                    leg_no++;
                                    prevLeg++;
                                }
                                cm.setEditempbooking_leg(lst);
                                hs = (Map) ecomvdsservice.setecomeditempbkgleginfo(cm);
                            }
                            //
                            // AP
                            lst = new ArrayList();
                            //
                            String cust_name = "";
                            cust_name = eBookingMasterData.getEbook_agreename();
                            if (cust_name.length() > 0) {
                                cust = new EcomBKGCustomer();
                                cm = new EcomCommonModel();
                                cust.setBook_no(book_refno);
                                cust.setCusttypeid("AP");
                                cust.setCustid(eBookingMasterData.getEbook_agreecode());
                                cust.setCountryid(eBookingMasterData.getEbook_agreecountry());
                                cust.setName(eBookingMasterData.getEbook_agreename());
                                cust.setAddress1(eBookingMasterData.getEbook_agreeaddress());
                                cust.setCity(eBookingMasterData.getEbook_agreecity());
                                cust.setZipCode(eBookingMasterData.getEbook_agreezipcode());
                                cust.setTelephone(eBookingMasterData.getEbook_agreephone());
                                cust.setFax(eBookingMasterData.getEbook_agreefax());
                                cust.setEmail(eBookingMasterData.getEbook_agreeemail());
                                cust.setEdireaderid(readerid);
                                cust.setAddress2(eBookingMasterData.getEbook_agreeaddress1());
                                cust.setStateid(splitMasterCode(eBookingMasterData.getEbook_agreestate()));
                                cust.setEori(eBookingMasterData.getEbook_agreeeori());
                                cust.setPersontype(eBookingMasterData.getAgreetypeperson());
                                lst.add(cust);
                            }
                            //
                            // BP
                            if ("BP".equalsIgnoreCase(custTypeConfig)) {
                                cust = new EcomBKGCustomer();
                                cm = new EcomCommonModel();
                                cust.setBook_no(book_refno);
                                cust.setCusttypeid("BP");
                                cust.setCustid(custcode);
                                cust.setCountryid(eBookingMasterData.getEbook_bookingcountry());
                                cust.setName(eBookingMasterData.getEbook_bookingname());
                                cust.setAddress1(eBookingMasterData.getEbook_bookingaddress());
                                cust.setCity(eBookingMasterData.getEbook_bookingcity_desc());
                                cust.setZipCode(eBookingMasterData.getEbook_bookingzipcode());
                                cust.setTelephone(eBookingMasterData.getEbook_bookingphone());
                                cust.setFax(eBookingMasterData.getEbook_bookingfax());
                                cust.setEmail(eBookingMasterData.getEbook_bookingemail());
                                cust.setEdireaderid(readerid);
                                cust.setAddress2(eBookingMasterData.getEbook_bookingaddress1());
                                cust.setStateid(splitMasterCode(eBookingMasterData.getEbook_bookingstate()));
                                cust.setEori(eBookingMasterData.getEbook_bookingeori());
                                cust.setPersontype(eBookingMasterData.getBookingtypeperson());
                                lst.add(cust);
                            } else {
                                cust = new EcomBKGCustomer();
                                cm = new EcomCommonModel();
                                cust.setBook_no(book_refno);
                                cust.setCusttypeid("BP");
                                cust.setCustid(eBookingMasterData.getEbook_agreecode());
                                cust.setCountryid(eBookingMasterData.getEbook_agreecountry());
                                cust.setName(eBookingMasterData.getEbook_agreename());
                                cust.setAddress1(eBookingMasterData.getEbook_agreeaddress());
                                cust.setCity(eBookingMasterData.getEbook_agreecity());
                                cust.setZipCode(eBookingMasterData.getEbook_agreezipcode());
                                cust.setTelephone(eBookingMasterData.getEbook_agreephone());
                                cust.setFax(eBookingMasterData.getEbook_agreefax());
                                cust.setEmail(eBookingMasterData.getEbook_agreeemail());
                                cust.setEdireaderid(readerid);
                                cust.setAddress2(eBookingMasterData.getEbook_agreeaddress1());
                                cust.setStateid(splitMasterCode(eBookingMasterData.getEbook_agreestate()));
                                cust.setEori(eBookingMasterData.getEbook_agreeeori());
                                cust.setPersontype(eBookingMasterData.getAgreetypeperson());
                                lst.add(cust);
                            }
                            //

                            // SH
                            cust_name = eBookingMasterData.getEbook_shippername();
                            if (cust_name.length() > 0) {
                                cust = new EcomBKGCustomer();
                                cust.setBook_no(book_refno);
                                cust.setCusttypeid("SH");
                                cust.setCustid(eBookingMasterData.getEbook_shippercode());
                                cust.setCountryid(eBookingMasterData.getEbook_shippercountry());
                                cust.setName(eBookingMasterData.getEbook_shippername());
                                cust.setAddress1(eBookingMasterData.getEbook_shipperaddress());
                                cust.setCity(eBookingMasterData.getEbook_shippercity());
                                cust.setZipCode(eBookingMasterData.getEbook_shipperzipcode());
                                cust.setTelephone(eBookingMasterData.getEbook_shipperphone());
                                cust.setFax(eBookingMasterData.getEbook_shipperfax());
                                cust.setEmail(eBookingMasterData.getEbook_shipperemail());
                                cust.setEdireaderid(readerid);
                                cust.setAddress2(eBookingMasterData.getEbook_shipperaddress1());
                                cust.setStateid(splitMasterCode(eBookingMasterData.getEbook_shipperstate()));
                                cust.setEori(eBookingMasterData.getEbook_shippereori());
                                cust.setPersontype(eBookingMasterData.getShippertypeperson());
                                lst.add(cust);
                            }
                            // CO
                            cust_name = eBookingMasterData.getEbook_consigneename();
                            if (cust_name.length() > 0) {
                                cust = new EcomBKGCustomer();
                                cust.setBook_no(book_refno);
                                cust.setCusttypeid("CN");
                                cust.setCustid(eBookingMasterData.getEbook_consigneecode());
                                cust.setCountryid(eBookingMasterData.getEbook_consigneecountry());
                                cust.setName(eBookingMasterData.getEbook_consigneename());
                                cust.setAddress1(eBookingMasterData.getEbook_consigneeaddress());
                                cust.setCity(eBookingMasterData.getEbook_consigneecity());
                                cust.setZipCode(eBookingMasterData.getEbook_consigneezipcode());
                                cust.setTelephone(eBookingMasterData.getEbook_consigneephone());
                                cust.setFax(eBookingMasterData.getEbook_consigneefax());
                                cust.setEmail(eBookingMasterData.getEbook_consigneeemail());
                                cust.setEdireaderid(readerid);
                                cust.setAddress2(eBookingMasterData.getEbook_consigneeaddress1());
                                cust.setStateid(splitMasterCode(eBookingMasterData.getEbook_consigneestate()));
                                cust.setEori(eBookingMasterData.getEbook_consigneeeori());
                                cust.setPersontype(eBookingMasterData.getConsigneetypeperson());
                                lst.add(cust);
                            }

                            /// NT
                            cust_name = eBookingMasterData.getEbook_notifyname();
                            if (cust_name.length() > 0) {
                                cust = new EcomBKGCustomer();
                                cust.setBook_no(book_refno);
                                cust.setCusttypeid("NT");
                                cust.setCustid(eBookingMasterData.getEbook_notifycode());
                                cust.setCountryid(eBookingMasterData.getEbook_notifycountry());
                                cust.setName(eBookingMasterData.getEbook_notifyname());
                                cust.setAddress1(eBookingMasterData.getEbook_notifyaddress());
                                cust.setCity(eBookingMasterData.getEbook_notifycity());
                                cust.setZipCode(eBookingMasterData.getEbook_notifyzipcode());
                                cust.setTelephone(eBookingMasterData.getEbook_notifyphone());
                                cust.setFax(eBookingMasterData.getEbook_notifyfax());
                                cust.setEmail(eBookingMasterData.getEbook_notifyemail());
                                cust.setEdireaderid(readerid);
                                cust.setAddress2(eBookingMasterData.getEbook_notifyaddress1());
                                cust.setStateid(splitMasterCode(eBookingMasterData.getEbook_notifystate()));
                                cust.setEori(eBookingMasterData.getEbook_notifyeori());
                                cust.setPersontype(eBookingMasterData.getNotifytypeperson());
                                lst.add(cust);
                            }
                            // NT1
                            cust_name = eBookingMasterData.getEbook_notifyname_n1();
                            if (cust_name.length() > 0) {
                                cust = new EcomBKGCustomer();
                                cust.setBook_no(book_refno);
                                cust.setCusttypeid("N1");
                                cust.setCustid(eBookingMasterData.getEbook_notifycode_n1());
                                cust.setCountryid(eBookingMasterData.getEbook_notifycountry_n1());
                                cust.setName(eBookingMasterData.getEbook_notifyname_n1());
                                cust.setAddress1(eBookingMasterData.getEbook_notifyaddress_n1());
                                cust.setCity(eBookingMasterData.getEbook_notifycity_n1());
                                cust.setZipCode(eBookingMasterData.getEbook_notifyzipcode_n1());
                                cust.setTelephone(eBookingMasterData.getEbook_notifyphone_n1());
                                cust.setFax(eBookingMasterData.getEbook_notifyfax_n1());
                                cust.setEmail(eBookingMasterData.getEbook_notifyemail_n1());
                                cust.setEdireaderid(readerid);
                                cust.setAddress2(eBookingMasterData.getEbook_notifyaddress1_n1());
                                cust.setStateid(splitMasterCode(eBookingMasterData.getEbook_notifystate_n1()));
                                cust.setEori(eBookingMasterData.getEbook_notifyeori_n1());
                                cust.setPersontype(eBookingMasterData.getNotifytypeperson_n1());
                                lst.add(cust);
                            }
                            // fw
                            cust_name = eBookingMasterData.getEbook_forwardername();
                            if (cust_name.length() > 0) {
                                cust = new EcomBKGCustomer();
                                cust.setBook_no(book_refno);
                                cust.setCusttypeid("FW");
                                cust.setCustid(eBookingMasterData.getEbook_forwardercode());
                                cust.setCountryid(eBookingMasterData.getEbook_forwardercountry());
                                cust.setName(eBookingMasterData.getEbook_forwardername());
                                cust.setAddress1(eBookingMasterData.getEbook_forwarderaddress());
                                cust.setCity(eBookingMasterData.getEbook_forwardercity());
                                cust.setZipCode(eBookingMasterData.getEbook_forwarderzipcode());
                                cust.setTelephone(eBookingMasterData.getEbook_forwarderphone());
                                cust.setFax(eBookingMasterData.getEbook_forwarderfax());
                                cust.setEmail(eBookingMasterData.getEbook_forwarderemail());
                                cust.setEdireaderid(readerid);
                                cust.setAddress2(eBookingMasterData.getEbook_forwarderaddress1());
                                cust.setStateid(splitMasterCode(eBookingMasterData.getEbook_forwarderstate()));
                                cust.setEori(eBookingMasterData.getEbook_forwardereori());
                                cust.setPersontype(eBookingMasterData.getForwardertypeperson());
                                lst.add(cust);
                            }
                            // WH
                            cust_name = eBookingMasterData.getEbook_Warehousename();
                            if (cust_name.length() > 0) {
                                cust = new EcomBKGCustomer();
                                cust.setBook_no(book_refno);
                                cust.setCusttypeid("WH");
                                cust.setCustid(eBookingMasterData.getEbook_Warehousecode());
                                cust.setCountryid(eBookingMasterData.getEbook_Warehousecountry());
                                cust.setName(eBookingMasterData.getEbook_Warehousename());
                                cust.setAddress1(eBookingMasterData.getEbook_Warehouseaddress());
                                cust.setCity(eBookingMasterData.getEbook_Warehousecity());
                                cust.setZipCode(eBookingMasterData.getEbook_Warehousezipcode());
                                cust.setTelephone(eBookingMasterData.getEbook_Warehousephone());
                                cust.setFax(eBookingMasterData.getEbook_Warehousefax());
                                cust.setEmail(eBookingMasterData.getEbook_Warehouseemail());
                                cust.setEdireaderid(readerid);
                                cust.setAddress2(eBookingMasterData.getEbook_Warehouseaddress1());
                                cust.setStateid(splitMasterCode(eBookingMasterData.getEbook_Warehousestate()));
                                lst.add(cust);
                            }
                            // NC
                            cust_name = eBookingMasterData.getEbook_NamedCustomername();
                            if (cust_name.length() > 0) {
                                cust = new EcomBKGCustomer();
                                cust.setBook_no(book_refno);
                                cust.setCusttypeid("NC");
                                cust.setCustid(eBookingMasterData.getEbook_NamedCustomercode());
                                cust.setCountryid(eBookingMasterData.getEbook_NamedCustomercountry());
                                cust.setName(eBookingMasterData.getEbook_NamedCustomername());
                                cust.setAddress1(eBookingMasterData.getEbook_NamedCustomeraddress());
                                cust.setCity(eBookingMasterData.getEbook_NamedCustomercity());
                                cust.setZipCode(eBookingMasterData.getEbook_NamedCustomerzipcode());
                                cust.setTelephone(eBookingMasterData.getEbook_NamedCustomerphone());
                                cust.setFax(eBookingMasterData.getEbook_NamedCustomerfax());
                                cust.setEmail(eBookingMasterData.getEbook_NamedCustomeremail());
                                cust.setEdireaderid(readerid);
                                cust.setAddress2(eBookingMasterData.getEbook_NamedCustomeraddress1());
                                cust.setStateid(splitMasterCode(eBookingMasterData.getEbook_NamedCustomerstate()));
                                cust.setEori(eBookingMasterData.getEbook_NamedCustomereori());
                                cust.setPersontype(eBookingMasterData.getEbook_NamedCustomerpersontype());
                                lst.add(cust);
                            }

                            //
                            if (custMap != null && !custMap.isEmpty()) {
                                ArrayList<EbookingValidatorFormBeanNew> buyer_CustList = new ArrayList<>();
                                ArrayList<EbookingValidatorFormBeanNew> seller_CustList = new ArrayList<>();
                                if (custMap != null && custMap.containsKey("SL")) {
                                    seller_CustList = (ArrayList<EbookingValidatorFormBeanNew>) custMap.get("SL");
                                    if (seller_CustList != null) {
                                        System.out.println("Seller len==>" + seller_CustList.size());
                                        for (EbookingValidatorFormBeanNew custData : seller_CustList) {
                                            if (custData != null) {
                                                cust = new EcomBKGCustomer();
                                                cust.setBook_no(book_refno);
                                                cust.setCusttypeid("SL");
                                                cust.setCustid(custData.getEbook_Custcode());
                                                cust.setCountryid(custData.getEbook_Custcountry());
                                                cust.setName(custData.getEbook_Custname());
                                                cust.setAddress1(custData.getEbook_Custaddress());
                                                cust.setCity(custData.getEbook_Custcity_desc());
                                                cust.setZipCode("");
                                                cust.setTelephone(custData.getEbook_Custphone());
                                                cust.setFax(custData.getEbook_Custfax());
                                                cust.setEmail(custData.getEbook_Custemail());
                                                cust.setEdireaderid(readerid);
                                                cust.setAddress2(custData.getEbook_Custaddress1());
                                                cust.setStateid(splitMasterCode(custData.getEbook_Custstate()));
                                                cust.setEori(custData.getEbook_Custeori());
                                                cust.setPersontype(custData.getEbook_Custpersontype());
                                                lst.add(cust);
                                            }
                                        }
                                    }
                                }

                                // BY (Buyer)
                                if (custMap != null && custMap.containsKey("BY")) {
                                    buyer_CustList = (ArrayList<EbookingValidatorFormBeanNew>) custMap.get("BY");
                                    if (buyer_CustList != null) {
                                        System.out.println("Buyer len==>" + buyer_CustList.size());
                                        for (EbookingValidatorFormBeanNew buyercustData : buyer_CustList) {
                                            if (buyercustData != null) {
                                                cust = new EcomBKGCustomer();
                                                cust.setBook_no(book_refno);
                                                cust.setCusttypeid("BY");
                                                cust.setCustid(buyercustData.getEbook_Custcode());
                                                cust.setCountryid(buyercustData.getEbook_Custcountry());
                                                cust.setName(buyercustData.getEbook_Custname());
                                                cust.setAddress1(buyercustData.getEbook_Custaddress());
                                                cust.setCity(buyercustData.getEbook_Custcity_desc());
                                                cust.setZipCode("");
                                                cust.setTelephone(buyercustData.getEbook_Custphone());
                                                cust.setFax(buyercustData.getEbook_Custfax());
                                                cust.setEmail(buyercustData.getEbook_Custemail());
                                                cust.setEdireaderid(readerid);
                                                cust.setAddress2(buyercustData.getEbook_Custaddress1());
                                                cust.setStateid(splitMasterCode(buyercustData.getEbook_Custstate()));
                                                cust.setEori(buyercustData.getEbook_Custeori());
                                                cust.setPersontype(buyercustData.getEbook_Custpersontype());
                                                lst.add(cust);
                                            }
                                        }
                                    }
                                }
                            } else {
                                // SL
                                cust_name = checkNull(eBookingMasterData.getEbook_Sellername());
                                if (cust_name.length() > 0) {
                                    cust = new EcomBKGCustomer();
                                    cust.setBook_no(book_refno);
                                    cust.setCusttypeid("SL");
                                    cust.setCustid(eBookingMasterData.getEbook_Sellercode());
                                    cust.setCountryid(eBookingMasterData.getEbook_Sellercountry());
                                    cust.setName(eBookingMasterData.getEbook_Sellername());
                                    cust.setAddress1(eBookingMasterData.getEbook_Selleraddress());
                                    cust.setCity(eBookingMasterData.getEbook_Sellercity());
                                    cust.setZipCode(eBookingMasterData.getEbook_Sellerzipcode());
                                    cust.setTelephone(eBookingMasterData.getEbook_Sellerphone());
                                    cust.setFax(eBookingMasterData.getEbook_Sellerfax());
                                    cust.setEmail(eBookingMasterData.getEbook_Selleremail());
                                    cust.setEdireaderid(readerid);
                                    cust.setAddress2(eBookingMasterData.getEbook_Selleraddress1());
                                    cust.setStateid(splitMasterCode(eBookingMasterData.getEbook_Sellerstate()));
                                    cust.setEori(eBookingMasterData.getEbook_Sellereori());
                                    cust.setPersontype(eBookingMasterData.getEbook_Sellerpersontype());
                                    lst.add(cust);
                                }
                                // BY
                                cust_name = checkNull(eBookingMasterData.getEbook_Buyername());
                                if (cust_name.length() > 0) {
                                    cust = new EcomBKGCustomer();
                                    cust.setBook_no(book_refno);
                                    cust.setCusttypeid("BY");
                                    cust.setCustid(eBookingMasterData.getEbook_Buyercode());
                                    cust.setCountryid(eBookingMasterData.getEbook_Buyercountry());
                                    cust.setName(eBookingMasterData.getEbook_Buyername());
                                    cust.setAddress1(eBookingMasterData.getEbook_Buyeraddress());
                                    cust.setCity(eBookingMasterData.getEbook_Buyercity());
                                    cust.setZipCode(eBookingMasterData.getEbook_Buyerzipcode());
                                    cust.setTelephone(eBookingMasterData.getEbook_Buyerphone());
                                    cust.setFax(eBookingMasterData.getEbook_Buyerfax());
                                    cust.setEmail(eBookingMasterData.getEbook_Buyeremail());
                                    cust.setEdireaderid(readerid);
                                    cust.setAddress2(eBookingMasterData.getEbook_Buyeraddress1());
                                    cust.setStateid(splitMasterCode(eBookingMasterData.getEbook_Buyerstate()));
                                    cust.setEori(eBookingMasterData.getEbook_Buyereori());
                                    cust.setPersontype(eBookingMasterData.getEbook_Buyerpersontype());
                                    lst.add(cust);
                                }
                            }
                            cm.setEditempbooking_cust(lst);
                            hs = (Map) ecomvdsservice.setecomeditempbookingcustomer(cm);
                            System.out.println("[ECOM] EcomBooingBean ===>>>" + hs);

                            //
                            // Insert ocean freight in remari
                            if (eBookingMasterData.getEbook_ocean_freight() != null && eBookingMasterData.getEbook_ocean_freight().length() > 0) {
                                // Insert into reference table
                                try {
                                    lst = new ArrayList();
                                    rf = new EcomBKGReference();
                                    cm = new EcomCommonModel();
                                    rf.setBook_no(book_refno);
                                    rf.setRefid("EOF");
                                    if (eBookingMasterData.getEbook_ocean_freight().equalsIgnoreCase("C")) {
                                        rf.setRefval("C~~Collect");
                                    } else {
                                        rf.setRefval("P~~Prepaid");
                                    }
                                    rf.setIsedited("N");
                                    rf.setEdireaderid(readerid);
                                    rf.setVersion("0");
                                    lst.add(rf);
                                    cm.setEditempbooking_reference(lst);
                                    hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (eBookingMasterData.getEbook_reftype() != null && eBookingMasterData.getEbook_reftype().length() > 0) {

                                // Insert into reference table
                                try {
                                    lst = new ArrayList();
                                    rf = new EcomBKGReference();
                                    cm = new EcomCommonModel();
                                    rf.setBook_no(book_refno);
                                    rf.setRefid(EcomConsts.REF_TYPE);
                                    if (eBookingMasterData.getEbook_reftype().equalsIgnoreCase("Express")) {
                                        rf.setRefval("Express");
                                    } else {
                                        rf.setRefval("Normal");
                                    }
                                    rf.setIsedited("N");
                                    rf.setEdireaderid(readerid);
                                    rf.setVersion("0");
                                    lst.add(rf);
                                    cm.setEditempbooking_reference(lst);
                                    hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (eBookingMasterData.getEbook_sisubmit_party() != null
                                    && eBookingMasterData.getEbook_sisubmit_party().length() > 0) {

                                // Insert into reference table
                                try {
                                    lst = new ArrayList();
                                    rf = new EcomBKGReference();
                                    cm = new EcomCommonModel();
                                    rf.setBook_no(book_refno);
                                    rf.setRefid(EcomConsts.sisubmit);
                                    rf.setRefval(eBookingMasterData.getEbook_sisubmit_party());
                                    rf.setIsedited("N");
                                    rf.setEdireaderid(readerid);
                                    rf.setVersion("0");
                                    lst.add(rf);
                                    cm.setEditempbooking_reference(lst);
                                    hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            // Insert empty pick up place
                            String emptyPickUp = null;
                            if (eBookingMasterData.getEmptyPickup() != null && eBookingMasterData.getEmptyPickup().length() > 0) {
                                emptyPickUp = eBookingMasterData.getEmptyPickUpName();
                                if (eBookingMasterData.getEmptyPickUpDate() != null && eBookingMasterData.getEmptyPickUpDate().length() > 0) {
                                    emptyPickUp = emptyPickUp + "  Date : " + eBookingMasterData.getEmptyPickUpDate();
                                }
                                // Insert into reference table
                                try {
                                    lst = new ArrayList();
                                    rf = new EcomBKGReference();
                                    cm = new EcomCommonModel();
                                    rf.setBook_no(book_refno);
                                    rf.setRefid("EEP");
                                    rf.setRefval(eBookingMasterData.getEbook_pickup_point() + "~~" + eBookingMasterData.getEmptyPickup());
                                    rf.setIsedited("N");
                                    rf.setEdireaderid(readerid);
                                    rf.setVersion("0");
                                    lst.add(rf);
                                    cm.setEditempbooking_reference(lst);
                                    hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // end

                                // Insert into reference table
                                if (eBookingMasterData.getEmptyPickUpDate() != null && eBookingMasterData.getEmptyPickUpDate().length() > 0) {
                                    try {
                                        lst = new ArrayList();
                                        rf = new EcomBKGReference();
                                        cm = new EcomCommonModel();
                                        rf.setBook_no(book_refno);
                                        rf.setRefid("EPD");
                                        rf.setRefval(eBookingMasterData.getEmptyPickUpDate());
                                        rf.setIsedited("N");
                                        rf.setEdireaderid(readerid);
                                        rf.setVersion("0");
                                        lst.add(rf);
                                        cm.setEditempbooking_reference(lst);
                                        hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            // Insert Commodity Details
                            List containernolis = new ArrayList();
                            Hashtable htcargo = new Hashtable();
                            ArrayList al;
                            int eqpQuantityvalue = Integer.parseInt(eqpQuantity);
                            int len_cargo = cargolist.size();
                            String eqpid = "";
                            hssb.append("");
                            otherssb.append("");
                            dgsb.append("");
                            int k = 1;
                            String flashpoint = null;
                            String flashunit = null, ediItemDesc = null;
                            boolean othersboolean = false, dgboolean = false, fakBoolean = false;
                            String hscomdty = null, isFak = null;
                            cm = new EcomCommonModel();
                            for (int i = 0; i < len_cargo; i++) {
                                lst = new ArrayList();
                                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                eqpQuantityvalue = Integer.parseInt(eBookingcargoData.getEbook_eqp_qty());
                                for (int j = 0; j < eqpQuantityvalue; j++) {
                                    //
                                    cmd = new EcomBKGCommodity();
                                    //
                                    eqpid = eBookingcargoData.getEbook_eqp_type_desc();
                                    if (htcargo.containsKey(eqpid)) {
                                        al = (ArrayList) htcargo.get(eqpid);
                                        al.add(j);
                                    } else {
                                        al = new ArrayList();
                                        al.add(j);
                                    }
                                    htcargo.put(eqpid, al);
                                    String eqp_result = eqpid.substring(0, eqpid.length() - 1);
                                    eqpid = eqp_result.concat("TNB");
                                    eqpid = eqpid + al.size();
                                    containernolis.add((i + 1) + "~~~" + eqpid);
                                    if (eBookingcargoData != null) {
                                        ediItemDesc = checkNull(eBookingcargoData.getEbooking_commodity_desc());
                                        if (ediItemDesc != null && ediItemDesc.toString().trim().length() > 255) {
                                            ediItemDesc = ediItemDesc.substring(0, 255);
                                            System.out.println("[ECOM] EcomBooingBean commval====>" + ediItemDesc);
                                        }
                                        hscomdty = eBookingcargoData.getHscode();
                                        isFak = eBookingcargoData.getIsFakCommodity();
                                        if ("Y".equalsIgnoreCase(isFak)) {
                                            hscomdty = null;
                                            ediItemDesc = null;
                                            fakBoolean = true;
                                        }
                                        String haz = "N";
                                        String hazflag = eBookingcargoData.getHzd();
                                        if ("Y".equalsIgnoreCase(checkNull(hazflag))) {
                                            haz = "Y";
                                        }
                                        String cargovolume = eBookingcargoData.getEbook_cargovolume();
                                        String netweightval = eBookingcargoData.getEbook_netweight();
                                        String volumeunit = eBookingcargoData.getEbook_volumeunit();
                                        String olforward = checkNull(eBookingcargoData.getEbook_olforward());
                                        String owleft = checkNull(eBookingcargoData.getEbook_owleft());
                                        String oh = checkNull(eBookingcargoData.getEbook_oh());
                                        String olaft = checkNull(eBookingcargoData.getEbook_olaft());
                                        String owright = checkNull(eBookingcargoData.getEbook_owright());
                                        String dimensionunit = eBookingcargoData.getEbook_dimensionunit();
                                        String itemqty = checkNull(eBookingcargoData.getEbook_packquantity());
                                        String marinePollut = checkNull(eBookingcargoData.getEbook_poluttunit());
                                        marinePollut = marinePollut != null && marinePollut.equalsIgnoreCase("Y")
                                                ? "Yes"
                                                : "No";
                                        if (cargovolume.length() == 0) {
                                            cargovolume = null;
                                        }
                                        if (netweightval.length() == 0) {
                                            netweightval = null;
                                        }

                                        if (itemqty.length() == 0) {
                                            itemqty = null;
                                        }
                                        if (olforward.length() == 0) {
                                            olforward = null;
                                        }
                                        if (owleft.length() == 0) {
                                            owleft = null;
                                        }
                                        if (oh.length() == 0) {
                                            oh = null;
                                        }
                                        if (olaft.length() == 0) {
                                            olaft = null;
                                        }
                                        if (owright.length() == 0) {
                                            owright = null;
                                        }
                                        if (eBookingcargoData.getHscode() != null
                                                && eBookingcargoData.getHscode().trim().length() > 0
                                                && (eBookingcargoData.getCommodityothers().equalsIgnoreCase("Y")
                                                || "Y".equalsIgnoreCase(isFak))) {
                                            hssb.append(eqpid + " : " + eBookingcargoData.getHscode());
                                            hssb.append("\n");
                                        }

                                        if (eBookingcargoData.getCommodityothers().equalsIgnoreCase("Y")) {
                                            othersboolean = true;
                                            otherssb.append(eqpid + " : " + eBookingcargoData.getEbook_commoditycode());
                                            otherssb.append("\n");
                                        }
                                        if (checkNull(eBookingcargoData.getHzd()).equalsIgnoreCase("Y")) {
                                            dgboolean = true;
                                            
                                            // Multiple DG Details Support - Iterate through all DG entries
                                            List<EbookingValidatorFormBeanNew> dgList = eBookingcargoData.getDgDetailsList();
                                            if (dgList != null && !dgList.isEmpty()) {
                                                int dgIdx = 1;
                                                for (EbookingValidatorFormBeanNew dgEntry : dgList) {
                                                    flashpoint = checkNull(dgEntry.getEbook_flashpoint());
                                                    flashunit = checkNull(dgEntry.getEbook_flashunit());
                                                    if ("F".equalsIgnoreCase(flashunit)) {
                                                        flashunit = " Fahrenheit ";
                                                    } else if ("C".equalsIgnoreCase(flashunit)) {
                                                        flashunit = " Celsius ";
                                                    }
                                                    if (flashpoint.length() <= 0) {
                                                        flashpoint = "";
                                                        flashunit = "";
                                                    }
                                                    
                                                    String dgPrefix = eqpid;
                                                 //   if (dgList.size() > 1) {
                                                        dgPrefix = eqpid + " (DG#" + dgIdx + ")";
                                                 // }
                                                    
                                                    dgsb.append(dgPrefix + " - DG Class : " + dgEntry.getEbook_dgclass()
                                                            + " UN NO : " + dgEntry.getEbook_unno()
                                                            + " Flash Point : " + flashpoint + flashunit + " Shipping Name : "
                                                            + dgEntry.getEbook_shipname() + " Marine Pollutant : "
                                                            + checkNull(dgEntry.getEbook_poluttunit()));
                                                    dgsb.append("\n");
                                                    dgIdx++;
                                                }
                                            } else {
                                                // Fallback to single DG entry (backward compatibility)
                                                flashpoint = checkNull(eBookingcargoData.getEbook_flashpoint());
                                                flashunit = checkNull(eBookingcargoData.getEbook_flashunit());
                                                if ("F".equalsIgnoreCase(flashunit)) {
                                                    flashunit = " Fahrenheit ";
                                                } else if ("C".equalsIgnoreCase(flashunit)) {
                                                    flashunit = " Celsius ";
                                                }
                                                if (flashpoint.length() <= 0) {
                                                    flashpoint = "";
                                                    flashunit = "";
                                                }
                                                dgsb.append(eqpid + " - DG Class : " + eBookingcargoData.getEbook_dgclass()
                                                        + " UN NO : " + eBookingcargoData.getEbook_unno()
                                                        + " Flash Point : " + flashpoint + flashunit + " Shipping Name : "
                                                        + eBookingcargoData.getEbook_shipname() + " Marine Pollutant : "
                                                        + marinePollut);
                                                dgsb.append("\n");
                                            }
                                        }

                                        cmd.setBook_no(book_refno);

                                        cmd.setEdireaderid(checkNull(readerid));
                                        cmd.setVersion("0");
                                        cmd.setItemqty(itemqty);
                                        cmd.setItemweight(ConvertDecimalFormat(
                                                checkNull(eBookingcargoData.getEbook_cargoweight())));
                                        cmd.setItemweightunit(checkNull(eBookingcargoData.getEbook_weightunit()));
                                        cmd.setHazardous(checkNull(haz));
                                        cmd.setEqpid(checkNull(eqpid));
                                        cmd.setAlcohol("N");
                                        cmd.setMilitarycargo("N");
                                        cmd.setCargono(k);
                                        cmd.setPackageid(eBookingcargoData.getEbook_packagecode());
                                        System.out.println("[ECOM] EcomBooingBean actco"
                                                + eBookingcargoData.getEbook_actCommoName());
                                        cmd.setItemvolume(cargovolume);

                                        // System.out.println("[ECOM] EcomBooingBean toPlainString" +
                                        // ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_cargoweight()))+"//"+ConvertDecimalFormat(checkNull(netweightval))+"//"+eBookingcargoData.getEbook_cargoweight());
                                        cmd.setNettwt(netweightval);

                                        // System.out.println("[ECOM] EcomBooingBean nettttttt" + cmd.getNettwt());
                                        cmd.setMarksdesc(eBookingcargoData.getEbook_actCommoName());
                                        System.out.println("[ECOM] EcomBooingBean getmark" + cmd.getMarksdesc());

                                        cmd.setItemvolumeunit(checkNull(volumeunit));
                                        cmd.setItemforward(olforward);
                                        cmd.setItemleft(owleft);
                                        cmd.setItemtop(oh);
                                        cmd.setItemaft(olaft);
                                        cmd.setItemright(owright);
                                        cmd.setItemlengthunit(checkNull(dimensionunit));
                                        if (eBookingcargoData.getCommodityothers().equalsIgnoreCase("Y")) {
                                            cmd.setComdtygrp("");
                                            cmd.setCommodityid("");
                                        } else {
                                            cmd.setComdtygrp(checkNull(eBookingcargoData.getEbook_commoditycode()));
                                            cmd.setCommodityid(checkNull(hscomdty));
                                        }
                                        cmd.setItemno("1");
                                        cmd.setCarriercode(client_code);
                                        cmd.setItemdesc(ediItemDesc);
                                        cmd.setBycode(eBookingcargoData.getEbook_cmdtybuyercode());
                                        cmd.setByname(eBookingcargoData.getEbook_cmdtybuyerName());
                                        cmd.setSlcode(eBookingcargoData.getEbook_cmdtysellercode());
                                        cmd.setSlname(eBookingcargoData.getEbook_cmdtysellerName());
                                        cmd.setCmdty_hblno(eBookingcargoData.getEbook_cmdtyhblno());
                                        lst.add(cmd);
                                        k++;
                                    }
                                }
                                cm.setEditempbooking_commodity(lst);
                                hs = (Map) ecomvdsservice.setecomeditempbookingcommodity(cm);
                            }
                            //
                            // insert booking Haulage details
                            if (haulagelist != null && haulagelist.size() > 0) {
                                lst = new ArrayList();
                                String conrownostr = null;
                                int conrowno;
                                String connoarr[];
                                // for (int j = 0; j < containernolis.size(); j++) {
                                for (int i = 0; i < haulagelist.size(); i++) {
                                    // conrownostr = (String) containernolis.get(j);
                                    // connoarr = conrownostr.split("~~~");
                                    // conrowno = Integer.parseInt(connoarr[0].trim());
                                    haulvo = (BookingHaulagedetailsVo) haulagelist.get(i);
                                    // if (conrowno == haulvo.getEqprowid()) {
                                    haul = new EcomBKGHaulage();
                                    haul.setBook_no(checkNull(book_refno));
                                    // haul.setEqpid(checkNull(connoarr[1].trim()));
                                    haul.setEqpid(checkNull(haulvo.getEqptype()) + checkNull(haulvo.getDummyeqp()));
                                    haul.setEqptypeid(checkNull(haulvo.getEqptype()));
                                    haul.setLoadlocation(checkNull(haulvo.getPickloccode()));
                                    haul.setLoadlocationname(checkNull(haulvo.getPicklocname()));
                                    haul.setContactaddress(checkNull(haulvo.getAddress()));
                                    haul.setReferencenumber(checkNull(haulvo.getRefno()));
                                    haul.setDropdate(checkNull(haulvo.getDropdate()));
                                    haul.setStopsequence(haulvo.getStopsequence());
                                    haul.setContactperson(checkNull(haulvo.getContactperson()));
                                    haul.setContactphone(checkNull(haulvo.getContactphone()));
                                    haul.setContactemail(checkNull(haulvo.getContactemail()));
                                    haul.setDoctype(checkNull(haulvo.getTwrsdoctype()));
                                    haul.setOverweight(checkNull(haulvo.getTwrsoverweight()));
                                    // haul.setTmsclientcode(checkNull(haulvo.getTmsclientcode()));
                                    haul.setRemarks(checkNull(haulvo.getRemarks()));
                                    haul.setEdireaderid(checkNull(readerid));
                                    haul.setVersion("0");
                                    lst.add(haul);
                                }
                                // }
                                // }
                                cm.setEditempbooking_haulage(lst);
                                hs = (Map) ecomvdsservice.setecomeditempbkghaulage(cm);
                            }
                            if (checkIsEmpty(eBookingMasterData.getBkgcutoff())
                                    || checkIsEmpty(eBookingMasterData.getDoccutoff())
                                    || checkIsEmpty(eBookingMasterData.getVgmcutoff())
                                    || checkIsEmpty(eBookingMasterData.getBbrorocutoff())
                                    || checkIsEmpty(eBookingMasterData.getHazcutoff())
                                    || checkIsEmpty(eBookingMasterData.getPortentrycutoff())) {
                                lst = new ArrayList();
                                conf = new EcomBKGConf();

                                conf.setBook_no(book_refno);
                                conf.setBkgcutoff(eBookingMasterData.getBkgcutoff());
                                conf.setDoccutoff(eBookingMasterData.getDoccutoff());
                                conf.setBbrorocutoff(eBookingMasterData.getBbrorocutoff());
                                conf.setHazcutoff(eBookingMasterData.getHazcutoff());
                                conf.setVgmcutoff(eBookingMasterData.getVgmcutoff());
                                conf.setPortentrycutoff(eBookingMasterData.getPortentrycutoff());
                                conf.setEdireaderid(readerid);
                                conf.setVersion("0");
                                lst.add(conf);
                                cm.setEditempbooking_conf(lst);
                                hs = (Map) ecomvdsservice.setecomeditempbkgconf(cm);
                            }
                            //
                            ENSDeclarantVo ensDec = eBookingMasterData.getEnsdecvo();
                            if (ensDec != null && checkNull(ensDec.getEuCustZone()).length() > 0) {
                                Editempbooking_ics_new ics = new Editempbooking_ics_new();
                                ArrayList icsToInsertList = new ArrayList();
                                ics.setBook_no(book_refno);
                                ics.setCust_name(ensDec.getDeclarantName());
                                ics.setAddress(ensDec.getDeclarantAddress());
                                ics.setAddress1(ensDec.getDeclarantAddress1());
                                ics.setCity(ensDec.getDeclarantCity());
                                ics.setCountry(ensDec.getDeclarantCountry());
                                ics.setState(ensDec.getDeclarantState());
                                ics.setTelephone(ensDec.getDeclarantTelephone());
                                ics.setEnseori(ensDec.getDeclarantEORI());
                                ics.setEmail(ensDec.getDeclarantEmail());
                                ics.setFiling_type(ensDec.getDeclarantFillingType());

                                ics.setPayment_mode(ensDec.getPaymentMethod());
                                ics.setBill_type(ensDec.getBlTypeENS());
                                ics.setEns_type(ensDec.getEnsFillingType());
                                ics.setEucustom(ensDec.getEuCustZone());
                                ics.setEdireaderid(readerid);
                                ics.setZipcode(ensDec.getDeclarantZipcode());
                                ics.setVersion("0");
                                icsToInsertList.add(ics);
                                EcomBKGCommonModel icsToInsertModel = new EcomBKGCommonModel();
                                icsToInsertModel.setEditempbooking_ics_new(icsToInsertList);
                                ecomvdsservice.seteditempbookingicsnew(icsToInsertModel);
                            }
                            // Query- EBOOKING_DG_DETAILS
                            String ishazardous = "N";
                            cm = new EcomCommonModel();
                            EcomBKGCommDG dg = null;
                            lst = new ArrayList();
                            for (int i = 0; i < len_cargo; i++) {
                                dg = new EcomBKGCommDG();
                                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                ishazardous = checkNull(eBookingcargoData.getHzd());
                                String unno[] = checkNull(eBookingcargoData.getEbook_unno()).split("~~");
                                if (ishazardous.equalsIgnoreCase("Y")) {
                                    dg.setBook_no(checkNull(book_refno));
                                    dg.setEqpid(checkNull(eqpid));
                                    dg.setEqptype(checkNull(eBookingcargoData.getEbook_eqp()));
                                    dg.setUnno(checkNull(unno[0]));
                                    dg.setShippingname(checkNull(eBookingcargoData.getEbook_shipname()));
                                    dg.setImdgclass(checkNull(eBookingcargoData.getEbook_dgclass()));
                                    dg.setFlashpoint(checkNull(eBookingcargoData.getEbook_flashpoint()));
                                    dg.setFlashunit(checkNull(eBookingcargoData.getEbook_flashunit()));
                                    dg.setMarinepollutant(checkNull(eBookingcargoData.getEbook_poluttunit()));
                                    dg.setEdireaderid(checkNull(readerid));
                                    lst.add(dg);
                                }
                            }
                            // cm.setEditempbooking_imo(lst);
                            // hs = (Map) ecomvdsservice.setecomeditempbkgdgdetails(cm);
                            //
                            String hscodecheck = hssb.toString();
                            if (hscodecheck != null && hscodecheck.trim().length() > 0
                                    && (othersboolean == true || fakBoolean)) {
                                try {
                                    lst = new ArrayList();
                                    rm = new EcomBKGRemarks();
                                    cm = new EcomCommonModel();
                                    rm.setBook_no(book_refno);
                                    rm.setRemarksref("HS Code  : ");
                                    rm.setRemarksval(hssb.toString());
                                    rm.setEdireaderid(readerid);
                                    rm.setVersion("0");
                                    rm.setCategory("M");
                                    lst.add(rm);
                                    cm.setEditempbooking_remarks(lst);
                                    hs = (Map) ecomvdsservice.setEdiTempBookingRemarks(cm);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (dgboolean) {
                                try {
                                    lst = new ArrayList();
                                    rm = new EcomBKGRemarks();
                                    cm = new EcomCommonModel();
                                    rm.setBook_no(book_refno);
                                    rm.setRemarksref("DG : ");
                                    rm.setRemarksval(dgsb.toString());
                                    rm.setEdireaderid(readerid);
                                    rm.setVersion("0");
                                    rm.setCategory("M");
                                    lst.add(rm);
                                    cm.setEditempbooking_remarks(lst);
                                    hs = (Map) ecomvdsservice.setEdiTempBookingRemarks(cm);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //
                            }
                            if (othersboolean) {
                                try {
                                    lst = new ArrayList();
                                    rm = new EcomBKGRemarks();
                                    cm = new EcomCommonModel();
                                    rm.setBook_no(book_refno);
                                    rm.setRemarksref("Other Commodity : ");
                                    rm.setRemarksval(otherssb.toString());
                                    rm.setEdireaderid(readerid);
                                    rm.setVersion("0");
                                    rm.setCategory("M");
                                    lst.add(rm);
                                    cm.setEditempbooking_remarks(lst);
                                    hs = (Map) ecomvdsservice.setEdiTempBookingRemarks(cm);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            // Insert eBooking Container Data
                            // Query - EBOOKING_CONT
                            Hashtable htc = new Hashtable();
                            ArrayList alc = new ArrayList();
                            String tareW = "";
                            cm = new EcomCommonModel();
                            EcomBKGCargo crg = null;
                            int m = 1;
                            for (int i = 0; i < len_cargo; i++) {
                                lst = new ArrayList();
                                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                                eqpQuantityvalue = Integer.parseInt(eBookingcargoData.getEbook_eqp_qty());
                                for (int j = 0; j < eqpQuantityvalue; j++) {
                                    crg = new EcomBKGCargo();
                                    tareW = eBookingcargoData.getEbook_tareweight();
                                    eqpid = eBookingcargoData.getEbook_eqp_type_desc();
                                    if (htc.containsKey(eqpid)) {
                                        alc = (ArrayList) htc.get(eqpid);
                                        alc.add(j);
                                    } else {
                                        alc = new ArrayList();
                                        alc.add(j);
                                    }
                                    htc.put(eqpid, alc);
                                    String eqp_resultc = eqpid.substring(0, eqpid.length() - 1);
                                    eqpid = eqp_resultc.concat("TNB");
                                    eqpid = eqpid + alc.size();
                                    String mintemp = checkNull(eBookingcargoData.getEbook_mintemp());
                                    String maxtemp = checkNull(eBookingcargoData.getEbook_maxtemp());
                                    String presetemp = checkNull(eBookingcargoData.getEbook_presetemp());
                                    String temperunit = eBookingcargoData.getEbook_temperunit();
                                    String humidity = "Y".equalsIgnoreCase(eBookingcargoData.getHumidityflag())
                                            ? eBookingcargoData.getHumidity()
                                            : null;
                                    String humidity_flag = "Y".equalsIgnoreCase(eBookingcargoData.getHumidityflag())
                                            ? "1"
                                            : "0";
                                    String drains = eBookingcargoData.getDrains();
                                    String co2 = eBookingcargoData.getCo2();
                                    String o2 = eBookingcargoData.getO2();
                                    String ventilation = eBookingcargoData.getVentilation();

                                    String dgflag = "N";
                                    String hazflag = eBookingcargoData.getHzd();
                                    presetemp = presetemp.length() > 0 ? presetemp : null;
                                    mintemp = mintemp.length() > 0 ? mintemp : null;
                                    maxtemp = maxtemp.length() > 0 ? maxtemp : null;

                                    if ("Y".equalsIgnoreCase(checkNull(hazflag))) {
                                        dgflag = "Y";
                                    }
                                    if ("Y".equalsIgnoreCase(checkNull(eBookingcargoData.getEbook_nor()))) {
                                        temperunit = null;
                                        mintemp = null;
                                        presetemp = null;
                                        maxtemp = null;
                                        humidity = drains = co2 = o2 = ventilation = null;
                                    }

                                    String laden = eBookingcargoData.getEqpstatus();
                                    if (eBookingcargoData != null) {
                                        crg.setBook_no(book_refno);
                                        String strEQP = checkNull(eBookingcargoData.getEbook_eqp_type());
                                        System.out.println("[ECOM] EcomBooingBean >>strEQP>>>" + strEQP);
                                        if (!("R".equalsIgnoreCase(strEQP))) {
                                            temperunit = null;
                                            mintemp = null;
                                            presetemp = null;
                                            maxtemp = null;
                                            humidity = drains = co2 = o2 = ventilation = null;
                                        }
                                        crg.setType(checkNull(eBookingcargoData.getEbook_eqp_type()));
                                        crg.setEqptypeid(checkNull(eBookingcargoData.getEbook_eqp()));
                                        crg.setEdireaderid(readerid);
                                        crg.setEqpid(eqpid);
                                        crg.setCargono(m);
                                        crg.setOog(checkNull(eBookingcargoData.getOog()));
                                        crg.setShipperown(checkNull(eBookingcargoData.getSoc()));
                                        // System.out.println("eBookingcargoData.getEbook_cargoweight()" +
                                        // eBookingcargoData.getEbook_cargoweight() + "///" +
                                        // ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_cargoweight())));

                                        crg.setCargoweight(ConvertDecimalFormat(
                                                checkNull(eBookingcargoData.getEbook_cargoweight())));
                                        crg.setGrossweight(ConvertDecimalFormat(
                                                checkNull(eBookingcargoData.getEbook_grossweight())));
                                        crg.setWeightunit(checkNull(eBookingcargoData.getEbook_weightunit()));
                                        crg.setTempmax(maxtemp);
                                        crg.setTempmin(mintemp);
                                        crg.setSettemp(presetemp);
                                        crg.setTempunit(temperunit);
                                        crg.setNor(eBookingcargoData.getEbook_nor());

                                        crg.setLoadingcondition("FF");
                                        crg.setIsdummy("Y");
                                        crg.setDgflag(dgflag);
                                        crg.setEqpstatus(checkNull(laden));
                                        crg.setOrderno(checkNull(eBookingMasterData.getEbook_orderNo()));
                                        crg.setHumidity_flag(humidity_flag);
                                        crg.setHumidity(humidity);
                                        crg.setDrains(drains);
                                        crg.setCo2value(co2);
                                        crg.setO2value(o2);
                                        crg.setVentpercentage(ventilation);
                                        if (ventilation != null && ventilation != "") {
                                            crg.setVentsetting("1");
                                        } else {
                                            crg.setVentsetting("0");
                                        }
                                        tareW = checkNull(tareW);
                                        if (tareW.length() == 0) {
                                            tareW = "0";
                                        }
                                        crg.setTareweight(ConvertDecimalFormat(tareW));
                                        lst.add(crg);
                                        m++;
                                    }
                                }
                                cm.setEditempbooking_cargo(lst);
                                hs = (Map) ecomvdsservice.setecomeditempbkgcargo(cm);
                            }

                            // Insert into edidocstatus
                            // Query - EDI_DOC_STATUS
                            Map configMap = cntrlBeanObj.getMasterConfigDetails();
                            lst = new ArrayList();
                            EcomBKGEDIDocStatus doc = new EcomBKGEDIDocStatus();
                            cm = new EcomCommonModel();
                            doc.setBook_no(book_refno);
                            doc.setEdireaderid(readerid);
                            doc.setTranstype("9");
                            doc.setStatus("R");
                            doc.setEdiparty(configMap.get(GeneralConf.CARRIER_CODE) + GeneralConf.EDI_TYPE_BKG);
                            doc.setMessage(GeneralConf.EDI_MESSAGETYPE_BKG);
                            doc.setServprovname(configMap.get(GeneralConf.CARRIER_CODE) + GeneralConf.EDI_ECOM);
                            doc.setAmendment_version(0);
                            lst.add(doc);
                            cm.setEdidocstatus(lst);
                            hs = (Map) ecomvdsservice.setecomeditempdocstatus(cm);
                            //
                            /*
                             * Map ediMap = ecomvdsservice.getecomediid(EcomConsts.ONLINE_BOOKING);
                             * System.out.println("[ECOM] EcomBooingBean >>ediMap>>" + ediMap);
                             * List<EcomBKGReaderMaster> ediList = null;
                             * if (ediMap != null && ediMap.size() > 0) {
                             * System.out.println("[ECOM] EcomBooingBean >>>yes value is there>>>>>");
                             * ediList = gson.fromJson(gson.toJson((ArrayList) ediMap.get("edidocconfig")),
                             * new TypeToken<List<EcomBKGReaderMaster>>() {
                             * }.getType());
                             * if (ediList != null && ediList.size() > 0) {
                             * EcomBKGReaderMaster locEdi = ediList.get(0);
                             * ediid = locEdi.getEdiid();
                             * }
                             * }
                             */
                            ediid = EcomConsts.ONLINE_BOOKING;
                            System.out.println("[ECOM] EcomBooingBean >>ediid>>>>" + ediid);
                            if (ediid != null && ediid.length() > 0) {

                                // Get Corporate agency
                                String corporateAgency = null;
                                List corpList = null;
                                Map corpAgency = ecomvdsservice.getEcomCorporateAgency(bookingagencyid);
                                System.out.println("[ECOM] EcomBooingBean >>corpAgency>>" + corpAgency);
                                if (corpAgency != null && corpAgency.size() > 0) {
                                    corpList = (List) corpAgency.get("agencymatrix");
                                    if (corpList != null && corpList.size() > 0) {
                                        corpAgency = (Map) corpList.get(0);
                                        corporateAgency = checkNull((String) corpAgency.get("corpagency"));
                                    }
                                }
                                System.out.println("[ECOM] EcomBooingBean >>corporateAgency>book>" + corporateAgency);

                                // Insert into edireadermaster
                                lst = new ArrayList();
                                edirm = new EcomBKGReaderMaster();
                                cm = new EcomCommonModel();
                                edirm.setEdiid(ediid);
                                edirm.setEdireaderid(readerid);
                                edirm.setAgencycode(corporateAgency);
                                edirm.setLocationcode(bookingagencyid);
                                edirm.setUploadstatus("Completed");
                                lst.add(edirm);
                                cm.setEdireadermaster(lst);
                                hs = (Map) ecomvdsservice.setedireadermaster(cm);

                                // Insert into EDI Remarks
                                submittedBy = loginCustName + " ";
                                contactName = checkNull(eBookingMasterData.getContactName());
                                if (contactName.length() > 0) {
                                    submittedBy = submittedBy + "/ " + " Contact Name : " + contactName + " ";
                                }
                                contactPhone = checkNull(eBookingMasterData.getCustPhoneNo());
                                if (contactPhone.length() > 0) {
                                    submittedBy = submittedBy + "/ " + " Contact Phone No. : " + contactPhone + "";
                                }
                                lst = new ArrayList();
                                rm = new EcomBKGRemarks();
                                cm = new EcomCommonModel();
                                rm.setBook_no(book_refno);
                                rm.setRemarksref("Submitted By");
                                rm.setRemarksval(submittedBy);
                                rm.setEdireaderid(readerid);
                                rm.setVersion("0");
                                rm.setCategory(null);
                                lst.add(rm);
                                cm.setEditempbooking_remarks(lst);
                                hs = (Map) ecomvdsservice.setEdiTempBookingRemarks(cm);
                                System.out.println("[ECOM] EcomBooingBean ===>>>" + hs);

                                //
                                if (isconvertzip && checkNull(filepath).length() > 0) {
                                    filepath = toRenameZipFile(book_refno, readerid, fileUploadPath, filepath);
                                }
                            }
                            String strAgencyCode = agencycode;
                            String strAgency = getAgencyMail(strAgencyCode);
                            rvo.setAdmToMailId(strAgency);

                            String agencycountry = getAgencyCountry(strAgencyCode);
                            rvo.setAgencycountry(agencycountry);
                        }
                        //
                        rvo.setReaderid(readerid);
                    } // Id Close
                    String subject = "";
                    MailFormatVO mailFormatObj = new MailFormatVO();
                    if (custCountry != null && "CN".equalsIgnoreCase(custCountry)) {
                        mailFormatObj = getMailContentLang("E-Booking", "submitmail", "C", "ch");

                    } else {
                        mailFormatObj = getMailContent("E-Booking", "submitmail", "C");
                    }

                    String msg = mailFormatObj.getStrMailContent();
                    msg = msg.replace("@book_no", book_refno);
                    subject = mailFormatObj.getStrSubject();
                    subject = subject + "-(" + book_refno + ")";

                    rvo.setStrMailMsg(msg);

                    rvo.setStrSubject(subject);

                    rvo.setActivityCode(mailFormatObj.getActivityCode());
                    rvo.setStrFromMailId(mailFormatObj.getStrFromAddr());
                    System.out.println("[ECOM] EcomBooingBean ===>setStrToMailId<==" + ApEmailID);
                    rvo.setStrToMailId(ApEmailID);
                    //
                    if (rvo.getAdmToMailId() != null && rvo.getAdmToMailId().length() > 0) {
                        MailFormatVO mailFormatObj2 = new MailFormatVO();
                        mailFormatObj2 = getMailContent("E-Booking", "Update", "C");
                        String msg2 = mailFormatObj2.getStrMailContent();
                        msg2 = msg2.replaceAll("@book_no", book_refno);
                        // msg2 = msg2.replaceAll("@customer", "Agency");
                        msg2 = msg2.replaceAll("@client", mailFormatObj2.getStrRegard());
                        rvo.setAdmMailMsg(msg2);
                        String subject1 = mailFormatObj2.getStrSubject();
                        subject1 = subject1.replace("@bookref", book_refno);
                        rvo.setAdmSubject(subject1);
                        rvo.setAdmFromMailId(mailFormatObj2.getStrFromAddr());

                        //
                    }
                }
                if ("en".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    String strReturn = checkNull(getForwardMessage("E-Booking", msgtype, "c"));
                    strReturn = strReturn.replaceAll("@bookno", book_refno);
                    rvo.setStrDisplayMsg(strReturn);
                    rvo.setStrRefNo(book_refno);
                    rvo.setFilenames(filepath);
                } else if ("ch".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    String strReturn = checkNull(getForwardMessageLang("E-Booking", msgtype, "c", "ch"));
                    strReturn = strReturn.replaceAll("@bookno", book_refno);
                    rvo.setStrDisplayMsg(strReturn);
                    rvo.setStrRefNo(book_refno);
                    rvo.setFilenames(filepath);

                } else if ("ma".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    String strReturn = checkNull(getForwardMessageLang("E-Booking", msgtype, "c", "ma"));
                    strReturn = strReturn.replaceAll("@bookno", book_refno);
                    rvo.setStrDisplayMsg(strReturn);
                    rvo.setStrRefNo(book_refno);
                    rvo.setFilenames(filepath);
                } else if ("es".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    String strReturn = checkNull(getForwardMessageLang("E-Booking", msgtype, "c", "es"));
                    strReturn = strReturn.replaceAll("@bookno", book_refno);
                    rvo.setStrDisplayMsg(strReturn);
                    rvo.setStrRefNo(book_refno);
                    rvo.setFilenames(filepath);
                }
            }

            if ("S".equalsIgnoreCase(savetype)) {
                SaveToLog(loginCustName, "Booking", "Sent To Agency", "Booking Information Submitted", book_refno,
                        custcode);
            }

            if ("DU".equalsIgnoreCase(bookSAVETYPE)
                    && !"S".equalsIgnoreCase(savetype)) {
                SaveToLog(loginCustName, "Booking", "Update", "Booking Information Updated", book_refno, custcode);
            }

            if ("DI".equalsIgnoreCase(bookSAVETYPE)
                    && !"S".equalsIgnoreCase(savetype)) {
                SaveToLog(loginCustName, "Booking", "Insert", "Booking Information Saved as Draft", book_refno,
                        custcode);
            }

            if ("TE".equalsIgnoreCase(bookSAVETYPE)
                    && !"S".equalsIgnoreCase(savetype)) {
                SaveToLog(loginCustName, "Booking", "Update", "Booking Template Information Updated", book_refno,
                        custcode);
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EcomBooingBean Exception While calling  saveToBooking->" + e.getMessage());
            rvo.setStrRefNo("Error");
            System.out.println("[ECOM] EcomBooingBean >>set roll back");
            rvo.setStrDisplayMsg("Booking not saved.");
            e.printStackTrace();
            context.setRollbackOnly();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                System.out.println(
                        "[ECOM] EcomBooingBean Exception While Closing connetion in  saveToBooking->" + e.getMessage());
            }
        }
        return rvo;
    }

    private void SaveToLog(String username, String module, String activity, String remarks, String referenceno,
            String compcode) {

        Connection con = null;
        PreparedStatement logpstmt = null;
        try {
            String strLogSQL = "insert into EcommuserlogDetails(username,module,activity,remarks,referenceno,Logdate,customercode)values('"
                    + username + "','" + module + "','" + activity + "','" + remarks + "','" + referenceno
                    + "',GETDATE(),'" + compcode + "')";
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            logpstmt = con.prepareStatement(strLogSQL);
            logpstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (logpstmt != null) {
                    logpstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // end booking
    public String getAgencyMail(String agencycode) {
        String email = "";
        try {
            int i = 0;
            Map hs = (Map) ecomvdsservice.getEcomBookingAgencyMail(agencycode);
            //////
            List lst = (List) hs.get("agencycontacts");
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map bkg = (Map) lst.get(j);
                    String eml = (String) bkg.get("contactemailid");
                    if (i == 0) {
                        email = eml;
                    } else {
                        email = email + "," + eml;
                    }
                    i++;
                }
            }
        } catch (Exception e) {
        }
        return email;
    }

    public String getAgencyCountry(String agencycode) {
        String country = "";
        try {
            Map hs = (Map) ecomvdsservice.getecomlocationwithagency(agencycode);
            List lst = (List) hs.get("agency");
            if (lst != null && lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                country = checkNull((String) rs.get("countrycode")).trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return country;
    }

    public String getEqpTareW(String eqpType) throws RemoteException {

        String str = "";
        try {
            Map hs = (Map) ecomvdsservice.getecomeqptareweight(eqpType);
            //
            List lst = (List) hs.get("ecom_equipments");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                str = (String) rs.get("tarekg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    // set constraints
    public String setConstraints(String columnName, String con, String Val) throws RemoteException {
        String result = "";
        try {
            if (con.equalsIgnoreCase("Con")) {
                result = columnName + " Like '%" + Val + "%'";
            } else if (con.equalsIgnoreCase("equal")) {
                result = columnName + " ='" + Val + "'";
            } else if (con.equalsIgnoreCase("notEqual")) {
                result = columnName + " !='" + Val + "'";
            } else if (con.equalsIgnoreCase("start")) {
                result = columnName + " Like '" + Val + "%'";
            } else if (con.equalsIgnoreCase("DStart")) {
                result = columnName + " not Like '" + Val + "%'";
            } else if (con.equalsIgnoreCase("End")) {
                result = columnName + " Like '%" + Val + "'";
            } else if (con.equalsIgnoreCase("DEnd")) {
                result = columnName + " not Like '%" + Val + "'";
            } else if (con.equalsIgnoreCase("DCon")) {
                result = columnName + " not Like '%" + Val + "%'";
            }
        } catch (Exception e) {
            result = "";
            e.printStackTrace();
        }
        return result;
    }

    @Override
    @SuppressWarnings("static-access")
    public String getAgencydetails(String port) throws RemoteException {
        String agency = "";
        if (port != null && port.contains("-")) {
            port = port.substring(0, port.indexOf("-"));
        }
        try {
            Map hs = (Map) ecomvdsservice.getEcomAgencyLocation(port);
            //////
            List lst = (List) hs.get("agencylocations");
            if (lst != null && lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                agency = (String) rs.get("agencycode");
            }
            System.out.println("[ECOM] EcomBooingBean =======getAgencydetails=======>>>>>" + agency);

        } catch (Exception e) {
            System.out.println("[ECOM] EcomBooingBean Exception while geting Agency code  " + e.getMessage());
            e.printStackTrace();
        }
        return agency;
    }

    @Override
    public String getbkgconfigDetails(String agencycode, String maxcode, String mincode) throws RemoteException {

        String maxvalue = "";
        String minvalue = "";
        String maxAvalue = "";
        String minAvalue = "";
        StringBuffer sb = new StringBuffer("");
        Map hs = null;
        List lst = null;
        try {

            hs = (Map) ecomvdsservice.getEcomBkgConfigDetails(maxcode);
            //////
            lst = (List) hs.get("bkgconfig");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                maxvalue = (String) rs.get("value");
                //
                hs = (Map) ecomvdsservice.getEcomBkgConfigDetails(mincode);
                lst = (List) hs.get("bkgconfig");
                if (lst.size() > 0) {
                    Map rs1 = (Map) lst.get(0);
                    minvalue = (String) rs1.get("value");
                }
                sb.append(maxvalue).append(",");
                sb.append(minvalue).append("##");
            } else {
                sb = new StringBuffer();
                sb.append("nominmaxvalue").append("##");
            }
            //
            String agency = getAgencydetails(checkNull(agencycode));
            //
            hs = (Map) ecomvdsservice.getEcomBkgAgencyConfig(maxcode, agency);
            //////
            lst = (List) hs.get("bkgagencyconfig");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                maxAvalue = (String) rs.get("modulename");

                //
                hs = (Map) ecomvdsservice.getEcomBkgAgencyConfig(mincode, agency);
                lst = (List) hs.get("bkgagencyconfig");
                if (lst.size() > 0) {
                    Map rs1 = (Map) lst.get(0);
                    minAvalue = (String) rs1.get("modulename");
                }
                sb.append(maxAvalue).append(",");
                sb.append(minAvalue);
            } else {
                sb.append("nominmaxAvalue");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public ArrayList getEbookingRoutingDetailsfrombookingleg(String bookNo) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null, rs1 = null;
        ArrayList all = new ArrayList();
        Route routing = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(Routingfromleg_qry);
            ps.setString(1, bookNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                routing = new Route();
                routing.setLegNo(rs.getInt("leg_no"));
                routing.setBound(checkNull(rs.getString("bound")));
                routing.setService(checkNull(rs.getString("servicecode")));
                routing.setService(checkNull(rs.getString("servicecode")));
                routing.setVessel(checkNull(rs.getString("vesselcode")));
                routing.setVoyage(checkNull(rs.getString("voyagecode")));
                routing.setLoadport(checkNull(rs.getString("loadportid")));
                routing.setDisport(checkNull(rs.getString("dischargeportid")));
                routing.setETD1(checkNull(rs.getString("etddate")));
                routing.setETA1(checkNull(rs.getString("etadate")));
                routing.setLegType(checkNull(rs.getString("legtypeid")));
                routing.setGrpCallId(checkNull(rs.getString("groupcallid")));
                routing.setLoadcallid(checkNull(rs.getString("callIdLoading")));
                routing.setDischargecallid(checkNull(rs.getString("callIdDischarge")));
                routing.setBlpolTer(checkNull(rs.getString("loadterminal")));
                routing.setBlpodTer(checkNull(rs.getString("dischargeTerminal")));
                routing.setLoadter(checkNull(rs.getString("loadterminal")));
                routing.setDister(checkNull(rs.getString("dischargeTerminal")));
                routing.setDischargebound(checkNull(rs.getString("dbound")));
                //
                Map hs = (Map) ecomvdsservice.getecomgetoffterminal(checkNull(rs.getString("groupcallid")));
                //////
                List lst = (List) hs.get("allschedules");
                if (lst.size() > 0) {
                    Map bkg = (Map) lst.get(0);
                    String result = (String) bkg.get("arrdate");
                    routing.setCutoff_polarrdate(checkNull(result));
                }
                all.add(routing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return all;
    }

    //
    @Override
    public String getPortType(String portcode) throws RemoteException {

        String type = "";
        portcode = checkNull(portcode);
        try {
            Map hs = (Map) ecomvdsservice.getecomports(portcode);
            //
            System.out.println("[ECOM] EcomBooingBean ######>" + hs);
            List lst = (List) hs.get("ports");
            if (lst.size() > 0) {
                Map bkg = (Map) lst.get(0);
                type = (String) bkg.get("porttype");
                type = checkNull(type);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    private String checkNullbigDecimal(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"0.0000".equalsIgnoreCase(str))
                    && (!"".equalsIgnoreCase(str))) ? str.trim() : "";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    private String checkNullDecimal(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "0.0000";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    private String checkNullUpperCase(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str)))
                    ? str.toUpperCase().trim()
                    : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    @Override
    public ArrayList getLRPBookingViewDetails(String bookNo) throws RemoteException {

        ArrayList viewlist = null;
        String custType = null;
        boolean n1 = false;
        String phone = null, fax = null;
        EbookingValidatorFormBeanNew bookVO = null;
        SimpleDateFormat target = new SimpleDateFormat(GeneralConf.getStrDbFormat());
        SimpleDateFormat source = new SimpleDateFormat(GeneralConf.strDbFormatWithOutTime);
        try {
            viewlist = new ArrayList();
            bookVO = new EbookingValidatorFormBeanNew();
            Map hs = (Map) ecomvdsservice.getecombookingdetails(bookNo);
            System.out.println("[ECOM] EcomBooingBean >>lrp booking details>>" + hs);
            //
            List lst = (List) hs.get("booking");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                System.out.println("[ECOM] EcomBooingBean rs=============>" + rs);
                bookVO.setEbook_date((String) rs.get("bookdate"));
                bookVO.setEbook_dateBut((String) rs.get("bookdate"));
                bookVO.setAmendment("Y");
                bookVO.setBooking_refno((String) rs.get("book_no"));
                bookVO.setPodintransit((String) rs.get("intransitport"));
                bookVO.setCarrierContact((String) rs.get("transportserviceid"));
                // bookVO.setEmptyPickup((String) rs.get("polterminal"));
                bookVO.setEbook_origin_country((String) rs.get("originportid"));
                bookVO.setEbook_delivery_country((String) rs.get("finalportid"));
                bookVO.setEbook_origin(checkNull((String) rs.get("originportid")));
                bookVO.setEbook_delivery(checkNull((String) rs.get("finalportid")));
                bookVO.setEbook_pod_country((String) rs.get("dischargeportid"));
                bookVO.setEbook_pol_country((String) rs.get("loadportid"));
                bookVO.setEbook_pod((String) rs.get("dischargeportid"));
                bookVO.setEbook_pol((String) rs.get("loadportid"));
                bookVO.setEbook_shipterm_desc((String) rs.get("shipment_type"));
                bookVO.setEbook_servicecode((String) rs.get("servicecode"));
                bookVO.setEbook_vesselcode(checkNull((String) rs.get("vesselcode")));
                bookVO.setEbook_voyage(checkNull((String) rs.get("voyagecode")));
                bookVO.setEbook_pol_eta(checkNull((String) rs.get("polat")));
                bookVO.setEbook_pol_etd(checkNull((String) rs.get("podat")));
                bookVO.setPreCarriage(checkNull((String) rs.get("precarriage")));
                bookVO.setOnCarriage(checkNull((String) rs.get("oncarriage")));
                bookVO.setEbook_rate_reference("");
                bookVO.setEbook_agency_reference(checkNullUpperCase((String) rs.get("agencyrefno")));
                bookVO.setEbook_status_remarks(checkNull((String) rs.get("custremarks")));
                bookVO.setAgencyid(checkNull((String) rs.get("agencyid")));
                bookVO.setUserdateformat(getUserDateFormat(checkNull((String) rs.get("usercr"))));
                bookVO.setEnsfiling(checkNull((String) rs.get("ensfiling")));
                bookVO.setEbook_preferred_agency(checkNull((String) rs.get("agencyid")));
                bookVO.setEbook_t2lfiling(checkNull((String) rs.get("t2lfiling")));
                bookVO.setEbook_EnsDocType(checkNull((String) rs.get("enstype")));
                if (rs.get("cargoprotectid") != null) {
                    bookVO.setCargoprotectid((String) rs.get("cargoprotectid"));
                }
            }
            hs = (Map) ecomvdsservice.getecombookingreference(bookNo);
            //
            List reflst = (List) hs.get("booking_reference");
            if (reflst.size() > 0) {
                for (int j = 0; j < reflst.size(); j++) {
                    Map rs = (Map) reflst.get(j);
                    System.out.println("[ECOM] EcomBooingBean =rs================>" + rs);
                    String refid = (String) rs.get("refid");
                    if ("EOR".equalsIgnoreCase(refid)) {
                        bookVO.setEbook_rate_reference(checkNull((String) rs.get("refval")));
                    } else if ("EOF".equalsIgnoreCase(refid)) {
                        String[] ocfreight = checkNull((String) rs.get("refval")).split("~~");
                        bookVO.setEbook_ocean_freight(ocfreight[0]);
                    } else if ("EEP".equalsIgnoreCase(refid)) {
                        String[] emptypickup = checkNull((String) rs.get("refval")).split("~~");
                        if (emptypickup.length == 2) {
                            bookVO.setEmptyPickup(checkNull(emptypickup[1]));
                            bookVO.setEbook_pickup_point(checkNull(emptypickup[0]));
                        } else {
                            bookVO.setEmptyPickup(checkNull(emptypickup[0]));
                        }
                    } else if ("EPD".equalsIgnoreCase(refid)) {
                        bookVO.setEmptyPickUpDate(checkNull((String) rs.get("refval")));
                        System.out.println("[ECOM] EcomBooingBean >>checkNull((String) rs.get(\"refval\"))>>" + checkNull((String) rs.get("refval")));
                    } else if ("NAT".equalsIgnoreCase(refid)) {
                        bookVO.setEbook_natcode(checkNull((String) rs.get("refval")));
                    } else if ("HAU".equalsIgnoreCase(refid)) {
                        bookVO.setEbook_haulercode(checkNull((String) rs.get("refval")));
                    } else if ("ELD".equalsIgnoreCase(refid)) {
                        String loadDate = checkNull((String) rs.get("refval")).trim();
                        loadDate = target.format(source.parse(loadDate));
                        bookVO.setEbook_pickup_date(loadDate);
                    } else if ("EDD".equalsIgnoreCase(refid)) {
                        String dropDate = checkNull((String) rs.get("refval")).trim();
                        dropDate = target.format(source.parse(dropDate));
                        bookVO.setEbook_drop_date(dropDate);
                    } else if (EcomConsts.EXPORT_REF.equalsIgnoreCase(refid)) {
                        bookVO.setEbook_exportref(checkNull((String) rs.get("refval")));
                    } else if (EcomConsts.CUST_PO.equalsIgnoreCase(refid)) {
                        bookVO.setEbook_custpo(checkNull((String) rs.get("refval")));
                    } else if (EcomConsts.REF_TYPE.equalsIgnoreCase(refid)) {
                        // String[] relType = checkNull((String) rs.get("refval")).split("~~");
                        // bookVO.setEbook_reftype(relType[0]);
                        bookVO.setEbook_reftype(checkNull((String) rs.get("refval")));
                    } else if (EcomConsts.CUST_REF.equalsIgnoreCase(refid)) {
                        bookVO.setCustomerreference(checkNull((String) rs.get("refval")));
                    } else if (EcomConsts.ACID.equalsIgnoreCase(refid)) {
                        bookVO.setACID(checkNull((String) rs.get("refval")));
                        System.out.println("[ECOM] EcomBooingBean ACID PANDING IN LINE  ==>" + rs.get("refval"));
                    } else if ("DPW".equalsIgnoreCase(refid)) {
                        String storedValue = checkNull((String) rs.get("refval"));
                        if ("nullnull".equalsIgnoreCase(storedValue)) {
                            storedValue = "";
                        }
                        if (storedValue.length() > 0) {
                            String type = storedValue.substring(0, 1);
                            String code = storedValue.substring(1);
                            bookVO.setShipperReferenceType(checkNull(type));
                            bookVO.setShipperReference(checkNull(code));
                        } else {
                            bookVO.setShipperReferenceType("");
                            bookVO.setShipperReference("");
                        }

                    } else if ("SPC".equalsIgnoreCase(refid)) {
                        bookVO.setEbook_sisubmit_party(checkNull((String) rs.get("refval")));
                    }
                }
            }

            //
            hs = (Map) ecomvdsservice.getecombookingcust(bookNo);
            List custlst = (List) hs.get("booking_cust");
            if (custlst.size() > 0) {
                for (int j = 0; j < custlst.size(); j++) {
                    Map rs = (Map) custlst.get(j);
                    custType = checkNull((String) rs.get("custtypeid"));
                    if (custType.equalsIgnoreCase("BP")) {
                        bookVO.setEbook_bookingparty_desc(checkNull((String) rs.get("name")));
                        bookVO.setEbook_bookingparty_desc(checkNull((String) rs.get("name")));
                        bookVO.setEbook_bookingcity(checkNull((String) rs.get("city")));
                        bookVO.setEbook_bookingcity_desc(checkNull((String) rs.get("city")));
                        bookVO.setEbook_bookingcountry(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_bookingname(checkNull((String) rs.get("name")));
                        bookVO.setEbook_bookingcode(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_bookingaddress(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_bookingaddress1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_bookingemail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_bookingstate(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_bookingzipcode(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_Custeori(checkNull((String) rs.get("eori")));
                        bookVO.setEbook_bookingeori(checkNull((String) rs.get("eori")));
                        bookVO.setBookingtypeperson(checkNull((String) rs.get("persontype")));
                        phone = checkNull((String) rs.get("telephone"));
                        fax = checkNull((String) rs.get("fax"));
                        if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                            bookVO.setEbook_bookingphone(phone);
                        } else {
                            bookVO.setEbook_bookingphone("");
                        }
                        if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                            bookVO.setEbook_bookingfax(fax);
                        } else {
                            bookVO.setEbook_bookingfax("");
                        }
                    }
                    if (custType.equalsIgnoreCase("AP")) {

                        bookVO.setEbook_agreecity(checkNull((String) rs.get("city")));
                        bookVO.setEbook_bookingparty_desc(checkNull((String) rs.get("name")));
                        bookVO.setEbook_agreecity_desc(checkNull((String) rs.get("city")));
                        bookVO.setEbook_agreecountry(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_agreename(checkNull((String) rs.get("name")));
                        bookVO.setEbook_agreecode(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_agreeaddress(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_agreeaddress1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_agreephone(checkNull((String) rs.get("telephone")));
                        bookVO.setEbook_agreeemail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_agreefax(checkNull((String) rs.get("fax")));
                        bookVO.setEbook_agreestate(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_agree_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_agreezipcode(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_agreeeori(checkNull((String) rs.get("eori")));
                        bookVO.setAgreetypeperson(checkNull((String) rs.get("persontype")));
                    } else if (custType.equalsIgnoreCase("SH")) {
                        bookVO.setEbook_shippercity(checkNull((String) rs.get("city")));
                        bookVO.setEbook_shippercity_desc(checkNull((String) rs.get("city")));
                        bookVO.setEbook_shippercountry(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_shippername(checkNull((String) rs.get("name")));
                        bookVO.setEbook_shippercode(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_shipperaddress(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_shipperaddress1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_shipperphone(checkNull((String) rs.get("telephone")));
                        bookVO.setEbook_shipperemail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_shipperfax(checkNull((String) rs.get("fax")));
                        bookVO.setEbook_shipperstate(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_shipper_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_shipperzipcode(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_shippereori(checkNull((String) rs.get("eori")));
                        bookVO.setShippertypeperson(checkNull((String) rs.get("persontype")));
                    } else if (custType.equalsIgnoreCase("CN")) {
                        bookVO.setEbook_consigneecity(checkNull((String) rs.get("city")));
                        bookVO.setEbook_consigneecity_desc(checkNull((String) rs.get("city")));
                        bookVO.setEbook_consigneecountry(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_consigneename(checkNull((String) rs.get("name")));
                        bookVO.setEbook_consigneecode(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_consigneeaddress(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_consigneeaddress1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_consigneephone(checkNull((String) rs.get("telephone")));
                        bookVO.setEbook_consigneeemail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_consigneefax(checkNull((String) rs.get("fax")));
                        bookVO.setEbook_consigneestate(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_consigne_id((checkNull((String) rs.get("cid"))));
                        bookVO.setEbook_consigneezipcode(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_consigneeeori(checkNull((String) rs.get("eori")));
                        bookVO.setConsigneetypeperson(checkNull((String) rs.get("persontype")));
                    } else if (custType.equalsIgnoreCase("NT") && n1 == false) {
                        n1 = true;
                        bookVO.setEbook_notifycity(checkNull((String) rs.get("city")));
                        bookVO.setEbook_notifycity_desc(checkNull((String) rs.get("city")));
                        bookVO.setEbook_notifycountry(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_notifyname(checkNull((String) rs.get("name")));
                        bookVO.setEbook_notifycode(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_notifyaddress(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_notifyaddress1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_notifyphone(checkNull((String) rs.get("telephone")));
                        bookVO.setEbook_notifyemail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_notifyfax(checkNull((String) rs.get("fax")));
                        bookVO.setEbook_notifystate(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_notify_id((checkNull((String) rs.get("cid"))));
                        bookVO.setEbook_notifyzipcode(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_notifyeori(checkNull((String) rs.get("eori")));
                        bookVO.setNotifytypeperson(checkNull((String) rs.get("persontype")));
                    } else if (custType.equalsIgnoreCase("NT") && n1 == true) {
                        bookVO.setEbook_notifycity_desc_n1(checkNull((String) rs.get("city")));
                        bookVO.setEbook_notifycity_n1(checkNull((String) rs.get("city")));
                        bookVO.setEbook_notifycountry_n1(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_notifyname_n1(checkNull((String) rs.get("name")));
                        bookVO.setEbook_notifycode_n1(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_notifyaddress_n1(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_notifyaddress1_n1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_notifyphone_n1(checkNull((String) rs.get("telephone")));
                        bookVO.setEbook_notifyemail_n1(checkNull((String) rs.get("email")));
                        bookVO.setEbook_notifyfax_n1(checkNull((String) rs.get("fax")));
                        bookVO.setEbook_notifystate_n1(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_notify_id_n1((checkNull((String) rs.get("cid"))));
                        bookVO.setEbook_notifyzipcode_n1(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_notifyeori_n1(checkNull((String) rs.get("eori")));
                        bookVO.setNotifytypeperson_n1(checkNull((String) rs.get("persontype")));
                    }
                    if (custType.equalsIgnoreCase("FW")) {

                        bookVO.setEbook_forwardercity(checkNull((String) rs.get("city")));
                        bookVO.setEbook_forwardername(checkNull((String) rs.get("name")));
                        bookVO.setEbook_forwardercity_desc(checkNull((String) rs.get("city")));
                        bookVO.setEbook_forwardercountry(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_forwardername(checkNull((String) rs.get("name")));
                        bookVO.setEbook_forwardercode(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_forwarderaddress(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_forwarderaddress1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_forwarderphone(checkNull((String) rs.get("telephone")));
                        bookVO.setEbook_forwarderemail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_forwarderfax(checkNull((String) rs.get("fax")));
                        bookVO.setEbook_forwarderstate(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_forwarder_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_forwarderzipcode(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_forwardereori(checkNull((String) rs.get("eori")));
                        bookVO.setForwardertypeperson(checkNull((String) rs.get("persontype")));
                    }
                    if (custType.equalsIgnoreCase("WH")) {

                        bookVO.setEbook_Warehousecity(checkNull((String) rs.get("city")));
                        bookVO.setEbook_Warehousename(checkNull((String) rs.get("name")));
                        bookVO.setEbook_Warehousecity_desc(checkNull((String) rs.get("city")));
                        bookVO.setEbook_Warehousecountry(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_Warehousename(checkNull((String) rs.get("name")));
                        bookVO.setEbook_Warehousecode(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_Warehouseaddress(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_Warehouseaddress1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_Warehousephone(checkNull((String) rs.get("telephone")));
                        bookVO.setEbook_Warehouseemail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_Warehousefax(checkNull((String) rs.get("fax")));
                        bookVO.setEbook_Warehousestate(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_Warehouse_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_Warehousezipcode(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_Warehouseeori(checkNull((String) rs.get("eori")));
                        bookVO.setWarehousetypeperson(checkNull((String) rs.get("persontype")));
                    }
                    if (custType.equalsIgnoreCase("NC")) {
                        bookVO.setEbook_NamedCustomercity(checkNull((String) rs.get("city")));
                        bookVO.setEbook_NamedCustomername(checkNull((String) rs.get("name")));
                        bookVO.setEbook_NamedCustomercity_desc(checkNull((String) rs.get("city")));
                        bookVO.setEbook_NamedCustomercountry(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_NamedCustomername(checkNull((String) rs.get("name")));
                        bookVO.setEbook_NamedCustomercode(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_NamedCustomeraddress(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_NamedCustomeraddress1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_NamedCustomerphone(checkNull((String) rs.get("telephone")));
                        bookVO.setEbook_NamedCustomeremail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_NamedCustomerfax(checkNull((String) rs.get("fax")));
                        bookVO.setEbook_NamedCustomerstate(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_NamedCustomer_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_NamedCustomerzipcode(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_NamedCustomereori(checkNull((String) rs.get("eori")));
                        bookVO.setEbook_NamedCustomerpersontype(checkNull((String) rs.get("persontype")));
                    }
                    if (custType.equalsIgnoreCase("SL")) {
                        bookVO.setEbook_Sellercity(checkNull((String) rs.get("city")));
                        bookVO.setEbook_Sellername(checkNull((String) rs.get("name")));
                        bookVO.setEbook_Sellercity_desc(checkNull((String) rs.get("city")));
                        bookVO.setEbook_Sellercountry(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_Sellername(checkNull((String) rs.get("name")));
                        bookVO.setEbook_Sellercode(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_Selleraddress(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_Selleraddress1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_Sellerphone(checkNull((String) rs.get("telephone")));
                        bookVO.setEbook_Selleremail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_Sellerfax(checkNull((String) rs.get("fax")));
                        bookVO.setEbook_Sellerstate(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_Seller_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_Sellerzipcode(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_Sellereori(checkNull((String) rs.get("eori")));
                        bookVO.setEbook_Sellerpersontype(checkNull((String) rs.get("persontype")));
                    }
                    if (custType.equalsIgnoreCase("BY")) {
                        bookVO.setEbook_Buyercity(checkNull((String) rs.get("city")));
                        bookVO.setEbook_Buyername(checkNull((String) rs.get("name")));
                        bookVO.setEbook_Buyercity_desc(checkNull((String) rs.get("city")));
                        bookVO.setEbook_Buyercountry(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_Buyername(checkNull((String) rs.get("name")));
                        bookVO.setEbook_Buyercode(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_Buyeraddress(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_Buyeraddress1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_Buyerphone(checkNull((String) rs.get("telephone")));
                        bookVO.setEbook_Buyeremail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_Buyerfax(checkNull((String) rs.get("fax")));
                        bookVO.setEbook_Buyerstate(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_Buyer_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_Buyerzipcode(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_Buyereori(checkNull((String) rs.get("eori")));
                        bookVO.setEbook_Buyerpersontype(checkNull((String) rs.get("persontype")));
                    }
                }
            }
            viewlist.add(bookVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewlist;

    }

    @Override
    public ArrayList getLRPBookingRoutingDetails(String bookNo) throws RemoteException {
        ArrayList all = new ArrayList();
        Route routing = null;
        try {
            Map hs = (Map) ecomvdsservice.getecombookingleg(bookNo);
            List lst = (List) hs.get("booking_leg");
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map rs = (Map) lst.get(j);
                    routing = new Route();
                    int legno = Integer.parseInt((String) rs.get("leg_no"));
                    routing.setLegNo(legno);
                    routing.setBound(checkNull((String) rs.get("bound")));
                    routing.setVoyage(checkNull((String) rs.get("voyagecode")));
                    routing.setService(checkNull((String) rs.get("servicecode")));
                    routing.setService(checkNull((String) rs.get("servicecode")));
                    routing.setVessel(checkNull((String) rs.get("vesselcode")));
                    routing.setLoadport(checkNull((String) rs.get("loadportid")));
                    routing.setDisport(checkNull((String) rs.get("dischargeportid")));
                    routing.setETD1(checkNull((String) rs.get("etddate")));
                    routing.setETA1(checkNull((String) rs.get("etadate")));
                    routing.setFeeder(checkNull((String) rs.get("legtypeid")));
                    routing.setLegType(checkNull((String) rs.get("legtypeid")));
                    all.add(routing);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return all;
    }

    @Override
    public ArrayList getLRPBookingViewCargoDetails(String bookNo, String ecombookno) throws RemoteException {
        ArrayList cargoList = null;
        int i = 0;
        int qty = 0;
        EbookingValidatorFormBeanNew eBookingcargo = null;
        Map comhs = null;
        List comlst = null;
        boolean dupCont = false;
        String prevCntr = null, currentCntr = null;
        String eqpId = null, cargoVol = null, weight = null, VolumeUnt = null, hazardous = null, oog = null, rfr = null, lcl = null, cargoType = null, netWt = null;
        Map commhs = null, insmap = null;
        String qouteno = null;
        try {
            insmap = getInsuranceDetils(bookNo, "L");
            cargoList = new ArrayList();
            int j = 1;
            Map hs = (Map) ecomvdsservice.getEcomBookingCargoForView(bookNo);
            List lst = (List) hs.get("booking_cargo");
            if (lst.size() > 0) {
                for (int k = 0; k < lst.size(); k++) {
                    Map rs = (Map) lst.get(k);
                    eBookingcargo = new EbookingValidatorFormBeanNew();
                    eqpId = checkNull((String) rs.get("eqpid"));
                    // F
                    if (i == 0) {
                        prevCntr = Validation.checkNull((String) rs.get("eqpid"));
                        currentCntr = Validation.checkNull((String) rs.get("eqpid"));
                    }
                    currentCntr = Validation.checkNull((String) rs.get("eqpid"));
                    dupCont = prevCntr.equalsIgnoreCase(currentCntr) ? false : true;
                    eBookingcargo.setCargoid_new(String.valueOf(j));
                    if (!dupCont) {
                        eBookingcargo.setDuplicateEqp("Y");
                        j++;
                    } else {
                        eBookingcargo.setDuplicateEqp("N");
                    }
                    i++;
                    eBookingcargo.setEbook_packquantity((String) rs.get("itemqty"));
                    eBookingcargo.setContainerNo(checkNull((String) rs.get("eqpid")));
                    eBookingcargo.setEbook_weightunit(checkNull((String) rs.get("weightunit")));
                    weight = checkNull((String) rs.get("cargoweight"));
                    weight = String.valueOf(new BigDecimal(weight).toPlainString());
                    eBookingcargo.setEbook_cargoweight(ConvertDecimalFormat(weight));
                    weight = checkNull((String) rs.get("tareweight"));
                    weight = String.valueOf(new BigDecimal(weight).toPlainString());
                    eBookingcargo.setEbook_tareweight(ConvertDecimalFormat(weight));
                    weight = checkNull((String) rs.get("grossweight"));
                    weight = String.valueOf(new BigDecimal(weight).toPlainString());
                    eBookingcargo.setEbook_grossweight(ConvertDecimalFormat(weight));
                    eBookingcargo.setEqpstatus(checkNull((String) rs.get("eqpstatus")));
                    String eqp_type = checkNull((String) rs.get("eqptypeid"));
                    eBookingcargo.setEbook_eqp_type(checkNull(""));
                    eBookingcargo.setEbook_eqp(eqp_type);

                    cargoVol = checkNullDecimal((String) rs.get("itemvolume"));
                    cargoVol = String.valueOf(new BigDecimal(cargoVol).toPlainString());
                    cargoVol = checkNullbigDecimal(cargoVol);
                    netWt = checkNullDecimal((String) rs.get("nettwt"));
                    netWt = String.valueOf(new BigDecimal(netWt).toPlainString());
                    netWt = checkNullbigDecimal(netWt);
                    VolumeUnt = checkNull((String) rs.get("itemvolumeunit"));

                    eBookingcargo.setEbook_cargovolume(cargoVol);
                    eBookingcargo.setEbook_netweight(netWt);
                    eBookingcargo.setEbook_volumeunit(VolumeUnt);
                    cargoType = "";
                    if (checkNull((String) rs.get("nor")).equalsIgnoreCase("Y")
                            || (checkNull((String) rs.get("nor")).equalsIgnoreCase("N")
                            && (checkNull((String) rs.get("settemp")).length() > 0))) {
                        cargoType = "R";
                    } else if (checkNull((String) rs.get("oog")).equalsIgnoreCase("Y")) {
                        cargoType = "O";
                    } else {
                        cargoType = "D";
                    }
                    eBookingcargo.setEbook_eqp_type(cargoType);
                    if (cargoType.equalsIgnoreCase("R")) {
                        rfr = "Y";
                    } else {
                        rfr = "N";
                    }
                    if (cargoType.equalsIgnoreCase("L")) {
                        lcl = "Y";
                    } else {
                        lcl = "N";
                    }
                    eBookingcargo.setRfr(rfr);
                    eBookingcargo.setOog(checkNull((String) rs.get("oog")));
                    eBookingcargo.setSoc(checkNull((String) rs.get("shipperown")));
                    eBookingcargo.setHidden_soc(checkNull((String) rs.get("shipperown")));
                    eBookingcargo.setLcl(lcl);

                    String settemp = checkNull((String) rs.get("settemp"));
                    String tempunit = checkNull((String) rs.get("tempunit"));
                    String mintemp = checkNull((String) rs.get("tempmin"));
                    String maxtemp = checkNull((String) rs.get("tempmax"));
                    if ("0.0".equalsIgnoreCase(settemp)) {
                        settemp = "";
                        tempunit = "";
                        mintemp = "";
                        maxtemp = "";
                    }
                    eBookingcargo.setEbook_nor(checkNull((String) rs.get("nor")));
                    eBookingcargo.setEbook_presetemp(settemp);
                    eBookingcargo.setEbook_temperunit(tempunit);
                    eBookingcargo.setEbook_mintemp(mintemp);
                    eBookingcargo.setEbook_maxtemp(maxtemp);

                    eBookingcargo.setHumidity(checkNull((String) rs.get("humidity")));
                    eBookingcargo.setHumidityflag(checkNull((String) rs.get("humidity_flag")));
                    eBookingcargo.setDrains(checkNull((String) rs.get("drains")));
                    eBookingcargo.setCo2(checkNull((String) rs.get("co2value")));
                    eBookingcargo.setO2(checkNull((String) rs.get("o2value")));
                    eBookingcargo.setVentilation(checkNull((String) rs.get("ventpercentage")));
                    // rs1 details
                    eBookingcargo.setEbook_commoditycode(checkNull((String) rs.get("comdtygrp")));
                    eBookingcargo.setHscode(checkNull((String) rs.get("commodityid")));
                    eBookingcargo.setEbook_eqp_qty(checkNull((String) rs.get("itemno")));
                    eBookingcargo.setEbook_hazardous(checkNull((String) rs.get("hazardous")));
                    eBookingcargo.setHzd(checkNull((String) rs.get("hazardous")));
                    eBookingcargo.setEbook_packagecode(checkNull((String) rs.get("packageid")));
                    eBookingcargo.setEbook_actCommoName(checkNull((String) rs.get("marksdesc")));
                    eBookingcargo.setEbook_cmdtyhblno(checkNull((String) rs.get("cmdty_hblno")));
                    eBookingcargo.setEbook_cmdtysellercode(checkNull((String) rs.get("slcode")));
                    eBookingcargo.setEbook_cmdtysellerName(checkNull((String) rs.get("slname")));
                    eBookingcargo.setEbook_cmdtybuyercode(checkNull((String) rs.get("bycode")));
                    eBookingcargo.setEbook_cmdtybuyerName(checkNull((String) rs.get("byname")));
                    System.out.println("[ECOM] EcomBooingBean marksdescbkg--->" + (String) rs.get("marksdesc")
                            + " marksdescrbkg--->" + (String) rs.get("descr"));
                    String olfwd = checkNull((String) rs.get("itemforward"));
                    String owlft = checkNull((String) rs.get("itemleft"));
                    String oh = checkNull((String) rs.get("itemtop"));
                    String olaft = checkNull((String) rs.get("itemaft"));
                    String owrgt = checkNull((String) rs.get("itemright"));
                    String dimenUnt = checkNull((String) rs.get("itemlengthunit"));
                    if (checkNull((String) rs.get("oog")).equalsIgnoreCase("Y")) {
                        if (!(olfwd.length() > 0)) {
                            olfwd = "0";
                        }
                        if (!(owlft.length() > 0)) {
                            owlft = "0";
                        }
                        if (!(oh.length() > 0)) {
                            oh = "0";
                        }
                        if (!(olaft.length() > 0)) {
                            olaft = "0";
                        }
                        if (!(owrgt.length() > 0)) {
                            owrgt = "0";
                        }
                    }
                    eBookingcargo.setEbook_olforward(olfwd);
                    eBookingcargo.setEbook_owleft(owlft);
                    eBookingcargo.setEbook_oh(oh);
                    eBookingcargo.setEbook_olaft(olaft);
                    eBookingcargo.setEbook_owright(owrgt);
                    eBookingcargo.setEbook_dimensionunit(dimenUnt);
                    hazardous = checkNull((String) rs.get("hazardous"));
                    // String strPattern = "^0+";
                    System.out.println("[ECOM] EcomBooingBean ---->" + eBookingcargo.getEbook_commoditycode()
                            + "---->" + (insmap.containsKey((eBookingcargo.getEbook_commoditycode()))));
                    if (insmap != null) {
                        eBookingcargo.setInsuredemail(checkNull((String) insmap.get("insuredemail")));
                        if ((insmap.containsKey((eBookingcargo.getEbook_commoditycode()))
                                || insmap.containsKey((eBookingcargo.getHscode())))) {// .replaceAll(strPattern, "")
                            qouteno = checkNull((String) insmap.get((eBookingcargo.getEbook_commoditycode())));
                            if (qouteno.length() == 0) {
                                qouteno = checkNull((String) insmap.get((eBookingcargo.getHscode())));
                            }
                            System.out.println("[ECOM] EcomBooingBean qoute==> " + qouteno);
                            eBookingcargo.setIsinsuranceavailable("Y");
                            eBookingcargo.setQuoteno(qouteno);
                        }
                    }
                    cargoList.add(eBookingcargo);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cargoList;
    }

    @Override
    public List getMoreCustomerDetails(String custCode, String custTypeconfig) throws RemoteException {
        System.out.println("[ECOM] EcomBooingBean   custcode " + custCode);
        List custList = null;
        try {
            custList = new ArrayList();
            // SH
            custList = getCustomerDetails(custList, "SH", custCode, custTypeconfig);
            // CO
            custList = getCustomerDetails(custList, "CN", custCode, custTypeconfig);
            // NT
            custList = getCustomerDetails(custList, "NT", custCode, custTypeconfig);
            // AP
            custList = getCustomerDetails(custList, "AP", custCode, custTypeconfig);
            // N1
            custList = getCustomerDetails(custList, "N1", custCode, custTypeconfig);
            // N2
            custList = getCustomerDetails(custList, "N2", custCode, custTypeconfig);
            // FW
            custList = getCustomerDetails(custList, "FW", custCode, custTypeconfig);
            // WH
            custList = getCustomerDetails(custList, "WH", custCode, custTypeconfig);
            // NC
            custList = getCustomerDetails(custList, "NC", custCode, custTypeconfig);
            // SL
            custList = getCustomerDetails(custList, "SL", custCode, custTypeconfig);
            // BY
            custList = getCustomerDetails(custList, "BY", custCode, custTypeconfig);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return custList;
    }

    private List getCustomerDetails(List custList, String custtype, String custcode, String custTypeconfig) {
        Map rethm = null, ret2 = null;
        List vlist = null;
        CustomerDetailsVO custVO = null;
        String phone = null, fax = null, email = null;
        SvmJsonList alist = null;
        try {
            alist = new SvmJsonList();
            System.out.println("[ECOM] EcomBooingBean custcode: " + custcode + ", custtype: " + custtype
                    + ", custTypeconfig: " + custTypeconfig);
            alist.add(custcode);
            alist.add("366");
            alist.add("");
            alist.add(custtype);
            alist.add("");
            alist.add(custTypeconfig);
            rethm = ecomvdspservice.getecomcustfrombooking(alist);
            if (rethm != null && rethm.size() > 0) {
                ret2 = (Map) rethm.get("rs");
                if (ret2 != null && ret2.size() > 0) {
                    vlist = (List) ret2.get("rs0");
                    if (vlist != null && vlist.size() > 0) {
                        for (int i = 0; i < vlist.size(); i++) {
                            Map rs = (Map) vlist.get(i);
                            custVO = new CustomerDetailsVO();
                            custVO.setName(checkNull((String) rs.get("custname")));
                            custVO.setCustCode(checkNull((String) rs.get("custcode")));
                            custVO.setCustType(checkNull((String) rs.get("custtypeid")));
                            custVO.setAddress(checkNull((String) rs.get("address1")));
                            custVO.setAddress1(checkNull((String) rs.get("address2")));
                            custVO.setCity(checkNull((String) rs.get("city")));
                            custVO.setCountryCode(checkNull((String) rs.get("countryid")));
                            System.out.println("[ECOM] EcomBooingBean ##########>countryid" + rs.get("countryid"));
                            custVO.setCountry(checkNull((String) rs.get("countryid")));
                            custVO.setState(checkNull((String) rs.get("state")));
                            custVO.setZipcode(checkNull((String) rs.get("zipcode")));
                            phone = checkNull((String) rs.get("telephone"));
                            fax = checkNull((String) rs.get("fax"));
                            if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                                custVO.setPhoneNo(checkNull(phone).replace("+", ""));
                            } else {
                                custVO.setPhoneNo("");
                            }
                            if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                                custVO.setFax(checkNull(fax).replace("+", ""));
                            } else {
                                custVO.setFax("");
                            }
                            email = checkNull((String) rs.get("email"));
                            if (email.length() > 0 && email.equalsIgnoreCase("-")) {
                                email = "";
                            }
                            custVO.setEmail(email);
                            custVO.setTaxId(checkNull((String) rs.get("vatno")));

                            custVO.setEroi(checkNull((String) rs.get("eori")));
                            custVO.setPerontype(checkNull((String) rs.get("persontype")));
                            custList.add(custVO);
                        }
                    }
                }
            } // END SH
            System.out.println("[ECOM] EcomBooingBean custList=" + custList.size() + "custtype=>" + custtype);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return custList;
    }

    @Override
    public String getUNODetails(String eqpsize) throws RemoteException {

        ArrayList unno = new ArrayList();
        StringBuffer sb = new StringBuffer("");
        try {
            Map hs = (Map) ecomvdsservice.getecomunodetails(eqpsize);
            //////
            System.out.println("[ECOM] EcomBooingBean ######>" + hs);
            List lst = (List) hs.get("equipments");
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map rs = (Map) lst.get(j);
                    unno.add(checkNull((String) rs.get("unno")) + "~~" + checkNull((String) rs.get("shippingname"))
                            + "-" + checkNull((String) rs.get("unno")));
                }
            }
            if (unno != null && unno.size() > 0) {
                for (int i = 0; i < unno.size(); i++) {
                    sb.append(unno.get(i));
                    sb.append("##");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public String getIMDGClassDetails(String unno) throws RemoteException {

        StringBuffer sb = new StringBuffer("");
        try {
            Map hs = (Map) ecomvdsservice.getecomimgclassdetails(unno);
            //
            System.out.println("[ECOM] EcomBooingBean ######>" + hs);
            List lst = (List) hs.get("dgdetail");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                sb.append(checkNull((String) rs.get("imdgclass")));
                sb.append("~~");
                sb.append(checkNull((String) rs.get("shippingname")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public String getTareweight(String type, String equipType) throws RemoteException {

        String result = "0";
        int i = 0;
        try {
            Map hs = (Map) ecomvdsservice.getecomtareweight(equipType);
            //////
            System.out.println("[ECOM] EcomBooingBean ######>" + hs);
            System.out.println("[ECOM] EcomBooingBean getTareweight hs " + hs + " equipType " + equipType);
            List lst = (List) hs.get("ecom_equipments");
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map rs = (Map) lst.get(j);
                    if (i == 0) {
                        result = (String) rs.get("tarekg");
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
/*
    synchronized private String bookNoGenerate(String module) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String bknum = null, prefixname = null;
        int bkgIndexNum = 0;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(BOOK_NO_GEN);
            ps.setString(1, module);
            rs = ps.executeQuery();
            while (rs.next()) {
                bkgIndexNum = rs.getInt("mdlindex");
                prefixname = rs.getString("mdlPrefix");
            }
            ps = con.prepareStatement(BOOK_NO_UPDATE);
            ps.setInt(1, bkgIndexNum + 1);
            ps.setString(2, module);
            ps.executeUpdate();
            bknum = prefixname + (bkgIndexNum + 1);
            System.out.println("[ECOM] EcomBooingBean  NEW bknum >>" + bknum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                System.out.println("[ECOM] EcomBooingBean exception while closing connection bookNoGenerate" + e);
                e.printStackTrace();
            }

        }
        return bknum;
    }
*/
private String bookNoGenerate(String module) {
    final String SQL =
        "UPDATE ecomMdlIndex WITH (ROWLOCK, UPDLOCK) " +
        "SET mdlIndex = mdlIndex + 1 " +
        "OUTPUT inserted.mdlPrefix, inserted.mdlIndex " +
        "WHERE mdlid = ?";

    try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection()) {
        con.setAutoCommit(false);

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(1, module);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    con.rollback();
                    throw new IllegalStateException("Module not found in ecomMdlIndex: " + module);
                }

                String prefix = rs.getString(1);   // inserted.mdlPrefix
                long next    = rs.getLong(2);      // inserted.mdlIndex (already incremented)
                String bknum = prefix + next;

                con.commit();
                System.out.println("[ECOM] EcomBookingBean NEW bknum >> " + bknum);
                return bknum;
            }
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        }
    } catch (Exception e) {
        e.printStackTrace();
        return ""; // or rethrow as unchecked, based on your convention
    }
}
    public int get4DigitRandomNumber() {
        int upperLimit = 10000;
        int s = getRandomNumber(upperLimit);
        return s;
    }

    public int getRandomNumber(int maxLimit) {
        return (int) ((Math.random() * maxLimit) * (Math.random() * maxLimit));
    }

    public String checkPointValue(String value) {
        String result = null;
        try {
            if (checkNull(value).length() > 2) {
                result = value.substring(value.length() - 2, value.length());
                if (result.equalsIgnoreCase(".0")) {
                    result = value.substring(0, value.length() - 2);
                } else {
                    result = value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String checkTemplateName(String templateName, String loginname) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "NotExist";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(checkTemplateqry);
            ps.setString(1, templateName);
            ps.setString(2, loginname);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = "Exist";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                System.out.println("[ECOM] EcomBooingBean exception while closing connection bookNoGenerate" + e);
                e.printStackTrace();
            }

        }
        return result;
    }

    @Override
    public String getBookingDeclinedRemarks(String ecombookno, String status) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "No Declined Message.";
        try {
            String BKG_REMARKS_QUERY = "select declined_remarks from ecombooking where ebook_status='V' and ebookno=?";
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(BKG_REMARKS_QUERY);
            ps.setString(1, ecombookno);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString("declined_remarks");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                System.out.println("[ECOM] EcomBooingBean exception while closing connection bookNoGenerate" + e);
                e.printStackTrace();
            }

        }
        return result;
    }

    private String getServiceName(String serviceCode) throws RemoteException {

        String serviceName = "";
        try {
            Map hs = (Map) ecomvdsservice.getservice(serviceCode);
            List lst = (List) hs.get("service");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                serviceName = checkNull((String) rs.get("servicetype"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceName;
    }

    @Override
    public ArrayList getTransportService() throws RemoteException {
        ArrayList result = new ArrayList();
        List<DropDownVO> main = new ArrayList<DropDownVO>();
        DropDownVO ddVO = null;
        HashMap map = new HashMap();
        try {
            ddVO = new DropDownVO();
            ddVO.setCode("");
            ddVO.setName("Select Carriage Contact");
            main.add(ddVO);
            //
            Map hs = (Map) ecomvdsservice.getecomtransportservice();
            //////
            List lst = (List) hs.get("transportservice");
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map rs = (Map) lst.get(j);
                    ddVO = new DropDownVO();
                    map.put(rs.get("trptsvccode"), rs.get("trptsvcname"));
                    ddVO.setCode((String) rs.get("trptsvccode"));
                    ddVO.setName((String) rs.get("trptsvcname"));
                    main.add(ddVO);
                }
            }
            result.add(main);
            result.add(map);
        } catch (Exception e) {
            System.out.println("[ECOM] EcomBooingBean Exception Caught On getServiceContractDetails==>" + e);
        }
        return result;
    }

    @Override
    public String deleteTemplateBooking(String Bookno) throws RemoteException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String result = "Deleted";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pstmt = con.prepareStatement(TEMPLATE_BOOKING);
            pstmt.setString(1, Bookno);
            pstmt.executeUpdate();
            //
            pstmt = con.prepareStatement(TEMPLATE_BOOKING_CUST);
            pstmt.setString(1, Bookno);
            pstmt.executeUpdate();
            //
            pstmt = con.prepareStatement(TEMPLATE_BOOKING_CARGO);
            pstmt.setString(1, Bookno);
            pstmt.executeUpdate();
            //
            pstmt = con.prepareStatement(TEMPLATE_BOOKING_COMM);
            pstmt.setString(1, Bookno);
            pstmt.executeUpdate();
            //
            pstmt = con.prepareStatement(TEMPLATE_BOOKING_DG);
            pstmt.setString(1, Bookno);
            pstmt.executeUpdate();
            //
            pstmt = con.prepareStatement(TEMPLATE_BOOKING_OOG);
            pstmt.setString(1, Bookno);
            pstmt.executeUpdate();
            //
            pstmt = con.prepareStatement(TEMPLATE_BOOKING_REFER);
            pstmt.setString(1, Bookno);
            pstmt.executeUpdate();
            //
            pstmt = con.prepareStatement(TEMPLATE_BOOKING_FILES);
            pstmt.setString(1, Bookno);
            pstmt.executeUpdate();
            //
            pstmt = con.prepareStatement(TEMPLATE_BOOKING_LEG);
            pstmt.setString(1, Bookno);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                System.out.println("[ECOM] EcomBooingBean exception while delete Temp booking" + e);
                e.printStackTrace();
            }

        }
        return result;
    }

    @Override
    public String getCountryCodeForPort(String port) throws RemoteException {
        String countryCode = null;
        try {
            Map hs = (Map) ecomvdsservice.getecomports(port);
            //
            System.out.println("[ECOM] EcomBooingBean ######>" + hs);
            List lst = (List) hs.get("ports");
            if (lst.size() > 0) {
                Map bkg = (Map) lst.get(0);
                countryCode = (String) bkg.get("countrycode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countryCode;
    }

    @Override
    public String getPODIntransit(String port) throws RemoteException {
        String result = null;
        int i = 0;
        try {
            Map hs = (Map) ecomvdsservice.getecompodintransit(port);
            //////
            List lst = (List) hs.get("dmsintransit");
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map rs = (Map) lst.get(j);
                    if (i == 0) {
                        result = checkNull((String) rs.get("dmsfinaldest")).trim() + "##"
                                + checkNull((String) rs.get("dmsfinaldestdesc")).trim();
                    } else {
                        result = result + "~~" + checkNull((String) rs.get("dmsfinaldest")).trim() + "##"
                                + checkNull((String) rs.get("dmsfinaldestdesc")).trim();
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private MailFormatVO getMailContent(String module, String criteria, String usertype) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MailFormatVO mailFormatObj = new MailFormatVO();
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_MAIL_CONTENT, 1004, 1007);
            ps.setString(1, criteria);
            ps.setString(2, module);
            ps.setString(3, usertype);
            rs = ps.executeQuery();
            while (rs.next()) {
                mailFormatObj.setStrMailContent(checkNull(rs.getString("message")));
                mailFormatObj.setStrFromAddr(checkNull(rs.getString("from_mailid")));
                mailFormatObj.setStrSubject(checkNull(rs.getString("subject")));
                mailFormatObj.setActivityCode(checkNull(rs.getString("activitycode")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return mailFormatObj;
    }

    private String getForwardMessage(String module, String criteria, String usertype) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(FORWARD_MESSAGE_QUERY, 1004, 1007);
            ps.setString(1, criteria);
            ps.setString(2, module);
            ps.setString(3, usertype);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = checkNull(rs.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return str;
        }
    }

    private String getForwardMessageLang(String module, String criteria, String usertype, String lang)
            throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(FORWARD_MESSAGE_LANG_QUERY, 1004, 1007);
            ps.setString(1, criteria);
            ps.setString(2, module);
            ps.setString(3, usertype);
            ps.setString(4, lang);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = checkNull(rs.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            return str;
        }
    }

    @Override
    public String getseaportfrominland(String port) throws RemoteException {

        String result = "";
        try {
            Map hs = (Map) ecomvdsservice.getecomports(port);
            //////
            List lst = (List) hs.get("ports");
            if (lst.size() > 0) {
                Map bkg = (Map) lst.get(0);
                result = (String) bkg.get("defseaport");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public float getTareWtForAmendment(String containerno, String eqpType, String eqpDesc) throws RemoteException {
        float val = 0;
        try {
            // String GET_AMEND_BKG_TAREWT = "select e.equiptype,tarewt1 from
            // containerprofile c inner join equipmentgroup e on c.EquipmentType=e.isocode "
            // + "where e.status='A'and c.containerno=? and e.equiptype=?";
            //
            Map hs = (Map) ecomvdsservice.getecomtarewtforamendment(containerno, eqpDesc);
            //
            System.out.println("[ECOM] EcomBooingBean ######>" + hs);
            List lst = (List) hs.get("containerprofile");
            if (lst != null && lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                val = Float.parseFloat((String) rs.get("tarewt"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    @Override
    public void setBkgPrint(String bookNo, String sessionid) {
        try {
            System.out.println(
                    "[ECOM] EcomBooingBean Inside setBkgPrint BKG No ====>" + bookNo + "sessionid ====>" + sessionid);
            List<EcomBKGPrintVO> lst = new ArrayList();
            EcomBKGPrintVO br = new EcomBKGPrintVO();
            br.setConrelno(bookNo);
            br.setSessionid(sessionid);
            EcomBKGPrintCommonModel cm = new EcomBKGPrintCommonModel();
            lst.add(br);
            cm.setRptbatchcro(lst);
            ecomvdsservice.setbkgprint(cm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delBkgPrint(String bookNo, String sessionid) {
        try {
            System.out.println(
                    "[ECOM] EcomBooingBean Inside delBkgPrint BKG No ====>" + bookNo + " sessionid ====>" + sessionid);
            List<EcomBKGPrintVO> lst = new ArrayList();
            EcomBKGPrintVO br = new EcomBKGPrintVO();
            br.setConrelno(bookNo);
            br.setSessionid(sessionid);
            EcomBKGPrintCommonModel cm = new EcomBKGPrintCommonModel();
            lst.add(br);
            cm.setRptbatchcro(lst);
            ecomvdsservice.delbkgprint(cm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // booking jasper
    @Override
    public String printBookingJaserSaveData(String bookno, String agencycode, Hashtable countryHashObj)
            throws RemoteException {
        EcomBkgPrintBookingSaveVo vo = getInputValues(bookno, agencycode, countryHashObj);
        try {
            // String query = "select top 1 book_no from booking_conf (NOLOCK) where
            // book_no=?";
            System.out.println(new StringBuilder().append("printSaveData==>").append(vo.bookNo).toString());
            Map vmap = ecomvdsservice.getbkgconfdet(vo.bookNo);
            List vlist = (List) vmap.get("booking_conf");
            if ((vo.getLoadPortFreeDays() != null) && (vo.getLoadPortFreeDays().trim().length() == 0)) {
                vo.setLoadPortFreeDays(null);
            }
            int total = vlist.size();
            System.out
                    .println(new StringBuilder().append(" shippercustcode >> ").append(vo.shippercustcode).toString());
            System.out.println(new StringBuilder().append(" fwdcustcode >> ").append(vo.fwdcustcode).toString());
            String cdate = this.vds.getDate();
            if (total > 0) {
                // query = "Update booking_conf set shipperName=?, shipperAddress=?,
                // shipperReference=?, fwdName=?, fwdAddress=?, fwdReference=?, mode=?,
                // stuffpoint=?, emptyPickup=?, delivery=?, remarks=?, loadPortFreeDays=?,
                // releaseterminal=?, acceptanceterminal=?, chasispickup = ?, chasisdropoff = ?,
                // userup=?, dateup=getDate(), SHcustcode=?, FWcustcode=?, showbound=?,
                // OPSCROremarks=? where book_No=?";
                // List lst = new ArrayList();
                // CommonBKGJaserModel cm = new CommonBKGJaserModel();
                // vo.dateup = cdate;
                // lst.add(vo);
                // cm.setBooking_conf(lst);
                // Map statusmap = ecomvdsservice.putbkgconf(cm);
                // System.out.println(new StringBuilder().append("statusmap
                // ").append(statusmap).toString());
                // Double sta = (Double) statusmap.get("status");
                // if (sta.doubleValue() == 200.0D) {
                // return "duplicate";
                // }
                // return "Failure";
                return "duplicate"; // duplicate
            } else {
                // query = "Insert into booking_conf (book_No, shipperName, shipperAddress,
                // shipperReference, fwdName, fwdAddress, fwdReference, mode, stuffpoint,
                // emptyPickup, delivery, remarks, loadPortFreeDays, releaseterminal,
                // acceptanceterminal, chasispickup, chasisdropoff, usercr, datecr, userup,
                // dateup, SHcustcode, FWcustcode, showbound, OPSCROremarks) values (?, ?, ?, ?,
                // ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, getDate(), ?, getDate(), ?, ?, ?,
                // ?)";
                List lst = new ArrayList();
                CommonBKGJaserModel cm = new CommonBKGJaserModel();
                vo.datecr = cdate;
                vo.dateup = cdate;
                lst.add(vo);
                cm.setBooking_conf(lst);
                Map statusmap = ecomvdsservice.setbkgconf(cm);
                System.out.println(new StringBuilder().append("statusmap").append(statusmap).toString());
                Double sta = (Double) statusmap.get("status");
                if (sta.doubleValue() == 200.0D) {
                    return "Success";
                }
                return "Failure";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    private EcomBkgPrintBookingSaveVo getInputValues(String bookno, String agencycode, Hashtable countryHashObj) {
        EcomBkgPrintBookingSaveVo dgvo = new EcomBkgPrintBookingSaveVo();
        String custType = "";
        String agencyid = agencycode;
        StringBuilder address = new StringBuilder();
        String country = "";
        try {
            //
            dgvo = new EcomBkgPrintBookingSaveVo();
            Map hs = (Map) ecomvdsservice.getecombookingdetails(bookno);
            System.out.println("[ECOM] EcomBooingBean >>lrp booking details>>" + hs);
            //
            List lst = (List) hs.get("booking");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                dgvo.bookNo = checkNull((String) rs.get("book_no"));
                dgvo.optionalpod = checkNull((String) rs.get("dischargeportid"));
                dgvo.showbound = checkNull((String) rs.get("bound"));
                dgvo.remarks = checkNull((String) rs.get("remarks"));
                dgvo.delivery = checkNull((String) rs.get("finalportid")) + "-"
                        + checkNull((String) rs.get("finalportdesc")); // finalPortId //finalPortDesc
                dgvo.releaseterminal = checkNull((String) rs.get("polterminal"));
                dgvo.acceptanceterminal = "";
                dgvo.chasispickup = "";
                dgvo.chasisdropoff = "";
                dgvo.emptyPickUp = "";
                dgvo.mode = "";
            }
            hs = (Map) ecomvdsservice.getecombookingcust(bookno);
            List custlst = (List) hs.get("booking_cust");
            if (custlst.size() > 0) {
                for (int j = 0; j < custlst.size(); j++) {
                    Map rs = (Map) custlst.get(j);
                    custType = checkNull((String) rs.get("custtypeid"));
                    if (custType.equalsIgnoreCase("SH")) {
                        dgvo.shipperName = checkNull(checkNull((String) rs.get("name")));
                        dgvo.shippercustcode = checkNull(checkNull((String) rs.get("cid"))); // need to check
                        dgvo.shipperRef = checkNull(checkNull((String) rs.get("cid"))); // need to check
                        if (rs.get("address1") != null) {
                            address.append(checkNull((String) rs.get("address1")));
                            address.append(" ");
                        }
                        if (rs.get("address2") != null) {
                            address.append(checkNull((String) rs.get("address2")));
                            address.append(" ");
                            address.append("\n");
                        }
                        if (rs.get("pobox") != null) {
                            address.append(checkNull((String) rs.get("pobox")));
                            address.append("\n");
                        }
                        if (rs.get("city") != null) {
                            address.append(checkNull((String) rs.get("city")));
                            address.append("\n");
                        }
                        if (rs.get("countryid") != null) {
                            country = (String) countryHashObj.get(rs.get("countryid"));
                            address.append(checkNull(country));
                            address.append("\n");
                        }
                        if (rs.get("zipcode") != null) {
                            address.append("zip:");
                            address.append(checkNull((String) rs.get("zipcode")));
                        }
                        if (rs.get("telephone") != null) {
                            address.append("Phone:");
                            address.append(checkNull((String) rs.get("telephone")));
                            address.append("\n");
                        }
                        if (rs.get("fax") != null) {
                            address.append("Fax:");
                            address.append(checkNull((String) rs.get("fax")));
                            address.append("\n");
                        }
                        if (rs.get("email") != null) {
                            address.append("Email:");
                            address.append(checkNull((String) rs.get("email")));
                            address.append("\n");
                        }
                        // dgvo.shipperAdd = checkNull((String) rs.get("address1"));
                        dgvo.shipperAdd = checkNull(address.toString());
                    } else if (custType.equalsIgnoreCase("FW")) {
                        dgvo.fwdName = checkNull(checkNull((String) rs.get("name")));
                        dgvo.fwdcustcode = checkNull(checkNull((String) rs.get("cid"))); // need to check
                        dgvo.fwdRef = checkNull(checkNull((String) rs.get("cid"))); // need to check
                        // dgvo.fwdAdd = checkNull((String) rs.get("address1"));
                        if (rs.get("address1") != null) {
                            address.append(checkNull((String) rs.get("address1")));
                            address.append("\n");
                        }
                        if (rs.get("address2") != null) {
                            address.append(checkNull((String) rs.get("address2")));
                            address.append("\n");
                        }
                        if (rs.get("pobox") != null) {
                            address.append(checkNull((String) rs.get("pobox")));
                            address.append("\n");
                        }
                        if (rs.get("city") != null) {
                            address.append(checkNull((String) rs.get("city")));
                            address.append("\n");
                        }
                        if (rs.get("countryid") != null) {
                            country = (String) countryHashObj.get(rs.get("countryid"));
                            address.append(checkNull(country));
                            address.append("\n");
                        }
                        if (rs.get("zipcode") != null) {
                            address.append("zip:");
                            address.append(checkNull((String) rs.get("zipcode")));
                        }
                        if (rs.get("telephone") != null) {
                            address.append("Phone:");
                            address.append(checkNull((String) rs.get("telephone")));
                            address.append("\n");
                        }
                        if (rs.get("fax") != null) {
                            address.append("Fax:");
                            address.append(checkNull((String) rs.get("fax")));
                            address.append("\n");
                        }
                        if (rs.get("email") != null) {
                            address.append("Email:");
                            address.append(checkNull((String) rs.get("email")));
                            address.append("\n");
                        }
                        dgvo.shipperAdd = checkNull(address.toString());
                    }
                }
            }
            dgvo.stuffpoint = "";
            dgvo.loadPortFreeDays = "";
            dgvo.OperationCroRemarks = "";
            dgvo.usercr = "";
            dgvo.userup = "";
            // Sgsin
            dgvo.sg_to = "";
            dgvo.sg_from = "";
            dgvo.sg_attn = "";
            dgvo.sg_email = "";
            dgvo.sg_tel = "";
            dgvo.sg_fax = "";
            dgvo.sg_cc = "";
            dgvo.sg_mainvsl = "";
            dgvo.sg_scncode = "";
            dgvo.sg_agentcode = "";
            dgvo.sg_vesselid = "";
            dgvo.sg_tos = "";
            dgvo.sg_commodity = "";
            dgvo.sg_closingtime = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dgvo;
    }

    @Override
    public String getecomjasperpath(String agencycode, String locationcode) throws RemoteException {
        String path = "";
        try {
            Map hs = (Map) ecomvdsservice.getecomjasperreportpath(agencycode, locationcode);
            //
            System.out.println("[ECOM] EcomBooingBean ######>" + hs);
            List lst = (List) hs.get("rptserverinfo");
            if (lst != null && lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                path = (String) rs.get("reportpath");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @Override
    public String getecomcorpagency(String agencycode) throws RemoteException {
        String corporateAgency = "";
        List corpList = null;
        try {
            Map corpAgency = ecomvdsservice.getEcomCorporateAgency(agencycode);
            System.out.println("[ECOM] EcomBooingBean >>corpAgency>>" + corpAgency);
            if (corpAgency != null && corpAgency.size() > 0) {
                corpList = (List) corpAgency.get("agencymatrix");
                if (corpList != null && corpList.size() > 0) {
                    corpAgency = (Map) corpList.get(0);
                    corporateAgency = checkNull((String) corpAgency.get("corpagency"));
                }
            }
            System.out.println("[ECOM] EcomBooingBean >>corporateAgency>book>" + corporateAgency);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return corporateAgency;
    }

    @Override
    public String getecompagencylocation(String agencycode) throws RemoteException {
        String countrycode = "";
        List LocList = null;
        try {
            Map AgencyLoc = ecomvdsservice.getEcomAgencyLoc(agencycode);
            System.out.println("[ECOM] EcomBooingBean >>AgencyLoc>>" + AgencyLoc);
            if (AgencyLoc != null && AgencyLoc.size() > 0) {
                LocList = (List) AgencyLoc.get("agency");
                if (LocList != null && LocList.size() > 0) {
                    AgencyLoc = (Map) LocList.get(0);
                    countrycode = checkNull((String) AgencyLoc.get("countrycode"));
                }
            }
            System.out.println("[ECOM] EcomBooingBean >>countrycode>agenct>" + countrycode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countrycode;
    }

    // private BkgPrintBookingSaveVo getInputValues(String bookno) {
    // BkgPrintBookingSaveVo dgvo = new BkgPrintBookingSaveVo();
    // String temp = this.zEmp;
    // try {
    // dgvo.bookNo = checkNull(this.mod.getTxtBookingNo(), this.zEmp);
    // System.out.println(new
    // StringBuilder().append("dgvo.bookNo===>").append(dgvo.bookNo).toString());
    // dgvo.shipperName = checkNull(this.mod.getTxtShipperName(), this.zEmp);
    // System.out.println(new
    // StringBuilder().append("dgvo.shipperName=temp===========>").append(dgvo.shipperName).toString());
    //
    // dgvo.shippercustcode = this.zEmp;
    // System.out.println(new
    // StringBuilder().append("dgvo.shippercustcode=temp===========>").append(dgvo.shippercustcode).toString());
    //
    // dgvo.fwdcustcode = this.zEmp;
    // System.out.println(new
    // StringBuilder().append("dgvo.fwdcustcode=temp===========>").append(dgvo.fwdcustcode).toString());
    // dgvo.shipperRef = checkNull(this.mod.getTxtSHRef(), this.zEmp);
    // dgvo.shipperAdd = checkNull(this.mod.getTxtShipperAddress(), this.zEmp);
    // dgvo.fwdName = checkNull(this.mod.getTxtForwarderName(), this.zEmp);
    // dgvo.fwdRef = checkNull(this.mod.getTxtFWRef(), this.zEmp);
    // dgvo.fwdAdd = checkNull(this.mod.getTxtForwrderAddress(), this.zEmp);
    // dgvo.emptyPickUp = checkNull(this.mod.getTxtEmptyPickup(), this.zEmp);
    // dgvo.releaseterminal = checkNull(this.mod.getTxtReleaseTerminal(),
    // this.zEmp);
    // dgvo.acceptanceterminal = checkNull(this.mod.getTxtAcceptanceTerminal(),
    // this.zEmp);
    // dgvo.chasispickup = checkNull(this.mod.getTxtChassisPickup(), this.zEmp);
    // dgvo.chasisdropoff = checkNull(this.mod.getTxtChassisDropOff(), this.zEmp);
    // dgvo.optionalpod = checkNull(this.book.getMaster().getOptionalPOD(),
    // this.zEmp);
    // dgvo.showbound = this.zN;
    // if (this.mod.isChkShowBound()) {
    // dgvo.showbound = this.zY;
    // }
    // if ((this.mod.getCmbMode() != null) &&
    // (!this.zEmp.equalsIgnoreCase(this.mod.getCmbMode()))) {
    // if (this.zTruck.equalsIgnoreCase(this.mod.getCmbMode())) {
    // temp = "T";
    // }
    // if ("Rail".equalsIgnoreCase(this.mod.getCmbMode())) {
    // temp = "R";
    // }
    // dgvo.mode = temp;
    // } else {
    // dgvo.mode = this.zEmp;
    // }
    // dgvo.stuffpoint = checkNull(this.mod.getTxtStuffPoint(), this.zEmp);
    // dgvo.delivery = checkNull(this.mod.getTxtpDelivery(), this.zEmp);
    // dgvo.remarks = checkNull(this.mod.getTxtUserRemarks(), this.zEmp);
    // dgvo.loadPortFreeDays = checkNull(this.mod.getTxtLoadPortFreedays(),
    // this.zEmp);
    // dgvo.OperationCroRemarks = checkNull(this.mod.getTxtOPSCRORemarks(),
    // this.zEmp);
    // dgvo.usercr = this.mData.getUserCode();
    // dgvo.userup = this.mData.getUserCode();
    // dgvo.sg_to = checkNull(this.mod.getTxtprintto(), this.zEmp);
    // dgvo.sg_from = checkNull(this.mod.getTxtprintfrom(), this.zEmp);
    // dgvo.sg_attn = checkNull(this.mod.getTxtprintattn(), this.zEmp);
    // dgvo.sg_email = checkNull(this.mod.getTxtEmailSGMY(), this.zEmp);
    // String strprintTelephone = new
    // StringBuilder().append(this.mod.getTxtprinttel1()).append("-").append(this.mod.getTxtprinttel2()).append("-").append(this.mod.getTxtprinttel()).toString();
    // dgvo.sg_tel = checkNull(strprintTelephone, this.zEmp);
    // String strprintFax = new
    // StringBuilder().append(this.mod.getTxtprintfax1()).append("-").append(this.mod.getTxtprintfax2()).append("-").append(this.mod.getTxtprintfax()).toString();
    // dgvo.sg_fax = checkNull(strprintFax, this.zEmp);
    // dgvo.sg_cc = checkNull(this.mod.getTxtprintcc(), this.zEmp);
    // dgvo.sg_mainvsl = checkNull(this.mod.getTxtprintmainvsl(), this.zEmp);
    // dgvo.sg_scncode = checkNull(this.mod.getTxtprintscncode(), this.zEmp);
    // dgvo.sg_agentcode = checkNull(this.mod.getTxtprintagentcode(), this.zEmp);
    // dgvo.sg_vesselid = checkNull(this.mod.getTxtprintvslid(), this.zEmp);
    // dgvo.sg_tos = checkNull(this.mod.getTxtprinttos(), this.zEmp);
    // dgvo.sg_commodity = checkNull(this.mod.getTxtPrintcommodity(), this.zEmp);
    //
    // if ((this.mod.getDpkclosingDate() != null) &&
    // (this.mod.getDpkclosingDate().toString().trim().length() > 0)) {
    // Date dt = this.mod.getDpkclosingDate();
    // String bkgdat = this.sdf2.format(dt);
    // System.out.println(new StringBuilder().append(" >>>>>>>>>>sg_closingtime Date
    // >>>>>>>>>>>").append(bkgdat).toString());
    //
    // dgvo.sg_closingtime = bkgdat;
    // } else {
    // dgvo.sg_closingtime = null;
    // }
    //
    // if ((type != null) && ("SGMY".equalsIgnoreCase(type.trim()))) {
    // dgvo.delivery = checkNull(this.mod.getTxtprintDelivery(), this.zEmp);
    // dgvo.emptyPickUp = checkNull(this.mod.getTxtprintEmptypickup(), this.zEmp);
    // } else {
    // dgvo.delivery = checkNull(this.mod.getTxtpDelivery(), this.zEmp);
    // dgvo.emptyPickUp = checkNull(this.mod.getTxtEmptyPickup(), this.zEmp);
    // }
    //
    // } catch (Exception e) {
    // this.log.fatal(e);
    // }
    // return dgvo;
    // }
    @Override
    public String cancelEcomBooking(String ecomBookNo, String lrpBookNo) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "Not Processed";
        Map resultMap = null;
        List list = null;
        String status = null, ecomStatus = null;
        ArrayList lst = null;
        EcomBKGEDIDocStatus doc = null;
        EcomCommonModel cm = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOM_BOOKING_STATUS);
            ps.setString(1, ecomBookNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                ecomStatus = rs.getString("ebook_status");
            }

            resultMap = ecomvdsservice.getEcomEDIDocstatus(ecomBookNo);
            if (resultMap != null) {
                list = (List) resultMap.get("edidocstatus");
                if (list != null && list.size() > 0) {
                    resultMap = (Map) list.get(0);
                    status = (String) resultMap.get("status");
                }
            }
            System.out.println("[ECOM] EcomBooingBean >>>edi status>>" + status + ">>ecomStatus>>" + ecomStatus);
            if ("R".equalsIgnoreCase(status) && "S".equalsIgnoreCase(ecomStatus)) {
                lst = new ArrayList();
                doc = new EcomBKGEDIDocStatus();
                cm = new EcomCommonModel();
                doc.setBook_no(ecomBookNo);
                lst.add(doc);
                cm.setEdidocstatus(lst);
                ecomvdsservice.putecomedidocstatus(cm);
                ps = con.prepareStatement(UPDATE_ECOM_CANCELBKG);
                if (lrpBookNo != null && lrpBookNo.length() > 0) {
                    ps.setString(1, "V");
                } else {
                    ps.setString(1, "I");
                }
                ps.setString(2, ecomBookNo);
                ps.executeUpdate();
            } else if ("P".equalsIgnoreCase(status)) {
                result = "Processed";
            } else {
                result = "Error";
            }
            System.out.println("[ECOM] EcomBooingBean result>>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public List<CommoditySearchVO> getComdtySearchList(String value, String columnName, String constraint,
            String groupcode) throws RemoteException {
        List<CommoditySearchVO> list = null;
        CommoditySearchVO vo = null;
        Map comMap = null;
        List comList = null;
        try {
            if ("contains".equalsIgnoreCase(constraint) && "comdtyname".equalsIgnoreCase(columnName)) {
                comMap = checkNull(groupcode).length() > 0 ? ecomGenService.getEcomComdtyForContains(value, groupcode)
                        : ecomGenService.getEcomComdtyForContains(value);
            } else if ("equalto".equalsIgnoreCase(constraint) && "comdtyname".equalsIgnoreCase(columnName)) {
                comMap = checkNull(groupcode).length() > 0 ? ecomGenService.getEcomComdtyForEqual(value, groupcode)
                        : ecomGenService.getEcomComdtyForEqual(value);
            } else if ("startwith".equalsIgnoreCase(constraint) && "comdtyname".equalsIgnoreCase(columnName)) {
                comMap = checkNull(groupcode).length() > 0 ? ecomGenService.getEcomComdtyForStartWith(value, groupcode)
                        : ecomGenService.getEcomComdtyForStartWith(value);
            } else if ("endwith".equalsIgnoreCase(constraint) && "comdtyname".equalsIgnoreCase(columnName)) {
                comMap = checkNull(groupcode).length() > 0 ? ecomGenService.getEcomComdtyForEndWith(value, groupcode)
                        : ecomGenService.getEcomComdtyForEndWith(value);
            } else if ("contains".equalsIgnoreCase(constraint) && "comdtycode".equalsIgnoreCase(columnName)) {
                comMap = checkNull(groupcode).length() > 0
                        ? ecomGenService.getEcomComdtyCodeForContains(value, groupcode)
                        : ecomGenService.getEcomComdtyCodeForContains(value);
            } else if ("equalto".equalsIgnoreCase(constraint) && "comdtycode".equalsIgnoreCase(columnName)) {
                comMap = checkNull(groupcode).length() > 0 ? ecomGenService.getEcomComdtyCodeForEqual(value, groupcode)
                        : ecomGenService.getEcomComdtyCodeForEqual(value);
            } else if ("startwith".equalsIgnoreCase(constraint) && "comdtycode".equalsIgnoreCase(columnName)) {
                comMap = checkNull(groupcode).length() > 0
                        ? ecomGenService.getEcomComdtyCodeForStartWith(value, groupcode)
                        : ecomGenService.getEcomComdtyCodeForStartWith(value);
            } else if ("endwith".equalsIgnoreCase(constraint) && "comdtycode".equalsIgnoreCase(columnName)) {
                comMap = checkNull(groupcode).length() > 0
                        ? ecomGenService.getEcomComdtyCodeForEndWith(value, groupcode)
                        : ecomGenService.getEcomComdtyCodeForEndWith(value);
            }
            if (comMap != null && comMap.size() > 0) {
                comList = (List) comMap.get("commodity");
                list = new ArrayList<>();
                if (comList != null && comList.size() > 0) {
                    for (Object obj : comList) {
                        comMap = (Map) obj;
                        vo = new CommoditySearchVO();
                        vo.setParentComdtyCode((String) comMap.get("grpcode"));
                        vo.setChileComdtyCode((String) comMap.get("comdtycode"));
                        vo.setChileComdtyName((String) comMap.get("comdtyname"));
                        list.add(vo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String validateCancelLRPBooking(String bookno, String validationtype, String disableConfirmBkgValidation)
            throws RemoteException {
        String result = "";
        Map resultMap = null;
        List list = null;
        boolean flag = true;
        try {
            System.out.println("[ECOM] EcomBooingBean  validateCancelLRPBooking validationtype>>>" + validationtype + ">>disableConfirmBkgValidation>>" + disableConfirmBkgValidation);
            if ((!"Y".equalsIgnoreCase(disableConfirmBkgValidation))
                    || ("draftbkgcancel".equalsIgnoreCase(validationtype))) {
                resultMap = ecomvdsservice.getCROStatusForBookingCancel(bookno);
                if (resultMap != null) {
                    list = (List) resultMap.get("plotdomaster");
                    if (list != null && list.size() > 0) {
                        flag = false;
                        result = "cro";
                    }
                }

                if (flag) {
                    resultMap = ecomvdsservice.getBLStatusForBookingCancel(bookno);
                    if (resultMap != null) {
                        list = (List) resultMap.get("bl");
                        if (list != null && list.size() > 0) {
                            flag = false;
                            result = "bl";
                        }
                    }
                }
                if (flag) {
                    resultMap = ecomvdsservice.getOperationStatusForBookingCancel(bookno);
                    if (resultMap != null) {
                        list = (List) resultMap.get("operationdetail");
                        if (list != null && list.size() > 0) {
                            flag = false;
                            result = "operation";
                        }
                    }
                }
                if (flag) {
                    SvmJsonList alist = new SvmJsonList();
                    alist.add(bookno);
                    resultMap = ecomvdsservice.getuspemsbookingcontmovement(alist);
                    Map ret2 = (Map) resultMap.get("rs");
                    if (ret2 != null) {
                        list = (List) ret2.get("rs0");
                        if (list != null && list.size() > 0) {
                            flag = false;
                            result = "ems";
                        }
                    }
                }
            }
            if (flag) {
                Connection ecomCon = null;
                PreparedStatement ecomPs = null;
                ResultSet ecomRs = null;
                try {
                    ecomCon = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                    ecomPs = ecomCon.prepareStatement(
                            "select esino from ecomSI(nolock) where status in ('D','S') and bookno=?");
                    ecomPs.setString(1, bookno);
                    ecomRs = ecomPs.executeQuery();
                    if (ecomRs.next()) {
                        flag = false;
                        result = "si";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (ecomRs != null)
                            ecomRs.close();
                    } catch (Exception e2) {
                    }
                    try {
                        if (ecomPs != null)
                            ecomPs.close();
                    } catch (Exception e2) {
                    }
                    try {
                        if (ecomCon != null)
                            ecomCon.close();
                    } catch (Exception e2) {
                    }
                }
            }
            if (flag) {
                result = "valid";
            }
            System.out.println("[ECOM] EcomBooingBean result>>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnVO cancelLRPBooking(String bookno, String canceluser, String reason, String ebookno)
            throws RemoteException {
        String result = "", agencyid = null, origin = null, delivery = null, custType = null, apmail = null,
                apname = null;
        Map resultMap = null;
        List list = null;
        ArrayList lst = null;
        Booking bkg = null;
        EcomCommonModel cm = null;
        String remarks = null;
        ReturnVO rvo = null;
        try {
            resultMap = ecomvdsservice.getLrpBookingRemarks(bookno);
            if (resultMap != null) {
                list = (List) resultMap.get("booking");
                if (list != null && list.size() > 0) {
                    resultMap = (Map) list.get(0);
                    remarks = checkNull((String) resultMap.get("remarks"));
                    agencyid = checkNull((String) resultMap.get("agencyid"));
                    origin = checkNull((String) resultMap.get("originportdesc"));
                    delivery = checkNull((String) resultMap.get("finalportdesc"));
                }
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            String dateString = format.format(new Date());
            remarks = new StringBuilder()
                    .append(remarks.trim().length() > 0 ? new StringBuilder().append(remarks).append("\n\n").toString()
                            : "")
                    .append(" Booking cancled by : ").append("Ecom").append(" :- ").append(dateString).append(": ")
                    .append(reason).toString();
            lst = new ArrayList();
            bkg = new Booking();
            cm = new EcomCommonModel();
            bkg.setRemarks_cancel(reason);
            bkg.setUserup("Ecom");
            bkg.setRemarks(remarks);
            bkg.setBook_no(bookno);
            lst.add(bkg);
            cm.setBooking(lst);
            resultMap = ecomvdsservice.putCancelEcomLRPBooking(cm);
            result = "Cancelled";
            System.out.println("[ECOM] EcomBooingBean result>>>" + result);
            String strAgency = getAgencyMail(agencyid);
            //
            Map hs = (Map) ecomvdsservice.getecombookingcust(bookno);
            List custlst = (List) hs.get("booking_cust");
            if (custlst.size() > 0) {
                for (int j = 0; j < custlst.size(); j++) {
                    Map rs = (Map) custlst.get(j);
                    custType = checkNull((String) rs.get("custtypeid"));
                    if (custType.equalsIgnoreCase("AP")) {
                        apname = checkNull((String) rs.get("name"));
                        apmail = checkNull((String) rs.get("email"));
                    }
                }
            }

            rvo = getBookingCancellationMail(bookno, strAgency, origin, delivery, reason, dateString, apname, apmail,
                    ebookno);
            rvo.setStrDisplayMsg(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rvo;
    }

    @Override
    public String getEcomCustMappingAgency(String custCode) throws RemoteException {
        String agency = "";
        try {
            Map hs = (Map) ecomvdsservice.getEcomCustMappingAgency(custCode);
            //////
            List lst = (List) hs.get("ecomagycustmapping");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                agency = (String) rs.get("agencycode");
            }
            System.out.println("[ECOM] EcomBooingBean  getAgencydetails=>" + agency);

        } catch (Exception e) {
            System.out.println("[ECOM] EcomBooingBean Exception while geting Agency code  " + e.getMessage());
            e.printStackTrace();
        }
        return agency;
    }

    @Override
    public List getFeederService() throws RemoteException {
        List<String> servicelis = null;
        try {
            Map hs = (Map) ecomvdsservice.getEcomFeederSchedules();
            //////
            List lst = (List) hs.get("service");
            if (lst.size() > 0) {
                servicelis = new ArrayList();
                for (int i = 0; i < lst.size(); i++) {
                    Map rs = (Map) lst.get(i);
                    servicelis.add(checkNull((String) rs.get("servicecode")));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servicelis;
    }

    @Override
    public String getBookingHaulageDocType(String originCode, String custCode) throws RemoteException {
        String docType = null;
        String agencycode = null;
        Map resultMap = null;
        List list = null;
        try {
            agencycode = getAgencydetails(checkNull(originCode));
            String custmapagency = checkNull(getEcomCustMappingAgency(custCode));
            if (custmapagency != null && custmapagency.length() > 0) {
                agencycode = custmapagency;
            }
            resultMap = ecomvdsservice.getBookingHaulageDocType(agencycode);
            if (resultMap != null) {
                list = (List) resultMap.get("bkgagencyconfig");
                if (list != null && list.size() > 0) {
                    resultMap = (Map) list.get(0);
                    docType = (String) resultMap.get("value");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkNull(docType);
    }

    @Override
    public List getBkgHaulCustDetails(String custcode) throws RemoteException {
        CustomerDetailsVO custvo = null;
        Map resultMap = null;
        List list = null;
        List custlis = null;
        try {

            resultMap = ecomvdsservice.getHaulageCustomerDetails(custcode);
            if (resultMap != null) {
                list = (List) resultMap.get("booking_haulage");
                if (list != null && list.size() > 0) {
                    custlis = new ArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        resultMap = (Map) list.get(i);
                        custvo = new CustomerDetailsVO();
                        custvo.setCustCode(checkNull((String) resultMap.get("custcode")));
                        custvo.setName(checkNull((String) resultMap.get("custname")));
                        custvo.setAddress(checkNull((String) resultMap.get("contactaddress")));
                        // custvo.setAddress1(checkNull((String) resultMap.get("address2")));
                        custvo.setEmail(checkNull((String) resultMap.get("contactemail")));
                        custvo.setPhoneNo(checkNull((String) resultMap.get("contactphone")));
                        custvo.setContactperson(checkNull((String) resultMap.get("contactperson")));
                        custvo.setState(checkNull((String) resultMap.get("statecode")));
                        custvo.setCity(checkNull((String) resultMap.get("city")));
                        custvo.setCountryCode(checkNull((String) resultMap.get("countryid")));
                        custlis.add(custvo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return custlis;
    }

    @Override
    public List getBkgHaulContactDetails(String custcode) throws RemoteException {
        CustomerDetailsVO custvo = null;
        Map resultMap = null;
        List list = null;
        List custlis = null;
        try {
            resultMap = ecomvdsservice.getHaulageContactDetails(custcode);
            if (resultMap != null) {
                list = (List) resultMap.get("customercontacts");
                if (list != null && list.size() > 0) {
                    custlis = new ArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        resultMap = (Map) list.get(i);
                        custvo = new CustomerDetailsVO();
                        custvo.setName(checkNull((String) resultMap.get("contactname")));
                        custvo.setPhoneNo(checkNull((String) resultMap.get("phone")));
                        custvo.setEmail(checkNull((String) resultMap.get("email")));
                        custlis.add(custvo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return custlis;
    }

    @Override
    public Map getBkgHaulTMSClientDetails(String loadport) throws RemoteException {
        Map resultMap = null;
        List list = null;
        Map result = null;
        try {
            resultMap = ecomvdsservice.getHaulageTMSClientDetails(loadport);
            list = (List) resultMap.get("twrsmaster");
            if (list != null && list.size() > 0) {
                result = new HashMap();
                for (int i = 0; i < list.size(); i++) {
                    resultMap = (Map) list.get(i);
                    result.put(checkNull((String) resultMap.get("code")), checkNull((String) resultMap.get("name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<BookingHaulagedetailsVo> getEcomBookingHaulageDetails(String ebookno) throws RemoteException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement ps = null;
        List<BookingHaulagedetailsVo> haullist = null;
        BookingHaulagedetailsVo haulvo = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOM_BKG_HAULAGE_DETAILS);
            ps.setString(1, ebookno);
            rs = ps.executeQuery();
            haullist = new ArrayList();
            while (rs.next()) {
                haulvo = new BookingHaulagedetailsVo();
                haulvo.setEbookno(checkNull(rs.getString("ebookno")));
                haulvo.setEqprowid(rs.getInt("eqprowid"));
                haulvo.setEqpid(checkNull(rs.getString("eqpId")));
                haulvo.setEqptype(checkNull(rs.getString("eqptypeid")));
                haulvo.setPickloccode(checkNull(rs.getString("loadLocation")));
                haulvo.setPicklocname(checkNull(rs.getString("loadlocationname")));
                haulvo.setContactperson(checkNull(rs.getString("contactPerson")));
                haulvo.setAddress(checkNull(rs.getString("contactAddress")));
                haulvo.setContactphone(checkNull(rs.getString("contactPhone")));
                haulvo.setContactemail(checkNull(rs.getString("contactEmail")));
                haulvo.setRefno(checkNull(rs.getString("referenceNumber")));
                haulvo.setDropdate(checkNull(rs.getString("dropDate")));
                haulvo.setRemarks(checkNull(rs.getString("remarks")));
                haulvo.setTwrsdoctype(checkNull(rs.getString("docType")));
                haulvo.setTwrsoverweight(checkNull(rs.getString("overWeight")));
                haulvo.setStopsequence(rs.getInt("stopSequence"));
                // haulvo.setTmsclientcode(checkNull(rs.getString("TMSClientCode")));
                haulvo.setDummyeqp(checkNull(rs.getString("eqpId")).replace(checkNull(rs.getString("eqptypeid")), ""));
                haullist.add(haulvo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return haullist;
    }

    private String addHours(String hrs, String date) {
        String ret = date;
        try {
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date a = sm.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(a);
            c.add(10, Integer.parseInt(hrs));
            return sm.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Adjusts legformdate if the previous leg was an ITT move.
     * If previous leg is ITT (ittPrevLegArrDate is not empty):
     * - Add layoverHrs to ittPrevLegArrDate
     * - Compare with legformdate and use the higher date
     * - Set layoverHrs to "0" since it's already applied
     * 
     * @param ittPrevLegArrDate Previous vessel arrival date from ITT leg (empty if not ITT)
     * @param legformdate Current leg form date
     * @param layoverHrs Layover hours to add
     * @return String array: [0] = adjusted legformdate, [1] = adjusted layoverHrs
     */
    private String[] adjustLegformdateForITTPreviousLeg(String ittPrevLegArrDate, String legformdate, String layoverHrs) {
        String[] result = new String[2];
        result[0] = legformdate;
        result[1] = layoverHrs;
        
        try {
            // Check if previous leg was ITT move (ittPrevLegArrDate will be non-empty)
            if (ittPrevLegArrDate != null && ittPrevLegArrDate.trim().length() > 0 
                && legformdate != null && legformdate.trim().length() > 0
                && layoverHrs != null && layoverHrs.trim().length() > 0) {
                
                // Add layover hours to ITT previous leg arrival date
                String ittDateWithLayover = addHours(layoverHrs, ittPrevLegArrDate);
                
                // Compare dates and use the higher one
                SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date ittDate = sm.parse(ittDateWithLayover);
                Date currentLegDate = sm.parse(legformdate);
                
                if (ittDate.after(currentLegDate)) {
                    // ITT date + layover is higher, use it as legformdate
                    result[0] = ittDateWithLayover;
                    logPrinter("[ITT LAYOVER ADJUST] Using ITT adjusted date. ittPrevLegArrDate:" + ittPrevLegArrDate 
                        + ", LayoverHrs:" + layoverHrs + ", ittDateWithLayover:" + ittDateWithLayover 
                        + ", originalLegformdate:" + legformdate + ", newLegformdate:" + result[0]);
                } else {
                    // Current legformdate is higher, keep it
                    logPrinter("[ITT LAYOVER ADJUST] Keeping original legformdate. ittPrevLegArrDate:" + ittPrevLegArrDate 
                        + ", LayoverHrs:" + layoverHrs + ", ittDateWithLayover:" + ittDateWithLayover 
                        + ", legformdate:" + legformdate);
                }
                // Set layover hours to 0 since it's been applied
                result[1] = "0";
            }
        } catch (Exception e) {
            logPrinter("[ITT LAYOVER ADJUST ERROR] " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }

    private String ConvertDecimalFormat(String value) {
        DecimalFormat dcc = new DecimalFormat("0.000");
        try {
            if (value != null && value.trim().length() > 0) {
                value = dcc.format(Double.parseDouble(value.trim()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public List getCutOffConfigDetails(String callid, String pol, String pod) throws RemoteException {
        List cutoff = null;
        try {
            String agcode = getAgencydetails(checkNull(pol));
            cutoff = getbkgschedulesETDCutoff(callid, pol, pod, agcode, "D", "E");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cutoff;
    }

    public List getbkgschedulesETDCutoff(String callid, String pol, String pod, String agcode, String type,
            String cuthrs) {
        List ret = new ArrayList();
        List acutetd = new ArrayList();
        List acuttype = new ArrayList();
        List acutoffhour = new ArrayList();
        List acutoffminute = new ArrayList();
        List acutoffday = new ArrayList();
        List acutoffweek = new ArrayList();

        boolean cutfalg = false;
        boolean bkgcutfalg = false;
        Date bkgcutdate = new Date();
        Map hm = new HashMap();
        String etd = "";
        String cuthour = "0";
        String cutmin = "0";
        String cargotype = "";
        String cutetd = "";
        String cutoffday = "";
        String cutoffhour = "0";
        String cuttype = "";
        String cutweek = "";
        int daydif, fromday, today = 0;
        System.out.println(
                "[ECOM] EcomBooingBean getbkgschedulesETDCutoff===>" + callid + "pol===>" + pol + "pod===>" + pod);
        try {
            Map vmap = ecomvdsservice.getecomgetoffterminal(callid);
            List vlist = (List) vmap.get("allschedules");
            for (int i = 0; i < vlist.size(); i++) {
                Map rs = (Map) vlist.get(i);
                etd = rs.get("depdate").toString().trim();
            }
            System.out.println("[ECOM] EcomBooingBean etd======>" + etd);
            if ((etd != null) && (etd.trim().length() > 0)) {
                SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sm1 = new SimpleDateFormat("EEEE");
                SimpleDateFormat sm2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c = Calendar.getInstance();
                Calendar tempc = Calendar.getInstance();
                Date a = sm.parse(etd);
                c.setTime(a);

                System.out.println("[ECOM] EcomBooingBean cuthrs====>" + cuthrs);
                vmap = ecomvdsservice.getecombkgcutoffdate(pol, pod, "A", agcode);
                vlist = (List) vmap.get("bookingcutoff");
                System.out.println("[ECOM] EcomBooingBean  getbkgcutoffdate Size==>" + vlist.size());
                for (int i = 0; i < vlist.size(); i++) {
                    int k = 0;
                    cutfalg = true;
                    Map rs = (Map) vlist.get(i);
                    if ((cuthrs != null) && ("E".equalsIgnoreCase(cuthrs))) {
                        cutetd = rs.get("etd") != null ? rs.get("etd").toString().trim() : "";
                        cutoffday = rs.get("cutoffday") != null ? rs.get("cutoffday").toString().trim() : "";
                        cutoffhour = rs.get("cutoffhour") != null ? rs.get("cutoffhour").toString().trim() : "0:0";
                        cuttype = rs.get("cutofftype") != null ? rs.get("cutofftype").toString().trim() : "";
                        cargotype = rs.get("cargotype") != null ? rs.get("cargotype").toString().trim() : "";
                        cutweek = rs.get("week") != null ? rs.get("week").toString().trim() : "";
                    } else if ((cuthrs != null) && ("P".equalsIgnoreCase(cuthrs))) {
                        cutetd = rs.get("etd") != null ? rs.get("etd").toString().trim() : "";
                        cutoffday = rs.get("cutoffday") != null ? rs.get("cutoffday").toString().trim() : "";
                        cutoffhour = rs.get("cutoffhourpst") != null ? rs.get("cutoffhourpst").toString().trim()
                                : "0:0";
                        cuttype = rs.get("cutofftype") != null ? rs.get("cutofftype").toString().trim() : "";
                        cargotype = rs.get("cargotype") != null ? rs.get("cargotype").toString().trim() : "";
                        cutweek = rs.get("week") != null ? rs.get("week").toString().trim() : "";
                    }
                    System.out.println("[ECOM] EcomBooingBean cuttype=>" + cuttype + "cargotype======>" + cargotype);
                    if ("Port Entry Cutoff".equalsIgnoreCase(cuttype.trim())) {
                        if ((type.trim().equalsIgnoreCase(cargotype)) || ("A".equalsIgnoreCase(cargotype))) {
                            cutfalg = true;
                        } else {
                            cutfalg = false;
                        }
                    } else if ("A".equalsIgnoreCase(cargotype)) {
                        cutfalg = true;
                    } else {
                        cutfalg = false;
                    }
                    System.out.println("[ECOM] EcomBooingBean cutfalg==>" + cutfalg);
                    if (cutfalg) {
                        if (cutoffhour.contains(":")) {
                            StringTokenizer val = new StringTokenizer(cutoffhour, ":");
                            cuthour = val.nextToken().trim();
                            cutmin = val.nextToken().trim();
                        } else {
                            cuthour = cutoffhour;
                            cutmin = "0";
                        }
                        acutetd.add(k, cutetd);
                        acutoffday.add(k, cutoffday);
                        acutoffhour.add(k, cuthour);
                        acutoffminute.add(k, cutmin);
                        acuttype.add(k, cuttype);
                        acutoffweek.add(k, cutweek);
                        k++;
                    }
                }
                System.out.println("[ECOM] EcomBooingBean Fcutetd: " + acutetd + ", Fcutoffday: " + acutoffday
                        + ", Fcutoffhour: " + acutoffhour
                        + ", Fcuthour: " + acutoffminute + ", Facuttype: " + acuttype + ", Facutoffweek: "
                        + acutoffweek);

                if ((acuttype != null) && (acuttype.size() > 0)) {
                    String val = "";
                    for (int i = 0; i < acuttype.size(); i++) {
                        val = acuttype.get(i).toString();
                        if ((val != null) && (val.contains("Booking"))) {
                            Collections.swap(acuttype, 0, i);
                            Collections.swap(acutetd, 0, i);
                            Collections.swap(acutoffday, 0, i);
                            Collections.swap(acutoffhour, 0, i);
                            Collections.swap(acutoffminute, 0, i);
                            Collections.swap(acutoffweek, 0, i);
                        }
                    }
                }
                System.out.println("[ECOM] EcomBooingBean C First: " + c.getTime() + ", cutetd: " + acutetd
                        + ", cutoffday: " + acutoffday + ", cutoffhour: " + acutoffhour
                        + ", cuthour: " + acutoffminute + ", acuttype: " + acuttype);

                int day = c.get(7);
                System.out.println("[ECOM] EcomBooingBean day ===>" + day);
                String etdday = sm1.format(a);
                System.out.println("[ECOM] EcomBooingBean etdday====>" + etdday);
                tempc = c;
                System.out.println("[ECOM] EcomBooingBean tempc First====>" + tempc.getTime());
                if ((acuttype != null) && (acuttype.size() > 0) && (etdday != null)) {
                    for (int i = 0; i < acuttype.size(); i++) {
                        System.out.println("[ECOM] EcomBooingBean Inside the date loop i: " + i + ", acuttype: "
                                + acuttype.get(i) + ", acutoffday: " + acutoffday.get(i)
                                + ", acutoffhour: " + acutoffhour.get(i) + ", acutoffweek: " + acutoffweek.get(i));
                        c = Calendar.getInstance();
                        a = sm.parse(etd);
                        c.setTime(a);

                        System.out.println("[ECOM] EcomBooingBean First C date ====>" + c.getTime());
                        if ((acutoffweek.get(i) != null)
                                && ("P".equalsIgnoreCase(acutoffweek.get(i).toString().trim()))) {
                            c.add(4, -1);
                        }
                        System.out.println("[ECOM] EcomBooingBean Inside C date ====>" + c.getTime());
                        System.out.println("[ECOM] EcomBooingBean EcomConsts.dayMap===> " + EcomConsts.dayMap);
                        if (EcomConsts.dayMap != null && EcomConsts.dayMap.get(etdday.toUpperCase()) != null
                                && EcomConsts.dayMap.get(acutoffday.get(i).toString().trim().toUpperCase()) != null) {
                            System.out.println("[ECOM] EcomBooingBean acutoffday.get(i)=> " + acutoffday.get(i)
                                    + " etdday " + etdday);
                            fromday = (int) EcomConsts.dayMap.get(etdday.trim().toUpperCase());
                            today = (int) EcomConsts.dayMap.get(acutoffday.get(i).toString().trim().toUpperCase());
                            daydif = today - fromday;
                            System.out.println("[ECOM] EcomBooingBean fromday => " + fromday + " today => " + today
                                    + " daydif => " + daydif);
                            daydif = daydif >= 0 ? daydif - 7 : daydif;
                            System.out.println("[ECOM] EcomBooingBean daydif====>>> " + daydif);
                            c.add(Calendar.DATE, daydif);
                        }
                        c.add(10, Integer.parseInt(acutoffhour.get(i).toString()));
                        c.add(12, Integer.parseInt(acutoffminute.get(i).toString()));

                        System.out.println("[ECOM] EcomBooingBean C====>" + c.getTime());
                        Date b = c.getTime();
                        System.out.println("[ECOM] EcomBooingBean B: " + sm1.format(b) + ", Date: " + b
                                + ", sm2.format(b): " + sm2.format(b));

                        hm.put(acuttype.get(i), sm2.format(b));
                    }
                }
                System.out.println("[ECOM] EcomBooingBean hm======>" + hm);
                ret.add(hm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public boolean checkIsEmpty(String value) {
        boolean result = false;
        try {
            if (value != null && value.trim().length() > 0) {
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM] EcomBooingBean result ==> " + result + " value => " + value);
        return result;
    }

    @Override
    public CutoffConfigVO getEcomBookingConfDetails(String bookno) throws RemoteException {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement ps = null;
        CutoffConfigVO cutoffvo = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOMBOOKNG_CONF);
            ps.setString(1, bookno);
            rs = ps.executeQuery();
            while (rs.next()) {
                cutoffvo = new CutoffConfigVO();
                cutoffvo.setBkgcutoff(
                        DateConversion_New.convertDefaultFormatwithTime(checkNull(rs.getString("bkgcutoff"))));
                cutoffvo.setDoccutoff(
                        DateConversion_New.convertDefaultFormatwithTime(checkNull(rs.getString("doccutoff"))));
                cutoffvo.setVgmcutoff(
                        DateConversion_New.convertDefaultFormatwithTime(checkNull(rs.getString("vgmcutoff"))));
                cutoffvo.setBbrorocutoff(
                        DateConversion_New.convertDefaultFormatwithTime(checkNull(rs.getString("bbrorocutoff"))));
                cutoffvo.setHazcutoff(
                        DateConversion_New.convertDefaultFormatwithTime(checkNull(rs.getString("hazcutoff"))));
                cutoffvo.setPortentrycutoff(
                        DateConversion_New.convertDefaultFormatwithTime(checkNull(rs.getString("portentrycutoff"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return cutoffvo;
    }

    @Override
    public String getbkgconfigDetailsForHaulage(String attrib) throws RemoteException {
        String value = "";
        Map hs = null;
        List lst = null;
        try {
            hs = (Map) ecomvdsservice.getEcomBkgConfigDetails(attrib);
            lst = (List) hs.get("bkgconfig");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                value = (String) rs.get("value");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    //
    private List<String> getPortListByAgency(String portCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> portList = new ArrayList<String>();
        String strQuery = "select b.portcode from agencylocations a "
                + "inner join agencylocations b on a.agencycode = b.agencycode "
                + "inner join ports p1 on p1.portcode = a.portcode "
                + "inner join ports p2 on p2.portcode = b.portcode "
                + "where a.portcode = ? "
                + "and p1.porttype = p2.porttype";
        // and b.portcode <> ?
        try {
            con = new lrp.serverutils.refs.ServerObject("VSSDB").getConnection();
            ps = con.prepareStatement(strQuery, 1004, 1007);
            ps.setString(1, portCode);
            // ps.setString(2, portCode);
            //
            rs = ps.executeQuery();
            // System.out.println("[ECOM] EcomBooingBean Date-->"+date);
            while (rs.next()) {
                portList.add(rs.getString("portCode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return portList;
    }

    //
    private List<String> getPortListByCountry(String portCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> portList = new ArrayList<String>();
        String strQuery = "select p2.portcode from ports p1 "
                + "inner join ports p2 on p1.countrycode = p2.countrycode "
                + "where p1.portcode = ? "
                + "and p2.status='A' and p1.porttype = p2.porttype";
        // and p2.portcode <> ?
        try {
            con = new lrp.serverutils.refs.ServerObject("VSSDB").getConnection();
            ps = con.prepareStatement(strQuery, 1004, 1007);
            ps.setString(1, portCode);
            // ps.setString(2, portCode);
            //
            rs = ps.executeQuery();
            // System.out.println("[ECOM] EcomBooingBean Date-->"+date);
            while (rs.next()) {
                portList.add(rs.getString("portCode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return portList;
    }
    //

    private String splitMasterCode(String value) {
        try {
            if (value != null && checkNull(value).length() > 0 && checkNull(value).contains("-")) {
                value = value.split("-")[0].trim();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    @Override
    public String getBKGJasperIdFromAgencycode(String agencycode) throws RemoteException {
        String jasperid = "";
        try {
            Map hs = (Map) ecomvdsservice.getecombkgjasperid(agencycode);
            //
            System.out.println("[ECOM] EcomBooingBean ######>" + hs);
            List lst = (List) hs.get("jasperfileconfig");
            if (lst != null && lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                jasperid = (String) rs.get("jasperid");
            }
            System.out.println("[ECOM] EcomBooingBean getBKGJasperIdFromAgencycode  => " + jasperid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jasperid;
    }

    public ReturnVO getBookingCancellationMail(String bookno, String agencymail, String origin, String delivery,
            String remarks, String dateString, String apname, String apmail, String ebookno) {
        ReturnVO rvo = null;
        MailFormatVO mailFormatObj = null;
        try {
            rvo = new ReturnVO();
            String subject = "";
            mailFormatObj = getMailContent("E-Booking", "bkcnl-cust", "C");
            String msg = mailFormatObj.getStrMailContent();
            msg = msg.replace("@book_no", bookno);
            msg = msg.replace("@origin", origin);
            msg = msg.replace("@delivery", delivery);
            msg = msg.replace("@remarks", remarks);
            msg = msg.replaceAll("@date", dateString);
            msg = msg.replaceAll("@apname", apname);
            msg = msg.replaceAll("@ebook_no", ebookno);
            subject = mailFormatObj.getStrSubject();
            subject = subject.replace("@bookref", bookno);

            rvo.setStrMailMsg(msg);

            rvo.setStrSubject(subject);

            rvo.setActivityCode(mailFormatObj.getActivityCode());
            rvo.setStrFromMailId(mailFormatObj.getStrFromAddr());
            System.out.println("[ECOM] EcomBooingBean ===>setStrToMailId<==" + apmail);
            rvo.setStrToMailId(apmail);
            //
            rvo.setAdmToMailId(agencymail);
            if (rvo.getAdmToMailId() != null && rvo.getAdmToMailId().length() > 0) {
                MailFormatVO mailFormatObj2 = new MailFormatVO();
                mailFormatObj2 = getMailContent("E-Booking", "bkcnl-agen", "C");
                String msg2 = mailFormatObj2.getStrMailContent();
                msg2 = msg2.replaceAll("@book_no", bookno);
                msg2 = msg2.replace("@origin", origin);
                msg2 = msg2.replace("@delivery", delivery);
                msg2 = msg2.replace("@remarks", remarks);
                msg2 = msg2.replaceAll("@client", mailFormatObj2.getStrRegard());
                msg2 = msg2.replaceAll("@date", dateString);
                msg2 = msg2.replaceAll("@apname", apname);
                msg2 = msg2.replaceAll("@ebook_no", ebookno);
                rvo.setAdmMailMsg(msg2);
                String subject1 = mailFormatObj2.getStrSubject();
                subject1 = subject1.replace("@bookref", bookno);
                rvo.setAdmSubject(subject1);
                //
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rvo;
    }

    private MailFormatVO getMailContentLang(String module, String criteria, String usertype, String lang)
            throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MailFormatVO mailFormatObj = new MailFormatVO();
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_MAIL_CONTENT_LANG, 1004, 1007);
            ps.setString(1, criteria);
            ps.setString(2, module);
            ps.setString(3, usertype);
            ps.setString(4, lang);
            rs = ps.executeQuery();
            while (rs.next()) {
                mailFormatObj.setStrMailContent(checkNull(rs.getString("message")));
                mailFormatObj.setStrFromAddr(checkNull(rs.getString("from_mailid")));
                mailFormatObj.setStrSubject(checkNull(rs.getString("subject")));
                mailFormatObj.setActivityCode(checkNull(rs.getString("activitycode")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return mailFormatObj;
    }

    private String getUserDateFormat(String username) {
        String result = "";
        try {
            Map hs = (Map) ecomvdsservice.getUserDetails(username);
            //
            System.out.println("[ECOM] EcomBooingBean ######> " + username + " --> " + hs);
            List lst = (List) hs.get("userdetails");
            if (lst != null && lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                result = (String) rs.get("dateformat");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ECOM] EcomBooingBean result-> " + result);
        if (result != null && result.trim().length() == 0) {
            result = "MM/dd/yyyy HH:mm";
        }
        return result;
    }

    public String getStateCode(String state, String countrycode, String book_no) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println("[ECOM] EcomBooingBean getStateCode  -- Book_no=" + book_no + "  State=> " + state
                    + "  countrycode==> " + countrycode);
            if (state != null && checkNull(state).length() > 0 && checkNull(state).contains("-")) {
                state = state.split("-")[0].trim();
            }
            if (state != null && checkNull(state).length() > 4) {
                con = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();
                String query = "select statecode from Statesmaster where statedesc = '" + state + "' and countrycode='"
                        + countrycode + "'";
                System.out.println("[ECOM] EcomBooingBean Query => " + query);
                ps = con.prepareStatement(query, 1004, 1007);
                rs = ps.executeQuery();
                if (rs.next()) {
                    state = checkNull(rs.getString("statecode"));
                } else {
                    state = "";
                }
            }
            System.out.println("[ECOM] EcomBooingBean result----> " + state);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return state;
    }

    @Override
    public void setInsurancedetails(List inslist, String readerid) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map inmap = null;
        String lrpbook_no = null, onlineBookingNumber = null, quoteNumber = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            System.out.println("[ECOM] EcomBooingBean inslist" + inslist + "inslist.size" + inslist.size());
            for (int i = 0; i < inslist.size(); i++) {
                //System.out.println("[ECOM] EcomBooingBean inside delete");
                inmap = (Map) inslist.get(i);
                ps = con.prepareStatement("delete from ecom_insurance where quoteno=?");
                // ps.setString(1, checkNull((String) inmap.get("onlineBookingNumber")));
                ps.setString(1, checkNull((String) inmap.get("quoteNumber")));
                ps.executeUpdate();
            }
            ps = con.prepareStatement(
                    "insert into ecom_insurance (book_no,lrpbook_no,eqpid,commodity,quoteno,policy_no,insurance_amount,partytype,lrpbl_no,suminsured,status,usercr,datecr,contactName,insuredParty,confirmBL_Status,email,primaryemail) values (?,?,?,?,?,?,?,?,?,?,'P',?,getdate(),?,?,'N',?,?)");
            for (int i = 0; i < inslist.size(); i++) {
                //System.out.println("[ECOM] EcomBooingBean inside insert");
                inmap = (Map) inslist.get(i);
                ps.setString(1, checkNull((String) inmap.get("onlineBookingNumber")));
                ps.setString(2, checkNull((String) inmap.get("lrpbook_no")));
                ps.setString(3, checkNull((String) inmap.get("eqpid")));
                ps.setString(4, checkNull((String) inmap.get("HScode")));
                ps.setString(5, checkNull((String) inmap.get("quoteNumber")));
                ps.setString(6, checkNull((String) inmap.get("policy_no")));
                ps.setString(7, checkNull((String) inmap.get("totalPremium")));
                ps.setString(8, checkNull((String) inmap.get("custtype")));
                ps.setString(9, checkNull((String) inmap.get("lrpbl_no")));
                ps.setString(10, checkNull((String) inmap.get("sumInsured")));
                ps.setString(11, checkNull((String) inmap.get("loginname")));
                ps.setString(12, checkNull((String) inmap.get("contactname")));
                ps.setString(13, checkNull((String) inmap.get("insuredparty")));
                ps.setString(14, checkNull((String) inmap.get("email")));
                ps.setString(15, checkNull((String) inmap.get("primaryemail")));
                ps.addBatch();
            }
            int[] refer_update = ps.executeBatch();
            System.out.println("[ECOM] EcomBooingBean setInsurancedetails==> " + refer_update.length);
            if (checkNull(readerid).length() > 0) {
                List lst = new ArrayList();
                EcomInsuranceVo insvo = null;
                EcomCommonModel cm = new EcomCommonModel();
                for (int i = 0; i < inslist.size(); i++) {
                    inmap = (Map) inslist.get(i);
                    insvo = new EcomInsuranceVo();
                    insvo.setBook_no(checkNull((String) inmap.get("onlineBookingNumber")));
                    insvo.setLrpbook_no(checkNull((String) inmap.get("lrpbook_no")));
                    insvo.setEqpid(checkNull((String) inmap.get("eqpid")));
                    insvo.setCommodity(checkNull((String) inmap.get("HScode")));
                    insvo.setQuoteno(checkNull((String) inmap.get("quoteNumber")));
                    insvo.setPolicy_no(checkNull((String) inmap.get("policy_no")));
                    insvo.setInsurance_amount(Float.parseFloat((String) inmap.get("totalPremium")));
                    insvo.setPartytype(checkNull((String) inmap.get("custtype")));
                    insvo.setLrpbl_no(checkNull((String) inmap.get("lrpbl_no")));
                    insvo.setEdireaderid(readerid);
                    insvo.setVersion("0");
                    lst.add(insvo);
                }
                cm.setEditempbooking_insurance(lst);
                Map hs = (Map) ecomvdsservice.setecominsurancedetails(cm);
                System.out.println("[ECOM] EcomBooingBean HS=======> " + hs);
                //
                lst = new ArrayList();
                EcomBKGReference rf = null;
                cm = new EcomCommonModel();
                for (int i = 0; i < inslist.size(); i++) {
                    inmap = (Map) inslist.get(i);
                    rf = new EcomBKGReference();
                    rf.setBook_no(checkNull((String) inmap.get("onlineBookingNumber")));
                    rf.setRefid("EIQ");
                    rf.setRefval(checkNull((String) inmap.get("quoteNumber")));
                    rf.setIsedited("N");
                    rf.setEdireaderid(readerid);
                    rf.setVersion("0");
                    lst.add(rf);
                }
                cm.setEditempbooking_reference(lst);
                hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                System.out.println("[ECOM] EcomBooingBean ===>>>" + hs);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private Map getInsuranceDetils(String bookno, String type) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map insmap = null;
        try {
            System.out.println("[ECOM] EcomBooingBean getInsuranceDetils==> ");
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            if ("L".equalsIgnoreCase(type)) {
                ps = con.prepareStatement(
                        "select commodity,quoteno,partytype,suminsured,email from ecom_insurance where lrpbook_no=?");
            } else {
                ps = con.prepareStatement(
                        "select commodity,quoteno,partytype,suminsured,email from ecom_insurance where book_no=?");
            }
            ps.setString(1, checkNull(bookno));
            rs = ps.executeQuery();
            insmap = new HashMap();
            String qouteno = null, commodity = "", sumins = "";
            while (rs.next()) {
                insmap.put("insuredemail", checkNull(rs.getString("email")));
                qouteno = rs.getString("quoteno");
                qouteno = qouteno + "~~" + rs.getString("partytype");
                commodity = rs.getString("commodity");
                sumins = rs.getString("suminsured");
                List inlis = new ArrayList(Arrays.asList(commodity.split("~~")));
                List suminslis = new ArrayList(Arrays.asList(sumins.split("~~")));
                for (int j = 0; j < inlis.size(); j++) {
                    insmap.put((String) inlis.get(j), qouteno + "~~" + ((String) suminslis.get(j)));
                }
            }
            System.out.println("[ECOM] EcomBooingBean insmap1===> " + insmap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return insmap;
        }
    }

    @Override
    public void removeInsuranceDetails(String quoteno, String book_no, List surveylis) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        SurveyList insur = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            System.out.println("[ECOM] EcomBooingBean quoteno" + quoteno);
            ps = con.prepareStatement("delete from ecom_insurance where quoteno=?");
            // ps.setString(1, checkNull((String) inmap.get("onlineBookingNumber")));
            ps.setString(1, checkNull(quoteno));
            ps.executeUpdate();

            ps = con.prepareStatement(
                    "Insert into ecom_insurance_survey (book_no,quoteNo,surveyoptioncode,surveyoptionname,version) values (?,?,?,?,?)");
            for (int i = 0; i < surveylis.size(); i++) {
                System.out.println("[ECOM] EcomBooingBean inside insert");
                insur = (SurveyList) surveylis.get(i);
                ps.setString(1, checkNull(book_no));
                ps.setString(2, checkNull(quoteno));
                ps.setString(3, checkNull(insur.getSurveyOptionCode()));
                ps.setString(4, checkNull(insur.getSurveyOptionValue()));
                ps.setInt(5, insur.getVersion());
                ps.addBatch();
            }
            int[] refer_update = ps.executeBatch();
            System.out.println("[ECOM] EcomBooingBean removeInsuranceSurveyDetails==> " + refer_update.length);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private String toRenameZipFile(String book_no, String edireaderid, String filepath, String filename) {
        String finalrs = "";
        String actualfile = "", renamefile = "";
        try {
            String arr[] = filename.split("~~~");
            for (int i = 0; i < arr.length; i++) {
                actualfile = arr[i];
                renamefile = actualfile.replace(book_no, book_no + "_" + edireaderid);
                renameFile(filepath + File.separator + actualfile, filepath + File.separator + renamefile);
                finalrs = finalrs + renamefile + "~~~";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (finalrs.length() > 3) {
            finalrs = finalrs.substring(0, finalrs.length() - 3);
        }
        return finalrs;
    }

    private void renameFile(String actualfile, String renamefile) {
        try {
            File file = new File(actualfile);
            File rename = new File(renamefile);
            System.out.println("[ECOM] EcomBooingBean actualfile: " + actualfile + " &renamefile: " + renamefile);
            FileUtils.moveFile(file, rename);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String validateConfirmBookingEdit(String bookno) throws RemoteException {
        String result = "";
        Map resultMap = null;
        List list = null;
        boolean flag = true;
        try {
            resultMap = ecomvdsservice.getCROStatusForBookingCancel(bookno);
            if (resultMap != null) {
                list = (List) resultMap.get("plotdomaster");
                if (list != null && list.size() > 0) {
                    flag = false;
                    result = "cro";
                }
            }
            if (flag) {
                resultMap = ecomvdsservice.getBLStatusForBookingCancel(bookno);
                if (resultMap != null) {
                    list = (List) resultMap.get("bl");
                    if (list != null && list.size() > 0) {
                        flag = false;
                        result = "bl";
                    }
                }
            }
            if (flag) {
                resultMap = ecomvdsservice.getOperationStatusForBookingCancel(bookno);
                if (resultMap != null) {
                    list = (List) resultMap.get("operationdetail");
                    if (list != null && list.size() > 0) {
                        flag = false;
                        result = "operation";
                    }
                }
            }

            if (flag) {
                result = "valid";
            }
            System.out.println("[ECOM] EcomBooingBean result>>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<BkgContainerHaulageDetails> getBookingHaulageDetails(String bookno) throws RemoteException {
        Map hs = null;
        List<BkgContainerHaulageDetails> bl_lst = null;
        // SvmJsonList alist = null;
        try {
            System.out.println("[ECOM] EcomBooingBean Inside getBookinghauala");
            Gson gson = new Gson();
            // alist = new SvmJsonList();
            // alist.add(bookno);
            hs = (Map) ecomvdsservice.getecombookinghaualagetracking(bookno);

            System.out.println("[ECOM] EcomBooingBean getecombookinghaulagetracking map size : ....." + hs);
            if (hs != null && hs.size() > 0) {
                bl_lst = gson.fromJson(gson.toJson((ArrayList) hs.get("booking_haulage")),
                        new TypeToken<List<BkgContainerHaulageDetails>>() {
                        }.getType());
            }

            System.out.println("[ECOM] EcomBooingBean getecombookinghaulagetracking list size : ....." + bl_lst);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bl_lst;
    }

    @Override
    public List<CargoProtectVo> getCargoProtectDetails(String type, String value) throws RemoteException {
        List<CargoProtectVo> lis = null;
        Map hs = null;
        try {
            System.out.println("[ECOM] EcomBooingBean type=> " + type + " &value=" + value);
            if ("id".equalsIgnoreCase(type)) {
                hs = (Map) ecomvdsservice.getEcomCargoProtectDetails(value);
            } else if ("shipmentdate".equalsIgnoreCase(type)) {
                hs = (Map) ecomvdsservice.getEcomCargoProtectDetails("A", value, value);
            }
            Gson gson = new Gson();
            lis = gson.fromJson(gson.toJson((ArrayList) hs.get("cargoprotectioncharges")),
                    new TypeToken<List<CargoProtectVo>>() {
                    }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lis;
    }

    public Map getBKGLegTransitDays(HashSet<String> legset) {
        HashMap leghs = null;
        String pol = null, pod = null, days = null;
        try {
            System.out.println("[ECOM] EcomBooingBean inside getBKGLegTransitDays=> " + legset);
            leghs = new HashMap();
            for (String ele : legset) {
                pol = ele.split("~~")[0];
                pod = ele.split("~~")[1];
                Map hs = (Map) ecomvdsservice.getEcomBKGLegTransitDays(pol, pod);
                List lst = (List) hs.get("bkgleg_transitdays");
                if (lst != null && lst.size() > 0) {
                    Map rs = (Map) lst.get(0);
                    days = (String) rs.get("days");
                }
                leghs.put(pol + "~~" + pod, days);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logPrinter(" leghs==> " + leghs);
        return leghs;
    }

    public int getTranstTime(String pol, String pod, Map transmap) {
        int transtime = 0;
        try {
            logPrinter(" getTranstTime=> " + pol + "," + pod);
            if (transmap != null) {
                String transtimestr = (String) transmap.get(pol + "~~" + pod);
                if (transtimestr != null && transtimestr.length() > 0) {
                    transtime = Integer.parseInt(transtimestr);
                }
            }
            logPrinter(" transtime=> " + transtime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transtime;
    }

    private List setTransLoadLegDetails(ResultSet rs, List<TransScheduleLegVo> loadlis, BoundConfigVo boundres, List<BoundConfigVo> bcList, boolean hasVesselVoyageInfo) {

        TransScheduleLegVo vo = null;
        BoundConfigVo boundConf = null;
        try {
            int existingCount = (loadlis != null ? loadlis.size() : 0);
            logPrinter("[setTransLoadLegDetails START] ExistingListSize:" + existingCount + ", hasVesselVoyageInfo:" + hasVesselVoyageInfo + ", bcListSize:" + (bcList != null ? bcList.size() : 0) + ", boundres.Lbound:" + (boundres != null ? boundres.getLbound() : "null") + ", boundres.Dbound:" + (boundres != null ? boundres.getDbound() : "null"));
            if (loadlis == null) {
                loadlis = new ArrayList<TransScheduleLegVo>();
            }

            String dischargebound = "N/A";
            List<TransScheduleLegVo> filteredSchedules = new ArrayList<>();

            while (rs.next()) {
                String scheduleService = checkNull(rs.getString("service"));
                String scheduleVessel = checkNull(rs.getString("vessel"));
                String scheduleVoyage = checkNull(rs.getString("voyage"));
                String scheduleBound = checkNull(rs.getString("bound"));

                // Apply filtering logic based on bcList
                boolean foundExactMatch = false;
                boolean hasVesselVoyageMatch = false;
                boolean hasDefaultBoundConfig = false;
                BoundConfigVo matchedBoundConfig = null;

                // If bcList is empty, add all schedules to filtered list
                if (bcList == null || bcList.isEmpty()) {
                    vo = new TransScheduleLegVo();
                    vo.setService(scheduleService);
                    vo.setVessel(scheduleVessel);
                    vo.setVoyage(scheduleVoyage);
                    vo.setBound(scheduleBound);
                    vo.setDischargeBound(scheduleBound);
                    vo.setDepatureDate(checkNull(rs.getString("depdate")));
                    vo.setArrivalDate(checkNull(rs.getString("arrdate")));
                    vo.setType(checkNull(rs.getString("type")));
                    vo.setGroupCallId(checkNull(rs.getString("groupcallid")));
                    vo.setTerminal(checkNull(rs.getString("terminal")));
                    vo.setBoundConfig(boundres);
                    if (!"N/A".equalsIgnoreCase(dischargebound)) {
                        vo.setDischargeBound(checkNull(dischargebound));
                    }
                    filteredSchedules.add(vo);
                    logPrinter("[setTransLoadLegDetails EMPTY_BC] Added: Vessel:" + scheduleVessel + ", Voyage:" + scheduleVoyage + ", Bound:" + scheduleBound + ", DepDate:" + vo.getDepatureDate() + ", Terminal:" + vo.getTerminal());
                    continue;
                }

                // First pass: Check if schedule vessel and voyage exists in any bound config
                for (BoundConfigVo boundConfig : bcList) {
                    if (boundConfig == null) {
                        continue;
                    }

                    // Check if bound config has vessel and voyage information
                    boolean hasVesselVoyage = (boundConfig.getVessel() != null && !boundConfig.getVessel().trim().isEmpty()
                            && boundConfig.getVoyage() != null && !boundConfig.getVoyage().trim().isEmpty());

                    if (hasVesselVoyage) {
                        if (boundConfig.getVessel().equalsIgnoreCase(scheduleVessel)
                                && boundConfig.getVoyage().equalsIgnoreCase(scheduleVoyage)) {
                            hasVesselVoyageMatch = true;
                            if (boundConfig.getLbound().equalsIgnoreCase(scheduleBound)) {
                                matchedBoundConfig = boundConfig;
                                foundExactMatch = true;
                                logPrinter("[setTransLoadLegDetails EXACT_MATCH] Vessel:" + scheduleVessel + ", Voyage:" + scheduleVoyage + ", Bound:" + scheduleBound + ", Lbound:" + boundConfig.getLbound() + ", Dbound:" + boundConfig.getDbound());
                                break;
                            }
                        }
                    }
                }

                // Only check default bound config if NO vessel/voyage match was found in bound configs
                if (!hasVesselVoyageMatch) {
                    logPrinter("[setTransLoadLegDetails NO_VV_MATCH] Checking default config for Vessel:" + scheduleVessel + ", Voyage:" + scheduleVoyage + ", Bound:" + scheduleBound);
                    for (BoundConfigVo boundConfig : bcList) {
                        if (boundConfig == null) {
                            continue;
                        }
                        boolean isDefaultConfig = (boundConfig.getVessel() == null || boundConfig.getVessel().trim().isEmpty())
                                && (boundConfig.getVoyage() == null || boundConfig.getVoyage().trim().isEmpty());
                        if (isDefaultConfig) {
                            hasDefaultBoundConfig = isDefaultConfig;
                        }
                        if (isDefaultConfig && boundConfig.getLbound().equalsIgnoreCase(scheduleBound)) {
                            matchedBoundConfig = boundConfig;
                            logPrinter("[setTransLoadLegDetails DEFAULT_MATCH] Vessel:" + scheduleVessel + ", Voyage:" + scheduleVoyage + ", Bound:" + scheduleBound + ", Lbound:" + boundConfig.getLbound() + ", Dbound:" + boundConfig.getDbound());
                            break;
                        }
                    }
                    if (!hasDefaultBoundConfig) {
                        logPrinter("[setTransLoadLegDetails NO_DEFAULT_CONFIG] Creating N/A config for Vessel:" + scheduleVessel + ", Voyage:" + scheduleVoyage);
                        matchedBoundConfig = new BoundConfigVo();
                        matchedBoundConfig.setDbound("N/A");
                    }
                } else if (!foundExactMatch) {
                    logPrinter("[setTransLoadLegDetails VV_MATCH_NO_BOUND] Vessel:" + scheduleVessel + ", Voyage:" + scheduleVoyage + ", Bound:" + scheduleBound + " - Skipping default config");
                }

                // Add schedule to filtered list if a match was found
                if (matchedBoundConfig != null) {
                    vo = new TransScheduleLegVo();
                    vo.setService(scheduleService);
                    vo.setVessel(scheduleVessel);
                    vo.setVoyage(scheduleVoyage);
                    vo.setBound(scheduleBound);
                    vo.setDischargeBound(scheduleBound);
                    vo.setDepatureDate(checkNull(rs.getString("depdate")));
                    vo.setArrivalDate(checkNull(rs.getString("arrdate")));
                    vo.setType(checkNull(rs.getString("type")));
                    vo.setGroupCallId(checkNull(rs.getString("groupcallid")));
                    vo.setTerminal(checkNull(rs.getString("terminal")));
                    vo.setBoundConfig(matchedBoundConfig);
                    dischargebound = matchedBoundConfig.getDbound();
                    if (!"N/A".equalsIgnoreCase(dischargebound)) {
                        vo.setDischargeBound(checkNull(dischargebound));
                    }
                    filteredSchedules.add(vo);
                    logPrinter("[setTransLoadLegDetails ADDED] Vessel:" + scheduleVessel + ", Voyage:" + scheduleVoyage + ", Bound:" + scheduleBound + ", DischBound:" + vo.getDischargeBound() + ", DepDate:" + vo.getDepatureDate() + ", Type:" + vo.getType() + ", Terminal:" + vo.getTerminal() + ", MatchType:" + (foundExactMatch ? "EXACT" : "DEFAULT"));
                } else {
                    logPrinter("[setTransLoadLegDetails NO_MATCH] Vessel:" + scheduleVessel + ", Voyage:" + scheduleVoyage + ", Bound:" + scheduleBound + " - Skipped");
                }
            }

            loadlis.addAll(filteredSchedules);
            logPrinter("[setTransLoadLegDetails END] FilteredCount:" + filteredSchedules.size() + ", TotalListSize:" + loadlis.size());

        } catch (Exception e) {
            logPrinter("[setTransLoadLegDetails ERROR] Exception:" + e.getMessage());
            e.printStackTrace();
        }
        return loadlis;
    }

    @Override
    public List getMaskPortList() throws RemoteException {
        List<String> lis = null;
        Map hs = null;
        List li = null;
        try {
            lis = new ArrayList();
            hs = (Map) ecomvdsservice.getEcomMaskPort();
            if (hs != null) {
                li = (List) hs.get("maskport");
                if (li != null) {

                    for (int i = 0; i < li.size(); i++) {
                        hs = (Map) li.get(i);
                        lis.add((String) hs.get("port"));
                    }
                }
            }
            System.out.println("[ECOM] EcomBooingBean getMaskPortList==> " + lis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lis;
    }

    public HashMap<String, String> checkEUPort(String pol, String pod) {
        HashMap<String, String> hsmap = null;
        Map map = null;
        List lis = null;
        try {
            System.out.println("[ECOM] EcomBooingBean inside checkEUPort" + pol + " pod: " + pod);
            String ports = pol + "," + pod;
            String isEuPol = null;
            String isEuPod = null;
            hsmap = new HashMap<String, String>();
            map = ecomvdsservice.getEUPort(ports);
            if (map != null) {
                lis = (List) map.get("ports");
                if (lis != null && lis.size() > 0) {
                    for (int i = 0; i < lis.size(); i++) {
                        map = (Map) lis.get(i);
                        if (pol.equalsIgnoreCase(checkNull((String) map.get("portcode")))) {
                            isEuPol = checkNull((String) map.get("eur"));
                            isEuPol = isEuPol.length() > 0 ? isEuPol : "N";
                            hsmap.put(pol, isEuPol);
                        }
                        if (pod.equalsIgnoreCase(checkNull((String) map.get("portcode")))) {
                            isEuPod = checkNull((String) map.get("eur"));
                            isEuPod = isEuPod.length() > 0 ? isEuPod : "N";
                            hsmap.put(pod, isEuPod);
                        }
                    }
                }

            }
            System.out.println("[ECOM] EcomBooingBean isEnsFiling==>" + hsmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hsmap;
    }

    public String isChkAPEnsFilingAgreement(String apcode) {
        String ensFiling = null;
        Map map = null;
        List lis = null;
        try {
            System.out.println("[ECOM] EcomBooingBean inside isChkAPEnsFilingAgreement" + apcode);
            map = ecomvdsservice.getApCustEnsfiling(apcode);
            if (map != null) {
                lis = (List) map.get("customermaster");
                if (lis != null && lis.size() > 0) {
                    for (int i = 0; i < lis.size(); i++) {
                        map = (Map) lis.get(i);
                        ensFiling = checkNull((String) map.get("fillingtype"));
                    }
                }
            }
            System.out.println("[ECOM] EcomBooingBean ensFiling" + ensFiling);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ensFiling;
    }

    public HashSet<String> getDisableDraftPrintAgency(String disableCountry) throws RemoteException {
        HashSet<String> disableAgencyId = new HashSet<String>();
        System.out.println("[ECOM] EcomBooingBean disableCountry disableCountry =>" + disableCountry);
        Map map = null;
        try {
            if (checkNull(disableCountry).length() > 0 && !"-".equalsIgnoreCase(disableCountry)) {
                if (disableCountry.contains("~~")) {
                    disableCountry = disableCountry.replace("~~", ",");
                }
                System.out
                        .println("[ECOM] EcomBooingBean getDisableDraftPrintAgency disableCountry=>" + disableCountry);
                Map agencyData = ecomvdsservice.getcountryagencyid(disableCountry);
                if (agencyData != null) {
                    List customerMasterList = (List) agencyData.get("agency");
                    if (customerMasterList != null && !customerMasterList.isEmpty()) {
                        for (int i = 0; i < customerMasterList.size(); i++) {
                            map = (Map) customerMasterList.get(i);
                            disableAgencyId.add(checkNull((String) map.get("agencycode")));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ECOM] EcomBooingBean getDisableDraftPrintAgency Exception " + e);
        }
        return disableAgencyId;
    }

    private boolean isValidContact(String contact) {
        return contact != null && !contact.trim().isEmpty()
                && !contact.equalsIgnoreCase("--")
                && !contact.equalsIgnoreCase("-");
    }

    // Common method to save customers (buyer or seller)
    private void saveCustDetails(Map custMap, String book_refno) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<EbookingValidatorFormBeanNew> buyer_CustList = null;
        ArrayList<EbookingValidatorFormBeanNew> seller_CustList = null;
        EbookingValidatorFormBeanNew eBookingcustData = null;
        try {
            // System.out.println("book_refno==>" + book_refno + custMap);
            if (custMap != null && !custMap.isEmpty()) {
                con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

                if (custMap != null && custMap.containsKey("BY")) {
                    buyer_CustList = (ArrayList<EbookingValidatorFormBeanNew>) custMap.get("BY");
                    if (buyer_CustList != null) {
                        int len = buyer_CustList.size();

                        ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                        for (int i = 0; i < len; i++) {
                            eBookingcustData = (EbookingValidatorFormBeanNew) buyer_CustList.get(i);
                            if (eBookingcustData != null) {
                                ps.setString(1, book_refno);
                                ps.setString(2, "BY");
                                ps.setString(3, eBookingcustData.getEbook_Custcode());
                                ps.setString(4, eBookingcustData.getEbook_Custcountry());
                                ps.setString(5, eBookingcustData.getEbook_Custname());
                                ps.setString(6, eBookingcustData.getEbook_Custaddress());
                                ps.setString(7, eBookingcustData.getEbook_Custcity_desc());
                                ps.setString(8, "");
                                ps.setString(9, eBookingcustData.getEbook_Custphone());
                                ps.setString(10, eBookingcustData.getEbook_Custfax());
                                ps.setString(11, eBookingcustData.getEbook_Custemail());
                                ps.setString(12, eBookingcustData.getEbook_Custaddress1());
                                ps.setString(13, null);
                                ps.setString(14, null);
                                ps.setString(15, splitMasterCode(eBookingcustData.getEbook_Custstate()));
                                ps.setString(16, eBookingcustData.getEbook_Custeori());
                                ps.setString(17, eBookingcustData.getEbook_Custpersontype());
                                ps.addBatch();
                            }

                        }
                        int[] Customer_update = ps.executeBatch();
                        System.out.println("[ECOM] EcomBookingBean saveCustDetails Buyer==>" + Customer_update);
                    }
                }
                // Seller
                if (custMap != null && custMap.containsKey("SL")) {
                    seller_CustList = (ArrayList<EbookingValidatorFormBeanNew>) custMap.get("SL");
                    if (seller_CustList != null) {
                        int len = seller_CustList.size();
                        System.out.println("save the Seller len==>" + len);

                        ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                        for (int i = 0; i < len; i++) {
                            eBookingcustData = (EbookingValidatorFormBeanNew) seller_CustList.get(i);
                            if (eBookingcustData != null) {
                                ps.setString(1, book_refno);
                                ps.setString(2, "SL");
                                ps.setString(3, eBookingcustData.getEbook_Custcode());
                                ps.setString(4, eBookingcustData.getEbook_Custcountry());
                                ps.setString(5, eBookingcustData.getEbook_Custname());
                                ps.setString(6, eBookingcustData.getEbook_Custaddress());
                                ps.setString(7, eBookingcustData.getEbook_Custcity_desc());
                                ps.setString(8, "");
                                ps.setString(9, eBookingcustData.getEbook_Custphone());
                                ps.setString(10, eBookingcustData.getEbook_Custfax());
                                ps.setString(11, eBookingcustData.getEbook_Custemail());
                                ps.setString(12, eBookingcustData.getEbook_Custaddress1());
                                ps.setString(13, null);
                                ps.setString(14, null);
                                ps.setString(15, splitMasterCode(eBookingcustData.getEbook_Custstate()));
                                ps.setString(16, eBookingcustData.getEbook_Custeori());
                                ps.setString(17, eBookingcustData.getEbook_Custpersontype());
                                ps.addBatch();
                            }
                            int[] Customer_update = ps.executeBatch();
                            System.out.println("[ECOM] EcomBookingBean saveCustDetails seller==>" + Customer_update);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Map getLRPBookingCutomerDetails(String bookNo) throws RemoteException {

        List<EbookingValidatorFormBeanNew> buyer_CustList = null;
        List<EbookingValidatorFormBeanNew> seller_CustList = null;
        HashMap<String, List<EbookingValidatorFormBeanNew>> finalmap = null;
        try {

            finalmap = new HashMap<>();
            buyer_CustList = new ArrayList<EbookingValidatorFormBeanNew>();
            seller_CustList = new ArrayList<EbookingValidatorFormBeanNew>();
            Map hs = (Map) ecomvdsservice.getecombookingcust(bookNo, "A", "A");
            List custlst = (List) hs.get("booking_cust");
            if (custlst.size() > 0) {
                for (int j = 0; j < custlst.size(); j++) {
                    Map rs = (Map) custlst.get(j);
                    String custType = checkNull((String) rs.get("custtypeid"));
                    if ("SL".equalsIgnoreCase(custType) || "BY".equalsIgnoreCase(custType)) {
                        EbookingValidatorFormBeanNew eBookingmaster = new EbookingValidatorFormBeanNew();

                        String city = checkNull((String) rs.get("city"));
                        String country = checkNull((String) rs.get("countryid"));
                        String name = checkNull((String) rs.get("name"));
                        String custId = checkNull((String) rs.get("custid"));
                        String address1 = checkNull((String) rs.get("address1"));
                        String address2 = checkNull((String) rs.get("address2"));
                        String phone = checkNull((String) rs.get("telephone"));
                        String email = checkNull((String) rs.get("email"));
                        String fax = checkNull((String) rs.get("fax"));
                        String cid = checkNull((String) rs.get("cid"));
                        String state = checkNull((String) rs.get("stateid"));
                        String eori = checkNull((String) rs.get("eori")); // Optional: if available
                        String persontype = checkNull((String) rs.get("persontype")); // Optional: if available

                        // Common setters
                        eBookingmaster.setEbook_Custcity(city);
                        eBookingmaster.setEbook_Custcity_desc("NA".equalsIgnoreCase(city) ? "No Cities Available" : city);
                        eBookingmaster.setEbook_Custcountry(country);
                        eBookingmaster.setEbook_Custname(name);
                        eBookingmaster.setEbook_Custcode(custId);
                        eBookingmaster.setEbook_Custaddress(address1);
                        eBookingmaster.setEbook_Custaddress1(address2);
                        eBookingmaster.setEbook_Custemail(email);
                        eBookingmaster.setEbook_Custstate(state);
                        eBookingmaster.setEbook_Custid(cid);
                        eBookingmaster.setEbook_Custeori(eori); // If applicable
                        eBookingmaster.setEbook_Custpersontype(persontype); // If applicable
                        eBookingmaster.setEbook_Custphone(isValidContact(phone) ? phone : "");
                        eBookingmaster.setEbook_Custfax(isValidContact(fax) ? fax : "");

                        // Add to respective list
                        if ("SL".equalsIgnoreCase(custType)) {
                            seller_CustList.add(eBookingmaster);
                        } else if ("BY".equalsIgnoreCase(custType)) {
                            buyer_CustList.add(eBookingmaster);
                        }
                    }
                }
            }

            // Final map setting
            if (!seller_CustList.isEmpty()) {
                finalmap.put("SL", seller_CustList);
            }
            if (!buyer_CustList.isEmpty()) {
                finalmap.put("BY", buyer_CustList);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Optionally use logging
        }

        return finalmap;
    }

    @Override
    public Map getEbookingViewCustDetails(String bookNo, String action) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList<EbookingValidatorFormBeanNew> buyer_CustList = null;
        ArrayList<EbookingValidatorFormBeanNew> seller_CustList = null;
        Map viewCustDetails = new HashMap<>();
        try {
            // System.out.println("[ECOM] getEbookingViewCustDetails >> bookNo: " + bookNo +
            // "//" + action);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            buyer_CustList = new ArrayList<EbookingValidatorFormBeanNew>();
            seller_CustList = new ArrayList<EbookingValidatorFormBeanNew>();
            if ("edit".equalsIgnoreCase(action)) {
                ps = con.prepareStatement(GET_BOOKING_CUSTEDIT);
            } else if ("template".equalsIgnoreCase(action)) {
                ps = con.prepareStatement(GET_BOOKING_CUSTTEMPLATE);
            } else {
                ps = con.prepareStatement(GET_BOOKING_CUSTVIEW);
            }

            ps.setString(1, checkNull(bookNo));
            if ("template".equalsIgnoreCase(action)) {
                ps.setString(2, checkNull(bookNo));
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                String custType = checkNull(rs.getString("custTypeId"));
                if ("SL".equalsIgnoreCase(custType) || "BY".equalsIgnoreCase(custType)) {
                    EbookingValidatorFormBeanNew eBookingmaster = new EbookingValidatorFormBeanNew();

                    String city = checkNull(rs.getString("city"));
                    String country = checkNull(rs.getString("countryId"));
                    String name = checkNull(rs.getString("name"));
                    String custId = checkNull(rs.getString("custId"));
                    String address1 = checkNull(rs.getString("address1"));
                    String address2 = checkNull(rs.getString("address2"));
                    String email = checkNull(rs.getString("email"));
                    String state = checkNull(rs.getString("stateid"));
                    String cid = checkNull(rs.getString("cid"));
                    String eori = checkNull(rs.getString("eori"));
                    String persontype = checkNull(rs.getString("persontype"));
                    String phone = checkNull(rs.getString("telephone"));
                    String fax = checkNull(rs.getString("fax"));

                    // Set common customer fields
                    eBookingmaster.setEbook_Custcity_desc(city);
                    if ("NA".equalsIgnoreCase(city)) {
                        eBookingmaster.setEbook_Custcity_desc("No Cities Available");
                    }
                    eBookingmaster.setEbook_Custcountry(country);
                    eBookingmaster.setEbook_Custname(name);
                    eBookingmaster.setEbook_Custcode(custId);
                    eBookingmaster.setEbook_Custaddress(address1);
                    eBookingmaster.setEbook_Custaddress1(address2);
                    eBookingmaster.setEbook_Custemail(email);
                    eBookingmaster.setEbook_Custstate(state);
                    eBookingmaster.setEbook_Custid(cid);
                    eBookingmaster.setEbook_Custeori(eori);
                    eBookingmaster.setEbook_Custpersontype(persontype);
                    eBookingmaster.setEbook_Custphone(isValidContact(phone) ? phone : "");
                    eBookingmaster.setEbook_Custfax(isValidContact(fax) ? fax : "");

                    // Add to respective list
                    if ("SL".equalsIgnoreCase(custType)) {
                        seller_CustList.add(eBookingmaster);
                    } else if ("BY".equalsIgnoreCase(custType)) {
                        buyer_CustList.add(eBookingmaster);
                    }
                }
            }

            if (!seller_CustList.isEmpty()) {
                viewCustDetails.put("SL", seller_CustList);
            }
            if (!buyer_CustList.isEmpty()) {
                viewCustDetails.put("BY", buyer_CustList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return viewCustDetails;

    }

    @Override
    public ENSDeclarantVo getENSTabDetails(String lrpBookNo) throws RemoteException {
        ENSDeclarantVo ensvo = null;
        try (
                Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BOOKING_ENSDEC)) {
            // System.out.println("[ECOM] getENSTabDetails >> lrpBookNo: " + lrpBookNo);
            ps.setString(1, lrpBookNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ensvo = new ENSDeclarantVo();
                    ensvo.setEuCustZone(checkNull(rs.getString("eucustom")));
                    ensvo.setBlTypeENS(checkNull(rs.getString("bill_type")));
                    ensvo.setEnsFillingType(checkNull(rs.getString("ens_type")));
                    ensvo.setPaymentMethod(checkNull(rs.getString("payment_mode")));
                    ensvo.setDeclarantName(checkNull(rs.getString("cust_name")));
                    ensvo.setDeclarantAddress(checkNull(rs.getString("address")));
                    ensvo.setDeclarantAddress1(checkNull(rs.getString("address1")));
                    ensvo.setDeclarantCity(checkNull(rs.getString("city")));
                    ensvo.setDeclarantCountry(checkNull(rs.getString("country")));
                    ensvo.setDeclarantState(checkNull(rs.getString("state")));
                    ensvo.setDeclarantTelephone(checkNull(rs.getString("telephone")));
                    ensvo.setDeclarantEORI(checkNull(rs.getString("enseori")));
                    ensvo.setDeclarantEmail(checkNull(rs.getString("email")));
                    ensvo.setDeclarantFillingType(checkNull(rs.getString("filing_type")));
                    ensvo.setDeclarantZipcode(checkNull(rs.getString("zipcode")));
                    System.out.println("[ECOM] getENSTabDetails >> ensvo: " + ensvo.getDeclarantZipcode());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[EcomAmendmentBookingBean] Exception in getENSTabDetails PortEndPoint- " + e);
        }
        return ensvo;

    }

    @Override
    public List getTOS() throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DropDownVO> main = new ArrayList<DropDownVO>();
        DropDownVO ddVO = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ReportDB").getConnection();
            ps = con.prepareStatement("select ShipTerms,termsDesc,'' from shipmentTerms(nolock) where status='A'", 1004,
                    1007);

            rs = ps.executeQuery();
            while (rs.next()) {
                ddVO = new DropDownVO();
                ddVO.setCode(rs.getString("ShipTerms"));
                ddVO.setName(rs.getString("termsDesc"));
                main.add(ddVO);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return main;
    }

    public AutoLegVo determineStrBoundThereLoad(List<BoundConfigVo> bcList) {
        String strBoundThereLoad = "N"; // Default value
        AutoLegVo autoLegVo = new AutoLegVo();
        boolean hasVesselVoyageInfo = false;
        autoLegVo.setBoundConfig(new BoundConfigVo());
        if (bcList != null && bcList.size() > 0) {

            // Check if any bound config has vessel and voyage information
            for (BoundConfigVo boundConfig : bcList) {
                if (boundConfig != null
                        && boundConfig.getVessel() != null && !boundConfig.getVessel().trim().isEmpty()
                        && boundConfig.getVoyage() != null && !boundConfig.getVoyage().trim().isEmpty()) {
                    hasVesselVoyageInfo = true;
                    logPrinter(" - Found vessel/voyage info: "
                            + boundConfig.getVessel() + "/" + boundConfig.getVoyage());
                    autoLegVo.setBoundConfig(boundConfig);
                    //  break;
                } else if (boundConfig != null) {
                    autoLegVo.setBoundConfig(boundConfig);
                    //  break;
                }
            }

            // If no vessel/voyage info found, set the first available bound config as
            // default
            /* if (!hasVesselVoyageInfo && bcList.size() > 0) {
                autoLegVo.setBoundConfig(bcList.get(0));
            } */
            // If bcList contains values but none have vessel and voyage info, set to 'Y'
            if (!hasVesselVoyageInfo) {
                strBoundThereLoad = "Y";
                logPrinter(" - bcList has values but no vessel/voyage info, setting strBoundThereLoad = 'Y'");
            } else {
                // If bcList contains values with vessel and voyage info, set to 'N'
                strBoundThereLoad = "N";
                logPrinter(" - bcList has vessel/voyage info, setting strBoundThereLoad = 'N'");
            }
        } else {
            // If bcList is null or empty (no matching records), set to 'N'
            strBoundThereLoad = "N";
            logPrinter(" - bcList is null or empty, setting strBoundThereLoad = 'N'");
        }

        autoLegVo.setStrBoundThereLoad(strBoundThereLoad);
        autoLegVo.setHasVesselVoyageInfo(hasVesselVoyageInfo);
        return autoLegVo;
    }

    public List<Allschedules> filterSchedulesByBoundConfig(List<Allschedules> scheduleList,
            List<BoundConfigVo> bcList) {
        List<Allschedules> filteredSchedules = new ArrayList<>();
        logPrinter("[filterSchedulesByBoundConfig START] InputSchedules:" + (scheduleList != null ? scheduleList.size() : 0) + ", BoundConfigCount:" + (bcList != null ? bcList.size() : 0));

        if (scheduleList == null || scheduleList.isEmpty() || bcList == null || bcList.isEmpty()) {
            logPrinter("[filterSchedulesByBoundConfig EMPTY] Returning original list");
            return scheduleList;
        }

        for (Allschedules schedule : scheduleList) {
            if (schedule == null) {
                continue;
            }

            boolean foundExactMatch = false;
            boolean hasVesselVoyageMatch = false;
            boolean hasDefaultBoundConfig = false;
            BoundConfigVo matchedBoundConfig = null;

            // First pass: Check if schedule vessel and voyage exists in any bound config
            for (BoundConfigVo boundConfig : bcList) {
                if (boundConfig == null) {
                    continue;
                }

                // Check if bound config has vessel and voyage information
                boolean hasVesselVoyage = (boundConfig.getVessel() != null && !boundConfig.getVessel().trim().isEmpty()
                        && boundConfig.getVoyage() != null && !boundConfig.getVoyage().trim().isEmpty());

                if (hasVesselVoyage) {
                    if (boundConfig.getVessel().equalsIgnoreCase(schedule.getVessel())
                            && boundConfig.getVoyage().equalsIgnoreCase(schedule.getVoyage())) {
                        hasVesselVoyageMatch = true;
                        if (boundConfig.getLbound().equalsIgnoreCase(schedule.getBound())) {
                            matchedBoundConfig = boundConfig;
                            foundExactMatch = true;
                            logPrinter("[filterSchedules EXACT_MATCH] Vessel:" + schedule.getVessel() + ", Voyage:" + schedule.getVoyage() + ", Bound:" + schedule.getBound() + ", Lbound:" + boundConfig.getLbound() + ", Dbound:" + boundConfig.getDbound());
                            break;
                        }
                    }
                }
            }

            // Only check default bound config if NO vessel/voyage match was found in bound configs
            if (!hasVesselVoyageMatch) {
                logPrinter("[filterSchedules NO_VV_MATCH] Checking default config for Vessel:" + schedule.getVessel() + ", Voyage:" + schedule.getVoyage() + ", Bound:" + schedule.getBound());
                for (BoundConfigVo boundConfig : bcList) {
                    if (boundConfig == null) {
                        continue;
                    }
                    boolean isDefaultConfig = (boundConfig.getVessel() == null || boundConfig.getVessel().trim().isEmpty())
                            && (boundConfig.getVoyage() == null || boundConfig.getVoyage().trim().isEmpty());
                    if (isDefaultConfig) {
                        hasDefaultBoundConfig = isDefaultConfig;
                    }
                    if (isDefaultConfig && boundConfig.getLbound().equalsIgnoreCase(schedule.getBound())) {
                        matchedBoundConfig = boundConfig;
                        logPrinter("[filterSchedules DEFAULT_MATCH] Vessel:" + schedule.getVessel() + ", Voyage:" + schedule.getVoyage() + ", Bound:" + schedule.getBound() + ", Lbound:" + boundConfig.getLbound() + ", Dbound:" + boundConfig.getDbound());
                        break;
                    }
                }
                if (!hasDefaultBoundConfig) {
                    logPrinter("[filterSchedules NO_DEFAULT_CONFIG] Creating N/A config for Vessel:" + schedule.getVessel() + ", Voyage:" + schedule.getVoyage());
                    matchedBoundConfig = new BoundConfigVo();
                    matchedBoundConfig.setDbound("N/A");
                }
            } else if (!foundExactMatch) {
                logPrinter("[filterSchedules VV_MATCH_NO_BOUND] Vessel:" + schedule.getVessel() + ", Voyage:" + schedule.getVoyage() + ", Bound:" + schedule.getBound() + " - Skipping default config");
            }

            // Add schedule to filtered list if a match was found
            if (matchedBoundConfig != null) {
                schedule.setBoundConfig(matchedBoundConfig);
                filteredSchedules.add(schedule);
                logPrinter("[filterSchedules ADDED] Vessel:" + schedule.getVessel() + ", Voyage:" + schedule.getVoyage() + ", Bound:" + schedule.getBound() + ", Service:" + schedule.getService() + ", MatchType:" + (foundExactMatch ? "EXACT" : "DEFAULT"));
            } else {
                logPrinter("[filterSchedules NO_MATCH] Vessel:" + schedule.getVessel() + ", Voyage:" + schedule.getVoyage() + ", Bound:" + schedule.getBound() + " - Skipped");
            }
        }

        logPrinter("[filterSchedulesByBoundConfig END] InputCount:" + scheduleList.size() + ", OutputCount:" + filteredSchedules.size());
        return filteredSchedules;
    }

    @Override
    public Map<String, Map<String, String>> getCargoProtectRestrictionCountryDG() throws RemoteException {
        String GET_CARGO_PROTECT_RESTRICTION_COUNTRY_DG
                = "SELECT type, shipmentType, code, dgonly FROM Ecom_CargoProtectRestriction (NOLOCK) WHERE status = 'A'";

        Map<String, Map<String, String>> countryMap = new HashMap<>();
        Map<String, String> importMap = new HashMap<>();
        Map<String, String> exportMap = new HashMap<>();

        try (
                Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                PreparedStatement ps = con.prepareStatement(GET_CARGO_PROTECT_RESTRICTION_COUNTRY_DG);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String shipmentType = checkNull(rs.getString("shipmentType"));
                String code = checkNull(rs.getString("code"));
                String dgonly = checkNull(rs.getString("dgonly"));

                if ("I".equalsIgnoreCase(shipmentType)) {
                    importMap.put(code, dgonly);
                } else if ("E".equalsIgnoreCase(shipmentType)) {
                    exportMap.put(code, dgonly);
                }
            }

        } catch (Exception e) {
            System.out.println("[ECOMEJB] EcomBookingBean getCargoProtectRestrictionCountryDG Exception: " + e);
            e.printStackTrace();
        }

        countryMap.put("import", importMap);
        countryMap.put("export", exportMap);
        return countryMap;

    }
    private void logPrinter(String message) {
    // System.out.println("logPrinter Enable_Sout_Log: " + GeneralConf.Enable_Sout_Log);
        if(GeneralConf.Enable_Sout_Log) {
            System.out.println("[ECOM] EcomBookingBean logPrinter - " + message);
        }
     }

    // ============================================================================
    // TRANS_LAYOVER_PORT_BASED Enhancement - Port-based Transhipment Layover Time
    // ============================================================================

   
    /**
     * Calculate the appropriate layover hours based on terminal comparison.
     * Terminal comparison is done ONLY AFTER the full transhipment leg is formed.
     * 
     * @param layoverConfig LayoverTimeVo from layover_time table (may be null)
     * @param prevDischTerminal Previous leg discharge terminal
     * @param nextLoadTerminal Next leg load terminal
     * @param fallbackRTH Fallback RTH value from global config
     * @return Layover hours as String
     */
    private String calculateTransLayoverHours(LayoverTimeVo layoverConfig, String prevDischTerminal, 
                                               String nextLoadTerminal, String fallbackRTH) {
        try {
            logPrinter("[calculateTransLayoverHours START] PrevDischTerminal:" + prevDischTerminal + ", NextLoadTerminal:" + nextLoadTerminal + ", FallbackRTH:" + fallbackRTH + ", ConfigPresent:" + (layoverConfig != null));
            
            // Rule 3 fallback: If no layover config, use RTH
            if (layoverConfig == null) {
                logPrinter("[calculateTransLayoverHours NO_CONFIG] Using RTH fallback:" + fallbackRTH);
                return fallbackRTH;
            }
            
            // Validate layover config values
            if (!layoverConfig.isValid()) {
                logPrinter("[calculateTransLayoverHours INVALID_CONFIG] Portcode:" + layoverConfig.getPortcode() + ", ServiceCode:" + layoverConfig.getServiceCode() + ", Terminal:" + layoverConfig.getTerminal() + " - Using RTH:" + fallbackRTH);
                return fallbackRTH;
            }
            
            logPrinter("[calculateTransLayoverHours CONFIG] Portcode:" + layoverConfig.getPortcode() + ", ServiceCode:" + layoverConfig.getServiceCode() + ", Terminal:" + layoverConfig.getTerminal() + ", SameTerminalHrs:" + layoverConfig.getSameTerminalHrs() + ", DiffTerminalHrs:" + layoverConfig.getDiffTerminalHrs());
            
            // Safe null handling for terminal comparison
            String safePrevTerminal = (prevDischTerminal != null) ? prevDischTerminal.trim().toUpperCase() : "";
            String safeNextTerminal = (nextLoadTerminal != null) ? nextLoadTerminal.trim().toUpperCase() : "";
            
            int layoverHours;
            
            // Terminal comparison (case-insensitive, trimmed)
            if (!safePrevTerminal.isEmpty() && !safeNextTerminal.isEmpty() && 
                safePrevTerminal.equalsIgnoreCase(safeNextTerminal)) {
                // Same terminal - use same_terminal_hrs
                layoverHours = layoverConfig.getSameTerminalHrs();
                logPrinter("[calculateTransLayoverHours SAME_TERMINAL] Terminal:" + safePrevTerminal + ", LayoverHrs:" + layoverHours);
            } else {
                // Different terminal - use diff_terminal_hrs
                layoverHours = layoverConfig.getDiffTerminalHrs();
                logPrinter("[calculateTransLayoverHours DIFF_TERMINAL] PrevTerminal:" + safePrevTerminal + ", NextTerminal:" + safeNextTerminal + ", LayoverHrs:" + layoverHours);
            }
            
            // Final validation
            if (layoverHours < 0) {
                logPrinter("[calculateTransLayoverHours NEGATIVE_HRS] LayoverHrs:" + layoverHours + ", Portcode:" + layoverConfig.getPortcode() + " - Using RTH:" + fallbackRTH);
                return fallbackRTH;
            }
            
            logPrinter("[calculateTransLayoverHours END] FinalLayoverHrs:" + layoverHours);
            return String.valueOf(layoverHours);
            
        } catch (Exception e) {
            logPrinter("[calculateTransLayoverHours ERROR] Exception:" + e.getMessage() + " - Using RTH:" + fallbackRTH);
            e.printStackTrace();
            return fallbackRTH;
        }
    }

    /**
     * Get initial layover hours for query filtering (before terminal comparison).
     * Uses minimum of same/diff terminal hours to get more candidates, then filter with actual layover.
     * When FFH is provided, uses min(layoverConfigMin, FFH) since current leg type is unknown at query time.
     * 
     * @param layoverConfig LayoverTimeVo from layover_time table (may be null)
     * @param fallbackRTH Fallback RTH value from global config
     * @param ffhrs Feeder layover hours - used to widen the SQL filter for M-to-? transitions
     * @return Layover hours as String for use in SQL query
     */
    private String getInitialLayoverHoursForQuery(LayoverTimeVo layoverConfig, String fallbackRTH, String ffhrs) {
        try {
            if (layoverConfig == null || !layoverConfig.isValid()) {
                return fallbackRTH;
            }
            int configMin = Math.min(layoverConfig.getSameTerminalHrs(), layoverConfig.getDiffTerminalHrs());
            int ffh = Integer.parseInt(ffhrs);
            int hrs = Math.min(configMin, ffh);
            logPrinter("getInitialLayoverHoursForQuery: Using minimum layover hours: " + hrs + 
                       " (same=" + layoverConfig.getSameTerminalHrs() + 
                       ", diff=" + layoverConfig.getDiffTerminalHrs() + 
                       ", ffh=" + ffh + ")");
            return String.valueOf(hrs);
        } catch (Exception e) {
            System.out.println("[ECOM] EcomBookingBean getInitialLayoverHoursForQuery Exception: " + e);
            return fallbackRTH;
        }
    }

    /**
     * Load all layover configs for given ports upfront to reduce DB calls.
     * Uses DOCDB connection for layover_time table.
     * Stores all records as List in cache with portcode as key.
     * 
     * @param layoverConfigCache Cache map for layover configurations (key=portcode, value=List<LayoverTimeVo>)
     * @param portcodes List of port codes to load (TS1, TS2, TS3, POD)
     */
    private void loadLayoverConfigsForPorts(Map<String, List<LayoverTimeVo>> layoverConfigCache, 
                                             String... portcodes) {
        Connection docCon = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            // Build IN clause for ports
            StringBuilder inClause = new StringBuilder();
            List<String> validPorts = new ArrayList<String>();
            for (String port : portcodes) {
                String safePort = checkNull(port);
                if (!safePort.isEmpty()) {
                    if (inClause.length() > 0) inClause.append(",");
                    inClause.append("?");
                    validPorts.add(safePort);
                }
            }
            
            if (validPorts.isEmpty()) {
                logPrinter("loadLayoverConfigsForPorts: No valid ports to load");
                return;
            }
            
            // Use DOCDB connection for layover_time table
            docCon = new lrp.serverutils.refs.ServerObject("DOCDB").getConnection();
            
            // Load all layover_time records for these ports in ONE query
            String sql = "SELECT portcode, serviceCode, terminal, same_terminal_hrs, diff_terminal_hrs " +
                         "FROM layover_time (NOLOCK) WHERE status='A' and portcode IN (" + inClause + ")";
            
            pst = docCon.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            int idx = 1;
            for (String port : validPorts) {
                pst.setString(idx++, port);
            }
            
            rs = pst.executeQuery();
            
            // Initialize empty lists for all ports
            for (String port : validPorts) {
                if (!layoverConfigCache.containsKey(port)) {
                    layoverConfigCache.put(port, new ArrayList<LayoverTimeVo>());
                }
            }
            
            // Populate cache with results
            int count = 0;
            while (rs.next()) {
                LayoverTimeVo vo = new LayoverTimeVo();
                vo.setPortcode(rs.getString("portcode"));
                vo.setServiceCode(rs.getString("serviceCode"));
                vo.setTerminal(checkNull(rs.getString("terminal")));
                vo.setSameTerminalHrs(rs.getInt("same_terminal_hrs"));
                vo.setDiffTerminalHrs(rs.getInt("diff_terminal_hrs"));
                
                String portKey = vo.getPortcode();
                if (layoverConfigCache.containsKey(portKey)) {
                    layoverConfigCache.get(portKey).add(vo);
                }
                count++;
            }
            
            logPrinter("loadLayoverConfigsForPorts: Loaded " + count + " layover configs for ports: " + validPorts);
            
        } catch (Exception e) {
            System.out.println("[ECOM] EcomBookingBean loadLayoverConfigsForPorts Exception: " + e);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (docCon != null) docCon.close();
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    /**
     * Get layover config from pre-loaded cache, filtering by port, service, and terminal.
     * Applies Rule 1 (exact terminal match) then Rule 2 (empty terminal fallback).
     * 
     * @param layoverConfigCache Cache map with pre-loaded configs (key=portcode, value=List<LayoverTimeVo>)
     * @param portcode Previous leg discharge port
     * @param serviceCode Previous leg service
     * @param terminal Previous leg discharge terminal (optional)
     * @param fallbackRTH Fallback RTH value (used in log only, actual fallback handled by caller)
     * @return LayoverTimeVo or null if not found
     */
    private LayoverTimeVo getLayoverTimeConfigFromCache(Map<String, List<LayoverTimeVo>> layoverConfigCache,
                                                         String portcode, String serviceCode, 
                                                         String terminal, String fallbackRTH) {
        try {
            logPrinter("[getLayoverTimeConfig START] Portcode:" + portcode + ", Service:" + serviceCode + ", Terminal:" + terminal + ", FallbackRTH:" + fallbackRTH + ", CacheSize:" + (layoverConfigCache != null ? layoverConfigCache.size() : 0));
            String safePortcode = checkNull(portcode);
            String safeServiceCode = checkNull(serviceCode);
            String safeTerminal = checkNull(terminal);
            
            if (safePortcode.isEmpty() || safeServiceCode.isEmpty()) {
                logPrinter("[getLayoverTimeConfig INVALID_INPUT] Portcode:" + safePortcode + ", Service:" + safeServiceCode + " - Empty value");
                return null;
            }
            
            // Get all configs for this port
            List<LayoverTimeVo> portConfigs = layoverConfigCache.get(safePortcode);
            if (portConfigs == null || portConfigs.isEmpty()) {
                logPrinter("[getLayoverTimeConfig NO_PORT_CONFIG] Portcode:" + safePortcode + ", FallbackRTH:" + fallbackRTH);
                return null;
            }
            logPrinter("[getLayoverTimeConfig FOUND_PORT_CONFIGS] Portcode:" + safePortcode + ", ConfigCount:" + portConfigs.size());
            
            // Rule 1: Exact match on portcode, serviceCode, terminal
            if (!safeTerminal.isEmpty()) {
                for (LayoverTimeVo vo : portConfigs) {
                    if (safeServiceCode.equalsIgnoreCase(checkNull(vo.getServiceCode())) &&
                        safeTerminal.equalsIgnoreCase(checkNull(vo.getTerminal()))) {
                        logPrinter("[getLayoverTimeConfig RULE1_EXACT] Portcode:" + vo.getPortcode() + ", Service:" + vo.getServiceCode() + ", Terminal:" + vo.getTerminal() + ", SameTerminalHrs:" + vo.getSameTerminalHrs() + ", DiffTerminalHrs:" + vo.getDiffTerminalHrs());
                        return vo;
                    }
                }
            }
            
            // Rule 2: Fallback - match on portcode, serviceCode with terminal IS NULL or empty
            for (LayoverTimeVo vo : portConfigs) {
                if (safeServiceCode.equalsIgnoreCase(checkNull(vo.getServiceCode())) &&
                    checkNull(vo.getTerminal()).isEmpty()) {
                    logPrinter("[getLayoverTimeConfig RULE2_FALLBACK] Portcode:" + vo.getPortcode() + ", Service:" + vo.getServiceCode() + ", Terminal:(null/empty), SameTerminalHrs:" + vo.getSameTerminalHrs() + ", DiffTerminalHrs:" + vo.getDiffTerminalHrs());
                    return vo;
                }
            }
            
            // Rule 3: No match found - will use RTH fallback
            logPrinter("[getLayoverTimeConfig RULE3_NO_MATCH] Portcode:" + safePortcode + ", Service:" + safeServiceCode + ", Terminal:" + safeTerminal + ", FallbackRTH:" + fallbackRTH);
            return null;
            
        } catch (Exception e) {
            logPrinter("[getLayoverTimeConfig ERROR] Exception:" + e.getMessage());
            return null;
        }
    }

    /**
     * Validate if schedule meets layover requirement based on actual terminal comparison.
     * Validation: prevLegArrDate + actualLayoverHours <= scheduleDate
     * 
     * @param prevLegArrDate Previous leg arrival date (format: yyyy-MM-dd HH:mm:ss)
     * @param scheduleDepDate Schedule departure date to validate against
     * @param scheduleArrDate Schedule arrival date to validate against
     * @param blt BLT flag - if 'Y' use arrdate, if 'N' use depdate
     * @param actualLayoverHrs Actual layover hours after terminal comparison
     * @return true if schedule is valid, false otherwise
     */
    private boolean isScheduleLayoverValid(String prevLegArrDate, String scheduleDepDate, 
                                           String scheduleArrDate, String blt, String actualLayoverHrs) {
        try {
            if (prevLegArrDate == null || prevLegArrDate.trim().isEmpty()) {
                return true; // Can't validate, assume valid
            }
            
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date prevArrival = sm.parse(prevLegArrDate.trim());
            
            // Calculate minimum required date (prevArrival + layover hours)
            Calendar c = Calendar.getInstance();
            c.setTime(prevArrival);
            c.add(Calendar.HOUR, Integer.parseInt(actualLayoverHrs));
            Date minRequiredDate = c.getTime();
            
            // Determine which date to validate against based on blt
            String dateToValidate = "Y".equalsIgnoreCase(blt) ? scheduleArrDate : scheduleDepDate;
            if (dateToValidate == null || dateToValidate.trim().isEmpty()) {
                dateToValidate = scheduleDepDate; // Fallback to depdate
            }
            
            Date scheduleDate = sm.parse(dateToValidate.trim());
            
            // Schedule is valid if scheduleDate >= minRequiredDate
            boolean isValid = !scheduleDate.before(minRequiredDate);
            
            if (!isValid) {
                logPrinter("isScheduleLayoverValid: INVALID - prevArrival=" + prevLegArrDate + 
                           ", actualLayoverHrs=" + actualLayoverHrs + 
                           ", minRequired=" + sm.format(minRequiredDate) + 
                           ", scheduleDate=" + dateToValidate + ", blt=" + blt);
            }
            
            return isValid;
        } catch (Exception e) {
            System.out.println("[ECOM] EcomBookingBean isScheduleLayoverValid Exception: " + e);
            return true; // On error, assume valid to not break flow
        }
    }

    // ============================================================================
    // End of TRANS_LAYOVER_PORT_BASED Enhancement
    // ============================================================================

    public ReturnVO cancelConfirmedBooking(String lrpbookno, String ecombookno, String cancelReason, String cancelRemarks, String cancelUser, String userCode) {
        ReturnVO rvo = new ReturnVO();
        rvo.setStrRefNo("Error");
        Connection ecomCon = null;
        PreparedStatement ecomPs = null;
        String agencyid = "";
        String origin = "";
        String delivery = "";
        try {
            System.out.println("[ECOM] EcomBookingBean cancelConfirmedBooking lrpbookno: " + lrpbookno
                    + " ecombookno: " + ecombookno + " reason: " + cancelReason + " user: " + cancelUser);

            // Insert into ecombooking_cancel (EcomDB)
            ecomCon = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ecomPs = ecomCon.prepareStatement(INSERT_BOOKING_CANCEL);
            ecomPs.setString(1, checkNull(ecombookno));
            ecomPs.setString(2, checkNull(lrpbookno));
            ecomPs.setString(3, checkNull(cancelReason));
            ecomPs.setString(4, checkNull(cancelRemarks));
            ecomPs.setString(5, "S");
            ecomPs.setString(6, checkNull(cancelUser));
            ecomPs.executeUpdate();
            System.out.println("[ECOM] EcomBookingBean cancelConfirmedBooking ecombooking_cancel inserted for: " + ecombookno);

            // EDI Processing
            String readerid = bookNoGenerate("EDI");
            System.out.println("[ECOM] EcomBookingBean cancelConfirmedBooking readerid: " + readerid);

            if (readerid != null && readerid.length() > 0) {

                Map configMap = cntrlBeanObj.getMasterConfigDetails();
                ArrayList lst = null;
                EcomCommonModel cm = null;
                Map hs = null;

                // Get agency and booking details from LRP booking
                Map resultMap = ecomvdsservice.getLrpBookingRemarks(lrpbookno);
                if (resultMap != null) {
                    List list = (List) resultMap.get("booking");
                    if (list != null && list.size() > 0) {
                        resultMap = (Map) list.get(0);
                        agencyid = checkNull((String) resultMap.get("agencyid"));
                        origin = checkNull((String) resultMap.get("originportdesc"));
                        delivery = checkNull((String) resultMap.get("finalportdesc"));
                    }
                }

                // Get Corporate agency
                String corporateAgency = null;
                Map corpAgency = ecomvdsservice.getEcomCorporateAgency(agencyid);
                if (corpAgency != null && corpAgency.size() > 0) {
                    List corpList = (List) corpAgency.get("agencymatrix");
                    if (corpList != null && corpList.size() > 0) {
                        corpAgency = (Map) corpList.get(0);
                        corporateAgency = checkNull((String) corpAgency.get("corpagency"));
                    }
                }
                System.out.println("[ECOM] EcomBookingBean cancelConfirmedBooking corporateAgency: " + corporateAgency);

                // Insert into edidocstatus
                lst = new ArrayList();
                EcomBKGEDIDocStatus doc = new EcomBKGEDIDocStatus();
                cm = new EcomCommonModel();
                doc.setBook_no(lrpbookno);
                doc.setEdireaderid(readerid);
                doc.setTranstype("1");
                doc.setStatus("R");
                doc.setEdiparty(configMap.get(GeneralConf.CARRIER_CODE) + GeneralConf.EDI_TYPE_BKG_CANCEL);
                doc.setMessage(GeneralConf.EDI_MESSAGETYPE_BKG);
                doc.setServprovname(configMap.get(GeneralConf.CARRIER_CODE) + GeneralConf.EDI_ECOM);
                doc.setAmendment_version(0);
                lst.add(doc);
                cm.setEdidocstatus(lst);
                hs = (Map) ecomvdsservice.setecomeditempdocstatus(cm);

                // Insert into edireadermaster
                lst = new ArrayList();
                EcomBKGReaderMaster edirm = new EcomBKGReaderMaster();
                cm = new EcomCommonModel();
                edirm.setEdiid(EcomConsts.ONLINE_BOOKING);
                edirm.setEdireaderid(readerid);
                edirm.setAgencycode(corporateAgency);
                edirm.setLocationcode(agencyid);
                edirm.setUploadstatus("Completed");
                lst.add(edirm);
                cm.setEdireadermaster(lst);
                hs = (Map) ecomvdsservice.setedireadermaster(cm);

                // Insert into editempbooking_remarks - Reason
                lst = new ArrayList();
                EcomBKGRemarks rm = new EcomBKGRemarks();
                cm = new EcomCommonModel();
                rm.setBook_no(lrpbookno);
                rm.setRemarksref("Reason");
                rm.setRemarksval(checkNull(cancelReason));
                rm.setEdireaderid(readerid);
                rm.setVersion("0");
                rm.setCategory(null);
                lst.add(rm);
                cm.setEditempbooking_remarks(lst);
                hs = (Map) ecomvdsservice.setEdiTempBookingRemarks(cm);

                // Insert into editempbooking_remarks - Remarks
                if (cancelRemarks != null && cancelRemarks.trim().length() > 0) {
                    lst = new ArrayList();
                    rm = new EcomBKGRemarks();
                    cm = new EcomCommonModel();
                    rm.setBook_no(lrpbookno);
                    rm.setRemarksref("Remarks");
                    rm.setRemarksval(checkNull(cancelRemarks));
                    rm.setEdireaderid(readerid);
                    rm.setVersion("0");
                    rm.setCategory(null);
                    lst.add(rm);
                    cm.setEditempbooking_remarks(lst);
                    hs = (Map) ecomvdsservice.setEdiTempBookingRemarks(cm);
                }
            }

            // Mail notification
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            String dateString = format.format(new Date());
            String strAgency = getAgencyMail(agencyid);
            String apname = "", apmail = "";
            Map hs = (Map) ecomvdsservice.getecombookingcust(lrpbookno);
            List custlst = (List) hs.get("booking_cust");
            if (custlst != null && custlst.size() > 0) {
                for (int j = 0; j < custlst.size(); j++) {
                    Map rs = (Map) custlst.get(j);
                    String custType = checkNull((String) rs.get("custtypeid"));
                    if ("AP".equalsIgnoreCase(custType)) {
                        apname = checkNull((String) rs.get("name"));
                        apmail = checkNull((String) rs.get("email"));
                    }
                }
            }

            // Customer mail
            MailFormatVO mailFormatObj = getMailContent("E-Booking", "cnfcnl-cus", "C");
            String msg = mailFormatObj.getStrMailContent();
            msg = msg.replace("@book_no", lrpbookno);
            msg = msg.replace("@origin", origin);
            msg = msg.replace("@delivery", delivery);
            msg = msg.replace("@remarks", checkNull(cancelReason));
            msg = msg.replace("@cancelremarks", checkNull(cancelRemarks));
            msg = msg.replaceAll("@date", dateString);
            msg = msg.replaceAll("@apname", apname);
            msg = msg.replaceAll("@ebook_no", ecombookno);
            String subject = mailFormatObj.getStrSubject();
            subject = subject.replace("@bookref", lrpbookno);

            rvo.setStrMailMsg(msg);
            rvo.setStrSubject(subject);
            rvo.setActivityCode(mailFormatObj.getActivityCode());
            rvo.setStrFromMailId(mailFormatObj.getStrFromAddr());
            rvo.setStrToMailId(cancelUser);

            // Agency mail
            rvo.setAdmToMailId(strAgency);
            if (strAgency != null && strAgency.length() > 0) {
                MailFormatVO mailFormatObj2 = getMailContent("E-Booking", "cnfcnl-agn", "C");
                String msg2 = mailFormatObj2.getStrMailContent();
                msg2 = msg2.replace("@book_no", lrpbookno);
                msg2 = msg2.replace("@origin", origin);
                msg2 = msg2.replace("@delivery", delivery);
                msg2 = msg2.replace("@remarks", checkNull(cancelReason));
                msg2 = msg2.replace("@cancelremarks", checkNull(cancelRemarks));
                msg2 = msg2.replaceAll("@date", dateString);
                msg2 = msg2.replaceAll("@apname", apname);
                msg2 = msg2.replaceAll("@ebook_no", ecombookno);
                String subject2 = mailFormatObj2.getStrSubject();
                subject2 = subject2.replace("@bookref", lrpbookno);
                rvo.setAdmMailMsg(msg2);
                rvo.setAdmSubject(subject2);
            }

            rvo.setStrRefNo("Success");
            rvo.setStrDisplayMsg("Success");
            System.out.println("[ECOM] EcomBookingBean cancelConfirmedBooking completed successfully for: " + lrpbookno);
        } catch (Exception e) {
            System.out.println("[ECOM] EcomBookingBean cancelConfirmedBooking Exception: " + e.getMessage());
            e.printStackTrace();
            rvo.setStrRefNo("Error");
            rvo.setStrDisplayMsg("Error");
        } finally {
            try { if (ecomPs != null) ecomPs.close(); } catch (Exception e) {}
            try { if (ecomCon != null) ecomCon.close(); } catch (Exception e) {}
        }
        return rvo;
    }
}
