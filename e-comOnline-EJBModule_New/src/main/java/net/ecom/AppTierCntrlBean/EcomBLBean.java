/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.svm.json.SvmJsonList;
import com.svm.lrp.nfr.query.cbean.VDSInvoker;
import java.io.File;
import java.io.FileOutputStream;
import javax.ejb.Stateless;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import net.ecom.vo.OBLPrintVo;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.inject.Inject;
import net.ecom.common.DateConversion_New;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.Validation1;
import net.ecom.common.ZipFileConversion;
import net.ecom.si.vo.Bl;
import net.ecom.si.vo.Bl_cargo;
import net.ecom.si.vo.Bl_cust;
import net.ecom.si.vo.Bl_header;
import net.ecom.si.vo.Booking_cargo;
import net.ecom.si.vo.Booking_commodity;
import net.ecom.si.vo.Booking_cust;
import net.ecom.si.vo.CompleteModel;
import net.ecom.si.vo.Ports;
import net.ecom.si.vo.Service;
import net.ecom.si.vo.Vesselmaster;
import net.ecom.utils.JDBCServiceLocatorFactory;
import net.ecom.v1.cntlbean.EcomV1BLBeanRemote;
import net.ecom.vds.service.EcomBlVdsService;
import net.ecom.vds.service.EcomSiVdsService;
import net.ecom.vds.service.EcomBKGVdsService;
import net.ecom.vds.serviceVo.BlSummaryVo;
import net.ecom.vds.serviceVo.EcomBKGCommonModel;
import net.ecom.vds.serviceVo.EcomBKGCustomer;
import net.ecom.vds.serviceVo.EcomBKGEDIDocStatus;
import net.ecom.vds.serviceVo.EcomBKGHeader;
import net.ecom.vds.serviceVo.EcomBKGMasterVO;
import net.ecom.vds.serviceVo.EcomBKGReaderMaster;
import net.ecom.vds.serviceVo.EcomBKGReference;
import net.ecom.vds.serviceVo.EcomBKGRemarks;
import net.ecom.vds.serviceVo.EcomBKGRouting;
import net.ecom.vds.serviceVo.EcomBLReference;
import net.ecom.vds.serviceVo.EcomBookingCharges;
import net.ecom.vds.serviceVo.EcomCommonModel;
import net.ecom.vds.serviceVo.Editempbooking_ics_new;
import net.ecom.vds.serviceVo.NoticeVO;
import net.ecom.vds.serviceVo.VesselMaster;
import net.ecom.vds.serviceVo.BLChargeVo;
import net.ecom.vo.ChargesVo;
import net.ecom.vo.CustomerDetailsVO;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.FileDetailsVO;
import net.ecom.vo.MailFormatVO;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.SIBLMasterVO;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIContainerDetailVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;
import net.ecom.vo.SISealVO;
import net.ecom.vo.SISubmitVO;
import org.apache.commons.io.FileUtils;

@Stateless(name = "EcomBLBean")
@Remote(EcomBLBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
    ,
        @Resource(name = "jdbc/LRPDB", mappedName = "java:/MSSQLDS")
    ,
        @Resource(name = "jdbc/ECOMDB1", mappedName = "java:/ECOMDS")
    ,
        @Resource(name = "jdbc/MASTERDB", mappedName = "java:/MASTERDS")

})
public class EcomBLBean implements EcomBLBeanRemote {

    @Resource
    private SessionContext context;
    private static final String sign_query = "select SignatureFile from OBLSignature where agency=?";
    private static final String BOOK_NO_GEN = "select mdlid,mdlprefix,mdlindex from ecomMdlIndex where mdlid = ?";
    private static final String BOOK_NO_UPDATE = "update ecomMdlIndex set mdlindex=? "
            + "where mdlid =?";
    private static final String DRAFT_DG_INSERT = "insert into ecomSI_dg(eSIno,conId,containerno,itemno,"
            + "MarinePollutant,unNo,class,FlashPt,FlashPtUnit,imocontact,imophone) values(?,?,?,?,?,?,?,?,?,?,?)";
    private static final String DRAFT_OOG_INSERT = "insert into ecomSI_oog (eSIno,conId,comItemNo,"
            + "containerno, leftft, rightft, forward,Alt,topft,Unit)values(?,?,?,?,?,?,?,?,?,?)";
    private static final String DRAFT_Refer_INSERT = "insert into ecomSI_reefer(eSIno,conId,containerno,"
            + "ref_setTemp,ref_minTemp,ref_maxTemp,ref_tempUnit,humidity,humidity_flag,drains,co2value,o2value,ventpercentage)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

