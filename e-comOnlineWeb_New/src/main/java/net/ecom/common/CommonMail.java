/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.ecom.vo.EbookingValidatorFormBeanNew;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.formbeans.SIEditForm;
import net.ecom.vo.AmendmentBookingFormBean;
import net.ecom.vo.BookingHaulagedetailsVo;
import net.ecom.vo.ChargesVo;
import net.ecom.vo.ENSDeclarantVo;
import net.ecom.vo.Route;
import net.ecom.vo.SIConatainerValueVO;
import net.ecom.vo.SIContainerDetailVO;
import net.ecom.vo.SIMasterVO;
import net.ecom.vo.SIPartiesVO;
import net.ecom.vo.SISealVO;

/**
 *
 * @author gobinath.t
 */
public class CommonMail {

    HashMap haulagetype = new HashMap();
    HashMap legtype = new HashMap();
    HashMap unitlis = new HashMap();
    HashMap chargemap = new HashMap();
    long jsdate = new Date().getTime();

    {
        haulagetype.put("L", "Live Load");
        haulagetype.put("D", "Drop Only");
        haulagetype.put("P", "Pickup only");
        haulagetype.put("B", "Pickup & Drop");

        legtype.put("F", EcomConsts.LEG_TYPE_FEEDER);
        legtype.put("M", EcomConsts.LEG_TYPE_MAINLINE);
        legtype.put("R", EcomConsts.LEG_TYPE_RAIL);
        legtype.put("T", EcomConsts.LEG_TYPE_ROAD);
        legtype.put("B", EcomConsts.LEG_TYPE_BARGE);
        legtype.put("C", EcomConsts.LEG_TYPE_COMBINED);
        legtype.put("I", EcomConsts.LEG_TYPE_ITT);

        unitlis.put("D", "Dry");
        unitlis.put("O", "Open");
        unitlis.put("R", "Reefer");
        unitlis.put("K", "Kilos");
        unitlis.put("P", "Pounds");
        unitlis.put("T", "Metric Tons");
        unitlis.put("C", "Celsius");
        unitlis.put("F", "Fahrenheit");

        chargemap.put("P", "Prepaid");
        chargemap.put("C", "Collect");
        chargemap.put("E", "Third Party");
        chargemap.put("AP", "Agr. Party");
        chargemap.put("SH", "Shipper");
        chargemap.put("FW", "Forwarder");
        chargemap.put("CN", "Consignee");
        chargemap.put("NT", "Notify");

    }

    public String newBookingMail(EbookingValidatorFormBeanNew eBookingMasterData, String imagepath, String logo_name, String book_no, HashMap masterCache, ArrayList all) {

        String output = "";
        ArrayList routinglist = null;
        Route eBookingrouting = null;
        ArrayList cargolist = null;
        try {
            HashMap carriageContract = (HashMap) masterCache.get("carriagecontact");
            List addinfo = (ArrayList) masterCache.get("addinfo");
            Map<String, String> intransitMap = (Map<String, String>) masterCache.get("PODINTRANSITMAPCONTEXT");
            Map<String, String> emptyPickUpMap = (Map<String, String>) masterCache.get("EMPTYPICKUPMAP");
            Map vessellist = (Map) masterCache.get("VESSELLIST");
            Map contrylist = (Map) masterCache.get("CONTRYLIST");
            Map serlist = (Map) masterCache.get("SERVICELIST");
            Map packagemap = (Map) masterCache.get("PACKAGELIST");
            Map eqpmap = (Map) masterCache.get("EQUIPMENT_TYPE");
            Map portlist = (Map) masterCache.get("PORTLIST");
            String carriercode = (String) masterCache.get("carriercode");
            Map comlist = (Map) masterCache.get("comlist");
            Map dgshipper = (Map) masterCache.get("SESSION_DGCLASS");
            String hideMinMaxTemp = (String) masterCache.get("hideMinMaxTemp");
            String userFruitCust = (String) masterCache.get("userFruitCust");
            String newdisclaimermsg = (String) masterCache.get("newdisclaimermsg");
            String EnableActualCommoName = (String) masterCache.get("EnableActualCommoName");
            String Enable_Drains_Humidity = (String) masterCache.get("Enable_Drains_Humidity");
            String GENS = (String) masterCache.get("Enable_Eori_Details");
            String enable_zipcode = (String) masterCache.get("enable_zipcode");
            String iseuport = (String) masterCache.get("IsEnableENSTabInBooking");
            String hsnamecode = (String) masterCache.get(GeneralConf.Display_HS_NAME_CODE);
            String hsCodelbl = (("Y".equalsIgnoreCase(checkNull(hsnamecode))) ? "HS Name and Code" : "HS Code");

            String Ventilationlabel = (String) masterCache.get("Ventilation");
            String hideApCustBkgMaster = (String) masterCache.get(GeneralConf.Hide_AP_CUST_BKG_Master);

            if (all != null && all.size() > 0) {
                for (int i = 0; i < all.size(); i++) {
                    if (i == 0) {
                        cargolist = (ArrayList) all.get(i);
                    } else {
                        routinglist = (ArrayList) all.get(i);
                    }
                }
            }
            // LOGO and Book No
            output = "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <meta charset=\"UTF-8\">\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "        <table>\n"
                    + "            <tbody>\n"
                    + "                <tr>\n"
                    + "                    <td>\n"
                    + "                        <table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                    + "                            <tbody>\n"
                    + "                                <tr><td></td></tr>\n"
                    + "                                <tr>\n"
                    + "                                    <td style=\"margin:0 20px 0 0;padding:0 15px 0 0;width:1%\">\n"
                    + "                                        <div style=\"display:inline-block;border-right:1px solid #ccc;margin:0 0 8px 0\">\n"
                    + "                                            <img style=\"padding:10px;max-height: 45px;\" src='" + imagepath + logo_name +"?" + jsdate + "' alt=\"Logo\">\n"
                    
                    + "                                        </div>\n"
                    + "                                    </td>\n"
                    + "\n"
                    + "                                    <td style=\"font-size:30px;margin:0;padding:3px;width:30%\">Booking Request : <span style=\"color:#62be82; font-weight: bold;\"> (" + book_no + ") </span></td>\n"
                    + "\n"
                    + "\n"
                    + "                                </tr>\n"
                    + "                            </tbody>\n"
                    + "                        </table>\n";

            // origin delivery header
            output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;border-bottom: 1px solid #FFC107 !important;\">\n"
                    + "                            <tbody>\n"
                    + "                                <tr>\n"
                    + "                                    <td></td>\n"
                    + "                                </tr>\n"
                    + "                                <tr style=\"height:60px;overflow:hidden;margin-top:20px;padding:0 0 5px;\">\n"
                    + "                                    <td colspan=\"2\" style=\"max-width:33.33%;font-size:16px\">\n"
                    + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0\">\n"
                    + "                                            <span>" + eBookingMasterData.getEbook_origin_country() + "</span>\n"
                    + "                                            <br>\n"
                    + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Origin Port</span>\n"
                    + "                                        </span>\n"
                    + "\n"
                    + "                                    </td>\n"
                    + "                                    <td colspan=\"2\" style=\"max-width:33.33%;text-align:center;font-size:16px\">\n"
                    + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0;margin-right:19px\">\n"
                    + "                                            <span>" + DateConversion_New.convertDefaultFormat(eBookingMasterData.getEbook_date()) + "</span>\n"
                    + "                                            <br>\n"
                    + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Cargo Ready Date</span>\n"
                    + "                                        </span>\n"
                    + "\n"
                    + "                                    </td>\n"
                    + "                                    <td colspan=\"2\" style=\"max-width:33.33%;text-align:right;font-size:16px;\">\n"
                    + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0;margin-right:19px\">\n"
                    + "                                            <span>" + eBookingMasterData.getEbook_delivery_country() + "</span>\n"
                    + "                                            <br>\n"
                    + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Delivery Port</span>\n"
                    + "                                        </span>\n"
                    + "\n"
                    + "                                    </td>\n"
                    + "                                </tr>\n"
                    + "\n"
                    + "                                <tr>\n"
                    + "                                    <td ></td>\n"
                    + "                                </tr>\n"
                    + "                            </tbody>\n"
                    + "                        </table>\n";

            // Master data
            output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;border-bottom: 1px solid #e0e0e0;\">\n"
                    + "                            <tbody>\n"
                    + "                                <tr><td></td></tr>\n"
                    + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                    + "                                    <td colspan='6' ><h2>Master Details</h2></td>\n"
                    + "                                </tr>\n"
                    + "\n"
                    + "\n"
                    + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                    + "                                            <span>" + book_no + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                    + "                                            <span>Online Booking No</span>\n"
                    + "                                        </span>\n"
                    + "                                    </td>\n"
                    + "\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span></span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">LRP Booking No</span>\n"
                    + "                                    </td>\n"
                    + ("Y".equalsIgnoreCase(hideApCustBkgMaster) ? ""
                    : "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span>" + eBookingMasterData.getEbook_agreename() + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Agreement Party</span>\n"
                    + "                                    </td>\n")
                    + "\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span>" + (carriageContract != null ? carriageContract.get(eBookingMasterData.getCarrierContact()) : "") + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Carriage Contract</span>\n"
                    + "                                    </td>\n"
                    + "                                </tr>\n"
                    + "\n"
                    + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                    + "                                            <span>" + ("C".equalsIgnoreCase(eBookingMasterData.getPreCarriage()) ? "Carrier" : ("M".equalsIgnoreCase(eBookingMasterData.getPreCarriage()) ? "Merchant" : "N/A")) + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                    + "                                            <span>Haulage Origin</span>\n"
                    + "                                        </span>\n"
                    + "                                    </td>\n"
                    + "\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span>" + ("C".equalsIgnoreCase(eBookingMasterData.getPreCarriage()) ? "Carrier" : ("M".equalsIgnoreCase(eBookingMasterData.getPreCarriage()) ? "Merchant" : "N/A")) + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Haulage Destination</span>\n"
                    + "                                    </td>\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span></span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\"></span>\n"
                    + "                                    </td>\n"
                    + "\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span></span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\"></span>\n"
                    + "                                    </td>\n"
                    + "                                </tr>\n";

            // Additional information
            if (addinfo != null && addinfo.size() > 0) {
                output += "<tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"padding:10px;\">\n"
                        + "                                        <span style=\"font-size:12px;margin:0;padding:0;font-weight:bold;text-decoration:underline;\">\n"
                        + "                                            Additional Information\n"
                        + "                                        </span>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n";
                output += "<tr style=\"margin:0;padding:0;line-height: 10px;\">\n";
                String addInfoID;
                for (int i = 0; i < addinfo.size(); i++) {
                    addInfoID = (String) addinfo.get(i);
                    if ("EOR".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_rate_reference() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                                + "                                            <span>Rate Reference</span>\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    } else if ("AGR".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_agency_reference() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Agency Ref No.</span>\n"
                                + "                                    </td>\n";
                    } else if ("EOF".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + (("P".equalsIgnoreCase(eBookingMasterData.getEbook_ocean_freight())) ? "Prepaid" : (("C".equalsIgnoreCase(eBookingMasterData.getEbook_ocean_freight()) ? "Collect" : ""))) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Ocean Freight</span>\n"
                                + "                                    </td>\n";
                    } else if ("PFR".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull((String) intransitMap.get(checkNull(eBookingMasterData.getPodintransit()))) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Place of Final Receipt (Intransit)</span>\n"
                                + "                                    </td>\n";
                    } else if ("NAT".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_natcode() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                                + "                                            <span>NAT Code</span>\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    } else if ("ETH".equalsIgnoreCase(addInfoID)) {
                        System.out.println("[ECOM-WAR] CommonMail eBookingMasterData.getEbook_haulagetype()==> " + eBookingMasterData.getEbook_haulagetype() + "haulagetype===> " + haulagetype);
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull((String) haulagetype.get(checkNull(eBookingMasterData.getEbook_haulagetype()))) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Haulage Type</span>\n"
                                + "                                    </td>\n";
                    } else if ("ELD".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(DateConversion_New.convertDefaultFormat(eBookingMasterData.getEbook_pickup_date())) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Pickup Date\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    } else if ("EDD".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(DateConversion_New.convertDefaultFormat(eBookingMasterData.getEbook_drop_date())) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Drop Date</span>\n"
                                + "                                    </td>\n";
                    } else if ("HAU".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_haulercode() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                                + "                                            <span>Haulier Code</span>\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    } else if ("EPO".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_custpo() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Customer PO</span>\n"
                                + "                                    </td>\n";
                    } else if ("ERT".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_reftype() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Release Type</span>\n"
                                + "                                    </td>";
                    } else if ("ERF".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_exportref() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Export Reference</span>\n"
                                + "                                    </td>\n";
                    } else if ("EOP".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                                + "                                            <span>" + (("T".equalsIgnoreCase(eBookingMasterData.getEbook_pickup_point())) ? "Terminal" : (("D".equalsIgnoreCase(eBookingMasterData.getEbook_pickup_point()) ? "Depot" : ""))) + " / " + checkNull(emptyPickUpMap.get(checkNull(eBookingMasterData.getEmptyPickup()))) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                                + "                                            <span>Empty Place of Pick Up\n"
                                + "                                            </span>\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    } else if ("EPD".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(DateConversion_New.convertDefaultFormatTime(eBookingMasterData.getEmptyPickUpDate())) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Empty Place of Pick Up Date</span>\n"
                                + "                                    </td>\n";
                    }
                    if ((i + 1) % 4 == 0 && i != addinfo.size() - 1) {
                        output += "</tr>\n"
                                + "                                <tr style=\"margin:0;padding:0;line-height: 10px;\">\n";
                    }
                }
                output += "                                </tr>\n"
                        + "                            </tbody>\n"
                        + "                        </table>\n";
            }
            //Schedule
            output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:5px;font-style:normal;width:900px;border-bottom: 1px solid #e0e0e0;\">\n"
                    + "\n"
                    + "\n"
                    + "                            <tbody>\n"
                    + "                                <tr style=\"margin:0;padding:0\">\n"
                    + "                                    <td colspan='6' style=\"padding:0px 10px;\">\n"
                    + "                                        <span style=\"color: #fff;background: #17a2b8;padding: 2px 8px;border-radius: 4px;\">USA CENTRAL COAST EXPRESS SERVICE (USSE)</span>\n"
                    + "                                    </td>\n"
                    + "                                </tr>\n"
                    + "                                <tr style=\"margin:0;padding:0\">\n"
                    + "                                    <td colspan='6' style=\"padding:0px 10px;\">\n"
                    + "\n"
                    + "                                        <table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:5px;font-style:normal;width:900px;border: 1px solid #b3b0b0;border-radius: 5px;margin-top:10px;\">\n"
                    + "\n"
                    + "\n"
                    + "                                            <tbody>\n"
                    + "                                                <tr style=\"margin:0;padding:0\">\n"
                    + "                                                    <td style=\"width:33.33%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                    + "                                                            <span>" + eBookingMasterData.getEbook_origin_country() + "</span><br>\n"
                    + "                                                            <span>" + DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getEbook_pol_etd()) + "</span>\n"
                    + "                                                        </p>\n"
                    + "                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                    + "                                                            <span>Departure</span>\n"
                    + "                                                        </span>\n"
                    + "                                                    </td>\n"
                    + "\n"
                    + "                                                    <td style=\"width:33.33%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                                            <span>" + eBookingMasterData.getEbook_delivery_country() + "</span><br>\n"
                    + "                                                            <span>" + DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getEbook_pol_eta()) + "</span>\n"
                    + "                                                        </p>\n"
                    + "                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Arrival</span>\n"
                    + "                                                    </td>\n"
                    + "                                                    <td style=\"width:23.33%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                                            <span>" + vessellist.get(eBookingMasterData.getEbook_vesselcode()) + "</span><br>\n"
                    + "                                                            <span>" + eBookingMasterData.getEbook_voyage() + eBookingMasterData.getEbook_bound_leg() + "</span>\n"
                    + "                                                        </p>\n"
                    + "                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Vessel / voyage / bound</span>\n"
                    + "                                                    </td>\n"
                    + "\n"
                    + "                                                    <td style=\"width:10%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                                            <span>" + DateConversion_New.daysDifferentBetweenTwoDays(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getEbook_pol_etd()), DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getEbook_pol_eta())) + " days</span>\n"
                    + "                                                        </p>\n"
                    + "                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Transit Time</span>\n"
                    + "                                                    </td>\n"
                    + "                                                </tr>\n";
            if (routinglist != null && routinglist.size() > 1) {
                output += "                                                 <tr>\n"
                        + "                                                    <td colspan=\"6\"><div style=\"width:100%;border-top: 1px dashed #9a8a8a;\"></div></td>\n"
                        + "                                                </tr>\n"
                        + "                                                <tr>\n"
                        + "                                                    <td colspan=\"6\">\n"
                        + "                                                        <table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                        + "                                                            <tbody>\n"
                        + "\n"
                        + "\n"
                        + "                                                                <tr style=\"margin:0;padding:0;text-align:center;\">\n"
                        + "                                                                    <th style=\"margin:0;padding:10px;vertical-align:middle;width:25%\">\n"
                        + "                                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Departure</span>\n"
                        + "                                                                    </th>\n"
                        + "                                                                    <th style=\"margin:0;padding:10px;vertical-align:middle;width:25%\">\n"
                        + "                                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Arrival</span>\n"
                        + "                                                                    </th>\n"
                        + "                                                                    <th  style=\"margin:0;padding:10px;vertical-align:middle;width:30%\">\n"
                        + "                                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\"> Service/Vessel </span>\n"
                        + "                                                                    </th>\n"
                        + "                                                                    <th  style=\"margin:0;padding:10px;vertical-align:middle;width:20%\">\n"
                        + "                                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\"> Voyage/Bound/Trans Type </span>\n"
                        + "                                                                    </th>\n"
                        + "\n"
                        + "                                                                </tr>\n";
                System.out.println("[ECOM-WAR] CommonMail legtype==>" + legtype);
                for (int i = 0; i < routinglist.size(); i++) {
                    eBookingrouting = (Route) routinglist.get(i);
                    output += "\n"
                            + "                                                                <tr style=\"margin:0;padding:0;text-align: center;\">\n"
                            + "                                                                    <td style=\"font-size:14px;margin:0;padding:10px;border-top:1px solid #e0e0e0;vertical-align:middle;\">\n"
                            + "                                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                                                            <span>" + portlist.get(eBookingrouting.getLoadport()) + "</span>\n"
                            + "                                                                            <br>\n"
                            + "                                                                            <span>" + DateConversion_New.convertDefaultFormatwithTime(eBookingrouting.getETD1()) + "</span>\n"
                            + "                                                                        </p>									\n"
                            + "                                                                    </td>\n"
                            + "                                                                    <td style=\"font-size:14px;margin:0;padding:10px;border-top:1px solid #e0e0e0;vertical-align:middle\">\n"
                            + "                                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                                                            <span>" + portlist.get(eBookingrouting.getDisport()) + "</span>\n"
                            + "                                                                            <br>\n"
                            + "                                                                            <span>" + DateConversion_New.convertDefaultFormatwithTime(eBookingrouting.getETA1()) + "</span>\n"
                            + "                                                                        </p>\n"
                            + "                                                                    </td>\n"
                            + "\n"
                            + "                                                                    <td style=\"font-size:14px;margin:0;padding:10px;border-top:1px solid #e0e0e0;vertical-align:middle\">\n"
                            + "                                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                                                            <span>" + checkNull((String) serlist.get(eBookingrouting.getService())) + "</span>\n"
                            + "                                                                            <br>\n"
                            + "                                                                            <span>" + checkNull((String) vessellist.get(eBookingrouting.getVessel())) + "</span>\n"
                            + "                                                                        </p>\n"
                            + "                                                                    </td>\n"
                            + "\n"
                            + "                                                                    <td style=\"font-size:14px;margin:0;padding:10px;border-top:1px solid #e0e0e0;vertical-align:middle\">\n"
                            + "                                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                                                            <span>" + checkNull(eBookingrouting.getVoyage()) + checkNull(eBookingrouting.getBound()) + "</span>\n"
                            + "                                                                            <br>\n"
                            + "                                                                            <span>" + checkNull((String) legtype.get(eBookingrouting.getLegType())) + "</span>\n"
                            + "                                                                        </p>\n"
                            + "                                                                    </td>\n"
                            + "\n"
                            + "                                                                </tr>\n";
                }
                output += "                                                            </tbody>\n"
                        + "                                                        </table>\n"
                        + "                                                    </td>\n"
                        + "                                                </tr>";
            }
            output += "                                            </tbody>\n"
                    + "                                        </table>\n"
                    + "\n"
                    + "                                    </td>\n"
                    + "                                </tr>\n";
            if (eBookingMasterData.getBkgcutoff() != null || eBookingMasterData.getDoccutoff() != null || eBookingMasterData.getVgmcutoff() != null || eBookingMasterData.getBbrorocutoff() != null || eBookingMasterData.getHazcutoff() != null || eBookingMasterData.getPortentrycutoff() != null) {
                output += "<tr>\n"
                        + "                                    <td colspan=\"6\" style=\"padding: 10px;\">\n"
                        + "                                        <table style=\"width: 100%;border: 1px solid #E3EBF3;\">\n"
                        + "                                            <tbody>\n"
                        + "                                                <tr>\n"
                        + "                                                    <td style=\"min-width: 25%;\">Booking Cutoff</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">" + checkNull(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getBkgcutoff())) + "</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">Documentation Cutoff</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">" + checkNull(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getDoccutoff())) + "</td>\n"
                        + "                                                </tr>\n"
                        + "                                                <tr>\n"
                        + "                                                    <td style=\"min-width: 25%;\">VGM Cutoff</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">" + checkNull(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getVgmcutoff())) + "</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">Port Entry Cutoff</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">" + checkNull(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getPortentrycutoff())) + "</td>\n"
                        + "                                                </tr>\n"
                        + "                                                <tr>\n"
                        + "                                                    <td style=\"min-width: 25%;\">Hazardous Cut Off</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">" + checkNull(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getHazcutoff())) + "</td>\n";
                if (eBookingMasterData.getBbrorocutoff() != null && eBookingMasterData.getBbrorocutoff().trim().length() > 0) {
                    output += "                                                    <td style=\"min-width: 25%;\">BBRORO Cutoff</td>\n"
                            + "                                                    <td style=\"min-width: 25%;\">" + checkNull(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getBbrorocutoff())) + "</td>\n";
                }
                output += "                                                </tr>\n"
                        + "                                            </tbody>\n"
                        + "                                        </table>\n"
                        + "                                    </td>\n"
                        + "                                </tr>";
            }

            output += "                            </tbody>\n"
                    + "                        </table>\n";

