
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.AppTierCntrlBean;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.svm.lrp.nfr.query.cbean.VDSInvoker;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import lrp.serverutils.refs.ServerObject;
import net.ecom.common.DateConversion_New;
import net.ecom.common.EcomConsts;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;
import net.ecom.si.vo.Bl;
import net.ecom.si.vo.Bl_cargo;
import net.ecom.si.vo.Bl_commodity;
import net.ecom.si.vo.Bl_cust;
import net.ecom.si.vo.CompleteModel;
import net.ecom.si.vo.Vesselmaster;
import net.ecom.vds.service.EcomBKGSpsService;
import net.ecom.vds.service.EcomBKGVdsService;
import net.ecom.vds.service.EcomBlVdsService;
import net.ecom.vds.service.EcomSiVdsService;
import net.ecom.vo.AmendmentBookingFormBean;
import net.ecom.vo.BokingDetailds;
import net.ecom.vo.EcomBookingCargoDetails;
import net.ecom.vo.EcomBookingCustomer;
import net.ecom.vo.EcomBookingScheduleVo;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIContainerDetailVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;
import net.ecom.vo.SISealVO;

/**
 *
 * @author preethi
 */
@Stateless(name = "EcomAmendmentMailBean")
@Remote(EcomAmendmentMailBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS")
    ,
    @Resource(name = "jdbc/MASTERDB", mappedName = "java:/MASTERDS")
})

public class EcomAmendmentMailBean implements EcomAmendmentMailBeanRemote {

    @Inject
    VDSInvoker vds;

    EcomBKGVdsService ecomvdsservice;
    EcomBKGSpsService ecomvdspservice;
//    EcomSiVdsService siVDS;
    EcomBlVdsService blVDS;

    @EJB(mappedName = "java:global/e-comOnline-EJBModule_New/EComCntrlBean!net.ecom.AppTierCntrlBean.EComCntrlBeanRemote")
    EComCntrlBeanRemote cntrlBeanObj;
    @EJB(mappedName = "java:global/e-comOnline-EJBModule_New/EcomBLBean!net.ecom.AppTierCntrlBean.EcomBLBeanRemote")
    EcomBLBeanRemote locobj;

    @PostConstruct
    public void inti() {
        ecomvdsservice = vds.create(EcomBKGVdsService.class);
        ecomvdspservice = vds.create(EcomBKGSpsService.class);
        blVDS = vds.create(EcomBlVdsService.class);
    }

    @Override
    public BokingDetailds getBookingDetails(Map bookChace, String booktype, String Type) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rst = null;
        BokingDetailds bookVO = null;
        ArrayList<EcomBookingCargoDetails> cargo_list = null;
        ArrayList<EcomBookingCustomer> cust_list = null;
        EcomBookingScheduleVo schedule_list = null;
        SimpleDateFormat target = new SimpleDateFormat(GeneralConf.getStrDbFormat());
        SimpleDateFormat source = new SimpleDateFormat(GeneralConf.strDbFormatWithOutTime);
        Map hs = null;
        Map BookDitails = bookChace;
        String bookNo = "", ecombookNo = "", ediReadId = "", siNo = "", blNo = "";

