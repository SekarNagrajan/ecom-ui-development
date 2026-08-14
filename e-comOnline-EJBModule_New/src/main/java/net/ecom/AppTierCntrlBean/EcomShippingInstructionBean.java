/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

//import com.svm.lrp.nfr.query.cbean.SVMServiceInvocker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.json.SvmJsonList;
import com.svm.lrp.nfr.query.cbean.VDSInvoker;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.SISummaryComparator;
import net.ecom.common.Validation;
import net.ecom.common.Validation1;
import net.ecom.common.ZipFileConversion;
import net.ecom.si.vo.CompleteModel;
import net.ecom.si.vo.Booking;
import net.ecom.si.vo.Booking_cargo;
import net.ecom.si.vo.Booking_commodity;
import net.ecom.si.vo.Booking_cust;
import net.ecom.si.vo.Ports;
import net.ecom.si.vo.Service;
import net.ecom.si.vo.Vesselmaster;
import net.ecom.v1.cntlbean.EcomV1SIBeanRemote;
import net.ecom.vds.service.EcomBKGSpsService;
import net.ecom.vds.service.EcomSiVdsService;
import net.ecom.vds.service.EcomBKGVdsService;
import net.ecom.vds.serviceVo.CustomerMasterVo;
import net.ecom.vds.serviceVo.EcomBKGCommonModel;
import net.ecom.vds.serviceVo.EcomBKGCustomer;
import net.ecom.vds.serviceVo.EcomBKGEDIDocStatus;
import net.ecom.vds.serviceVo.EcomBKGHeader;
import net.ecom.vds.serviceVo.EcomBKGMasterVO;
import net.ecom.vds.serviceVo.EcomBKGReaderMaster;
import net.ecom.vds.serviceVo.EcomBKGReference;
import net.ecom.vds.serviceVo.EcomBKGRemarks;
import net.ecom.vds.serviceVo.EcomBKGRouting;
import net.ecom.vds.serviceVo.EcomBookingCharges;
import net.ecom.vds.serviceVo.EcomCommonModel;
import net.ecom.vds.serviceVo.Editempbooking_ics_new;
import net.ecom.vds.serviceVo.VesselMaster;
import net.ecom.vo.ChargesVo;
import net.ecom.vo.CustomerDetailsVO;
import net.ecom.vo.CustomerProfile;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.FileDetailsVO;
import net.ecom.vo.MailFormatVO;
import net.ecom.vo.PortVO;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.SIBLMasterVO;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIContainerDetailVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;
import net.ecom.vo.SISealVO;
import net.ecom.vo.SISubmitVO;
import net.ecom.vo.SIsummaryVO;
import org.apache.commons.io.FileUtils;

@Stateless(name = "EcomShippingInstructionBean")
@Remote(EcomShippingInstructionBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
    ,
        @Resource(name = "jdbc/LRPDB", mappedName = "java:/MSSQLDS")
    ,
        @Resource(name = "jdbc/MASTERDB", mappedName = "java:/MASTERDS")
    ,
        @Resource(name = "jdbc/ReportDB", mappedName = "java:/REPORTDS")
})
public class EcomShippingInstructionBean implements EcomShippingInstructionBeanRemote {

    @Inject
    VDSInvoker vds;

    EcomSiVdsService siVDS;
    EcomBKGVdsService bkgVDS;
    EcomBKGSpsService ecomvdspservice;

    @EJB(mappedName = "java:global/e-comOnline-EJBModule_New/EcomV1SIBean!net.ecom.v1.cntlbean.EcomV1SIBeanRemote")
    EcomV1SIBeanRemote v1SIBeanObj;

    @EJB(mappedName = "java:global/e-comOnline-EJBModule_New/EComCntrlBean!net.ecom.AppTierCntrlBean.EComCntrlBeanRemote")
    EComCntrlBeanRemote cntrlBeanObj;

    @PostConstruct
    public void init() {
        System.out.println("[ECOM] EcomShippingInstructionBean <<v1SIBeanObj<<" + v1SIBeanObj);
        siVDS = vds.create(EcomSiVdsService.class);
        ecomvdspservice = vds.create(EcomBKGSpsService.class);
        bkgVDS = vds.create(EcomBKGVdsService.class);
    }

    @Resource
    private SessionContext context;
    protected static final String QrygetEDIID = "select ediid from edidocconfig(nolock) where ediname='"
            + EcomConsts.ONLINE_SI + "'";
    private static final String BOOK_NO_GEN = "select mdlid,mdlprefix,mdlindex from ecomMdlIndex where mdlid = ?";
    private static final String BOOK_NO_UPDATE = "update ecomMdlIndex set mdlindex=? "
            + "where mdlid =?";
    private static final String DRAFT_DG_INSERT = "insert into ecomSI_dg(eSIno,conId,containerno,itemno,"
            + "MarinePollutant,unNo,class,FlashPt,FlashPtUnit,imocontact,imophone) values(?,?,?,?,?,?,?,?,?,?,?)";
    private static final String DRAFT_OOG_INSERT = "insert into ecomSI_oog (eSIno,conId,comItemNo,"
            + "containerno, leftft, rightft, forward,Alt,topft,Unit)values(?,?,?,?,?,?,?,?,?,?)";
    private static final String DRAFT_Refer_INSERT = "insert into ecomSI_reefer(eSIno,conId,containerno,"
            + "ref_setTemp,ref_minTemp,ref_maxTemp,ref_tempUnit,humidity,humidity_flag,drains,co2value,o2value,ventpercentage)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SAVE_SI_MASTER_DET = "insert into ecomSI(eSINo,blno,bookno,Vessel,Voyage,bound,pol,pod,pla,pld,"
            + "custcode,CrUser,CrDate,ChgUser,ChgDate,editbl,blversion,status,declined_remarks,"
            + "custremarks,plaHeaderName,polHeaderName,podHeaderName,pldHeaderName,appversion,ccmailid,shipper,shippername,consigee,consigeename,notify,notifyname,AESNumber,AESDiscliamer,releasetype,declaredvalue,blType,ACID,Freight,cargoprotectid,t2lfiling,packinglst,invoiceupload,telexrls,enstype,bltype_UAE,mpci_id,nvocc)"
            + " values(?,?,?,?,?,?,?,?,?,?,?,?,getDate(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SAVE_SI_CUST_DET = "insert into ecomSI_cust(eSINo,custTypeId,custId, countryId, name, address1, "
            + "address2, city, zipCode, telephone, fax, email, flag,toorder,stateId,eori,persontype)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SAVE_SI_CARGO_DET = "insert into ecomSI_cargo(eSINo, conId, eqpType, eqpSize, containerno, "
            + "shipperOwn, carrierSeal, ShipperSeal, tareWeight, cargoWeight, grossweight, "
            + "weightUnit, ref_nor,eqpstatus,oog,dgflag,rate_eqpTypeid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String SAVE_SI_COMMODITY_DET = "insert into ecomSI_commodity(eSINo,conId, containerno, itemno,commodity,package,"
            + " packageQty, itemWeight, itemWeightUnit, itemVolume, itemVolumeUnit,marksNo,"
            + " marksDesc,hscommodity,hazardous,nettwt,hssuffixcode,bycode,byname,slcode,slname,cmdty_hblno)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_SI_REQUEST_MASTER = "update ecomSIRequestMaster set createdSI=createdSI+1,chgUser=?,"
            + "chgDate=getdate() where bookno=?";
    private static final String GET_CARGO_FROM_SI = "select conId,containerno,ref_nor from ecomSI_cargo(nolock) where eSINo=? and "
            + "containerno=?";
    private static final String DELETE_SI_COMMODITY = "delete from ecomSI_commodity where eSINo=?";
    private static final String DELETE_SI_CARGO = "delete from ecomSI_cargo where eSINo=?";
    private static final String INSERT_ECOM_SI_GEN = "insert into ecomSISummaryGeneration(id,bkstatus,blstatus,loadPortId,dischargePortId,polAt,book_no,servicecode,vesselcode,"
            + "voyagecode,bound,bpCustId,bpName,bpCustTypeId,apCustTypeId,apCustId,apName,originPortId,finalPortId,polTerminal,podTerminal,bl_no,custRefNo,datecr,agencyrefno,appversion)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,getDate(),?,?)";
    private static final String SI_SUMMARY_SP = "exec usp_ecomSISummaryNew ?";
    private static final String INSERTECOMFILESQRY = "insert into ecomSI_files(eSINo,FileCatgeory,FileName) "
            + "values(?,?,?)";
    private static final String SI_DRAFT_UPDATE = "update ecomSI set status=?,custremarks=?,plaHeaderName=?,"
            + "polHeaderName=?,podHeaderName=?,pldHeaderName=?,Voyage=?,ccmailid=?,shipper=?,shippername=?,consigee=?,consigeename=?,notify=?,notifyname=?,AESNumber=?,AESDiscliamer=?,releasetype=?,declaredvalue=?,blType=?,ChgUser=?,ChgDate=getdate(),ACID=?,Freight=?,cargoprotectid=?,t2lfiling=?,packinglst=?,invoiceupload=?,telexrls=?,enstype=?,bltype_UAE=?,mpci_id=?,nvocc=? where eSINo=? and "
            + "bookno=?";
    private static final String SI_FILES_UPDATE_QRY = "update ecomSI_files set FileName=? where eSINo = ? and "
            + "FileCatgeory=?";
    private static final String SI_FILES_DELETE_QRY = "delete from ecomSI_files where eSINo=?";
    private static final String GET_ECOMSI_CARGO_DET = " select eqpSize,eqpType,containerno,carrierSeal,ShipperSeal,conId,shipperOwn,"
            + "cargoWeight,tareWeight,grossweight,ref_nor,eqpstatus,dgflag,rate_eqpTypeid from ecomSI_cargo (nolock) where "
            + "eSINo =? order by conid asc";
    private static final String GET_ECOMSI_CMDTY_DET = " select commodity, packageQty, itemWeight, itemWeightUnit,"
            + " package, itemVolume, itemVolumeUnit, marksNo, marksDesc,hscommodity,hazardous,nettwt,hssuffixcode,itemno,bycode,byname,slcode,slname,cmdty_hblno "
            + " from ecomSI_commodity  (nolock) where  esino=? and containerno=?";
    private static final String GET_ECOMSI_REF_DET = "select eSIno,containerno,conId,ref_maxTemp,ref_setTemp,ref_minTemp,"
            + "ref_tempUnit,humidity,humidity_flag,drains,co2value,o2value,ventpercentage from ecomSI_reefer  where eSIno=? and containerno=?";
    private static final String GET_SI_DECLINED_REMARKS = "select declined_remarks from ecomsi where status='V' and esino=?";
    private static final String GET_SI_MASTER_QRY = "select plaHeaderName,polHeaderName,podHeaderName,"
            + "pldHeaderName,Voyage,aesnumber,aesdiscliamer,releasetype,declaredvalue,bltype,ACID,Freight,cargoprotectid,t2lfiling,packinglst,invoiceupload,telexrls,enstype,bltype_UAE,mpci_id,nvocc From ecomSI(nolock) where eSINo=?";
    private static final String GET_ECOM_SI_FILE = "select FileName,FileCatgeory from  ecomSI_files where eSINo=?";
    private static final String GET_ECOM_SI_REMARKS = "select custremarks,bookno,ccmailid,shipper,shippername,consigee,consigeename,notify,notifyname from  ecomSI where eSINo=?";
    private static final String GET_ECOM_SI_CUST = "select name,custId,custTypeId,address1,address2,city,countryId,email,"
            + "telephone,fax,toorder,flag,stateid,eori,persontype,zipcode from ecomsi_cust where eSINo = ?";
    private static final String ECOM_SI_REQUEST_INSERT = "insert into ecomSIRequestMaster(reqID, userId,requestedSI, createdSI, bookno,custcode,"
            + "crUser,crDate,chgUser,chgDate) values(?,?,?,?,?,?,?,getdate(),?,getdate())";
    private static final String GET_ECOM_SI_STATUS = "select status From ecomsi where esino=?";
    private static final String UPDATE_ECOM_SI_STATUS = "update ecomsi set status='I' where esino=?";
    private static final String DELETE_SI_REQUEST = "delete from ecomsirequestmaster where bookno=?";
    // V1 qry
    private static final String V1_SI_BOOKING_SP = "exec usp_ecomBookingSiSummary ?";
    private static final String GETECOM_BOOKNO = "select ebookno,ccmailid From ecombooking where extbookno=?";
    private static final String SAVE_CCMAIN_USERDET = "update ecomuserdetails set ccmailid=? where emailid=?";
    protected static final String GET_MAIL_CONTENT_LANG = "select message,from_mailid,subject,activitycode from ecom_messageformatlang(nolock) where Status='a' and Type ='mail' and criteria = ? and Module =? and usertype = ? and lang=?";
    private static final String SI_CHARGES = "select ec.esino,ec.chargeid,c.chargename,ec.payagencyid,ec.custtypeid,ec.prepaidcollect,ec.eqptypeid from  ecomsi_charges ec  inner join charges c on ec.chargeid = c.chargecode where c.status='A' and ec.esino=?";

    private static final String INSERT_SI_CHARGES = "insert into ecomsi_charges (esino,chargeid,payagencyid,custtypeid,prepaidcollect,eqptypeid) values (?,?,?,?,?,?)";
    private static final String DELETE_SI_CHARGES = "delete from ecomsi_charges where esino=?";

    private static final String INSERT_SI_ENSDEC = "INSERT INTO ecomsi_ics_new "
            + "(eSINo, eucustom, bill_type, ens_type, payment_mode, cust_name, "
            + "address, address1, country, state, telephone, email, enseori, filing_type, city,zipcode) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

    private static final String DELETE_SI_ENSDEC = "delete from ecomsi_ics_new where esino=?";

    private static final String DELETE_REEFER = "delete from ecomSI_reefer where eSINo=?";
    private static final String DELETE_OOG = "delete from ecomSI_oog where eSINo=?";
    private static final String DELETE_DG = "delete from ecomSI_dg where eSINo=?";
    private static final String GET_AP_CUSTCODE = "select custId  from booking_cust(nolock) where book_no=? and custtypeid='AP'";

    private static final String SI_ENSDeclarant = "SELECT eucustom,bill_type,ens_type,payment_mode,cust_name,address,address1,city,country,state,telephone,enseori,email,filing_type,zipcode FROM ecomsi_ics_new(nolock) where eSINo=?";

    private String converUtilDateToString(java.util.Date utilDate) {
        String strDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.ms");
        SimpleDateFormat cdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.ms");
        String convertBookDate = dateFormat.format(utilDate);
        try {
            strDate = cdf.format(dateFormat.parse(convertBookDate));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strDate;
    }

    @Override
    public ArrayList getSISummary(String strUserId, String custTypeConfig, String v1DataEnableStatus,
            String SICreForCnfrmBk) throws RemoteException {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection Ecommcon = null, con = null;
        int iRandomNo = 0;
        String doc_res = null, appversion = null;
        SIsummaryVO SIvo = null;
        String randomid = null, status = null, blstatus = null, strLoadPort = null, strDischargePort = null,
                strDate = null, strTrustBookNo = null, service = null;
        String strVesselCode = null, strVoyageCode = null, bound = null, bpCustId = null, bpName = null,
                apCustId = null, apName = null, refval = null;
        String strPla = null, strPld = null, strPolTerminal = null, strPodTerminal = null, strBlNo = null,
                strBpCustTypeId = null, strApCustTypeId, bkstatus = null;
        ArrayList SIsummaryList = new ArrayList();
        try {
            Ecommcon = new lrp.serverutils.refs.ServerObject("ReportDB").getConnection();
            //
            /*
             * Date currDate = new Date();
             * Date fromDate = new Date();
             * Date toDate = new Date();
             * fromDate.setDate(currDate.getDate() - 60);
             * toDate.setDate(currDate.getDate() + 30);
             * 
             * String strFromDate = converUtilDateToString(fromDate);
             * String strToDate = converUtilDateToString(toDate);
             * strFromDate = strFromDate.substring(0, strFromDate.length() - 1);
             * strToDate = strToDate.substring(0, strToDate.length() - 1);
             * String strFromAndToDate = strFromDate + "," + strToDate;
             * System.out.println("[ECOM] EcomShippingInstructionBean ===strUserId======>" +
             * strUserId);
             * System.out.
             * println("[ECOM] EcomShippingInstructionBean ===strFromAndToDate======>" +
             * strFromAndToDate);
             * custTypeConfig = "AP,BP";
             * bkstatus = "Y".equalsIgnoreCase(SICreForCnfrmBk) ? "C" : "D,C";
             * CompleteModel siModelObj = siVDS.getecombookingsisummary(strUserId,
             * strFromAndToDate, custTypeConfig, bkstatus);
             * String allBooknums = "";
             * for (Booking siVoObj : siModelObj.getBooking()) {
             * allBooknums = siVoObj.getBook_no() + "," + allBooknums;
             * }
             * /*-----------------------Get Master Details Done
             * Here--------------------------------------
             */
 /*
             * if (allBooknums != null && allBooknums.length() > 0) {
             * allBooknums = allBooknums.substring(0, allBooknums.length() - 1);
             * }
             * CompleteModel siModelForBlStatus = siVDS.getblstatusforsi(allBooknums);
             * HashMap blStatusHM = new HashMap();
             * for (Bl siBlStatusObj : siModelForBlStatus.getBl()) {
             * blStatusHM.put(siBlStatusObj.getBl_no(), siBlStatusObj.getBook_no() + "~~" +
             * siBlStatusObj.getStatus());
             * }
             * /*-----------------------Get blstatus Done
             * Here--------------------------------------------
             */
 /*
             * iRandomNo = get4DigitRandomNumber();
             * 
             * randomid = String.valueOf(iRandomNo);
             * System.out.
             * println("[ECOM] EcomShippingInstructionBean ====randomid is======>" +
             * randomid);
             * for (Booking siVoObj : siModelObj.getBooking()) {
             * status = siVoObj.getStatus();
             * blstatus = "";
             * strBlNo = "";
             * Iterator itr = blStatusHM.keySet().iterator();
             * while (itr.hasNext()) {
             * String keyBlNum = (String) itr.next();
             * String value = (String) blStatusHM.get(keyBlNum);
             * String temp1[] = value.split("~~");
             * if (temp1[0] != null && temp1[0].equalsIgnoreCase(siVoObj.getBook_no())) {
             * blstatus = temp1[1];
             * strBlNo = keyBlNum;
             * }
             * if (blstatus != null && "D".equalsIgnoreCase(blstatus)) {
             * break;
             * }
             * }
             * 
             * strLoadPort = siVoObj.getLoadportid();
             * strDischargePort = siVoObj.getDischargeportid();
             * strDate = siVoObj.getPolat();
             * strTrustBookNo = siVoObj.getBook_no();
             * service = siVoObj.getServicecode();
             * strVesselCode = siVoObj.getVesselcode();
             * strVoyageCode = siVoObj.getVoyagecode();
             * bound = siVoObj.getBound();
             * bpCustId = siVoObj.getCustid();
             * bpName = siVoObj.getName();
             * strBpCustTypeId = siVoObj.getCusttypeid();
             * strApCustTypeId = "";
             * apCustId = "";
             * apName = "";
             * strPla = siVoObj.getOriginportid();
             * strPld = siVoObj.getFinalportid();
             * strPolTerminal = siVoObj.getPolterminal();
             * strPodTerminal = siVoObj.getPodterminal();
             * 
             * refval = siVoObj.getCustrefno();
             * 
             * pst = Ecommcon.prepareStatement(INSERT_ECOM_SI_GEN);
             * pst.setString(1, randomid);
             * pst.setString(2, status);
             * pst.setString(3, blstatus);
             * pst.setString(4, strLoadPort);
             * pst.setString(5, strDischargePort);
             * pst.setString(6, strDate);
             * pst.setString(7, strTrustBookNo);
             * pst.setString(8, service);
             * pst.setString(9, strVesselCode);
             * pst.setString(10, strVoyageCode);
             * pst.setString(11, bound);
             * pst.setString(12, bpCustId);
             * pst.setString(13, bpName);
             * pst.setString(14, strBpCustTypeId);
             * pst.setString(15, strApCustTypeId);
             * pst.setString(16, apCustId);
             * pst.setString(17, apName);
             * pst.setString(18, strPla);
             * pst.setString(19, strPld);
             * pst.setString(20, strPolTerminal);
             * pst.setString(21, strPodTerminal);
             * pst.setString(22, strBlNo);
             * pst.setString(23, refval);
             * pst.setString(24, siVoObj.getAgencyRefNo());
             * pst.setInt(25, 2);
             * pst.executeUpdate();
             * }
             * if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
             * //Inserver v1 data into summary generation table
             * con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
             * pst = con.prepareStatement(V1_SI_BOOKING_SP);
             * pst.setString(1, strUserId);
             * rs = pst.executeQuery();
             * pst = Ecommcon.prepareStatement(INSERT_ECOM_SI_GEN);
             * while (rs.next()) {
             * status = rs.getString("bkstatus");
             * blstatus = rs.getString("blstatus");
             * strLoadPort = rs.getString("loadPortId");
             * strDischargePort = rs.getString("dischargePortId");
             * strDate = rs.getString("polAt");
             * strTrustBookNo = rs.getString("book_no");
             * service = rs.getString("servicecode");
             * strVesselCode = rs.getString("vesselcode");
             * strVoyageCode = rs.getString("voyagecode");
             * bound = rs.getString("bound");
             * bpCustId = rs.getString("bpCustId");
             * bpName = rs.getString("bpName");
             * strBpCustTypeId = rs.getString("bpcustTypeId");
             * strApCustTypeId = rs.getString("apcustTypeId");
             * apCustId = rs.getString("apCustId");
             * apName = rs.getString("apName");
             * strPla = rs.getString("originPortId");
             * strPld = rs.getString("finalPortId");
             * strPolTerminal = rs.getString("polTerminal");
             * strPodTerminal = rs.getString("podTerminal");
             * strBlNo = rs.getString("bl_no");
             * refval = rs.getString("custRefNo");
             * 
             * pst = Ecommcon.prepareStatement(INSERT_ECOM_SI_GEN);
             * pst.setString(1, randomid);
             * pst.setString(2, status);
             * pst.setString(3, blstatus);
             * pst.setString(4, strLoadPort);
             * pst.setString(5, strDischargePort);
             * pst.setString(6, strDate);
             * pst.setString(7, strTrustBookNo);
             * pst.setString(8, service);
             * pst.setString(9, strVesselCode);
             * pst.setString(10, strVoyageCode);
             * pst.setString(11, bound);
             * pst.setString(12, bpCustId);
             * pst.setString(13, bpName);
             * pst.setString(14, strBpCustTypeId);
             * pst.setString(15, strApCustTypeId);
             * pst.setString(16, apCustId);
             * pst.setString(17, apName);
             * pst.setString(18, strPla);
             * pst.setString(19, strPld);
             * pst.setString(20, strPolTerminal);
             * pst.setString(21, strPodTerminal);
             * pst.setString(22, strBlNo);
             * pst.setString(23, refval);
             * pst.setString(24, "");
             * pst.setInt(25, 1);
             * pst.executeUpdate();
             * }
             * }
             */
            //
            pst = Ecommcon.prepareStatement(SI_SUMMARY_SP);
            pst.setString(1, strUserId);
            rs = pst.executeQuery();
            //
            //
            while (rs.next()) {
                SIvo = new SIsummaryVO();
                SIvo.setBookno(checkNull(rs.getString("book_no")));
                SIvo.setDepdate(checkNull(rs.getString("polAt")));
                SIvo.setPol(checkNull(rs.getString("loadportid")));
                SIvo.setPod(checkNull(rs.getString("dischargeportid")));
                SIvo.setStatus(checkNull(rs.getString("sistatus")));
                SIvo.setOriginPort(checkNull(rs.getString("originPortId")));
                SIvo.setDeliveryPort(checkNull(rs.getString("finalPortId")));
                if (rs.getString("docid") != null && rs.getString("docid").length() > 0) {
                    doc_res = GeneralConf.SI_PREFIX + rs.getString("docid");
                } else {
                    doc_res = "";
                }
                SIvo.setSI_no(doc_res);
                // SIvo.setDocid(rs.getInt("docid"));
                SIvo.setSI_no(rs.getString("docid"));
                SIvo.setBl_no(checkNull(rs.getString("bl_no")));
                SIvo.setCountReq(rs.getInt("requestedsi"));
                SIvo.setCountCre(rs.getInt("createdsi"));
                SIvo.setBl_status(checkNull(rs.getString("blstatus")));
                SIvo.setAgencyRefNo(checkNull(rs.getString("agencyrefno")));
                // System.out.println("[ECOM] EcomShippingInstructionBean >>>blstatus>>>" +
                // rs.getString("blstatus"));
                SIvo.setBookstatus(checkNull(rs.getString("bkstatus")));
                appversion = checkNull(rs.getString("appversion"));
                if ("1".equalsIgnoreCase(appversion)) {
                    SIvo.setAppVersion("1");
                } else {
                    SIvo.setAppVersion("2");
                }
                SIvo.setIssilocked(checkNull(rs.getString("issilocked")));
                SIvo.setDatecr(checkNull(rs.getString("datecr")));
                SIvo.setDateup(checkNull(rs.getString("dateup")));
                int requestcount = rs.getInt("requestedsi");
                int createcount = rs.getInt("createdsi");
                int cnt = requestcount - createcount;
                SIsummaryList.add(SIvo);
                if (cnt > 0) {
                    for (int i = 0; i < cnt - 1; i++) {
                        SIvo.setStatus("Create Multiple SI");
                        SIsummaryList.add(SIvo.clone());
                    }
                }
            }
            SISummaryComparator objcmp = new SISummaryComparator();
            Collections.sort(SIsummaryList, objcmp);
            //
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (Ecommcon != null) {
                    Ecommcon.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SIsummaryList;
    }

    public int get4DigitRandomNumber() {
        int upperLimit = 10000;
        int s = getRandomNumber(upperLimit);
        return s;
    }

    public int getRandomNumber(int maxLimit) {
        return (int) ((Math.random() * maxLimit) * (Math.random() * maxLimit));
    }
    // for customer search

    @Override
    @SuppressWarnings("static-access")
    public List getSICustomerInfo(String columnName, String constraints, String columnValue, String custCode)
            throws RemoteException {
        System.out.println("[ECOM] EcomShippingInstructionBean inside getSICustomerInfo--->");
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
                            profile.setCustId(checkNull((String) rs.get("custID")));
                            profile.setCustName(checkNull((String) rs.get("name")));
                            profile.setAddr1(checkNull((String) rs.get("address1")));
                            profile.setAddr2(checkNull((String) rs.get("address2")));
                            profile.setCity(checkNull((String) rs.get("city")).toUpperCase());
                            profile.setCountry(checkNull((String) rs.get("countryId")).toUpperCase());
                            profile.setCountryName(checkNull((String) rs.get("countryName")).toUpperCase());
                            if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                                profile.setTeleNo(phone);
                            } else {
                                profile.setTeleNo("");
                            }
                            if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                                profile.setFax(fax);
                            } else {
                                profile.setFax("");
                            }
                            email = checkNull((String) rs.get("email"));
                            if (email.length() > 0 && email.equalsIgnoreCase("-")) {
                                email = "";
                            }
                            profile.setEmailId(checkNull((String) rs.get("email")));
                            profile.setCitycode(checkNull((String) rs.get("city")).toUpperCase());
                            profile.setVatno(checkNull((String) rs.get("vatno")));
                            //
                            profile.setEnseori(checkNull((String) rs.get("eori")));
                            profile.setTypeofperson(checkNull((String) rs.get("persontype")));
                            parties.add(profile);
                        }
                    }
                }
            } // END SH
        } catch (Exception e) {
            System.out.println("[ECOM] EcomShippingInstructionBean Exception" + e);
        }
        return parties;
    }