// customer details
            output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                    + "\n"
                    + "\n"
                    + "                            <tbody>\n"
                    + "\n"
                    + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                    + "                                    <td colspan='6'><h2>Customer Details</h2></td>\n"
                    + "                                </tr>\n"
                    + "                                <tr style=\"margin:0;padding:0;text-align:center;\">\n"
                    + "                                    <th style=\"margin:0;padding:10px;vertical-align:middle;width:25%\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Customer Type</span>\n"
                    + "                                    </th>\n"
                    + "                                    <th style=\"margin:0;padding:10px;vertical-align:middle;width:33.33%\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Customer Name</span>\n"
                    + "                                    </th>\n"
                    + "                                    <th  style=\"margin:0;padding:10px;vertical-align:middle;width:50%\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\"> Address </span>\n"
                    + "                                    </th>\n"
                    + "                                </tr>\n"
                    + "\n";

            if (eBookingMasterData.getEbook_bookingname() != null && eBookingMasterData.getEbook_bookingname().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle;\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Booking Party</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_bookingname() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_bookingaddress() + " <br>"
                        + eBookingMasterData.getEbook_bookingaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_bookingcity_desc() + "<br>"
                        + "                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_bookingcountry().trim()) + " <br>";
                        if("Y".equalsIgnoreCase(enable_zipcode) && eBookingMasterData.getEbook_bookingzipcode()!= null && eBookingMasterData.getEbook_bookingzipcode().trim().length() > 0){
                            output += "                                                      Zip Code : " + eBookingMasterData.getEbook_bookingzipcode() +" <br>";
                        };
                       output +="                                                      Telephone : " + eBookingMasterData.getEbook_bookingphone() + " <br> ";
                if (eBookingMasterData.getEbook_bookingfax() != null && eBookingMasterData.getEbook_bookingfax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_bookingfax() + " <br> ";
                }
                output += "                                                              Email : " + eBookingMasterData.getEbook_bookingemail() + " </span>\n";
                if ("Y".equalsIgnoreCase(GENS)) {
                        output += "   <br>                                                   EORI : " + checkNull(eBookingMasterData.getEbook_bookingeori())+ " <br> "
                               + "                                                      Type of Person : " +checkNull( eBookingMasterData.getBookingtypeperson()) ;
                    };
                    output += "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_agreename() != null && eBookingMasterData.getEbook_agreename().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Agreement Party</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_agreename() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_agreeaddress() + " <br>"
                        + eBookingMasterData.getEbook_agreeaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_agreecity_desc() + "<br>";

                        if("Y".equalsIgnoreCase(enable_zipcode) && eBookingMasterData.getEbook_agreezipcode()!= null && eBookingMasterData.getEbook_agreezipcode().trim().length() > 0){
                            output += "                                                      Zip Code : " + eBookingMasterData.getEbook_agreezipcode() +" <br>";
                        };
                         output +="                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_agreecountry().trim()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_agreephone() + " <br> ";
                if (eBookingMasterData.getEbook_agreefax() != null && eBookingMasterData.getEbook_agreefax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_agreefax() + " <br> ";
                }
                output += "                                                              Email : " + eBookingMasterData.getEbook_agreeemail();
                 if ("Y".equalsIgnoreCase(GENS)) {
                    output += "   <br>                                                   EORI : " + checkNull(eBookingMasterData.getEbook_agreeeori())+ " <br> "
                           + "                                                      Type of Person : " + checkNull(eBookingMasterData.getAgreetypeperson()) ;
                }
                output += "   </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_shippername() != null && eBookingMasterData.getEbook_shippername().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Shipper</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_shippername() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_shipperaddress() + " <br>"
                        + eBookingMasterData.getEbook_shipperaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_shippercity_desc() + "<br>";

                        if("Y".equalsIgnoreCase(enable_zipcode) && eBookingMasterData.getEbook_shipperzipcode()!= null && eBookingMasterData.getEbook_shipperzipcode().trim().length() > 0){
                            output += "                                                      Zip Code : " + eBookingMasterData.getEbook_shipperzipcode() +" <br>";
                        };
                        output += "                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_shippercountry()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_shipperphone() + " <br> ";
                if (eBookingMasterData.getEbook_shipperfax() != null && eBookingMasterData.getEbook_shipperfax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_shipperfax() + " <br> ";
                }
                output += "                                                              Email : " + eBookingMasterData.getEbook_shipperemail() ;
                 if ("Y".equalsIgnoreCase(GENS)) {
                    output += "    <br>                                                  EORI : " + checkNull(eBookingMasterData.getEbook_shippereori())+ " <br> "
                           + "                                                      Type of Person : " + checkNull(eBookingMasterData.getShippertypeperson()) ;
                 }
                    output += "                               </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_consigneename() != null && eBookingMasterData.getEbook_consigneename().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Consignee</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_consigneename() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_consigneeaddress() + " <br>"
                        + eBookingMasterData.getEbook_consigneeaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_consigneecity_desc() + "<br>";

                        if("Y".equalsIgnoreCase(enable_zipcode) && eBookingMasterData.getEbook_consigneezipcode()!= null && eBookingMasterData.getEbook_consigneezipcode().trim().length() > 0){
                            output += "                                                      Zip Code : " + eBookingMasterData.getEbook_consigneezipcode() +" <br>";
                        };
                        output += "                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_consigneecountry()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_consigneephone() + " <br> ";
                if (eBookingMasterData.getEbook_consigneefax() != null && eBookingMasterData.getEbook_consigneefax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_consigneefax() + " <br> ";
                }
                output += "                                                              Email : " + eBookingMasterData.getEbook_consigneeemail() ;
                 if ("Y".equalsIgnoreCase(GENS)) {
                    output += "   <br>                                                   EORI : " + checkNull(eBookingMasterData.getEbook_consigneeeori())+ " <br> "
                           + "                                                      Type of Person : " + checkNull(eBookingMasterData.getConsigneetypeperson());
                    }
                        output += " </span>\n"

                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_notifyname() != null && eBookingMasterData.getEbook_notifyname().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Notify</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_notifyname() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_notifyaddress() + " <br>"
                        + eBookingMasterData.getEbook_notifyaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_notifycity_desc() + "<br>";

                        if("Y".equalsIgnoreCase(enable_zipcode) && eBookingMasterData.getEbook_notifyzipcode()!= null && eBookingMasterData.getEbook_notifyzipcode().trim().length() > 0){
                            output += "                                                      Zip Code : " + eBookingMasterData.getEbook_notifyzipcode() +" <br>";
                        };
                        output += "                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_notifycountry()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_notifyphone() + " <br> ";
                if (eBookingMasterData.getEbook_notifyfax() != null && eBookingMasterData.getEbook_notifyfax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_notifyfax() + " <br> ";
                }
                output += "                                                              Email : " + eBookingMasterData.getEbook_notifyemail();
                 if ("Y".equalsIgnoreCase(GENS)) {
                    output += "   <br>                                                   EORI : " + checkNull(eBookingMasterData.getEbook_notifyeori())+ " <br> "
                           + "                                                      Type of Person : " + checkNull(eBookingMasterData.getNotifytypeperson());
                }
                output += "                                         </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_notifyname_n1() != null && eBookingMasterData.getEbook_notifyname_n1().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Notify 1 </p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_notifyname_n1() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_notifyaddress_n1() + " <br>"
                        + eBookingMasterData.getEbook_notifyaddress1_n1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_notifycity_desc_n1() + "<br>";

                        if("Y".equalsIgnoreCase(enable_zipcode) && eBookingMasterData.getEbook_notifyzipcode_n1()!= null && eBookingMasterData.getEbook_notifyzipcode_n1().trim().length() > 0){
                            output += "                                                      Zip Code : " + eBookingMasterData.getEbook_notifyzipcode_n1() +" <br>";
                        };
                        output += "                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_notifycountry_n1()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_notifyphone_n1() + " <br> ";
                if (eBookingMasterData.getEbook_notifyfax_n1() != null && eBookingMasterData.getEbook_notifyfax_n1().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_notifyfax_n1() + " <br> ";
                }
                output += "                                                              Email : " + eBookingMasterData.getEbook_notifyemail_n1();
                         if ("Y".equalsIgnoreCase(GENS)) {
                    output += "    <br>                                                  EORI : " + checkNull(eBookingMasterData.getEbook_notifyeori_n1())+ " <br> "
                           + "                                                      Type of Person : " +checkNull( eBookingMasterData.getNotifytypeperson_n1());
                }
                output += " </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_forwardername() != null && eBookingMasterData.getEbook_forwardername().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Forwarder</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_forwardername() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_forwarderaddress() + " <br>"
                        + eBookingMasterData.getEbook_forwarderaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_forwardercity_desc() + "<br>";
                        if("Y".equalsIgnoreCase(enable_zipcode) && eBookingMasterData.getEbook_forwarderzipcode()!= null && eBookingMasterData.getEbook_forwarderzipcode().trim().length() > 0){
                            output += "                                                      Zip Code : " + eBookingMasterData.getEbook_forwarderzipcode() +" <br>";
                        };
                        output +="                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_forwardercountry()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_forwarderphone() + " <br> ";
                if (eBookingMasterData.getEbook_forwarderfax() != null && eBookingMasterData.getEbook_forwarderfax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_forwarderfax() + " <br> ";
                }
                output += "                                                              Email : " + eBookingMasterData.getEbook_forwarderemail() ;
                 if ("Y".equalsIgnoreCase(GENS)) {
                    output += "  <br>                                                    EORI : " + checkNull(eBookingMasterData.getEbook_forwardereori())+ " <br> "
                           + "                                                      Type of Person : " + checkNull(eBookingMasterData.getForwardertypeperson());
                }
                output += "   </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_Warehousename() != null && eBookingMasterData.getEbook_Warehousename().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Warehouse</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_Warehousename() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_Warehouseaddress() + " <br>"
                        + eBookingMasterData.getEbook_Warehouseaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_Warehousecity_desc() + "<br>";
                        if("Y".equalsIgnoreCase(enable_zipcode) && eBookingMasterData.getEbook_Warehousezipcode()!= null && eBookingMasterData.getEbook_Warehousezipcode().trim().length() > 0){
                            output += "                                                      Zip Code : " + eBookingMasterData.getEbook_Warehousezipcode() +" <br>";
                        };
                        output += "                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_Warehousecountry()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_Warehousephone() + " <br> ";
                if (eBookingMasterData.getEbook_Warehousefax() != null && eBookingMasterData.getEbook_Warehousefax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_Warehousefax() + " <br> ";
                }
                output += "                                                              Email : " + eBookingMasterData.getEbook_Warehouseemail() ;
                   if ("Y".equalsIgnoreCase(GENS)) {
                    output += " <br>                                                     EORI : " + checkNull(eBookingMasterData.getEbook_Warehouseeori())+ " <br> "
                           + "                                                      Type of Person : " + checkNull(eBookingMasterData.getWarehousetypeperson());
                  }
                   output += "   </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            output += "                            </tbody>\n"
                    + "                        </table>\n";

//Cargo Details
            output += "                        <table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;\">\n"
                    + "                            <tbody>\n"
                    + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                    + "                                    <td colspan='6'><h2>Cargo Details</h2></td>\n"
                    + "                                </tr>\n"
                    + "\n"
                    + "                                <tr style=\"margin:0;padding:0;text-align:center;\">\n"
                    + "                                    <th colspan=\"1\" style=\"margin:0;padding:10px;vertical-align:middle;\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">S.NO.</span>\n"
                    + "                                    </th>\n"
                    + "                                    <th colspan=\"4\" style=\"margin:0;padding:10px;vertical-align:middle;width:50%;\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Equiment Description</span>\n"
                    + "                                    </th>\n"
                    + "                                </tr>\n";
            EbookingValidatorFormBeanNew eBookingcargoData = null;
            for (int i = 0; i < cargolist.size(); i++) {
                eBookingcargoData = (EbookingValidatorFormBeanNew) cargolist.get(i);
                output += "<tr style=\"margin:0;padding:0;line-height: 10px;\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\"> " + (i + 1) + " </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + unitlis.get(eBookingcargoData.getEbook_eqp_type()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Equipment</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingcargoData.getEbook_eqp_qty() + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp.Qty.</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eqpmap.get(eBookingcargoData.getEbook_eqp()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp. Size</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + ("M".equalsIgnoreCase(checkNull(eBookingcargoData.getEqpstatus())) ? "Empty" : "Laden") + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp.Status</span>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                                <tr style=\"margin:0;padding:0;line-height: 10px;\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 35px;\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + ("Y".equalsIgnoreCase(eBookingcargoData.getSoc()) ? "Yes" : "No") + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">SOC</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width: 25%;font-size:14px;margin:0;padding: 9px;vertical-align:middle;\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + ("Y".equalsIgnoreCase(eBookingcargoData.getHzd()) ? "Yes" : "No") + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Hazardous</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull(eBookingcargoData.getEbook_cargoweight()) + " " + unitlis.get(checkNull(eBookingcargoData.getEbook_weightunit())) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Cargo Weight(per Container)</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull(eBookingcargoData.getEbook_tareweight()) + " " + unitlis.get(checkNull(eBookingcargoData.getEbook_weightunit())) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Tare Weight</span>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                                <tr style=\"margin:0;padding:0;line-height: 10px;\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 35px;\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull(eBookingcargoData.getEbook_grossweight()) + " " + unitlis.get(checkNull(eBookingcargoData.getEbook_weightunit())) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Gross Weight</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width: 25%;font-size:14px;margin:0;padding: 9px;vertical-align:middle;\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull((String) packagemap.get(eBookingcargoData.getEbook_packagecode())) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">" + "Package" + "</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingcargoData.getEbook_packquantity() + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">" + "No.of Packages" + "</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingcargoData.getEbook_cargovolume() + " " + (("F".equalsIgnoreCase(eBookingcargoData.getEbook_volumeunit())) ? "Cubic Feet" : (("M".equalsIgnoreCase(eBookingcargoData.getEbook_volumeunit())) ? "Cubic Meter" : "")) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Volume</span>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n";
                if ("Y".equalsIgnoreCase(EnableActualCommoName)) {
                    output += "                                <tr style=\"margin:0;padding:0;line-height: 10px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "                                    <td colspan='6' style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 35px;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_actCommoName()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Actual Commodity Name</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n";
                }
                output += "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                        + "\n"
                        + "                                    <td colspan='6' style=\"font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                        + "\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull((String) comlist.get(checkNull(eBookingcargoData.getEbook_commoditycode()))) + " ~~ " + checkNull(eBookingcargoData.getEbook_commoditycode()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Commodity\n"
                        + "                                        </span>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\"> </td>\n"
                        + "\n"
                        + "                                    <td colspan='6' style=\"font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                        + "\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull((String) comlist.get(eBookingcargoData.getHscode())) + " ~~ " + checkNull(eBookingcargoData.getHscode()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">" + hsCodelbl + "\n"
                        + "                                        </span>\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "\n"
                        + "                                </tr>\n"
                        + "\n"
                        + "                                <tr> <td style=\"font-size:14px;margin:0;padding:10px;\"> </td>\n"
                        + "<td colspan=\"6\" style=\"margin:0;padding: 9px;\"><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td></tr>\n";
                if ("Y".equalsIgnoreCase(eBookingcargoData.getHzd())) {
                    output += "<tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + ("Y".equalsIgnoreCase(eBookingcargoData.getEbook_poluttunit()) ? "Yes" : ("N".equalsIgnoreCase(eBookingcargoData.getEbook_poluttunit()) ? "No" : "")) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Marine Pollutant</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + eBookingcargoData.getEbook_unno() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">UN No.</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_flashpoint()) + " " + unitlis.get(checkNull(eBookingcargoData.getEbook_flashunit())) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Flash Point</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_dgclass()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">DG Class</span>\n"
                            + "                                    </td>\n"
                            + "\n"
                            + "\n"
                            + "\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + dgshipper.get(checkNull(checkNull(eBookingcargoData.getEbook_unno()).split("~~")[0])) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Shipping Name</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr> <td style=\"font-size:14px;margin:0;padding:10px;\"> </td>\n"
                            + "<td colspan=\"6\" style=\"margin:0;padding: 9px;\"><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td></tr>";
                }
                if ("O".equalsIgnoreCase(eBookingcargoData.getEbook_eqp_type())) {
                    output += "<tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + ((("M".equalsIgnoreCase(eBookingcargoData.getEbook_dimensionunit())) ? "Meters" : (("F".equalsIgnoreCase(eBookingcargoData.getEbook_dimensionunit())) ? "Feet" : (("C".equalsIgnoreCase(eBookingcargoData.getEbook_dimensionunit()) ? "Centimeters" : ""))))) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Unit</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_owleft()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Left</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_owright()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Right</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_olforward()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Forward</span>\n"
                            + "                                    </td>\n"
                            + "\n"
                            + "\n"
                            + "\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_olaft()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Aft</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_oh()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Top</span>\n"
                            + "                                    </td>\n"
                            + "\n"
                            + "                                </tr>\n"
                            + "                                <tr> <td style=\"font-size:14px;margin:0;padding:10px;\"> </td>\n"
                            + "<td colspan=\"6\" style=\"margin:0;padding: 9px;\"><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td></tr>\n";
                }
                if ("R".equalsIgnoreCase(eBookingcargoData.getEbook_eqp_type())) {
                    output += "<tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + (("Y".equalsIgnoreCase(checkNull(eBookingcargoData.getEbook_nor()))) ? "Yes" : "No") + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Non-Operating Reefer\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_presetemp()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Set Temp\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n";
                    if (!"Y".equalsIgnoreCase(hideMinMaxTemp)) {
                        output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(eBookingcargoData.getEbook_mintemp()) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Min Temp\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n"
                                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(eBookingcargoData.getEbook_maxtemp()) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Max Temp\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n"
                                + "                                </tr>\n";
                    }
                    output += "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull((String) unitlis.get(checkNull(eBookingcargoData.getEbook_temperunit()))) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Temp Unit</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getVentilation()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">" + Ventilationlabel + "\n"
                            + "\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n";
                    if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {
                        output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + eBookingcargoData.getHumidity() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Humidity(%)\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n"
                                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + ("O".equalsIgnoreCase(eBookingcargoData.getDrains()) ? "Open" : "C".equalsIgnoreCase(eBookingcargoData.getDrains()) ? "Closed" : "") + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Drains\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    }
                    output += "                                </tr>\n";
                    if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {
                        output += "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                                + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 35px\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(eBookingcargoData.getCo2()) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">CO2\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n"
                                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(eBookingcargoData.getO2()) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">O2\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n"
                                + "                                    \n"
                                + "                                </tr>\n";
                    }
                    output += "                                <tr> <td style=\"font-size:14px;margin:0;padding:10px;\"> </td>\n"
                            + "<td colspan=\"6\" style=\"margin:0;padding: 9px;padding-left: 35px;\"><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td></tr> \n";
                }
                output += "                                <tr> <td colspan='6'><hr></td></tr> \n";

            }
            output += "</tbody>\n"
                    + "                        </table>\n";

            //Haulage Details
            List<BookingHaulagedetailsVo> haulagelist = eBookingMasterData.getHaulagelist();
            if (haulagelist != null && haulagelist.size() > 0) {
                output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                        + "\n"
                        + "\n"
                        + "                            <tbody>\n"
                        + "\n"
                        + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                        + "                                    <td colspan='6'><h2>Haulage Details</h2></td>\n"
                        + "                                </tr>\n";
                for (int i = 0; i < haulagelist.size(); i++) {
                    BookingHaulagedetailsVo haulvo = haulagelist.get(i);
                    output += "<tr style=\"margin:0;padding:0;line-height:12px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getEqprowid() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp.Seq No</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getEqptype() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp.Type</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getPicklocname() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">PickUp Location</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getAddress() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getRefno() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Ref.No</span>\n"
                            + "                                    </td>\n"
                            + "                                   <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getStopsequence() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Stop Sequence</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + DateConversion_New.convertDefaultFormatwithTime(haulvo.getDropdate()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Drop Date*</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getContactperson() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Contact Person</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getContactphone() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Contact Phone</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getContactemail() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Contact Email</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + ("Y".equalsIgnoreCase(haulvo.getTwrsoverweight()) ? "Yes" : "No") + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">OverWeight</span>\n"
                            + "                                    </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getRemarks() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Remarks</span>\n"
                            + "                                    </td>\n"
                            + "                                    \n"
                            + "                                </tr>\n"
                            + "                                <tr> <td colspan='6'><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td></tr>";
                }
                output += "                      <tr> <td colspan='6'><hr></td></tr>"
                        + "      </tbody>\n"
                        + "                        </table>\n";
            }
           //ENS 
           ENSDeclarantVo edvo = eBookingMasterData.getEnsdecvo();
           if ("Y".equalsIgnoreCase(GENS) && "Y".equalsIgnoreCase(iseuport) && edvo != null && checkNull(edvo.getEnsFillingType()).length() > 0) {
               output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:#fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                       + "                            <tbody>\n"
                       + "                                <tr style=\"margin:0;padding:0;font-weight:bold;text-decoration:underline;text-align:center\">\n"
                       + "                                    <td colspan=\"6\">\n"
                       + "                                        <h2>ENS Customer Declaration</h2>\n"
                       + "                                    </td>\n"
                       + "                                </tr>\n"
                       + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                       + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                       + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                       + "                                            <span>" + ("Y".equalsIgnoreCase(checkNull(edvo.getEuCustZone())) ? "Yes" : "No") + "</span>\n"
                       + "                                        </p>\n"
                       + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                       + "                                            <span>Is cargo to be delivered to EU Customs Zone</span>\n"
                       + "                                        </span>\n"
                       + "                                    </td>\n"
                       + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                       + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                       + "                                            <span>" + checkNull(edvo.getBlTypeENS()) + "</span>\n"
                       + "                                        </p>\n"
                       + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Type of Bill of Lading</span>\n"
                       + "                                    </td>\n"
                       + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                       + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                       + "                                             <span>" + checkNull(edvo.getEnsFillingType()) + "</span>\n"
                       + "                                        </p>\n"
                       + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Type of ENS Filing</span>\n"
                       + "                                    </td>\n"
                       + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                       + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                       + "                                           <span>" + checkNull(edvo.getPaymentMethod()) + "</span>\n"
                       + "                                        </p>\n"
                       + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Method of Payment</span>\n"
                       + "                                    </td>\n"
                       + "                                </tr>"
                       + "                                <tr>\n"
                       + "                                    <td colspan=\"6\"><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td>\n"
                       + "                                </tr>";

               if ("Multiple Filing".equalsIgnoreCase(checkNull(edvo.getEnsFillingType()))) {
                   output += "<tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:left\">\n"
                           + "                                    <td colspan='6'><h4>Multiple Filing - Supplementary Declarant details</h4></td>\n"
                           + "                                </tr>\n"
                           + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                           + "                                            <span>" + checkNull(edvo.getDeclarantName()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                           + "                                            <span>Name</span>\n"
                           + "                                        </span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(edvo.getDeclarantAddress()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(edvo.getDeclarantAddress1()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address1</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(edvo.getDeclarantCity()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">City</span>\n"
                           + "                                    </td>\n"
                           + "                                </tr>\n"
                           + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                           + "                                            <span>" + checkNull(edvo.getDeclarantCountry()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                           + "                                            <span>Country</span>\n"
                           + "                                        </span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(edvo.getDeclarantState()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">State</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(edvo.getDeclarantTelephone()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Telephone</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(edvo.getDeclarantEmail()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Email</span>\n"
                           + "                                    </td>\n"
                           + "                                </tr>\n"
                           + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                           + "                                            <span>" + checkNull(edvo.getDeclarantZipcode()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                           + "                                            <span>Zipcode</span>\n"
                           + "                                        </span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                           + "                                            <span>" + checkNull(edvo.getDeclarantEORI()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                           + "                                            <span>EORI</span>\n"
                           + "                                        </span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(edvo.getDeclarantFillingType()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Filing Type</span>\n"
                           + "                                    </td>\n"
                           + "                                </tr>";
               }
               if ("Single Filing".equalsIgnoreCase(checkNull(edvo.getEnsFillingType()))) {
                   //Buyer
                   output += "<tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:left\">\n"
                           + "                                    <td colspan='6'><h4>Buyer</h4></td>\n"
                           + "                                </tr>\n"
                           + "								<tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyername()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                           + "                                            <span>Name</span>\n"
                           + "                                        </span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyeraddress()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyeraddress1()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address1</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyercity()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">City</span>\n"
                           + "                                    </td>\n"
                           + "                                </tr>\n"
                           + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyercountry()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                           + "                                            <span>Country</span>\n"
                           + "                                        </span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyerphone()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Telephone</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyeremail()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Email</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyerstate()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">State</span>\n"
                           + "                                    </td>\n"
                           + "                                </tr>\n"
                           + "				        <tr style=\"margin:0;padding:0;line-height:15px;\">\n";
                   if (checkNull(eBookingMasterData.getEbook_Buyerfax()).length() > 0) {
                       output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                               + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                               + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyerfax()) + "</span>\n"
                               + "                                        </p>\n"
                               + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                               + "                                            <span>Fax</span>\n"
                               + "                                        </span>\n"
                               + "                                    </td>\n";
                   }
                   output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyerzipcode()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Zipcode</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyereori()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">EORI</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyerpersontype()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Type of Person</span>\n"
                           + "                                    </td>\n"
                           + "                                </tr>\n";
                   //Seller
                   output += "<tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:left\">\n"
                           + "                                    <td colspan='6'><h4>Seller</h4></td>\n"
                           + "                                </tr>\n"
                           + "								<tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellername()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                           + "                                            <span>Name</span>\n"
                           + "                                        </span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Selleraddress()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Selleraddress1()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address1</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellercity()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">City</span>\n"
                           + "                                    </td>\n"
                           + "                                </tr>\n"
                           + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellercountry()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                           + "                                            <span>Country</span>\n"
                           + "                                        </span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellerphone()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Telephone</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Selleremail()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Email</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellerstate()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">State</span>\n"
                           + "                                    </td>\n"
                           + "                                </tr>\n"
                           + "				      <tr style=\"margin:0;padding:0;line-height:15px;\">\n";
                   if (checkNull(eBookingMasterData.getEbook_Sellerfax()).length() > 0) {
                       output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                               + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                               + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellerfax()) + "</span>\n"
                               + "                                        </p>\n"
                               + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                               + "                                            <span>Fax</span>\n"
                               + "                                        </span>\n"
                               + "                                    </td>\n";
                   }
                   output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellerzipcode()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Zipcode</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellereori()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">EORI</span>\n"
                           + "                                    </td>\n"
                           + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                           + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                           + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellerpersontype()) + "</span>\n"
                           + "                                        </p>\n"
                           + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Type of Person</span>\n"
                           + "                                    </td>\n"
                           + "                                </tr>\n";

               }
               output += "<tr style=\"margin:0;padding:0;font-weight: bold;text-align:center\">\n"
                       + "                                    <td colspan='8'>\n"
                       + "                                        <hr>\n"
                       + "                                    </td>\n"
                       + "                                </tr>\n"
                       + "                            </tbody>\n"
                       + "                        </table>";
           }

            // Additional Remarks
            if (eBookingMasterData.getSalesRateAgree() != null || eBookingMasterData.getEbook_status_remarks() != null) {
                output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                        + "                            <tbody>\n"
                        + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                        + "                                    <td colspan='6'><h2>Additional Remarks</h2></td>\n"
                        + "                                </tr>\n"
                        + "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle;width:20%\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Comments on Rates : \n"
                        + "                                        </p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle;width:80%\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getSalesRateAgree() + "</span>\n"
                        + "                                        </p>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;vertical-align:middle;width:20%\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Additional Information : \n"
                        + "                                        </p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;vertical-align:middle;width:80%\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_status_remarks() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                            </tbody>\n"
                        + "                        </table>\n";
            }
            output += "</td>\n"
                    + "                </tr>\n"
                    + "            </tbody>\n"
                    + "        </table>		\n"
                    + "    </body>\n"
                    + "</html>\n";

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return output;
    }

    public String amendmentBookingMail(AmendmentBookingFormBean eBookingMasterData, String imagepath, String logo_name, String book_no, HashMap masterCache, List routinglist, List cargolist) {

        String output = "";
        Route eBookingrouting = null;
        try {
            HashMap carriageContract = (HashMap) masterCache.get("carriagecontact");
            List addinfo = (ArrayList) masterCache.get("addinfo");
            Map<String, String> intransitMap = (Map<String, String>) masterCache.get("PODINTRANSITMAPCONTEXT");
            Map<String, String> emptyPickUpMap = (Map<String, String>) masterCache.get("EMPTYPICKUPMAP");
            Map vessellist = (Map) masterCache.get("VESSELLIST");
            Map contrylist = (Map) masterCache.get("CONTRYLIST");
            Map serlist = (Map) masterCache.get("SERVICELIST");
            Map packagemap = (Map) masterCache.get("PACKAGELIST");
            Map eqpmap = (Map) masterCache.get("EQUIPMENT_TYPE");
            Map portlist = (Map) masterCache.get("PORTLIST");
            String carriercode = (String) masterCache.get("carriercode");
            Map comlist = (Map) masterCache.get("comlist");
            Map dgshipper = (Map) masterCache.get("SESSION_DGCLASS");
            String hideMinMaxTemp = (String) masterCache.get("hideMinMaxTemp");
            String userFruitCust = (String) masterCache.get("userFruitCust");
            String EnableActualCommoName = (String) masterCache.get("EnableActualCommoName");
            String Ebkg_type = (String) masterCache.get("Ebkg_type");
            String Enable_Drains_Humidity = (String) masterCache.get("Enable_Drains_Humidity");
            String GENS = (String) masterCache.get("Enable_Eori_Details");
            String enable_zipcode = (String) masterCache.get("enable_zipcode");
            String hsnamecode = (String) masterCache.get(GeneralConf.Display_HS_NAME_CODE);
            String hsCodelbl = (("Y".equalsIgnoreCase(checkNull(hsnamecode))) ? "HS Name and Code" : "HS Code");
            String iseuport = (String) masterCache.get("IsEnableENSTabInBooking");
            String Ventilationlabel = (String) masterCache.get("Ventilation");
            String hideApCustBkgMaster = (String) masterCache.get(GeneralConf.Hide_AP_CUST_BKG_Master);

            // LOGO and Book No
            output = "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <meta charset=\"UTF-8\">\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "        <table>\n"
                    + "            <tbody>\n"
                    + "                <tr>\n"
                    + "                    <td>\n"
                    + "                        <table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                    + "                            <tbody>\n"
                    + "                                <tr><td></td></tr>\n"
                    + "                                <tr>\n"
                    + "                                    <td style=\"margin:0 20px 0 0;padding:0 15px 0 0;width:1%\">\n"
                    + "                                        <div style=\"display:inline-block;border-right:1px solid #ccc;margin:0 0 8px 0\">\n"
                    + "                                            <img style=\"padding:10px;max-height: 45px;\" src='" + imagepath + logo_name +"?" + jsdate + "' alt=\"Logo\">\n"
                    + "                                        </div>\n"
                    + "                                    </td>\n"
                    + "\n"
                    + "                                    <td style=\"font-size:30px;margin:0;padding:3px;width:30%\"> " + ("R".equalsIgnoreCase(Ebkg_type) ? "Booking Request : " : "Amendment Booking Request : ") + " <span style=\"color:#62be82; font-weight: bold;\"> (" + book_no + ") </span></td>\n"
                    + "\n"
                    + "\n"
                    + "                                </tr>\n"
                    + "                            </tbody>\n"
                    + "                        </table>\n";

            // origin delivery header
            output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;border-bottom: 1px solid #FFC107 !important;\">\n"
                    + "                            <tbody>\n"
                    + "                                <tr>\n"
                    + "                                    <td></td>\n"
                    + "                                </tr>\n"
                    + "                                <tr style=\"height:60px;overflow:hidden;margin-top:20px;padding:0 0 5px;\">\n"
                    + "                                    <td colspan=\"2\" style=\"max-width:33.33%;font-size:16px\">\n"
                    + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0\">\n"
                    + "                                            <span>" + eBookingMasterData.getEbook_origin_country() + "</span>\n"
                    + "                                            <br>\n"
                    + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Origin Port</span>\n"
                    + "                                        </span>\n"
                    + "\n"
                    + "                                    </td>\n"
                    + "                                    <td colspan=\"2\" style=\"max-width:33.33%;text-align:center;font-size:16px\">\n"
                    + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0;margin-right:19px\">\n"
                    + "                                            <span>" + DateConversion_New.convertDefaultFormat(eBookingMasterData.getEbook_date()) + "</span>\n"
                    + "                                            <br>\n"
                    + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Cargo Ready Date</span>\n"
                    + "                                        </span>\n"
                    + "\n"
                    + "                                    </td>\n"
                    + "                                    <td colspan=\"2\" style=\"max-width:33.33%;text-align:right;font-size:16px;\">\n"
                    + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0;margin-right:19px\">\n"
                    + "                                            <span>" + eBookingMasterData.getEbook_delivery_country() + "</span>\n"
                    + "                                            <br>\n"
                    + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Delivery Port</span>\n"
                    + "                                        </span>\n"
                    + "\n"
                    + "                                    </td>\n"
                    + "                                </tr>\n"
                    + "\n"
                    + "                                <tr>\n"
                    + "                                    <td ></td>\n"
                    + "                                </tr>\n"
                    + "                            </tbody>\n"
                    + "                        </table>\n";

            // Master data
            output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;border-bottom: 1px solid #e0e0e0;\">\n"
                    + "                            <tbody>\n"
                    + "                                <tr><td></td></tr>\n"
                    + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                    + "                                    <td colspan='6' ><h2>Master Details</h2></td>\n"
                    + "                                </tr>\n"
                    + "\n"
                    + "\n"
                    + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                    + "                                            <span>" + book_no + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                    + "                                            <span>Online Booking No</span>\n"
                    + "                                        </span>\n"
                    + "                                    </td>\n"
                    + "\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span>" + checkNull(eBookingMasterData.getLrpBookNo()) + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">LRP Booking No</span>\n"
                    + "                                    </td>\n"
                    + ("Y".equalsIgnoreCase(hideApCustBkgMaster) ? ""
                    : "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span>" + eBookingMasterData.getEbook_agreename() + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Agreement Party</span>\n"
                    + "                                    </td>\n")
                    + "\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span>" + (carriageContract != null ? carriageContract.get(eBookingMasterData.getCarrierContact()) : "") + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Carriage Contract</span>\n"
                    + "                                    </td>\n"
                    + "                                </tr>\n"
                    + "\n"
                    + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                    + "                                            <span>" + ("C".equalsIgnoreCase(eBookingMasterData.getPreCarriage()) ? "Carrier" : ("M".equalsIgnoreCase(eBookingMasterData.getPreCarriage()) ? "Merchant" : "N/A")) + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                    + "                                            <span>Haulage Origin</span>\n"
                    + "                                        </span>\n"
                    + "                                    </td>\n"
                    + "\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span>" + ("C".equalsIgnoreCase(eBookingMasterData.getPreCarriage()) ? "Carrier" : ("M".equalsIgnoreCase(eBookingMasterData.getPreCarriage()) ? "Merchant" : "N/A")) + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Haulage Destination</span>\n"
                    + "                                    </td>\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span></span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\"></span>\n"
                    + "                                    </td>\n"
                    + "\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span></span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\"></span>\n"
                    + "                                    </td>\n"
                    + "                                </tr>\n";

            // Additional information
            if (addinfo != null && addinfo.size() > 0) {
                output += "<tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"padding:10px;\">\n"
                        + "                                        <span style=\"font-size:12px;margin:0;padding:0;font-weight:bold;text-decoration:underline;\">\n"
                        + "                                            Additional Information\n"
                        + "                                        </span>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n";
                output += "<tr style=\"margin:0;padding:0;line-height: 10px;\">\n";
                String addInfoID;
                for (int i = 0; i < addinfo.size(); i++) {
                    addInfoID = (String) addinfo.get(i);
                    if ("EOR".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_rate_reference() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                                + "                                            <span>Rate Reference</span>\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    } else if ("AGR".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_agency_reference() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Agency Ref No.</span>\n"
                                + "                                    </td>\n";
                    } else if ("EOF".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + (("P".equalsIgnoreCase(eBookingMasterData.getEbook_ocean_freight())) ? "Prepaid" : (("C".equalsIgnoreCase(eBookingMasterData.getEbook_ocean_freight()) ? "Collect" : ""))) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Ocean Freight</span>\n"
                                + "                                    </td>\n";
                    } else if ("PFR".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull((String) intransitMap.get(checkNull(eBookingMasterData.getPodintransit()))) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Place of Final Receipt (Intransit)</span>\n"
                                + "                                    </td>\n";
                    } else if ("NAT".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_natcode() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                                + "                                            <span>NAT Code</span>\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    } else if ("ETH".equalsIgnoreCase(addInfoID)) { 
                        System.out.println("[ECOM-WAR] CommonMail eBookingMasterData.getEbook_haulagetype() ==> " + eBookingMasterData.getEbook_haulagetype() +" haulagetype==> " + haulagetype);
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull((String) haulagetype.get(checkNull(eBookingMasterData.getEbook_haulagetype()))) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Haulage Type</span>\n"
                                + "                                    </td>\n";
                    } else if ("ELD".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(DateConversion_New.convertDefaultFormat(eBookingMasterData.getEbook_pickup_date())) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Pickup Date\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    } else if ("EDD".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(DateConversion_New.convertDefaultFormat(eBookingMasterData.getEbook_drop_date())) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Drop Date</span>\n"
                                + "                                    </td>\n";
                    } else if ("HAU".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_haulercode() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                                + "                                            <span>Haulier Code</span>\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    } else if ("EPO".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_custpo() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Customer PO</span>\n"
                                + "                                    </td>\n";
                    } else if ("ERT".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_reftype() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Release Type</span>\n"
                                + "                                    </td>";
                    } else if ("ERF".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + eBookingMasterData.getEbook_exportref() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Export Reference</span>\n"
                                + "                                    </td>\n";
                    } else if ("EOP".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                                + "                                            <span>" + (("T".equalsIgnoreCase(eBookingMasterData.getEbook_pickup_point())) ? "Terminal" : (("D".equalsIgnoreCase(eBookingMasterData.getEbook_pickup_point()) ? "Depot" : ""))) + " / " + checkNull(emptyPickUpMap.get(checkNull(eBookingMasterData.getEmptyPickup()))) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                                + "                                            <span>Empty Place of Pick Up\n"
                                + "                                            </span>\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    } else if ("EPD".equalsIgnoreCase(addInfoID)) {
                        output += "<td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(DateConversion_New.convertDefaultFormatTime(eBookingMasterData.getEmptyPickUpDate())) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Empty Place of Pick Up Date</span>\n"
                                + "                                    </td>\n";
                    }
                    if ((i + 1) % 4 == 0 && i != addinfo.size() - 1) {
                        output += "</tr>\n"
                                + "                                <tr style=\"margin:0;padding:0;line-height: 10px;\">\n";
                    }
                }
                output += "                                </tr>\n"
                        + "                            </tbody>\n"
                        + "                        </table>\n";
            }
            //Schedule
            output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:5px;font-style:normal;width:900px;border-bottom: 1px solid #e0e0e0;\">\n"
                    + "\n"
                    + "\n"
                    + "                            <tbody>\n"
                    + "                                <tr style=\"margin:0;padding:0\">\n"
                    + "                                    <td colspan='6' style=\"padding:0px 10px;\">\n"
                    + "                                        <span style=\"color: #fff;background: #17a2b8;padding: 2px 8px;border-radius: 4px;\">USA CENTRAL COAST EXPRESS SERVICE (USSE)</span>\n"
                    + "                                    </td>\n"
                    + "                                </tr>\n"
                    + "                                <tr style=\"margin:0;padding:0\">\n"
                    + "                                    <td colspan='6' style=\"padding:0px 10px;\">\n"
                    + "\n"
                    + "                                        <table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:5px;font-style:normal;width:900px;border: 1px solid #b3b0b0;border-radius: 5px;margin-top:10px;\">\n"
                    + "\n"
                    + "\n"
                    + "                                            <tbody>\n"
                    + "                                                <tr style=\"margin:0;padding:0\">\n"
                    + "                                                    <td style=\"width:33.33%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                    + "                                                            <span>" + eBookingMasterData.getEbook_origin_country() + "</span><br>\n"
                    + "                                                            <span>" + DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getEbook_pol_etd()) + "</span>\n"
                    + "                                                        </p>\n"
                    + "                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                    + "                                                            <span>Departure</span>\n"
                    + "                                                        </span>\n"
                    + "                                                    </td>\n"
                    + "\n"
                    + "                                                    <td style=\"width:33.33%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                                            <span>" + eBookingMasterData.getEbook_delivery_country() + "</span><br>\n"
                    + "                                                            <span>" + DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getEbook_pol_eta()) + "</span>\n"
                    + "                                                        </p>\n"
                    + "                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Arrival</span>\n"
                    + "                                                    </td>\n"
                    + "                                                    <td style=\"width:23.33%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                                            <span>" + vessellist.get(eBookingMasterData.getEbook_vesselcode()) + "</span><br>\n"
                    + "                                                            <span>" + eBookingMasterData.getEbook_voyage() + eBookingMasterData.getEbook_bound_leg() + "</span>\n"
                    + "                                                        </p>\n"
                    + "                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Vessel / voyage / bound</span>\n"
                    + "                                                    </td>\n"
                    + "\n"
                    + "                                                    <td style=\"width:10%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                                            <span>" + DateConversion_New.daysDifferentBetweenTwoDays(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getEbook_pol_etd()), DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getEbook_pol_eta())) + " days</span>\n"
                    + "                                                        </p>\n"
                    + "                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Transit Time</span>\n"
                    + "                                                    </td>\n"
                    + "                                                </tr>\n";
            if (routinglist != null && routinglist.size() > 1) {
                output += "                                                 <tr>\n"
                        + "                                                    <td colspan=\"6\"><div style=\"width:100%;border-top: 1px dashed #9a8a8a;\"></div></td>\n"
                        + "                                                </tr>\n"
                        + "                                                <tr>\n"
                        + "                                                    <td colspan=\"6\">\n"
                        + "                                                        <table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                        + "                                                            <tbody>\n"
                        + "\n"
                        + "\n"
                        + "                                                                <tr style=\"margin:0;padding:0;text-align:center;\">\n"
                        + "                                                                    <th style=\"margin:0;padding:10px;vertical-align:middle;width:25%\">\n"
                        + "                                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Departure</span>\n"
                        + "                                                                    </th>\n"
                        + "                                                                    <th style=\"margin:0;padding:10px;vertical-align:middle;width:25%\">\n"
                        + "                                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Arrival</span>\n"
                        + "                                                                    </th>\n"
                        + "                                                                    <th  style=\"margin:0;padding:10px;vertical-align:middle;width:30%\">\n"
                        + "                                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\"> Service/Vessel </span>\n"
                        + "                                                                    </th>\n"
                        + "                                                                    <th  style=\"margin:0;padding:10px;vertical-align:middle;width:20%\">\n"
                        + "                                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\"> Voyage/Bound/Trans Type </span>\n"
                        + "                                                                    </th>\n"
                        + "\n"
                        + "                                                                </tr>\n";
                System.out.println("[ECOM-WAR] CommonMail legtype==>" + legtype);
                for (int i = 0; i < routinglist.size(); i++) {
                    eBookingrouting = (Route) routinglist.get(i);
                    output += "\n"
                            + "                                                                <tr style=\"margin:0;padding:0;text-align: center;\">\n"
                            + "                                                                    <td style=\"font-size:14px;margin:0;padding:10px;border-top:1px solid #e0e0e0;vertical-align:middle;\">\n"
                            + "                                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                                                            <span>" + portlist.get(eBookingrouting.getLoadport()) + "</span>\n"
                            + "                                                                            <br>\n"
                            + "                                                                            <span>" + DateConversion_New.convertDefaultFormatwithTime(eBookingrouting.getETD1()) + "</span>\n"
                            + "                                                                        </p>									\n"
                            + "                                                                    </td>\n"
                            + "                                                                    <td style=\"font-size:14px;margin:0;padding:10px;border-top:1px solid #e0e0e0;vertical-align:middle\">\n"
                            + "                                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                                                            <span>" + portlist.get(eBookingrouting.getDisport()) + "</span>\n"
                            + "                                                                            <br>\n"
                            + "                                                                            <span>" + DateConversion_New.convertDefaultFormatwithTime(eBookingrouting.getETA1()) + "</span>\n"
                            + "                                                                        </p>\n"
                            + "                                                                    </td>\n"
                            + "\n"
                            + "                                                                    <td style=\"font-size:14px;margin:0;padding:10px;border-top:1px solid #e0e0e0;vertical-align:middle\">\n"
                            + "                                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                                                            <span>" + checkNull((String) serlist.get(eBookingrouting.getService())) + "</span>\n"
                            + "                                                                            <br>\n"
                            + "                                                                            <span>" + checkNull((String) vessellist.get(eBookingrouting.getVessel())) + "</span>\n"
                            + "                                                                        </p>\n"
                            + "                                                                    </td>\n"
                            + "\n"
                            + "                                                                    <td style=\"font-size:14px;margin:0;padding:10px;border-top:1px solid #e0e0e0;vertical-align:middle\">\n"
                            + "                                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                                                            <span>" + checkNull(eBookingrouting.getVoyage()) + checkNull(eBookingrouting.getBound()) + "</span>\n"
                            + "                                                                            <br>\n"
                            + "                                                                            <span>" + checkNull((String) legtype.get(eBookingrouting.getLegType())) + "</span>\n"
                            + "                                                                        </p>\n"
                            + "                                                                    </td>\n"
                            + "\n"
                            + "                                                                </tr>\n";
                }
                output += "                                                            </tbody>\n"
                        + "                                                        </table>\n"
                        + "                                                    </td>\n"
                        + "                                                </tr>";
            }
            output += "                                            </tbody>\n"
                    + "                                        </table>\n"
                    + "\n"
                    + "                                    </td>\n"
                    + "                                </tr>\n";
            if (eBookingMasterData.getBkgcutoff() != null || eBookingMasterData.getDoccutoff() != null || eBookingMasterData.getVgmcutoff() != null || eBookingMasterData.getBbrorocutoff() != null || eBookingMasterData.getHazcutoff() != null || eBookingMasterData.getPortentrycutoff() != null) {
                output += "<tr>\n"
                        + "                                    <td colspan=\"6\" style=\"padding: 10px;\">\n"
                        + "                                        <table style=\"width: 100%;border: 1px solid #E3EBF3;\">\n"
                        + "                                            <tbody>\n"
                        + "                                                <tr>\n"
                        + "                                                    <td style=\"min-width: 25%;\">Booking Cutoff</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">" + checkNull(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getBkgcutoff())) + "</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">Documentation Cutoff</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">" + checkNull(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getDoccutoff())) + "</td>\n"
                        + "                                                </tr>\n"
                        + "                                                <tr>\n"
                        + "                                                    <td style=\"min-width: 25%;\">VGM Cutoff</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">" + checkNull(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getVgmcutoff())) + "</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">Port Entry Cutoff</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">" + checkNull(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getPortentrycutoff())) + "</td>\n"
                        + "                                                </tr>\n"
                        + "                                                <tr>\n"
                        + "                                                    <td style=\"min-width: 25%;\">Hazardous Cut Off</td>\n"
                        + "                                                    <td style=\"min-width: 25%;\">" + checkNull(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getHazcutoff())) + "</td>\n";
                if (eBookingMasterData.getBbrorocutoff() != null && eBookingMasterData.getBbrorocutoff().trim().length() > 0) {
                    output += "                                                    <td style=\"min-width: 25%;\">BBRORO Cutoff</td>\n"
                            + "                                                    <td style=\"min-width: 25%;\">" + checkNull(DateConversion_New.convertDefaultFormatwithTime(eBookingMasterData.getBbrorocutoff())) + "</td>\n";
                }
                output += "                                                </tr>\n"
                        + "                                            </tbody>\n"
                        + "                                        </table>\n"
                        + "                                    </td>\n"
                        + "                                </tr>";
            }

            output += "                            </tbody>\n"
                    + "                        </table>\n";