    // Insert into si table
    private static final String ECOM_SI_MASTER_INSERT = "insert into ecomSI(eSINo,blno,bookno,Vessel,Voyage,bound,pol,pod,pla,pld,"
            + "custcode,CrUser,CrDate,ChgUser,ChgDate,editbl,blversion,status,declined_remarks,"
            + "custremarks,plaHeaderName,polHeaderName,podHeaderName,pldHeaderName,appversion,ccmailid,shipper,shippername,consigee,consigeename,notify,notifyname,aesnumber,aesdiscliamer,releasetype,declaredvalue,blType,fcnno,"
            + "ACID,cargoprotectid,t2lfiling,packinglst,invoiceupload,telexrls,enstype,bltype_UAE,mpci_id,nvocc)"
            + " values(?,?,?,?,?,?,?,?,?,?,?,?,getDate(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String ECOM_SI_CUST_INSERT = "insert into ecomSI_cust(eSINo,custTypeId,custId, countryId, name, address1, "
            + "address2, city, zipCode, telephone, fax, email, flag,toorder,stateId,eori,persontype)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String ECOM_SI_CARGO_INSERT = "insert into ecomSI_cargo(eSINo, conId, eqpType, eqpSize, containerno, "
            + "shipperOwn, carrierSeal, ShipperSeal, tareWeight, cargoWeight, grossweight, "
            + "weightUnit, ref_nor,eqpstatus,dgflag,rate_eqpTypeid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String ECOM_SI_COMMDOITY_INSERT = "insert into ecomSI_commodity(eSINo,conId, containerno, itemno,commodity,package,"
            + " packageQty, itemWeight, itemWeightUnit, itemVolume, itemVolumeUnit,marksNo,"
            + " marksDesc,hscommodity,hazardous,nettwt,hssuffixcode,bycode,byname,slcode,slname,cmdty_hblno)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String GET_ECOM_SI_CARGO_NO = "select conId,containerno,ref_nor from ecomSI_cargo(nolock) where eSINo=? and "
            + "containerno=?";
    private static final String ECOM_SI_FILE_INSERT = "insert into ecomSI_files(eSINo,FileCatgeory,FileName) "
            + "values(?,?,?)";
    private static final String GET_ECOM_BL_VERSION = "select top 1 eSINO from ecomSI where blno = ? order by blversion desc";
    private static final String GET_ECOM_REMARKS = "select custremarks from ecomSI(nolock) where blno=? order by blversion desc";
    private static final String GET_ECOM_SI_CUST = "select custTypeid,name,custid,address1,address2,city,countryid,telephone,"
            + "fax,email, stateid,eori,persontype,zipcode from ecomSI_cust (nolock) where eSINO=?";
    private static final String GET_ECOM_SI_CARGO = "select eqpSize,containerno,carrierSeal,ShipperSeal,conId,eqpType,"
            + "shipperOwn,cargoWeight,tareWeight,grossweight,ref_nor,eqpstatus,dgflag,rate_eqpTypeid from ecomSI_cargo (nolock) "
            + "where  eSINO =?  order by conid asc";
    private static final String GET_ECOM_SI_COMDTY = "select commodity, packageQty, itemWeight, itemWeightUnit,package, "
            + "itemVolume, itemVolumeUnit, marksNo, marksDesc,hscommodity,hazardous,nettwt,hssuffixcode,itemno,bycode,byname,slcode,slname,cmdty_hblno from ecomSI_commodity  (nolock) where"
            + " eSINO = ? and containerno=?";
    private static final String V1_BL_SUMMARY_QRY = "exec usp_GetBLNumbersEcomOn ?";
    private static final String GETECOM_BOOKNO = "select ebookno,ccmailid From ecombooking where extbookno=?";
    private static final String SAVE_CCMAIN_USERDET = "update ecomuserdetails set ccmailid=? where emailid=?";
    private static final String GET_ECOM_SI_MASTERDET = "select custremarks,bookno,ccmailid,shipper,shippername,consigee,consigeename,notify,notifyname from  ecomSI where eSINo=?";
    private static final String GET_ECOMSI_REF_DET = "select eSIno,containerno,conId,ref_maxTemp,ref_setTemp,ref_minTemp,"
            + "ref_tempUnit,humidity,humidity_flag,drains,co2value,o2value,ventpercentage from ecomSI_reefer  where eSIno=? and containerno=?";
    private static final String DELETE_CUST = "delete from ecomSI_cust where eSINo=?";
    private static final String DELETE_COMMODITY = "delete from ecomSI_commodity where eSINo=?";
    private static final String DELETE_CARGO = "delete from ecomSI_cargo where eSINo=?";
    private static final String FILES_DELETE_QRY = "delete from ecomSI_files where eSINo=?";
    private static final String UPDATE_MASTER = "update ecomSI set status=?,custremarks=?,plaHeaderName=?,\n"
            + "polHeaderName=?,podHeaderName=?,pldHeaderName=?,Voyage=?,ccmailid=?,shipper=?,shippername=?,consigee=?,\n"
            + "consigeename=?,notify=?,notifyname=?,blversion=?,chguser=?,chgdate=getdate(),aesnumber=?,aesdiscliamer=?,releasetype=?,declaredvalue=?,blType=?,fcnno=? ,ACID=?,cargoprotectid=?,t2lfiling=?,packinglst=?,invoiceupload=?,telexrls=?,enstype=?,bltype_UAE=?,mpci_id=?,nvocc=? where eSINo=? ";
    private static final String DELETE_REEFER = "delete from ecomSI_reefer where eSINo=?";
    private static final String DELETE_OOG = "delete from ecomSI_oog where eSINo=?";
    private static final String DELETE_DG = "delete from ecomSI_dg where eSINo=?";

    private static final String INSERT_SI_CHARGES = "insert into ecomsi_charges (esino,chargeid,payagencyid,custtypeid,prepaidcollect,eqptypeid) values (?,?,?,?,?,?)";
    private static final String DELETE_SI_CHARGES = "delete from ecomsi_charges where esino=?";
    //
    private static final String INSERT_SI_ENSDEC = "INSERT INTO ecomsi_ics_new "
            + "(eSINo, eucustom, bill_type, ens_type, payment_mode, cust_name, "
            + "address, address1, country, state, telephone, email, enseori, filing_type, city,zipcode) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

    private static final String DELETE_SI_ENSDEC = "delete from ecomsi_ics_new where esino=?";

    private static final String GET_ECOM_SI_STATUS_BL = "select status From ecomsi where esino=?";
    private static final String UPDATE_ECOM_SI_STATUS_BL = "update ecomsi set status='I' where esino=?";

    @Inject
    VDSInvoker vds;

    EcomBKGVdsService ecomvdsservice;
    EcomBlVdsService blVDS;
    EcomSiVdsService siVDS;

    @EJB(mappedName = "java:global/e-comOnline-EJBModule_New/EcomV1BLBean!net.ecom.v1.cntlbean.EcomV1BLBeanRemote")
    EcomV1BLBeanRemote v1BLBeanObj;

    @EJB(mappedName = "java:global/e-comOnline-EJBModule_New/EComCntrlBean!net.ecom.AppTierCntrlBean.EComCntrlBeanRemote")
    EComCntrlBeanRemote cntrlBeanObj;

    @PostConstruct
    public void init() {
        try {
            ecomvdsservice = vds.create(EcomBKGVdsService.class);
            blVDS = vds.create(EcomBlVdsService.class);
            siVDS = vds.create(EcomSiVdsService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<OBLPrintVo> getBLSummary(String Custid, String custTypeConfig, String v1DataEnableStatus,
            String blbatchPrint) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs1 = null;
        String datestatus = "";
        List<OBLPrintVo> view = new ArrayList();
        OBLPrintVo eBooking = null;
        Map hs = null;
        List<BlSummaryVo> bl_lst = null;
        Gson gson = new Gson();
        String status = null, book_no = null;
        String blno = null;
        SvmJsonList alist = null;
        try {
            System.out.println("[ECOM] EcomBLBean ==Custid is=>" + Custid);
            SimpleDateFormat targer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date todate = DateConversion_New.convertaddDays(new Date(), 30);
            Date fromdate = DateConversion_New.convertsubDays(new Date(), 90);
            String to_date = targer.format(todate);
            String from_date = targer.format(fromdate);
            String date = from_date + "," + to_date;
            custTypeConfig = "AP,BP";
            //
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ArrayList lockedlis = new ArrayList();
            ps = con.prepareStatement("select bl_no from ecomsilock", 1004, 1007);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                lockedlis.add(checkNull(rs1.getString("bl_no")));
            }
            alist = new SvmJsonList();
            alist.add(Custid);
            alist.add(custTypeConfig);
            alist.add(blbatchPrint);

            hs = (Map) ecomvdsservice.getecomblsummarynew(alist);
            hs = (Map) hs.get("rs");
            if (hs != null && hs.size() > 0) {
                bl_lst = gson.fromJson(gson.toJson((ArrayList) hs.get("rs0")), new TypeToken<List<BlSummaryVo>>() {
                }.getType());
            }
            if (bl_lst.size() > 0) {
                for (int i = 0; i < bl_lst.size(); i++) {
                    BlSummaryVo rs = (BlSummaryVo) bl_lst.get(i);
                    eBooking = new OBLPrintVo();
                    eBooking.setBookNo(checkNull(rs.getBl_no()));
                    eBooking.setOriginPortId(checkNull(rs.getOriginportid()));
                    eBooking.setFinalPortId(checkNull(rs.getFinalportid()));
                    eBooking.setLoadPortID(checkNull(rs.getLoadportid()));
                    eBooking.setDischargePortId(checkNull(rs.getDischargeportid()));
                    eBooking.setCustid(Custid);
                    eBooking.setStatus(checkNull(rs.getStatus()));
                    eBooking.setPrintstatus(checkNull(rs.getEcomprntstatus()));
                    eBooking.setOriginals(checkNull(rs.getOriginals()));
                    eBooking.setShipmentstatus(checkNull(rs.getShipmentstatus()));
                    eBooking.setBook_no(checkNull(rs.getBook_no()));
                    eBooking.setCopies(checkNull(rs.getCopies()));
                    eBooking.setDatestatus(datestatus);
                    eBooking.setAgencyId(checkNull(rs.getAgencyissueid()));
                    eBooking.setAgencyRefNo(checkNull(rs.getAgencyrefno()));
                    eBooking.setEcomsino("");
                    eBooking.setDocumentalagencyid(checkNull(rs.getDocumentalagencyid()));
                    // System.out.println("[ECOM] EcomBLBean >>rs.getConfirmeddate()>>>" +
                    // rs.getConfirmeddate());
                    eBooking.setConfirmedDate(checkNull(rs.getConfirmeddate()));
                    eBooking.setAppVersion("2");
                    // System.out.println("[ECOM] EcomBLBean >>rs.getRefval()>>" + rs.getRefval());
                    eBooking.setBlAcceptByUserStatus(checkNull(rs.getRefval()));
                    eBooking.setReceiptshipdate(checkNull(rs.getReceiptshipdate()));
                    // System.out.println("[ECOM] EcomBLBean rs.getDatecr()" + rs.getDatecr());
                    eBooking.setCrdate(checkNull(rs.getDatecr()));
                    eBooking.setFcnverno(checkNull(rs.getFcnverno()));
                    eBooking.setFcnno(checkNull(rs.getFcnno()));
                    //
                    eBooking.setEcomsino(checkNull(rs.getEcomsino()));
                    //
                    if (checkNull(rs.getPolicy_no()).length() > 0) {
                        eBooking.setIsinsurance("Y");
                        eBooking.setPolicyno(checkNull(rs.getPolicy_no()));
                    }
                    eBooking.setPayAmountUsd(checkNull(rs.getPay_amount_usd()));
                    eBooking.setPayCustomerType(checkNull(rs.getPay_customer_type()));
                    eBooking.setPay_agency(checkNull(rs.getPayagency()));
                    eBooking.setPaymentEligible(checkNull(rs.getIs_payment_eligible()));
                    eBooking.setPaymentCompleted(checkNull(rs.getIs_payment_completed()));
                    eBooking.setPaymentRequestId(checkNull(rs.getPaymentrequestid()));
                    //
                    blno = rs.getBl_no();
                    if (lockedlis != null && lockedlis.contains(checkNull(blno))) {
                        eBooking.setIssilocked("Y");
                    }
                    /*
                     * hs = (Map) ecomvdsservice.getecomblsummaryfromeditemp(blno);
                     * List lst = (List) hs.get("editempbooking");
                     * if (lst.size() > 0) {
                     * Map bl = (Map) lst.get(0);
                     * status = (String) bl.get("status");
                     * book_no = (String) bl.get("book_no");
                     * if ("D".equalsIgnoreCase(eBooking.getStatus())) {
                     * eBooking.setStatus(status);
                     * eBooking.setEcomsino(book_no);
                     * }
                     * }
                     * ps = con.
                     * prepareStatement("select policy_no from ecom_insurance where policyStatus='Y' and lrpbl_no=?"
                     * , 1004, 1007);
                     * ps.setString(1, blno);
                     * rs1 = ps.executeQuery();
                     * if (rs1.next()) {
                     * eBooking.setIsinsurance("Y");
                     * eBooking.setPolicyno(checkNull(rs1.getString("policy_no")));
                     * }
                     */

                    view.add(eBooking);
                }
            }
            if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
                con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
                ps = con.prepareStatement(V1_BL_SUMMARY_QRY, 1004, 1007);
                ps.setString(1, Custid);
                rs1 = ps.executeQuery();
                while (rs1.next()) {
                    eBooking = new OBLPrintVo();
                    eBooking.setBookNo(checkNull(rs1.getString("blno")));
                    blno = checkNull(rs1.getString("blno"));
                    eBooking.setOriginPortId(checkNull(rs1.getString("pla")));
                    eBooking.setFinalPortId(checkNull(rs1.getString("pld")));
                    eBooking.setLoadPortID(checkNull(rs1.getString("pol")));
                    eBooking.setDischargePortId(checkNull(rs1.getString("pod")));
                    eBooking.setCustid(Custid);
                    eBooking.setStatus(checkNull(rs1.getString("blstatus")));
                    eBooking.setPrintstatus(checkNull(rs1.getString("ecomPrntStatus")));
                    eBooking.setOriginals(checkNull(rs1.getString("originals")));
                    eBooking.setShipmentstatus(checkNull(rs1.getString("shipmentStatus")));
                    eBooking.setBook_no(checkNull(rs1.getString("bookno")));
                    eBooking.setCopies(checkNull(rs1.getString("copies")));
                    eBooking.setDatestatus(datestatus);
                    eBooking.setAgencyId(checkNull(rs1.getString("AgencyIssueid")));
                    eBooking.setEcomsino(checkNull(rs1.getString("ecomsino")));
                    eBooking.setAgencyRefNo(checkNull(""));
                    eBooking.setPolAt(checkNull(rs1.getString("polat")));
                    eBooking.setAppVersion("1");
                    view.add(eBooking);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs1 != null) {
                    rs1.close();
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
        System.out.println("[ECOM] EcomBLBean =view=>>>" + view.size());
        return view;
    }

    @Override
    public String getBlSignatureStatus(String blno, String signature, String Shipmentstatus) {
        System.out.println("[ECOM] EcomBLBean ***getBlSignatureStatus for BL*" + signature);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Hashtable isLoadConfimed = new Hashtable();
        String result = "", agencysing = "";
        int mapsize = 0;
        try {
            con = new lrp.serverutils.refs.ServerObject("LRPDB").getConnection();
            ps = con.prepareStatement(sign_query, 1004, 1007);
            ps.setString(1, signature.trim());
            rs = ps.executeQuery();
            if (rs.next()) {
                agencysing = "Y";
            }
            if (Shipmentstatus.trim().equalsIgnoreCase("R")) {
                isLoadConfimed = new Hashtable();
            } else {
                isLoadConfimed = isLoadConfirmed(blno);
            }
            mapsize = isLoadConfimed.size();
            result = agencysing + "~~" + String.valueOf(mapsize);
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
    public HashMap fetchBookingDetForSI(String blno) {
        HashMap hm = new HashMap();
        String custtype = "", custname = "", address1 = "", address2 = "", city = "", telephone = "", fax = "",
                email = "", custCode = "";
        try {
            SIPartiesVO partiesVO = null;
            CompleteModel blModelobj = blVDS.getecomblcust(blno);
            for (Bl_cust blcustObj : blModelobj.getBl_cust()) {
                partiesVO = new SIPartiesVO();
                custtype = Validation.checkNull(blcustObj.getCusttypeid());
                custCode = Validation.checkNull(blcustObj.getCustid());
                custname = Validation.checkNull(blcustObj.getName());
                address1 = Validation.checkNull(blcustObj.getAddress1());
                address2 = Validation.checkNull(blcustObj.getAddress2());
                city = Validation.checkNull(blcustObj.getCity());
                telephone = Validation.checkNull(blcustObj.getTelephone()).replace("+", "");
                if ("--".equalsIgnoreCase(telephone)) {
                    telephone = "";
                }
                fax = Validation.checkNull(blcustObj.getFax()).replace("+", "");
                if ("--".equalsIgnoreCase(fax)) {
                    fax = "";
                }
                email = Validation.checkNull(blcustObj.getEmail());
                if (email.length() > 0 && email.equalsIgnoreCase("-")) {
                    email = "";
                }
                partiesVO.setCustname(custname);
                partiesVO.setCustCode(custCode);
                partiesVO.setAddress1(address1);
                partiesVO.setAddress2(address2);
                partiesVO.setCity(city);
                partiesVO.setTelephone(telephone);
                partiesVO.setFax(fax);
                partiesVO.setEmail(email);
                partiesVO.setState(Validation.checkNull(blcustObj.getStateid()));
                partiesVO.setZipcode(Validation.checkNull(blcustObj.getZipcode()));
                partiesVO.setEori(Validation.checkNull(blcustObj.getEori()));
                partiesVO.setCountryid(Validation.checkNull(blcustObj.getCountryid()));
                partiesVO.setPersontype(Validation.checkNull(blcustObj.getPersontype()));
                hm.put(custtype, partiesVO);
            }
            System.out.println("[ECOM] EcomBLBean ===Fetching customer details from bl_cust table is done====");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hm;
    }

    public Hashtable isLoadConfirmed(String bookno) throws RemoteException {
        Connection con = null;
        Hashtable loadConfirmed = new Hashtable();
        PreparedStatement ptLoadConfirmed = null;
        ResultSet rsLoadConfirmed = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("LRPDB").getConnection();
            ptLoadConfirmed = con.prepareStatement("exec USP_ISLOADCONFIRMED ?,?,?,? ", 1004, 1007);
            ptLoadConfirmed.setString(1, bookno);
            ptLoadConfirmed.setString(2, "N");
            ptLoadConfirmed.setString(3, "N");
            ptLoadConfirmed.setString(4, "N");
            rsLoadConfirmed = ptLoadConfirmed.executeQuery();
            rsLoadConfirmed.last();
            rsLoadConfirmed.beforeFirst();
            while (rsLoadConfirmed.next()) {
                loadConfirmed.put(rsLoadConfirmed.getString(1), rsLoadConfirmed.getString(2));
            }
        } catch (Exception e) {
            System.out.println("[ECOM] EcomBLBean Exception at checking the LoadConfimrd" + e);
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                System.out.println("[ECOM] EcomBLBean Exception at closing the Connection" + e);
            }
        }
        return loadConfirmed;
    }

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str)) && !"null".equalsIgnoreCase(str)) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private String resolveNvoccForDb(String nvocc) throws RemoteException {
        nvocc = checkNull(nvocc);
        if (nvocc.length() == 0) {
            return null;
        }
        return "Y".equalsIgnoreCase(nvocc) ? "Y" : "N";
    }

    @Override
    public HashMap fetchSICustomerDetails(String bookno, String sino) throws RemoteException {
        HashMap hm = new HashMap();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String custtype;
        SIBLMasterVO masterVO = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOM_SI_CUST);
            ps.setString(1, checkNull(sino));
            rs = ps.executeQuery();
            SIPartiesVO partiesVO;
            while (rs.next()) {
                partiesVO = new SIPartiesVO();
                custtype = checkNull(rs.getString("custtypeid"));
                partiesVO.setCusttype(custtype);
                partiesVO.setCustname(checkNull(rs.getString("name")));
                partiesVO.setCustCode(checkNull(rs.getString("custid")));
                partiesVO.setAddress1(checkNull(rs.getString("address1")));
                partiesVO.setAddress2(checkNull(rs.getString("address2")));
                partiesVO.setCity(checkNull(rs.getString("city")));
                partiesVO.setCountryid(checkNull(rs.getString("countryid")));
                partiesVO.setTelephone(checkNull(rs.getString("telephone")));
                partiesVO.setFax(checkNull(rs.getString("fax")));
                partiesVO.setEmail(checkNull(rs.getString("email")));
                partiesVO.setState(checkNull(rs.getString("stateid")));
                partiesVO.setEori(checkNull(rs.getString("eori")));
                partiesVO.setPersontype(checkNull(rs.getString("persontype")));
                partiesVO.setZipcode(checkNull(rs.getString("zipcode")));   
                // partiesVO.setTaxId(checkNull(rs.getString("vatno")));
                hm.put(custtype, partiesVO);
            }
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOM_SI_MASTERDET);
            ps.setString(1, checkNull(sino));
            rs = ps.executeQuery();
            if (rs.next()) {
                masterVO = new SIBLMasterVO();
                masterVO.setShipperName(checkNull(rs.getString("shippername")));
                masterVO.setShipperHeader(checkNull(rs.getString("shipper")));
                masterVO.setConsigneeName(checkNull(rs.getString("consigeename")));
                masterVO.setConsigneeHeader(checkNull(rs.getString("consigee")));
                masterVO.setNotifyName(checkNull(rs.getString("notifyname")));
                masterVO.setNotifyHeader(checkNull(rs.getString("notify")));
            }
            hm.put("MASTERDETAILS", masterVO);
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
        return hm;
    }

    @Override
    public Map<SIMasterVO, SIConatainerValueVO> fetchSIContainerDetailForDraft(String blno) throws RemoteException {
        Map<SIMasterVO, SIConatainerValueVO> siCntHashObj = new LinkedHashMap<SIMasterVO, SIConatainerValueVO>();

        String volume = null, netweight = null;
        SIMasterVO siCntMasterVoObj = new SIMasterVO();
        SIConatainerValueVO siCntValVO = new SIConatainerValueVO();
        SISealVO siSealVO = new SISealVO();
        ArrayList<SIContainerDetailVO> cntDetailObj = new ArrayList<SIContainerDetailVO>();
        SIContainerDetailVO siCntDetailsVoObj = new SIContainerDetailVO();

        int i = 0;
        String strCntNo = "";
        System.out.println("[ECOM] EcomBLBean   book no.." + blno);
        String soc = "";
        String prevCntr = null, currentCntr = null;
        int contDup = 0;
        boolean dupCont = false;
        DecimalFormat df = new DecimalFormat("#.###");
        Map insmap = null;
        String qouteno = null;
        try {
            insmap = getInsuranceDetils(blno);
            CompleteModel siModelCargoObj = blVDS.getecomblcargodetails(blno);
            System.out.println("[ECOM] EcomBLBean >>>inner join cargo >>>" + siModelCargoObj);
            for (Bl_cargo bkngCargoObj : siModelCargoObj.getBl_cargo()) {
                if (contDup == 0) {
                    prevCntr = Validation.checkNull(bkngCargoObj.getEqpid());
                    currentCntr = Validation.checkNull(bkngCargoObj.getEqpid());
                }
                System.out.println("[ECOM] EcomBLBean >>>inner join venti>>>" + bkngCargoObj.getVentpercentage());
                currentCntr = Validation.checkNull(bkngCargoObj.getEqpid());
                dupCont = prevCntr.equalsIgnoreCase(currentCntr) ? false : true;
                if (contDup == 0 || dupCont) {
                    System.out.println("[ECOM] EcomBLBean >>>enter count" + contDup);
                    prevCntr = currentCntr;
                    siCntMasterVoObj = new SIMasterVO();
                    siSealVO = new SISealVO();
                    siCntValVO = new SIConatainerValueVO();
                    strCntNo = Validation.checkNull(bkngCargoObj.getEqpid());
                    siCntMasterVoObj.setStrType(bkngCargoObj.getEqptypeid());
                    siCntMasterVoObj.setRateeqptypeid(bkngCargoObj.getRate_eqpTypeid());
                    siCntMasterVoObj.setStrEqpType(bkngCargoObj.getType());
                    siCntMasterVoObj.setStrCntNo(strCntNo);
                    soc = Validation.checkNull(bkngCargoObj.getShipperown());
                    if (soc != null && "Y".equalsIgnoreCase(soc)) {
                        siCntMasterVoObj.setStrsoc("Y");
                    } else {
                        siCntMasterVoObj.setStrsoc("N");
                    }
                    siCntMasterVoObj.setCargoNo(bkngCargoObj.getCargono() + "");
                    siCntMasterVoObj
                            .setCargoweight(ConvertDecimalFormat(String.valueOf(bkngCargoObj.getCargoweight())));
                    siCntMasterVoObj.setTareweight(ConvertDecimalFormat(String.valueOf(bkngCargoObj.getTareweight())));
                    siCntMasterVoObj
                            .setGrossweight(ConvertDecimalFormat(String.valueOf(bkngCargoObj.getGrossweight())));
                    siCntMasterVoObj.setTempMin(checkNull(bkngCargoObj.getTempmin()));
                    siCntMasterVoObj.setTempMax(checkNull(bkngCargoObj.getTempmax()));
                    siCntMasterVoObj.setSetTemp(checkNull(bkngCargoObj.getSettemp()));
                    siCntMasterVoObj.setTempunit(bkngCargoObj.getTempunit() + "");
                    siCntMasterVoObj.setEqpStatus(checkNull(bkngCargoObj.getEqpstatus()));
                    siCntMasterVoObj.setDgFlag(checkNull(bkngCargoObj.getDgflag()));

                    siCntMasterVoObj.setHumidity(checkNull(bkngCargoObj.getHumidity()));
                    siCntMasterVoObj.setHumidity_flag(checkNull(bkngCargoObj.getHumidity_flag()));
                    siCntMasterVoObj.setDrains(checkNull(bkngCargoObj.getDrains()));
                    siCntMasterVoObj.setCo2value(checkNull(bkngCargoObj.getCo2value()));
                    siCntMasterVoObj.setO2value(checkNull(bkngCargoObj.getO2value()));
                    siCntMasterVoObj.setVentilation(checkNull(bkngCargoObj.getVentpercentage()));
                    siCntMasterVoObj.setOog(bkngCargoObj.getOog());
                    siCntMasterVoObj.setNor(bkngCargoObj.getNor());
                    if (siCntMasterVoObj.getSetTemp() != null && siCntMasterVoObj.getSetTemp().length() > 0) {
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
                siCntDetailsVoObj.setStrQty(Validation.checkNull(bkngCargoObj.getItemqty() + ""));
                siCntDetailsVoObj.setHssuffixcode(bkngCargoObj.getHssuffixcode());
                String wightcargo = (bkngCargoObj.getItemweight() + "");
                Double wtn = Double.parseDouble(wightcargo);
                String wightcargowt = df.format(wtn);
                siCntDetailsVoObj.setStrWgt(ConvertDecimalFormat(wightcargowt));
                siCntDetailsVoObj.setStrUnit(Validation.checkNull(bkngCargoObj.getItemweightunit()));
                siCntDetailsVoObj.setStrPkg(Validation.checkNull(bkngCargoObj.getPackageid()));
                volume = bkngCargoObj.getItemvolume() + "";
                volume = checkNull(String.valueOf(bkngCargoObj.getItemvolume())).length() > 0
                        ? checkNull(String.valueOf(bkngCargoObj.getItemvolume()))
                        : "0";
                volume = String.valueOf(new BigDecimal(volume).toPlainString());
                System.out.println("[ECOM] EcomBLBean >>>volume>>" + volume + "Float.parseFloat(volume)>>"
                        + Float.parseFloat(volume));
                String volumeDouble = df.format(Float.parseFloat(volume));
                if (Float.parseFloat(volume) <= 0) {
                    volumeDouble = "";
                }
                siCntDetailsVoObj.setStrVol(volumeDouble);
                // netweight
                netweight = bkngCargoObj.getNettwt() + "";
                netweight = checkNull(String.valueOf(bkngCargoObj.getNettwt())).length() > 0
                        ? checkNull(String.valueOf(bkngCargoObj.getNettwt()))
                        : "0";
                netweight = String.valueOf(new BigDecimal(netweight).toPlainString());
                System.out.println("[ECOM] EcomBLBean >>>netweight>>" + netweight + "Float.parseFloat(netweight)>>"
                        + Float.parseFloat(netweight));
                String netWeightDouble = df.format(Float.parseFloat(netweight));
                if (Float.parseFloat(netweight) <= 0) {
                    netWeightDouble = "";
                }

                siCntDetailsVoObj.setNettwt(netWeightDouble);
                siCntDetailsVoObj.setStrVut(Validation.checkNull(bkngCargoObj.getItemvolumeunit()));
                siCntDetailsVoObj.setStrMrk(Validation.checkNull(bkngCargoObj.getMarksnumbers()));
                siCntDetailsVoObj.setStrDes(Validation.checkNull(bkngCargoObj.getMarksdesc()));
                siCntDetailsVoObj.setByscode(Validation.checkNull(bkngCargoObj.getBycode()));
                siCntDetailsVoObj.setBysname(Validation.checkNull(bkngCargoObj.getByname()));
                siCntDetailsVoObj.setSlscode(Validation.checkNull(bkngCargoObj.getSlcode()));
                siCntDetailsVoObj.setSlsname(Validation.checkNull(bkngCargoObj.getSlname()));
                siCntDetailsVoObj.setCmdty_hblno(Validation.checkNull(bkngCargoObj.getCmdty_hblno()));
                //
                siCntDetailsVoObj.setImophone(bkngCargoObj.getImophone());
                siCntDetailsVoObj.setImocontact(bkngCargoObj.getImocontact());
                siCntDetailsVoObj.setItemforward((bkngCargoObj.getItemforward() + ""));
                siCntDetailsVoObj.setItemtop((bkngCargoObj.getItemtop() + ""));
                siCntDetailsVoObj.setItemright((bkngCargoObj.getItemright() + ""));
                siCntDetailsVoObj.setItemaft((bkngCargoObj.getItemaft() + ""));
                siCntDetailsVoObj.setHazardous(bkngCargoObj.getHazardous());
                siCntDetailsVoObj.setItemlengthUnit(bkngCargoObj.getItemlengthunit());
                siCntDetailsVoObj.setItemleft((bkngCargoObj.getItemleft() + ""));
                //
                // String strPattern = "^0+";
                System.out.println("[ECOM] EcomBLBean ----> " + siCntDetailsVoObj.getStrCommodity()
                        + " ----> " + (insmap.containsKey((siCntDetailsVoObj.getStrCommodity()))));
                if (insmap != null) {
                    siCntDetailsVoObj.setInsuredemail(checkNull((String) insmap.get("insuredemail")));
                    if ((insmap.containsKey((siCntDetailsVoObj.getStrCommodity()))
                            || insmap.containsKey((siCntDetailsVoObj.getHsCode())))) {// .replaceAll(strPattern, "")
                        qouteno = checkNull((String) insmap.get((siCntDetailsVoObj.getStrCommodity())));
                        if (qouteno.length() == 0) {
                            qouteno = checkNull((String) insmap.get((siCntDetailsVoObj.getHsCode())));
                        }
                        System.out.println("[ECOM] EcomBLBean qoute==> " + qouteno);
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
            System.out.println(
                    "[ECOM] EcomBLBean ===Fetching container and commodity details from bl_cargo and bl_commodity table is done====");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return siCntHashObj;
    }

    @Override
    public Map<SIMasterVO, SIConatainerValueVO> fetchSIContainerDetail(String blno, String sino)
            throws RemoteException {

        Map siCntHashObj = new LinkedHashMap();
        Connection con_cargo = null;
        PreparedStatement ps_cargo = null, ps_cargo1 = null;
        ResultSet rs_cargo = null;
        PreparedStatement ps_com = null;
        ResultSet rs_com = null, rs_cargo1 = null;
        PreparedStatement ps_dgcom = null;
        ResultSet rs_dg = null;
        PreparedStatement ps_oogcom = null;
        ResultSet rs_oog = null;
        String eqpid = "";
        String weight = null, volume = null, netweight = null;
        SIMasterVO siCntMasterVoObj = new SIMasterVO();
        SIConatainerValueVO siCntValVO = new SIConatainerValueVO();
        SISealVO siSealVO = new SISealVO();
        ArrayList<SIContainerDetailVO> cntDetailObj = new ArrayList<SIContainerDetailVO>();
        SIContainerDetailVO siCntDetailsVoObj = new SIContainerDetailVO();
        int i = 0;
        String strCntNo = "";
        String strcargono = "";
        String soc = "";
        String conId = "", comconId = "";
        Map insmap = null;
        String qouteno = null;
        try {
            insmap = getInsuranceDetils(blno);
            con_cargo = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps_cargo = con_cargo.prepareStatement(GET_ECOM_SI_CARGO);
            ps_cargo.setString(1, checkNull(sino));
            rs_cargo = ps_cargo.executeQuery();
            while (rs_cargo.next()) {
                siCntMasterVoObj = new SIMasterVO();
                siSealVO = new SISealVO();
                siCntValVO = new SIConatainerValueVO();
                strCntNo = checkNull(rs_cargo.getString("containerno"));
                siCntMasterVoObj.setStrType(checkNull(rs_cargo.getString("eqpSize")));
                siCntMasterVoObj.setStrEqpType(checkNull(rs_cargo.getString("eqpType")));
                siCntMasterVoObj.setRateeqptypeid(rs_cargo.getString("rate_eqpTypeid"));
                strcargono = checkNull(rs_cargo.getString("conId"));
                conId = checkNull(rs_cargo.getString("conId"));
                siCntMasterVoObj.setStrCntNo(strCntNo);
                siCntMasterVoObj.setStrSelect(i);
                siSealVO.setStrShipperSeal(checkNull(rs_cargo.getString("ShipperSeal")));
                siCntMasterVoObj.setEqpStatus(rs_cargo.getString("eqpstatus"));
                siSealVO.setStrCarrierSeal(checkNull(rs_cargo.getString("carrierSeal")));
                soc = checkNull(rs_cargo.getString("shipperOwn"));
                if (soc != null && "Y".equalsIgnoreCase(soc)) {
                    siCntMasterVoObj.setStrsoc("Y");
                } else {
                    siCntMasterVoObj.setStrsoc("N");
                }
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
                System.out.println("[ECOM] EcomBLBean sino:::" + sino + " eqpid " + eqpid);
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
                siSealVO.setStrCargoid(strcargono);
                int j = 0;
                cntDetailObj = new ArrayList<SIContainerDetailVO>();
                if (strcargono.length() > 0) {
                    comconId = "";
                    ps_com = con_cargo.prepareStatement(GET_ECOM_SI_COMDTY);
                    ps_com.setString(1, sino);
                    ps_com.setString(2, strCntNo);
                    rs_com = ps_com.executeQuery();
                    while (rs_com.next()) {
                        siCntDetailsVoObj = new SIContainerDetailVO();
                        siCntDetailsVoObj.setStrCommodity(checkNull(rs_com.getString("commodity")));
                        siCntDetailsVoObj.setHsCode(checkNull(rs_com.getString("hscommodity")));
                        siCntDetailsVoObj.setStrQty(checkNull(rs_com.getString("packageQty")));
                        siCntDetailsVoObj.setHssuffixcode(rs_com.getString("hssuffixcode"));
                        weight = rs_com.getString("itemWeight") != null && !rs_com.getString("itemWeight").isEmpty()
                                ? rs_com.getString("itemWeight")
                                : "0.0";
                        siCntDetailsVoObj.setStrWgt(ConvertDecimalFormat(
                                checkNull(String.valueOf(new BigDecimal(weight).toPlainString()))));
                        siCntDetailsVoObj.setStrUnit(checkNull(rs_com.getString("itemweightUnit")));
                        siCntDetailsVoObj.setStrCid("");
                        siCntDetailsVoObj.setStrPkg(checkNull(rs_com.getString("package")));
                        volume = rs_com.getString("itemVolume") != null && !rs_com.getString("itemVolume").isEmpty()
                                ? rs_com.getString("itemVolume")
                                : "0.0";
                        siCntDetailsVoObj.setStrVol(checkNull(String.valueOf(new BigDecimal(volume).toPlainString())));
                        siCntDetailsVoObj.setStrVut(checkNull(rs_com.getString("itemVolumeUnit")));
                        siCntDetailsVoObj.setStrMrk(checkNull(rs_com.getString("marksNo")));
                        siCntDetailsVoObj.setStrDes(checkNull(rs_com.getString("marksDesc")));
                        siCntDetailsVoObj.setHazardous(checkNull(rs_com.getString("hazardous")));
                        netweight = rs_com.getString("nettwt") != null && !rs_com.getString("nettwt").isEmpty()
                                ? rs_com.getString("nettwt")
                                : "0.0";
                        siCntDetailsVoObj
                                .setNettwt(checkNull(String.valueOf(new BigDecimal(netweight).toPlainString())));
                        System.out.println("[ECOM] EcomBLBean netweightvalue" + netweight);
                        siCntDetailsVoObj.setByscode(checkNull(rs_com.getString("bycode")));
                        siCntDetailsVoObj.setBysname(checkNull(rs_com.getString("byname")));
                        siCntDetailsVoObj.setSlscode(checkNull(rs_com.getString("slcode")));
                        siCntDetailsVoObj.setSlsname(checkNull(rs_com.getString("slname")));
                        siCntDetailsVoObj.setCmdty_hblno(checkNull(rs_com.getString("cmdty_hblno")));
                        comconId = checkNull(rs_com.getString("itemno"));
                        //
                        // String strPattern = "^0+";
                        System.out.println("[ECOM] EcomBLBean ---->" + siCntDetailsVoObj.getStrCommodity()
                                + " ----> " + (insmap.containsKey((siCntDetailsVoObj.getStrCommodity()))));
                        if (insmap != null) {
                            siCntDetailsVoObj.setInsuredemail(checkNull((String) insmap.get("insuredemail")));
                            if ((insmap.containsKey((siCntDetailsVoObj.getStrCommodity()))
                                    || insmap.containsKey((siCntDetailsVoObj.getHsCode())))) {// .replaceAll(strPattern,
                                // "")
                                qouteno = checkNull((String) insmap.get((siCntDetailsVoObj.getStrCommodity())));
                                if (qouteno.length() == 0) {
                                    qouteno = checkNull((String) insmap.get((siCntDetailsVoObj.getHsCode())));
                                }
                                System.out.println("[ECOM] EcomBLBean qoute==> " + qouteno);
                                siCntDetailsVoObj.setIsinsuranceavailable("Y");
                                siCntDetailsVoObj.setQuoteno(qouteno);
                            }
                        }
                        //
                        // ----------------------START FROM HERE----------------------------------
                        System.out.println("[ECOM] EcomBLBean conId= " + conId + " sino= " + sino);
                        String DRAFT_DG_SELECT = "select eSIno,containerno,conId,imocontact,imophone "
                                + "from ecomSI_dg where eSIno=? and conId=?";
                        ps_dgcom = con_cargo.prepareStatement(DRAFT_DG_SELECT);
                        ps_dgcom.setString(1, sino);
                        ps_dgcom.setString(2, conId);
                        rs_dg = ps_dgcom.executeQuery();
                        while (rs_dg.next()) {
                            System.out.println("[ECOM] EcomBLBean rs_dg.getString('imocontact')===> "
                                    + rs_dg.getString("imocontact"));
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
                            System.out.println("[ECOM] EcomBLBean >>rs_oog.getString(\"forward\")>>>"
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
            System.out.println("[ECOM] EcomBLBean Exception While Calling  fetchSIContainerDetail->" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs_cargo != null) {
                    rs_cargo.close();
                }
                if (ps_cargo != null) {
                    ps_cargo.close();
                }
                if (rs_com != null) {
                    rs_com.close();
                }
                if (ps_com != null) {
                    ps_com.close();
                }
                if (ps_cargo1 != null) {
                    ps_cargo1.close();
                }
                if (rs_cargo1 != null) {
                    rs_cargo1.close();
                }
                if (con_cargo != null) {
                    con_cargo.close();
                }
            } catch (Exception e) {
                System.out.println("[ECOM] EcomBLBean Exception While Closing connetion in  fetchSIContainerDetail->"
                        + e.getMessage());
                e.printStackTrace();
            }
        }
        return siCntHashObj;
    }

    @Override
    public ReturnVO saveToBL(String bookno, String custcode, String bookinguserid,
            Map<SIMasterVO, SIConatainerValueVO> cntMapObj, HashMap hm, SIPartiesVO partiesVO, String savetype,
            String sino, ArrayList voidCargoObj, ArrayList voidComObj, String username1, String tbl_no,
            String client_code, FileDetailsVO fileDetailsVO, String addremarks, String appVersion, String ccMailId,
            String isCCMail, Map custMap) throws RemoteException {
        System.out.println("[ECOM] EcomBLBean  while saving BL is tbl_no i ==>" + tbl_no + " custcode ==>" + custcode
                + " bookno=>" + bookno + " sino==>" + sino);
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
        StringBuffer hssb = new StringBuffer();
        String agencyid = "", oceansercode = "", serviceCode = "", polTerminal = "", podTerminal = "";
        String oceanvslcode = null;
        String oceanvoyage = null;
        String oceanBound = null;
        String bookDate = "";
        String bookReady = "";
        String preCarriage = "", onCarriage = "";
        String transportserviceid = "", callid = "", firstsvctype = "", shipment_type = "", crossbook = "";
        String originportid = "", finalPortId = "";
        String strDelivery = "", ediid = "";
        String msgtype = "";
        String packge = "", packgeId = "", volume = "", volumeUnt = "", netweight = "", marks = "", description = "",
                rateeqptypeid = "", bycode = "", byname = "", slcode = "", slname = "", cmdty_hblno = "";
        savetype = checkNull(savetype);
        sino = checkNull(sino);
        String strSOC = "", documentalAgencyid = "";
        String originportdesc = "", finalportdesc = "";
        String retFilePath = "";
        boolean newblsubmit = false;
        try {

            ecomConn = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            System.out.println("[ECOM] EcomBLBean savetype is===>" + savetype + " sino is =>" + sino);
            String str_sino = null;
            if (sino.length() > 0) {
                str_sino = sino;
            } else {
                str_sino = bookNoGenerate("SI");
                newblsubmit = true;
            }
            bknum = str_sino;
            returnVO.setStrSaveType("D");
            // update in userdetails if cc mail id newly added
            if ("YES".equalsIgnoreCase(isCCMail)) {
                ps = ecomConn.prepareStatement(SAVE_CCMAIN_USERDET);
                ps.setString(1, checkNull(ccMailId));
                ps.setString(2, checkNull(username1));
                ps.executeUpdate();
            }
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
            int editbl = 1;
            int blVersion = 1;
            String blVersionQry = "select top 1 blversion from ecomSI where blno = ? and editbl=1 order by blversion desc";
            ps = ecomConn.prepareStatement(blVersionQry);
            ps.setString(1, tbl_no);
            rs = ps.executeQuery();
            if (rs.next()) {
                blVersion = rs.getInt("blversion") + 1;
            }
            if (sino.length() > 0) {
                ps = ecomConn.prepareStatement(UPDATE_MASTER);
                ps.setString(1, "S");
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
                ps.setInt(15, blVersion);
                ps.setString(16, checkNull(username1));
                ps.setString(17, partiesVO.getAESNumber());
                ps.setString(18, partiesVO.getAESDiscliamer());
                ps.setString(19, partiesVO.getReleasetype());
                ps.setString(20, partiesVO.getDeclaredValue());
                ps.setString(21, partiesVO.getBltype());
                ps.setString(22, checkNull(partiesVO.getFcnno()));
                ps.setString(23, checkNull(partiesVO.getACID()));
                ps.setString(24, checkNull(partiesVO.getCargoprotectid()));
                ps.setString(25, checkNull(partiesVO.getEbook_t2lfiling()));
                ps.setString(26, checkNull(partiesVO.getPackinglst()));
                ps.setString(27, checkNull(partiesVO.getInvoiceupload()));
                ps.setString(28, checkNull(partiesVO.getOrgTelType()));
                ps.setString(29, checkNull(partiesVO.getEbook_EnsDocType()));
                ps.setString(30, checkNull(partiesVO.getBltype_UAE()));
                ps.setString(31, checkNull(partiesVO.getMpci_id()));
                ps.setString(32, resolveNvoccForDb(partiesVO.getEbook_nvocc()));
                ps.setString(33, sino);
                ps.executeUpdate();

            } else {
                System.out.println("[ECOM] EcomBLBean >>>blVersion>>>" + blVersion);
                ps = ecomConn.prepareStatement(ECOM_SI_MASTER_INSERT);
                ps.setString(1, str_sino);
                ps.setString(2, tbl_no);
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
                ps.setString(17, "");
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
                ps.setString(32, partiesVO.getAESNumber());
                ps.setString(33, partiesVO.getAESDiscliamer());
                ps.setString(34, partiesVO.getReleasetype());
                ps.setString(35, partiesVO.getDeclaredValue());
                ps.setString(36, partiesVO.getBltype());
                ps.setString(37, checkNull(partiesVO.getFcnno()));
                ps.setString(38, checkNull(partiesVO.getACID()));
                ps.setString(39, checkNull(partiesVO.getCargoprotectid()));
                ps.setString(40, checkNull(partiesVO.getEbook_t2lfiling()));
                ps.setString(41, checkNull(partiesVO.getPackinglst()));
                ps.setString(42, checkNull(partiesVO.getInvoiceupload()));
                ps.setString(43, checkNull(partiesVO.getOrgTelType()));
                ps.setString(44, checkNull(partiesVO.getEbook_EnsDocType()));
                ps.setString(45, checkNull(partiesVO.getBltype_UAE()));
                ps.setString(46, checkNull(partiesVO.getMpci_id()));
                ps.setString(47, resolveNvoccForDb(partiesVO.getEbook_nvocc()));
                System.out.println("[ECOM] EcomBLBean CLEAR_MPCI_FOR_DIRECTBL is Y");
                ps.executeUpdate();
            }
            System.out.println("[ECOM] EcomBLBean bknum==>" + bknum);
            ps = ecomConn.prepareStatement(DELETE_CUST);
            ps.setString(1, bknum);
            ps.executeUpdate();
            SISubmitVO objSH = (SISubmitVO) hm.get("SH");

            ps = ecomConn.prepareStatement(ECOM_SI_CUST_INSERT);
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
            ps = ecomConn.prepareStatement(ECOM_SI_CUST_INSERT);
            SISubmitVO objCN = (SISubmitVO) hm.get("CN");
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

            // notify
            SISubmitVO objNT = (SISubmitVO) hm.get("NT");
            if (objNT.getCustname() != null && objNT.getCustname().length() > 0
                    && checkNull(partiesVO.getNotifyName()).length() > 0) {
                ps = ecomConn.prepareStatement(ECOM_SI_CUST_INSERT);
                ps.setString(1, str_sino);
                ps.setString(2, "NT");
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
                System.out.println(
                        "[ECOM] EcomBLBean ==>" + checkNull(objNT.getEori()) + "//" + checkNull(objNT.getPersontype()));
                ps.setString(16, checkNull(objNT.getEori()));
                ps.setString(17, checkNull(objNT.getPersontype()));
                ps.executeUpdate();
            }

            // Notify2
            SISubmitVO objNT2 = (SISubmitVO) hm.get("N1");
            if (objNT2.getCustname() != null && objNT2.getCustname().length() > 0) {
                ps = ecomConn.prepareStatement(ECOM_SI_CUST_INSERT);
                ps.setString(1, str_sino);
                ps.setString(2, "N1");
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
                ps = ecomConn.prepareStatement(ECOM_SI_CUST_INSERT);
                ps.setString(1, str_sino);
                ps.setString(2, "N2");
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
                ps = ecomConn.prepareStatement(ECOM_SI_CUST_INSERT);
                ps.setString(1, str_sino);
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

                ps = ecomConn.prepareStatement(ECOM_SI_CUST_INSERT);
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

                ps = ecomConn.prepareStatement(ECOM_SI_CUST_INSERT);
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
            ps = ecomConn.prepareStatement(DELETE_CARGO, 1004, 1007);
            ps.setString(1, bknum);
            ps.executeUpdate();
            ps = ecomConn.prepareStatement(DELETE_COMMODITY, 1004, 1007);
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
            // con = null;
            /* VDS */
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
                    strSOC = Validation.checkNull(siMasterVO.getStrsoc());
                    cntno = Validation.checkNull(siMasterVO.getStrCntNo());
                    cntType = Validation.checkNull(siMasterVO.getStrType());
                    seal2No = Validation.checkNull(siSealVOObj.getStrShipperSeal());
                    seal1No = Validation.checkNull(siSealVOObj.getStrCarrierSeal());
                    ps = ecomConn.prepareStatement(ECOM_SI_CARGO_INSERT);
                    System.out.println("[ECOM] EcomBLBean  str_sino is " + str_sino
                            + ", cntType " + cntType
                            + ", siMasterVO.getEqpSize() " + siMasterVO.getEqpSize()
                            + ", cntno " + cntno
                            + ", strSOC " + strSOC
                            + ", seal1No " + seal1No
                            + ", seal2No " + seal2No
                            + ", siMasterVO.getTareweight() " + siMasterVO.getTareweight()
                            + ", siMasterVO.getCargoweight() " + siMasterVO.getCargoweight()
                            + ", siMasterVO.getGrossweight() " + siMasterVO.getGrossweight()
                            + ", siMasterVO.getWeightUnit() " + siMasterVO.getWeightUnit()
                            + ", siMasterVO.getNor() " + siMasterVO.getNor()
                            + ", siMasterVO.getNeweqptypeid() " + siMasterVO.getRateeqptypeid());

                    rateeqptypeid = siMasterVO.getRateeqptypeid();

                    ps.setString(1, str_sino);
                    ps.setInt(2, i);
                    ps.setString(3, checkNull(siMasterVO.getStrEqpType()));
                    ps.setString(4, cntType);
                    ps.setString(5, cntno);
                    ps.setString(6, strSOC);
                    ps.setString(7, checkNull(seal1No));
                    ps.setString(8, checkNull(seal2No));
                    ps.setDouble(9, Double.parseDouble(ConvertDecimalFormat(siMasterVO.getTareweight())));
                    ps.setDouble(10, Double.parseDouble(ConvertDecimalFormat(siMasterVO.getCargoweight())));
                    ps.setDouble(11, Double.parseDouble(ConvertDecimalFormat(siMasterVO.getGrossweight())));
                    ps.setString(12, checkNull(siMasterVO.getWeightUnit()));
                    ps.setString(13, checkNull(siMasterVO.getNor()));
                    ps.setString(14, checkNull(siMasterVO.getEqpStatus()));
                    ps.setString(15, checkNull(siMasterVO.getDgFlag()));
                    ps.setString(16,
                            checkNull(siMasterVO.getStrEqpType()).equalsIgnoreCase(rateeqptypeid) ? "" : rateeqptypeid);
                    ps.executeUpdate();

                    if ("Y".equalsIgnoreCase(siMasterVO.getRefer())) {
                        ps = ecomConn.prepareStatement(DRAFT_Refer_INSERT);

                        ps.setString(1, str_sino);
                        ps.setInt(2, i);
                        ps.setString(3, cntno);
                        ps.setString(4, Validation1.convertEmptytoNull(siMasterVO.getSetTemp()));
                        ps.setString(5, Validation1.convertEmptytoNull(siMasterVO.getTempMin()));
                        ps.setString(6, Validation1.convertEmptytoNull(siMasterVO.getTempMax()));
                        ps.setString(7, siMasterVO.getTempunit());
                        ps.setString(8, siMasterVO.getHumidity());
                        ps.setString(9,
                                "1".equalsIgnoreCase(siMasterVO.getHumidity_flag()) ? siMasterVO.getHumidity_flag()
                                : "0");
                        ps.setString(10, siMasterVO.getDrains());
                        ps.setString(11, siMasterVO.getCo2value());
                        ps.setString(12, siMasterVO.getO2value());
                        ps.setString(13, siMasterVO.getVentilation());
                        ps.executeUpdate();
                    }

                }
            }

            i = 0;
            alsize = 0;
            String oog = "";

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
                    ps = ecomConn.prepareStatement(GET_ECOM_SI_CARGO_NO, 1004, 1007);
                    ps.setString(1, str_sino);
                    ps.setString(2, cntno);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        cargoId = rs.getInt("conId");
                        // oog = rs.getString("ref_nor");
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
                            wt = Validation.checkNull(siCntDetVo.getStrWgt());
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
                                cmdId = Validation.checkNull(commodity.substring((cmdIndex + 1), commodity.length()));
                            }

                            if (packge.length() > 0) {
                                pkgIndex = packge.lastIndexOf("~");
                                packgeId = Validation.checkNull(packge.substring((pkgIndex + 1), packge.length()));
                            }
                            ps = ecomConn.prepareStatement(ECOM_SI_COMMDOITY_INSERT);
                            ps.setString(1, str_sino);
                            ps.setInt(2, cargoId);
                            ps.setString(3, cntno);
                            ps.setInt(4, j + 1);
                            ps.setString(5, cmdId);
                            ps.setString(6, packgeId);
                            ps.setString(7, qty);
                            ps.setString(8, ConvertDecimalFormat(wt));
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

                            if ("y".equalsIgnoreCase(siCntDetVo.getHazardous())) {
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

                            if ("Y".equalsIgnoreCase(siMasterVO.getOog())) {
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
                    }
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
                    System.out.println("[ECOM] EcomBLBean Record inserted successfully into ecomsi_ics_new!");
                }

            }

            if (newblsubmit) {
                try {
                    String ecompath = fileDetailsVO.getFilePath() + str_sino;
                    String lrppath = fileDetailsVO.getFilePath().replace("Ecom", "BL") + File.separator + tbl_no;
                    File directory = new File(lrppath);
                    if (directory.exists()) {
                        File srcDir = new File(lrppath);
                        File destDir = new File(ecompath);
                        FileUtils.deleteDirectory(destDir);
                        FileUtils.copyDirectory(srcDir, destDir);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ZipFileConversion zipconv = new ZipFileConversion();
            HashMap<String, byte[]> filehm = new HashMap<String, byte[]>();
            boolean isconvertzip = ("Y".equalsIgnoreCase(fileDetailsVO.getEnableZipConversion()));
            String zipFileName = "";
            System.out.println("[ECOM] EcomBLBean isconvertzip=> " + isconvertzip + "  getEnableZipConversion=> "
                    + fileDetailsVO.getEnableZipConversion());
            //
            ps = ecomConn.prepareStatement(FILES_DELETE_QRY);
            ps.setString(1, bknum);
            ps.executeUpdate();
            fileDetailsVO.setFilePath(fileDetailsVO.getFilePath() + str_sino);
            String saveFilePath = fileDetailsVO.getFilePath();
            FileOutputStream fos = null;
            System.out
                    .println("[ECOM] EcomBLBean fileDetailsVO.isFileAvailable()===> " + fileDetailsVO.isFileAvailable()
                            + " fileDetailsVO.getFilePath()===> " + fileDetailsVO.getFilePath());
            if (fileDetailsVO.isFileAvailable()) {
                File directory = new File(saveFilePath);
                if (!directory.exists()) {
                    directory.mkdir();
                }
            }
            String deletedfile = fileDetailsVO.getDeletedfilename();
            System.out.println("[ECOM] EcomBLBean deletedfile==> " + deletedfile);
            if (deletedfile != null && deletedfile.length() > 0) {
                deletedfile = deletedfile.substring(2, deletedfile.length());
                ArrayList fileset = new ArrayList(Arrays.asList(deletedfile.split("~~")));
                for (int i1 = 0; i1 < fileset.size(); i1++) {
                    System.out.println("[ECOM] EcomBLBean checkNull((String)iterator.next())==> "
                            + checkNull((String) fileset.get(i1)));
                    File dfile = new File(
                            fileDetailsVO.getFilePath() + File.separator + checkNull((String) fileset.get(i1)));
                    FileUtils.forceDelete(dfile);
                }
            }
            ps = ecomConn.prepareStatement(ECOM_SI_FILE_INSERT);
            Boolean flag = false;
            System.out.println("[ECOM] EcomBLBean  File path is -->" + saveFilePath);
            if (fileDetailsVO.getVgmFileName() != null && fileDetailsVO.getVgmFileName().trim().length() > 0) {
                String vgmFileName = "VGM_" + fileDetailsVO.getVgmFileName();
                if (isconvertzip) {
                    filehm.put(vgmFileName, fileDetailsVO.getVgmFileData());
                } else {
                    File vgmFilePath = new File(saveFilePath, vgmFileName);
                    fos = new FileOutputStream(vgmFilePath);
                    fos.write(fileDetailsVO.getVgmFileData());
                }

                ps.setString(1, str_sino);
                ps.setString(2, "VGM");
                ps.setString(3, vgmFileName);
                // ps.executeUpdate();

                flag = true;
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
                String dgFileName = "DG_" + fileDetailsVO.getDgFileName();
                if (isconvertzip) {
                    filehm.put(dgFileName, fileDetailsVO.getDgFileData());
                } else {
                    File dgFilePath = new File(saveFilePath, dgFileName);
                    fos = new FileOutputStream(dgFilePath);
                    fos.write(fileDetailsVO.getDgFileData());
                }
                ps.setString(1, str_sino);
                ps.setString(2, "DG");
                ps.setString(3, dgFileName);
                // ps.executeUpdate();

                flag = true;
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
                zipconv.convertZipFile(EcomConsts.FILETYPE_DGC + "_" + str_sino, saveFilePath, filehm);
                zipFileName = zipFileName + EcomConsts.FILETYPE_DGC + "_" + str_sino + ".zip" + "~~~";
            }
            filehm = new HashMap<String, byte[]>();

            if (fileDetailsVO.getLolFileName() != null && fileDetailsVO.getLolFileName().trim().length() > 0) {
                String lolFileName = "LOL_" + fileDetailsVO.getLolFileName();
                if (isconvertzip) {
                    filehm.put(lolFileName, fileDetailsVO.getLolFileData());
                } else {
                    File lolFilePath = new File(saveFilePath, lolFileName);
                    fos = new FileOutputStream(lolFilePath);
                    fos.write(fileDetailsVO.getLolFileData());
                }
                ps.setString(1, str_sino);
                ps.setString(2, "LOL");
                ps.setString(3, lolFileName);
                // ps.executeUpdate();

                flag = true;
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
                zipconv.convertZipFile(EcomConsts.FILETYPE_LOL + "_" + str_sino, saveFilePath, filehm);
                zipFileName = zipFileName + EcomConsts.FILETYPE_LOL + "_" + str_sino + ".zip" + "~~~";
            }
            filehm = new HashMap<String, byte[]>();
            if (fileDetailsVO.getMsdsFileName() != null && fileDetailsVO.getMsdsFileName().trim().length() > 0) {
                String msdsFileName = "MSD_" + fileDetailsVO.getMsdsFileName();
                if (isconvertzip) {
                    filehm.put(msdsFileName, fileDetailsVO.getMsdsFileData());
                } else {
                    File msdsFilePath = new File(saveFilePath, msdsFileName);
                    fos = new FileOutputStream(msdsFilePath);
                    fos.write(fileDetailsVO.getMsdsFileData());
                }

                ps.setString(1, str_sino);
                ps.setString(2, "MSD");
                ps.setString(3, msdsFileName);
                // ps.executeUpdate();

                flag = true;
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
                zipconv.convertZipFile(EcomConsts.FILETYPE_MSD + "_" + str_sino, saveFilePath, filehm);
                zipFileName = zipFileName + EcomConsts.FILETYPE_MSD + "_" + str_sino + ".zip" + "~~~";
            }
            filehm = new HashMap<String, byte[]>();
            if (fileDetailsVO.getOthersFileName() != null && fileDetailsVO.getOthersFileName().trim().length() > 0) {
                String othersFileName = "OT_" + fileDetailsVO.getOthersFileName();
                if (isconvertzip) {
                    filehm.put(othersFileName, fileDetailsVO.getOthersFileData());
                } else {
                    File othersFilePath = new File(saveFilePath, othersFileName);
                    fos = new FileOutputStream(othersFilePath);
                    fos.write(fileDetailsVO.getOthersFileData());

                }
                ps.setString(1, str_sino);
                ps.setString(2, "OT");
                ps.setString(3, othersFileName);
                // ps.executeUpdate();

                flag = true;
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
                zipconv.convertZipFile(EcomConsts.FILETYPE_OTHERS + "_" + str_sino, saveFilePath, filehm);
                zipFileName = zipFileName + EcomConsts.FILETYPE_OTHERS + "_" + str_sino + ".zip" + "~~~";
            }
            if (isconvertzip) {
                if (zipFileName != null && zipFileName.length() > 3) {
                    retFilePath = zipFileName.substring(0, zipFileName.length() - 3);
                }
            }
            System.out.println("[ECOM] EcomBLBean retFilePath=> " + retFilePath);
            if (flag) {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            }
            if ("1".equalsIgnoreCase(appVersion)) {
                returnVO = v1BLBeanObj.v1SaveToBL(bookno, custcode, bookinguserid, cntMapObj, hm, partiesVO, savetype,
                        str_sino, voidCargoObj, voidComObj, username1, tbl_no, client_code, addremarks);
            } else {
                String polAtNEW = "";
                String podAtNEW = "";
                String readerid = null;
                ArrayList lst = null;
                EcomBKGEDIDocStatus doc = null;
                EcomCommonModel cm = null;
                String agencyRefNo = null, intransitPort = null, intransitName = null, transportTermId = null;
                readerid = bookNoGenerate("ESI");
                String status = "";
                System.out.println("[ECOM] EcomBLBean si readeerid::::" + readerid);
                returnVO.setStrSaveType("S");
                System.out.println("[ECOM] EcomBLBean  bknum " + bknum + " bookno " + bookno + " tbl_no is " + tbl_no);
                CompleteModel siModelObj = blVDS.getecombldetails(tbl_no);

                for (Bl blObj : siModelObj.getBl()) {
                    // System.out.println("[ECOM] EcomBLBean =======inside for==============");
                    documentalAgencyid = blObj.getDocumentalagencyid();
                    agencyid = blObj.getAgencyid();
                    agencyRefNo = checkNull(blObj.getAgencyrefno());
                    intransitPort = checkNull(blObj.getIntransitport());
                    intransitName = checkNull(blObj.getIntransitportname());
                    transportTermId = checkNull(blObj.getTransporttermid());
                    oceansercode = blObj.getOceansercode();
                    oceanvoyage = blObj.getOceanvoyage();
                    oceanvslcode = blObj.getOceanvslcode();
                    oceanBound = blObj.getOceanbound();
                    serviceCode = blObj.getServicecode();
                    polTerminal = blObj.getPolterminal();
                    polAtNEW = blObj.getPolat();
                    podTerminal = blObj.getPodterminal();
                    podAtNEW = blObj.getPodat();
                    bookReady = blObj.getBookready();
                    bookDate = blObj.getBookdate();
                    transportserviceid = blObj.getTransportserviceid();
                    preCarriage = blObj.getPrecarriage();
                    onCarriage = blObj.getOncarriage();
                    originportid = blObj.getOriginportid();
                    finalPortId = blObj.getFinalportid();
                    originportdesc = blObj.getOriginportdesc();
                    finalportdesc = blObj.getFinalportdesc();
                    status = blObj.getStatus();
                }
                Map hsm = (Map) ecomvdsservice.getEcomEDIDocstatus(bknum);
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
                doc.setBook_no(bknum);
                lst.add(doc);
                cm.setEdidocstatus(lst);
                ecomvdsservice.putecomedidocstatus(cm);

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
                System.out.println(
                        "[ECOM] EcomBLBean  polAtNEW  " + polAtNEW + " podAtNEW " + podAtNEW + " bookDate " + bookDate);
                ediTempBookingVo.setPolat(polAtNEW);
                ediTempBookingVo.setPodterminal(podTerminal);
                ediTempBookingVo.setPodat(podAtNEW);
                ediTempBookingVo.setBookdate(bookDate);
                ediTempBookingVo.setBookready(bookReady);
                ediTempBookingVo.setPrecarriage(preCarriage);
                ediTempBookingVo.setOncarriage(onCarriage);
                ediTempBookingVo.setStatus("");
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
                ediTempBookingVo.setTbl_no(tbl_no);
                ediTempBookingVo.setEnsdoctype(partiesVO.getEbook_EnsDocType());
                if (partiesVO.getCargoprotectid() != null && partiesVO.getCargoprotectid().length() > 0) {
                    ediTempBookingVo.setCargoprotectid(partiesVO.getCargoprotectid());
                }
                if (checkNull(partiesVO.getEbook_t2lfiling()).length() > 0) {
                    ediTempBookingVo.setT2lfiling(partiesVO.getEbook_t2lfiling());
                }
                ediTempBookingVo.setNvocc(resolveNvoccForDb(partiesVO.getEbook_nvocc()));
                EcomBKGCommonModel ediTempBkgModelObj = new EcomBKGCommonModel();
                ediTempBookingLst.add(ediTempBookingVo);
                ediTempBkgModelObj.setEditempbooking(ediTempBookingLst);

                siVDS.setecomeditempbooking(ediTempBkgModelObj);

                String vesslename = getVesselName(partiesVO.getVesselcode());
                vesslename = vesslename + "/" + partiesVO.getVoyagecode() + "/" + partiesVO.getBound();
                //
                // Parties
                ArrayList ediTempBookingCustLstSH = new ArrayList();
                EcomBKGCustomer ediTempBookingSHCustVo = new EcomBKGCustomer();
                SISubmitVO objSHEdi = (SISubmitVO) hm.get("SH");
                ediTempBookingSHCustVo.setBook_no(bknum);
                ediTempBookingSHCustVo.setCusttypeid("SH");
                ediTempBookingSHCustVo.setCustid(checkNull(objSHEdi.getCustCode()));
                ediTempBookingSHCustVo.setCountryid(checkNull(objSHEdi.getCountryid()));
                ediTempBookingSHCustVo.setName(checkNull(objSHEdi.getCustname()));
                ediTempBookingSHCustVo.setAddress1(checkNull(objSHEdi.getAddress()));
                ediTempBookingSHCustVo.setAddress2(checkNull(objSHEdi.getAddress1()));
                ediTempBookingSHCustVo.setCity(checkNull(objSHEdi.getCity()));
                ediTempBookingSHCustVo.setZipCode(checkNull(objSHEdi.getZipCode()));
                ediTempBookingSHCustVo.setTelephone(checkNull(objSHEdi.getTelephone()));
                ediTempBookingSHCustVo.setFax(checkNull(objSHEdi.getFax()));
                ediTempBookingSHCustVo.setEmail(checkNull(objSHEdi.getEmail()));
                ediTempBookingSHCustVo.setStateid(checkNull(objSHEdi.getState()));
                ediTempBookingSHCustVo.setEori(checkNull(objSHEdi.getEori()));
                ediTempBookingSHCustVo.setPersontype(checkNull(objSHEdi.getPersontype()));
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
                ediTempBookingCOCustVo.setEori(checkNull(objCO.getEori()));
                ediTempBookingCOCustVo.setPersontype(checkNull(objCO.getPersontype()));
                ediTempBookingCOCustVo.setEdireaderid(readerid);
                ediTempBookingCustLstCO.add(ediTempBookingCOCustVo);
                EcomBKGCommonModel ediTempBkgCustCOModelObj = new EcomBKGCommonModel();
                ediTempBkgCustCOModelObj.setEditempbooking_cust(ediTempBookingCustLstCO);
                siVDS.setecomeditempbookingcustomer(ediTempBkgCustCOModelObj);

                ArrayList ediTempBookingCustLstNT = new ArrayList();
                EcomBKGCustomer ediTempBookingNTCustVo = new EcomBKGCustomer();
                SISubmitVO objNTEdi = (SISubmitVO) hm.get("NT");
                if (objNTEdi.getCustname() != null && objNTEdi.getCustname().length() > 0
                        && checkNull(partiesVO.getNotifyName()).length() > 0) {
                    ediTempBookingNTCustVo.setBook_no(bknum);
                    ediTempBookingNTCustVo.setCusttypeid("NT");
                    ediTempBookingNTCustVo.setCustid(checkNull(objNTEdi.getCustCode()));
                    ediTempBookingNTCustVo.setCountryid(checkNull(objNTEdi.getCountryid()));
                    ediTempBookingNTCustVo.setName(checkNull(objNTEdi.getCustname()));
                    ediTempBookingNTCustVo.setAddress1(checkNull(objNTEdi.getAddress()));
                    ediTempBookingNTCustVo.setAddress2(checkNull(objNTEdi.getAddress1()));
                    ediTempBookingNTCustVo.setCity(checkNull(objNTEdi.getCity()));
                    ediTempBookingNTCustVo.setZipCode(checkNull(objNTEdi.getZipCode()));
                    ediTempBookingNTCustVo.setTelephone(checkNull(objNTEdi.getTelephone()));
                    ediTempBookingNTCustVo.setFax(checkNull(objNTEdi.getFax()));
                    ediTempBookingNTCustVo.setEmail(checkNull(objNTEdi.getEmail()));
                    ediTempBookingNTCustVo.setStateid(checkNull(objNTEdi.getState()));
                    ediTempBookingNTCustVo.setEori(checkNull(objNTEdi.getEori()));
                    ediTempBookingNTCustVo.setPersontype(checkNull(objNTEdi.getPersontype()));
                    ediTempBookingNTCustVo.setEdireaderid(readerid);
                    ediTempBookingCustLstNT.add(ediTempBookingNTCustVo);
                    EcomBKGCommonModel ediTempBkgCustNTModelObj = new EcomBKGCommonModel();
                    ediTempBkgCustNTModelObj.setEditempbooking_cust(ediTempBookingCustLstNT);
                    siVDS.setecomeditempbookingcustomer(ediTempBkgCustNTModelObj);
                }

                ArrayList ediTempBookingCustLstNT2 = new ArrayList();
                EcomBKGCustomer ediTempBookingNT2CustVo = new EcomBKGCustomer();
                objNT2 = (SISubmitVO) hm.get("N1");
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
                    ediTempBookingNT2CustVo.setEori(checkNull(objNT2.getEori()));
                    ediTempBookingNT2CustVo.setPersontype(checkNull(objNT2.getPersontype()));
                    ediTempBookingNT2CustVo.setEdireaderid(readerid);
                    ediTempBookingCustLstNT2.add(ediTempBookingNT2CustVo);
                    EcomBKGCommonModel ediTempBkgCustNT2ModelObj = new EcomBKGCommonModel();
                    ediTempBkgCustNT2ModelObj.setEditempbooking_cust(ediTempBookingCustLstNT2);
                    siVDS.setecomeditempbookingcustomer(ediTempBkgCustNT2ModelObj);
                }

                ArrayList ediTempBookingCustLstNT3 = new ArrayList();
                EcomBKGCustomer ediTempBookingNT3CustVo = new EcomBKGCustomer();
                objNT3 = (SISubmitVO) hm.get("N2");
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
                    ediTempBookingNT3CustVo.setEori(checkNull(objNT3.getEori()));
                    ediTempBookingNT3CustVo.setPersontype(checkNull(objNT3.getPersontype()));
                    ediTempBookingNT3CustVo.setEdireaderid(readerid);
                    ediTempBookingCustLstNT3.add(ediTempBookingNT3CustVo);
                    EcomBKGCommonModel ediTempBkgCustNT3ModelObj = new EcomBKGCommonModel();
                    ediTempBkgCustNT3ModelObj.setEditempbooking_cust(ediTempBookingCustLstNT3);
                    siVDS.setecomeditempbookingcustomer(ediTempBkgCustNT3ModelObj);
                }
                // FW
                ArrayList ediTempBookingCustLstFW = new ArrayList();
                EcomBKGCustomer ediTempBookingFWCustVo = new EcomBKGCustomer();
                objFW = (SISubmitVO) hm.get("FW");
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
                objBY = (SISubmitVO) hm.get("BY");
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
                objSL = (SISubmitVO) hm.get("SL");
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
                                ediTempBookingSLListCustVo.setCountryid( extractCountryCode(checkNull(SLVO.getCountryid())));
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
                    ediTempBookingothersCustVo.setEori(checkNull(bkngCutsObj.getEori()));
                    ediTempBookingothersCustVo.setTelephone(checkNull(bkngCutsObj.getTelephone()));
                    ediTempBookingothersCustVo.setFax(checkNull(bkngCutsObj.getFax()));
                    ediTempBookingothersCustVo.setEmail(checkNull(bkngCutsObj.getEmail()));
                    ediTempBookingothersCustVo.setStateid(checkNull(bkngCutsObj.getStateid()));
                    ediTempBookingothersCustVo.setPersontype(checkNull(bkngCutsObj.getPersontype()));
                    ediTempBookingothersCustVo.setEdireaderid(readerid);
                    ediTempBookingCustLstothers.add(ediTempBookingothersCustVo);
                    EcomBKGCommonModel ediTempBkgCustOthersModelObj = new EcomBKGCommonModel();
                    ediTempBkgCustOthersModelObj.setEditempbooking_cust(ediTempBookingCustLstothers);
                    siVDS.setecomeditempbookingcustomer(ediTempBkgCustOthersModelObj);
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
                        cargoNo = cargoNo + 1;
                        eqp_type = "";
                        itemNo = 0;
                        cntno = Validation.checkNull(siMasterVO.getStrCntNo());
                        cntType = Validation.checkNull(siMasterVO.getStrType());
                        seal2No = Validation.checkNull(siSealVOObj.getStrShipperSeal());
                        seal1No = Validation.checkNull(siSealVOObj.getStrCarrierSeal());
                        eqp_type = Validation.checkNull(siMasterVO.getStrEqpType());

                        String oog1 = Validation.checkNull(siMasterVO.getOog());
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

                        double cargowt = Double.parseDouble(ConvertDecimalFormat(cargoweight));
                        double tarewt = Double.parseDouble(ConvertDecimalFormat(tareweigth));

                        double grweight = Double.parseDouble(ConvertDecimalFormat(grossweigt));
                        //
                        System.out.println("[ECOM] EcomBLBean ssoocc " + ssoocc + " tareweigth " + tarewt
                                + "cargoweight " + cargowt
                                + " grossweigt " + grweight + " oog1 " + oog1 + " dgflag " + dgflag + " SOC " + ssoocc
                                + " ssoocc.length " + ssoocc.length() + " readerid " + readerid);

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
                        System.out.println("[ECOM] EcomBLBean tempmax: " + tempmax + ", settemp: " + settemp
                                + ", tempmin: " + tempmin + ", tempunit: " + tempunit
                                + ", eqp_type: " + eqp_type + ", cntType: " + cntType + ", cntno: " + cntno + ", unit: "
                                + unit);
                        rateeqptypeid = siMasterVO.getRateeqptypeid();

                        Booking_cargo cargoToInsert = new Booking_cargo();
                        ArrayList cargoToInsertList = new ArrayList();
                        cargoToInsert.setBook_no(bknum);
                        cargoToInsert.setType(checkNull(siMasterVO.getStrEqpType()));
                        cargoToInsert.setEqptypeid(checkNull(cntType));
                        cargoToInsert.setEdireaderid(readerid);
                        cargoToInsert.setEqpid(cntno);
                        cargoToInsert.setCargono(cargoNo);
                        cargoToInsert.setOog(oog1);
                        cargoToInsert.setSeal1no(seal1No);
                        cargoToInsert.setSeal2no(seal2No);
                        cargoToInsert.setShipperown(checkNull(ssoocc));
                        cargoToInsert.setCargoweight(String.valueOf(cargowt));
                        cargoToInsert.setGrossweight(String.valueOf(grweight));
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
                        cargoToInsert.setTareweight(String.valueOf(tarewt));
                        cargoToInsert.setHumidity(siMasterVO.getHumidity());
                        cargoToInsert.setHumidity_flag(
                                "1".equalsIgnoreCase(siMasterVO.getHumidity_flag()) ? siMasterVO.getHumidity_flag()
                                : "0");
                        cargoToInsert.setDrains(siMasterVO.getDrains());
                        cargoToInsert.setCo2value(siMasterVO.getCo2value());
                        cargoToInsert.setO2value(siMasterVO.getO2value());
                        cargoToInsert.setVentpercentage(siMasterVO.getVentilation());
                        cargoToInsert.setRate_eqptypeid(
                                checkNull(siMasterVO.getStrEqpType()).equalsIgnoreCase(rateeqptypeid) ? ""
                                : rateeqptypeid);
                        System.out.println("[ECOM] EcomBLBean ventvalueblbean" + siMasterVO.getVentilation());
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
                                itemNo = itemNo + 1;
                                packgeId = "";
                                volume = "";
                                volumeUnt = "";
                                netweight = "";
                                marks = "";
                                description = "";
                                pkgIndex = 0;
                                wt = ConvertDecimalFormat(Validation.checkNull(siCntDetVo.getStrWgt()));
                                unit = Validation.checkNull(siCntDetVo.getStrUnit());
                                qty = Validation.checkNull(siCntDetVo.getStrQty());
                                commodity = Validation.checkNull(siCntDetVo.getStrCommodity());
                                packge = Validation.checkNull(siCntDetVo.getStrPkg());
                                volume = Validation.checkNull(siCntDetVo.getStrVol());
                                volumeUnt = Validation.checkNull(siCntDetVo.getStrVut());
                                marks = Validation.checkNull(siCntDetVo.getStrMrk());
                                netweight = Validation.checkNull(siCntDetVo.getNettwt());
                                description = Validation.checkNull(siCntDetVo.getStrDes());
                                bycode = Validation.checkNull(siCntDetVo.getByscode());
                                byname = Validation.checkNull(siCntDetVo.getBysname());
                                slcode = Validation.checkNull(siCntDetVo.getSlscode());
                                slname = Validation.checkNull(siCntDetVo.getSlsname());
                                cmdty_hblno = Validation.checkNull(siCntDetVo.getCmdty_hblno());
                                if (packge.length() > 0) {
                                    pkgIndex = packge.lastIndexOf("~");
                                    packgeId = Validation.checkNull(packge.substring((pkgIndex + 1), packge.length()));
                                }
                                if (commodity.length() > 0) {
                                    cmdIndex = commodity.lastIndexOf("~");
                                    cmdDesc = Validation.checkNull(commodity.substring(0, (cmdIndex - 1)));
                                    cmdId = Validation
                                            .checkNull(commodity.substring((cmdIndex + 1), commodity.length()));
                                }

                                String itemleft = Validation1.checkNullfloat(siCntDetVo.getItemleft());
                                String itemforward = Validation1.checkNullfloat(siCntDetVo.getItemforward());
                                String itemtop = Validation1.checkNullfloat(siCntDetVo.getItemtop());
                                String itemright = Validation1.checkNullfloat(siCntDetVo.getItemright());
                                String itemaft = Validation1.checkNullfloat(siCntDetVo.getItemaft());
                                String itemlengthunit = Validation.checkNull(siCntDetVo.getItemlengthUnit());
                                String hsComdtyCode = Validation.checkNull(siCntDetVo.getHsCode());
                                if ("Y".equalsIgnoreCase(siCntDetVo.getIsFakCommodity())) {
                                    hsComdtyCode = null;
                                    if (Validation.checkNull(siCntDetVo.getHsCode()).length() > 0) {
                                        hssb.append(cntno + " : " + siCntDetVo.getHsCode());
                                        hssb.append(" ");
                                    }
                                }
                                System.out.println("[ECOM] EcomBLBean get:::::" + siCntDetVo.getHazardous());
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
                                    System.out.println("[ECOM] EcomBLBean commval====>" + ediItemDesc);
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
                                if(checkNull(netweight).length()>0){
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
                                commodityToInsert.setCarriercode(client_code);
                                commodityToInsert.setMarksnumbers(marks.replace("\r", ""));
                                commodityToInsert.setMarksdesc(description.replace("\r", ""));
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
                    }
                }

                Chargelist = partiesVO.getChargeDetails();
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
                ensDec = partiesVO.getEnsdecvo();
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
                    ics.setZipcode(ensDec.getDeclarantZipcode());
                    ics.setEdireaderid(readerid);
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

                // get Origin of country
                CompleteModel blModelObj = blVDS.getecomblheader(tbl_no);
                String originofcountry = null;
                for (Bl_header blheaderObj : blModelObj.getBl_header()) {
                    originofcountry = blheaderObj.getOriginofcountry();
                }
                //
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
                headerVO.setShipperName(checkNull(partiesVO.getShipperName()));
                headerVO.setConsigneeName(checkNull(partiesVO.getConsigneeName()));
                headerVO.setNotifierName(checkNull(partiesVO.getNotifyName()));
                headerVO.setEdireaderid(readerid);
                headerVO.setVersion("0");
                headerVO.setLoadVSL("");
                headerVO.setOriginofcountry(checkNull(originofcountry));
                headerList.add(headerVO);
                headerModel.setEditempbooking_header(headerList);
                siVDS.setecomeditempbookingheader(headerModel);

                Map configMap = cntrlBeanObj.getMasterConfigDetails();
                EcomBKGEDIDocStatus docStatusVo = new EcomBKGEDIDocStatus();
                ArrayList docStatusList = new ArrayList();
                docStatusVo.setBook_no(bknum);
                docStatusVo.setEdireaderid(readerid);
                docStatusVo.setTranstype("5");
                docStatusVo.setStatus("R");
                docStatusVo.setEdiparty(configMap.get(GeneralConf.CARRIER_CODE)
                        + ("I".equalsIgnoreCase(status) ? GeneralConf.EDI_TYPE_MCN : GeneralConf.EDI_TYPE_SI));
                docStatusVo.setReceivdate("");
                docStatusVo.setMessage(GeneralConf.EDI_MESSAGETYPE_SI);
                docStatusVo.setServprovname(configMap.get(GeneralConf.CARRIER_CODE) + GeneralConf.EDI_ECOM);
                System.out.println("[ECOM] EcomBLBean amendment_version===>" + amendment_version);
                docStatusVo.setAmendment_version(amendment_version);

                docStatusList.add(docStatusVo);
                EcomBKGCommonModel docStatusToInsertModel = new EcomBKGCommonModel();
                docStatusToInsertModel.setEdidocstatus(docStatusList);
                siVDS.setecomeditempdocstatus(docStatusToInsertModel);

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
                String strHsCode = hssb.toString();
                System.out.println("[ECOM] EcomBLBean >>strHsCode>>>" + strHsCode);
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
                if (retFilePath != null && retFilePath.length() > 0) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");
                        String dateString = format.format(new Date());
                        String newfile = "(" + retFilePath.replace("~~~", ",") + ") at " + dateString;
                        lst = new ArrayList();
                        EcomBKGRemarks rm = new EcomBKGRemarks();
                        cm = new EcomCommonModel();
                        rm.setBook_no(bknum);
                        rm.setRemarksref("File Added  ");
                        rm.setRemarksval(newfile);
                        rm.setEdireaderid(readerid);
                        rm.setVersion("0");
                        rm.setCategory(null);
                        lst.add(rm);
                        cm.setEditempbooking_remarks(lst);
                        ecomvdsservice.setEdiTempBookingRemarks(cm);
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
                        EcomBKGRemarks rm = new EcomBKGRemarks();
                        cm = new EcomCommonModel();
                        rm.setBook_no(bknum);
                        rm.setRemarksref("File Removed  ");
                        rm.setRemarksval(newfile);
                        rm.setEdireaderid(readerid);
                        rm.setVersion("0");
                        rm.setCategory(null);
                        lst.add(rm);
                        cm.setEditempbooking_remarks(lst);
                        ecomvdsservice.setEdiTempBookingRemarks(cm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

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
                ecomvdsservice.seteditempbookingreference(refcm);

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
                    ecomvdsservice.seteditempbookingreference(refcm);
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
                    ecomvdsservice.seteditempbookingreference(refcm);
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
                    ecomvdsservice.seteditempbookingreference(refcm);
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
                    ecomvdsservice.seteditempbookingreference(refcm);
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
                    ecomvdsservice.seteditempbookingreference(refcm);
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
                    ecomvdsservice.seteditempbookingreference(refcm);
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
                    ecomvdsservice.seteditempbookingreference(refcm);
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
                    ecomvdsservice.seteditempbookingreference(refcm);
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
                    ecomvdsservice.seteditempbookingreference(refcm);
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
                    ecomvdsservice.seteditempbookingreference(refcm);
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
                    ecomvdsservice.seteditempbookingreference(refcm);
                } 
                // edireadermaster
                /*
                 * Gson gson = new Gson();
                 * Map ediMap = ecomvdsservice.getecomediid(EcomConsts.ONLINE_SI);
                 * System.out.println("[ECOM] EcomBLBean >>ediMap>>" + ediMap);
                 * List<EcomBKGReaderMaster> ediList = null;
                 * if (ediMap != null && ediMap.size() > 0) {
                 * System.out.println("[ECOM] EcomBLBean >>>yes value is there>>>>>");
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
                System.out.println("[ECOM] EcomBLBean >>ediid>>>>" + ediid);
                String corporateAgency = null;
                List corpList = null;
                Map corpAgency = ecomvdsservice.getEcomCorporateAgency(documentalAgencyid);
                System.out.println("[ECOM] EcomBLBean >>corpAgency>>" + corpAgency);
                if (corpAgency != null && corpAgency.size() > 0) {
                    corpList = (List) corpAgency.get("agencymatrix");
                    if (corpList != null && corpList.size() > 0) {
                        corpAgency = (Map) corpList.get(0);
                        corporateAgency = checkNull((String) corpAgency.get("corpagency"));
                    }
                }
                System.out.println("[ECOM] EcomBLBean >>corporateAgency>bl>" + corporateAgency);
                lst = null;
                EcomBKGReaderMaster edirm = null;
                cm = null;
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
                    ecomvdsservice.setedireadermaster(cm);

                }
                String strAgencyCode = documentalAgencyid;
                String strAgency = getAgencyMail(strAgencyCode);
                if ((strAgency == null || "".equalsIgnoreCase(strAgency.trim()))
                        && "BAKOFF".equalsIgnoreCase(strAgencyCode)) {
                    strAgency = getBackOfficeMailId();
                }
                returnVO.setAdmToMailId(strAgency);
                returnVO.setReaderid(readerid);
            }
            // agencyid
            MailFormatVO mailFormatObj = new MailFormatVO();
            mailFormatObj = getMailContent("BL", "submitmail", "C");
            strReturn = mailFormatObj.getStrMailContent();
            strReturn = strReturn.replaceAll("@bl_no", tbl_no);
            strReturn = strReturn.replaceAll("@book_no", bookno);
            returnVO.setStrMailMsg(strReturn);
            returnVO.setActivityCode(mailFormatObj.getActivityCode());
            returnVO.setStrSubject(mailFormatObj.getStrSubject()
                    + "-(" + bknum + ")");
            returnVO.setStrFromMailId(mailFormatObj.getStrFromAddr());

            if (returnVO.getAdmToMailId() != null && returnVO.getAdmToMailId().length() > 0) {
                MailFormatVO mailFormatObj2 = new MailFormatVO();
                mailFormatObj2 = getMailContent("BL", "Update", "C");
                String msg2 = mailFormatObj2.getStrMailContent();
                msg2 = msg2.replaceAll("@BLNO", tbl_no);
                // msg2 = msg2.replaceAll("@customer", "Agency");
                msg2 = msg2.replaceAll("@client", mailFormatObj2.getStrRegard());
                msg2 = msg2.replaceAll("@bookno", bookno);
                returnVO.setAdmMailMsg(msg2);
                String subject1 = mailFormatObj2.getStrSubject();
                subject1 = subject1.replace("@sirefno", bknum);
                returnVO.setAdmSubject(subject1);
                returnVO.setAdmFromMailId(mailFormatObj2.getStrFromAddr());

            }

            strReturn = checkNull(getForwardMessage("SI", msgtype, "c"));
            strReturn = strReturn.replaceAll("@SINO", bknum);
            returnVO.setStrDisplayMsg(strReturn);
            returnVO.setStrRefNo(bknum);
            returnVO.setFilenames(retFilePath);

            if ("S".equalsIgnoreCase(savetype)) {
                SaveToLog(username1, "BL", "Submit", "BL Correction Sent", bookno, custcode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            returnVO.setStrRefNo("");
            returnVO.setStrDisplayMsg("Bill Of Lading not saved.");
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

    private String getForwardMessage(String module, String criteria, String usertype) {
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
            System.out.println("[ECOM] EcomBLBean >>bl agency mail>>" + resultEmail);
        } catch (Exception e) {
            System.out.println("[ECOM] EcomBLBean ******==>" + e);
        }
        return resultEmail;

    }

    public MailFormatVO getMailContent(String module, String criteria, String usertype) {
        // tln("*********getMailContent********");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        MailFormatVO mailFormatObj = new MailFormatVO();
        String query = "select message ,from_mailid,subject,activitycode from ecom_messageformat(nolock) where Status='a' and Type ='mail' and criteria = ? and Module =? and usertype = ?";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB1").getConnection();
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
    public String getBLVersionNumber(String blno) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String result = "";
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            pstmt = con.prepareStatement(GET_ECOM_BL_VERSION);
            pstmt.setString(1, blno);
            rst = pstmt.executeQuery();
            if (rst.next()) {
                result = checkNull(rst.getString("eSINO"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (pstmt != null) {
                    pstmt.close();
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

  /*  synchronized private String bookNoGenerate(String module) {
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
            System.out.println("[ECOM] EcomBLBean ===========NEW bknum==========>>>>>>" + bkNum);
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
                System.out.println("[ECOM] EcomBLBean exception while closing connection bookNoGenerate" + e);
                e.printStackTrace();
            }

        }
        return bkNum;
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
                System.out.println("[ECOM] EcomBLBean NEW bknum >> " + bknum);
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

    private String getVesselName(String vesselcode) {
        String result = null;
        List<VesselMaster> vessellst = null;
        Gson gson = null;
        try {
            gson = new Gson();
            Map hs = (Map) ecomvdsservice.getecomvesseldetails(vesselcode);
            vessellst = gson.fromJson(gson.toJson((ArrayList) hs.get("vesselmaster")),
                    new TypeToken<List<VesselMaster>>() {
                    }.getType());
            if (vessellst != null && vessellst.size() > 0) {
                VesselMaster rs = (VesselMaster) vessellst.get(0);
                result = checkNull(rs.getVesselname());
            } else {
                result = "";
            }
            System.out.println("[ECOM] EcomBLBean >>>getVesselName>>>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public String UpdateBLStatus(String blno, String type, String usercode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "";
        String query = null;
        int Sh_update = 0;
        String printaccess = "select oblprint from customermaster where custcode=? ";
        //
        try {
            con = new lrp.serverutils.refs.ServerObject("LRPDB").getConnection();
            ps = con.prepareStatement(printaccess);
            ps.setString(1, usercode);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = checkNull(rs.getString("oblprint"));
            }
            if (result == null || result.length() == 0 || "N".equalsIgnoreCase(result)) {
                result = "access_denied";
            }
            //
            if (result != null && "Y".equalsIgnoreCase(result)) {
                query = "select bl_no from bl where  status=? and bl_no=?";
                ps = con.prepareStatement(query);
                ps.setString(1, "I");
                ps.setString(2, blno);
                rs = ps.executeQuery();
                if (rs.next()) {
                    result = "bl_issued";
                }
                if ("Y".equalsIgnoreCase(result)) {
                    query = "update bl set status=?,printedOriginals=originals,PrintedDate=getDate(),PrintedUser=? where bl_no=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, "I");
                    ps.setString(2, usercode);
                    ps.setString(3, blno);
                    Sh_update = ps.executeUpdate();
                    if (Sh_update == 0) {
                        result = "fail";
                    } else {
                        result = "success";
                    }
                }
                System.out.println("[ECOM] EcomBLBean *********result******" + result);
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
                System.out.println("[ECOM] EcomBLBean exception while closing connection bookNoGenerate" + e);
                e.printStackTrace();
            }
        }
        System.out.println("[ECOM] EcomBLBean Print result==============>>>>>" + result);
        return result;
    }

    @Override
    public String AgencySigmature(String agency_code) throws RemoteException {
        String result = "Yes";
        try {
            Map<String, Object> hs = (Map<String, Object>) ecomvdsservice.getecomoblsignature(agency_code);
            if (hs != null) {
                List<Map<String, Object>> agencyOblList = (List<Map<String, Object>>) hs.get("digitalsignature");
                if (agencyOblList != null && !agencyOblList.isEmpty()) {
                    for (int i = 0; i < agencyOblList.size(); i++) {
                        Map<String, Object> resultMap = agencyOblList.get(i);
                        String filepath = checkNull((String) resultMap.get("filepath"));
                        String agency = checkNull((String) resultMap.get("agency"));
                        if (checkNull(filepath).length() == 0 && filepath.isEmpty()) {
                            result = "No~~" + agency;
                            break;
                        }
                    }
                }
            }
            System.out.println("[ECOM] EcomBLBean >>>AgencySignature>>>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
            result = "No";
        }
        return result;
    }

    @Override
    public HashMap fetchAddremarks(String blno, String type) throws RemoteException {
        Connection ecomcon = null;
        PreparedStatement ecomps = null;
        ResultSet ecomrs = null;
        String addremarks = null;
        HashMap hm = new HashMap();
        try {
            ecomcon = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ecomps = ecomcon.prepareStatement(GET_ECOM_REMARKS);
            ecomps.setString(1, blno);
            ecomrs = ecomps.executeQuery();
            addremarks = "";
            if (ecomrs.next()) {
                addremarks = checkNull(ecomrs.getString("custremarks"));
            }
            hm.put("addremarks", addremarks);
            System.out.println("[ECOM] EcomBLBean ===Fetching custremarks from ecomSI table is done============");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ecomrs != null) {
                    ecomrs.close();
                }
                if (ecomps != null) {
                    ecomps.close();
                }
                if (ecomcon != null) {
                    ecomcon.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hm;
    }

    @Override
    public SIPartiesVO getBLMasterDetails(String blno) throws RemoteException {
        SIPartiesVO partiesVO = null;
        try {
            CompleteModel blModelObj = blVDS.getecomblheader(blno);
            for (Bl_header blheaderObj : blModelObj.getBl_header()) {
                partiesVO = new SIPartiesVO();
                partiesVO.setVslvoyage_free_txt(blheaderObj.getPrecarriage());
                partiesVO.setPol_free_txt(blheaderObj.getPol());
                partiesVO.setPod_free_txt(blheaderObj.getPod());
                partiesVO.setPla_free_txt(blheaderObj.getOrigin());
                partiesVO.setPld_free_txt(blheaderObj.getDelivery());
                partiesVO.setShipperName(checkNull(blheaderObj.getShipperName()));
                partiesVO.setShipperHeader(checkNull(blheaderObj.getShipper()));
                partiesVO.setConsigneeName(checkNull(blheaderObj.getConsigneeName()));
                partiesVO.setConsigneeHeader(checkNull(blheaderObj.getConsignee()));
                partiesVO.setNotifyName(checkNull(blheaderObj.getNotifierName()));
                partiesVO.setNotifyHeader(checkNull(blheaderObj.getNotify()));
                partiesVO.setOrgTelType(checkNull(blheaderObj.getTelex_release()));
            }
            System.out.println("[ECOM] EcomBLBean ====fetching BL header details from bl_header is done========");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return partiesVO;
    }

    @Override
    public HashMap fetchPortDetForBL(String blNum) {
        HashMap hm = new HashMap();
        String bookNo = null, ccMailId = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        try {
            SIPartiesVO partiesVO = null;
            CompleteModel blModelObj = blVDS.getecomportdetforbl(blNum);
            for (Vesselmaster vesselMasterObj : blModelObj.getVesselmaster()) {
                partiesVO = new SIPartiesVO();
                partiesVO = new SIPartiesVO();
                partiesVO.setVesselcode(vesselMasterObj.getVesselcode());
                partiesVO.setVoyagecode(vesselMasterObj.getVoyagecode());
                partiesVO.setBound(vesselMasterObj.getBound());
                partiesVO.setPod(vesselMasterObj.getDischargeportid());
                partiesVO.setPol(vesselMasterObj.getLoadportid());
                partiesVO.setIn_origin(vesselMasterObj.getOriginportid());
                partiesVO.setIn_delivery(vesselMasterObj.getDischargeportid());
                partiesVO.setReceipt(vesselMasterObj.getOriginportdesc());
                partiesVO.setDelivery(vesselMasterObj.getFinalportdesc());
                partiesVO.setVesselName(checkNull(vesselMasterObj.getVesselcode()).toUpperCase());
                partiesVO.setPolName(vesselMasterObj.getLoadportid());
                partiesVO.setPodName(vesselMasterObj.getDischargeportid());
                partiesVO.setAgencyRefNo(checkNull(vesselMasterObj.getAgencyRefNo()));
                partiesVO.setServicecode(vesselMasterObj.getServicecode());
                partiesVO.setBltype(checkNull(vesselMasterObj.getBltype()));
                partiesVO.setCargoprotectid(vesselMasterObj.getCargoprotectid());
                partiesVO.setEbook_t2lfiling(vesselMasterObj.getT2lfiling());
                partiesVO.setEbook_nvocc(vesselMasterObj.getNvocc());
                partiesVO.setEbook_EnsDocType(vesselMasterObj.getEnsdoctype());
                partiesVO.setEbook_EnsFiling(vesselMasterObj.getEnsfiling());
                System.out.println("[ECOM] EcomBLBean t2lfiling==>" + partiesVO.getEbook_t2lfiling());
                //
                String str_org = checkNull(vesselMasterObj.getPrecarriage());
                String str_des = checkNull(vesselMasterObj.getOncarriage());
                partiesVO.setPolat(vesselMasterObj.getPolat());
                partiesVO.setPodat(vesselMasterObj.getPodat());
                partiesVO.setFinalport(vesselMasterObj.getFinalportid());
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
                bookNo = checkNull(vesselMasterObj.getBook_no());
                hm.put("routing", partiesVO);
                break;
            }
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GETECOM_BOOKNO);
            ps.setString(1, bookNo);
            rst = ps.executeQuery();
            if (rst.next()) {
                ccMailId = checkNull(rst.getString("ccmailid"));
            }
            hm.put("CCMAILID", ccMailId);
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
    public String cancelSubmittedBL(String siNo) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "Not Processed";
        String ediStatus = null;
        String ecomStatus = null;
        ArrayList lst = null;
        EcomBKGEDIDocStatus doc = null;
        EcomCommonModel cm = null;
        Map resultMap = null;
        List list = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOM_SI_STATUS_BL);
            ps.setString(1, siNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                ecomStatus = rs.getString("status");
            }
            resultMap = ecomvdsservice.getEcomEDIDocstatus(siNo);
            if (resultMap != null) {
                list = (List) resultMap.get("edidocstatus");
                if (list != null && list.size() > 0) {
                    resultMap = (Map) list.get(0);
                    ediStatus = (String) resultMap.get("status");
                }
            }
            System.out.println("[ECOM] EcomBLBean >>>edi status>>" + ediStatus + ">>ecomStatus>>" + ecomStatus);
            if ("R".equalsIgnoreCase(ediStatus) && "S".equalsIgnoreCase(ecomStatus)) {
                lst = new ArrayList();
                doc = new EcomBKGEDIDocStatus();
                cm = new EcomCommonModel();
                doc.setBook_no(siNo);
                lst.add(doc);
                cm.setEdidocstatus(lst);
                ecomvdsservice.putecomedidocstatus(cm);
                ps = con.prepareStatement(UPDATE_ECOM_SI_STATUS_BL);
                ps.setString(1, siNo);
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

    @Override
    public ReturnVO acceptBLByUser(String blNo, String agencycode) throws RemoteException {
        ArrayList lst = null;
        EcomCommonModel cm = null;
        EcomBLReference rf = null;
        String result = "", message = "", subject = "";
        ReturnVO returnVO = null;
        // TimeZoneUtils timeZoneObj=new TimeZoneUtils();
        try {
            returnVO = new ReturnVO();
            lst = new ArrayList();
            rf = new EcomBLReference();
            cm = new EcomCommonModel();
            rf.setBl_no(blNo);
            rf.setRefid("BAU");
            rf.setRefval("Y");
            lst.add(rf);
            cm.setBl_reference(lst);
            blVDS.setEcomBLAcceptReference(cm);

            // System.out.println("[ECOM] EcomBLBean
            // >>timeZoneObj.getCurrentUTCDateAsString()>>"+timeZoneObj.getCurrentUTCDateAsString());
            SimpleDateFormat targer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date acceptedDate = new Date();
            String refval = targer.format(acceptedDate);

            lst = new ArrayList();
            rf = new EcomBLReference();
            cm = new EcomCommonModel();
            rf.setBl_no(blNo);
            rf.setRefid("BAD");
            rf.setRefval(refval);
            System.out.println("[ECOM] EcomBLBean >>refval>>>" + refval);
            // rf.setRefval(timeZoneObj.getCurrentUTCDateAsString());
            lst.add(rf);
            cm.setBl_reference(lst);
            blVDS.setEcomBLAcceptReference(cm);

            result = checkNull(getAgencyMail(agencycode));
            if ((result == null || "".equalsIgnoreCase(result.trim())) && "BAKOFF".equalsIgnoreCase(result)) {
                result = getBackOfficeMailId();
            }
            System.out.println("[ECOM] EcomBLBean >>result>>" + result);
            MailFormatVO mailFormatObj = new MailFormatVO();
            mailFormatObj = getMailContent("BL", "blcon-cust", "C");
            message = mailFormatObj.getStrMailContent();
            message = message.replaceAll("@blno", blNo);
            System.out.println("[ECOM] EcomBLBean <message<<" + message);
            subject = mailFormatObj.getStrSubject();
            subject = subject.replace("@blno", blNo);
            System.out.println("[ECOM] EcomBLBean subject======> " + subject);
            returnVO.setStrSubject(subject);
            returnVO.setStrMailMsg(message);
            returnVO.setStrToMailId(result);
            if (result.length() > 0) {
                MailFormatVO mailFormatObj2 = getMailContent("BL", "blcon-age", "C");
                message = mailFormatObj2.getStrMailContent();
                message = message.replaceAll("@blno", blNo);
                System.out.println("[ECOM] EcomBLBean <message agency<<" + message);
                subject = mailFormatObj2.getStrSubject();
                subject = subject.replace("@blno", blNo);
                returnVO.setAdmSubject(subject);
                returnVO.setAdmMailMsg(message);
                returnVO.setAdmToMailId(result);
            }
            System.out.println("[ECOM] EcomBLBean >>>result aency mail>>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnVO;
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


    @Override
    public Map getBlReference(String blno) {
        Map result = null;
        List lis = null;
        Map map = null;
        try {
            System.out.println("[ECOM] EcomBLBean Inside getBlReference");
            result = new HashMap();
            map = blVDS.getEcomBlReference(blno, "AES,ACI," + EcomConsts.REF_TYPE + "," + EcomConsts.BLType + ","
                    + EcomConsts.packinglst + "," + EcomConsts.invoiceupload + "," + EcomConsts.bltype_UAE + "," + EcomConsts.mpci_id);
            System.out.println("[ECOM] EcomBLBean getBlReference--> " + map);
            if (map != null) {
                lis = (List) map.get("bl_reference");
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
    public List getBLCharges(String blno) {
        List chargelis = null;
        ChargesVo charge = null;
        try {
            chargelis = new ArrayList();
            System.out.println("[ECOM] EcomBLBean inside getBLCharges");
            Map map = (Map) blVDS.getEcomBLCharges(blno);
            System.out.println("[ECOM] EcomBLBean ######>" + map);
            List lst = (List) map.get("bl_chargenew");
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    map = (Map) lst.get(i);
                    charge = new ChargesVo();
                    charge.setBook_no((String) map.get("bl_no"));
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
    public String getBLAPCustomer(String blno) {
        String custname = null;
        try {
            System.out.println("[ECOM] EcomBLBean getBLAPCustomer===> " + blno);
            CompleteModel blModelobj = blVDS.getecomblcust(blno);
            for (Bl_cust blcustObj : blModelobj.getBl_cust()) {
                if ("AP".equalsIgnoreCase(Validation.checkNull(blcustObj.getCusttypeid()))) {
                    custname = Validation.checkNull(blcustObj.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return custname;
    }

    public String getStateCode(String state, String countrycode, String book_no) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println("[ECOM] EcomBLBean getStateCode  -- Book_no=" + book_no + "  State=> " + state
                    + "  countrycode==> " + countrycode);
            if (state != null && checkNull(state).length() > 0 && checkNull(state).contains("-")) {
                state = state.split("-")[0].trim();
            }
            if (state != null && checkNull(state).length() > 2) {
                con = new lrp.serverutils.refs.ServerObject("MASTERDB").getConnection();
                String query = "select statecode from Statesmaster where statedesc = '" + state + "' and countrycode='"
                        + countrycode + "'";
                System.out.println("[ECOM] EcomBLBean Query => " + query);
                ps = con.prepareStatement(query, 1004, 1007);
                rs = ps.executeQuery();
                if (rs.next()) {
                    state = checkNull(rs.getString("statecode"));
                } else {
                    state = "";
                }
            }
            System.out.println("[ECOM] EcomBLBean result----> " + state);
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

    private Map getInsuranceDetils(String blno) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map insmap = null;
        try {
            System.out.println("[ECOM] EcomBLBean getInsuranceDetils==> ");
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

            ps = con.prepareStatement(
                    "select commodity,quoteno,partytype,suminsured,email from ecom_insurance where lrpbl_no=?");
            ps.setString(1, checkNull(blno));
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
            System.out.println("[ECOM] EcomBLBean insmap===> " + insmap);
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
    public Map<String, String> getBLStatus(String blno) {
        Map<String, String> finalMap = new HashMap<>();
        try {
            System.out.println("[ECOM] EcomBLBean inside getBLStatus" + blno);
            Map<String, Object> refMap = blVDS.getecomstatusfrombl(blno);
            System.out.println("[ECOM] EcomBLBean >>refMap>> " + refMap);
            if (refMap != null && !refMap.isEmpty()) {
                List<Map<String, Object>> refNoList = (List<Map<String, Object>>) refMap.get("bl");
                if (refNoList != null && !refNoList.isEmpty()) {
                    for (Map<String, Object> rft : refNoList) {
                        String status = checkNull((String) rft.get("status"));
                        String bltype = checkNull((String) rft.get("bltype"));
                        String bl = checkNull((String) rft.get("bl_no"));
                        String result = status + "~~" + bltype;
                        finalMap.put(bl, result);
                    }
                }
            }
            System.out.println("[ECOM] EcomBLBean Final map => " + finalMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalMap;
    }

    @Override
    public String getMCNStatus(String fcnno) {
        String result = null, bltype = null, printeduser = null, status = null;
        try {
            System.out.println("[ECOM] EcomBLBean inside getMCNStatus");
            Map refMap = blVDS.getEcomMCNStatus(fcnno);
            System.out.println("[ECOM] EcomBLBean >>refMap>>" + refMap);
            if (refMap != null && refMap.size() > 0) {
                List refNoList = (List) refMap.get("fcn_bl");
                if (refNoList != null && refNoList.size() > 0) {
                    refMap = (Map) refNoList.get(0);
                    status = checkNull((String) refMap.get("status"));
                    bltype = checkNull((String) refMap.get("bltype"));
                    printeduser = checkNull((String) refMap.get("printeduser"));
                }
            }
            result = printeduser + "~~" + bltype + "~~" + status;
            System.out.println("[ECOM] EcomBLBean result=> " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

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
            System.out.println("[ECOM] EcomBLBean actualfile: " + actualfile + " &renamefile: " + renamefile);
            FileUtils.moveFile(file, rename);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getisvoyage(String blno) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;

        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

            System.out.println("[ECOM] EcomBLBean >>>>Blnoisvoyageclosed>>>>" + blno);

            ps = con.prepareStatement("select isvoyageclosed  from bl (nolock) where bl_no=?");
            ps.setString(1, checkNull(blno));
            rs = ps.executeQuery();
            if (rs.next()) {
                result = checkNull(rs.getString("isvoyageclosed"));
            }

            System.out.println("[ECOM] EcomBLBean isVoyageclosed==>" + result);

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
        return result;
    }

    @Override
    public HashMap fetchPortDetForMCN(String fcnno) throws RemoteException {
        HashMap hm = new HashMap();
        String bookNo = null, ccMailId = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        try {
            System.out.println("[ECOM] EcomBLBean Inside fetchPortDetForMCN");
            SIPartiesVO partiesVO = null;
            CompleteModel blModelObj = blVDS.getecomportdetformcn(fcnno);
            for (Vesselmaster vesselMasterObj : blModelObj.getVesselmaster()) {
                partiesVO = new SIPartiesVO();
                partiesVO.setVesselcode(vesselMasterObj.getVesselcode());
                partiesVO.setVoyagecode(vesselMasterObj.getVoyagecode());
                partiesVO.setBound(vesselMasterObj.getBound());
                partiesVO.setPod(vesselMasterObj.getDischargeportid());
                partiesVO.setPol(vesselMasterObj.getLoadportid());
                partiesVO.setIn_origin(vesselMasterObj.getOriginportid());
                partiesVO.setIn_delivery(vesselMasterObj.getDischargeportid());
                partiesVO.setReceipt(vesselMasterObj.getOriginportdesc());
                partiesVO.setDelivery(vesselMasterObj.getFinalportdesc());
                partiesVO.setVesselName(checkNull(vesselMasterObj.getVesselcode()).toUpperCase());
                partiesVO.setPolName(vesselMasterObj.getLoadportid());
                partiesVO.setPodName(vesselMasterObj.getDischargeportid());
                partiesVO.setAgencyRefNo(checkNull(vesselMasterObj.getAgencyRefNo()));
                partiesVO.setServicecode(vesselMasterObj.getServicecode());
                partiesVO.setBltype(checkNull(vesselMasterObj.getBltype()));
                //
                String str_org = checkNull(vesselMasterObj.getPrecarriage());
                String str_des = checkNull(vesselMasterObj.getOncarriage());
                partiesVO.setPolat(vesselMasterObj.getPolat());
                partiesVO.setPodat(vesselMasterObj.getPodat());
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
                bookNo = checkNull(vesselMasterObj.getBook_no());
                hm.put("routing", partiesVO);
                break;
            }
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GETECOM_BOOKNO);
            ps.setString(1, bookNo);
            rst = ps.executeQuery();
            if (rst.next()) {
                ccMailId = checkNull(rst.getString("ccmailid"));
            }
            hm.put("CCMAILID", ccMailId);
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
    public HashMap fetchBookingDetForMCN(String fcnno) throws RemoteException {
        HashMap hm = new HashMap();
        String custtype = "", custname = "", address1 = "", address2 = "", city = "", telephone = "", fax = "",
                email = "", custCode = "";
        try {
            System.out.println("[ECOM] EcomBLBean Inside fetchBookingDetForMCN");
            SIPartiesVO partiesVO = null;
            CompleteModel blModelobj = blVDS.getecomfcncust(fcnno);
            for (Bl_cust blcustObj : blModelobj.getFcn_bl_cust()) {
                partiesVO = new SIPartiesVO();
                custtype = Validation.checkNull(blcustObj.getCusttypeid());
                custCode = Validation.checkNull(blcustObj.getCustid());
                custname = Validation.checkNull(blcustObj.getName());
                address1 = Validation.checkNull(blcustObj.getAddress1());
                address2 = Validation.checkNull(blcustObj.getAddress2());
                city = Validation.checkNull(blcustObj.getCity());
                telephone = Validation.checkNull(blcustObj.getTelephone()).replace("+", "");
                if ("--".equalsIgnoreCase(telephone)) {
                    telephone = "";
                }
                fax = Validation.checkNull(blcustObj.getFax()).replace("+", "");
                if ("--".equalsIgnoreCase(fax)) {
                    fax = "";
                }
                email = Validation.checkNull(blcustObj.getEmail());
                if (email.length() > 0 && email.equalsIgnoreCase("-")) {
                    email = "";
                }
                partiesVO.setCustname(custname);
                partiesVO.setCustCode(custCode);
                partiesVO.setAddress1(address1);
                partiesVO.setAddress2(address2);
                partiesVO.setCity(city);
                partiesVO.setTelephone(telephone);
                partiesVO.setFax(fax);
                partiesVO.setEmail(email);
                partiesVO.setState(Validation.checkNull(blcustObj.getStateid()));
                partiesVO.setZipcode(Validation.checkNull(blcustObj.getZipcode()));
                partiesVO.setEori(Validation.checkNull(blcustObj.getEori()));
                partiesVO.setCountryid(Validation.checkNull(blcustObj.getCountryid()));
                hm.put(custtype, partiesVO);
            }
            System.out.println("[ECOM] EcomBLBean ===Fetching customer details from bl_cust table is done====");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hm;
    }

    @Override
    public Map<SIMasterVO, SIConatainerValueVO> fetchMCNContainerDetailForDraft(String fcnno) throws RemoteException {
        Map<SIMasterVO, SIConatainerValueVO> siCntHashObj = new LinkedHashMap<SIMasterVO, SIConatainerValueVO>();

        String volume = null, netweight = null;
        SIMasterVO siCntMasterVoObj = new SIMasterVO();
        SIConatainerValueVO siCntValVO = new SIConatainerValueVO();
        SISealVO siSealVO = new SISealVO();
        ArrayList<SIContainerDetailVO> cntDetailObj = new ArrayList<SIContainerDetailVO>();
        SIContainerDetailVO siCntDetailsVoObj = new SIContainerDetailVO();

        int i = 0;
        String strCntNo = "";
        System.out.println("[ECOM] EcomBLBean  fcnno " + fcnno);
        String soc = "";
        String prevCntr = null, currentCntr = null;
        int contDup = 0;
        boolean dupCont = false;
        DecimalFormat df = new DecimalFormat("#.###");
        Map insmap = null;
        String qouteno = null;
        try {
            System.out.println("[ECOM] EcomBLBean Inside fetchMCNContainerDetailForDraft");
            // insmap = getInsuranceDetils(fcnno);
            CompleteModel siModelCargoObj = blVDS.getecomfcncargodetails(fcnno);
            System.out.println("[ECOM] EcomBLBean >>>inner join cargo >>>" + siModelCargoObj);
            for (Bl_cargo bkngCargoObj : siModelCargoObj.getFcn_bl_cargo()) {
                if (contDup == 0) {
                    prevCntr = Validation.checkNull(bkngCargoObj.getEqpid());
                    currentCntr = Validation.checkNull(bkngCargoObj.getEqpid());
                }
                currentCntr = Validation.checkNull(bkngCargoObj.getEqpid());
                dupCont = prevCntr.equalsIgnoreCase(currentCntr) ? false : true;
                if (contDup == 0 || dupCont) {
                    System.out.println("[ECOM] EcomBLBean >>>enter count" + contDup);
                    prevCntr = currentCntr;
                    siCntMasterVoObj = new SIMasterVO();
                    siSealVO = new SISealVO();
                    siCntValVO = new SIConatainerValueVO();
                    strCntNo = Validation.checkNull(bkngCargoObj.getEqpid());
                    siCntMasterVoObj.setStrType(bkngCargoObj.getEqptypeid());
                    siCntMasterVoObj.setRateeqptypeid(bkngCargoObj.getRate_eqpTypeid());
                    siCntMasterVoObj.setStrEqpType(bkngCargoObj.getType());
                    siCntMasterVoObj.setStrCntNo(strCntNo);
                    soc = Validation.checkNull(bkngCargoObj.getShipperown());
                    if (soc != null && "Y".equalsIgnoreCase(soc)) {
                        siCntMasterVoObj.setStrsoc("Y");
                    } else {
                        siCntMasterVoObj.setStrsoc("N");
                    }
                    siCntMasterVoObj.setCargoNo(bkngCargoObj.getCargono() + "");
                    siCntMasterVoObj
                            .setCargoweight(ConvertDecimalFormat(String.valueOf(bkngCargoObj.getCargoweight())));
                    siCntMasterVoObj.setTareweight(ConvertDecimalFormat(String.valueOf(bkngCargoObj.getTareweight())));
                    siCntMasterVoObj
                            .setGrossweight(ConvertDecimalFormat(String.valueOf(bkngCargoObj.getGrossweight())));
                    siCntMasterVoObj.setTempMin(checkNull(bkngCargoObj.getTempmin()));
                    siCntMasterVoObj.setTempMax(checkNull(bkngCargoObj.getTempmax()));
                    siCntMasterVoObj.setSetTemp(checkNull(bkngCargoObj.getSettemp()));
                    siCntMasterVoObj.setTempunit(bkngCargoObj.getTempunit() + "");
                    siCntMasterVoObj.setEqpStatus(checkNull(bkngCargoObj.getEqpstatus()));
                    siCntMasterVoObj.setDgFlag(checkNull(bkngCargoObj.getDgflag()));

                    siCntMasterVoObj.setHumidity(checkNull(bkngCargoObj.getHumidity()));
                    siCntMasterVoObj.setHumidity_flag(checkNull(bkngCargoObj.getHumidity_flag()));
                    siCntMasterVoObj.setDrains(checkNull(bkngCargoObj.getDrains()));
                    siCntMasterVoObj.setCo2value(checkNull(bkngCargoObj.getCo2value()));
                    siCntMasterVoObj.setO2value(checkNull(bkngCargoObj.getO2value()));
                    siCntMasterVoObj.setVentilation(checkNull(bkngCargoObj.getVentpercentage()));
                    siCntMasterVoObj.setOog(bkngCargoObj.getOog());
                    siCntMasterVoObj.setNor(bkngCargoObj.getNor());
                    if (siCntMasterVoObj.getSetTemp() != null && siCntMasterVoObj.getSetTemp().length() > 0) {
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
                siCntDetailsVoObj.setStrQty(Validation.checkNull(bkngCargoObj.getItemqty() + ""));
                siCntDetailsVoObj.setHssuffixcode(bkngCargoObj.getHssuffixcode());
                String wightcargo = (bkngCargoObj.getItemweight() + "");
                Double wtn = Double.parseDouble(wightcargo);
                String wightcargowt = df.format(wtn);
                siCntDetailsVoObj.setStrWgt(ConvertDecimalFormat(wightcargowt));
                siCntDetailsVoObj.setStrUnit(Validation.checkNull(bkngCargoObj.getItemweightunit()));
                siCntDetailsVoObj.setStrPkg(Validation.checkNull(bkngCargoObj.getPackageid()));
                volume = bkngCargoObj.getItemvolume() + "";
                volume = checkNull(String.valueOf(bkngCargoObj.getItemvolume())).length() > 0
                        ? checkNull(String.valueOf(bkngCargoObj.getItemvolume()))
                        : "0";
                volume = String.valueOf(new BigDecimal(volume).toPlainString());
                System.out.println("[ECOM] EcomBLBean >>>volume>>" + volume + " Float.parseFloat(volume)>>"
                        + Float.parseFloat(volume));
                String volumeDouble = df.format(Float.parseFloat(volume));
                if (Float.parseFloat(volume) <= 0) {
                    volumeDouble = "";
                }
                siCntDetailsVoObj.setStrVol(volumeDouble);
                // netweight
                netweight = bkngCargoObj.getNettwt() + "";
                netweight = checkNull(String.valueOf(bkngCargoObj.getNettwt())).length() > 0
                        ? checkNull(String.valueOf(bkngCargoObj.getNettwt()))
                        : "0";
                netweight = String.valueOf(new BigDecimal(netweight).toPlainString());
                System.out.println("[ECOM] EcomBLBean >>>netweight>>" + netweight + "Float.parseFloat(netweight)>>"
                        + Float.parseFloat(netweight));
                String netWeightDouble = df.format(Float.parseFloat(netweight));
                if (Float.parseFloat(netweight) <= 0) {
                    netWeightDouble = "";
                }

                siCntDetailsVoObj.setNettwt(netWeightDouble);
                siCntDetailsVoObj.setStrVut(Validation.checkNull(bkngCargoObj.getItemvolumeunit()));
                siCntDetailsVoObj.setStrMrk(Validation.checkNull(bkngCargoObj.getMarksnumbers()));
                siCntDetailsVoObj.setStrDes(Validation.checkNull(bkngCargoObj.getMarksdesc()));
                siCntDetailsVoObj.setByscode(Validation.checkNull(bkngCargoObj.getBycode()));
                siCntDetailsVoObj.setBysname(Validation.checkNull(bkngCargoObj.getByname()));
                siCntDetailsVoObj.setSlscode(Validation.checkNull(bkngCargoObj.getSlcode()));
                siCntDetailsVoObj.setSlsname(Validation.checkNull(bkngCargoObj.getSlname()));
                siCntDetailsVoObj.setCmdty_hblno(Validation.checkNull(bkngCargoObj.getCmdty_hblno()));
                //
                siCntDetailsVoObj.setImophone(bkngCargoObj.getImophone());
                siCntDetailsVoObj.setImocontact(bkngCargoObj.getImocontact());
                siCntDetailsVoObj.setItemforward((bkngCargoObj.getItemforward() + ""));
                siCntDetailsVoObj.setItemtop((bkngCargoObj.getItemtop() + ""));
                siCntDetailsVoObj.setItemright((bkngCargoObj.getItemright() + ""));
                siCntDetailsVoObj.setItemaft((bkngCargoObj.getItemaft() + ""));
                siCntDetailsVoObj.setHazardous(bkngCargoObj.getHazardous());
                siCntDetailsVoObj.setItemlengthUnit(bkngCargoObj.getItemlengthunit());
                siCntDetailsVoObj.setItemleft((bkngCargoObj.getItemleft() + ""));
                //
                // String strPattern = "^0+";
                /*
                 * System.out.println("[ECOM] EcomBLBean ---->" +
                 * siCntDetailsVoObj.getStrCommodity());
                 * System.out.println("[ECOM] EcomBLBean ---->" +
                 * (insmap.containsKey((siCntDetailsVoObj.getStrCommodity()))));
                 * if (insmap != null) {
                 * siCntDetailsVoObj.setInsuredemail(checkNull((String)
                 * insmap.get("insuredemail")));
                 * if ((insmap.containsKey((siCntDetailsVoObj.getStrCommodity())) ||
                 * insmap.containsKey((siCntDetailsVoObj.getHsCode()))))
                 * {//.replaceAll(strPattern, "")
                 * qouteno = checkNull((String)
                 * insmap.get((siCntDetailsVoObj.getStrCommodity())));
                 * if (qouteno.length() == 0) {
                 * qouteno = checkNull((String) insmap.get((siCntDetailsVoObj.getHsCode())));
                 * }
                 * System.out.println("[ECOM] EcomBLBean qoute==> " + qouteno);
                 * siCntDetailsVoObj.setIsinsuranceavailable("Y");
                 * siCntDetailsVoObj.setQuoteno(qouteno);
                 * }
                 * }
                 */
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
            System.out.println(
                    "[ECOM] EcomBLBean ===Fetching container and commodity details from bl_cargo and bl_commodity table is done====");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return siCntHashObj;
    }

    @Override
    public SIPartiesVO getMCNMasterDetails(String fcnno) throws RemoteException {
        SIPartiesVO partiesVO = null;
        try {
            System.out.println("[ECOM] EcomBLBean inside getMCNMasterDetails");
            CompleteModel blModelObj = blVDS.getecomfcnheader(fcnno);
            for (Bl_header blheaderObj : blModelObj.getFcn_bl_header()) {
                partiesVO = new SIPartiesVO();
                partiesVO.setVslvoyage_free_txt(blheaderObj.getPrecarriage());
                partiesVO.setPol_free_txt(blheaderObj.getPol());
                partiesVO.setPod_free_txt(blheaderObj.getPod());
                partiesVO.setPla_free_txt(blheaderObj.getOrigin());
                partiesVO.setPld_free_txt(blheaderObj.getDelivery());
                partiesVO.setShipperName(checkNull(blheaderObj.getShipperName()));
                partiesVO.setShipperHeader(checkNull(blheaderObj.getShipper()));
                partiesVO.setConsigneeName(checkNull(blheaderObj.getConsigneeName()));
                partiesVO.setConsigneeHeader(checkNull(blheaderObj.getConsignee()));
                partiesVO.setNotifyName(checkNull(blheaderObj.getNotifierName()));
                partiesVO.setNotifyHeader(checkNull(blheaderObj.getNotify()));
            }
            System.out.println("[ECOM] EcomBLBean ====fetching BL header details from bl_header is done========");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return partiesVO;
    }

    @Override
    public Map getMCNReference(String fcnno) throws RemoteException {
        Map result = null;
        List lis = null;
        Map map = null;
        try {
            System.out.println("[ECOM] EcomBLBean Inside getMCNReference");
            result = new HashMap();
            map = blVDS.getEcomFCNReference(fcnno, "AES," + EcomConsts.REF_TYPE);
            System.out.println("[ECOM] EcomBLBean getMCNReference--> " + map);
            if (map != null) {
                lis = (List) map.get("fcn_bl_reference");
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
    public boolean getBLServiceIsSignature(String agencycode) throws RemoteException {
        boolean signature = false;
        String value = null;
        List lst = null;
        Map hm = ecomvdsservice.getEcomBkgAgencyConfig("ENS", agencycode);
        lst = (List) hm.get("bkgagencyconfig");
        if (lst.size() > 0) {
            Map rs1 = (Map) lst.get(0);
            value = (String) rs1.get("value");
            signature = ("Y".equalsIgnoreCase(value) ? true : false);
        } else {
            hm = ecomvdsservice.getEcomBkgConfigDetails("ENS");
            lst = (List) hm.get("bkgconfig");
            if (lst.size() > 0) {
                Map rs1 = (Map) lst.get(0);
                value = (String) rs1.get("value");
                signature = ("Y".equalsIgnoreCase(value) ? true : false);
            }
        }
        return signature;
    }

    @Override
    public List<OBLPrintVo> getMCNSummary(String Custid, String custTypeConfig, String v1DataEnableStatus)
            throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs1 = null;
        String datestatus = "";
        List<OBLPrintVo> view = new ArrayList();
        OBLPrintVo eBooking = null;
        Map hs = null;
        List<BlSummaryVo> bl_lst = null;
        Gson gson = new Gson();
        String status = null, book_no = null;
        String blno = null;
        SvmJsonList alist = null;
        try {
            System.out.println("[ECOM] EcomBLBean  Custid is ===>" + Custid);
            SimpleDateFormat targer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date todate = DateConversion_New.convertaddDays(new Date(), 30);
            Date fromdate = DateConversion_New.convertsubDays(new Date(), 90);
            String to_date = targer.format(todate);
            String from_date = targer.format(fromdate);
            String date = from_date + "," + to_date;
            custTypeConfig = "AP,BP";
            //
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ArrayList lockedlis = new ArrayList();
            ps = con.prepareStatement("select bl_no from ecomsilock", 1004, 1007);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                lockedlis.add(checkNull(rs1.getString("bl_no")));
            }
            alist = new SvmJsonList();
            alist.add(Custid);
            alist.add(custTypeConfig);

            hs = (Map) ecomvdsservice.getecommcnsummarynew(alist);
            hs = (Map) hs.get("rs");
            if (hs != null && hs.size() > 0) {
                bl_lst = gson.fromJson(gson.toJson((ArrayList) hs.get("rs0")), new TypeToken<List<BlSummaryVo>>() {
                }.getType());
            }
            if (bl_lst.size() > 0) {
                for (int i = 0; i < bl_lst.size(); i++) {
                    BlSummaryVo rs = (BlSummaryVo) bl_lst.get(i);
                    eBooking = new OBLPrintVo();
                    eBooking.setBookNo(checkNull(rs.getBl_no()));
                    eBooking.setOriginPortId(checkNull(rs.getOriginportid()));
                    eBooking.setFinalPortId(checkNull(rs.getFinalportid()));
                    eBooking.setLoadPortID(checkNull(rs.getLoadportid()));
                    eBooking.setDischargePortId(checkNull(rs.getDischargeportid()));
                    eBooking.setCustid(Custid);
                    eBooking.setStatus(checkNull(rs.getStatus()));
                    eBooking.setPrintstatus(checkNull(rs.getEcomprntstatus()));
                    eBooking.setOriginals(checkNull(rs.getOriginals()));
                    eBooking.setShipmentstatus(checkNull(rs.getShipmentstatus()));
                    eBooking.setBook_no(checkNull(rs.getBook_no()));
                    eBooking.setCopies(checkNull(rs.getCopies()));
                    eBooking.setDatestatus(datestatus);
                    eBooking.setAgencyId(checkNull(rs.getAgencyissueid()));
                    eBooking.setAgencyRefNo(checkNull(rs.getAgencyrefno()));
                    eBooking.setEcomsino("");
                    eBooking.setDocumentalagencyid(checkNull(rs.getDocumentalagencyid()));
                    eBooking.setConfirmedDate(checkNull(rs.getConfirmeddate()));
                    eBooking.setAppVersion("2");
                    eBooking.setBlAcceptByUserStatus(checkNull(rs.getRefval()));
                    eBooking.setReceiptshipdate(checkNull(rs.getReceiptshipdate()));
                    System.out.println("[ECOM] EcomBLBean >>rs.getConfirmeddate()>>>" + rs.getConfirmeddate()
                            + ">>rs.getRefval()>>" + rs.getRefval() + "rs.getDatecr()" + rs.getDatecr());
                    eBooking.setCrdate(checkNull(rs.getDatecr()));
                    eBooking.setFcnverno(checkNull(rs.getFcnverno()));
                    eBooking.setFcnno(checkNull(rs.getFcnno()));
                    //
                    eBooking.setEcomsino(checkNull(rs.getEcomsino()));
                    //
                    if (checkNull(rs.getPolicy_no()).length() > 0) {
                        eBooking.setIsinsurance("Y");
                        eBooking.setPolicyno(checkNull(rs.getPolicy_no()));
                    }
                    //
                    blno = rs.getBl_no();
                    if (lockedlis != null && lockedlis.contains(checkNull(blno))) {
                        eBooking.setIssilocked("Y");
                    }
                    System.out.println("[ECOM] EcomBLBean rs.getMcnstatus()=> " + rs.getMcnstatus());
                    eBooking.setMcnstatus(checkNull(rs.getMcnstatus()));

                    /*
                     * hs = (Map) ecomvdsservice.getecomblsummaryfromeditemp(blno);
                     * List lst = (List) hs.get("editempbooking");
                     * if (lst.size() > 0) {
                     * Map bl = (Map) lst.get(0);
                     * status = (String) bl.get("status");
                     * book_no = (String) bl.get("book_no");
                     * if ("D".equalsIgnoreCase(eBooking.getStatus())) {
                     * eBooking.setStatus(status);
                     * eBooking.setEcomsino(book_no);
                     * }
                     * }
                     * ps = con.
                     * prepareStatement("select policy_no from ecom_insurance where policyStatus='Y' and lrpbl_no=?"
                     * , 1004, 1007);
                     * ps.setString(1, blno);
                     * rs1 = ps.executeQuery();
                     * if (rs1.next()) {
                     * eBooking.setIsinsurance("Y");
                     * eBooking.setPolicyno(checkNull(rs1.getString("policy_no")));
                     * }
                     */
                    view.add(eBooking);
                }
            }
            if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {
                con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
                ps = con.prepareStatement(V1_BL_SUMMARY_QRY, 1004, 1007);
                ps.setString(1, Custid);
                rs1 = ps.executeQuery();
                while (rs1.next()) {
                    eBooking = new OBLPrintVo();
                    eBooking.setBookNo(checkNull(rs1.getString("blno")));
                    blno = checkNull(rs1.getString("blno"));
                    eBooking.setOriginPortId(checkNull(rs1.getString("pla")));
                    eBooking.setFinalPortId(checkNull(rs1.getString("pld")));
                    eBooking.setLoadPortID(checkNull(rs1.getString("pol")));
                    eBooking.setDischargePortId(checkNull(rs1.getString("pod")));
                    eBooking.setCustid(Custid);
                    eBooking.setStatus(checkNull(rs1.getString("blstatus")));
                    eBooking.setPrintstatus(checkNull(rs1.getString("ecomPrntStatus")));
                    eBooking.setOriginals(checkNull(rs1.getString("originals")));
                    eBooking.setShipmentstatus(checkNull(rs1.getString("shipmentStatus")));
                    eBooking.setBook_no(checkNull(rs1.getString("bookno")));
                    eBooking.setCopies(checkNull(rs1.getString("copies")));
                    eBooking.setDatestatus(datestatus);
                    eBooking.setAgencyId(checkNull(rs1.getString("AgencyIssueid")));
                    eBooking.setEcomsino(checkNull(rs1.getString("ecomsino")));
                    eBooking.setAgencyRefNo(checkNull(""));
                    eBooking.setPolAt(checkNull(rs1.getString("polat")));
                    eBooking.setAppVersion("1");
                    view.add(eBooking);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs1 != null) {
                    rs1.close();
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
        System.out.println("[ECOM] EcomBLBean  view==>>" + view.size());
        return view;
    }

    @Override
    public String checkbloperationdetails(String bl_no) throws RemoteException {
        String rsl = "";
        Map map = null;
        List lis = null;
        try {
            if (!"".equalsIgnoreCase(checkNull(bl_no))) {
                System.out.println("[ECOM] EcomBLBean inside checkbloperationdetails");
                // map = blVDS.getbloperationdetail(blNo);
                map = blVDS.getbloperationdetail(bl_no);
                System.out.println("[ECOM] EcomBLBean getMCNReference--> " + map);
                if (map != null) {
                    lis = (List) map.get("bl");
                    if (lis != null && lis.size() > 0) {
                        for (int i = 0; i < lis.size(); i++) {
                            rsl = "Processed";
                        }
                    }
                } else {
                    rsl = "Unprocessed";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rsl;
    }

    @Override
    public ENSDeclarantVo getBLENSTabDetails(String blno) throws RemoteException {
        ENSDeclarantVo ensvo = null;
        try {
            System.out.println("[ECOM] EcomBLBean inside getBLENSTabDetails");
            Map map = (Map) blVDS.getEcomBLICSnew(blno);
            List lst = (List) map.get("bl_ics_new");
            if (lst != null && lst.size() > 0) {
                map = (Map) lst.get(0);
                ensvo = new ENSDeclarantVo();
                ensvo.setEuCustZone((String) map.get("eucustom"));
                ensvo.setBlTypeENS((String) map.get("bill_type"));
                ensvo.setEnsFillingType((String) map.get("ens_type"));
                ensvo.setPaymentMethod((String) map.get("payment_mode"));
                ensvo.setDeclarantName((String) map.get("cust_name"));
                ensvo.setDeclarantAddress((String) map.get("address"));
                ensvo.setDeclarantAddress1((String) map.get("address1"));
                ensvo.setDeclarantCity((String) map.get("city"));
                ensvo.setDeclarantCountry((String) map.get("country"));
                ensvo.setDeclarantState((String) map.get("state"));
                ensvo.setDeclarantTelephone((String) map.get("telephone"));
                ensvo.setDeclarantEORI((String) map.get("enseori"));
                ensvo.setDeclarantEmail((String) map.get("email"));
                ensvo.setDeclarantFillingType((String) map.get("filing_type"));
                ensvo.setDeclarantZipcode((String) map.get("zipcode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ensvo;
    }

    private boolean checkEUPort(String ports) {
        boolean iseuport = false;
        Map map = null;
        List lis = null;
        try {
            System.out.println("[ECOM] EcomBLBean inside checkEUPort");
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

    public List<BLChargeVo> getBlCgargeSummaryView(String blno) {
        Map map = null;
        List lis = null;
        BLChargeVo vo = null;
        List<BLChargeVo> ls = null;
        try {
            System.out.println("[ECOM] EcomBLBean getBlCgargeSummaryView BLNO=" + blno);
            map = blVDS.getblchangesummary(blno);
            if (map != null) {
                lis = (List) map.get("bl_chargenew");
                if (lis != null && lis.size() > 0) {
                    ls = new ArrayList<BLChargeVo>();
                    for (int i = 0; i < lis.size(); i++) {
                        map = (Map) lis.get(i);
                        vo = new BLChargeVo();
                        vo.setChargeId(checkNull((String) map.get("chargeid")));
                        vo.setCurrencyId(checkNull((String) map.get("currencyid")));
                        vo.setChargeName(checkNull((String) map.get("chargename")));
                        String amount = checkNull((String) map.get("chargeamount"));
                        BigDecimal chargeAmount = new BigDecimal(amount);
                        vo.setChargeAmount(ConvertDecimalFormat(chargeAmount.toPlainString()));
                        vo.setPrepaidCollect(checkNull((String) map.get("prepaidcollect")));
                        ls.add(vo);

                    }
                    // System.out.println("[ECOM] EcomBLBean ls==>" + ls.size());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ls;
    }

    public List<BLChargeVo> getBlCgargeSummaryList(String blno) {
        Map map = null;
        List lis = null;
        BLChargeVo vo = null;
        List<BLChargeVo> ls = null;
        String rate = null;
        String amount = null;
        BigDecimal decrate = null;
        BigDecimal chargeAmount = null;
        try {
            System.out.println("[ECOM] EcomBLBean getBlCgargeSummaryList BLNO=" + blno);
            map = blVDS.getSIblchargesummarylist(blno);
            if (map != null) {
                lis = (List) map.get("bl_chargenew");
                if (lis != null && lis.size() > 0) {
                    ls = new ArrayList<BLChargeVo>();
                    for (int i = 0; i < lis.size(); i++) {
                        map = (Map) lis.get(i);
                        vo = new BLChargeVo();
                        vo.setBaseRateUnit(checkNull((String) map.get("baserateunit")));
                        vo.setPayAgencyId(checkNull((String) map.get("payagencyid")));
                        vo.setChargeId(checkNull((String) map.get("chargeid")));
                        vo.setChargeName(checkNull((String) map.get("chargename")));
                        vo.setChargeRoe(checkNull((String) map.get("chargeroe")));
                        vo.setCurrencyId(checkNull((String) map.get("currencyid")));
                        // vo.setCustTypeId(checkNull((String) map.get("custtypeid")));
                        vo.setCustTypeId(checkNull((String) map.get("typedesc")));
                        vo.setEqpTypeId(checkNull((String) map.get("eqptypeid")));
                        rate = checkNull((String) map.get("baserate"));
                        decrate = new BigDecimal(rate);
                        vo.setBaseRate(ConvertDecimalFormat(decrate.toPlainString()));
                        amount = (String) map.get("chargeamount");
                        chargeAmount = new BigDecimal(amount);
                        vo.setChargeAmount(ConvertDecimalFormat(chargeAmount.toPlainString()));
                        vo.setPrepaidCollect(checkNull((String) map.get("prepaidcollect")));

                        ls.add(vo);

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ls;
    }

    public List<BLChargeVo> getSICgargeSummaryList(String bookno) {
        Map map = null;
        List lis = null;
        BLChargeVo vo = null;
        List<BLChargeVo> ls = null;
        String rate = null;
        String amount = null;
        BigDecimal decrate = null;
        BigDecimal chargeAmount = null;
        try {
            System.out.println("[ECOM] EcomBLBean  getSICgargeSummaryList BLNO=" + bookno);
            map = blVDS.getSIchargesummarylist(bookno);
            if (map != null) {
                lis = (List) map.get("booking_chargenew");
                if (lis != null && lis.size() > 0) {
                    ls = new ArrayList<BLChargeVo>();
                    for (int i = 0; i < lis.size(); i++) {
                        map = (Map) lis.get(i);
                        vo = new BLChargeVo();
                        vo.setBaseRateUnit(checkNull((String) map.get("baserateunit")));
                        vo.setPayAgencyId(checkNull((String) map.get("payagencyid")));
                        // vo.setChargeAmount(checkNull((String) map.get("chargeamount")));
                        vo.setChargeId(checkNull((String) map.get("chargeid")));
                        vo.setChargeName(checkNull((String) map.get("chargename")));
                        vo.setChargeRoe(checkNull((String) map.get("chargeqty")));
                        vo.setCurrencyId(checkNull((String) map.get("currencyid")));
                        // vo.setCustTypeId(checkNull((String) map.get("custtypeid")));
                        vo.setCustTypeId(checkNull((String) map.get("typedesc")));
                        vo.setEqpTypeId(checkNull((String) map.get("eqptypeid")));
                        rate = checkNull((String) map.get("baserate"));
                        decrate = new BigDecimal(rate);
                        vo.setBaseRate(ConvertDecimalFormat(decrate.toPlainString()));
                        amount = (String) map.get("chargeamount");
                        chargeAmount = new BigDecimal(amount);
                        vo.setChargeAmount(ConvertDecimalFormat(chargeAmount.toPlainString()));
                        vo.setPrepaidCollect(checkNull((String) map.get("prepaidcollect")));

                        ls.add(vo);

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ls;
    }

    public HashMap fetchBookingCustForSI(String blno) {
        HashMap<String, ArrayList<CustomerDetailsVO>> hm = new HashMap<>();
        ArrayList<CustomerDetailsVO> buyerList = new ArrayList<>();
        ArrayList<CustomerDetailsVO> sellerList = new ArrayList<>();
        System.out.println("fetchBookingCustForSI======>"+blno);
        try {
            CompleteModel blModelobj = blVDS.getecomblcust(blno);

            for (Bl_cust blcustObj : blModelobj.getBl_cust()) {
                CustomerDetailsVO partiesVO = new CustomerDetailsVO();
                String custtype = Validation.checkNull(blcustObj.getCusttypeid()).trim();
                String custCode = Validation.checkNull(blcustObj.getCustid());
                String custname = Validation.checkNull(blcustObj.getName());
                String address1 = Validation.checkNull(blcustObj.getAddress1());
                String address2 = Validation.checkNull(blcustObj.getAddress2());
                String city = Validation.checkNull(blcustObj.getCity());

                String telephone = Validation.checkNull(blcustObj.getTelephone()).replace("+", "");
                if ("--".equalsIgnoreCase(telephone)) {
                    telephone = "";
                }

                String fax = Validation.checkNull(blcustObj.getFax()).replace("+", "");
                if ("--".equalsIgnoreCase(fax)) {
                    fax = "";
                }

                String email = Validation.checkNull(blcustObj.getEmail());
                if (email.equalsIgnoreCase("-")) {
                    email = "";
                }

                partiesVO.setCustType(custtype);
                partiesVO.setName(custname);
                partiesVO.setCustCode(custCode);
                partiesVO.setAddress(address1);
                partiesVO.setAddress1(address2);
                partiesVO.setCity(city);
                partiesVO.setPhoneNo(telephone);
                partiesVO.setFax(fax);
                partiesVO.setEmail(email);
                partiesVO.setState(Validation.checkNull(blcustObj.getStateid()));
                partiesVO.setZipcode(Validation.checkNull(blcustObj.getZipcode()));
                partiesVO.setCountry(Validation.checkNull(blcustObj.getCountryid()));
                partiesVO.setCountryCode(Validation.checkNull(blcustObj.getCountryid()));
                partiesVO.setEroi(Validation.checkNull(blcustObj.getEori()));
                partiesVO.setPerontype(Validation.checkNull(blcustObj.getPersontype()));
                // Group into buyer/seller lists
                if ("BY".equalsIgnoreCase(custtype)) {
                    buyerList.add(partiesVO);
                } else if ("SL".equalsIgnoreCase(custtype)) {
                    sellerList.add(partiesVO);
                }
            }

            hm.put("Buyer", buyerList);
            hm.put("Seller", sellerList);

            System.out.println("[ECOM] EcomBLBean sellerList==="+sellerList.size());
            System.out.println("[ECOM] EcomBLBean === Fetching customer details from bl_cust table is done ==="+hm);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hm;
    }

    @Override
    public HashMap fetchSICustDetail(String bookno, String sino) throws RemoteException {

        HashMap<String, ArrayList<CustomerDetailsVO>> hm = new HashMap<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Lists for Buyer and Seller
        ArrayList<CustomerDetailsVO> buyerList = new ArrayList<>();
        ArrayList<CustomerDetailsVO> sellerList = new ArrayList<>();

        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            ps = con.prepareStatement(GET_ECOM_SI_CUST);
            ps.setString(1, checkNull(sino));
            rs = ps.executeQuery();

            while (rs.next()) {
                CustomerDetailsVO partiesVO = new CustomerDetailsVO();

                String custtype = checkNull(rs.getString("custtypeid"));
                partiesVO.setCustType(custtype);
                partiesVO.setName(checkNull(rs.getString("name")));
                partiesVO.setCustCode(checkNull(rs.getString("custid")));
                partiesVO.setAddress(checkNull(rs.getString("address1")));
                partiesVO.setAddress1(checkNull(rs.getString("address2")));
                partiesVO.setCity(checkNull(rs.getString("city")));
                partiesVO.setCountry(checkNull(rs.getString("countryid")));
                partiesVO.setCountryCode(checkNull(rs.getString("countryid")));
                partiesVO.setPhoneNo(checkNull(rs.getString("telephone")));
                partiesVO.setFax(checkNull(rs.getString("fax")));
                partiesVO.setEmail(checkNull(rs.getString("email")));
                partiesVO.setState(checkNull(rs.getString("stateid")));


                // Group by Buyer or Seller
                if ("BY".equalsIgnoreCase(custtype)) {
                    buyerList.add(partiesVO);
                } else if ("SL".equalsIgnoreCase(custtype)) {
                    sellerList.add(partiesVO);
                } else {
                    // Optional: handle other types if needed
                    System.out.println("[ECOM] Skipping ungrouped customer type: " + custtype);
                }
            }

            // Add lists to HashMap
            hm.put("Buyer", buyerList);
            hm.put("Seller", sellerList);

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
        return hm;
    }

    private void saveCustDetails(Map custMap, String book_refno) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            System.out.println("book_refno ==> " + book_refno + " | custMap ==> " + custMap);

            if (custMap != null && !custMap.isEmpty()) {
                con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
                con.setAutoCommit(false);  // Start transaction

                ps = con.prepareStatement(ECOM_SI_CUST_INSERT);

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

    public String fetForBLENSFiling(String blNum) throws RemoteException {
        System.out.println("[ECOM] EcomShippingInstructionBean ===inside fetchPortDetForSI============" + blNum);
        String ensFiling = null;
        try {
            CompleteModel blModelObj = blVDS.getecomportdetforbl(blNum);
            for (Vesselmaster vesselMasterObj : blModelObj.getVesselmaster()) {
                ensFiling = vesselMasterObj.getEnsfiling();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return ensFiling;
    }

    private String extractCountryCode(String input) {
        System.out.println("[Ecom] EcomBLBean extractCountryCode => "+input);
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
}
