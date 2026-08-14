/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.lrp.nfr.multiAgencyUtil.MultiAgencyUtil;
import com.svm.lrp.nfr.query.cbean.VDSInvoker;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import static net.ecom.AppTierCntrlBean.EcomBookingBean.GET_MAIL_CONTENT_LANG;
import net.ecom.common.DateConversion_New;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.ZipFileConversion;
import net.ecom.si.vo.Booking_cust;
import net.ecom.si.vo.CompleteModel;
import net.ecom.v1.cntlbean.EcomV1AmendmentBookingBeanRemote;
import net.ecom.vds.service.EcomBKGVdsService;
import net.ecom.vds.service.EcomBKGSpsService;
import net.ecom.vds.service.EcomSiVdsService;
import net.ecom.vds.serviceVo.EcomBKGCargo;
import net.ecom.vds.serviceVo.EcomBKGCommDG;
import net.ecom.vds.serviceVo.EcomBKGCommodity;
import net.ecom.vds.serviceVo.EcomBKGCommonModel;
import net.ecom.vds.serviceVo.EcomBKGConf;
import net.ecom.vds.serviceVo.EcomCommonModel;
import net.ecom.vds.serviceVo.Editempbooking_ics_new;
import net.ecom.vds.serviceVo.EcomBKGCustomer;
import net.ecom.vds.serviceVo.EcomBKGEDIDocStatus;
import net.ecom.vds.serviceVo.EcomBKGHaulage;
import net.ecom.vds.serviceVo.EcomBKGLeg;
import net.ecom.vds.serviceVo.EcomBKGMasterVO;
import net.ecom.vds.serviceVo.EcomBKGReaderMaster;
import net.ecom.vds.serviceVo.EcomBKGReference;
import net.ecom.vds.serviceVo.EcomBKGRemarks;
import net.ecom.vds.serviceVo.EcomBKGRouting;
import net.ecom.vds.serviceVo.RouteVo;
import net.ecom.vo.Agencydto;
import net.ecom.vo.AmendmentBookingFormBean;
import net.ecom.vo.BookingHaulagedetailsVo;
import net.ecom.vo.CutoffConfigVO;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.FileDetailsVO;
import net.ecom.vo.MailFormatVO;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.Route;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author deepa.p
 */
@Stateless(name = "EcomAmendmentBookingBean")
@Remote(EcomAmendmentBookingBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
    ,
    @Resource(name = "jdbc/MASTERDB", mappedName = "java:/MASTERDS")
})
public class EcomAmendmentBookingBean implements EcomAmendmentBookingBeanRemote {

