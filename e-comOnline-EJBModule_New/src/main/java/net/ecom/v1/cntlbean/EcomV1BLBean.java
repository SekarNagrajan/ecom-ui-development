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
@Stateless(name = "EcomV1BLBean")
@Remote(EcomV1BLBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
})
public class EcomV1BLBean implements EcomV1BLBeanRemote {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private static final String BL_CUST_QRY = "select bc.custtypeid,bc.custid,bc.name as custname,bc.address1,bc.address2,bc.city,bc.countryid,bc.telephone, bc.fax, bc.email from bl_cust bc (nolock) where bc.bl_no=? order by bc.cid";
    private static final String BL_MASTER_DETAILS_QRY = "select precarriage,pol,pod,origin,delivery,shipper,consignee,notify From bl_header where bl_no=?";
    private static final String BL_CARGO_QRY = "select ca.eqptypeid,ca.eqpId,ca.grossWeight,ca.cargoWeight,ca.weightUnit,ca.tempMin,ca.tempMax,ca.setTemp,ca.oog,ca.tempunit,seal1No,seal2No,nor,shipperown,ca.cargoNo,ca.cargoweight,ca.tareweight,ca.grossweight from bl_cargo ca(nolock) where ca.bl_no=? order by cargono asc";
    private static final String BL_COMMDOITY_QRY = "select bc.eqpId,bc.commodityId, "
            + "bc.packageId,  bc.itemQty, bc.marksNumbers, bc.marksDesc, "
            + "bc.itemweight, bc.itemweightUnit, bc.itemVolume, bc.itemVolumeUnit,bc.itemleft,"
            + "bc.imocontact,bc.imophone,bc.itemforward,bc.itemtop,bc.itemright,bc.itemaft,"
            + "bc.itemlengthUnit,bc.hazardous from "
            + "bl_commodity bc(nolock) where bc.bl_no=? and bc.eqpid=?";
    private static final String PORT_DETAILS_QRY = "select top 1 bk.vesselCode , bk.voyageCode ,bk.bound, bk.loadPortId as pol,"
            + "bk.dischargePortId as pod,bk.originPortDesc as receipt, bk.finalPortDesc as delivery,"
            + "bk.book_no,vm.vesselname,p.portname as polName,p1.portname as podName,"
            + "bk.preCarriage  As transportOrigin,"
            + "bk.OnCarriage As transportDestination,bk.agencyrefno "
            //    + "(case when bk.preCarriage='M' then 'Merchant' else 'Carrier' end ) As transportOrigin,"
            //    + "(case when bk.OnCarriage='M' then 'Merchant' else 'Carrier' end ) As transportDestination "
            + "from bl bk (nolock) Inner join vesselmaster vm(nolock) on bk.vesselcode=vm.vesselcode "
            + "inner join ports p(nolock) on bk.loadPortId=p.portcode inner join ports p1(nolock) "
            + "on bk.dischargePortId=p1.portcode where bl_no=?";
    private static final String BOOK_NO_GEN = "select mdlid,mdlprefix,mdlindex from ecomMdlIndex where mdlid = ?";
    private static final String BOOK_NO_UPDATE = "update ecomMdlIndex set mdlindex=? "
            + "where mdlid =?";
    private static final String UPDATE_EDI_DOC_STATUS = "update edidocstatus set status='V' where book_no=?";
    private static final String SAVE_EDIBKG_HEADER = "insert into editempbooking_header(book_no,pol,pod,origin,delivery,precarriage,edireaderid,version,shipper,shippername,consignee,consigneename,notify,notifiername)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String str_masterdata_from_bl = "select documentalAgencyid,agencyid, oceanSerCode,oceanvoyage,oceanvslcode,oceanbound, serviceCode, polTerminal, polAt, podTerminal, podAt,"
            + " thirdparty, bookdeadline,transportserviceid,"
            + "preCarriage, onCarriage,usercr,"
            + "originportid,finalPortId,originportdesc,finalportdesc,agencyrefno  from bl(nolock) where book_no=?";
    private static final String str_editempbooking_cust = "insert into editempbooking_cust (book_no, custtypeid, custid, countryid, name,"
            + "address1, city, telephone, fax, email, edireaderid) values"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String str_editempbooking = "insert into editempbooking (book_no, agencyid, carrierCode, "
            + "oceanVslCode, oceanVoyage, oceanSerCode, oceanBound, serviceCode, vesselCode, voyageCode, bound, originPortId, "
            + "finalPortId, loadPortId, dischargePortId, polTerminal, polAt,  "
            + "preCarriage, onCarriage, status,usercr, datecr,messagetype,edireaderid,documentalAgencyId,agencyissueid,agencypolid,"
            + "tbook_no,messagesender, booktype,prepareops, preparebl,isdgbook, transportserviceid, callid,firstsvctype,"
            + "shipment_type, priority, booksource, crossbook, thirdparty,podterminal,podat,tbl_no,originportdesc,finalportdesc,custremarks,agencyrefno,intransitport,intransitportname) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
            + "?, ?, ?, ?, ?, ?,getdate(), ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String str_editempbooking_cargo = "insert into editempbooking_cargo (book_no, eqpstatus, isdummy,"
            + " type, eqptypeid, eqpid, seal2no,seal1no, "
            + " cargoweight, weightunit, ventsetting, loadingcondition, actstatus, edireaderid,shipperown,"
            + " tempmax,settemp,tempmin,tempunit,tareweight,grossweight,oog,dgflag,nor) values"
            + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String str_editempbooking_commodity = " insert into editempbooking_commodity(book_no,cargono,eqpId,"
            + "  commodityid, itemqty, itemWeight, itemweightUnit,"
            + " hazardous, alcohol, militarycargo, edireaderid, itemDesc,"
            + " packageId, itemVolume, itemVolumeUnit, marksNumbers, marksDesc,"
            + " itemleft, itemforward, itemtop, itemright, itemaft,itemlengthunit,comdtygrp,itemNo,carriercode)"
            + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String str_portQuery = "select top 1 portcode from ports(nolock) where portname=?";
    private static final String str_editempbooking_routing = "insert into editempbooking_routing(book_no,origin,"
            + " pol, polter, polser, polmode, pod, podTer,"
            + " delivery, svccode, defrout,edireaderid)"
            + " values(?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String strSQLQuery = "select custtypeid,custid,countryid,name,address1,city,telephone,fax,email from booking_cust(nolock) where custtypeid in('AP', 'FW') and  book_no=?";
    private static final String str_SOCQry = "select shipperown from booking_cargo where book_no =? ";
    private static final String str_edidocstatus = "insert into edidocstatus (edireaderid,servprovname,"
            + " receivdate, message, transtype, status, book_no,"
            + " ediparty)"
            + " values(?,?,getDate(),?,?,?,?,?)";
    private static final String str_edireadermaster = "insert into edireadermaster (ediid,edireaderid,datecr,requestdate,agencycode,locationcode, uploadstatus) values (?,?,getDate(),getDate(),?,?,?)";
    private static final String str_edimaster = "select ediid from edimaster(nolock) where ediname like 'ECOMONLINE SI%'";
    private static final String GET_EDIDOCSTATUS = "select status from edidocstatus where book_no=? order by receivdate desc";
    private static final String UPDATE_EDIDOCSTATUS = "update edidocstatus set status='V' where book_no=?";

    @Override

    public HashMap fetchV1BLDetForSI(String bookno) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap hm = new HashMap();
        ArrayList custtypelist = new ArrayList();
        String custtype = "", custname = "", address1 = "", address2 = "", city = "", telephone = "", fax = "", email = "", custCode = "";
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(BL_CUST_QRY, 1004, 1007);
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
                    //   //System.out.println("[ECOM] EcomV1BLBean custtypelist::::" + custtypelist + ":::::" + custtype);
                    if (custtypelist != null && custtypelist.size() <= 2) {
                        custtypelist.add(custtype);
                    }
                }
                if (custtype != null && ("CO").equalsIgnoreCase(custtype)) {
                    custtype = "CN";
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
    public SIPartiesVO getV1BLMasterDetails(String blno) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        SIPartiesVO partiesVO = null;
        try {
            //System.out.println("[ECOM] EcomV1BLBean entered in bl header...");
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(BL_MASTER_DETAILS_QRY, 1004, 1007);
            ps.setString(1, blno);
            rs = ps.executeQuery();
            while (rs.next()) {
                partiesVO = new SIPartiesVO();
                partiesVO.setVslvoyage_free_txt(rs.getString("precarriage"));
                partiesVO.setPol_free_txt(rs.getString("pol"));
                partiesVO.setPod_free_txt(rs.getString("pod"));
                partiesVO.setPla_free_txt(rs.getString("origin"));
                partiesVO.setPld_free_txt(rs.getString("delivery"));
                //partiesVO.setShipperName(checkNull(rs.getString("shippername")));
                partiesVO.setShipperHeader(checkNull(rs.getString("shipper")));
                //partiesVO.setConsigneeName(checkNull(rs.getString("consigeename")));
                partiesVO.setConsigneeHeader(checkNull(rs.getString("consignee")));
                //partiesVO.setNotifyName(checkNull(rs.getString("notifiername")));
                partiesVO.setNotifyHeader(checkNull(rs.getString("notify")));
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
        return partiesVO;
    }

    @Override
    public Map<SIMasterVO, SIConatainerValueVO> fetchContainerDetailForSI(String bookno) throws RemoteException {
        Map<SIMasterVO, SIConatainerValueVO> siCntHashObj = new LinkedHashMap<SIMasterVO, SIConatainerValueVO>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        String weight = null, volume = null;
        boolean checkCont = true;
        int packQty = 0;
        SIMasterVO siCntMasterVoObj = new SIMasterVO();
        SIConatainerValueVO siCntValVO = new SIConatainerValueVO();
        SISealVO siSealVO = new SISealVO();
        ArrayList<SIContainerDetailVO> cntDetailObj = new ArrayList<SIContainerDetailVO>();
        SIContainerDetailVO siCntDetailsVoObj = new SIContainerDetailVO();
        int i = 0;
        String strCntNo = "";
        String soc = "";
        DecimalFormat df = new DecimalFormat("#.##");
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(BL_CARGO_QRY, 1004, 1007);
            ps.setString(1, bookno);
            rs = ps.executeQuery();
            while (rs.next()) {
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
                String cargoweight = Validation1.checkNullfloat(rs.getString("cargoweight"));
                //  Double n = Double.parseDouble(cargoweight);
                //   String cargowt = df.format(n);
                siCntMasterVoObj.setCargoweight(cargoweight);
                siCntMasterVoObj.setTareweight(Validation1.checkNullfloat(rs.getString("tareweight")));
                siCntMasterVoObj.setGrossweight(Validation1.checkNullfloat(rs.getString("Grossweight")));

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
                siSealVO.setStrShipperSeal(Validation.checkNull(rs.getString("seal2No")));
                siSealVO.setStrCarrierSeal(Validation.checkNull(rs.getString("seal1no")));
                siSealVO.setStrIsSelect("NO");
                cntDetailObj = new ArrayList<SIContainerDetailVO>();
                if (strCntNo.length() > 0) {
                    ps1 = con.prepareStatement(BL_COMMDOITY_QRY, 1004, 1007);
                    ps1.setString(1, bookno);
                    ps1.setString(2, strCntNo);
                    rs1 = ps1.executeQuery();
                    while (rs1.next()) {
                        checkCont = false;
                        siCntDetailsVoObj = new SIContainerDetailVO();
                        siCntDetailsVoObj.setStrCommodity(Validation.checkNull(rs1.getString("commodityId")));
                        packQty = rs1.getInt("itemQty");
                        if (packQty > 0) {
                            siCntDetailsVoObj.setStrQty(String.valueOf(packQty));
                        } else {
                            siCntDetailsVoObj.setStrQty("");
                        }
                        //  weight = rs1.getString("itemweight") != null && !rs1.getString("itemweight").isEmpty() ? rs1.getString("itemweight") : "0.0";
                        String wightcargo = Validation1.checkNullfloat(rs1.getString("itemweight"));
                        Double wtn = Double.parseDouble(wightcargo);
                        String wightcargowt = null;
                        String eqpstatus = rs1.getString("eqpstatus");
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
                        //  siCntDetailsVoObj.setStrWgt(checkNull(String.valueOf(new BigDecimal(weight).toPlainString())));
                        siCntDetailsVoObj.setStrUnit(Validation.checkNull(rs1.getString("itemweightUnit")));
                        siCntDetailsVoObj.setStrPkg(Validation.checkNull(rs1.getString("packageId")));
                        volume = rs1.getString("itemVolume") != null && !rs1.getString("itemVolume").isEmpty() ? rs1.getString("itemVolume") : "0.0";
                        if (Float.parseFloat(volume) > 0) {
                            volume = String.valueOf(new BigDecimal(volume).toPlainString());
                        } else {
                            volume = "";
                        }
                        siCntDetailsVoObj.setStrVol(volume);
                        siCntDetailsVoObj.setStrVut(Validation.checkNull(rs1.getString("itemVolumeUnit")));
                        siCntDetailsVoObj.setStrMrk(Validation.checkNull(rs1.getString("marksNumbers")));
                        siCntDetailsVoObj.setStrDes(Validation.checkNull(rs1.getString("marksDesc")));
                        //
                        siCntDetailsVoObj.setImophone(rs1.getString("imophone"));
                        siCntDetailsVoObj.setImocontact(rs1.getString("imocontact"));
                        siCntDetailsVoObj.setItemforward(Validation1.checkNullfloat(rs1.getString("itemforward")));
                        siCntDetailsVoObj.setItemtop(Validation1.checkNullfloat(rs1.getString("itemtop")));
                        siCntDetailsVoObj.setItemright(Validation1.checkNullfloat(rs1.getString("itemright")));
                        siCntDetailsVoObj.setItemaft(Validation1.checkNullfloat(rs1.getString("itemaft")));
                        siCntDetailsVoObj.setHazardous(Validation1.checkNullfloat(rs1.getString("hazardous")));
                        siCntDetailsVoObj.setItemlengthUnit(rs1.getString("itemlengthUnit"));
                        siCntDetailsVoObj.setItemleft(Validation1.checkNullfloat(rs1.getString("itemleft")));
                        //
                        cntDetailObj.add(siCntDetailsVoObj);
                    }
                }
                if (checkCont) {
                    siCntDetailsVoObj = new SIContainerDetailVO();
                    cntDetailObj.add(siCntDetailsVoObj);
                }
                siCntValVO.setSiSealObj(siSealVO);
                siCntValVO.setCntDeatilsObj(cntDetailObj);
                siCntHashObj.put(siCntMasterVoObj, siCntValVO);
                i++;
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
    public HashMap fetchV1PortDetForBL(String bookno) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap hm = new HashMap();
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(PORT_DETAILS_QRY, 1004, 1007);
            ps.setString(1, bookno);
            rs = ps.executeQuery();
            SIPartiesVO partiesVO = null;
            while (rs.next()) {
                //System.out.println("[ECOM] EcomV1BLBean >>rs.getString(\"vesselCode\")>" + rs.getString("vesselCode"));
                partiesVO = new SIPartiesVO();
                partiesVO.setVesselcode(rs.getString("vesselCode"));
                partiesVO.setVoyagecode(rs.getString("voyageCode"));
                partiesVO.setBound(rs.getString("bound"));
                partiesVO.setPod(rs.getString("pod"));
                partiesVO.setPol(rs.getString("pol"));
                partiesVO.setReceipt(rs.getString("receipt"));
                partiesVO.setDelivery(rs.getString("delivery"));
                partiesVO.setAgencyRefNo(checkNull(rs.getString("agencyrefno")));
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
    public ReturnVO v1SaveToBL(String bookno, String custcode, String bookinguserid, Map<SIMasterVO, SIConatainerValueVO> cntMapObj, HashMap hm, SIPartiesVO partiesVO, String savetype, String sino, ArrayList voidCargoObj, ArrayList voidComObj, String username1, String tbl_no, String client_code, String addremarks) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ReturnVO returnVO = new ReturnVO();
        StringBuffer hssb = new StringBuffer();
        String cntno = "", strHsCode = null, cntType = "", seal1No = "", seal2No = "", commodity = "", cmdId = "", cmdDesc = "", wt = "", unit = "", qty = "", cid = "", cargo_id = "", eqp_type = "";
        int i = 0, alsize = 0;
        int cargoId = 0, cargoNo = 0, cntListSize = 0, cmdIndex = 0, pkgIndex = 0;
        String bknum = "";
        String agencyid = "", oceansercode = "", serviceCode = "", polTerminal = "", podTerminal = "";
        String oceanvslcode = null;
        String oceanvoyage = null;
        String oceanBound = null;
        String booktype = "", thirdparty = "", preCarriage = "", onCarriage = "", usercr = "";
        String transportserviceid = "", callid = "", firstsvctype = "", shipment_type = "", crossbook = "", prepareops = "";
        String preparebl = "", isdgbook = "", priority = "", booksource = "", originportid = "", finalPortId = "";
        String strDelivery = "", ediid = "";
        String packge = "", packgeId = "", packgeDesc = "", volume = "", volumeUnt = "", marks = "", description = "";
        savetype = checkNull(savetype);
        sino = checkNull(sino);
        String CarrierCode = "", hzdcheck = null;
        String strSOC = "", documentalAgencyid = "", soc = "";
        String originportdesc = "", finalportdesc = "", loadportdesc = "", dischargeportdesc = "";
        try {

            String polAtNEW = "";
            String podAtNEW = "";

            bknum = sino;
            String agencyRefNo = null, intransitPort = null, intransitPortName = null;
            String readerid = null;

            readerid = bookNoGenerate("ESI");
            //System.out.println("[ECOM] EcomV1BLBean bl readeerid::::" + readerid);

            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(UPDATE_EDI_DOC_STATUS);      // readid update
            ps.setString(1, bknum);
            ps.executeUpdate();

            ps = con.prepareStatement(str_masterdata_from_bl, 1004, 1007);
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

            ps = con.prepareStatement(str_editempbooking);
            ps.setString(1, bknum);
            ps.setString(2, agencyid);
            CarrierCode = EcomConsts.SET_CARRIER_CODE;
            ps.setString(3, checkNull(CarrierCode));
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
            ps.setString(43, tbl_no);
            ps.setString(44, originportdesc);
            ps.setString(45, finalportdesc);
            ps.setString(46, addremarks);
            ps.setString(47, agencyRefNo);
            ps.setString(48, intransitPort);
            ps.setString(49, intransitPortName);
            ps.executeUpdate();
            //
            //Parties
            ps = con.prepareStatement(str_editempbooking_cust);
            SISubmitVO objSH = (SISubmitVO) hm.get("SH");
            ps.setString(1, bknum);
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
            ps.executeUpdate();

            ps = con.prepareStatement(str_editempbooking_cust);
            SISubmitVO objCN = (SISubmitVO) hm.get("CN");
            ps.setString(1, bknum);
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
            ps.executeUpdate();

            ps = con.prepareStatement(str_editempbooking_cust);
            SISubmitVO objNT = (SISubmitVO) hm.get("NT");
            if (objNT != null && objNT.getCustname() != null && objNT.getCustname().length() > 0) {
                ps.setString(1, bknum);
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
                ps.executeUpdate();
            }
            //
            //Notify2
            ps = con.prepareStatement(str_editempbooking_cust);
            SISubmitVO objNT2 = (SISubmitVO) hm.get("N1");
            if (objNT2 != null && objNT2.getCustname() != null && objNT2.getCustname().length() > 0) {
                ps.setString(1, bknum);
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
                ps.executeUpdate();
            }
            //Notify3
            ps = con.prepareStatement(str_editempbooking_cust);
            SISubmitVO objNT3 = (SISubmitVO) hm.get("N2");
            if (objNT3 != null && objNT3.getCustname() != null && objNT3.getCustname().length() > 0) {
                ps.setString(1, bknum);
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
                ps.executeUpdate();
            }
//

            ps1 = con.prepareStatement(strSQLQuery, 1004, 1007);
            ps1.setString(1, bookno);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                ps = con.prepareStatement(str_editempbooking_cust);
                ps.setString(1, bknum);
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
                ps.executeUpdate();
            }
            ps = con.prepareStatement(str_SOCQry, 1004, 1007);
            ps.setString(1, bookno);
            rs = ps.executeQuery();
            if (rs.next()) {
                strSOC = rs.getString("shipperown");
            }
            alsize = 0;
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
                    cargoNo = 1;
                    eqp_type = "";
                    cntno = Validation.checkNull(siMasterVO.getStrCntNo());
                    cntType = Validation.checkNull(siMasterVO.getStrType());
                    seal2No = Validation.checkNull(siSealVOObj.getStrShipperSeal());
                    seal1No = Validation.checkNull(siSealVOObj.getStrCarrierSeal());
                    eqp_type = Validation.checkNull(siMasterVO.getStrEqpType());
                    //   String cargoget = getCargDet(cntno, bookno);
                    //   String cargogetSplit[] = cargoget.split("##");
                    String tempmax = Validation1.checkNullfloat(siMasterVO.getTempMax());
                    String settemp = Validation1.checkNullfloat(siMasterVO.getSetTemp());
                    String tempmin = Validation1.checkNullfloat(siMasterVO.getTempMin());
                    String tempunit = Validation.checkNull(siMasterVO.getTempunit());
                    String oog = Validation.checkNull(siMasterVO.getOog());
                    String ssoocc = Validation.checkNull(siMasterVO.getStrsoc());
                    String dgflag = "";
//                            if (cargogetSplit.length >= 3) {
//                                tempmax = cargogetSplit[0];
//                                settemp = cargogetSplit[1];
//                                tempmin = cargogetSplit[2];
//                                tempunit = cargogetSplit[3];
//                                oog = cargogetSplit[4];
//                                ssoocc = cargogetSplit[5];
//                                dgflag = "";
//                            }
//                            ArrayList carteareweight = getTareweight(cntno, bookno);
//                            String tareweigth = "";
//                            String grossweight = "";
//                            tareweigth = carteareweight.get(0).toString();
//                            grossweight = carteareweight.get(1).toString();

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
                    double grweight = Double.parseDouble(grossweigt);
                    //     double grweight = tot_wee + Double.parseDouble(tareweigth);
                    //System.out.println("[ECOM] EcomV1BLBean ssoocc: " + ssoocc + ", tempmax: " + tempmax + ", settemp: " + settemp + ", tempmin: " + tempmin
                         //   + ", tempunit: " + tempunit + ", tareweight: " + tareweigth + ", cargoweight: " + cargoweight + ", grossweight: " + grossweigt);
                    if ("Y".equalsIgnoreCase(siMasterVO.getRefer())) {
                        double tempmx = Double.parseDouble(tempmax);
                        double sttemp = Double.parseDouble(settemp);
                        double tmpmin = Double.parseDouble(tempmin);
                        tempmax = String.valueOf(tempmx);
                        settemp = String.valueOf(sttemp);
                        tempmin = String.valueOf(tmpmin);
                    } else {
                        tempmax = null;
                        settemp = null;
                        tempmin = null;
                    }

                    ps = con.prepareStatement(str_editempbooking_cargo, 1004, 1007);
                    ps.setString(1, bknum);
                    ps.setString(2, "F");
                    ps.setString(3, "Y");
                    ps.setString(4, checkNull(eqp_type));
                    ps.setString(5, checkNull(cntType));
                    ps.setString(6, checkNull(cntno));
                    ps.setString(7, checkNull(seal2No));
                    ps.setString(8, checkNull(seal1No));
                    ps.setString(9, Double.toString(cargowt));    // Double.toString(tot_wee)
                    ps.setString(10, checkNull(unit));
                    ps.setInt(11, 0);
                    ps.setString(12, "FF");
                    ps.setString(13, "0");
                    ps.setString(14, readerid);  ////  readerid changed instead of bknum
                    ps.setString(15, checkNull(ssoocc));
                    ps.setString(16, tempmax);    // tempmax
                    ps.setString(17, settemp);    // settemp
                    ps.setString(18, tempmin);    // tempmin
                    ps.setString(19, checkNull(tempunit));
                    ps.setString(20, Double.toString(tarewt));
                    ps.setString(21, Double.toString(grweight));
                    ps.setString(22, checkNull(oog));
                    ps.setString(23, checkNull(dgflag));
                    ps.setString(24, checkNull(siMasterVO.getNor()));
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
                                packgeDesc = "";
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
                                    packgeDesc = Validation.checkNull(packge.substring(0, (pkgIndex - 1)));
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
                                //       String getcommodityDet = getcommodityDeteel(cntno, bookno);
                                //       String commoditySplit[] = getcommodityDet.split("##");
                                String itemleft = Validation1.checkNullfloat(siCntDetVo.getItemleft());
                                String itemforward = Validation1.checkNullfloat(siCntDetVo.getItemforward());
                                String itemtop = Validation1.checkNullfloat(siCntDetVo.getItemtop());
                                String itemright = Validation1.checkNullfloat(siCntDetVo.getItemright());
                                String itemaft = Validation1.checkNullfloat(siCntDetVo.getItemaft());
                                String itemlengthunit = Validation.checkNull(siCntDetVo.getItemlengthUnit());
//                                        if (commoditySplit.length >= 5) {
//                                            itemleft = commoditySplit[0];
//                                            itemforward = commoditySplit[1];
//                                            itemtop = commoditySplit[2];
//                                            itemright = commoditySplit[3];
//                                            itemaft = commoditySplit[4];
//                                            itemlengthunit = commoditySplit[5];
//                                        }
                                //    String getHaz = getHazardousDeteel(cntno, bookno);
                                String getHaz = checkNull(siCntDetVo.getHazardous());
                                hzdcheck = Validation.checkNull(siCntDetVo.getHazardous());
                                if (!(hzdcheck.length() > 0)) {
                                    hzdcheck = "N";
                                }

                                double itleft = Double.parseDouble(itemleft);
                                double itforward = Double.parseDouble(itemforward);
                                double ittop = Double.parseDouble(itemtop);
                                double itright = Double.parseDouble(itemright);
                                double itaft = Double.parseDouble(itemaft);

                                ps = con.prepareStatement(str_editempbooking_commodity);
                                ps.setString(1, bknum);
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
            ps = con.prepareStatement(SAVE_EDIBKG_HEADER);
            ps.setString(1, bknum);
            ps.setString(2, partiesVO.getPol_free_txt());
            ps.setString(3, partiesVO.getPod_free_txt());
            ps.setString(4, partiesVO.getPla_free_txt());
            ps.setString(5, partiesVO.getPld_free_txt());
            ps.setString(6, partiesVO.getVoyage_free_txt());
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
            ps = con.prepareStatement(str_portQuery, 1004, 1007);
            ps.setString(1, partiesVO.getDelivery());
            rs = ps.executeQuery();
            if (rs.next()) {
                strDelivery = checkNull(rs.getString("portcode"));
            }
            ps = con.prepareStatement(str_editempbooking_routing);
            ps.setString(1, bknum);
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

            ps = con.prepareStatement(str_edidocstatus);
            ps.setString(1, readerid);
            ps.setString(2, GeneralConf.EDI_ECOM);
            ps.setString(3, GeneralConf.EDI_MESSAGETYPE_SI);
            ps.setString(4, "5");
            ps.setString(5, "R");
            ps.setString(6, bknum);
            ps.setString(7, GeneralConf.EDI_TYPE_SI);
            ps.executeUpdate();

            String EBOOKING_REMARKS = "insert into editempbooking_remarks(book_no,remarksref,remarksval,edireaderid,version,category) values(?,?,?,?,?,?)";
            // Insert into EDI Remarks
            ps = con.prepareStatement(EBOOKING_REMARKS);

            ps.setString(1, bknum);
            ps.setString(2, "Submitted By");
            ps.setString(3, username1);
            ps.setString(4, readerid);//agencycode
            ps.setString(5, "0");
            ps.setString(6, null);
            ps.executeUpdate();

            ps = con.prepareStatement(EBOOKING_REMARKS);
            ps.setString(1, bknum);
            ps.setString(2, "Online Reference No");
            ps.setString(3, bknum);
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
            ps = con.prepareStatement(str_edimaster, 1004, 1007);
            rs = ps.executeQuery();
            if (rs.next()) {
                ediid = checkNull(rs.getString("ediid"));
            }
            ps = con.prepareStatement(str_edireadermaster);
            ps.setString(1, ediid);
            ps.setString(2, readerid);
            ps.setString(3, agencyid);
            ps.setString(4, agencyid);
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

            //System.out.println("[ECOM] EcomV1BLBean ===========NEW bknum==========>>>>>>" + bkNum);

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
                //System.out.println("[ECOM] EcomV1BLBean exception while closing connection bookNoGenerate" + e);
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
            //System.out.println("[ECOM] EcomV1BLBean ******==>" + e);
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
                //System.out.println("[ECOM] EcomV1BLBean Exception While Closing Connection" + e.getMessage());
                e.printStackTrace();
            }
        }
        return email;
    }

    @Override
    public String cancelSubmittedV1BL(String siNo) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "Not Processed";
        String ediStatus = null;

        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(GET_EDIDOCSTATUS);
            ps.setString(1, siNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                ediStatus = rs.getString("status");
            }
            //System.out.println("[ECOM] EcomV1BLBean >>>edi status>>" + ediStatus);
            if ("R".equalsIgnoreCase(ediStatus)) {
                ps = con.prepareStatement(UPDATE_EDIDOCSTATUS);
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
    public String getV1BLAgencySigmature(String agency_code, String bookno, String shipstatus) throws RemoteException {
        String returnValue = "No";
        String output = "";
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int isLoadConfimed = 0;
        //Hashtable loadConfirmed = null;
        // String oblpath="Select EDIPath from fmcdocumentpath(Nolock) where FMCModule='OBLSignature'";
        String query = "select signaturefile from EslOBLSignature where agency=?";
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            pst = con.prepareStatement(query);
            pst.setString(1, agency_code);
            rs = pst.executeQuery();
            if (rs.next()) {
                returnValue = rs.getString("signaturefile");
            }
            //CRM bl validation
            if ("R".equalsIgnoreCase(shipstatus)) {
                isLoadConfimed = 0;
            } else {
                //loadConfirmed=new Hashtable();
                pst = con.prepareStatement("exec USP_ISLOADCONFIRMED ?", 1004, 1007);
                pst.setString(1, bookno);
                rs = pst.executeQuery();
                if (rs.next()) {
                    isLoadConfimed = 1;
                    //loadConfirmed.put(rs.getString(1), rs.getString(2));
                }
                //isLoadConfimed = loadConfirmed.size();
            }
            //
            output = returnValue + "~~" + isLoadConfimed;
            //System.out.println("[ECOM] EcomV1BLBean ===> Agency Code: " + agency_code + ", Result: " + returnValue + ", Output: " + output);
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
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return output;
    }

    @Override
    public String updateV1BLStatus(String blno, String type, String usercode) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "";
        String query = null;
        int Sh_update = 0;
        String printaccess = "select oblprint from customermaster where custcode=? ";
        //
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
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
                //System.out.println("[ECOM] EcomV1BLBean *********result******" + result);
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
                //System.out.println("[ECOM] EcomV1BLBean exception while closing connection bookNoGenerate" + e);
                e.printStackTrace();
            }
        }
        //System.out.println("[ECOM] EcomV1BLBean Print result==>>" + result);
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
