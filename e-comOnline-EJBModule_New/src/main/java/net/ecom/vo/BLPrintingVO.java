/*
 * BatchPrintingVO.java
 *
 * Created on June 21, 2006, 10:16 AM
 */

package net.ecom.vo;

/**
 * @author  arjun
 */
public class BLPrintingVO implements java.io.Serializable{
    
    /** Creates a new instance of BatchPrintingVO */
    public String blNo;
    public String bookNo;
    public String forwarderName;
    public String forwarderAddress;
    public String remarks;
    public String shipperName;
    public String shipperAddress;
    public String consigneeName;
    public String consigneeAddress;
    public String initialCarriage;
    public String loadVSL;
    public String portofLoad;
    public String portofDestination;
    public String finalDestination;
    public String routing;
    public String marksNo;
    public String descriptionofPackages;
    public String grossWeight;
    public String freightPayableAt;
    public String placeOfIssue;
    public String notifyName;
    public String notifyAddress;
    public String placeofReciept;
    public String voyage;
    public String measurement;
    public String documentAgency;
    public String service;
    public String footerText;
    public String clauses;
    public String exportReferences;
    public String dateofIssue;
    
    //For Footer Details
    public String footerDesc;
    public String chargeDesc;
    public String rate;
    public String prepaid;
    public String collect;
    public String payAt;
    public String originofCountry;
    //For Footer Details
    public int noofOriginals;
    public String securityNo;
    public String originals_word;
    public String reference;
    //public String freightpayableAt;
    public String shipperdeclaration;
    public String bltype;
    public String printtype;
    public int noofCopies;
    public String containerno;  
    public String sysbltype;
    public String shipmenttype ;
    public String vslvog;
    public String sob;
    public String rightof;
    public int sh_fontsize;
    public int cn_fontsize;
    public int nt_fontsize;
    public String bldateofIssue;   
    
    
    //for  US LD
    
    public String FRTchargeDesc;
    public String FRTrate;
    public String FRTprepaid;
    public String FRTcollect;
    public String FRTpayAt;

    public String linevessel;

    public String chargeId;

    public String chargeqty;

    public String prepaidcollect;

    public String chargeamount;

    public String currencyid;

    public String baseRateUnit;

    public String baserate;

    public String eqp;

    public String chargename;

    public String countrycode;

    public String custTypeId;

    public String name;

    public String address1;

    public String address2;

    public String POBox;

    public String city;

    public String CountryName;

    public String zipCode;

    public String telephone;

    public String fax;

    public String faxc0;

    public String telephonec0;

    public String zipCodec0;

    public String CountryNamec0;

    public String cityc0;

    public String POBoxc0;

    public String address2c0;

    public String address1c0;

    public String namec0;

    public String a1;

    public String a2;

    public String blno;

    public String Tarifflg;

    public String svccode;

    public String eqpTypeId;

    public String ratevaliditydate;

    public String dischargePortId;

    public String loadPortId;

    public String vslname;

    public String voybnd1;

    public String vslname1;

    public String voybnd;
       
    public String Plugin;

    public String containerno1;

    public String port;

    public String payIndicator;
    public BLPrintingVO(){
        blNo = new String();
        bookNo = new String();
        forwarderName = new String();
        forwarderAddress = new String();
        remarks = new String();
        shipperName = new String();
        shipperAddress = new String();
        consigneeName = new String();;
        consigneeAddress = new String();
        initialCarriage = new String();
        loadVSL = new String();
        portofLoad = new String();
        portofDestination = new String();
        routing = new String();
        finalDestination = new String();
        marksNo = new String();
        descriptionofPackages = new String();
        grossWeight = new String();
        notifyName = new String();
        notifyAddress = new String();
        voyage = new String();
        measurement = new String();
        documentAgency = new String();
        service = new String();
        footerText = new String();
        clauses = new String();
        placeOfIssue = new String();
        placeofReciept = new String();
        exportReferences = new String();
        dateofIssue = new String();

        footerDesc = new String();
        chargeDesc = new String();
        rate = new String();
        prepaid = new String();
        collect = new String();
        payAt = new String();
        originofCountry = new String();
        
        noofOriginals = 0;
        securityNo = new String();
        
        originals_word = new String();
        reference = new String();
        
        freightPayableAt = new String();
        shipperdeclaration = new String();
        bltype = new String();
        printtype = new String();
        noofCopies = 0;
        containerno = new String();
        sysbltype = new String();
        shipmenttype = new String();

        vslvog = new String();
        sob = new String();
        rightof = new String();
        sh_fontsize = 0;
        cn_fontsize = 0;
        nt_fontsize = 0;
        
        bldateofIssue = new String();
        
        FRTchargeDesc = new String();
        FRTrate  = new  String();
        FRTprepaid = new String();
        FRTcollect = new String();
        FRTpayAt = new String();
                
    }    
}
