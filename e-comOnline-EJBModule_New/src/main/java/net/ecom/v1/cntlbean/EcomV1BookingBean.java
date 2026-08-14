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
import java.util.Map;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import net.ecom.common.GeneralConf;
import net.ecom.utils.JDBCServiceLocatorFactory;
import net.ecom.vo.Agencydto;
import net.ecom.vo.CustomerDetailsVO;
import net.ecom.vo.CustomerProfile;
import net.ecom.vo.EbookingValidatorFormBeanNew;
import net.ecom.vo.LoginCustomerVo;
import net.ecom.vo.ReturnVO;
import net.ecom.vo.Route;

/**
 *
 * @author deepa.p
 */
@Stateless(name = "EcomV1BookingBean")
@Remote(EcomV1BookingBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
})
public class EcomV1BookingBean implements EcomV1BookingBeanRemote {

    private static final String getLRPBookingViewbookingMasterQry = "select b.book_no,transportserviceid,servicecode,vesselcode,voyagecode,bound,originportid,finalportid,loadportid,dischargeportid,"
            + "originportdesc,finalportdesc,polterminal,polat,podat,bookdate,precarriage,oncarriage,'' as shipment_type,'' as refval,AgencyRefNo from "
            + "booking b where b.book_no=? ";
    private static final String getLRPBookingViewbookingCustQry = "select cid,book_no,custtypeid,custid,countryid,name,address1,address2,city,zipcode,telephone,fax,email from booking_cust where book_no=?";
    private static final String getLRPBookingRouting_qry = "select leg_no,bound,servicecode,vesselcode,loadportid,dischargeportid,etddate,etadate,voyagecode,legTypeId from booking_leg (nolock)  where book_no =?";
    private static final String LRPbookingCargoQry = "select book_no,oog,cargono,eqpstatus,eqptypeid,eqpid,shipperown,nor,tempmax,tempmin,settemp,tempunit,cargoweight,weightunit,tareweight,grossweight,"
            + "eqpvolume,eqpvolumeunit from booking_cargo where book_no = ?";
    private static final String LRPbookingComdtyQry = "select eqpid,commodityId,packageid,itemno,itemvolume,itemvolumeunit,itemqty,itemleft,itemforward,itemtop,itemright,itemaft,itemlengthunit,"
            + "hazardous from booking_commodity where eqpid=? and book_no = ?";
    private static final String GET_BKG_BREF_VALUES = "select refval from booking_reference where refid=? and book_no=?";
    protected static final String EDI_DOC_STATUS = "insert into edidocstatus (book_no,edireaderid,transtype,status,ediparty,receivdate,message,servprovname) values (?,?,?,?,?,getDate(),?,?)";
    protected static final String EDIREADER_MASTER = "insert into edireadermaster (ediid,edireaderid,datecr,requestdate,agencycode,locationcode,uploadstatus) values (?,?,getDate(),getDate(),?,?,?)";
    protected static final String BOOKING_COMMODITY = "insert into editempbooking_commodity(book_no,commodityid,edireaderid,version,itemQty, "
            + "itemWeight,itemWeightUnit,hazardous,eqpid,Alcohol,militarycargo,cargono,"
            + "packageId,itemVolume,itemVolumeUnit,itemForward,itemLeft,itemTop,itemAft,itemRight,itemLengthUnit,comdtygrp,itemNo,carriercode,itemDesc) "
            + "values(?,?,?,?,?,?,?,?,?,'N','N',?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    protected static final String BOOKING_ROUTING = "insert into editempbooking_routing (origin,originmode,pol,polter,polser,polmode,trans1,trans1ter,trans1ser,"
            + "trans1mode,trans2,trans2ter,trans2ser,trans2mode,trans3,trans3ter,trans3ser,trans3mode,"
            + "pod,podter,podmode,delivery,svccode,book_no,edireaderid,defRout)"
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    protected static final String EBOOKING_REMARKS = "insert into editempbooking_remarks(book_no,remarksref,remarksval,edireaderid,version,category) values(?,?,?,?,?,?)";
    protected static final String EBOOKING_DG_DETAILS = "insert into editempbooking_imo(book_no,eqpid,eqptype,unno,shippingname,imdgclass,flashpoint,flashunit,marinepollutant,edireaderid)"
            + "values(?,?,?,?,?,?,?,?,?,?)";
    protected static final String QrygetEDIID = "select ediid from edimaster(nolock) where ediname='ECOMONLINE BOOKING'";
    protected static final String strgetAgencyMailQry = "select agc.contactemailid from agencycontacts  agc inner join agency ag on agc.agencycode=ag.agencycode where contactdesignation='Customer Service Executive' and ag.agencycode=?";
    protected static final String GetLoginCustomerquery = "select comp.countryCode as Country,comp.compname as comp_name,comp.address1 as Address,comp.address2 as Address1,City,um.emailId as User_Email,(um.FirstName+um.LastName) as name,"
            + " um.fax as fax,um.phone as phone,comp.postalcode as Pin_Code"
            + " from ECOM_USERDETAIL  um (nolock) inner join ECOM_COMPANY comp(nolock) on  um.refid=comp.refid"
            + " where  comp.CompCode = ? and um.loginname=?";
    private static final String BOOK_NO_GEN = "select mdlid,mdlprefix,mdlindex from ecomMdlIndex where mdlid = ?";
    private static final String BOOK_NO_UPDATE = "update ecomMdlIndex set mdlindex=? "
            + "where mdlid =?";
    private static final String GET_AGENCY_CODE = "select agencycode from agencylocationsnew where portcode=?";
    private static final String INSER_EDIBKG_REF = " insert into editempbooking_reference(book_no,refid,refval,isedited,edireaderid,version) values(?,?,?,?,?,?)";
    protected static final String ServiceNameQRY = "select serviceType from service(nolock) where servicecode=?";
    protected static final String BOOKING_CUST = "insert into editempbooking_cust(book_no, custTypeId,custId, "
            + "countryId, name, address1, city, zipCode, telephone, fax,email,edireaderid,address2) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?,?)";
    protected static final String EBOOKING_CONT = "insert into editempbooking_cargo (book_no, type,eqptypeid,edireaderid,eqpId, cargono,oog,shipperown,cargoweight,"
            + "grossweight,weightunit,tempMax,tempMin,setTemp,tempUnit,"
            + "nor,ventSetting,loadingCondition,isDummy,dgflag,eqpstatus,orderno,tareWeight) "
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0,'FF','Y',?,?,?,?)";
    protected static final String booking_leg_editemp = "insert into editempbooking_leg(book_no,leg_no,bound,servicecode,vesselcode,loadportid,dischargeportid,etddate,etadate,legTypeId,precarriage,oncarriage,loadtype,dischtype,voyagecode,dateup,edireaderid,version,previousLeg,groupcallid,routeid)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,getdate(),?,?,?,?,?)";
    protected static final String strEqTrWQry = "select distinct TareKg,Equiptype,equipdesc from equipments(nolock) where equiptype=? and status='A'";
    protected static final String GET_BKG_CUST_SEARCH = "exec usp_eEcomBookingCustomerSearch ?,?";
    protected static final String GET_TOP_BKG_CUSTS = "exec usp_getEcomLastestCustomerFromBooking ?,'366','MC','',?";
    private static final String GET_ECOM_BOOKING_STATUS = "select ebook_status from ecombooking where ebookno=?";
    private static final String GET_EDIDOCSTATUS = "select status from edidocstatus where book_no=? order by receivdate desc";
    private static final String UPDATE_EDIDOCSTATUS = "update edidocstatus set status='V' where book_no=?";
    private static final String UPDATE_ECOM_CANCELBKG = "update ecombooking set ebook_status=? where ebookno=?";

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public ArrayList getV1LRPBookingViewDetails(String bookNo) throws RemoteException {
        ArrayList viewlist = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String custType = null;
        boolean n1 = false;
        String phone = null, fax = null;
        EbookingValidatorFormBeanNew bookVO = null;
        try {
            viewlist = new ArrayList();
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(getLRPBookingViewbookingMasterQry, 1004, 1007);
            ps.setString(1, bookNo);
            rs = ps.executeQuery();
            bookVO = new EbookingValidatorFormBeanNew();
            if (rs.next()) {
                bookVO.setEbook_date(checkNull(rs.getString("bookdate")));
                bookVO.setEbook_dateBut(checkNull(rs.getString("bookdate")));
                bookVO.setAmendment("Y");
                bookVO.setBooking_refno(checkNull(rs.getString("book_no")));
                bookVO.setPodintransit(checkNull("").trim());
                bookVO.setCarrierContact(checkNull(rs.getString("transportserviceid")));
                bookVO.setEmptyPickup(checkNull(rs.getString("polterminal")));
                bookVO.setEbook_origin_country(checkNull(rs.getString("originportid")));
                bookVO.setEbook_delivery_country(checkNull(rs.getString("finalportid")));
                bookVO.setEbook_origin(checkNull(rs.getString("originportid")));
                bookVO.setEbook_delivery(checkNull(rs.getString("finalportid")));
                bookVO.setEbook_pod_country(checkNull(rs.getString("dischargeportid")));
                bookVO.setEbook_pol_country(checkNull(rs.getString("loadportid")));
                bookVO.setEbook_pod(checkNull(rs.getString("dischargeportid")));
                bookVO.setEbook_pol(checkNull(rs.getString("loadportid")));
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
                //System.out.println("[ECOM] EcomV1BookingBean L>>ref values>>" + rs.getString("refval"));
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
            ps = con.prepareStatement(GET_BKG_BREF_VALUES, 1004, 1007);
            ps.setString(1, "EOR");
            ps.setString(2, bookNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                bookVO.setEbook_status_remarks(checkNull(rs.getString("refval")));
            }
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
                    bookVO.setEbook_bookingcity(checkNull(rs.getString("city")));
                    bookVO.setEbook_bookingcity_desc(checkNull(rs.getString("city")));
                    bookVO.setEbook_bookingcountry(checkNull(rs.getString("countryId")));
                    bookVO.setEbook_bookingname(checkNull(rs.getString("name")));
                    bookVO.setEbook_bookingcode(checkNull(rs.getString("custId")));
                    bookVO.setEbook_bookingaddress(checkNull(rs.getString("address1")));
                    bookVO.setEbook_bookingaddress1(checkNull(rs.getString("address2")));
                    bookVO.setEbook_bookingemail(checkNull(rs.getString("email")));
                    phone = checkNull(rs.getString("telephone"));
                    fax = checkNull(rs.getString("fax"));
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

                    String strtelephone = bookVO.getEbook_consigneephone();
                } else if (custType.equalsIgnoreCase("NT") && n1 == false) {
                    n1 = true;
                    bookVO.setEbook_notifycity(checkNull(rs.getString("city")));
                    bookVO.setEbook_notifycity_desc(checkNull(rs.getString("city")));
                    bookVO.setEbook_notifycountry(checkNull(rs.getString("countryId")));
                    bookVO.setEbook_notifyname(checkNull(rs.getString("name")));
                    bookVO.setEbook_notifycode(checkNull(rs.getString("custId")));
                    bookVO.setEbook_notifyaddress(checkNull(rs.getString("address1")));
                    bookVO.setEbook_notifyaddress1(checkNull(rs.getString("address2")));
                    bookVO.setEbook_notifyphone(checkNull(rs.getString("telephone")));
                    bookVO.setEbook_notifyemail(checkNull(rs.getString("email")));
                    bookVO.setEbook_notifyfax(checkNull(rs.getString("fax")));
                    bookVO.setEbook_notify_id((checkNull(rs.getString("cid"))));
                } else if (custType.equalsIgnoreCase("NT") && n1 == true) {
                    //System.out.println("[ECOM] EcomV1BookingBean .....cidty............" + rs.getString("city"));
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
        return viewlist;

    }

    @Override
    public ArrayList getV1LRPBookingRoutingDetails(String bookNo) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
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
                //   //System.out.println("[ECOM] EcomV1BookingBean .........routing details..........");
                routing.setBound(checkNull(rs.getString("bound")));
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

    @Override
    public ArrayList getV1LRPBookingViewCargoDetails(String bookNo) throws RemoteException {
        ArrayList cargoList = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null, rs1 = null, rs2 = null;
        int i = 0;
        int qty = 0;
        EbookingValidatorFormBeanNew eBookingcargo = null;
        String eqpId = null, cargoVol = null, weight = null, VolumeUnt = null, hazardous = null, oog = null, rfr = null, lcl = null, cargoType = null;
        try {
            DecimalFormat df = new DecimalFormat("#.##");
            cargoList = new ArrayList();
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(LRPbookingCargoQry, 1004, 1007);
            ps.setString(1, bookNo);
            rs = ps.executeQuery();
            int j = 1;
            while (rs.next()) {
                eqpId = checkNull(rs.getString("eqpid"));
                ps = con.prepareStatement(LRPbookingComdtyQry, 1004, 1007);
                ps.setString(1, eqpId);
                ps.setString(2, bookNo);
                rs1 = ps.executeQuery();
                i = 0;
                while (rs1.next()) {
                    qty = qty + rs1.getInt("itemNO");
                    eBookingcargo = new EbookingValidatorFormBeanNew();
                    if (i > 0) {
                        eBookingcargo.setDuplicateEqp("Y");
                    } else {
                        eBookingcargo.setDuplicateEqp("N");
                    }
                    i++;
                    eBookingcargo.setEbook_packquantity(rs1.getString("itemqty"));
                    eBookingcargo.setContainerNo(checkNull(rs.getString("eqpid")));
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

                    eBookingcargo.setCargoid_new(String.valueOf(j));

                    cargoVol = checkNullDecimal(rs1.getString("itemvolume"));
                    cargoVol = String.valueOf(new BigDecimal(cargoVol).toPlainString());
                    cargoVol = checkNullbigDecimal(cargoVol);
                    VolumeUnt = checkNull(rs1.getString("itemvolumeunit"));
                    if (cargoVol != null && cargoVol.length() > 0) {
                        Double doublegrosscargoVol = Double.parseDouble(cargoVol);
                        cargoVol = df.format(doublegrosscargoVol);
                    }
                    eBookingcargo.setEbook_cargovolume(cargoVol);
                    eBookingcargo.setEbook_volumeunit(VolumeUnt);
                    cargoType = "";
                    ////System.out.println("[ECOM] EcomV1BookingBean .............values,......in temp,,,,,,,,,,," + !(checkNull(rs.getString("settemp")).equalsIgnoreCase("0") && checkNull(rs.getString("tempmin")).equalsIgnoreCase("0") && checkNull(rs.getString("tempmax")).equalsIgnoreCase("0")));
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
                    cargoList.add(eBookingcargo);
                }
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
    public ReturnVO saveToBooking(EbookingValidatorFormBeanNew eBookingMasterData, ArrayList all, String custcode, String custname, String bookno, String savetype, ArrayList voidCargoListObj, ArrayList voidComListObj, String eqpQuantity, String client_code, String slottype) throws RemoteException {
        ReturnVO rvo = new ReturnVO();
        Connection con = null;
        Connection con_new = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt_new = null, pstmt_remarks = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        custcode = checkNull(custcode);
        custname = checkNull(custname);
        savetype = checkNull(savetype);
        rvo.setStrSaveType(savetype);
        String loginCustName = checkNull(custname);
        String agencycode = "", contactName = null, contactPhone = null, submittedBy = null;
        String book_refno = checkNull(bookno);
        String strgetshipmentTerms = eBookingMasterData.getEbook_shipment();
        if (strgetshipmentTerms.trim().length() > 4) {
            String str1[] = strgetshipmentTerms.split("-");
            eBookingMasterData.setEbook_shipment(str1[0]);
        }
        EbookingValidatorFormBeanNew eBookingcargoData = null;
        try {

            //System.out.println("[ECOM] EcomV1BookingBean >>savetype??>>" + savetype);
            ArrayList cargolist = new ArrayList();
            ArrayList routinglist = new ArrayList();
            Route eBookingrouting = null;
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
                for (int i = 0; i < size; i++) {
                    if (i == 0) {
                        cargolist = (ArrayList) all.get(i);
                    } else {
                        routinglist = (ArrayList) all.get(i);
                    }
                }
            }
            //Getting Login Customer Details
            preCarriage = eBookingMasterData.getPreCarriage();
            onCarriage = eBookingMasterData.getOnCarriage();

            StringBuffer hssb = new StringBuffer();
            StringBuffer otherssb = new StringBuffer();
            StringBuffer dgsb = new StringBuffer();
            String ediid = null;
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            String readerid = "";
            size = all.size();
            if (all != null && size > 0) {
                for (int i = 0; i
                        < size; i++) {
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
//                    if (eBookingMasterData.getEbook_portofloading() != null && eBookingMasterData.getEbook_portofloading().length() > 0) {
//                        agencycode = getAgencydetails(eBookingMasterData.getEbook_portofloading());
//                    } else {
            agencycode = getAgencydetails(eBookingMasterData.getEbook_origin());
            //  }
            //System.out.println("[ECOM] EcomV1BookingBean =======agencycode======>" + agencycode);
            //
            readerid = bookNoGenerate("EDI");
            //System.out.println("[ECOM] EcomV1BookingBean >>readerid>>" + readerid);
            if (readerid != null && readerid.length() > 0) {
                String lclflag = "NR"; //editempbooking - shipment_type
                //Rate reference
                ps = con.prepareStatement(INSER_EDIBKG_REF);
                ps.setString(1, book_refno);
                ps.setString(2, "DAK");
                ps.setString(3, book_refno);
                ps.setString(4, "N");
                ps.setString(5, readerid);
                ps.setString(6, "0");
                ps.executeUpdate();

                if (checkNull(eBookingMasterData.getEbook_rate_reference()).length() > 0) {
                    pstmt = con.prepareStatement(EBOOKING_REMARKS);
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, "Rate reference");
                    pstmt.setString(3, eBookingMasterData.getEbook_rate_reference());
                    pstmt.setString(4, readerid);//agencycode
                    pstmt.setString(5, "0");
                    pstmt.setString(6, "E");
                    int up_remarks = pstmt.executeUpdate();
                    //
                    pstmt_remarks = con.prepareStatement(INSER_EDIBKG_REF);
                    pstmt_remarks.setString(1, book_refno);
                    pstmt_remarks.setString(2, "EOR");
                    pstmt_remarks.setString(3, eBookingMasterData.getEbook_rate_reference());
                    pstmt_remarks.setString(4, "N");
                    pstmt_remarks.setString(5, readerid);
                    pstmt_remarks.setString(6, "0");
                    pstmt_remarks.executeUpdate();

                }

                preCarriage = eBookingMasterData.getPreCarriage();
                onCarriage = eBookingMasterData.getOnCarriage();

                //
                String routeID = eBookingMasterData.getEbook_routeid();
                String Routing_qry = "SELECT r.origin, r.BLPOL, r.BLPOLTer, r.mode, r.BLPOLSvc, r.OpsPol1,r.OpsPol1Ter,"
                        + "r.opsMode1, r.OpsPOLSVC1, r.OpsPol2, r.OpsPol2Ter, r.opsMode2, r.OpsPOLSVC2,r.OpsPol3, r.OpsPol3Ter, r.OpsMode3,"
                        + "r.OpsPolSvc3, r.BlPod, r.BlPodTer, r.delivery, r.DefRoute, r.MainSvc,r.TransitTime,r.orgmode,r.delmode,r.id,r.datecr FROM Routedetail r(nolock) "
                        + " WHERE r.id=? and r.status = 'A'";
                ps = con.prepareStatement(Routing_qry, 1004, 1007);
                ps.setString(1, routeID);
                rs = ps.executeQuery();
                String Origin = "", Blpol = "", BlpolTer = "", BlpolSVC = "", BlpolMode = "", Trans1 = "";
                String Trans1Ter = "", Trans1SVC = "", Trans1Mode = "", Trans2 = "", Trans2Ter = "", Trans2SVC = "", Trans2Mode = "";
                String Trans3 = "", Trans3Ter = "", Trans3SVC = "", Trans3Mode = "", Blpod = "", BlpodTer = "", Delivery = "", BlpodMode = "";
                String Service = "", DefRoute = "", Routing_id = "", Feeder = "";
                if (rs.next()) {
                    Origin = checkNull(rs.getString("origin"));
                    BlpolMode = checkNull(rs.getString("orgmode"));
                    Blpol = checkNull(rs.getString("BLPOL"));
                    BlpolTer = checkNull(rs.getString("BLPOLTer"));
                    BlpolSVC = checkNull(rs.getString("BLPOLSvc"));
                    BlpolMode = checkNull(rs.getString("mode"));
                    Trans1 = checkNull(rs.getString("OpsPol1"));
                    Trans1Ter = checkNull(rs.getString("OpsPol1Ter"));
                    Trans1SVC = checkNull(rs.getString("OpsPOLSVC1"));
                    Trans1Mode = checkNull(rs.getString("opsMode1"));
                    Trans2 = checkNull(rs.getString("OpsPol2"));
                    Trans2Ter = checkNull(rs.getString("OpsPol2Ter"));
                    Trans2SVC = checkNull(rs.getString("OpsPOLSVC2"));
                    Trans2Mode = checkNull(rs.getString("opsMode2"));
                    Trans3 = checkNull(rs.getString("OpsPol3"));
                    Trans3Ter = checkNull(rs.getString("OpsPol3Ter"));
                    Trans3SVC = checkNull(rs.getString("OpsPolSvc3"));
                    Trans3Mode = checkNull(rs.getString("OpsMode3"));
                    Blpod = checkNull(rs.getString("BlPod"));
                    BlpodTer = checkNull(rs.getString("BlPodTer"));
                    BlpodMode = checkNull(rs.getString("delmode"));
                    Delivery = checkNull(rs.getString("delivery"));
                    Service = checkNull(rs.getString("MainSvc"));
                    DefRoute = checkNull(rs.getString("DefRoute"));
                    Routing_id = String.valueOf(rs.getInt("id"));
                }
                //
                polterminal = BlpolTer;
                podterminal = BlpodTer;
                //
                //System.out.println("[ECOM] EcomV1BookingBean #########MainService####>>>>>" + Service);
                String oceanVslCode = "", oceanVoyage = "", oceanSerCode = "", oceanBound = "", oceanSerCode_temp = "";
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
                if (eBookingMasterData.getRemarks() != null) {
                    addremarks = addremarks + eBookingMasterData.getRemarks().replace("\r", "");
                }
                String BOOKING_MASTER = "insert into editempbooking (book_no, agencyid,"
                        + "oceanVslCode, oceanVoyage, oceanSerCode, oceanBound, serviceCode,vesselCode, voyageCode, bound, originPortId, "
                        + "finalPortId, loadPortId, dischargePortId, polTerminal, polAt,podTerminal, podAt, bookDate, bookReady, "
                        + "preCarriage, onCarriage, status,usercr, datecr,messagetype,transtype,edireaderid,documentalAgencyId,agencyissueid,agencypolid,remarks,firstsvctype,bookType,crossBook,originPortDesc,finalPortDesc,shipment_type,paymenttermsId,booksource,messagesender,carriercode,callid,custremarks,AgencyRefNo,transportserviceid,intransitport,intransitportname) "
                        + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, getdate(), ?, ?, ?, ?, ?, getDate(),?,?,?,?,?,?,?,?,'online','',?,?,?,?,'E','EcomOnlineBkg','',?,?,?,?,?,?)";
                feeder = getServiceName(service);
                pstmt = con.prepareStatement(BOOKING_MASTER);
                pstmt.setString(1, book_refno);
                pstmt.setString(2, agencycode);
                pstmt.setString(3, oceanVslCode);
                pstmt.setString(4, oceanVoyage);
                pstmt.setString(5, oceanSerCode);
                pstmt.setString(6, oceanBound);
                pstmt.setString(7, service);
                pstmt.setString(8, vessel);
                pstmt.setString(9, voyage);
                pstmt.setString(10, bound);
                pstmt.setString(11, eBookingMasterData.getEbook_origin());
                pstmt.setString(12, eBookingMasterData.getEbook_delivery());
                pstmt.setString(13, eBookingMasterData.getEbook_portofloading());
                pstmt.setString(14, eBookingMasterData.getEbook_portofdischarge());
                pstmt.setString(15, polterminal);
                if (poletd != null && poletd.length() > 0 && (!poletd.equalsIgnoreCase("-"))) {
                    pstmt.setString(16, poletd);
                } else {
                    pstmt.setString(16, null);
                }
                pstmt.setString(17, podterminal);
                if (poleta != null && poleta.length() > 0 && (!poleta.equalsIgnoreCase("-"))) {
                    pstmt.setString(18, poleta);
                } else {
                    pstmt.setString(18, null);
                }
                if (eBookingMasterData.getEbook_date().length() > 0) {
                    pstmt.setString(19, eBookingMasterData.getEbook_date());
                } else {
                    pstmt.setString(19, null);
                }
                pstmt.setString(20, preCarriage);
                pstmt.setString(21, onCarriage);
                pstmt.setString(22, "D");
                pstmt.setString(23, "ECOM");
                pstmt.setString(24, GeneralConf.EDI_MESSAGETYPE_BKG);
                pstmt.setString(25, "9");
                pstmt.setString(26, readerid);
                pstmt.setString(27, agencycode);
                pstmt.setString(28, agencycode);
                pstmt.setString(29, book_refno);
                pstmt.setString(30, "");
                pstmt.setString(31, feeder);
                String strOrigin = eBookingMasterData.getEbook_origin_country().substring(7, eBookingMasterData.getEbook_origin_country().length());
                String strDeli = eBookingMasterData.getEbook_delivery_country().substring(7, eBookingMasterData.getEbook_delivery_country().length());
                pstmt.setString(32, strOrigin.trim());
                pstmt.setString(33, strDeli.trim());
                pstmt.setString(34, lclflag);
                pstmt.setString(35, eBookingMasterData.getEbook_shipment());
                pstmt.setString(36, slottype);
                pstmt.setString(37, addremarks);
                pstmt.setString(38, checkNullUpperCase(eBookingMasterData.getEbook_agency_reference()));
                pstmt.setString(39, (eBookingMasterData.getCarrierContact()));
                pstmt.setString(40, checkNull(eBookingMasterData.getPodintransit()).trim());
                pstmt.setString(41, checkNull(eBookingMasterData.getHidPodIntransitName()).trim());
                int Master_update = pstmt.executeUpdate();
                //
                pstmt = con.prepareStatement(BOOKING_ROUTING);
                pstmt.setString(1, Origin);
                pstmt.setString(2, BlpolMode);
                pstmt.setString(3, Blpol);
                pstmt.setString(4, BlpolTer);
                pstmt.setString(5, BlpolSVC);
                pstmt.setString(6, BlpolMode);
                pstmt.setString(7, Trans1);
                pstmt.setString(8, Trans1Ter);
                pstmt.setString(9, Trans1SVC);
                pstmt.setString(10, Trans1Mode);
                pstmt.setString(11, Trans2);
                pstmt.setString(12, Trans2Ter);
                pstmt.setString(13, Trans2SVC);
                pstmt.setString(14, Trans2Mode);
                pstmt.setString(15, Trans3);
                pstmt.setString(16, Trans3Ter);
                pstmt.setString(17, Trans3SVC);
                pstmt.setString(18, Trans3Mode);
                pstmt.setString(19, Blpod);
                pstmt.setString(20, BlpodTer);
                pstmt.setString(21, BlpodMode);
                pstmt.setString(22, Delivery);
                pstmt.setString(23, Service);
                pstmt.setString(24, book_refno);
                pstmt.setString(25, readerid);
                pstmt.setString(26, DefRoute);
                int Route_update = pstmt.executeUpdate();
                //
                pstmt = con.prepareStatement(booking_leg_editemp);
                if (routinglist != null && routinglist.size() > 0) {
                    int len = routinglist.size();
                    for (int k = 0; k < len; k++) {
                        eBookingrouting = (Route) routinglist.get(k);
                        String pol = eBookingrouting.getLoadport();
                        String pod = eBookingrouting.getDisport();
                        String pol_str = eBookingrouting.getLoadtype();
                        String pod_str = eBookingrouting.getDisctype();
                        int leg_no = eBookingrouting.getLegNo();
                        String legTypeId = eBookingrouting.getLegType();
                        service = eBookingrouting.getService();
                        vessel = eBookingrouting.getVessel();
                        voyage = eBookingrouting.getVoyage();
                        poleta = eBookingrouting.getETA1();
                        poletd = eBookingrouting.getETD1();
                        bound = eBookingrouting.getBound();
                        preCarriage = eBookingMasterData.getPreCarriage();
                        onCarriage = eBookingMasterData.getOnCarriage();
                        String grp_CallId = eBookingrouting.getGrpCallId();
                        pstmt.setString(1, book_refno);
                        pstmt.setInt(2, leg_no);
                        pstmt.setString(3, bound);
                        pstmt.setString(4, service);
                        pstmt.setString(5, vessel);
                        pstmt.setString(6, pol);
                        pstmt.setString(7, pod);
                        pstmt.setString(8, poleta);  //
                        pstmt.setString(9, poletd);   //
                        pstmt.setString(10, legTypeId);
                        pstmt.setString(11, preCarriage);
                        pstmt.setString(12, onCarriage);
                        pstmt.setString(13, pol_str);
                        pstmt.setString(14, pod_str);
                        pstmt.setString(15, voyage);
                        //   pstmt.setString(16, loginCustName);
                        pstmt.setString(16, readerid);
                        pstmt.setInt(17, 0);
                        pstmt.setInt(18, leg_no - 1);
                        pstmt.setString(19, grp_CallId);
                        pstmt.setString(20, routeID);
                        pstmt.executeUpdate();
                    }
                }
                //
                if (eBookingMasterData.getEbook_agreename() != null) {
                    // Insert Login user as AGrement Party
                    pstmt = con.prepareStatement(BOOKING_CUST);
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, "AP");
                    pstmt.setString(3, custcode);
                    pstmt.setString(4, eBookingMasterData.getEbook_agreecountry());
                    pstmt.setString(5, eBookingMasterData.getEbook_agreename());
                    pstmt.setString(6, eBookingMasterData.getEbook_agreeaddress());
                    pstmt.setString(7, eBookingMasterData.getEbook_agreecity());
                    pstmt.setString(8, "");
                    pstmt.setString(9, eBookingMasterData.getEbook_agreephone());
                    pstmt.setString(10, eBookingMasterData.getEbook_agreefax());
                    pstmt.setString(11, eBookingMasterData.getEbook_agreeemail());
                    pstmt.setString(12, readerid);
                    pstmt.setString(13, eBookingMasterData.getEbook_agreeaddress1());
                    int Ap_update = pstmt.executeUpdate();
                }
                String cust_name = "";
                cust_name = eBookingMasterData.getEbook_shippername();
                if (cust_name.length() > 0) {
                    pstmt = con.prepareStatement(BOOKING_CUST);
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, "SH");
                    pstmt.setString(3, eBookingMasterData.getEbook_shippercode());
                    pstmt.setString(4, eBookingMasterData.getEbook_shippercountry());
                    pstmt.setString(5, eBookingMasterData.getEbook_shippername());
                    pstmt.setString(6, eBookingMasterData.getEbook_shipperaddress());
                    pstmt.setString(7, eBookingMasterData.getEbook_shippercity());
                    pstmt.setString(8, "");
                    pstmt.setString(9, eBookingMasterData.getEbook_shipperphone());
                    pstmt.setString(10, eBookingMasterData.getEbook_shipperfax());
                    pstmt.setString(11, eBookingMasterData.getEbook_shipperemail());
                    pstmt.setString(12, readerid);
                    pstmt.setString(13, eBookingMasterData.getEbook_shipperaddress1());
                    int Sh_update = pstmt.executeUpdate();
                }
                cust_name = "";
                cust_name = eBookingMasterData.getEbook_consigneename();
                if (cust_name.length() > 0) {
                    pstmt = con.prepareStatement(BOOKING_CUST);
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, "CO");
                    pstmt.setString(3, eBookingMasterData.getEbook_consigneecode());
                    pstmt.setString(4, eBookingMasterData.getEbook_consigneecountry());
                    pstmt.setString(5, eBookingMasterData.getEbook_consigneename());
                    pstmt.setString(6, eBookingMasterData.getEbook_consigneeaddress());
                    pstmt.setString(7, eBookingMasterData.getEbook_consigneecity());
                    pstmt.setString(8, "");
                    pstmt.setString(9, eBookingMasterData.getEbook_consigneephone());
                    pstmt.setString(10, eBookingMasterData.getEbook_consigneefax());
                    pstmt.setString(11, eBookingMasterData.getEbook_consigneeemail());
                    pstmt.setString(12, readerid);
                    pstmt.setString(13, eBookingMasterData.getEbook_consigneeaddress1());
                    int Cn_update = pstmt.executeUpdate();

                }
                cust_name = "";
                cust_name = eBookingMasterData.getEbook_notifyname();
                if (cust_name.length() > 0) {
                    pstmt = con.prepareStatement(BOOKING_CUST);
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, "NT");
                    pstmt.setString(3, eBookingMasterData.getEbook_notifycode());
                    pstmt.setString(4, eBookingMasterData.getEbook_notifycountry());
                    pstmt.setString(5, eBookingMasterData.getEbook_notifyname());
                    pstmt.setString(6, eBookingMasterData.getEbook_notifyaddress());
                    pstmt.setString(7, eBookingMasterData.getEbook_notifycity());
                    pstmt.setString(8, "");
                    pstmt.setString(9, eBookingMasterData.getEbook_notifyphone());
                    pstmt.setString(10, eBookingMasterData.getEbook_notifyfax());
                    pstmt.setString(11, eBookingMasterData.getEbook_notifyemail());
                    pstmt.setString(12, readerid);
                    pstmt.setString(13, eBookingMasterData.getEbook_notifyaddress1());
                    int Nt_update = pstmt.executeUpdate();
                }
                cust_name = "";
                cust_name = eBookingMasterData.getEbook_notifyname_n1();
                if (cust_name.length() > 0) {
                    pstmt = con.prepareStatement(BOOKING_CUST);
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, "NT");
                    pstmt.setString(3, eBookingMasterData.getEbook_notifycode_n1());
                    pstmt.setString(4, eBookingMasterData.getEbook_notifycountry_n1());
                    pstmt.setString(5, eBookingMasterData.getEbook_notifyname_n1());
                    pstmt.setString(6, eBookingMasterData.getEbook_notifyaddress_n1());
                    pstmt.setString(7, eBookingMasterData.getEbook_notifycity_n1());
                    pstmt.setString(8, "");
                    pstmt.setString(9, eBookingMasterData.getEbook_notifyphone_n1());
                    pstmt.setString(10, eBookingMasterData.getEbook_notifyfax_n1());
                    pstmt.setString(11, eBookingMasterData.getEbook_notifyemail_n1());
                    pstmt.setString(12, readerid);
                    pstmt.setString(13, eBookingMasterData.getEbook_notifyaddress1_n1());
                    int Nt_update = pstmt.executeUpdate();
                }
                //Insert ocean freight in remari
                if (eBookingMasterData.getEbook_ocean_freight() != null && eBookingMasterData.getEbook_ocean_freight().length() > 0) {
                    pstmt_remarks = con.prepareStatement(EBOOKING_REMARKS);
                    pstmt_remarks.setString(1, book_refno);
                    pstmt_remarks.setString(2, "Ocean Freight : ");
                    if (eBookingMasterData.getEbook_ocean_freight().equalsIgnoreCase("C")) {
                        pstmt_remarks.setString(3, "Collect");
                    } else {
                        pstmt_remarks.setString(3, "Prepaid");
                    }
                    pstmt_remarks.setString(4, readerid);
                    pstmt_remarks.setString(5, "0");
                    pstmt_remarks.setString(6, "M");
                    pstmt_remarks.executeUpdate();

                    //Insert into reference table
                    pstmt_remarks = con.prepareStatement(INSER_EDIBKG_REF);
                    pstmt_remarks.setString(1, book_refno);
                    pstmt_remarks.setString(2, "EOF");
                    pstmt_remarks.setString(3, eBookingMasterData.getEbook_ocean_freight());
                    pstmt_remarks.setString(4, "N");
                    pstmt_remarks.setString(5, readerid);
                    pstmt_remarks.setString(6, "0");
                    pstmt_remarks.executeUpdate();

                }
                //Insert empty pick up place
                String emptyPickUp = null;
                if (eBookingMasterData.getEmptyPickup() != null && eBookingMasterData.getEmptyPickup().length() > 0) {
                    emptyPickUp = eBookingMasterData.getEmptyPickUpName();
                    if (eBookingMasterData.getEmptyPickUpDate() != null && eBookingMasterData.getEmptyPickUpDate().length() > 0) {
                        emptyPickUp = emptyPickUp + "  Date : " + eBookingMasterData.getEmptyPickUpDate();
                    }
                    pstmt_remarks = con.prepareStatement(EBOOKING_REMARKS);
                    pstmt_remarks.setString(1, book_refno);
                    pstmt_remarks.setString(2, "Empty Place of Pick Up : ");
                    pstmt_remarks.setString(3, emptyPickUp);
                    pstmt_remarks.setString(4, readerid);
                    pstmt_remarks.setString(5, "0");
                    pstmt_remarks.setString(6, "M");
                    pstmt_remarks.executeUpdate();

                    //Insert into reference table
                    pstmt_remarks = con.prepareStatement(INSER_EDIBKG_REF);
                    pstmt_remarks.setString(1, book_refno);
                    pstmt_remarks.setString(2, "EEP");
                    pstmt_remarks.setString(3, eBookingMasterData.getEmptyPickup());
                    pstmt_remarks.setString(4, "N");
                    pstmt_remarks.setString(5, readerid);
                    pstmt_remarks.setString(6, "0");
                    pstmt_remarks.executeUpdate();
                    //Insert into reference table
                    if (eBookingMasterData.getEmptyPickUpDate() != null && eBookingMasterData.getEmptyPickUpDate().length() > 0) {
                        pstmt_remarks = con.prepareStatement(INSER_EDIBKG_REF);
                        pstmt_remarks.setString(1, book_refno);
                        pstmt_remarks.setString(2, "EPD");
                        pstmt_remarks.setString(3, eBookingMasterData.getEmptyPickUpDate());
                        pstmt_remarks.setString(4, "N");
                        pstmt_remarks.setString(5, readerid);
                        pstmt_remarks.setString(6, "0");
                        pstmt_remarks.executeUpdate();
                    }
                }

                // Insert Commodity Details
                pstmt = con.prepareStatement(BOOKING_COMMODITY);
                Hashtable htcargo = new Hashtable();
                ArrayList al;
                int eqpQuantityvalue = Integer.parseInt(eqpQuantity);
                int len_cargo = cargolist.size();
                String eqpid = "";
                int k = 0;
                hssb.append("");
                otherssb.append("");
                dgsb.append("");
                boolean othersboolean = false, dgboolean = false;
                for (int i = 0; i < len_cargo; i++) {
                    eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                    eqpQuantityvalue = Integer.parseInt(eBookingcargoData.getEbook_eqp_qty());
                    int CargoNo = 1;
                    for (int j = 0; j < eqpQuantityvalue; j++) {
                        eqpid = eBookingcargoData.getEbook_eqp_type_desc();
                        if (htcargo.containsKey(eqpid)) {
                            al = (ArrayList) htcargo.get(eqpid);
                            al.add(j);
                        } else {
                            al = new ArrayList();
                            al.add(j);
                        }
                        htcargo.put(eqpid, al);
                        k = (j + 1);
                        String eqp_result = eqpid.substring(0, eqpid.length() - 1);
                        eqpid = eqp_result.concat("TBN");
                        eqpid = eqpid + al.size();
                        if (eBookingcargoData != null) {
                            String haz = "N";
                            String hazflag = eBookingcargoData.getHzd();
                            if ("Y".equalsIgnoreCase(checkNull(hazflag))) {
                                haz = "Y";
                            }
                            String cargovolume = eBookingcargoData.getEbook_cargovolume();
                            String volumeunit = eBookingcargoData.getEbook_volumeunit();
                            String olforward = eBookingcargoData.getEbook_olforward();
                            String owleft = eBookingcargoData.getEbook_owleft();
                            String oh = eBookingcargoData.getEbook_oh();
                            String olaft = eBookingcargoData.getEbook_olaft();
                            String owright = eBookingcargoData.getEbook_owright();
                            String dimensionunit = eBookingcargoData.getEbook_dimensionunit();
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
                            pstmt.setString(1, book_refno);
                            if (eBookingcargoData.getCommodityothers().equalsIgnoreCase("Y")) {
                                pstmt.setString(2, "");
                            } else {
                                pstmt.setString(2, checkNull(eBookingcargoData.getEbook_commoditycode()));
                            }
                            pstmt.setString(3, checkNull(readerid));
                            pstmt.setString(4, "0");
                            pstmt.setString(5, checkNull(eBookingcargoData.getEbook_packquantity()));
                            pstmt.setString(6, checkNull(eBookingcargoData.getEbook_cargoweight()));
                            pstmt.setString(7, checkNull(eBookingcargoData.getEbook_weightunit()));
                            pstmt.setString(8, checkNull(haz));
                            pstmt.setString(9, checkNull(eqpid));
                            pstmt.setInt(10, (al.size()));
                            pstmt.setString(11, checkNull(eBookingcargoData.getEbook_packagecode()));
                            pstmt.setString(12, checkNull(cargovolume));
                            pstmt.setString(13, checkNull(volumeunit));
                            pstmt.setString(14, checkNull(olforward));
                            pstmt.setString(15, checkNull(owleft));
                            pstmt.setString(16, checkNull(oh));
                            pstmt.setString(17, checkNull(olaft));
                            pstmt.setString(18, checkNull(owright));
                            pstmt.setString(19, checkNull(dimensionunit));
                            if (eBookingcargoData.getCommodityothers().equalsIgnoreCase("Y")) {
                                pstmt.setString(20, "");
                            } else {
                                pstmt.setString(20, checkNull(eBookingcargoData.getEbook_commoditycode()));
                            }
                            pstmt.setString(21, "0");
                            pstmt.setString(22, client_code);
                            pstmt.setString(23, eBookingcargoData.getEbooking_commodity_desc());
                            CargoNo++;//
                            pstmt.addBatch();
                            k++;
                        }

                    }
                }
                int[] Commodity_update = pstmt.executeBatch();
                String ishazardous = "N";
                for (int i = 0; i < len_cargo; i++) {
                    eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                    ishazardous = checkNull(eBookingcargoData.getHzd());
                    if (ishazardous.equalsIgnoreCase("Y")) {
                        //System.out.println("[ECOM] EcomV1BookingBean book_refno " + book_refno + " eqpid " + eqpid + " ebook_eqp " + eBookingcargoData.getEbook_eqp()
                          //      + " ebook_unno " + eBookingcargoData.getEbook_unno() + " ebook_shipname " + eBookingcargoData.getEbook_shipname()
                     //           + " ebook_dgclass " + eBookingcargoData.getEbook_dgclass() + " ebook_flashpoint " + eBookingcargoData.getEbook_flashpoint()
                      //          + " ebook_flashunit " + eBookingcargoData.getEbook_flashunit() + " ebook_poluttunit " + eBookingcargoData.getEbook_poluttunit() + " readerid " + readerid);
                        String unno = checkNull(eBookingcargoData.getEbook_unno());
                        if (unno != null && unno.length() > 0) {
                            unno = unno.split("~~")[0].trim();
                        }
                        pstmt = con.prepareStatement(EBOOKING_DG_DETAILS);
                        pstmt.setString(1, checkNull(book_refno));
                        pstmt.setString(2, checkNull(eqpid));
                        pstmt.setString(3, checkNull(eBookingcargoData.getEbook_eqp()));
                        pstmt.setString(4, unno);
                        pstmt.setString(5, checkNull(checkNull(eBookingcargoData.getEbook_shipname())));
                        pstmt.setString(6, checkNull(checkNull(eBookingcargoData.getEbook_dgclass())));
                        pstmt.setString(7, checkNull(checkNull(eBookingcargoData.getEbook_flashpoint())));
                        pstmt.setString(8, checkNull(checkNull(eBookingcargoData.getEbook_flashunit())));
                        pstmt.setString(9, checkNull(checkNull(eBookingcargoData.getEbook_poluttunit())));
                        pstmt.setString(10, checkNull(readerid));
                        pstmt.addBatch();
                    }
                }
                //pstmt.executeBatch();
                //
                String hscodecheck = hssb.toString();
                if (hscodecheck != null && hscodecheck.trim().length() > 0) {
                    pstmt_remarks = con.prepareStatement(EBOOKING_REMARKS);
                    pstmt_remarks.setString(1, book_refno);
                    pstmt_remarks.setString(2, "HS Code  : ");
                    pstmt_remarks.setString(3, hssb.toString());
                    pstmt_remarks.setString(4, readerid);
                    pstmt_remarks.setString(5, "0");
                    pstmt_remarks.setString(6, "M");
                    pstmt_remarks.executeUpdate();
                    //
                }
                if (dgboolean) {
                    pstmt_remarks = con.prepareStatement(EBOOKING_REMARKS);
                    pstmt_remarks.setString(1, book_refno);
                    pstmt_remarks.setString(2, "DG : ");
                    pstmt_remarks.setString(3, dgsb.toString());
                    pstmt_remarks.setString(4, readerid);
                    pstmt_remarks.setString(5, "0");
                    pstmt_remarks.setString(6, "M");
                    pstmt_remarks.executeUpdate();
                    //
                }
                if (othersboolean) {
                    pstmt_remarks = con.prepareStatement(EBOOKING_REMARKS);
                    pstmt_remarks.setString(1, book_refno);
                    pstmt_remarks.setString(2, "Other Commodity : ");
                    pstmt_remarks.setString(3, otherssb.toString());
                    pstmt_remarks.setString(4, readerid);
                    pstmt_remarks.setString(5, "0");
                    pstmt_remarks.setString(6, "M");
                    pstmt_remarks.executeUpdate();

                    //
                }

                //Insert eBooking Container Data
                pstmt = con.prepareStatement(EBOOKING_CONT);
                Hashtable htc = new Hashtable();
                ArrayList alc = new ArrayList();
                String eqpidnew = "";
                String tareW = "";
                int increment = 0;
                for (int i = 0; i < len_cargo; i++) {
                    eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                    String eqptype = eBookingcargoData.getEbook_eqp_type();
                    eqpQuantityvalue = Integer.parseInt(eBookingcargoData.getEbook_eqp_qty());
                    for (int j = 0; j < eqpQuantityvalue; j++) {
                        tareW = getEqpTareW(eBookingcargoData.getEbook_eqp());
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
                        eqpid = eqp_resultc.concat("TBN");
                        eqpid = eqpid + alc.size();
                        increment = (j + 1);
                        String flashpoint = eBookingcargoData.getEbook_flashpoint();
                        String mintemp = eBookingcargoData.getEbook_mintemp();
                        String maxtemp = eBookingcargoData.getEbook_maxtemp();
                        String presetemp = eBookingcargoData.getEbook_presetemp();
                        String flashunit = eBookingcargoData.getEbook_flashunit();
                        String temperunit = eBookingcargoData.getEbook_temperunit();
                        String dgflag = "N";
                        String hazflag = eBookingcargoData.getHzd();
                        if ("Y".equalsIgnoreCase(checkNull(hazflag))) {
                            dgflag = "Y";
                        }
                        String laden = eBookingcargoData.getEqpstatus();
                        String oogflag = eBookingMasterData.getOog();
                        if (eBookingcargoData != null) {
                            pstmt.setString(1, book_refno);
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
                                pstmt.setString(2, checkNull(eBookingcargoData.getEbook_eqp_type()));
                            } else {
                                pstmt.setString(2, "N");
                            }
                            pstmt.setString(3, checkNull(eBookingcargoData.getEbook_eqp()));
                            pstmt.setString(4, readerid);
                            pstmt.setString(5, eqpid);
                            pstmt.setInt(6, alc.size());
                            pstmt.setString(7, checkNull(eBookingcargoData.getOog()));
                            pstmt.setString(8, checkNull(eBookingcargoData.getSoc()));
                            pstmt.setString(9, checkNull(eBookingcargoData.getEbook_cargoweight()));
                            pstmt.setString(10, checkNull(eBookingcargoData.getEbook_cargoweight()));
                            pstmt.setString(11, checkNull(eBookingcargoData.getEbook_weightunit()));
                            pstmt.setString(12, maxtemp);
                            pstmt.setString(13, mintemp);
                            pstmt.setString(14, presetemp);
                            pstmt.setString(15, temperunit);
                            pstmt.setString(16, eBookingcargoData.getEbook_nor());
                            pstmt.setString(17, checkNull(dgflag));
                            pstmt.setString(18, checkNull(laden));
                            pstmt.setString(19, checkNull(eBookingMasterData.getEbook_orderNo()));
                            //System.out.println("[ECOM] EcomV1BookingBean >>tareW>>>>>" + tareW + ">>>maxtempcheck>>>" + maxtemp);
                            if ("T".equalsIgnoreCase(eBookingcargoData.getEbook_weightunit())) {
                                float tare = (float) (Float.parseFloat(tareW) * 0.001);
                                tareW = String.valueOf(tare);
                            } else if ("P".equalsIgnoreCase(eBookingcargoData.getEbook_weightunit())) {
                                float tare = (float) (Float.parseFloat(tareW) * 2.204);
                                tareW = String.valueOf(tare);
                            }
                            pstmt.setString(20, tareW);
                            pstmt.addBatch();
                            increment++;
                        }
                    }
                }
                int[] Cargo_update = pstmt.executeBatch();

                // int[] Cargo_update1 = pstmt.executeBatch();
                //int[] Cargo_update3 = pstmt.executeBatch();
                // Insert into edidocstatus
                pstmt = con.prepareStatement(EDI_DOC_STATUS);
                pstmt.setString(1, book_refno);
                pstmt.setString(2, readerid);
                pstmt.setString(3, "9");
                pstmt.setString(4, "R");
                pstmt.setString(5, GeneralConf.EDI_TYPE_BKG);
                pstmt.setString(6, GeneralConf.EDI_MESSAGETYPE_BKG);
                pstmt.setString(7, GeneralConf.EDI_ECOM);
                int Doc_Update = pstmt.executeUpdate();
                pstmt = con.prepareStatement(QrygetEDIID, 1004, 1007);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    ediid = checkNull(rs.getString("ediid"));
                }
                if (ediid != null && ediid.length() > 0) {
                    // Insert into edireadermaster
                    pstmt = con.prepareStatement(EDIREADER_MASTER);
                    pstmt.setString(1, ediid);
                    pstmt.setString(2, readerid);
                    pstmt.setString(3, agencycode);
                    pstmt.setString(4, agencycode);
                    pstmt.setString(5, "Completed");
                    int Edi_Master_Update = pstmt.executeUpdate();
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
                    pstmt = con.prepareStatement(EBOOKING_REMARKS);
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, "Submitted By");
                    pstmt.setString(3, submittedBy);
                    pstmt.setString(4, readerid);//agencycode
                    pstmt.setString(5, "0");
                    pstmt.setString(6, null);
                    pstmt.executeUpdate();

