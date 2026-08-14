/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.v1.cntlbean;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.common.Validation1;
import net.ecom.utils.JDBCServiceLocatorFactory;
import net.ecom.vo.CustomerProfile;
import net.ecom.vo.MailFormatVO;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIContainerDetailVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;
import net.ecom.vo.SISealVO;
import net.ecom.vo.SISubmitVO;

/**
 *
 * @author deepa.p
 */
@Stateless(name = "EcomV1SIBean")
@Remote(EcomV1SIBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
})
public class EcomV1SIBean implements EcomV1SIBeanRemote {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private static final String GET_BKG_CARGO_COM_DET = "select ca.eqptypeid,ca.eqpId,ca.grossWeight,ca.cargoWeight,ca.weightUnit,ca.tempMin,ca.tempMax,ca.setTemp,ca.oog,"
            + "ca.tempunit,seal1No,seal2No,nor,shipperown,ca.cargoNo,ca.tareweight, "
            + "bc.commodityId,bc.packageId,  bc.itemQty, bc.marksNumbers, bc.marksDesc, "
            + "bc.itemweight, bc.itemweightUnit, bc.itemVolume, bc.itemVolumeUnit,bc.itemleft,"
            + "bc.imocontact,bc.imophone,bc.itemforward,bc.itemtop,bc.itemright,bc.itemaft,bc.itemlengthUnit,bc.hazardous "
            + "from Booking_cargo ca(nolock) left join  Booking_commodity bc(nolock) on ca.book_no=bc.book_no and ca.eqpId=bc.eqpId "
            + "where ca.book_no=? order by ca.cargono asc";
    private static final String CHECK_BL_STATUS = "select bl_no,status from bl where book_no=?";
    private static final String BOOK_NO_GEN = "select mdlid,mdlprefix,mdlindex from ecomMdlIndex where mdlid = ?";
    private static final String BOOK_NO_UPDATE = "update ecomMdlIndex set mdlindex=? "
            + "where mdlid =?";
    private static final String UPDATE_EDI_DOC_STATUS = "update edidocstatus set status='V' where book_no=?";
    private static final String SAVE_EDIBKG_HEADER = "insert into editempbooking_header(book_no,pol,pod,origin,delivery,precarriage,edireaderid,version,shipper,shippername,consignee,consigneename,notify,notifiername)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String GET_ECOM_SI_STATUS = "select status From ecomsi where esino=?";
    private static final String UPDATE_ECOM_SI_STATUS = "update ecomsi set status='I' where esino=?";
    private static final String DELETE_SI_REQUEST = "delete from ecomsirequestmaster where bookno=?";
    private static final String GET_EDIDOCSTATUS = "select status from edidocstatus where book_no=? order by receivdate desc";
    private static final String UPDATE_EDIDOCSTATUS = "update edidocstatus set status='V' where book_no=?";