    @Override
    public List getSIMultifilingCustomerInfo(String custCode, String custTypeConfig) throws RemoteException {
        System.out.println("[ECOM] EcomShippingInstructionBean getSIMultifilingCustomerInfo------");
        List parties = new ArrayList();
        String result = "";
        CustomerProfile profile = null;
        String phone = null, fax = null, email = null;
        SvmJsonList alist = null;
        Map rethm = null, ret2 = null;
        List vlist = null;
        try {
            alist = new SvmJsonList();
            alist.add(custCode);
            alist.add(custTypeConfig);
            System.out.println("[ECOM] EcomShippingInstructionBean alist----" + alist);
            rethm = siVDS.getEcomsiMultiFilingCustSearch(alist);
            System.out.println("[ECOM] EcomShippingInstructionBean rethm------" + rethm);
            if (rethm != null && rethm.size() > 0) {
                ret2 = (Map) rethm.get("rs");
                if (ret2 != null && ret2.size() > 0) {
                    vlist = (List) ret2.get("rs0");
                    if (vlist != null && vlist.size() > 0) {
                        for (int i = 0; i < vlist.size(); i++) {
                            Map rs = (Map) vlist.get(i);
                            phone = checkNull((String) rs.get("telephone"));
                            profile = new CustomerProfile();
                            profile.setCustName(checkNull((String) rs.get("cust_name")));
                            profile.setAddr1(checkNull((String) rs.get("address")));
                            profile.setAddr2(checkNull((String) rs.get("address1")));
                            profile.setCity(checkNull((String) rs.get("city")).toUpperCase());
                            profile.setCountry(checkNull((String) rs.get("country")).toUpperCase());
                            profile.setState(checkNull((String) rs.get("state")).toUpperCase());
                            if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                                profile.setTeleNo(phone);
                            } else {
                                profile.setTeleNo("");
                            }
                            email = checkNull((String) rs.get("email"));
                            if (email.length() > 0 && email.equalsIgnoreCase("-")) {
                                email = "";
                            }
                            profile.setEmailId(checkNull((String) rs.get("email")));
                            profile.setEnseori(checkNull((String) rs.get("enseori")));
                            profile.setZipcode(checkNull((String) rs.get("zipcode")));
                            profile.setFilingtype(checkNull((String) rs.get("filing_type")));
                            parties.add(profile);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EcomShippingInstructionBean Exception" + e);
        }
        return parties;
    }

    public String setConstraints(String columnName, String con, String Val) throws RemoteException {
        String result = "";
        //
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
    //

    private String checkNull(String str) {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str)) && !"null".equalsIgnoreCase(str)) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private String resolveNvoccForDb(String nvocc) {
        nvocc = checkNull(nvocc);
        if (nvocc.length() == 0) {
            return null;
        }
        return "Y".equalsIgnoreCase(nvocc) ? "Y" : "N";
    }

    @Override
    public ReturnVO saveToSI(String bookno, String custcode, String bookinguserid,
            Map<SIMasterVO, SIConatainerValueVO> cntMapObj, HashMap hm, SIPartiesVO partiesVO, String savetype,
            String sino, ArrayList voidCargoObj, ArrayList voidComObj, String username1, String tbl_no,
            String client_code, FileDetailsVO fileDetailsVO, String addremarks, String custTypeConfig,
            String appVersion, String ccMailId, String isCCMail, String lang, String custCountry, Map custMap)
            throws RemoteException {
        Connection ecomConn = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ReturnVO returnVO = new ReturnVO();
        String strReturn = "";
        String cntno = "", cntType = "", seal1No = "", seal2No = "", commodity = "", cmdId = "", cmdDesc = "", wt = "",
                unit = "", qty = "", cargo_id = "", eqp_type = "";
        int i = 0, alsize = 0;
        int cargoId = 0, cargoNo = 0, cntListSize = 0, cmdIndex = 0, pkgIndex = 0;
        String bknum = "";
        String retFilePath = "";
        String agencyid = "", oceansercode = "", serviceCode = "", polTerminal = "", podTerminal = "";
        String oceanvslcode = null;
        String oceanvoyage = null;
        String oceanBound = null;
        String bookDate = "";
        String bookReady = "";
        String booktype = "", preCarriage = "", onCarriage = "", usercr = "";
        String transportserviceid = "", callid = "", firstsvctype = "", shipment_type = "", crossbook = "",
                prepareops = "";
        String preparebl = "", isdgbook = "", priority = "", booksource = "", originportid = "", finalPortId = "";
        Date datecr = null;
        String strDelivery = "", ediid = "";
        String msgtype = "";
        String Si_Type = "";
        String Ecom_SaveType = "";
        String packge = "", packgeId = "", volume = "", netweight = "", volumeUnt = "", marks = "", description = "", bycode = "", byname = "", slcode = "", slname = "", cmdty_hblno = "";
        savetype = checkNull(savetype);
        sino = checkNull(sino);
        StringBuffer hssb = new StringBuffer();
        String strSOC = "", documentalAgencyid = "", soc = "";
        String siSAVETYPE = "", originportdesc = "", finalportdesc = "", loadportdesc = "", dischargeportdesc = "",
                rateeqptypeid = "";
        String siStatus = "";
        boolean isconvertzip = false;
        String saveFilePath = null;
        try {

            ecomConn = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            System.out.println(
                    "[ECOM] EcomShippingInstructionBean ===savetype is===>" + savetype + "====sino is====>" + sino);
            if (savetype.length() > 0) {
                if ("D".equalsIgnoreCase(savetype)) {
                    Si_Type = "D";
                    siStatus = "D";
                } else if ("S".equalsIgnoreCase(savetype)) {
                    Si_Type = "S";
                    msgtype = "insert";
                    siSAVETYPE = "S";
                    siStatus = "S";
                }
                if (sino.length() > 0) {
                    Ecom_SaveType = "DraftUpdate";
                    siSAVETYPE = "DU";
                    msgtype = "Draft_up";
                } else {
                    Ecom_SaveType = "DraftInsert";
                    siSAVETYPE = "DI";
                    msgtype = "Draft_in";
                }
                // update in userdetails if cc mail id newly added
                if ("YES".equalsIgnoreCase(isCCMail)) {
                    ps = ecomConn.prepareStatement(SAVE_CCMAIN_USERDET);
                    ps.setString(1, checkNull(ccMailId));
                    ps.setString(2, checkNull(username1));
                    ps.executeUpdate();
                }
                System.out.println("[ECOM] EcomShippingInstructionBean freightoptionval: " + partiesVO.getFreight()
                        + ", Ecom_SaveType: " + Ecom_SaveType + ", siSAVETYPE: " + siSAVETYPE
                        + ", msgtype: " + msgtype + ", Si_Type: " + Si_Type);

                // STATE CUST CHECK
                SISubmitVO objcust = (SISubmitVO) hm.get("SH");
                objcust.setState(getStateCode(objcust.getState(), objcust.getCountryid(), bookno));
                hm.put("SH", objcust);
                objcust = (SISubmitVO) hm.get("CN");
                objcust.setState(getStateCode(objcust.getState(), objcust.getCountryid(), bookno));
                hm.put("CN", objcust);
                objcust = (SISubmitVO) hm.get("NT");
                objcust.setState(getStateCode(objcust.getState(), objcust.getCountryid(), bookno));
                hm.put("NT", objcust);
                objcust = (SISubmitVO) hm.get("N1");
                objcust.setState(getStateCode(objcust.getState(), objcust.getCountryid(), bookno));
                hm.put("N1", objcust);
                objcust = (SISubmitVO) hm.get("N2");
                objcust.setState(getStateCode(objcust.getState(), objcust.getCountryid(), bookno));
                hm.put("N2", objcust);
                objcust = (SISubmitVO) hm.get("FW");
                objcust.setState(getStateCode(objcust.getState(), objcust.getCountryid(), bookno));
                hm.put("FW", objcust);
                objcust = (SISubmitVO) hm.get("BY");
                objcust.setState(getStateCode(objcust.getState(), objcust.getCountryid(), bookno));
                hm.put("BY", objcust);
                objcust = (SISubmitVO) hm.get("SL");
                objcust.setState(getStateCode(objcust.getState(), objcust.getCountryid(), bookno));
                hm.put("SL", objcust);
                //
                if ("DraftInsert".equalsIgnoreCase(Ecom_SaveType)) {
                    saveSI(1, bookno, custcode, username1);
                    // save_doc_id
                    String str_sino = bookNoGenerate("SI");
                    bknum = str_sino;
                    // save_docID = Integer.parseInt(str_sino);
                    returnVO.setStrSaveType("D");
                    int editbl = 0;
                    int blVersion = 0;
                    ps = ecomConn.prepareStatement(SAVE_SI_MASTER_DET);
                    ps.setString(1, str_sino);
                    ps.setString(2, null);
                    ps.setString(3, bookno);
                    ps.setString(4, partiesVO.getVesselcode());
                    ps.setString(5, partiesVO.getVoyagecode());
                    ps.setString(6, partiesVO.getBound());
                    ps.setString(7, partiesVO.getPol());
                    ps.setString(8, partiesVO.getPod());
                    ps.setString(9, partiesVO.getPol());
                    ps.setString(10, partiesVO.getPod());
                    ps.setString(11, custcode);
                    ps.setString(12, username1);
                    ps.setString(13, null);
                    ps.setString(14, null);
                    ps.setInt(15, editbl);
                    ps.setInt(16, blVersion);
                    ps.setString(17, siStatus);
                    ps.setString(18, addremarks);
                    ps.setString(19, addremarks);
                    ps.setString(20, partiesVO.getPla_free_txt());
                    ps.setString(21, partiesVO.getPol_free_txt());
                    ps.setString(22, partiesVO.getPod_free_txt());
                    ps.setString(23, partiesVO.getPld_free_txt());
                    if ("1".equalsIgnoreCase(appVersion)) {
                        ps.setInt(24, 1);
                    } else {
                        ps.setInt(24, 2);
                    }
                    ps.setString(25, checkNull(ccMailId));
                    ps.setString(26, checkNull(partiesVO.getShipperHeader()));
                    ps.setString(27, checkNull(partiesVO.getShipperName()));
                    ps.setString(28, checkNull(partiesVO.getConsigneeHeader()));
                    ps.setString(29, checkNull(partiesVO.getConsigneeName()));
                    ps.setString(30, checkNull(partiesVO.getNotifyHeader()));
                    ps.setString(31, checkNull(partiesVO.getNotifyName()));
                    ps.setString(32, checkNull(partiesVO.getAESNumber()));
                    ps.setString(33, checkNull(partiesVO.getAESDiscliamer()));
                    ps.setString(34, checkNull(partiesVO.getReleasetype()));
                    ps.setString(35, checkNull(partiesVO.getDeclaredValue()));
                    ps.setString(36, checkNull(partiesVO.getBltype()));
                    ps.setString(37, checkNull(partiesVO.getACID()));
                    ps.setString(38, checkNull(partiesVO.getFreight()));
                    ps.setString(39, checkNull(partiesVO.getCargoprotectid()));
                    ps.setString(40, checkNull(partiesVO.getEbook_t2lfiling()));
                    ps.setString(41, checkNull(partiesVO.getPackinglst()));
                    ps.setString(42, checkNull(partiesVO.getInvoiceupload()));
                    ps.setString(43, checkNull(partiesVO.getOrgTelType()));
                    ps.setString(44, checkNull(partiesVO.getEbook_EnsDocType()));
                    ps.setString(45, checkNull(partiesVO.getBltype_UAE()));
                    ps.setString(46, checkNull(partiesVO.getMpci_id()));
                    ps.setString(47, resolveNvoccForDb(partiesVO.getEbook_nvocc()));
                    ps.executeUpdate();

                    // Insert SI Cust details.
                    SISubmitVO objSH = (SISubmitVO) hm.get("SH");
                    System.out.println("[ECOM] EcomShippingInstructionBean SH===**==> " + checkNull(objSH.getCustCode())
                            + " ~~ " + checkNull(objSH.getCountryid()) + " ~~ " + checkNull(objSH.getCustname())
                            + " ~~ " + checkNull(objSH.getAddress()) + " ~~ " + checkNull(objSH.getAddress1()) + " ~~ "
                            + checkNull(objSH.getCity()) + " ~~ " + checkNull(objSH.getZipCode()) + " ~~ "
                            + checkNull(objSH.getTelephone()) + " ~~ " + checkNull(objSH.getFax()) + " ~~ "
                            + checkNull(objSH.getEmail()) + " ~~ " + checkNull(objSH.getState()));

                    ps = ecomConn.prepareStatement(SAVE_SI_CUST_DET);
                    ps.setString(1, str_sino);
                    ps.setString(2, "SH");
                    ps.setString(3, checkNull(objSH.getCustCode()));
                    ps.setString(4, checkNull(objSH.getCountryid()));
                    ps.setString(5, checkNull(objSH.getCustname()));
                    ps.setString(6, checkNull(objSH.getAddress()));
                    ps.setString(7, checkNull(objSH.getAddress1()));
                    ps.setString(8, checkNull(objSH.getCity()));
                    ps.setString(9, checkNull(objSH.getZipCode()));
                    ps.setString(10, checkNull(objSH.getTelephone()));
                    ps.setString(11, checkNull(objSH.getFax()));
                    ps.setString(12, checkNull(objSH.getEmail()));
                    ps.setString(13, null);
                    ps.setString(14, null);
                    ps.setString(15, checkNull(objSH.getState()));
                    ps.setString(16, checkNull(objSH.getEori()));
                    ps.setString(17, checkNull(objSH.getPersontype()));
                    ps.executeUpdate();

                    // consignee
                    ps = ecomConn.prepareStatement(SAVE_SI_CUST_DET);
                    SISubmitVO objCN = (SISubmitVO) hm.get("CN");

                    System.out.println("[ECOM] EcomShippingInstructionBean SH===**==> " + checkNull(objCN.getCustCode())
                            + " ~~ " + checkNull(objCN.getCountryid()) + " ~~ " + checkNull(objCN.getCustname())
                            + " ~~ " + checkNull(objCN.getAddress()) + " ~~ " + checkNull(objCN.getAddress1()) + " ~~ "
                            + checkNull(objCN.getCity()) + " ~~ " + checkNull(objCN.getZipCode()) + " ~~ "
                            + checkNull(objCN.getTelephone()) + " ~~ " + checkNull(objCN.getFax()) + " ~~ "
                            + checkNull(objCN.getEmail()) + " ~~ " + checkNull(objCN.getState()));

                    ps.setString(1, str_sino);
                    ps.setString(2, "CN");
                    ps.setString(3, checkNull(objCN.getCustCode()));
                    ps.setString(4, checkNull(objCN.getCountryid()));
                    ps.setString(5, checkNull(objCN.getCustname()));
                    ps.setString(6, checkNull(objCN.getAddress()));
                    ps.setString(7, checkNull(objCN.getAddress1()));
                    ps.setString(8, checkNull(objCN.getCity()));
                    ps.setString(9, checkNull(objCN.getZipCode()));
                    ps.setString(10, checkNull(objCN.getTelephone()));
                    ps.setString(11, checkNull(objCN.getFax()));
                    ps.setString(12, checkNull(objCN.getEmail()));
                    ps.setString(13, null);
                    if ("Y".equalsIgnoreCase(objCN.getOrdercheck())) {
                        ps.setString(14, "Y");
                    } else {
                        ps.setString(14, "N");
                    }
                    ps.setString(15, checkNull(objCN.getState()));
                    ps.setString(16, checkNull(objCN.getEori()));
                    ps.setString(17, checkNull(objCN.getPersontype()));
                    ps.executeUpdate();

                    // notifynotify
                    SISubmitVO objNT = (SISubmitVO) hm.get("NT");
                    System.out.println("[ECOM] EcomShippingInstructionBean SH===**==> " + checkNull(objNT.getCustCode())
                            + " ~~ " + checkNull(objNT.getCountryid()) + " ~~ " + checkNull(objNT.getCustname())
                            + " ~~ " + checkNull(objNT.getAddress()) + " ~~ " + checkNull(objNT.getAddress1()) + " ~~ "
                            + checkNull(objNT.getCity()) + " ~~ " + checkNull(objNT.getZipCode()) + " ~~ "
                            + checkNull(objNT.getTelephone()) + " ~~ " + checkNull(objNT.getFax()) + " ~~ "
                            + checkNull(objNT.getEmail()) + " ~~ " + checkNull(objNT.getState()));
                    if (objNT.getCustname() != null && objNT.getCustname().length() > 0
                            && checkNull(partiesVO.getNotifyName()).length() > 0) {
                        ps = ecomConn.prepareStatement(SAVE_SI_CUST_DET);
                        ps.setString(1, str_sino);
                        ps.setString(2, "NT"); // NT
                        ps.setString(3, checkNull(objNT.getCustCode()));
                        ps.setString(4, checkNull(objNT.getCountryid()));
                        ps.setString(5, checkNull(objNT.getCustname()));
                        ps.setString(6, checkNull(objNT.getAddress()));
                        ps.setString(7, checkNull(objNT.getAddress1()));
                        ps.setString(8, checkNull(objNT.getCity()));
                        ps.setString(9, checkNull(objNT.getZipCode()));
                        ps.setString(10, checkNull(objNT.getTelephone()));
                        ps.setString(11, checkNull(objNT.getFax()));
                        ps.setString(12, checkNull(objNT.getEmail()));
                        String NotifyCheck = "";
                        if ("Y".equalsIgnoreCase(objNT.getNotifyCheck())) {
                            NotifyCheck = "Y";
                        } else {
                            NotifyCheck = "N";
                        }
                        ps.setString(13, checkNull(NotifyCheck));
                        if ("Y".equalsIgnoreCase(objNT.getOrdercheck())) {
                            ps.setString(14, "Y");
                        } else {
                            ps.setString(14, "N");
                        }
                        ps.setString(15, checkNull(objNT.getState()));
                        ps.setString(16, checkNull(objNT.getEori()));
                        ps.setString(17, checkNull(objNT.getPersontype()));
                        ps.executeUpdate();
                    }
                    // Notify2
                    SISubmitVO objNT2 = (SISubmitVO) hm.get("N1");
                    if (objNT2.getCustname() != null && objNT2.getCustname().length() > 0) {
                        ps = ecomConn.prepareStatement(SAVE_SI_CUST_DET);
                        ps.setString(1, str_sino);
                        ps.setString(2, "N1"); // NT
                        ps.setString(3, checkNull(objNT2.getCustCode()));
                        ps.setString(4, checkNull(objNT2.getCountryid()));
                        ps.setString(5, checkNull(objNT2.getCustname()));
                        ps.setString(6, checkNull(objNT2.getAddress()));
                        System.out.println(
                                "[ECOM] EcomShippingInstructionBean objNT2.getAddress()::::" + objNT2.getAddress());
                        ps.setString(7, checkNull(objNT2.getAddress1()));
                        ps.setString(8, checkNull(objNT2.getCity()));
                        ps.setString(9, checkNull(objNT2.getZipCode()));
                        ps.setString(10, checkNull(objNT2.getTelephone()));
                        ps.setString(11, checkNull(objNT2.getFax()));
                        ps.setString(12, checkNull(objNT2.getEmail()));
                        ps.setString(13, objNT2.getNotifyCheck());
                        ps.setString(14, objNT2.getOrdercheck());
                        ps.setString(15, checkNull(objNT2.getState()));
                        ps.setString(16, checkNull(objNT2.getEori()));
                        ps.setString(17, checkNull(objNT2.getPersontype()));
                        ps.executeUpdate();
                    }
                    // Notify3
                    SISubmitVO objNT3 = (SISubmitVO) hm.get("N2");
                    if (objNT3.getCustname() != null && objNT3.getCustname().length() > 0) {
                        ps = ecomConn.prepareStatement(SAVE_SI_CUST_DET);
                        ps.setString(1, str_sino);
                        ps.setString(2, "N2"); // N3
                        ps.setString(3, checkNull(objNT3.getCustCode()));
                        ps.setString(4, checkNull(objNT3.getCountryid()));
                        ps.setString(5, checkNull(objNT3.getCustname()));
                        ps.setString(6, checkNull(objNT3.getAddress()));
                        ps.setString(7, checkNull(objNT3.getAddress1()));
                        ps.setString(8, checkNull(objNT3.getCity()));
                        ps.setString(9, checkNull(objNT3.getZipCode()));
                        ps.setString(10, checkNull(objNT3.getTelephone()));
                        ps.setString(11, checkNull(objNT3.getFax()));
                        ps.setString(12, checkNull(objNT3.getEmail()));
                        ps.setString(13, objNT3.getNotifyCheck());
                        ps.setString(14, objNT3.getOrdercheck());
                        ps.setString(15, checkNull(objNT3.getState()));
                        ps.setString(16, checkNull(objNT3.getEori()));
                        ps.setString(17, checkNull(objNT3.getPersontype()));
                        ps.executeUpdate();
                    }

                    // Notify3
                    // FW
                    SISubmitVO objFW = (SISubmitVO) hm.get("FW");
                    if (objFW.getCustname() != null && objFW.getCustname().length() > 0) {

                        ps = ecomConn.prepareStatement(SAVE_SI_CUST_DET);
                        ps.setString(1, str_sino);
                        ps.setString(2, "FW");
                        ps.setString(3, checkNull(objFW.getCustCode()));
                        ps.setString(4, checkNull(objFW.getCountryid()));
                        ps.setString(5, checkNull(objFW.getCustname()));
                        ps.setString(6, checkNull(objFW.getAddress()));
                        ps.setString(7, checkNull(objFW.getAddress1()));
                        ps.setString(8, checkNull(objFW.getCity()));
                        ps.setString(9, checkNull(objFW.getZipCode()));
                        ps.setString(10, checkNull(objFW.getTelephone()));
                        ps.setString(11, checkNull(objFW.getFax()));
                        ps.setString(12, checkNull(objFW.getEmail()));
                        ps.setString(13, null);
                        ps.setString(14, null);
                        ps.setString(15, checkNull(objFW.getState()));
                        ps.setString(16, checkNull(objFW.getEori()));
                        ps.setString(17, checkNull(objFW.getPersontype()));
                        ps.executeUpdate();
                    }
                    // Buyer
                    // objBY
                    SISubmitVO objBY = (SISubmitVO) hm.get("BY");
                    if (objBY.getCustname() != null && objBY.getCustname().length() > 0) {

                        ps = ecomConn.prepareStatement(SAVE_SI_CUST_DET);
                        ps.setString(1, str_sino);
                        ps.setString(2, "BY");
                        ps.setString(3, checkNull(objBY.getCustCode()));
                        ps.setString(4, checkNull(objBY.getCountryid()));
                        ps.setString(5, checkNull(objBY.getCustname()));
                        ps.setString(6, checkNull(objBY.getAddress()));
                        ps.setString(7, checkNull(objBY.getAddress1()));
                        ps.setString(8, checkNull(objBY.getCity()));
                        ps.setString(9, checkNull(objBY.getZipCode()));
                        ps.setString(10, checkNull(objBY.getTelephone()));
                        ps.setString(11, checkNull(objBY.getFax()));
                        ps.setString(12, checkNull(objBY.getEmail()));
                        ps.setString(13, null);
                        ps.setString(14, null);
                        ps.setString(15, checkNull(objBY.getState()));
                        ps.setString(16, checkNull(objBY.getEori()));
                        ps.setString(17, checkNull(objBY.getPersontype()));
                        ps.executeUpdate();
                    }
                    // Seller
                    // SL
                    SISubmitVO objSL = (SISubmitVO) hm.get("SL");
                    if (objSL.getCustname() != null && objSL.getCustname().length() > 0) {

                        ps = ecomConn.prepareStatement(SAVE_SI_CUST_DET);
                        ps.setString(1, str_sino);
                        ps.setString(2, "SL");
                        ps.setString(3, checkNull(objSL.getCustCode()));
                        ps.setString(4, checkNull(objSL.getCountryid()));
                        ps.setString(5, checkNull(objSL.getCustname()));
                        ps.setString(6, checkNull(objSL.getAddress()));
                        ps.setString(7, checkNull(objSL.getAddress1()));
                        ps.setString(8, checkNull(objSL.getCity()));
                        ps.setString(9, checkNull(objSL.getZipCode()));
                        ps.setString(10, checkNull(objSL.getTelephone()));
                        ps.setString(11, checkNull(objSL.getFax()));
                        ps.setString(12, checkNull(objSL.getEmail()));
                        ps.setString(13, null);
                        ps.setString(14, null);
                        ps.setString(15, checkNull(objSL.getState()));
                        ps.setString(16, checkNull(objSL.getEori()));
                        ps.setString(17, checkNull(objSL.getPersontype()));
                        ps.executeUpdate();
                    }
//Cust Details 
                    saveCustDetails(custMap, str_sino);

                    i = 0;
                    alsize = 0;

                    if (cntMapObj != null && cntMapObj.size() > 0) {
                        alsize = cntMapObj.size();
                        Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = cntMapObj.entrySet();
                        for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                            i = i + 1;
                            SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                            SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                            SISealVO siSealVOObj = (SISealVO) siCNTValueVO.getSiSealObj();
                            cntno = "";
                            cntType = "";
                            seal1No = "";
                            seal2No = "";
                            soc = "";
                            strSOC = Validation.checkNull(siMasterVO.getStrsoc());
                            cntno = Validation.checkNull(siMasterVO.getStrCntNo());
                            cntType = Validation.checkNull(siMasterVO.getStrType());
                            seal2No = Validation.checkNull(siSealVOObj.getStrShipperSeal());
                            seal1No = Validation.checkNull(siSealVOObj.getStrCarrierSeal());
                            ps = ecomConn.prepareStatement(SAVE_SI_CARGO_DET);
                            System.out.println("[ECOM] EcomShippingInstructionBean str_sino: " + str_sino
                                    + ", cntType: " + cntType + ", siMasterVO.getEqpSize(): " + siMasterVO.getEqpSize()
                                    + ", cntno: " + cntno + ", strSOC: " + strSOC + ", seal1No: " + seal1No
                                    + ", seal2No: " + seal2No
                                    + ", siMasterVO.getTareweight(): " + siMasterVO.getTareweight()
                                    + ", siMasterVO.getCargoweight(): " + siMasterVO.getCargoweight()
                                    + ", siMasterVO.getGrossweight(): " + siMasterVO.getGrossweight()
                                    + ", siMasterVO.getWeightUnit(): " + siMasterVO.getWeightUnit()
                                    + ", siMasterVO.getNor(): " + siMasterVO.getNor()
                                    + ", siMasterVO.getNeweqptypeid(): " + siMasterVO.getRateeqptypeid());
                            rateeqptypeid = siMasterVO.getRateeqptypeid();
                            siMasterVO.setConId(i);
                            ps.setString(1, str_sino);
                            ps.setInt(2, i);
                            ps.setString(3, checkNull(siMasterVO.getStrEqpType()));
                            ps.setString(4, checkNull(cntType));
                            ps.setString(5, checkNull(cntno));
                            ps.setString(6, strSOC);
                            ps.setString(7, checkNull(seal1No));
                            ps.setString(8, checkNull(seal2No));
                            ps.setDouble(9, Double.parseDouble(ConvertDecimalFormat(siMasterVO.getTareweight())));
                            ps.setDouble(10, Double.parseDouble(ConvertDecimalFormat(siMasterVO.getCargoweight())));
                            ps.setDouble(11, Double.parseDouble(ConvertDecimalFormat(siMasterVO.getGrossweight())));
                            ps.setString(12, checkNull(siMasterVO.getWeightUnit()));
                            ps.setString(13, checkNull(siMasterVO.getNor()));
                            ps.setString(14, checkNull(siMasterVO.getEqpStatus()));
                            ps.setString(15, checkNull(siMasterVO.getOog()));
                            ps.setString(16, checkNull(siMasterVO.getDgFlag()));
                            ps.setString(17, checkNull(cntType).equalsIgnoreCase(rateeqptypeid) ? "" : rateeqptypeid);
                            ps.executeUpdate();

                            if ("Y".equalsIgnoreCase(siMasterVO.getRefer())
                                    && (!"Y".equalsIgnoreCase(siMasterVO.getNor()))) {
                                ps = ecomConn.prepareStatement(DRAFT_Refer_INSERT);
                                // si_no,eqpid,cargoNo,tempmax,settemp,tempmin,tempunit
                                ps.setString(1, str_sino);
                                ps.setInt(2, i);
                                ps.setString(3, cntno);
                                ps.setString(4, Validation1.convertEmptytoNull(siMasterVO.getSetTemp()));
                                ps.setString(5, Validation1.convertEmptytoNull(siMasterVO.getTempMin()));
                                ps.setString(6, Validation1.convertEmptytoNull(siMasterVO.getTempMax()));
                                ps.setString(7, siMasterVO.getTempunit());
                                ps.setString(8, siMasterVO.getHumidity());
                                ps.setString(9,
                                        "1".equalsIgnoreCase(siMasterVO.getHumidity_flag())
                                        ? siMasterVO.getHumidity_flag()
                                        : "0");
                                ps.setString(10, siMasterVO.getDrains());
                                ps.setString(11, siMasterVO.getCo2value());
                                ps.setString(12, siMasterVO.getO2value());
                                ps.setString(13, siMasterVO.getVentilation());
                                ps.executeUpdate();
                            }

                        }
                    }
                    ps = ecomConn.prepareStatement(UPDATE_SI_REQUEST_MASTER, 1004, 1007);
                    ps.setString(1, username1);
                    ps.setString(2, bookno);
                    ps.executeUpdate();
                    i = 0;
                    alsize = 0;
                    String oog = "";
                    int conId = 0;

                    if (cntMapObj != null && cntMapObj.size() > 0) {
                        alsize = cntMapObj.size();
                        Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = cntMapObj.entrySet();
                        for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                            SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                            SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                            SISealVO siSealVOObj = (SISealVO) siCNTValueVO.getSiSealObj();
                            ArrayList cntList = (ArrayList) siCNTValueVO.getCntDeatilsObj();
                            cntno = "";
                            cntType = "";
                            seal1No = "";
                            seal2No = "";
                            cargoId = 0;
                            cargoNo = 0;
                            cntno = Validation.checkNull(siMasterVO.getStrCntNo());
                            cntType = Validation.checkNull(siMasterVO.getStrType());
                            ps = ecomConn.prepareStatement(GET_CARGO_FROM_SI, 1004, 1007);
                            ps.setString(1, str_sino);
                            ps.setString(2, cntno);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                cargoId = rs.getInt("conId");
                            }
                            if (cntList != null && cntList.size() > 0) {
                                cntListSize = 0;
                                cntListSize = cntList.size();
                                for (int j = 0; j < cntListSize; j++) {
                                    SIContainerDetailVO siCntDetVo = (SIContainerDetailVO) cntList.get(j);
                                    commodity = "";
                                    cmdId = "";
                                    cmdDesc = "";
                                    wt = "";
                                    unit = "";
                                    qty = "";
                                    packge = "";
                                    packgeId = "";
                                    volume = "";
                                    volumeUnt = "";
                                    netweight = "";
                                    marks = "";
                                    description = "";
                                    pkgIndex = 0;
                                    cmdIndex = 0;
                                    wt = ConvertDecimalFormat(Validation.checkNull(siCntDetVo.getStrWgt()));
                                    unit = Validation.checkNull(siCntDetVo.getStrUnit());
                                    qty = Validation.checkNull(siCntDetVo.getStrQty());
                                    commodity = Validation.checkNull(siCntDetVo.getStrCommodity());
                                    packge = Validation.checkNull(siCntDetVo.getStrPkg());
                                    volume = Validation.checkNull(siCntDetVo.getStrVol());
                                    volumeUnt = Validation.checkNull(siCntDetVo.getStrVut());
                                    netweight = Validation.checkNull(siCntDetVo.getNettwt());
                                    marks = Validation.checkNull(siCntDetVo.getStrMrk());
                                    description = Validation.checkNull(siCntDetVo.getStrDes());
                                    bycode = Validation.checkNull(siCntDetVo.getByscode());
                                    byname = Validation.checkNull(siCntDetVo.getBysname());
                                    slcode = Validation.checkNull(siCntDetVo.getSlscode());
                                    slname = Validation.checkNull(siCntDetVo.getSlsname());
                                    cmdty_hblno = Validation.checkNull(siCntDetVo.getCmdty_hblno());

                                    if (commodity.length() > 0) {
                                        cmdIndex = commodity.lastIndexOf("~");
                                        cmdDesc = Validation.checkNull(commodity.substring(0, (cmdIndex - 1)));
                                        cmdId = Validation
                                                .checkNull(commodity.substring((cmdIndex + 1), commodity.length()));
                                    }

                                    if (packge.length() > 0) {
                                        pkgIndex = packge.lastIndexOf("~");
                                        packgeId = Validation
                                                .checkNull(packge.substring((pkgIndex + 1), packge.length()));
                                    }
                                    System.out.println("[ECOM] EcomShippingInstructionBean cargoId: " + cargoId
                                            + ", cntno: " + cntno + ", j: " + j
                                            + ", cmdId: " + cmdId + ", packgeId: " + packgeId + ", qty: " + qty
                                            + ", wt: " + wt
                                            + ", unit: " + unit + ", volume: " + volume + ", volumeUnt: " + volumeUnt
                                            + ", netweight: " + netweight + ", marks: " + marks
                                            + ", description: " + description + ", siCntDetVo.getHsCode(): "
                                            + siCntDetVo.getHsCode() + ", siCntDetVo.getHazardous(): "
                                            + siCntDetVo.getHazardous());
                                    ps = ecomConn.prepareStatement(SAVE_SI_COMMODITY_DET);
                                    ps.setString(1, str_sino);
                                    ps.setInt(2, cargoId);
                                    ps.setString(3, cntno);
                                    ps.setInt(4, j + 1);
                                    ps.setString(5, cmdId);
                                    ps.setString(6, packgeId);
                                    ps.setString(7, qty);
                                    ps.setString(8, wt);
                                    ps.setString(9, unit);
                                    ps.setString(10, volume);
                                    ps.setString(11, volumeUnt);
                                    ps.setString(12, marks.replace("\r", ""));
                                    ps.setString(13, description.replace("\r", ""));
                                    ps.setString(14, siCntDetVo.getHsCode());
                                    ps.setString(15, checkNull(siCntDetVo.getHazardous()));
                                    ps.setString(16, netweight);
                                    ps.setString(17, siCntDetVo.getHssuffixcode());
                                    ps.setString(18, bycode);
                                    ps.setString(19, byname);
                                    ps.setString(20, slcode);
                                    ps.setString(21, slname);
                                    ps.setString(22, cmdty_hblno);

                                    ps.executeUpdate();
                                    //
                                    if ("y".equalsIgnoreCase(siCntDetVo.getHazardous())) {
                                        // si_no,eqpid,cargoNo,imoContact,imoPhone
                                        ps = ecomConn.prepareStatement(DRAFT_DG_INSERT);
                                        ps.setString(1, str_sino);
                                        ps.setInt(2, cargoId);
                                        ps.setString(3, cntno);
                                        ps.setInt(4, j + 1);
                                        ps.setString(5, siCntDetVo.getMarinePollutant());
                                        ps.setString(6, siCntDetVo.getUnNo());
                                        ps.setString(7, siCntDetVo.getcClass());
                                        ps.setString(8, siCntDetVo.getFlashPt());
                                        ps.setString(9, siCntDetVo.getFlashPtUnit());
                                        ps.setString(10, siCntDetVo.getImocontact());
                                        ps.setString(11, siCntDetVo.getImophone());
                                        ps.executeUpdate();
                                    }
                                    System.out.println("[ECOM] EcomShippingInstructionBean siCntDetVo.getItemleft(): "
                                            + siCntDetVo.getItemleft()
                                            + ", siCntDetVo.getItemright(): " + siCntDetVo.getItemright()
                                            + ", siCntDetVo.getItemforward(): " + siCntDetVo.getItemforward()
                                            + ", siCntDetVo.getAlt(): " + siCntDetVo.getItemaft()
                                            + ", siCntDetVo.getItemtop(): " + siCntDetVo.getItemtop()
                                            + ", siCntDetVo.getItemlengthUnit(): " + siCntDetVo.getItemlengthUnit()
                                            + ", oog: " + oog);
                                    if ("Y".equalsIgnoreCase(siMasterVO.getOog())) {
                                        // si_no,eqpId,cargoNo,itemforward, itemtop, itemright,
                                        // itemaft,itemLeft,itemLengthUnit
                                        ps = ecomConn.prepareStatement(DRAFT_OOG_INSERT);
                                        ps.setString(1, str_sino);
                                        ps.setInt(2, cargoId);
                                        ps.setInt(3, j + 1);
                                        ps.setString(4, cntno);
                                        ps.setString(5, checkNull(siCntDetVo.getItemleft()));
                                        ps.setString(6, checkNull(siCntDetVo.getItemright()));
                                        ps.setString(7, checkNull(siCntDetVo.getItemforward()));
                                        ps.setString(8, checkNull(siCntDetVo.getItemaft()));
                                        ps.setString(9, checkNull(siCntDetVo.getItemtop()));
                                        ps.setString(10, checkNull(siCntDetVo.getItemlengthUnit()));

                                        ps.executeUpdate();
                                    }
                                }
                            } // comd
                        }
                    }

                    List Chargelist = partiesVO.getChargeDetails();
                    if (Chargelist != null && Chargelist.size() > 0) {
                        ChargesVo chargevo = null;
                        ps = ecomConn.prepareStatement(INSERT_SI_CHARGES);
                        for (int j = 0; j < Chargelist.size(); j++) {
                            chargevo = (ChargesVo) Chargelist.get(j);
                            System.out.println("[ECOM] EcomShippingInstructionBean j=" + j + "&1=" + str_sino + "&2="
                                    + chargevo.getChargeid() + "&3=" + chargevo.getPayagencyid() + "&4="
                                    + chargevo.getCusttypeid() + "&5=" + chargevo.getFprepaidcollect() + "&6="
                                    + chargevo.getEqptypeid());
                            ps.setString(1, str_sino);
                            ps.setString(2, chargevo.getChargeid());
                            ps.setString(3, chargevo.getPayagencyid());
                            ps.setString(4, chargevo.getCusttypeid());
                            ps.setString(5, chargevo.getFprepaidcollect());
                            ps.setString(6, chargevo.getEqptypeid());

                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                    // ENS
                    ENSDeclarantVo ensDec = partiesVO.getEnsdecvo();
                    if (ensDec != null && checkNull(ensDec.getEnsFillingType()).length() > 0) {

                        ps = ecomConn.prepareStatement(INSERT_SI_ENSDEC);

                        ps.setString(1, str_sino);
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
                        // Execute the insert statement
                        int rowsInserted = ps.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println(
                                    "[ECOM] EcomShippingInstructionBean Record inserted successfully into ecomsi_ics_new!");
                        }

                    }
                    //
                    ZipFileConversion zipconv = new ZipFileConversion();
                    HashMap<String, byte[]> filehm = new HashMap<String, byte[]>();
                    isconvertzip = ("Y".equalsIgnoreCase(fileDetailsVO.getEnableZipConversion()));
                    System.out.println("[ECOM] EcomShippingInstructionBean isconvertzip=> " + isconvertzip
                            + "  getEnableZipConversion=> " + fileDetailsVO.getEnableZipConversion());
                    String zipFileName = "";
                    //
                    fileDetailsVO.setFilePath(fileDetailsVO.getFilePath() + str_sino);
                    saveFilePath = fileDetailsVO.getFilePath();
                    FileOutputStream fos = null;
                    System.out.println("[ECOM] EcomShippingInstructionBean fileDetailsVO.isFileAvailable()===> "
                            + fileDetailsVO.isFileAvailable() + "fileDetailsVO.getFilePath()===> "
                            + fileDetailsVO.getFilePath());
                    if (fileDetailsVO.isFileAvailable()) {
                        File directory = new File(saveFilePath);
                        if (!directory.exists()) {
                            directory.mkdir();
                        }
                    }

                    String deletedfile = fileDetailsVO.getDeletedfilename();
                    System.out.println("[ECOM] EcomShippingInstructionBean deletedfile==> " + deletedfile);
                    if (deletedfile != null && deletedfile.length() > 0) {
                        deletedfile = deletedfile.substring(2, deletedfile.length());
                        ArrayList fileset = new ArrayList(Arrays.asList(deletedfile.split("~~")));
                        for (int i1 = 0; i1 < fileset.size(); i1++) {
                            System.out
                                    .println("[ECOM] EcomShippingInstructionBean checkNull((String)iterator.next())==> "
                                            + checkNull((String) fileset.get(i1)));
                            File dfile = new File(
                                    fileDetailsVO.getFilePath() + File.separator + checkNull((String) fileset.get(i1)));
                            FileUtils.forceDelete(dfile);
                        }
                    }
                    ps = ecomConn.prepareStatement(INSERTECOMFILESQRY);
                    Boolean flag = false;
                    System.out.println("[ECOM] EcomShippingInstructionBean -----File path is --->" + saveFilePath);
                    if (fileDetailsVO.getVgmFileName() != null && fileDetailsVO.getVgmFileName().trim().length() > 0) {
                        String vgmFileName = fileDetailsVO.getVgmFileName();
                        if (fileDetailsVO.getVgmFileData() != null) {
                            vgmFileName = "VGM_" + fileDetailsVO.getVgmFileName();
                            if (isconvertzip) {
                                filehm.put(vgmFileName, fileDetailsVO.getVgmFileData());
                            } else {
                                File vgmFilePath = new File(saveFilePath, vgmFileName);
                                fos = new FileOutputStream(vgmFilePath);
                                fos.write(fileDetailsVO.getVgmFileData());
                            }
                            flag = true;
                        }
                        ps.setString(1, str_sino);
                        ps.setString(2, "VGM");
                        ps.setString(3, vgmFileName);
                        // ps.executeUpdate();

                        retFilePath += vgmFileName + "~~~";

                    }
                    if (fileDetailsVO.getVgmFileData1() != null && fileDetailsVO.getVgmFileName1() != null
                            && fileDetailsVO.getVgmFileName1().length() > 0) {
                        String filename6 = EcomConsts.FILETYPE_VMG + "_" + fileDetailsVO.getVgmFileName1();
                        if (isconvertzip) {
                            filehm.put(filename6, fileDetailsVO.getVgmFileData1());
                        } else {
                            File Ffilepath1 = new File(fileDetailsVO.getFilePath(), filename6);
                            fos = new FileOutputStream(Ffilepath1);
                            fos.write(fileDetailsVO.getVgmFileData1());
                        }
                        flag = true;
                        retFilePath += filename6 + "~~~";
                    }
                    if (fileDetailsVO.getVgmFileData2() != null && fileDetailsVO.getVgmFileName2() != null
                            && fileDetailsVO.getVgmFileName2().length() > 0) {
                        String filename7 = EcomConsts.FILETYPE_VMG + "_" + fileDetailsVO.getVgmFileName2();
                        if (isconvertzip) {
                            filehm.put(filename7, fileDetailsVO.getVgmFileData2());
                        } else {
                            File Ffilepath1 = new File(fileDetailsVO.getFilePath(), filename7);
                            fos = new FileOutputStream(Ffilepath1);
                            fos.write(fileDetailsVO.getVgmFileData2());
                        }
                        flag = true;
                        retFilePath += filename7 + "~~~";
                    }
                    if (filehm != null && filehm.size() > 0) {
                        zipconv.convertZipFile(EcomConsts.FILETYPE_VMG + "_" + str_sino, saveFilePath, filehm);
                        zipFileName = zipFileName + EcomConsts.FILETYPE_VMG + "_" + str_sino + ".zip" + "~~~";
                    }
                    filehm = new HashMap<String, byte[]>();
                    if (fileDetailsVO.getDgFileName() != null && fileDetailsVO.getDgFileName().trim().length() > 0) {
                        String dgFileName = fileDetailsVO.getDgFileName();
                        if (fileDetailsVO.getDgFileData() != null) {
                            dgFileName = "DG_" + fileDetailsVO.getDgFileName();
                            if (isconvertzip) {
                                filehm.put(dgFileName, fileDetailsVO.getDgFileData());
                            } else {
                                File dgFilePath = new File(saveFilePath, dgFileName);
                                fos = new FileOutputStream(dgFilePath);
                                fos.write(fileDetailsVO.getDgFileData());
                            }
                            flag = true;
                        }

                        ps.setString(1, str_sino);
                        ps.setString(2, "DG");
                        ps.setString(3, dgFileName);
                        // ps.executeUpdate();

                        retFilePath += dgFileName + "~~~";
                    }
                    if (fileDetailsVO.getDgFileData1() != null && fileDetailsVO.getDgFileName1() != null
                            && fileDetailsVO.getDgFileName1().length() > 0) {
                        String filename8 = EcomConsts.FILETYPE_DGC + "_" + fileDetailsVO.getDgFileName1();
                        if (isconvertzip) {
                            filehm.put(filename8, fileDetailsVO.getDgFileData1());
                        } else {
                            File Ffilepath2 = new File(fileDetailsVO.getFilePath(), filename8);
                            fos = new FileOutputStream(Ffilepath2);
                            fos.write(fileDetailsVO.getDgFileData1());
                        }
                        flag = true;
                        retFilePath += filename8 + "~~~";
                    }
                    if (fileDetailsVO.getDgFileData2() != null && fileDetailsVO.getDgFileName2() != null
                            && fileDetailsVO.getDgFileName2().length() > 0) {
                        String filename9 = EcomConsts.FILETYPE_DGC + "_" + fileDetailsVO.getDgFileName2();
                        if (isconvertzip) {
                            filehm.put(filename9, fileDetailsVO.getDgFileData2());
                        } else {
                            File Ffilepath2 = new File(fileDetailsVO.getFilePath(), filename9);
                            fos = new FileOutputStream(Ffilepath2);
                            fos.write(fileDetailsVO.getDgFileData2());
                        }
                        flag = true;
                        retFilePath += filename9 + "~~~";
                    }
                    if (filehm != null && filehm.size() > 0) {
                        zipconv.convertZipFile("DG_" + str_sino, saveFilePath, filehm);
                        zipFileName = zipFileName + "DG_" + str_sino + ".zip" + "~~~";
                    }
                    filehm = new HashMap<String, byte[]>();

                    if (fileDetailsVO.getLolFileName() != null && fileDetailsVO.getLolFileName().trim().length() > 0) {
                        String lolFileName = fileDetailsVO.getLolFileName();
                        if (fileDetailsVO.getLolFileData() != null) {
                            lolFileName = "LOL_" + fileDetailsVO.getLolFileName();
                            if (isconvertzip) {
                                filehm.put(lolFileName, fileDetailsVO.getLolFileData());
                            } else {
                                File lolFilePath = new File(saveFilePath, lolFileName);
                                fos = new FileOutputStream(lolFilePath);
                                fos.write(fileDetailsVO.getLolFileData());
                            }
                            flag = true;
                        }
                        ps.setString(1, str_sino);
                        ps.setString(2, "LOL");
                        ps.setString(3, lolFileName);
                        // ps.executeUpdate();

                        retFilePath += lolFileName + "~~~";
                    }
                    if (fileDetailsVO.getLolFileData1() != null && fileDetailsVO.getLolFileName1() != null
                            && fileDetailsVO.getLolFileName1().length() > 0) {
                        String filename10 = EcomConsts.FILETYPE_LOL + "_" + fileDetailsVO.getLolFileName1();
                        if (isconvertzip) {
                            filehm.put(filename10, fileDetailsVO.getLolFileData1());
                        } else {
                            File Ffilepath3 = new File(fileDetailsVO.getFilePath(), filename10);
                            fos = new FileOutputStream(Ffilepath3);
                            fos.write(fileDetailsVO.getLolFileData1());
                        }
                        flag = true;
                        retFilePath += filename10 + "~~~";

                    }
                    if (fileDetailsVO.getLolFileData2() != null && fileDetailsVO.getLolFileName2() != null
                            && fileDetailsVO.getLolFileName2().length() > 0) {
                        String filename11 = EcomConsts.FILETYPE_LOL + "_" + fileDetailsVO.getLolFileName2();
                        if (isconvertzip) {
                            filehm.put(filename11, fileDetailsVO.getLolFileData2());
                        } else {
                            File Ffilepath3 = new File(fileDetailsVO.getFilePath(), filename11);
                            fos = new FileOutputStream(Ffilepath3);
                            fos.write(fileDetailsVO.getLolFileData2());
                        }
                        flag = true;
                        retFilePath += filename11 + "~~~";

                    }
                    if (filehm != null && filehm.size() > 0) {
                        zipconv.convertZipFile("LOL_" + str_sino, saveFilePath, filehm);
                        zipFileName = zipFileName + "LOL_" + str_sino + ".zip" + "~~~";
                    }
                    filehm = new HashMap<String, byte[]>();

                    if (fileDetailsVO.getMsdsFileName() != null
                            && fileDetailsVO.getMsdsFileName().trim().length() > 0) {
                        String msdsFileName = fileDetailsVO.getMsdsFileName();
                        if (fileDetailsVO.getMsdsFileData() != null) {
                            msdsFileName = "MSD_" + fileDetailsVO.getMsdsFileName();
                            if (isconvertzip) {
                                filehm.put(msdsFileName, fileDetailsVO.getMsdsFileData());
                            } else {
                                File msdsFilePath = new File(saveFilePath, msdsFileName);
                                fos = new FileOutputStream(msdsFilePath);
                                fos.write(fileDetailsVO.getMsdsFileData());
                            }
                            flag = true;
                        }
                        ps.setString(1, str_sino);
                        ps.setString(2, "MSD");
                        ps.setString(3, msdsFileName);
                        // ps.executeUpdate();

                        retFilePath += msdsFileName + "~~~";
                    }
                    if (fileDetailsVO.getMsdsFileData1() != null && fileDetailsVO.getMsdsFileName1() != null
                            && fileDetailsVO.getMsdsFileName1().length() > 0) {
                        String filename12 = EcomConsts.FILETYPE_MSD + "_" + fileDetailsVO.getMsdsFileName1();
                        if (isconvertzip) {
                            filehm.put(filename12, fileDetailsVO.getMsdsFileData1());
                        } else {
                            File Ffilepath4 = new File(fileDetailsVO.getFilePath(), filename12);
                            fos = new FileOutputStream(Ffilepath4);
                            fos.write(fileDetailsVO.getMsdsFileData1());
                        }
                        flag = true;
                        retFilePath += filename12 + "~~~";
                    }
                    if (fileDetailsVO.getMsdsFileData2() != null && fileDetailsVO.getMsdsFileName2() != null
                            && fileDetailsVO.getMsdsFileName2().length() > 0) {
                        String filename13 = EcomConsts.FILETYPE_MSD + "_" + fileDetailsVO.getMsdsFileName2();
                        if (isconvertzip) {
                            filehm.put(filename13, fileDetailsVO.getMsdsFileData2());
                        } else {
                            File Ffilepath4 = new File(fileDetailsVO.getFilePath(), filename13);
                            fos = new FileOutputStream(Ffilepath4);
                            fos.write(fileDetailsVO.getMsdsFileData2());
                        }
                        flag = true;
                        retFilePath += filename13 + "~~~";
                    }
                    if (filehm != null && filehm.size() > 0) {
                        zipconv.convertZipFile("MSD_" + str_sino, saveFilePath, filehm);
                        zipFileName = zipFileName + "MSD_" + str_sino + ".zip" + "~~~";
                    }
                    filehm = new HashMap<String, byte[]>();

                    if (fileDetailsVO.getOthersFileName() != null
                            && fileDetailsVO.getOthersFileName().trim().length() > 0) {
                        String othersFileName = fileDetailsVO.getOthersFileName();
                        if (fileDetailsVO.getOthersFileData() != null) {
                            othersFileName = "OT_" + fileDetailsVO.getOthersFileName();
                            if (isconvertzip) {
                                filehm.put(othersFileName, fileDetailsVO.getOthersFileData());
                            } else {
                                File othersFilePath = new File(saveFilePath, othersFileName);
                                fos = new FileOutputStream(othersFilePath);
                                fos.write(fileDetailsVO.getOthersFileData());
                            }
                            flag = true;

                        }
                        ps.setString(1, str_sino);
                        ps.setString(2, "OT");
                        ps.setString(3, othersFileName);
                        // ps.executeUpdate();

                        retFilePath += othersFileName + "~~~";
                    }

                    if (fileDetailsVO.getOthersFileData1() != null && fileDetailsVO.getOthersFileName1() != null
                            && fileDetailsVO.getOthersFileName1().length() > 0) {
                        String filename14 = EcomConsts.FILETYPE_OTHERS + "_" + fileDetailsVO.getOthersFileName1();
                        if (isconvertzip) {
                            filehm.put(filename14, fileDetailsVO.getOthersFileData1());
                        } else {
                            File Ffilepath5 = new File(fileDetailsVO.getFilePath(), filename14);
                            fos = new FileOutputStream(Ffilepath5);
                            fos.write(fileDetailsVO.getOthersFileData1());
                        }
                        flag = true;
                        retFilePath += filename14 + "~~~";
                    }
                    if (fileDetailsVO.getOthersFileData2() != null && fileDetailsVO.getOthersFileName2() != null
                            && fileDetailsVO.getOthersFileName2().length() > 0) {
                        String filename15 = EcomConsts.FILETYPE_OTHERS + "_" + fileDetailsVO.getOthersFileName2();
                        if (isconvertzip) {
                            filehm.put(filename15, fileDetailsVO.getOthersFileData2());
                        } else {
                            File Ffilepath5 = new File(fileDetailsVO.getFilePath(), filename15);
                            fos = new FileOutputStream(Ffilepath5);
                            fos.write(fileDetailsVO.getOthersFileData2());
                        }
                        flag = true;
                        retFilePath += filename15 + "~~~";
                    }
                    if (filehm != null && filehm.size() > 0) {
                        zipconv.convertZipFile("OT_" + str_sino, saveFilePath, filehm);
                        zipFileName = zipFileName + "OT_" + str_sino + ".zip" + "~~~";
                    }
                    if (isconvertzip) {
                        if (zipFileName != null && zipFileName.length() > 3) {
                            retFilePath = zipFileName.substring(0, zipFileName.length() - 3);
                        }
                    }
                    System.out.println("[ECOM] EcomShippingInstructionBean >>filepath>>112233>si in>" + retFilePath);
                    if (flag) {
                        if (fos != null) {
                            fos.flush();
                            fos.close();
                        }
                    }

                } else if ("DraftUpdate".equalsIgnoreCase(Ecom_SaveType)) { 
                    System.out.println("[ECOM] EcomShippingInstructionBean si not in update>>>bknum>>" + bknum);
                    bknum = sino;
                    ps = ecomConn.prepareStatement(SI_DRAFT_UPDATE);
                    ps.setString(1, Si_Type);
                    ps.setString(2, addremarks);
                    ps.setString(3, partiesVO.getPla_free_txt());
                    ps.setString(4, partiesVO.getPol_free_txt());
                    ps.setString(5, partiesVO.getPod_free_txt());
                    ps.setString(6, partiesVO.getPld_free_txt());
                    ps.setString(7, partiesVO.getVslvoyage_free_txt());
                    ps.setString(8, checkNull(ccMailId));
                    ps.setString(9, checkNull(partiesVO.getShipperHeader()));
                    ps.setString(10, checkNull(partiesVO.getShipperName()));
                    ps.setString(11, checkNull(partiesVO.getConsigneeHeader()));
                    ps.setString(12, checkNull(partiesVO.getConsigneeName()));
                    ps.setString(13, checkNull(partiesVO.getNotifyHeader()));
                    ps.setString(14, checkNull(partiesVO.getNotifyName()));
                    ps.setString(15, partiesVO.getAESNumber());
                    ps.setString(16, partiesVO.getAESDiscliamer());
                    ps.setString(17, partiesVO.getReleasetype());
                    ps.setString(18, partiesVO.getDeclaredValue());
                    ps.setString(19, partiesVO.getBltype());
                    ps.setString(20, checkNull(username1));
                    ps.setString(21, partiesVO.getACID());
                    ps.setString(22, partiesVO.getFreight());
                    ps.setString(23, partiesVO.getCargoprotectid());
                    ps.setString(24, partiesVO.getEbook_t2lfiling());
                    ps.setString(25, partiesVO.getPackinglst());
                    ps.setString(26, partiesVO.getInvoiceupload());
                    ps.setString(27, partiesVO.getOrgTelType());
                    ps.setString(28, partiesVO.getEbook_EnsDocType());
                    ps.setString(29, partiesVO.getBltype_UAE());
                    ps.setString(30, partiesVO.getMpci_id());
                    ps.setString(31, resolveNvoccForDb(partiesVO.getEbook_nvocc()));
                    ps.setString(32, sino);
                    ps.setString(33, bookno);
                    ps.executeUpdate();

                    // String delete_Cust = "delete from Ecomm_Parties_Trans where trans_id=?";
                    String delete_Cust = "delete from ecomSI_cust where eSINo=?";
                    ps = ecomConn.prepareStatement(delete_Cust);
                    ps.setString(1, bknum);
                    ps.executeUpdate();

                    String Cust_Update_Qry = "insert into ecomSI_cust(eSINo,custTypeId,custId,countryId,name,"
                            + "address1,address2,city,zipCode,telephone,fax,email,flag,toorder,stateId,eori,persontype)"
                            + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    ps = ecomConn.prepareStatement(Cust_Update_Qry);
                    SISubmitVO objCust = (SISubmitVO) hm.get("SH");
                    int update_len = 0;
                    if (objCust != null) {
                        ps.setString(1, bknum);
                        ps.setString(2, "SH");
                        ps.setString(3, checkNull(objCust.getCustCode()));
                        ps.setString(4, checkNull(objCust.getCountryid()));
                        ps.setString(5, checkNull(objCust.getCustname()));
                        ps.setString(6, checkNull(objCust.getAddress()));
                        ps.setString(7, checkNull(objCust.getAddress1()));
                        ps.setString(8, checkNull(objCust.getCity()));
                        ps.setString(9, checkNull(objCust.getZipCode()));
                        ps.setString(10, checkNull(objCust.getTelephone()));
                        ps.setString(11, checkNull(objCust.getFax()));
                        ps.setString(12, checkNull(objCust.getEmail()));
                        ps.setString(13, null);
                        if ("Y".equalsIgnoreCase(objCust.getOrdercheck())) {
                            ps.setString(14, "Y");
                        } else {
                            ps.setString(14, "N");
                        }
                        ps.setString(15, checkNull(objCust.getState()));
                        ps.setString(16, checkNull(objCust.getEori()));
                        ps.setString(17, checkNull(objCust.getPersontype()));
                        update_len = ps.executeUpdate();
                    }
                    // Shipper Details
                    ps = ecomConn.prepareStatement(Cust_Update_Qry);
                    objCust = (SISubmitVO) hm.get("CN");
                    if (objCust != null) {

                        ps.setString(1, bknum);
                        ps.setString(2, "CN");
                        ps.setString(3, checkNull(objCust.getCustCode()));
                        ps.setString(4, checkNull(objCust.getCountryid()));
                        ps.setString(5, checkNull(objCust.getCustname()));
                        ps.setString(6, checkNull(objCust.getAddress()));
                        ps.setString(7, checkNull(objCust.getAddress1()));
                        ps.setString(8, checkNull(objCust.getCity()));
                        ps.setString(9, checkNull(objCust.getZipCode()));
                        ps.setString(10, checkNull(objCust.getTelephone()));
                        ps.setString(11, checkNull(objCust.getFax()));
                        ps.setString(12, checkNull(objCust.getEmail()));
                        ps.setString(13, null);
                        if ("Y".equalsIgnoreCase(objCust.getOrdercheck())) {
                            ps.setString(14, "Y");
                        } else {
                            ps.setString(14, "N");
                        }
                        ps.setString(15, checkNull(objCust.getState()));
                        ps.setString(16, checkNull(objCust.getEori()));
                        ps.setString(17, checkNull(objCust.getPersontype()));
                        update_len = ps.executeUpdate();
                    }
                    // Notify Details
                    ps = ecomConn.prepareStatement(Cust_Update_Qry);
                    objCust = (SISubmitVO) hm.get("NT");
                    if (objCust != null) {
                        if (objCust.getCustname() != null && objCust.getCustname().length() > 0
                                && checkNull(partiesVO.getNotifyName()).length() > 0) {
                            String NotifyCheck = "";
                            if ("Y".equalsIgnoreCase(objCust.getNotifyCheck())) {
                                NotifyCheck = "Y";
                            } else {
                                NotifyCheck = "N";
                            }
                            ps.setString(1, bknum);
                            ps.setString(2, "NT"); // NT
                            ps.setString(3, checkNull(objCust.getCustCode()));
                            ps.setString(4, checkNull(objCust.getCountryid()));
                            ps.setString(5, checkNull(objCust.getCustname()));
                            ps.setString(6, checkNull(objCust.getAddress()));
                            ps.setString(7, checkNull(objCust.getAddress1()));
                            ps.setString(8, checkNull(objCust.getCity()));
                            ps.setString(9, checkNull(objCust.getZipCode()));
                            ps.setString(10, checkNull(objCust.getTelephone()));
                            ps.setString(11, checkNull(objCust.getFax()));
                            ps.setString(12, checkNull(objCust.getEmail()));
                            ps.setString(13, NotifyCheck);

                            if ("Y".equalsIgnoreCase(objCust.getOrdercheck())) {
                                ps.setString(14, "Y");
                            } else {
                                ps.setString(14, "N");
                            }
                            ps.setString(15, checkNull(objCust.getState()));
                            ps.setString(16, checkNull(objCust.getEori()));
                            ps.setString(17, checkNull(objCust.getPersontype()));
                            update_len = ps.executeUpdate();
                            System.out.println("[ECOM] EcomShippingInstructionBean update_len" + update_len);
                        }
                    }
                    SISubmitVO objNT2 = (SISubmitVO) hm.get("N1");
                    System.out.println(
                            "[ECOM] EcomShippingInstructionBean Edit objNT2.getAddress()::::" + objNT2.getAddress());
                    if (objNT2.getCustname() != null && objNT2.getCustname().length() > 0) {
                        ps = ecomConn.prepareStatement(SAVE_SI_CUST_DET);
                        ps.setString(1, bknum);
                        ps.setString(2, "N1"); // NT
                        ps.setString(3, checkNull(objNT2.getCustCode()));
                        ps.setString(4, checkNull(objNT2.getCountryid()));
                        ps.setString(5, checkNull(objNT2.getCustname()));
                        ps.setString(6, checkNull(objNT2.getAddress()));
                        ps.setString(7, checkNull(objNT2.getAddress1()));
                        ps.setString(8, checkNull(objNT2.getCity()));
                        ps.setString(9, checkNull(objNT2.getZipCode()));
                        ps.setString(10, checkNull(objNT2.getTelephone()));
                        ps.setString(11, checkNull(objNT2.getFax()));
                        ps.setString(12, checkNull(objNT2.getEmail()));
                        ps.setString(13, objNT2.getNotifyCheck());
                        ps.setString(14, objNT2.getOrdercheck());
                        ps.setString(15, checkNull(objNT2.getState()));
                        ps.setString(16, checkNull(objNT2.getEori()));
                        ps.setString(17, checkNull(objNT2.getPersontype()));
                        ps.executeUpdate();
                    }
                    // Notify3
                    SISubmitVO objNT3 = (SISubmitVO) hm.get("N2");
                    if (objNT3.getCustname() != null && objNT3.getCustname().length() > 0) {
                        ps = ecomConn.prepareStatement(SAVE_SI_CUST_DET);
                        ps.setString(1, bknum);
                        ps.setString(2, "N2"); // NT
                        ps.setString(3, checkNull(objNT3.getCustCode()));
                        ps.setString(4, checkNull(objNT3.getCountryid()));
                        ps.setString(5, checkNull(objNT3.getCustname()));
                        ps.setString(6, checkNull(objNT3.getAddress()));
                        ps.setString(7, checkNull(objNT3.getAddress1()));
                        ps.setString(8, checkNull(objNT3.getCity()));
                        ps.setString(9, checkNull(objNT3.getZipCode()));
                        ps.setString(10, checkNull(objNT3.getTelephone()));
                        ps.setString(11, checkNull(objNT3.getFax()));
                        ps.setString(12, checkNull(objNT3.getEmail()));
                        ps.setString(13, objNT3.getNotifyCheck());
                        ps.setString(14, objNT3.getOrdercheck());
                        ps.setString(15, checkNull(objNT3.getState()));
                        ps.setString(16, checkNull(objNT3.getEori()));
                        ps.setString(17, checkNull(objNT3.getPersontype()));
                        ps.executeUpdate();
                    }
                    // FW
                    SISubmitVO objFW = (SISubmitVO) hm.get("FW");
                    if (objFW.getCustname() != null && objFW.getCustname().length() > 0) {
                        ps = ecomConn.prepareStatement(SAVE_SI_CUST_DET);
                        ps.setString(1, bknum);
                        ps.setString(2, "FW ");
                        ps.setString(3, checkNull(objFW.getCustCode()));
                        ps.setString(4, checkNull(objFW.getCountryid()));
                        ps.setString(5, checkNull(objFW.getCustname()));
                        ps.setString(6, checkNull(objFW.getAddress()));
                        ps.setString(7, checkNull(objFW.getAddress1()));
                        ps.setString(8, checkNull(objFW.getCity()));
                        ps.setString(9, checkNull(objFW.getZipCode()));
                        ps.setString(10, checkNull(objFW.getTelephone()));
                        ps.setString(11, checkNull(objFW.getFax()));
                        ps.setString(12, checkNull(objFW.getEmail()));
                        ps.setString(13, null);
                        ps.setString(14, null);
                        ps.setString(15, checkNull(objFW.getState()));
                        ps.setString(16, checkNull(objFW.getEori()));
                        ps.setString(17, checkNull(objFW.getPersontype()));
                        ps.executeUpdate();
                    }
                    // Buyer
                    // objBY
                    SISubmitVO objBY = (SISubmitVO) hm.get("BY");
                    if (objBY.getCustname() != null && objBY.getCustname().length() > 0) {

                        ps = ecomConn.prepareStatement(SAVE_SI_CUST_DET);
                        ps.setString(1, bknum);
                        ps.setString(2, "BY");
                        ps.setString(3, checkNull(objBY.getCustCode()));
                        ps.setString(4, checkNull(objBY.getCountryid()));
                        ps.setString(5, checkNull(objBY.getCustname()));
                        ps.setString(6, checkNull(objBY.getAddress()));
                        ps.setString(7, checkNull(objBY.getAddress1()));
                        ps.setString(8, checkNull(objBY.getCity()));
                        ps.setString(9, checkNull(objBY.getZipCode()));
                        ps.setString(10, checkNull(objBY.getTelephone()));
                        ps.setString(11, checkNull(objBY.getFax()));
                        ps.setString(12, checkNull(objBY.getEmail()));
                        ps.setString(13, null);
                        ps.setString(14, null);
                        ps.setString(15, checkNull(objBY.getState()));
                        ps.setString(16, checkNull(objBY.getEori()));
                        ps.setString(17, checkNull(objBY.getPersontype()));
                        ps.executeUpdate();
                    }
                    // Notify3
                    // FW
                    SISubmitVO objSL = (SISubmitVO) hm.get("SL");
                    if (objSL.getCustname() != null && objSL.getCustname().length() > 0) {

                        ps = ecomConn.prepareStatement(SAVE_SI_CUST_DET);
                        ps.setString(1, bknum);
                        ps.setString(2, "SL");
                        ps.setString(3, checkNull(objSL.getCustCode()));
                        ps.setString(4, checkNull(objSL.getCountryid()));
                        ps.setString(5, checkNull(objSL.getCustname()));
                        ps.setString(6, checkNull(objSL.getAddress()));
                        ps.setString(7, checkNull(objSL.getAddress1()));
                        ps.setString(8, checkNull(objSL.getCity()));
                        ps.setString(9, checkNull(objSL.getZipCode()));
                        ps.setString(10, checkNull(objSL.getTelephone()));
                        ps.setString(11, checkNull(objSL.getFax()));
                        ps.setString(12, checkNull(objSL.getEmail()));
                        ps.setString(13, null);
                        ps.setString(14, null);
                        ps.setString(15, checkNull(objSL.getState()));
                        ps.setString(16, checkNull(objSL.getEori()));
                        ps.setString(17, checkNull(objSL.getPersontype()));
                        ps.executeUpdate();
                    }
                    saveCustDetails(custMap, bknum);
                    // Cargo Details
                    ps = ecomConn.prepareStatement(DELETE_SI_CARGO, 1004, 1007);
                    ps.setString(1, bknum);
                    ps.executeUpdate();
                    ps = ecomConn.prepareStatement(DELETE_SI_COMMODITY, 1004, 1007);
                    ps.setString(1, bknum);
                    ps.executeUpdate();
                    ps = ecomConn.prepareStatement(DELETE_REEFER, 1004, 1007);
                    ps.setString(1, bknum);
                    ps.executeUpdate();
                    ps = ecomConn.prepareStatement(DELETE_OOG, 1004, 1007);
                    ps.setString(1, bknum);
                    ps.executeUpdate();
                    ps = ecomConn.prepareStatement(DELETE_DG, 1004, 1007);
                    ps.setString(1, bknum);
                    ps.executeUpdate();

                    if (cntMapObj != null && cntMapObj.size() > 0) {
                        alsize = cntMapObj.size();
                        Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = cntMapObj.entrySet();
                        for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                            i = i + 1;
                            SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                            SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                            SISealVO siSealVOObj = (SISealVO) siCNTValueVO.getSiSealObj();
                            cntno = "";
                            cntType = "";
                            seal1No = "";
                            seal2No = "";
                            soc = "";
                            strSOC = Validation.checkNull(siMasterVO.getStrsoc());
                            cntno = Validation.checkNull(siMasterVO.getStrCntNo());
                            cntType = Validation.checkNull(siMasterVO.getStrType());
                            seal2No = Validation.checkNull(siSealVOObj.getStrShipperSeal());
                            seal1No = Validation.checkNull(siSealVOObj.getStrCarrierSeal());
                            ps = ecomConn.prepareStatement(SAVE_SI_CARGO_DET);
                            System.out.println("[ECOM] EcomShippingInstructionBean str_sino: " + bknum + ", cntType: "
                                    + cntType
                                    + ", siMasterVO.getEqpSize(): " + siMasterVO.getEqpSize() + ", cntno: " + cntno
                                    + ", strSOC: " + strSOC + ", seal1No: " + seal1No + ", seal2No: " + seal2No
                                    + ", siMasterVO.getTareweight(): " + siMasterVO.getTareweight()
                                    + ", siMasterVO.getCargoweight(): " + siMasterVO.getCargoweight()
                                    + ", siMasterVO.getGrossweight(): " + siMasterVO.getGrossweight()
                                    + ", siMasterVO.getWeightUnit(): " + siMasterVO.getWeightUnit()
                                    + ", siMasterVO.getNor(): " + siMasterVO.getNor()
                                    + ", siMasterVO.getNeweqptypeid(): " + siMasterVO.getRateeqptypeid());
                            rateeqptypeid = siMasterVO.getRateeqptypeid();

                            ps.setString(1, bknum);
                            ps.setInt(2, i);
                            ps.setString(3, siMasterVO.getStrEqpType());
                            ps.setString(4, cntType);
                            ps.setString(5, cntno);
                            ps.setString(6, strSOC);
                            ps.setString(7, seal1No);
                            ps.setString(8, seal2No);
                            ps.setDouble(9, Double.parseDouble(ConvertDecimalFormat(siMasterVO.getTareweight())));
                            ps.setDouble(10, Double.parseDouble(ConvertDecimalFormat(siMasterVO.getCargoweight())));
                            ps.setDouble(11, Double.parseDouble(ConvertDecimalFormat(siMasterVO.getGrossweight())));
                            ps.setString(12, checkNull(siMasterVO.getWeightUnit()));
                            ps.setString(13, checkNull(siMasterVO.getNor()));
                            ps.setString(14, checkNull(siMasterVO.getEqpStatus()));
                            ps.setString(15, checkNull(siMasterVO.getOog()));
                            ps.setString(16, checkNull(siMasterVO.getDgFlag()));
                            ps.setString(17, checkNull(cntType).equalsIgnoreCase(rateeqptypeid) ? "" : rateeqptypeid);
                            ps.executeUpdate();

                            if ("Y".equalsIgnoreCase(siMasterVO.getRefer())
                                    && (!"Y".equalsIgnoreCase(siMasterVO.getNor()))) {
                                ps = ecomConn.prepareStatement(DRAFT_Refer_INSERT);
                                // si_no,eqpid,cargoNo,tempmax,settemp,tempmin,tempunit
                                ps.setString(1, bknum);
                                ps.setInt(2, i);
                                ps.setString(3, cntno);
                                ps.setString(4, Validation1.convertEmptytoNull(siMasterVO.getSetTemp()));
                                ps.setString(5, Validation1.convertEmptytoNull(siMasterVO.getTempMin()));
                                ps.setString(6, Validation1.convertEmptytoNull(siMasterVO.getTempMax()));
                                ps.setString(7, siMasterVO.getTempunit());
                                ps.setString(8, siMasterVO.getHumidity());
                                ps.setString(9,
                                        "1".equalsIgnoreCase(siMasterVO.getHumidity_flag())
                                        ? siMasterVO.getHumidity_flag()
                                        : "0");
                                ps.setString(10, siMasterVO.getDrains());
                                ps.setString(11, siMasterVO.getCo2value());
                                ps.setString(12, siMasterVO.getO2value());
                                ps.setString(13, siMasterVO.getVentilation());
                                ps.executeUpdate();
                            }

                        }
                    }
                    if (cntMapObj != null && cntMapObj.size() > 0) {
                        alsize = cntMapObj.size();
                        Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = cntMapObj.entrySet();
                        for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                            SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                            SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                            SISealVO siSealVOObj = (SISealVO) siCNTValueVO.getSiSealObj();
                            ArrayList cntList = (ArrayList) siCNTValueVO.getCntDeatilsObj();
                            cntno = "";
                            cntType = "";
                            seal1No = "";
                            seal2No = "";
                            cargoId = 0;
                            cargoNo = 0;
                            cntno = Validation.checkNull(siMasterVO.getStrCntNo());
                            cntType = Validation.checkNull(siMasterVO.getStrType());
                            ps = ecomConn.prepareStatement(GET_CARGO_FROM_SI, 1004, 1007);
                            ps.setString(1, bknum);
                            ps.setString(2, cntno);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                cargoId = rs.getInt("conId");
                                // cargoNo = Integer.parseInt(rs.getString("containerno"));
                            }
                            if (cntList != null && cntList.size() > 0) {
                                cntListSize = 0;
                                cntListSize = cntList.size();
                                for (int j = 0; j < cntListSize; j++) {
                                    SIContainerDetailVO siCntDetVo = (SIContainerDetailVO) cntList.get(j);
                                    commodity = "";
                                    cmdId = "";
                                    cmdDesc = "";
                                    wt = "";
                                    unit = "";
                                    qty = "";
                                    packge = "";
                                    packgeId = "";
                                    volume = "";
                                    volumeUnt = "";
                                    marks = "";
                                    description = "";
                                    netweight = "";
                                    pkgIndex = 0;
                                    cmdIndex = 0;
                                    wt = ConvertDecimalFormat(Validation.checkNull(siCntDetVo.getStrWgt()));
                                    unit = Validation.checkNull(siCntDetVo.getStrUnit());
                                    qty = Validation.checkNull(siCntDetVo.getStrQty());
                                    commodity = Validation.checkNull(siCntDetVo.getStrCommodity());
                                    packge = Validation.checkNull(siCntDetVo.getStrPkg());
                                    volume = Validation.checkNull(siCntDetVo.getStrVol());
                                    volumeUnt = Validation.checkNull(siCntDetVo.getStrVut());
                                    marks = Validation.checkNull(siCntDetVo.getStrMrk());
                                    description = Validation.checkNull(siCntDetVo.getStrDes());
                                    netweight = Validation.checkNull(siCntDetVo.getNettwt());
                                    bycode = Validation.checkNull(siCntDetVo.getByscode());
                                    byname = Validation.checkNull(siCntDetVo.getBysname());
                                    slcode = Validation.checkNull(siCntDetVo.getSlscode());
                                    slname = Validation.checkNull(siCntDetVo.getSlsname());
                                    cmdty_hblno = Validation.checkNull(siCntDetVo.getCmdty_hblno());

                                    if (commodity.length() > 0) {
                                        cmdIndex = commodity.lastIndexOf("~");
                                        cmdDesc = Validation.checkNull(commodity.substring(0, (cmdIndex - 1)));
                                        cmdId = Validation
                                                .checkNull(commodity.substring((cmdIndex + 1), commodity.length()));
                                    }

                                    if (packge.length() > 0) {
                                        pkgIndex = packge.lastIndexOf("~");
                                        packgeId = Validation
                                                .checkNull(packge.substring((pkgIndex + 1), packge.length()));
                                    }
                                    System.out.println("[ECOM] EcomShippingInstructionBean cargoId: " + cargoId
                                            + ", cntno: " + cntno + ", j: " + j + ", cmdId: " + cmdId
                                            + ", packgeId: " + packgeId + ", qty: " + qty + ", wt: " + wt + ", unit: "
                                            + unit + ", volume: " + volume
                                            + ", volumeUnt: " + volumeUnt + ", marks: " + marks + ", description: "
                                            + description + ", siCntDetVo.getHsCode(): " + siCntDetVo.getHsCode()
                                            + ", siCntDetVo.getHazardous(): " + siCntDetVo.getHazardous()
                                            + ", netweight: " + netweight);
                                    ps = ecomConn.prepareStatement(SAVE_SI_COMMODITY_DET);
                                    ps.setString(1, bknum);
                                    ps.setInt(2, cargoId);
                                    ps.setString(3, cntno);
                                    ps.setInt(4, j + 1);
                                    ps.setString(5, cmdId);
                                    ps.setString(6, packgeId);
                                    ps.setString(7, qty);
                                    ps.setString(8, wt);
                                    ps.setString(9, unit);
                                    ps.setString(10, volume);
                                    ps.setString(11, volumeUnt);
                                    ps.setString(12, marks.replace("\r", ""));
                                    ps.setString(13, description.replace("\r", ""));
                                    ps.setString(14, siCntDetVo.getHsCode());
                                    ps.setString(15, checkNull(siCntDetVo.getHazardous()));
                                    ps.setString(16, netweight);
                                    ps.setString(17, siCntDetVo.getHssuffixcode());
                                    ps.setString(18, bycode);
                                    ps.setString(19, byname);
                                    ps.setString(20, slcode);
                                    ps.setString(21, slname);
                                    ps.setString(22, cmdty_hblno);

                                    ps.executeUpdate();
                                    //
                                    if ("y".equalsIgnoreCase(siCntDetVo.getHazardous())) {
                                        // si_no,eqpid,cargoNo,imoContact,imoPhone
                                        ps = ecomConn.prepareStatement(DRAFT_DG_INSERT);
                                        ps.setString(1, bknum);
                                        ps.setInt(2, cargoId);
                                        ps.setString(3, cntno);
                                        ps.setInt(4, j + 1);
                                        ps.setString(5, siCntDetVo.getMarinePollutant());
                                        ps.setString(6, siCntDetVo.getUnNo());
                                        ps.setString(7, siCntDetVo.getcClass());
                                        ps.setString(8, siCntDetVo.getFlashPt());
                                        ps.setString(9, siCntDetVo.getFlashPtUnit());
                                        ps.setString(10, siCntDetVo.getImocontact());
                                        ps.setString(11, siCntDetVo.getImophone());
                                        ps.executeUpdate();
                                    }
                                    System.out.println("[ECOM] EcomShippingInstructionBean siCntDetVo - Itemleft: "
                                            + siCntDetVo.getItemleft() + ", Itemright: " + siCntDetVo.getItemright()
                                            + ", Itemforward: " + siCntDetVo.getItemforward() + ", Alt(Itemaft): "
                                            + siCntDetVo.getItemaft()
                                            + ", Itemtop: " + siCntDetVo.getItemtop() + ", ItemlengthUnit: "
                                            + siCntDetVo.getItemlengthUnit());
                                    if ("Y".equalsIgnoreCase(siMasterVO.getOog())) {
                                        // si_no,eqpId,cargoNo,itemforward, itemtop, itemright,
                                        // itemaft,itemLeft,itemLengthUnit
                                        ps = ecomConn.prepareStatement(DRAFT_OOG_INSERT);
                                        ps.setString(1, bknum);
                                        ps.setInt(2, cargoId);
                                        ps.setInt(3, j + 1);
                                        ps.setString(4, cntno);
                                        ps.setString(5, checkNull(siCntDetVo.getItemleft()));
                                        ps.setString(6, checkNull(siCntDetVo.getItemright()));
                                        ps.setString(7, checkNull(siCntDetVo.getItemforward()));
                                        ps.setString(8, checkNull(siCntDetVo.getItemaft()));
                                        ps.setString(9, checkNull(siCntDetVo.getItemtop()));
                                        ps.setString(10, checkNull(siCntDetVo.getItemlengthUnit()));

                                        ps.executeUpdate();
                                    }

                                }
                            } // comd
                        }
                    }

                    ps = ecomConn.prepareStatement(DELETE_SI_CHARGES, 1004, 1007);
                    ps.setString(1, bknum);
                    ps.executeUpdate();

                    List Chargelist = partiesVO.getChargeDetails();
                    if (Chargelist != null && Chargelist.size() > 0) {
                        ChargesVo chargevo = null;
                        ps = ecomConn.prepareStatement(INSERT_SI_CHARGES);
                        for (int j = 0; j < Chargelist.size(); j++) {
                            chargevo = (ChargesVo) Chargelist.get(j);
                            ps.setString(1, bknum);
                            ps.setString(2, chargevo.getChargeid());
                            ps.setString(3, chargevo.getPayagencyid());
                            ps.setString(4, chargevo.getCusttypeid());
                            ps.setString(5, chargevo.getFprepaidcollect());
                            ps.setString(6, chargevo.getEqptypeid());

                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    // ENS
                    ps = ecomConn.prepareStatement(DELETE_SI_ENSDEC, 1004, 1007);
                    ps.setString(1, bknum);
                    ps.executeUpdate();

                    ENSDeclarantVo ensDec = partiesVO.getEnsdecvo();
                    if (ensDec != null && checkNull(ensDec.getEnsFillingType()).length() > 0) {

                        ps = ecomConn.prepareStatement(INSERT_SI_ENSDEC);

                        ps.setString(1, bknum);
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

                        // Execute the insert statement
                        int rowsInserted = ps.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println(
                                    "[ECOM] EcomShippingInstructionBean Record inserted successfully into ecomsi_ics_new!");
                        }

                    }

                    ZipFileConversion zipconv = new ZipFileConversion();
                    HashMap<String, byte[]> filehm = new HashMap<String, byte[]>();
                    isconvertzip = ("Y".equalsIgnoreCase(fileDetailsVO.getEnableZipConversion()));
                    String zipFileName = "";
                    System.out.println("[ECOM] EcomShippingInstructionBean isconvertzip=> " + isconvertzip
                            + "  getEnableZipConversion=> " + fileDetailsVO.getEnableZipConversion());
                    //
                    fileDetailsVO.setFilePath(fileDetailsVO.getFilePath() + bknum);
                    saveFilePath = fileDetailsVO.getFilePath();
                    System.out.println("[ECOM] EcomShippingInstructionBean fileDetailsVO.isFileAvailable()===> "
                            + fileDetailsVO.isFileAvailable() + " fileDetailsVO.getFilePath()===> "
                            + fileDetailsVO.getFilePath());
                    if (fileDetailsVO.isFileAvailable()) {
                        File directory = new File(saveFilePath);
                        if (!directory.exists()) {
                            directory.mkdir();
                        }
                    }
                    String deletedfile = fileDetailsVO.getDeletedfilename();
                    System.out.println("[ECOM] EcomShippingInstructionBean deletedfile==> " + deletedfile);
                    if (deletedfile != null && deletedfile.length() > 0) {
                        deletedfile = deletedfile.substring(2, deletedfile.length());
                        ArrayList fileset = new ArrayList(Arrays.asList(deletedfile.split("~~")));
                        for (int i1 = 0; i1 < fileset.size(); i1++) {
                            System.out
                                    .println("[ECOM] EcomShippingInstructionBean checkNull((String)iterator.next())==> "
                                            + checkNull((String) fileset.get(i1)));
                            File dfile = new File(
                                    fileDetailsVO.getFilePath() + File.separator + checkNull((String) fileset.get(i1)));
                            FileUtils.forceDelete(dfile);
                        }
                    }

                    FileOutputStream fos = null;
                    retFilePath = "";
                    ps = ecomConn.prepareStatement(SI_FILES_DELETE_QRY);
                    ps.setString(1, bknum);
                    ps.executeUpdate();
                    ps = ecomConn.prepareStatement(INSERTECOMFILESQRY);
                    Boolean flag = false;
                    System.out.println("[ECOM] EcomShippingInstructionBean -----File path --->" + saveFilePath);
                    if (fileDetailsVO.getVgmFileName() != null && fileDetailsVO.getVgmFileName().trim().length() > 0) {
                        String vgmFileName = fileDetailsVO.getVgmFileName();
                        if (fileDetailsVO.getVgmFileData() != null) {
                            vgmFileName = "VGM_" + fileDetailsVO.getVgmFileName();
                            fileDetailsVO.getLolFileName();
                            if (isconvertzip) {
                                filehm.put(vgmFileName, fileDetailsVO.getVgmFileData());
                            } else {
                                File vgmFilePath = new File(saveFilePath, vgmFileName);
                                fos = new FileOutputStream(vgmFilePath);
                                fos.write(fileDetailsVO.getVgmFileData());
                            }
                            flag = true;
                        }
                        ps.setString(1, bknum);
                        ps.setString(2, "VGM");
                        ps.setString(3, vgmFileName);
                        // ps.executeUpdate();

                        retFilePath += vgmFileName + "~~~";

                    }
                    if (fileDetailsVO.getVgmFileData1() != null && fileDetailsVO.getVgmFileName1() != null
                            && fileDetailsVO.getVgmFileName1().length() > 0) {
                        String filename6 = EcomConsts.FILETYPE_VMG + "_" + fileDetailsVO.getVgmFileName1();
                        if (isconvertzip) {
                            filehm.put(filename6, fileDetailsVO.getVgmFileData1());
                        } else {
                            File Ffilepath1 = new File(fileDetailsVO.getFilePath(), filename6);
                            fos = new FileOutputStream(Ffilepath1);
                            fos.write(fileDetailsVO.getVgmFileData1());
                        }
                        flag = true;
                        retFilePath += filename6 + "~~~";
                    }
                    if (fileDetailsVO.getVgmFileData2() != null && fileDetailsVO.getVgmFileName2() != null
                            && fileDetailsVO.getVgmFileName2().length() > 0) {
                        String filename7 = EcomConsts.FILETYPE_VMG + "_" + fileDetailsVO.getVgmFileName2();
                        if (isconvertzip) {
                            filehm.put(filename7, fileDetailsVO.getVgmFileData2());
                        } else {
                            File Ffilepath1 = new File(fileDetailsVO.getFilePath(), filename7);
                            fos = new FileOutputStream(Ffilepath1);
                            fos.write(fileDetailsVO.getVgmFileData2());
                        }
                        flag = true;
                        retFilePath += filename7 + "~~~";
                    }
                    if (filehm != null && filehm.size() > 0) {
                        zipconv.convertZipFile(EcomConsts.FILETYPE_VMG + "_" + bknum, saveFilePath, filehm);
                        zipFileName = zipFileName + EcomConsts.FILETYPE_VMG + "_" + bknum + ".zip" + "~~~";
                    }
                    filehm = new HashMap<String, byte[]>();
                    if (fileDetailsVO.getDgFileName() != null && fileDetailsVO.getDgFileName().trim().length() > 0) {
                        String dgFileName = fileDetailsVO.getDgFileName();
                        if (fileDetailsVO.getDgFileData() != null) {
                            dgFileName = "DG_" + fileDetailsVO.getDgFileName();
                            if (isconvertzip) {
                                filehm.put(dgFileName, fileDetailsVO.getDgFileData());
                            } else {
                                File dgFilePath = new File(saveFilePath, dgFileName);
                                fos = new FileOutputStream(dgFilePath);
                                fos.write(fileDetailsVO.getDgFileData());
                            }
                            flag = true;
                        }

                        ps.setString(1, bknum);
                        ps.setString(2, "DG");
                        ps.setString(3, dgFileName);
                        // ps.executeUpdate();

                        retFilePath += dgFileName + "~~~";
                    }
                    if (fileDetailsVO.getDgFileData1() != null && fileDetailsVO.getDgFileName1() != null
                            && fileDetailsVO.getDgFileName1().length() > 0) {
                        String filename8 = EcomConsts.FILETYPE_DGC + "_" + fileDetailsVO.getDgFileName1();
                        if (isconvertzip) {
                            filehm.put(filename8, fileDetailsVO.getDgFileData1());
                        } else {
                            File Ffilepath2 = new File(fileDetailsVO.getFilePath(), filename8);
                            fos = new FileOutputStream(Ffilepath2);
                            fos.write(fileDetailsVO.getDgFileData1());
                        }
                        flag = true;
                        retFilePath += filename8 + "~~~";
                    }
                    if (fileDetailsVO.getDgFileData2() != null && fileDetailsVO.getDgFileName2() != null
                            && fileDetailsVO.getDgFileName2().length() > 0) {
                        String filename9 = EcomConsts.FILETYPE_DGC + "_" + fileDetailsVO.getDgFileName2();
                        if (isconvertzip) {
                            filehm.put(filename9, fileDetailsVO.getDgFileData2());
                        } else {
                            File Ffilepath2 = new File(fileDetailsVO.getFilePath(), filename9);
                            fos = new FileOutputStream(Ffilepath2);
                            fos.write(fileDetailsVO.getDgFileData2());
                        }
                        flag = true;
                        retFilePath += filename9 + "~~~";
                    }
                    if (filehm != null && filehm.size() > 0) {
                        zipconv.convertZipFile(EcomConsts.FILETYPE_DGC + "_" + bknum, saveFilePath, filehm);
                        zipFileName = zipFileName + EcomConsts.FILETYPE_DGC + "_" + bknum + ".zip" + "~~~";
                    }
                    filehm = new HashMap<String, byte[]>();
                    if (fileDetailsVO.getLolFileName() != null && fileDetailsVO.getLolFileName().trim().length() > 0) {
                        String lolFileName = fileDetailsVO.getLolFileName();
                        if (fileDetailsVO.getLolFileData() != null) {
                            lolFileName = "LOL_" + fileDetailsVO.getLolFileName();
                            if (isconvertzip) {
                                filehm.put(lolFileName, fileDetailsVO.getLolFileData());
                            } else {
                                File lolFilePath = new File(saveFilePath, lolFileName);
                                fos = new FileOutputStream(lolFilePath);
                                fos.write(fileDetailsVO.getLolFileData());
                            }
                            flag = true;
                        }

                        ps.setString(1, bknum);
                        ps.setString(2, "LOL");
                        ps.setString(3, lolFileName);
                        // ps.executeUpdate();

                        retFilePath += lolFileName + "~~~";
                    }
                    if (fileDetailsVO.getLolFileData1() != null && fileDetailsVO.getLolFileName1() != null
                            && fileDetailsVO.getLolFileName1().length() > 0) {
                        String filename10 = EcomConsts.FILETYPE_LOL + "_" + fileDetailsVO.getLolFileName1();
                        if (isconvertzip) {
                            filehm.put(filename10, fileDetailsVO.getLolFileData1());
                        } else {
                            File Ffilepath3 = new File(fileDetailsVO.getFilePath(), filename10);
                            fos = new FileOutputStream(Ffilepath3);
                            fos.write(fileDetailsVO.getLolFileData1());
                        }
                        flag = true;
                        retFilePath += filename10 + "~~~";

                    }
                    if (fileDetailsVO.getLolFileData2() != null && fileDetailsVO.getLolFileName2() != null
                            && fileDetailsVO.getLolFileName2().length() > 0) {
                        String filename11 = EcomConsts.FILETYPE_LOL + "_" + fileDetailsVO.getLolFileName2();
                        if (isconvertzip) {
                            filehm.put(filename11, fileDetailsVO.getLolFileData2());
                        } else {
                            File Ffilepath3 = new File(fileDetailsVO.getFilePath(), filename11);
                            fos = new FileOutputStream(Ffilepath3);
                            fos.write(fileDetailsVO.getLolFileData2());
                        }
                        flag = true;
                        retFilePath += filename11 + "~~~";

                    }
                    if (filehm != null && filehm.size() > 0) {
                        zipconv.convertZipFile(EcomConsts.FILETYPE_LOL + "_" + bknum, saveFilePath, filehm);
                        zipFileName = zipFileName + EcomConsts.FILETYPE_LOL + "_" + bknum + ".zip" + "~~~";
                    }
                    filehm = new HashMap<String, byte[]>();
                    if (fileDetailsVO.getMsdsFileName() != null
                            && fileDetailsVO.getMsdsFileName().trim().length() > 0) {
                        String msdsFileName = fileDetailsVO.getMsdsFileName();
                        if (fileDetailsVO.getMsdsFileData() != null) {
                            msdsFileName = "MSD_" + fileDetailsVO.getMsdsFileName();
                            if (isconvertzip) {
                                filehm.put(msdsFileName, fileDetailsVO.getMsdsFileData());
                            } else {
                                File msdsFilePath = new File(saveFilePath, msdsFileName);
                                fos = new FileOutputStream(msdsFilePath);
                                fos.write(fileDetailsVO.getMsdsFileData());
                            }
                            flag = true;
                        }

                        ps.setString(1, bknum);
                        ps.setString(2, "MSD");
                        ps.setString(3, msdsFileName);
                        // ps.executeUpdate();

                        retFilePath += msdsFileName + "~~~";
                    }
                    if (fileDetailsVO.getMsdsFileData1() != null && fileDetailsVO.getMsdsFileName1() != null
                            && fileDetailsVO.getMsdsFileName1().length() > 0) {
                        String filename12 = EcomConsts.FILETYPE_MSD + "_" + fileDetailsVO.getMsdsFileName1();
                        if (isconvertzip) {
                            filehm.put(filename12, fileDetailsVO.getMsdsFileData1());
                        } else {
                            File Ffilepath4 = new File(fileDetailsVO.getFilePath(), filename12);
                            fos = new FileOutputStream(Ffilepath4);
                            fos.write(fileDetailsVO.getMsdsFileData1());
                        }
                        flag = true;
                        retFilePath += filename12 + "~~~";
                    }
                    if (fileDetailsVO.getMsdsFileData2() != null && fileDetailsVO.getMsdsFileName2() != null
                            && fileDetailsVO.getMsdsFileName2().length() > 0) {
                        String filename13 = EcomConsts.FILETYPE_MSD + "_" + fileDetailsVO.getMsdsFileName2();
                        if (isconvertzip) {
                            filehm.put(filename13, fileDetailsVO.getMsdsFileData2());
                        } else {
                            File Ffilepath4 = new File(fileDetailsVO.getFilePath(), filename13);
                            fos = new FileOutputStream(Ffilepath4);
                            fos.write(fileDetailsVO.getMsdsFileData2());
                        }
                        flag = true;
                        retFilePath += filename13 + "~~~";
                    }
                    if (filehm != null && filehm.size() > 0) {
                        zipconv.convertZipFile(EcomConsts.FILETYPE_MSD + "_" + bknum, saveFilePath, filehm);
                        zipFileName = zipFileName + EcomConsts.FILETYPE_MSD + "_" + bknum + ".zip" + "~~~";
                    }
                    filehm = new HashMap<String, byte[]>();
                    if (fileDetailsVO.getOthersFileName() != null
                            && fileDetailsVO.getOthersFileName().trim().length() > 0) {
                        String othersFileName = fileDetailsVO.getOthersFileName();
                        if (fileDetailsVO.getOthersFileData() != null) {
                            othersFileName = "OT_" + fileDetailsVO.getOthersFileName();
                            if (isconvertzip) {
                                filehm.put(othersFileName, fileDetailsVO.getOthersFileData());
                            } else {
                                File othersFilePath = new File(saveFilePath, othersFileName);
                                fos = new FileOutputStream(othersFilePath);
                                fos.write(fileDetailsVO.getOthersFileData());
                            }
                            flag = true;
                        }

                        ps.setString(1, bknum);
                        ps.setString(2, "OT");
                        ps.setString(3, othersFileName);
                        // ps.executeUpdate();

                        retFilePath += othersFileName + "~~~";
                    }

                    if (fileDetailsVO.getOthersFileData1() != null && fileDetailsVO.getOthersFileName1() != null
                            && fileDetailsVO.getOthersFileName1().length() > 0) {
                        String filename14 = EcomConsts.FILETYPE_OTHERS + "_" + fileDetailsVO.getOthersFileName1();
                        if (isconvertzip) {
                            filehm.put(filename14, fileDetailsVO.getOthersFileData1());
                        } else {
                            File Ffilepath5 = new File(fileDetailsVO.getFilePath(), filename14);
                            fos = new FileOutputStream(Ffilepath5);
                            fos.write(fileDetailsVO.getOthersFileData1());
                        }
                        flag = true;
                        retFilePath += filename14 + "~~~";
                    }
                    if (fileDetailsVO.getOthersFileData2() != null && fileDetailsVO.getOthersFileName2() != null
                            && fileDetailsVO.getOthersFileName2().length() > 0) {
                        String filename15 = EcomConsts.FILETYPE_OTHERS + "_" + fileDetailsVO.getOthersFileName2();
                        if (isconvertzip) {
                            filehm.put(filename15, fileDetailsVO.getOthersFileData2());
                        } else {
                            File Ffilepath5 = new File(fileDetailsVO.getFilePath(), filename15);
                            fos = new FileOutputStream(Ffilepath5);
                            fos.write(fileDetailsVO.getOthersFileData2());
                        }
                        flag = true;
                        retFilePath += filename15 + "~~~";
                    }
                    if (filehm != null && filehm.size() > 0) {
                        zipconv.convertZipFile(EcomConsts.FILETYPE_OTHERS + "_" + bknum, saveFilePath, filehm);
                        zipFileName = zipFileName + EcomConsts.FILETYPE_OTHERS + "_" + bknum + ".zip" + "~~~";
                    }
                    if (isconvertzip) {
                        if (zipFileName != null && zipFileName.length() > 3) {
                            retFilePath = zipFileName.substring(0, zipFileName.length() - 3);
                        }
                    }
                    System.out.println("[ECOM] EcomShippingInstructionBean retFilePath=> " + retFilePath
                            + " >>filepath>>112233>si up> " + retFilePath);
                    if (flag) {
                        if (fos != null) {
                            fos.flush();
                            fos.close();
                        }
                    }
                }
                if ("S".equalsIgnoreCase(savetype)) {
                    msgtype = "insert";
                    if ("1".equalsIgnoreCase(appVersion)) {
                        returnVO = v1SIBeanObj.saveToSI(bookno, custcode, bookinguserid, cntMapObj, hm, partiesVO,
                                savetype, bknum, voidCargoObj, voidComObj, username1, tbl_no, client_code, addremarks);
                    } else {
                        Si_Type = "S";
                        siSAVETYPE = "S";
                        String polAtNEW = "";
                        String podAtNEW = "";
                        String readerid = null;
                        String agencyRefNo = null, intransitPort = null, intransitName = null, transportTermId = null,
                                trptsvccode = null;
                        readerid = bookNoGenerate("ESI");
                        System.out.println("[ECOM] EcomShippingInstructionBean si readeerid::::" + readerid);
                        returnVO.setStrSaveType("S");
                        System.out.println("[ECOM] EcomShippingInstructionBean === bknum is=========>" + bknum
                                + "=== bookno is=========>" + bookno);
                        Map hsm = (Map) siVDS.getEcomEDIDocstatus(bknum);
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
                        EcomBKGEDIDocStatus updateDocStatusVo = new EcomBKGEDIDocStatus();
                        ArrayList updateDocStatusList = new ArrayList();
                        updateDocStatusVo.setBook_no(bknum);
                        updateDocStatusList.add(updateDocStatusVo);
                        EcomBKGCommonModel updateDocStatusModel = new EcomBKGCommonModel();
                        updateDocStatusModel.setEdidocstatus(updateDocStatusList);
                        siVDS.putecomedidocstatus(updateDocStatusModel);
                        CompleteModel siModelObj = siVDS.getecombookingdetails(bookno);

                        for (Booking bookingObj : siModelObj.getBooking()) {
                            documentalAgencyid = bookingObj.getDocumentalagencyid();
                            agencyid = bookingObj.getAgencyid();
                            agencyRefNo = checkNull(bookingObj.getAgencyRefNo());
                            intransitPort = checkNull(bookingObj.getIntransitport());
                            intransitName = checkNull(bookingObj.getIntransitportname());
                            transportTermId = checkNull(bookingObj.getTransporttermid());
                            trptsvccode = checkNull(bookingObj.getTransportserviceid());
                            System.out.println("[ECOM] EcomShippingInstructionBean >>>intransitPort>>>" + intransitPort
                                    + ">>>intransitName>>>" + intransitName);
                            oceansercode = bookingObj.getOceansercode();
                            oceanvoyage = bookingObj.getOceanvoyage();
                            oceanvslcode = bookingObj.getOceanvslcode();
                            oceanBound = bookingObj.getOceanbound();
                            serviceCode = bookingObj.getServicecode();
                            polTerminal = bookingObj.getPolterminal();
                            polAtNEW = bookingObj.getPolat();
                            podTerminal = bookingObj.getPodterminal();
                            podAtNEW = bookingObj.getPodat();
                            booktype = bookingObj.getBooktype();
                            bookReady = bookingObj.getBookReady();
                            bookDate = bookingObj.getBookdate();
                            transportserviceid = bookingObj.getTransportserviceid();
                            preCarriage = bookingObj.getPrecarriage();
                            onCarriage = bookingObj.getOncarriage();
                            usercr = bookingObj.getUsercr();
                            originportid = bookingObj.getOriginportid();
                            finalPortId = bookingObj.getFinalportid();
                            originportdesc = bookingObj.getOriginportdesc();
                            finalportdesc = bookingObj.getFinalportdesc();

                        }

                        CompleteModel servType = siVDS.getecomservicetype(serviceCode);
                        for (Service serviceObj : servType.getService()) {
                            firstsvctype = serviceObj.getServicetype();
                        }

                        ArrayList ediTempBookingLst = new ArrayList();
                        EcomBKGMasterVO ediTempBookingVo = new EcomBKGMasterVO();
                        ediTempBookingVo.setBook_no(bknum);
                        ediTempBookingVo.setAgencyid(agencyid);
                        ediTempBookingVo.setOceanvslcode(oceanvslcode);
                        ediTempBookingVo.setOceanvoyage(oceanvoyage);
                        ediTempBookingVo.setOceansercode(oceansercode);
                        ediTempBookingVo.setOceanbound(oceanBound);
                        ediTempBookingVo.setServicecode(serviceCode);
                        ediTempBookingVo.setVesselcode(partiesVO.getVesselcode());
                        ediTempBookingVo.setVoyagecode(partiesVO.getVoyagecode());
                        ediTempBookingVo.setBound(partiesVO.getBound());
                        ediTempBookingVo.setOriginportid(originportid);
                        ediTempBookingVo.setFinalportid(finalPortId);
                        ediTempBookingVo.setLoadportid(partiesVO.getPol());
                        ediTempBookingVo.setDischargeportid(partiesVO.getPod());
                        ediTempBookingVo.setPolterminal(polTerminal);
                        System.out.println("[ECOM] EcomShippingInstructionBean polAtNEW: " + polAtNEW + ", podAtNEW: "
                                + podAtNEW + ", bookDate: " + bookDate);
                        ediTempBookingVo.setPolat(polAtNEW);
                        ediTempBookingVo.setPodterminal(podTerminal);
                        ediTempBookingVo.setPodat(podAtNEW);
                        ediTempBookingVo.setBookdate(bookDate);
                        ediTempBookingVo.setBookready(bookReady);
                        ediTempBookingVo.setPrecarriage(preCarriage);
                        ediTempBookingVo.setOncarriage(onCarriage);
                        ediTempBookingVo.setStatus("C");
                        ediTempBookingVo.setUsercr("Ecom");
                        ediTempBookingVo.setMessagetype(GeneralConf.EDI_MESSAGETYPE_SI);
                        ediTempBookingVo.setTranstype(savetype);
                        ediTempBookingVo.setEdireaderid(readerid);
                        ediTempBookingVo.setDocumentalagencyid(documentalAgencyid);
                        ediTempBookingVo.setAgencyissueid(agencyid);
                        ediTempBookingVo.setAgencypolid(agencyid);
                        ediTempBookingVo.setRemarks(marks);
                        ediTempBookingVo.setFirstsvctype(firstsvctype);
                        ediTempBookingVo.setBooktype("online");
                        ediTempBookingVo.setCrossBook(crossbook);
                        ediTempBookingVo.setOriginportdesc(originportdesc);
                        ediTempBookingVo.setFinalportdesc(finalportdesc);
                        ediTempBookingVo.setShipment_type(shipment_type);
                        ediTempBookingVo.setPaymenttermsid("pay");
                        ediTempBookingVo.setBooksource(EcomConsts.SET_BOOK_SOURCE);
                        ediTempBookingVo.setMessagesender("EcomOnlineSI");
                        ediTempBookingVo.setCarriercode(EcomConsts.SET_CARRIER_CODE);
                        ediTempBookingVo.setCallid(callid);
                        ediTempBookingVo.setCustremarks(addremarks);
                        ediTempBookingVo.setAgencyrefno(agencyRefNo);
                        ediTempBookingVo.setIntransitport(intransitPort);
                        ediTempBookingVo.setIntransitportname(intransitName);
                        ediTempBookingVo.setTransportserviceid(transportserviceid);
                        ediTempBookingVo.setTransporttermid(transportTermId);
                        ediTempBookingVo.setTbook_no(bookno);
                        ediTempBookingVo.setVersion("0");
                        ediTempBookingVo.setTbl_no(null);
                        ediTempBookingVo.setT2lfiling(partiesVO.getEbook_t2lfiling());
                        ediTempBookingVo.setNvocc(resolveNvoccForDb(partiesVO.getEbook_nvocc()));
                        ediTempBookingVo.setEnsdoctype(partiesVO.getEbook_EnsDocType());

                        if (partiesVO.getCargoprotectid() != null && partiesVO.getCargoprotectid().length() > 0) {
                            ediTempBookingVo.setCargoprotectid(partiesVO.getCargoprotectid());
                        }

                        EcomBKGCommonModel ediTempBkgModelObj = new EcomBKGCommonModel();
                        ediTempBookingLst.add(ediTempBookingVo);
                        ediTempBkgModelObj.setEditempbooking(ediTempBookingLst);

                        siVDS.setecomeditempbooking(ediTempBkgModelObj);

                        //
                        loadportdesc = getPortName(partiesVO.getPol());
                        dischargeportdesc = getPortName(partiesVO.getPod());
                        String vesslename = getVesselName(partiesVO.getVesselcode());
                        vesslename = vesslename + "/" + partiesVO.getVoyagecode() + "/" + partiesVO.getBound();
                        //
                        // Parties
                        ArrayList ediTempBookingCustLstSH = new ArrayList();
                        EcomBKGCustomer ediTempBookingSHCustVo = new EcomBKGCustomer();
                        SISubmitVO objSH = (SISubmitVO) hm.get("SH");
                        ediTempBookingSHCustVo.setBook_no(bknum);
                        ediTempBookingSHCustVo.setCusttypeid("SH");
                        ediTempBookingSHCustVo.setCustid(checkNull(objSH.getCustCode()));
                        ediTempBookingSHCustVo.setCountryid(checkNull(objSH.getCountryid()));
                        ediTempBookingSHCustVo.setName(checkNull(objSH.getCustname()));
                        ediTempBookingSHCustVo.setAddress1(checkNull(objSH.getAddress()));
                        ediTempBookingSHCustVo.setAddress2(checkNull(objSH.getAddress1()));
                        ediTempBookingSHCustVo.setCity(checkNull(objSH.getCity()));
                        ediTempBookingSHCustVo.setZipCode(checkNull(objSH.getZipCode()));
                        ediTempBookingSHCustVo.setTelephone(checkNull(objSH.getTelephone()));
                        ediTempBookingSHCustVo.setFax(checkNull(objSH.getFax()));
                        ediTempBookingSHCustVo.setEmail(checkNull(objSH.getEmail()));
                        ediTempBookingSHCustVo.setStateid(checkNull(objSH.getState()));
                        ediTempBookingSHCustVo.setEori(checkNull(objSH.getEori()));
                        ediTempBookingSHCustVo.setPersontype(checkNull(objSH.getPersontype()));
                        ediTempBookingSHCustVo.setEdireaderid(readerid);
                        ediTempBookingCustLstSH.add(ediTempBookingSHCustVo);
                        EcomBKGCommonModel ediTempBkgCustModelObj = new EcomBKGCommonModel();
                        ediTempBkgCustModelObj.setEditempbooking_cust(ediTempBookingCustLstSH);
                        siVDS.setecomeditempbookingcustomer(ediTempBkgCustModelObj);

                        ArrayList ediTempBookingCustLstCO = new ArrayList();
                        EcomBKGCustomer ediTempBookingCOCustVo = new EcomBKGCustomer();
                        SISubmitVO objCO = (SISubmitVO) hm.get("CN");
                        ediTempBookingCOCustVo.setBook_no(bknum);
                        ediTempBookingCOCustVo.setCusttypeid("CN");
                        ediTempBookingCOCustVo.setCustid(checkNull(objCO.getCustCode()));
                        ediTempBookingCOCustVo.setCountryid(checkNull(objCO.getCountryid()));
                        ediTempBookingCOCustVo.setName(checkNull(objCO.getCustname()));
                        ediTempBookingCOCustVo.setAddress1(checkNull(objCO.getAddress()));
                        ediTempBookingCOCustVo.setAddress2(checkNull(objCO.getAddress1()));
                        ediTempBookingCOCustVo.setCity(checkNull(objCO.getCity()));
                        ediTempBookingCOCustVo.setZipCode(checkNull(objCO.getZipCode()));
                        ediTempBookingCOCustVo.setTelephone(checkNull(objCO.getTelephone()));
                        ediTempBookingCOCustVo.setFax(checkNull(objCO.getFax()));
                        ediTempBookingCOCustVo.setEmail(checkNull(objCO.getEmail()));
                        ediTempBookingCOCustVo.setStateid(checkNull(objCO.getState()));
                        ediTempBookingCOCustVo.setEdireaderid(readerid);
                        ediTempBookingCOCustVo.setEori(checkNull(objCO.getEori()));
                        ediTempBookingCOCustVo.setPersontype(checkNull(objCO.getPersontype()));
                        ediTempBookingCustLstCO.add(ediTempBookingCOCustVo);
                        EcomBKGCommonModel ediTempBkgCustCOModelObj = new EcomBKGCommonModel();
                        ediTempBkgCustCOModelObj.setEditempbooking_cust(ediTempBookingCustLstCO);
                        siVDS.setecomeditempbookingcustomer(ediTempBkgCustCOModelObj);

                        ArrayList ediTempBookingCustLstNT = new ArrayList();
                        EcomBKGCustomer ediTempBookingNTCustVo = new EcomBKGCustomer();
                        SISubmitVO objNT = (SISubmitVO) hm.get("NT");
                        if (objNT.getCustname() != null && objNT.getCustname().length() > 0
                                && checkNull(partiesVO.getNotifyName()).length() > 0) {
                            ediTempBookingNTCustVo.setBook_no(bknum);
                            ediTempBookingNTCustVo.setCusttypeid("NT");
                            ediTempBookingNTCustVo.setCustid(checkNull(objNT.getCustCode()));
                            ediTempBookingNTCustVo.setCountryid(checkNull(objNT.getCountryid()));
                            ediTempBookingNTCustVo.setName(checkNull(objNT.getCustname()));
                            ediTempBookingNTCustVo.setAddress1(checkNull(objNT.getAddress()));
                            ediTempBookingNTCustVo.setAddress2(checkNull(objNT.getAddress1()));
                            ediTempBookingNTCustVo.setCity(checkNull(objNT.getCity()));
                            ediTempBookingNTCustVo.setZipCode(checkNull(objNT.getZipCode()));
                            ediTempBookingNTCustVo.setTelephone(checkNull(objNT.getTelephone()));
                            ediTempBookingNTCustVo.setFax(checkNull(objNT.getFax()));
                            ediTempBookingNTCustVo.setEmail(checkNull(objNT.getEmail()));
                            ediTempBookingNTCustVo.setStateid(checkNull(objNT.getState()));
                            ediTempBookingNTCustVo.setEdireaderid(readerid);
                            ediTempBookingNTCustVo.setEori(checkNull(objNT.getEori()));
                            ediTempBookingNTCustVo.setPersontype(checkNull(objNT.getPersontype()));
                            ediTempBookingCustLstNT.add(ediTempBookingNTCustVo);
                            EcomBKGCommonModel ediTempBkgCustNTModelObj = new EcomBKGCommonModel();
                            ediTempBkgCustNTModelObj.setEditempbooking_cust(ediTempBookingCustLstNT);
                            siVDS.setecomeditempbookingcustomer(ediTempBkgCustNTModelObj);
                        }

                        ArrayList ediTempBookingCustLstNT2 = new ArrayList();
                        EcomBKGCustomer ediTempBookingNT2CustVo = new EcomBKGCustomer();
                        SISubmitVO objNT2 = (SISubmitVO) hm.get("N1");
                        if (objNT2.getCustname() != null && objNT2.getCustname().length() > 0) {
                            ediTempBookingNT2CustVo.setBook_no(bknum);
                            ediTempBookingNT2CustVo.setCusttypeid("N1");
                            ediTempBookingNT2CustVo.setCustid(checkNull(objNT2.getCustCode()));
                            ediTempBookingNT2CustVo.setCountryid(checkNull(objNT2.getCountryid()));
                            ediTempBookingNT2CustVo.setName(checkNull(objNT2.getCustname()));
                            ediTempBookingNT2CustVo.setAddress1(checkNull(objNT2.getAddress()));
                            ediTempBookingNT2CustVo.setAddress2(checkNull(objNT2.getAddress1()));
                            ediTempBookingNT2CustVo.setCity(checkNull(objNT2.getCity()));
                            ediTempBookingNT2CustVo.setZipCode(checkNull(objNT2.getZipCode()));
                            ediTempBookingNT2CustVo.setTelephone(checkNull(objNT2.getTelephone()));
                            ediTempBookingNT2CustVo.setFax(checkNull(objNT2.getFax()));
                            ediTempBookingNT2CustVo.setEmail(checkNull(objNT2.getEmail()));
                            ediTempBookingNT2CustVo.setStateid(checkNull(objNT2.getState()));
                            ediTempBookingNT2CustVo.setEdireaderid(readerid);
                            ediTempBookingNT2CustVo.setEori(checkNull(objNT2.getEori()));
                            ediTempBookingNT2CustVo.setPersontype(checkNull(objNT2.getPersontype()));
                            ediTempBookingCustLstNT2.add(ediTempBookingNT2CustVo);
                            EcomBKGCommonModel ediTempBkgCustNT2ModelObj = new EcomBKGCommonModel();
                            ediTempBkgCustNT2ModelObj.setEditempbooking_cust(ediTempBookingCustLstNT2);
                            siVDS.setecomeditempbookingcustomer(ediTempBkgCustNT2ModelObj);
                        }

                        ArrayList ediTempBookingCustLstNT3 = new ArrayList();
                        EcomBKGCustomer ediTempBookingNT3CustVo = new EcomBKGCustomer();
                        SISubmitVO objNT3 = (SISubmitVO) hm.get("N2");
                        if (objNT3.getCustname() != null && objNT3.getCustname().length() > 0) {
                            ediTempBookingNT3CustVo.setBook_no(bknum);
                            ediTempBookingNT3CustVo.setCusttypeid("N2");
                            ediTempBookingNT3CustVo.setCustid(checkNull(objNT3.getCustCode()));
                            ediTempBookingNT3CustVo.setCountryid(checkNull(objNT3.getCountryid()));
                            ediTempBookingNT3CustVo.setName(checkNull(objNT3.getCustname()));
                            ediTempBookingNT3CustVo.setAddress1(checkNull(objNT3.getAddress()));
                            ediTempBookingNT3CustVo.setAddress2(checkNull(objNT3.getAddress1()));
                            ediTempBookingNT3CustVo.setCity(checkNull(objNT3.getCity()));
                            ediTempBookingNT3CustVo.setZipCode(checkNull(objNT3.getZipCode()));
                            ediTempBookingNT3CustVo.setTelephone(checkNull(objNT3.getTelephone()));
                            ediTempBookingNT3CustVo.setFax(checkNull(objNT3.getFax()));
                            ediTempBookingNT3CustVo.setEmail(checkNull(objNT3.getEmail()));
                            ediTempBookingNT3CustVo.setStateid(checkNull(objNT3.getState()));
                            ediTempBookingNT3CustVo.setEdireaderid(readerid);
                            ediTempBookingNT3CustVo.setEori(checkNull(objNT3.getEori()));
                            ediTempBookingNT3CustVo.setPersontype(checkNull(objNT3.getPersontype()));
                            ediTempBookingCustLstNT3.add(ediTempBookingNT3CustVo);
                            EcomBKGCommonModel ediTempBkgCustNT3ModelObj = new EcomBKGCommonModel();
                            ediTempBkgCustNT3ModelObj.setEditempbooking_cust(ediTempBookingCustLstNT3);
                            siVDS.setecomeditempbookingcustomer(ediTempBkgCustNT3ModelObj);
                        }
                        // FW
                        ArrayList ediTempBookingCustLstFW = new ArrayList();
                        EcomBKGCustomer ediTempBookingFWCustVo = new EcomBKGCustomer();
                        SISubmitVO objFW = (SISubmitVO) hm.get("FW");
                        if (objFW.getCustname() != null && objFW.getCustname().length() > 0) {
                            ediTempBookingFWCustVo.setBook_no(bknum);
                            ediTempBookingFWCustVo.setCusttypeid("FW");
                            ediTempBookingFWCustVo.setCustid(checkNull(objFW.getCustCode()));
                            ediTempBookingFWCustVo.setCountryid(checkNull(objFW.getCountryid()));
                            ediTempBookingFWCustVo.setName(checkNull(objFW.getCustname()));
                            ediTempBookingFWCustVo.setAddress1(checkNull(objFW.getAddress()));
                            ediTempBookingFWCustVo.setAddress2(checkNull(objFW.getAddress1()));
                            ediTempBookingFWCustVo.setCity(checkNull(objFW.getCity()));
                            ediTempBookingFWCustVo.setZipCode(checkNull(objFW.getZipCode()));
                            ediTempBookingFWCustVo.setTelephone(checkNull(objFW.getTelephone()));
                            ediTempBookingFWCustVo.setFax(checkNull(objFW.getFax()));
                            ediTempBookingFWCustVo.setEmail(checkNull(objFW.getEmail()));
                            ediTempBookingFWCustVo.setStateid(checkNull(objFW.getState()));
                            ediTempBookingFWCustVo.setEdireaderid(readerid);
                            ediTempBookingFWCustVo.setEori(checkNull(objFW.getEori()));
                            ediTempBookingFWCustVo.setPersontype(checkNull(objFW.getPersontype()));
                            ediTempBookingCustLstFW.add(ediTempBookingFWCustVo);
                            EcomBKGCommonModel ediTempBkgCustFWModelObj = new EcomBKGCommonModel();
                            ediTempBkgCustFWModelObj.setEditempbooking_cust(ediTempBookingCustLstFW);
                            siVDS.setecomeditempbookingcustomer(ediTempBkgCustFWModelObj);
                        }

                        // BY
                        ArrayList ediTempBookingCustLstBY = new ArrayList();
                        EcomBKGCustomer ediTempBookingBYCustVo = new EcomBKGCustomer();
                        SISubmitVO objBY = (SISubmitVO) hm.get("BY");
                        if (objBY.getCustname() != null && objBY.getCustname().length() > 0) {
                            ediTempBookingBYCustVo.setBook_no(bknum);
                            ediTempBookingBYCustVo.setCusttypeid("BY");
                            ediTempBookingBYCustVo.setCustid(checkNull(objBY.getCustCode()));
                            ediTempBookingBYCustVo.setCountryid(checkNull(objBY.getCountryid()));
                            ediTempBookingBYCustVo.setName(checkNull(objBY.getCustname()));
                            ediTempBookingBYCustVo.setAddress1(checkNull(objBY.getAddress()));
                            ediTempBookingBYCustVo.setAddress2(checkNull(objBY.getAddress1()));
                            ediTempBookingBYCustVo.setCity(checkNull(objBY.getCity()));
                            ediTempBookingBYCustVo.setZipCode(checkNull(objBY.getZipCode()));
                            ediTempBookingBYCustVo.setTelephone(checkNull(objBY.getTelephone()));
                            ediTempBookingBYCustVo.setFax(checkNull(objBY.getFax()));
                            ediTempBookingBYCustVo.setEmail(checkNull(objBY.getEmail()));
                            ediTempBookingBYCustVo.setStateid(checkNull(objBY.getState()));
                            ediTempBookingBYCustVo.setEdireaderid(readerid);
                            ediTempBookingBYCustVo.setEori(checkNull(objBY.getEori()));
                            ediTempBookingBYCustVo.setPersontype(checkNull(objBY.getPersontype()));
                            ediTempBookingCustLstBY.add(ediTempBookingBYCustVo);
                            EcomBKGCommonModel ediTempBkgCustBYModelObj = new EcomBKGCommonModel();
                            ediTempBkgCustBYModelObj.setEditempbooking_cust(ediTempBookingCustLstBY);
                            siVDS.setecomeditempbookingcustomer(ediTempBkgCustBYModelObj);
                        }

                        // SL
                        ArrayList ediTempBookingCustLstSL = new ArrayList();
                        EcomBKGCustomer ediTempBookingSLCustVo = new EcomBKGCustomer();
                        SISubmitVO objSL = (SISubmitVO) hm.get("SL");
                        if (objSL.getCustname() != null && objSL.getCustname().length() > 0) {
                            ediTempBookingSLCustVo.setBook_no(bknum);
                            ediTempBookingSLCustVo.setCusttypeid("SL");
                            ediTempBookingSLCustVo.setCustid(checkNull(objSL.getCustCode()));
                            ediTempBookingSLCustVo.setCountryid(checkNull(objSL.getCountryid()));
                            ediTempBookingSLCustVo.setName(checkNull(objSL.getCustname()));
                            ediTempBookingSLCustVo.setAddress1(checkNull(objSL.getAddress()));
                            ediTempBookingSLCustVo.setAddress2(checkNull(objSL.getAddress1()));
                            ediTempBookingSLCustVo.setCity(checkNull(objSL.getCity()));
                            ediTempBookingSLCustVo.setZipCode(checkNull(objSL.getZipCode()));
                            ediTempBookingSLCustVo.setTelephone(checkNull(objSL.getTelephone()));
                            ediTempBookingSLCustVo.setFax(checkNull(objSL.getFax()));
                            ediTempBookingSLCustVo.setEmail(checkNull(objSL.getEmail()));
                            ediTempBookingSLCustVo.setStateid(checkNull(objSL.getState()));
                            ediTempBookingSLCustVo.setEdireaderid(readerid);
                            ediTempBookingSLCustVo.setEori(checkNull(objSL.getEori()));
                            ediTempBookingSLCustVo.setPersontype(checkNull(objSL.getPersontype()));
                            ediTempBookingCustLstSL.add(ediTempBookingSLCustVo);
                            EcomBKGCommonModel ediTempBkgCustSLModelObj = new EcomBKGCommonModel();
                            ediTempBkgCustSLModelObj.setEditempbooking_cust(ediTempBookingCustLstSL);
                            siVDS.setecomeditempbookingcustomer(ediTempBkgCustSLModelObj);
                        }
                        ediTempBookingCustLstSL.clear();
                        if (custMap != null && custMap.containsKey("Seller")) {
                            List<SISubmitVO> sellerList = (List<SISubmitVO>) custMap.get("Seller");
                            if (sellerList != null && !sellerList.isEmpty()) {
                                for (SISubmitVO SLVO : sellerList) {
                                    if (SLVO.getCustname() != null && SLVO.getCustname().length() > 0) {
                                        EcomBKGCustomer ediTempBookingSLListCustVo = new EcomBKGCustomer();
                                        ediTempBookingSLListCustVo.setBook_no(bknum);
                                        ediTempBookingSLListCustVo.setCusttypeid("SL");
                                        ediTempBookingSLListCustVo.setCustid(checkNull(SLVO.getCustCode()));
                                        ediTempBookingSLListCustVo.setCountryid(extractCountryCode(checkNull(SLVO.getCountryid())));
                                        ediTempBookingSLListCustVo.setName(checkNull(SLVO.getCustname()));
                                        ediTempBookingSLListCustVo.setAddress1(checkNull(SLVO.getAddress()));
                                        ediTempBookingSLListCustVo.setAddress2(checkNull(SLVO.getAddress1()));
                                        ediTempBookingSLListCustVo.setCity(checkNull(SLVO.getCity()));
                                        ediTempBookingSLListCustVo.setZipCode(checkNull(SLVO.getZipCode()));
                                        ediTempBookingSLListCustVo.setTelephone(checkNull(SLVO.getTelephone()));
                                        ediTempBookingSLListCustVo.setFax(checkNull(SLVO.getFax()));
                                        ediTempBookingSLListCustVo.setEmail(checkNull(SLVO.getEmail()));
                                        ediTempBookingSLListCustVo.setStateid(checkNull(SLVO.getState()));
                                        ediTempBookingSLListCustVo.setEdireaderid(readerid);
                                        ediTempBookingSLListCustVo.setEori(checkNull(SLVO.getEori()));
                                        ediTempBookingSLListCustVo.setPersontype(checkNull(SLVO.getPersontype()));

                                        ediTempBookingCustLstSL.add(ediTempBookingSLListCustVo);
                                    }
                                }

                                EcomBKGCommonModel ediTempBkgCustSLModelObj = new EcomBKGCommonModel();
                                ediTempBkgCustSLModelObj.setEditempbooking_cust(ediTempBookingCustLstSL);
                                siVDS.setecomeditempbookingcustomer(ediTempBkgCustSLModelObj);
                            }
                        }
                        ediTempBookingCustLstSL.clear();
                        if (custMap != null && custMap.containsKey("Buyer")) {
                            List<SISubmitVO> buyerList = (List<SISubmitVO>) custMap.get("Buyer");
                            if (buyerList != null && !buyerList.isEmpty()) {
                                for (SISubmitVO BLVO : buyerList) {
                                    if (BLVO.getCustname() != null && BLVO.getCustname().length() > 0) {
                                        EcomBKGCustomer ediTempBookingBYListCustVo = new EcomBKGCustomer();
                                        ediTempBookingBYListCustVo.setBook_no(bknum);
                                        ediTempBookingBYListCustVo.setCusttypeid("BY");
                                        ediTempBookingBYListCustVo.setCustid(checkNull(BLVO.getCustCode()));
                                        ediTempBookingBYListCustVo.setCountryid(extractCountryCode(checkNull(BLVO.getCountryid())));
                                        ediTempBookingBYListCustVo.setName(checkNull(BLVO.getCustname()));
                                        ediTempBookingBYListCustVo.setAddress1(checkNull(BLVO.getAddress()));
                                        ediTempBookingBYListCustVo.setAddress2(checkNull(BLVO.getAddress1()));
                                        ediTempBookingBYListCustVo.setCity(checkNull(BLVO.getCity()));
                                        ediTempBookingBYListCustVo.setZipCode(checkNull(BLVO.getZipCode()));
                                        ediTempBookingBYListCustVo.setTelephone(checkNull(BLVO.getTelephone()));
                                        ediTempBookingBYListCustVo.setFax(checkNull(BLVO.getFax()));
                                        ediTempBookingBYListCustVo.setEmail(checkNull(BLVO.getEmail()));
                                        ediTempBookingBYListCustVo.setStateid(checkNull(BLVO.getState()));
                                        ediTempBookingBYListCustVo.setEdireaderid(readerid);
                                        ediTempBookingBYListCustVo.setEori(checkNull(BLVO.getEori()));
                                        ediTempBookingBYListCustVo.setPersontype(checkNull(BLVO.getPersontype()));

                                        ediTempBookingCustLstSL.add(ediTempBookingBYListCustVo);
                                    }
                                }

                                EcomBKGCommonModel ediTempBkgCustSLModelObj = new EcomBKGCommonModel();
                                ediTempBkgCustSLModelObj.setEditempbooking_cust(ediTempBookingCustLstSL);
                                siVDS.setecomeditempbookingcustomer(ediTempBkgCustSLModelObj);
                            }
                        }

                        CompleteModel bkngCustModelObj = siVDS.getecombookingcustnesedonAPFW(bookno);
                        ArrayList ediTempBookingCustLstothers = null;
                        EcomBKGCustomer ediTempBookingothersCustVo = null;
                        for (Booking_cust bkngCutsObj : bkngCustModelObj.getBooking_cust()) {
                            ediTempBookingCustLstothers = new ArrayList();
                            ediTempBookingothersCustVo = new EcomBKGCustomer();

                            ediTempBookingothersCustVo.setBook_no(bknum);
                            ediTempBookingothersCustVo.setCusttypeid(bkngCutsObj.getCusttypeid());
                            ediTempBookingothersCustVo.setCustid(checkNull(bkngCutsObj.getCustid()));
                            ediTempBookingothersCustVo.setCountryid(checkNull(bkngCutsObj.getCountryid()));
                            ediTempBookingothersCustVo.setName(checkNull(bkngCutsObj.getName()));
                            ediTempBookingothersCustVo.setAddress1(checkNull(bkngCutsObj.getAddress1()));
                            ediTempBookingothersCustVo.setAddress2(checkNull(bkngCutsObj.getAddress2()));
                            ediTempBookingothersCustVo.setCity(checkNull(bkngCutsObj.getCity()));
                            ediTempBookingothersCustVo.setZipCode(checkNull(bkngCutsObj.getZipcode()));
                            ediTempBookingothersCustVo.setTelephone(checkNull(bkngCutsObj.getTelephone()));
                            ediTempBookingothersCustVo.setFax(checkNull(bkngCutsObj.getFax()));
                            ediTempBookingothersCustVo.setEmail(checkNull(bkngCutsObj.getEmail()));
                            ediTempBookingothersCustVo.setStateid(checkNull(bkngCutsObj.getStateid()));
                            ediTempBookingothersCustVo.setEdireaderid(readerid);
                            ediTempBookingothersCustVo.setEori(checkNull(bkngCutsObj.getEori()));
                            ediTempBookingothersCustVo.setPersontype(checkNull(bkngCutsObj.getPersontype()));
                            ediTempBookingCustLstothers.add(ediTempBookingothersCustVo);
                            EcomBKGCommonModel ediTempBkgCustOthersModelObj = new EcomBKGCommonModel();
                            ediTempBkgCustOthersModelObj.setEditempbooking_cust(ediTempBookingCustLstothers);
                            siVDS.setecomeditempbookingcustomer(ediTempBkgCustOthersModelObj);
                        }

                        CompleteModel bkngCargoModelOj = siVDS.getecombookingcargo(bookno);
                        for (Booking_cargo bkngCargoObj : bkngCargoModelOj.getBooking_cargo()) {
                            strSOC = bkngCargoObj.getShipperown();
                        }

                        alsize = 0;
                        cargoId = 0;
                        cargoNo = 0;
                        cntListSize = 0;
                        cmdIndex = 0;
                        cntno = "";
                        cntType = "";
                        seal1No = "";
                        seal2No = "";
                        commodity = "";
                        cmdId = "";
                        cmdDesc = "";
                        wt = "";
                        unit = "";
                        qty = "";
                        eqp_type = "";
                        int itemNo = 0;
                        if (cntMapObj != null && cntMapObj.size() > 0) {
                            alsize = cntMapObj.size();
                            Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = cntMapObj.entrySet();
                            for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                                SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                                SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                                SISealVO siSealVOObj = (SISealVO) siCNTValueVO.getSiSealObj();
                                cntno = "";
                                cntType = "";
                                seal1No = "";
                                seal2No = "";
                                cargoId = 0;
                                itemNo = 0;
                                cargoNo = cargoNo + 1;
                                eqp_type = "";
                                cntno = Validation.checkNull(siMasterVO.getStrCntNo());
                                cntType = Validation.checkNull(siMasterVO.getStrType());

                                seal2No = Validation.checkNull(siSealVOObj.getStrShipperSeal());
                                seal1No = Validation.checkNull(siSealVOObj.getStrCarrierSeal());
                                eqp_type = Validation.checkNull(siMasterVO.getStrEqpType());
                                // String cargoget = getCargDet(cntno, bookno);
                                // String cargogetSplit[] = cargoget.split("##");

                                String oog = Validation.checkNull(siMasterVO.getOog());
                                String ssoocc = Validation.checkNull(siMasterVO.getStrsoc());
                                String dgflag = Validation.checkNull(siMasterVO.getDgFlag());

                                double wee = 0.00;
                                double tot_wee = 0.00;
                                ArrayList cntList2 = (ArrayList) siCNTValueVO.getCntDeatilsObj();
                                if (cntList2 != null && cntList2.size() > 0) {
                                    cntListSize = 0;
                                    cntListSize = cntList2.size();
                                    for (int j = 0; j < cntListSize; j++) {
                                        SIContainerDetailVO siCntDetVo2 = (SIContainerDetailVO) cntList2.get(j);
                                        unit = "";
                                        wee = Double.parseDouble(siCntDetVo2.getStrWgt());
                                        unit = Validation.checkNull(siCntDetVo2.getStrUnit());
                                        tot_wee = tot_wee + wee;
                                    }
                                }
                                String tareweigth = Validation1.checkNullfloat(siMasterVO.getTareweight());
                                String cargoweight = Validation1.checkNullfloat(siMasterVO.getCargoweight());
                                String grossweigt = Validation1.checkNullfloat(siMasterVO.getGrossweight());

                                double cargowt = Double.parseDouble(cargoweight);
                                double tarewt = Double.parseDouble(tareweigth);

                                // double grweight = tot_wee + Double.parseDouble(tareweigth);
                                double grweight = Double.parseDouble(grossweigt);
                                System.out.println("[ECOM] EcomShippingInstructionBean ssoocc: " + ssoocc
                                        + "ssoocc.length() " + ssoocc.length() + ", tareweight: " + tarewt
                                        + ", cargoweight: " + cargowt + ", grossweight: " + grweight
                                        + ", oog: " + oog + ", dgflag: " + dgflag + ", readerid: " + readerid);
                                String tempmax = null;
                                String settemp = null;
                                String tempmin = null;
                                String tempunit = null;
                                if ("R".equalsIgnoreCase(checkNull(siMasterVO.getStrEqpType()))
                                        && !"Y".equalsIgnoreCase(siMasterVO.getNor())) {
                                    tempmax = Validation1.convertEmptytoNull(siMasterVO.getTempMax());
                                    settemp = Validation1.convertEmptytoNull(siMasterVO.getSetTemp());
                                    tempmin = Validation1.convertEmptytoNull(siMasterVO.getTempMin());
                                    tempunit = Validation.checkNull(siMasterVO.getTempunit());

                                    if (tempmax != null && tempmax.length() > 0) {
                                        double tempmx = Double.parseDouble(tempmax);
                                        tempmax = String.valueOf(tempmx);
                                    }
                                    if (settemp != null && settemp.length() > 0) {
                                        double sttemp = Double.parseDouble(settemp);
                                        settemp = String.valueOf(sttemp);
                                    }
                                    if (tempmin != null && tempmin.length() > 0) {
                                        double tmpmin = Double.parseDouble(tempmin);
                                        tempmin = String.valueOf(tmpmin);
                                    }

                                }
                                System.out.println("[ECOM] EcomShippingInstructionBean tempmax: " + tempmax
                                        + ", settemp: " + settemp + ", tempmin: " + tempmin
                                        + ", tempunit: " + tempunit + ", eqp_type: " + eqp_type + ", cntType: "
                                        + cntType + ", cntno: " + cntno + ", unit: " + unit);
                                rateeqptypeid = siMasterVO.getRateeqptypeid();

                                Booking_cargo cargoToInsert = new Booking_cargo();
                                ArrayList cargoToInsertList = new ArrayList();
                                cargoToInsert.setBook_no(bknum);
                                cargoToInsert.setType(checkNull(siMasterVO.getStrEqpType()));
                                cargoToInsert.setEqptypeid(checkNull(cntType));
                                cargoToInsert.setEdireaderid(readerid);
                                cargoToInsert.setEqpid(cntno);
                                cargoToInsert.setCargono(cargoNo);
                                cargoToInsert.setOog(checkNull(siMasterVO.getOog()));
                                cargoToInsert.setShipperown(checkNull(ssoocc));
                                cargoToInsert.setSeal1no(seal1No);
                                cargoToInsert.setSeal2no(seal2No);
                                cargoToInsert.setCargoweight(ConvertDecimalFormat(String.valueOf(cargowt)));
                                cargoToInsert.setGrossweight(ConvertDecimalFormat(String.valueOf(grweight)));
                                cargoToInsert.setWeightunit(checkNull(unit));
                                cargoToInsert.setTempmax(tempmax);
                                cargoToInsert.setTempmin(tempmin);
                                cargoToInsert.setTempunit(checkNull(tempunit));
                                cargoToInsert.setSettemp(settemp);
                                cargoToInsert.setNor(siMasterVO.getNor());

                                cargoToInsert.setLoadingcondition("FF");
                                if (cntno.contains(cntType + "TNB")) {
                                    cargoToInsert.setIsdummy("Y");
                                } else {
                                    cargoToInsert.setIsdummy("N");
                                }

                                cargoToInsert.setDgflag(checkNull(dgflag));
                                cargoToInsert.setEqpstatus(siMasterVO.getEqpStatus());
                                cargoToInsert.setOrderno("");
                                cargoToInsert.setTareweight(ConvertDecimalFormat(String.valueOf(tarewt)));

                                cargoToInsert.setHumidity(siMasterVO.getHumidity());
                                cargoToInsert.setHumidity_flag("1".equalsIgnoreCase(siMasterVO.getHumidity_flag())
                                        ? siMasterVO.getHumidity_flag()
                                        : "0");
                                cargoToInsert.setDrains(siMasterVO.getDrains());
                                cargoToInsert.setCo2value(siMasterVO.getCo2value());
                                cargoToInsert.setO2value(siMasterVO.getO2value());
                                cargoToInsert.setVentpercentage(siMasterVO.getVentilation());
                                cargoToInsert.setRate_eqptypeid(
                                        checkNull(cntType).equalsIgnoreCase(rateeqptypeid) ? "" : rateeqptypeid);
                                System.out.println("[ECOM] EcomShippingInstructionBean cntType==> " + cntType + " == "
                                        + rateeqptypeid + " ventvaluesibean " + siMasterVO.getVentilation());
                                String ventilation = siMasterVO.getVentilation();
                                if (checkNull(ventilation).length() > 0) {
                                    // if (ventilation != null && ventilation != "") {
                                    cargoToInsert.setVentsetting("1");
                                } else {
                                    cargoToInsert.setVentsetting("0");
                                }

                                cargoToInsertList.add(cargoToInsert);
                                EcomBKGCommonModel cargoToInsertModel = new EcomBKGCommonModel();
                                cargoToInsertModel.setEditempbooking_cargo(cargoToInsertList);
                                siVDS.setecomeditempbkgcargo(cargoToInsertModel);

                                ArrayList cntList = (ArrayList) siCNTValueVO.getCntDeatilsObj();
                                if (cntList != null && cntList.size() > 0) {
                                    cntListSize = 0;
                                    cntListSize = cntList.size();
                                    for (int j = 0; j < cntListSize; j++) {
                                        SIContainerDetailVO siCntDetVo = (SIContainerDetailVO) cntList.get(j);
                                        commodity = "";
                                        cmdId = "";
                                        cmdDesc = "";
                                        wt = "";
                                        unit = "";
                                        qty = "";
                                        cmdIndex = 0;
                                        packge = "";
                                        packgeId = "";
                                        itemNo = itemNo + 1;
                                        volume = "";
                                        volumeUnt = "";
                                        marks = "";
                                        description = "";
                                        pkgIndex = 0;
                                        netweight = "";
                                        wt = ConvertDecimalFormat(Validation.checkNull(siCntDetVo.getStrWgt()));
                                        unit = Validation.checkNull(siCntDetVo.getStrUnit());
                                        qty = Validation.checkNull(siCntDetVo.getStrQty());
                                        commodity = Validation.checkNull(siCntDetVo.getStrCommodity());
                                        packge = Validation.checkNull(siCntDetVo.getStrPkg());
                                        volume = Validation.checkNull(siCntDetVo.getStrVol());
                                        volumeUnt = Validation.checkNull(siCntDetVo.getStrVut());
                                        marks = Validation.checkNull(siCntDetVo.getStrMrk());
                                        description = Validation.checkNull(siCntDetVo.getStrDes());
                                        netweight = Validation.checkNull(siCntDetVo.getNettwt());
                                        bycode = Validation.checkNull(siCntDetVo.getByscode());
                                        byname = Validation.checkNull(siCntDetVo.getBysname());
                                        slcode = Validation.checkNull(siCntDetVo.getSlscode());
                                        slname = Validation.checkNull(siCntDetVo.getSlsname());
                                        cmdty_hblno = Validation.checkNull(siCntDetVo.getCmdty_hblno());

                                        if (packge.length() > 0) {
                                            pkgIndex = packge.lastIndexOf("~");
                                            packgeId = Validation
                                                    .checkNull(packge.substring((pkgIndex + 1), packge.length()));
                                        }
                                        if (commodity.length() > 0) {
                                            cmdIndex = commodity.lastIndexOf("~");
                                            cmdDesc = Validation.checkNull(commodity.substring(0, (cmdIndex - 1)));
                                            cmdId = Validation
                                                    .checkNull(commodity.substring((cmdIndex + 1), commodity.length()));
                                        }
                                        String hsComdtyCode = siCntDetVo.getHsCode();
                                        if ("Y".equalsIgnoreCase(siCntDetVo.getIsFakCommodity())) {
                                            hsComdtyCode = null;
                                            if (Validation.checkNull(siCntDetVo.getHsCode()).length() > 0) {
                                                hssb.append(cntno + " : " + siCntDetVo.getHsCode());
                                                hssb.append(" ");
                                            }
                                        }
                                        String itemleft = Validation1.checkNullfloat(siCntDetVo.getItemleft());
                                        String itemforward = Validation1.checkNullfloat(siCntDetVo.getItemforward());
                                        String itemtop = Validation1.checkNullfloat(siCntDetVo.getItemtop());
                                        String itemright = Validation1.checkNullfloat(siCntDetVo.getItemright());
                                        String itemaft = Validation1.checkNullfloat(siCntDetVo.getItemaft());
                                        String itemlengthunit = Validation.checkNull(siCntDetVo.getItemlengthUnit());
                                        System.out.println("[ECOM] EcomShippingInstructionBean get:::::"
                                                + siCntDetVo.getHazardous());
                                        String getHaz = checkNull(siCntDetVo.getHazardous());
                                        double itleft = Double.parseDouble(itemleft);
                                        double itforward = Double.parseDouble(itemforward);
                                        double ittop = Double.parseDouble(itemtop);
                                        double itright = Double.parseDouble(itemright);
                                        double itaft = Double.parseDouble(itemaft);
                                        String ediItemDesc = checkNull(siCntDetVo.getHsCodeDesc());
                                        ediItemDesc = checkNull(ediItemDesc.replace("(" + hsComdtyCode + ")", ""));
                                        if (ediItemDesc != null && ediItemDesc.toString().trim().length() > 255) {
                                            ediItemDesc = ediItemDesc.substring(0, 255);
                                            System.out.println(
                                                    "[ECOM] EcomShippingInstructionBean commval====>" + ediItemDesc);
                                        }
                                        Booking_commodity commodityToInsert = new Booking_commodity();
                                        ArrayList commodityToInsertList = new ArrayList();
                                        commodityToInsert.setBook_no(bknum);
                                        commodityToInsert.setCommodityid(hsComdtyCode);
                                        commodityToInsert.setEdireaderid(readerid);
                                        commodityToInsert.setVersion("0");
                                        commodityToInsert.setItemqty(qty);
                                        commodityToInsert.setItemweight(wt);
                                        commodityToInsert.setItemweightunit(unit);
                                        commodityToInsert.setHazardous(getHaz);
                                        if ("Y".equalsIgnoreCase(getHaz)) {
                                            commodityToInsert.setImocontact(siCntDetVo.getImocontact());
                                            commodityToInsert.setImophone(siCntDetVo.getImophone());
                                        }
                                        commodityToInsert.setEqpid(cntno);
                                        commodityToInsert.setAlcohol("N");
                                        commodityToInsert.setMilitarycargo("N");
                                        commodityToInsert.setCargono(cargoNo);
                                        commodityToInsert.setPackageid(packgeId);
                                        commodityToInsert.setItemvolume(volume);
                                        commodityToInsert.setItemvolumeunit(volumeUnt);
                                        commodityToInsert.setNettwt(netweight);
                                        if (checkNull(netweight).length() > 0) {
                                            commodityToInsert.setNetwtunit(unit);
                                        }
                                        commodityToInsert.setItemforward(Double.toString(itforward));
                                        commodityToInsert.setItemleft(Double.toString(itleft));
                                        commodityToInsert.setItemtop(Double.toString(ittop));
                                        commodityToInsert.setItemaft(Double.toString(itaft));
                                        commodityToInsert.setItemright(Double.toString(itright));
                                        commodityToInsert.setItemlengthunit(checkNull(itemlengthunit));
                                        commodityToInsert.setComdtygrp(cmdId);
                                        commodityToInsert.setItemno(itemNo);
                                        commodityToInsert.setMarksnumbers(marks.replace("\r", ""));
                                        commodityToInsert.setMarksdesc(description.replace("\r", ""));
                                        commodityToInsert.setCarriercode(client_code);
                                        commodityToInsert.setItemdesc(ediItemDesc);
                                        commodityToInsert.setHssuffixcode(siCntDetVo.getHssuffixcode());
                                        commodityToInsert.setBycode(bycode);
                                        commodityToInsert.setByname(byname);
                                        commodityToInsert.setSlcode(slcode);
                                        commodityToInsert.setSlname(slname);
                                        commodityToInsert.setCmdty_hblno(cmdty_hblno);

                                        commodityToInsertList.add(commodityToInsert);
                                        EcomBKGCommonModel commodityToInsertModel = new EcomBKGCommonModel();
                                        commodityToInsertModel.setEditempbooking_commodity(commodityToInsertList);
                                        siVDS.setecomeditempbookingcommodity(commodityToInsertModel);

                                    }
                                }
                                // }

                            }
                        }

                        List Chargelist = partiesVO.getChargeDetails();
                        if (Chargelist != null && Chargelist.size() > 0) {
                            List chargelis = new ArrayList();
                            ChargesVo chargevo = null;
                            EcomBookingCharges chvo = null;
                            for (int j = 0; j < Chargelist.size(); j++) {
                                chargevo = (ChargesVo) Chargelist.get(j);
                                chvo = new EcomBookingCharges();

                                chvo.setBook_no(bknum);
                                chvo.setChargeid(chargevo.getChargeid());
                                chvo.setCusttypeid(chargevo.getCusttypeid());
                                chvo.setPayagencyid(chargevo.getPayagencyid());
                                chvo.setPrepaidcollect(chargevo.getFprepaidcollect());
                                chvo.setEdireaderid(readerid);
                                chvo.setVersion("0");
                                chvo.setEqptypeid(chargevo.getEqptypeid());

                                chargelis.add(chvo);
                            }
                            EcomBKGCommonModel chargemodel = new EcomBKGCommonModel();
                            chargemodel.setEditempbooking_charges(chargelis);
                            siVDS.setecomeditempbookingcharges(chargemodel);
                        }

                        ENSDeclarantVo ensDec = partiesVO.getEnsdecvo();
                        if (ensDec != null && checkNull(ensDec.getEnsFillingType()).length() > 0) {
                            Editempbooking_ics_new ics = new Editempbooking_ics_new();
                            ArrayList icsToInsertList = new ArrayList();
                            ics.setBook_no(bknum);
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
                            siVDS.seteditempbookingicsnew(icsToInsertModel);
                        }
                        CompleteModel portModelObj = siVDS.getecomportcheck(partiesVO.getDelivery());
                        for (Ports portsVOobj : portModelObj.getPorts()) {
                            strDelivery = checkNull(portsVOobj.getPortcode());
                        }

                        EcomBKGRouting bkgRouting = new EcomBKGRouting();
                        ArrayList routingToInsertList = new ArrayList();
                        bkgRouting.setOrigin(originportid);
                        bkgRouting.setOriginmode("Y");
                        bkgRouting.setPol(partiesVO.getPol());
                        bkgRouting.setPolter(polTerminal);
                        bkgRouting.setPolser(serviceCode);
                        bkgRouting.setPolmode("N");
                        bkgRouting.setTrans1("");
                        bkgRouting.setTrans1ter("");
                        bkgRouting.setTrans1ser("");
                        bkgRouting.setTrans1mode("");
                        bkgRouting.setTrans2("");
                        bkgRouting.setTrans2ter("");
                        bkgRouting.setTrans2ser("");
                        bkgRouting.setTrans2mode("");
                        bkgRouting.setTrans3("");
                        bkgRouting.setTrans3ter("");
                        bkgRouting.setTrans3ser("");
                        bkgRouting.setTrans3mode("");
                        bkgRouting.setPod(partiesVO.getPod());
                        bkgRouting.setPodter(podTerminal);
                        bkgRouting.setPodmode("N");
                        bkgRouting.setDelivery(strDelivery);
                        bkgRouting.setSvccode(serviceCode);
                        bkgRouting.setBook_no(bknum);
                        bkgRouting.setEdireaderid(readerid);
                        bkgRouting.setDefrout("N");

                        routingToInsertList.add(bkgRouting);
                        EcomBKGCommonModel routingToInsertModel = new EcomBKGCommonModel();
                        routingToInsertModel.setEditempbooking_routing(routingToInsertList);
                        siVDS.setecomeditempbkgroutinginfo(routingToInsertModel);

                        EcomBKGCommonModel headerModel = new EcomBKGCommonModel();
                        List<EcomBKGHeader> headerList = new ArrayList();
                        EcomBKGHeader headerVO = new EcomBKGHeader();
                        headerVO.setBook_no(bknum);
                        headerVO.setPol(partiesVO.getPol_free_txt());
                        headerVO.setPod(partiesVO.getPod_free_txt());
                        headerVO.setOrigin(partiesVO.getPla_free_txt());
                        headerVO.setDelivery(partiesVO.getPld_free_txt());
                        headerVO.setPrecarriage(partiesVO.getVslvoyage_free_txt());
                        headerVO.setShipper(checkNull(partiesVO.getShipperHeader()));
                        headerVO.setConsignee(checkNull(partiesVO.getConsigneeHeader()));
                        headerVO.setNotify(checkNull(partiesVO.getNotifyHeader()));
                        System.out.println("[ECOM] EcomShippingInstructionBean >>>partiesVO.getNotifyHeader()>>>"
                                + partiesVO.getNotifyHeader());
                        headerVO.setShipperName(checkNull(partiesVO.getShipperName()));
                        headerVO.setConsigneeName(checkNull(partiesVO.getConsigneeName()));
                        headerVO.setNotifierName(checkNull(partiesVO.getNotifyName()));
                        headerVO.setEdireaderid(readerid);
                        headerVO.setVersion("0");
                        headerVO.setLoadVSL("");
                        headerList.add(headerVO);
                        headerModel.setEditempbooking_header(headerList);
                        siVDS.setecomeditempbookingheader(headerModel);

                        /**
                         * ps = ecomConn.prepareStatement(SAVE_EDIBKG_HEADER);
                         * ps.setString(1, bknum); ps.setString(2,
                         * partiesVO.getPol_free_txt()); ps.setString(3,
                         * partiesVO.getPod_free_txt()); ps.setString(4,
                         * partiesVO.getPla_free_txt()); ps.setString(5,
                         * partiesVO.getPld_free_txt()); ps.setString(6,
                         * partiesVO.getVslvoyage_free_txt()); ps.setString(7,
                         * readerid); ps.setString(8, "0"); ps.executeUpdate();*
                         */
                        //
                        Map configMap = cntrlBeanObj.getMasterConfigDetails();
                        EcomBKGEDIDocStatus docStatusVo = new EcomBKGEDIDocStatus();
                        ArrayList docStatusList = new ArrayList();
                        docStatusVo.setBook_no(bknum);
                        docStatusVo.setEdireaderid(readerid);
                        docStatusVo.setTranstype("9");
                        docStatusVo.setStatus("R");
                        docStatusVo.setEdiparty(configMap.get(GeneralConf.CARRIER_CODE) + GeneralConf.EDI_TYPE_SI);
                        docStatusVo.setReceivdate("");
                        docStatusVo.setMessage(GeneralConf.EDI_MESSAGETYPE_SI);
                        docStatusVo.setServprovname(configMap.get(GeneralConf.CARRIER_CODE) + GeneralConf.EDI_ECOM);
                        System.out.println(
                                "[ECOM] EcomShippingInstructionBean amendment_versionSI====>" + amendment_version);
                        docStatusVo.setAmendment_version(amendment_version);

                        docStatusList.add(docStatusVo);
                        EcomBKGCommonModel docStatusToInsertModel = new EcomBKGCommonModel();
                        docStatusToInsertModel.setEdidocstatus(docStatusList);
                        siVDS.setecomeditempdocstatus(docStatusToInsertModel);

                        /*
                         * Gson gson = new Gson();
                         * Map ediMap = bkgVDS.getecomediid(EcomConsts.ONLINE_SI);
                         * System.out.println(">>ediMap>>" + ediMap);
                         * List<EcomBKGReaderMaster> ediList = null;
                         * if (ediMap != null && ediMap.size() > 0) {
                         * System.out.println(">>>yes value is there>>>>>");
                         * ediList = gson.fromJson(gson.toJson((ArrayList) ediMap.get("edidocconfig")),
                         * new TypeToken<List<EcomBKGReaderMaster>>() {
                         * }.getType());
                         * if (ediList != null && ediList.size() > 0) {
                         * EcomBKGReaderMaster locEdi = ediList.get(0);
                         * ediid = locEdi.getEdiid();
                         * }
                         * }
                         */
                        ediid = EcomConsts.ONLINE_SI;
                        System.out.println("[ECOM] EcomShippingInstructionBean >>ediid>>>>" + ediid);
                        String corporateAgency = null;
                        List corpList = null;
                        Map corpAgency = bkgVDS.getEcomCorporateAgency(documentalAgencyid);
                        System.out.println("[ECOM] EcomShippingInstructionBean >>corpAgency>>" + corpAgency);
                        if (corpAgency != null && corpAgency.size() > 0) {
                            corpList = (List) corpAgency.get("agencymatrix");
                            if (corpList != null && corpList.size() > 0) {
                                corpAgency = (Map) corpList.get(0);
                                corporateAgency = checkNull((String) corpAgency.get("corpagency"));
                            }
                        }
                        System.out.println("[ECOM] EcomShippingInstructionBean >>corporateAgency>>" + corporateAgency);
                        ArrayList lst = null;
                        EcomBKGReaderMaster edirm = null;
                        EcomCommonModel cm = null;
                        if (ediid != null && ediid.length() > 0) {
                            lst = new ArrayList();
                            edirm = new EcomBKGReaderMaster();
                            cm = new EcomCommonModel();
                            edirm.setEdiid(ediid);
                            edirm.setEdireaderid(readerid);
                            edirm.setAgencycode(corporateAgency);
                            edirm.setLocationcode(documentalAgencyid);
                            edirm.setUploadstatus("Completed");
                            lst.add(edirm);
                            cm.setEdireadermaster(lst);
                            bkgVDS.setedireadermaster(cm);
                        }
                        EcomBKGRemarks bkgRemarksVO = new EcomBKGRemarks();
                        ArrayList bkgRemarksList = new ArrayList();
                        bkgRemarksVO.setBook_no(bknum);
                        bkgRemarksVO.setRemarksref("Submitted By");
                        bkgRemarksVO.setRemarksval(username1);
                        bkgRemarksVO.setEdireaderid(readerid);
                        bkgRemarksVO.setVersion("0");
                        bkgRemarksVO.setCategory(null);

                        bkgRemarksList.add(bkgRemarksVO);
                        EcomBKGCommonModel remarksToInsertModel = new EcomBKGCommonModel();
                        remarksToInsertModel.setEditempbooking_remarks(bkgRemarksList);
                        siVDS.setEdiTempBookingRemarks(remarksToInsertModel);

                        /**
                         * bkgRemarksVO = null; bkgRemarksVO = new
                         * EcomBKGRemarks(); bkgRemarksList = null;
                         * bkgRemarksList = new ArrayList();
                         * bkgRemarksVO.setBook_no(bknum);
                         * bkgRemarksVO.setRemarksref("Online Reference No");
                         * bkgRemarksVO.setRemarksval(bknum);
                         * bkgRemarksVO.setEdireaderid(readerid);
                         * bkgRemarksVO.setVersion("0");
                         * bkgRemarksVO.setCategory(null);
                         *
                         * bkgRemarksList.add(bkgRemarksVO);
                         * remarksToInsertModel = null; remarksToInsertModel =
                         * new EcomBKGCommonModel();
                         * remarksToInsertModel.setEditempbooking_remarks(bkgRemarksList);
                         * siVDS.setEdiTempBookingRemarks(remarksToInsertModel);*
                         */
                        ArrayList reflst = new ArrayList();
                        EcomBKGReference rf = new EcomBKGReference();
                        EcomCommonModel refcm = new EcomCommonModel();
                        rf.setBook_no(bknum);
                        rf.setRefid("ESI");
                        rf.setRefval(bknum);
                        rf.setIsedited("N");
                        rf.setEdireaderid(readerid);
                        rf.setVersion("0");
                        reflst.add(rf);
                        refcm.setEditempbooking_reference(reflst);
                        bkgVDS.seteditempbookingreference(refcm);

                        if (partiesVO.getAESNumber() != null && partiesVO.getAESNumber().length() > 0) {
                            reflst = new ArrayList();
                            rf = new EcomBKGReference();
                            refcm = new EcomCommonModel();
                            rf.setBook_no(bknum);
                            rf.setRefid("AES");
                            rf.setRefval(partiesVO.getAESNumber());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            reflst.add(rf);
                            refcm.setEditempbooking_reference(reflst);
                            bkgVDS.seteditempbookingreference(refcm);
                        }

                        if (partiesVO.getAESDiscliamer() != null && partiesVO.getAESDiscliamer().length() > 0) {
                            reflst = new ArrayList();
                            rf = new EcomBKGReference();
                            refcm = new EcomCommonModel();
                            rf.setBook_no(bknum);
                            rf.setRefid("AES");
                            rf.setRefval(partiesVO.getAESDiscliamer());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            reflst.add(rf);
                            refcm.setEditempbooking_reference(reflst);
                            bkgVDS.seteditempbookingreference(refcm);
                        }

                        if (partiesVO.getReleasetype() != null && partiesVO.getReleasetype().length() > 0) {
                            reflst = new ArrayList();
                            rf = new EcomBKGReference();
                            refcm = new EcomCommonModel();
                            rf.setBook_no(bknum);
                            rf.setRefid(EcomConsts.REF_TYPE);
                            rf.setRefval(partiesVO.getReleasetype());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            reflst.add(rf);
                            refcm.setEditempbooking_reference(reflst);
                            bkgVDS.seteditempbookingreference(refcm);
                        }
                        if (partiesVO.getDeclaredValue() != null && partiesVO.getDeclaredValue().length() > 0) {
                            reflst = new ArrayList();
                            rf = new EcomBKGReference();
                            refcm = new EcomCommonModel();
                            rf.setBook_no(bknum);
                            rf.setRefid(EcomConsts.Declared_Value);
                            rf.setRefval(partiesVO.getDeclaredValue());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            reflst.add(rf);
                            refcm.setEditempbooking_reference(reflst);
                            bkgVDS.seteditempbookingreference(refcm);
                        }
                        if (partiesVO.getACID() != null && partiesVO.getACID().length() > 0) {
                            reflst = new ArrayList();
                            rf = new EcomBKGReference();
                            refcm = new EcomCommonModel();
                            rf.setBook_no(bknum);
                            rf.setRefid(EcomConsts.ACID);
                            rf.setRefval(partiesVO.getACID());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            reflst.add(rf);
                            refcm.setEditempbooking_reference(reflst);
                            bkgVDS.seteditempbookingreference(refcm);
                        }
                        if (partiesVO.getFreight() != null && partiesVO.getFreight().length() > 0) {
                            reflst = new ArrayList();
                            rf = new EcomBKGReference();
                            refcm = new EcomCommonModel();
                            rf.setBook_no(bknum);
                            rf.setRefid(EcomConsts.Freight);
                            rf.setRefval(partiesVO.getFreight());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            reflst.add(rf);
                            refcm.setEditempbooking_reference(reflst);
                            bkgVDS.seteditempbookingreference(refcm);
                        }
                        if (partiesVO.getBltype() != null && partiesVO.getBltype().length() > 0) {
                            reflst = new ArrayList();
                            rf = new EcomBKGReference();
                            refcm = new EcomCommonModel();
                            rf.setBook_no(bknum);
                            rf.setRefid(EcomConsts.BLType);
                            rf.setRefval(partiesVO.getBltype());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            reflst.add(rf);
                            refcm.setEditempbooking_reference(reflst);
                            bkgVDS.seteditempbookingreference(refcm);
                        }
                        if (partiesVO.getPackinglst() != null && partiesVO.getPackinglst().length() > 0) {
                            reflst = new ArrayList();
                            rf = new EcomBKGReference();
                            refcm = new EcomCommonModel();
                            rf.setBook_no(bknum);
                            rf.setRefid(EcomConsts.packinglst);
                            rf.setRefval(partiesVO.getPackinglst());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            reflst.add(rf);
                            refcm.setEditempbooking_reference(reflst);
                            bkgVDS.seteditempbookingreference(refcm);
                        }
                        if (partiesVO.getInvoiceupload() != null && partiesVO.getInvoiceupload().length() > 0) {
                            reflst = new ArrayList();
                            rf = new EcomBKGReference();
                            refcm = new EcomCommonModel();
                            rf.setBook_no(bknum);
                            rf.setRefid(EcomConsts.invoiceupload);
                            rf.setRefval(partiesVO.getInvoiceupload());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            reflst.add(rf);
                            refcm.setEditempbooking_reference(reflst);
                            bkgVDS.seteditempbookingreference(refcm);
                        }
                        if (partiesVO.getOrgTelType() != null && partiesVO.getOrgTelType().length() > 0) {
                            reflst = new ArrayList();
                            rf = new EcomBKGReference();
                            refcm = new EcomCommonModel();
                            rf.setBook_no(bknum);
                            rf.setRefid(EcomConsts.orgTelType);
                            rf.setRefval(partiesVO.getOrgTelType());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            reflst.add(rf);
                            refcm.setEditempbooking_reference(reflst);
                            bkgVDS.seteditempbookingreference(refcm);
                        }
                        if (partiesVO.getBltype_UAE() != null && partiesVO.getBltype_UAE().length() > 0) {
                            reflst = new ArrayList();
                            rf = new EcomBKGReference();
                            refcm = new EcomCommonModel();
                            rf.setBook_no(bknum);
                            rf.setRefid(EcomConsts.bltype_UAE);
                            rf.setRefval(partiesVO.getBltype_UAE());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            reflst.add(rf);
                            refcm.setEditempbooking_reference(reflst);
                            bkgVDS.seteditempbookingreference(refcm);
                        }
                        if (partiesVO.getMpci_id() != null && partiesVO.getMpci_id().length() > 0) {
                            reflst = new ArrayList();
                            rf = new EcomBKGReference();
                            refcm = new EcomCommonModel();
                            rf.setBook_no(bknum);
                            rf.setRefid(EcomConsts.mpci_id);
                            rf.setRefval(partiesVO.getMpci_id());
                            rf.setIsedited("N");
                            rf.setEdireaderid(readerid);
                            rf.setVersion("0");
                            reflst.add(rf);
                            refcm.setEditempbooking_reference(reflst);
                            bkgVDS.seteditempbookingreference(refcm);
                        }
                        // insert hscode to remarks
                        String strHsCode = hssb.toString();
                        System.out.println("[ECOM] EcomShippingInstructionBean >>strHsCode>>>" + strHsCode);
                        if (strHsCode.length() > 0) {
                            EcomBKGRemarks bkgRemarksVOHS = new EcomBKGRemarks();
                            ArrayList bkgRemarksListHS = new ArrayList();
                            bkgRemarksVOHS.setBook_no(bknum);
                            bkgRemarksVOHS.setRemarksref("HS Code : ");
                            bkgRemarksVOHS.setRemarksval(strHsCode);
                            bkgRemarksVOHS.setEdireaderid(readerid);
                            bkgRemarksVOHS.setVersion("0");
                            bkgRemarksVOHS.setCategory(null);

                            bkgRemarksListHS.add(bkgRemarksVOHS);
                            EcomBKGCommonModel remarksToInsertModelHS = new EcomBKGCommonModel();
                            remarksToInsertModelHS.setEditempbooking_remarks(bkgRemarksListHS);
                            siVDS.setEdiTempBookingRemarks(remarksToInsertModelHS);
                        }

                        if (isconvertzip && checkNull(retFilePath).length() > 0) {
                            retFilePath = toRenameZipFile(bknum, readerid, saveFilePath, retFilePath);
                        }
                        /**
                         * Map ediMap =
                         * bkgVDS.getecomediid(EcomConsts.ONLINE_SI);
                         * System.out.println("[ECOM]
                         * EcomShippingInstructionBean >>ediMap>>" + ediMap);
                         * List<EcomBKGReaderMaster> ediList = null; if (ediMap
                         * != null && ediMap.size() > 0) {
                         * System.out.println("[ECOM]
                         * EcomShippingInstructionBean >>>yes value is
                         * there>>>>>"); ediList =
                         * gson.fromJson(gson.toJson((ArrayList)
                         * ediMap.get("edidocconfig")), new
                         * TypeToken<List<EcomBKGReaderMaster>>() {
                         * }.getType()); if (ediList != null && ediList.size() >
                         * 0) { EcomBKGReaderMaster locEdi = ediList.get(0);
                         * ediid = locEdi.getEdiid(); } }*
                         */
                        System.out.println("[ECOM] EcomShippingInstructionBean >>>>>edidi>>>ediid" + ediid);
                        EcomBKGReaderMaster ediReaderMasterVo = new EcomBKGReaderMaster();
                        ediReaderMasterVo.setEdiid(ediid);
                        ediReaderMasterVo.setEdireaderid(readerid);
                        ediReaderMasterVo.setDatecr("");
                        ediReaderMasterVo.setRequestdate("");
                        ediReaderMasterVo.setAgencycode(agencyid);
                        ediReaderMasterVo.setLocationcode(agencyid);
                        ediReaderMasterVo.setUploadstatus("Completed");
                        String strAgencyCode = documentalAgencyid;
                        System.out.println(
                                "[ECOM] EcomShippingInstructionBean ====strAgencyCode is to send mail=========>"
                                + strAgencyCode);
                        String strAgency = getAgencyMail(strAgencyCode);
                        if ((strAgency == null || "".equalsIgnoreCase(strAgency.trim()))
                                && "BAKOFF".equalsIgnoreCase(strAgencyCode)) {
                            strAgency = getBackOfficeMailId();
                        }
                        returnVO.setAdmToMailId(strAgency);
                        System.out.println(
                                "[ECOM] EcomShippingInstructionBean ====strAgencyMail is to send mail=========>"
                                + strAgency);
                        returnVO.setReaderid(readerid);
                    }
                }
            }
            // agencyid
            MailFormatVO mailFormatObj = new MailFormatVO();
            if (custCountry != null && "CN".equalsIgnoreCase(custCountry)) {
                mailFormatObj = getMailContentLang("SI", "submitmail", "C", "ch");
            } else {
                mailFormatObj = getMailContent("SI", "submitmail", "C");
            }

            strReturn = mailFormatObj.getStrMailContent();
            strReturn = strReturn.replaceAll("@si_no", bknum);
            strReturn = strReturn.replaceAll("@book_no", bookno);
            // strReturn = strReturn.replaceAll("@customer", agencyVo.getAgencyname());
            returnVO.setStrMailMsg(strReturn);
            returnVO.setActivityCode(mailFormatObj.getActivityCode());
            returnVO.setStrSubject(mailFormatObj.getStrSubject()
                    + "-(" + bknum + ")");
            returnVO.setStrFromMailId(mailFormatObj.getStrFromAddr());
            if (returnVO.getAdmToMailId() != null && returnVO.getAdmToMailId().length() > 0) {
                MailFormatVO mailFormatObj2 = new MailFormatVO();
                mailFormatObj2 = getMailContent("SI", "Update", "C");
                String msg2 = mailFormatObj2.getStrMailContent();
                msg2 = msg2.replaceAll("@SINO", bknum);
                // msg2 = msg2.replaceAll("@customer", "Agency");
                msg2 = msg2.replaceAll("@client", mailFormatObj2.getStrRegard());
                msg2 = msg2.replaceAll("@bookno", bookno);
                returnVO.setAdmMailMsg(msg2);
                String subject1 = mailFormatObj2.getStrSubject();
                subject1 = subject1.replace("@sirefno", bknum);
                returnVO.setAdmSubject(subject1);
                returnVO.setAdmFromMailId(mailFormatObj2.getStrFromAddr());
            }
            if ("en".equalsIgnoreCase(lang)) {

                strReturn = checkNull(getForwardMessage("SI", msgtype, "c"));
                strReturn = strReturn.replaceAll("@SINO", bknum);
                returnVO.setStrDisplayMsg(strReturn);
                returnVO.setStrRefNo(bknum);
                returnVO.setFilenames(retFilePath);
            } else if ("ch".equalsIgnoreCase(lang)) {
                strReturn = checkNull(getForwardMessageLang("SI", msgtype, "c", "ch"));
                strReturn = strReturn.replaceAll("@SINO", bknum);
                returnVO.setStrDisplayMsg(strReturn);
                returnVO.setStrRefNo(bknum);
                returnVO.setFilenames(retFilePath);
            } else if ("ma".equalsIgnoreCase(lang)) {
                strReturn = checkNull(getForwardMessageLang("SI", msgtype, "c", "ma"));
                strReturn = strReturn.replaceAll("@SINO", bknum);
                returnVO.setStrDisplayMsg(strReturn);
                returnVO.setStrRefNo(bknum);
                returnVO.setFilenames(retFilePath);
            } else if ("es".equalsIgnoreCase(lang)) {
                strReturn = checkNull(getForwardMessageLang("SI", msgtype, "c", "es"));
                strReturn = strReturn.replaceAll("@SINO", bknum);
                returnVO.setStrDisplayMsg(strReturn);
                returnVO.setStrRefNo(bknum);
                returnVO.setFilenames(retFilePath);
            }
            if ("S".equalsIgnoreCase(savetype)) {
                SaveToLog(username1, "SI", "Submit", "New Shipping Instruction Submitted", bookno, custcode);
            }
            if ("DU".equalsIgnoreCase(siSAVETYPE) && !"S".equalsIgnoreCase(savetype)) {
                SaveToLog(username1, "SI", "Update", "New Shipping Instruction Updated", bookno, custcode);
            }
            if ("DI".equalsIgnoreCase(siSAVETYPE) && !"S".equalsIgnoreCase(savetype)) {
                SaveToLog(username1, "SI", "Insert", "New Shipping Instruction Saved as Draft", bookno, custcode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            returnVO.setStrRefNo("");
            returnVO.setStrDisplayMsg("Shipping Instruction not saved.");
            System.out.println("[ECOM] EcomShippingInstructionBean context==> " + context);
            context.setRollbackOnly();
        } finally {
            try {
                if (rs1 != null) {
                    rs1.close();
                }
                if (ps1 != null) {
                    ps1.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (ecomConn != null) {
                    ecomConn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return returnVO;
    }

    private String getAgencyMail(String agencycode) {
        int i = 0;
        String resultEmail = "", mail = "";
        Map mailMap = null;
        try {
            Map hs = (Map) siVDS.getEcomSIAgencyMail(agencycode);
            if (hs != null) {
                List lst = (List) hs.get("agency");
                if (lst != null && lst.size() > 0) {
                    for (int j = 0; j < lst.size(); j++) {
                        mailMap = (Map) lst.get(j);
                        mail = checkNull((String) mailMap.get("contactemailid"));
                        if (i == 0) {
                            resultEmail = mail;
                        } else {
                            resultEmail = resultEmail + "," + mail;
                        }
                        i++;
                    }
                }
            }
            System.out.println("[ECOM] EcomShippingInstructionBean >>si agency mail>>" + resultEmail);
        } catch (Exception e) {
            System.out.println("[ECOM] EcomShippingInstructionBean ******==>" + e);
        }
        return resultEmail;

    }

    public void SaveToLog(String username, String module, String activity, String remarks, String referenceno,
            String compcode) {

        Connection logcon = null;
        PreparedStatement logpstmt = null;

        try {
            String strLogSQL = "insert into EcommuserlogDetails(username,module,activity,remarks,referenceno,Logdate,customercode)values('"
                    + username + "','" + module + "','" + activity + "','" + remarks + "','" + referenceno
                    + "',GETDATE(),'" + compcode + "')";
            logcon = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            logpstmt = logcon.prepareStatement(strLogSQL);
            logpstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {

                if (logpstmt != null) {
                    logpstmt.close();

                }
                if (logcon != null) {
                    logcon.close();

                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    private String getForwardMessage(String module, String criteria, String usertype) {

        // System.out.println("[ECOM] EcomShippingInstructionBean
        // *****getForwardMessage*****");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        String Forward_query = "select message from ecom_messageformat(nolock) where Status='a' and Type ='msg' "
                + "and criteria = '" + criteria + "'"
                + " and Module ='" + module + "' and usertype='" + usertype + "'";

        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(Forward_query, 1004, 1007);
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

    private String getForwardMessageLang(String module, String criteria, String usertype, String lang) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        String Forward_lang_query = "select message from ecom_messageformatlang(nolock) where Status='a' and Type ='msg' "
                + "and criteria = '" + criteria + "'"
                + " and Module ='" + module + "' and usertype='" + usertype + "'and lang='" + lang + "'";

        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(Forward_lang_query, 1004, 1007);
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

    public MailFormatVO getMailContent(String module, String criteria, String usertype) {
        // tln("*********getMailContent********");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MailFormatVO mailFormatObj = new MailFormatVO();
        String query = "select message ,from_mailid,subject,activitycode from ecom_messageformat(nolock) where Status='a' and Type ='mail' and criteria = ? and Module =? and usertype = ?";

        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(query, 1004, 1007);
            ps.setString(1, criteria);
            ps.setString(2, module);
            ps.setString(3, usertype);
            rs = ps.executeQuery();

            while (rs.next()) {
                mailFormatObj.setStrMailContent(checkNull(rs.getString("message")));
                mailFormatObj.setStrRegard("");
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

    @Override
    public Map<SIMasterVO, SIConatainerValueVO> fetchContainerDetailForSI(String bookno) throws RemoteException {
        Map<SIMasterVO, SIConatainerValueVO> siCntHashObj = new LinkedHashMap<SIMasterVO, SIConatainerValueVO>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        String weight = null, volume = null, netweight = null;

        SIMasterVO siCntMasterVoObj = new SIMasterVO();
        SIConatainerValueVO siCntValVO = new SIConatainerValueVO();
        SISealVO siSealVO = new SISealVO();
        ArrayList<SIContainerDetailVO> cntDetailObj = new ArrayList<SIContainerDetailVO>();
        SIContainerDetailVO siCntDetailsVoObj = new SIContainerDetailVO();

        int i = 0;
        String strCntNo = "";
        System.out.println("[ECOM] EcomShippingInstructionBean  book no " + bookno);
        String soc = "";
        String prevCntr = null, currentCntr = null;
        int contDup = 0;
        boolean dupCont = false;
        String strQtn = null;
        DecimalFormat df = new DecimalFormat("#.###");
        Map insmap = null;
        String qouteno = null;
        try {
            insmap = getInsuranceDetils(bookno);
            CompleteModel siModelCargoObj = siVDS.getecombookingcargoforsi(bookno);
            System.out.println("[ECOM] EcomShippingInstructionBean >>>inner join cargo >>>" + siModelCargoObj);
            for (Booking_cargo bkngCargoObj : siModelCargoObj.getBooking_cargo()) {
                if (contDup == 0) {
                    prevCntr = Validation.checkNull(bkngCargoObj.getEqpid());
                    currentCntr = Validation.checkNull(bkngCargoObj.getEqpid());
                }
                currentCntr = Validation.checkNull(bkngCargoObj.getEqpid());
                dupCont = prevCntr.equalsIgnoreCase(currentCntr) ? false : true;
                if (contDup == 0 || dupCont) {
                    System.out.println("[ECOM] EcomShippingInstructionBean >>>enter count" + contDup);
                    prevCntr = currentCntr;
                    siCntMasterVoObj = new SIMasterVO();
                    siSealVO = new SISealVO();
                    siCntValVO = new SIConatainerValueVO();
                    strCntNo = Validation.checkNull(bkngCargoObj.getEqpid());
                    siCntMasterVoObj.setStrType(bkngCargoObj.getEqptypeid());
                    siCntMasterVoObj.setStrEqpType(bkngCargoObj.getType());
                    siCntMasterVoObj.setRateeqptypeid(bkngCargoObj.getRate_eqptypeid());
                    siCntMasterVoObj.setStrCntNo(strCntNo);
                    soc = Validation.checkNull(bkngCargoObj.getShipperown());
                    if (soc != null && "Y".equalsIgnoreCase(soc)) {
                        siCntMasterVoObj.setStrsoc("Y");
                    } else {
                        siCntMasterVoObj.setStrsoc("N");
                    }
                    siCntMasterVoObj.setCargoNo(bkngCargoObj.getCargono() + "");
                    siCntMasterVoObj.setCargoweight(
                            ConvertDecimalFormat(Validation1.checkNullfloat(bkngCargoObj.getCargoweight())));
                    System.out.println("[ECOM] EcomShippingInstructionBean >>>bkngCargoObj.getCargoweight()>>>"
                            + bkngCargoObj.getCargoweight());
                    siCntMasterVoObj.setTareweight(
                            ConvertDecimalFormat(Validation1.checkNullfloat(bkngCargoObj.getTareweight())));
                    siCntMasterVoObj.setGrossweight(
                            ConvertDecimalFormat(Validation1.checkNullfloat(bkngCargoObj.getGrossweight())));
                    siCntMasterVoObj.setWeightUnit(Validation1.checkNullfloat(bkngCargoObj.getWeightunit()));
                    siCntMasterVoObj.setTempMin(Validation.checkNull(bkngCargoObj.getTempmin()));
                    siCntMasterVoObj.setTempMax(Validation.checkNull(bkngCargoObj.getTempmax()));
                    siCntMasterVoObj.setSetTemp(Validation.checkNull(bkngCargoObj.getSettemp()));
                    siCntMasterVoObj.setEqpStatus(checkNull(bkngCargoObj.getEqpstatus()));
                    siCntMasterVoObj.setTempunit(checkNull(bkngCargoObj.getTempunit()));
                    siCntMasterVoObj.setDgFlag(checkNull(bkngCargoObj.getDgflag()));

                    siCntMasterVoObj.setHumidity(checkNull(bkngCargoObj.getHumidity()));
                    siCntMasterVoObj.setHumidity_flag(checkNull(bkngCargoObj.getHumidity_flag()));
                    siCntMasterVoObj.setDrains(checkNull(bkngCargoObj.getDrains()));
                    siCntMasterVoObj.setCo2value(checkNull(bkngCargoObj.getCo2value()));
                    siCntMasterVoObj.setO2value(checkNull(bkngCargoObj.getO2value()));
                    siCntMasterVoObj.setVentilation(checkNull(bkngCargoObj.getVentpercentage()));
                    System.out.println("[ECOM] EcomShippingInstructionBean >>>bkngCargoObj.getDgflag()>>>"
                            + bkngCargoObj.getDgflag());

                    siCntMasterVoObj.setOog(bkngCargoObj.getOog());
                    siCntMasterVoObj.setNor(bkngCargoObj.getNor());
                    if (siCntMasterVoObj.getSetTemp() != null && siCntMasterVoObj.getSetTemp().length() > 0
                            && checkNull(bkngCargoObj.getTempunit()).length() > 0) {
                        siCntMasterVoObj.setRefer("Y");
                    } else {
                        siCntMasterVoObj.setRefer("N");
                    }
                    //
                    siCntMasterVoObj.setStrSelect(i);
                    siSealVO.setStrShipperSeal(Validation.checkNull(bkngCargoObj.getSeal2no()));
                    siSealVO.setStrCarrierSeal(Validation.checkNull(bkngCargoObj.getSeal1no()));
                    siSealVO.setStrIsSelect("NO");
                    cntDetailObj = new ArrayList<SIContainerDetailVO>();
                }

                siCntDetailsVoObj = new SIContainerDetailVO();
                siCntDetailsVoObj.setHsCode(Validation.checkNull(bkngCargoObj.getCommodityid()));
                siCntDetailsVoObj.setStrCommodity(Validation.checkNull(bkngCargoObj.getComdtygrp()));
                strQtn = Validation.checkNull(bkngCargoObj.getItemqty());
                strQtn = Validation.checkNull(bkngCargoObj.getItemqty()).length() > 0 ? strQtn : "0";
                if (Integer.parseInt(strQtn) > 0) {
                    siCntDetailsVoObj.setStrQty(strQtn);
                } else {
                    siCntDetailsVoObj.setStrQty("");
                }

                String wightcargo = checkNull(bkngCargoObj.getItemweight());
                wightcargo = wightcargo.length() > 0 ? wightcargo : "0";
                Double wtn = Double.parseDouble(wightcargo);
                String wightcargowt = df.format(wtn);
                siCntDetailsVoObj.setStrWgt(ConvertDecimalFormat(wightcargowt));
                siCntDetailsVoObj.setStrUnit(Validation.checkNull(bkngCargoObj.getItemweightunit()));
                siCntDetailsVoObj.setStrPkg(Validation.checkNull(bkngCargoObj.getPackageid()));
                volume = checkNull(bkngCargoObj.getItemvolume()).length() > 0 ? checkNull(bkngCargoObj.getItemvolume())
                        : "0";
                volume = String.valueOf(new BigDecimal(volume).toPlainString());
                System.out.println("[ECOM] EcomShippingInstructionBean >>>volume>>" + volume
                        + " Float.parseFloat(volume)>> " + Float.parseFloat(volume));
                String volumeDouble = df.format(Float.parseFloat(volume));
                if (Float.parseFloat(volume) <= 0) {
                    volumeDouble = "";
                }
                siCntDetailsVoObj.setStrVol(volumeDouble);
                siCntDetailsVoObj.setStrVut(Validation.checkNull(bkngCargoObj.getItemvolumeunit()));
                siCntDetailsVoObj.setStrMrk(Validation.checkNull(bkngCargoObj.getMarksnumbers()));
                siCntDetailsVoObj.setStrDes(Validation.checkNull(bkngCargoObj.getMarksdesc()));
                // netweight
                netweight = bkngCargoObj.getNettwt() + "";
                netweight = checkNull(String.valueOf(bkngCargoObj.getNettwt())).length() > 0
                        ? checkNull(String.valueOf(bkngCargoObj.getNettwt()))
                        : "0";
                netweight = String.valueOf(new BigDecimal(netweight).toPlainString());
                System.out.println("[ECOM] EcomShippingInstructionBean >>>netweight>>" + netweight
                        + " Float.parseFloat(netweight)>>" + Float.parseFloat(netweight));
                String netWeightDouble = df.format(Float.parseFloat(netweight));
                if (Float.parseFloat(netweight) <= 0) {
                    netWeightDouble = "";
                }

                siCntDetailsVoObj.setNettwt(netWeightDouble);
                siCntDetailsVoObj.setByscode(bkngCargoObj.getBycode());
                siCntDetailsVoObj.setBysname(bkngCargoObj.getByname());
                siCntDetailsVoObj.setSlscode(bkngCargoObj.getSlcode());
                siCntDetailsVoObj.setSlsname(bkngCargoObj.getSlname());
                siCntDetailsVoObj.setCmdty_hblno(bkngCargoObj.getCmdty_hblno());
                System.out.println("hbl=>" + bkngCargoObj.getCmdty_hblno() + "//" + bkngCargoObj.getSlname() + "//" + bkngCargoObj.getSlcode());
                //
                siCntDetailsVoObj.setImophone(bkngCargoObj.getImophone());
                siCntDetailsVoObj.setImocontact(bkngCargoObj.getImocontact());
                siCntDetailsVoObj.setItemforward((bkngCargoObj.getItemforward()));
                siCntDetailsVoObj.setItemtop((bkngCargoObj.getItemtop()));
                siCntDetailsVoObj.setItemright((bkngCargoObj.getItemright()));
                siCntDetailsVoObj.setItemaft((bkngCargoObj.getItemaft()));
                siCntDetailsVoObj.setHazardous(bkngCargoObj.getHazardous());
                siCntDetailsVoObj.setItemlengthUnit(bkngCargoObj.getItemlengthunit());
                siCntDetailsVoObj.setItemleft((bkngCargoObj.getItemleft()));
                siCntDetailsVoObj.setHssuffixcode(bkngCargoObj.getHssuffixcode());
                //
                // String strPattern = "^0+";
                System.out.println("[ECOM] EcomShippingInstructionBean ---->" + siCntDetailsVoObj.getStrCommodity()
                        + "---->" + (insmap.containsKey((siCntDetailsVoObj.getStrCommodity()))));
                if (insmap != null) {
                    siCntDetailsVoObj.setInsuredemail(checkNull((String) insmap.get("insuredemail")));
                    if ((insmap.containsKey((siCntDetailsVoObj.getStrCommodity()))
                            || insmap.containsKey((siCntDetailsVoObj.getHsCode())))) {// .replaceAll(strPattern, "")
                        qouteno = checkNull((String) insmap.get((siCntDetailsVoObj.getStrCommodity())));
                        if (qouteno.length() == 0) {
                            qouteno = checkNull((String) insmap.get((siCntDetailsVoObj.getHsCode())));
                        }
                        System.out.println("[ECOM] EcomShippingInstructionBean qoute==> " + qouteno);
                        siCntDetailsVoObj.setIsinsuranceavailable("Y");
                        siCntDetailsVoObj.setQuoteno(qouteno);
                    }
                }
                //
                cntDetailObj.add(siCntDetailsVoObj);
                if (contDup == 0 || dupCont) {
                    siCntValVO.setSiSealObj(siSealVO);
                    siCntValVO.setCntDeatilsObj(cntDetailObj);
                    siCntHashObj.put(siCntMasterVoObj, siCntValVO);
                    i++;
                }
                contDup++;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            try {
                if (rs1 != null) {
                    rs1.close();

                }
                if (ps1 != null) {
                    ps1.close();

                }

                if (rs != null) {
                    rs.close();

                }
                if (ps != null) {
                    ps.close();

                }

                if (con != null) {
                    con.close();

                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }
        return siCntHashObj;

    }

    @Override
    public Map<SIMasterVO, SIConatainerValueVO> fetchSIContainerDetail(String bookno, String sino)
            throws RemoteException {

        Map siCntHashObj = new LinkedHashMap();
        Connection con_cargo = null;
        PreparedStatement ps_cargo = null;
        ResultSet rs_cargo = null;

        PreparedStatement ps_cargo1 = null;
        ResultSet rs_cargo1 = null;

        PreparedStatement ps_com = null;
        ResultSet rs_com = null;

        PreparedStatement ps_dgcom = null;
        ResultSet rs_dg = null;

        PreparedStatement ps_oogcom = null;
        ResultSet rs_oog = null;

        String weight = null, volume = null, netweight = null;

        SIMasterVO siCntMasterVoObj = new SIMasterVO();
        SIConatainerValueVO siCntValVO = new SIConatainerValueVO();
        SISealVO siSealVO = new SISealVO();
        ArrayList<SIContainerDetailVO> cntDetailObj = new ArrayList<SIContainerDetailVO>();
        SIContainerDetailVO siCntDetailsVoObj = new SIContainerDetailVO();

        int i = 0;
        String strCntNo = "";
        String conId = "", comconId = "";
        String soc = "";
        String eqpid = "";
        DecimalFormat df = new DecimalFormat("#.###");
        Map insmap = null;
        String qouteno = null;
        try {
            insmap = getInsuranceDetils(bookno);
            con_cargo = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps_cargo = con_cargo.prepareStatement(GET_ECOMSI_CARGO_DET);
            ps_cargo.setString(1, sino);
            rs_cargo = ps_cargo.executeQuery();

            while (rs_cargo.next()) {
                siCntMasterVoObj = new SIMasterVO();
                siSealVO = new SISealVO();
                siCntValVO = new SIConatainerValueVO();
                strCntNo = checkNull(rs_cargo.getString("containerno"));
                siCntMasterVoObj.setStrType(checkNull(rs_cargo.getString("eqpSize")));
                siCntMasterVoObj.setStrEqpType(checkNull(rs_cargo.getString("eqpType")));
                siCntMasterVoObj.setRateeqptypeid(rs_cargo.getString("rate_eqpTypeid"));
                conId = checkNull(rs_cargo.getString("conId"));
                siCntMasterVoObj.setStrCntNo(strCntNo);
                siCntMasterVoObj.setStrSelect(i);
                siSealVO.setStrShipperSeal(checkNull(rs_cargo.getString("ShipperSeal")));
                siSealVO.setStrCarrierSeal(checkNull(rs_cargo.getString("carrierSeal")));
                soc = checkNull(rs_cargo.getString("shipperOwn"));
                if (soc != null && "Y".equalsIgnoreCase(soc)) {
                    siCntMasterVoObj.setStrsoc("Y");
                } else {
                    siCntMasterVoObj.setStrsoc("N");
                }
                // cargoweight,tareweight,grossweight
                siCntMasterVoObj.setTareweight(ConvertDecimalFormat(rs_cargo.getString("tareWeight")));
                siCntMasterVoObj.setEqpStatus(rs_cargo.getString("eqpstatus"));
                siCntMasterVoObj.setCargoweight(ConvertDecimalFormat(rs_cargo.getString("cargoWeight")));
                siCntMasterVoObj.setGrossweight(ConvertDecimalFormat(rs_cargo.getString("grossweight")));
                siCntMasterVoObj.setDgFlag(rs_cargo.getString("dgflag"));
                siCntMasterVoObj.setOog("N");
                siCntMasterVoObj.setNor(rs_cargo.getString("ref_nor"));
                eqpid = rs_cargo.getString("containerno");
                siSealVO.setStrIsSelect("YES");
                siSealVO.setStrCargoid(conId);
                siCntMasterVoObj.setCargoNo(strCntNo);
                System.out.println("[ECOM] EcomShippingInstructionBean sinumber eqpid:::::" + sino + ":::::" + eqpid);
                ps_cargo1 = con_cargo.prepareStatement(GET_ECOMSI_REF_DET);
                ps_cargo1.setString(1, checkNull(sino));
                ps_cargo1.setString(2, checkNull(eqpid));
                rs_cargo1 = ps_cargo1.executeQuery();
                while (rs_cargo1.next()) {
                    siCntMasterVoObj.setTempMin(rs_cargo1.getString("ref_minTemp"));
                    siCntMasterVoObj.setTempMax(rs_cargo1.getString("ref_maxTemp"));
                    siCntMasterVoObj.setSetTemp(rs_cargo1.getString("ref_setTemp"));
                    siCntMasterVoObj.setTempunit(rs_cargo1.getString("ref_tempUnit"));
                    siCntMasterVoObj.setHumidity(checkNull(rs_cargo1.getString("humidity")));
                    siCntMasterVoObj.setHumidity_flag(checkNull(rs_cargo1.getString("humidity_flag")));
                    siCntMasterVoObj.setDrains(checkNull(rs_cargo1.getString("drains")));
                    siCntMasterVoObj.setCo2value(checkNull(rs_cargo1.getString("co2value")));
                    siCntMasterVoObj.setO2value(checkNull(rs_cargo1.getString("o2value")));
                    siCntMasterVoObj.setVentilation(checkNull(rs_cargo1.getString("ventpercentage")));
                    siCntMasterVoObj.setRefer("Y");
                }
                //
                int j = 0;
                cntDetailObj = new ArrayList<SIContainerDetailVO>();

                if (Integer.parseInt(conId) > 0) {
                    comconId = "";
                    ps_com = con_cargo.prepareStatement(GET_ECOMSI_CMDTY_DET);
                    ps_com.setString(1, sino);
                    ps_com.setString(2, eqpid);
                    rs_com = ps_com.executeQuery();

                    while (rs_com.next()) {
                        siCntDetailsVoObj = new SIContainerDetailVO();
                        siCntDetailsVoObj.setHazardous(rs_com.getString("hazardous"));
                        System.out.println("[ECOM] EcomShippingInstructionBean ===commodity is===>"
                                + rs_com.getString("commodity"));
                        siCntDetailsVoObj.setStrCommodity(checkNull(rs_com.getString("commodity")));
                        siCntDetailsVoObj.setHsCode(checkNull(rs_com.getString("hscommodity")));
                        siCntDetailsVoObj.setStrQty(checkNull(rs_com.getString("packageQty")));
                        String wightcargo = Validation1.checkNullfloat(rs_com.getString("itemweight"));
                        String eqpstatus = rs_cargo.getString("eqpstatus");
                        Double wtn = Double.parseDouble(wightcargo);
                        String wightcargowt = null;
                        if (wtn > 0) {
                            wightcargowt = df.format(wtn);
                        } else {
                            if (!"M".equalsIgnoreCase(eqpstatus)) {
                                wightcargowt = "";
                            } else if ("M".equalsIgnoreCase(eqpstatus)) {
                                wightcargowt = df.format(wtn);
                            }
                        }
                        siCntDetailsVoObj.setStrWgt(ConvertDecimalFormat(wightcargowt));
                        siCntDetailsVoObj.setStrUnit(checkNull(rs_com.getString("itemWeightUnit")));
                        siCntDetailsVoObj.setStrCid("");
                        siCntDetailsVoObj.setStrPkg(checkNull(rs_com.getString("package")));
                        siCntDetailsVoObj.setHssuffixcode(rs_com.getString("hssuffixcode"));
                        volume = rs_com.getString("itemVolume") != null && !rs_com.getString("itemVolume").isEmpty()
                                ? rs_com.getString("itemVolume")
                                : "0.0";
                        String volumeDouble = "";
                        if (Float.parseFloat(volume) > 0) {
                            volume = String.valueOf(new BigDecimal(volume).toPlainString());
                            volumeDouble = df.format(Float.parseFloat(volume));
                        } else {
                            volume = "";
                        }

                        siCntDetailsVoObj.setStrVol(volumeDouble);
                        siCntDetailsVoObj.setStrVut(checkNull(rs_com.getString("itemVolumeUnit")));
                        siCntDetailsVoObj.setStrMrk(checkNull(rs_com.getString("marksNo")));
                        siCntDetailsVoObj.setStrDes(checkNull(rs_com.getString("marksDesc")));
                        netweight = rs_com.getString("nettwt") != null && !rs_com.getString("nettwt").isEmpty()
                                ? rs_com.getString("nettwt")
                                : "0.0";
                        siCntDetailsVoObj
                                .setNettwt(checkNull(String.valueOf(new BigDecimal(netweight).toPlainString())));
                        System.out.println("[ECOM] EcomShippingInstructionBean sinetweightvalue" + netweight);
                        siCntDetailsVoObj.setByscode(checkNull(rs_com.getString("bycode")));
                        siCntDetailsVoObj.setBysname(checkNull(rs_com.getString("byname")));
                        siCntDetailsVoObj.setSlscode(checkNull(rs_com.getString("slcode")));
                        siCntDetailsVoObj.setSlsname(checkNull(rs_com.getString("slname")));
                        siCntDetailsVoObj.setCmdty_hblno(checkNull(rs_com.getString("cmdty_hblno")));

                        comconId = checkNull(rs_com.getString("itemno"));

                        // String strPattern = "^0+";
                        System.out.println(
                                "[ECOM] EcomShippingInstructionBean ---->" + siCntDetailsVoObj.getStrCommodity()
                                + "---->" + (insmap.containsKey((siCntDetailsVoObj.getStrCommodity()))));
                        if (insmap != null) {
                            siCntDetailsVoObj.setInsuredemail(checkNull((String) insmap.get("insuredemail")));
                            if ((insmap.containsKey((siCntDetailsVoObj.getStrCommodity()))
                                    || insmap.containsKey((siCntDetailsVoObj.getHsCode())))) {// .replaceAll(strPattern,
                                // "")
                                qouteno = checkNull((String) insmap.get((siCntDetailsVoObj.getStrCommodity())));
                                if (qouteno.length() == 0) {
                                    qouteno = checkNull((String) insmap.get((siCntDetailsVoObj.getHsCode())));
                                }
                                System.out.println("[ECOM] EcomShippingInstructionBean qoute==> " + qouteno);
                                siCntDetailsVoObj.setIsinsuranceavailable("Y");
                                siCntDetailsVoObj.setQuoteno(qouteno);
                            }
                        }
                        //
                        System.out.println("[ECOM] EcomShippingInstructionBean sino::::" + sino + "::conId::" + conId);
                        // ----------------------START FROM HERE----------------------------------
                        String DRAFT_DG_SELECT = "select eSIno,containerno,conId,imocontact,imophone "
                                + "from ecomSI_dg where eSIno=? and conId=?";
                        ps_dgcom = con_cargo.prepareStatement(DRAFT_DG_SELECT);
                        ps_dgcom.setString(1, sino);
                        ps_dgcom.setString(2, conId);
                        rs_dg = ps_dgcom.executeQuery();
                        while (rs_dg.next()) {
                            siCntDetailsVoObj.setImocontact(rs_dg.getString("imocontact"));
                            siCntDetailsVoObj.setImophone(rs_dg.getString("imophone"));
                        }

                        String DRAFT_OOG_SELECT = "select eSIno,containerno,conId,forward,topft,rightft,"
                                + "Alt,leftft,Unit from ecomSI_oog where eSIno=? and conId=? and comitemno=?";
                        ps_oogcom = con_cargo.prepareStatement(DRAFT_OOG_SELECT);

                        ps_oogcom.setString(1, sino);
                        ps_oogcom.setString(2, conId);
                        ps_oogcom.setString(3, comconId);
                        rs_oog = ps_oogcom.executeQuery();

                        while (rs_oog.next()) {
                            System.out.println("[ECOM] EcomShippingInstructionBean >>rs_oog.getString(\"forward\")>>>"
                                    + rs_oog.getString("forward"));
                            // si_no,eqpId,cargoNo,itemforward, itemtop, itemright,
                            // itemaft,itemLeft,itemLengthUnit
                            siCntDetailsVoObj.setItemforward(rs_oog.getString("forward"));
                            siCntDetailsVoObj.setItemtop(rs_oog.getString("topft"));
                            siCntDetailsVoObj.setItemright(rs_oog.getString("rightft"));
                            siCntDetailsVoObj.setItemaft(rs_oog.getString("Alt"));
                            siCntDetailsVoObj.setItemleft(rs_oog.getString("leftft"));
                            siCntDetailsVoObj.setItemlengthUnit(rs_oog.getString("Unit"));
                            siCntMasterVoObj.setOog("Y");
                        }
                        //
                        cntDetailObj.add(siCntDetailsVoObj);
                    }
                }
                siCntValVO.setSiSealObj(siSealVO);
                siCntValVO.setCntDeatilsObj(cntDetailObj);
                siCntHashObj.put(siCntMasterVoObj, siCntValVO);
                i++;

            }

        } catch (Exception e) {
            System.out.println("[ECOM] EcomShippingInstructionBean Exception While Calling  fetchSIContainerDetail->"
                    + e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                if (rs_cargo != null) {
                    rs_cargo.close();
                }

                if (rs_cargo1 != null) {
                    rs_cargo1.close();
                }

                if (ps_cargo != null) {
                    ps_cargo.close();
                }
                if (ps_cargo1 != null) {
                    ps_cargo1.close();
                }

                if (rs_com != null) {
                    rs_com.close();
                }
                if (rs_dg != null) {
                    rs_dg.close();
                }
                if (rs_oog != null) {
                    rs_oog.close();
                }
                if (ps_com != null) {
                    ps_com.close();
                }
                if (ps_dgcom != null) {
                    ps_dgcom.close();
                }
                if (ps_oogcom != null) {
                    ps_oogcom.close();
                }
                if (con_cargo != null) {
                    con_cargo.close();
                }

            } catch (Exception e) {
                System.out.println(
                        "[ECOM] EcomShippingInstructionBean Exception While Closing connetion in  fetchSIContainerDetail->"
                        + e.getMessage());
                e.printStackTrace();

            }
        }
        return siCntHashObj;

    }

    @Override
    public HashMap fetchPortDetForSI(String bookno) {
        System.out.println("[ECOM] EcomShippingInstructionBean ===inside fetchPortDetForSI============");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        HashMap hm = new HashMap();
        String ccMailId = null;
        try {

            SIPartiesVO partiesVO = null;
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GETECOM_BOOKNO);
            ps.setString(1, bookno);
            rst = ps.executeQuery();
            if (rst.next()) {
                ccMailId = checkNull(rst.getString("ccmailid"));
            }
            CompleteModel soModelObj = siVDS.getEcomPortDetForSI(bookno);
            for (Vesselmaster vesselMasterObj : soModelObj.getVesselmaster()) {
                partiesVO = new SIPartiesVO();
                partiesVO.setVesselcode(vesselMasterObj.getVesselcode());
                partiesVO.setVoyagecode(vesselMasterObj.getVoyagecode());
                partiesVO.setBound(vesselMasterObj.getBound());
                partiesVO.setCcMailId(checkNull(ccMailId));
                partiesVO.setPod(vesselMasterObj.getDischargeportid());
                partiesVO.setPol(vesselMasterObj.getLoadportid());
                // partiesVO.setReceipt(vesselMasterObj.getOriginportdesc());
                // partiesVO.setDelivery(vesselMasterObj.getFinalportdesc());
                partiesVO.setReceipt(vesselMasterObj.getOriginportid());
                partiesVO.setDelivery(vesselMasterObj.getFinalportid());
                partiesVO.setVesselName(checkNull(vesselMasterObj.getVesselcode()).toUpperCase());
                System.out.println("[ECOM] EcomShippingInstructionBean >>partiesVO>>>" + partiesVO.getVesselName()
                        + ">>>vesselMasterObj.getVesselname()>>>" + vesselMasterObj.getVesselname()
                        + ">>partiesVO.setVesselcode>>>" + partiesVO.getVesselcode());
                partiesVO.setPolName(vesselMasterObj.getLoadportid());
                partiesVO.setPodName(vesselMasterObj.getDischargeportid());
                partiesVO.setAgencyRefNo(checkNull(vesselMasterObj.getAgencyRefNo()));
                partiesVO.setPolat(vesselMasterObj.getPolat());
                partiesVO.setPodat(vesselMasterObj.getPodat());
                partiesVO.setServicecode(vesselMasterObj.getServicecode());
                partiesVO.setCargoprotectid(vesselMasterObj.getCargoprotectid());
                partiesVO.setEbook_t2lfiling(vesselMasterObj.getT2lfiling());
                partiesVO.setEbook_nvocc(vesselMasterObj.getNvocc());
                partiesVO.setEbook_EnsDocType(vesselMasterObj.getEnsdoctype());
                partiesVO.setEbook_EnsFiling(vesselMasterObj.getEnsfiling());
                System.out.println(
                        "[ECOM] EcomShippingInstructionBean >>setEbook_t2lfiling" + partiesVO.getEbook_t2lfiling() + vesselMasterObj.getEnsfiling() + vesselMasterObj.getEnsdoctype());
                //
                String str_org = checkNull(vesselMasterObj.getPrecarriage());
                String str_des = checkNull(vesselMasterObj.getOncarriage());

                if ("M".equalsIgnoreCase(str_org)) {
                    str_org = "Merchant";

                } else if ("C".equalsIgnoreCase(str_org)) {
                    str_org = "Carrier";

                } else {
                    str_org = "NA";

                }
                if ("M".equalsIgnoreCase(str_des)) {
                    str_des = "Merchant";

                } else if ("C".equalsIgnoreCase(str_des)) {
                    str_des = "Carrier";

                } else {
                    str_des = "NA";

                }
                partiesVO.setTransportOrigin(str_org);
                partiesVO.setTransportDestination(str_des);
                partiesVO.setIseuport(checkEUPort(checkNull(vesselMasterObj.getDischargeportid()) + ","
                        + checkNull(vesselMasterObj.getFinalportid())));
                hm.put("routing", partiesVO);
                break;
            }

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
        return hm;

    }

    @Override
    public SIPartiesVO getSIMasterDetails(String siNo) {
        SIPartiesVO vo = new SIPartiesVO();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_SI_MASTER_QRY);
            ps.setString(1, siNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                vo.setPla_free_txt(checkNull(rs.getString("plaHeaderName")));
                vo.setPol_free_txt(checkNull(rs.getString("polHeaderName")));
                vo.setPod_free_txt(checkNull(rs.getString("podHeaderName")));
                vo.setPld_free_txt(checkNull(rs.getString("pldHeaderName")));
                vo.setVslvoyage_free_txt(checkNull(rs.getString("Voyage")));
                vo.setAESNumber(checkNull(rs.getString("aesnumber")));
                vo.setAESDiscliamer(checkNull(rs.getString("aesdiscliamer")));
                vo.setReleasetype(checkNull(rs.getString("releasetype")));
                vo.setDeclaredValue(checkNull(rs.getString("declaredvalue")));
                vo.setACID(checkNull(rs.getString("ACID")));
                vo.setFreight(checkNull(rs.getString("Freight")));
                vo.setBltype(checkNull(rs.getString("bltype")));
                vo.setCargoprotectid(rs.getString("cargoprotectid"));
                vo.setEbook_t2lfiling(rs.getString("t2lfiling"));
                vo.setPackinglst(rs.getString("packinglst"));
                vo.setInvoiceupload(rs.getString("invoiceupload"));
                vo.setOrgTelType(rs.getString("telexrls"));
                vo.setEbook_EnsDocType(rs.getString("enstype"));
                vo.setBltype_UAE(rs.getString("bltype_UAE"));
                vo.setMpci_id(rs.getString("mpci_id"));
                vo.setEbook_nvocc(rs.getString("nvocc"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            System.out.println("[ECOM] EcomShippingInstructionBean exception while closing connection" + e);
            e.printStackTrace();
        } finally {
            System.out.println("[ECOM] EcomShippingInstructionBean ====getSIMasterDetails Done==============");
        }
        return vo;
    }

    private List getecomcustfrombooking(Map rethm, String bookno) {
        List custList = new ArrayList();
        Map ret2 = null;
        List vlist = null;
        CustomerDetailsVO custVO = null;
        // SIPartiesVO partiesVO = null;
        String phone = null, fax = null;
        try {
            if (rethm != null && rethm.size() > 0) {
                ret2 = (Map) rethm.get("rs");
                if (ret2 != null && ret2.size() > 0) {
                    vlist = (List) ret2.get("rs0");
                    if (vlist != null && vlist.size() > 0) {
                        for (int i = 0; i < vlist.size(); i++) {
                            Map rs = (Map) vlist.get(i);
                            // custVO = new CustomerDetailsVO();
                            custVO = new CustomerDetailsVO();
                            custVO.setName(checkNull((String) rs.get("custname")));
                            custVO.setCustCode(checkNull((String) rs.get("custcode")));
                            custVO.setCustType(checkNull((String) rs.get("custtypeid")));
                            custVO.setAddress(checkNull((String) rs.get("address1")) + ","
                                    + checkNull((String) rs.get("address2")));
                            custVO.setAddress1(checkNull((String) rs.get("address2")));
                            custVO.setCity(checkNull((String) rs.get("city")));
                            custVO.setCountryCode(checkNull((String) rs.get("countryid")));
                            System.out.println(
                                    "[ECOM] EcomShippingInstructionBean ============coutnry id from SP ========>"
                                    + rs.get("countryid"));
                            custVO.setCountry(checkNull((String) rs.get("countryid")));
                            custVO.setEmail(checkNull((String) rs.get("email")));
                            phone = checkNull((String) rs.get("telephone"));
                            fax = checkNull((String) rs.get("fax"));
                            if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                                custVO.setPhoneNo(phone);
                            } else {
                                custVO.setPhoneNo("");
                            }
                            if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                                custVO.setFax(fax);
                            } else {
                                custVO.setFax("");
                            }
                            custVO.setEroi(checkNull((String) rs.get("eori")));
                            custList.add(custVO);
                        }
                    }
                }
            }
            CompleteModel siModelObj = siVDS.getecombookingcust(bookno);
            for (Booking_cust bkngCustObj : siModelObj.getBooking_cust()) {
                custVO = new CustomerDetailsVO();
                custVO.setName(checkNull((String) bkngCustObj.getName()));
                custVO.setCustCode(checkNull((String) bkngCustObj.getCustid()));
                custVO.setCustType(checkNull((String) bkngCustObj.getCusttypeid()));
                custVO.setAddress(checkNull((String) bkngCustObj.getAddress1()) + ","
                        + checkNull((String) bkngCustObj.getAddress2()));
                custVO.setAddress1(checkNull((String) bkngCustObj.getAddress2()));
                custVO.setCity(checkNull((String) bkngCustObj.getCity()));
                custVO.setCountryCode(checkNull((String) bkngCustObj.getCountryid()));
                custVO.setEroi(checkNull((String) bkngCustObj.getEori()));
                custVO.setPerontype(checkNull((String) bkngCustObj.getPersontype()));
                System.out.println("[ECOM] EcomShippingInstructionBean ============coutnry id from SP ========>"
                        + bkngCustObj.getCountryid());
                custVO.setCountry(checkNull((String) bkngCustObj.getCountryid()));
                custVO.setEmail(checkNull((String) bkngCustObj.getEmail()));
                phone = checkNull((String) bkngCustObj.getTelephone());
                fax = checkNull((String) bkngCustObj.getFax());
                if (phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                    custVO.setPhoneNo(phone);
                } else {
                    custVO.setPhoneNo("");
                }
                if (fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                    custVO.setFax(fax);
                } else {
                    custVO.setFax("");
                }
                custList.add(custVO);
            }
        } catch (Exception e) {
        }
        return custList;
    }

    @Override
    public HashMap fetchSICustomerDetailsFromEcom(String sino) throws RemoteException {
        HashMap partiesHm = new HashMap();
        ArrayList custList = null;
        CustomerDetailsVO custVO = null;
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        int i = 0;
        SIBLMasterVO masterVO = null;
        String custtype = null, ccMailId = null;

        try {
            System.out.println(
                    "[ECOM] EcomShippingInstructionBean ===sino to select parties from ecom table=========>" + sino);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOM_SI_CUST);
            ps.setString(1, sino);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("[ECOM] EcomShippingInstructionBean ---inside rst====custtypeis is===>"
                        + rs.getString("custTypeId"));
                custList = new ArrayList();
                custVO = new CustomerDetailsVO();
                custVO.setName(rs.getString("name"));
                custVO.setCustCode(rs.getString("custId"));
                custtype = rs.getString("custTypeId");
                custVO.setAddress(rs.getString("address1"));
                custVO.setAddress1(rs.getString("address2"));
                custVO.setCity(rs.getString("city"));
                custVO.setCountryCode(rs.getString("countryId"));
                custVO.setCountry(rs.getString("countryId"));
                custVO.setEmail(rs.getString("email"));
                custVO.setPhoneNo(rs.getString("telephone"));
                custVO.setFax(rs.getString("fax"));
                custVO.setToorder(rs.getString("toorder"));
                custVO.setNotifyCheck(rs.getString("flag"));
                custVO.setState(rs.getString("stateid"));
                custVO.setEroi(rs.getString("eori"));
                custVO.setPerontype(rs.getString("persontype"));
                custVO.setZipcode(rs.getString("zipcode"));

                custList.add(custVO);
                partiesHm.put(custtype, custList);
            }
            System.out.println("[ECOM] EcomShippingInstructionBean >>partiesHm>>" + partiesHm);

            /* con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection(); */
            ps = null;
            rs = null;
            ps = con.prepareStatement(GET_ECOM_SI_FILE);
            ps.setString(1, checkNull(sino));
            rs = ps.executeQuery();
            HashMap fileHM = new HashMap();
            while (rs.next()) {
                fileHM.put(rs.getString("FileCatgeory"), rs.getString("FileName"));
            }
            ps = null;
            rs = null;
            ps = con.prepareStatement(GET_ECOM_SI_REMARKS);
            ps.setString(1, sino);
            rs = ps.executeQuery();
            if (rs.next()) {
                masterVO = new SIBLMasterVO();
                masterVO.setShipperName(checkNull(rs.getString("shippername")));
                masterVO.setShipperHeader(checkNull(rs.getString("shipper")));
                masterVO.setConsigneeName(checkNull(rs.getString("consigeename")));
                masterVO.setConsigneeHeader(checkNull(rs.getString("consigee")));
                masterVO.setNotifyName(checkNull(rs.getString("notifyname")));
                masterVO.setNotifyHeader(checkNull(rs.getString("notify")));
                masterVO.setCustRemarks(rs.getString("custremarks"));
                masterVO.setCcMailId(checkNull(rs.getString("ccmailid")));
            }
            System.out.println("[ECOM] EcomShippingInstructionBean >>>si cc mail id>>" + ccMailId);
            partiesHm.put("MASTERDETAILS", masterVO);
            partiesHm.put("File_path", fileHM);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcomShippingInstructionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcomShippingInstructionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcomShippingInstructionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return partiesHm;
    }

    @Override
    public HashMap fetchSICustomerDetails(String bookno, String sino, String custref) throws RemoteException {

        HashMap partiesHm = new HashMap();
        ArrayList custList = null;
        CustomerDetailsVO custVO = null;
        List custlst = null;
        String custtype = null, email = null, phone = null, fax = null;
        try {
            int custCount = 0;
            System.out.println(
                    "[ECOM] EcomShippingInstructionBean ===sino to select parties from ecom table=========>" + sino);
            Map hs = (Map) bkgVDS.getecombookingcust(bookno);
            System.out.println("[ECOM] EcomShippingInstructionBean >>>booking cust for si>>>" + hs);
            if (hs != null) {
                custlst = (List) hs.get("booking_cust");
            }
            if (custlst != null && custlst.size() > 0) {
                for (int j = 0; j < custlst.size(); j++) {
                    Map rs = (Map) custlst.get(j);
                    checkNull((String) rs.get("custtypeid"));
                    custtype = Validation.checkNull((String) rs.get("custtypeid"));
                    custList = new ArrayList();
                    custVO = new CustomerDetailsVO();
                    custVO.setName(checkNull((String) rs.get("name")));
                    custVO.setCustCode(checkNull((String) rs.get("custid")));
                    custVO.setCustType(custtype);
                    custVO.setAddress(checkNull((String) rs.get("address1")));
                    custVO.setAddress1(checkNull((String) rs.get("address2")));
                    custVO.setCity(checkNull((String) rs.get("city")));
                    custVO.setCountryCode(checkNull((String) rs.get("countryid")));
                    custVO.setCountry(checkNull((String) rs.get("countryid")));
                    email = checkNull((String) rs.get("email"));
                    if (email.length() > 0 && email.equalsIgnoreCase("-")) {
                        email = "";
                    }
                    custVO.setEmail(checkNull(email));
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
                    custVO.setPhoneNo(checkNull(phone));
                    custVO.setFax(checkNull(fax));
                    custVO.setState(checkNull((String) rs.get("stateid")));
                    custVO.setZipcode(checkNull((String) rs.get("zipcode")));
                    custVO.setEroi(checkNull((String) rs.get("eori")));
                    custVO.setTaxId(checkNull((String) rs.get("vatno")));
                    custVO.setPerontype(checkNull((String) rs.get("persontype")));
                    custList.add(custVO);
                    partiesHm.put(checkNull((String) rs.get("custtypeid")).trim(), custList);
                }
            }
            System.out.println("[ECOM] EcomShippingInstructionBean >>>partiesHm map in ejb>>>>" + partiesHm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return partiesHm;
    }

    @Override
    public String saveSI(int sicount, String bookno, String custcode, String login_name) {
        Connection con = null;
        PreparedStatement ps = null;
        String result = "";
        ResultSet rst = null;

        String reqId = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(BOOK_NO_GEN);
            ps.setString(1, "SI");
            rst = ps.executeQuery();
            while (rst.next()) {
                int cnt = rst.getInt("mdlindex") + 1;
                reqId = rst.getString("mdlprefix") + cnt;
            }
            rst = null;
            ps = null;
            ps = con.prepareStatement(ECOM_SI_REQUEST_INSERT);
            ps.setString(1, reqId);
            ps.setString(2, login_name);
            ps.setInt(3, sicount);
            ps.setInt(4, 0);
            ps.setString(5, bookno);
            ps.setString(6, custcode);
            ps.setString(7, login_name);
            ps.setString(8, login_name);
            int res = ps.executeUpdate();
            if (res == 1) {
                result = "inserted";
            } else {
                result = "notinserted";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
                if (rst != null) {
                    rst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("[ECOM] EcomShippingInstructionBean Result=========>" + result);
        return result;

    }

    private String getPortName(String portcode) {
        String result = null;
        List portList = null;
        Map resultMap = null;
        try {
            result = "";
            Map hs = (Map) bkgVDS.getecomports(portcode);
            if (hs != null) {
                portList = (List) hs.get("ports");
                if (portList != null && portList.size() > 0) {
                    resultMap = (Map) portList.get(0);
                    result = checkNull((String) resultMap.get("portname"));
                }
            }
            System.out.println("[ECOM] EcomShippingInstructionBean >>>getPortName>>>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String getVesselName(String vesselcode) {
        String result = null;
        List<VesselMaster> vessellst = null;
        Gson gson = null;
        try {
            gson = new Gson();
            Map hs = (Map) bkgVDS.getecomvesseldetails(vesselcode);
            vessellst = gson.fromJson(gson.toJson((ArrayList) hs.get("vesselmaster")),
                    new TypeToken<List<VesselMaster>>() {
                    }.getType());
            if (vessellst != null && vessellst.size() > 0) {
                VesselMaster rs = (VesselMaster) vessellst.get(0);
                result = checkNull(rs.getVesselname());
            } else {
                result = "";
            }
            System.out.println("[ECOM] EcomShippingInstructionBean >>>getVesselName>>>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

   /*synchronized private String bookNoGenerate(String module) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String prefixname = null;
        int siIndexNum = 0;
        String bkNum = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(BOOK_NO_GEN);
            ps.setString(1, module);
            rs = ps.executeQuery();
            while (rs.next()) {
                siIndexNum = rs.getInt("mdlindex");
                prefixname = rs.getString("mdlPrefix");
            }
            ps = con.prepareStatement(BOOK_NO_UPDATE);
            ps.setInt(1, siIndexNum + 1);
            ps.setString(2, module);
            ps.executeUpdate();
            bkNum = prefixname + (siIndexNum + 1);

            System.out.println("[ECOM] EcomShippingInstructionBean ===========NEW bknum==========>>>>>>" + bkNum);

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
                System.out.println(
                        "[ECOM] EcomShippingInstructionBean exception while closing connection bookNoGenerate" + e);
                e.printStackTrace();
            }

        }
        return bkNum;
    } */
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
                    System.out.println("[ECOM] EcomShippingInstructionBean NEW bknum >> " + bknum);
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

    @Override
    public String getTareWtForCntr(String cntrNo, String eqpSize, String nor, String soc) throws RemoteException {
        String tareWt = null, neweqptype = null, coneqptype = null;
        Map rs = null;
        try {
            Map map = (Map) siVDS.getEcomTareWeightForSI(cntrNo);
            System.out.println("[ECOM] EcomShippingInstructionBean ######>" + map);
            List lst = (List) map.get("containerprofile");
            if (lst != null && lst.size() > 0) {
                rs = (Map) lst.get(0);
                coneqptype = checkNull((String) rs.get("equipmenttype")).trim();
                tareWt = checkNull((String) rs.get("tarewt")).trim();
            }
            // if (eqpSize != null && nor != null && coneqptype != null &&
            // (!eqpSize.trim().equalsIgnoreCase(coneqptype))) {
            // map = (Map) siVDS.getecomeqpsizemapping(eqpSize, coneqptype, nor);
            // lst = (List) map.get("eqpsizemapping");
            // if (lst != null && lst.size() > 0) {
            // map = (Map) lst.get(0);
            // neweqptype = checkNull((String) map.get("mappedtype"));
            // }
            // if (!(neweqptype != null && neweqptype.equalsIgnoreCase(coneqptype))) {
            // tareWt = null;
            // }
            // }

            if ((tareWt == null || tareWt.trim().length() == 0) && (!"Y".equalsIgnoreCase(soc)
                    || (cntrNo.contains(eqpSize + "TNB") || (cntrNo.contains(eqpSize + "TBN"))))) {
                map = null;
                lst = null;
                rs = null;
                map = (Map) siVDS.getEcomTarewtFromEqp(eqpSize);
                System.out.println("[ECOM] EcomShippingInstructionBean ######>" + map);
                lst = (List) map.get("ecom_equipments");
                if (lst != null && lst.size() > 0) {
                    rs = (Map) lst.get(0);
                    tareWt = checkNull((String) rs.get("tarekg")).trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tareWt;
    }

    @Override
    public String getBLNoFromBookNo(String book_no) throws RemoteException {
        String result = null;
        List lst = null;
        Map rs = null;
        try {
            Map map = (Map) siVDS.getBLNoFromBookno(book_no);
            System.out.println("[ECOM] EcomShippingInstructionBean >>getBLNoFromBookno>>+" + map);
            if (map != null) {
                lst = (List) map.get("bl");
                if (lst != null && lst.size() > 0) {
                    rs = (Map) lst.get(0);
                    result = checkNull((String) rs.get("status"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getSIDeclinedRemarks(String siNo) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "No Declined Message.";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_SI_DECLINED_REMARKS);
            ps.setString(1, siNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = checkNull(rs.getString("declined_remarks"));
            }
            System.out.println("[ECOM] EcomShippingInstructionBean >>result>>" + result);
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
                System.out.println(
                        "[ECOM] EcomShippingInstructionBean exception while closing connection bookNoGenerate" + e);
                e.printStackTrace();
            }

        }
        return result;
    }

    @Override
    public String cancelSubmittedSI(String siNo, String bookNo) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "Not Processed";
        String ediStatus = null, ecomStatus = null;
        ArrayList lst = null;
        EcomBKGEDIDocStatus doc = null;
        EcomCommonModel cm = null;
        Map resultMap = null;
        List list = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOM_SI_STATUS);
            ps.setString(1, siNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                ecomStatus = rs.getString("status");
            }
            resultMap = bkgVDS.getEcomEDIDocstatus(siNo);
            if (resultMap != null) {
                list = (List) resultMap.get("edidocstatus");
                if (list != null && list.size() > 0) {
                    resultMap = (Map) list.get(0);
                    ediStatus = (String) resultMap.get("status");
                }
            }
            System.out.println(
                    "[ECOM] EcomShippingInstructionBean >>>edi status>>" + ediStatus + ">>ecomStatus>>" + ecomStatus);
            if ("R".equalsIgnoreCase(ediStatus) && "S".equalsIgnoreCase(ecomStatus)) {
                lst = new ArrayList();
                doc = new EcomBKGEDIDocStatus();
                cm = new EcomCommonModel();
                doc.setBook_no(siNo);
                lst.add(doc);
                cm.setEdidocstatus(lst);
                bkgVDS.putecomedidocstatus(cm);
                ps = con.prepareStatement(UPDATE_ECOM_SI_STATUS);
                ps.setString(1, siNo);
                ps.executeUpdate();

                // Delete from SI request master
                ps = con.prepareStatement(DELETE_SI_REQUEST);
                ps.setString(1, bookNo);
                ps.executeUpdate();
            } else {
                result = "Processed";
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

    private String getBackOfficeMailId() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement("select pathvalue from globalconfigproduct where pathname ='BACKOFFICEMAILID'");
            rs = ps.executeQuery();
            while (rs.next()) {
                result = checkNull(rs.getString("pathvalue"));
            }
            System.out.println("[ECOM] EcomShippingInstructionBean getBackOfficeMailId>>result>>" + result);
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
                System.out.println(
                        "[ECOM] EcomShippingInstructionBean exception while closing connection getBackOfficeMailId"
                        + e);
                e.printStackTrace();
            }

        }
        return result;
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

    @Override
    public String getAgencyEmail(String bookNo) throws RemoteException {
        String agencymail = "";
        try {
            CompleteModel siModelObj = siVDS.getecombookingdetails(bookNo);
            String documentalAgencyid = null;
            for (Booking bookingObj : siModelObj.getBooking()) {
                documentalAgencyid = bookingObj.getDocumentalagencyid();
            }
            agencymail = getAgencyMail(documentalAgencyid);
            if ((agencymail == null || "".equalsIgnoreCase(agencymail.trim()))
                    && "BAKOFF".equalsIgnoreCase(documentalAgencyid)) {
                agencymail = getBackOfficeMailId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agencymail;
    }

    @Override
    public List getBookingcharges(String bookno) {
        List chargelis = null;
        ChargesVo charge = null;
        try {
            chargelis = new ArrayList();
            System.out.println("[ECOM] EcomShippingInstructionBean inside getBookingcharges");
            Map map = (Map) siVDS.getEcomBookingCharges(bookno);
            System.out.println("[ECOM] EcomShippingInstructionBean ######>" + map);
            List lst = (List) map.get("booking_chargenew");
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    map = (Map) lst.get(i);
                    charge = new ChargesVo();
                    charge.setBook_no((String) map.get("book_no"));
                    charge.setChargeid((String) map.get("chargeid"));
                    charge.setChargename((String) map.get("chargename"));
                    charge.setCusttypeid((String) map.get("custtypeid"));
                    charge.setPayagencyid((String) map.get("payagencyid"));
                    charge.setFprepaidcollect((String) map.get("prepaidcollect"));
                    charge.setEqptypeid((String) map.get("eqptypeid"));
                    chargelis.add(charge);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargelis;
    }

    @Override
    public List getSICharges(String sino) {
        List chargelis = null;
        ChargesVo charge = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            chargelis = new ArrayList();
            System.out.println("[ECOM] EcomShippingInstructionBean inside getSICharges");
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(SI_CHARGES, 1004, 1007);
            ps.setString(1, sino);
            rs = ps.executeQuery();
            while (rs.next()) {
                charge = new ChargesVo();
                charge.setBook_no(rs.getString("esino"));
                charge.setChargeid(rs.getString("chargeid"));
                charge.setChargename(rs.getString("chargename"));
                charge.setCusttypeid(rs.getString("custtypeid"));
                charge.setPayagencyid(rs.getString("payagencyid"));
                charge.setFprepaidcollect(rs.getString("prepaidcollect"));
                charge.setEqptypeid(rs.getString("eqptypeid"));
                chargelis.add(charge);
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
        return chargelis;
    }

    @Override
    public String getPrepaidCollectLocation(String book_no, String frieght) {
        String Result = "", agencyid = null, docagencyid = null, delivery = null, loadagency = null;
        try {
            System.out.println("[ECOM] EcomShippingInstructionBean inside getPrepaidCollectLocation");
            Map map = (Map) bkgVDS.getecombookingdetails(book_no);
            System.out.println("[ECOM] EcomShippingInstructionBean ######>" + map);
            List lst = (List) map.get("booking");
            if (lst != null && lst.size() > 0) {
                map = (Map) lst.get(0);
                agencyid = (String) map.get("agencyid");
                docagencyid = (String) map.get("documentalagencyid");
                delivery = (String) map.get("finalportid");
            }
            if ("P".equalsIgnoreCase(frieght)) {
                if ("BAKOFF".equalsIgnoreCase(docagencyid)) {
                    loadagency = agencyid;
                } else {
                    loadagency = docagencyid;
                }
                map = (Map) siVDS.getEcomPrepaidLocation(loadagency);
                System.out.println("[ECOM] EcomShippingInstructionBean agency######>" + map);
                lst = (List) map.get("agency");
                if (lst != null && lst.size() > 0) {
                    map = (Map) lst.get(0);
                    Result = (String) map.get("location");
                }
            } else if (("C".equalsIgnoreCase(frieght))) {
                map = (Map) siVDS.getEcomCollectLocation(delivery);
                System.out.println("[ECOM] EcomShippingInstructionBean Collect#####>" + map);
                lst = (List) map.get("agency");
                if (lst != null && lst.size() > 0) {
                    map = (Map) lst.get(0);
                    Result = (String) map.get("location");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }

    @Override
    public List getThirdPartyLocation(String book_no) {
        List loclis = null;
        PortVO loc = null;
        String agencyid = null, docagencyid = null, pol = null, pod = null, pla = null, pld = null, loadagency = null;
        try {
            loclis = new ArrayList();
            System.out.println("[ECOM] EcomShippingInstructionBean inside getThirdPartyLocation");
            Map map = (Map) bkgVDS.getecombookingdetails(book_no);
            System.out.println("[ECOM] EcomShippingInstructionBean ######>" + map);
            List lst = (List) map.get("booking");
            if (lst != null && lst.size() > 0) {
                map = (Map) lst.get(0);
                agencyid = (String) map.get("agencyid");
                docagencyid = (String) map.get("documentalagencyid");

                pla = (String) map.get("originportid");
                pld = (String) map.get("finalportid");
                pod = (String) map.get("loadportid");
                pol = (String) map.get("dischargeportid");

            }
            if ("BAKOFF".equalsIgnoreCase(docagencyid)) {
                loadagency = agencyid;
            } else {
                loadagency = docagencyid;
            }

            String port = pla + "," + pld + "," + pod + "," + pol;
            map = (Map) siVDS.getEcomThirdPartyLocation(port);
            System.out.println("[ECOM] EcomShippingInstructionBean agency######>" + map);
            lst = (List) map.get("agency");
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    map = (Map) lst.get(i);
                    loc = new PortVO();
                    loc.setPortCode((String) map.get("location"));
                    loc.setPortName((String) map.get("portname"));
                    loclis.add(loc);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loclis;
    }

    @Override
    public String checkContainerValidation(String book_no, String containerlisstr) {
        String result = "";
        try {
            Map map = (Map) siVDS.getEcomSIContainerValidation(book_no, containerlisstr);
            System.out.println("[ECOM] EcomShippingInstructionBean checkContainerValidation##>" + map);
            List lst = (List) map.get("booking");
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    map = (Map) lst.get(i);
                    if (i != 0) {
                        result = result + ",";
                    }
                    result = result + map.get("eqpid");
                }
                result = "false~~~(" + result + ")";
            } else {
                result = "true~~~()";
            }
            System.out.println("[ECOM] EcomShippingInstructionBean  checkContainerValidation " + result);

        } catch (Exception e) {
            result = "error~~~()";
            e.printStackTrace();
        }
        return result;
    }

    public String getStateCode(String state, String countrycode, String book_no) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println("[ECOM] EcomShippingInstructionBean getStateCode  -- Book_no=" + book_no + "  State=> "
                    + state + "  countrycode==> " + countrycode);
            if (state != null && checkNull(state).length() > 0 && checkNull(state).contains("-")) {
                state = state.split("-")[0].trim();
            }
            if (state != null && checkNull(state).length() > 4) {
                con = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();
                String query = "select statecode from Statesmaster where statedesc = '" + state + "' and countrycode='"
                        + countrycode + "'";
                System.out.println("[ECOM] EcomShippingInstructionBean Query => " + query);
                ps = con.prepareStatement(query, 1004, 1007);
                rs = ps.executeQuery();
                if (rs.next()) {
                    state = checkNull(rs.getString("statecode"));
                } else {
                    state = "";
                }
            }
            System.out.println("[ECOM] EcomShippingInstructionBean result----> " + state);
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
            System.out.println("[ECOM] EcomShippingInstructionBean getInsuranceDetils==> ");
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

            ps = con.prepareStatement(
                    "select commodity,quoteno,partytype,suminsured,email from ecom_insurance where lrpbook_no=?");
            ps.setString(1, checkNull(bookno));
            rs = ps.executeQuery();
            insmap = new HashMap();
            String qouteno = null, commodity = "", sumins = "";
            ;
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
            System.out.println("[ECOM] EcomShippingInstructionBean insmap===> " + insmap);
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
            System.out.println(
                    "[ECOM] EcomShippingInstructionBean actualfile: " + actualfile + " &renamefile: " + renamefile);
            FileUtils.moveFile(file, rename);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map getBKGReference(String bkgno) throws RemoteException {
        Map result = null;
        List lis = null;
        Map map = null;
        try {
            System.out.println("[ECOM] EcomShippingInstructionBean Inside getBKGReference");
            result = new HashMap();
            map = bkgVDS.getecombookingreference(bkgno);
            System.out.println("[ECOM] EcomShippingInstructionBean getBKGReference--> " + map);
            if (map != null) {
                lis = (List) map.get("booking_reference");
                if (lis != null && lis.size() > 0) {
                    for (int i = 0; i < lis.size(); i++) {
                        map = (Map) lis.get(i);
                        result.put((String) map.get("refid"), (String) map.get("refval"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean checkMaskPortForSIBL(String bookno) throws RemoteException {
        boolean maskport = false;
        List lis = null;
        Map map = null;
        try {
            System.out.println("[ECOM] EcomShippingInstructionBean Inside checkMaskPortForSIBL");

            map = bkgVDS.getEcomSIBLMaskPort(bookno);
            System.out.println("[ECOM] EcomShippingInstructionBean getBKGReference--> " + map);
            if (map != null) {
                lis = (List) map.get("booking_leg");
                System.out.println("[ECOM] EcomShippingInstructionBean lis==> " + lis);
                if (lis != null && lis.size() > 0) {
                    maskport = true;
                }
            }
            System.out.println("[ECOM] EcomShippingInstructionBean maskport=> " + maskport);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maskport;
    }

    @Override
    public String getRateCommodityList(String refno, String Type) throws RemoteException {
        String rateCmdtyJson = "[]";
        List lis = null;
        Map map = null;
        String raterefno = null, ratetype = null, orderno = null;
        Map<String, String> ratecmdtymap = null;
        List<Map<String, String>> ratecmdtylist = null;
        try {
            System.out.println(
                    "[ECOM] EcomShippingInstructionBean Inside getRateCommodityList=> " + Type + " &refno=> " + refno);
            ratecmdtylist = new ArrayList<Map<String, String>>();
            if ("BL".equalsIgnoreCase(Type)) {
                map = bkgVDS.getecomblvgmcontainers(refno);
                if (map != null) {
                    lis = (List) map.get("bl_cargo");
                    if (lis != null && lis.size() > 0) {
                        for (int i = 0; i < lis.size(); i++) {
                            map = (Map) lis.get(i);
                            if (map != null && checkNull((String) map.get("raterefno")).length() > 0) {
                                raterefno = checkNull((String) map.get("raterefno"));
                                ratetype = checkNull((String) map.get("ratetype"));
                                orderno = checkNull((String) map.get("orderno"));
                                break;
                            }
                        }
                    }
                }
            } else {
                map = bkgVDS.getecombookingcargo(refno);
                if (map != null) {
                    lis = (List) map.get("booking_cargo");
                    if (lis != null && lis.size() > 0) {
                        for (int i = 0; i < lis.size(); i++) {
                            map = (Map) lis.get(i);
                            if (map != null && checkNull((String) map.get("raterefno")).length() > 0) {
                                raterefno = checkNull((String) map.get("raterefno"));
                                ratetype = checkNull((String) map.get("ratetype"));
                                orderno = checkNull((String) map.get("orderno"));
                                break;
                            }
                        }
                    }
                }
            }
            System.out.println("[ECOM] EcomShippingInstructionBean raterefno=>" + raterefno + " &ratetype=>" + ratetype
                    + " &orderno=>" + orderno);
            if (!"TAF".equalsIgnoreCase(ratetype)) {
                map = bkgVDS.getEcomRateCommodity(raterefno, orderno);
                if (map != null) {
                    lis = (List) map.get("ratecommodity");
                    if (lis != null && lis.size() > 0) {
                        for (int i = 0; i < lis.size(); i++) {
                            map = (Map) lis.get(i);
                            ratecmdtymap = new HashMap();
                            ratecmdtymap.put("commoditycode", checkNull((String) map.get("commodity")));
                            ratecmdtymap.put("commodityname", checkNull((String) map.get("commodityname")));
                            ratecmdtylist.add(ratecmdtymap);
                        }
                    }
                }
            } else {
                System.out.println("[ECOM] EcomShippingInstructionBean Tariff");
            }
            rateCmdtyJson = new Gson().toJson(ratecmdtylist);
            System.out.println("[ECOM] EcomShippingInstructionBean maskport=> " + rateCmdtyJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rateCmdtyJson;
    }

    @Override
    public ENSDeclarantVo getENSTabDetails(String sino) throws RemoteException {

        ENSDeclarantVo ensvo = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            System.out.println("[ECOM] EcomShippingInstructionBean inside getENSTabDetails");
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(SI_ENSDeclarant, 1004, 1007);
            ps.setString(1, sino);
            rs = ps.executeQuery();
            while (rs.next()) {
                ensvo = new ENSDeclarantVo();
                ensvo.setEuCustZone(rs.getString("eucustom"));
                ensvo.setBlTypeENS(rs.getString("bill_type"));
                ensvo.setEnsFillingType(rs.getString("ens_type"));
                ensvo.setPaymentMethod(rs.getString("payment_mode"));
                ensvo.setDeclarantName(rs.getString("cust_name"));
                ensvo.setDeclarantAddress(rs.getString("address"));
                ensvo.setDeclarantAddress1(rs.getString("address1"));
                ensvo.setDeclarantCity(rs.getString("city"));
                ensvo.setDeclarantCountry(rs.getString("country"));
                ensvo.setDeclarantState(rs.getString("state"));
                ensvo.setDeclarantTelephone(rs.getString("telephone"));
                ensvo.setDeclarantEORI(rs.getString("enseori"));
                ensvo.setDeclarantEmail(rs.getString("email"));
                ensvo.setDeclarantFillingType(rs.getString("filing_type"));
                ensvo.setDeclarantZipcode(rs.getString("zipcode"));

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
        return ensvo;
    }

    private boolean checkEUPort(String ports) {
        boolean iseuport = false;
        Map map = null;
        List lis = null;
        try {
            System.out.println("[ECOM] EcomShippingInstructionBean inside checkEUPort");
            map = siVDS.getEUPort(ports);
            if (map != null) {
                lis = (List) map.get("ports");
                if (lis != null && lis.size() > 0) {
                    for (int i = 0; i < lis.size(); i++) {
                        map = (Map) lis.get(i);
                        if (map != null && "Y".equalsIgnoreCase(checkNull((String) map.get("eur")))) {
                            iseuport = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iseuport;
    }

    public boolean checkIsInlandPort(String lrpbookno) {
        boolean isInlandPort = false;
        try {
            System.out.println("[ECOM] EcomShippingInstructionBean inside checkInLandPort" + lrpbookno);
            if (checkNull(lrpbookno).length() > 0) {
                CompleteModel siModelObj = siVDS.getecombookingdetails(lrpbookno);
                String dischargePortId = "", finalPortId = "", onCarriage = "";
                for (Booking bookingObj : siModelObj.getBooking()) {
                    onCarriage = checkNull(bookingObj.getOncarriage());
                    finalPortId = checkNull(bookingObj.getFinalportid());
                    dischargePortId = checkNull(bookingObj.getDischargeportid());
                }
                if (finalPortId.length() > 0 && dischargePortId.length() > 0) {
                    if ("C".equalsIgnoreCase(onCarriage) && !finalPortId.equalsIgnoreCase(dischargePortId)) {
                        isInlandPort = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EcomShippingInstructionBean Exception e " + e);
            e.printStackTrace();
        }
        return isInlandPort;
    }

    public HashMap<String, ArrayList<CustomerDetailsVO>> fetchSICusListFromEcom(String sino) throws RemoteException {
        HashMap<String, ArrayList<CustomerDetailsVO>> partiesHm = new HashMap<>();
        ArrayList<CustomerDetailsVO> byList = new ArrayList<>();
        ArrayList<CustomerDetailsVO> slList = new ArrayList<>();
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;

        try {
            System.out.println("[ECOM] EcomShippingInstructionBean === sino to select parties from ecom table ========>" + sino);
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOM_SI_CUST);
            ps.setString(1, sino);
            rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("[ECOM] EcomShippingInstructionBean --- inside result set ==== custTypeId is ====>" + rs.getString("custTypeId"));
                String custtype = checkNull(rs.getString("custTypeId"));
                if ("BY".equalsIgnoreCase(custtype) || "SL".equalsIgnoreCase(custtype)) {
                    CustomerDetailsVO custVO = new CustomerDetailsVO();
                    custVO.setName(rs.getString("name"));
                    custVO.setCustCode(rs.getString("custId"));
                    custVO.setAddress(rs.getString("address1"));
                    custVO.setAddress1(rs.getString("address2"));
                    custVO.setCity(rs.getString("city"));
                    custVO.setCountryCode(rs.getString("countryId"));
                    custVO.setCountry(rs.getString("countryId"));
                    custVO.setEmail(rs.getString("email"));
                    custVO.setPhoneNo(rs.getString("telephone"));
                    custVO.setFax(rs.getString("fax"));
                    custVO.setToorder(rs.getString("toorder"));
                    custVO.setNotifyCheck(rs.getString("flag"));
                    custVO.setState(rs.getString("stateid"));
                    custVO.setEroi(rs.getString("eori"));
                    custVO.setPerontype(rs.getString("persontype"));

                    String custType = checkNull(rs.getString("custTypeId"));  // Assuming you meant custTypeId here

                    if ("BY".equalsIgnoreCase(custType)) {
                        byList.add(custVO);
                    } else if ("SL".equalsIgnoreCase(custType)) {
                        slList.add(custVO);
                    }
                }
            }

            partiesHm.put("BY", byList);
            partiesHm.put("SL", slList);
            System.out.println("partiesHm==>" + partiesHm);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcomShippingInstructionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcomShippingInstructionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(EcomShippingInstructionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return partiesHm;
    }

    public HashMap<String, ArrayList<CustomerDetailsVO>> fetchSICustDetail(String bookno, String sino, String custref) throws RemoteException {
        HashMap<String, ArrayList<CustomerDetailsVO>> partiesHm = new HashMap<>();
        ArrayList<CustomerDetailsVO> buyerList = new ArrayList<>();
        ArrayList<CustomerDetailsVO> sellerList = new ArrayList<>();
        List<Map<String, String>> custlst = null;

        try {
            System.out.println("[ECOM] EcomShippingInstructionBean === sino to select parties from ecom table ========>" + sino);
            Map hs = (Map) bkgVDS.getecombookingcust(bookno);
            System.out.println("[ECOM] EcomShippingInstructionBean >>> booking cust for si >>>" + hs);

            if (hs != null) {
                custlst = (List<Map<String, String>>) hs.get("booking_cust");
            }

            if (custlst != null && !custlst.isEmpty()) {
                for (Map<String, String> rs : custlst) {
                    String custtype = Validation.checkNull(rs.get("custtypeid")).trim();

                    if ("BY".equalsIgnoreCase(custtype) || "SL".equalsIgnoreCase(custtype)) {
                        CustomerDetailsVO custVO = new CustomerDetailsVO();
                        custVO.setName(checkNull(rs.get("name")));
                        custVO.setCustCode(checkNull(rs.get("custid")));
                        custVO.setCustType(custtype);
                        custVO.setAddress(checkNull(rs.get("address1")));
                        custVO.setAddress1(checkNull(rs.get("address2")));
                        custVO.setCity(checkNull(rs.get("city")));
                        custVO.setCountryCode(checkNull(rs.get("countryid")));
                        custVO.setCountry(checkNull(rs.get("countryid")));

                        String email = checkNull(rs.get("email"));
                        if (email.equalsIgnoreCase("-")) {
                            email = "";
                        }
                        custVO.setEmail(email);

                        String phone = checkNull(rs.get("telephone"));
                        if (!phone.equals("--") && !phone.equals("-")) {
                            custVO.setPhoneNo(phone.replace("+", ""));
                        } else {
                            custVO.setPhoneNo("");
                        }

                        String fax = checkNull(rs.get("fax"));
                        if (!fax.equals("--") && !fax.equals("-")) {
                            custVO.setFax(fax.replace("+", ""));
                        } else {
                            custVO.setFax("");
                        }

                        custVO.setState(checkNull(rs.get("stateid")));
                        custVO.setZipcode(checkNull(rs.get("zipcode")));
                        custVO.setEroi(checkNull(rs.get("eori")));
                        custVO.setTaxId(checkNull(rs.get("vatno")));
                        custVO.setPerontype(checkNull(rs.get("persontype")));
                        // Group into buyer/seller lists
                        if ("BY".equalsIgnoreCase(custtype)) {
                            buyerList.add(custVO);
                        } else if ("SL".equalsIgnoreCase(custtype)) {
                            sellerList.add(custVO);
                        }
                    }
                }
            }

            partiesHm.put("BY", buyerList);
            partiesHm.put("SL", sellerList);

            System.out.println("[ECOM] EcomShippingInstructionBean >>> partiesHm map in EJB >>>>" + partiesHm);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return partiesHm;
    }

    private void saveCustDetails(Map custMap, String book_refno) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            System.out.println("book_refno ==> " + book_refno + " | custMap ==> " + custMap);

            if (custMap != null) {
                con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

                ps = con.prepareStatement(SAVE_SI_CUST_DET);

                // ========= Insert Seller List =========
                List<SISubmitVO> sellerList = (List<SISubmitVO>) custMap.get("Seller");
                if (sellerList != null && !sellerList.isEmpty()) {
                    System.out.println("Seller list size: " + sellerList.size());
                    for (SISubmitVO objSLVO : sellerList) {

                        ps.setString(1, book_refno);
                        ps.setString(2, "SL");
                        ps.setString(3, checkNull(objSLVO.getCustCode()));
                        ps.setString(4, extractCountryCode(checkNull(objSLVO.getCountryid())));
                        ps.setString(5, checkNull(objSLVO.getCustname()));
                        ps.setString(6, checkNull(objSLVO.getAddress()));
                        ps.setString(7, checkNull(objSLVO.getAddress1()));
                        ps.setString(8, checkNull(objSLVO.getCity()));
                        ps.setString(9, checkNull(objSLVO.getZipCode()));
                        ps.setString(10, checkNull(objSLVO.getTelephone()));
                        ps.setString(11, checkNull(objSLVO.getFax()));
                        ps.setString(12, checkNull(objSLVO.getEmail()));
                        ps.setString(13, null);
                        ps.setString(14, null);
                        ps.setString(15, checkNull(objSLVO.getState()));
                        ps.setString(16, checkNull(objSLVO.getEori()));
                        ps.setString(17, checkNull(objSLVO.getPersontype()));
                        ps.addBatch();
                    }

                }

                // ========= Insert Buyer List =========
                List<SISubmitVO> buyerList = (List<SISubmitVO>) custMap.get("Buyer");
                if (buyerList != null && !buyerList.isEmpty()) {
                    System.out.println("Buyer list size: " + buyerList.size());
                    for (SISubmitVO objBYO : buyerList) {

                        ps.setString(1, book_refno);
                        ps.setString(2, "BY");
                        ps.setString(3, checkNull(objBYO.getCustCode()));
                        ps.setString(4, extractCountryCode(checkNull(objBYO.getCountryid())));
                        ps.setString(5, checkNull(objBYO.getCustname()));
                        ps.setString(6, checkNull(objBYO.getAddress()));
                        ps.setString(7, checkNull(objBYO.getAddress1()));
                        ps.setString(8, checkNull(objBYO.getCity()));
                        ps.setString(9, checkNull(objBYO.getZipCode()));
                        ps.setString(10, checkNull(objBYO.getTelephone()));
                        ps.setString(11, checkNull(objBYO.getFax()));
                        ps.setString(12, checkNull(objBYO.getEmail()));
                        ps.setString(13, null);
                        ps.setString(14, null);
                        ps.setString(15, checkNull(objBYO.getState()));
                        ps.setString(16, checkNull(objBYO.getEori()));
                        ps.setString(17, checkNull(objBYO.getPersontype()));
                        ps.addBatch();
                    }
                }

                int[] result = ps.executeBatch();
                System.out.println("Batch executed. Insert counts: " + Arrays.toString(result));

            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                    System.out.println("Transaction rolled back due to error.");
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
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

    public String fetForSIENSFiling(String bookno) throws RemoteException {
        System.out.println("[ECOM] EcomShippingInstructionBean ===inside fetchPortDetForSI============" + bookno);
        String ensFiling = null;
        try {
            CompleteModel soModelObj = siVDS.getEcomPortDetForSI(bookno);
            for (Vesselmaster vesselMasterObj : soModelObj.getVesselmaster()) {
                ensFiling = vesselMasterObj.getEnsfiling();
                System.out.println("ensFiling==>" + ensFiling);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return ensFiling;
    }

    private String extractCountryCode(String input) {
        System.out.println("[Ecom] EcomSI extractCountryCode => " + input);
        if (input == null || input.isEmpty()) {
            return null;
        }

        if (input.contains(" - ")) {
            String[] parts = input.split(" - ", 2);
            if (parts.length == 2 && !parts[0].trim().isEmpty()) {
                return parts[0].trim();
            }
        }
        // Fallback: return the first 2 characters if no " - " exists
        return input.length() >= 2 ? input.substring(0, 2) : input;
    }
    @Override
    public ENSDeclarantVo getLRPBookingENSTabDetails(String lrpBookNo) throws RemoteException {
        ENSDeclarantVo ensvo = null;
        try {
            Map hs = (Map) bkgVDS.getecombookingicsdetails(lrpBookNo);
            System.out.println("[ECOM] EcomAmendmentBookingBean getLRPBookingENSTabDetails hs==>" + hs+lrpBookNo);

            List lst = (List) hs.get("booking_ics_new");
            if (lst != null && !lst.isEmpty()) {
                Map rs =(Map) lst.get(0);
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