                    pstmt = con.prepareStatement(EBOOKING_REMARKS);
                    pstmt.setString(1, book_refno);
                    pstmt.setString(2, "Online Reference No");
                    pstmt.setString(3, book_refno);
                    pstmt.setString(4, readerid);//agencycode
                    pstmt.setString(5, "0");
                    pstmt.setString(6, null);
                    pstmt.executeUpdate();
                }
                //
                String strAgencyCode = agencycode;
                String strAgency = getAgencyMail(strAgencyCode);
                rvo.setAdmToMailId(strAgency);

            } // Id Close
        } catch (Exception e) {
            //System.out.println("[ECOM] EcomV1BookingBean Exception While calling  saveToBooking->" + e.getMessage());
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
                if (pstmt_new != null) {
                    pstmt_new.close();
                }
                if (con_new != null) {
                    con_new.close();
                }
            } catch (Exception e) {
                //System.out.println("[ECOM] EcomV1BookingBean Exception While Closing connetion in  saveToBooking->" + e.getMessage());
            }
        }
        return rvo;
    }

    public String getEqpTareW(String eqpType) throws RemoteException {
        ////System.out.println("[ECOM] EcomV1BookingBean *********getEqpTareW********");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String str = "";
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(strEqTrWQry, 1004, 1007);
            ps.setString(1, eqpType);
            rs = ps.executeQuery();
            while (rs.next()) {
                str = checkNull(rs.getString("TareKg"));
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

    public String getAgencyMail(String agencycode) {
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
            //System.out.println("[ECOM] EcomV1BookingBean *******email*************==>" + email);
        } catch (Exception e) {
            //System.out.println("[ECOM] EcomV1BookingBean ******==>" + e);
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
                //System.out.println("[ECOM] EcomV1BookingBean Exception While Closing Connection" + e.getMessage());
                e.printStackTrace();
            }
        }
        return email;
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

    public String getAgencydetails(String port) throws RemoteException {
        //System.out.println("[ECOM] EcomV1BookingBean getAgencydetails" + port);
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
            ////System.out.println("[ECOM] EcomV1BookingBean Query" + GET_AGENCY_CODE);
            //System.out.println("[ECOM] EcomV1BookingBean port>>>" + port);
            pstmt.setString(1, port);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                agency = checkNull(rs.getString("agencycode"));
            }
        } catch (Exception e) {
            //System.out.println("[ECOM] EcomV1BookingBean Exception while geting Agency code  " + e.getMessage());
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
                //System.out.println("[ECOM] EcomV1BookingBean Exception while closing the connection : " + e.getMessage());
                e.printStackTrace();
            }
        }

        return agency;
    }

    private String bookNoGenerate(String module) {
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
            //System.out.println("[ECOM] EcomV1BookingBean ===========NEW bknum==========>>>>>>" + bknum);
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
                //System.out.println("[ECOM] EcomV1BookingBean exception while closing connection bookNoGenerate" + e);
                e.printStackTrace();
            }

        }
        return bknum;
    }

    @Override
    @SuppressWarnings("static-access")
    public List getV1eBookingCustomerInfo(String columnName, String constraints, String columnValue, String custCode) throws RemoteException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List parties = new ArrayList();
        String result = "";
        CustomerProfile profile = null;
        String phone = null, fax = null, email = null;
        try {
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            result = setConstraints(columnName, constraints, columnValue);
            ps = con.prepareStatement(GET_BKG_CUST_SEARCH, 1004, 1007);
            ps.setString(1, custCode);
            ps.setString(2, result);
            rs = ps.executeQuery();
            while (rs.next()) {
                phone = checkNull(rs.getString("telephone"));
                fax = checkNull(rs.getString("fax"));
                profile = new CustomerProfile();
                profile.setCustId(checkNull(rs.getString("custID")));
                profile.setCustName(checkNull(rs.getString("name")));
                profile.setAddr1(checkNull(rs.getString("address1")));
                profile.setAddr2(checkNull(rs.getString("address2")));
                profile.setCity(checkNull(rs.getString("city")).toUpperCase());
                profile.setCountry(checkNull(rs.getString("countryId")).toUpperCase());
                profile.setCountryName(checkNull(rs.getString("countryName")).toUpperCase());
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
                email = checkNull(rs.getString("email"));
                if (email.length() > 0 && email.equalsIgnoreCase("-")) {
                    email = "";
                }
                profile.setEmailId(checkNull(rs.getString("email")));
                profile.setCitycode(checkNull(rs.getString("city")).toUpperCase());  // Included by Lavanya M on 03/01/2009  Purpose: To set the city description
                parties.add(profile);
            }
        } catch (Exception e) {
            //System.out.println("[ECOM] EcomV1BookingBean Exception" + e);
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
                //System.out.println("[ECOM] EcomV1BookingBean Exception " + es);
            }
        }
        return parties;
    }

    @Override
    public List getV1MoreCustomerDetails(String custCode, String moduleType) throws RemoteException {
        //   //System.out.println("[ECOM] EcomV1BookingBean .......custcode.............." + custCode);
        List custList = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String custType = null, phone = null, fax = null;
        CustomerDetailsVO custVO = null;
        try {
            custList = new ArrayList();
            con = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(GET_TOP_BKG_CUSTS, 1004, 1007);
            ps.setString(1, custCode);
            ps.setString(2, moduleType);
            rs = ps.executeQuery();
            while (rs.next()) {
                custVO = new CustomerDetailsVO();
                custVO.setName(checkNull(rs.getString("custname")));
                custVO.setCustCode(checkNull(rs.getString("custcode")));
                if ("CO".equalsIgnoreCase(checkNull(rs.getString("custtypeid")))) {
                    custVO.setCustType("CN");
                } else {
                    custVO.setCustType(checkNull(rs.getString("custtypeid")));
                }
                custVO.setAddress(checkNull(rs.getString("address1")));
                custVO.setAddress1(checkNull(rs.getString("address2")));
                custVO.setCity(checkNull(rs.getString("city")));
                custVO.setCountryCode(checkNull(rs.getString("countryId")));
                custVO.setCountry(checkNull(rs.getString("countryId")));
                custVO.setEmail(checkNull(rs.getString("email")));
                phone = checkNull(rs.getString("telephone"));
                fax = checkNull(rs.getString("fax"));
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
        return custList;
    }

    @Override
    public String cancelEcomV1Booking(String ecomBookNo, String lrpBookNo) throws RemoteException {
        Connection con = null, v1lrp = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "Not Processed";
        String status = null, ecomStatus = null;
        try {
            con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
            v1lrp = JDBCServiceLocatorFactory.getConnection("LRPV1DS");
            ps = con.prepareStatement(GET_ECOM_BOOKING_STATUS);
            ps.setString(1, ecomBookNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                ecomStatus = rs.getString("ebook_status");
            }
            ps = v1lrp.prepareStatement(GET_EDIDOCSTATUS);
            ps.setString(1, ecomBookNo);
            if (rs.next()) {
                status = rs.getString("status");
            }

            //System.out.println("[ECOM] EcomV1BookingBean >>>edi status>>" + status+ ">>ecomStatus>>" + ecomStatus);
            if ("R".equalsIgnoreCase(status) && "S".equalsIgnoreCase(ecomStatus)) {
                ps = v1lrp.prepareStatement(UPDATE_EDIDOCSTATUS);
                ps.setString(1, ecomBookNo);
                ps.executeUpdate();
                ps = con.prepareStatement(UPDATE_ECOM_CANCELBKG);
                if (lrpBookNo != null && lrpBookNo.length() > 0) {
                    ps.setString(1, "V");
                } else {
                    ps.setString(1, "I");
                }
                ps.setString(2, ecomBookNo);
                ps.executeUpdate();
            } else {
                result = "Processed";
            }
            //System.out.println("[ECOM] EcomV1BookingBean result>>>" + result);
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
        ////System.out.println("[ECOM] EcomV1BookingBean Column name==>" + columnName + con + Val);
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

    private String checkNullbigDecimal(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"0.0000".equalsIgnoreCase(str)) && (!"".equalsIgnoreCase(str))) ? str.trim() : "";

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

}
