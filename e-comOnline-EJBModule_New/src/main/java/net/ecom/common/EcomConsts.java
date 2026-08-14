/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.util.HashMap;

/**
 *
 * @author vasanthakumar.r
 */
public class EcomConsts {

    public static final String REG_EXPIRE_MSG = "YOUR LOGIN IS ALREADY EXPIRED.</br></br>PLEASE CONTACT AGENCY USER.";
    public static final String REG_BLOCK_MSG = "YOUR LOGIN IS ALREADY LOCKED.</br></br>PLEASE CONTACT AGENCY USER.";
    public static final String REG_INVALID_USER_MSG = "INVALID USERNAME / PASSWORD";
    public static final String REG_VALID_USER_MSG = "VALID";
    public static final String WS_ERROR_MESSAGE = "Error while retrieving information from server";
    public static final String WS_CONNECTION_ERROR = "Connection to Server Failed";
    public static final String NO_RECORD = "No Record Found";
    public static final String Error_cust = "Error while saving";
    public static final String VERFYANDAPPROVE = "This Customer has been already Updated";
    public static final String SuccessMsg = "Successfully Updated";
    public static final String ACCP_VALIDATION = "Transaction is available for this login id";
    public static final String NO_RECORD_FOR_COAMPANY = "Company details is not available";
    public static final String TRACKING_NO_RECORD = "Error! No results found. Please enquire again or contact our nearest agent office for assistance.";
    public static final String EDI_SI_PREFIX = "EDIMIN";
    public static final String EDI_BKG_PREFIX = "EDIMBF";
    public static final String INVALID_URL = "Invalid request. Please try again later!";
    public static final String EDI_SERVER_FAIL = "Connection to Server Failed";
    public static final String SERVER_RETRIVE_ERROR = "Error while retrieving information from server";
    public static final String EDI_FTP_ERROR = "File Not available in ftp path";
    public static final String BL_INVALID_VAL = "This BL is already Issued. Hence you cannot print this BL.";
    //
    public static final String LEG_TYPE_FEEDER = "Feeder";
    public static final String LEG_TYPE_MAINLINE = "Mainline";
    public static final String LEG_TYPE_RAIL = "Rail";
    public static final String LEG_TYPE_ROAD = "Road";
    public static final String LEG_TYPE_COMBINED = "Combined";
    public static final String LEG_TYPE_BARGE = "Barge";
    public static final String LEG_TYPE_ITT = "ITT";

    //Registration
    public static final String NEW_CUSTOMER_TYPE = "N";
    public static final String NEW_CUSTOMER_ISVALID = "I";
    public static final String NEW_CUSTOMER_STATUS = "A";
    public static final String NEW_CUSTOMER_ONLINESTATUS = "N";

    public static final String NEW_CUSTOMER_REG_HISTORY_REMARKS = "Customer Registered";
    public static final String NEW_CUSTOMER_REG_HISTORY_STATUS = "I";
    public static final String NEW_CUSTOMER_REG_HISTORY_TYPE = "C";

    public static final String REG_MSG_NEWCUST_MODULE = "Registration";
    public static final String REG_MSG_NEWCUST_CRITERIA = "Insert";
    public static final String REG_MSG_NEWCUST_USERTYPE = "C";
    
    public static final String REG_MSG_EXISTINGCUST_MODULE = "Registration";
    public static final String REG_MSG_EXISTINGCUST_CRITERIA = "existsuser";
    public static final String REG_MSG_EXISTINGCUST_USERTYPE = "C";
    
    public static final String REG_MSG_NEWCUST_CUSTOMER = "@customer";
    public static final String REG_MSG_NEWCUST_CLIENTID = "@client";

    public static final String NEW_CUSTOMER_ACTIVE_LINK_HISTORY_REMARKS = "Customer Activate Link Clicked";
    public static final String NEW_CUSTOMER_ACTIVE_LINK_HISTORY_STATUS = "C";
    public static final String NEW_CUSTOMER_ACTIVE_LINK_HISTORY_TYPE = "C";

    //Booking File type
    public static final String FILETYPE_VMG = "VGM";
    public static final String FILETYPE_DGC = "DGC";
    public static final String FILETYPE_MSD = "MSD";
    public static final String FILETYPE_OTHERS = "OT";
    public static final String SET_MESSAGE_SENDER = "EcomOnlineBkg";
    public static String SET_CARRIER_CODE = "ESL";
    public static final String SET_BOOK_TYPE = "online";
    public static final String SET_CREATER_USER = "ECOM";
    public static final String SET_BOOK_SOURCE = "C";
    public static final String VGM_FILE_UPLOAD = "VGM";

    public static final String SET_CUSTTYPE_AP = "AP";
    public static final String SET_CUSTTYPE_SH = "SH";
    public static final String SET_CUSTTYPE_CN = "CN";
    public static final String SET_CUSTTYPE_NT = "NT";
    public static final String SET_CUSTTYPE_N1 = "N1";

    //SI File Type
    public static final String FILETYPE_VGM = "VGM";
    public static final String FILETYPE_DG = "DGC";
    public static final String FILETYPE_LOL = "LOL";
    public static final String FILETYPE_MSDS = "MSDS";

    //
    public static final String ONLINE_BOOKING = "EDI109";
    public static final String ONLINE_SI = "EDI110";
    public static final String EXPORT_REF = "ERF";
    public static final String CUST_PO = "SHR";
    public static final String REF_TYPE = "ERT";
    public static final String CUST_REF = "CRN";
    public static final String ACID = "ACI";
    public static final String Declared_Value = "EDV";
    public static final String BLType = "EBL";
    public static final String Freight = "FRE";
    public static final String packinglst = "PAC";
    public static final String invoiceupload = "INU";
    public static final String orgTelType = "ORG";
    public static final String sisubmit = "SPC";
    public static final String bltype_UAE = "MB1";
    public static final String mpci_id = "MB2";
    
    
    public static final HashMap dayMap;
    
    static {
        dayMap = new HashMap();
        dayMap.put("MONDAY", 1);
        dayMap.put("TUESDAY", 2);
        dayMap.put("WEDNESDAY", 3);
        dayMap.put("THURSDAY", 4);
        dayMap.put("FRIDAY", 5);
        dayMap.put("SATURDAY", 6);
        dayMap.put("SUNDAY", 7);
    }
    
}