    @Override
    public HashMap fetchV1BookingDetForSI(String bookno) throws RemoteException {

        ////System.out.println("[ECOM] EcomV1SIBean  *******fetchBookingDetForSI******");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap hm = new HashMap();
        ArrayList custtypelist = new ArrayList();
        String custtype = "", custname = "", address1 = "", address2 = "", city = "", telephone = "", fax = "", email = "", custCode = "";
        String query = "select bc.custtypeid,bc.custid,bc.name as custname,bc.address1,bc.address2,bc.city,bc.countryid,bc.telephone, bc.fax, bc.email from booking_cust bc (nolock) where bc.book_no=? order by bc.cid";
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(query, 1004, 1007);
            ps.setString(1, bookno);
            rs = ps.executeQuery();
            SIPartiesVO partiesVO = null;
            while (rs.next()) {
                partiesVO = new SIPartiesVO();
                custtype = Validation.checkNull(rs.getString("custtypeid"));
                if (custtype != null && ("NT").equalsIgnoreCase(custtype)) {
                    if (custtypelist != null && custtypelist.size() == 0) {
                        custtype = "NT";
                    } else if (custtypelist != null && custtypelist.size() == 1) {
                        custtype = "NT2";
                    } else if (custtypelist != null && custtypelist.size() == 2) {
                        custtype = "NT3";
                    }
                    //  //System.out.println("[ECOM] EcomV1SIBean  custtypelist::::"+custtypelist+":::::"+custtype);
                    if (custtypelist != null && custtypelist.size() <= 2) {
                        custtypelist.add(custtype);
                    }
                }
                custCode = Validation.checkNull(rs.getString("custid"));
                custname = Validation.checkNull(rs.getString("custname"));
                address1 = Validation.checkNull(rs.getString("address1"));
                address2 = Validation.checkNull(rs.getString("address2"));
                city = Validation.checkNull(rs.getString("city"));
                telephone = Validation.checkNull(rs.getString("telephone"));
                if ("--".equalsIgnoreCase(telephone)) {
                    telephone = "";
                }
                fax = Validation.checkNull(rs.getString("fax"));
                if ("--".equalsIgnoreCase(fax)) {
                    fax = "";
                }
                email = Validation.checkNull(rs.getString("email"));
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
                partiesVO.setCountryid(Validation.checkNull(rs.getString("countryid")));
                if (custtype != null && !("NT").equalsIgnoreCase(custtype)) {
                    hm.put(custtype, partiesVO);
                } else {
                    if (custtype != null && ("NT").equalsIgnoreCase(custtype) && custtypelist != null && custtypelist.size() < 3) {
                        hm.put(custtype, partiesVO);
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
        return hm;

    }

    @Override
    public Map<SIMasterVO, SIConatainerValueVO> fetchV1ContainerDetailForSI(String bookno) throws RemoteException {
        HashMap<SIMasterVO, SIConatainerValueVO> siCntHashObj = new LinkedHashMap<SIMasterVO, SIConatainerValueVO>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps1 = null;
        String weight = null, volume = null;

        SIMasterVO siCntMasterVoObj = new SIMasterVO();
        SIConatainerValueVO siCntValVO = new SIConatainerValueVO();
        SISealVO siSealVO = new SISealVO();
        ArrayList<SIContainerDetailVO> cntDetailObj = new ArrayList<SIContainerDetailVO>();
        SIContainerDetailVO siCntDetailsVoObj = new SIContainerDetailVO();
        boolean checkCommodity = true;
        int i = 0;
        int packQty = 0;
        String strCntNo = "";
        //System.out.println("[ECOM] EcomV1SIBean   book no " + bookno);
        String soc = "";
        String prevCntr = null, currentCntr = null;
        int contDup = 0;
        boolean dupCont = false;
        DecimalFormat df = new DecimalFormat("#.##");
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(GET_BKG_CARGO_COM_DET, 1004, 1007);
            ps.setString(1, bookno);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (contDup == 0) {
                    prevCntr = Validation.checkNull(rs.getString("eqpId"));
                    currentCntr = Validation.checkNull(rs.getString("eqpId"));
                }
                currentCntr = Validation.checkNull(rs.getString("eqpId"));
                dupCont = prevCntr.equalsIgnoreCase(currentCntr) ? false : true;
                if (contDup == 0 || dupCont) {
                    //System.out.println("[ECOM] EcomV1SIBean  >>>enter count" + contDup);
                    prevCntr = currentCntr;
                    siCntMasterVoObj = new SIMasterVO();
                    siSealVO = new SISealVO();
                    siCntValVO = new SIConatainerValueVO();
                    strCntNo = Validation.checkNull(rs.getString("eqpId"));
                    siCntMasterVoObj.setStrType(Validation.checkNull(rs.getString("eqptypeid")));
                    siCntMasterVoObj.setStrEqpType("");
                    siCntMasterVoObj.setStrCntNo(strCntNo);
                    soc = Validation.checkNull(rs.getString("shipperown"));
                    if (soc != null && "Y".equalsIgnoreCase(soc)) {
                        siCntMasterVoObj.setStrsoc("Y");
                    } else {
                        siCntMasterVoObj.setStrsoc("N");
                    }

                    //
                    siCntMasterVoObj.setCargoNo(rs.getString("cargoNo"));
                    String cargoweight = Validation1.checkNullfloat(rs.getString("cargoWeight"));

                    //  Double n = Double.parseDouble(cargoweight);
                    //  String cargowt = df.format(n);
                    siCntMasterVoObj.setCargoweight(cargoweight);
                    //  //System.out.println("[ECOM] EcomV1SIBean  cargo:::::::" + cargowt);
                    siCntMasterVoObj.setTareweight(Validation1.checkNullfloat(rs.getString("tareweight")));
                    siCntMasterVoObj.setGrossweight(Validation1.checkNullfloat(rs.getString("grossWeight")));

                    siCntMasterVoObj.setTempMin(Validation1.checkNullfloat(rs.getString("tempmin")));
                    siCntMasterVoObj.setTempMax(Validation1.checkNullfloat(rs.getString("tempMax")));
                    siCntMasterVoObj.setSetTemp(Validation1.checkNullfloat(rs.getString("settemp")));
                    siCntMasterVoObj.setTempunit(rs.getString("tempunit"));

                    siCntMasterVoObj.setOog(rs.getString("oog"));
                    siCntMasterVoObj.setNor(rs.getString("nor"));
                    if (rs.getString("settemp") != null && rs.getString("settemp").length() > 0) {
                        siCntMasterVoObj.setRefer("Y");
                    } else {
                        siCntMasterVoObj.setRefer("N");
                    }
//
                    siCntMasterVoObj.setStrSelect(i);
                    //System.out.println("[ECOM] EcomV1SIBean   shipperinbean " + rs.getString("seal2No") + " carrierbean " + rs.getString("seal1no"));
                    siSealVO.setStrShipperSeal(Validation.checkNull(rs.getString("seal2No")));
                    siSealVO.setStrCarrierSeal(Validation.checkNull(rs.getString("seal1no")));
                    siSealVO.setStrIsSelect("NO");
                    cntDetailObj = new ArrayList<SIContainerDetailVO>();

                }
                //System.out.println("[ECOM] EcomV1SIBean  ..strCntNo.. " + strCntNo);

                checkCommodity = false;
                siCntDetailsVoObj = new SIContainerDetailVO();
                siCntDetailsVoObj.setStrCommodity(Validation.checkNull(rs.getString("commodityId")));
                packQty = rs.getInt("itemQty");
                if (packQty > 0) {
                    siCntDetailsVoObj.setStrQty(String.valueOf(packQty));
                } else {
                    siCntDetailsVoObj.setStrQty("");
                }
                // weight = rs1.getString("itemweight") != null && !rs1.getString("itemweight").isEmpty() ? rs1.getString("itemweight") : "0.0";

                String wightcargo = Validation1.checkNullfloat(rs.getString("itemweight"));
                Double wtn = Double.parseDouble(wightcargo);
                String eqpstatus = rs.getString("eqpstatus");
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

                siCntDetailsVoObj.setStrWgt(wightcargowt);
                //System.out.println("[ECOM] EcomV1SIBean  cargo:::::::" + wightcargowt);

                // siCntDetailsVoObj.setStrWgt(checkNull(String.valueOf(new BigDecimal(weight).toPlainString())));
                siCntDetailsVoObj.setStrUnit(Validation.checkNull(rs.getString("itemweightUnit")));
                siCntDetailsVoObj.setStrPkg(Validation.checkNull(rs.getString("packageId")));
                volume = rs.getString("itemVolume") != null && !rs.getString("itemVolume").isEmpty() ? rs.getString("itemVolume") : "0.0";
                volume = String.valueOf(new BigDecimal(volume).toPlainString());
                if (Float.parseFloat(volume) <= 0) {
                    volume = "";
                }
                siCntDetailsVoObj.setStrVol(volume);
                siCntDetailsVoObj.setStrVut(Validation.checkNull(rs.getString("itemVolumeUnit")));
                siCntDetailsVoObj.setStrMrk(Validation.checkNull(rs.getString("marksNumbers")));
                siCntDetailsVoObj.setStrDes(Validation.checkNull(rs.getString("marksDesc")));
                //
                siCntDetailsVoObj.setImophone(rs.getString("imophone"));
                siCntDetailsVoObj.setImocontact(rs.getString("imocontact"));
                siCntDetailsVoObj.setItemforward(Validation1.checkNullfloat(rs.getString("itemforward")));
                siCntDetailsVoObj.setItemtop(Validation1.checkNullfloat(rs.getString("itemtop")));
                siCntDetailsVoObj.setItemright(Validation1.checkNullfloat(rs.getString("itemright")));
                siCntDetailsVoObj.setItemaft(Validation1.checkNullfloat(rs.getString("itemaft")));
                siCntDetailsVoObj.setHazardous(rs.getString("hazardous"));
                siCntDetailsVoObj.setItemlengthUnit(rs.getString("itemlengthUnit"));
                siCntDetailsVoObj.setItemleft(Validation1.checkNullfloat(rs.getString("itemleft")));
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
    public HashMap fetchPortDetForSI(String bookno) throws RemoteException {
        //System.out.println("[ECOM] EcomV1SIBean  >>print v1 book no>>" + bookno);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap hm = new HashMap();
        String portdetails = "select top 1 bk.vesselCode , bk.voyageCode ,bk.bound, bk.loadPortId as pol,"
                + "bk.dischargePortId as pod,bk.originPortDesc as receipt, bk.finalPortDesc as delivery,"
                + "bk.book_no,vm.vesselname,p.portname as polName,p1.portname as podName,"
                + "bk.preCarriage  As transportOrigin,"
                + "bk.OnCarriage As transportDestination,bk.agencyrefno "
                + "from booking bk (nolock) Inner join vesselmaster vm(nolock) on bk.vesselcode=vm.vesselcode "
                + "inner join ports p(nolock) on bk.loadPortId=p.portcode inner join ports p1(nolock) "
                + "on bk.dischargePortId=p1.portcode where book_no=?";
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(portdetails, 1004, 1007);
            ps.setString(1, bookno);
            rs = ps.executeQuery();
            SIPartiesVO partiesVO = null;

            while (rs.next()) {
                //System.out.println("[ECOM] EcomV1SIBean  >rs.getString(\"vesselCode\")>>" + rs.getString("vesselCode"));
                partiesVO = new SIPartiesVO();
                partiesVO.setVesselcode(rs.getString("vesselCode"));
                partiesVO.setVoyagecode(rs.getString("voyageCode"));
                partiesVO.setBound(rs.getString("bound"));
                partiesVO.setPod(rs.getString("pod"));
                partiesVO.setPol(rs.getString("pol"));
                partiesVO.setReceipt(rs.getString("receipt"));
                partiesVO.setAgencyRefNo(checkNull(rs.getString("agencyrefno")));
                partiesVO.setDelivery(rs.getString("delivery"));
                partiesVO.setVesselName(rs.getString("vesselname"));
                partiesVO.setPolName(rs.getString("polName"));
                partiesVO.setPodName(rs.getString("podName"));
                String str_org = checkNull(rs.getString("transportOrigin"));
                String str_des = checkNull(rs.getString("transportDestination"));
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
                hm.put("routing", partiesVO);
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
        return hm;

    }

    @Override
    public String checkV1BLStatus(String bookNo) throws RemoteException {
        String result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            result = "";
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(CHECK_BL_STATUS);
            ps.setString(1, bookNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = checkNull(rs.getString("status")).toUpperCase();
                if ("D".equalsIgnoreCase(result)) {
                    break;
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
                //System.out.println("[ECOM] EcomV1SIBean  exception while closing connection checkBLStatus" + e);
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public ReturnVO saveToSI(String bookno, String custcode, String bookinguserid, Map<SIMasterVO, SIConatainerValueVO> cntMapObj, HashMap hm, SIPartiesVO partiesVO, String savetype, String sino, ArrayList voidCargoObj, ArrayList voidComObj, String username1, String tbl_no, String client_code, String addremarks) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String str_SQLQRY = "";
        ReturnVO returnVO = new ReturnVO();
        String cntno = "", cntType = "", seal1No = "", seal2No = "", commodity = "", cmdId = "", cmdDesc = "", wt = "", unit = "", qty = "", cargo_id = "", eqp_type = "";
        int cargoId = 0, cargoNo = 0, cntListSize = 0, cmdIndex = 0, pkgIndex = 0;
        String agencyid = "", oceansercode = "", serviceCode = "", polTerminal = "", podTerminal = "";
        String oceanvslcode = null;
        String oceanvoyage = null;
        String oceanBound = null;
        String booktype = "", thirdparty = "", preCarriage = "", onCarriage = "", usercr = "";
        String agencyRefNo = null, intransitPort = null, intransitPortName = null;
        String transportserviceid = "", callid = "", firstsvctype = "", shipment_type = "", crossbook = "", prepareops = "";
        String preparebl = "", isdgbook = "", priority = "", booksource = "", originportid = "", finalPortId = "";
        String strSQLQuery = "", strDelivery = "", ediid = "";
        String packge = "", packgeId = "", volume = "", volumeUnt = "", marks = "", description = "";
        savetype = checkNull(savetype);
        sino = checkNull(sino);
        String strSOC = "", documentalAgencyid = "", strHsCode = null;
        StringBuffer hssb = new StringBuffer();
        String siSAVETYPE = "", originportdesc = "", finalportdesc = "", loadportdesc = "", dischargeportdesc = "", hzdcheck = null;
        try {
            siSAVETYPE = "S";
            String polAtNEW = "";
            String podAtNEW = "";
            String readerid = null;
            String blNo = null;
            readerid = readerid = bookNoGenerate("ESI");
            //System.out.println("[ECOM] EcomV1SIBean  si readeerid::::" + readerid);
            returnVO.setStrSaveType("S");
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(UPDATE_EDI_DOC_STATUS);      // readid update
            ps.setString(1, sino);
            ps.executeUpdate();
            //System.out.println("[ECOM] EcomV1SIBean  >>>blNo>>" + blNo);
            str_SQLQRY = "select documentalAgencyid,agencyid, oceanSerCode,oceanvoyage,oceanvslcode,oceanbound, serviceCode, polTerminal, polAt, podTerminal, podAt,"
                    + " booktype, thirdparty, bookdeadline,transportserviceid,"
                    + "preCarriage, onCarriage,usercr,"
                    + "originportid,finalPortId,originportdesc,finalportdesc,agencyrefno  from booking(nolock) where book_no=? and (status='c' or status='D')";
            ps = con.prepareStatement(str_SQLQRY, 1004, 1007);
            ps.setString(1, bookno);
            rs = ps.executeQuery();
            if (rs.next()) {
                documentalAgencyid = rs.getString("documentalAgencyid");
                agencyid = rs.getString("agencyid");
                oceansercode = rs.getString("oceanSerCode");
                oceanvoyage = rs.getString("oceanvoyage");
                oceanvslcode = rs.getString("oceanvslcode");
                oceanBound = rs.getString("oceanbound");
                serviceCode = rs.getString("serviceCode");
                polTerminal = rs.getString("polTerminal");
                polAtNEW = rs.getString("polAt");
                podTerminal = rs.getString("podTerminal");
                podAtNEW = rs.getString("podAt");
                booktype = rs.getString("booktype");
                thirdparty = rs.getString("thirdparty");
                transportserviceid = rs.getString("transportserviceid");
                preCarriage = rs.getString("preCarriage");
                onCarriage = rs.getString("onCarriage");
                usercr = rs.getString("usercr");
                originportid = rs.getString("originportid");
                finalPortId = rs.getString("finalPortId");
                originportdesc = rs.getString("originportdesc");
                finalportdesc = rs.getString("finalportdesc");
                agencyRefNo = checkNull(rs.getString("agencyrefno"));
                intransitPort = checkNull("");
                intransitPortName = checkNull("");
            }
            String getservicetype = "select servicetype from service where servicecode=?";
            ps = con.prepareStatement(getservicetype, 1004, 1007);
            ps.setString(1, serviceCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                firstsvctype = rs.getString("servicetype");
            }

            str_SQLQRY = "insert into editempbooking (book_no, agencyid, carrierCode, "
                    + "oceanVslCode, oceanVoyage, oceanSerCode, oceanBound, serviceCode, vesselCode, voyageCode, bound, originPortId, "
                    + "finalPortId, loadPortId, dischargePortId, polTerminal, polAt,  "
                    + "preCarriage, onCarriage, status,usercr, datecr,messagetype,edireaderid,documentalAgencyId,agencyissueid,agencypolid,"
                    + "tbook_no,messagesender, booktype,prepareops, preparebl,isdgbook, transportserviceid, callid,firstsvctype,"
                    + "shipment_type, priority, booksource, crossbook, thirdparty,podterminal,podat,tbl_no,originportdesc,finalportdesc,custremarks,agencyrefno,intransitport,intransitportname) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?,getdate(), ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(str_SQLQRY);
            ps.setString(1, sino);
            ps.setString(2, agencyid);
            ps.setString(3, EcomConsts.SET_CARRIER_CODE);
            ps.setString(4, oceanvslcode);
            ps.setString(5, oceanvoyage);
            ps.setString(6, oceansercode);
            ps.setString(7, oceanBound);
            ps.setString(8, serviceCode);
            ps.setString(9, partiesVO.getVesselcode());
            ps.setString(10, partiesVO.getVoyagecode());
            ps.setString(11, partiesVO.getBound());
            ps.setString(12, originportid);
            ps.setString(13, finalPortId);
            ps.setString(14, partiesVO.getPol());
            ps.setString(15, partiesVO.getPod());
            ps.setString(16, polTerminal);
            ps.setString(17, polAtNEW);
            ps.setString(18, preCarriage);
            ps.setString(19, onCarriage);
            ps.setString(20, "C");
            ps.setString(21, "Ecom");
            ps.setString(22, GeneralConf.EDI_MESSAGETYPE_SI);
            ps.setString(23, readerid);
            ps.setString(24, agencyid);
            ps.setString(25, agencyid);
            ps.setString(26, agencyid);
            ps.setString(27, bookno);
            ps.setString(28, "EcomOnlineSI");
            ps.setString(29, "online");
            ps.setString(30, prepareops);
            ps.setString(31, preparebl);
            ps.setString(32, isdgbook);
            ps.setString(33, transportserviceid);
            ps.setString(34, callid);
            ps.setString(35, firstsvctype);
            ps.setString(36, shipment_type);
            ps.setString(37, priority);
            ps.setString(38, "E");
            ps.setString(39, crossbook);
            ps.setString(40, thirdparty);
            ps.setString(41, podTerminal);
            ps.setString(42, podAtNEW);
            ps.setString(43, null);
            ps.setString(44, originportdesc);
            ps.setString(45, finalportdesc);
            ps.setString(46, addremarks);
            ps.setString(47, agencyRefNo);
            ps.setString(48, intransitPort);
            ps.setString(49, intransitPortName);
            ps.executeUpdate();

            //Parties
            str_SQLQRY = "insert into editempbooking_cust (book_no, custtypeid, custid, countryid, name,"
                    + "address1, city, telephone, fax, email, edireaderid,address2) values"
                    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            ps = con.prepareStatement(str_SQLQRY);
            SISubmitVO objSH = (SISubmitVO) hm.get("SH");
            ps.setString(1, sino);
            ps.setString(2, "SH");
            ps.setString(3, checkNull(objSH.getCustCode()));
            ps.setString(4, checkNull(objSH.getCountryid()));
            ps.setString(5, checkNull(objSH.getCustname()));
            ps.setString(6, checkNull(objSH.getAddress()));
            ps.setString(7, checkNull(objSH.getCity()));
            ps.setString(8, checkNull(objSH.getTelephone()));
            ps.setString(9, checkNull(objSH.getFax()));
            ps.setString(10, checkNull(objSH.getEmail()));
            ps.setString(11, readerid);
            ps.setString(12, checkNull(objSH.getAddress1()));
            ps.executeUpdate();

            ps = con.prepareStatement(str_SQLQRY);
            SISubmitVO objCN = (SISubmitVO) hm.get("CN");
            ps.setString(1, sino);
            ps.setString(2, "CO");
            ps.setString(3, checkNull(objCN.getCustCode()));
            ps.setString(4, checkNull(objCN.getCountryid()));
            ps.setString(5, checkNull(objCN.getCustname()));
            ps.setString(6, checkNull(objCN.getAddress()));
            ps.setString(7, checkNull(objCN.getCity()));
            ps.setString(8, checkNull(objCN.getTelephone()));
            ps.setString(9, checkNull(objCN.getFax()));
            ps.setString(10, checkNull(objCN.getEmail()));
            ps.setString(11, readerid);
            ps.setString(12, checkNull(objCN.getAddress1()));
            ps.executeUpdate();

            ps = con.prepareStatement(str_SQLQRY);
            SISubmitVO objNT = (SISubmitVO) hm.get("NT");
            if (objNT.getCustname() != null && objNT.getCustname().length() > 0) {
                ps.setString(1, sino);
                ps.setString(2, "NT");
                ps.setString(3, checkNull(objNT.getCustCode()));
                ps.setString(4, checkNull(objNT.getCountryid()));
                ps.setString(5, checkNull(objNT.getCustname()));
                ps.setString(6, checkNull(objNT.getAddress()));
                ps.setString(7, checkNull(objNT.getCity()));
                ps.setString(8, checkNull(objNT.getTelephone()));
                ps.setString(9, checkNull(objNT.getFax()));
                ps.setString(10, checkNull(objNT.getEmail()));
                ps.setString(11, readerid);
                ps.setString(12, checkNull(objNT.getAddress1()));
                ps.executeUpdate();
            }
            //Notify2
            ps = con.prepareStatement(str_SQLQRY);
            SISubmitVO objNT2 = (SISubmitVO) hm.get("N1");
            if (objNT2 != null && objNT2.getCustname() != null && objNT2.getCustname().length() > 0) {
                ps.setString(1, sino);
                ps.setString(2, "NT");
                ps.setString(3, checkNull(objNT2.getCustCode()));
                ps.setString(4, checkNull(objNT2.getCountryid()));
                ps.setString(5, checkNull(objNT2.getCustname()));
                ps.setString(6, checkNull(objNT2.getAddress()));
                ps.setString(7, checkNull(objNT2.getCity()));
                ps.setString(8, checkNull(objNT2.getTelephone()));
                ps.setString(9, checkNull(objNT2.getFax()));
                ps.setString(10, checkNull(objNT2.getEmail()));
                ps.setString(11, readerid);
                ps.setString(12, checkNull(objNT2.getAddress1()));
                ps.executeUpdate();
            }
            //Notify3
            ps = con.prepareStatement(str_SQLQRY);
            SISubmitVO objNT3 = (SISubmitVO) hm.get("N2");
            if (objNT3 != null && objNT3.getCustname() != null && objNT3.getCustname().length() > 0) {
                ps.setString(1, sino);
                ps.setString(2, "NT");
                ps.setString(3, checkNull(objNT3.getCustCode()));
                ps.setString(4, checkNull(objNT3.getCountryid()));
                ps.setString(5, checkNull(objNT3.getCustname()));
                ps.setString(6, checkNull(objNT3.getAddress()));
                ps.setString(7, checkNull(objNT3.getCity()));
                ps.setString(8, checkNull(objNT3.getTelephone()));
                ps.setString(9, checkNull(objNT3.getFax()));
                ps.setString(10, checkNull(objNT3.getEmail()));
                ps.setString(11, readerid);
                ps.setString(12, checkNull(objNT3.getAddress1()));
                ps.executeUpdate();
            }

            strSQLQuery = "select custtypeid,custid,countryid,name,address1,address2,city,telephone,fax,email from booking_cust(nolock) where custtypeid in('AP', 'FW') and  book_no=?";
            ps1 = con.prepareStatement(strSQLQuery, 1004, 1007);
            ps1.setString(1, bookno);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                ps = con.prepareStatement(str_SQLQRY);
                ps.setString(1, sino);
                ps.setString(2, rs1.getString("custtypeid"));
                ps.setString(3, rs1.getString("custid"));
                ps.setString(4, checkNull(rs1.getString("countryid")));
                ps.setString(5, checkNull(rs1.getString("name")));
                ps.setString(6, checkNull(rs1.getString("address1")));
                ps.setString(7, checkNull(rs1.getString("city")));
                ps.setString(8, checkNull(rs1.getString("telephone")));
                ps.setString(9, checkNull(rs1.getString("fax")));
                ps.setString(10, checkNull(rs1.getString("email")));
                ps.setString(11, readerid);
                ps.setString(12, checkNull(rs1.getString("address2")));
                ps.executeUpdate();
            }
            String str_SOCQry = "select shipperown from booking_cargo where book_no =? ";
            ps = con.prepareStatement(str_SOCQry, 1004, 1007);
            ps.setString(1, bookno);
            rs = ps.executeQuery();
            if (rs.next()) {
                strSOC = rs.getString("shipperown");
            }
            strSQLQuery = "insert into editempbooking_cargo (book_no, eqpstatus, isdummy,"
                    + " type, eqptypeid, eqpid, seal2no,seal1no, "
                    + " cargoweight, weightunit, ventsetting, loadingcondition, actstatus, edireaderid,shipperown,"
                    + " tempmax,settemp,tempmin,tempunit,tareweight,grossweight,oog,dgflag) values"
                    + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?)"; //cargo
            str_SQLQRY = " insert into editempbooking_commodity(book_no,cargono,eqpId,"
                    + "  commodityid, itemqty, itemWeight, itemweightUnit,"
                    + " hazardous, alcohol, militarycargo, edireaderid, itemDesc,"
                    + " packageId, itemVolume, itemVolumeUnit, marksNumbers, marksDesc,"
                    + " itemleft, itemforward, itemtop, itemright, itemaft,itemlengthunit,comdtygrp,itemNo,carriercode)"
                    + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; //commodity
            cargoId = 0;
            cargoNo = 1;
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
            hssb.append("");
            if (cntMapObj != null && cntMapObj.size() > 0) {
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
                    cargoNo = 1;
                    eqp_type = "";
                    cntno = Validation.checkNull(siMasterVO.getStrCntNo());
                    cntType = Validation.checkNull(siMasterVO.getStrType());

                    seal2No = Validation.checkNull(siSealVOObj.getStrShipperSeal());
                    seal1No = Validation.checkNull(siSealVOObj.getStrCarrierSeal());
                    eqp_type = Validation.checkNull(siMasterVO.getStrEqpType());
                    //String cargoget = getCargDet(cntno, bookno);
                    //String cargogetSplit[] = cargoget.split("##");
                    String tempmax = Validation1.checkNullfloat(siMasterVO.getTempMax());
                    String settemp = Validation1.checkNullfloat(siMasterVO.getSetTemp());
                    String tempmin = Validation1.checkNullfloat(siMasterVO.getTempMin());
                    String tempunit = Validation.checkNull(siMasterVO.getTempunit());
                    String oog = Validation.checkNull(siMasterVO.getOog());
                    String ssoocc = Validation.checkNull(siMasterVO.getStrsoc());
                    String dgflag = "";

                    /**
                     * ArrayList carteareweight = getTareweight(cntno, bookno);
                     * String tareweigth = ""; String grossweight = "";
                     * tareweigth = carteareweight.get(0).toString();
                     * grossweight = carteareweight.get(1).toString();*
                     */
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

                    //     double grweight = tot_wee + Double.parseDouble(tareweigth);
                    double grweight = Double.parseDouble(grossweigt);
//
                    //System.out.println("[ECOM] EcomV1SIBean  ssoocc " + ssoocc + " tareweight " + tarewt + " cargoweight " + cargowt + " grossweight " + grweight
                    //        + " oog " + oog + " dgflag " + dgflag + " soc_length " + ssoocc.length() + " readerid " + readerid);
                    double tempmx = Double.parseDouble(tempmax);
                    double sttemp = Double.parseDouble(settemp);
                    double tmpmin = Double.parseDouble(tempmin);
                    //System.out.println("[ECOM] EcomV1SIBean  tempmax " + tempmx + " settemp " + sttemp + " tempmin " + tmpmin + " tempunit " + tempunit
                    //        + " eqp_type " + eqp_type + " cntType " + cntType + " cntno " + cntno + " unit " + unit);
                    ps = con.prepareStatement(strSQLQuery, 1004, 1007);
                    ps.setString(1, sino);
                    ps.setString(2, "F");
                    ps.setString(3, "Y");
                    ps.setString(4, checkNull(eqp_type));
                    ps.setString(5, checkNull(cntType));
                    ps.setString(6, cntno);
                    ps.setString(7, checkNull(seal2No));
                    ps.setString(8, seal1No);
                    ps.setString(9, String.valueOf(cargowt));    // Double.toString(tot_wee)
                    ps.setString(10, checkNull(unit));
                    ps.setInt(11, 0);
                    ps.setString(12, "FF");
                    ps.setString(13, "0");
                    ps.setString(14, readerid);  ////  readerid changed instead of bknum
                    ps.setString(15, checkNull(ssoocc));
                    ps.setString(16, String.valueOf(tempmx));    // tempmax
                    ps.setString(17, String.valueOf(sttemp));    // settemp
                    ps.setString(18, String.valueOf(tmpmin));    // tempmin
                    ps.setString(19, checkNull(tempunit));
                    ps.setString(20, String.valueOf(tarewt));
                    ps.setString(21, String.valueOf(grweight));
                    ps.setString(22, checkNull(oog));
                    ps.setString(23, checkNull(dgflag));
                    int up = ps.executeUpdate();
                    if (up > 0) {
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
                                volume = "";
                                volumeUnt = "";
                                marks = "";
                                description = "";
                                pkgIndex = 0;
                                wt = Validation.checkNull(siCntDetVo.getStrWgt());
                                unit = Validation.checkNull(siCntDetVo.getStrUnit());
                                qty = Validation.checkNull(siCntDetVo.getStrQty());
                                commodity = Validation.checkNull(siCntDetVo.getStrCommodity());
                                packge = Validation.checkNull(siCntDetVo.getStrPkg());
                                volume = Validation.checkNull(siCntDetVo.getStrVol());
                                volumeUnt = Validation.checkNull(siCntDetVo.getStrVut());
                                marks = Validation.checkNull(siCntDetVo.getStrMrk());
                                description = Validation.checkNull(siCntDetVo.getStrDes());
                                if (packge.length() > 0) {
                                    pkgIndex = packge.lastIndexOf("~");
                                    packgeId = Validation.checkNull(packge.substring((pkgIndex + 1), packge.length()));
                                }
                                if (commodity.length() > 0) {
                                    cmdIndex = commodity.lastIndexOf("~");
                                    cmdDesc = Validation.checkNull(commodity.substring(0, (cmdIndex - 1)));
                                    cmdId = Validation.checkNull(commodity.substring((cmdIndex + 1), commodity.length()));
                                }
                                if (Validation.checkNull(siCntDetVo.getHsCode()).length() > 0) {
                                    hssb.append(cntno + " : " + siCntDetVo.getHsCode());
                                    hssb.append(" ");
                                }
                                //  String getcommodityDet = getcommodityDeteel(cntno, bookno);  // vasanth
                                //   String commoditySplit[] = getcommodityDet.split("##");    // vasanth
                                String itemleft = Validation1.checkNullfloat(siCntDetVo.getItemleft());
                                String itemforward = Validation1.checkNullfloat(siCntDetVo.getItemforward());
                                String itemtop = Validation1.checkNullfloat(siCntDetVo.getItemtop());
                                String itemright = Validation1.checkNullfloat(siCntDetVo.getItemright());
                                String itemaft = Validation1.checkNullfloat(siCntDetVo.getItemaft());
                                String itemlengthunit = Validation.checkNull(siCntDetVo.getItemlengthUnit());
                                hzdcheck = Validation.checkNull(siCntDetVo.getHazardous());
                                if (!(hzdcheck.length() > 0)) {
                                    hzdcheck = "N";
                                }
//                                        if (commoditySplit.length >= 5) {
//                                            itemleft = commoditySplit[0];
//                                            itemforward = commoditySplit[1];
//                                            itemtop = commoditySplit[2];
//                                            itemright = commoditySplit[3];
//                                            itemaft = commoditySplit[4];
//                                            itemlengthunit = commoditySplit[5];
//                                        }
                                //     String getHaz = getHazardousDeteel(cntno, bookno);  // vasanth
                                //System.out.println("[ECOM] EcomV1SIBean  get:::::" + siCntDetVo.getHazardous());
                                String getHaz = checkNull(siCntDetVo.getHazardous());
                                double itleft = Double.parseDouble(itemleft);
                                double itforward = Double.parseDouble(itemforward);
                                double ittop = Double.parseDouble(itemtop);
                                double itright = Double.parseDouble(itemright);
                                double itaft = Double.parseDouble(itemaft);

                                ps = con.prepareStatement(str_SQLQRY);
                                ps.setString(1, sino);
                                ps.setInt(2, cargoNo);
                                ps.setString(3, cntno);
                                ps.setString(4, cmdId);
                                ps.setString(5, qty);
                                ps.setString(6, wt);
                                ps.setString(7, unit);
                                ps.setString(8, hzdcheck);
                                ps.setString(9, "N");
                                ps.setString(10, "N");
                                ps.setString(11, readerid);  ////  readerid changed instead of bknum
                                ps.setString(12, cmdDesc);
                                ps.setString(13, packgeId);
                                ps.setString(14, volume);
                                ps.setString(15, volumeUnt);
                                ps.setString(16, marks.replace("\r", ""));
                                ps.setString(17, description.replace("\r", ""));
                                ps.setString(18, Double.toString(itleft));   // itemleft
                                ps.setString(19, Double.toString(itforward));   // itemforward
                                ps.setString(20, Double.toString(ittop));   // itemtop
                                ps.setString(21, Double.toString(itright));   // itemright
                                ps.setString(22, Double.toString(itaft));   // itemaft
                                ps.setString(23, checkNull(itemlengthunit));
                                ps.setString(24, cmdId);
                                ps.setString(25, "0");
                                ps.setString(26, client_code);
                                ps.executeUpdate();
                            }
                        }
                    }
                    cargoNo = cargoNo + 1;
                }
            }
            //
//                    String update_bookingHeader = "insert into editempbooking_header(book_no,edireaderid,origin,delivery,pol,pod,precarriage)values(?,?,?,?,?,?,?)";
//                    ps = con.prepareStatement(update_bookingHeader);
//                    ps.setString(1, bknum);
//                    ps.setString(2, bknum);
//                    ps.setString(3, originportdesc);
//                    ps.setString(4, finalportdesc);
//                    ps.setString(5, loadportdesc);
//                    ps.setString(6, dischargeportdesc);
//                    ps.setString(7, vesslename);
//                    ps.executeUpdate();
            //
            str_SQLQRY = "select top 1 portcode from ports(nolock) where portname=?";
            ps = con.prepareStatement(str_SQLQRY, 1004, 1007);
            ps.setString(1, partiesVO.getDelivery());
            rs = ps.executeQuery();
            if (rs.next()) {
                strDelivery = checkNull(rs.getString("portcode"));
            }
            str_SQLQRY = "insert into editempbooking_routing(book_no,origin,"
                    + " pol, polter, polser, polmode, pod, podTer,"
                    + " delivery, svccode, defrout,edireaderid)"
                    + " values(?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(str_SQLQRY);
            ps.setString(1, sino);
            ps.setString(2, originportid);
            ps.setString(3, partiesVO.getPol());
            ps.setString(4, polTerminal);
            ps.setString(5, serviceCode);
            ps.setString(6, "N");
            ps.setString(7, partiesVO.getPod());
            ps.setString(8, podTerminal);
            ps.setString(9, strDelivery);
            ps.setString(10, serviceCode);
            ps.setString(11, "N");
            ps.setString(12, readerid);
            ps.executeUpdate();
            //edidocstatus
            //headerinfo
            ps = con.prepareStatement(SAVE_EDIBKG_HEADER);
            ps.setString(1, sino);
            ps.setString(2, partiesVO.getPol_free_txt());
            ps.setString(3, partiesVO.getPod_free_txt());
            ps.setString(4, partiesVO.getPla_free_txt());
            ps.setString(5, partiesVO.getPld_free_txt());
            ps.setString(6, partiesVO.getVslvoyage_free_txt());
            ps.setString(7, readerid);
            ps.setString(8, "0");
            ps.setString(9, checkNull(partiesVO.getShipperHeader()));
            ps.setString(10, checkNull(partiesVO.getShipperName()));
            ps.setString(11, checkNull(partiesVO.getConsigneeHeader()));
            ps.setString(12, checkNull(partiesVO.getConsigneeName()));
            ps.setString(13, checkNull(partiesVO.getNotifyHeader()));
            ps.setString(14, checkNull(partiesVO.getNotifyName()));
            ps.executeUpdate();
            //
            str_SQLQRY = "insert into edidocstatus (edireaderid,servprovname,"
                    + " receivdate, message, transtype, status, book_no,"
                    + " ediparty)"
                    + " values(?,?,getDate(),?,?,?,?,?)";

            ps = con.prepareStatement(str_SQLQRY);
            ps.setString(1, readerid);
            ps.setString(2, GeneralConf.EDI_ECOM);
            ps.setString(3, GeneralConf.EDI_MESSAGETYPE_SI);
            ps.setString(4, "9");
            ps.setString(5, "R");
            ps.setString(6, sino);
            ps.setString(7, GeneralConf.EDI_TYPE_SI);
            ps.executeUpdate();
            String EBOOKING_REMARKS = "insert into editempbooking_remarks(book_no,remarksref,remarksval,edireaderid,version,category) values(?,?,?,?,?,?)";
            // Insert into EDI Remarks
            ps = con.prepareStatement(EBOOKING_REMARKS);

            ps.setString(1, sino);
            ps.setString(2, "Submitted By");
            ps.setString(3, username1);
            ps.setString(4, readerid);//agencycode
            ps.setString(5, "0");
            ps.setString(6, null);
            ps.executeUpdate();

            ps = con.prepareStatement(EBOOKING_REMARKS);
            ps.setString(1, sino);
            ps.setString(2, "Online Reference No");
            ps.setString(3, sino);
            ps.setString(4, readerid);//agencycode
            ps.setString(5, "0");
            ps.setString(6, null);
            ps.executeUpdate();

            //hs code remarks
            strHsCode = hssb.toString();
            if (strHsCode.length() > 0) {
                ps = con.prepareStatement(EBOOKING_REMARKS);
                ps.setString(1, sino);
                ps.setString(2, "HS Code  : ");
                ps.setString(3, strHsCode);
                ps.setString(4, readerid);//agencycode
                ps.setString(5, "0");
                ps.setString(6, null);
                ps.executeUpdate();
            }
            //edireadermaster
            str_SQLQRY = "select ediid from edimaster(nolock) where ediname like 'ECOMONLINE SI%'";
            ps = con.prepareStatement(str_SQLQRY, 1004, 1007);
            rs = ps.executeQuery();
            if (rs.next()) {
                ediid = checkNull(rs.getString("ediid"));
            }
            str_SQLQRY = "insert into edireadermaster (ediid,edireaderid,datecr,requestdate,agencycode,locationcode, uploadstatus) values (?,?,getDate(),getDate(),?,?,?)";
            ps = con.prepareStatement(str_SQLQRY);

            ps.setString(1, ediid);
            ps.setString(2, readerid);
            ps.setString(3, agencyid);
            ps.setString(4, agencyid);//agencycode
            ps.setString(5, "Completed");
            ps.executeUpdate();
            //agencyid

            String strAgencyCode = documentalAgencyid;
            String strAgency = getAgencyMail(strAgencyCode);
            if (strAgency != null && strAgency.length() > 0) {
                returnVO.setAdmToMailId(strAgency);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            returnVO.setStrRefNo("");
            returnVO.setStrDisplayMsg("Shipping Instruction not saved.");
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
        return returnVO;
    }

    private String bookNoGenerate(String module) {
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

            //System.out.println("[ECOM] EcomV1SIBean  ===========NEW bknum==========>>>>>>" + bkNum);

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
                //System.out.println("[ECOM] EcomV1SIBean  exception while closing connection bookNoGenerate" + e);
                e.printStackTrace();
            }

        }
        return bkNum;
    }

    private String getAgencyMail(String agencycode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String email = "";
        try {
            String strgetAgencyMailQry = "select agc.contactemailid from agencycontacts  agc inner join agency ag on agc.agencycode=ag.agencycode where contactdesignation='Ecom-Documentation' and ag.agencycode=?";
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(strgetAgencyMailQry);
            ps.setString(1, agencycode);
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                if (i == 0) {
                    email = rs.getString("contactemailid");
                } else {
                    email = email + "," + rs.getString("contactemailid");
                }
                i++;
            }
        } catch (Exception e) {
            //System.out.println("[ECOM] EcomV1SIBean  ******==>" + e);

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
                //System.out.println("[ECOM] EcomV1SIBean  Exception While Closing Connection" + e.getMessage());
                e.printStackTrace();

            }
        }
        return email;

    }