    protected static final String BOOKING_MASTEREDIT = "select agencyid,originPortId,"
            + "finalPortId,loadPortId, dischargePortId,bookDate, bookReady, preCarriage,TemplateName, onCarriage, ebook_status,intransitport,"
            + "remarks ,rateno,routeId,AgencyRefNo,isamendment,emptypickup,emptypickupdate,oceanfreight,"
            + "trptsvccode,nat_code,extbookno,pickup_point,salesagreedrate,ccmailid,haulercode,haulagetype,pickupdate,dropdate,exportRef,releasetype,custPO,customerreference,ACID,cargoprotectid,ShipperReference,ensfiling,t2lfiling,sisubmitparty,enstype  from ecombooking(nolock) where ebookno = ?";
    protected static final String GET_BOOKING_FILES = "select filecatgeory,filename from ecombooking_files(nolock) where ebookno=?";
    protected static final String BOOKING_CUSTEDIT = "select bc.custTypeId,bc.custId,bc.countryId,bc.name,bc.address1,bc.address2,bc.city,bc.zipCode,bc.telephone,bc.fax,"
            + "bc.email,bc.cid,bc.flag,bc.toorder,bc.stateid,bc.eori,bc.persontype from ecombooking_cust bc(nolock) left join customermaster(nolock) cm on cm.custcode = bc.custId and cm.status='A' and cm.inactiveflag='A' where bc.ebookno = ?";
    private static final String Routingfromleg_qry = "select leg_no,bound,servicecode,vesselcode,voyagecode,loadportid,dischargeportid,etddate,etadate,legtypeid,groupcallid,loadTerminal,dischargeTerminal,callIdLoading,callIdDischarge,dbound from ecombooking_leg (nolock)  where ebookno =?";
    private static final String SAVE_EBOOKING_CUST_QRY = "insert into ecombooking_cust (ebookno, custTypeId, custId, "
            + "countryId, name, address1, city, zipCode, telephone, fax, email, address2,flag,toorder,stateId,eori,persontype) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";
    private static final String BOOK_NO_GEN = "select mdlid,mdlprefix,mdlindex from ecomMdlIndex(nolock) where mdlid = ?";
    private static final String BOOK_NO_UPDATE = "update ecomMdlIndex set mdlindex=? where mdlid =?";
    private static final String GET_ECOM_BKG_VERSION = "select version,extbookno from ecombooking(nolock) where ebookno=?";
    private static final String CHECKSICREATION = "select esino from ecomSI(nolock) where status in ('D','A','S') and bookno=? ";
    private static final String SAVE_NEW_EBOOKING_MASTER_QRY = "insert into ecombooking (ebookno, agencyid,"
            + "originPortId,finalPortId, loadPortId, dischargePortId,bookDate,bookReady,preCarriage, onCarriage,remarks, "
            + " usercr,datecr,ebook_status,cust_code,templatename,rateno, "
            + " routeId,agencyRefNo,oceanfreight,trptsvccode,emptypickup,emptypickupdate,version,intransitport,submitted_date,dateup,userup) "
            + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,getdate(),?,?,?,?,?,?,?,?,?,?,?,?,getdate(),getdate(),?)";
    private static final String SAVE_EBOOKING_FILE = "insert into ecombooking_files (ebookno,FileName,FileCatgeory) values(?,?,?)";
    private static final String DELETE_FILES = "delete ecombooking_files where ebookno=?";
    private static final String ECOM_BOOKING_CARGO = "select ebookno,conid,containerno,eqpType, eqpSize, quantity, soc,ref_nor,dgFlag,tareWeight,grossWeight,cargoWeight,weightUnit,eqpstatus,oog from ecombooking_cargo(nolock)where ebookno=?";
    private static final String ECOM_BOOKING_COMMODITY = "select ebookno,conid, itemno, commodityFlag, commodity, HSCommodity,package,packageQty,cargoVolume,volumnUnit,nettwt,descr,bycode,byname,slcode,slname,cmdty_hblno from ecombooking_commodity(nolock)where ebookno=?";
    private static final String ECOM_MAIL_CONTENT = "select message,from_mailid,subject,activitycode from ecom_messageformat(nolock) where Status='a' and Type ='mail' and criteria = ? and Module =? and usertype = ?";
    private static final String SAVE_EBOOKING_REFER_QRY_NEW = "insert into ecombooking_refer (ebookno,conid,ref_setTemp, ref_minTemp, ref_maxTemp,ref_tempUnit, Humidity, Humidity_flag, Drains, CO2Value, O2Value, itemno,VentPercentage) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SAVE_EBOOKING_OOG_QRY_NEW = "insert into ecombooking_oog(ebookno,conid,leftft, rightft, forward,alt,topft,unit,comitemno) values(?,?,?,?,?,?,?,?,?)";
    private static final String SAVE_EBOOKING_DG_QRY_NEW = "insert into ecombooking_dg(ebookno,conid, itemno, MarinePollutant, unNo, class,FlashPt,FlashPtUnit) values(?,?,?,?,?,?,?,?)";
    private static final String SAVE_EBOOKING_COMMODITY_QRY_NEW = "insert into ecombooking_commodity(ebookno,conid, itemno, commodityFlag, commodity, HSCommodity,package,packageQty,cargoVolume,volumnUnit,nettwt,descr,bycode,byname,slcode,slname,cmdty_hblno) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SAVE_EBOOKING_CONT_QRY_NEW = "insert into ecombooking_cargo (ebookno,conid, eqpType, eqpSize, quantity, soc,ref_nor,dgFlag,tareWeight,grossWeight,cargoWeight,weightUnit,containerno,eqpstatus,oog) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String DELETE_BKG_CARGO = "delete ecombooking_cargo where ebookno=?";
    private static final String DELETE_BKG_COMMODITY = "delete ecombooking_commodity where ebookno=?";
    private static final String DELETE_BKG_DG = "delete ecombooking_dg where ebookno=?";
    private static final String DELETE_BKG_OOG = "delete ecombooking_oog where ebookno=?";
    private static final String DELETE_BKG_REFER = "delete ecombooking_refer where ebookno=?";
    private static final String DELETE_BOOKING_LEG = "delete from ecombooking_leg where ebookno=?";
    private static final String INSERT_BKG_LEG = "insert into ecombooking_leg(ebookno,leg_no,bound,servicecode,vesselcode,loadportid,dischargeportid,etddate,etadate,legTypeId,loadtype,dischtype,voyagecode,groupcallid,loadTerminal,dischargeTerminal,callIdLoading,callIdDischarge,dbound)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String DELETE_ECOM_CUST = "delete from ecombooking_cust where ebookno=?";
    private static final String BOOKING_DG = "select ebookno,conid,itemno,MarinePollutant,unNo,class,FlashPt,FlashPtUnit from ecombooking_dg(nolock)where ebookno=?";
    private static final String BOOKING_OOG = "select ebookno,conid,containerno,leftft,rightft,forward,alt,topft,unit,comItemNo from ecombooking_oog(nolock)where ebookno=?";
    private static final String BOOKING_REFER = "select ebookno,itemno,containerno,ref_setTemp,ref_minTemp,ref_maxTemp,ref_tempUnit,humidity,humidity_flag,drains,co2value,o2value,ventpercentage from ecombooking_refer(nolock)where ebookno=?";
    private static final String SAVE_CCMAIN_USERDET = "update ecomuserdetails set ccmailid=? where emailid=?";
    private static final String GETECOM_BOOKNO = "select ebookno,ccmailid From ecombooking(nolock) where extbookno=?";
    //
    // Haulage details 
    private static final String SAVE_ECOM_BKG_HAULAGE = "insert into ecombooking_haulage (ebookno,eqprowid,eqptypeid,loadLocation,loadlocationname,contactPerson,contactAddress,contactPhone,contactEmail,referenceNumber,dropDate,remarks,docType,overWeight,stopSequence,TMSClientCode, eqpId) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String DELETE_ECOM_BKG_HAULAGE = "delete from ecombooking_haulage where ebookno=?";
    private static final String INSERT_ECOMBOOKNG_CONF = "insert into ecombooking_conf (ebookno,bkgCutoff,docCutoff,vgmCutoff,bbroroCutoff,hazCutoff,portentryCutoff) values(?,?,?,?,?,?,?)";
    private static final String DELETE_ECOMBOOKNG_CONF = "delete from ecombooking_conf where ebookno=?";
    // ENS Filling
    private static final String DELETE_BOOKING_ENSDEC = "DELETE FROM ecombooking_ics_new WHERE ebookno = ?";
    private static final String INSERT_BOOKING_ENSDEC = "INSERT INTO ecombooking_ics_new "
            + "(ebookno, eucustom, bill_type, ens_type, payment_mode, cust_name, "
            + "address, address1, country, state, telephone, email, enseori, filing_type, city,zipcode) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
    private static final String GET_BOOKING_ENSDEC = "SELECT ebookno, eucustom, bill_type, ens_type, payment_mode, cust_name, address, address1, country, state, telephone, email, enseori, filing_type, city,zipcode FROM ecombooking_ics_new WHERE ebookno = ?";
    //
    protected static final String GET_MAIL_CONTENT_LANG = "select message,from_mailid,subject,activitycode from ecom_messageformatlang(nolock) where Status='a' and Type ='mail' and criteria = ? and Module =? and usertype = ? and lang=?";

    @Inject
    VDSInvoker vds;

    EcomBKGVdsService ecomvdsservice;
    EcomBKGSpsService ecomvdspservice;
    EcomSiVdsService siVDS;

    @EJB(mappedName = "java:global/e-comOnline-EJBModule_New/EcomV1AmendmentBookingBean!net.ecom.v1.cntlbean.EcomV1AmendmentBookingBeanRemote")
    EcomV1AmendmentBookingBeanRemote vAmend1BKGBeanObj;

    @EJB(mappedName = "java:global/e-comOnline-EJBModule_New/EComCntrlBean!net.ecom.AppTierCntrlBean.EComCntrlBeanRemote")
    EComCntrlBeanRemote cntrlBeanObj;

    @Resource
    private SessionContext context;

    @PostConstruct
    public void inti() {
        ecomvdsservice = vds.create(EcomBKGVdsService.class);
        ecomvdspservice = vds.create(EcomBKGSpsService.class);
        siVDS = vds.create(EcomSiVdsService.class);
    }

    @Override
    public AmendmentBookingFormBean getEcomBookingDetails(String bookNo) throws RemoteException {
        AmendmentBookingFormBean eBookingmaster = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Agencydto agencyVo = new Agencydto();
        String phone = null, fax = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(BOOKING_MASTEREDIT);
            System.out.println("[ECOM] EcomAmendmentBookingBean >>>entered in booking details" + bookNo);
            ps.setString(1, checkNull(bookNo));
            rs = ps.executeQuery();
            while (rs.next()) {
                eBookingmaster = new AmendmentBookingFormBean();
                eBookingmaster.setBooking_refno(bookNo.trim());
                eBookingmaster.setEbook_date(rs.getString("bookDate"));
                eBookingmaster.setEbook_dateBut(rs.getString("bookDate"));
                eBookingmaster.setEmptyPickup(rs.getString("emptypickup"));
                eBookingmaster.setPodintransit(checkNull(rs.getString("intransitport")).trim());
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
                String extbookno = checkNull(rs.getString("extbookno"));
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
                if(shipperReference.length() > 0){
                    eBookingmaster.setShipperReferenceType(checkNull(shipperReference.substring(0, 1)));
                    eBookingmaster.setShipperReference(checkNull(shipperReference.substring(1)));
                } else {
                    eBookingmaster.setShipperReferenceType("");
                    eBookingmaster.setShipperReference("");
                }
                eBookingmaster.setEnsfiling(rs.getString("ensfiling"));
                eBookingmaster.setEbook_preferred_agency(rs.getString("agencyid"));
                eBookingmaster.setEbook_t2lfiling(rs.getString("t2lfiling"));
                eBookingmaster.setEbook_sisubmit_party(rs.getString("sisubmitparty"));
                eBookingmaster.setEbook_EnsDocType(rs.getString("enstype"));

                if (extbookno != null && extbookno.length() > 0) {
                    Map hs = (Map) ecomvdsservice.getecombookingdetails(extbookno);
                    List lst = (List) hs.get("booking");
                    if (lst.size() > 0) {
                        Map hsmap = (Map) lst.get(0);
                        eBookingmaster.setDocumentalagencyid(checkNull((String) hsmap.get("documentalagencyid")));
                        eBookingmaster.setAgencyissueid(checkNull((String) hsmap.get("agencyissueid")));
                        eBookingmaster.setAgencyid(checkNull((String) hsmap.get("agencyid")));
                    }
                }
                ps = con.prepareStatement(BOOKING_CUSTEDIT);
                ps.setString(1, bookNo);
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
                        eBookingmaster.setEbook_bookingzipcode(checkNull(rs.getString("zipCode")));
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
                        eBookingmaster.setEbook_bookingparty_desc(checkNull(rs.getString("name")));
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
                        eBookingmaster.setEbook_agreezipcode(checkNull(rs.getString("zipCode")));
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
                        eBookingmaster.setEbook_shipperzipcode(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setEbook_shipper_id(checkNull(rs.getString("cid")));
                        eBookingmaster.setShipperCheck(checkNull(rs.getString("flag")));
                        eBookingmaster.setEbook_shippereori(checkNull(rs.getString("eori")));
                        eBookingmaster.setShippertypeperson(checkNull(rs.getString("persontype")));
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
                        eBookingmaster.setEbook_consigneezipcode(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setEbook_consigne_id((checkNull(rs.getString("cid"))));
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
                        eBookingmaster.setEbook_notifyzipcode(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setEbook_notify_id((checkNull(rs.getString("cid"))));
                        eBookingmaster.setNotifyCheck(checkNull(rs.getString("flag")));
                        eBookingmaster.setHidNTToOrder(checkNull(rs.getString("toorder")));
                        eBookingmaster.setEbook_notifyeori(checkNull(rs.getString("eori")));
                        eBookingmaster.setNotifytypeperson(checkNull(rs.getString("persontype")));
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
                        eBookingmaster.setEbook_notifyzipcode_n1(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setEbook_notify_id_n1((checkNull(rs.getString("cid"))));
                        eBookingmaster.setHidNT1ToOrder(checkNull(rs.getString("toorder")));
                        eBookingmaster.setNotify1Check(checkNull(rs.getString("flag")));
                        eBookingmaster.setEbook_notifyeori_n1(checkNull(rs.getString("eori")));
                        eBookingmaster.setNotifytypeperson_n1(checkNull(rs.getString("persontype")));
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
                        eBookingmaster.setEbook_forwarderzipcode(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setEbook_forwarder_id(checkNull(rs.getString("cid")));
                        eBookingmaster.setEbook_forwardereori(checkNull(rs.getString("eori")));
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
                        eBookingmaster.setEbook_Warehousezipcode(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setEbook_Warehouse_id(checkNull(rs.getString("cid")));
                        eBookingmaster.setEbook_Warehouseeori(checkNull(rs.getString("eori")));
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
                        eBookingmaster.setEbook_NamedCustomerzipcode(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setEbook_NamedCustomer_id((checkNull(rs.getString("cid"))));
                        eBookingmaster.setEbook_NamedCustomereori(checkNull(rs.getString("eori")));
                        eBookingmaster.setEbook_NamedCustomerpersontype(checkNull(rs.getString("persontype")));
                        eBookingmaster.setHidCNToOrder(checkNull(rs.getString("toorder")));
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
                    //SL
                    if (checkNull(rs.getString("custTypeId")).equalsIgnoreCase("SL")) {
                        eBookingmaster.setEbook_Sellercity(checkNull(rs.getString("city")));
                        if (checkNull(rs.getString("city")).equalsIgnoreCase("NA")) {
                            eBookingmaster.setEbook_Sellercity_desc("No Cities Available");
                        }
                        eBookingmaster.setEbook_Sellercountry(checkNull(rs.getString("countryId")));
                        eBookingmaster.setEbook_Sellername(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_Sellercode(checkNull(rs.getString("custId")));
                        eBookingmaster.setEbook_Selleraddress(checkNull(rs.getString("address1")));
                        eBookingmaster.setEbook_Selleraddress1(checkNull(rs.getString("address2")));
                        eBookingmaster.setEbook_Selleremail(checkNull(rs.getString("email")));
                        eBookingmaster.setEbook_Sellerstate(checkNull(rs.getString("stateid")));
                        eBookingmaster.setEbook_Sellerzipcode(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setEbook_Seller_id((checkNull(rs.getString("cid"))));
                        eBookingmaster.setEbook_Sellereori(checkNull(rs.getString("eori")));
                        eBookingmaster.setEbook_Sellerpersontype(checkNull(rs.getString("persontype")));
                        eBookingmaster.setHidCNToOrder(checkNull(rs.getString("toorder")));
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
                    //BY
                    if (checkNull(rs.getString("custTypeId")).equalsIgnoreCase("BY")) {
                        eBookingmaster.setEbook_Buyercity(checkNull(rs.getString("city")));
                        if (checkNull(rs.getString("city")).equalsIgnoreCase("NA")) {
                            eBookingmaster.setEbook_Buyercity_desc("No Cities Available");
                        }
                        eBookingmaster.setEbook_Buyercountry(checkNull(rs.getString("countryId")));
                        eBookingmaster.setEbook_Buyername(checkNull(rs.getString("name")));
                        eBookingmaster.setEbook_Buyercode(checkNull(rs.getString("custId")));
                        eBookingmaster.setEbook_Buyeraddress(checkNull(rs.getString("address1")));
                        eBookingmaster.setEbook_Buyeraddress1(checkNull(rs.getString("address2")));
                        eBookingmaster.setEbook_Buyeremail(checkNull(rs.getString("email")));
                        eBookingmaster.setEbook_Buyerstate(checkNull(rs.getString("stateid")));
                        eBookingmaster.setEbook_Buyerzipcode(checkNull(rs.getString("zipCode")));
                        eBookingmaster.setEbook_Buyer_id((checkNull(rs.getString("cid"))));
                        eBookingmaster.setEbook_Buyereori(checkNull(rs.getString("eori")));
                        eBookingmaster.setEbook_Buyerpersontype(checkNull(rs.getString("persontype")));
                        eBookingmaster.setHidCNToOrder(checkNull(rs.getString("toorder")));
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
        return eBookingmaster;
    }

    @Override
    public List getEcomBookingCargoDetails(String bookNo) throws RemoteException {
        System.out.println("[ECOM] EcomAmendmentBookingBean >>>enterenced in send to agency>>");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String weight = null, volume = null, netweight = null;
        String unitvalues = null;
        List commlist = null;
        Map<String, Integer> cotainerNoMap = new HashMap<String, Integer>();
        int i = 1;
        AmendmentBookingFormBean eBookingcargo = null;
        Map commhs = null, insmap = null;
        String qouteno = null;
        try {
            insmap = getInsuranceDetils(bookNo);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(ECOM_BOOKING_CARGO);
            ps.setString(1, checkNull(bookNo));
            rs = ps.executeQuery();
            System.out.println("[ECOM] EcomAmendmentBookingBean =>>>>" + bookNo);
            commlist = new ArrayList();
            commhs = new HashMap();
            while (rs.next()) {
                eBookingcargo = new AmendmentBookingFormBean();
                //ecombooking_cargo
                eBookingcargo.setCargoid_new(String.valueOf(i));
                eBookingcargo.setConid(rs.getInt("conid"));
                String eqpType = checkNull(rs.getString("eqpSize"));
                String eqp_type = checkNull(rs.getString("eqpType"));
                String containerNo = checkNull(rs.getString("containerno"));
                if (containerNo.length() > 0) {
                    eBookingcargo.setContainerNo(containerNo);
                    eBookingcargo.setEqptemp("Y");
                } else {
                    containerNo = eqpType + "TNB";
                    eBookingcargo.setContainerNo(containerNo);
                    eBookingcargo.setEqptemp("N");
                }
                if (containerNo.contains(eqpType + "TNB")) {
                    eBookingcargo.setEbook_isdummy("Y");
                } else {
                    eBookingcargo.setEbook_isdummy("N");
                }
                // System.out.println("[ECOM] EcomAmendmentBookingBean ############>>>containerNo" + containerNo);
                String setTypedesc = checkNull(rs.getString("eqpSize")) + eqp_type;
                eBookingcargo.setEbook_hazardous(checkNull(rs.getString("dgFlag")));
                eBookingcargo.setEbook_eqp(checkNull(rs.getString("eqpSize")));
                eBookingcargo.setEbook_eqp_type(checkNull(rs.getString("eqpType")));
                eBookingcargo.setEbook_eqp_type_desc(setTypedesc);
                eBookingcargo.setEbook_eqp_qty(checkNull(rs.getString("quantity")));
                eBookingcargo.setEbook_nor(checkNull(rs.getString("ref_nor")));
                eBookingcargo.setHzd(checkNull(rs.getString("dgFlag")));
                eBookingcargo.setEqpstatus(checkNull(rs.getString("eqpstatus")));
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
                eBookingcargo.setOog(checkNull(rs.getString("oog")));
                //   System.out.println("[ECOM] EcomAmendmentBookingBean eBookingcargo.setOog " + checkNull(rs.getString("oog")));
                commhs.put(rs.getInt("conid"), eBookingcargo);
                i++;
                //end ecombooking_cargo
            }

            //ecombooking_commodity
            ps = con.prepareStatement(ECOM_BOOKING_COMMODITY);
            ps.setString(1, checkNull(bookNo));
            rs = ps.executeQuery();
            while (rs.next()) {
                eBookingcargo = new AmendmentBookingFormBean();
                int conid = (rs.getInt("conid"));
                eBookingcargo.setEbook_commoditycode(checkNull(rs.getString("commodity")));
                eBookingcargo.setEbook_conid(checkNull(rs.getString("conid")));
                String cargoVol, netWt;
                String VolumeUnt = checkNull(rs.getString("volumnUnit"));
                volume = rs.getString("cargoVolume") != null && !rs.getString("cargoVolume").isEmpty() && !"0.0".equalsIgnoreCase(rs.getString("cargoVolume")) ? rs.getString("cargoVolume") : "";
                if (rs.getString("cargoVolume") != null && !rs.getString("cargoVolume").isEmpty() && !"0.0".equalsIgnoreCase(rs.getString("cargoVolume"))) {
                    //    System.out.println("[ECOM] EcomAmendmentBookingBean ========>>>>cargoVolumeAmend" + rs.getString("cargoVolume"));
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
                //   System.out.println("[ECOM] EcomAmendmentBookingBean cargoVol---" + cargoVol);
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
                    //     System.out.println("[ECOM] EcomAmendmentBookingBean ========>>>>netweight" + rs.getString("nettwt"));
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
                System.out.println("HBLNo: " + checkNull(rs.getString("cmdty_hblno")) + ", SellerCode: " + checkNull(rs.getString("slcode")) + ", SellerName: " + checkNull(rs.getString("slname")) + ", BuyerCode: " + checkNull(rs.getString("bycode")) + ", BuyerName: " + checkNull(rs.getString("byname")));

                //String strPattern = "^0+";
                if (insmap != null) {
                    eBookingcargo.setInsuredemail(checkNull((String) insmap.get("insuredemail")));
                    if ((insmap.containsKey((eBookingcargo.getEbook_commoditycode())) || insmap.containsKey((eBookingcargo.getHscode())))) { //.replaceAll(strPattern, "")
                        eBookingcargo.setIsinsuranceavailable("Y");
                        qouteno = checkNull((String) insmap.get((eBookingcargo.getEbook_commoditycode())));
                        if (qouteno.length() == 0) {
                            qouteno = checkNull((String) insmap.get((eBookingcargo.getHscode())));
                        }
                        eBookingcargo.setQuoteno(qouteno);
                    }
                }
                commhs = setCommodityInfo(commhs, eBookingcargo, conid);
            }
            //end
            //ecombooking_dg - Load Multiple DG Details
            ps = con.prepareStatement(BOOKING_DG);
            ps.setString(1, checkNull(bookNo));
            rs = ps.executeQuery();
            
            // Group DG entries by conid
            Map<Integer, List<AmendmentBookingFormBean>> dgEntriesMap = new HashMap<>();
            
            while (rs.next()) {
                int conid = rs.getInt("conid");
                int itemno = rs.getInt("itemno");
                
                eBookingcargo = new AmendmentBookingFormBean();
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
                    dgEntriesMap.put(conid, new ArrayList<AmendmentBookingFormBean>());
                }
                dgEntriesMap.get(conid).add(eBookingcargo);
                
                System.out.println("[ECOM] EcomAmendmentBookingBean Load DG: conid=" + conid + ", itemno=" + itemno + ", unNo=" + eBookingcargo.getEbook_unno());
            }
            
            // Set dgDetailsList for each container
            for (Map.Entry<Integer, List<AmendmentBookingFormBean>> entry : dgEntriesMap.entrySet()) {
                int conid = entry.getKey();
                List<AmendmentBookingFormBean> dgList = entry.getValue();
                
                AmendmentBookingFormBean commVo = (AmendmentBookingFormBean) commhs.get(conid);
                if (commVo != null) {
                    commVo.setDgDetailsList(dgList);
                    commVo.setDgCount(dgList.size());
                    
                    // Set first DG entry fields for backward compatibility
                    if (!dgList.isEmpty()) {
                        AmendmentBookingFormBean firstDG = dgList.get(0);
                        commVo.setEbook_flashpoint(firstDG.getEbook_flashpoint());
                        commVo.setEbook_flashunit(firstDG.getEbook_flashunit());
                        commVo.setEbook_unno(firstDG.getEbook_unno());
                        commVo.setEbook_poluttunit(firstDG.getEbook_poluttunit());
                        commVo.setEbook_dgclass(firstDG.getEbook_dgclass());
                    }
                    
                    commhs.put(conid, commVo);
                    System.out.println("[ECOM] EcomAmendmentBookingBean Set dgDetailsList for conid=" + conid + ", count=" + dgList.size());
                }
            }
            //end
            //ecombooking_oog

            ps = con.prepareStatement(BOOKING_OOG);
            ps.setString(1, checkNull(bookNo));
            rs = ps.executeQuery();
            while (rs.next()) {
                //  System.out.println("[ECOM] EcomAmendmentBookingBean ===>BOOKING_OOG<===");
                eBookingcargo = new AmendmentBookingFormBean();
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
            //end
            //ecombooking_refer

            ps = con.prepareStatement(BOOKING_REFER);
            ps.setString(1, checkNull(bookNo));
            rs = ps.executeQuery();
            while (rs.next()) {
                eBookingcargo = new AmendmentBookingFormBean();
                int itemno = (rs.getInt("itemno"));
                String settemp = checkNull(rs.getString("ref_setTemp"));
                String tempunit = checkNull(rs.getString("ref_tempUnit"));
                String mintemp = checkNull(rs.getString("ref_minTemp"));
                String maxtemp = checkNull(rs.getString("ref_maxTemp"));
                if ("0.0".equalsIgnoreCase(settemp)) {
                    settemp = "";
                    tempunit = "";
                    mintemp = "";
                    maxtemp = "";
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
                eBookingcargo.setVentilation(checkNull(rs.getString("ventpercentage")));

                commhs = setCommodityRefer(commhs, eBookingcargo, itemno);
            }
            //end

            Iterator it = commhs.entrySet().iterator();
            int j = 1;
            int inc = 0;
            j = 1;
            String prev = null, curr = null;
            while (it.hasNext()) {

                Map.Entry pairs = (Map.Entry) it.next();
                AmendmentBookingFormBean crg = (AmendmentBookingFormBean) pairs.getValue();

                String eqptemp = crg.getEqptemp();

                if ("N".equalsIgnoreCase(eqptemp)) {
                    int size = Integer.parseInt(crg.getEbook_eqp_qty());

                    String cntno = crg.getContainerNo();
                    if (inc == 0) {
                        prev = cntno;
                        curr = cntno;
                    }
                    curr = cntno;
                    if (!prev.equalsIgnoreCase(curr)) {
                        prev = curr;
                        j = 1;
                    } else {
                        if (inc != 0) {
                            j = cotainerNoMap.get(cntno) + 1;
                        }
                    }
                    if (cotainerNoMap.containsKey(cntno)) {
                        j = cotainerNoMap.get(cntno);
                    } else {
                        j = 1;
                    }
                    for (int k = 0; k < size; k++) {

                        crg.setContainerNo(cntno + j);
                        commlist.add(crg.clone());
                        j++;
                    }
                    cotainerNoMap.put(cntno, j);
                    //  System.out.println("[ECOM] EcomAmendmentBookingBean >>cotainerNoMap>>" + cotainerNoMap);
                    //cotainerNoMap.put(cntno, j);
                    inc++;
                } else {
                    commlist.add(crg.clone());
                }
                System.out.println("[ECOM] EcomAmendmentBookingBean >>>cotainerNoMap>>" + cotainerNoMap);
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
        return commlist;
    }

    private Map setCommodityInfo(Map commlist, AmendmentBookingFormBean eBookingcargo, int itemno) {
        AmendmentBookingFormBean commVo = (AmendmentBookingFormBean) commlist.get(itemno);
        //
        try {
            System.out.println("[ECOM] EcomAmendmentBookingBean ===>setCommodityInfo<===" + itemno);
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

    private Map setCommodityDG(Map commlist, AmendmentBookingFormBean eBookingcargo, int itemno) {
        AmendmentBookingFormBean commVo = (AmendmentBookingFormBean) commlist.get(itemno);
        //
        try {
            System.out.println("[ECOM] EcomAmendmentBookingBean ===>setCommodityDG<===" + itemno);
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

    private Map setCommodityOOG(Map commlist, AmendmentBookingFormBean eBookingcargo, int itemno) {
        AmendmentBookingFormBean commVo = (AmendmentBookingFormBean) commlist.get(itemno);
        //
        try {
            System.out.println("[ECOM] EcomAmendmentBookingBean ===>setCommodityOOG<===" + itemno);
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

    private Map setCommodityRefer(Map commlist, AmendmentBookingFormBean eBookingcargo, int itemno) {
        AmendmentBookingFormBean commVo = (AmendmentBookingFormBean) commlist.get(itemno);
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
//

    @Override
    public ArrayList getEcomRoutingDetailsfrombookingleg(String bookNo) throws RemoteException {

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
                //
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
                routing.setBlpolTer(checkNull(rs.getString("loadTerminal")));
                routing.setBlpodTer(checkNull(rs.getString("dischargeTerminal")));
                routing.setLoadcallid(checkNull(rs.getString("callIdLoading")));
                routing.setDischargecallid(checkNull(rs.getString("callIdDischarge")));
                routing.setLoadter(checkNull(rs.getString("loadTerminal")));
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
    public AmendmentBookingFormBean getLRPBookingMasterDetails(String bookNo) throws RemoteException {
        ArrayList viewlist = null;
        String custType = null, ccMailId = null;
        boolean n1 = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        AmendmentBookingFormBean bookVO = null;
        SimpleDateFormat target = new SimpleDateFormat(GeneralConf.getStrDbFormat());
        SimpleDateFormat source = new SimpleDateFormat(GeneralConf.strDbFormatWithOutTime);
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GETECOM_BOOKNO);
            ps.setString(1, bookNo);
            rst = ps.executeQuery();
            if (rst.next()) {
                ccMailId = checkNull(rst.getString("ccmailid"));
            }
            viewlist = new ArrayList();
            bookVO = new AmendmentBookingFormBean();
            Map hs = (Map) ecomvdsservice.getecombookingdetails(bookNo);
            List lst = (List) hs.get("booking");
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                System.out.println("[ECOM] EcomAmendmentBookingBean getecombookingdetails==>" + rs);
                bookVO.setEbook_date(checkNull((String) rs.get("polat")));
                bookVO.setEbook_dateBut(checkNull((String) rs.get("polat")));
                bookVO.setCcMailId(checkNull(ccMailId));
                bookVO.setBooking_refno(checkNull((String) rs.get("book_no")));
                bookVO.setEbook_terminal(checkNull((String) rs.get("polterminal")));
                bookVO.setEbook_terminalpod(checkNull((String) rs.get("podterminal")));
                bookVO.setEbook_origin_country(checkNull((String) rs.get("originportid")));
                bookVO.setEbook_delivery_country(checkNull((String) rs.get("finalportid")));
                bookVO.setEbook_origin(checkNull((String) rs.get("originportid")));
                bookVO.setEbook_delivery(checkNull((String) rs.get("finalportid")));
                bookVO.setEbook_pod_country(checkNull((String) rs.get("dischargeportid")));
                bookVO.setEbook_pol_country(checkNull((String) rs.get("loadportid")));
                bookVO.setEbook_pod(checkNull((String) rs.get("dischargeportid")));
                bookVO.setEbook_pol(checkNull((String) rs.get("loadportid")));
                bookVO.setEbook_portofloading(checkNull((String) rs.get("loadportid")));
                bookVO.setPodintransit(checkNull((String) rs.get("intransitport")).trim());
                bookVO.setCarrierContact(checkNull((String) rs.get("transportserviceid")));
                bookVO.setEbook_shipterm_desc("");
                bookVO.setEbook_servicecode(checkNull((String) rs.get("servicecode")));
                bookVO.setEbook_vesselcode(checkNull((String) rs.get("vesselcode")));
                bookVO.setEbook_voyage(checkNull((String) rs.get("voyagecode")));
                bookVO.setEbook_pol_eta(checkNull((String) rs.get("podat")));
                bookVO.setEbook_pol_etd(checkNull((String) rs.get("polat")));
                bookVO.setPreCarriage(checkNull((String) rs.get("precarriage")));
                bookVO.setOnCarriage(checkNull((String) rs.get("oncarriage")));
                bookVO.setEbook_rate_reference("");
                bookVO.setEbook_agency_reference(checkNullUpperCase((String) rs.get("agencyrefno")));
                bookVO.setEbook_status_remarks(checkNull((String) rs.get("custremarks")));
                bookVO.setDocumentalagencyid(checkNull((String) rs.get("documentalagencyid")));
                bookVO.setAgencyissueid(checkNull((String) rs.get("agencyissueid")));
                bookVO.setAgencyid(checkNull((String) rs.get("agencyid")));
                bookVO.setEnsfiling(checkNull((String) rs.get("ensfiling")));
                bookVO.setEbook_preferred_agency(checkNull((String) rs.get("agencyid")));
                bookVO.setEbook_t2lfiling(checkNull((String) rs.get("t2lfiling")));
                bookVO.setEbook_EnsDocType(checkNull((String) rs.get("enstype")));
                if (rs.get("cargoprotectid") != null) {
                    bookVO.setCargoprotectid(((String) rs.get("cargoprotectid")));
                }
            }
            hs = (Map) ecomvdsservice.getecombookingreference(bookNo);
            //
            List reflst = (List) hs.get("booking_reference");
            if (reflst.size() > 0) {
                for (int j = 0; j < reflst.size(); j++) {
                    Map rs = (Map) reflst.get(j);
                    //    System.out.println("[ECOM] EcomAmendmentBookingBean getecombookingreference=>" + rs);
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
                    } else if ("NAT".equalsIgnoreCase(refid)) {
                        bookVO.setEbook_natcode(checkNull((String) rs.get("refval")));
                    } else if ("HAU".equalsIgnoreCase(refid)) {
                        bookVO.setEbook_haulercode(checkNull((String) rs.get("refval")));
                    } else if ("ETH".equalsIgnoreCase(refid)) {
                        String[] hauType = checkNull((String) rs.get("refval")).trim().split("~~");
                        bookVO.setEbook_haulagetype(hauType[0].trim());
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
                        bookVO.setEbook_reftype(checkNull((String) rs.get("refval")));
//                        String[] relType = checkNull((String) rs.get("refval")).split("~~");
//                        bookVO.setEbook_reftype(relType[0]);
                    } else if (EcomConsts.CUST_REF.equalsIgnoreCase(refid)) {
                        bookVO.setCustomerreference(checkNull((String) rs.get("refval")));
                    } else if (EcomConsts.ACID.equalsIgnoreCase(refid)) {
                        bookVO.setACID(checkNull((String) rs.get("refval")));
                    } else if ("DPW".equalsIgnoreCase(refid)) {
                        String storedValue = checkNull((String) rs.get("refval"));
                        if ("nullnull".equalsIgnoreCase(storedValue)) {
                            storedValue = "";
                        }
                        System.out.println("[ECOM] EcomAmendmentBookingBean getecombookingreference=>" + storedValue);
                        if(storedValue.length() > 0){
                            bookVO.setShipperReference(checkNull(storedValue.substring(1)));
                            bookVO.setShipperReferenceType(checkNull(storedValue.substring(0, 1)));
                        } else {
                            bookVO.setShipperReference("");
                            bookVO.setShipperReferenceType("");
                        }
                    } else if (EcomConsts.sisubmit.equalsIgnoreCase(refid)) {
                        bookVO.setEbook_sisubmit_party(checkNull((String) rs.get("refval")));
                    }

                }
            }
            hs = (Map) ecomvdsservice.getecombookingcust(bookNo, "A", "A");
            List custlst = (List) hs.get("booking_cust");
            if (custlst.size() > 0) {
                for (int j = 0; j < custlst.size(); j++) {
                    Map rs = (Map) custlst.get(j);
                    custType = checkNull((String) rs.get("custtypeid"));
                    if (custType.equalsIgnoreCase("BP")) {
                        bookVO.setEbook_bookingparty_desc(checkNull((String) rs.get("name")));
                        bookVO.setEbook_bookingcity(checkNull((String) rs.get("city")));
                        bookVO.setEbook_bookingcity_desc(checkNull((String) rs.get("city")));
                        bookVO.setEbook_bookingcountry(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_bookingname(checkNull((String) rs.get("name")));
                        bookVO.setEbook_bookingcode(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_bookingaddress(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_bookingaddress1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_bookingphone(checkNull((String) rs.get("telephone")).replace("+", ""));
                        bookVO.setEbook_bookingemail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_bookingfax(checkNull((String) rs.get("fax")).replace("+", ""));
                        bookVO.setEbook_bookingstate(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_bookingzipcode(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_bookingeori(checkNull((String) rs.get("eori")));
                        bookVO.setBookingtypeperson(checkNull((String) rs.get("persontype")));
                        bookVO.setEbook_Custeori(checkNull((String) rs.get("eori")));
                    } else if (custType.equalsIgnoreCase("AP")) {
                        bookVO.setEbook_bookingparty_desc(checkNull((String) rs.get("name")));
                        bookVO.setEbook_agreecity(checkNull((String) rs.get("city")));
                        bookVO.setEbook_agreecity_desc(checkNull((String) rs.get("city")));
                        bookVO.setEbook_agreecountry(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_agreename(checkNull((String) rs.get("name")));
                        bookVO.setEbook_agreecode(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_agreeaddress(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_agreeaddress1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_agreephone(checkNull((String) rs.get("telephone")).replace("+", ""));
                        bookVO.setEbook_agreeemail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_agreefax(checkNull((String) rs.get("fax")).replace("+", ""));
                        bookVO.setEbook_agree_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_agreestate(checkNull((String) rs.get("stateid")));
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
                        bookVO.setEbook_shipperphone(checkNull((String) rs.get("telephone")).replace("+", ""));
                        bookVO.setEbook_shipperemail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_shipperfax(checkNull((String) rs.get("fax")).replace("+", ""));
                        bookVO.setEbook_shipper_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_shipperstate(checkNull((String) rs.get("stateid")));
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
                        bookVO.setEbook_consigneephone(checkNull((String) rs.get("telephone")).replace("+", ""));
                        bookVO.setEbook_consigneeemail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_consigneefax(checkNull((String) rs.get("fax")).replace("+", ""));
                        bookVO.setEbook_consigne_id((checkNull((String) rs.get("cid"))));
                        bookVO.setEbook_consigneestate(checkNull((String) rs.get("stateid")));
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
                        bookVO.setEbook_notifyphone(checkNull((String) rs.get("telephone")).replace("+", ""));
                        bookVO.setEbook_notifyemail(checkNull((String) rs.get("email")));
                        bookVO.setEbook_notifyfax(checkNull((String) rs.get("fax")).replace("+", ""));
                        bookVO.setEbook_notify_id((checkNull((String) rs.get("cid"))));
                        bookVO.setEbook_notifystate(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_notifyzipcode(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_notifyeori(checkNull((String) rs.get("eori")));
                        bookVO.setNotifytypeperson(checkNull((String) rs.get("persontype")));
                    } else if (custType.equalsIgnoreCase("N1") && n1 == true) {
                        bookVO.setEbook_notifycity_desc_n1(checkNull((String) rs.get("city")));
                        bookVO.setEbook_notifycity_n1(checkNull((String) rs.get("city")));
                        bookVO.setEbook_notifycountry_n1(checkNull((String) rs.get("countryid")));
                        bookVO.setEbook_notifyname_n1(checkNull((String) rs.get("name")));
                        bookVO.setEbook_notifycode_n1(checkNull((String) rs.get("custid")));
                        bookVO.setEbook_notifyaddress_n1(checkNull((String) rs.get("address1")));
                        bookVO.setEbook_notifyaddress1_n1(checkNull((String) rs.get("address2")));
                        bookVO.setEbook_notifyphone_n1(checkNull((String) rs.get("telephone")).replace("+", ""));
                        bookVO.setEbook_notifyemail_n1(checkNull((String) rs.get("email")));
                        bookVO.setEbook_notifyfax_n1(checkNull((String) rs.get("fax")).replace("+", ""));
                        bookVO.setEbook_notify_id_n1((checkNull((String) rs.get("cid"))));
                        bookVO.setEbook_notifystate_n1(checkNull((String) rs.get("stateid")));
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
                        bookVO.setEbook_forwarder_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_forwarderstate(checkNull((String) rs.get("stateid")));
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
                        bookVO.setEbook_Warehouse_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_Warehousestate(checkNull((String) rs.get("stateid")));
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
                        bookVO.setEbook_NamedCustomer_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_NamedCustomerstate(checkNull((String) rs.get("stateid")));
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
                        bookVO.setEbook_Seller_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_Sellerstate(checkNull((String) rs.get("stateid")));
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
                        bookVO.setEbook_Buyer_id(checkNull((String) rs.get("cid")));
                        bookVO.setEbook_Buyerstate(checkNull((String) rs.get("stateid")));
                        bookVO.setEbook_Buyerzipcode(checkNull((String) rs.get("zipcode")));
                        bookVO.setEbook_Buyereori(checkNull((String) rs.get("eori")));
                        bookVO.setEbook_Buyerpersontype(checkNull((String) rs.get("persontype")));
                    }
                }
            }
            viewlist.add(bookVO);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rst != null) {
                    rst.close();
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
        return bookVO;

    }

    @Override
    public ArrayList getLRPBookingRoutingDetails(String bookNo) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs1 = null;
        ArrayList all = new ArrayList();
        Route routing = null;
        List lstcut = null;
        try {
            Map hs = (Map) ecomvdsservice.getecombookingleg(bookNo);
            System.out.println("[ECOM] EcomAmendmentBookingBean ####hs###>" + hs);
            List lst = (List) hs.get("booking_leg");
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map rs = (Map) lst.get(j);
                    routing = new Route();

                    int legno = Integer.parseInt((String) rs.get("leg_no"));
                    System.out.println("[ECOM] EcomAmendmentBookingBean ==legno===" + legno);
                    routing.setLegNo(legno);
                    routing.setBound(checkNull((String) rs.get("bound")));
                    routing.setRouteId(checkNull((String) rs.get("routeid")));
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
                    routing.setGrpCallId(checkNull((String) rs.get("callidloading")));
                    routing.setLoadcallid(checkNull((String) rs.get("callidloading")));
                    routing.setDischargecallid(checkNull((String) rs.get("calliddischarge")));
                    routing.setBlpolTer(checkNull((String) rs.get("polterminal")));
                    routing.setBlpodTer(checkNull((String) rs.get("podterminal")));
                    routing.setDischargebound(checkNull((String) rs.get("dbound")));

                    hs = (Map) ecomvdsservice.getecomgetoffterminal(checkNull((String) rs.get("callidloading")));
                    //////
                    lstcut = (List) hs.get("allschedules");
                    if (lstcut.size() > 0) {
                        Map bkg = (Map) lstcut.get(0);
                        String result = (String) bkg.get("arrdate");
                        routing.setCutoff_polarrdate(checkNull(result));
                    }
                    all.add(routing);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
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

    @Override
    public ArrayList getLRPBookingViewCargoDetails(String bookNo, String type, String ecomBookNo,String clearTemperatureWhileCopy) throws RemoteException {
        ArrayList cargoList = null;

        int i = 0;
        int qty = 0;
        AmendmentBookingFormBean eBookingcargo = null;
        String eqpId = null, cargoVol = null, weight = null, nettWt = null, VolumeUnt = null, hazardous = null, oog = null, rfr = null, lcl = null, cargoType = null;
        boolean isDuplicateCmdty = false;
        Map comhs = null;
        List comlst = null;
        // reuse
        String eqpdummyid = "";
        Hashtable htcargo = new Hashtable();
        HashMap cargono = new HashMap();
        String eqp_result = "";
        ArrayList al;
        //
        Map commhs = null, insmap = null;
        String qouteno = null;
        try {
            if ("R".equalsIgnoreCase(type)) {
                insmap = new HashMap();
            } else {
                insmap = getInsuranceDetils(ecomBookNo);
            }

            cargoList = new ArrayList();
            int j = 1;
            Map hs = (Map) ecomvdsservice.getecombookingcargo(bookNo);
            List lst = (List) hs.get("booking_cargo");
            if (lst.size() > 0) {
                for (int k = 0; k < lst.size(); k++) {
                    Map rs = (Map) lst.get(k);
                    eBookingcargo = new AmendmentBookingFormBean();
                    isDuplicateCmdty = false;
                    eqpId = checkNull((String) rs.get("eqpid"));
                    i = 0;
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
                    String settemp = checkNull((String) rs.get("settemp"));
                    cargoType = checkNull((String) rs.get("type"));

                    String oogtype = checkNull((String) rs.get("oog"));
                    eBookingcargo.setEbook_isdummy(checkNull((String) rs.get("isdummy")));
                    //   System.out.println("[ECOM] EcomAmendmentBookingBean ===>>isdummy " + checkNull((String) rs.get("isdummy")) + " ===>>equpType" + cargoType);
//                    if (checkNull((String) rs.get("nor")).equalsIgnoreCase("Y") || (checkNull((String) rs.get("nor")).equalsIgnoreCase("N") && (!"0.0".equalsIgnoreCase(settemp) && settemp.length() > 0))) {
                    if (cargoType.equalsIgnoreCase("R")) {
                        cargoType = "R";
                    } else if (oogtype.equalsIgnoreCase("Y")) {
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
                    eBookingcargo.setOog(oogtype);
                    eBookingcargo.setSoc(checkNull((String) rs.get("shipperown")));
                    eBookingcargo.setHidden_soc(checkNull((String) rs.get("shipperown")));
                    eBookingcargo.setLcl(lcl);

                    String tempunit = checkNull((String) rs.get("tempunit"));
                    String mintemp = checkNull((String) rs.get("tempmin"));
                    String maxtemp = checkNull((String) rs.get("tempmax"));
                    String ventilation = checkNull((String) rs.get("ventpercentage"));
                    String nor = checkNull((String) rs.get("nor"));
                    if ("0.0".equalsIgnoreCase(settemp)) {
                        settemp = "";
                        tempunit = "";
                        mintemp = "";
                        maxtemp = "";
                    }

                    if ("Y".equalsIgnoreCase(clearTemperatureWhileCopy) && "R".equalsIgnoreCase(type)) {
                        System.out.println("[ECOM] EcomAmendmentBookingBean ===>BOOKING_REFER<===CLEAR_TEMPERATURE_WHILE_COPY clearTemperatureWhileCopy=" + clearTemperatureWhileCopy + " type=" + type);
                        settemp = "";
                        tempunit = "";
                        mintemp = "";
                        maxtemp = "";
                        ventilation = "";
                        nor = "";
                    }

                    eBookingcargo.setEbook_nor(nor);
                    eBookingcargo.setEbook_presetemp(settemp);
                    eBookingcargo.setEbook_temperunit(tempunit);
                    eBookingcargo.setEbook_mintemp(mintemp);
                    eBookingcargo.setEbook_maxtemp(maxtemp);
                    eBookingcargo.setContainerNo(eqpId);

                    eBookingcargo.setHumidity(checkNull((String) rs.get("humidity")));
                    eBookingcargo.setHumidityflag(checkNull((String) rs.get("humidity_flag")));
                    eBookingcargo.setDrains(checkNull((String) rs.get("drains")));
                    eBookingcargo.setCo2(checkNull((String) rs.get("co2value")));
                    eBookingcargo.setO2(checkNull((String) rs.get("o2value")));
                    eBookingcargo.setVentilation(ventilation);

                    comhs = (Map) ecomvdsservice.getecombookingcommodity(bookNo, eqpId);
//                    System.out.println("getLRPBookingViewCargoDetails==> "+comhs);
                    comlst = (List) comhs.get("booking_commodity");
                    if (comlst.size() > 0) {
                        for (int m = 0; m < comlst.size(); m++) {
                            Map rs1 = (Map) comlst.get(m);
                            if (!isDuplicateCmdty) {
                                i++;
                                if (!checkNull((String) rs1.get("itemqty")).equalsIgnoreCase("0")) {
                                    eBookingcargo.setEbook_packquantity((String) rs1.get("itemqty"));
                                } else {
                                    eBookingcargo.setEbook_packquantity("");
                                }
                                eBookingcargo.setCargoid_new(String.valueOf(j));
                                cargoVol = checkNullDecimal((String) rs1.get("itemvolume"));
                                cargoVol = String.valueOf(new BigDecimal(cargoVol).toPlainString());
                                cargoVol = checkNullbigDecimal(cargoVol);
                                VolumeUnt = checkNull((String) rs1.get("itemvolumeunit"));
                                if (cargoVol == null || cargoVol.equalsIgnoreCase("0") || cargoVol.equalsIgnoreCase("0.0") || "".equalsIgnoreCase(cargoVol) || Float.parseFloat(cargoVol) < 0) {
                                    eBookingcargo.setEbook_cargovolume("");
                                } else {
                                    eBookingcargo.setEbook_cargovolume(cargoVol);
                                }
                                nettWt = checkNullDecimal((String) rs1.get("nettwt"));
                                nettWt = String.valueOf(new BigDecimal(nettWt).toPlainString());
                                nettWt = checkNullbigDecimal(nettWt);
                                if (nettWt == null || nettWt.equalsIgnoreCase("0")) {
                                    eBookingcargo.setEbook_netweight("");
                                } else {
                                    eBookingcargo.setEbook_netweight(nettWt);
                                }

                                eBookingcargo.setEbook_volumeunit(VolumeUnt);
                                cargoType = "";
                                //rs1 details
                                eBookingcargo.setEbook_commoditycode(checkNull((String) rs1.get("comdtygrp")));
                                eBookingcargo.setHscode(checkNull((String) rs1.get("commodityid")));
                                eBookingcargo.setEbook_eqp_qty(checkNull((String) rs1.get("itemno")));
                                eBookingcargo.setEbook_hazardous(checkNull((String) rs1.get("hazardous")));
                                eBookingcargo.setHzd(checkNull((String) rs1.get("hazardous")));
                                eBookingcargo.setEbook_packagecode(checkNull((String) rs1.get("packageid")));
                                eBookingcargo.setEbook_actCommoName(checkNull((String) rs1.get("marksdesc")));
                                eBookingcargo.setEbook_cmdtyhblno(checkNull((String) rs1.get("cmdty_hblno")));
                                eBookingcargo.setEbook_cmdtysellercode(checkNull((String) rs1.get("slcode")));
                                eBookingcargo.setEbook_cmdtysellerName(checkNull((String) rs1.get("slname")));
                                eBookingcargo.setEbook_cmdtybuyercode(checkNull((String) rs1.get("bycode")));
                                eBookingcargo.setEbook_cmdtybuyerName(checkNull((String) rs1.get("byname")));

                                //   System.out.println("[ECOM] EcomAmendmentBookingBean marksdesc--->" + (String) rs1.get("marksdesc") + "marksdescr--->" + (String) rs1.get("descr"));
                                String olfwd = checkNull((String) rs1.get("itemforward"));
                                String owlft = checkNull((String) rs1.get("itemleft"));
                                String oh = checkNull((String) rs1.get("itemtop"));
                                String olaft = checkNull((String) rs1.get("itemaft"));
                                String owrgt = checkNull((String) rs1.get("itemright"));
                                String dimenUnt = checkNull((String) rs1.get("itemlengthunit"));
                                if (checkNull((String) rs.get("oog")).equalsIgnoreCase("Y")) {
                                    if ("0.0".equalsIgnoreCase(olfwd)) {
                                        olfwd = "";
                                    }
                                    if ("0.0".equalsIgnoreCase(owlft)) {
                                        owlft = "";
                                    }
                                    if ("0.0".equalsIgnoreCase(oh)) {
                                        oh = "";
                                    }
                                    if ("0.0".equalsIgnoreCase(olaft)) {
                                        olaft = "";
                                    }
                                    if ("0.0".equalsIgnoreCase(owrgt)) {
                                        owrgt = "";
                                    }
                                }
                                eBookingcargo.setEbook_olforward(olfwd);
                                eBookingcargo.setEbook_owleft(owlft);
                                eBookingcargo.setEbook_oh(oh);
                                eBookingcargo.setEbook_olaft(olaft);
                                eBookingcargo.setEbook_owright(owrgt);
                                eBookingcargo.setEbook_dimensionunit(dimenUnt);
                                hazardous = checkNull((String) rs1.get("hazardous"));
                                isDuplicateCmdty = true;
                                //String strPattern = "^0+";
                                if (insmap != null) {
                                    eBookingcargo.setInsuredemail(checkNull((String) insmap.get("insuredemail")));
                                    if ((insmap.containsKey((eBookingcargo.getEbook_commoditycode())) || insmap.containsKey((eBookingcargo.getHscode())))) {//.replaceAll(strPattern, "")
                                        eBookingcargo.setIsinsuranceavailable("Y");
                                        qouteno = checkNull((String) insmap.get((eBookingcargo.getEbook_commoditycode())));
                                        if (qouteno.length() == 0) {
                                            qouteno = checkNull((String) insmap.get((eBookingcargo.getHscode())));
                                        }
                                        eBookingcargo.setQuoteno(qouteno);
                                    }
                                }
                            } else {
                                eBookingcargo.setEbook_packquantity("");
                                eBookingcargo.setContainerNo(eqpId);
                                eBookingcargo.setEbook_cargovolume("");
                                eBookingcargo.setEbook_netweight("");
                                eBookingcargo.setEbook_volumeunit("M");
                                eBookingcargo.setEbook_actCommoName("");
                                eBookingcargo.setEbook_cmdtyhblno("");
                                eBookingcargo.setEbook_cmdtysellercode("");
                                eBookingcargo.setEbook_cmdtysellerName("");
                                eBookingcargo.setEbook_cmdtybuyercode("");
                                eBookingcargo.setEbook_cmdtybuyerName("");
                                cargoType = "";
                                //rs1 details
                                eBookingcargo.setEbook_commoditycode("");
                                eBookingcargo.setHscode("");
                                eBookingcargo.setEbook_eqp_qty("");
                                eBookingcargo.setEbook_hazardous("N");
                                eBookingcargo.setHzd("N");
                                eBookingcargo.setEbook_packagecode("");
                                eBookingcargo.setEbook_olforward("");
                                eBookingcargo.setEbook_owleft("");
                                eBookingcargo.setEbook_oh("");
                                eBookingcargo.setEbook_olaft("");
                                eBookingcargo.setEbook_owright("");
                                eBookingcargo.setEbook_dimensionunit("M");
                                hazardous = checkNull("N");
                            }
                        }
                    }
                    // Reuse
                    System.out.println("[ECOM] EcomAmendmentBookingBean type:::>" + type);
                    if ("R".equalsIgnoreCase(type)) {
                        eqpdummyid = eqp_type;
                        if (htcargo.containsKey(eqpdummyid)) {
                            al = (ArrayList) htcargo.get(eqpdummyid);
                            al.add(j);
                        } else {
                            al = new ArrayList();
                            al.add(j);
                        }
                        htcargo.put(eqpdummyid, al);
                        //  eqp_result = eqpdummyid.substring(0, eqpdummyid.length() - 1);
                        //   System.out.println("[ECOM] EcomAmendmentBookingBean eqp_result::::::::::::::::::"+eqp_result);
                        eqpdummyid = eqpdummyid.concat("TNB");
                        eqpdummyid = eqpdummyid + al.size();
                        eBookingcargo.setContainerNo(eqpdummyid);
                        cargono.put(eqpId, eqpdummyid);
                        if (k == lst.size() - 1) {
                            eBookingcargo.setCargonomap(cargono);
                        }
                        eBookingcargo.setEbook_isdummy("Y");
                        System.out.println("[ECOM] EcomAmendmentBookingBean eqpdummyid:::>>" + eqpdummyid);
                    }
                    //
                    cargoList.add(eBookingcargo);
                    j++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cargoList;
    }

    @Override
    public ReturnVO saveAmendmentBooking(AmendmentBookingFormBean eBookingMasterData, ArrayList routingList, List<AmendmentBookingFormBean> cargoList, String client_code, FileDetailsVO fls, String custTypeConfig, String SaveType, String ApEmailID, String bkg_type, String custCountry, Map<String, List<AmendmentBookingFormBean>> CustomerMap) throws RemoteException {
        ReturnVO rvo = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String loginCustName = null;
        String agencycode = "", contactName = null, contactPhone = null, submittedBy = null;
        String book_refno = null;
        String ebook_status = "";
        String status = null, documentagencyid = null, agencyissuedid = null, polagencyid = null, custmapagency = null, bookingagencyid = null;
        String service = null, poleta = null, polterminal = null;
        String feeder = null, podterminal = null, poletd = null;
        String soc = null, cust_name = null, cust_id = null, cargovolume = null, netweight = null, haz = "N", ediid = null, readerid = null;
        String routeID = null, addremarks = null, mintemp = null, presetemp = null, temperunit = null, maxtemp = null;
        String BlpolTer = null, BlpodTer = null, Service = null, dgflag = "N", subject = null;
        String oceanVslCode = null, oceanVoyage = null, oceanSerCode = null, oceanBound = null, oceanSerCode_temp = null, deletedfile = null;
        Route eBookingrouting = null;
        boolean isconvertzip = false;
        String fileUploadPath = null;
        AmendmentBookingFormBean eBookingcargoData = null;
        BookingHaulagedetailsVo haulvo = null;
        List<BookingHaulagedetailsVo> haulagelist = eBookingMasterData.getHaulagelist();
        StringBuffer hssb = new StringBuffer();
        StringBuffer otherssb = new StringBuffer();
        StringBuffer dgsb = new StringBuffer();
        int version = 0;
        //eBookingMasterData.setAppVersion("2");
        System.out.println("[ECOM] EcomAmendmentBookingBean eBookingMasterData.getAppVersion()==> " + eBookingMasterData.getAppVersion());
        String loadType = null, dischType = null;
        MailFormatVO mailFormatObj = new MailFormatVO();
        String filepath = "";
        String amendBookNo = null;
        String lrpDraftBookingSave = "N", lrpBookNo = null, haulage_org = null, haulage_des = null;;
        int itemno = 1;
        boolean pluginBAG = false, pluginMAG = false;
        String multiAgencyid = null, shipment_type = null;;
        ArrayList<AmendmentBookingFormBean> buyer_CustList = null;
        ArrayList<AmendmentBookingFormBean> seller_CustList = null;
        try {
            shipment_type = eBookingMasterData.getShipment_type();
            System.out.println("[ECOM] EcomAmendmentBookingBean saveAmendmentBooking  shipment_type ==> " + shipment_type);
            Map pluginconfig = ecomvdsservice.getEcomBookingPluginConfig("BAG,MAG");
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
            System.out.println("[ECOM] EcomAmendmentBookingBean pluginBAG = > " + pluginBAG + " pluginMAG = > " + pluginMAG);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            //  System.out.println("[ECOM] EcomAmendmentBookingBean >>getEbook_routeid>> " + eBookingMasterData.getEbook_routeid()
            //          + " >>getEbook_templatename>> " + eBookingMasterData.getEbook_templatename());
            if (checkNull(eBookingMasterData.getEbook_templatename()).length() > 0) {
                System.out.println("[ECOM] EcomAmendmentBookingBean >>template name>>" + eBookingMasterData.getEbook_templatename());
            }
            //Update master details in ecom table
            if (eBookingMasterData != null) {
                rvo = new ReturnVO();
                status = eBookingMasterData.getSaveStatus();
                book_refno = eBookingMasterData.getEcomBookNo();
                loginCustName = eBookingMasterData.getLoginName();
                lrpBookNo = checkNull(eBookingMasterData.getLrpBookNo());
            }

            System.out.println("[ECOM] EcomAmendmentBookingBean >>Existing bookno>> " + book_refno + " status>> " + status + " lrpBookNo>> " + lrpBookNo
                    + " SaveType>> " + SaveType + " bkg_type>> " + bkg_type);
            //
            if ("D".equalsIgnoreCase(SaveType)) {
                ebook_status = "D";
                eBookingMasterData.setEbook_templatename("");
                book_refno = "";
                lrpBookNo = "";
            } else if ("S".equalsIgnoreCase(SaveType) && "R".equalsIgnoreCase(bkg_type)) {
                eBookingMasterData.setEbook_templatename("");
                ebook_status = "S";
                book_refno = "";
                lrpBookNo = "";
            } else if ("S".equalsIgnoreCase(SaveType)) {
                eBookingMasterData.setEbook_templatename("");
                ebook_status = "S";
            } else {
                ebook_status = "T";
                book_refno = "";
                lrpBookNo = "";
            }

            if (book_refno == null || book_refno.length() == 0) {
                lrpDraftBookingSave = "Y";
                book_refno = bookNoGenerate("BKG");
            }
            //update in userdetails if cc mail id newly added
            if ("YES".equalsIgnoreCase(eBookingMasterData.getIsCCMailId())) {
                ps = con.prepareStatement(SAVE_CCMAIN_USERDET);
                ps.setString(1, checkNull(eBookingMasterData.getCcMailId()));
                ps.setString(2, checkNull(loginCustName));
                ps.executeUpdate();
            }
            //
            System.out.println("[ECOM] EcomAmendmentBookingBean >>book_refno>>" + book_refno);
            //
            haulage_org = checkNull(eBookingMasterData.getPreCarriage());
            haulage_des = checkNull(eBookingMasterData.getOnCarriage());
            haulage_org = haulage_org != null && haulage_org.length() > 0 ? haulage_org : "N";
            haulage_des = haulage_des != null && haulage_des.length() > 0 ? haulage_des : "N";
            //
            if (book_refno != null && book_refno.length() > 0) {
                ps = con.prepareStatement(GET_ECOM_BKG_VERSION);
                ps.setString(1, book_refno);
                rs = ps.executeQuery();
                if (rs.next()) {
                    amendBookNo = checkNull(rs.getString("extbookno"));
                    if (rs.getString("version") == null) {
                        version = 0;
                    } else {
                        version = rs.getInt("version") + 1;
                    }
                } else {
                    version = 0;
                }
                //
                // get Ecom Customer mapping Agency
                custmapagency = checkNull(getEcomCustMappingAgency(eBookingMasterData.getCustCode()));
                if (custmapagency != null && custmapagency.length() > 0) { // Customer mapping agency
                    agencycode = custmapagency;
                } else { // multi agency logic
                    System.out.println("[ECOM] EcomAmendmentBookingBean AMEND eBookingMasterData.getEnableMultiAgency()==> " + eBookingMasterData.getEnableMultiAgency());
                    if (pluginBAG && pluginMAG && ("Y".equalsIgnoreCase(eBookingMasterData.getEnableMultiAgency()))) {
                        System.out.println("[ECOM] EcomAmendmentBookingBean routinglist=> " + routingList);
                        if (routingList != null && routingList.size() > 0) {
                            String oriportcode = null, servicecode = null, vesselcode = null, voyagecode = null, boundcode = null, pol_etd = null, ebookdate = null;
                            List<Map<String, String>> evaluateValueList = new ArrayList();
                            Map<String, String> resultData = new HashMap();
                            Map<String, String> valueMap = new HashMap();
                            for (int k = 0; k < routingList.size(); k++) {
                                eBookingrouting = (Route) routingList.get(k);
                                if (eBookingMasterData.getEbook_origin().equalsIgnoreCase(eBookingrouting.getLoadport())) {
                                    oriportcode = eBookingrouting.getLoadport().trim();
                                    pol_etd = eBookingMasterData.getEbook_pol_etd();
                                    ebookdate = eBookingMasterData.getEbook_date();
                                    //
                                }
                                System.out.println("[ECOM] EcomAmendmentBookingBean servicecode=========> " + checkNull(servicecode).length());
                                if (checkNull(servicecode).length() == 0) {
                                    System.out.println("[ECOM] EcomAmendmentBookingBean inside if=========> ");
                                    servicecode = eBookingrouting.getService();
                                    vesselcode = eBookingrouting.getVessel();
                                    voyagecode = eBookingrouting.getVoyage();
                                    boundcode = eBookingrouting.getBound();
                                }
                            }
                            System.out.println("[ECOM] EcomAmendmentBookingBean oriportcode=" + oriportcode + "&pol_etd=" + pol_etd + "&ebookdate" + ebookdate + "&servicecode=>" + servicecode + "&vesselcode=" + vesselcode + "&voyagecode=" + voyagecode + "&boundcode=" + boundcode);
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
                            System.out.println("[ECOM] EcomAmendmentBookingBean resultData===>  " + resultData);
                            if (resultData != null) {
                                System.out.println("[ECOM] EcomAmendmentBookingBean Route PLA===>" + eBookingMasterData.getEbook_origin());
                                if (eBookingMasterData.getEbook_origin() != null && resultData.containsKey(eBookingMasterData.getEbook_origin().trim())) {
                                    multiAgencyid = resultData.get(eBookingMasterData.getEbook_origin().trim());
                                }
                            }
                        }
                    }
                    System.out.println("[ECOM] EcomAmendmentBookingBean multiAgencyid ==> " + multiAgencyid + "preferred agency===>" + eBookingMasterData.getEbook_preferred_agency());
                    if (multiAgencyid != null && multiAgencyid.length() > 0) {
                        System.out.println("[ECOM] EcomAmendmentBookingBean inside multiAgencyid if => " + multiAgencyid);
                        agencycode = multiAgencyid;
                        documentagencyid = multiAgencyid;
                        agencyissuedid = multiAgencyid;
                    } else { // old agency logic
                        agencycode = getAgencydetails(eBookingMasterData.getEbook_origin());
                    }

                }
                bookingagencyid = agencycode;
                if (checkNull(eBookingMasterData.getEbook_preferred_agency()).length() > 0) {
                    bookingagencyid = checkNull(eBookingMasterData.getEbook_preferred_agency());
                }
                if (lrpBookNo != null && lrpBookNo.length() > 0) {
                    bookingagencyid = eBookingMasterData.getAgencyid();
                    documentagencyid = eBookingMasterData.getDocumentalagencyid();
                    agencyissuedid = eBookingMasterData.getAgencyissueid();
                } else {
                    documentagencyid = bookingagencyid;
                    agencyissuedid = bookingagencyid;
                }
                //
                if (!"FC".equalsIgnoreCase(shipment_type)) {
                    Map docagency = ecomvdsservice.getecombookingdocagency(bookingagencyid);
                    List doclis = (List) docagency.get("backofficemapping");
                    if (doclis != null && doclis.size() > 0) {
                        docagency = (Map) doclis.get(0);
                        documentagencyid = checkNull(docagency.get("backofficeagency").toString());
                        System.out.println("[ECOM] EcomAmendmentBookingBean documentagencyid -> backofficeagency -> " + documentagencyid);
                    }
                }
                polagencyid = agencycode;

              /*  if (checkNull(eBookingMasterData.getEbook_preferred_agency()).length() > 0) {
                    documentagencyid = checkNull(eBookingMasterData.getEbook_preferred_agency());
                    agencyissuedid = checkNull(eBookingMasterData.getEbook_preferred_agency());
                }*/

                System.out.println("[ECOM] EcomAmendmentBookingBean =>polagencyid" + polagencyid + " ==>agencycode " + bookingagencyid
                        + " >documentagencyid" + documentagencyid + " ==>agencyissuedid " + agencyissuedid);
                //
                if (lrpDraftBookingSave.equalsIgnoreCase("Y")) {
                    String SAVE_LRP_AMENDMENT_ECOM = "insert into ecombooking (ebookno, agencyid,"
                            + "originPortId,finalPortId, loadPortId, dischargePortId,bookDate,bookReady,preCarriage, onCarriage,remarks, "
                            + " usercr,datecr,ebook_status,cust_code,templatename,rateno, "
                            + " routeId,agencyRefNo,oceanfreight,trptsvccode,emptypickup,emptypickupdate,version,intransitport,submitted_date,"
                            + " dateup,userup,extbookno,nat_code,pickup_point,appversion,salesagreedrate,ccmailid,haulercode,haulagetype,pickupdate,dropdate,exportRef,releasetype,custPO,customerreference,ACID,cargoprotectid,ShipperReference,ensfiling,t2lfiling,sisubmitparty,enstype) "
                            + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,getdate(),?,?,?,?,?,?,?,?,?,?,?,?,getdate(),getdate(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    ps = con.prepareStatement(SAVE_LRP_AMENDMENT_ECOM);
                    ps.setString(1, book_refno);
                    ps.setString(2, checkNull(bookingagencyid));
                    ps.setString(3, eBookingMasterData.getEbook_origin());
                    ps.setString(4, eBookingMasterData.getEbook_delivery());
                    ps.setString(5, eBookingMasterData.getEbook_portofloading());
                    ps.setString(6, eBookingMasterData.getEbook_portofdischarge());
                    if (eBookingMasterData.getEbook_date().length() > 0) {
                        ps.setString(7, eBookingMasterData.getEbook_date());
                    } else {
                        ps.setString(7, null);
                    }
                    if (eBookingMasterData.getEbook_date().length() > 0) {
                        ps.setString(8, eBookingMasterData.getEbook_date());
                    } else {
                        ps.setString(8, null);
                    }
                    ps.setString(9, haulage_org);
                    ps.setString(10, haulage_des);
                    ps.setString(11, eBookingMasterData.getRemarks());
                    ps.setString(12, loginCustName);
                    ps.setString(13, ebook_status);
                    ps.setString(14, eBookingMasterData.getCustCode());
                    ps.setString(15, eBookingMasterData.getEbook_templatename());
                    ps.setString(16, eBookingMasterData.getEbook_rate_reference());
                    if (eBookingMasterData.getEbook_routeid() != null && eBookingMasterData.getEbook_routeid().length() > 0) {
                        ps.setString(17, eBookingMasterData.getEbook_routeid());
                    } else {
                        ps.setString(17, null);
                    }
                    ps.setString(18, checkNullUpperCase(eBookingMasterData.getEbook_agency_reference()));
                    ps.setString(19, eBookingMasterData.getEbook_ocean_freight());
                    ps.setString(20, eBookingMasterData.getCarrierContact());
                    ps.setString(21, eBookingMasterData.getEmptyPickup());
                    ps.setString(22, eBookingMasterData.getEmptyPickUpDate());
                    ps.setInt(23, version);
                    ps.setString(24, checkNull(eBookingMasterData.getPodintransit()).trim());
                    ps.setString(25, loginCustName);
                    ps.setString(26, checkNull(lrpBookNo));
                    ps.setString(27, checkNull(eBookingMasterData.getEbook_natcode()));
                    String pickuppoint = eBookingMasterData.getEmptyPickup() != null && eBookingMasterData.getEmptyPickup().length() > 0 ? eBookingMasterData.getEbook_pickup_point() : "";
                    ps.setString(28, pickuppoint);
                    if ("1".equalsIgnoreCase(eBookingMasterData.getAppVersion())) {
                        ps.setInt(29, 1);
                    } else {
                        ps.setInt(29, 2);
                    }
                    ps.setString(30, checkNull(eBookingMasterData.getSalesRateAgree()).trim());
                    ps.setString(31, checkNull(eBookingMasterData.getCcMailId()).trim());
                    ps.setString(32, checkNull(eBookingMasterData.getEbook_haulercode()).trim());
                    ps.setString(33, checkNull(eBookingMasterData.getEbook_haulagetype()));
                    if (eBookingMasterData.getEbook_pickup_date() != null && eBookingMasterData.getEbook_pickup_date().length() > 0) {
                        ps.setString(34, eBookingMasterData.getEbook_pickup_date());
                    } else {
                        ps.setString(34, null);
                    }
                    if (eBookingMasterData.getEbook_drop_date() != null && eBookingMasterData.getEbook_drop_date().length() > 0) {
                        ps.setString(35, eBookingMasterData.getEbook_drop_date());
                    } else {
                        ps.setString(35, null);
                    }
                    ps.setString(36, eBookingMasterData.getEbook_exportref());
                    ps.setString(37, eBookingMasterData.getEbook_reftype());
                    ps.setString(38, eBookingMasterData.getEbook_custpo());
                    ps.setString(39, eBookingMasterData.getCustomerreference());
                    ps.setString(40, eBookingMasterData.getACID());
                    ps.setString(41, eBookingMasterData.getCargoprotectid());
                    String dpwShipperRef = checkNull(eBookingMasterData.getShipperReferenceType())+checkNull(eBookingMasterData.getShipperReference());
                    if ("nullnull".equalsIgnoreCase(dpwShipperRef) || "ullnull".equalsIgnoreCase(checkNull(eBookingMasterData.getShipperReference()))) {
                        dpwShipperRef = "";
                    }
                    ps.setString(42, dpwShipperRef);
                    ps.setString(43, eBookingMasterData.getEnsfiling());
                    ps.setString(44, eBookingMasterData.getEbook_t2lfiling());
                    ps.setString(45, eBookingMasterData.getEbook_sisubmit_party());
                    ps.setString(46, eBookingMasterData.getEbook_EnsDocType());

                    ps.executeUpdate();

                } else {
                    System.out.println("[ECOM] EcomAmendmentBookingBean -----version--" + version);
                    String UPDATE_BOOKING_MASTER = "update ecombooking set agencyid=?,"
                            + "originPortId=?,finalPortId=?, loadPortId=?, dischargePortId=?,bookDate=?,bookReady=?,preCarriage=?, onCarriage=?,remarks=?, "
                            + " userup=?,dateup=getdate(),ebook_status=?,cust_code=?,templatename=?,rateno=?, "
                            + " routeId=?,agencyRefNo=?,oceanfreight=?,trptsvccode=?,emptypickup=?,emptypickupdate=?,"
                            + "intransitport=?,submitted_date=getdate(),version=?,isAmendment=?,extbookno=?,nat_code=?,"
                            + "pickup_point=?,appversion=?,salesagreedrate=?,ccmailid=?,haulercode=?,haulagetype=?,pickupdate=?,dropdate=?,exportRef=?,releasetype=?,custPO=?,customerreference=?,ACID=?,cargoprotectid=?,ShipperReference=?,ensfiling=?,t2lfiling=?,sisubmitparty=?,enstype=? where ebookno=?";
                    if (amendBookNo != null && amendBookNo.length() > 0) {
                        eBookingMasterData.setLrpBookNo(amendBookNo);
                    }
                    System.out.println("[ECOM] EcomAmendmentBookingBean >>amendBookNo>>" + amendBookNo);
                    ps = con.prepareStatement(UPDATE_BOOKING_MASTER);
                    soc = eBookingMasterData.getSoc();
                    //

                    ps.setString(1, checkNull(bookingagencyid));
                    ps.setString(2, eBookingMasterData.getEbook_origin());
                    ps.setString(3, eBookingMasterData.getEbook_delivery());
                    ps.setString(4, eBookingMasterData.getEbook_portofloading());
                    ps.setString(5, eBookingMasterData.getEbook_portofdischarge());
                    if (eBookingMasterData.getEbook_date().length() > 0) {
                        ps.setString(6, eBookingMasterData.getEbook_date());
                    } else {
                        ps.setString(6, null);
                    }
                    if (eBookingMasterData.getEbook_date().length() > 0) {
                        ps.setString(7, eBookingMasterData.getEbook_date());
                    } else {
                        ps.setString(7, null);
                    }
                    ps.setString(8, haulage_org);
                    ps.setString(9, haulage_des);
                    ps.setString(10, eBookingMasterData.getRemarks());
                    ps.setString(11, loginCustName);
                    ps.setString(12, "S");
                    ps.setString(13, eBookingMasterData.getCustCode());
                    ps.setString(14, eBookingMasterData.getEbook_templatename());
                    ps.setString(15, eBookingMasterData.getEbook_rate_reference());
                    if (eBookingMasterData.getEbook_routeid() != null && eBookingMasterData.getEbook_routeid().length() > 0) {
                        ps.setString(16, eBookingMasterData.getEbook_routeid());
                    } else {
                        ps.setString(16, null);
                    }
                    ps.setString(17, checkNullUpperCase(eBookingMasterData.getEbook_agency_reference()));
                    ps.setString(18, eBookingMasterData.getEbook_ocean_freight());
                    ps.setString(19, eBookingMasterData.getCarrierContact());
                    ps.setString(20, eBookingMasterData.getEmptyPickup());
                    ps.setString(21, eBookingMasterData.getEmptyPickUpDate());
                    ps.setString(22, checkNull(eBookingMasterData.getPodintransit()).trim());
                    ps.setInt(23, version);
                    ps.setString(24, "Y");
                    ps.setString(25, checkNull(eBookingMasterData.getLrpBookNo()));
                    ps.setString(26, checkNull(eBookingMasterData.getEbook_natcode()));
                    String pickuppoint = eBookingMasterData.getEmptyPickup() != null && eBookingMasterData.getEmptyPickup().length() > 0 ? eBookingMasterData.getEbook_pickup_point() : "";
                    ps.setString(27, pickuppoint);
                    if ("1".equalsIgnoreCase(eBookingMasterData.getAppVersion())) {
                        ps.setInt(28, 1);
                    } else {
                        ps.setInt(28, 2);
                    }
                    ps.setString(29, checkNull(eBookingMasterData.getSalesRateAgree()).trim());
                    ps.setString(30, checkNull(eBookingMasterData.getCcMailId()).trim());
                    ps.setString(31, checkNull(eBookingMasterData.getEbook_haulercode()).trim());
                    ps.setString(32, checkNull(eBookingMasterData.getEbook_haulagetype()));
                    if (eBookingMasterData.getEbook_pickup_date() != null && eBookingMasterData.getEbook_pickup_date().length() > 0) {
                        ps.setString(33, eBookingMasterData.getEbook_pickup_date());
                    } else {
                        ps.setString(33, null);
                    }
                    if (eBookingMasterData.getEbook_drop_date() != null && eBookingMasterData.getEbook_drop_date().length() > 0) {
                        ps.setString(34, eBookingMasterData.getEbook_drop_date());
                    } else {
                        ps.setString(34, null);
                    }

                    ps.setString(35, eBookingMasterData.getEbook_exportref());
                    ps.setString(36, eBookingMasterData.getEbook_reftype());
                    System.out.println("[ECOM] EcomAmendmentBookingBean aexpref" + eBookingMasterData.getEbook_exportref());

                    ps.setString(37, eBookingMasterData.getEbook_custpo());
                    System.out.println("[ECOM] EcomAmendmentBookingBean acustpovalue" + eBookingMasterData.getEbook_custpo());
                    ps.setString(38, eBookingMasterData.getCustomerreference());
                    ps.setString(39, eBookingMasterData.getACID());
                    ps.setString(40, eBookingMasterData.getCargoprotectid());
                    String dpwShipperRef = checkNull(eBookingMasterData.getShipperReferenceType())+checkNull(eBookingMasterData.getShipperReference());
                    if ("nullnull".equalsIgnoreCase(dpwShipperRef) || "ullnull".equalsIgnoreCase(checkNull(eBookingMasterData.getShipperReference()))) {
                        dpwShipperRef = "";
                    }
                    ps.setString(41, dpwShipperRef);
                    ps.setString(42, eBookingMasterData.getEnsfiling());
                    ps.setString(43, eBookingMasterData.getEbook_t2lfiling());
                    ps.setString(44, eBookingMasterData.getEbook_sisubmit_party());
                    ps.setString(45, eBookingMasterData.getEbook_EnsDocType());
                    ps.setString(46, book_refno);
                    int master_Update = ps.executeUpdate();

                }

                //Delete booking leg in ecom table
                ps = con.prepareStatement(DELETE_BOOKING_LEG);
                ps.setString(1, book_refno);
                ps.executeUpdate();

                //Insert booking leg details in ecom table
                ps = con.prepareStatement(INSERT_BKG_LEG);
                if (routingList != null && routingList.size() > 0) {
                    int len = routingList.size();
                    int leg_no = 1;
                    for (int k = 0; k < len; k++) {
                        eBookingrouting = (Route) routingList.get(k);
                        String pol = eBookingrouting.getLoadport();
                        String pod = eBookingrouting.getDisport();
                        String loadTerminal = eBookingrouting.getBlpolTer();
                        String dischargeTerminal = eBookingrouting.getBlpodTer();
                        String pol_str = eBookingrouting.getLoadtype();
                        String pod_str = eBookingrouting.getDisctype();
                        //    int leg_no = eBookingrouting.getLegNo();
                        String legTypeId = eBookingrouting.getLegType();
                        String servic = eBookingrouting.getService();
                        String vessel = eBookingrouting.getVessel();
                        String voyage = eBookingrouting.getVoyage();
                        String disbound = eBookingrouting.getDischargebound();

                        poleta = eBookingrouting.getETA1();
                        poletd = eBookingrouting.getETD1();
                        String loadcallid = eBookingrouting.getLoadcallid();
                        String dischargcallid = eBookingrouting.getDischargecallid();
                        String bound = eBookingrouting.getBound();
                        String grpid = eBookingrouting.getGrpCallId();
                        //   System.out.println("[ECOM] EcomAmendmentBookingBean >>poleta>>>in amendment>> " + poleta + " >>poletd>> " + poletd);
                        ps.setString(1, book_refno);
                        ps.setInt(2, leg_no);
                        ps.setString(3, bound);
                        ps.setString(4, servic);
                        ps.setString(5, vessel);
                        ps.setString(6, pol);
                        ps.setString(7, pod);
                        ps.setString(8, poletd);  //
                        ps.setString(9, poleta);   //
                        ps.setString(10, legTypeId);
                        ps.setString(11, pol_str);
                        ps.setString(12, pod_str);
                        ps.setString(13, voyage);
                        ps.setString(14, grpid);
                        ps.setString(15, loadTerminal);
                        ps.setString(16, dischargeTerminal);
                        ps.setString(17, loadcallid);
                        ps.setString(18, dischargcallid);
                        ps.setString(19, disbound);
                        ps.executeUpdate();
                        leg_no++;
                    }
                }

                //Delete cust details in ecom table
                ps = con.prepareStatement(DELETE_ECOM_CUST);
                ps.setString(1, book_refno);
                ps.executeUpdate();
                //State check
                eBookingMasterData.setEbook_bookingstate(getStateCode(eBookingMasterData.getEbook_bookingstate(), eBookingMasterData.getEbook_bookingcountry(), book_refno));
                eBookingMasterData.setEbook_agreestate(getStateCode(eBookingMasterData.getEbook_agreestate(), eBookingMasterData.getEbook_agreecountry(), book_refno));
                eBookingMasterData.setEbook_shipperstate(getStateCode(eBookingMasterData.getEbook_shipperstate(), eBookingMasterData.getEbook_shippercountry(), book_refno));
                eBookingMasterData.setEbook_consigneestate(getStateCode(eBookingMasterData.getEbook_consigneestate(), eBookingMasterData.getEbook_consigneecountry(), book_refno));
                eBookingMasterData.setEbook_notifystate(getStateCode(eBookingMasterData.getEbook_notifystate(), eBookingMasterData.getEbook_notifycountry(), book_refno));
                eBookingMasterData.setEbook_notifystate_n1(getStateCode(eBookingMasterData.getEbook_notifystate_n1(), eBookingMasterData.getEbook_notifycountry_n1(), book_refno));
                eBookingMasterData.setEbook_forwarderstate(getStateCode(eBookingMasterData.getEbook_forwarderstate(), eBookingMasterData.getEbook_forwardercountry(), book_refno));
                eBookingMasterData.setEbook_Warehousestate(getStateCode(eBookingMasterData.getEbook_Warehousestate(), eBookingMasterData.getEbook_Warehousecountry(), book_refno));
                eBookingMasterData.setEbook_NamedCustomerstate(getStateCode(eBookingMasterData.getEbook_NamedCustomerstate(), eBookingMasterData.getEbook_NamedCustomercountry(), book_refno));
                //
                //bp details insert
                if ("BP".equalsIgnoreCase(custTypeConfig)) {
                    cust_name = eBookingMasterData.getEbook_bookingname();

                    if (cust_name.length() > 0) {
                        ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                        ps.setString(1, book_refno);
                        ps.setString(2, "BP");
                        ps.setString(3, eBookingMasterData.getEbook_bookingcode());
                        ps.setString(4, eBookingMasterData.getEbook_bookingcountry());
                        ps.setString(5, eBookingMasterData.getEbook_bookingname());
                        ps.setString(6, eBookingMasterData.getEbook_bookingaddress());
                        ps.setString(7, eBookingMasterData.getEbook_bookingcity_desc());
                        ps.setString(8, eBookingMasterData.getEbook_bookingzipcode());
                        ps.setString(9, eBookingMasterData.getEbook_bookingphone());
                        ps.setString(10, eBookingMasterData.getEbook_bookingfax());
                        ps.setString(11, eBookingMasterData.getEbook_bookingemail());
                        ps.setString(12, eBookingMasterData.getEbook_bookingaddress1());
                        ps.setString(13, null);
                        ps.setString(14, null);
                        ps.setString(15, splitMasterCode(eBookingMasterData.getEbook_bookingstate()));
                        ps.setString(16, eBookingMasterData.getEbook_bookingeori());
                        ps.setString(17, eBookingMasterData.getBookingtypeperson());
                        ps.executeUpdate();
                    }
                } else {
                    ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                    ps.setString(1, book_refno);
                    ps.setString(2, "BP");
                    ps.setString(3, eBookingMasterData.getEbook_agreecode());
                    ps.setString(4, eBookingMasterData.getEbook_agreecountry());
                    ps.setString(5, eBookingMasterData.getEbook_agreename());
                    ps.setString(6, eBookingMasterData.getEbook_agreeaddress());
                    ps.setString(7, eBookingMasterData.getEbook_agreecity_desc());
                    ps.setString(8, eBookingMasterData.getEbook_agreezipcode());
                    ps.setString(9, eBookingMasterData.getEbook_agreephone());
                    ps.setString(10, eBookingMasterData.getEbook_agreefax());
                    ps.setString(11, eBookingMasterData.getEbook_agreeemail());
                    ps.setString(12, eBookingMasterData.getEbook_agreeaddress1());
                    ps.setString(13, null);
                    ps.setString(14, null);
                    ps.setString(15, splitMasterCode(eBookingMasterData.getEbook_agreestate()));
                    ps.setString(16, eBookingMasterData.getEbook_bookingeori());
                    ps.setString(17, eBookingMasterData.getBookingtypeperson());
                    ps.executeUpdate();
                }

                //Insert ap details in ecom table.
                cust_name = eBookingMasterData.getEbook_agreename();

                if (cust_name.length() > 0) {
                    ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                    ps.setString(1, book_refno);
                    ps.setString(2, "AP");
                    ps.setString(3, eBookingMasterData.getEbook_agreecode());
                    ps.setString(4, eBookingMasterData.getEbook_agreecountry());
                    ps.setString(5, eBookingMasterData.getEbook_agreename());
                    ps.setString(6, eBookingMasterData.getEbook_agreeaddress());
                    ps.setString(7, eBookingMasterData.getEbook_agreecity_desc());
                    ps.setString(8, eBookingMasterData.getEbook_agreezipcode());
                    ps.setString(9, eBookingMasterData.getEbook_agreephone());
                    ps.setString(10, eBookingMasterData.getEbook_agreefax());
                    ps.setString(11, eBookingMasterData.getEbook_agreeemail());
                    ps.setString(12, eBookingMasterData.getEbook_agreeaddress1());
                    ps.setString(13, null);
                    ps.setString(14, null);
                    ps.setString(15, splitMasterCode(eBookingMasterData.getEbook_agreestate()));
                    ps.setString(16, eBookingMasterData.getEbook_agreeeori());
                    ps.setString(17, eBookingMasterData.getAgreetypeperson());
                    ps.executeUpdate();
                }

                //Insert shipper customer details in ecom table.
                cust_name = checkNull(eBookingMasterData.getEbook_shippername());
                if (cust_name.length() > 0) {
                    ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                    ps.setString(1, book_refno);
                    ps.setString(2, "SH");
                    ps.setString(3, eBookingMasterData.getEbook_shippercode());
                    ps.setString(4, eBookingMasterData.getEbook_shippercountry());
                    ps.setString(5, eBookingMasterData.getEbook_shippername());
                    ps.setString(6, eBookingMasterData.getEbook_shipperaddress());
                    ps.setString(7, eBookingMasterData.getEbook_shippercity_desc());
                    ps.setString(8, eBookingMasterData.getEbook_shipperzipcode());
                    ps.setString(9, eBookingMasterData.getEbook_shipperphone());
                    ps.setString(10, eBookingMasterData.getEbook_shipperfax());
                    ps.setString(11, eBookingMasterData.getEbook_shipperemail());
                    ps.setString(12, eBookingMasterData.getEbook_shipperaddress1());
                    ps.setString(13, eBookingMasterData.getShipperCheck());
                    ps.setString(14, null);
                    ps.setString(15, splitMasterCode(eBookingMasterData.getEbook_shipperstate()));
                    ps.setString(16, eBookingMasterData.getEbook_shippereori());
                    ps.setString(17, eBookingMasterData.getShippertypeperson());
                    ps.executeUpdate();
                }

                //Insert consignee customer details in ecom table.
                cust_name = eBookingMasterData.getEbook_consigneename();
                if (cust_name.length() > 0) {
                    ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                    ps.setString(1, book_refno);
                    ps.setString(2, "CN");
                    ps.setString(3, eBookingMasterData.getEbook_consigneecode());
                    ps.setString(4, eBookingMasterData.getEbook_consigneecountry());
                    ps.setString(5, eBookingMasterData.getEbook_consigneename());
                    ps.setString(6, eBookingMasterData.getEbook_consigneeaddress());
                    ps.setString(7, eBookingMasterData.getEbook_consigneecity_desc());
                    ps.setString(8, eBookingMasterData.getEbook_consigneecode());
                    ps.setString(9, eBookingMasterData.getEbook_consigneephone());
                    ps.setString(10, eBookingMasterData.getEbook_consigneefax());
                    ps.setString(11, eBookingMasterData.getEbook_consigneeemail());
                    ps.setString(12, eBookingMasterData.getEbook_consigneeaddress1());
                    ps.setString(13, eBookingMasterData.getNotifyCheck());
                    ps.setString(14, eBookingMasterData.getHidCNToOrder());
                    ps.setString(15, splitMasterCode(eBookingMasterData.getEbook_consigneestate()));
                    ps.setString(16, eBookingMasterData.getEbook_consigneeeori());
                    ps.setString(17, eBookingMasterData.getConsigneetypeperson());
                    ps.executeUpdate();
                }

                //Insert Notify customer details in ecom table.
                cust_name = eBookingMasterData.getEbook_notifyname();
                if (cust_name.length() > 0) {
                    ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                    ps.setString(1, book_refno);
                    ps.setString(2, "NT");
                    ps.setString(3, eBookingMasterData.getEbook_notifycode());
                    ps.setString(4, eBookingMasterData.getEbook_notifycountry());
                    ps.setString(5, eBookingMasterData.getEbook_notifyname());
                    ps.setString(6, eBookingMasterData.getEbook_notifyaddress());
                    ps.setString(7, eBookingMasterData.getEbook_notifycity_desc());
                    ps.setString(8, eBookingMasterData.getEbook_notifyzipcode());
                    ps.setString(9, eBookingMasterData.getEbook_notifyphone());
                    ps.setString(10, eBookingMasterData.getEbook_notifyfax());
                    ps.setString(11, eBookingMasterData.getEbook_notifyemail());
                    ps.setString(12, eBookingMasterData.getEbook_notifyaddress1());
                    ps.setString(13, eBookingMasterData.getNotifyCheck());
                    ps.setString(14, null);
                    ps.setString(15, splitMasterCode(eBookingMasterData.getEbook_notifystate()));
                    ps.setString(16, eBookingMasterData.getEbook_notifyeori());
                    ps.setString(17, eBookingMasterData.getNotifytypeperson());
                    ps.executeUpdate();
                }

                //Insert Notify1 customer details in ecom table.
                cust_name = eBookingMasterData.getEbook_notifyname_n1();
                if (cust_name.length() > 0) {
                    ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                    ps.setString(1, book_refno);
                    ps.setString(2, "N1");
                    ps.setString(3, eBookingMasterData.getEbook_notifycode_n1());
                    ps.setString(4, eBookingMasterData.getEbook_notifycountry_n1());
                    ps.setString(5, eBookingMasterData.getEbook_notifyname_n1());
                    ps.setString(6, eBookingMasterData.getEbook_notifyaddress_n1());
                    ps.setString(7, eBookingMasterData.getEbook_notifycity_desc_n1());
                    ps.setString(8, eBookingMasterData.getEbook_notifyzipcode_n1());
                    ps.setString(9, eBookingMasterData.getEbook_notifyphone_n1());
                    ps.setString(10, eBookingMasterData.getEbook_notifyfax_n1());
                    ps.setString(11, eBookingMasterData.getEbook_notifyemail_n1());
                    ps.setString(12, eBookingMasterData.getEbook_notifyaddress1_n1());
                    ps.setString(13, eBookingMasterData.getNotify1Check());
                    ps.setString(14, null);
                    ps.setString(15, splitMasterCode(eBookingMasterData.getEbook_notifystate_n1()));
                    ps.setString(16, eBookingMasterData.getEbook_notifyeori_n1());
                    ps.setString(17, eBookingMasterData.getNotifytypeperson_n1());
                    ps.executeUpdate();
                }
                //FW
                cust_name = eBookingMasterData.getEbook_forwardername();

                if (cust_name.length() > 0) {
                    System.out.println("[ECOM] EcomAmendmentBookingBean >>>>eBookingMasterData.getEbook_forwardercode()>>>" + eBookingMasterData.getEbook_forwardercode());
                    ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                    ps.setString(1, book_refno);
                    ps.setString(2, "FW");
                    ps.setString(3, eBookingMasterData.getEbook_forwardercode());
                    ps.setString(4, eBookingMasterData.getEbook_forwardercountry());
                    ps.setString(5, eBookingMasterData.getEbook_forwardername());
                    ps.setString(6, eBookingMasterData.getEbook_forwarderaddress());
                    ps.setString(7, eBookingMasterData.getEbook_forwardercity_desc());
                    ps.setString(8, eBookingMasterData.getEbook_forwarderzipcode());
                    ps.setString(9, eBookingMasterData.getEbook_forwarderphone());
                    ps.setString(10, eBookingMasterData.getEbook_forwarderfax());
                    ps.setString(11, eBookingMasterData.getEbook_forwarderemail());
                    ps.setString(12, eBookingMasterData.getEbook_forwarderaddress1());
                    ps.setString(13, null);
                    ps.setString(14, null);
                    ps.setString(15, splitMasterCode(eBookingMasterData.getEbook_forwarderstate()));
                    ps.setString(16, eBookingMasterData.getEbook_forwardereori());
                    ps.setString(17, eBookingMasterData.getForwardertypeperson());
                    ps.executeUpdate();
                }
                //WH
                cust_name = eBookingMasterData.getEbook_Warehousename();

                if (cust_name.length() > 0) {
                    System.out.println("[ECOM] EcomAmendmentBookingBean >>>>eBookingMasterData.getEbook_Warehousecode()>>>" + eBookingMasterData.getEbook_Warehousecode());
                    ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                    ps.setString(1, book_refno);
                    ps.setString(2, "WH");
                    ps.setString(3, eBookingMasterData.getEbook_Warehousecode());
                    ps.setString(4, eBookingMasterData.getEbook_Warehousecountry());
                    ps.setString(5, eBookingMasterData.getEbook_Warehousename());
                    ps.setString(6, eBookingMasterData.getEbook_Warehouseaddress());
                    ps.setString(7, eBookingMasterData.getEbook_Warehousecity_desc());
                    ps.setString(8, eBookingMasterData.getEbook_Warehousezipcode());
                    ps.setString(9, eBookingMasterData.getEbook_Warehousephone());
                    ps.setString(10, eBookingMasterData.getEbook_Warehousefax());
                    ps.setString(11, eBookingMasterData.getEbook_Warehouseemail());
                    ps.setString(12, eBookingMasterData.getEbook_Warehouseaddress1());
                    ps.setString(13, null);
                    ps.setString(14, null);
                    ps.setString(15, splitMasterCode(eBookingMasterData.getEbook_Warehousestate()));
                    ps.setString(16, eBookingMasterData.getEbook_Warehouseeori());
                    ps.setString(17, eBookingMasterData.getWarehousetypeperson());

                    ps.executeUpdate();
                }
                //NC
                cust_name = eBookingMasterData.getEbook_NamedCustomername();
                if (cust_name.length() > 0) {
                    System.out.println("[ECOM] EcomAmendmentBookingBean >>>>eBookingMasterData.getEbook_NamedCustomercode()>>>" + eBookingMasterData.getEbook_NamedCustomercode());
                    ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                    ps.setString(1, book_refno);
                    ps.setString(2, "NC");
                    ps.setString(3, eBookingMasterData.getEbook_NamedCustomercode());
                    ps.setString(4, eBookingMasterData.getEbook_NamedCustomercountry());
                    ps.setString(5, eBookingMasterData.getEbook_NamedCustomername());
                    ps.setString(6, eBookingMasterData.getEbook_NamedCustomeraddress());
                    ps.setString(7, eBookingMasterData.getEbook_NamedCustomercity_desc());
                    ps.setString(8, eBookingMasterData.getEbook_NamedCustomerzipcode());
                    ps.setString(9, eBookingMasterData.getEbook_NamedCustomerphone());
                    ps.setString(10, eBookingMasterData.getEbook_NamedCustomerfax());
                    ps.setString(11, eBookingMasterData.getEbook_NamedCustomeremail());
                    ps.setString(12, eBookingMasterData.getEbook_NamedCustomeraddress1());
                    ps.setString(13, null);
                    ps.setString(14, null);
                    ps.setString(15, splitMasterCode(eBookingMasterData.getEbook_NamedCustomerstate()));
                    ps.setString(16, eBookingMasterData.getEbook_NamedCustomereori());
                    ps.setString(17, eBookingMasterData.getEbook_NamedCustomerpersontype());
                    ps.executeUpdate();
                }
                cust_name = checkNull(eBookingMasterData.getEbook_Sellername());
                if (cust_name.length() > 0) {
                    System.out.println("[ECOM] EcomAmendmentBookingBean >>>>eBookingMasterData.getEbook_Sellercode()>>>" + eBookingMasterData.getEbook_Sellercode());
                    ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                    ps.setString(1, book_refno);
                    ps.setString(2, "SL");
                    ps.setString(3, eBookingMasterData.getEbook_Sellercode());
                    ps.setString(4, eBookingMasterData.getEbook_Sellercountry());
                    ps.setString(5, eBookingMasterData.getEbook_Sellername());
                    ps.setString(6, eBookingMasterData.getEbook_Selleraddress());
                    ps.setString(7, eBookingMasterData.getEbook_Sellercity_desc());
                    ps.setString(8, eBookingMasterData.getEbook_Sellerzipcode());
                    ps.setString(9, eBookingMasterData.getEbook_Sellerphone());
                    ps.setString(10, eBookingMasterData.getEbook_Sellerfax());
                    ps.setString(11, eBookingMasterData.getEbook_Selleremail());
                    ps.setString(12, eBookingMasterData.getEbook_Selleraddress1());
                    ps.setString(13, null);
                    ps.setString(14, null);
                    ps.setString(15, splitMasterCode(eBookingMasterData.getEbook_Sellerstate()));
                    ps.setString(16, eBookingMasterData.getEbook_Sellereori());
                    ps.setString(17, eBookingMasterData.getEbook_Sellerpersontype());
                    ps.executeUpdate();
                }
                cust_name = checkNull(eBookingMasterData.getEbook_Buyername());
                if (cust_name.length() > 0) {
                    System.out.println("[ECOM] EcomAmendmentBookingBean >>>>eBookingMasterData.getEbook_Buyercode()>>>" + eBookingMasterData.getEbook_Buyercode());
                    ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                    ps.setString(1, book_refno);
                    ps.setString(2, "BY");
                    ps.setString(3, eBookingMasterData.getEbook_Buyercode());
                    ps.setString(4, eBookingMasterData.getEbook_Buyercountry());
                    ps.setString(5, eBookingMasterData.getEbook_Buyername());
                    ps.setString(6, eBookingMasterData.getEbook_Buyeraddress());
                    ps.setString(7, eBookingMasterData.getEbook_Buyercity_desc());
                    ps.setString(8, eBookingMasterData.getEbook_Buyerzipcode());
                    ps.setString(9, eBookingMasterData.getEbook_Buyerphone());
                    ps.setString(10, eBookingMasterData.getEbook_Buyerfax());
                    ps.setString(11, eBookingMasterData.getEbook_Buyeremail());
                    ps.setString(12, eBookingMasterData.getEbook_Buyeraddress1());
                    ps.setString(13, null);
                    ps.setString(14, null);
                    ps.setString(15, splitMasterCode(eBookingMasterData.getEbook_Buyerstate()));
                    ps.setString(16, eBookingMasterData.getEbook_Buyereori());
                    ps.setString(17, eBookingMasterData.getEbook_Buyerpersontype());
                    ps.executeUpdate();
                }
                saveCustDetails(CustomerMap, book_refno);
                // Delete cargo and commodity details in ecom table
                ps = con.prepareStatement(DELETE_BKG_CARGO);
                ps.setString(1, book_refno);
                ps.executeUpdate();

                ps = con.prepareStatement(DELETE_BKG_COMMODITY);
                ps.setString(1, book_refno);
                ps.executeUpdate();

                ps = con.prepareStatement(DELETE_BKG_DG);
                ps.setString(1, book_refno);
                ps.executeUpdate();

                ps = con.prepareStatement(DELETE_BKG_OOG);
                ps.setString(1, book_refno);
                ps.executeUpdate();
                ps = con.prepareStatement(DELETE_BKG_REFER);
                ps.setString(1, book_refno);
                ps.executeUpdate();

                ps = con.prepareStatement(DELETE_ECOM_BKG_HAULAGE);
                ps.setString(1, book_refno);
                ps.executeUpdate();

                // insert cargo and commodity details in ecom table
                System.out.println("[ECOM] EcomAmendmentBookingBean >>cargoList>>" + cargoList);
                if (cargoList != null) {
                    int len = cargoList.size();
                    itemno = 1;
                    ps = con.prepareStatement(SAVE_EBOOKING_CONT_QRY_NEW);
                    for (int i = 0; i < len; i++) {
                        eBookingcargoData = (AmendmentBookingFormBean) cargoList.get(i);
                        if (eBookingcargoData != null) {
                            haz = "N";
                            String hazflag = eBookingcargoData.getHzd();
                            if ("Y".equalsIgnoreCase(checkNull(hazflag))) {
                                haz = "Y";
                            }
                            //      System.out.println("[ECOM] EcomAmendmentBookingBean >>getContainerNo" + eBookingcargoData.getContainerNo());
                            ps.setString(1, book_refno);
                            ps.setInt(2, itemno);
                            ps.setString(3, eBookingcargoData.getEbook_eqp_type());
                            ps.setString(4, eBookingcargoData.getEbook_eqp());
                            ps.setString(5, eBookingcargoData.getEbook_eqp_qty());
                            ps.setString(6, eBookingcargoData.getSoc());
                            ps.setString(7, checkNull(eBookingcargoData.getEbook_nor()));
                            ps.setString(8, haz);
                            ps.setString(9, ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_tareweight())));
                            ps.setString(10, ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_grossweight())));
                            ps.setString(11, ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_cargoweight())));
                            ps.setString(12, checkNull(eBookingcargoData.getEbook_weightunit()));
                            ps.setString(13, checkNull(eBookingcargoData.getContainerNo()));
                            ps.setString(14, checkNull(eBookingcargoData.getEqpstatus()));
                            ps.setString(15, checkNull(eBookingcargoData.getOog()));
                            ps.addBatch();
                        }
                        itemno = itemno + 1;
                    }
                    int[] Cargo_update = ps.executeBatch();
                    //
                    itemno = 1;
                    ps = con.prepareStatement(SAVE_EBOOKING_COMMODITY_QRY_NEW);
                    for (int i = 0; i < len; i++) {
                        eBookingcargoData = (AmendmentBookingFormBean) cargoList.get(i);
                        if (eBookingcargoData != null) {
                            ps.setString(1, book_refno);
                            ps.setInt(2, itemno);
                            ps.setInt(3, itemno);
                            ps.setString(4, checkNull(eBookingcargoData.getCommodityothers()));
                            ps.setString(5, checkNull(eBookingcargoData.getEbook_commoditycode()));
                            ps.setString(6, checkNull(eBookingcargoData.getHscode()));
                            ps.setString(7, checkNull(eBookingcargoData.getEbook_packagecode()));
                            ps.setString(8, checkNull(eBookingcargoData.getEbook_packquantity()));
                            ps.setString(9, checkNull(eBookingcargoData.getEbook_cargovolume()));
                            ps.setString(10, checkNull(eBookingcargoData.getEbook_volumeunit()));
                            ps.setString(11, checkNull(eBookingcargoData.getEbook_netweight()));
                            ps.setString(12, checkNull(eBookingcargoData.getEbook_actCommoName()));
                            ps.setString(13, checkNull(eBookingcargoData.getEbook_cmdtybuyercode()));
                            ps.setString(14, checkNull(eBookingcargoData.getEbook_cmdtybuyerName()));
                            ps.setString(15, checkNull(eBookingcargoData.getEbook_cmdtysellercode()));
                            ps.setString(16, checkNull(eBookingcargoData.getEbook_cmdtysellerName()));
                            ps.setString(17, checkNull(eBookingcargoData.getEbook_cmdtyhblno()));
                            ps.addBatch();
                        }
                        itemno = itemno + 1;
                    }
                    if (itemno > 0) {
                        int[] comm_update = ps.executeBatch();
                    }
                    //
                    // Unified DG Save - Multiple DG Details Support
                    itemno = 1;
                    int conid = 1;
                    ps = con.prepareStatement(SAVE_EBOOKING_DG_QRY_NEW);
                    for (int i = 0; i < len; i++) {
                        eBookingcargoData = (AmendmentBookingFormBean) cargoList.get(i);
                        if (eBookingcargoData != null && "Y".equalsIgnoreCase(eBookingcargoData.getHzd())) {
                            // Unified DG Save - Always use dgDetailsList (works for single & multiple)
                            List<AmendmentBookingFormBean> dgList = eBookingcargoData.getDgDetailsList();
                            
                            if (dgList != null && !dgList.isEmpty()) {
                                // Insert all DG entries from list
                                for (AmendmentBookingFormBean dgEntry : dgList) {
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
                                        System.out.println("[ECOM] EcomAmendmentBookingBean Error parsing comid: " + e.getMessage());
                                    }
                                    
                                    // Split UN number (format: "1100~~ALCOHOL" or "1100 ~~ ALCOHOL")
                                    String unno[] = checkNull(dgEntry.getEbook_unno()).split("~~");
                                    
                                    ps.setString(1, book_refno);
                                    ps.setInt(2, conid);
                                    ps.setInt(3, dgItemNo); // itemno from dgEntry
                                    ps.setString(4, checkNull(dgEntry.getEbook_poluttunit()));
                                    ps.setString(5, checkNull(unno[0]).trim());
                                    ps.setString(6, checkNull(dgEntry.getEbook_dgclass()));
                                    ps.setString(7, flashpoint);
                                    ps.setString(8, flashunit);
                                    ps.addBatch();
                                    System.out.println("[ECOM] EcomAmendmentBookingBean DG Save: conid=" + conid + ", itemno=" + dgItemNo + ", unNo=" + unno[0]);
                                }
                                conid = conid + 1;
                            }
                            itemno = itemno + 1;
                        } else {
                            itemno = itemno + 1;
                        }
                    }
                    if (itemno > 1) {
                        int[] dg_update = ps.executeBatch();
                    }
                    //
                    //
                    itemno = 1;
                    conid = 1;
                    ps = con.prepareStatement(SAVE_EBOOKING_OOG_QRY_NEW);
                    for (int i = 0; i < len; i++) {
                        eBookingcargoData = (AmendmentBookingFormBean) cargoList.get(i);
                        if (eBookingcargoData != null && "O".equalsIgnoreCase(eBookingcargoData.getEbook_eqp_type())) {
                            ps.setString(1, book_refno);
                            ps.setInt(2, conid);
                            ps.setString(3, checkNull(eBookingcargoData.getEbook_owleft()));
                            ps.setString(4, checkNull(eBookingcargoData.getEbook_owright()));
                            ps.setString(5, checkNull(eBookingcargoData.getEbook_olforward()));
                            ps.setString(6, checkNull(eBookingcargoData.getEbook_olaft()));
                            ps.setString(7, checkNull(eBookingcargoData.getEbook_oh()));
                            ps.setString(8, checkNull(eBookingcargoData.getEbook_dimensionunit()));
                            ps.setInt(9, itemno);
                            ps.addBatch();
                            conid = conid + 1;
                            itemno = itemno + 1;
                        } else {
                            itemno = itemno + 1;
                        }
                    }
                    if (itemno > 1) {
                        int[] oog_update = ps.executeBatch();
                    }
                    //
                    //
                    itemno = 1;
                    conid = 1;
                    ps = con.prepareStatement(SAVE_EBOOKING_REFER_QRY_NEW);
                    for (int i = 0; i < len; i++) {
                        eBookingcargoData = (AmendmentBookingFormBean) cargoList.get(i);
                        if (eBookingcargoData != null && "R".equalsIgnoreCase(eBookingcargoData.getEbook_eqp_type())) {
                            mintemp = checkNull(eBookingcargoData.getEbook_mintemp());
                            maxtemp = checkNull(eBookingcargoData.getEbook_maxtemp());
                            presetemp = checkNull(eBookingcargoData.getEbook_presetemp());
                            mintemp = mintemp.length() > 0 ? mintemp : null;
                            maxtemp = maxtemp.length() > 0 ? maxtemp : null;
                            presetemp = presetemp.length() > 0 ? presetemp : null;

                            ps.setString(1, book_refno);
                            ps.setInt(2, conid);
                            ps.setString(3, presetemp);
                            ps.setString(4, mintemp);
                            ps.setString(5, maxtemp);
                            ps.setString(6, checkNull(eBookingcargoData.getEbook_temperunit()));
                            ps.setString(7, checkNull((eBookingcargoData.getHumidityflag() != null && "Y".equalsIgnoreCase(eBookingcargoData.getHumidityflag()) ? eBookingcargoData.getHumidity() : "")));
                            ps.setString(8, checkNull((eBookingcargoData.getHumidityflag() != null && "Y".equalsIgnoreCase(eBookingcargoData.getHumidityflag()) ? "1" : "0")));
                            ps.setString(9, checkNull(eBookingcargoData.getDrains()));
                            ps.setString(10, checkNull(eBookingcargoData.getCo2()));
                            ps.setString(11, checkNull(eBookingcargoData.getO2()));
                            ps.setInt(12, itemno);
                            ps.setString(13, checkNull(eBookingcargoData.getVentilation()));
                            ps.addBatch();
                            conid = conid + 1;
                            itemno = itemno + 1;
                        } else {
                            itemno = itemno + 1;
                        }
                    }
                    if (itemno > 1) {
                        int[] refer_update = ps.executeBatch();
                    }
                } // end cargo

                // insert  haulage
                if (haulagelist != null && haulagelist.size() > 0) {
                    ps = con.prepareStatement(SAVE_ECOM_BKG_HAULAGE);
                    for (int i = 0; i < haulagelist.size(); i++) {
                        haulvo = (BookingHaulagedetailsVo) haulagelist.get(i);
                        ps.setString(1, book_refno);
                        ps.setInt(2, haulvo.getEqprowid());
                        ps.setString(3, checkNull(haulvo.getEqptype()));
                        ps.setString(4, checkNull(haulvo.getPickloccode()));
                        ps.setString(5, checkNull(haulvo.getPicklocname()));
                        ps.setString(6, checkNull(haulvo.getContactperson()));
                        ps.setString(7, checkNull(haulvo.getAddress()));
                        ps.setString(8, checkNull(haulvo.getContactphone()));
                        ps.setString(9, checkNull(haulvo.getContactemail()));
                        ps.setString(10, checkNull(haulvo.getRefno()));
                        ps.setString(11, checkNull(haulvo.getDropdate()));
                        ps.setString(12, checkNull(haulvo.getRemarks()));
                        ps.setString(13, checkNull(haulvo.getTwrsdoctype()));
                        ps.setString(14, checkNull(haulvo.getTwrsoverweight()));
                        ps.setInt(15, haulvo.getStopsequence());
                        ps.setString(16, null);
                        ps.setString(17, checkNull(haulvo.getEqpid()));
                        ps.addBatch();
                        itemno = itemno + 1;
                    }
                    if (itemno > 1) {
                        int[] refer_update = ps.executeBatch();
                    }

                }
                ps = con.prepareStatement(DELETE_ECOMBOOKNG_CONF);
                ps.setString(1, book_refno);
                ps.executeUpdate();

                if (checkIsEmpty(eBookingMasterData.getBkgcutoff()) || checkIsEmpty(eBookingMasterData.getDoccutoff())
                        || checkIsEmpty(eBookingMasterData.getVgmcutoff()) || checkIsEmpty(eBookingMasterData.getBbrorocutoff())
                        || checkIsEmpty(eBookingMasterData.getHazcutoff()) || checkIsEmpty(eBookingMasterData.getPortentrycutoff())) {
                    ps = con.prepareStatement(INSERT_ECOMBOOKNG_CONF);
                    ps.setString(1, book_refno);
                    ps.setString(2, eBookingMasterData.getBkgcutoff());
                    ps.setString(3, eBookingMasterData.getDoccutoff());
                    ps.setString(4, eBookingMasterData.getVgmcutoff());
                    ps.setString(5, eBookingMasterData.getBbrorocutoff());
                    ps.setString(6, eBookingMasterData.getHazcutoff());
                    ps.setString(7, eBookingMasterData.getPortentrycutoff());
                    ps.executeUpdate();

                }
                //
                // ENS Filling  
                ps = con.prepareStatement(DELETE_BOOKING_ENSDEC);
                ps.setString(1, book_refno);
                ps.executeUpdate();

                ENSDeclarantVo ensDec = eBookingMasterData.getEnsdecvo();
                if (ensDec != null && checkNull(ensDec.getEnsFillingType()).length() > 0) {
                    ps = con.prepareStatement(INSERT_BOOKING_ENSDEC);
                    ps.setString(1, book_refno);
                    ps.setString(2, ensDec.getEuCustZone());
                    ps.setString(3, ensDec.getBlTypeENS());
                    ps.setString(4, ensDec.getEnsFillingType());
                    ps.setString(5, ensDec.getPaymentMethod());
                    ps.setString(6, ensDec.getDeclarantName());
                    ps.setString(7, ensDec.getDeclarantAddress());
                    ps.setString(8, ensDec.getDeclarantAddress1());
                    ps.setString(9, ensDec.getDeclarantCountry());
                    ps.setString(10, ensDec.getDeclarantState());
                    ps.setString(11, ensDec.getDeclarantTelephone());
                    ps.setString(12, ensDec.getDeclarantEmail());
                    ps.setString(13, ensDec.getDeclarantEORI());
                    ps.setString(14, ensDec.getDeclarantFillingType());
                    ps.setString(15, ensDec.getDeclarantCity());
                    ps.setString(16, ensDec.getDeclarantZipcode());
                    ps.executeUpdate();
                }

                ps = con.prepareStatement(DELETE_FILES);
                ps.setString(1, book_refno);
                ps.executeUpdate();

                //File concept
                FileOutputStream fos = null;
                boolean flag = false;
                ZipFileConversion zipconv = new ZipFileConversion();
                HashMap<String, byte[]> filehm = new HashMap<String, byte[]>();
                String zipFileName = "";
                isconvertzip = ("Y".equalsIgnoreCase(eBookingMasterData.getEnableZipConversion()));
                System.out.println("[ECOM] EcomAmendmentBookingBean isconvertzip=> " + isconvertzip + "  getEnableZipConversion=> " + eBookingMasterData.getEnableZipConversion());

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
                System.out.println("[ECOM] EcomAmendmentBookingBean fls.isFileAvailable()===> " + fls.isFileAvailable() + " fls.getFilePath()===> " + fls.getFilePath());
                if (fls.isFileAvailable()) {
                    File directory = new File(fls.getFilePath());
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                }
                deletedfile = eBookingMasterData.getDeletedfilename();
                System.out.println("[ECOM] EcomAmendmentBookingBean deletedfile==> " + deletedfile);
                if (deletedfile != null && deletedfile.length() > 0) {
                    deletedfile = deletedfile.substring(2, deletedfile.length());
                    ArrayList fileset = new ArrayList(Arrays.asList(deletedfile.split("~~")));
                    for (int i = 0; i < fileset.size(); i++) {
                        //  System.out.println("[ECOM] EcomAmendmentBookingBean checkNull((String)iterator.next())==> " + checkNull((String) fileset.get(i)));
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
                ps = con.prepareStatement(SAVE_EBOOKING_FILE);
                if (fls.getVgmFileName() != null && fls.getVgmFileName().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename1);
                    ps.setString(3, EcomConsts.FILETYPE_VMG);
                    ps.addBatch();
                    filepath += filename1 + "~~~";
                }
                if (fls.getVgmFileName1() != null && fls.getVgmFileName1().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename6);
                    ps.setString(3, EcomConsts.FILETYPE_VMG + "1");
                    ps.addBatch();
                    filepath += filename6 + "~~~";
                }
                if (fls.getVgmFileName2() != null && fls.getVgmFileName2().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename7);
                    ps.setString(3, EcomConsts.FILETYPE_VMG + "2");
                    ps.addBatch();
                    filepath += filename7 + "~~~";
                }
                //
                if (fls.getDgFileName() != null && fls.getDgFileName().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename2);
                    ps.setString(3, EcomConsts.FILETYPE_DGC);
                    ps.addBatch();
                    filepath += filename2 + "~~~";
                }
                if (fls.getDgFileName1() != null && fls.getDgFileName1().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename8);
                    ps.setString(3, EcomConsts.FILETYPE_DGC + "1");
                    ps.addBatch();
                    filepath += filename8 + "~~~";
                }
                if (fls.getDgFileName2() != null && fls.getDgFileName2().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename9);
                    ps.setString(3, EcomConsts.FILETYPE_DGC + "2");
                    ps.addBatch();
                    filepath += filename9 + "~~~";
                }
                //
                if (fls.getLolFileName() != null && fls.getLolFileName().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename3);
                    ps.setString(3, EcomConsts.FILETYPE_LOL);
                    ps.addBatch();
                    filepath += filename3 + "~~~";
                }
                if (fls.getLolFileName1() != null && fls.getLolFileName1().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename10);
                    ps.setString(3, EcomConsts.FILETYPE_LOL + "1");
                    ps.addBatch();
                    filepath += filename10 + "~~~";
                }
                if (fls.getLolFileName2() != null && fls.getLolFileName2().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename11);
                    ps.setString(3, EcomConsts.FILETYPE_LOL + "2");
                    ps.addBatch();
                    filepath += filename11 + "~~~";
                }
                //
                if (fls.getMsdsFileName() != null && fls.getMsdsFileName().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename4);
                    ps.setString(3, EcomConsts.FILETYPE_MSD);
                    ps.addBatch();
                    filepath += filename4 + "~~~";
                }
                if (fls.getMsdsFileName1() != null && fls.getMsdsFileName1().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename12);
                    ps.setString(3, EcomConsts.FILETYPE_MSD + "1");
                    ps.addBatch();
                    filepath += filename12 + "~~~";
                }
                if (fls.getMsdsFileName2() != null && fls.getMsdsFileName2().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename13);
                    ps.setString(3, EcomConsts.FILETYPE_MSD + "2");
                    ps.addBatch();
                    filepath += filename13 + "~~~";
                }
                //
                if (fls.getOthersFileName() != null && fls.getOthersFileName().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename5);
                    ps.setString(3, EcomConsts.FILETYPE_OTHERS);
                    ps.addBatch();
                    filepath += filename5 + "~~~";
                }
                if (fls.getOthersFileName1() != null && fls.getOthersFileName1().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename14);
                    ps.setString(3, EcomConsts.FILETYPE_OTHERS + "1");
                    ps.addBatch();
                    filepath += filename14 + "~~~";
                }
                if (fls.getOthersFileName2() != null && fls.getOthersFileName2().length() > 0) {
                    ps.setString(1, book_refno);
                    ps.setString(2, filename15);
                    ps.setString(3, EcomConsts.FILETYPE_OTHERS + "2");
                    ps.addBatch();
                    filepath += filename15 + "~~~";
                }
                if (isconvertzip) {
                    if (zipFileName != null && zipFileName.length() > 3) {
                        filepath = zipFileName.substring(0, zipFileName.length() - 3);
                    }
                }
                // pstmt.executeBatch();
                //
            }
            System.out.println("[ECOM] EcomAmendmentBookingBean >>filepath>>112233>amend>" + filepath);
            if ("S".equalsIgnoreCase(SaveType)) {
                //  Insert booking values to lrp gate way
                System.out.println("[ECOM] EcomAmendmentBookingBean >>eBookingcargoData.getAppVersion()>>" + eBookingMasterData.getAppVersion());
                if ("1".equalsIgnoreCase(eBookingMasterData.getAppVersion())) {
                    eBookingMasterData.setEcomBookNo(book_refno);
                    rvo = vAmend1BKGBeanObj.saveAmendmentBooking(eBookingMasterData, routingList, cargoList, client_code);
                } else {
                    readerid = bookNoGenerate("EDI");
                    ArrayList lst = null;
                    EcomBKGEDIDocStatus doc = null;
                    EcomCommonModel cm = null;
                    Map hs = null;
                    EcomBKGReaderMaster edirm = null;
                    EcomBKGRemarks rm = null;
                    String vessel = null, voyage = null, bound = null, callid = null;
                    service = eBookingMasterData.getEbook_servicecode();
                    vessel = eBookingMasterData.getEbook_vesselcode();
                    voyage = eBookingMasterData.getEbook_voyage();
                    poleta = eBookingMasterData.getEbook_pol_eta();
                    poletd = eBookingMasterData.getEbook_pol_etd();
                    bound = eBookingMasterData.getEbook_bound_leg();
                    callid = eBookingMasterData.getGroupcallid();
                    Gson gson = new Gson();

                    if (readerid != null && readerid.length() > 0) {
                        Map hsm = (Map) ecomvdsservice.getEcomEDIDocstatus(book_refno);
                        String statuss;
                        int amendment_version = 0;
                        List lste = (List) hsm.get("edidocstatus");
                        if (lste != null && lste.size() > 0) {
                            Map rsl = (Map) lste.get(0);
                            if (checkNull((String) rsl.get("amendment_version")).length() > 0) {
                                amendment_version = Integer.parseInt((String) rsl.get("amendment_version"));
                            }
                            statuss = (String) rsl.get("status");
                            if ("P".equalsIgnoreCase(statuss)) {
                                amendment_version = amendment_version + 1;
                            }
                        }

                        lst = new ArrayList();
                        doc = new EcomBKGEDIDocStatus();
                        cm = new EcomCommonModel();
                        doc.setBook_no(book_refno);
                        lst.add(doc);
                        cm.setEdidocstatus(lst);
                        hs = (Map) ecomvdsservice.putecomedidocstatus(cm);

                        // Insert into edidocstatus
                        Map configMap = cntrlBeanObj.getMasterConfigDetails();
                        lst = new ArrayList();
                        doc = new EcomBKGEDIDocStatus();
                        cm = new EcomCommonModel();
                        doc.setBook_no(book_refno);
                        doc.setEdireaderid(readerid);
                        if (eBookingMasterData.getLrpBookNo() != null && eBookingMasterData.getLrpBookNo().length() > 0) {
                            doc.setTranstype("5");
                        } else {
                            doc.setTranstype("9");
                        }
                        doc.setStatus("R");
                        doc.setEdiparty(configMap.get(GeneralConf.CARRIER_CODE) + GeneralConf.EDI_TYPE_BKG);
                        doc.setMessage(GeneralConf.EDI_MESSAGETYPE_BKG);
                        doc.setServprovname(configMap.get(GeneralConf.CARRIER_CODE) + GeneralConf.EDI_ECOM);
                        doc.setAmendment_version(amendment_version);
                        lst.add(doc);
                        cm.setEdidocstatus(lst);
                        hs = (Map) ecomvdsservice.setecomeditempdocstatus(cm);

                        //Get EDIID
                        /* Map ediMap = ecomvdsservice.getecomediid(EcomConsts.ONLINE_BOOKING);
                        System.out.println("[ECOM] EcomAmendmentBookingBean >>ediMap>>" + ediMap);
                        List<EcomBKGReaderMaster> ediList = null;
                        if (ediMap != null && ediMap.size() > 0) {
                            System.out.println("[ECOM] EcomAmendmentBookingBean >>>yes value is there>>>>>");
                            ediList = gson.fromJson(gson.toJson((ArrayList) ediMap.get("edidocconfig")), new TypeToken<List<EcomBKGReaderMaster>>() {
                            }.getType());
                            if (ediList != null && ediList.size() > 0) {
                                EcomBKGReaderMaster locEdi = ediList.get(0);
                                ediid = locEdi.getEdiid();
                            }
                        } */
                        ediid = EcomConsts.ONLINE_BOOKING;
                        if (ediid != null && ediid.length() > 0) {
                            //Corporate agency
                            String corporateAgency = null;
                            List corpList = null;
                            Map corpAgency = ecomvdsservice.getEcomCorporateAgency(bookingagencyid);
                            System.out.println("[ECOM] EcomAmendmentBookingBean >>corpAgency>>" + corpAgency);
                            if (corpAgency != null && corpAgency.size() > 0) {
                                corpList = (List) corpAgency.get("agencymatrix");
                                if (corpList != null && corpList.size() > 0) {
                                    corpAgency = (Map) corpList.get(0);
                                    corporateAgency = checkNull((String) corpAgency.get("corpagency"));
                                }
                            }
                            System.out.println("[ECOM] EcomAmendmentBookingBean >>corporateAgency>>" + corporateAgency);
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
                            System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>" + hs);
                            //
                        }

                        // String lclflag = "NR"; //editempbooking - shipment_type
                        String Origin = "", Blpol = "", BlpolSVC = "", BlpolMode = "", Trans1 = "";
                        String Trans3 = "", Trans3Ter = "", Trans3SVC = "", Trans3Mode = "", Blpod = "", Delivery = "", BlpodMode = "";
                        String Trans1Ter = "", Trans1SVC = "", DefRoute = "", Routing_id = "", originmoe = "", Trans1Mode = "", Trans2 = "", Trans2Ter = "", Trans2SVC = "", Trans2Mode = "", opstrans1Mode = "", opstrans2Mode = "";

                        //Get routing details and save routing details in editempbooking routing table.
                        routeID = eBookingMasterData.getEbook_routeid();

                        System.out.println("[ECOM] EcomAmendmentBookingBean >>>routeID>>oaksdfja;sld>" + routeID);

                        if (routeID != null && routeID.length() > 0) {
                            Map rout = (Map) ecomvdsservice.getecombkgroutingserivice(routeID);
                            List routlst = gson.fromJson(gson.toJson((ArrayList) rout.get("routedetail")), new TypeToken<List<RouteVo>>() {
                            }.getType());
                            if (routlst.size() > 0) {
                                RouteVo rst = (RouteVo) routlst.get(0);
                                Origin = rst.getOrigin();
                                originmoe = rst.getOrgmode();
                                Blpol = rst.getBlpol();
                                BlpolTer = rst.getBlpolter();
                                BlpolSVC = rst.getBlpolsvc();
                                BlpolMode = rst.getMode();
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
                        } else {
                            System.out.println("[ECOM] EcomAmendmentBookingBean >>lrpBookNo" + lrpBookNo);
                            Map rout = (Map) ecomvdsservice.getecombookingrouting(lrpBookNo);
                            System.out.println("[ECOM] EcomAmendmentBookingBean >>lrp routing Map>>" + rout);
                            List routlst = gson.fromJson(gson.toJson((ArrayList) rout.get("booking_routing")), new TypeToken<List<RouteVo>>() {
                            }.getType());
                            if (routlst.size() > 0) {
                                RouteVo rst = (RouteVo) routlst.get(0);
                                Origin = rst.getOrigin();
                                System.out.println("[ECOM] EcomAmendmentBookingBean >>rst.getOrigin()>>>" + rst.getOrigin());
                                originmoe = rst.getOriginmode();
                                Blpol = rst.getPol();
                                BlpolTer = rst.getPolter();
                                BlpolSVC = rst.getPolser();
                                BlpolMode = rst.getPolmode();
                                Trans1 = rst.getTrans1();
                                Trans1Ter = rst.getTrans1ter();
                                Trans1SVC = rst.getTrans1ser();
                                Trans1Mode = rst.getTrans1mode();
                                Trans2 = rst.getTrans2();
                                Trans2Ter = rst.getTrans2ter();
                                Trans2SVC = rst.getTrans2ser();
                                Trans2Mode = rst.getTrans2mode();
                                Trans3 = rst.getTrans3();
                                Trans3Ter = rst.getTrans3ter();
                                Trans3SVC = rst.getTrans3ser();
                                Trans3Mode = rst.getTrans3mode();
                                Blpod = rst.getPod();
                                BlpodTer = rst.getPodter();
                                BlpodMode = rst.getPodmode();
                                Delivery = rst.getDelivery();
                                Service = rst.getSvccode();
                                DefRoute = rst.getDefroute();
                                opstrans1Mode = rst.getOpstrans1mde();
                                opstrans2Mode = rst.getOpstrans2mde();
                                routeID = String.valueOf(rst.getRouteid());
                            }
                        }
                        //
                        polterminal = BlpolTer;
                        podterminal = BlpodTer;
                        //
                        System.out.println("[ECOM] EcomAmendmentBookingBean #########MainService####>>>>>" + Service);

                        if (routingList != null && routingList.size() > 0) {
                            int len = routingList.size();
                            for (int k = 0; k < len; k++) {
                                eBookingrouting = (Route) routingList.get(k);
                                oceanSerCode_temp = checkNull(eBookingrouting.getService());
                                if (oceanSerCode_temp != null && oceanSerCode_temp.length() > 0) {
                                    if (oceanSerCode_temp.equalsIgnoreCase(Service)) {
                                        oceanSerCode = checkNull(eBookingrouting.getService());
                                        oceanVslCode = eBookingrouting.getVessel();
                                        oceanVoyage = eBookingrouting.getVoyage();
                                        oceanBound = eBookingrouting.getBound();
                                        break;
                                    }
                                }
                            }
                        }
                        System.out.println("[ECOM] EcomAmendmentBookingBean oceanSerCode_temp " + oceanSerCode_temp + " oceanSerCode " + oceanSerCode
                                + " oceanVslCode " + oceanVslCode + " oceanVoyage " + oceanVoyage);
                        //Insert edit temp booking master details
                        addremarks = "";
                        String salesRateAgreed = checkNull(eBookingMasterData.getSalesRateAgree());
                        if (salesRateAgreed.length() > 0) {
                            addremarks = "Sales Rep. & Rate Agreed : " + salesRateAgreed + "\n";
                        }
                        if (eBookingMasterData.getRemarks() != null) {
                            addremarks = addremarks + eBookingMasterData.getRemarks().replace("\r", "");
                        }
                        feeder = getServiceName(eBookingMasterData.getEbook_servicecode());
                        poleta = eBookingMasterData.getEbook_pol_eta();
                        poletd = eBookingMasterData.getEbook_pol_etd();
                        //

                        Map carrierMap = null;
                        List carrierTermList = null;
                        String carrierTermCode = null;
                        //GET CARRIER TERM
                        if (eBookingMasterData.getCarrierContact() != null && eBookingMasterData.getCarrierContact().length() > 0) {
                            carrierMap = ecomvdsservice.getEcomCarrierTerm(eBookingMasterData.getCarrierContact());
                            System.out.println("[ECOM] EcomAmendmentBookingBean >>carrierMap>>" + carrierMap);
                            if (carrierMap != null) {
                                carrierTermList = (List) carrierMap.get("transportservice");
                                if (carrierTermList != null && carrierTermList.size() > 0) {
                                    carrierMap = (Map) carrierTermList.get(0);
                                    carrierTermCode = (String) carrierMap.get("trptcode");
                                }
                            }
                        }

                        //Master Data saved
                        lst = new ArrayList();
                        EcomBKGMasterVO mst = new EcomBKGMasterVO();
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
                        if (eBookingMasterData.getLrpBookNo() != null && eBookingMasterData.getLrpBookNo().length() > 0) {
                            mst.setTranstype("5");
                        } else {
                            mst.setTranstype("9");
                        }
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
                        mst.setCallid(eBookingMasterData.getSlotType());
                        mst.setCustremarks(addremarks);
                        mst.setAgencyrefno(checkNullUpperCase(eBookingMasterData.getEbook_agency_reference()));
                        mst.setTransportserviceid((eBookingMasterData.getCarrierContact()));
                        mst.setTransporttermid(checkNull(carrierTermCode));
                        mst.setTbook_no(eBookingMasterData.getLrpBookNo());
                        mst.setIntransitport(checkNull(eBookingMasterData.getPodintransit()).trim());
                        mst.setIntransitportname(checkNull(eBookingMasterData.getHidPodIntransitName()).trim());
                        mst.setVersion("0");
                        mst.setEnsfiling(checkNull(eBookingMasterData.getEnsfiling()).trim());
                        mst.setT2lfiling(checkNull(eBookingMasterData.getEbook_t2lfiling()));
                        mst.setEnsdoctype(checkNull(eBookingMasterData.getEbook_EnsDocType()));
                        if (eBookingMasterData.getCargoprotectid() != null && eBookingMasterData.getCargoprotectid().length() > 0) {
                            mst.setCargoprotectid(eBookingMasterData.getCargoprotectid());
                        }
                        //        mst.setNat_code(checkNull(eBookingMasterData.getEbook_natcode()).trim());
                        lst.add(mst);
                        cm.setEditempbooking(lst);
                        hs = (Map) ecomvdsservice.setecomeditempbooking(cm);
                        System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>" + hs);

                        //Rate reference in lrp editempbooking remarks table
                        lst = new ArrayList();
                        EcomBKGReference rf = new EcomBKGReference();
                        cm = new EcomCommonModel();
                        rf.setBook_no(book_refno);
                        rf.setRefid("DAK");
                        rf.setRefval(book_refno);
                        rf.setIsedited("N");
                        rf.setEdireaderid(readerid);
                        rf.setVersion("0");
                        lst.add(rf);
                        cm.setEditempbooking_reference(lst);
                        hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                        System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>" + hs);

                        String nat = checkNull(eBookingMasterData.getEbook_natcode()); //NAT Code
                        if (nat != null && nat.length() > 0) {
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
                            System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>" + hs);
                        }
                        //Save hauler code in reference
                        String haulerCode = checkNull(eBookingMasterData.getEbook_haulercode());
                        if (haulerCode != null && haulerCode.length() > 0) {
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
                            System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>" + hs);
                        }
                        //Save hauler code in reference
                        String sisubmitparty = checkNull(eBookingMasterData.getEbook_sisubmit_party());
                        if (sisubmitparty != null && sisubmitparty.length() > 0) {
                            lst = new ArrayList();
                            rf = new EcomBKGReference();
                            cm = new EcomCommonModel();
                            rf.setBook_no(book_refno);
                            rf.setRefid(EcomConsts.sisubmit);
                            rf.setRefval(checkNull(sisubmitparty));
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            lst.add(rf);
                            cm.setEditempbooking_reference(lst);
                            hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                            System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>" + hs);
                        }
                        //
                        //Insert haulge type and picup&drop date in reference
                        String haulgeType = checkNull(eBookingMasterData.getEbook_haulagetype());
                        String pickUpDate = checkNull(eBookingMasterData.getEbook_pickup_date());
                        String dropDate = checkNull(eBookingMasterData.getEbook_drop_date());
                        String strDbFormatTime = "yyyy-MM-dd HH:mm:ss";
                        String strDbFormat = "yyyy-MM-dd";
                        SimpleDateFormat source = new SimpleDateFormat(strDbFormatTime);
                        SimpleDateFormat traget = new SimpleDateFormat(strDbFormat);
                        if (haulgeType != null && haulgeType.length() > 0) {
                            //NAT Code
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
                            System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>" + hs);
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
                            System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>" + hs);
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
                            System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>" + hs);
                        }
                        //

                        if (checkNull(eBookingMasterData.getEbook_rate_reference()).length() > 0) {
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
                            System.out.println("[ECOM] EcomAmendmentBookingBean exprefvalue" + EcomConsts.EXPORT_REF);
                            rf.setRefid(EcomConsts.EXPORT_REF);
                            rf.setRefval(eBookingMasterData.getEbook_exportref());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            lst.add(rf);
                            cm.setEditempbooking_reference(lst);
                            hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                            System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>HSVALUE" + hs);
                        }
                        if (checkNull(eBookingMasterData.getEbook_custpo()).length() > 0) {
                            //
                            lst = new ArrayList();
                            rf = new EcomBKGReference();
                            cm = new EcomCommonModel();
                            rf.setBook_no(book_refno);
                            System.out.println("[ECOM] EcomAmendmentBookingBean custpovalue" + EcomConsts.CUST_PO);
                            rf.setRefid(EcomConsts.CUST_PO);
                            rf.setRefval(eBookingMasterData.getEbook_custpo());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            lst.add(rf);
                            cm.setEditempbooking_reference(lst);
                            hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                            System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>HSVALUE" + hs);
                        }
                        if (checkNull(eBookingMasterData.getCustomerreference()).length() > 0) {
                            //
                            lst = new ArrayList();
                            rf = new EcomBKGReference();
                            cm = new EcomCommonModel();
                            rf.setBook_no(book_refno);
                            System.out.println("[ECOM] EcomAmendmentBookingBean getCustomerreference" + eBookingMasterData.getCustomerreference());
                            rf.setRefid(EcomConsts.CUST_REF);
                            rf.setRefval(eBookingMasterData.getCustomerreference());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            lst.add(rf);
                            cm.setEditempbooking_reference(lst);
                            hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
                            System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>CUST_REF" + hs);
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
                            System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>ACID" + hs);
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
                            System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>getShipperReference" + hs);
                        }

//                        if (checkNull(eBookingMasterData.getEbook_reftype()).length() > 0) {
//                            //
//                            lst = new ArrayList();
//                            rf = new EcomBKGReference();
//                            cm = new EcomCommonModel();
//                            rf.setBook_no(book_refno);
//                            System.out.println("[ECOM] EcomAmendmentBookingBean custpovalue" + EcomConsts.REF_TYPE);
//                            rf.setRefid(EcomConsts.REF_TYPE);
//                            rf.setRefval(eBookingMasterData.getEbook_reftype());
//                            rf.setIsedited("N");
//                            rf.setEdireaderid(readerid);
//                            rf.setVersion("0");
//                            lst.add(rf);
//                            cm.setEditempbooking_reference(lst);
//                            hs = (Map) ecomvdsservice.seteditempbookingreference(cm);
//                            System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>HSVALUE" + hs);
//                        }
                        //Query - BOOKING_ROUTING
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
                        rut.setOpstrans1mde(checkNull(opstrans1Mode));
                        rut.setOpstrans2mde(checkNull(opstrans2Mode));
                        if (checkNull(routeID).length() > 0) {
                            rut.setRouteid(Integer.parseInt(checkNull(routeID)));
                        }
                        lst.add(rut);
                        cm.setEditempbooking_routing(lst);
                        hs = (Map) ecomvdsservice.setecomeditempbkgroutinginfo(cm);
                        //Insert into edi temp booking leg details
                        lst = new ArrayList();
                        EcomBKGLeg leg = null;
                        Boolean isInland = false;
                        Route finalRouteVO = null;
                        cm = new EcomCommonModel();
                        if (routingList != null && routingList.size() > 0) {
                            int len = routingList.size();
                            int leg_no = 1;
                            for (int k = 0; k < len; k++) {
                                leg = new EcomBKGLeg();
                                eBookingrouting = (Route) routingList.get(k);
                                finalRouteVO = (Route) routingList.get(len - 1);
                                String pol = eBookingrouting.getLoadport();
                                String pod = eBookingrouting.getDisport();
                                String pol_str = eBookingrouting.getLoadtype();
                                String pod_str = eBookingrouting.getDisctype();
                                //     int leg_no = eBookingrouting.getLegNo();
                                String legTypeId = eBookingrouting.getLegType();
                                service = eBookingrouting.getService();
                                vessel = eBookingrouting.getVessel();
                                voyage = eBookingrouting.getVoyage();
                                poleta = eBookingrouting.getETA1();
                                poletd = eBookingrouting.getETD1();
                                bound = eBookingrouting.getBound();
                                String preCarriage = eBookingMasterData.getPreCarriage();
                                String onCarriage = eBookingMasterData.getOnCarriage();
                                String grp_CallId = eBookingrouting.getGrpCallId();
                                String loadcallid = eBookingrouting.getLoadcallid();
                                String dischargcallid = eBookingrouting.getDischargecallid();
                                String dbound = eBookingrouting.getDischargebound();
                                polterminal = eBookingrouting.getBlpolTer();
                                String podTerminal = eBookingrouting.getBlpodTer();
                                /* if (checkNull(pod).equalsIgnoreCase(checkNull(eBookingMasterData.getEbook_portofdischarge()))) {
                                    podTerminal = podterminal;
                                }*/
                                System.out.println("[ECOM] EcomAmendmentBookingBean podTerminal " + podTerminal + " Amend-loadcallid " + loadcallid + "Amend-dischargcallid" + dischargcallid);
                                //Set load and discharge type

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
                                System.out.println("[ECOM] EcomAmendmentBookingBean  leg_no===" + leg_no);
                                leg.setLeg_no(leg_no);
                                leg.setBound(bound);
                                leg.setServicecode(service);
                                leg.setVesselcode(vessel);
                                leg.setLoadportid(pol);
                                leg.setDischargeportid(pod);
                                leg.setEtddate(poletd);  //
                                leg.setEtadate(poleta);   //
                                leg.setLegtypeid(legTypeId);
                                leg.setPrecarriage(haulage_org);
                                leg.setOncarriage(haulage_des);
                                leg.setLoadtype(loadType);
                                leg.setDischtype(dischType);
                                leg.setVoyagecode(voyage);
                                leg.setEdireaderid(readerid);
                                leg.setVersion(0);
                                leg.setPreviousleg(leg_no - 1);
                                leg.setGroupcallid(grp_CallId);
                                leg.setRouteid(routeID);
                                leg.setCalliddischarge(dischargcallid);
                                leg.setCallidloading(loadcallid);
                                leg.setDbound(dbound);
                                leg.setPolterminal(polterminal);
                                leg.setPodterminal(podTerminal);
                                lst.add(leg);
                                leg_no++;
                            }
                            cm.setEditempbooking_leg(lst);
                            hs = (Map) ecomvdsservice.setecomeditempbkgleginfo(cm);
                        }
                        //Insert customer details into editempbooking customer table
                        //AP
                        lst = new ArrayList();
                        EcomBKGCustomer cust = new EcomBKGCustomer();
                        cm = new EcomCommonModel();
                        cust_name = eBookingMasterData.getEbook_agreename();
                        if (cust_name.length() > 0) {
                            cust.setBook_no(book_refno);
                            cust.setCusttypeid("AP");
                            cust.setCustid(eBookingMasterData.getEbook_agreecode());
                            cust.setCountryid(eBookingMasterData.getEbook_agreecountry());
                            cust.setName(eBookingMasterData.getEbook_agreename());
                            cust.setAddress1(eBookingMasterData.getEbook_agreeaddress());
                            cust.setCity(eBookingMasterData.getEbook_agreecity_desc());
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
                        //BP
                        if (eBookingMasterData.getEbook_bookingname() != null && eBookingMasterData.getEbook_bookingname().length() > 0) {
                            cust = new EcomBKGCustomer();
                            cm = new EcomCommonModel();
                            cust.setBook_no(book_refno);
                            cust.setCusttypeid("BP");
                            cust.setCustid(eBookingMasterData.getEbook_bookingcode());
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
                        //SH
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
                        //CO
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
                            cust.setZipCode(eBookingMasterData.getEbook_consigneecode());
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

                        ///NT
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
                        //NT1
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
                        //fw
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
                        //WH
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
                            cust.setEori(eBookingMasterData.getEbook_Warehouseeori());
                            cust.setPersontype(splitMasterCode(eBookingMasterData.getWarehousetypeperson()));

                            lst.add(cust);
                        }
                        //NC
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
                        //BY
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
                        //
                        // SL (Seller)
                        if (CustomerMap != null && CustomerMap.containsKey("SL")) {
                            seller_CustList = (ArrayList<AmendmentBookingFormBean>) CustomerMap.get("SL");
                            if (seller_CustList != null) {
                                System.out.println("Seller len==>" + seller_CustList.size());
                                for (AmendmentBookingFormBean custData : seller_CustList) {
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
                        if (CustomerMap != null && CustomerMap.containsKey("BY")) {
                            buyer_CustList = (ArrayList<AmendmentBookingFormBean>) CustomerMap.get("BY");
                            if (buyer_CustList != null) {
                                System.out.println("Buyer len==>" + buyer_CustList.size());
                                for (AmendmentBookingFormBean buyercustData : buyer_CustList) {
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
                        System.out.println("save buyer editemp vds lst==>" + lst.size());
                        cm.setEditempbooking_cust(lst);
                        hs = (Map) ecomvdsservice.setecomeditempbookingcustomer(cm);
                        System.out.println("[ECOM] EcomAmendmentBookingBean ===>>>" + hs);

                        //Insert ocean freight in remarks
                        if (eBookingMasterData.getEbook_ocean_freight() != null && eBookingMasterData.getEbook_ocean_freight().length() > 0) {

                            //Insert into reference table
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

                            //Insert into reference table
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

                        //Insert empty pick up place
                        String emptyPickUp = null;
                        if (eBookingMasterData.getEmptyPickup() != null && eBookingMasterData.getEmptyPickup().length() > 0) {
                            emptyPickUp = eBookingMasterData.getEmptyPickUpName();
                            if (eBookingMasterData.getEmptyPickUpDate() != null && eBookingMasterData.getEmptyPickUpDate().length() > 0) {
                                emptyPickUp = emptyPickUp + "  Date : " + eBookingMasterData.getEmptyPickUpDate();
                            }
                            //Insert into reference table
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
                            //end

                            //Insert into reference table
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

                            //end
                        }
                        // Insert Commodity Details
                        int len_cargo = cargoList.size();
                        String eqpid = "";
                        int k = 1;
                        hssb.append("");
                        otherssb.append("");
                        dgsb.append("");
                        ArrayList alc = new ArrayList();
                        String tareW = "";
                        String flashpoint = null;
                        String flashunit = null;
                        boolean othersboolean = false, dgboolean = false, fakBoolean = false;
                        cm = new EcomCommonModel();
                        EcomBKGCommodity cmd = null;
                        EcomBKGCargo crg = null;
                        String hscomdty = null, isFak = null, ediItemDesc = null;
                        ArrayList cmdlst = new ArrayList();
                        ArrayList crglst = new ArrayList();
                        for (int i = 0; i < len_cargo; i++) {
                            cmd = new EcomBKGCommodity();
                            crg = new EcomBKGCargo();
                            eBookingcargoData = (AmendmentBookingFormBean) cargoList.get(i);
                            ediItemDesc = checkNull(eBookingcargoData.getEbooking_commodity_desc());
                            if (ediItemDesc != null && ediItemDesc.toString().trim().length() > 255) {
                                ediItemDesc = ediItemDesc.substring(0, 255);
                                System.out.println("[ECOM] EcomAmendmentBookingBean commval=>" + ediItemDesc);
                            }
                            eqpid = eBookingcargoData.getContainerNo();
                            tareW = eBookingcargoData.getEbook_tareweight();
                            if (eBookingcargoData != null) {
                                haz = "N";
                                String hazflag = eBookingcargoData.getHzd();
                                if ("Y".equalsIgnoreCase(checkNull(hazflag))) {
                                    haz = "Y";
                                }
                                hscomdty = eBookingcargoData.getHscode();
                                isFak = eBookingcargoData.getIsFakCommodity();
                                if ("Y".equalsIgnoreCase(isFak)) {
                                    hscomdty = null;
                                    ediItemDesc = null;
                                    fakBoolean = true;
                                }
                                cargovolume = eBookingcargoData.getEbook_cargovolume();
                                netweight = eBookingcargoData.getEbook_netweight();
                                String volumeunit = eBookingcargoData.getEbook_volumeunit();
                                String olforward = checkNull(eBookingcargoData.getEbook_olforward());
                                String owleft = checkNull(eBookingcargoData.getEbook_owleft());
                                String oh = checkNull(eBookingcargoData.getEbook_oh());
                                String olaft = checkNull(eBookingcargoData.getEbook_olaft());
                                String owright = checkNull(eBookingcargoData.getEbook_owright());
                                String dimensionunit = eBookingcargoData.getEbook_dimensionunit();
                                String itempackqty = checkNull(eBookingcargoData.getEbook_packquantity());
                                String marinePollut = checkNull(eBookingcargoData.getEbook_poluttunit());
                                marinePollut = marinePollut != null && marinePollut.equalsIgnoreCase("Y") ? "Yes" : "No";
                                if (cargovolume.length() == 0) {
                                    cargovolume = null;
                                }
                                if (netweight.length() == 0) {
                                    netweight = null;
                                }
                                if (itempackqty.length() == 0) {
                                    itempackqty = null;
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

                                if (eBookingcargoData.getHscode() != null && eBookingcargoData.getHscode().trim().length() > 0 && (eBookingcargoData.getCommodityothers().equalsIgnoreCase("Y") || "Y".equalsIgnoreCase(isFak))) {
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
                                    List<AmendmentBookingFormBean> dgList = eBookingcargoData.getDgDetailsList();
                                    if (dgList != null && !dgList.isEmpty()) {
                                        int dgIdx = 1;
                                        for (AmendmentBookingFormBean dgEntry : dgList) {
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
                                          //  if (dgList.size() > 1) {
                                                dgPrefix = eqpid + " (DG#" + dgIdx + ")";
                                         //   }
                                            
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
                                cmd.setItemqty(itempackqty);
                                cmd.setItemweight(ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_cargoweight())));
                                cmd.setItemweightunit(checkNull(eBookingcargoData.getEbook_weightunit()));
                                cmd.setHazardous(checkNull(haz));
                                cmd.setEqpid(checkNull(eqpid));
                                cmd.setAlcohol("N");
                                cmd.setMilitarycargo("N");
                                cmd.setCargono(k);
                                cmd.setPackageid(eBookingcargoData.getEbook_packagecode());
                                cmd.setMarksdesc(eBookingcargoData.getEbook_actCommoName());
                                System.out.println("[ECOM] EcomAmendmentBookingBean getactcommo " + eBookingcargoData.getEbook_actCommoName() + " getmarkamend " + cmd.getMarksdesc());
                                cmd.setItemvolume(cargovolume);
                                cmd.setNettwt(netweight);
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
                                cmdlst.add(cmd);

                                //Add container batch
                                dgflag = "N";
                                hazflag = eBookingcargoData.getHzd();
                                if ("Y".equalsIgnoreCase(checkNull(hazflag))) {
                                    dgflag = "Y";
                                }

                                String laden = eBookingcargoData.getEqpstatus();
                                mintemp = checkNull(eBookingcargoData.getEbook_mintemp());
                                maxtemp = checkNull(eBookingcargoData.getEbook_maxtemp());
                                presetemp = checkNull(eBookingcargoData.getEbook_presetemp());
                                mintemp = mintemp.length() > 0 ? mintemp : null;
                                maxtemp = maxtemp.length() > 0 ? maxtemp : null;
                                presetemp = presetemp.length() > 0 ? presetemp : null;
                                temperunit = eBookingcargoData.getEbook_temperunit();
                                String humidity = "Y".equalsIgnoreCase(eBookingcargoData.getHumidityflag()) ? eBookingcargoData.getHumidity() : null;
                                String humidity_flag = "Y".equalsIgnoreCase(eBookingcargoData.getHumidityflag()) ? "1" : "0";
                                String drains = eBookingcargoData.getDrains();
                                String co2 = eBookingcargoData.getCo2();
                                String o2 = eBookingcargoData.getO2();
                                String ventilation = eBookingcargoData.getVentilation();

                                crg.setBook_no(book_refno);
                                String strEQP = eBookingcargoData.getEbook_eqp_type();
                                if ("Y".equalsIgnoreCase(checkNull(eBookingcargoData.getEbook_nor()))) {
                                    temperunit = null;
                                    mintemp = null;
                                    presetemp = null;
                                    maxtemp = null;
                                    humidity = drains = co2 = o2 = ventilation = null;
                                }
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
                                crg.setCargono(k);
                                crg.setOog(checkNull(eBookingcargoData.getOog()));
                                crg.setShipperown(checkNull(eBookingcargoData.getSoc()));
                                crg.setCargoweight(ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_cargoweight())));
                                crg.setGrossweight(ConvertDecimalFormat(checkNull(eBookingcargoData.getEbook_grossweight())));
                                crg.setWeightunit(checkNull(eBookingcargoData.getEbook_weightunit()));
                                crg.setTempmax(maxtemp);
                                crg.setTempmin(mintemp);
                                crg.setSettemp(presetemp);
                                crg.setTempunit(temperunit);
                                crg.setNor(eBookingcargoData.getEbook_nor());

                                crg.setLoadingcondition("FF");
                                crg.setIsdummy(eBookingcargoData.getEbook_isdummy());
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
                                crglst.add(crg);
                                k++;
                            }
                        }
                        //commodity
                        cm.setEditempbooking_commodity(cmdlst);
                        hs = (Map) ecomvdsservice.setecomeditempbookingcommodity(cm);
                        //cargo
                        cm.setEditempbooking_cargo(crglst);
                        hs = (Map) ecomvdsservice.setecomeditempbkgcargo(cm);

                        //  insert booking Haulage details
                        if (haulagelist != null && haulagelist.size() > 0) {
                            EcomBKGHaulage haul = null;
                            lst = new ArrayList();
                            for (int i = 0; i < haulagelist.size(); i++) {

                                haulvo = (BookingHaulagedetailsVo) haulagelist.get(i);
                                haul = new EcomBKGHaulage();
                                haul.setBook_no(checkNull(book_refno));
                                haul.setEqpid(checkNull(haulvo.getEqpid()));
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
//                                haul.setTmsclientcode(checkNull(haulvo.getTmsclientcode()));
                                haul.setRemarks(checkNull(haulvo.getRemarks()));
                                haul.setEdireaderid(checkNull(readerid));
                                haul.setVersion("0");
                                lst.add(haul);

                            }
                            cm.setEditempbooking_haulage(lst);
                            hs = (Map) ecomvdsservice.setecomeditempbkghaulage(cm);

                        }
                        if (checkIsEmpty(eBookingMasterData.getBkgcutoff()) || checkIsEmpty(eBookingMasterData.getDoccutoff())
                                || checkIsEmpty(eBookingMasterData.getVgmcutoff()) || checkIsEmpty(eBookingMasterData.getBbrorocutoff())
                                || checkIsEmpty(eBookingMasterData.getHazcutoff()) || checkIsEmpty(eBookingMasterData.getPortentrycutoff())) {
                            lst = new ArrayList();
                            EcomBKGConf conf = new EcomBKGConf();

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

                        // ENS Filling  
                        ENSDeclarantVo ensDec = eBookingMasterData.getEnsdecvo();
                        if (ensDec != null && checkNull(ensDec.getEnsFillingType()).length() > 0) {
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

                        String ishazardous = "N";
                        cm = new EcomCommonModel();
                        EcomBKGCommDG dg = null;
                        lst = new ArrayList();
                        for (int i = 0; i < len_cargo; i++) {
                            dg = new EcomBKGCommDG();
                            eBookingcargoData = (AmendmentBookingFormBean) cargoList.get(i);
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
                        //  cm.setEditempbooking_imo(lst);
                        //  hs = (Map) ecomvdsservice.setecomeditempbkgdgdetails(cm);
                        //
                        String hscodecheck = hssb.toString();
                        if (hscodecheck != null && hscodecheck.trim().length() > 0 && (othersboolean == true || fakBoolean)) {
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
                        if (isconvertzip && checkNull(filepath).length() > 0) {
                            filepath = toRenameZipFile(book_refno, readerid, fileUploadPath, filepath);
                        }
                        if (filepath != null && filepath.length() > 0) {
                            try {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");
                                String dateString = format.format(new Date());
                                String newfile = "(" + filepath.replace("~~~", ",") + ") at " + dateString;
                                lst = new ArrayList();
                                rm = new EcomBKGRemarks();
                                cm = new EcomCommonModel();
                                rm.setBook_no(book_refno);
                                rm.setRemarksref("File Added ");
                                rm.setRemarksval(newfile);
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
                        if (deletedfile != null && deletedfile.length() > 0) {
                            try {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");
                                String dateString = format.format(new Date());
                                String newfile = "(" + deletedfile.replace("~~", ",") + ") at " + dateString;
                                lst = new ArrayList();
                                rm = new EcomBKGRemarks();
                                cm = new EcomCommonModel();
                                rm.setBook_no(book_refno);
                                rm.setRemarksref("File Removed ");
                                rm.setRemarksval(newfile);
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
                    } // Id Close
                    String strAgencyCode = agencycode;
                    String strAgency = getAgencyMail(strAgencyCode);
                    System.out.println("[ECOM] EcomAmendmentBookingBean strAgency==>" + strAgency + "strAgencyCode" + strAgencyCode);
                    rvo.setAdmToMailId(strAgency);

                    String agencycountry = getAgencyCountry(strAgencyCode);
                    rvo.setAgencycountry(agencycountry);

                    rvo.setReaderid(readerid);
                }
            }

            //Mail format for customer
            String strReturn = "";
            if ("S".equalsIgnoreCase(SaveType) && "R".equalsIgnoreCase(bkg_type)) {
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
                System.out.println("[ECOM] EcomAmendmentBookingBean ===>setStrToMailId<==" + ApEmailID);
                rvo.setStrToMailId(ApEmailID);
                //

                if (rvo.getAdmToMailId() != null && rvo.getAdmToMailId().length() > 0) {
                    MailFormatVO mailFormatObj2 = new MailFormatVO();
                    mailFormatObj2 = getMailContent("E-Booking", "Update", "C");
                    String msg2 = mailFormatObj2.getStrMailContent();
                    msg2 = msg2.replaceAll("@book_no", book_refno);
                    //msg2 = msg2.replaceAll("@customer", "Agency");
                    msg2 = msg2.replaceAll("@client", mailFormatObj2.getStrRegard());
                    rvo.setAdmMailMsg(msg2);
                    String subject1 = mailFormatObj2.getStrSubject();
                    subject1 = subject1.replace("@bookref", book_refno);
                    rvo.setAdmSubject(subject1);
                    rvo.setAdmFromMailId(mailFormatObj2.getStrFromAddr());

                    rvo.setFilenames(filepath);
                    //
                }
                if ("en".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    strReturn = checkNull(getForwardMessage("E-Booking", "insert", "c"));
                } else if ("ch".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    strReturn = checkNull(getForwardMessageLang("E-Booking", "insert", "c", "ch"));
                } else if ("ma".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    strReturn = checkNull(getForwardMessageLang("E-Booking", "insert", "c", "ma"));

                } else if ("es".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    strReturn = checkNull(getForwardMessageLang("E-Booking", "insert", "c", "es"));

                }
            } else if ("S".equalsIgnoreCase(SaveType) && !"R".equalsIgnoreCase(bkg_type)) {
                if (custCountry != null && "CN".equalsIgnoreCase(custCountry)) {
                    mailFormatObj = getMailContentLang("E-Booking", "BKAmClient", "C", "ch");

                } else {
                    mailFormatObj = getMailContent("E-Booking", "BKAmClient", "C");
                }
                String msg = mailFormatObj.getStrMailContent();
                msg = msg.replace("@book_no", book_refno);
                subject = mailFormatObj.getStrSubject();
                subject = subject + "-(" + book_refno + ")";
                rvo.setStrMailMsg(msg);
                rvo.setStrSubject(subject);
                rvo.setActivityCode(mailFormatObj.getActivityCode());
                rvo.setStrFromMailId(mailFormatObj.getStrFromAddr());
                rvo.setFilenames(filepath);
                //Mail format for agency
                if (rvo.getAdmToMailId() != null && rvo.getAdmToMailId().length() > 0) {
                    MailFormatVO mailFormatObj2 = new MailFormatVO();
                    mailFormatObj2 = getMailContent("E-Booking", "BKAmAgen", "C");
                    String msg2 = mailFormatObj2.getStrMailContent();
                    msg2 = msg2.replaceAll("@book_no", book_refno);
                    msg2 = msg2.replaceAll("@customOnline Reference Noer", "Agency");
                    msg2 = msg2.replaceAll("@client", mailFormatObj2.getStrRegard());
                    rvo.setAdmMailMsg(msg2);
                    String subject1 = mailFormatObj2.getStrSubject();
                    subject1 = subject1.replace("@bookref", book_refno);
                    rvo.setAdmSubject(subject1);
                    rvo.setAdmFromMailId(mailFormatObj2.getStrFromAddr());
                    rvo.setFilenames(filepath);
                }
                //
                if ("en".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    strReturn = checkNull(getForwardMessage("E-Booking", "AmendMsg", "c"));
                } else if ("ch".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    strReturn = checkNull(getForwardMessageLang("E-Booking", "AmendMsg", "c", "ch"));
                } else if ("ma".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    strReturn = checkNull(getForwardMessageLang("E-Booking", "AmendMsg", "c", "ma"));
                } else if ("es".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    strReturn = checkNull(getForwardMessageLang("E-Booking", "AmendMsg", "c", "es"));
                }
            } else if ("D".equalsIgnoreCase(SaveType)) {
                if ("en".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    strReturn = checkNull(getForwardMessage("E-Booking", "Draft_in", "c"));
                } else if ("ch".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    strReturn = checkNull(getForwardMessageLang("E-Booking", "Draft_in", "c", "ch"));
                } else if ("ma".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    strReturn = checkNull(getForwardMessageLang("E-Booking", "Draft_in", "c", "ma"));
                } else if ("es".equalsIgnoreCase(eBookingMasterData.getLangType())) {
                    strReturn = checkNull(getForwardMessageLang("E-Booking", "Draft_in", "c", "es"));
                }
            }
            strReturn = strReturn.replaceAll("@bookno", book_refno);
            System.out.println("[ECOM] EcomAmendmentBookingBean >>strReturn>>" + strReturn);
            rvo.setStrDisplayMsg(strReturn);
            rvo.setStrRefNo(book_refno);
            rvo.setFilenames(filepath);
        } catch (Exception e) {
            rvo.setStrRefNo("ERROR");
            rvo.setStrSaveType(SaveType);
            rvo.setStrDisplayMsg("Amendment Booking not saved.");
            e.printStackTrace();
            context.setRollbackOnly();
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
        return rvo;
    }

    private String getForwardMessage(String module, String criteria, String usertype) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        try {
            String Forward_query = "select message from ecom_messageformat(nolock) where Status='a' and Type ='msg' "
                    + "and criteria =? and Module =? and usertype=? ";
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(Forward_query, 1004, 1007);
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

    private String getForwardMessageLang(String module, String criteria, String usertype, String lang) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        try {
            String Forward_lang_query = "select message from ecom_messageformatlang(nolock) where Status='a' and Type ='msg' "
                    + "and criteria =? and Module =? and usertype=? and lang=?";
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(Forward_lang_query, 1004, 1007);
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

    private String checkNullDecimal(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "0.0000";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    private String checkNullbigDecimal(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"0.0000".equalsIgnoreCase(str)) && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
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

    private Agencydto getAgencyEmail(String ebook_bookagency, String view) {

        Agencydto agencyVO = new Agencydto();
        try {
            Map hs = (Map) ecomvdsservice.getecomagencydetails(ebook_bookagency);
            //
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

    private String getAgencyMail(String agencycode) {
        String email = "";
        try {
            int i = 0;
            Map hs = (Map) ecomvdsservice.getEcomBookingAgencyMail(agencycode);
            //
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

    public String getAgencydetails(String port) throws RemoteException {
        String agency = "";
        if (port != null && port.contains("-")) {
            port = port.substring(0, port.indexOf("-"));
        }
        try {
            Map hs = (Map) ecomvdsservice.getEcomAgencyLocation(port);
            //
            List lst = (List) hs.get("agencylocations");
            if (lst != null && lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                agency = (String) rs.get("agencycode");
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EcomAmendmentBookingBean Exception while geting Agency code  " + e.getMessage());
            e.printStackTrace();
        }
        return agency;
    }

    //
   /* private String bookNoGenerate(String module) {
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
            System.out.println("[ECOM] EcomAmendmentBookingBean =NEW bknum===>>" + bknum);
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
                System.out.println("[ECOM] EcomAmendmentBookingBean exception while closing connection bookNoGenerate" + e);
                e.printStackTrace();
            }

        }
        return bknum;
    }*/
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
                    System.out.println("[ECOM] EcomAmendmentBookingBean NEW bknum >> " + bknum);
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
    public String checkSICreation(String bookNo) throws RemoteException {
        System.out.println("[ECOM] EcomAmendmentBookingBean <<bookNo>>" + bookNo);
        String result = "NO";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(CHECKSICREATION, 1004, 1007);
            ps.setString(1, bookNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = "YES";
            }
            System.out.println("[ECOM] EcomAmendmentBookingBean >>result>>" + result);
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

    private String checkNullUpperCase(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.toUpperCase().trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    private MailFormatVO getMailContent(String module, String criteria, String usertype) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MailFormatVO mailFormatObj = new MailFormatVO();
        try {

            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(ECOM_MAIL_CONTENT, 1004, 1007);
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

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

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
            System.out.println("[ECOM] EcomAmendmentBookingBean =getAgencydetails==>>" + agency);

        } catch (Exception e) {
            System.out.println("[ECOM] EcomAmendmentBookingBean Exception while getting Agency code  " + e.getMessage());
            e.printStackTrace();
        }
        return agency;
    }

    @Override
    public List getLRPBookingHaulageDetails(String bookNo, String type, HashMap cargonomap) throws RemoteException {
        List<BookingHaulagedetailsVo> haullist = null;
        BookingHaulagedetailsVo haulvo = null;
        try {
            Map hs = (Map) ecomvdsservice.getlrpbookinghaulagedetails(bookNo);
            List lst = (List) hs.get("booking_haulage");
            if (lst.size() > 0) {
                haullist = new ArrayList();
                for (int i = 0; i < lst.size(); i++) {
                    haulvo = new BookingHaulagedetailsVo();
                    Map rs = (Map) lst.get(i);
                    haulvo.setEbookno(checkNull((String) rs.get("book_no")));
                    haulvo.setPickloccode(checkNull((String) rs.get("loadlocation")));
                    if ("R".equalsIgnoreCase(type)) {
                        haulvo.setEqpid(checkNull((String) cargonomap.get(checkNull((String) rs.get("eqpid")))));
                    } else {
                        haulvo.setEqpid(checkNull((String) rs.get("eqpid")));
                    }
                    haulvo.setContactperson(checkNull((String) rs.get("contactperson")));
                    haulvo.setAddress(checkNull((String) rs.get("contactaddress")));
                    haulvo.setContactphone(checkNull((String) rs.get("contactphone")));
                    haulvo.setContactemail(checkNull((String) rs.get("contactemail")));
                    haulvo.setRefno(checkNull((String) rs.get("referencenumber")));
                    haulvo.setDropdate(checkNull((String) rs.get("dropdate")));
                    haulvo.setRemarks(checkNull((String) rs.get("remarks")));
                    haulvo.setTwrsdoctype(checkNull((String) rs.get("doctype")));
                    haulvo.setTwrsoverweight(checkNull((String) rs.get("overweight")));
                    haulvo.setStopsequence(Integer.parseInt((String) rs.get("stopsequence")));
                    haulvo.setPicklocname(checkNull((String) rs.get("loadlocationname")));
//                    haulvo.setTmsclientcode(checkNull((String) rs.get("tmsclientcode")));
                    haulvo.setEqptype(checkNull((String) rs.get("eqptypeid")));
                    haullist.add(haulvo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return haullist;
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

    public boolean checkIsEmpty(String value) {
        boolean result = false;
        try {
            if (value != null && value.trim().length() > 0) {
                result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public CutoffConfigVO getLRPBookingConfDetails(String bookno) throws RemoteException {
        CutoffConfigVO confvo = null;
        try {
            Map hs = (Map) ecomvdsservice.getLrpBookingConf(bookno);
            List lst = (List) hs.get("booking_conf");
            if (lst.size() > 0) {
                confvo = new CutoffConfigVO();
                Map rs = (Map) lst.get(0);
                confvo.setBkgcutoff(DateConversion_New.convertDefaultFormatwithTime(checkNull((String) rs.get("bkgcutoff"))));
                confvo.setDoccutoff(DateConversion_New.convertDefaultFormatwithTime(checkNull((String) rs.get("doccutoff"))));
                confvo.setVgmcutoff(DateConversion_New.convertDefaultFormatwithTime(checkNull((String) rs.get("vgmcutoff"))));
                confvo.setBbrorocutoff(DateConversion_New.convertDefaultFormatwithTime(checkNull((String) rs.get("bbrorocutoff"))));
                confvo.setHazcutoff(DateConversion_New.convertDefaultFormatwithTime(checkNull((String) rs.get("hazcutoff"))));
                confvo.setPortentrycutoff(DateConversion_New.convertDefaultFormatwithTime(checkNull((String) rs.get("portentrycutoff"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return confvo;
    }

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

    private MailFormatVO getMailContentLang(String module, String criteria, String usertype, String lang) throws RemoteException {
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

    public String getStateCode(String state, String countrycode, String book_no) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println("[ECOM] EcomAmendmentBookingBean getStateCode  -- Book_no=" + book_no + "  State=> " + state + "  countrycode==> " + countrycode);
            if (state != null && checkNull(state).length() > 0 && checkNull(state).contains("-")) {
                state = state.split("-")[0].trim();
            }
            if (state != null && checkNull(state).length() > 4) {
                con = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();
                String query = "select statecode from Statesmaster(nolock) where statedesc = '" + state + "' and countrycode='" + countrycode + "'";
                System.out.println("[ECOM] EcomAmendmentBookingBean Query => " + query);
                ps = con.prepareStatement(query, 1004, 1007);
                rs = ps.executeQuery();
                if (rs.next()) {
                    state = checkNull(rs.getString("statecode"));
                } else {
                    state = "";
                }
            }
            System.out.println("[ECOM] EcomAmendmentBookingBean result----> " + state);
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

    private Map getInsuranceDetils(String bookno) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map insmap = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

            ps = con.prepareStatement("select commodity,quoteno,partytype,suminsured,email from ecom_insurance(nolock) where book_no=?");
            ps.setString(1, checkNull(bookno));
            rs = ps.executeQuery();
            insmap = new HashMap();
            String qouteno = null, commodity = "", sumins = "";;
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
            System.out.println("[ECOM] EcomAmendmentBookingBean getInsuranceDetils=> " + insmap);
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
            System.out.println("[ECOM] EcomAmendmentBookingBean actualfile: " + actualfile + " &renamefile: " + renamefile);
            FileUtils.moveFile(file, rename);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, List<AmendmentBookingFormBean>> getLRPBookingCutomerDetails(String bookNo) throws RemoteException {

        List<AmendmentBookingFormBean> buyer_CustList = null;
        List<AmendmentBookingFormBean> seller_CustList = null;
        HashMap<String, List<AmendmentBookingFormBean>> finalmap = null;
        try {

            finalmap = new HashMap<>();
            Map hs = (Map) ecomvdsservice.getecombookingcust(bookNo, "A", "A");
            List custlst = (List) hs.get("booking_cust");
            if (custlst.size() > 0) {
                seller_CustList = new ArrayList<AmendmentBookingFormBean>();
                buyer_CustList = new ArrayList<AmendmentBookingFormBean>();

                for (int j = 0; j < custlst.size(); j++) {
                    Map rs = (Map) custlst.get(j);
                    String custType = checkNull((String) rs.get("custtypeid"));
                    if ("SL".equalsIgnoreCase(custType) || "BY".equalsIgnoreCase(custType)) {
                        AmendmentBookingFormBean eBookingmaster = new AmendmentBookingFormBean();
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

    public HashMap<String, List<AmendmentBookingFormBean>> getEcomCutomerDetails(String bookNo) throws RemoteException {
        AmendmentBookingFormBean eBookingmaster;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<AmendmentBookingFormBean> buyer_list = null;
        List<AmendmentBookingFormBean> seller_list = null;
        HashMap<String, List<AmendmentBookingFormBean>> finalmap = null;
        try {
            finalmap = new HashMap<>();
            buyer_list = new ArrayList<AmendmentBookingFormBean>();
            seller_list = new ArrayList<AmendmentBookingFormBean>();
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(BOOKING_CUSTEDIT);
            ps.setString(1, bookNo);
            rs = ps.executeQuery();

            while (rs.next()) {
                String custType = checkNull(rs.getString("custTypeId"));
                eBookingmaster = new AmendmentBookingFormBean();
                if ("SL".equalsIgnoreCase(custType) || "BY".equalsIgnoreCase(custType)) {
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
                    String toOrder = checkNull(rs.getString("toorder"));
                    String phone = checkNull(rs.getString("telephone"));
                    String fax = checkNull(rs.getString("fax"));

                    // Common customer setters
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
                    eBookingmaster.setEbook_Custeori(eori);
                    eBookingmaster.setEbook_Custpersontype(persontype);
                    eBookingmaster.setHidCNToOrder(toOrder);
                    eBookingmaster.setEbook_Custphone(isValidContact(phone) ? phone : "");
                    eBookingmaster.setEbook_Custfax(isValidContact(fax) ? fax : "");

                    // Add to respective list
                    if ("SL".equalsIgnoreCase(custType)) {
                        seller_list.add(eBookingmaster);
                    } else if ("BY".equalsIgnoreCase(custType)) {
                        buyer_list.add(eBookingmaster);
                    }
                }
            }

            if (!seller_list.isEmpty()) {
                finalmap.put("SL", seller_list);
            }
            if (!buyer_list.isEmpty()) {
                finalmap.put("BY", buyer_list);
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
        System.out.println("///finalmap==>" + finalmap);
        return finalmap;
    }

    private boolean isValidContact(String contact) {
        return contact != null && contact.length() > 0
                && !contact.equalsIgnoreCase("--")
                && !contact.equalsIgnoreCase("-");
    }

    private void saveCustDetails(Map custMap, String book_refno) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<AmendmentBookingFormBean> buyer_CustList = null;
        ArrayList<AmendmentBookingFormBean> seller_CustList = null;
        AmendmentBookingFormBean eBookingcustData = null;
        try {
            if (custMap != null && !custMap.isEmpty()) {
                con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                buyer_CustList = new ArrayList<AmendmentBookingFormBean>();
                seller_CustList = new ArrayList<AmendmentBookingFormBean>();
                if (custMap != null && custMap.containsKey("BY")) {
                    buyer_CustList = (ArrayList<AmendmentBookingFormBean>) custMap.get("BY");
                    if (buyer_CustList != null) {
                        int len = buyer_CustList.size();

                        ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                        for (int i = 0; i < len; i++) {
                            eBookingcustData = (AmendmentBookingFormBean) buyer_CustList.get(i);
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
                        System.out.println("[ECOM] EcomAmendmentBookingBean saveCustDetails Buyer==>" + Customer_update);
                    }
                }
                //Seller 
                if (custMap != null && custMap.containsKey("SL")) {
                    seller_CustList = (ArrayList<AmendmentBookingFormBean>) custMap.get("SL");
                    if (seller_CustList != null) {
                        int len = seller_CustList.size();
                        System.out.println("save the Seller len==>" + len);

                        ps = con.prepareStatement(SAVE_EBOOKING_CUST_QRY);
                        for (int i = 0; i < len; i++) {
                            eBookingcustData = (AmendmentBookingFormBean) seller_CustList.get(i);
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
                            System.out.println("[ECOM] EcomAmendmentBookingBean saveCustDetails seller==>" + Customer_update);
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
    public ENSDeclarantVo getLRPBookingENSTabDetails(String lrpBookNo) throws RemoteException {
        ENSDeclarantVo ensvo = null;
        try {
            Map hs = (Map) ecomvdsservice.getecombookingicsdetails(lrpBookNo);
            System.out.println("[ECOM] EcomAmendmentBookingBean getLRPBookingENSTabDetails hs==>" + hs + lrpBookNo);

            List lst = (List) hs.get("booking_ics_new");
            if (lst != null && !lst.isEmpty()) {
                Map rs = (Map) lst.get(0);
                ensvo = new ENSDeclarantVo();
                ensvo.setEuCustZone(checkNull((String) rs.get("eucustom")));
                ensvo.setBlTypeENS(checkNull((String) rs.get("bill_type")));
                ensvo.setEnsFillingType(checkNull((String) rs.get("ens_type")));
                ensvo.setPaymentMethod(checkNull((String) rs.get("payment_mode")));
                ensvo.setDeclarantName(checkNull((String) rs.get("cust_name")));
                ensvo.setDeclarantAddress(checkNull((String) rs.get("address")));
                ensvo.setDeclarantAddress1(checkNull((String) rs.get("address1")));
                ensvo.setDeclarantCity(checkNull((String) rs.get("city")));
                ensvo.setDeclarantCountry(checkNull((String) rs.get("country")));
                ensvo.setDeclarantState(checkNull((String) rs.get("state")));
                ensvo.setDeclarantTelephone(checkNull((String) rs.get("telephone")));
                ensvo.setDeclarantEORI(checkNull((String) rs.get("enseori")));
                ensvo.setDeclarantEmail(checkNull((String) rs.get("email")));
                ensvo.setDeclarantFillingType(checkNull((String) rs.get("filing_type")));
                ensvo.setDeclarantZipcode(checkNull((String) rs.get("zipcode")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[EcomAmendmentBookingBean] Exception in getLRPBookingENSTabDetails PortEndPoint- " + e);
        }
        return ensvo;
    }
}