        if ("Booking".equalsIgnoreCase(booktype)) {
            bookNo = (String) BookDitails.get("LRPBook_no");
            ediReadId = (String) BookDitails.get("EDIRead_id");
            ecombookNo = (String) BookDitails.get("EcomBookNo");
        } else {
            bookNo = (String) BookDitails.get("LRPBook_no");
            ediReadId = (String) BookDitails.get("EDIRead_id");
            siNo = (String) BookDitails.get("SINo");
            blNo = (String) BookDitails.get("BLNo");

        }
        System.out.println("[ECOM] EcomAmendmentMailBean Inside bookNo " + bookNo + "ediReadId==>" + ediReadId);
        try {
            bookVO = new BokingDetailds();
            cargo_list = new ArrayList<EcomBookingCargoDetails>();
            cust_list = new ArrayList<EcomBookingCustomer>();
            schedule_list = new EcomBookingScheduleVo();
            List lst = null;
//            Map referencemap = new HashMap();
            if ("BL".equalsIgnoreCase(booktype) && "P".equalsIgnoreCase(Type)) {
                try {
                    CompleteModel blModelObj = blVDS.getecomportdetforbl(blNo);
                    for (Vesselmaster vesselMasterObj : blModelObj.getVesselmaster()) {
                        bookVO.setEbook_date(checkNull(vesselMasterObj.getPolat()));
                        bookVO.setEbook_origin_country(checkNull(vesselMasterObj.getOriginportid()));
                        bookVO.setEbook_delivery_country(checkNull(vesselMasterObj.getFinalportid()));
                        bookVO.setCarrierContact(checkNull(vesselMasterObj.getServicecode()));
                        bookVO.setAgency_reference(checkNullUpperCase(vesselMasterObj.getAgencyRefNo()));
                        bookVO.setCargoprotectid((vesselMasterObj.getCargoprotectid()));
                        break;
                    }
                    Map referencemap = (Map) locobj.getBlReference(blNo);
                    System.out.println("[ECOM] EcomAmendmentMailBean referencemap==>" + referencemap);
                    if (referencemap != null && referencemap.size() > 0) {
                        bookVO.setACID(checkNull((String) referencemap.get(EcomConsts.ACID)));
                    }

                    CompleteModel siModelObj = blVDS.getecombldetails(blNo);
                    System.out.println("[ECOM] EcomAmendmentMailBean siModelObj==>" + siModelObj);
                    String userup = "", usercr = "";
                    for (Bl blObj : siModelObj.getBl()) {
                        userup = checkNull(blObj.getUserup());
                        usercr = checkNull(blObj.getUsercr());
                        bookVO.setBlType(checkNull(blObj.getBltype()));
                        System.out.println("[ECOM] EcomAmendmentMailBean setBlType==>" + bookVO.getBlType());
                    }
                    String agencymailid = !"".equalsIgnoreCase(checkNull(userup)) ? checkNull(userup) : checkNull(usercr);
                    System.out.println("[ECOM] EcomAmendmentMailBean blNo===>" + blNo + "agencymailid" + agencymailid + "userup" + userup + "usercr" + usercr);
                    String mailContactId = getAgencyMail(agencymailid);
                    bookVO.setAmendmentTomailId(checkNull(mailContactId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("[ECOM] EcomAmendmentMailBean type===>" + Type + "ediReadId=>" + ediReadId + "bookNo==>" + bookNo);
                if ("P".equalsIgnoreCase(Type)) {
                    hs = (Map) ecomvdsservice.getecombookingdetails(bookNo);
                    lst = (List) hs.get("booking");
                } else if ("C".equalsIgnoreCase(Type)) {
                    hs = (Map) ecomvdsservice.geteditembookdetails(ediReadId);
                    lst = (List) hs.get("editempbooking");
                }
                if (lst.size() > 0) {
                    Map rs = (Map) lst.get(0);
                    System.out.println("[ECOM] EcomAmendmentMailBean rs==>" + rs);
                    bookVO.setEbook_date(checkNull((String) rs.get("polat")));
                    bookVO.setEbook_origin_country(checkNull((String) rs.get("originportid")));
                    bookVO.setEbook_delivery_country(checkNull((String) rs.get("finalportid")));
                    bookVO.setCarrierContact(checkNull((String) rs.get("transportserviceid")));
                    bookVO.setAgency_reference(checkNullUpperCase((String) rs.get("agencyrefno")));
                    if (rs.get("cargoprotectid") != null) {
                        bookVO.setCargoprotectid(((String) rs.get("cargoprotectid")));
                    }
                    bookVO.setEmptyPickup(checkNull((String) rs.get("emptypickupdate")));
                    String agencymailid = !"".equalsIgnoreCase(checkNull((String) rs.get("userup"))) ? checkNull((String) rs.get("userup")) : checkNull((String) rs.get("usercr"));
                    String mailContactId = getAgencyMail(agencymailid);
                    bookVO.setAmendmentTomailId(checkNull(mailContactId));
                }

                List reflst = null;
                if ("P".equalsIgnoreCase(Type)) {
                    hs = (Map) ecomvdsservice.getecombookingreference(bookNo);
                    reflst = (List) hs.get("booking_reference");
                } else {
                    hs = (Map) ecomvdsservice.getecomeditembookrefndetails(ediReadId);
                    reflst = (List) hs.get("editempbooking_reference");
                }

                System.out.println("[ECOM] EcomAmendmentMailBean hs==>" + hs);
                //Additional information 
                if ("Booking".equalsIgnoreCase(booktype)) {
                    if (reflst != null && reflst.size() > 0) {
                        for (int j = 0; j < reflst.size(); j++) {
                            Map rs = (Map) reflst.get(j);
                            String refid = (String) rs.get("refid");
                            if ("EEP".equalsIgnoreCase(refid)) {
                                String[] emptypickup = checkNull((String) rs.get("refval")).split("~~");
                                if (emptypickup.length == 2) {
                                    bookVO.setEmptyPickup(checkNull(emptypickup[1]));
                                    bookVO.setEbook_pickup_point(checkNull(emptypickup[0]));
                                } else {
                                    bookVO.setEmptyPickup(checkNull(emptypickup[0]));
                                }
                            } else if ("EPD".equalsIgnoreCase(refid)) {
                                bookVO.setEmptyPickUpDate(checkNull((String) rs.get("refval")));
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
                                String[] relType = checkNull((String) rs.get("refval")).split("~~");
                                bookVO.setEbook_reftype(relType[0]);
                            } else if (EcomConsts.CUST_REF.equalsIgnoreCase(refid)) {
                                bookVO.setCustomerreference(checkNull((String) rs.get("refval")));
                            } else if ("ACI".equalsIgnoreCase(refid)) {
                                bookVO.setACID(checkNull((String) rs.get("refval")));
                            } else if ("DPW".equalsIgnoreCase(refid)) { 
                                String storedValue = checkNull((String) rs.get("refval"));
                                if ("nullnull".equalsIgnoreCase(storedValue)) {
                                    storedValue = "";

                                }
                                if(storedValue.length() > 0){
                                    bookVO.setShipperReference(checkNull(storedValue.substring(1)));
                                    bookVO.setShipperReferenceType(checkNull(storedValue.substring(0, 1)));
                                } else {
                                    bookVO.setShipperReference("");
                                    bookVO.setShipperReferenceType("");
                                }
                            } 

                        }
                    }
                } else {
                    if (reflst != null && reflst.size() > 0) {
                        for (int j = 0; j < reflst.size(); j++) {
                            Map rs = (Map) reflst.get(j);
                            String refid = (String) rs.get("refid");
                            if ("ACI".equalsIgnoreCase(refid)) {
                                bookVO.setACID(checkNull((String) rs.get("refval")));
                           //     System.out.println("[ECOM] EcomAmendmentMailBean bookVO.setACID==>" + bookVO.getACID());
                            } else if ("EBL".equalsIgnoreCase(refid)) {
                                bookVO.setBlType(checkNull((String) rs.get("refval")));
                          //      System.out.println("[ECOM] EcomAmendmentMailBean setBlType==>" + bookVO.getBlType());
                            }
                        }
                    }
                }
            }
            //Schedule 
            if ("Booking".equalsIgnoreCase(booktype)) {
                schedule_list = (EcomBookingScheduleVo) assignScheduleToVo(lst);
                bookVO.setScheduleDetails(schedule_list);
                System.out.println("[ECOM] EcomAmendmentMailBean schedule_list==>" + schedule_list);
            }
            //Cargo
            cargo_list = (ArrayList<EcomBookingCargoDetails>) assignCargoToVo(ecombookNo, bookNo, ediReadId, blNo, Type, booktype);
            if (cargo_list != null && cargo_list.size() > 0) {
                bookVO.setCargoDetails(cargo_list);
                System.out.println("[ECOM] EcomAmendmentMailBean cargo_list==>" + cargo_list);
            }

            //Customer 
            cust_list = (ArrayList<EcomBookingCustomer>) (assignCustomerToVo(ecombookNo, bookNo, ediReadId, blNo, Type, booktype));
            if (cust_list != null && cust_list.size() > 0) {
                bookVO.setCustomerDetails(cust_list);
                System.out.println("[ECOM] EcomAmendmentMailBean cust_list==>" + cust_list);
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
        return bookVO;

    }

    public ArrayList<EcomBookingCargoDetails> assignCargoToVo(String ecombook, String bookNo, String ediReadId, String blno, String type, String bookType) throws RemoteException {
        ArrayList<EcomBookingCargoDetails> cargo_list = null;
        EcomBookingCargoDetails eBookingcargo = null;
        String cargoVol = null, weight = null, nettWt = null, VolumeUnt = null, hazardous = null, oog = null, rfr = null, lcl = null, cargoType = null;
        boolean isDuplicateCmdty = false;
        Map comhs = null;
        List comlst = null;
        Map hs = null;
        // reuse
        try {
            System.out.println("[ECOM] EcomAmendmentMailBean bookType===>" + bookType);
            cargo_list = new ArrayList<EcomBookingCargoDetails>();
            List lst = null;
            DecimalFormat df = new DecimalFormat("#.###");
            String volume = "", netweight = "";
            HashMap SI_CustDetails = null;
            if ("BL".equalsIgnoreCase(bookType) && "P".equalsIgnoreCase(type)) {
                CompleteModel siModelCargoObj = blVDS.getecomblcargodetails(blno);
                System.out.println("[ECOM] EcomAmendmentMailBean >>>inner join cargo >>>" + siModelCargoObj + " ==after service===");
                cargo_list = new ArrayList<EcomBookingCargoDetails>();
                for (Bl_cargo bkngCargoObj : siModelCargoObj.getBl_cargo()) {
                   // System.out.println("[ECOM] EcomAmendmentMailBean bkngCargoObj==>" + bkngCargoObj);
                    eBookingcargo = new EcomBookingCargoDetails();
                    String bleqpId = checkNull(bkngCargoObj.getEqpid());
                    eBookingcargo.setCargoWtUnit(ConvertDecimalFormat(String.valueOf(bkngCargoObj.getCargoweight())));
                    eBookingcargo.setCargoNo(checkNull(bkngCargoObj.getCargono() + ""));
                //    System.out.println("[ECOM] EcomAmendmentMailBean eBookingcargo.setCargoNo=>" + eBookingcargo.getCargoNo() + "bl ==" + bkngCargoObj.getCargono());
                    eBookingcargo.setTareWt(ConvertDecimalFormat(String.valueOf(bkngCargoObj.getTareweight())));
                    eBookingcargo.setGrossWtUnit(ConvertDecimalFormat(String.valueOf(bkngCargoObj.getGrossweight())));
                    eBookingcargo.setEqpStatus(checkNull(bkngCargoObj.getEqpstatus()));
                    eBookingcargo.setEqp(checkNull(bkngCargoObj.getType()));
                    eBookingcargo.setEqpType(checkNull(bkngCargoObj.getEqptypeid()));
//                    eBookingcargo.setEqpStatus(cargoType);
                    String bloog = checkNull(bkngCargoObj.getOog());
                    eBookingcargo.setOogflag(bloog);
                    eBookingcargo.setSoc(checkNull(bkngCargoObj.getShipperown()));
                    eBookingcargo.setNor(bkngCargoObj.getNor());
                    eBookingcargo.setPresetTemp(checkNull(bkngCargoObj.getSettemp()));
                    eBookingcargo.setTempUnit(bkngCargoObj.getTempunit() + "");
                    eBookingcargo.setMinTemp(checkNull(bkngCargoObj.getTempmin()));
                    eBookingcargo.setMaxTemp(checkNull(bkngCargoObj.getTempmax()));
                    eBookingcargo.setContainerNo(bleqpId);
                    eBookingcargo.setHumidityflag(checkNull(bkngCargoObj.getHumidity_flag()));
                    eBookingcargo.setDrains(checkNull(bkngCargoObj.getDrains()));
                    eBookingcargo.setCo2(checkNull(bkngCargoObj.getCo2value()));
                    eBookingcargo.setO2(checkNull(bkngCargoObj.getO2value()));
                    eBookingcargo.setVentilation(checkNull(bkngCargoObj.getVentpercentage()));
                    eBookingcargo.setCarrierseal(checkNull(bkngCargoObj.getSeal1no()));
                    eBookingcargo.setShipperseal(checkNull(bkngCargoObj.getSeal2no()));
                    eBookingcargo.setMarksDesc(checkNull(bkngCargoObj.getMarksdesc()));
                    cargoType = "";
                    eBookingcargo.setHzd(checkNull(bkngCargoObj.getHazardous()));
                    cargo_list.add(eBookingcargo);
                 //   System.out.println("[ECOM] EcomAmendmentMailBean blno=>" + blno + "bleqpId=>" + bleqpId);
                    CompleteModel siModelComoObj = blVDS.getecomblcommodity(blno, bleqpId);
                    for (Bl_commodity bkngComObj : siModelComoObj.getBl_commodity()) {
                  //      System.out.println("[ECOM] EcomAmendmentMailBean >>>inner join bkngComObj >>>" + bkngComObj);
                        eBookingcargo.setPackageQty(checkNull(String.valueOf(bkngComObj.getItemqty())));
                        volume = bkngComObj.getItemvolume() + "";
//                        volume = checkNull(String.valueOf(bkngComObj.getItemvolume())).length() > 0 ? checkNull(String.valueOf(bkngComObj.getItemvolume())) : "0";
//                        volume = String.valueOf(new BigDecimal(volume).toPlainString());
//                        System.out.println("[ECOM] EcomAmendmentMailBean >>>volume>>" + volume);
//                        System.out.println("[ECOM] EcomAmendmentMailBean Float.parseFloat(volume)>>" + Float.parseFloat(volume));
//                        String volumeDouble = df.format(Float.parseFloat(volume));
//                        if (Float.parseFloat(volume) <= 0) {
//                            volumeDouble = "";
//                        }
                        volume = checkNullDecimal((volume));
                        volume = String.valueOf(new BigDecimal(volume).toPlainString());
                        volume = checkNullbigDecimal(volume);
                        volume = checkNull(volume);
                        if (volume == null || volume.equalsIgnoreCase("0") || volume.equalsIgnoreCase("0.0") || "".equalsIgnoreCase(volume) || Float.parseFloat(volume) < 0) {
                            eBookingcargo.setVolume("");
                        } else {
                            eBookingcargo.setVolume(volume);
                        }
                    //    System.out.println("[ECOM] EcomAmendmentMailBean eBookingcargo.setVolume(==>" + eBookingcargo.getVolume());

                        netweight = bkngComObj.getNettwt() + "";
                    //    System.out.println("[ECOM] EcomAmendmentMailBean Float.parseFloat(netweight)>>" + Float.parseFloat(netweight));
                        String netWeightDouble = df.format(Float.parseFloat(netweight));
                        if (Float.parseFloat(netweight) <= 0) {
                            netWeightDouble = "";
                        }
                        String finalnetwet = !"0.0".equalsIgnoreCase(checkNull(netWeightDouble)) ? checkNull(netWeightDouble) : "";
                        eBookingcargo.setNetWt(checkNull(finalnetwet));
                        eBookingcargo.setVolUnit(checkNull(bkngComObj.getItemvolumeunit()));
                        //rs1 details
                        eBookingcargo.setCommodity(checkNull(bkngComObj.getComdtygrp()));
                  //      System.out.println("[ECOM] EcomAmendmentMailBean  eBookingcargo.setCommodity==>" + eBookingcargo.getCommodity());
                        eBookingcargo.setHscode(checkNull(bkngComObj.getCommodityid()));
                        eBookingcargo.setEqpquantity(checkNull(String.valueOf(bkngComObj.getItemqty())));

                        eBookingcargo.setPackagename(checkNull(bkngComObj.getPackageid()));
                        eBookingcargo.setActCommoName(checkNull(bkngComObj.getMarksdesc()));
                        eBookingcargo.setMarksNumbers(checkNull(bkngComObj.getMarksnumbers()));
                        String olfwd = !"0.0".equalsIgnoreCase(checkNull(String.valueOf(bkngComObj.getItemforward()))) ? checkNull(String.valueOf(bkngComObj.getItemforward())) : "";
                        String owlft = !"0.0".equalsIgnoreCase(checkNull(String.valueOf(bkngComObj.getItemleft()))) ? checkNull(String.valueOf(bkngComObj.getItemleft())) : "";
                        String oh = !"0.0".equalsIgnoreCase(checkNull(String.valueOf(bkngComObj.getItemtop()))) ? checkNull(String.valueOf(bkngComObj.getItemtop())) : "";
                        String olaft = !"0.0".equalsIgnoreCase(checkNull(String.valueOf(bkngComObj.getItemaft()))) ? checkNull(String.valueOf(bkngComObj.getItemaft())) : "";
                        String owrgt = !"0.0".equalsIgnoreCase(checkNull(String.valueOf(bkngComObj.getItemright()))) ? checkNull(String.valueOf(bkngComObj.getItemright())) : "";
                        String dimenUnt = !"".equalsIgnoreCase(checkNull(String.valueOf(bkngComObj.getItemlengthunit()))) ? checkNull(String.valueOf(bkngComObj.getItemlengthunit())) : "M";
                        if (checkNull(bloog).equalsIgnoreCase("Y")) {
                            if ("".equalsIgnoreCase(dimenUnt)) {
                                dimenUnt = "M";
                            }
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
                        eBookingcargo.setOlForward(olfwd);
                        eBookingcargo.setOwLeft(owlft);
                        eBookingcargo.setOh(oh);
                        eBookingcargo.setOlAft(olaft);
                        eBookingcargo.setOwRight(owrgt);
                        eBookingcargo.setDimUnit(dimenUnt);
                        hazardous = checkNull(bkngComObj.getHazardous());
                    }

                }
                System.out.println("[ECOM] EcomAmendmentMailBean cargo_list==>" + cargo_list.toString());
            } else {
                if ("P".equalsIgnoreCase(type)) {
                    hs = (Map) ecomvdsservice.getecombookingcargo(bookNo);
                    lst = (List) hs.get("booking_cargo");
                } else if ("C".equalsIgnoreCase(type)) {
                    hs = (Map) ecomvdsservice.geteditempbookcargodetails(ediReadId);
                    lst = (List) hs.get("editempbooking_cargo");
                }
                if (lst != null && lst.size() > 0) {
                    cargo_list = new ArrayList<EcomBookingCargoDetails>();
                    int j = 1;
                    for (int k = 0; k < lst.size(); k++) {
                        Map rs = (Map) lst.get(k);
                    //    System.out.println("[ECOM] EcomAmendmentMailBean rs==>" + rs);
                        eBookingcargo = new EcomBookingCargoDetails();
                        if ("Booking".equalsIgnoreCase(bookType)) {

                        }
                        String eqpId = checkNull((String) rs.get("eqpid"));
                        eBookingcargo.setCargoWtUnit(ConvertDecimalFormat(checkNull((String) rs.get("cargoweight"))));
                        eBookingcargo.setCargoNo(checkNull((String) rs.get("cargono")));
               //         System.out.println("[ECOM] EcomAmendmentMailBean eBookingcargo.tareWeight=>" + (String) rs.get("tareweight"));
                        weight = checkNull((String) rs.get("tareweight"));
                        weight = String.valueOf(new BigDecimal(weight).toPlainString());
                 //       System.out.println("[ECOM] EcomAmendmentMailBean weight=>" + weight);
                        eBookingcargo.setTareWt(ConvertDecimalFormat(weight));
                        weight = checkNull((String) rs.get("grossweight"));
                        weight = String.valueOf(new BigDecimal(weight).toPlainString());
                        eBookingcargo.setGrossWtUnit(ConvertDecimalFormat(weight));
                        eBookingcargo.setEqpStatus(checkNull((String) rs.get("eqpstatus")));
                        String eqp_type = checkNull((String) rs.get("eqptypeid"));
                   //     System.out.println("[ECOM] EcomAmendmentMailBean eqp_type=>" + eqp_type);
                        eBookingcargo.setCarrierseal(checkNull((String) rs.get("seal1no")));
                        eBookingcargo.setShipperseal(checkNull((String) rs.get("seal2no")));

                        eBookingcargo.setMarksDesc(checkNull((String) rs.get("marksdesc")));
                        eBookingcargo.setEqp(checkNull((String) rs.get("type")));
                        eBookingcargo.setEqpType(eqp_type);
                        eBookingcargo.setSoc(checkNull((String) rs.get("shipperown")));
                        String settemp = checkNull((String) rs.get("settemp"));
                        cargoType = checkNull((String) rs.get("type"));
                        String oogtype = checkNull((String) rs.get("oog"));
                   //     System.out.println("[ECOM] EcomAmendmentMailBean ===>>isdummy" + checkNull((String) rs.get("isdummy")) + "===>>equpType" + cargoType);
//                    if (checkNull((String) rs.get("nor")).equalsIgnoreCase("Y") || (checkNull((String) rs.get("nor")).equalsIgnoreCase("N") && (!"0.0".equalsIgnoreCase(settemp) && settemp.length() > 0))) {
                        if (cargoType.equalsIgnoreCase("R")) {
                            cargoType = "R";
                        } else if (oogtype.equalsIgnoreCase("Y")) {
                            cargoType = "O";
                        } else {
                            cargoType = "D";
                        }
//                        eBookingcargo.setEqpStatus(cargoType);
                        eBookingcargo.setOogflag(oogtype);
                        String tempunit = checkNull((String) rs.get("tempunit"));
                        String mintemp = checkNull((String) rs.get("tempmin"));
                        String maxtemp = checkNull((String) rs.get("tempmax"));
                        if ("0.0".equalsIgnoreCase(settemp)) {
                            settemp = "";
                            tempunit = "";
                            mintemp = "";
                            maxtemp = "";
                        }
                        eBookingcargo.setNor(checkNull((String) rs.get("nor")));
                        eBookingcargo.setPresetTemp(settemp);
                        eBookingcargo.setTempUnit(tempunit);
                        eBookingcargo.setMinTemp(mintemp);
                        eBookingcargo.setMaxTemp(maxtemp);
                        eBookingcargo.setContainerNo(eqpId);

                        eBookingcargo.setHumidityflag(checkNull((String) rs.get("humidity_flag")));
                        eBookingcargo.setDrains(checkNull((String) rs.get("drains")));
                        eBookingcargo.setCo2(checkNull((String) rs.get("co2value")));
                        eBookingcargo.setO2(checkNull((String) rs.get("o2value")));
                        eBookingcargo.setVentilation(checkNull((String) rs.get("ventpercentage")));
                  //      System.out.println("[ECOM] EcomAmendmentMailBean ediReadId=>" + ediReadId + "eqpId" + eqpId);
                        if ("P".equalsIgnoreCase(type)) {
                            comhs = (Map) ecomvdsservice.getecombookingcommodity(bookNo, eqpId);
                            comlst = (List) comhs.get("booking_commodity");
                        } else {
                            comhs = (Map) ecomvdsservice.geteditempbookcommodity(ediReadId, eqpId);
                            comlst = (List) comhs.get("editempbooking_commodity");
                        }

                        if (comlst.size() > 0) {
                            for (int m = 0; m < comlst.size(); m++) {
                                Map rs1 = (Map) comlst.get(m);
                    //            System.out.println("[ECOM] EcomAmendmentMailBean rs1=>>" + rs1);
                                if (!checkNull((String) rs1.get("itemqty")).equalsIgnoreCase("0")) {
                                    eBookingcargo.setPackageQty((String) rs1.get("itemqty"));
                                } else {
                                    eBookingcargo.setPackageQty("");
                                }
                                cargoVol = checkNullDecimal((String) rs1.get("itemvolume"));
                                cargoVol = String.valueOf(new BigDecimal(cargoVol).toPlainString());
                                cargoVol = checkNullbigDecimal(cargoVol);
                                VolumeUnt = checkNull((String) rs1.get("itemvolumeunit"));
                                if (cargoVol == null || cargoVol.equalsIgnoreCase("0") || cargoVol.equalsIgnoreCase("0.0") || "".equalsIgnoreCase(cargoVol) || Float.parseFloat(cargoVol) < 0) {
                                    eBookingcargo.setVolume("");
                                } else {
                                    eBookingcargo.setVolume(cargoVol);
                                }
                                nettWt = checkNullDecimal((String) rs1.get("nettwt"));
                                nettWt = String.valueOf(new BigDecimal(nettWt).toPlainString());
                                nettWt = !"0.0".equalsIgnoreCase(checkNullbigDecimal(nettWt)) ? checkNullbigDecimal(nettWt) : "";
                                if (nettWt == null || nettWt.equalsIgnoreCase("0")) {
                                    eBookingcargo.setNetWt("");
                                } else {
                                    eBookingcargo.setNetWt(nettWt);
                                }
                     //           System.out.println("[ECOM] EcomAmendmentMailBean VolumeUnt==>" + VolumeUnt + "eBookingcargo.setPackageQty(\"\");" + eBookingcargo.getPackageQty());
                                eBookingcargo.setVolUnit(VolumeUnt);
                                cargoType = "";
                                //rs1 details
                                eBookingcargo.setCommodity(checkNull((String) rs1.get("comdtygrp")));
                                eBookingcargo.setHscode(checkNull((String) rs1.get("commodityid")));
                                eBookingcargo.setEqpquantity(checkNull((String) rs1.get("itemno")));
                                eBookingcargo.setHzd(checkNull((String) rs1.get("hazardous")));
                                eBookingcargo.setPackagename(checkNull((String) rs1.get("packageid")));
                                eBookingcargo.setActCommoName(checkNull((String) rs1.get("marksdesc")));
                                eBookingcargo.setMarksNumbers(checkNull((String) rs1.get("marksnumbers")));
                                String olfwd = !"0.0".equalsIgnoreCase(checkNull((String) rs1.get("itemforward"))) ? checkNull((String) rs1.get("itemforward")) : "";
                                String owlft = !"0.0".equalsIgnoreCase(checkNull((String) rs1.get("itemleft"))) ? checkNull((String) rs1.get("itemleft")) : "";
                                String oh = !"0.0".equalsIgnoreCase(checkNull((String) rs1.get("itemtop"))) ? checkNull((String) rs1.get("itemtop")) : "";
                                String olaft = !"0.0".equalsIgnoreCase(checkNull((String) rs1.get("itemaft"))) ? checkNull((String) rs1.get("itemaft")) : "";
                                String owrgt = !"0.0".equalsIgnoreCase(checkNull((String) rs1.get("itemright"))) ? checkNull((String) rs1.get("itemright")) : "";
                                String dimenUnt = !"".equalsIgnoreCase(checkNull((String) rs1.get("itemlengthunit"))) ? checkNull((String) rs1.get("itemlengthunit")) : "M";
                                eBookingcargo.setOlForward(olfwd);
                                eBookingcargo.setOwLeft(owlft);
                                eBookingcargo.setOh(oh);
                                eBookingcargo.setOlAft(olaft);
                                eBookingcargo.setOwRight(owrgt);
                                eBookingcargo.setDimUnit(dimenUnt);
                                hazardous = checkNull((String) rs1.get("hazardous"));

                            }
                        }
                        cargo_list.add(eBookingcargo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cargo_list;
    }

    public ArrayList<EcomBookingCustomer> assignCustomerToVo(String ecombookNo, String bookNo, String ediReadId, String blno, String Type, String booktype) throws RemoteException {
        EcomBookingCustomer vo = null;
        Map hs = null;
        String custType;
        boolean n1 = false;
        ArrayList<EcomBookingCustomer> cust_list = null;
        try {
            cust_list = new ArrayList<EcomBookingCustomer>();
            System.out.println("[ECOM] EcomAmendmentMailBean Type===>" + Type);
            List custlst = null;
            if ("BL".equalsIgnoreCase(booktype) && "P".equalsIgnoreCase(Type)) {
                String telephone = "", email = "", fax = "";
                CompleteModel blModelobj = blVDS.getecomblcust(blno);
                System.out.println("[ECOM] EcomAmendmentMailBean blModelobj=>" + blModelobj);
                for (Bl_cust blcustObj : blModelobj.getBl_cust()) {
                    vo = new EcomBookingCustomer();
                    vo.setCustTypeId(checkNull(blcustObj.getCusttypeid()));
                    vo.setName(checkNull(blcustObj.getName()));
                    vo.setCity(checkNull(blcustObj.getCity()));
//                    vo.setCity_desc("");
                    vo.setCountryId(checkNull(blcustObj.getCountryid()));
                    vo.setCustid(blcustObj.getCustid());
                    vo.setAddress(checkNull(blcustObj.getAddress1()));
                    vo.setAddress1(checkNull(blcustObj.getAddress2()));
                    telephone = checkNull(blcustObj.getTelephone().replace("+", ""));
                    if ("--".equalsIgnoreCase(checkNull(blcustObj.getTelephone()).replace("+", ""))) {
                        telephone = "";
                    }
                    vo.setPhoneNo(telephone);
                 //   System.out.println("[ECOM] EcomAmendmentMailBean telephone==>" + telephone + " ===blcustObj.getTelephone()== " + blcustObj.getTelephone());
                    email = checkNull(blcustObj.getEmail());
                    if (email.length() > 0 && email.equalsIgnoreCase("-")) {
                        email = "";
                    }
                    vo.setEmail(email);
                    fax = checkNull(blcustObj.getFax()).replace("+", "");
                    if ("--".equalsIgnoreCase(fax)) {
                        fax = "";
                    }
                    vo.setFax(fax);
                    vo.setState(checkNull(blcustObj.getStateid()));
                    cust_list.add(vo);
                }
            } else {
                if ("P".equalsIgnoreCase(Type)) {
                    hs = (Map) ecomvdsservice.getecombookingcust(bookNo);
                    custlst = (List) hs.get("booking_cust");
                } else if ("C".equalsIgnoreCase(Type)) {
                    hs = (Map) ecomvdsservice.geteditempbookingcustdetails(ediReadId);
                    custlst = (List) hs.get("editempbooking_cust");
                }
                if (custlst.size() > 0) {

                    for (int j = 0; j < custlst.size(); j++) {
                        Map rs = (Map) custlst.get(j);
                        custType = checkNull((String) rs.get("custtypeid"));

                        if (custType.equalsIgnoreCase("NT") && n1 == false) {
                            n1 = true;
                            vo = new EcomBookingCustomer();
                            vo.setCustTypeId(checkNull("NT"));
                            vo.setName(checkNull((String) rs.get("name")));
                            vo.setCity(checkNull((String) rs.get("city")));
//                            vo.setCity_desc(checkNull((String) rs.get("city")));
                            vo.setCountryId(checkNull((String) rs.get("countryid")));
                            vo.setCustid(checkNull((String) rs.get("custid")));
                            vo.setAddress(checkNull((String) rs.get("address1")));
                            vo.setAddress1(checkNull((String) rs.get("address2")));
                            vo.setPhoneNo(checkNull((String) rs.get("telephone")).replace("+", ""));
                            vo.setEmail(checkNull((String) rs.get("email")));
                            vo.setFax(checkNull((String) rs.get("fax")).replace("+", ""));
                            vo.setState(checkNull((String) rs.get("stateid")));
                            cust_list.add(vo);
                        } else if (custType.equalsIgnoreCase("N1") && n1 == true) {
                            vo = new EcomBookingCustomer();
                            vo.setCustTypeId(checkNull("N1"));
                            vo.setName(checkNull((String) rs.get("name")));
                            vo.setCity(checkNull((String) rs.get("city")));
//                            vo.setCity_desc(checkNull((String) rs.get("city")));
                            vo.setCountryId(checkNull((String) rs.get("countryid")));
                            vo.setCustid(checkNull((String) rs.get("custid")));
                            vo.setAddress(checkNull((String) rs.get("address1")));
                            vo.setAddress1(checkNull((String) rs.get("address2")));
                            vo.setPhoneNo(checkNull((String) rs.get("telephone")).replace("+", ""));
                            vo.setEmail(checkNull((String) rs.get("email")));
                            vo.setFax(checkNull((String) rs.get("fax")).replace("+", ""));
                            vo.setState(checkNull((String) rs.get("stateid")));
                            cust_list.add(vo);
                        } else if (!"".equalsIgnoreCase(checkNull(custType))) {
                            vo = new EcomBookingCustomer();
                      //      System.out.println("[ECOM] EcomAmendmentMailBean custTypeId==>" + custType);
                            vo.setCustTypeId(checkNull(custType));
                            vo.setName(checkNull((String) rs.get("name")));
                            vo.setCity(checkNull((String) rs.get("city")));
//                            vo.setCity_desc(checkNull((String) rs.get("city")));
                            vo.setCountryId(checkNull((String) rs.get("countryid")));
                            vo.setCustid(checkNull((String) rs.get("custid")));
                            vo.setAddress(checkNull((String) rs.get("address1")));
                            vo.setAddress1(checkNull((String) rs.get("address2")));
                            vo.setPhoneNo(checkNull((String) rs.get("telephone")).replace("+", ""));
                            vo.setEmail(checkNull((String) rs.get("email")));
                            vo.setFax(checkNull((String) rs.get("fax")).replace("+", ""));
                            vo.setState(checkNull((String) rs.get("stateid")));
                            cust_list.add(vo);
                        }
                    }
                    System.out.println("[ECOM] EcomAmendmentMailBean cust_list:::::" + cust_list.toString());
                }
            }
        } catch (Exception e) {
        }

        return cust_list;
    }

    public EcomBookingScheduleVo assignScheduleToVo(List masterlist) throws RemoteException {
        EcomBookingScheduleVo bookVO = null;
        Map hs = null;
        try {
            bookVO = new EcomBookingScheduleVo();

            List lst = (List) masterlist;
            if (lst.size() > 0) {
                Map rs = (Map) lst.get(0);
                bookVO.setEbook_origin_country(checkNull((String) rs.get("originportid")));
                bookVO.setEbook_delivery_country(checkNull((String) rs.get("finalportid")));
                bookVO.setEbook_vesselcode(checkNull((String) rs.get("vesselcode")));
                bookVO.setEbook_voyage(checkNull((String) rs.get("voyagecode")));
                bookVO.setEbook_pol_eta(checkNull((String) rs.get("podat")));
                bookVO.setEbook_pol_etd(checkNull((String) rs.get("polat")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookVO;
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

    @Override
    public String generateSheduledetails(String bookNo, String EDIReadId, Map PORTLIST) throws ParseException, RemoteException {
        String orgiginCn = "", pol_etd = "", pol_eta = "", deliveryCon = "", vesselcode = "", boudLeg = "", voyage = "", haulageType = "", AICD = "", emptyPickUpDate = "", emptyPickUp = "", exportReference = "", customerPO = "", haulierCode = "", dropDate = "", pickupDate = "";
        String aforgiginCn = "", afpol_etd = "", afpol_eta = "", afdeliveryCon = "", afvesselcode = "", afboudLeg = "", afvoyage = "";
        String str_type = "", depdate = "", arrdate = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        str_type = "N";
        try {
            Map portlist = (Map) PORTLIST;
            Map hs = (Map) ecomvdsservice.getecombookingleg(bookNo);
            System.out.println("[ECOM] EcomAmendmentMailBean ####hs###>" + hs);
            List lst = (List) hs.get("booking_leg");
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map rs = (Map) lst.get(j);
                    orgiginCn = checkNull(String.valueOf(portlist.get(checkNull((String) rs.get("loadportid")))));
                    deliveryCon = checkNull(String.valueOf(portlist.get(checkNull((String) rs.get("dischargeportid")))));
                 //   System.out.println("[ECOM] EcomAmendmentMailBean orgiginCn=> " + orgiginCn + " deliveryCon=> " + deliveryCon);
                    String lordporttype = Validation.checkNull(cntrlBeanObj.getPortType(checkNull((String) rs.get("loadportid"))));
                    String disporttype = Validation.checkNull(cntrlBeanObj.getPortType(checkNull((String) rs.get("calliddischarge"))));
                    if (!"I".equalsIgnoreCase(lordporttype)) {
                        depdate = checkNull((String) rs.get("etddate"));
                 //       System.out.println("[ECOM] EcomAmendmentMailBean depdate==>" + depdate);
                        Date date = inputFormat.parse((checkNull(depdate)));
                        String formattedDate = outputFormat.format(date);
                        pol_etd = (checkNull(formattedDate));
                    }
                    if (!"I".equalsIgnoreCase(disporttype)) {
                        arrdate = checkNull((String) rs.get("etadate"));
                        Date date = inputFormat.parse((checkNull(arrdate)));
                        String formattedDate = outputFormat.format(date);
                        pol_eta = checkNull(formattedDate);
                    }
                    if ("N".equalsIgnoreCase(str_type) && ("M".equalsIgnoreCase(checkNull((String) rs.get("legtypeid")))) || "F".equalsIgnoreCase(checkNull((String) rs.get("legtypeid")))) {
                        str_type = "Y";
                        vesselcode = checkNull((String) rs.get("vesselcode"));
                        voyage = checkNull((String) rs.get("voyagecode"));
                    }
                }
            }
            Map hss = (Map) ecomvdsservice.geteditempbookinglegdetails(EDIReadId);
            System.out.println("[ECOM] EcomAmendmentMailBean ####hs###>" + hss);
            List lst1 = (List) hss.get("editempbooking_leg");
            if (lst1.size() > 0) {
                for (int j = 0; j < lst1.size(); j++) {
                    Map rs = (Map) lst1.get(j);
                    aforgiginCn = checkNull(String.valueOf(portlist.get(checkNull((String) rs.get("loadportid")))));
                    afdeliveryCon = checkNull(String.valueOf(portlist.get(checkNull((String) rs.get("dischargeportid")))));
                    String lordporttype = Validation.checkNull(cntrlBeanObj.getPortType(checkNull((String) rs.get("loadportid"))));
                    String disporttype = Validation.checkNull(cntrlBeanObj.getPortType(checkNull((String) rs.get("calliddischarge"))));
                    if (!"I".equalsIgnoreCase(lordporttype)) {
                        depdate = checkNull((String) rs.get("etddate"));
                        Date date = inputFormat.parse(checkNull(depdate));
                        String formattedDate = outputFormat.format(date);
                  //      System.out.println("[ECOM] EcomAmendmentMailBean depdate==>" + formattedDate);
                        afpol_etd = formattedDate;
                    }
                    if (!"I".equalsIgnoreCase(disporttype)) {
                        arrdate = checkNull((String) rs.get("etadate"));
                        Date date = inputFormat.parse(arrdate);
                        String formattedDate = outputFormat.format(date);
                        afpol_eta = checkNull(formattedDate);
                    }
                    if ("N".equalsIgnoreCase(str_type) && ("M".equalsIgnoreCase(checkNull((String) rs.get("legtypeid")))) || "F".equalsIgnoreCase(checkNull((String) rs.get("legtypeid")))) {
                        str_type = "Y";
                        afvesselcode = checkNull((String) rs.get("vesselcode"));
                        afvoyage = checkNull((String) rs.get("voyagecode"));
                    }
                }
            }
        } catch (Exception e) {
        }
        StringBuilder htmlTable = new StringBuilder();

        htmlTable.append("<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;\">   <tr><td></td></tr>\n"
                + "                                 <tr><td></td></tr><tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                + "                                    <td colspan='6' ><h2>Schedule Details</h2></td>\n"
                + "                                </tr> <tr><td></td></tr></table>");
        htmlTable.append("<span></span></br><span style=\"font-size:15px;color:#999;margin:0;padding:3px;width:30%;\">Before Amendment</span>");
        htmlTable.append("<table style=\"overflow: visible; text-align: left; font-variant: normal; font-weight: normal; font-size: 14px; background-color: #fff; line-height: 20px; font-family: Asap, sans-serif; color: #333; padding: 5px; font-style: normal; width: 900px; border: 3px solid #b3b0b0; border-radius: 5px; margin-top: 10px;\">");
        htmlTable.append("<tr style=\"margin:0;padding:0\">");
        // Column 1
        htmlTable.append("<td style=\"width:33.33%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">");
        htmlTable.append("<p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">");
        htmlTable.append("<span>" + orgiginCn + "</span><br>");
        htmlTable.append("<span>" + (pol_etd) + "</span>");
        htmlTable.append("</p>");
        htmlTable.append("<span style=\"font-size:12px;color:#999;margin:0;padding:0\">");
        htmlTable.append("<span>Departure</span>");
        htmlTable.append("</span>");
//        htmlTable.append("<hr style=\"position: absolute; width: 300%; height: 100%; transform: rotate(-5deg); margin: 0; border: none; border-top: 3px solid #b0b0b0; bottom: -35px;\">\n");
        htmlTable.append("</td>");

        // Column 2
        htmlTable.append("<td style=\"width:33.33%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">");
        htmlTable.append("<p style=\"font-weight:700;margin:0 0 5px;padding:0\">");
        htmlTable.append("<span>" + deliveryCon + "</span><br>");
        htmlTable.append("<span>" + (pol_eta) + "</span>");
        htmlTable.append("</p>");
        htmlTable.append("<span style=\"font-size:12px;color:#999;margin:0;padding:0\"> &#x2605 Arrival</span>");
        htmlTable.append("</td>");

        // Column 3
        htmlTable.append("<td style=\"width:23.33%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">");
        htmlTable.append("<p style=\"font-weight:700;margin:0 0 5px;padding:0\">");
        htmlTable.append("<span>" + (vesselcode) + "</span><br>");
        htmlTable.append("<span>" + (voyage + boudLeg) + "</span>");
        htmlTable.append("</p>");
        htmlTable.append("<span style=\"font-size:12px;color:#999;margin:0;padding:0\">Vessel / voyage / bound</span>");
        htmlTable.append("</td>");

        // Column 4
        htmlTable.append("<td style=\"width:10%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">");
        htmlTable.append("<p style=\"font-weight:700;margin:0 0 5px;padding:0\">");
        htmlTable.append("<span>" + daysDifferentBetweenTwoDays(pol_etd, pol_eta) + " days</span>");
        htmlTable.append("</p>");
        htmlTable.append("<span style=\"font-size:12px;color:#999;margin:0;padding:0\">Transit Time</span>");
        htmlTable.append("</td>");

        htmlTable.append("</tr>");
        htmlTable.append("</table>");
        htmlTable.append("<span></span></br><span style=\"font-size:15px;color:#999;margin:0;padding:3px;width:30%;\">After Amendment</span>");
        htmlTable.append("<table style=\"overflow: visible; text-align: left; font-variant: normal; font-weight: normal; font-size: 14px; background-color: #fff; line-height: 20px; font-family: Asap, sans-serif; color: #333; padding: 5px; font-style: normal; width: 900px; border: 3px solid #b3b0b0; border-radius: 5px; margin-top: 10px;\">\n");
        htmlTable.append("    <tr style=\"margin: 0; padding: 0\">\n");

        // Cell 1
        htmlTable.append("      <td style=\"width: 33.33%; font-size: 14px; margin: 0; padding: 10px; vertical-align: middle; position: relative;\">\n");
        htmlTable.append("        <p style=\"font-weight: bold; margin: 0 0 5px; padding: 0; text-transform: capitalize;\">\n");
        htmlTable.append("          <span>" + (aforgiginCn) + "</span><br>\n");
        htmlTable.append("          <span>" + (afpol_etd) + "</span>\n");
        htmlTable.append("        </p>\n");
        htmlTable.append("        <span style=\"font-size: 12px; color: #999; margin: 0; padding: 0;\">\n");
        htmlTable.append("          <span>Departure</span>\n");
        htmlTable.append("        </span>\n");
        htmlTable.append("      </td>\n");

        // Cell 2
        htmlTable.append("      <td style=\"width: 33.33%; font-size: 14px; margin: 0; padding: 10px; vertical-align: middle; position: relative;\">\n");
        htmlTable.append("        <p style=\"font-weight: 700; margin: 0 0 5px; padding: 0;\">\n");
        htmlTable.append("          <span>" + (afdeliveryCon) + "</span><br>\n");
        htmlTable.append("          <span>" + (afpol_eta) + "</span>\n");
        htmlTable.append("        </p>\n");
        htmlTable.append("        <span style=\"font-size: 12px; color: #999; margin: 0; padding: 0;\"> &#x2605 Arrival</span>\n");
        htmlTable.append("      </td>\n");

        // Cell 3
        htmlTable.append("      <td style=\"width: 23.33%; font-size: 14px; margin: 0; padding: 10px; vertical-align: middle; position: relative;\">\n");
        htmlTable.append("        <p style=\"font-weight: 700; margin: 0 0 5px; padding: 0;\">\n");
        htmlTable.append("          <span>" + (afvesselcode) + "</span><br>\n");
        htmlTable.append("          <span>" + (afvoyage) + (afboudLeg) + "</span>\n");
        htmlTable.append("        </p>\n");
        htmlTable.append("        <span style=\"font-size: 12px; color: #999; margin: 0; padding: 0;\">Vessel / voyage / bound</span>\n");
        htmlTable.append("      </td>\n");

        // Cell 4
        htmlTable.append("      <td style=\"width: 10%; font-size: 14px; margin: 0; padding: 10px; vertical-align: middle; position: relative;\">\n");
        htmlTable.append("        <p style=\"font-weight: 700; margin: 0 0 5px; padding: 0;\">\n");
        htmlTable.append("          <span>" + daysDifferentBetweenTwoDays(afpol_etd, afpol_eta) + " days</span>\n");
        htmlTable.append("        </p>\n");
        htmlTable.append("        <span style=\"font-size: 12px; color: #999; margin: 0; padding: 0;\">Transit Time</span>\n");
        htmlTable.append("      </td>\n");

        htmlTable.append("    </tr>\n");
        htmlTable.append("  </table>");

        return htmlTable.toString();
    }

    public int daysDifferentBetweenTwoDays(String fromdate, String todate) throws ParseException {
        SimpleDateFormat formatter = null;
        Date depaturedate = null;
        Date arrdate = null;
        int result = 0;
        try {
            System.out.println("[ECOM] EcomAmendmentMailBean ===>>>>daysDifferentBetweenTwoDays=>" + fromdate + " & " + todate);
            SimpleDateFormat source = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat traget = new SimpleDateFormat("yyyy-MM-dd");
            if (!"".equalsIgnoreCase(checkNull(fromdate)) && !"".equalsIgnoreCase(checkNull(todate))) {
                fromdate = traget.format(source.parse(fromdate));
                todate = traget.format(source.parse(todate));

                formatter = new SimpleDateFormat("yyyy-MM-dd");
                depaturedate = formatter.parse(fromdate);
                arrdate = formatter.parse(todate);
                result = (int) ((depaturedate.getTime() - arrdate.getTime()) / (24 * 60 * 60 * 1000));
                result = Math.abs(result);
            }
        } catch (Exception e) {
            result = 0;
            e.printStackTrace();
        }
        return result;
    }

    private String getAgencyMail(String agencycode) {
        String email = "";
        try {
            int i = 0;
            Map hs = (Map) blVDS.getagencyamendmentmailid(agencycode);
            //
            List lst = (List) hs.get("userdetails");
            if (lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    Map bkg = (Map) lst.get(j);
                    String eml = (String) bkg.get("email");
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
}
