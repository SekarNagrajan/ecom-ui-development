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
import java.util.Hashtable;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import net.ecom.common.GeneralConf;
import net.ecom.utils.JDBCServiceLocatorFactory;
import net.ecom.vo.AmendmentBookingFormBean;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.Route;

/**
 *
 * @author deepa.p
 */
@Stateless(name = "EcomV1AmendmentBookingBean")
@Remote(EcomV1AmendmentBookingBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
})
public class EcomV1AmendmentBookingBean implements EcomV1AmendmentBookingBeanRemote {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private static final String getLRPBookingViewbookingMasterQry = "select b.book_no,servicecode,vesselcode,voyagecode,bound,originportid,finalportid,loadportid,dischargeportid,"
            + "originportdesc,finalportdesc,polterminal,podterminal,polat,podat,bookdate,precarriage,oncarriage,transportserviceid,'' as shipment_type,'' as refval,AgencyRefNo from "
            + "booking b where b.book_no=? ";
    private static final String getLRPBookingViewbookingCustQry = "select cid,book_no,custtypeid,custid,countryid,name,address1,address2,city,zipcode,telephone,fax,email from booking_cust where book_no=?";
    private static final String getLRPBookingRouting_qry = "select leg_no,bound,servicecode,vesselcode,loadportid,dischargeportid,etddate,etadate,voyagecode,legTypeId,groupcallid,routeid from booking_leg (nolock)  where book_no =?";
    private static final String LRPbookingCargoQry = "select book_no,oog,cargono,eqpstatus,eqptypeid,eqpid,shipperown,nor,tempmax,tempmin,settemp,tempunit,cargoweight,weightunit,tareweight,grossweight,"
            + "eqpvolume,eqpvolumeunit from booking_cargo where book_no = ?";
    private static final String LRPbookingComdtyQry = "select eqpid,commodityId,packageid,itemno,itemvolume,itemvolumeunit,itemqty,itemleft,itemforward,itemtop,itemright,itemaft,itemlengthunit,"
            + "hazardous from booking_commodity where eqpid=? and book_no = ?";
    private static final String GET_BKG_BREF_VALUES = "select refval from booking_reference where refid=? and book_no=?";
    private static final String GET_BKG_ROUTE_ID = "select routeid From booking_leg where book_no=?";
    private static final String GET_CUTOFFTERMINAL = "select arrdate from allschedules where groupcallid=?";
    private static final String EBOOKING_REMARKS = "insert into editempbooking_remarks(book_no,remarksref,remarksval,edireaderid,version,category) values(?,?,?,?,?,?)";
    private static final String BOOKING_ROUTING = "insert into editempbooking_routing (origin,originmode,pol,polter,polser,polmode,trans1,trans1ter,trans1ser,"
            + "trans1mode,trans2,trans2ter,trans2ser,trans2mode,trans3,trans3ter,trans3ser,trans3mode,"
            + "pod,podter,podmode,delivery,svccode,book_no,edireaderid,defRout)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String booking_leg_editemp = "insert into editempbooking_leg(book_no,leg_no,bound,servicecode,vesselcode,loadportid,dischargeportid,etddate,etadate,legTypeId,precarriage,oncarriage,loadtype,dischtype,voyagecode,dateup,edireaderid,version,previousLeg,groupcallid,routeid)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,getdate(),?,?,?,?,?)";
    private static final String BOOKING_CUST = "insert into editempbooking_cust(book_no, custTypeId,custId, "
            + "countryId, name, address1, city, zipCode, telephone, fax,email,edireaderid,address2) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?,?)";
    private static final String EBOOKING_CONT = "insert into editempbooking_cargo (book_no, type,eqptypeid,edireaderid,eqpId, cargono,oog,shipperown,cargoweight,"
            + "grossweight,weightunit,tempMax,tempMin,setTemp,tempUnit,"
            + "nor,ventSetting,loadingCondition,isDummy,dgflag,eqpstatus,orderno,tareWeight) "
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0,'FF','Y',?,?,?,?)";
    private static final String BOOK_NO_GEN = "select mdlid,mdlprefix,mdlindex from ecomMdlIndex where mdlid = ?";
    private static final String BOOK_NO_UPDATE = "update ecomMdlIndex set mdlindex=? "
            + "where mdlid =?";
    private static final String EDI_DOC_STATUS = "insert into edidocstatus (book_no,edireaderid,transtype,status,ediparty,receivdate,message,servprovname) values (?,?,?,?,?,getDate(),?,?)";
    private static final String UPDATE_EDI_DOC_STATUS = "update edidocstatus set status='V' where book_no=?";
    private static final String EDIREADER_MASTER = "insert into edireadermaster (ediid,edireaderid,datecr,requestdate,agencycode,locationcode,uploadstatus) values (?,?,getDate(),getDate(),?,?,?)";
    private static final String BOOKING_COMMODITY = "insert into editempbooking_commodity(book_no,commodityid,edireaderid,version,itemQty, "
            + "itemWeight,itemWeightUnit,hazardous,eqpid,Alcohol,militarycargo,cargono,"
            + "packageId,itemVolume,itemVolumeUnit,itemForward,itemLeft,itemTop,itemAft,itemRight,itemLengthUnit,comdtygrp,itemNo,carriercode,itemDesc) "
            + "values(?,?,?,?,?,?,?,?,?,'N','N',?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String EBOOKING_DG_DETAILS = "insert into editempbooking_imo(book_no,eqpid,eqptype,unno,shippingname,imdgclass,flashpoint,flashunit,marinepollutant,edireaderid)"
            + "values(?,?,?,?,?,?,?,?,?,?)";
    private static final String QrygetEDIID = "select ediid from edimaster(nolock) where ediname='ECOMONLINE BOOKING'";
    private static final String strgetAgencyMailQry = "select agc.contactemailid from agencycontacts  agc inner join agency ag on agc.agencycode=ag.agencycode where contactdesignation='Customer Service Executive' and ag.agencycode=?";
    private static final String strEqTrWQry = "select distinct TareKg,Equiptype,equipdesc from equipments(nolock) where equiptype=? and status='A'";
    private static final String GET_AGENCY_CODE = "select agencycode from agencylocationsnew where portcode=?";
    private static final String ServiceNameQRY = "select serviceType from service(nolock) where servicecode=?";
    private static final String GETROUTINGDETAILS = "SELECT r.origin, r.BLPOL, r.BLPOLTer, r.mode, r.BLPOLSvc, r.OpsPol1,r.OpsPol1Ter,"
            + "r.opsMode1, r.OpsPOLSVC1, r.OpsPol2, r.OpsPol2Ter, r.opsMode2, r.OpsPOLSVC2,r.OpsPol3, r.OpsPol3Ter, r.OpsMode3,"
            + "r.OpsPolSvc3, r.BlPod, r.BlPodTer, r.delivery, r.DefRoute, r.MainSvc,r.TransitTime,r.orgmode,r.delmode,r.id,r.datecr FROM Routedetail r(nolock) "
            + " WHERE r.id=? and r.status = 'A'";
    private static final String SAVE_EDI_TEMP_BOOKING_MASTER = "insert into editempbooking (book_no, agencyid,"
            + "oceanVslCode, oceanVoyage, oceanSerCode, oceanBound, serviceCode,vesselCode, voyageCode, bound, originPortId, "
            + "finalPortId, loadPortId, dischargePortId, polTerminal, polAt,podTerminal, podAt, bookDate, bookReady, "
            + "preCarriage, onCarriage, status,usercr, datecr,messagetype,transtype,edireaderid,documentalAgencyId,agencyissueid,agencypolid,remarks,firstsvctype,bookType,crossBook,originPortDesc,finalPortDesc,shipment_type,paymenttermsId,booksource,messagesender,carriercode,callid,custremarks,AgencyRefNo,transportserviceid,tbook_no,"
            + "intransitport,intransitportname) "
            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, getdate(), ?, ?, ?, ?, ?, getDate(),?,?,?,?,?,?,?,?,'online','',?,?,?,?,'E','EcomOnlineBkg','',?,?,?,?,?,?,?)";
    private static final String INSER_EDIBKG_REF = " insert into editempbooking_reference(book_no,refid,refval,isedited,edireaderid,version) values(?,?,?,?,?,?)";

    @Override
    public AmendmentBookingFormBean getV1LRPBookingMasterDetails(String bookNo) throws RemoteException {
        ArrayList viewlist = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String custType = null;
        boolean n1 = false;
        AmendmentBookingFormBean bookVO = null;
        try {
            viewlist = new ArrayList();
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(getLRPBookingViewbookingMasterQry, 1004, 1007);
            ps.setString(1, bookNo);
            rs = ps.executeQuery();
            bookVO = new AmendmentBookingFormBean();
            if (rs.next()) {
                bookVO.setEbook_date(checkNull(rs.getString("polat")));
                bookVO.setEbook_dateBut(checkNull(rs.getString("polat")));
                bookVO.setBooking_refno(checkNull(rs.getString("book_no")));
                //bookVO.setEmptyPickup(checkNull(rs.getString("polterminal")));
                bookVO.setEbook_terminal(checkNull(rs.getString("polterminal")));
                bookVO.setEbook_terminalpod(checkNull(rs.getString("podterminal")));
                bookVO.setEbook_origin_country(checkNull(rs.getString("originportid")));
                bookVO.setEbook_delivery_country(checkNull(rs.getString("finalportid")));
                bookVO.setEbook_origin(checkNull(rs.getString("originportid")));
                bookVO.setEbook_delivery(checkNull(rs.getString("finalportid")));
                bookVO.setEbook_pod_country(checkNull(rs.getString("dischargeportid")));
                bookVO.setEbook_pol_country(checkNull(rs.getString("loadportid")));
                bookVO.setEbook_pod(checkNull(rs.getString("dischargeportid")));
                bookVO.setEbook_pol(checkNull(rs.getString("loadportid")));
                bookVO.setEbook_portofloading(checkNull(rs.getString("loadportid")));
                bookVO.setPodintransit(checkNull("").trim());
                bookVO.setCarrierContact(checkNull(rs.getString("transportserviceid")));
                bookVO.setEbook_shipterm_desc(checkNull(rs.getString("shipment_type")));
                bookVO.setEbook_servicecode(checkNull(rs.getString("servicecode")));
                bookVO.setEbook_vesselcode(checkNull(rs.getString("vesselcode")));
                bookVO.setEbook_voyage(checkNull(rs.getString("voyagecode")));
                bookVO.setEbook_pol_eta(checkNull(rs.getString("podat")));
                bookVO.setEbook_pol_etd(checkNull(rs.getString("polat")));
                bookVO.setPreCarriage(checkNull(rs.getString("precarriage")));
                bookVO.setOnCarriage(checkNull(rs.getString("oncarriage")));
                bookVO.setEbook_rate_reference(checkNull(rs.getString("refval")));
                bookVO.setEbook_agency_reference(checkNullUpperCase(rs.getString("AgencyRefNo")));
                bookVO.setEbook_status_remarks(checkNull(""));
            }
            ps = con.prepareStatement(GET_BKG_BREF_VALUES, 1004, 1007);
            ps.setString(1, "EOR");
            ps.setString(2, bookNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                ////System.out.println("[ECOM] EcomV1AmendmentBookingBean   L>>ref values>>" + rs.getString("refval"));
                bookVO.setEbook_rate_reference(checkNull(rs.getString("refval")));
            }
            ps = con.prepareStatement(GET_BKG_BREF_VALUES, 1004, 1007);
            ps.setString(1, "EOF");
            ps.setString(2, bookNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                bookVO.setEbook_ocean_freight(checkNull(rs.getString("refval")));
            }
            ps = con.prepareStatement(GET_BKG_BREF_VALUES, 1004, 1007);
            ps.setString(1, "EEP");
            ps.setString(2, bookNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                bookVO.setEmptyPickup(checkNull(rs.getString("refval")));
            }
            ps = con.prepareStatement(GET_BKG_BREF_VALUES, 1004, 1007);
            ps.setString(1, "EPD");
            ps.setString(2, bookNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                bookVO.setEmptyPickUpDate(checkNull(rs.getString("refval")));
            }

            ps = con.prepareStatement(GET_BKG_ROUTE_ID, 1004, 1007);
            ps.setString(1, bookNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                bookVO.setEbook_routeid(rs.getString("routeid"));
            }
            ////System.out.println("[ECOM] EcomV1AmendmentBookingBean   >bookVO.setEbook_routeid>>" + bookVO.getEbook_routeid());
            ps = con.prepareStatement(getLRPBookingViewbookingCustQry, 1004, 1007);
            ps.setString(1, bookNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                custType = checkNull(rs.getString("custtypeid"));
                if (custType.equalsIgnoreCase("AP")) {
                    bookVO.setEbook_bookingparty_desc(checkNull(rs.getString("name")));
                    bookVO.setEbook_agreecity(checkNull(rs.getString("city")));
                    bookVO.setEbook_agreecity_desc(checkNull(rs.getString("city")));
                    bookVO.setEbook_agreecountry(checkNull(rs.getString("countryId")));
                    bookVO.setEbook_agreename(checkNull(rs.getString("name")));
                    bookVO.setEbook_agreecode(checkNull(rs.getString("custId")));
                    bookVO.setEbook_agreeaddress(checkNull(rs.getString("address1")));
                    bookVO.setEbook_agreeaddress1(checkNull(rs.getString("address2")));
                    bookVO.setEbook_agreephone(checkNull(rs.getString("telephone")));
                    bookVO.setEbook_agreeemail(checkNull(rs.getString("email")));
                    bookVO.setEbook_agreefax(checkNull(rs.getString("fax")));
                    bookVO.setEbook_agree_id(checkNull(rs.getString("cid")));
                } else if (custType.equalsIgnoreCase("SH")) {
                    bookVO.setEbook_shippercity(checkNull(rs.getString("city")));
                    bookVO.setEbook_shippercity_desc(checkNull(rs.getString("city")));
                    bookVO.setEbook_shippercountry(checkNull(rs.getString("countryId")));
                    bookVO.setEbook_shippername(checkNull(rs.getString("name")));
                    bookVO.setEbook_shippercode(checkNull(rs.getString("custId")));
                    bookVO.setEbook_shipperaddress(checkNull(rs.getString("address1")));
                    bookVO.setEbook_shipperaddress1(checkNull(rs.getString("address2")));
                    bookVO.setEbook_shipperphone(checkNull(rs.getString("telephone")));
                    bookVO.setEbook_shipperemail(checkNull(rs.getString("email")));
                    bookVO.setEbook_shipperfax(checkNull(rs.getString("fax")));
                    bookVO.setEbook_shipper_id(checkNull(rs.getString("cid")));

                } else if (custType.equalsIgnoreCase("CO")) {
                    bookVO.setEbook_consigneecity(checkNull(rs.getString("city")));
                    bookVO.setEbook_consigneecity_desc(checkNull(rs.getString("city")));
                    bookVO.setEbook_consigneecountry(checkNull(rs.getString("countryId")));
                    bookVO.setEbook_consigneename(checkNull(rs.getString("name")));
                    bookVO.setEbook_consigneecode(checkNull(rs.getString("custId")));
                    bookVO.setEbook_consigneeaddress(checkNull(rs.getString("address1")));
                    bookVO.setEbook_consigneeaddress1(checkNull(rs.getString("address2")));
                    bookVO.setEbook_consigneephone(checkNull(rs.getString("telephone")));
                    bookVO.setEbook_consigneeemail(checkNull(rs.getString("email")));
                    bookVO.setEbook_consigneefax(checkNull(rs.getString("fax")));
                    bookVO.setEbook_consigne_id((checkNull(rs.getString("cid"))));
                    if (checkNull(rs.getString("countryId")).trim().length() == 0 && checkNull(rs.getString("address1")).trim().length() == 0 && checkNull(rs.getString("address1")).trim().length() == 0) {
                        bookVO.setHidCNToOrder("Y");
                    }
                    String strtelephone = bookVO.getEbook_consigneephone();
                } else if (custType.equalsIgnoreCase("NT") && n1 == false) {
                    n1 = true;
                    bookVO.setEbook_notifycity(checkNull(rs.getString("city")));
                    bookVO.setEbook_notifycity_desc(checkNull(rs.getString("address1")));
                    bookVO.setEbook_notifycountry(checkNull(rs.getString("countryId")));
                    bookVO.setEbook_notifyname(checkNull(rs.getString("name")));
                    bookVO.setEbook_notifycode(checkNull(rs.getString("custId")));
                    bookVO.setEbook_notifyaddress(checkNull(rs.getString("address1")));
                    bookVO.setEbook_notifyaddress1(checkNull(rs.getString("address2")));
                    bookVO.setEbook_notifyphone(checkNull(rs.getString("telephone")));
                    bookVO.setEbook_notifyemail(checkNull(rs.getString("email")));
                    bookVO.setEbook_notifyfax(checkNull(rs.getString("fax")));
                    bookVO.setEbook_notify_id((checkNull(rs.getString("cid"))));
                    if (checkNull(rs.getString("countryId")).trim().length() == 0 && checkNull(rs.getString("address1")).trim().length() == 0 && checkNull(rs.getString("address1")).trim().length() == 0) {
                        bookVO.setHidNTToOrder("Y");
                    }
                } else if (custType.equalsIgnoreCase("NT") && n1 == true) {
                    ////System.out.println("[ECOM] EcomV1AmendmentBookingBean   .....cidty............" + rs.getString("city"));
                    bookVO.setEbook_notifycity_desc_n1(checkNull(rs.getString("city")));
                    bookVO.setEbook_notifycity_n1(checkNull(rs.getString("city")));
                    bookVO.setEbook_notifycountry_n1(checkNull(rs.getString("countryId")));
                    bookVO.setEbook_notifyname_n1(checkNull(rs.getString("name")));
                    bookVO.setEbook_notifycode_n1(checkNull(rs.getString("custId")));
                    bookVO.setEbook_notifyaddress_n1(checkNull(rs.getString("address1")));
                    bookVO.setEbook_notifyaddress1_n1(checkNull(rs.getString("address2")));
                    bookVO.setEbook_notifyphone_n1(checkNull(rs.getString("telephone")));
                    bookVO.setEbook_notifyemail_n1(checkNull(rs.getString("email")));
                    bookVO.setEbook_notifyfax_n1(checkNull(rs.getString("fax")));
                    bookVO.setEbook_notify_id_n1((checkNull(rs.getString("cid"))));
                    if (checkNull(rs.getString("countryId")).trim().length() == 0 && checkNull(rs.getString("address1")).trim().length() == 0 && checkNull(rs.getString("address1")).trim().length() == 0) {
                        bookVO.setHidNT1ToOrder("Y");
                    }
                }
            }
            viewlist.add(bookVO);
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
        return bookVO;

    }

    @Override
    public ArrayList getV1LRPBookingRoutingDetails(String bookNo) throws RemoteException {
        //System.out.println("[ECOM] EcomV1AmendmentBookingBean   <<<<getrouting>>>>");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null, rs1 = null;
        ArrayList all = new ArrayList();
        Route routing = null;
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(getLRPBookingRouting_qry);
            ps.setString(1, bookNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                routing = new Route();
                routing.setLegNo(rs.getInt("leg_no"));
                //   //System.out.println("[ECOM] EcomV1AmendmentBookingBean   .........routing details..........");
                routing.setBound(checkNull(rs.getString("bound")));
                routing.setRouteId(checkNull(rs.getString("routeid")));
                routing.setVoyage(checkNull(rs.getString("voyagecode")));
                routing.setService(checkNull(rs.getString("servicecode")));
                routing.setService(checkNull(rs.getString("servicecode")));
                routing.setVessel(checkNull(rs.getString("vesselcode")));
                routing.setLoadport(checkNull(rs.getString("loadportid")));
                routing.setDisport(checkNull(rs.getString("dischargeportid")));
                routing.setETD1(checkNull(rs.getString("etadate")));
                routing.setETA1(checkNull(rs.getString("etddate")));
                routing.setFeeder(checkNull(rs.getString("legTypeId")));
                routing.setLegType(checkNull(rs.getString("legTypeId")));
                routing.setGrpCallId(checkNull(rs.getString("groupcallid")));
                //System.out.println("[ECOM] EcomV1AmendmentBookingBean   >>rs.getString(\"groupcallid\")>>" + rs.getString("groupcallid"));
                ps = con.prepareStatement(GET_CUTOFFTERMINAL);
                ps.setString(1, checkNull(rs.getString("groupcallid")));
                ////System.out.println("[ECOM] EcomV1AmendmentBookingBean   >>>GET_CUTOFFTERMINAL>>" + GET_CUTOFFTERMINAL);
                //System.out.println("[ECOM] EcomV1AmendmentBookingBean   >>>rs.getString(groupcallid))>>" + rs.getString("groupcallid"));
                rs1 = ps.executeQuery();
                if (rs1.next()) {
                    routing.setCutoff_polarrdate(checkNull(rs1.getString("arrdate")));
                    ////System.out.println("[ECOM] EcomV1AmendmentBookingBean   >>get arr date in edit routing>>" + rs1.getString("arrdate"));
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

    @Override
    public ArrayList getV1LRPBookingViewCargoDetails(String bookNo, String type) throws RemoteException {
        ArrayList cargoList = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null, rs1 = null, rs2 = null;
        int i = 0;
        int qty = 0;
        AmendmentBookingFormBean eBookingcargo = null;
        String eqpId = null, cargoVol = null, weight = null, VolumeUnt = null, hazardous = null, oog = null, rfr = null, lcl = null, cargoType = null;
        boolean isDuplicateCmdty = false;
        // reuse
        String eqpdummyid = "";
        Hashtable htcargo = new Hashtable();
        String eqp_result = "";
        ArrayList al;
        //
        try {
            cargoList = new ArrayList();
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(LRPbookingCargoQry, 1004, 1007);
            ps.setString(1, bookNo);
            rs = ps.executeQuery();
            int j = 1;
            while (rs.next()) {
                DecimalFormat df = new DecimalFormat("#.##");
                eBookingcargo = new AmendmentBookingFormBean();
                isDuplicateCmdty = false;
                eqpId = checkNull(rs.getString("eqpid"));
                ps = con.prepareStatement(LRPbookingComdtyQry, 1004, 1007);
                ps.setString(1, eqpId);
                ps.setString(2, bookNo);
                rs1 = ps.executeQuery();
                i = 0;
                eBookingcargo.setEbook_weightunit(checkNull(rs.getString("weightunit")));
                weight = checkNull(rs.getString("cargoweight"));
                weight = String.valueOf(new BigDecimal(weight).toPlainString());
                if (weight != null && weight.length() > 0) {
                    Double doublecargo = Double.parseDouble(weight);
                    weight = df.format(doublecargo);
                }
                eBookingcargo.setEbook_cargoweight(weight);
                weight = checkNull(rs.getString("tareweight"));
                weight = String.valueOf(new BigDecimal(weight).toPlainString());
                eBookingcargo.setEbook_tareweight(weight);
                weight = checkNull(rs.getString("grossweight"));
                weight = String.valueOf(new BigDecimal(weight).toPlainString());
                if (weight != null && weight.length() > 0) {
                    Double doublegrossweight = Double.parseDouble(weight);
                    weight = df.format(doublegrossweight);
                }
                eBookingcargo.setEbook_grossweight(weight);
                eBookingcargo.setEqpstatus(checkNull(rs.getString("eqpstatus")));
                String eqp_type = checkNull(rs.getString("eqptypeid"));
                eBookingcargo.setEbook_eqp_type(checkNull(""));
                eBookingcargo.setEbook_eqp(eqp_type);
                ////System.out.println("[ECOM] EcomV1AmendmentBookingBean   .............values,......in temp,,,,,,,,,,," + !(checkNull(rs.getString("settemp")).equalsIgnoreCase("0") && checkNull(rs.getString("tempmin")).equalsIgnoreCase("0") && checkNull(rs.getString("tempmax")).equalsIgnoreCase("0")));
                if (checkNull(rs.getString("nor")).equalsIgnoreCase("Y") || (checkNull(rs.getString("nor")).equalsIgnoreCase("N") && (checkNull(rs.getString("settemp")).length() > 0))) {
                    cargoType = "R";
                } else if (checkNull(rs.getString("oog")).equalsIgnoreCase("Y")) {
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
                eBookingcargo.setOog(checkNull(rs.getString("oog")));
                eBookingcargo.setSoc(checkNull(rs.getString("shipperown")));
                eBookingcargo.setHidden_soc(checkNull(rs.getString("shipperown")));
                eBookingcargo.setLcl(lcl);
                String settemp = checkNull(rs.getString("settemp"));
                String tempunit = checkNull(rs.getString("tempunit"));
                String mintemp = checkNull(rs.getString("tempmin"));
                String maxtemp = checkNull(rs.getString("tempmax"));
                if ("0.0".equalsIgnoreCase(settemp)) {
                    settemp = "";
                    tempunit = "";
                    mintemp = "";
                    maxtemp = "";
                }
                if (!"0".equalsIgnoreCase(settemp) && !"0.0".equalsIgnoreCase(settemp) && settemp != null && settemp.length() > 0) {
                    Double doublesettemp = Double.parseDouble(settemp);
                    settemp = df.format(doublesettemp);
                    Double doublemintemp = Double.parseDouble(mintemp);
                    mintemp = df.format(doublemintemp);
                    Double doublemaxtemp = Double.parseDouble(maxtemp);
                    maxtemp = df.format(doublemaxtemp);
                }
                eBookingcargo.setEbook_nor(checkNull(rs.getString("nor")));
                eBookingcargo.setEbook_presetemp(settemp);
                eBookingcargo.setEbook_temperunit(tempunit);
                eBookingcargo.setEbook_mintemp(mintemp);
                eBookingcargo.setEbook_maxtemp(maxtemp);

                while (rs1.next()) {
                    ////System.out.println("[ECOM] EcomV1AmendmentBookingBean   >>isDuplicateCmdty>>" + isDuplicateCmdty);
                    if (!isDuplicateCmdty) {
                        i++;
                        if (!checkNull(rs1.getString("itemqty")).equalsIgnoreCase("0")) {
                            eBookingcargo.setEbook_packquantity(rs1.getString("itemqty"));
                        } else {
                            eBookingcargo.setEbook_packquantity("");
                        }
                        eBookingcargo.setContainerNo(eqpId);

                        eBookingcargo.setCargoid_new(String.valueOf(j));
                        cargoVol = checkNullDecimal(rs1.getString("itemvolume"));
                        cargoVol = String.valueOf(new BigDecimal(cargoVol).toPlainString());
                        if (cargoVol != null && cargoVol.length() > 0) {
                            Double doublegrosscargoVol = Double.parseDouble(cargoVol);
                            cargoVol = df.format(doublegrosscargoVol);
                        }
                        cargoVol = checkNullbigDecimal(cargoVol);

                        VolumeUnt = checkNull(rs1.getString("itemvolumeunit"));
                        if (cargoVol == null || cargoVol.equalsIgnoreCase("0") || (cargoVol.length() > 0 && !(Float.parseFloat(cargoVol) > 0))) {
                            eBookingcargo.setEbook_cargovolume("");
                        } else {
                            eBookingcargo.setEbook_cargovolume(cargoVol);
                        }
                        //System.out.println("[ECOM] EcomV1AmendmentBookingBean   >>>eBookingcargo.setEbook_cargovolume>>" + eBookingcargo.getEbook_cargovolume());
                        eBookingcargo.setEbook_volumeunit(VolumeUnt);
                        cargoType = "";
                        //rs1 details
                        eBookingcargo.setEbook_commoditycode(checkNull(rs1.getString("commodityId")));
                        eBookingcargo.setEbook_eqp_qty(checkNull(rs1.getString("itemNO")));
                        eBookingcargo.setEbook_hazardous(checkNull(rs1.getString("hazardous")));
                        eBookingcargo.setHzd(checkNull(rs1.getString("hazardous")));
                        eBookingcargo.setEbook_packagecode(checkNull(rs1.getString("packageid")));
                        String olfwd = checkNull(rs1.getString("itemforward"));
                        String owlft = checkNull(rs1.getString("itemleft"));
                        String oh = checkNull(rs1.getString("itemtop"));
                        String olaft = checkNull(rs1.getString("itemaft"));
                        String owrgt = checkNull(rs1.getString("itemright"));
                        String dimenUnt = checkNull(rs1.getString("itemlengthunit"));
                        if (checkNull(rs.getString("oog")).equalsIgnoreCase("Y")) {
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
                        if (olfwd != null && olfwd.length() > 0) {
                            Double doubleolfwd = Double.parseDouble(olfwd);
                            olfwd = df.format(doubleolfwd);
                        }
                        if (owlft != null && owlft.length() > 0) {
                            Double doubleowlft = Double.parseDouble(owlft);
                            owlft = df.format(doubleowlft);
                        }
                        if (oh != null && oh.length() > 0) {
                            Double doubleoh = Double.parseDouble(oh);
                            oh = df.format(doubleoh);
                        }
                        if (olaft != null && olaft.length() > 0) {
                            Double doubleolaft = Double.parseDouble(olaft);
                            olaft = df.format(doubleolaft);
                        }
                        if (owrgt != null && owrgt.length() > 0) {
                            Double doubleowrgt = Double.parseDouble(owrgt);
                            owrgt = df.format(doubleowrgt);
                        }
                        eBookingcargo.setEbook_olforward(olfwd);
                        eBookingcargo.setEbook_owleft(owlft);
                        eBookingcargo.setEbook_oh(oh);
                        eBookingcargo.setEbook_olaft(olaft);
                        eBookingcargo.setEbook_owright(owrgt);
                        eBookingcargo.setEbook_dimensionunit(dimenUnt);
                        hazardous = checkNull(rs1.getString("hazardous"));
                        isDuplicateCmdty = true;
                    } else {
                        eBookingcargo.setEbook_packquantity("");
                        eBookingcargo.setContainerNo(eqpId);
                        eBookingcargo.setEbook_cargovolume("");
                        eBookingcargo.setEbook_volumeunit("M");
                        cargoType = "";
                        //rs1 details
                        eBookingcargo.setEbook_commoditycode("");
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

                // Reuse
                //System.out.println("[ECOM] EcomV1AmendmentBookingBean   type::::::::::::::::>>>>>>>>>>>>" + type);
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
                    //   eqp_result = eqpdummyid.substring(0, eqpdummyid.length() - 1);
                    //   //System.out.println("[ECOM] EcomV1AmendmentBookingBean   eqp_result::::::::::::::::::"+eqp_result);
                    eqpdummyid = eqpdummyid.concat("TNB");
                    eqpdummyid = eqpdummyid + al.size();
                    eBookingcargo.setContainerNo(eqpdummyid);
                    eBookingcargo.setEbook_isdummy("Y");
                    //System.out.println("[ECOM] EcomV1AmendmentBookingBean   eqpdummyid::::::::::::::>>>>>>>>>>>" + eqpdummyid);
                }

                //
                cargoList.add(eBookingcargo);
                j++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (rs1 != null) {
                    rs.close();
                }
                if (rs2 != null) {
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
        return cargoList;
    }

    @Override
    public ReturnVO saveAmendmentBooking(AmendmentBookingFormBean eBookingMasterData, ArrayList routingList, List<AmendmentBookingFormBean> cargoList, String client_code) throws RemoteException {
        ReturnVO rvo = null;
        Connection con = null;
        Connection con_gate = null;
        PreparedStatement ps = null, ps_cargo = null, ps_cmdty = null, ps_dg = null;
        ResultSet rs = null;
        String loginCustName = null;
        String agencycode = "", contactName = null, contactPhone = null, submittedBy = null;
        String book_refno = null;
        String status = null;
        String service = null, poleta = null, polterminal = null;
        String feeder = null, podterminal = null, poletd = null;
        String soc = null, cust_name = null, cust_id = null, cargovolume = null, haz = "N", ediid = null, readerid = null;
        String routeID = null, addremarks = null, mintemp = null, presetemp = null, temperunit = null, maxtemp = null;
        String BlpolTer = null, BlpodTer = null, Service = null, dgflag = "N", subject = null, lrpBookNo = null;
        String oceanVslCode = null, oceanVoyage = null, oceanSerCode = null, oceanBound = null, oceanSerCode_temp = null;
        Route eBookingrouting = null;
        AmendmentBookingFormBean eBookingcargoData = null;
        StringBuffer hssb = new StringBuffer();
        StringBuffer otherssb = new StringBuffer();
        StringBuffer dgsb = new StringBuffer();
        boolean isSaveLrp = false, isSaveEcom = false;
        int version = 0;
        String lrpDraftBookingSave = "N";
        String cargoweight = "", grossweight = "";
        DecimalFormat df = new DecimalFormat("#.00");
        try {
            con_gate = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ////System.out.println("[ECOM] EcomV1AmendmentBookingBean   >>getEbook_routeid>>" + eBookingMasterData.getEbook_routeid());
            //Update master details in ecom table
            if (eBookingMasterData != null) {
                rvo = new ReturnVO();
                status = checkNull(eBookingMasterData.getSaveStatus()).trim();
                book_refno = eBookingMasterData.getEcomBookNo();
                loginCustName = eBookingMasterData.getLoginName();
                lrpBookNo = checkNull(eBookingMasterData.getLrpBookNo());
            }
            //System.out.println("[ECOM] EcomV1AmendmentBookingBean   >>v1>>book_refno>>" + book_refno);
            readerid = bookNoGenerate("EDI");
            //System.out.println("[ECOM] EcomV1AmendmentBookingBean   >>readerid in amendment>>" + readerid);
            agencycode = getAgencydetails(eBookingMasterData.getEbook_origin());
            if (readerid != null && readerid.length() > 0) {
                ps = con_gate.prepareStatement(UPDATE_EDI_DOC_STATUS);
                ps.setString(1, book_refno);
                ps.executeUpdate();
                // Insert into edidocstatus
                ps = con_gate.prepareStatement(EDI_DOC_STATUS);
                ps.setString(1, book_refno);
                ps.setString(2, readerid);
                if (eBookingMasterData.getLrpBookNo() != null && eBookingMasterData.getLrpBookNo().length() > 0) {
                    ps.setString(3, "5");
                } else {
                    ps.setString(3, "9");
                }
                ps.setString(4, "R");
                ps.setString(5, GeneralConf.EDI_TYPE_BKG);
                ps.setString(6, GeneralConf.EDI_MESSAGETYPE_BKG);
                ps.setString(7, GeneralConf.EDI_ECOM);
                ps.executeUpdate();
                //Get EDIID
                ps = con_gate.prepareStatement(QrygetEDIID, 1004, 1007);
                rs = ps.executeQuery();
                if (rs.next()) {
                    ediid = checkNull(rs.getString("ediid"));
                }
                if (ediid != null && ediid.length() > 0) {
                    // Insert into edireadermaster
                    ps = con_gate.prepareStatement(EDIREADER_MASTER);
                    ps.setString(1, ediid);
                    ps.setString(2, readerid);
                    ps.setString(3, agencycode);
                    ps.setString(4, agencycode);
                    ps.setString(5, "Completed");
                    ps.executeUpdate();

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
                    ps = con_gate.prepareStatement(EBOOKING_REMARKS);
                    ps.setString(1, book_refno);
                    ps.setString(2, "Submitted By");
                    ps.setString(3, submittedBy);
                    ps.setString(4, readerid);//agencycode
                    ps.setString(5, "0");
                    ps.setString(6, null);
                    ps.executeUpdate();

                    ps = con_gate.prepareStatement(EBOOKING_REMARKS);
                    ps.setString(1, book_refno);
                    ps.setString(2, "Online Reference No");
                    ps.setString(3, book_refno);
                    ps.setString(4, readerid);//agencycode
                    ps.setString(5, "0");
                    ps.setString(6, null);
                    ps.executeUpdate();
                }

                String lclflag = "NR"; //editempbooking - shipment_type
                //Get routing details and save routing details in editempbooking routing table.
                routeID = eBookingMasterData.getEbook_routeid();

                ps = con_gate.prepareStatement(GETROUTINGDETAILS, 1004, 1007);
                ps.setString(1, routeID);
                rs = ps.executeQuery();

                if (rs.next()) {
                    BlpolTer = checkNull(rs.getString("BLPOLTer"));
                    BlpodTer = checkNull(rs.getString("BlPodTer"));
                    Service = checkNull(rs.getString("MainSvc"));
                    ps = con_gate.prepareStatement(BOOKING_ROUTING);
                    ps.setString(1, checkNull(rs.getString("origin")));
                    ps.setString(2, checkNull(rs.getString("orgmode")));
                    ps.setString(3, checkNull(rs.getString("BLPOL")));
                    ps.setString(4, checkNull(rs.getString("BLPOLTer")));
                    ps.setString(5, checkNull(rs.getString("BLPOLSvc")));
                    ps.setString(6, checkNull(rs.getString("mode")));
                    ps.setString(7, checkNull(rs.getString("mode")));
                    ps.setString(8, checkNull(rs.getString("OpsPol1")));
                    ps.setString(9, checkNull(rs.getString("OpsPOLSVC1")));
                    ps.setString(10, checkNull(rs.getString("opsMode1")));
                    ps.setString(11, checkNull(rs.getString("OpsPol2")));
                    ps.setString(12, checkNull(rs.getString("OpsPol2Ter")));
                    ps.setString(13, checkNull(rs.getString("OpsPOLSVC2")));
                    ps.setString(14, checkNull(rs.getString("opsMode2")));
                    ps.setString(15, checkNull(rs.getString("OpsPol3")));
                    ps.setString(16, checkNull(rs.getString("OpsPol3Ter")));
                    ps.setString(17, checkNull(rs.getString("OpsPolSvc3")));
                    ps.setString(18, checkNull(rs.getString("OpsMode3")));
                    ps.setString(19, checkNull(rs.getString("BlPod")));
                    ps.setString(20, checkNull(rs.getString("BlPodTer")));
                    ps.setString(21, checkNull(rs.getString("delmode")));
                    ps.setString(22, checkNull(rs.getString("delivery")));
                    ps.setString(23, Service);
                    ps.setString(24, book_refno);
                    ps.setString(25, readerid);
                    ps.setString(26, checkNull(rs.getString("DefRoute")));
                    ps.executeUpdate();
                }
                //
                polterminal = BlpolTer;
                podterminal = BlpodTer;
                //
                //System.out.println("[ECOM] EcomV1AmendmentBookingBean   #########MainService####>>>>>" + Service);

                if (routingList != null && routingList.size() > 0) {
                    int len = routingList.size();
                    for (int k = 0; k < len; k++) {
                        eBookingrouting = (Route) routingList.get(k);
                        oceanSerCode_temp = checkNull(eBookingrouting.getService());
                        ////System.out.println("[ECOM] EcomV1AmendmentBookingBean   >>oceanSerCode_temp>>" + oceanSerCode_temp);
                        if (oceanSerCode_temp != null && oceanSerCode_temp.length() > 0) {
                            if (oceanSerCode_temp.equalsIgnoreCase(Service)) {
                                oceanSerCode = checkNull(eBookingrouting.getService());
                                oceanVslCode = eBookingrouting.getVessel();
                                oceanVoyage = eBookingrouting.getVoyage();
                                ////System.out.println("[ECOM] EcomV1AmendmentBookingBean   >>eBookingrouting.getBound()>>eBookingrouting.getBound()" + eBookingrouting.getBound());
                                oceanBound = eBookingrouting.getBound();
                            }
                        }
                    }
                }

                //Insert edit temp booking master details
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
                ps = con_gate.prepareStatement(SAVE_EDI_TEMP_BOOKING_MASTER);
                ps.setString(1, book_refno);
                ps.setString(2, agencycode);
                ps.setString(3, oceanVslCode);
                ps.setString(4, oceanVoyage);
                ps.setString(5, oceanSerCode);
                ps.setString(6, oceanBound);
                ps.setString(7, eBookingMasterData.getEbook_servicecode());
                ps.setString(8, eBookingMasterData.getEbook_vesselcode());
                ps.setString(9, eBookingMasterData.getEbook_voyage());
                ps.setString(10, eBookingMasterData.getEbook_bound_leg());
                ps.setString(11, eBookingMasterData.getEbook_origin());
                ps.setString(12, eBookingMasterData.getEbook_delivery());
                ps.setString(13, eBookingMasterData.getEbook_portofloading());
                ps.setString(14, eBookingMasterData.getEbook_portofdischarge());
                ps.setString(15, polterminal);
                if (poletd != null && poletd.length() > 0 && (!poletd.equalsIgnoreCase("-"))) {
                    ps.setString(16, poletd);
                } else {
                    ps.setString(16, null);
                }
                ps.setString(17, podterminal);
                if (poleta != null && poleta.length() > 0 && (!poleta.equalsIgnoreCase("-"))) {
                    ps.setString(18, poleta);
                } else {
                    ps.setString(18, null);
                }
                if (eBookingMasterData.getEbook_date().length() > 0) {
                    ps.setString(19, eBookingMasterData.getEbook_date());
                } else {
                    ps.setString(19, null);
                }
                ps.setString(20, eBookingMasterData.getPreCarriage());
                ps.setString(21, eBookingMasterData.getOnCarriage());
                ps.setString(22, "D");
                ps.setString(23, "ECOM");
                ps.setString(24, GeneralConf.EDI_MESSAGETYPE_BKG);
                if (eBookingMasterData.getLrpBookNo() != null && eBookingMasterData.getLrpBookNo().length() > 0) {
                    ps.setString(25, "5");
                } else {
                    ps.setString(25, "9");
                }
                ps.setString(26, readerid);
                ps.setString(27, agencycode);
                ps.setString(28, agencycode);
                ps.setString(29, book_refno);
                ps.setString(30, "");
                ps.setString(31, feeder);
                String strOrigin = eBookingMasterData.getEbook_origin_country().substring(7, eBookingMasterData.getEbook_origin_country().length());
                String strDeli = eBookingMasterData.getEbook_delivery_country().substring(7, eBookingMasterData.getEbook_delivery_country().length());
                ps.setString(32, strOrigin.trim());
                ps.setString(33, strDeli.trim());
                ps.setString(34, lclflag);
                ps.setString(35, eBookingMasterData.getEbook_shipment());
                ps.setString(36, eBookingMasterData.getSlotType());
                ps.setString(37, addremarks);
                ps.setString(38, checkNullUpperCase(eBookingMasterData.getEbook_agency_reference()));
                ps.setString(39, (eBookingMasterData.getCarrierContact()));
                ps.setString(40, (eBookingMasterData.getLrpBookNo()));
                ps.setString(41, checkNull(eBookingMasterData.getPodintransit()).trim());
                ps.setString(42, checkNull(eBookingMasterData.getHidPodIntransitName()).trim());
                ps.executeUpdate();
                //
                //Rate reference in lrp editempbooking remarks table
                ps = con_gate.prepareStatement(INSER_EDIBKG_REF);
                ps.setString(1, book_refno);
                ps.setString(2, "DAK");
                ps.setString(3, book_refno);
                ps.setString(4, "N");
                ps.setString(5, readerid);
                ps.setString(6, "0");
                ps.executeUpdate();

                if (checkNull(eBookingMasterData.getEbook_rate_reference()).length() > 0) {
                    ps = con_gate.prepareStatement(EBOOKING_REMARKS);
                    ps.setString(1, book_refno);
                    ps.setString(2, "Rate reference");
                    ps.setString(3, eBookingMasterData.getEbook_rate_reference());
                    ps.setString(4, readerid);//agencycode
                    ps.setString(5, "0");
                    ps.setString(6, "E");
                    ps.executeUpdate();
                    //
                    ps = con_gate.prepareStatement(INSER_EDIBKG_REF);
                    ps.setString(1, book_refno);
                    ps.setString(2, "EOR");
                    ps.setString(3, eBookingMasterData.getEbook_rate_reference());
                    ps.setString(4, "N");
                    ps.setString(5, readerid);
                    ps.setString(6, "0");
                    ps.executeUpdate();
                }

                //Insert into edi temp booking leg details
                ps = con_gate.prepareStatement(booking_leg_editemp);
                if (routingList != null && routingList.size() > 0) {
                    int len = routingList.size();
                    for (int k = 0; k < len; k++) {
                        eBookingrouting = (Route) routingList.get(k);
                        ps.setString(1, book_refno);
                        ps.setInt(2, eBookingrouting.getLegNo());
                        ps.setString(3, eBookingrouting.getBound());
                        ps.setString(4, eBookingrouting.getService());
                        ps.setString(5, eBookingrouting.getVessel());
                        ps.setString(6, eBookingrouting.getLoadport());
                        ps.setString(7, eBookingrouting.getDisport());
                        ps.setString(8, eBookingrouting.getETA1());  //
                        ps.setString(9, eBookingrouting.getETD1());   //
                        ps.setString(10, eBookingrouting.getLegType());
                        ps.setString(11, eBookingMasterData.getPreCarriage());
                        ps.setString(12, eBookingMasterData.getOnCarriage());
                        ps.setString(13, eBookingrouting.getLoadtype());
                        ps.setString(14, eBookingrouting.getDisctype());
                        ps.setString(15, eBookingrouting.getVoyage());
                        //   pstmt.setString(16, loginCustName);
                        ps.setString(16, readerid);
                        ps.setInt(17, 0);
                        ps.setInt(18, eBookingrouting.getLegNo() - 1);
                        ps.setString(19, eBookingrouting.getGrpCallId());
                        ps.setString(20, routeID);
                        ps.executeUpdate();
                    }
                }

                //Insert customer details into editempbooking customer table
                if (eBookingMasterData.getEbook_agreename() != null) {
                    // Insert Login user as AGrement Party
                    ps = con_gate.prepareStatement(BOOKING_CUST);
                    ps.setString(1, book_refno);
                    ps.setString(2, "AP");
                    ps.setString(3, eBookingMasterData.getCustCode());
                    ps.setString(4, eBookingMasterData.getEbook_agreecountry());
                    ps.setString(5, eBookingMasterData.getEbook_agreename());
                    ps.setString(6, eBookingMasterData.getEbook_agreeaddress());
                    ps.setString(7, eBookingMasterData.getEbook_agreecity());
                    ps.setString(8, "");
                    ps.setString(9, eBookingMasterData.getEbook_agreephone());
                    ps.setString(10, eBookingMasterData.getEbook_agreefax());
                    ps.setString(11, eBookingMasterData.getEbook_agreeemail());
                    ps.setString(12, readerid);
                    ps.setString(13, eBookingMasterData.getEbook_agreeaddress1());
                    ps.executeUpdate();
                }
                //
                if (checkNull(eBookingMasterData.getEbook_shippername()).length() > 0) {
                    ps = con_gate.prepareStatement(BOOKING_CUST);
                    ps.setString(1, book_refno);
                    ps.setString(2, "SH");
                    ps.setString(3, eBookingMasterData.getEbook_shippercode());
                    ps.setString(4, eBookingMasterData.getEbook_shippercountry());
                    ps.setString(5, eBookingMasterData.getEbook_shippername());
                    ps.setString(6, eBookingMasterData.getEbook_shipperaddress());
                    ps.setString(7, eBookingMasterData.getEbook_shippercity());
                    ps.setString(8, "");
                    ps.setString(9, eBookingMasterData.getEbook_shipperphone());
                    ps.setString(10, eBookingMasterData.getEbook_shipperfax());
                    ps.setString(11, eBookingMasterData.getEbook_shipperemail());
                    ps.setString(12, readerid);
                    ps.setString(13, eBookingMasterData.getEbook_shipperaddress1());
                    ps.executeUpdate();
                }

                if (checkNull(eBookingMasterData.getEbook_consigneename()).length() > 0) {
                    ps = con_gate.prepareStatement(BOOKING_CUST);
                    ps.setString(1, book_refno);
                    ps.setString(2, "CO");
                    ps.setString(3, eBookingMasterData.getEbook_consigneecode());
                    ps.setString(4, eBookingMasterData.getEbook_consigneecountry());
                    ps.setString(5, eBookingMasterData.getEbook_consigneename());
                    ps.setString(6, eBookingMasterData.getEbook_consigneeaddress());
                    ps.setString(7, eBookingMasterData.getEbook_consigneecity());
                    ps.setString(8, "");
                    ps.setString(9, eBookingMasterData.getEbook_consigneephone());
                    ps.setString(10, eBookingMasterData.getEbook_consigneefax());
                    ps.setString(11, eBookingMasterData.getEbook_consigneeemail());
                    ps.setString(12, readerid);
                    ps.setString(13, eBookingMasterData.getEbook_consigneeaddress1());
                    ps.executeUpdate();

                }
                //
                if (checkNull(eBookingMasterData.getEbook_notifyname()).length() > 0) {
                    ps = con_gate.prepareStatement(BOOKING_CUST);
                    ps.setString(1, book_refno);
                    ps.setString(2, "NT");
                    ps.setString(3, eBookingMasterData.getEbook_notifycode());
                    ps.setString(4, eBookingMasterData.getEbook_notifycountry());
                    ps.setString(5, eBookingMasterData.getEbook_notifyname());
                    ps.setString(6, eBookingMasterData.getEbook_notifyaddress());
                    ps.setString(7, eBookingMasterData.getEbook_notifycity());
                    ps.setString(8, "");
                    ps.setString(9, eBookingMasterData.getEbook_notifyphone());
                    ps.setString(10, eBookingMasterData.getEbook_notifyfax());
                    ps.setString(11, eBookingMasterData.getEbook_notifyemail());
                    ps.setString(12, readerid);
                    ps.setString(13, eBookingMasterData.getEbook_notifyaddress1());
                    ps.executeUpdate();
                }

                //
                if (checkNull(eBookingMasterData.getEbook_notifyname_n1()).length() > 0) {
                    ps = con_gate.prepareStatement(BOOKING_CUST);
                    ps.setString(1, book_refno);
                    ps.setString(2, "NT");
                    ps.setString(3, eBookingMasterData.getEbook_notifycode_n1());
                    ps.setString(4, eBookingMasterData.getEbook_notifycountry_n1());
                    ps.setString(5, eBookingMasterData.getEbook_notifyname_n1());
                    ps.setString(6, eBookingMasterData.getEbook_notifyaddress_n1());
                    ps.setString(7, eBookingMasterData.getEbook_notifycity_n1());
                    ps.setString(8, "");
                    ps.setString(9, eBookingMasterData.getEbook_notifyphone_n1());
                    ps.setString(10, eBookingMasterData.getEbook_notifyfax_n1());
                    ps.setString(11, eBookingMasterData.getEbook_notifyemail_n1());
                    ps.setString(12, readerid);
                    ps.setString(13, eBookingMasterData.getEbook_notifyaddress1_n1());
                    ps.executeUpdate();
                }

                //Insert ocean freight in remarks
                if (eBookingMasterData.getEbook_ocean_freight() != null && eBookingMasterData.getEbook_ocean_freight().length() > 0) {
                    ps = con_gate.prepareStatement(EBOOKING_REMARKS);
                    ps.setString(1, book_refno);
                    ps.setString(2, "Ocean Freight : ");
                    if (eBookingMasterData.getEbook_ocean_freight().equalsIgnoreCase("C")) {
                        ps.setString(3, "Collect");
                    } else {
                        ps.setString(3, "Prepaid");
                    }
                    ps.setString(4, readerid);
                    ps.setString(5, "0");
                    ps.setString(6, "M");
                    ps.executeUpdate();

                    //Insert into reference table
                    ps = con_gate.prepareStatement(INSER_EDIBKG_REF);
                    ps.setString(1, book_refno);
                    ps.setString(2, "EOF");
                    ps.setString(3, eBookingMasterData.getEbook_ocean_freight());
                    ps.setString(4, "N");
                    ps.setString(5, readerid);
                    ps.setString(6, "0");
                    ps.executeUpdate();
                }
                //Insert empty pick up place
                String emptyPickUp = null;
                if (eBookingMasterData.getEmptyPickup() != null && eBookingMasterData.getEmptyPickup().length() > 0 && eBookingMasterData.getEmptyPickUpDate() != null && eBookingMasterData.getEmptyPickUpDate().length() > 0) {
                    emptyPickUp = eBookingMasterData.getEmptyPickUpName();
                    if (eBookingMasterData.getEmptyPickUpDate() != null && eBookingMasterData.getEmptyPickUpDate().length() > 0) {
                        emptyPickUp = emptyPickUp + "  Date : " + eBookingMasterData.getEmptyPickUpDate();
                    }
                    ps = con_gate.prepareStatement(EBOOKING_REMARKS);
                    ps.setString(1, book_refno);
                    ps.setString(2, "Empty Place of Pick Up : ");
                    ps.setString(3, emptyPickUp);
                    ps.setString(4, readerid);
                    ps.setString(5, "0");
                    ps.setString(6, "M");
                    ps.executeUpdate();

                    //Insert into reference table
                    ps = con_gate.prepareStatement(INSER_EDIBKG_REF);
                    ps.setString(1, book_refno);
                    ps.setString(2, "EEP");
                    ps.setString(3, eBookingMasterData.getEmptyPickup());
                    ps.setString(4, "N");
                    ps.setString(5, readerid);
                    ps.setString(6, "0");
                    ps.executeUpdate();
                    //Insert into reference table
                    if (eBookingMasterData.getEmptyPickUpDate() != null && eBookingMasterData.getEmptyPickUpDate().length() > 0) {
                        ps = con_gate.prepareStatement(INSER_EDIBKG_REF);
                        ps.setString(1, book_refno);
                        ps.setString(2, "EPD");
                        ps.setString(3, eBookingMasterData.getEmptyPickUpDate());
                        ps.setString(4, "N");
                        ps.setString(5, readerid);
                        ps.setString(6, "0");
                        ps.executeUpdate();
                    }
                }
                // Insert Commodity Details
                ps_cmdty = con_gate.prepareStatement(BOOKING_COMMODITY);
                ps_cargo = con_gate.prepareStatement(EBOOKING_CONT);
                int len_cargo = cargoList.size();
                String eqpid = "";
                int k = 0;
                hssb.append("");
                otherssb.append("");
                dgsb.append("");

                ArrayList alc = new ArrayList();
                String tareW = "";
                int increment = 0;
                boolean othersboolean = false, dgboolean = false;
                for (int i = 0; i < len_cargo; i++) {
                    eBookingcargoData = (AmendmentBookingFormBean) cargoList.get(i);
                    //eqpQuantityvalue = Integer.parseInt(eBookingcargoData.getEbook_eqp_qty());
                    int CargoNo = 1;

                    eqpid = eBookingcargoData.getContainerNo();
                    //tareW = getEqpTareW(eBookingcargoData.getEbook_eqp());
                    if (eBookingcargoData != null) {
                        haz = "N";
                        String hazflag = eBookingcargoData.getHzd();
                        if ("Y".equalsIgnoreCase(checkNull(hazflag))) {
                            haz = "Y";
                        }
                        cargovolume = eBookingcargoData.getEbook_cargovolume();

                        if (eBookingcargoData.getHscode() != null && eBookingcargoData.getHscode().trim().length() > 0) {
                            hssb.append(eqpid + " : " + eBookingcargoData.getHscode());
                            hssb.append(" ");
                        }
                        if (eBookingcargoData.getCommodityothers().equalsIgnoreCase("Y")) {
                            othersboolean = true;
                            otherssb.append(eqpid + " : " + eBookingcargoData.getEbook_commoditycode());
                            otherssb.append(" ");
                        }
                        if (checkNull(eBookingcargoData.getHzd()).equalsIgnoreCase("Y")) {
                            dgboolean = true;
                            dgsb.append(eqpid + " - DG Class : " + eBookingcargoData.getEbook_dgclass() + " UN NO : " + eBookingcargoData.getEbook_unno() + " Flash Point : " + eBookingcargoData.getEbook_flashpoint() + eBookingcargoData.getEbook_flashunit());
                            dgsb.append(" ");
                        }
                        ps_cmdty.setString(1, book_refno);
                        if (eBookingcargoData.getCommodityothers().equalsIgnoreCase("Y")) {
                            ps_cmdty.setString(2, "");
                        } else {
                            ps_cmdty.setString(2, checkNull(eBookingcargoData.getEbook_commoditycode()));
                        }
                        cargoweight = checkNull(eBookingcargoData.getEbook_cargoweight());
                        if (cargoweight != null && cargoweight.length() > 0) {
                            Double doublecargo = Double.parseDouble(cargoweight);
                            cargoweight = df.format(doublecargo);
                        }
                        ps_cmdty.setString(3, checkNull(readerid));
                        ps_cmdty.setString(4, "0");
                        ps_cmdty.setString(5, checkNull(eBookingcargoData.getEbook_packquantity()));
                        ps_cmdty.setString(6, cargoweight);
                        ps_cmdty.setString(7, checkNull(eBookingcargoData.getEbook_weightunit()));
                        ps_cmdty.setString(8, checkNull(haz));
                        ps_cmdty.setString(9, checkNull(eqpid));
                        ps_cmdty.setInt(10, 1);
                        ps_cmdty.setString(11, checkNull(eBookingcargoData.getEbook_packagecode()));
                        ps_cmdty.setString(12, checkNull(cargovolume));
                        ps_cmdty.setString(13, checkNull(eBookingcargoData.getEbook_volumeunit()));
                        ps_cmdty.setString(14, checkNull(eBookingcargoData.getEbook_olforward()));
                        ps_cmdty.setString(15, checkNull(eBookingcargoData.getEbook_owleft()));
                        ps_cmdty.setString(16, checkNull(eBookingcargoData.getEbook_oh()));
                        ps_cmdty.setString(17, checkNull(eBookingcargoData.getEbook_olaft()));
                        ps_cmdty.setString(18, checkNull(eBookingcargoData.getEbook_owright()));
                        ps_cmdty.setString(19, checkNull(eBookingcargoData.getEbook_dimensionunit()));
                        if (eBookingcargoData.getCommodityothers().equalsIgnoreCase("Y")) {
                            ps_cmdty.setString(20, "");
                        } else {
                            ps_cmdty.setString(20, checkNull(eBookingcargoData.getEbook_commoditycode()));
                        }
                        ps_cmdty.setString(21, "0");
                        ps_cmdty.setString(22, client_code);
                        ps_cmdty.setString(23, eBookingcargoData.getEbooking_commodity_desc());
                        CargoNo++;//
                        ps_cmdty.addBatch();
                        k++;

                        //Add container batch
                        dgflag = "N";
                        hazflag = eBookingcargoData.getHzd();
                        if ("Y".equalsIgnoreCase(checkNull(hazflag))) {
                            dgflag = "Y";
                        }
                        String laden = eBookingcargoData.getEqpstatus();

                        mintemp = eBookingcargoData.getEbook_mintemp();
                        presetemp = eBookingcargoData.getEbook_presetemp();
                        maxtemp = eBookingcargoData.getEbook_maxtemp();
                        temperunit = eBookingcargoData.getEbook_temperunit();
                        ps_cargo.setString(1, book_refno);
                        String strEQP = eBookingcargoData.getEbook_eqp_type();
                        if (!(checkNull(strEQP).trim().equalsIgnoreCase("R"))) {
                            temperunit = null;
                            mintemp = null;
                            presetemp = null;
                            maxtemp = null;
                        }
                        if ("Y".equalsIgnoreCase(eBookingcargoData.getEbook_nor())) {
                            temperunit = null;
                            mintemp = null;
                            presetemp = null;
                            maxtemp = null;
                        }
                        if (strEQP.trim().equalsIgnoreCase("R")) {
                            ps_cargo.setString(2, checkNull(eBookingcargoData.getEbook_eqp_type()));
                        } else {
                            ps_cargo.setString(2, "N");
                        }
                        cargoweight = checkNull(eBookingcargoData.getEbook_cargoweight());
                        if (cargoweight != null && cargoweight.length() > 0) {
                            Double doublecargo = Double.parseDouble(cargoweight);
                            cargoweight = df.format(doublecargo);
                        }
                        grossweight = checkNull(eBookingcargoData.getEbook_grossweight());
                        if (grossweight != null && grossweight.length() > 0) {
                            Double doublecargogross = Double.parseDouble(grossweight);
                            grossweight = df.format(doublecargogross);
                        }
                        ps_cargo.setString(3, checkNull(eBookingcargoData.getEbook_eqp()));
                        ps_cargo.setString(4, readerid);
                        ps_cargo.setString(5, eqpid);
                        ps_cargo.setInt(6, alc.size());
                        ps_cargo.setString(7, checkNull(eBookingcargoData.getOog()));
                        ps_cargo.setString(8, checkNull(eBookingcargoData.getSoc()));
                        ps_cargo.setString(9, cargoweight);
                        ps_cargo.setString(10, grossweight);
                        ps_cargo.setString(11, checkNull(eBookingcargoData.getEbook_weightunit()));
                        ps_cargo.setString(12, maxtemp);
                        ps_cargo.setString(13, mintemp);
                        ps_cargo.setString(14, presetemp);
                        ps_cargo.setString(15, temperunit);
                        ps_cargo.setString(16, eBookingcargoData.getEbook_nor());
                        ps_cargo.setString(17, checkNull(dgflag));
                        ps_cargo.setString(18, checkNull(laden));
                        ps_cargo.setString(19, checkNull(eBookingMasterData.getEbook_orderNo()));
                        ps_cargo.setString(20, eBookingcargoData.getEbook_tareweight());
                        ps_cargo.addBatch();
                        increment++;
                    }

                }
                ps_cargo.executeBatch();
                ps_cmdty.executeBatch();

                String ishazardous = "N";
                for (int i = 0; i < len_cargo; i++) {
                    eBookingcargoData = (AmendmentBookingFormBean) cargoList.get(i);
                    ishazardous = checkNull(eBookingcargoData.getHzd());
                    if (ishazardous.equalsIgnoreCase("Y")) {
                        String unno = checkNull(eBookingcargoData.getEbook_unno());
                        if (unno != null && unno.length() > 0) {
                            unno = unno.split("~~")[0].trim();
                        }
                        ps = con_gate.prepareStatement(EBOOKING_DG_DETAILS);
                        ps.setString(1, checkNull(book_refno));
                        ps.setString(2, checkNull(eqpid));
                        ps.setString(3, checkNull(eBookingcargoData.getEbook_eqp()));
                        ps.setString(4, unno);
                        ps.setString(5, checkNull(checkNull(eBookingcargoData.getEbook_shipname())));
                        ps.setString(6, checkNull(checkNull(eBookingcargoData.getEbook_dgclass())));
                        ps.setString(7, checkNull(checkNull(eBookingcargoData.getEbook_flashpoint())));
                        ps.setString(8, checkNull(checkNull(eBookingcargoData.getEbook_flashunit())));
                        ps.setString(9, checkNull(checkNull(eBookingcargoData.getEbook_poluttunit())));
                        ps.setString(10, checkNull(readerid));
                        ps.addBatch();
                    }
                }
                //ps.executeBatch();
                //
                String hscodecheck = hssb.toString();
                if (hscodecheck != null && hscodecheck.trim().length() > 0) {
                    ps = con_gate.prepareStatement(EBOOKING_REMARKS);
                    ps.setString(1, book_refno);
                    ps.setString(2, "HS Code  : ");
                    ps.setString(3, hssb.toString());
                    ps.setString(4, readerid);
                    ps.setString(5, "0");
                    ps.setString(6, "M");
                    ps.executeUpdate();
                }
                if (dgboolean) {
                    ps = con_gate.prepareStatement(EBOOKING_REMARKS);
                    ps.setString(1, book_refno);
                    ps.setString(2, "DG : ");
                    ps.setString(3, dgsb.toString());
                    ps.setString(4, readerid);
                    ps.setString(5, "0");
                    ps.setString(6, "M");
                    ps.executeUpdate();
                }
                if (othersboolean) {
                    ps = con_gate.prepareStatement(EBOOKING_REMARKS);
                    ps.setString(1, book_refno);
                    ps.setString(2, "Other Commodity : ");
                    ps.setString(3, otherssb.toString());
                    ps.setString(4, readerid);
                    ps.setString(5, "0");
                    ps.setString(6, "M");
                    ps.executeUpdate();

                }
            } // Id Close

            //Mail format for customer
            String strAgencyCode = agencycode;
            String strAgency = getAgencyMail(strAgencyCode);
            if (strAgency != null && strAgency.length() > 0) {
                rvo.setAdmToMailId(strAgency);
            }
        } catch (Exception e) {
            rvo.setStrRefNo("ERROR");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (ps_cargo != null) {
                    ps_cargo.close();
                }
                if (ps_cmdty != null) {
                    ps_cmdty.close();
                }
                if (con != null) {
                    con.close();
                }
                if (con_gate != null) {
                    con_gate.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rvo;
    }

    private String getAgencyMail(String agencycode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String email = "";
        try {
            // String strgetAgencyMailQry = "Select email from agency where agencycode='" + agencycode + "'";
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
            //System.out.println("[ECOM] EcomV1AmendmentBookingBean   *******email*************==>" + email);
        } catch (Exception e) {
            //System.out.println("[ECOM] EcomV1AmendmentBookingBean   ******==>" + e);
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
                //System.out.println("[ECOM] EcomV1AmendmentBookingBean   Exception While Closing Connection" + e.getMessage());
                e.printStackTrace();
            }
        }
        return email;
    }

    private String getAgencydetails(String port) throws RemoteException {
        //System.out.println("[ECOM] EcomV1AmendmentBookingBean   getAgencydetails" + port);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String agency = "";
        if (port != null && port.contains("-")) {
            port = port.substring(0, port.indexOf("-"));
        }
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            pstmt = con.prepareStatement(GET_AGENCY_CODE, 1004, 1007);
            ////System.out.println("[ECOM] EcomV1AmendmentBookingBean   Query" + GET_AGENCY_CODE);
            pstmt.setString(1, port);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                agency = checkNull(rs.getString("agencycode"));
            }
        } catch (Exception e) {
            //System.out.println("[ECOM] EcomV1AmendmentBookingBean   Exception while geting Agency code  " + e.getMessage());
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
                //System.out.println("[ECOM] EcomV1AmendmentBookingBean   Exception while closing the connection : " + e.getMessage());
                e.printStackTrace();
            }
        }

        return agency;
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

            //System.out.println("[ECOM] EcomV1AmendmentBookingBean   ===========NEW bknum==========>>>>>>" + bkNum);

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
                //System.out.println("[ECOM] EcomV1AmendmentBookingBean   exception while closing connection bookNoGenerate" + e);
                e.printStackTrace();
            }

        }
        return bkNum;
    }

    private String getServiceName(String serviceCode) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String serviceName = "";
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(ServiceNameQRY, 1004, 1007);
            ps.setString(1, serviceCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                serviceName = checkNull(rs.getString("serviceType"));
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
        return serviceName;
    }

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    private String checkNullUpperCase(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.toUpperCase().trim() : "";
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

    private String checkNullbigDecimal(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"0.0000".equalsIgnoreCase(str)) && (!"".equalsIgnoreCase(str))) ? str.trim() : "";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }
}