    @Override
    @SuppressWarnings("static-access")
    public List getV1SICustomerInfo(String columnName, String constraints, String columnValue, String custCode) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List parties = new ArrayList();
        String result = "";
        CustomerProfile profile = null;
        String phone = null, fax = null, email = null;
        String query = "exec usp_siCustomerSearch ?,?";
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            result = setConstraints(columnName, constraints, columnValue);
            ps = con.prepareStatement(query, 1004, 1007);
            ps.setString(1, custCode);
            ps.setString(2, result);
            //System.out.println("[ECOM] EcomV1SIBean  ************>>>>>>custCode" + custCode + "*>>>>>>result" + result);
            rs = ps.executeQuery();
            while (rs.next()) {
                profile = new CustomerProfile();
                profile.setCustId(checkNull(rs.getString("custID")));
                profile.setCustName(checkNull(rs.getString("name")));
                profile.setAddr1(checkNull(rs.getString("address1")));
                profile.setAddr2(checkNull(rs.getString("address2")));
                profile.setCity(checkNull(rs.getString("city")).toUpperCase());
                profile.setCountry(checkNull(rs.getString("countryId")).toUpperCase());
                profile.setCountryName(checkNull(rs.getString("countryName")).toUpperCase());
                profile.setTeleNo(checkNull(rs.getString("telephone")));
                profile.setFax(checkNull(rs.getString("fax")));
                phone = checkNull(rs.getString("telephone"));
                fax = checkNull(rs.getString("fax"));
                if (phone != null && phone.length() > 0 && !phone.equalsIgnoreCase("--") && !phone.equalsIgnoreCase("-")) {
                    profile.setTeleNo(phone);
                } else {
                    profile.setTeleNo("");
                }
                if (fax != null && fax.length() > 0 && !fax.equalsIgnoreCase("--") && !fax.equalsIgnoreCase("-")) {
                    profile.setFax(fax);
                } else {
                    profile.setFax("");
                }
                email = checkNull(rs.getString("email"));
                if (email != null && email.length() > 0 && email.equalsIgnoreCase("-")) {
                    email = "";
                }
                profile.setEmailId(email);
                profile.setCitycode(checkNull(rs.getString("city")).toUpperCase());  // Included by Lavanya M on 03/01/2009  Purpose: To set the city description
                parties.add(profile);
            }
        } catch (Exception e) {
            //System.out.println("[ECOM] EcomV1SIBean  Exception" + e);
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
            } catch (Exception es) {
                //System.out.println("[ECOM] EcomV1SIBean  Exception " + es);
            }
        }
        return parties;
    }

    @Override
    public String cancelSubmittedV1SI(String siNo, String bookNo) throws RemoteException {
        Connection con = null, v1lrp = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "Not Processed";
        String ediStatus = null, ecomStatus = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            v1lrp = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(GET_ECOM_SI_STATUS);
            ps.setString(1, siNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                ecomStatus = rs.getString("status");
            }
            ps = v1lrp.prepareStatement(GET_EDIDOCSTATUS);
            ps.setString(1, siNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                ediStatus = rs.getString("status");
            }
            //System.out.println("[ECOM] EcomV1SIBean  >>>edi status>>" + ediStatus+">>ecomStatus>>" + ecomStatus);
            if ("R".equalsIgnoreCase(ediStatus) && "S".equalsIgnoreCase(ecomStatus)) {
                ps = v1lrp.prepareStatement(UPDATE_EDIDOCSTATUS);
                ps.setString(1, siNo);
                ps.executeUpdate();
                ps = con.prepareStatement(UPDATE_ECOM_SI_STATUS);
                ps.setString(1, siNo);
                ps.executeUpdate();

                //Delete from SI request master
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
                if (v1lrp != null) {
                    v1lrp.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String setConstraints(String columnName, String con, String Val) throws RemoteException {
        String result = "";
        //
        ////System.out.println("[ECOM] EcomV1SIBean  Column name==>" + columnName + con + Val);
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

    private String checkNull(String str) {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }
}