// customer details
            output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                    + "\n"
                    + "\n"
                    + "                            <tbody>\n"
                    + "\n"
                    + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                    + "                                    <td colspan='6'><h2>Customer Details</h2></td>\n"
                    + "                                </tr>\n"
                    + "                                <tr style=\"margin:0;padding:0;text-align:center;\">\n"
                    + "                                    <th style=\"margin:0;padding:10px;vertical-align:middle;width:25%\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Customer Type</span>\n"
                    + "                                    </th>\n"
                    + "                                    <th style=\"margin:0;padding:10px;vertical-align:middle;width:33.33%\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Customer Name</span>\n"
                    + "                                    </th>\n"
                    + "                                    <th  style=\"margin:0;padding:10px;vertical-align:middle;width:50%\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\"> Address </span>\n"
                    + "                                    </th>\n"
                    + "                                </tr>\n"
                    + "\n";

            if (eBookingMasterData.getEbook_bookingname() != null && eBookingMasterData.getEbook_bookingname().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle;\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Booking Party</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_bookingname() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_bookingaddress() + " <br>"
                        + eBookingMasterData.getEbook_bookingaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_bookingcity_desc() + "<br>";
                        if (eBookingMasterData.getEbook_bookingcity_desc() != null && eBookingMasterData.getEbook_bookingcity_desc().trim().length() > 0) {
                            output += "                                                      City : " + eBookingMasterData.getEbook_bookingcity_desc() + "<br>";
                            if ("Y".equalsIgnoreCase(enable_zipcode)) {
                                output += "                                                  Zip Code : " + eBookingMasterData.getEbook_bookingzipcode() + "<br>";
                            }
                        };
                        output += "                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_bookingcountry()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_bookingphone() + " <br> ";
                if (eBookingMasterData.getEbook_bookingfax() != null && eBookingMasterData.getEbook_bookingfax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_bookingfax() + " <br> ";
                }
                output += "                                                              Email : " + eBookingMasterData.getEbook_bookingemail() + " </span>\n";
                if ("Y".equalsIgnoreCase(GENS)) {
                        output += "   <br>                                                   EORI : " + checkNull(eBookingMasterData.getEbook_bookingeori())+ " <br> "
                               + "                                                      Type of Person : " +checkNull( eBookingMasterData.getBookingtypeperson()) ;
                    };
                    output += "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_agreename() != null && eBookingMasterData.getEbook_agreename().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Agreement Party</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_agreename() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_agreeaddress() + " <br>"
                        + eBookingMasterData.getEbook_agreeaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_agreecity_desc() + "<br>";
                        if ("Y".equalsIgnoreCase(enable_zipcode)) {
                            output += "                                          Zip Code : " + eBookingMasterData.getEbook_agreezipcode() + "<br>";
                        };
                       output +="                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_agreecountry()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_agreephone() + " <br> ";
                if (eBookingMasterData.getEbook_agreefax() != null && eBookingMasterData.getEbook_agreefax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_agreefax() + " <br> ";
                }
               output += "                                                              Email : " + eBookingMasterData.getEbook_agreeemail();
                 if ("Y".equalsIgnoreCase(GENS)) {
                    output += "   <br>                                                   EORI : " + checkNull(eBookingMasterData.getEbook_agreeeori())+ " <br> "
                           + "                                                      Type of Person : " +checkNull( eBookingMasterData.getAgreetypeperson()) ;
                }
                output += "   </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_shippername() != null && eBookingMasterData.getEbook_shippername().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Shipper</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_shippername() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_shipperaddress() + " <br>"
                        + eBookingMasterData.getEbook_shipperaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_shippercity_desc() + "<br>";
                        if ("Y".equalsIgnoreCase(enable_zipcode)) {
                            output += "                                         Zip Code : " + checkNull(eBookingMasterData.getEbook_shipperzipcode()) + "<br>";
                        };
                        output += "                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_shippercountry()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_shipperphone() + " <br> ";
                if (eBookingMasterData.getEbook_shipperfax() != null && eBookingMasterData.getEbook_shipperfax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_shipperfax() + " <br> ";
                }
                 output += "                                                              Email : " + eBookingMasterData.getEbook_shipperemail() ;
                 if ("Y".equalsIgnoreCase(GENS)) {
                    output += "    <br>                                                  EORI : " + checkNull(eBookingMasterData.getEbook_shippereori())+ " <br> "
                           + "                                                      Type of Person : " +checkNull( eBookingMasterData.getShippertypeperson()) ;
                 }
                    output += "                               </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_consigneename() != null && eBookingMasterData.getEbook_consigneename().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Consignee</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_consigneename() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_consigneeaddress() + " <br>"
                        + eBookingMasterData.getEbook_consigneeaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_consigneecity_desc() + "<br>";
                  if ("Y".equalsIgnoreCase(enable_zipcode)) {
                            output += "                                            Zip Code : " + checkNull(eBookingMasterData.getEbook_consigneezipcode()) + "<br>";
                        };
                        output +="                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_consigneecountry()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_consigneephone() + " <br> ";
                if (eBookingMasterData.getEbook_consigneefax() != null && eBookingMasterData.getEbook_consigneefax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_consigneefax() + " <br> ";
                }
               output += "                                                              Email : " + eBookingMasterData.getEbook_consigneeemail() ;
                 if ("Y".equalsIgnoreCase(GENS)) {
                    output += "   <br>                                                   EORI : " +checkNull( eBookingMasterData.getEbook_consigneeeori())+ " <br> "
                           + "                                                      Type of Person : " +checkNull( eBookingMasterData.getConsigneetypeperson());
                    }
                        output += " </span>\n"                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_notifyname() != null && eBookingMasterData.getEbook_notifyname().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Notify</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_notifyname() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_notifyaddress() + " <br>"
                        + eBookingMasterData.getEbook_notifyaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_notifycity_desc() + "<br>";
                        if ("Y".equalsIgnoreCase(enable_zipcode)) {
                            output += "                                           Zip Code : " + checkNull(eBookingMasterData.getEbook_notifyzipcode()) + "<br>";
                        };
                        output += "                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_notifycountry()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_notifyphone() + " <br> ";
                if (eBookingMasterData.getEbook_notifyfax() != null && eBookingMasterData.getEbook_notifyfax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_notifyfax() + " <br> ";
                }
                      output += "                                                              Email : " + eBookingMasterData.getEbook_notifyemail();
                 if ("Y".equalsIgnoreCase(GENS)) {
                    output += "   <br>                                                   EORI : " +checkNull( eBookingMasterData.getEbook_notifyeori())+ " <br> "
                           + "                                                      Type of Person : " +checkNull( eBookingMasterData.getNotifytypeperson());
                }
                output += "                                         </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_notifyname_n1() != null && eBookingMasterData.getEbook_notifyname_n1().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Notify 1 </p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_notifyname_n1() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_notifyaddress_n1() + " <br>"
                        + eBookingMasterData.getEbook_notifyaddress1_n1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_notifycity_desc_n1() + "<br>";
                        if ("Y".equalsIgnoreCase(enable_zipcode)) {
                            output += "                                             Zip Code : " + checkNull(eBookingMasterData.getEbook_notifyzipcode_n1()) + "<br>";
                        };
                        output += "                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_notifycountry_n1()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_notifyphone_n1() + " <br> ";
                if (eBookingMasterData.getEbook_notifyfax_n1() != null && eBookingMasterData.getEbook_notifyfax_n1().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_notifyfax_n1() + " <br> ";
                }
                 output += "                                                              Email : " + eBookingMasterData.getEbook_notifyemail_n1();
                         if ("Y".equalsIgnoreCase(GENS)) {
                    output += "    <br>                                                  EORI : " +checkNull( eBookingMasterData.getEbook_notifyeori_n1())+ " <br> "
                           + "                                                      Type of Person : " + checkNull(eBookingMasterData.getNotifytypeperson_n1());
                }
                output += " </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_forwardername() != null && eBookingMasterData.getEbook_forwardername().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Forwarder</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_forwardername() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_forwarderaddress() + " <br>"
                        + eBookingMasterData.getEbook_forwarderaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_forwardercity_desc() + "<br>";
                        if ("Y".equalsIgnoreCase(enable_zipcode)) {
                            output += "                                            Zip Code : " + checkNull(eBookingMasterData.getEbook_forwarderzipcode()) + "<br>";
                        };
                       output +="                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_forwardercountry()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_forwarderphone() + " <br> ";
                if (eBookingMasterData.getEbook_forwarderfax() != null && eBookingMasterData.getEbook_forwarderfax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_forwarderfax() + " <br> ";
                }
                 output += "                                                              Email : " + eBookingMasterData.getEbook_forwarderemail() ;
                 if ("Y".equalsIgnoreCase(GENS)) {
                    output += "  <br>                                                    EORI : " + checkNull(eBookingMasterData.getEbook_forwardereori())+ " <br> "
                           + "                                                      Type of Person : " + checkNull(eBookingMasterData.getForwardertypeperson());
                 }
                     output += "   </span>\n"                     
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (eBookingMasterData.getEbook_Warehousename() != null && eBookingMasterData.getEbook_Warehousename().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Warehouse</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_Warehousename() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + eBookingMasterData.getEbook_Warehouseaddress() + " <br>"
                        + eBookingMasterData.getEbook_Warehouseaddress1() + " <br><br> "
                        + "                                                      City : " + eBookingMasterData.getEbook_Warehousecity_desc() + "<br>";
                        if ("Y".equalsIgnoreCase(enable_zipcode)) {
                            output += "                                                      Zip Code : " + checkNull(eBookingMasterData.getEbook_Warehousezipcode()) + "<br>";
                        };
                        output += "                                                      Country : " + contrylist.get(eBookingMasterData.getEbook_Warehousecountry()) + " <br>"
                        + "                                                      Telephone : " + eBookingMasterData.getEbook_Warehousephone() + " <br> ";
                if (eBookingMasterData.getEbook_Warehousefax() != null && eBookingMasterData.getEbook_Warehousefax().trim().length() > 0) {
                    output += "                                                      Fax : " + eBookingMasterData.getEbook_Warehousefax() + " <br> ";
                }
                  output += "                                                              Email : " + eBookingMasterData.getEbook_Warehouseemail() ;
                   if ("Y".equalsIgnoreCase(GENS)) {
                    output += " <br>                                                     EORI : " + checkNull(eBookingMasterData.getEbook_Warehouseeori())+ " <br> "
                           + "                                                      Type of Person : " + checkNull(eBookingMasterData.getWarehousetypeperson());
                  }
                   output += "   </span>\n"                    
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            output += "                            </tbody>\n"
                    + "                        </table>\n";

//Cargo Details
            output += "                        <table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;\">\n"
                    + "                            <tbody>\n"
                    + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                    + "                                    <td colspan='6'><h2>Cargo Details</h2></td>\n"
                    + "                                </tr>\n"
                    + "\n"
                    + "                                <tr style=\"margin:0;padding:0;text-align:center;\">\n"
                    + "                                    <th colspan=\"1\" style=\"margin:0;padding:10px;vertical-align:middle;\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">S.NO.</span>\n"
                    + "                                    </th>\n"
                    + "                                    <th colspan=\"4\" style=\"margin:0;padding:10px;vertical-align:middle;width:50%;\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Equiment Description</span>\n"
                    + "                                    </th>\n"
                    + "                                </tr>\n";
            AmendmentBookingFormBean eBookingcargoData = null;
            for (int i = 0; i < cargolist.size(); i++) {
                eBookingcargoData = (AmendmentBookingFormBean) cargolist.get(i);
                output += "<tr style=\"margin:0;padding:0;line-height: 10px;\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\"> " + (i + 1) + " </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + unitlis.get(eBookingcargoData.getEbook_eqp_type()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Equipment</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingcargoData.getContainerNo() + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp. No.</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eqpmap.get(eBookingcargoData.getEbook_eqp()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp. Size</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + ("M".equalsIgnoreCase(checkNull(eBookingcargoData.getEqpstatus())) ? "Empty" : "Laden") + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp.Status</span>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                                <tr style=\"margin:0;padding:0;line-height: 10px;\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 35px;\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + ("Y".equalsIgnoreCase(eBookingcargoData.getSoc()) ? "Yes" : "No") + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">SOC</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width: 25%;font-size:14px;margin:0;padding: 9px;vertical-align:middle;\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + ("Y".equalsIgnoreCase(eBookingcargoData.getHzd()) ? "Yes" : "No") + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Hazardous</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull(eBookingcargoData.getEbook_cargoweight()) + " " + unitlis.get(checkNull(eBookingcargoData.getEbook_weightunit())) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Cargo Weight(per Container)</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull(eBookingcargoData.getEbook_tareweight()) + " " + unitlis.get(checkNull(eBookingcargoData.getEbook_weightunit())) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Tare Weight</span>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                                <tr style=\"margin:0;padding:0;line-height: 10px;\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 35px;\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull(eBookingcargoData.getEbook_grossweight()) + " " + unitlis.get(checkNull(eBookingcargoData.getEbook_weightunit())) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Gross Weight</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width: 25%;font-size:14px;margin:0;padding: 9px;vertical-align:middle;\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull((String) packagemap.get(eBookingcargoData.getEbook_packagecode())) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">" + "Package" + "</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingcargoData.getEbook_packquantity() + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">" + "No.of Packages" + "</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingcargoData.getEbook_cargovolume() + " " + (("F".equalsIgnoreCase(eBookingcargoData.getEbook_volumeunit())) ? "Cubic Feet" : (("M".equalsIgnoreCase(eBookingcargoData.getEbook_volumeunit())) ? "Cubic Meter" : "")) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Volume</span>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n";

                if ("Y".equalsIgnoreCase(EnableActualCommoName)) {
                    output += "                                <tr style=\"margin:0;padding:0;line-height: 10px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "                                    <td colspan='6' style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 35px;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_actCommoName()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Actual Commodity Name</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n";
                }
                output += "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                        + "\n"
                        + "                                    <td colspan='6' style=\"font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                        + "\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull((String) comlist.get(checkNull(eBookingcargoData.getEbook_commoditycode()))) + " ~~ " + checkNull(eBookingcargoData.getEbook_commoditycode()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Commodity\n"
                        + "                                        </span>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\"> </td>\n"
                        + "\n"
                        + "                                    <td colspan='6' style=\"font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                        + "\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull((String) comlist.get(eBookingcargoData.getHscode())) + " ~~ " + checkNull(eBookingcargoData.getHscode()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">" + hsCodelbl + "\n"
                        + "                                        </span>\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "\n"
                        + "                                </tr>\n"
                        + "\n"
                        + "                                <tr> <td style=\"font-size:14px;margin:0;padding:10px;\"> </td>\n"
                        + "<td colspan=\"6\" style=\"margin:0;padding: 9px;\"><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td></tr>\n";
                if ("Y".equalsIgnoreCase(eBookingcargoData.getHzd())) {
                    output += "<tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + ("Y".equalsIgnoreCase(eBookingcargoData.getEbook_poluttunit()) ? "Yes" : ("N".equalsIgnoreCase(eBookingcargoData.getEbook_poluttunit()) ? "No" : "")) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Marine Pollutant</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + eBookingcargoData.getEbook_unno() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">UN No.</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_flashpoint()) + " " + unitlis.get(checkNull(eBookingcargoData.getEbook_flashunit())) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Flash Point</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_dgclass()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">DG Class</span>\n"
                            + "                                    </td>\n"
                            + "\n"
                            + "\n"
                            + "\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + dgshipper.get(checkNull(checkNull(eBookingcargoData.getEbook_unno()).split("~~")[0])) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Shipping Name</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr> <td style=\"font-size:14px;margin:0;padding:10px;\"> </td>\n"
                            + "<td colspan=\"6\" style=\"margin:0;padding: 9px;\"><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td></tr>";
                }
                if ("O".equalsIgnoreCase(eBookingcargoData.getEbook_eqp_type())) {
                    output += "<tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + ((("M".equalsIgnoreCase(eBookingcargoData.getEbook_dimensionunit())) ? "Meters" : (("F".equalsIgnoreCase(eBookingcargoData.getEbook_dimensionunit())) ? "Feet" : (("C".equalsIgnoreCase(eBookingcargoData.getEbook_dimensionunit()) ? "Centimeters" : ""))))) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Unit</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_owleft()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Left</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_owright()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Right</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_olforward()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Forward</span>\n"
                            + "                                    </td>\n"
                            + "\n"
                            + "\n"
                            + "\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_olaft()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Aft</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_oh()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Top</span>\n"
                            + "                                    </td>\n"
                            + "\n"
                            + "                                </tr>\n"
                            + "                                <tr> <td style=\"font-size:14px;margin:0;padding:10px;\"> </td>\n"
                            + "<td colspan=\"6\" style=\"margin:0;padding: 9px;\"><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td></tr>\n";
                }
                if ("R".equalsIgnoreCase(eBookingcargoData.getEbook_eqp_type())) {
                    output += "<tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + (("Y".equalsIgnoreCase(checkNull(eBookingcargoData.getEbook_nor()))) ? "Yes" : "No") + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Non-Operating Reefer\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getEbook_presetemp()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Set Temp\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n";
                    if (!"Y".equalsIgnoreCase(hideMinMaxTemp)) {
                        output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(eBookingcargoData.getEbook_mintemp()) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Min Temp\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n"
                                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(eBookingcargoData.getEbook_maxtemp()) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Max Temp\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n"
                                + "                                </tr>\n";
                    }
                    output += "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull((String) unitlis.get(checkNull(eBookingcargoData.getEbook_temperunit()))) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Temp Unit</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingcargoData.getVentilation()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">" + Ventilationlabel + "\n"
                            + "\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n";
                    if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {
                        output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + eBookingcargoData.getHumidity() + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Humidity(%)\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n"
                                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + ("O".equalsIgnoreCase(eBookingcargoData.getDrains()) ? "Open" : "C".equalsIgnoreCase(eBookingcargoData.getDrains()) ? "Closed" : "") + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Drains\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    }
                    output += "                                </tr>\n";
                    if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {
                        output += "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                                + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 35px\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(eBookingcargoData.getCo2()) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">CO2\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n"
                                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(eBookingcargoData.getO2()) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">O2\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n"
                                + "                                    \n"
                                + "                                </tr>\n";
                    }
                    output += "                                <tr> <td style=\"font-size:14px;margin:0;padding:10px;\"> </td>\n"
                            + "<td colspan=\"6\" style=\"margin:0;padding: 9px;\"><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td></tr> \n";
                }
                output += "                                <tr> <td colspan='6'><hr></td></tr> \n";

            }
            output += "</tbody>\n"
                    + "                        </table>\n";

            //Haulage Details
            List<BookingHaulagedetailsVo> haulagelist = eBookingMasterData.getHaulagelist();
            if (haulagelist != null && haulagelist.size() > 0) {
                output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                        + "\n"
                        + "\n"
                        + "                            <tbody>\n"
                        + "\n"
                        + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                        + "                                    <td colspan='6'><h2>Haulage Details</h2></td>\n"
                        + "                                </tr>\n";
                for (int i = 0; i < haulagelist.size(); i++) {
                    BookingHaulagedetailsVo haulvo = haulagelist.get(i);
                    output += "<tr style=\"margin:0;padding:0;line-height:12px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getEqpid() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp. No.</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getEqptype() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp.Type</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getPicklocname() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">PickUp Location</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getAddress() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getRefno() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Ref.No</span>\n"
                            + "                                    </td>\n"
                            + "                                   <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getStopsequence() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Stop Sequence</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + DateConversion_New.convertDefaultFormatwithTime(haulvo.getDropdate()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Drop Date*</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getContactperson() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Contact Person</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getContactphone() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Contact Phone</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getContactemail() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Contact Email</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + ("Y".equalsIgnoreCase(haulvo.getTwrsoverweight()) ? "Yes" : "No") + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">OverWeight</span>\n"
                            + "                                    </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:12px;margin:0;padding: 0px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + haulvo.getRemarks() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Remarks</span>\n"
                            + "                                    </td>\n"
                            + "                                    \n"
                            + "                                </tr>\n"
                            + "                                <tr> <td colspan='6'><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td></tr>";
                }
                output += "                               <tr> <td colspan='6'><hr></td></tr>"
                        + "</tbody>\n"
                        + "                        </table>\n";
            }
            ENSDeclarantVo edvo = eBookingMasterData.getEnsdecvo();
            if ("Y".equalsIgnoreCase(GENS) && "Y".equalsIgnoreCase(iseuport) && edvo != null && checkNull(edvo.getEnsFillingType()).length() > 0) {
                output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:#fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                        + "                            <tbody>\n"
                        + "                                <tr style=\"margin:0;padding:0;font-weight:bold;text-decoration:underline;text-align:center\">\n"
                        + "                                    <td colspan=\"6\">\n"
                        + "                                        <h2>ENS Customer Declaration</h2>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                        + "                                            <span>" + ("Y".equalsIgnoreCase(checkNull(edvo.getEuCustZone())) ? "Yes" : "No") + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                        + "                                            <span>Is cargo to be delivered to EU Customs Zone</span>\n"
                        + "                                        </span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull(edvo.getBlTypeENS()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Type of Bill of Lading</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                             <span>" + checkNull(edvo.getEnsFillingType()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Type of ENS Filing</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                           <span>" + checkNull(edvo.getPaymentMethod()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Method of Payment</span>\n"
                        + "                                    </td>\n"
                        + "                                </tr>"
                        + "                                <tr>\n"
                        + "                                    <td colspan=\"6\"><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td>\n"
                        + "                                </tr>";
 
                if ("Multiple Filing".equalsIgnoreCase(checkNull(edvo.getEnsFillingType()))) {
                    output += "<tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:left\">\n"
                            + "                                    <td colspan='6'><h4>Multiple Filing - Supplementary Declarant details</h4></td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantName()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Name</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantAddress()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantAddress1()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address1</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantCity()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">City</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantCountry()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Country</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantState()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">State</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantTelephone()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Telephone</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantEmail()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Email</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantZipcode()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Zipcode</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantEORI()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>EORI</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantFillingType()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Filing Type</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>";
                }
                if ("Single Filing".equalsIgnoreCase(checkNull(edvo.getEnsFillingType()))) {
                    //Buyer
                    output += "<tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:left\">\n"
                            + "                                    <td colspan='6'><h4>Buyer</h4></td>\n"
                            + "                                </tr>\n"
                            + "								<tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyername()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Name</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyeraddress()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyeraddress1()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address1</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyercity()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">City</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyercountry()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Country</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyerphone()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Telephone</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyeremail()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Email</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyerstate()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">State</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "				        <tr style=\"margin:0;padding:0;line-height:15px;\">\n";
                    if (checkNull(eBookingMasterData.getEbook_Buyerfax()).length() > 0) {
                        output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                                + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyerfax()) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                                + "                                            <span>Fax</span>\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    }
                    output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyerzipcode()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Zipcode</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyereori()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">EORI</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Buyerpersontype()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Type of Person</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n";
                    //Seller
                    output += "<tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:left\">\n"
                            + "                                    <td colspan='6'><h4>Seller</h4></td>\n"
                            + "                                </tr>\n"
                            + "								<tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellername()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Name</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Selleraddress()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Selleraddress1()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address1</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellercity()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">City</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellercountry()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Country</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellerphone()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Telephone</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Selleremail()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Email</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellerstate()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">State</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "				      <tr style=\"margin:0;padding:0;line-height:15px;\">\n";
                    if (checkNull(eBookingMasterData.getEbook_Sellerfax()).length() > 0) {
                        output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                                + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellerfax()) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                                + "                                            <span>Fax</span>\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    }
                    output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellerzipcode()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Zipcode</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellereori()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">EORI</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(eBookingMasterData.getEbook_Sellerpersontype()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Type of Person</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n";
 
                }
                output += "<tr style=\"margin:0;padding:0;font-weight: bold;text-align:center\">\n"
                        + "                                    <td colspan='8'>\n"
                        + "                                        <hr>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                            </tbody>\n"
                        + "                        </table>";
            }
 
            // Additional Remarks
            if (eBookingMasterData.getSalesRateAgree() != null || eBookingMasterData.getEbook_status_remarks() != null) {
                output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                        + "                            <tbody>\n"
                        + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                        + "                                    <td colspan='6'><h2>Additional Remarks</h2></td>\n"
                        + "                                </tr>\n"
                        + "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle;width:20%\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Comments on Rates : \n"
                        + "                                        </p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle;width:80%\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getSalesRateAgree() + "</span>\n"
                        + "                                        </p>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;vertical-align:middle;width:20%\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Additional Information : \n"
                        + "                                        </p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;vertical-align:middle;width:80%\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + eBookingMasterData.getEbook_status_remarks() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                            </tbody>\n"
                        + "                        </table>\n";
            }
            output += "</td>\n"
                    + "                </tr>\n"
                    + "            </tbody>\n"
                    + "        </table>		\n"
                    + "    </body>\n"
                    + "</html>\n";

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return output;
    }

    public String SIBLcommonMailMail(String imagepath, String logo_name, String si_no, HashMap masterCache, String bookno, String blno, SIEditForm siOBJ, SIPartiesVO parties_routing, Map<SIMasterVO, SIConatainerValueVO> cntMapObj, String type) {
        String output = "";
        try {
            Map vessellist = (Map) masterCache.get("VESSELLIST");
            Map contrylist = (Map) masterCache.get("CONTRYLIST");
            Map serlist = (Map) masterCache.get("SERVICELIST");
            Map packagemap = (Map) masterCache.get("PACKAGELIST");
            Map portlist = (Map) masterCache.get("PORTLIST");
            String carriercode = (String) masterCache.get("carriercode");
            Map comlist = (Map) masterCache.get("comlist");
            String agencyRefNo = (String) masterCache.get("agencyRefNo");
            String SHOW_CHARGES_IN_SIBL = (String) masterCache.get("SHOW_CHARGES_IN_SIBL");
            boolean is_SI_restriction = (boolean) masterCache.get("is_SI_restriction");
            String EnableReeferDetails = (String) masterCache.get("EnableReeferDetails");
            String GENS = (String) masterCache.get("Enable_Eori_Details");
            String enable_zipcode = (String) masterCache.get("enable_zipcode");

            String hsnamecode = (String) masterCache.get(GeneralConf.Display_HS_NAME_CODE);
            String hsCodelbl = (("Y".equalsIgnoreCase(checkNull(hsnamecode))) ? "HS Name and Code" : "HS Code");

            String Ventilationlabel = (String) masterCache.get("Ventilation");

            output = "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <meta charset=\"UTF-8\">\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "        <table>\n"
                    + "            <tbody>\n"
                    + "                <tr>\n"
                    + "                    <td>\n"
                    + "                        <table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                    + "                            <tbody>\n"
                    + "                                <tr><td></td></tr>\n"
                    + "\n"
                    + "                                <tr>\n"
                    + "                                    <td style=\"margin:0 20px 0 0;padding:0 15px 0 0;width:1%\">\n"
                    + "                                        <div style=\"display:inline-block;border-right:1px solid #ccc;margin:0 0 8px 0\">\n"
                    + "                                            <img style=\"padding:10px;max-height: 45px;\" src='" + imagepath + logo_name + "?" + jsdate + "' alt=\"Logo\">\n"
                    + "                                        </div>\n"
                    + "                                    </td>\n"
                    + "\n"
                    + "                                    <td style=\"font-size:30px;margin:0;padding:3px;width:30%;\"> " + ("SI".equalsIgnoreCase(type) ? "Shipping Instruction" : "Bill Of Lading") + " : <span style=\"color:#62be82; font-weight: bold;\"> (" + si_no + ") </span></td>\n"
                    + "\n"
                    + "\n"
                    + "                                </tr>\n"
                    + "                            </tbody>\n"
                    + "                        </table>\n"
                    + "\n"
                    + "                        <table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;border-bottom: 1px solid #FFC107 !important;\">\n"
                    + "                            <tbody>\n"
                    + "                                <tr>\n"
                    + "                                    <td></td>\n"
                    + "                                </tr>\n"
                    + "                                <tr style=\"height:60px;overflow:hidden;margin-top:20px;padding:0 0 5px;\">\n"
                    + "                                    <td style=\"font-size:16px;\">\n"
                    + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0\">\n"
                    + "                                            <span>" + bookno + "</span>\n"
                    + "                                            <br>\n"
                    + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Booking No </span>\n"
                    + "                                        </span>\n"
                    + "\n"
                    + "                                    </td>\n"
                    + "                                    <td  style=\"font-size:16px;\">\n"
                    + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0;margin-right:19px;\">\n"
                    + "                                            <span>" + si_no + "</span>\n"
                    + "                                            <br>\n"
                    + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">SI No </span>\n"
                    + "                                        </span>\n"
                    + "\n"
                    + "                                    </td>\n";
            if (blno != null && blno.trim().length() > 0) {
                output += "                                    <td  style=\"font-size:16px;\">\n"
                        + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0;margin-right:19px\">\n"
                        + "                                            <span>" + blno + "</span>\n"
                        + "                                            <br>\n"
                        + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">BL No   </span>\n"
                        + "                                        </span>\n"
                        + "\n"
                        + "                                    </td>\n";
            }
            output += "                                    <td  style=\"font-size:16px;\">\n"
                    + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0;margin-right:19px\">\n"
                    + "                                            <span>" + checkNull(agencyRefNo) + "</span>\n"
                    + "                                            <br>\n"
                    + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Agency Ref No.   </span>\n"
                    + "                                        </span>\n"
                    + "\n"
                    + "                                    </td>\n";
            String nvoccVal = checkNull(parties_routing != null ? parties_routing.getEbook_nvocc() : null);
            if (nvoccVal.length() == 0 && siOBJ != null) {
                nvoccVal = checkNull(siOBJ.getEbook_nvocc());
            }
            if ("Y".equalsIgnoreCase(nvoccVal) || "N".equalsIgnoreCase(nvoccVal)) {
                output += "                                    <td  style=\"font-size:16px;\">\n"
                        + "                                        <span style=\"display:-moz-inline-stack;display:inline-block;zoom:1;margin:0 0 7px 0;font-weight:bold;padding:0;margin-right:19px\">\n"
                        + "                                            <span>" + ("Y".equalsIgnoreCase(nvoccVal) ? "Yes" : "No") + "</span>\n"
                        + "                                            <br>\n"
                        + "                                            <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Automated NVOCC Master BL</span>\n"
                        + "                                        </span>\n"
                        + "\n"
                        + "                                    </td>\n";
            }
            output += "                                </tr>\n"
                    + "\n"
                    + "                                <tr>\n"
                    + "                                    <td></td>\n"
                    + "                                </tr>\n"
                    + "                            </tbody>\n"
                    + "                        </table>\n";

            //Customer details
            output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                    + "\n"
                    + "\n"
                    + "                            <tbody>\n"
                    + "\n"
                    + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                    + "                                    <td colspan='6'><h2>Customer Details</h2></td>\n"
                    + "                                </tr>\n"
                    + "                                <tr style=\"margin:0;padding:0;text-align:center;\">\n"
                    + "                                    <th style=\"margin:0;padding:10px;vertical-align:middle;width:25%\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Customer Type</span>\n"
                    + "                                    </th>\n"
                    + "                                    <th style=\"margin:0;padding:10px;vertical-align:middle;width:33.33%\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Customer Name</span>\n"
                    + "                                    </th>\n"
                    + "                                    <th  style=\"margin:0;padding:10px;vertical-align:middle;width:50%\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\"> Address </span>\n"
                    + "                                    </th>\n"
                    + "                                </tr>\n"
                    + "\n";

            if (siOBJ.getSpnameheader() != null && siOBJ.getSpnameheader().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Shipper</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + siOBJ.getSpnameheader() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + siOBJ.getHeaderShipper() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (siOBJ.getConsnameheader() != null && siOBJ.getConsnameheader().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Consignee</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + siOBJ.getConsnameheader() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + siOBJ.getHeaderConsignee() + " </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (siOBJ.getNpnameheader() != null && siOBJ.getNpnameheader().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Notify</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + siOBJ.getNpnameheader() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + siOBJ.getHeaderNotify() + " </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (siOBJ.getNp2name() != null && siOBJ.getNp2name().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Notify 1 </p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + siOBJ.getNp2name() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + siOBJ.getNp2addr() + " <br>"
                        + siOBJ.getNp2addr1() + " <br><br> "
                        + "                                                      City : " + siOBJ.getNp2city() + "<br>";
                        if("Y".equalsIgnoreCase(enable_zipcode) && siOBJ.getNp2zipcode()!= null && siOBJ.getNp2zipcode().trim().length() > 0){
                            output += "                                                      Zip Code : " + siOBJ.getNp2zipcode() + " <br>";
                        };
                        output +=  "                                                      Country : " + siOBJ.getNp2CountryName() + " <br>"
                        + "                                                      Telephone : " + siOBJ.getNp2phone() + " <br> ";
                if (siOBJ.getNp2fax() != null && siOBJ.getNp2fax().trim().length() > 0) {
                    output += "                                                      Fax : " + siOBJ.getNp2fax() + " <br> ";
                }
                output += "                                                            Email : " + siOBJ.getNp2email() ;
                if ("Y".equalsIgnoreCase(GENS)) {
                    output += "  <br>                                                     EORI : " + checkNull(siOBJ.getNp2eori()) + " <br> "
                             + "                                                      Type of Person : " + checkNull(siOBJ.getNp2persontype()) + "<br>";
                }
                output += " </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (siOBJ.getNp3name() != null && siOBJ.getNp3name().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Notify 2 </p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + siOBJ.getNp3name() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + siOBJ.getNp3addr() + " <br>"
                        + siOBJ.getNp3addr1() + " <br><br> "
                        + "                                                      City : " + siOBJ.getNp3city() + "<br>";
                        if("Y".equalsIgnoreCase(enable_zipcode) && siOBJ.getNp3zipcode()!= null && siOBJ.getNp3zipcode().trim().length() > 0){
                            output += "                                                      Zip Code : " + siOBJ.getNp3zipcode() + " <br>";
                        };
                        output += "                                                      Country : " + siOBJ.getNp3CountryName() + " <br>"
                        + "                                                      Telephone : " + siOBJ.getNp3phone() + " <br> ";
                if (siOBJ.getNp2fax() != null && siOBJ.getNp2fax().trim().length() > 0) {
                    output += "                                                      Fax : " + siOBJ.getNp3fax() + " <br> ";
                }
                output += "                                                              Email : " + siOBJ.getNp3email();
                 if ("Y".equalsIgnoreCase(GENS)) {
                    output += "  <br>                                                     EORI : " + checkNull(siOBJ.getNp3eori() )+ " <br> "
                             + "                                                          Type of Person : " + checkNull(siOBJ.getNp3persontype()) + "<br>";
                }
                output += "     </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }
            if (siOBJ.getFwname() != null && siOBJ.getFwname().trim().length() > 0) {
                output += "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Forwarder</p>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + siOBJ.getFwname() + "</span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "\n"
                        + "                                    <td style=\"font-size:14px;margin:0;padding:10px;border-bottom:1px solid #e0e0e0;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span> " + siOBJ.getFwaddr() + " <br>"
                        + siOBJ.getFwaddr1() + " <br><br> "
                        + "                                                      City : " + siOBJ.getFwcity() + "<br>";
                        if("Y".equalsIgnoreCase(enable_zipcode) && siOBJ.getFwzipcode()!= null && siOBJ.getFwzipcode().trim().length() > 0){
                            output += "                                                      Zip Code : " + siOBJ.getFwzipcode() + " <br>";
                        };
                        output += "                                                      Country : " + siOBJ.getFwCountryName() + " <br>"
                        + "                                                      Telephone : " + siOBJ.getFwphone() + " <br> ";
                if (siOBJ.getFwfax() != null && siOBJ.getFwfax().trim().length() > 0) {
                    output += "                                                      Fax : " + siOBJ.getFwfax() + " <br> ";
                }
                output += "                                                              Email : " + siOBJ.getFwemail() ;
                   if ("Y".equalsIgnoreCase(GENS)) {
                    output += "  <br>                                                     EORI : " + checkNull(siOBJ.getFweori()) + " <br> "
                             + "                                                          Type of Person : " + checkNull(siOBJ.getFwpersontype())+ "<br>";
                }
                output += "  </span>\n"
                        + "                                        </p>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "\n";
            }

            output += "                            </tbody>\n"
                    + "                        </table>\n";

            //Header info
            output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                    + "\n"
                    + "\n"
                    + "                            <tbody>\n"
                    + "\n"
                    + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                    + "                                    <td colspan='6'><h2>Header Information</h2></td>\n"
                    + "                                </tr>\n"
                    + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                    + "                                            <span>" + siOBJ.getHid_pla_free_txt() + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                    + "                                            <span>Origin</span>\n"
                    + "                                        </span>\n"
                    + "                                    </td>\n"
                    + "\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span>" + siOBJ.getHid_pol_free_txt() + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Port of Loading</span>\n"
                    + "                                    </td>\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span>" + siOBJ.getHid_pod_free_txt() + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Port of Discharge</span>\n"
                    + "                                    </td>\n"
                    + "\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span>" + siOBJ.getHid_pld_free_txt() + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Delivery</span>\n"
                    + "                                    </td>\n"
                    + "                                </tr>\n"
                    + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                    + "                                            <span>" + siOBJ.getPla_free_txt() + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                    + "                                            <span>Print on BL as </span>\n"
                    + "                                        </span>\n"
                    + "                                    </td>\n"
                    + "\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span>" + siOBJ.getPol_free_txt() + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Print on BL as </span>\n"
                    + "                                    </td>\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span>" + siOBJ.getPod_free_txt() + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Print on BL as </span>\n"
                    + "                                    </td>\n"
                    + "\n"
                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                            <span>" + siOBJ.getPld_free_txt() + "</span>\n"
                    + "                                        </p>\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Print on BL as </span>\n"
                    + "                                    </td>\n"
                    + "                                </tr>\n"
                    + "                            </tbody>\n"
                    + "                        </table>\n";
            //Schedule Details
            int transittime = 0;
            if (parties_routing != null) {
                transittime = DateConversion_New.daysDifferentBetweenTwoDays(parties_routing.getPolat(), parties_routing.getPodat());
                output += " <table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:5px;font-style:normal;width:900px;border-bottom: 1px solid #e0e0e0;\">                            <tbody>\n"
                        + "                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                    <td colspan='6' style=\"padding:0px 10px;\">\n"
                        + "\n"
                        + "                                        <table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:20px;font-family:Asap,sans-serif;color:#333;padding:5px;font-style:normal;width:900px;border: 1px solid #b3b0b0;border-radius: 5px;margin-top:10px;\">\n"
                        + "                                            <tbody>\n"
                        + "                                                <tr style=\"margin:0;padding:0\">\n"
                        + "                                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                        + "                                                            <span>" + parties_routing.getReceipt() + "</span><br>\n"
                        + "                                                            <span>" + parties_routing.getPolat() + "</span>\n"
                        + "                                                        </p>\n"
                        + "                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                        + "                                                            <span>Departure</span>\n"
                        + "                                                        </span>\n"
                        + "                                                    </td>\n"
                        + "\n"
                        + "                                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                                            <span>" + parties_routing.getDelivery() + "</span><br>\n"
                        + "                                                            <span>" + parties_routing.getPodat() + "</span>\n"
                        + "                                                        </p>\n"
                        + "                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Arrival</span>\n"
                        + "                                                    </td>\n"
                        + "                                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                                            <span>" + parties_routing.getVesselName() + "</span><br>\n"
                        + "                                                            <span>" + parties_routing.getVoyagecode() + parties_routing.getBound() + "</span>\n"
                        + "                                                        </p>\n"
                        + "                                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Vessel / voyage / bound</span>\n"
                        + "                                                    </td>\n"
                        + "\n"
                        + "                                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                                            <span>Transit Time : " + transittime + " days</span><br>\n"
                        + "                                                            <span>Haulage Origin :" + parties_routing.getTransportOrigin() + "  </span><br>\n"
                        + "                                                            <span>Haulage Destination :" + parties_routing.getTransportDestination() + " </span>\n"
                        + "                                                        </p>\n"
                        + "                                                    </td>\n"
                        + "                                                </tr>\n"
                        + "                                            </tbody>\n"
                        + "                                        </table>\n"
                        + "\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                            </tbody>\n"
                        + "                        </table>\n";
            }
            //Cargo Details 
            output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;\">\n"
                    + "                            <tbody>\n"
                    + "\n"
                    + "                                <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                    + "                                    <td colspan='6'><h2>Cargo Details</h2></td>\n"
                    + "                                </tr>\n"
                    + "\n"
                    + "                                <tr style=\"margin:0;padding:0;text-align:center;\">\n"
                    + "                                    <th colspan=\"1\" style=\"margin:0;padding:10px;vertical-align:middle;\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">S.NO.</span>\n"
                    + "                                    </th>\n"
                    + "                                    <th colspan=\"4\" style=\"margin:0;padding:10px;vertical-align:middle;width:50%;\">\n"
                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Container Nos/Seal Nos and Package Description</span>\n"
                    + "                                    </th>\n"
                    + "\n"
                    + "                                </tr>\n";
            if (cntMapObj != null && cntMapObj.size() > 0) {
                Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = cntMapObj.entrySet();
                int sno = 1;
                for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                    SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                    SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                    SISealVO siSealVOObj = (SISealVO) siCNTValueVO.getSiSealObj();
                    ArrayList cntList = (ArrayList) siCNTValueVO.getCntDeatilsObj();
                    output += "<tr style=\"margin:0;padding:0;line-height: 10px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">" + sno + " </td>\n"
                            + "\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + siMasterVO.getStrCntNo() + ""
                            + "                                            </span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Container No.\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + ("Y".equalsIgnoreCase(checkNull(siMasterVO.getStrsoc())) ? "Yes" : "No") + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">SOC</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siMasterVO.getStrType()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Eqp. Size</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + siSealVOObj.getStrCarrierSeal() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Carrier Seal\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "\n"
                            + "\n"
                            + "\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height: 10px;\">\n"
                            + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 35px;\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + siSealVOObj.getStrShipperSeal() + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Shipper Seal\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "\n"
                            + "                                    <td style=\"width: 25%;font-size:14px;margin:0;padding: 9px;vertical-align:middle;\">\n"
                            + "\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + siMasterVO.getTareweight() + " " + checkNull((String) unitlis.get(siMasterVO.getWeightUnit())) + ""
                            + "                                            </span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Tare Weight\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n";
                    if ("R".equalsIgnoreCase(checkNull(siMasterVO.getStrEqpType())) && "Y".equalsIgnoreCase(EnableReeferDetails)) {
                        output += "                                    <td style=\"width: 25%;font-size:14px;margin:0;padding: 9px;vertical-align:middle;\">\n"
                                + "\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(siMasterVO.getNor()) + ""
                                + "                                            </span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Non-Operating Reefer\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n"
                                + "                                    <td style=\"width: 25%;font-size:14px;margin:0;padding: 9px;vertical-align:middle;\">\n"
                                + "\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + NumberFormat.numberFormat(checkNull(siMasterVO.getSetTemp())) + ""
                                + "                                            </span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Set Temp\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n"
                                + "                                </tr>\n"
                                + "                                <tr style=\"margin:0;padding:0;line-height: 10px;\">\n"
                                + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                                + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 35px;\">\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + ("C".equalsIgnoreCase(checkNull(siMasterVO.getTempunit())) ? "Celsius" : ("F".equalsIgnoreCase(checkNull(siMasterVO.getTempunit())) ? "Fahrenheit" : "")) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Temp Unit\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n"
                                + "\n"
                                + "                                    <td style=\"width: 25%;font-size:14px;margin:0;padding: 9px;vertical-align:middle;\">\n"
                                + "\n"
                                + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                + "                                            <span>" + checkNull(siMasterVO.getVentilation()) + ""
                                + "                                            </span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">" + Ventilationlabel + "\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    }
                    output += "                                </tr>\n";

                    if (cntList != null && cntList.size() > 0) {
                        for (int j = 0; j < cntList.size(); j++) {
                            SIContainerDetailVO siCntDetVo = (SIContainerDetailVO) cntList.get(j);
                            output += "<tr>\n"
                                    + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                                    + "                                    <td colspan=\"4\"><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td>\n"
                                    + "                                </tr>\n"
                                    + "                                \n"
                                    + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                                    + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 26px;\">\n"
                                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                    + "                                            <span>" + checkNull(siCntDetVo.getStrQty()) + "</span>\n"
                                    + "                                        </p>\n"
                                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">" + "No.of Packages" + "\n"
                                    + "                                        </span>\n"
                                    + "                                    </td>\n"
                                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 35px;\">\n"
                                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                    + "                                            <span>" + checkNull(siCntDetVo.getStrPkg()) + "</span>\n"
                                    + "                                        </p>\n"
                                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">" + "Package" + "\n"
                                    + "\n"
                                    + "                                        </span>\n"
                                    + "                                    </td>\n"
                                    + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                    + "                                            <span>" + siCntDetVo.getStrVol() + " " + (("F".equalsIgnoreCase(siCntDetVo.getStrVut())) ? "Cubic Feet" : (("M".equalsIgnoreCase(siCntDetVo.getStrVut())) ? "Cubic Meter" : "")) + "</span>\n"
                                    + "                                        </p>\n"
                                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Volume\n"
                                    + "                                        </span>\n"
                                    + "                                    </td>\n"
                                    + "\n"
                                    + "                                    <td style=\"width: 25%;font-size:14px;margin:0;padding: 9px;vertical-align:middle;\">\n"
                                    + "\n"
                                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                    + "                                            <span>" + checkNull(siCntDetVo.getStrWgt()) + " " + unitlis.get(checkNull(siCntDetVo.getStrUnit())) + "</span>\n"
                                    + "                                        </p>\n"
                                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Cargo Weight\n"
                                    + "                                        </span>\n"
                                    + "                                    </td>\n"
                                    + "                                </tr>\n"
                                    + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                                    + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\">  </td>\n"
                                    + "\n"
                                    + "                                    <td colspan='2' style=\"font-size:14px;margin:0;padding: 10px;vertical-align:middle;padding-left: 26px;\">\n"
                                    + "\n"
                                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                    + "                                            <span>" + siCntDetVo.getStrCommodity() + "</span>\n"
                                    + "                                        </p>\n"
                                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Commodity\n"
                                    + "                                        </span>\n"
                                    + "                                    </td>\n"
                                    + "                                    <td colspan='2' style=\"font-size:14px;margin:0;padding: 0px;vertical-align:middle;\">\n"
                                    + "\n"
                                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0;padding-left: 26px;\">\n"
                                    + "                                            <span>" + siCntDetVo.getHsCodeDesc() + " ~~ " + siCntDetVo.getHsCode() + "</span>\n"
                                    + "                                        </p>\n"
                                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0;padding-left: 26px;\">" + hsCodelbl + "\n"
                                    + "                                        </span>\n"
                                    + "                                    </td>\n"
                                    + "\n"
                                    + "                                </tr>\n"
                                    + "                                <tr style=\"margin:0;padding:0;line-height: 15px;\">\n"
                                    + "                                    <td style=\"font-size:14px;margin:0;padding:10px;\"> </td>\n"
                                    + "                                    <td colspan='1' style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 26px;\">\n"
                                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                    + "                                            <span>" + checkNull(siCntDetVo.getStrMrk()) + "</span>\n"
                                    + "                                        </p>\n"
                                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Marks No.\n"
                                    + "\n"
                                    + "                                        </span>\n"
                                    + "                                    </td>\n"
                                    + "                                    <td colspan='2' style=\"font-size:14px;margin:0;padding: 9px;padding-left: 35px;vertical-align:middle;\">\n"
                                    + "\n"
                                    + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                                    + "                                            <span>" + checkNull(siCntDetVo.getStrDes()) + "</span>\n"
                                    + "                                        </p>\n"
                                    + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Commodity Desc.\n"
                                    + "                                        </span>\n"
                                    + "                                    </td>\n"
                                    + "\n"
                                    + "\n"
                                    + "                                </tr>\n";
                        }
                    }
                    output += "<tr> <td colspan='6'><hr></td></tr>\n";
                    sno++;
                }
            }
            output += "</tbody>\n"
                    + "                        </table>\n";

            //Charges
            List chargelis = (List) parties_routing.getChargeDetails();
            if ("Y".equalsIgnoreCase(SHOW_CHARGES_IN_SIBL)) {
                output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;\">\n"
                        + "\n"
                        + "<tbody>\n"
                        + "\n"
                        + "<tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                        + "<td colspan='8'><h2> Charges </h2></td>\n"
                        + "</tr>\n"
                        + "\n"
                        + "<tr style=\"margin:0;padding:0;font-weight: bold;text-align:center\">\n"
                        + "<td colspan='8'>\n"
                        + "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px;\">\n"
                        + "<thead style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                        + "<tr>\n"
                        + "<th>S.NO.</th>\n"
                        + "<th>Charge Description</th>\n"
                        + "<th>Frieght</th>\n"
                        + "<th>Payor</th>\n"
                        + "<th>Location</th>\n"
                        + "<th>Eqp.type</th>\n"
                        + "</tr>\n"
                        + "</thead>\n"
                        + "<tbody style=\"font-size:14px;vertical-align:middle;font-weight:700;\">";
                ChargesVo chargevo = null;
                if (chargelis != null && chargelis.size() > 0) {
                    for (int j = 0; j < chargelis.size(); j++) {
                        chargevo = (ChargesVo) chargelis.get(j);

                        output += "<tr>				\n"
                                + "<td>" + (j + 1) + "</td>\n"
                                + "<td>" + chargevo.getChargename() + "</td>\n"
                                + "<td>" + chargemap.get(chargevo.getFprepaidcollect()) + "</td>\n"
                                + "<td>" + chargemap.get(chargevo.getCusttypeid()) + "</td>\n"
                                + "<td>" + chargevo.getPayagencyid() + "</td>\n"
                                + "<td>" + chargevo.getEqptypeid() + "</td>\n"
                                + "</tr>";
                    }
                }

                output += "</tbody>\n"
                        + "</table>\n"
                        + "</td>\n"
                        + "</tr>\n"
                        + "\n"
                        + "<tr style=\"margin:0;padding:0;font-weight: bold;text-align:center\">\n"
                        + "<td colspan='8'>\n"
                        + "<div style=\"border-top: 1px dashed #9a8a8a;\"></div>\n"
                        + "</td>\n"
                        + "</tr>\n"
                        + "\n"
                        + "<tr style=\"margin:0;padding:0;font-weight: bold;\">\n"
                        + "<td colspan='8'>\n"
                        + "<span style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 26px;\"> Release Type :  </span>\n"
                        + "<span style=\"font-size:14px;vertical-align:middle;font-weight:700;\"> " + parties_routing.getReleasetype() + " </style>\n"
                        + "</td>\n"
                        + "</tr>\n";
                String nvoccChargeVal = checkNull(parties_routing != null ? parties_routing.getEbook_nvocc() : null);
                if (nvoccChargeVal.length() == 0 && siOBJ != null) {
                    nvoccChargeVal = checkNull(siOBJ.getEbook_nvocc());
                }
                if ("Y".equalsIgnoreCase(nvoccChargeVal) || "N".equalsIgnoreCase(nvoccChargeVal)) {
                    output += "<tr style=\"margin:0;padding:0;font-weight: bold;\">\n"
                            + "<td colspan='8'>\n"
                            + "<span style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle;padding-left: 26px;\"> Automated NVOCC Master BL :  </span>\n"
                            + "<span style=\"font-size:14px;vertical-align:middle;font-weight:700;\"> " + ("Y".equalsIgnoreCase(nvoccChargeVal) ? "Yes" : "No") + " </span>\n"
                            + "</td>\n"
                            + "</tr>\n";
                }
                output += "\n"
                        + "<tr style=\"margin:0;padding:0;font-weight: bold;text-align:center\">\n"
                        + "<td colspan='8'>\n"
                        + "<hr>\n"
                        + "</td>\n"
                        + "</tr>\n"
                        + "\n"
                        + "</tbody>\n"
                        + "</table>";
            }
            //ENS 
            ENSDeclarantVo edvo = parties_routing.getEnsdecvo();
            if ("Y".equalsIgnoreCase(GENS) && edvo != null && checkNull(edvo.getEnsFillingType()).length() > 0) {
                output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:#fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                        + "                            <tbody>\n"
                        + "                                <tr style=\"margin:0;padding:0;font-weight:bold;text-decoration:underline;text-align:center\">\n"
                        + "                                    <td colspan=\"6\">\n"
                        + "                                        <h2>ENS Customer Declaration</h2>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                        + "                                            <span>" + ("Y".equalsIgnoreCase(checkNull(edvo.getEuCustZone())) ? "Yes" : "No") + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                        + "                                            <span>Is cargo to be delivered to EU Customs Zone</span>\n"
                        + "                                        </span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                            <span>" + checkNull(edvo.getBlTypeENS()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Type of Bill of Lading</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                             <span>" + checkNull(edvo.getEnsFillingType()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Type of ENS Filing</span>\n"
                        + "                                    </td>\n"
                        + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                        + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                           <span>" + checkNull(edvo.getPaymentMethod()) + "</span>\n"
                        + "                                        </p>\n"
                        + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Method of Payment</span>\n"
                        + "                                    </td>\n"
                        + "                                </tr>"
                        + "                                <tr>\n"
                        + "                                    <td colspan=\"6\"><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td>\n"
                        + "                                </tr>";

                if ("Multiple Filing".equalsIgnoreCase(checkNull(edvo.getEnsFillingType()))) {
                    output += "<tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:left\">\n"
                            + "                                    <td colspan='6'><h4>Multiple Filing - Supplementary Declarant details</h4></td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantName()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Name</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantAddress()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantAddress1()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address1</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantCity()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">City</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantCountry()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Country</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantState()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">State</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantTelephone()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Telephone</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantEmail()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Email</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantZipcode()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Zipcode</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantEORI()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>EORI</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(edvo.getDeclarantFillingType()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Filing Type</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>";
                }
                if ("Single Filing".equalsIgnoreCase(checkNull(edvo.getEnsFillingType()))) {
                    //Buyer
                    output += "<tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:left\">\n"
                            + "                                    <td colspan='6'><h4>Buyer</h4></td>\n"
                            + "                                </tr>\n"
                            + "								<tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getByname()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Name</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getByaddr()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getByaddr1()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address1</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getBycity()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">City</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getByCountryName()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Country</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getByphone()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Telephone</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getByemail()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Email</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getBystate()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">State</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "				        <tr style=\"margin:0;padding:0;line-height:15px;\">\n";
                    if (checkNull(siOBJ.getByfax()).length() > 0) {
                        output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                                + "                                            <span>" + checkNull(siOBJ.getByfax()) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                                + "                                            <span>Fax</span>\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    }
                    output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getByzipcode()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Zipcode</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getByeori()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">EORI</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getBypersontype()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Type of Person</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n";
                    //Seller
                    output += "<tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:left\">\n"
                            + "                                    <td colspan='6'><h4>Seller</h4></td>\n"
                            + "                                </tr>\n"
                            + "								<tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getSlname()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Name</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getSladdr()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getSladdr1()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Address1</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getSlcity()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">City</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "                                <tr style=\"margin:0;padding:0;line-height:15px;\">\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getSlCountryName()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                            + "                                            <span>Country</span>\n"
                            + "                                        </span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getSlphone()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Telephone</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getSlemail()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Email</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getSlstate()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">State</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n"
                            + "				      <tr style=\"margin:0;padding:0;line-height:15px;\">\n";
                    if (checkNull(siOBJ.getSlfax()).length() > 0) {
                        output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                                + "                                        <p style=\"font-weight:bold;margin:0 0 5px;padding:0;text-transform:capitalize\">\n"
                                + "                                            <span>" + checkNull(siOBJ.getSlfax()) + "</span>\n"
                                + "                                        </p>\n"
                                + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">\n"
                                + "                                            <span>Fax</span>\n"
                                + "                                        </span>\n"
                                + "                                    </td>\n";
                    }
                    output += "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getSlzipcode()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Zipcode</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getSleori()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">EORI</span>\n"
                            + "                                    </td>\n"
                            + "                                    <td style=\"width:25%;font-size:14px;margin:0;padding:10px;vertical-align:middle\">\n"
                            + "                                        <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                            + "                                            <span>" + checkNull(siOBJ.getSlpersontype()) + "</span>\n"
                            + "                                        </p>\n"
                            + "                                        <span style=\"font-size:12px;color:#999;margin:0;padding:0\">Type of Person</span>\n"
                            + "                                    </td>\n"
                            + "                                </tr>\n";

                }
                output += "<tr style=\"margin:0;padding:0;font-weight: bold;text-align:center\">\n"
                        + "                                    <td colspan='8'>\n"
                        + "                                        <hr>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                            </tbody>\n"
                        + "                        </table>";
            }

            output += "<table style=\"overflow:visible;text-align:left;font-variant:normal;font-weight:normal;font-size:14px;background-color:fff;line-height:15px;font-family:Asap,sans-serif;color:#333;padding:0;font-style:normal;width:900px\">\n"
                    + "\n"
                    + "\n"
                    + "                                <tbody>\n"
                    + "\n"
                    + "                                    <tr style=\"margin:0;padding:0;font-weight: bold;text-decoration:underline;text-align:center\">\n"
                    + "                                        <td colspan='6'><h2>Additional Information</h2></td>\n"
                    + "                                    </tr>\n"
                    + "                                    <tr style=\"margin:0;padding:0\">\n"
                    + "                                        <td style=\"font-size:14px;margin:0;padding:10px;vertical-align:middle;width:20%\">\n"
                    + "                                            <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Additional Information\n"
                    + "                                            </p>\n"
                    + "                                        </td>\n"
                    + "                                        <td style=\"font-size:14px;margin:0;padding:10px;vertical-align:middle;width:80%\">\n"
                    + "                                            <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                    + "                                                <span>" + siOBJ.getSicustremarks() + "</span>\n"
                    + "                                            </p>\n"
                    + "\n"
                    + "                                        </td></tr>\n"
                    + "                                       <tr><td colspan=\"4\"><div style=\"border-top: 1px dashed #9a8a8a;\"></div></td></tr>\n";
            if (parties_routing.getDeclaredValue() != null && parties_routing.getDeclaredValue().length() > 0) {
                output += "                                    <tr style=\"margin:0;padding:0\">\n"
                        + "                                        <td style=\"font-size:14px;margin:0;padding:10px;vertical-align:middle;width:20%\">\n"
                        + "                                            <p style=\"font-weight:700;margin:0 0 5px;padding:0\">Declared Value \n"
                        + "                                            </p>\n"
                        + "                                        </td>\n"
                        + "                                        <td style=\"font-size:14px;margin:0;padding:10px;vertical-align:middle;width:80%\">\n"
                        + "                                            <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                                <span>" + parties_routing.getDeclaredValue() + "</span>\n"
                        + "                                            </p>\n"
                        + "\n"
                        + "                                        </td></tr>\n";
            }
            if (parties_routing.getAESNumber() != null && parties_routing.getAESNumber().length() > 0) {
                output += "                                    <tr style=\"margin:0;padding:0\">\n"
                        + "                                        <td style=\"font-size:14px;margin:0;padding:10px;vertical-align:middle;width:20%\">\n"
                        + "                                            <p style=\"font-weight:700;margin:0 0 5px;padding:0\">AES Number \n"
                        + "                                            </p>\n"
                        + "                                        </td>\n"
                        + "                                        <td style=\"font-size:14px;margin:0;padding:10px;vertical-align:middle;width:80%\">\n"
                        + "                                            <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                                <span>" + parties_routing.getAESNumber() + "</span>\n"
                        + "                                            </p>\n"
                        + "\n"
                        + "                                        </td></tr>\n";
            }
            if (parties_routing.getAESDiscliamer() != null && parties_routing.getAESDiscliamer().length() > 0) {
                output += "                                    <tr style=\"margin:0;padding:0\">\n"
                        + "                                        <td style=\"font-size:14px;margin:0;padding:10px;vertical-align:middle;width:20%\">\n"
                        + "                                            <p style=\"font-weight:700;margin:0 0 5px;padding:0\">AES Disclaimer\n"
                        + "                                            </p>\n"
                        + "                                        </td>\n"
                        + "                                        <td style=\"font-size:14px;margin:0;padding:10px;vertical-align:middle;width:80%\">\n"
                        + "                                            <p style=\"font-weight:700;margin:0 0 5px;padding:0\">\n"
                        + "                                                <span>" + parties_routing.getAESDiscliamer() + "</span>\n"
                        + "                                            </p>\n"
                        + "\n"
                        + "                                        </td></tr>\n";
            }
            output += "                                    <tr> <td colspan='6'><hr></td></tr>\n"
                    + "\n"
                    + "                                </tbody>\n"
                    + "                            </table>\n";

            output += " </td>\n"
                    + "                </tr>\n"
                    + "            </tbody>\n"
                    + "        </table>		\n"
                    + "    </body>\n"
                    + "</html>\n";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    private static String checkNull(String value) {
        return (value != null && (!value.equalsIgnoreCase("")) && (!value.equalsIgnoreCase("null"))) ? value.trim() : "";
    }
}
