<%@page import="net.ecom.vds.serviceVo.BLChargeVo"%>
<%@page contentType="text/html; charset=UTF-8" import="net.ecom.common.GeneralConf,java.util.ArrayList, java.util.HashMap,java.util.Set,java.util.Hashtable,net.ecom.vo.SIDtableVO, net.ecom.vo.SIPartiesVO,net.ecom.vo.SIContainerVO,java.util.List,net.ecom.common.Validation,java.util.Map,net.ecom.vo.SIContainerDetailVO,net.ecom.vo.SIMasterVO,net.ecom.vo.SISealVO,net.ecom.vo.SIConatainerValueVO,net.ecom.vo.CustomerDetailsVO,net.ecom.common.utils.DateConversion_New,net.ecom.common.NumberFormat,net.ecom.vo.ChargesVo"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
    session.setAttribute("SISEND", "SISend");
    long jsdate = new java.util.Date().getTime();
    String type12 = ((String) session.getAttribute("BLtype"));
    String blno = (String) session.getAttribute("edit_blno") != null ? (String) session.getAttribute("edit_blno") : "";
    String sino_new = (String) session.getAttribute("edit_sino") != null ? (String) session.getAttribute("edit_sino") : "";
    String errors_new = (String) request.getAttribute("sierrors") != null ? (String) request.getAttribute("sierrors") : "";
    String agencyRefNo = (String) request.getAttribute("SIAGENCYREFNO") != null ? (String) request.getAttribute("SIAGENCYREFNO") : "";
    String user = (String) session.getAttribute("sessionAdmin");
    List apList = (List) session.getAttribute("APCUSTLIST");
    List shList = (List) session.getAttribute("SHCUSTLIST_BL");
    List cnList = (List) session.getAttribute("CNCUSTLIST_BL");
    List ntList = (List) session.getAttribute("NTCUSTLIST_BL");
    List fwList = (List) session.getAttribute("FWCUSTLIST_BL");
    List whList = (List) session.getAttribute("WHCUSTLIST_BL");
    String CommoAcceptEngOnly = (String) getServletContext().getAttribute(GeneralConf.CommodityDescInOnlyENG);
    String DispConsigToOrder = (String) getServletContext().getAttribute(GeneralConf.DisplayConsigneeToOrder);
    String printOnBlAdd = (String) getServletContext().getAttribute(GeneralConf.PrintOnBlAddMandatory);
    List custtypelist = (List) getServletContext().getAttribute(GeneralConf.CUSTTYPECONFIG);
    String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);
    String enableaesno = (String) getServletContext().getAttribute(GeneralConf.ENABLE_AES_NUMBER);
    String ccMailId = Validation.checkNull((String) session.getAttribute("SESSION_USER_CC_MAILID"));
    Hashtable comlist = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
    Hashtable packagemap = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
    String CommodityName_as_HS_Chapter = (String) getServletContext().getAttribute(GeneralConf.CommodityName_as_HS_Chapter);
    String clientcode = (String) session.getAttribute("clientcode");
    String Ventilationlabel = (String) getServletContext().getAttribute("Ventilation");
    String pdfpath = (String) getServletContext().getAttribute("Imagepath");
    CustomerDetailsVO custVO = null;
    String booknum = (String) session.getAttribute("edit_bookno");
    String bookstatus = (String) session.getAttribute("bookstatus");
    String sino = "";
    if ((String) session.getAttribute("edit_sino") != null) {
        sino = GeneralConf.SI_PREFIX + (String) session.getAttribute("edit_sino");
    }
    String weight_unit_kilos_only = (String) getServletContext().getAttribute(GeneralConf.WEIGHT_UNIT_KILOS_ONLY);
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String carrierActualOptional = (String) getServletContext().getAttribute(GeneralConf.CARRIER_SEAL_CONTAINER_OPTIONAL);
    String exceluploadpath = (String) getServletContext().getAttribute(GeneralConf.exceluploadpath);
    String headerConfig = (String) request.getAttribute("HEADERCONFIGURATION");
    String hscodeconfig = (String) request.getAttribute("HSCODECONFIGURATION");
    String siAppVersion = (String) request.getAttribute("EDITBLAPPVERSION");
    String shipperSealconfig = (String) session.getAttribute("SHIPPERSEALCONFIG");
    String v1DataEnableStatus = (String) getServletContext().getAttribute(GeneralConf.V1DATAENABLESTATUS);
    String fakCommodity = (String) getServletContext().getAttribute(GeneralConf.FAKCOMMODITY);
    String fakComdtyCode = "";
    String fakComdtyName = "";
    System.out.println("[ECOM-WAR] BLEdit fakCommodity>>>>" + fakCommodity);
    fakCommodity = fakCommodity != null ? fakCommodity : "";
    if (fakCommodity != null && fakCommodity.length() > 0) {
        String fakCommodityArr[] = fakCommodity.split("~~");
        if (fakCommodityArr.length == 2) {
            fakComdtyCode = fakCommodityArr[0].trim();
            fakComdtyName = fakCommodityArr[1].trim();
        }
    }
    Map<String, String> mailAlertMap = (Map<String, String>) session.getAttribute("USERMAILALERTSESSIONMAP");
    String checkMailConfig = "N";
    Map<String, List<String>> map = (Map<String, List<String>>) getServletContext().getAttribute("MAILALERTACTIVITYLIST");
    String termsandconditions = (String) getServletContext().getAttribute("Terms_and_Conditions");
    if (map != null) {
        List<String> mailActList = map.get("BKG");
        System.out.println("[ECOM-WAR] BLEdit mailActList>>>" + mailActList + " mailAlertMap>>>" + mailAlertMap);
        if (mailActList != null && mailAlertMap != null) {
            for (int i = 0; i < mailActList.size(); i++) {
                System.out.println("[ECOM-WAR] BLEdit mailAlertMap.get(mailActList.get(i))>>" + mailAlertMap.get(mailActList.get(i)));
                if ("Y".equalsIgnoreCase(mailAlertMap.get(Validation.checkNull(mailActList.get(i)).trim()))) {
                    checkMailConfig = "Y";
                }
            }
        }
    }
    List EBK_pluginList = (List) getServletContext().getAttribute("EBK_PLUGIN_DETAILS");
    boolean is_SI_restriction = false, is_SI_restriction_cust = false;
    if (EBK_pluginList != null && EBK_pluginList.contains("ESP")) {
        is_SI_restriction = true;
    }
    if (EBK_pluginList != null && EBK_pluginList.contains("ESC")) {
        is_SI_restriction_cust = true;
    }
    List addInfoList = (List) getServletContext().getAttribute(GeneralConf.AdditionalInfoBkg);
    boolean is_SI_Addinfo = false;
    if (addInfoList != null && addInfoList.contains("ACD")) {
        is_SI_Addinfo = true;
    }
    String agencyemailid = (String) request.getAttribute("agencyemailid");
    String loadportcountry = (String) request.getAttribute("LoadportCountry");
    String SHOW_CHARGES_IN_SIBL = (String) getServletContext().getAttribute(GeneralConf.SHOW_CHARGES_IN_SIBL);
    List Chargedetails = (List) request.getAttribute("CHARGEDETAILS");
    String Enable_Customize_Import_Export_Excel = (String) getServletContext().getAttribute(GeneralConf.Enable_Customize_Import_Export_Excel);
    String ENABLE_INSURANCE = (String) getServletContext().getAttribute(GeneralConf.ENABLE_INSURANCE);
    String insuranceaccess = (String) session.getAttribute("SESSION_InsuranceAccess");
    String Enable_BLType_Selection = (String) getServletContext().getAttribute(GeneralConf.Enable_BLType_Selection);
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
    String ENABLE_ECOMOOG_DETAILS = (String) getServletContext().getAttribute(GeneralConf.ENABLE_OOG_DETAILS);
    boolean is_SI_OOG = false;
    if (ENABLE_ECOMOOG_DETAILS != null && "Y".equalsIgnoreCase(ENABLE_ECOMOOG_DETAILS)) {
        is_SI_OOG = true;
    }
    String EnableFileBackendValidation = (String) getServletContext().getAttribute(GeneralConf.EnableFileBackendValidation);
    String fileSize = (String) getServletContext().getAttribute(GeneralConf.EcomFileUploadSize);
    String fileExt = (String) getServletContext().getAttribute(GeneralConf.EcomFileExtension);
    String BL_TypeList = (String) getServletContext().getAttribute(GeneralConf.BL_TYPE_LIST);
    String duplicateComdyVal = (String) getServletContext().getAttribute(GeneralConf.Duplicate_Commodity_Validation);
    String enableCargoProtect = (String) getServletContext().getAttribute(GeneralConf.Enable_Cargo_Protect);
    String hsnamecode = (String) getServletContext().getAttribute(GeneralConf.Display_HS_NAME_CODE);
    String openCommodityJson = (String) getServletContext().getAttribute("OpenCommodityJson");

    String cmdtyeditoption = (String) getServletContext().getAttribute(GeneralConf.SI_CommodityGroup_Edit_Option);
    String rateCmdtyJsonarr = (String) session.getAttribute("SIBLRateCmdtyJsonarr");
    String GENS = (String) getServletContext().getAttribute(GeneralConf.GENS);
    String Enable_OOGRequired_Validation = (String) getServletContext().getAttribute(GeneralConf.Enable_OOGRequired_Validation);
    String[] blTypes = null;
    if (BL_TypeList != null) {
        blTypes = BL_TypeList.split("~~");
    }
    String enableENSTab = "N";
    if ("Y".equalsIgnoreCase(GENS)) {
        enableENSTab = (String) session.getAttribute("IsEnableENSTabInSIBL");
    }
    List<BLChargeVo> Chargesummary = (ArrayList<BLChargeVo>) session.getAttribute("SIBLChargeSummary");
    String Display_Charge_Tab = (String) getServletContext().getAttribute(GeneralConf.Display_Charge_Tab);
    System.out.println("[ECOM-WAR] BLEdit Display_Charge_Tab==>" + Display_Charge_Tab);
    String EuropeCountryJson = (String) getServletContext().getAttribute("EuropeCountryJson");
    String Enable_T2LFiling = (String) getServletContext().getAttribute(GeneralConf.Enable_T2LFiling);
    String Enable_NVOCC_In_SI = (String) getServletContext().getAttribute(GeneralConf.Enable_NVOCC_In_SI);
    String SI_NVOCC_Eligible_Country = (String) getServletContext().getAttribute(GeneralConf.SI_NVOCC_Eligible_Country);
    String enbleafricaReqsMandatory = (String) getServletContext().getAttribute(GeneralConf.Enable_africaReqsMandatory);
    String Enable_ReleaseType = (String) getServletContext().getAttribute(GeneralConf.Enable_Telex_ReleaseType);
    String TENS_Documentation = (String) getServletContext().getAttribute(GeneralConf.TENS_Documentation);
    String africaReqsContryIdJson = (String) getServletContext().getAttribute("africaReqsContryIdJson");
    String dischargeportcountry = (String) request.getAttribute("dischargeportcountry");
    String Enable_Zip_Code = (String) getServletContext().getAttribute(GeneralConf.Enable_Zip_Code);
    System.out.println("[ECOM-WAR] BLEdit Enable_Zip_Code==>" + Enable_Zip_Code);
    boolean showNvocc = false;
    if ("Y".equalsIgnoreCase(Enable_NVOCC_In_SI) && dischargeportcountry != null && SI_NVOCC_Eligible_Country != null) {
        for (String cty : SI_NVOCC_Eligible_Country.split(",")) {
            if (dischargeportcountry.equalsIgnoreCase(cty.trim())) {
                showNvocc = true;
                break;
            }
        }
    }


%>
<%! int tablerowcount = 0;

    private String checkNull(String str) {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
%>


<script type="text/javascript" src="/Ecommaster/commodity.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/packages.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/state.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/country.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/equipment.js?<%=jsno%>"></script>
<%if ("Y".equalsIgnoreCase(v1DataEnableStatus)) {%>
<script type="text/javascript" src="/Ecommaster/v1commodity.js?<%=jsno%>"></script>
<%}%>
<script type="text/javascript">
    var checkMailConfig = '<%=checkMailConfig%>';
    var shipperSealconfig = '<%=shipperSealconfig%>';
    var v1DataEnableStatus = '<%=v1DataEnableStatus%>';
    var commodityJson = commodityJson;
    var v1commodityJson;
    if (v1DataEnableStatus == 'Y') {
        v1commodityJson = v1commodityJson;
    }
    var fakComdtyCode = "<%=fakComdtyCode%>";
    var fakComdtyName = "<%=fakComdtyName%>";
    var packagesJson = packagesJson;
    var countryJson = countryJson;
    var counlist = countryJson;
    var hscodelst;
    var headerConfigScript = '<%=headerConfig%>';
    var openCommodityJson = '<%=openCommodityJson%>';
    var openCommodityJsonArray = JSON.parse(openCommodityJson);
    var europeCountryJson = JSON.parse('<%=EuropeCountryJson%>');
    var africaReqsContryIdJson = JSON.parse('<%=africaReqsContryIdJson%>');
    
    var cmdgrp = commodityJson.filter(function (item) {
        return (item["parentcode"] == "0")
    });
    var allHScode = commodityJson.filter(function (item) {
        return (item["parentcode"] != "0")
    });
    <%if ("Y".equalsIgnoreCase(cmdtyeditoption)) {%>
    var rateCommodityJson = '<%=rateCmdtyJsonarr%>';
    var rateCommodityJsonArray = JSON.parse(rateCommodityJson);
    const ratecomdtyarr = new Set(rateCommodityJsonArray.map(item => item.commoditycode));
    cmdgrp = commodityJson.filter(item => ratecomdtyarr.has(item.commoditycode));
    <%}%>
    function commodityload(comid, hiddencomid, hscodeid, hiddenhsid, comidvalue)
    {
        if ($('#appVersion').val() == '1' && false) {
            var portlist = v1commodityJson;
            var ports = new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('commodityname', 'commoditycode'),
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                local: portlist
            });
            ports.initialize();
            $("#" + comid).typeahead(
                    {
                        hint: true,
                        highlight: true,
                        minLength: 2
                    },
                    {
                        name: comid,
                        limit: 5,
                        displayKey: function (portlist) {
                            return portlist.commodityname + " ~~ " + portlist.commoditycode;
                        },
                        source: ports.ttAdapter()
                    }).on('typeahead:render', function (obj, datum) {
                $("#" + hiddencomid).val("");
            }).on('typeahead:selected', function (obj, datum) {
                $("#" + hiddencomid).val(datum.commodityname);
            }).on('typeahead:autocomplete', function (obj, datum) {
                $("#" + hiddencomid).val(datum.commodityname);
            }).on("typeahead:change", function (obj, datum) {
                var hiddenValue = $("#" + hiddencomid).val();
                if (hiddenValue == null || hiddenValue == "") {
                    $("#" + comid).typeahead('val', "");
                    $(obj.currentTarget).val("");
                }
            });
        } else {
            var portlist = cmdgrp;
            var ports = new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('commodityname', 'commoditycode'),
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                local: portlist
            });
            if (comidvalue != null && comidvalue != fakComdtyCode) {
                if (openCommodityJsonArray.includes(comidvalue)) {
                    console.log("openCommodityJsonArray");
                    hscodelst = allHScode;
                } else {
                    hscodelst = commodityJson.filter(function (item) {
                        return (item["grpcode"] == comidvalue)
                    });
                }
                $("#" + hscodeid).typeahead('val', "");
                $("#" + hscodeid).typeahead('destroy');
                hscommodityload(hscodeid, hiddenhsid, hscodelst);
            }

            ports.initialize();
            $("#" + comid).typeahead(
                    {
                        hint: true,
                        highlight: true,
                        minLength: 2
                    },
                    {
                        name: comid,
                        limit: 5,
                        displayKey: function (portlist) {
                            return portlist.commodityname + " ~~ " + portlist.commoditycode;
                        },
                        source: ports.ttAdapter()
                    }).on('typeahead:render', function (obj, datum) {
                $("#" + hiddencomid).val("");
            }).on('typeahead:selected', function (obj, datum) {
                $("#" + hiddencomid).val(datum.commoditycode);
            }).on('typeahead:autocomplete', function (obj, datum) {
                $("#" + hiddencomid).val(datum.commoditycode);
            }).on("typeahead:change", function (obj, datum) {
                var hiddenValue = $("#" + hiddencomid).val();
                if (hiddenValue != fakComdtyCode) {
                    if (openCommodityJsonArray.includes(hiddenValue)) {
                        console.log("openCommodityJsonArray");
                        hscodelst = allHScode;
                    } else {
                        hscodelst = commodityJson.filter(function (item) {
                            return (item["grpcode"] == hiddenValue)
                        });
                    }
                    $("#" + hscodeid).typeahead('val', "");
                    $("#" + hscodeid).typeahead('destroy');
                    hscommodityload(hscodeid, hiddenhsid, hscodelst);
                }
                if (hiddenValue == null || hiddenValue == "") {
                    $("#" + comid).typeahead('val', "");
                    $(obj.currentTarget).val("");
                }
            });
        }
    }
    function hscommodityload(comid, hiddenhscodeid, hscodelst)
    {
        if ($('#appVersion').val() != '1') {
            var ports = new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('commodityname', 'commoditycode'),
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                local: hscodelst
            });
            ports.initialize();
            $("#" + comid).typeahead(
                    {
                        hint: true,
                        highlight: true,
                        minLength: 2
                    },
                    {
                        name: comid,
                        limit: 5,
                        displayKey: function (hscodelst) {
                            return hscodelst.commodityname + " ~~ " + hscodelst.commoditycode;
                        },
                        source: ports.ttAdapter()
                    }).on('typeahead:render', function (obj, datum) {
                $("#" + hiddenhscodeid).val("");
            }).on('typeahead:selected', function (obj, datum) {
                $("#" + hiddenhscodeid).val(datum.commodityname);
            }).on('typeahead:autocomplete', function (obj, datum) {
                $("#" + hiddenhscodeid).val(datum.commodityname);
            }).on("typeahead:change", function (obj, datum) {
                var hiddenValue = $("#" + hiddenhscodeid).val();
                if (hiddenValue == null || hiddenValue == "") {
                    $("#" + comid).typeahead('val', "");
                    $(obj.currentTarget).val("");
                }
            });
        }
    }
    function packageload(packid, hiddenpackid)
    {
        var portlist = packagesJson;
        var ports = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('packagename', 'packagecode'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: portlist
        });
        ports.initialize();
        $("#" + packid).typeahead(
                {
                    hint: true,
                    highlight: true,
                    minLength: 2
                },
                {
                    name: packid,
                    limit: 5,
                    displayKey: function (portlist) {
                        return portlist.packagename + " ~~ " + portlist.packagecode;
                    },
                    source: ports.ttAdapter()
                }).on('typeahead:render', function (obj, datum) {
            $("#" + hiddenpackid).val("");
        }).on('typeahead:selected', function (obj, datum) {
            $("#" + hiddenpackid).val(datum.packagename);
        }).on('typeahead:autocomplete', function (obj, datum) {
            $("#" + hiddenpackid).val(datum.packagename);
        }).on("typeahead:change", function (obj, datum) {
            var hiddenValue = $("#" + hiddenpackid).val();
            if (hiddenValue == null || hiddenValue == "") {
                $("#" + packid).typeahead('val', "");
                $(obj.currentTarget).val("");
            }
        });
    }
    function setContNo(id, classname, val, qtyclass, weigthclass, volumeclass, obj) {
        $("." + classname).addClass("required");
        if (obj.checked) {
            $("." + classname).addClass("required");
            $("." + qtyclass).addClass("maxone");
            $("." + weigthclass).addClass("validatezeropoint");
            $("." + volumeclass).addClass("validatepoint");
            $('#containerno' + val).removeClass('checkcontclass');
            $('#containerno' + val).addClass('checkcontclass');
            $('#containerno' + val).addClass('required');
            document.getElementById(id).value = val;
        } else {
            $("." + classname).removeClass("required");
            $("." + qtyclass).removeClass("maxone");
            $("." + weigthclass).removeClass("validatezeropoint");
            $("." + volumeclass).removeClass("validatepoint");
            $('#containerno' + val).removeClass('checkcontclass');
            $('#containerno' + val).removeClass('required');
            document.getElementById(id).value = "";
        }
    }
    function socstatuscheck(id, conid)
    {

        var checkid = 'select' + id;
        var containerid = conid;
        if (document.getElementById(checkid).checked == true)
        {
            $('#containerno' + containerid).addClass('checkcontclass');
        } else
        {
            $('#containerno' + containerid).removeClass('checkcontclass');
        }

    }
    <%
        if ("zh".equalsIgnoreCase(clientcode) || "ma".equalsIgnoreCase(clientcode) || "es".equalsIgnoreCase(clientcode)) {
    %>
    toastr.info("<bean:message key="ecom.msg.formsineng" bundle="<%=clientcode%>"/>", "INFO");
    <%}%>

</script>


<style>
    /*    .tooltip-inner {
            background-color: #1FBBA6;
            color: #fff;
        }
    
        .tooltip.right .tooltip-arrow  {
            border-right-color: #1FBBA6;
            color: #fff;
        }
        .alert{
            margin-left:5%;
            margin-right:5%;
        }
        .tt-dropdown-menu {
            max-height: 130px;
            overflow-y: auto;
            top:30px !important;
        }
        @media (min-width: 768px){
            .commoditywidth {
                width: 23%;
            }
            .containerwidth{
                width: 11.6%;
                padding-left: 1px;
            }
            .eqpwidth{
                width: 7.33%;
            }
            .no-right-pad{
                padding-right: 0px;
            }
            .no-left-pad{
                padding-left: 0px;
            }
        }
        @media (max-width: 768px){
            .no-right-pad{
                padding-bottom:2px;
            }
    
        }
        textarea {
            resize: none;
        }*/
</style>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" />   <bean:message key="ecom.billoflading" bundle="<%=clientcode%>"/> 
    <img class="back_btn" onclick="backtosiview()" src="Design/assets/images/page_icons/back_btn.svg" title="Back" /> 
</div>

<div class="card card-shadow page_bottom_content">
    <div class="card-body app-content">
        <input type="hidden" id="is_SI_restriction" value="<%=(is_SI_restriction ? "Y" : "")%>"/>
        <html:form method="post" action="SubmitBL.do" styleId="SIEditForm" styleClass="icons-tab-steps wizard-circle" enctype="multipart/form-data">
            <%if (errors_new.length() > 0 && errors_new.equalsIgnoreCase("sierrors")) {
            %>
            <div class="alert alert-danger alert-dismissible">
                <html:errors></html:errors >
                </div>
            <%                                    }%>
            <html:hidden   property="sino" styleId="sino" />
            <html:hidden   property="blno" styleId="blno" />
            <html:hidden   property="fcnno" styleId="fcnno" />
            <html:hidden   property="blstatus" styleId="blstatus" />
            <input type="hidden" id="cargotypecheck" value="true"/>
            <input type="hidden" id="cargovaluecheckT" value="true"/>
            <input type="hidden" id="cargovaluecheckG" value="true"/>
            <input type="hidden" id="hidn1check" name="hidn1check"/>
            <input type="hidden" id="ebook_shippercountryhdn"/>
            <input type="hidden" id="ebook_consigneecountryhdn"/>
            <input type="hidden" id="ebook_notifycountryhdn"/>
            <html:hidden property="appVersion" styleId="appVersion" />
            <html:hidden property="notifyCheck" styleId="notifyCheck"/>
            <html:hidden property="notify2Check" styleId="notify2Check"/>
            <html:hidden property="notify3Check" styleId="notify3Check"/>
            <html:hidden property="consigneeordercheck" styleId="consigneeordercheck"/>
            <html:hidden property="notifyordercheck" styleId="notifyordercheck"/>
            <html:hidden property="notify2ordercheck" styleId="notify2ordercheck"/>
            <html:hidden property="notify3ordercheck" styleId="notify3ordercheck"/>
            <html:hidden property="ccMailId" styleId="ccMailId" />
            <input type="hidden" name="stepWizardNewIndex" id="stepWizardNewIndex"/>
            <input type="hidden" name="typeheadhidden" id="typeheadhidden"/>
            <input type="hidden" name="typeheadhidden1" id="typeheadhidden1"/>
            <input type="hidden" name="typeheadhidden2" id="typeheadhidden2"/>
            <input type="hidden" name="typeheadhidden3" id="typeheadhidden3"/>
            <input type="hidden" name="typeheadhidden4" id="typeheadhidden4"/>
            <input type="hidden" name="typeheadhidden5" id="typeheadhidden5"/>
            <input type="hidden" name="typeheadhidden6" id="typeheadhidden6"/>
            <input type="hidden" name="typeheadhidden7" id="typeheadhidden7"/>
            <input type="hidden" name="typeheadhidden8" id="typeheadhidden8"/>
            <input type="hidden" name="typeheadhidden9" id="typeheadhidden9"/>
            <input type="hidden" name="typeheadhidden10" id="typeheadhidden10"/>
            <input type="hidden" name="typeheadhidden11" id="typeheadhidden11"/>
            <input type="hidden" name="typeheadhidden12" id="typeheadhidden12"/>
            <input type="hidden" name="typeheadhidden13" id="typeheadhidden13"/>
            <input type="hidden" name="typeheadhidden14" id="typeheadhidden14"/>
            <input type="hidden" name="typeheadhidden15" id="typeheadhidden15"/>
            <input type="hidden" name="typeheadhidden16" id="typeheadhidden16"/>
            <input type="hidden" name="typeheadhidden17" id="typeheadhidden17"/>
            <input type="hidden" name="typeheadhidden18" id="typeheadhidden18"/>
            <input type="hidden" name="typeheadhidden19" id="typeheadhidden19"/>

            <html:hidden property="spCustCode" styleId="shipperCustCode"/>
            <html:hidden property="spname" styleId="ebook_shippername"/>
            <html:hidden  property="spaddr" styleId="ebook_shipperaddress"/>
            <html:hidden  property="spaddr1" styleId="ebook_shipperaddress1" />
            <html:hidden property="spcity" styleId="ebook_shippercity" />
            <html:hidden property="spCityName" styleId="ebook_shippercity_desc" />
            <html:hidden property="spcountry" styleId="ebook_shippercountry" />
            <html:hidden property="spCountryName" styleId="ebook_shippercountry_name"  />
            <html:hidden property="spphone" styleId="ebook_shipperphone" />
            <html:hidden  property="spfax" styleId="ebook_shipperfax" />
            <html:hidden property="spemail" styleId="ebook_shipperemail" />
            <html:hidden property="spstate" styleId="ebook_shipperstate" />
            <html:hidden property="speori" styleId="ebook_shippereori" />
            <html:hidden property="sppersontype" styleId="shippertypeperson" />
            <html:hidden property="spzipcode" styleId="ebook_shipperzipcode" />
            <html:hidden property="consCustCode" styleId="consigneeCustCode"/>
            <html:hidden  property="consname" styleId="ebook_consigneename" />
            <html:hidden property="consaddr" styleId="ebook_consigneeaddress"  />
            <html:hidden property="consaddr1" styleId="ebook_consigneeaddress1"  />
            <html:hidden property="conscity" styleId="ebook_consigneecity" />
            <html:hidden property="consCityName" styleId="ebook_consigneecity_desc" />
            <html:hidden property="conscountry" styleId="ebook_consigneecountry" />
            <html:hidden property="consCountryName" styleId="ebook_consigneecountry_name" />
            <html:hidden property="consphone" styleId="ebook_consigneephone" />
            <html:hidden property="consfax" styleId="ebook_consigneefax" />
            <html:hidden property="consemail" styleId="ebook_consigneeemail" />
            <html:hidden property="consstate" styleId="ebook_consigneestate" />
            <html:hidden property="conszipcode" styleId="ebook_consigneezipcode" />
            <html:hidden property="conseori" styleId="ebook_consigneeeori" />
            <html:hidden property="conspersontype" styleId="consigneetypeperson" />
            <html:hidden  property="npemail" styleId="ebook_notifyemail" />
            <html:hidden property="npCustCode" styleId="notifyCustCode"/>
            <html:hidden  property="npname" styleId="ebook_notifyname" />
            <html:hidden  property="npaddr" styleId="ebook_notifyaddress" />
            <html:hidden  property="npaddr1" styleId="ebook_notifyaddress1" />
            <html:hidden property="npcity" styleId="ebook_notifycity"  />
            <html:hidden property="npCityName" styleId="ebook_notifycity_desc"/>
            <html:hidden property="npcountry" styleId="ebook_notifycountry" />
            <html:hidden  property="npCountryName" styleId="ebook_notifycountry_name" />
            <html:hidden property="npphone" styleId="ebook_notifyphone" />
            <html:hidden  property="npfax" styleId="ebook_notifyfax" />
            <html:hidden  property="npstate" styleId="ebook_notifystate" />
            <html:hidden  property="npzipcode" styleId="ebook_notifyzipcode" />
            <html:hidden  property="npeori" styleId="ebook_notifyeori" />
            <html:hidden  property="nppersontype" styleId="notifytypeperson" />
            <html:hidden property="deletedfilename" styleId="deletedfilename" />

            <html:hidden property="apCustCode" styleId="apCustCode"/>
            <html:hidden property="apname" styleId="apname"/>
            <html:hidden  property="apaddr" styleId="apaddr"/>
            <html:hidden  property="apaddr1" styleId="apaddr1" />
            <html:hidden property="apcity" styleId="apcity" />
            <html:hidden property="apCityName" styleId="apCityName" />
            <html:hidden property="apcountry" styleId="apcountry" />
            <html:hidden property="apCountryName" styleId="apCountryName"  />
            <html:hidden property="apphone" styleId="apphone" />
            <html:hidden  property="apfax" styleId="apfax" />
            <html:hidden property="apemail" styleId="apemail" />
            <html:hidden property="apstate" styleId="apstate" />
            <html:hidden property="apzipcode" styleId="apzipcode" />
            <html:hidden property="apeori" styleId="apeori" />
            <html:hidden property="appersontype" styleId="appersontype" />
            <html:hidden   property="originid" styleId="originid" />
            <html:hidden   property="finalportid" styleId="finalportid" />
            <html:hidden property="ebook_t2lfiling" styleId="ebook_t2lfiling"/>
            <html:hidden property="ebook_nvocc" styleId="ebook_nvocc"/>
            <input type="hidden" name="shippernewcustimgtype" id="shippernewcustimgtype" value="new"/>
            <input type="hidden" name="consigneenewcustimgtype" id="consigneenewcustimgtype" value="new"/>
            <input type="hidden" name="notifynewcustimgtype" id="notifynewcustimgtype" value="new"/>
            <input type="hidden" name="optionalcheck" id="optionalcheck" value="<%=carrierActualOptional%>"/>
            <input type="hidden" name="oogplugin" id="oogplugin" value="<%=ENABLE_ECOMOOG_DETAILS%>"/>
            <input type="hidden" id="euport" value="N"/>

            <h6><i class="step-icon fa_cust"></i><bean:message key="ebooking.custdetails.display" bundle="<%=clientcode%>"/></h6>

            <fieldset>  	
                <div class="details_search ">

                    <div class="header_tle"> <bean:message key="ebooking.custdetails.display" bundle="<%=clientcode%>"/> </div>

                    <div class="form-group row">
                        <label class="col-sm-2 control-label"><bean:message key="bl.Bookingno" bundle="<%=clientcode%>"/> :</label>

                        <label class="col-sm-2 "> <%=(String) session.getAttribute("edit_bookno")%>  </label>
                        <input type="hidden" name="lrpbookno" id="lrpbookno" value="<%=(String) session.getAttribute("edit_bookno")%>"/>
                        <%if (blno.length() > 0) {%>
                        <label class="col-sm-2 control-label"> <bean:message key="bl.blno" bundle="<%=clientcode%>"/> : </label>

                        <label class="col-sm-2 "> <%=(String) session.getAttribute("edit_blno")%></label>
                        <%}%>
                        <label class="col-sm-2 control-label"> <bean:message key="shipping.agency.refno" bundle="<%=clientcode%>"/> :  </label>

                        <label class="col-sm-2 "><%=agencyRefNo%>  </label>
                        <input type="hidden" id="SI_RefNo" value="<%=(String) session.getAttribute("edit_sino")%> ">
                    </div>
                    <div class="form-group row">
                        <%if ("Y".equalsIgnoreCase(Enable_BLType_Selection)) {%>
                        <label class="col-sm-2 control-label"> <bean:message key="shipping.bl.type" bundle="<%=clientcode%>"/> : </label>
                        <label class="col-sm-2 control-label">
                            <html:select property="bltype"  styleId="bltype"  styleClass="form-control light_border" onchange="resetBLTypeFlag()">
                                <%  if (blTypes != null) {
                                        for (String blType : blTypes) {%>
                                <html:option value="<%= blType%>"><%= blType%></html:option>
                                <% }
                                    } %>
                            </html:select>
                            <input type="hidden" id="hidden_bltype">
                            <input type="hidden" id="hidden_bltype_flag">
                        </label>
                        <%}%>
                         <%if ("Y".equalsIgnoreCase(Enable_ReleaseType)) {%>
                        <label class="col-sm-2 control-label" ><bean:message key="ebooking.booking.releaseType" bundle="<%=clientcode%>"/>: </label>
                        <label class="col-sm-2 control-label">
                            <html:select property="orgTelType"  styleId="orgTelType"  styleClass="form-control light_border"  >
                                <html:option value="O">Original</html:option>
                                <html:option value="T">Telex</html:option>
                            </html:select>
                          
                        </label>
                        <%}%>
                        <%if (showNvocc) {%>
                        <label class="col-sm-4 control-label" style="white-space:nowrap;margin-top: -5px;">
                            <span style="vertical-align:middle;"><bean:message key="ebooking.booking.NVOCC" bundle="<%=clientcode%>"/></span>
                            <label class="switch" style="margin:0 0 0 6px;vertical-align:middle;transform:scale(0.55);transform-origin:left center;">
                                <input type="checkbox" value="N" id="nvocc" onchange="nvoccEnable(this)" >
                                <span class="slider round" ></span>
                            </label>
                        </label>
                        <%}%>

                    </div>
                    <div class="form-group row" style="position: relative;left: 40px;" >
                    <%if (is_SI_restriction_cust) {%>
                        <div class="col-sm-12">
                            <span class="pull-right"> 
                                <span class="text-info font-weight-bold" style="color: #2196f3 !important;padding-right: 10px;"> * To change the non-editable fields. Please click on 'Request Booking update' button. </span>
                                <a href="javascript:void(0);" onclick="showMailDialog();" class="btn btn-success btn-xs refreshsearch" id="genmailbtn" style="background: #ffc107 !important;border-color: #ffc107 !important;text-transform: uppercase;"><i class="fa fa-envelope-o"></i> <bean:message key='ecom.si.generateemail' bundle="<%=clientcode%>"/></a>
                            </span>
                        </div>
                    
                    <%}%>
                    <%if("Y".equalsIgnoreCase(TENS_Documentation)){%>
                     <div class="col-sm-4 text-center" >      
                       <label style="position: relative;right: 82px;" > ENS Filing</label><br/>
                           <div class="btn-group booking_btn " id="ensfilling_btn">
                              <button type="button" class="btn btn-primary sng">Single</button>
                              <button type="button" class="btn btn-primary prl">Partial</button>
                              <button type="button" class="btn btn-primary ena">NA</button>
                         </div>
                          <html:hidden property="ebook_EnsFiling" styleId="ebook_EnsFiling"/>
                      </div>
                       <div class="col-sm-4 text-center" style="left: 22px;" >
                        <label style="position: relative;right: 34px;" ><bean:message key="ebooking.booking.shipmentType" bundle="<%=clientcode%>"/></label><br/>
                        <div class="btn-group booking_btn" id="ensDocType_btn">
                         <button type="button" class="btn btn-primary sbl">Straight B/L</button>
                         <button type="button" class="btn btn-primary cnd">Consolidated</button>
                        </div>
                        <html:hidden property="ebook_EnsDocType" styleId="ebook_EnsDocType"/>
                    </div>
                    <%}%>
                         <%if ("Y".equalsIgnoreCase(Enable_T2LFiling)) {%>
                        <div class="col-sm-2 text-center" style="position: relative;right: 6px;" >
                        <label ><bean:message key="ebooking.booking.T2LFiling" bundle="<%=clientcode%>"/> </label><br/>
                        <label class="switch" style="left: 10px">
                            <input type="checkbox" value="N" id="t2lfiling" onchange="t2lfilingEnable(this)" >
                            <span class="slider round" ></span>
                        </label>
                        </div>
                        <%}%>
                </div>
                    <hr/>
                    <%if("Y".equalsIgnoreCase(GENS)){%>
                        <div class="row">
                            <div class="col-sm-12 text-right">
                                <span class="text-info font-weight-bold text-danger">The EORI number is required only for cargo destined for
                                    Europe.</span>
                            </div>
                        </div>
                        <%}%>
                    <div class="row">

                        <div class="col-md-4">

                            <div id="shipper" class="panel_right">

                                <div>
                                    <div class="custom-panel-head"><bean:message key="ebooking.shipper.display" bundle="<%=clientcode%>"/><span class="pull-right"> <a data-toggle="collapse" class="toggle-btn" data-target="#collapseAgreementParty" aria-expanded="false" aria-controls="collapseAgreementParty"> <i class="fa fa-minus-square-o"></i> </a> </span> </div>
                                </div>

                                <div class="collapse show" id="collapseAgreementParty">

                                    <div class="row form-group">

                                        <div class="col-sm-8"><select id="shipperSelect" name="shipperSelect" class="form-control input_si" onchange="setCustDetails(this.value, 'shipper', '<%=printOnBlAdd%>')">
                                                <%
                                                    if (shList != null) {
                                                %>
                                                <option value="">Select Customer</option>
                                                <%
                                                    for (int i = 0; i < shList.size(); i++) {
                                                        custVO = (CustomerDetailsVO) shList.get(i);

                                                %>
                                                <option value="<%=custVO.getName()%>~~<%=custVO.getCustCode()%>~~<%=custVO.getAddress()%>~~<%=custVO.getCity()%>~~<%=custVO.getCountry()%>~~<%=custVO.getCountryCode()%>~~<%=custVO.getEmail()%>~~<%=custVO.getPhoneNo()%>~~<%=custVO.getFax()%>~~<%=custVO.getAddress1()%>~~<%=custVO.getState()%>~~<%=custVO.getZipcode()%>~~<%=custVO.getTaxId()%>~~<%=custVO.getPerontype()%>~~<%=custVO.getEroi()%>~~<%=custVO.getZipcode()%>">
                                                    <%=custVO.getName()%></option>
                                                    <%                                                                    }
                                                    } else {
                                                    %>


                                                <option value="">New Customer</option>
                                                <%                                                                }%>
                                                <option value="new">New Customer</option>
                                            </select>

                                        </div>

                                        <label class="col-sm-2 control-label p-t-5">
                                            <a href="javascript:void(0)" onclick="callsearchmethodsi('ebooking', 'esiSearch', 'shipper')" class="btn btn-success search_symbol btn-xs" id="shippersearch"> <i class="fa fa-search"></i> </a>
                                        </label>

                                        <label class="col-sm-2 control-label ecom_padding_left_right p-t-5">
                                            <a href="javascript:void(0)" class="btn btn-warning btn-xs new_btn_si" onclick="shownewcustmodal('shipper');" id="shippernewcustimg" data-toggle="tooltip" title="" data-placement="right" class="btn btn-warning btn-xs" data-original-title="Edit">
                                                <i class="fa fa-pencil-square"> </i> New</a>
                                        </label>

                                    </div>

                                    <div class="row">

                                        <label class="col-sm-8"> <bean:message key="shipping.printonbl" bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>

                                    </div>

                                    <div class="row">
                                        <div class="col-sm-12 form-group">
                                            <html:text property="spnameheader" styleId="ebook_shippernameheader" styleClass="form-control required input_si SI_Process_Restriction_Cust" maxlength="150" readonly="true"/>
                                        </div>
                                    </div>
                                    <%if ("Y".equalsIgnoreCase(printOnBlAdd)) {%>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <html:textarea styleId="headershipper" property="headerShipper"  styleClass="form-control required SI_Process_Restriction_Cust" rows="5" readonly="true" >
                                            </html:textarea>

                                        </div>
                                    </div>
                                    <%} else {%>

                                    <div class="row">
                                        <div class="col-sm-12">
                                            <html:textarea styleId="headershipper" property="headerShipper"  styleClass="form-control required SI_Process_Restriction_Cust" rows="5" >
                                            </html:textarea>

                                        </div>
                                    </div>
                                    <%}%>
                                </div>


                            </div>

                        </div> 	<!-- col-md-4 end  -->



                        <div class="col-md-4">

                            <div id="Consignee" class="panel_right">

                                <div>
                                    <div class="custom-panel-head"> <bean:message key="ebooking.consignee.display" bundle="<%=clientcode%>"/> <span class="pull-right"> <a data-toggle="collapse" href="javascript:void(0)" onclick="openCollapseFun('collapseConsignee');" class="toggle-btn" data-target="#collapseConsignee" aria-expanded="false" aria-controls="collapseConsignee"> <i class="fa fa-minus-square-o"></i> </a> </span> 
                                        <%if ("Y".equalsIgnoreCase(DispConsigToOrder)) {%>
                                        <div class="form-check-inline">
                                            <label> <input type="checkbox" id="consigneeorder" name="consigneeorder" onClick="checkToOrder('consignee', 'consigneeorder');" value="1"> To Order </label>
                                        </div>
                                        <%}%>

                                    </div>


                                </div>

                                <div class="collapse show" id="collapseConsignee">

                                    <div class="row form-group">

                                        <div class="col-sm-8"> 
                                            <select id="consigneeSelect" name="consigneeSelect" class="form-control input_si" onchange="setCustDetails(this.value, 'consignee', '<%=printOnBlAdd%>')">
                                                <%
                                                    if (cnList != null) {
                                                %>
                                                <option value="">Select Customer</option>
                                                <%
                                                    for (int i = 0; i < cnList.size(); i++) {
                                                        custVO = (CustomerDetailsVO) cnList.get(i);

                                                %>
                                                <option value="<%=custVO.getName()%>~~<%=custVO.getCustCode()%>~~<%=custVO.getAddress()%>~~<%=custVO.getCity()%>~~<%=custVO.getCountry()%>~~<%=custVO.getCountryCode()%>~~<%=custVO.getEmail()%>~~<%=custVO.getPhoneNo()%>~~<%=custVO.getFax()%>~~<%=custVO.getAddress1()%>~~<%=custVO.getState()%>~~<%=custVO.getZipcode()%>~~<%=custVO.getTaxId()%>~~<%=custVO.getPerontype()%>~~<%=custVO.getEroi()%>~~<%=custVO.getZipcode()%>">
                                                    <%=custVO.getName()%></option>
                                                    <%                                                                    }
                                                    } else {
                                                    %>
                                                <option value="">New Customer</option>
                                                <%                                                                }%>
                                                <option value="new">New Customer</option>
                                            </select>

                                        </div>

                                        <label class="col-sm-2 control-label p-t-5">
                                            <a href="javascript:void(0)" onclick="callsearchmethodsi('ebooking', 'esiSearch', 'consignee')" class="btn btn-success search_symbol btn-xs" id="consigneesearch"><i class="fa fa-search"></i></a>
                                        </label>

                                        <label class="col-sm-2 control-label ecom_padding_left_right p-t-5">
                                            <a onclick="shownewcustmodal('consignee');" id="consigneenewcustimg" href="javascript:void(0)" data-toggle="tooltip" title="" data-placement="right" class="btn btn-warning btn-xs new_btn_si" data-original-title="Edit">
                                                <i class="fa fa-pencil-square" id="ccupdateimg"></i>  New</a>
                                        </label>

                                    </div>

                                    <div class="row">

                                        <label class="col-sm-8"> <bean:message key="shipping.printonbl" bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>

                                    </div>

                                    <div class="row">
                                        <div class="col-sm-12 form-group">
                                            <html:text  property="consnameheader" styleId="ebook_consigneenameheader" styleClass="form-control input_si required SI_Process_Restriction_Cust" maxlength="150" readonly="true"/>

                                        </div>
                                    </div>
                                    <%if ("Y".equalsIgnoreCase(printOnBlAdd)) {%>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <html:textarea styleId="headerconsignee" property="headerConsignee"  styleClass="form-control required SI_Process_Restriction_Cust" rows="5" readonly="true" >
                                            </html:textarea>

                                        </div>
                                    </div>
                                    <%} else {%>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <html:textarea styleId="headerconsignee" property="headerConsignee"  styleClass="form-control required SI_Process_Restriction_Cust" rows="5" >
                                            </html:textarea>

                                        </div>
                                    </div>
                                    <%}%>

                                </div>


                            </div>

                        </div> 	<!-- col-md-4 end  -->

                        <div class="col-md-4 ">

                            <div id="Notify" class="panel_right">

                                <div>
                                    <div class="custom-panel-head"> <bean:message key="Booking.party.notify" bundle="<%=clientcode%>"/> <span class="pull-right"> <a data-toggle="collapse" class="toggle-btn"  href="javascript:void(0)" onclick="openCollapseFun('collapseNotify');" data-target="#collapseNotify" aria-expanded="false" aria-controls="collapseNotify"> <i class="fa fa-minus-square-o"></i> </a> </span> 

                                        <div class="form-check-inline">

                                            <label>
                                                <input type="checkbox" id="notifyconsig" name="notifyconsig" onClick="setSISameAsConsignee('notify', 'notifyconsig');" value="1"><bean:message key='ebooking.SameasConsignee.display' bundle="<%=clientcode%>"/> </label>
                                        </div>

                                    </div>
                                </div>

                                <div class="collapse show" id="collapseNotify">

                                    <div class="row form-group">

                                        <div class="col-sm-8">
                                            <select id="notifySelect" name="notifySelect" class="form-control input_si" onchange="setCustDetails(this.value, 'notify', '<%=printOnBlAdd%>')">
                                                <%
                                                    if (ntList != null) {
                                                %>
                                                <option value="">Select Customer</option>
                                                <%
                                                    for (int i = 0; i < ntList.size(); i++) {
                                                        custVO = (CustomerDetailsVO) ntList.get(i);

                                                %>
                                                <option value="<%=custVO.getName()%>~~<%=custVO.getCustCode()%>~~<%=custVO.getAddress()%>~~<%=custVO.getCity()%>~~<%=custVO.getCountry()%>~~<%=custVO.getCountryCode()%>~~<%=custVO.getEmail()%>~~<%=custVO.getPhoneNo()%>~~<%=custVO.getFax()%>~~<%=custVO.getAddress1()%>~~<%=custVO.getState()%>~~<%=custVO.getZipcode()%>~~<%=custVO.getTaxId()%>~~<%=custVO.getPerontype()%>~~<%=custVO.getEroi()%>~~<%=custVO.getZipcode()%>">
                                                    <%=custVO.getName()%></option>
                                                    <%                                                                    }
                                                    } else {
                                                    %>
                                                <option value="">New Customer</option>
                                                <%                                                                }%>
                                                <option value="new">New Customer</option>
                                            </select>
                                        </div>

                                        <label class="col-sm-2 control-label p-t-5">
                                            <a href="javascript:void(0)" onclick="callsearchmethodsi('ebooking', 'esiSearch', 'notify')"  class="btn btn-success search_symbol btn-xs"  id="notifysearch" > <i class="fa fa-search"></i> </a>
                                        </label>

                                        <label class="col-sm-2 control-label ecom_padding_left_right p-t-5">
                                            <a onclick="shownewcustmodal('notify');" id="notifynewcustimg" href="javascript:void(0)" class="btn btn-warning btn-xs new_btn_si" data-toggle="tooltip" title="" data-placement="right"  data-original-title="Edit">
                                                <i class="fa fa-pencil-square" id="ccupdateimg" > </i> New </a>
                                        </label>

                                    </div>

                                    <div class="row">

                                        <label class="col-sm-8"> <bean:message key="shipping.printonbl" bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>

                                    </div>

                                    <div class="row">
                                        <div class="col-sm-12 form-group">
                                            <html:text  property="npnameheader" styleId="ebook_notifynameheader" styleClass="form-control input_si SI_Process_Restriction_Cust" maxlength="150" readonly="true"/>
                                        </div>
                                    </div>
                                    <%if ("Y".equalsIgnoreCase(printOnBlAdd)) {%>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <html:textarea styleId="headernotify" property="headerNotify"  styleClass="form-control SI_Process_Restriction_Cust" rows="5" readonly="true" >
                                            </html:textarea>

                                        </div>
                                    </div>
                                    <%} else {%>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <html:textarea styleId="headernotify" property="headerNotify"  styleClass="form-control SI_Process_Restriction_Cust" rows="5" >
                                            </html:textarea>

                                        </div>
                                    </div>
                                    <%}%>
                                </div>


                            </div>

                        </div> 	<!-- col-md-4 end  -->

                    </div>
                    <% if (custtypelist.contains("N1")) {%>

                    <div class="row">

                        <div class="col-md-12">

                            <div class="panel_right">

                                <div class="custom-panel-head"> <bean:message key="Booking.party.notify1" bundle="<%=clientcode%>"/>
                                    <div class="form-check-inline">
                                        <label>
                                            <input type="checkbox" id="notify2consig" name="notify2consig" onClick="setSISameAsConsignee('notify2', 'notify2consig');" value="1">
                                            <bean:message key="shipping.booking.party.sameasConsignee" bundle="<%=clientcode%>"/></label>
                                    </div>
                                    <span class="pull-right" style="margin-top: -7px;"> <a href="javascript:void(0)" onclick="callsearchmethodsi('ebooking', 'esiSearch', 'notify2')" class="btn btn-success search_symbol btn-xs" id="notify2search" > <i class="fa fa-search"></i></a> 

                                        <a data-toggle="collapse" class="toggle-btn"   href="javascript:void(0)" onclick="openCollapseFun('collapseNotify2');" data-target="#collapseNotify2" aria-expanded="false" aria-controls="collapseNotify2"> <i class="fa fa-plus-square-o"></i> </a>

                                    </span>

                                </div>

                                <div class="collapse" id="collapseNotify2">

                                    <div class="row form-group">

                                        <div class="col-sm-1"> </div>

                                        <div class="col-sm-3">
                                            <select id="notify2Select" name="notify2Select" class="form-control input_si" onchange="setCustDetails(this.value, 'notify2')">
                                                <%
                                                    if (ntList != null) {
                                                %>
                                                <option value="">Select Customer</option>
                                                <%
                                                    for (int i = 0; i < ntList.size(); i++) {
                                                        custVO = (CustomerDetailsVO) ntList.get(i);

                                                %>
                                                <option value="<%=custVO.getName()%>~~<%=custVO.getCustCode()%>~~<%=custVO.getAddress()%>~~<%=custVO.getCity()%>~~<%=custVO.getCountry()%>~~<%=custVO.getCountryCode()%>~~<%=custVO.getEmail()%>~~<%=custVO.getPhoneNo()%>~~<%=custVO.getFax()%>~~<%=custVO.getAddress1()%>~~<%=custVO.getState()%>~~<%=custVO.getZipcode()%>~~<%=custVO.getTaxId()%>~~<%=custVO.getPerontype()%>~~<%=custVO.getEroi()%>~~<%=custVO.getZipcode()%>">
                                                    <%=custVO.getName()%></option>
                                                    <%                                                                    }
                                                    } else {
                                                    %>
                                                <option value="">New Customer</option>
                                                <%                                                                }%>
                                                <option value="new">New Customer</option>
                                            </select>
                                        </div>

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                        <div class="col-sm-3 ">
                                            <html:hidden property="np2CustCode" styleId="notify2CustCode"/>
                                            <html:text  property="np2name" styleId="ebook_notify2name" styleClass="form-control required input_si SI_Process_Restriction_Cust" maxlength="150"/>
                                        </div>

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text  property="np2addr" styleId="ebook_notify2address" styleClass="form-control required input_si SI_Process_Restriction_Cust" maxlength="150"/>

                                        </div>

                                    </div>

                                    <div class="row form-group">

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                        <div class="col-sm-3">
                                            <html:text  property="np2addr1" styleId="ebook_notify2address1" styleClass="form-control  input_si SI_Process_Restriction_Cust" maxlength="150"/>

                                        </div>

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text property="np2city" styleId="ebook_notify2city"  styleClass="form-control required input_si SI_Process_Restriction_Cust" maxlength="150"/>
                                            <html:hidden property="np2CityName" styleId="ebook_notify2city_desc"/>

                                        </div>

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                        <div class="col-sm-3 ">
                                            <html:hidden property="np2country" styleId="ebook_notify2country" />
                                            <html:text  property="np2CountryName" styleId="ebook_notify2country_name" styleClass="form-control required input_si SI_Process_Restriction_Cust" onchange="loadStateAutoComplete('ebook_notify2country_name', 'ebook_notify2state', 'typeheadhidden10');" />
                                        </div>

                                    </div>


                                    <div class="row form-group">

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                        <div class="col-sm-3">
                                            <html:text property="np2phone" styleId="ebook_notify2phone" styleClass="form-control input_si SI_Process_Restriction_Cust" maxlength="20"/>

                                        </div>
                                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                        <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text  property="np2fax" styleId="ebook_notify2fax" styleClass="form-control input_si SI_Process_Restriction_Cust" maxlength="20"/>

                                        </div>
                                        <%} else {%>

                                        <html:hidden property="np2fax" styleId="ebook_notify2fax"   styleClass="form-control input_si" />

                                        <%}%>
                                        <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text size="43" property="np2email" styleId="ebook_notify2email" styleClass="form-control email input_si SI_Process_Restriction_Cust" maxlength="75"/>

                                        </div>
                                        <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                        <label class="col-sm-1 control-label"> <bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/> </label>
                                        <div class="col-sm-3">
                                            <html:text property="np2zipcode" styleId="ebook_notify2zipcode" styleClass="form-control zip_code input_si SI_Process_Restriction_Cust" maxlength="15"/>
                                        </div>
                                        <%}%>
                                         

                                    </div>
                                    <div class="row form-group">

                                        <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/> </label>

                                        <div class="col-sm-3">
                                            <html:text property="np2state" styleId="ebook_notify2state" styleClass="form-control input_si SIBL_state SI_Process_Restriction_Cust" onclick="validateState('ebook_notify2country_name',this);"/>

                                        </div> 
                                        <%if ("Y".equalsIgnoreCase(GENS)) {%>
                                        <label class="col-sm-1 control-label ebook_notify2div" ><bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>   </label>

                                        <div class="col-sm-3 ">
                                            <html:text  property="np2eori" styleId="ebook_notify2eori" styleClass="form-control input_si  ebook_notify2div"  maxlength="17" />
                                        </div>
                                        <label class="col-sm-1 control-label ebook_notify2div"  ><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/> </label>

                                        <div class="col-sm-3">
                                            <html:select property="np2persontype" styleId="notify2typeperson" styleClass="form-control input_si ebook_notify2div "   >
                                                <html:option value="">Type of Person</html:option>
                                                <html:option value="Legal">Legal</html:option>
                                                <html:option value="Natural">Natural</html:option>
                                                <html:option value="Association of persons">Association of persons</html:option>
                                            </html:select>  

                                        </div>
                                        <%}%> 
                                    </div>

                                </div>

                            </div>


                        </div>

                    </div>

                    <%}%>
                    <% if (custtypelist.contains("N2")) {%>

                    <div class="row">

                        <div class="col-md-12">

                            <div class="panel_right">

                                <div class="custom-panel-head"><bean:message key="Booking.party.notify2" bundle="<%=clientcode%>"/>
                                    <div class="form-check-inline">
                                        <label>
                                            <input type="checkbox" id="notify3consig" name="notify3consig" onClick="setSISameAsConsignee('notify3', 'notify3consig');" value="1">
                                            <bean:message key="shipping.booking.party.sameasConsignee" bundle="<%=clientcode%>"/></label>
                                    </div>
                                    <span class="pull-right" style="margin-top: -7px;"> <a href="javascript:void(0)" onclick="callsearchmethodsi('ebooking', 'esiSearch', 'notify3')" class="btn btn-success search_symbol btn-xs" id="notify3search"> <i class="fa fa-search"></i></a> 

                                        <a data-toggle="collapse" href="javascript:void(0)" onclick="openCollapseFun('collapseNotify3');" class="toggle-btn"  data-target="#collapseNotify3" aria-expanded="false" aria-controls="collapseNotify3"> <i class="fa fa-plus-square-o"></i> </a>

                                    </span>

                                </div>

                                <div class="collapse" id="collapseNotify3">

                                    <div class="row form-group">

                                        <div class="col-sm-1"> </div>

                                        <div class="col-sm-3">

                                            <select id="notify3Select" name="notify3Select" class="form-control input_si" onchange="setCustDetails(this.value, 'notify3')">
                                                <%
                                                    if (ntList != null) {
                                                %>
                                                <option value="">Select Customer</option>
                                                <%
                                                    for (int i = 0; i < ntList.size(); i++) {
                                                        custVO = (CustomerDetailsVO) ntList.get(i);

                                                %>
                                                <option value="<%=custVO.getName()%>~~<%=custVO.getCustCode()%>~~<%=custVO.getAddress()%>~~<%=custVO.getCity()%>~~<%=custVO.getCountry()%>~~<%=custVO.getCountryCode()%>~~<%=custVO.getEmail()%>~~<%=custVO.getPhoneNo()%>~~<%=custVO.getFax()%>~~<%=custVO.getAddress1()%>~~<%=custVO.getState()%>~~<%=custVO.getZipcode()%>~~<%=custVO.getTaxId()%>~~<%=custVO.getPerontype()%>~~<%=custVO.getEroi()%>~~<%=custVO.getZipcode()%>">
                                                    <%=custVO.getName()%></option>
                                                    <%                                                                    }
                                                    } else {
                                                    %>
                                                <option value="">New Customer</option>
                                                <%                                                                }%>
                                                <option value="new">New Customer</option>
                                            </select> 
                                        </div>

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                        <div class="col-sm-3 ">
                                            <html:hidden property="np3CustCode" styleId="notify3CustCode"/>
                                            <html:text  property="np3name" styleId="ebook_notify3name" styleClass="form-control required input_si SI_Process_Restriction_Cust" maxlength="150"/>
                                        </div>

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text  property="np3addr" styleId="ebook_notify3address" styleClass="form-control required input_si SI_Process_Restriction_Cust" maxlength="150"/>
                                        </div>

                                    </div>

                                    <div class="row form-group">

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>  </label>

                                        <div class="col-sm-3">
                                            <html:text  property="np3addr1" styleId="ebook_notify3address1" styleClass="form-control  input_si SI_Process_Restriction_Cust" maxlength="150"/>

                                        </div>

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text property="np3city" styleId="ebook_notify3city"  styleClass="form-control required input_si SI_Process_Restriction_Cust" maxlength="150"/>
                                            <html:hidden property="np3CityName" styleId="ebook_notify3city_desc"/>

                                        </div>

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                        <div class="col-sm-3 ">
                                            <html:hidden property="np3country" styleId="ebook_notify3country" />
                                            <html:text  property="np3CountryName" styleId="ebook_notify3country_name" styleClass="form-control required input_si SI_Process_Restriction_Cust" onchange="loadStateAutoComplete('ebook_notify3country_name', 'ebook_notify3state', 'typeheadhidden11');"/>
                                        </div>

                                    </div>


                                    <div class="row form-group">

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                        <div class="col-sm-3">
                                            <html:text property="np3phone" styleId="ebook_notify3phone" styleClass="form-control input_si SI_Process_Restriction_Cust" maxlength="20"/>
                                        </div>
                                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                        <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text  property="np3fax" styleId="ebook_notify3fax" styleClass="form-control input_si SI_Process_Restriction_Cust" maxlength="20"/>
                                        </div>
                                        <%} else {%>

                                        <html:hidden property="np3fax" styleId="ebook_notify3fax"   styleClass="form-control input_si SI_Process_Restriction_Cust" />

                                        <%}%>
                                        <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text size="43" property="np3email" styleId="ebook_notify3email" styleClass="form-control email input_si SI_Process_Restriction_Cust" maxlength="75"/>
                                        </div>

                                        <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                        <label class="col-sm-1 control-label"> <bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/> </label>
                                        <div class="col-sm-3">
                                            <html:text property="np3zipcode" styleId="ebook_notify3zipcode" styleClass="form-control zip_code input_si SI_Process_Restriction_Cust" maxlength="15"/>
                                        </div>
                                        <%}%>
                                         

                                        </div>
                                    <div class="row form-group">

                                        <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/> </label>

                                        <div class="col-sm-3">
                                            <html:text property="np3state" styleId="ebook_notify3state" styleClass="form-control input_si SIBL_state SI_Process_Restriction_Cust" onclick="validateState('ebook_notify3country_name',this);" />

                                        </div>
                                        <%if ("Y".equalsIgnoreCase(GENS)) {%>
                                        <label class="col-sm-1 control-label ebook_notify3div" ><bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text  property="np3eori" styleId="ebook_notify3eori"  styleClass="form-control input_si  ebook_notify3div "  maxlength="17" />
                                        </div>
                                        <label class="col-sm-1 control-label ebook_notify3div"  ><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/> </label>

                                        <div class="col-sm-3">
                                            <html:select property="np3persontype" styleId="notify3typeperson"   styleClass="form-control input_si ebook_notify3div "  >
                                                <html:option value="">Type of Person</html:option>
                                                <html:option value="Legal">Legal</html:option>
                                                <html:option value="Natural">Natural</html:option>
                                                <html:option value="Association of persons">Association of persons</html:option>
                                            </html:select>  

                                        </div>
                                        <%}%> 
                                    </div>

                                </div>

                            </div>


                        </div>

                    </div>
                    <%}%>
                    <% if (custtypelist.contains("FW")) {%>

                    <div class="row">

                        <div class="col-md-12">

                            <div class="panel_right">

                                <div class="custom-panel-head"> <bean:message key="ecom.bkg.custfw" bundle="<%=clientcode%>"/>
                                    <!--                                    <div class="form-check-inline">
                                                                            <label>
                                                                                <input type="checkbox" id="notify3consig" name="notify3consig" onClick="setSISameAsConsignee('notify3', 'notify3consig');" value="1">
                                                                                Same as Consignee</label>
                                                                        </div>-->
                                    <span class="pull-right" style="margin-top: -7px;"> <a href="javascript:void(0)" onclick="callsearchmethodsi('ebooking', 'esiSearch', 'forwarder')" class="btn btn-success search_symbol btn-xs" id="forwardersearch"> <i class="fa fa-search"></i></a> 

                                        <a data-toggle="collapse" href="javascript:void(0)" onclick="openCollapseFun('collapseForwarder');" class="toggle-btn"  data-target="#collapseForwarder" aria-expanded="false" aria-controls="collapseForwarder"> <i class="fa fa-plus-square-o"></i> </a>

                                    </span>

                                </div>

                                <div class="collapse" id="collapseForwarder">

                                    <div class="row form-group">

                                        <div class="col-sm-1"> </div>

                                        <div class="col-sm-3">

                                            <select id="forwarderSelect" name="forwarderSelect" class="form-control input_si" onchange="setCustDetails(this.value, 'forwarder')">
                                                <%
                                                    if (fwList != null) {
                                                %>
                                                <option value="">Select Customer</option>
                                                <%
                                                    for (int i = 0; i < fwList.size(); i++) {
                                                        custVO = (CustomerDetailsVO) fwList.get(i);

                                                %>
                                                <option value="<%=custVO.getName()%>~~<%=custVO.getCustCode()%>~~<%=custVO.getAddress()%>~~<%=custVO.getCity()%>~~<%=custVO.getCountry()%>~~<%=custVO.getCountryCode()%>~~<%=custVO.getEmail()%>~~<%=custVO.getPhoneNo()%>~~<%=custVO.getFax()%>~~<%=custVO.getAddress1()%>~~<%=custVO.getState()%>~~<%=custVO.getZipcode()%>~~<%=custVO.getTaxId()%>~~<%=custVO.getPerontype()%>~~<%=custVO.getEroi()%>~~<%=custVO.getZipcode()%>">
                                                    <%=custVO.getName()%></option>
                                                    <%                                                                    }
                                                    } else {
                                                    %>
                                                <option value="">New Customer</option>
                                                <%                                                                }%>
                                                <option value="new">New Customer</option>
                                            </select> 
                                        </div>

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                        <div class="col-sm-3 ">
                                            <html:hidden property="fwCustCode" styleId="forwarderCustCode"/>
                                            <html:text  property="fwname" styleId="ebook_forwardername" styleClass="form-control required input_si SI_Process_Restriction_Cust" maxlength="150"/>
                                        </div>

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text  property="fwaddr" styleId="ebook_forwarderaddress" styleClass="form-control required input_si SI_Process_Restriction_Cust" maxlength="150"/>
                                        </div>

                                    </div>

                                    <div class="row form-group">

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                        <div class="col-sm-3">
                                            <html:text  property="fwaddr1" styleId="ebook_forwarderaddress1" styleClass="form-control  input_si SI_Process_Restriction_Cust" maxlength="150"/>

                                        </div>

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text property="fwcity" styleId="ebook_forwardercity"  styleClass="form-control required input_si SI_Process_Restriction_Cust" maxlength="150"/>
                                            <html:hidden property="fwCityName" styleId="ebook_forwardercity_desc"/>

                                        </div>

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                        <div class="col-sm-3 ">
                                            <html:hidden property="fwcountry" styleId="ebook_forwardercountry" />
                                            <html:text  property="fwCountryName" styleId="ebook_forwardercountry_name" styleClass="form-control required input_si SI_Process_Restriction_Cust" onchange="loadStateAutoComplete('ebook_forwardercountry_name', 'ebook_forwarderstate', 'typeheadhidden12');"/>
                                        </div>

                                    </div>


                                    <div class="row form-group">

                                        <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                        <div class="col-sm-3">
                                            <html:text property="fwphone" styleId="ebook_forwarderphone" styleClass="form-control input_si SI_Process_Restriction_Cust" maxlength="20"/>
                                        </div>
                                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                        <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text  property="fwfax" styleId="ebook_forwarderfax" styleClass="form-control input_si SI_Process_Restriction_Cust" maxlength="20"/>
                                        </div>
                                        <%} else {%>

                                        <html:hidden property="fwfax" styleId="ebook_forwarderfax"   styleClass="form-control input_si SI_Process_Restriction_Cust" />

                                        <%}%>
                                        <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text size="43" property="fwemail" styleId="ebook_forwarderemail" styleClass="form-control email input_si SI_Process_Restriction_Cust" maxlength="75"/>
                                        </div>
                                        <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                        <label class="col-sm-1 control-label"> <bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/> </label>
                                        <div class="col-sm-3">
                                            <html:text property="fwzipcode" styleId="ebook_forwarderzipcode" styleClass="form-control zip_code input_si SI_Process_Restriction_Cust" maxlength="15"/>
                                        </div>
                                        <%}%>
                                          

                                    </div>
                                    <div class="row form-group">

                                        <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                        <div class="col-sm-3">
                                            <html:text property="fwstate" styleId="ebook_forwarderstate" styleClass="form-control input_si SIBL_state SI_Process_Restriction_Cust" onclick="validateState('ebook_forwardercountry_name',this);" />

                                        </div>
                                        <%if ("Y".equalsIgnoreCase(GENS)) {%>
                                        <label class="col-sm-1 control-label ebook_forwarderdiv"  ><bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>

                                        <div class="col-sm-3 ">
                                            <html:text  property="fweori" styleId="ebook_forwardereori" styleClass="form-control input_si  ebook_forwarderdiv"  maxlength="17"  />
                                        </div>
                                        <label class="col-sm-1 control-label ebook_forwarderdiv"  ><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/> </label>

                                        <div class="col-sm-3">
                                            <html:select property="fwpersontype" styleId="forwardertypeperson" styleClass="form-control input_si ebook_forwarderdiv "   >
                                                <html:option value="">Type of Person</html:option>
                                                <html:option value="Legal">Legal</html:option>
                                                <html:option value="Natural">Natural</html:option>
                                                <html:option value="Association of persons">Association of persons</html:option>
                                            </html:select>  

                                        </div>
                                        <%}%> 
                                    </div>

                                </div>

                            </div>


                        </div>

                    </div>
                    <%}%>
                   
              <jsp:include page="/WEB-INF/jsp/SI/SIBLCommonAddCustomer.jsp">
              <jsp:param name="custype" value="Seller" />
              <jsp:param name="id" value="1" />
              <jsp:param name="flag" value="false" />
             </jsp:include>  

            <jsp:include page="/WEB-INF/jsp/SI/SIBLCommonAddCustomer.jsp">
            <jsp:param name="custype" value="Buyer" />
            <jsp:param name="id" value="1" />
            <jsp:param name="flag" value="false" />
            </jsp:include>
             

                </div>

            </fieldset> 

            <jsp:include page="../SI/SLBLCommonHeader.jsp"/>

            <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
            <jsp:include page="../SI/SIBLCommonCargoNew.jsp"/>
            <%} else { %>
            <jsp:include page="../SI/SIBLCommonCargo.jsp"/>
            <%}%>

            <jsp:include page="../SI/SIBLCommonInsurance.jsp"/>

            <jsp:include page="../Common/Cargo_Protect.jsp"/>

            <jsp:include page="../SI/SIBLCommonCharges.jsp"/>

            <jsp:include page="../SI/SIBLCommonENS.jsp"/>


            <%if ("Y".equalsIgnoreCase(Display_Charge_Tab)) {%>
            <jsp:include page="../SI/SIBLCommonChargesTab.jsp"/>
            <%}%>

            <h6><i class="step-icon fa_upload"></i><bean:message key="Booking.head.file" bundle="<%=clientcode%>"/> </h6>

            <fieldset>  	
                <div class="details_search txt_upload">

                    <div class="header_tle">  <bean:message key="Booking.msg.fileupload" bundle="<%=clientcode%>"/> </div>
                    <div class='col-sm-12 row'>
                        <div class="col-sm-4">
                            <div class="row">
                                <div class="col-xs-12 col-sm-12">
                                    <div class="txt_note file_disclaimer_color"><bean:message key="ecom.bkg.filenote" bundle="<%=clientcode%>"/>:</div> 
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-12 col-sm-12 ">
                                    <%if ("Y".equalsIgnoreCase(EnableFileBackendValidation)) {%>
                                    <div class="file_disclaimer_color"><bean:message key="ebooking.msg.fileonlyext" bundle="<%=clientcode%>"/> <%=fileExt%> files only accepted.</div>
                                    <%} else {%>
                                    <div class="file_disclaimer_color"><bean:message key="ebooking.msg.fileext" bundle="<%=clientcode%>"/>  </div>
                                    <%}%>
                                    <div class="file_disclaimer_color"> <bean:message key="ebooking.msg.filesizelimit" bundle="<%=clientcode%>"/><%=fileSize%>MB.</div>
                                </div>
                            </div>                          
                        </div>
                        <div class='col-sm-8'>
                            <div class="form-group">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.vgm' bundle="<%=clientcode%>"/></span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path1" styleId="file_path1" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>
                                        </div>
                                       <!-- <label id="filename1_lbl" style="display:none"> <bean1:message key='ebooking.filename.fname' bundle="<%=clientcode%>"/>: <span id="filename1_span" class="file_nmae">  </span> </label>
                                        <html1:hidden  styleId="filename1"  property="filename1"/>-->

                                    </div>
                                    <div class="col-xs-12 col-sm-2"><button type="button" onclick="moreFile('vgm')" class="btn btn_add_si si_cmdty_add_btn"> <i class="fa fa-plus"></i>   ADD</button></div>

                                </div>
                            </div>

                            <div class="form-group d-none" id="vgm1">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.vgm' bundle="<%=clientcode%>"/>  1 </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path6" styleId="file_path6" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>
                                        </div>
                                        <!--<label id="filename1_lbl" style="display:none"> <bean1:message key='ebooking.filename.fname' bundle="<%=clientcode%>"/>: <span id="filename1_span" class="file_nmae">  </span> </label>
                                        <html1:hidden  styleId="filename1"  property="filename1"/>-->

                                    </div>

                                </div>
                            </div>
                            <div class="form-group d-none" id="vgm2">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.vgm' bundle="<%=clientcode%>"/> 2 </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path7" styleId="file_path7" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>
                                        </div>


                                    </div>

                                </div>
                            </div>

                            <div class="form-group">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.dg' bundle="<%=clientcode%>"/> </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path2" styleId="file_path2"  styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-2"><button type="button" onclick="moreFile('dg')" class="btn btn_add_si si_cmdty_add_btn"> <i class="fa fa-plus"></i>   ADD</button></div>
                                </div>
                            </div>
                            <div class="form-group d-none" id="dg1">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.dg' bundle="<%=clientcode%>"/> 1 </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path8" styleId="file_path8"  styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group d-none" id="dg2">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.dg' bundle="<%=clientcode%>"/> 2 </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path9" styleId="file_path9"  styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.others1' bundle="<%=clientcode%>"/>  </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path3" styleId="file_path3" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="col-xs-12 col-sm-2"><button type="button" onclick="moreFile('others1')" class="btn btn_add_si si_cmdty_add_btn"> <i class="fa fa-plus"></i>   ADD</button></div>

                                </div>
                            </div>

                            <div class="form-group d-none" id="others11">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.others1' bundle="<%=clientcode%>"/> 1 </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path10" styleId="file_path10" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>
                                        </div>

                                    </div>

                                </div>
                            </div>

                            <div class="form-group d-none" id="others12">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.others1' bundle="<%=clientcode%>"/> 2 </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path11" styleId="file_path11" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>
                                        </div>

                                    </div>

                                </div>
                            </div>


                            <div class="form-group">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.others2' bundle="<%=clientcode%>"/> </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path4" styleId="file_path4" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="col-xs-12 col-sm-2"><button type="button" onclick="moreFile('others2')" class="btn btn_add_si si_cmdty_add_btn"> <i class="fa fa-plus"></i>   ADD</button></div>

                                </div>
                            </div>
                            <div class="form-group d-none" id="others21">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.others2' bundle="<%=clientcode%>"/> 1 </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path12" styleId="file_path12" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>
                                        </div>

                                    </div>

                                </div>
                            </div>

                            <div class="form-group d-none" id="others22">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.others2' bundle="<%=clientcode%>"/> 2 </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path13" styleId="file_path13" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>
                                        </div>

                                    </div>

                                </div>
                            </div>

                            <div class="form-group">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.others3' bundle="<%=clientcode%>"/> </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path5" styleId="file_path5" value="" title="" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>

                                        </div>

                                    </div>
                                    <div class="col-xs-12 col-sm-2"><button type="button" onclick="moreFile('others3')" class="btn btn_add_si si_cmdty_add_btn"> <i class="fa fa-plus"></i>   ADD</button></div>

                                </div>
                            </div>

                            <div class="form-group d-none" id="others31">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.others3' bundle="<%=clientcode%>"/> 1 </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path14" styleId="file_path14" value="" title="" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>

                                        </div>

                                    </div>

                                </div>
                            </div>

                            <div class="form-group d-none" id="others32">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">

                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text-fileupload"><bean:message key='ecom.file.others3' bundle="<%=clientcode%>"/> 2 </span>
                                            </div>
                                            <div class="custom-file">
                                                <html:file property="file_path15" styleId="file_path15" value="" title="" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                                <label class="custom-file-label" for=""><bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                            </div>

                                        </div>

                                    </div>

                                </div>
                            </div>


                            <% ArrayList filenamelist = (ArrayList) request.getAttribute("FILENAMELIST");
                                if (filenamelist != null && filenamelist.size() > 0) {%>
                            <div class="form-group">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-10">
                                        <table class="table table-bordered" id="sifilenamelist" >
                                            <thead>
                                                <tr>
                                                    <th scope="col">FileName</th>
                                                    <th scope="col" width='10'>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%for (int i = 0; i < filenamelist.size(); i++) {%>
                                                <tr>
                                                    <th><%=filenamelist.get(i)%></th>
                                                    <td><a href="javascript:void(0);" onclick="downloadfile('<%=filenamelist.get(i)%>')" class='text-success' ><i class="fa fa-download"  aria-hidden="true"></i></a>
                                                        <a href="javascript:void(0);" onclick="deletefile('<%=filenamelist.get(i)%>', this)" class='text-danger'><i class="fa fa-trash-o"  aria-hidden="true"></i></a></td>
                                                </tr>
                                                <%}%>
                                            </tbody>
                                        </table>

                                    </div>
                                </div>
                            </div>
                            <%}%>
                        </div>
                    </div>

                </div>

            </fieldset> 

            <jsp:include page="/WEB-INF/jsp/BL/BLPreview.jsp" />


            <html:hidden  styleId ="savetype" property ="savetype"></html:hidden>
        </html:form>
    </div>
</div>
<div class="modal fade" id="myModalSearch" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <div class="modal-header bg-info text-white "> <h5 class="modal-title"> <bean:message key='ebooking.cust.custsearch' bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="myModalImportErrors" role="dialog">
    <div class="modal-dialog modal-lg" style="width:100%">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title">  <bean:message key='ebooking.cargo.impexcel' bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
                <div class="col-sm-12" id="importerr" style="height: 450px;overflow: auto;"></div>
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class="modal fade" id="myImportModal" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="row">
                <div class="col-md-12 column">
                    <div class="modal-header bg-info text-white">  <h5 class="modal-title">  <bean:message key='ebooking.cargo.impexcel' bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                            <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="form-group row">
                            <div class="col-sm-offset-0 col-sm-11">
                                <div class="custom-file">
                                    <input type="file" name="importexcel" id="importexcel" class='custom-file-input'>
                                    <label class="custom-file-label" for="">  <bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                                <!--                                <span class="pull-left">  <input type="file" name="importexcel" id="importexcel" > &nbsp;&nbsp;&nbsp;</span>-->
                            </div>
                            <div class="col-sm-offset-0 col-sm-11">
                                <span class="pull-left"> <label class="error" style="color:red"><bean:message key='ecom.bkg.xlfileformat' bundle="<%=clientcode%>"/> </label> </span>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="text-right">
                            <a href="javascript:void(0)" class="btn btn-info btn-xs" onclick='<%=(("Y".equalsIgnoreCase(Enable_Customize_Import_Export_Excel)) ? "ImportDataCustomize()" : "ImportData()")%>' id="importsearch"><i class="fa fa-upload"></i>  <%if ("Y".equalsIgnoreCase(Enable_Customize_Import_Export_Excel)) {%> Upload  Excel <%} else {%><bean:message key="ebooking.cargo.impexcel" bundle="<%=clientcode%>"/><%}%> </a>
                            <a href="javascript:void(0)"  data-dismiss="modal" class="btn btn-info btn-xs" title="Close" data-placement="right" onclick="closeModel();"><i class="fa fa-times-circle"></i> <bean:message key="ecom.close" bundle="<%=clientcode%>"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--marks and desc view modal-->
<div class="modal fade" id="markdescmodal" role="dialog" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="row">
                <div class="col-md-12 column">
                    <div class="modal-header bg-info text-white">  <h5 class="modal-title"> <bean:message key='ecom.shippingins.marksanddescview' bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                            <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <label class="col-sm-12"><bean:message key='shipping.marks.display' bundle="<%=clientcode%>"/></label>
                        </div>
                        <hr/>
                        <div class="form-group row">
                            <div class="col-sm-12">
                                <textarea name="modalmarkid" id="modalmarkid" value="" class="form-control" rows="3" readonly="true" style="background-color: #fff;"></textarea>
                            </div>
                        </div>
                        <div class="row">
                            <label class="col-sm-12"><bean:message key='ecom.shippingins.commdescview' bundle="<%=clientcode%>"/></label>
                        </div>
                        <hr/>
                        <div class="form-group row">
                            <div class="col-sm-12">
                                <textarea name="modaldescid" id="modaldescid" value="" class="form-control" rows="9" readonly="true" style="background-color: #fff;"></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="text-right"><a href="javascript:void(0)" title="Close"  class="btn btn-info btn-xs" onclick="markDescClose()" id="mailbtn"><bean:message key="ecom.close" bundle="<%=clientcode%>"/></a></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="blccmailmodal" role="dialog">
    <div class="modal-dialog modal-lg" >
        <div class="modal-content">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title"> <bean:message key='ebooking.msg.sendccmail' bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
                <form id="ccmailidform" action="" method="post" class="form-horizontal">

                    <div class="form-group row">
                        <label class="col-sm-9 control-label" style="text-align: left;"><bean:message key='ebooking.msg.ccmailid' bundle="<%=clientcode%>"/> :  <span class="label label-danger"><bean:message key='ebooking.msg.commasepmutiplemail' bundle="<%=clientcode%>"/></span></label>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-10">
                            <textarea class="form-control" id="ccmailidmodal" name="ccmailidmodal" rows="3"  maxlength="200" cols="50" style="resize:none"></textarea>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-9 control-label" style="text-align: left;color: #337ab7;"><bean:message key='ebooking.msg.updatemailid' bundle="<%=clientcode%>"/>.</label>
                    </div>
                </form>
                <br/>
                <div class="text-right">
                    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                    <button type="button" class="btn btn-primary" title="Access denied" disabled><bean:message key="ecom.but.sendtoagency" bundle="<%=clientcode%>"/></button>
                    <%} else {%>
                    <button type="button" class="btn btn-primary" onclick="submitBL();" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="templatebtn"><bean:message key="ecom.but.sendtoagency" bundle="<%=clientcode%>"/></button>
                    <%}%>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><bean:message key="ecom.close" bundle="<%=clientcode%>"/></button>
                </div>
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<div class="modal fade" id="ccalertmoal" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title">  <bean:message key="ecom.but.sendtoagency" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
                <form id="ccmailidform" action="" method="post" class="form-horizontal">
                    <div class="form-group row">
                        <label class="col-sm-12 control-label" style="text-align: left;"><bean:message key='ecom.msg.mailtocolleague' bundle="<%=clientcode%>"/></label>
                    </div>

                </form>
                <div class="text-right">
                    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                    <button type="button" class="btn btn-primary" title="Access denied" disabled>Yes</button>
                    <%} else {%>
                    <button type="button" class="btn btn-primary" onclick="submitwithcc();" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="templatebtn">Yes</button>
                    <%}%>
                    <button type="button" class="btn btn-default" onclick="submitwithoutcc();">No</button>
                </div>
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<!--Marks and desc modal view end-->
<div class="modal fade" id="copypastemodal" role="dialog" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="row">
                <div class="col-md-12 column">
                    <div class="modal-header bg-info text-white">  <h5 class="modal-title"> <bean:message key="bl.msg.marksnddesc" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                            <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="form-group row">
                            <label class="col-sm-12 control-label">
                                <input type="checkbox" id="copypasteall" name="copypasteall" onClick="copyPasteCBFunc();" value="1" style="margin-top:0.5%">
                                <bean:message key="bl.msg.cpytoalcnt" bundle="<%=clientcode%>"/>. </label>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-12 control-label">(OR)</label>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-12 control-label"><bean:message key="bl.availcont" bundle="<%=clientcode%>"/> </label>
                            <div class="col-sm-offset-0 col-sm-11">
                                <select multiple="true" id="availablecontainerList" class="availablecontainerList" name="availablecontainerList" size="6" style="height:150px; width:200px;">
                                </select>
                                <input type="hidden" id="copydmarksid" name="copydmarksid">
                                <input type="hidden" id="copycomdtyid" name="copycomdtyid">
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="text-right"><a href="javascript:void(0)" title="OK"  class="btn btn-info btn-xs" onclick="markDescPasteFunc()" id="mailbtn">OK</a></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--new customer modal-->
<div class="modal fade" id="newcustmodal" role="dialog">
    <div class="modal-dialog modal-lg" style="width:75%">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title"><bean:message key="ecom.shippingins.newcust" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="newcustform" role="form" name="newcustform"  autocomplete="off" method="post">
                    <input type="hidden" name="newcusttypeheadhid" id="newcusttypeheadhid"/>
                    <input type="hidden" name="newcustcountrycode" id="newcustcountrycode"/>
                    <input type="hidden" name="newcusttype" id="newcusttype"/>
                    <%if("Y".equalsIgnoreCase(GENS)){%>
                        <div class="form-group row "><div class="col-sm-12 text-right">
                            <span class="text-info font-weight-bold text-danger">The EORI number is required only for cargo destined for Europe.</span>
                            </div>
                        </div>
                    <%}%>
                    <div class="form-group row">
                        <label class="col-sm-2 control-label"> <bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/><span class="mandatory">*</span> </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border required" name="newcustname" id="newcustname" maxlength="150"/>
                        </div>
                        <label class="col-sm-2 control-label"> <bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/><span class="mandatory">*</span> </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border required" name="newcustaddr" id="newcustaddr" maxlength="150"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-2 control-label"> <bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/> </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border" name="newcustaddr1" id="newcustaddr1" maxlength="150"/>
                        </div>
                        <label class="col-sm-2 control-label"> <bean:message key="shipping.city.display" bundle="<%=clientcode%>"/><span class="mandatory">*</span> </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border required" name="newcustcity" id="newcustcity" maxlength="150"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-2 control-label"> <bean:message key="shipping.country.display" bundle="<%=clientcode%>"/><span class="mandatory">*</span> </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border required" name="newcustcountry" id="newcustcountry" onchange="loadStateAutoComplete('newcustcountry', 'newcuststate', 'typeheadhidden13');"/>
                        </div>

                        <label class="col-sm-2 control-label"> <bean:message key="Reg.State" bundle="<%=clientcode%>"/> </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border SIBL_state" name="newcuststate" id="newcuststate" onclick="validateState('newcustcountry', this);"/>
                        </div>

                    </div>
                    <div class="form-group row">

                        <label class="col-sm-2 control-label"> <bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/> </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border" name="newcustphone" id="newcustphone" maxlength="20"/>
                        </div>
                        <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                        <label class="col-sm-2 control-label"> <bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/> </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border" name="newcustfax" id="newcustfax" maxlength="20"/>
                        </div>
                        <%}%>
                        <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                        <label class="col-sm-2 control-label"> <bean:message key="ebooking.zipcode.display" bundle="<%=clientcode%>"/> </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border zip_code" name="newcustzipcode" id="newcustzipcode" maxlength="15"/>
                        </div>
                        <%}%>
                    </div>


                    <div class="form-group row">

                        <label class="col-sm-2 control-label consemail"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/> <span class="mandatory" id="newcustemailmand" >*</span></label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border email" name="newcustemail" id="newcustemail" maxlength="75"/>
                        </div>
                        <%if ("Y".equalsIgnoreCase(GENS)) {%>
                        <label class="col-sm-2 control-label "> <bean:message key="Reg.Eori" bundle="<%=clientcode%>"/></label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control light_border " name="newcusteori" id="newcusteori" maxlength="17"/>
                        </div>

                    </div>
                    <div class="form-group row">

                        <label class="col-sm-2 control-label "> <bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/><span class="mandatory">*</span> </label>
                        <div class="col-sm-4">
                            <!--                            <input type="text" class="form-control light_border email" name="newcustemail" id="newcustemail" maxlength="40"/>-->
                            <select id="newcusttypeperson" name="newcusttypeperson" class="form-control ebooking_typeperson required"    >
                                <option value="">Type of Person</option>
                                <option value="Legal">Legal</option>
                                <option value="Natural">Natural</option>
                                <option value="Association of persons">Association of persons</option>
                            </select>   
                        </div>
                        <%}%>
                    </div>

                    <div class="form-group row">
                        <div class="col-sm-5">
                        </div>
                        <div class="col-sm-4">
                            <button type="submit" class="btn  btn-success btn-sm" id="searchbtn"><span class="fa fa-plus"></span><bean:message key='specialprivi.add' bundle="<%=clientcode%>"/>  </button>
                            <button type="button" class="btn  btn-danger btn-sm" id="searchbtn" onclick="newCustModalReset();"><bean:message key='ecom.reset' bundle="<%=clientcode%>"/>  </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class="modal fade" id="simailmodal" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="row">
                <div class="col-md-12 column">
                    <div class="modal-header bg-info text-white">  <h5 class="modal-title"> <bean:message key='ecom.si.generateemail' bundle="<%=clientcode%>"/>
                        </h5>  <div class="pull-right">
                            <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="form-group row">
                            <label class="col-sm-2"><bean:message key='rr.toDate' bundle="<%=clientcode%>"/> : </label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control light_border" name="tomail" id="tomail" value="<%=agencyemailid%>"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-2"><bean:message key='ViewProfile.userifo.subject' bundle="<%=clientcode%>"/>: </label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control light_border" name="subjectmail" id="subjectmail" value="BL Changes Request : <%=(String) session.getAttribute("edit_blno")%>" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-2"><bean:message key='ecom.si.mailbody' bundle="<%=clientcode%>"/> : </label>
                            <div class="col-sm-10">
                                <textarea name="bodymail" style="width: 630px;height: 180px;overflow:auto;" class="light_border" id="bodymail">
                                </textarea>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <div class="text-right"><a href="javascript:void(0)" title="Close"  class="btn btn-info btn-xs" onclick="mailDialogClose();" id="mailbt1"><bean:message key="ecom.close" bundle="<%=clientcode%>"/></a> <a href="javascript:void(0)" title="Send Mail"  class="btn btn-info btn-xs" onclick="sendSIMail()" id="sendmail"><bean:message key='ecom.mail' bundle="<%=clientcode%>"/>
                            </a></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="comdtySearchModal" role="dialog">
    <div class="modal-dialog modal-xl" >
        <div class="modal-content">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title"><bean:message key='ebooking.cargo.commsearch' bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">

            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<!--<script type="text/javascript" src="Design/app-assets/vendors/js/extensions/jquery.steps.min.js"></script>-->
<div class="modal fade" id="smartImportModal" role="dialog">
    <div class="modal-dialog modal-xl" >
        <div class="modal-content">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title">Smart Import</h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body" style="padding: 3px 10px 3px 10px !important;">
                <div class="container-full p-l-15 p-r-15">
                    <div class="row">
                        <div id="siblsmartimportbox">
                            <div id="siblsmartimport">

                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <div class="modal-footer">
                <div class="d-flex justify-content-between align-items-center w-100">
                    <div><span> Note: </span><span class="text-info">Double-click a cell to edit its contents.</span></div>
                    <a href="javascript:void(0)" title="Update"  class="btn btn-info btn-xs" onclick="getSmartImportData()" id="smartimpupdate">Update </a>
                </div>
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class='d-none'>
    <form action="FileAjax" method="post" id='filedownloadform'>
        <input type="hidden" id="BKGfilename" name="filename"/>
        <input type="hidden" id="BKGtype" name="type"/>
        <input type="hidden" id="BKGrefno" name="refno"/>
    </form>
</div>
<script type='text/javascript' src='js/SIPreviewScript.js?<%=jsdate%>'></script>
<script type='text/javascript' src='js/BLexcelImport.js?<%=jsdate%>'></script>
<script type='text/javascript' src='js/SIBLCommonJS.js?<%=jsdate%>'></script>
<script type='text/javascript' src='js/smartImport.js?<%=jsdate%>'></script>
<script type="text/javascript">
                        var termsconditionshtml = "<div id='termsconditionhidden'><div class='w-100 h-100'><div class='card-header page_header' ><img class='header_icon_pages' src='Design/assets/images/page_icons/schdule.png'> <bean:message key='ecom.termsandcond.head' bundle="<%=clientcode%>"/> </div> <div style='max-height: 495px;overflow-y: scroll;' ><%=termsandconditions%></div>  <div class='text-center'> <a href='javascript:;' class='btn btn-info btn-lg m-t-5 m-b-5 text-white' style='width: 200px;' onclick='agreebtnclicked()'> <bean:message key='ecom.but.iagree' bundle="<%=clientcode%>"/> </a> </div> </div> </div>";</script>
<script type="text/javascript">

    function showtermsconditions() {
        $.fancybox.open(termsconditionshtml, {
            padding: 0,
            showCloseButton: false,
            closeClick: false,
            maxWidth: 1200,
            maxHeight: 600,
            fitToView: true,
            async: false,
            width: '90%',
            height: '100%',
            autoSize: true,
            scrollOutside: true,
            closeBtn: true,
            hideOnOverlayClick: false,
            overlayShow: false,
            helpers: {
                overlay: {closeClick: false}
            }

        });
    }
    function agreebtnclicked() {
        if (checkMailConfig == 'Y') {
            $('#ccalertmoal').modal('show');
        } else {
            submitwithoutcc();
        }
        $.fancybox.close();
    }
</script>
<script type="text/javascript">
    var ccMailId = '<%=ccMailId%>';
    function trim(value) {
        return LTrim(RTrim(value));
    }
    function LTrim(value) {
        var re = /\s*((\S+\s*)*)/;
        return value.replace(re, "$1");
    }
    // Removes ending whitespaces
    function RTrim(value) {
        var re = /((\s*\S+)*)\s*/;
        return value.replace(re, "$1");
    }
    function backtosiview() {
        // callLoading();
        location.href = "ViewBookingAll.do?BookingRequestType=oblprint";
    }
    setShipperSealMandatory(shipperSealconfig);
    function markDescView(marksNoId, descId) {
        var markval = $('#' + marksNoId).val();
        var descval = $('#' + descId).val();
        $('#modalmarkid').val(markval);
        $('#modaldescid').val(descval);
        $("#markdescmodal").modal("show");
    }
    function markDescClose() {
        $("#markdescmodal").modal("hide");
    }
    function openCollapseFun(id) {
        if ($('#' + id).css('display') == 'none') {
            $('#' + id).slideDown();
            $('#' + id).css("visibility", "inherit");
            $('#' + id).parent().find(".fa-plus-square-o").removeClass("fa-plus-square-o").addClass("fa-minus-square-o");
        } else {
            $('#' + id).slideUp();
            $('#' + id).parent().find(".fa-minus-square-o").removeClass("fa-minus-square-o").addClass("fa-plus-square-o");
        }

    }
    var substringMatcher = function (url) {
        return function findMatches(q, cb) {
            var matches, substrRegex, strs;
            var arr1 = new Array();
            $.ajax({
                url: url,
                type: 'post',
                async: false,
                success: function (data) {
                    if (data != null) {
                        var arr = data.split("~~");
                        for (var i = 1; i < arr.length; i++) {
                            arr1[i] = arr[i];
                        }
                        strs = arr1;
                    }
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
            matches = [];
            substrRegex = new RegExp(q, 'i');
            $.each(strs, function (i, str) {
                if (substrRegex.test(str)) {
                    matches.push({value: str});
                }
            });
            cb(matches);
        };
    };
    function setSaveType(type) {
        // Validate BL Type UAE - mandatory field
        if ($('#blType_UAE').length > 0 && $('#blType_UAE').is(':visible') && $('#blType_UAE').val() == '') {
            $('#blType_UAE').addClass('error');
            var errorLabel = $('#blType_UAE').parent().find('label.error');
            if (errorLabel.length == 0) {
                $('#blType_UAE').after('<label id="blType_UAE-error" class="error" style="display:block;"><bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/></label>');
            } else {
                errorLabel.show();
            }
            return;
        }
        // Validate MPCI ID - mandatory when BL Type is Master BL
        if ($('#mpciId_UAE').length > 0 && $('#mpciId_UAE').hasClass('required') && $('#mpciId_UAE').is(':visible') && $('#mpciId_UAE').val().trim() == '') {
            $('#mpciId_UAE').addClass('error');
            var errorLabel = $('#mpciId_UAE').parent().find('label.error');
            if (errorLabel.length == 0) {
                $('#mpciId_UAE').after('<label id="mpciId_UAE-error" class="error" style="display:block;"><bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/></label>');
            } else {
                errorLabel.show();
            }
            return;
        }
        $('.selectcls').attr("disabled", false);
        $('.socslt').attr("disabled", false);
        $(".SI_Process_Restriction_Cust").prop('disabled', false);
        $(".si_reefer_option").prop('disabled', false);
        var code1, dataVal;
        document.getElementById('savetype').value = type;
        dataVal = $("#ebook_shippercountry_name").val();
        code1 = trim(dataVal.substring(0, dataVal.indexOf('-')));
        $("#ebook_shippercountry").val(code1);
        dataVal = $("#ebook_consigneecountry_name").val();
        code1 = trim(dataVal.substring(0, dataVal.indexOf('-')));
        $("#ebook_consigneecountry").val(code1);
        dataVal = $("#ebook_notifycountry_name").val();
        code1 = trim(dataVal.substring(0, dataVal.indexOf('-')));
        $("#ebook_notifycountry").val(code1);
    <% if (custtypelist.contains("N1")) {%>
        dataVal = $("#ebook_notify2country_name").val();
        code1 = trim(dataVal.substring(0, dataVal.indexOf('-')));
        $("#ebook_notify2country").val(code1);
    <%}%>
    <% if (custtypelist.contains("N2")) {%>
        dataVal = $("#ebook_notify3country_name").val();
        code1 = trim(dataVal.substring(0, dataVal.indexOf('-')));
        $("#ebook_notify3country").val(code1);
        if ($('#consigneeorder').is(':checked')) {
            $('#ebook_consigneename').val($('#ebook_consigneenameheader').val());
        }
    <%}%>
    <% if (custtypelist.contains("FW")) {%>
        dataVal = $("#ebook_forwardercountry_name").val();
        code1 = trim(dataVal.substring(0, dataVal.indexOf('-')));
        $("#ebook_forwardercountry").val(code1);
    <%}%>
        if ($("#ebook_buyercountry_name").length > 0) {
            dataVal = $("#ebook_buyercountry_name").val();
            code1 = trim(dataVal.substring(0, dataVal.indexOf('-')));
            $("#ebook_buyercountry").val(code1);
        }
        if ($("#ebook_sellercountry_name").length > 0) {
            dataVal = $("#ebook_sellercountry_name").val();
            code1 = trim(dataVal.substring(0, dataVal.indexOf('-')));
            $("#ebook_sellercountry").val(code1);
        }
           validationENSDocType('Buyer');
           validationENSDocType('Seller');
        document.getElementById('SIEditForm').action = "SubmitBL.do";
    }

    $(document).ready(function () {
        $('#sicustremarks').attr('maxlength', "450");
        $('[data-toggle="tooltip"]').tooltip();
        $('.v1hscommodity').each(function () {
            if ($('#appVersion').val() == '1') {
                $(this).attr('maxlength', "100");
            }
        });
        $("#newcustform").validate({
            rules: {
                newcustphone: {
                    validnumbers: "#ebook_shipperphone",
                    ZeroTest: "#ebook_shipperphone",
                    phonefax: ["#ebook_shipperphone"],
                    required: function (element) {
                        return($("#newcusteori").length > 0 && $("#newcusteori").val() == "" && checkEUCountry('newcustcountry'))
                    }

                },
                newcustfax: {
                    validnumbers: "#ebook_shipperphone",
                    ZeroTest: "#ebook_shipperphone",
                    phonefax: ["#ebook_shipperphone"]

                },
                newcustemail: {
                    required: function (element) {
                        return($("#newcusteori").length > 0 && $("#newcusteori").val() == "" && checkEUCountry('newcustcountry'))
                    }
                },
                newcusttypeperson: {
                    required: function (element) {
                        return($("#newcusttypeperson").length > 0)
                    }
                },
                newcustzipcode: {
                    required: function (element) {
                        return($("#newcustzipcode").length > 0 && $("#newcustname").val().trim().length > 0 && $("#ensFillingType").length > 0)
                    }
                },
                newcusteori: {
                    required: function (element) {
                        return($("#newcusteori").length > 0 && $("#newcustcountry").val().length > 0 && $("#ensFillingType").length > 0 && checkEUCountry('newcustcountry'))
                    }
                }
            },
            messages: {
                newcustphone: {
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                newcustemail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                newcustfax: {
                    validnumbers: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                },
                newcustaddr: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                newcustzipcode: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                }
            },
            submitHandler: function (form) {
                addNewCustomer('<%=printOnBlAdd%>');
                return false;
            }
        });
        var formcustadd = $("#newcustform").show();
        formcustadd.validate().settings.ignore = ":disabled,:hidden,[readonly=readonly]";
        $("#SIEditForm").validate({
            rules: {
                /* notify 2*/
                np2name: {
                    required: function (element) {
                        return  $("#ebook_notify2name").val() != "" || $("#ebook_notify2address").val() != "" || $("#ebook_notify2address1").val() != "" || $("#ebook_notify2country_name").val() != "" || $("#ebook_notify2city").val() != "" || $("#ebook_notify2phone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notify2fax").val() != "") || $("#ebook_notify2email").val() != "";
                    },
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneename" : "#ebook_notify2name";
                    }
                },
                np2addr: {
                    required: function (element) {
                        return ($("#ebook_notify2name").val() != "" || $("#ebook_notify2address").val() != "" || $("#ebook_notify2address1").val() != "" || $("#ebook_notify2country_name").val() != "" || $("#ebook_notify2city").val() != "" || $("#ebook_notify2phone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notify2fax").val() != "") || $("#ebook_notify2email").val() != "");
                    },
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneeaddress" : "#ebook_notify2address";
                    }
                },
                np2CountryName: {
                    required: function (element) {
                        return  ($("#ebook_notify2name").val() != "" || $("#ebook_notify2address").val() != "" || $("#ebook_notify2address1").val() != "" || $("#ebook_notify2country_name").val() != "" || $("#ebook_notify2city").val() != "" || $("#ebook_notify2phone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notify2fax").val() != "") || $("#ebook_notify2email").val() != "");
                    },
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneecountry_name" : "#ebook_notify2country_name";
                    }
                },
                np2city: {
                    required: function (element) {
                        return  ($("#ebook_notify2name").val() != "" || $("#ebook_notify2address").val() != "" || $("#ebook_notify2address1").val() != "" || $("#ebook_notify2country_name").val() != "" || $("#ebook_notify2city").val() != "" || $("#ebook_notify2phone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notify2fax").val() != "") || $("#ebook_notify2email").val() != "");
                    },
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneecity" : "#ebook_notify2city";
                    }
                },
                np2phone: {
                    validnumbers: "#ebook_notify2phone",
                    ZeroTest: "#ebook_notify2phone",
                    phonefax: ["#ebook_notify2phone"],
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneephone" : "#ebook_notify2phone";
                    },
                    required: function (element) {

                        return(($("#ebook_notify2name").length > 0 && $("#ebook_notify2name").val().trim().length > 0) && $("#ebook_notify2eori").length > 0 && $("#ebook_notify2eori").val() == "" && isNewCustomer("notify2CustCode") && checkEUCountry('ebook_notify2country_name'))

                    }
                },
                np2fax: {
                    validnumbers: "#ebook_notify2fax",
                    ZeroTest: "#ebook_notify2fax",
                    phonefax: ["#ebook_notify2fax"],
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneefax" : "#ebook_notify2fax";
                    }
                },
                np2email: {
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneeemail" : "#ebook_notify2email";
                    },
                    required: function (element) {

                        return(($("#ebook_notify2name").length > 0 && $("#ebook_notify2name").val().trim().length > 0) && $("#ebook_notify2eori").length > 0 && $("#ebook_notify2eori").val() == "" && isNewCustomer("notify2CustCode") && checkEUCountry('ebook_notify2country_name'));

                    }
                },
                np2persontype: {
                    required: function (element) {
                        return($("#notify2typeperson").length > 0 &&  $("#ebook_notify2name").val().trim().length > 0)
                    }
                },
                np2zipcode: {
                    required: function (element) {
                        return($("#ebook_notify2zipcode").length > 0 && $("#ebook_notify2name").val().trim().length > 0 && $("#ensFillingType").length > 0)
                    }
                },
                /* notify 3*/

                np3name: {
                    required: function (element) {
                        return  $("#ebook_notify3name").val() != "" || $("#ebook_notify3address").val() != "" || $("#ebook_notify3address1").val() != "" || $("#ebook_notify3country_name").val() != "" || $("#ebook_notify3city").val() != "" || $("#ebook_notify3phone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notify3fax").val() != "") || $("#ebook_notify3email").val() != "";
                    },
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneename" : "#ebook_notify3name";
                    }
                },
                np3addr: {
                    required: function (element) {
                        return  ($("#ebook_notify3name").val() != "" || $("#ebook_notify3address").val() != "" || $("#ebook_notify3address1").val() != "" || $("#ebook_notify3country_name").val() != "" || $("#ebook_notify3city").val() != "" || $("#ebook_notify3phone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notify3fax").val() != "") || $("#ebook_notify3email").val() != "");
                    },
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneeaddress" : "#ebook_notify3address";
                    }
                },
                np3CountryName: {
                    required: function (element) {
                        return ($("#ebook_notify3name").val() != "" || $("#ebook_notify3address").val() != "" || $("#ebook_notify3address1").val() != "" || $("#ebook_notify3country_name").val() != "" || $("#ebook_notify3city").val() != "" || $("#ebook_notify3phone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notify3fax").val() != "") || $("#ebook_notify3email").val() != "");
                    },
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneecountry_name" : "#ebook_notify3country_name";
                    }
                },
                np3city: {
                    required: function (element) {
                        return  ($("#ebook_notify3name").val() != "" || $("#ebook_notify3address").val() != "" || $("#ebook_notify3address1").val() != "" || $("#ebook_notify3country_name").val() != "" || $("#ebook_notify3city").val() != "" || $("#ebook_notify3phone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_notify3fax").val() != "") || $("#ebook_notify3email").val() != "");
                    },
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneecity" : "#ebook_notify3city";
                    }
                },
                np3phone: {
                    validnumbers: "#ebook_notifyphone",
                    ZeroTest: "#ebook_notifyphone",
                    phonefax: ["#ebook_notifyphone"],
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneephone" : "#ebook_notify3phone";
                    },
                    required: function (element) {
                        return($("#notify3CustCode").length > 0 && $("#ebook_notify3name").val().trim().length > 0 && $("#ebook_notify3eori").length > 0 && $("#ebook_notify3eori").val() == "" && isNewCustomer("notify3CustCode") && checkEUCountry('ebook_notify3country_name'));

                    }
                },
                np3fax: {
                    validnumbers: "#ebook_notifyfax",
                    ZeroTest: "#ebook_notifyfax",
                    phonefax: ["#ebook_notifyfax"],
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneefax" : "#ebook_notify3fax";
                    }
                },
                np3email: {
                    equalTo: function (element) {
                        return $('#hidn1check').val() == '1' ? "#ebook_consigneeemail" : "#ebook_notify3email";
                    },
                    required: function (element) {

                        return($("#ebook_notify3name").val().trim().length > 0 && $("#ebook_notify3eori").length > 0 && $("#ebook_notify3eori").val() == "" && isNewCustomer("notify3CustCode") && checkEUCountry('ebook_notify3country_name'));

                    }
                },
                np3persontype: {
                    required: function (element) {
                        return($("#notify3typeperson").length > 0 && $("#ebook_notify3name").val().trim().length > 0)
                    }
                },
                np3zipcode: {
                    required: function (element) {
                        return($("#ebook_notify3zipcode").length > 0 && $("#ebook_notify3name").val().trim().length > 0 && $("#ensFillingType").length > 0)
                    }
                },
                //FW
                fwname: {
                    required: function (element) {
                        return  $("#ebook_forwardername").val() != "" || $("#ebook_forwarderaddress").val() != "" || $("#ebook_forwarderaddress1").val() != "" || $("#ebook_forwardercountry_name").val() != "" || $("#ebook_forwardercity").val() != "" || $("#ebook_forwarderphone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_forwarderfax").val() != "") || $("#ebook_forwarderemail").val() != "";
                    }
                },
                fwaddr: {
                    required: function (element) {
                        return  ($("#ebook_forwardername").val() != "" || $("#ebook_forwarderaddress").val() != "" || $("#ebook_forwarderaddress1").val() != "" || $("#ebook_forwardercountry_name").val() != "" || $("#ebook_forwardercity").val() != "" || $("#ebook_forwarderphone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_forwarderfax").val() != "") || $("#ebook_forwarderemail").val() != "");
                    }
                },
                fwCountryName: {
                    required: function (element) {
                        return  ($("#ebook_forwardername").val() != "" || $("#ebook_forwarderaddress").val() != "" || $("#ebook_forwarderaddress1").val() != "" || $("#ebook_forwardercountry_name").val() != "" || $("#ebook_forwardercity").val() != "" || $("#ebook_forwarderphone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_forwarderfax").val() != "") || $("#ebook_forwarderemail").val() != "");
                    }
                },
                fwcity: {
                    required: function (element) {
                        return  ($("#ebook_forwardername").val() != "" || $("#ebook_forwarderaddress").val() != "" || $("#ebook_forwarderaddress1").val() != "" || $("#ebook_forwardercountry_name").val() != "" || $("#ebook_forwardercity").val() != "" || $("#ebook_forwarderphone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_forwarderfax").val() != "") || $("#ebook_forwarderemail").val() != "");
                    }
                },
                fwphone: {
                    validnumbers: "#ebook_forwarderphone",
                    ZeroTest: "#ebook_forwarderphone",
                    phonefax: ["#ebook_forwarderphone"],
                    required: function (element) {
                        return($("#ebook_forwardername").val().trim().length > 0 && $("#ebook_forwardereori").length > 0 && $("#ebook_forwardereori").val() == "" && isNewCustomer("forwarderCustCode") && checkEUCountry('ebook_forwardercountry_name'));
                    }
                },
                fwfax: {
                    validnumbers: "#ebook_forwarderfax",
                    ZeroTest: "#ebook_forwarderfax",
                    phonefax: ["#ebook_forwarderfax"],
                },
                fwzipcode: {
                    required: function (element) {
                        return($("#ebook_forwarderzipcode").length > 0 && $("#ebook_forwardername").val().trim().length > 0 && $("#ensFillingType").length > 0)
                    }
                },
                //WH
                whname: {
                    required: function (element) {
                        return  $("#ebook_Warehousename").val() != "" || $("#ebook_Warehouseaddress").val() != "" || $("#ebook_Warehouseaddress1").val() != "" || $("#ebook_Warehousecountry_name").val() != "" || $("#ebook_Warehousecity").val() != "" || $("#ebook_Warehousephone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_Warehousefax").val() != "") || $("#ebook_Warehouseemail").val() != "";
                    }
                },
                whaddr: {
                    required: function (element) {
                        return  ($("#ebook_Warehousename").val() != "" || $("#ebook_Warehouseaddress").val() != "" || $("#ebook_Warehouseaddress1").val() != "" || $("#ebook_Warehousecountry_name").val() != "" || $("#ebook_Warehousecity").val() != "" || $("#ebook_Warehousephone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_Warehousefax").val() != "") || $("#ebook_Warehouseemail").val() != "");
                    }
                },
                whCountryName: {
                    required: function (element) {
                        return  ($("#ebook_Warehousename").val() != "" || $("#ebook_Warehouseaddress").val() != "" || $("#ebook_Warehouseaddress1").val() != "" || $("#ebook_Warehousecountry_name").val() != "" || $("#ebook_Warehousecity").val() != "" || $("#ebook_Warehousephone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_Warehousefax").val() != "") || $("#ebook_Warehouseemail").val() != "");
                    }
                },
                whcity: {
                    required: function (element) {
                        return  ($("#ebook_Warehousename").val() != "" || $("#ebook_Warehouseaddress").val() != "" || $("#ebook_Warehouseaddress1").val() != "" || $("#ebook_Warehousecountry_name").val() != "" || $("#ebook_Warehousecity").val() != "" || $("#ebook_Warehousephone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_Warehousefax").val() != "") || $("#ebook_Warehouseemail").val() != "");
                    }
                },
                whphone: {
                    validnumbers: "#ebook_Warehousephone",
                    ZeroTest: "#ebook_Warehousephone",
                    phonefax: ["#ebook_Warehousephone"],
                },
                whfax: {
                    validnumbers: "#ebook_Warehousefax",
                    ZeroTest: "#ebook_Warehousefax",
                    phonefax: ["#ebook_Warehousefax"],
                },
                whzipcode: {
                    required: function (element) {
                        return($("#ebook_Warehousezipcode").length > 0 && $("#ebook_Warehousename").val().trim().length > 0 && $("#ensFillingType").length > 0)
                    }
                },
                siAESnumber: {
                    required: function (element) {
                        return  ($("#siAESnumber").val() == '' && (!$('input[name=AESDisclaimer]').is(':checked')) && $("#siAESnumber").val().length == 0 && $("#siAESnumber").is(":visible"));
                    }
                },
                fwemail: {
                    required: function (element) {

                        return($("#ebook_forwardername").val().trim().length > 0 && $("#ebook_forwardereori").length > 0 && $("#ebook_forwardereori").val() == "" && isNewCustomer("forwarderCustCode") && checkEUCountry('ebook_forwardercountry_name'))

                    }
                },
                fwpersontype: {
                    required: function (element) {
                        return($("#forwardertypeperson").length > 0 && $("#ebook_forwardername").val().trim().length > 0)
                    }
                },
                declarantName: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() > 4);
                    }
                },
                declarantAddress: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() > 4);
                    }
                },
                declarantAddress1: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() > 4);
                    }
                },
                declarantCity: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() > 4);
                    }
                },
                declarantCountry: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() > 4);
                    }
                },
                declarantState: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() > 4);
                    }
                },
                declarantTelephone: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() > 4);
                    }
                },
                declarantEORI: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() > 4);
                    }
                },
                declarantEmail: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() > 4);
                    }
                },
                declarantFillingType: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() > 4);
                    }
                },
                declarantZipcode: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Multiple Filing' && $("#stepWizardNewIndex").val() > 4);
                    }
                },
                //BY
                byname: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() > 4); //($("#ebook_buyername").val() != "" || $("#ebook_buyeraddress").val() != "" || $("#ebook_buyeraddress1").val() != "" || $("#ebook_buyercountry_name").val() != "" || $("#ebook_buyercity").val() != "" || $("#ebook_buyerphone").val() != "" || ('Y' != '<%=HideFaxNo%>' && $("#ebook_buyerfax").val() != "") || $("#ebook_buyeremail").val() != "");
                    }

                },
                byaddr: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() > 4);
                    }

                },
                byCountryName: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() > 4);
                    }

                },
                bycity: {
                    required: function (element) {
                        return  ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() > 4);
                    }

                },
                byphone: {
                    validnumbers: function () {
                        return $("#stepWizardNewIndex").val() > 4 ? "#ebook_buyerphone" : "";
                    },
                    ZeroTest: function () {
                        return $("#stepWizardNewIndex").val() > 4 ? "#ebook_buyerphone" : "";
                    },
                    phonefax: function () {
                        return ($("#stepWizardNewIndex").val() > 4 ? ["#ebook_buyerphone"] : []);
                    },
                    required: function (element) {
                        return ($("#buyerCustCode").length > 0 && $("#stepWizardNewIndex").val() > 4 && $("#ebook_buyername").val().length > 0 && $("#ebook_buyereori").val() == "" && isNewCustomer("buyerCustCode") && checkEUCountry('ebook_buyercountry_name'));
                    }
                },
                byfax: {
                    validnumbers: function () {
                        return $("#stepWizardNewIndex").val() > 4 ? "#ebook_buyerfax" : "";
                    },
                    ZeroTest: function () {
                        return $("#stepWizardNewIndex").val() > 4 ? "#ebook_buyerfax" : "";
                    },
                    phonefax: function () {
                        return ($("#stepWizardNewIndex").val() > 4 ? ["#ebook_buyerfax"] : []);
                    }
                },
                byemail: {
                    required: function (element) {
                        return ($("#buyerCustCode").length > 0 && $("#stepWizardNewIndex").val() > 4 && $("#ebook_buyername").val().length > 0 && $("#ebook_buyereori").val() == "" && isNewCustomer("buyerCustCode") && checkEUCountry('ebook_buyercountry_name'));
                    }
                },
                bypersontype: {
                    required: function (element) {
                        return($("#buyertypeperson").length > 0 && $("#stepWizardNewIndex").val() > 4 && $("#ebook_buyername").val().trim().length > 0)
                    }
                },
                byzipcode: {
                    required: function (element) {
                        return($("#ebook_buyerzipcode").length > 0 && $("#stepWizardNewIndex").val() > 4  && $("#ebook_buyername").val().trim().length > 0)
                    }
                },
                //SL
                slname: {
                    required: function (element) {
                        return   ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() > 4);
                    }

                },
                sladdr: {
                    required: function (element) {
                        return   ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() > 4);
                    }

                },
                slCountryName: {
                    required: function (element) {
                        return   ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() > 4);
                    }

                },
                slcity: {
                    required: function (element) {
                        return   ($("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() > 4);
                    }

                },
                slphone: {
                    validnumbers: function () {
                        return $("#stepWizardNewIndex").val() > 4 ? "#ebook_sellerphone" : "";
                    },
                    ZeroTest: function () {
                        return $("#stepWizardNewIndex").val() > 4 ? "#ebook_sellerphone" : "";
                    },
                    phonefax: function () {
                        return ($("#stepWizardNewIndex").val() > 4 ? ["#ebook_sellerphone"] : []);

                    },
                    required: function (element) {
                        return ($("#sellerCustCode").length > 0 && $("#stepWizardNewIndex").val() > 4 && $("#ebook_sellername").val().length > 0 && $("#ebook_sellereori").val() == "" && isNewCustomer("sellerCustCode") && checkEUCountry('ebook_sellercountry_name'));
                    }
                },
                slfax: {
                    validnumbers: function () {
                        return $("#stepWizardNewIndex").val() > 4 ? "#ebook_sellerfax" : "";
                    },
                    ZeroTest: function () {
                        return $("#stepWizardNewIndex").val() > 4 ? "#ebook_sellerfax" : "";
                    },
                    phonefax: function () {
                        return ($("#stepWizardNewIndex").val() > 4 ? ["#ebook_sellerfax"] : []);

                    }
                },
                slemail: {
                    required: function (element) {
                        return ($("#sellerCustCode").length > 0 && $("#stepWizardNewIndex").val() > 4 && $("#ebook_sellername").val().length > 0 && $("#ebook_sellereori").val() == "" && isNewCustomer("sellerCustCode") && checkEUCountry('ebook_sellercountry_name'));
                    }
                },
                slpersontype: {
                    required: function (element) {
                        return($("#sellertypeperson").length > 0 && $("#stepWizardNewIndex").val() > 4 && $("#ebook_sellername").val().trim().length > 0)
                    }
                },
                slzipcode: {
                    required: function (element) {
                        return($("#ebook_sellerzipcode").length > 0 && $("#stepWizardNewIndex").val() > 4  && $("#ebook_sellername").val().trim().length > 0)
                    }
                },
                np2eori: {
                    required: function (element) {
                        return($("#ebook_notify2eori").length > 0 && $("#ebook_notify2country_name").val().length > 0 && $("#ensFillingType").length > 0 && checkEUCountry('ebook_notify2country_name'))
                    }
                },
                np3eori: {
                    required: function (element) {
                        return($("#ebook_notify3eori").length > 0 && $("#ebook_notify3country_name").val().length > 0 && $("#ensFillingType").length > 0 && checkEUCountry('ebook_notify3country_name'))
                    }
                },
                fweori: {
                    required: function (element) {
                        return($("#ebook_forwardereori").length > 0 && $("#ebook_forwardercountry_name").val().length > 0 && $("#ensFillingType").length > 0 && checkEUCountry('ebook_forwardercountry_name'))
                    }
                },
                byeori: {
                    required: function (element) {
                        return  ($("#ebook_buyereori").length > 0 && $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() > 4 && $("#ebook_buyercountry_name").val().length > 0 && checkEUCountry('ebook_buyercountry_name'));
                    }
                },
                sleori: {
                    required: function (element) {
                        return  ($("#ebook_sellereori").length > 0 && $("#ensFillingType").length > 0 && $("#ensFillingType").val() == 'Single Filing' && $("#stepWizardNewIndex").val() > 4 && $("#ebook_sellercountry_name").val().length > 0 && checkEUCountry('ebook_sellercountry_name'));
                    }
                }
            },
            messages: {
                npCountryName: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                consigneeSelect: {
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                consname: {
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                consaddr: {
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                conscity: {
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                consphone:
                        {
                            ZeroTest: "<bean:message key="ecom.msg.sicustfax" bundle="<%=clientcode%>"/>",
                            equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                        },
                consemail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                consCountryName: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                np3addr: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                fwaddr: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                npcity:
                        {
                            equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                        },
                np2city:
                        {
                            equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                        },
                np3city:
                        {
                            equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                        },
                spCountryName: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                }, npname: {
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                }, npaddr: {
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                npphone: {
                    ZeroTest: "<bean:message key="ecom.msg.sicustfax" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>"
                },
                npfax: {
                    ZeroTest: "<bean:message key="ecom.msg.sicustfax" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                npemail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                np2name: {
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                np2addr: {
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                np2phone: {
                    ZeroTest: "<bean:message key="ecom.msg.sicustfax" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>"
                },
                np2fax: {
                    ZeroTest: "<bean:message key="ecom.msg.sicustfax" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                np2email: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                np3name: {
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                npaddr: {
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                np3phone: {
                    ZeroTest: "<bean:message key="ecom.msg.sicustfax" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                np3fax: {
                    ZeroTest: "<bean:message key="ecom.msg.sicustfax" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                np3email: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                consfax: {
                    ZeroTest: "<bean:message key="ecom.msg.sicustfax" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                consphone: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    equalTo: "<bean:message key="ecom.msg.bkg.sameascons" bundle="<%=clientcode%>"/>"
                },
                fwphone: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>"
                },
                fwfax: {
                    ZeroTest: "<bean:message key="ecom.msg.sicustfax" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"


                },
                fwemail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                whemail: {
                    email: "<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>"
                },
                whphone: {
                    ZeroTest: "<bean:message key="ecom.msg.sicustfax" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>"
                },
                whfax: {
                    ZeroTest: "<bean:message key="ecom.msg.sicustfax" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                },
                spphone: {
                    ZeroTest: "<bean:message key="ecom.msg.sicustfax" bundle="<%=clientcode%>"/>",
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>"
                },
                spfax: {
                    ZeroTest: "<bean:message key="ecom.msg.sicustfax" bundle="<%=clientcode%>"/>",
                    phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                },
                siAESnumber: {
                    required: "Enter AES Number or check the disclaimer"
                }
            }
        });
        $('.numberval').each(function () {
            $(this).rules('add', {
                numberval: ["#ebook_shipperfax"],
                messages: {
                    numberval: "<bean:message key="ecom.msg.entvalidnum" bundle="<%=clientcode%>"/>"
                }
            });
        });
        $('.numbervalpoint').each(function () {
            $(this).rules('add', {
                numbervalpoint: ["#ebook_shipperfax"],
                messages: {
                    numbervalpoint: "<bean:message key="ecom.msg.entvalidnum" bundle="<%=clientcode%>"/>"
                }
            });
        });
        var form = $("#SIEditForm").show();
        form.steps({
            headerTag: "h6",
            bodyTag: "fieldset",
            autoFocus: true,
            enableAllSteps: true,
            transitionEffect: "fade",
            transitionEffectSpeed: 500,
            titleTemplate: '<span class="step">#index#</span> #title#',
            labels: {
                finish: "<bean:message key="ecom.but.sendcrction" bundle="<%=clientcode%>"/>",
                next: "<bean:message key="ecom.next" bundle="<%=clientcode%>"/>  <span class=\"glyphicon glyphicon-arrow-right\"></span>",
                previous: " <span class=\"glyphicon glyphicon-arrow-left\"></span> <bean:message key="ecom.prev" bundle="<%=clientcode%>"/>"
            },
            onInit: function (event, current)
            {
                //  $(window).load(function () {
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_shippercountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden2").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden2").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden2").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_shippercountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                //     });
                //
                //     $(window).load(function () {
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#newcustcountry').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#newcusttypeheadhid").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#newcusttypeheadhid").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#newcusttypeheadhid").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#newcusttypeheadhid").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#newcustcountry').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                //     });
                // ebook_consigneecountry_name
                //      $(window).load(function () {
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_consigneecountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden3").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden3").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden3").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden3").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_consigneecountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                //     });


                // ebook_notifycountry_name
                //     $(window).load(function () {
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_notifycountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country2',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden4").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden4").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden4").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden4").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_notifycountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                //       });
                // end
                // ebook_notifycountry_name2
                //     $(window).load(function () {
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_notify2country_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country3',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden6").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden6").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden6").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden6").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_notify2country_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_forwardercountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country3',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden8").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden8").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden8").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden8").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_forwardercountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_Warehousecountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country6',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden9").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden9").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden9").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden9").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_Warehousecountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                //     });
                // end
                // ebook_notifycountry_name3
                //      $(window).load(function () {
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_notify3country_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'country4',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden7").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden7").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden7").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden7").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_notify3country_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_buyercountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'countryby',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden14").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden14").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden14").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden14").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_buyercountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });
                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#ebook_sellercountry_name').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'countrysl',
                            limit: 5,
                            displayKey: function (counlist) {
                                return counlist.countrycode + " - " + counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden15").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden15").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden15").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden15").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#ebook_sellercountry_name').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });


                var ports = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    local: counlist
                });
                ports.initialize();
                $('#declarantCountry').typeahead(
                        {
                            hint: true,
                            highlight: true,
                            minLength: 2
                        },
                        {
                            name: 'countrysl',
                            limit: 5,
                            displayKey: function (counlist) {
                                return  counlist.countryname;
                            },
                            source: ports.ttAdapter()
                        }).on('typeahead:render', function (obj, datum) {
                    $("#typeheadhidden18").val("");
                }).on('typeahead:selected', function (obj, datum) {
                    $("#typeheadhidden18").val(datum.countryname);
                }).on('typeahead:autocomplete', function (obj, datum) {
                    $("#typeheadhidden18").val(datum.countryname);
                }).on("typeahead:change", function (obj, datum) {
                    var hiddenValue = $("#typeheadhidden18").val();
                    if (hiddenValue == null || hiddenValue == "") {
                        $('#declarantCountry').typeahead('val', "");
                        $(obj.currentTarget).val("");
                    }
                });

                if (headerConfigScript == 'YES') {
                    $('#pla_free_txt,#pol_free_txt,#pod_free_txt,#pld_free_txt').attr("readonly", false);
                }
                if ($('#consigneeordercheck').val() == 'Y') {
                    $('#headerconsignee').removeClass("required");
                    $('#ebook_consigneeaddress,#ebook_consigneecity,#ebook_consigneecountry_name,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#ebook_consigneestate').attr("readonly", true);
                    $('#ebook_consigneecountry_name').css("background-color", "#eee");
                    $('#consigneeSelect').attr("disabled", true);
                    $('#consordercheck').val('Y');
                    $('#ebook_consigneename').focus();
                    $('#consigneenewcustimg').attr("disabled", true);
                    document.getElementById('consigneeorder').checked = true;
                    $('#consigneesearch').attr("disabled", true);
                    $('#notifyconsig,#notify2consig,#notify3consig').attr("disabled", true);
                }

                if ($('#notifyCheck').val() == 'Y') {
                    //document.getElementById('notifyconsig').checked = true;
                    $('#notifyconsig').attr("checked", true).trigger("onclick");
                    $('#ebook_notifyname,#ebook_notifyaddress,#ebook_notifycity,#ebook_notifycountry_name,#ebook_notifyphone,#ebook_notifyfax,#ebook_notifyemail,#ebook_notifystate').attr("readonly", true);
                    $('#notifySelect').attr("disabled", true);
                    $('#ebook_notifycountry_name').css("background-color", "#eee");
                    $('#ebook_consigneename,#ebook_consigneeaddress,#ebook_consigneecity,#ebook_consigneecountry_name,#ebook_consigneephone,#ebook_consigneefax,#ebook_consigneeemail,#ebook_consigneestate').attr("readonly", true);
                    $('#consigneeSelect').attr("disabled", true);
                    $('#ebook_consigneecountry_name').css("background-color", "#eee");
                    $('#notifyCheck').val("Y");
                    $('#consigneesearch').attr("disabled", true);
                    $('#notifysearch').attr("disabled", true);
                    $('#consigneeorder').attr("disabled", true);
                    $('#' + custtype + 'newcustimg').attr("disabled", true);
                }
                setCustHeaderInfo('shipper');
                setCustHeaderInfo('consignee');
                setCustHeaderInfo('notify');
                enableMultiDeclarant('N');
                // end
    <%if (is_SI_restriction) {%>
                $(".SI_Process_Restriction").prop('disabled', true);
    <%}%>
    <%if ("Y".equalsIgnoreCase(GENS)) {%>
                if ($('#forwarderSelect').val() == '' && $('#ebook_forwardername').val() && $('#ebook_forwardereori').val()) {
                    console.log('inside');
                    $('.ebook_forwarderdiv').show();
                }
                if ($('#notify2Select').val() == '' && $('#ebook_notify2eori').val() && $('#ebook_notify2name').val()) {
                    $('.ebook_notify2div').show();
                }
                if ($('#notify3Select').val() == '' && $('#ebook_notify3name').val() && $('#ebook_notify3eori').val()) {
                    $('.ebook_notify3div').show();
                }

    <%}%>
    <%if ("Y".equalsIgnoreCase(Enable_T2LFiling)) {%>
                var pol = $('#OriginPort').val();
                var pod = $('#DeliveryPort').val();
                setT2LFiling(pol, pod);
    <%}%>
    <%if (showNvocc) {%>
                setNvoccCheckbox();
    <%} else {%>
                $('#ebook_nvocc').val('');
    <%}%>
       
    <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
        setENSFiling();
             setENSType();
             disableCustAddButton();
    <%}%>
            },
            onStepChanging: function (event, currentIndex, newIndex)
            {
                $("#stepWizardNewIndex").val(newIndex);
                form.validate().settings.ignore = ":disabled,:hidden,[readonly=readonly],#blType_UAE,#mpciId_UAE";
                if (currentIndex > newIndex)
                {
                    return true;
                }
                if (currentIndex == 0 && newIndex > 0) {
                    if (trim($('#ebook_shippernameheader').val()) == '' && (trim($('#ebook_shippername').val()) == '')) {
                        alert('<bean:message key="ecom.msg.entshipperdet" bundle="<%=clientcode%>"/>');
                        return false;
                    }
                    if (trim($('#ebook_consigneenameheader').val()) == '' && (trim($('#ebook_consigneename').val()) == '')) {
                        alert('<bean:message key="ecom.msg.entconsdet" bundle="<%=clientcode%>"/>');
                        return false;
                    }
                }
                if (currentIndex == 0 && newIndex > 0)
                {

                    $('#collapseNotify2').slideDown();
                    $('#collapseNotify2').css("visibility", "inherit");
                    $('#collapseNotify3').slideDown();
                    $('#collapseNotify3').css("visibility", "inherit");
                    $('#collapseForwarder').slideDown();
                    $('#collapseForwarder').css("visibility", "inherit");
                    $('#collapseWarehouse').slideDown();
                    $('#collapseWarehouse').css("visibility", "inherit");
                    $('#collapseNotify2,#collapseNotify3,#collapseForwarder,#collapseWarehouse').parent().find(".fa-plus-square-o").removeClass("fa-plus-square-o").addClass("fa-minus-square-o");
                    if (form.valid()) {
                        $('#collapseNotify2').slideUp();
                        $('#collapseNotify3').slideUp();
                        $('#collapseNotify2,#collapseNotify3#collapseForwarder,#collapseWarehouse').parent().find(".fa-minus-square-o").removeClass("fa-minus-square-o").addClass("fa-plus-square-o");
                        //$("#collapseShipper").toggle('slow');
                    }
                }
          if(newIndex == 1) {
           if(checkForDuplicateNames()){
             return false; 
            }
           } 
          if(newIndex == 2) {
            setSellerNameSelectOption();
           } 
    <%if ("Y".equalsIgnoreCase(SHOW_CHARGES_IN_SIBL)) {%>
                if (newIndex > 3) {
                    var chargecheck = false;
                    $('#datatablebychargetbl').find('.prepaidcollect').each(function () {
                        if ($(this).find('select').val() == '') {
                            chargecheck = true;
                        }
                    });
                    $('#datatablebychargetbl').find('.custtypeid').each(function () {
                        if ($(this).find('select').val() == '') {
                            chargecheck = true;
                        }
                    });
                    if (chargecheck) {
                        alert("Frieght/Payor is missing,kindly select the values.");
                        return false;
                    }
                }
                var previewindx = 5;
    <%if ("Y".equalsIgnoreCase(enableENSTab)) {%>
                if (newIndex > 4) {
                    if ($("#ensFillingType").val() == 'Single Filing' && $("#blTypeENS").val() != 'Straight BL') {
                        alert("Single Filing is applicable only for Straight BL.");
                        return false;
                    }
                }
                previewindx = 6;
    <%}%>
                if (newIndex == previewindx) {//newIndex == 5
                    convertChargedetailstoJson();
                    setSIPreviewDetails();
    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                    $('.app-content .wizard>.actions >ul > li:last-child').addClass('disabled');
    <%}%>
                }
                
    <%} else if ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(insuranceaccess)) {%>
                /*if (newIndex == 3) {
                 setInsuranceDetails();
                 }*/
                var isinsavble = true;
                $("#cargo_details").find('.si_cargo_pkg_des').each(function () {
                    if ('Y' == $(this).find('.inavailable').val()) {
                        isinsavble = false;
                    }
                });
                console.log('isinsavble=> ' + isinsavble);
                if (newIndex == 4 && isinsavble && $("#insurance_not_need").prop('checked') == false) {
                    alert("Do you want to continue without adding insurance,\nPlease check the 'I do not need insurance protection for my cargo'");
                    return false;
                }

    <%if ("Y".equalsIgnoreCase(enableCargoProtect)) {%>
                var enableMandatoryInx = 5;
                var validateMandatoryInx = 6;
    <%if ("Y".equalsIgnoreCase(Display_Charge_Tab)) {%>
                enableMandatoryInx = 6;
                validateMandatoryInx = 7;
    <%}%>
                if (newIndex == enableMandatoryInx) {//5
                    // enable_MandatoryfileforSeawaybl();
                }
                if (newIndex == validateMandatoryInx) {//6
//                    var rs11 = validateFileValidationSeawayBL();
//                    if (rs11 == false) {
//                        return false;
//                    }
                    setSIPreviewDetails();
    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                    $('.app-content .wizard>.actions >ul > li:last-child').addClass('disabled');
    <%}%>
                }
                 
    <%} else {%>
                var enableMandatoryInx = 4;
                var validateMandatoryInx = 5;
               
    <%if ("Y".equalsIgnoreCase(Display_Charge_Tab)) {%>
                enableMandatoryInx = 5;
                validateMandatoryInx = 6;
               
    <%}%>
                if (newIndex == enableMandatoryInx) {//4
                    // enable_MandatoryfileforSeawaybl();
                }
                if (newIndex == validateMandatoryInx) {//5
//                    var rs11 = validateFileValidationSeawayBL();
//                    if (rs11 == false) {
//                        return false;
//                    }
                    convertChargedetailstoJson();
                    setSIPreviewDetails();
    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                    $('.app-content .wizard>.actions >ul > li:last-child').addClass('disabled');
    <%}%>
                }
                
    <%}%>
    <%} else if ("Y".equalsIgnoreCase(enableCargoProtect)) {%>
                var enableMandatoryInx = 4;
                var validateMandatoryInx = 5;
             
    <%if ("Y".equalsIgnoreCase(Display_Charge_Tab)) {%>
                enableMandatoryInx = 5;
                validateMandatoryInx = 6;
              
    <%}%>
                if (newIndex == enableMandatoryInx) {//4
                    // enable_MandatoryfileforSeawaybl();
                }
                if (newIndex == validateMandatoryInx) {//5
//                    var rs11 = validateFileValidationSeawayBL();
//                    if (rs11 == false) {
//                        return false;
//                    }
                    setSIPreviewDetails();
    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                    $('.app-content .wizard>.actions >ul > li:last-child').addClass('disabled');
    <%}%>
                }
                

    <%} else { %>
                var enableMandatoryInx = 3;
                var validateMandatoryInx = 4;
               
    <%if ("Y".equalsIgnoreCase(Display_Charge_Tab)) {%>
                enableMandatoryInx = 4;
                validateMandatoryInx = 5;
               
    <%}%>
                if (newIndex == enableMandatoryInx) {//3
                    // enable_MandatoryfileforSeawaybl();
                }
                if (newIndex == validateMandatoryInx) {//4
//                    var rs11 = validateFileValidationSeawayBL();
//                    if (rs11 == false) {
//                        return false;
//                    }
                    setSIPreviewDetails();
    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                    $('.app-content .wizard>.actions >ul > li:last-child').addClass('disabled');
    <%}%>
                }
                 
    <%}%>
                if (currentIndex < newIndex)
                {
                    form.find(".body:eq(" + newIndex + ") label.error").remove();
                    form.find(".body:eq(" + newIndex + ") .error").removeClass("error");
                }
                $('.checkcontclass:not(.skip-container-validation)').each(function () {
                    $(this).rules('add', {
                        checkcontclass: true,
                        messages: {
                            checkcontclass: "<bean:message key="ecom.msg.invalidcontnum" bundle="<%=clientcode%>"/>"
                        }
                    });
                });
                
                if (currentIndex == 2 && newIndex > 2) {
    <%if (is_SI_restriction) {%>
                    var mandmissing = true;
                    $("#cargo_details").find('.SI_Process_Restriction').each(function () {
                        if ($(this).is('.required')) {
                            if (($(this).val() == '' || $(this).val().length == 0) && $(this).attr('id') !== undefined && (typeof $(this).attr('id') !== "undefined")) {
                                mandmissing = false;
                            }
                        }
                    });
                    if (!mandmissing) {
                        alert("Please click on 'Request Booking update' to notify agency for required information.");
                        return mandmissing;
                    }
    <%}%>
                    var contnovalidate = true;
                    var sicontnostrlis = "";
                    var sicontstarcut = 0;
                    $("#cargo_details").find('.validatebkgcontno').each(function () {
                        if (sicontstarcut != 0) {
                            sicontnostrlis = sicontnostrlis + ",";
                        }
                        sicontnostrlis = sicontnostrlis + $(this).val();
                        sicontstarcut = sicontstarcut + 1;
                    });
                    console.log("sicontnostrlis===> " + sicontnostrlis);
                    var contstrlis1;
                    var resultdata = checkSIContainerNoValidation(sicontnostrlis);
                    if (resultdata != '' && resultdata.length > 0) {
                        var contarr = resultdata.split('~~~')
                                ;
                        if (contarr[0] == 'false') {
                            contstrlis1 = "The below container already used in other booking " + contarr[1];
                            contnovalidate = false;
                        } else if (contarr[0] == 'true') {
                            contnovalidate = true;
                        } else if (contarr[0] == 'error') {
                            contstrlis1 = "Error occured,Try again later.";
                            contnovalidate = false;
                        }
                    }

                    if (!contnovalidate) {
                        alert(contstrlis1);
                        return contnovalidate;
                    }


                    var checkemptycmdty = true;
                    $('.checkcommodityvalue').each(function () {
                        console.log('name>>' + $(this).attr('name'));
                        if ($(this).val() == null || $(this).val() == '') {
                            checkemptycmdty = false;
                        }
                    });
                    if (!checkemptycmdty) {
                        alert("<bean:message key="ecom.msg.updatecomm" bundle="<%=clientcode%>"/>");
                        return checkemptycmdty;
                    }
         
                }
                if (form.valid() && currentIndex == 2 && newIndex > 2) {
                    var checdupcntr = true;
                    var checdupcarrier = true;
                    var cntrarray = new Array();
                    var carrierarray = new Array();
                    var cntrarrcount = 0;
                    var carrierrarrcount = 0;
                    $('.checkcntrdupclass:not(.skip-container-validation)').each(function () {
                        if ($(this).hasClass("required")) {
                            cntrarray[cntrarrcount] = $(this).val();
                            cntrarrcount++;
                        }

                    });
                    $('.checkcarrierseal').each(function () {
                        if ($(this).hasClass("required")) {
                            carrierarray[carrierrarrcount] = $(this).val();
                            carrierrarrcount++;
                        }
                    });
                    for (var ii = 0; ii < cntrarray.length; ii++) {
                        for (var jj = ii + 1; jj < cntrarray.length; jj++) {
                            if (cntrarray[ii].length > 0 && cntrarray[jj].length > 0 && (cntrarray[ii] == cntrarray[jj])) {
                                checdupcntr = false;
                            }
                        }
                    }
                    if (!checdupcntr) {
                        alert("<bean:message key="ecom.msg.samecontnotallowed" bundle="<%=clientcode%>"/>");
                        return checdupcntr;
                    }
                    for (var ii = 0; ii < carrierarray.length; ii++) {
                        for (var jj = ii + 1; jj < carrierarray.length; jj++) {
                            if (carrierarray[ii].length > 0 && carrierarray[jj].length > 0 && (carrierarray[ii] == carrierarray[jj])) {
                                checdupcarrier = false;
                            }
                        }
                    }
                    if (!checdupcarrier) {
                        alert("<bean:message key="ecom.msg.samecarriersealnotallowed" bundle="<%=clientcode%>"/>");
                        return checdupcarrier;
                    }
                    //check wt unit difference and grosswt exceed validation
                    var checkdiffunittype = true;
                    var checkgrosswtexceed = true;
                    var wtcontid;
                    var wttarewtid, wtunitvalue, prevunit, wttarewtval, strwtval;
                    var wtgrosswtval = 0;
                    var wtdiff = 1, wtcount = 0, wtval = 0;
                    var errorcntrid, errorgrosscontainerid, errorunitvalue;
                    /* $('.docgrosswtcheck').each(function () {
                     wtcontid = $(this).val();
                     wtgrosswtval = 0;
                     wtcontid = wtcontid.replace("sicontainerid", "");
                     wttarewtid = "#tareweightsicontainerid" + wtcontid;
                     wttarewtval = $(wttarewtid).val();
                     wtgrosswtval = parseFloat(wtgrosswtval) + parseFloat(wttarewtval);
                     wtcount = 0;
                     wtdiff = 1;
                     $('.weigthclass' + wtcontid).each(function () {
                     if (wtcount == 0) {
                     wtcount++;
                     wtunitvalue = $('#unitsicontainerid' + wtcontid + wtdiff).val();
                     prevunit = wtunitvalue;
                     } else {
                     wtunitvalue = $('#unitsicontainerid' + wtcontid + wtdiff).val();
                     if (checkdiffunittype && prevunit != wtunitvalue) {
                     errorcntrid = $('#containernosicontainerid' + wtcontid).val();
                     checkdiffunittype = false;
                     }
                     
                     prevunit = wtunitvalue
                     }
                     if (checkdiffunittype && checkgrosswtexceed) {
                     strwtval = $('#weightsicontainerid' + wtcontid + wtdiff).val();
                     wtval = parseFloat(strwtval);
                     wtgrosswtval = parseFloat(wtgrosswtval) + parseFloat(wtval);
                     if (wtunitvalue == 'T') {
                     if (parseFloat(wtgrosswtval) > 50) {
                     errorunitvalue = wtunitvalue;
                     errorgrosscontainerid = $('#containernosicontainerid' + wtcontid).val();
                     checkgrosswtexceed = false;
                     }
                     } else if (wtunitvalue == 'P') {
                     if (parseFloat(wtgrosswtval) > 100000) {
                     errorunitvalue = wtunitvalue;
                     errorgrosscontainerid = $('#containernosicontainerid' + wtcontid).val();
                     checkgrosswtexceed = false;
                     }
                     } else {
                     if (parseFloat(wtgrosswtval) > 50000) {
                     errorunitvalue = wtunitvalue;
                     errorgrosscontainerid = $('#containernosicontainerid' + wtcontid).val();
                     checkgrosswtexceed = false;
                     }
                     }
                     }
                     wtdiff++;
                     });
                     });
                     if (!checkdiffunittype) {
                     alert("<bean:message key="ecom.msg.cargoweighttype" bundle="<%=clientcode%>"/>.<bean:message key="Tracking.containernum" bundle="<%=clientcode%>"/> : " + errorcntrid);
                     return checkdiffunittype;
                     }
                     if (checkdiffunittype && !checkgrosswtexceed) {
                     if (errorunitvalue == 'T') {
                     alert("<bean:message key="ecom.msg.grossweightton" bundle="<%=clientcode%>"/>. <bean:message key="Tracking.containernum" bundle="<%=clientcode%>"/> : " + errorgrosscontainerid);
                     } else if (errorunitvalue == 'P') {
                     alert("<bean:message key="si.cargo.cargowtpounds" bundle="<%=clientcode%>"/>. <bean:message key="Tracking.containernum" bundle="<%=clientcode%>"/>  : " + errorgrosscontainerid);
                     } else {
                     alert("<bean:message key="ecom.msg.grossweightkilos" bundle="<%=clientcode%>"/>.<bean:message key="Tracking.containernum" bundle="<%=clientcode%>"/> : " + errorgrosscontainerid);
                     }
                     return checkgrosswtexceed;
                     }*/


                }
                console.log('newwwwwwwwwwwww indexxxxxxxxxxxx -> ' + newIndex);
                if (newIndex >= 3) {
                    $('#ebook_buyerfax, #ebook_buyerphone, #ebook_sellerphone, #ebook_sellerfax').each(function () {
                        $(this).val($(this).val().replaceAll("[^0-9+\\-]", "").replaceAll("(?<!^)\\+", "").replaceAll("\\+-", "+").replaceAll("--+", "-").replaceAll("^-+", "").replaceAll("-+$", ""));
                    });
                  $('#ebook_buyerphone, #ebook_sellerphone').each(function () {
                  if ($(this).val().length <= 3) {
                       $(this).val('');
                    }
                  });
                    form.validate().settings.ignore = ":disabled,[readonly=readonly],#blType_UAE,#mpciId_UAE";
                    var ret = form.valid();
                    var tareMissingCont = '';
                    console.log('ret_____________> ' + ret);
                    if (!ret) {
                        console.log('error id== > ' + $('#cargo_details').find('label.error:visible').attr('id'));
                        $('.container-body-inner').show();
                        var enableid = $('#cargo_details').find('label.error:visible:first').parents('.container-body-inner').attr('id');
                        console.log("enableid===> " + enableid);
                        enableContainer(enableid);
                        $(".si_cmdty_accord").each(function () {
                            if (!$(this).hasClass('show')) {
                                $(this).addClass('show');
                            }
                        });
                        /* $('#cargo_details').find('label.error').each(function () {
                         console.log('TEST_____________> ');
                         console.log('==> '+$(this).css('display'));
                         if (!$(this).css('display') == 'none') {
                         var enableid = $(this).parents('.container-body-inner').attr('id');
                         console.log("enableid===> " + enableid);
                         enableContainer(enableid);
                         return false;
                         }
                         });*/
                    }
                    if (newIndex == 3 && ret) {
                        $('#cargo_details').find('.container-body-inner').each(function () {
                            var contNo = $(this).find('.checkcontclass').val();
                            var tareWeight = $(this).find('.tareweightinputclass').val();
                            console.log('tareweight...............' + tareWeight);
                            $(this).find('.si_cargo_pkg_des').each(function () {
                                var currqtyid = $(this).find('.cp_noofpackage').attr('id');
                                console.log('currqtyid' + currqtyid)
                                var siId = currqtyid.replaceAll("qty", "");
                                var packageCode = $('#package' + siId).val();
                                console.log('Package' + packageCode);
                                if (!packageCode.includes('~~')) {
                                    alert('Package Type is Invalid for this Container ' + contNo);
                                    ret = false;
                                }
                            }
                            )
                            console.log('length of tare weight : ' + tareWeight.length);
                            if (tareWeight == null || tareWeight == "undefined" || tareWeight.length == 0)
                            {
                                tareMissingCont += contNo + ',';
                            }
                            console.log('tareMissingCont........>' + tareMissingCont + 'Container Name :' + tareMissingCont);
                        }
                        )
                        console.log('Packagecode.....return....>' + ret);
                    }
                    if (tareMissingCont.length > 0 && ret) {
                        alert('TareWeight is missing for this container ( ' + tareMissingCont.slice(0, -1) + ' )');
                        ret = false;
                    }
    <%if ("Y".equalsIgnoreCase(duplicateComdyVal)) {%>
                    if (ret) {
                        ret = duplicateComdtyVal();
                        console.log('duplicatecomdty.....return....>' + ret);
                    }

    <%}%>
    <%if ("Y".equalsIgnoreCase(Enable_OOGRequired_Validation)) {%>
                    if (newIndex == 3 && ret) {
                        const oogMismatch = oogDimensionCompare();
                        ret = oogMismatch;
                        console.log('oogDimensionCompare.....return....>' + oogMismatch);
                    }


    <%}%>
   <%if("Y".equalsIgnoreCase(Enable_ReleaseType)){%>
               if(ret){ ret = validateLOIMsg(); }
               console.log('Enable_ReleaseType==>'+ret);
                  <%}%>
                    return ret;
                }

                form.validate().settings.ignore = ":disabled,:hidden,[readonly=readonly],#blType_UAE,#mpciId_UAE";
                return form.valid();
            },
            onStepChanged: function (event, currentIndex, priorIndex) {
                if (currentIndex == 2) {
                    loadAccordionHeading();
                }
    <% if ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(insuranceaccess)) {%>
                if (currentIndex == 3) {
                    setInsuranceDetails();
                    loadQuoteDetails();
                }
    <%if ("Y".equalsIgnoreCase(enableCargoProtect)) {%>
                if (currentIndex == 4) {
                    loadCargoProtect('edit');
                }
    <%}
    } else if ("Y".equalsIgnoreCase(enableCargoProtect)) {%>
                if (currentIndex == 3) {
                    loadCargoProtect('edit');
                }
    <%}%>
    <%if (user != null && user.equalsIgnoreCase("Y")) {%>
                $('.app-content .wizard>.actions >ul > li:last-child').addClass('disabled');
    <%}%>
   
            },
            onFinishing: function (event, currentIndex)
            {
                form.validate().settings.ignore = ":disabled,[readonly=readonly]";
                var checkAES = true;
                var checkBLTypeUAE = true;
                 validationENSDocType('Buyer');
                 validationENSDocType('Seller');
                if ($("#siAESnumber").length > 0 && $('input[name=AESDisclaimer]').length > 0 && $("#siAESnumber").val() == '' && (!$('input[name=AESDisclaimer]').is(':checked')) && $("#siAESnumber").val().length == 0 && $("#siAESnumber").is(":visible")) {
                    form.valid();
                    checkAES = false;
                }
                
                if ($("#blType_UAE").length > 0 && $("#blType_UAE").is(":visible") && $("#blType_UAE").val() == "") {
                    form.valid();
                    checkBLTypeUAE = false;
                }
                if ($("#mpciId_UAE").length > 0 && $("#mpciId_UAE").hasClass("required") && $("#mpciId_UAE").is(":visible") && $("#mpciId_UAE").val() == "") {
                    form.valid();
                    checkBLTypeUAE = false;
                }
             
                if (form.valid() == false && checkAES && checkBLTypeUAE) {
                    alert("<bean:message key="ecom.msg.fieldmissing" bundle="<%=clientcode%>"/>");
                }
                if ($('#cargoprotectid').length > 0 && $('#temp_cargoprotectid').val().length > 0) {
                    $('#cargoprotectid').val($('#temp_cargoprotectid').val());
                }
                checkafricaReqsMandatory('<%=dischargeportcountry%>');
                return form.valid();
            },
            onFinished: function (event, currentIndex)
            {
                $('.selectcls').attr("disabled", false);
                $('.socslt').attr("disabled", false);
                $(".SI_Process_Restriction").prop('disabled', false);
                $(".SI_Process_Restriction_Cust").prop('disabled', false);
                $(".si_reefer_option").prop('disabled', false);
                var checdupcntr = true;
                var cntrarray = new Array();
                var cntrarrcount = 0;
                $('.checkcntrdupclass:not(.skip-container-validation)').each(function () {
                    if ($(this).hasClass("required")) {
                        cntrarray[cntrarrcount] = $(this).val();
                        cntrarrcount++;
                    }

                });
                for (var ii = 0; ii < cntrarray.length; ii++) {
                    for (var jj = ii + 1; jj < cntrarray.length; jj++) {
                        if (cntrarray[ii].length > 0 && cntrarray[jj].length > 0 && (cntrarray[ii] == cntrarray[jj])) {
                            checdupcntr = false;
                        }
                    }
                }
                if (checdupcntr) {
    <% if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                    showtermsconditions();
    <%} else {%>
                    if (checkMailConfig == 'Y') {
                        $('#ccalertmoal').modal('show');
                    } else {
                        submitwithoutcc();
                    }
    <%}%>
                } else {
                    $('.selectcls').attr("disabled", true);
                    $('.socslt').attr("disabled", true);
                    alert("<bean:message key="ecom.msg.samecontnotallowed" bundle="<%=clientcode%>"/>");
                    return checdupcntr;
                }
            }
        });
        $(".steps li").addClass("done");
        $(".steps li.first").removeClass("done");
        $(".steps li a span.number").addClass("glyphicon glyphicon-ok").text("");
    });
    function submitwithcc() {
        $('#ccalertmoal').modal('hide');
        $('#ccmailidmodal').val("");
        if ($('#ccMailId').val() != null && $('#ccMailId').val() != '') {
            $('#ccmailidmodal').val($('#ccMailId').val());
        } else if (ccMailId != '' && ccMailId != 'NO DATA') {
            $('#ccmailidmodal').val(ccMailId);
        }
        $('#blccmailmodal').modal('show');
    }
    function submitwithoutcc() {
        $('#ccMailId').val("");
        setSaveType('S');
        //callLoading();
        document.getElementById('SIEditForm').action = "SubmitBL.do";
        document.getElementById('SIEditForm').submit();
    }
    function submitBL() {
        var checksubmit = true;
        if ($('#ccmailidmodal').val() != '') {
            if (!validateDupMailId(trim($('#ccmailidmodal').val()))) {
                if (!validEmailCheckToCommon(trim($('#ccmailidmodal').val()))) {
                    alert("<bean:message key="ecom.msg.invalidmailid" bundle="<%=clientcode%>"/>");
                    checksubmit = false;
                }
            } else {
                alert("<bean:message key="ecom.msg.repeatmailid" bundle="<%=clientcode%>"/>.");
                checksubmit = false;
            }
        } else {
            alert('<bean:message key="ecom.msg.ccmailid" bundle="<%=clientcode%>"/>');
            checksubmit = false;
        }
        //alert(cclist);
        if (checksubmit) {
            splitCCMailId($('#ccmailidmodal').val());
            //$('#ccMailId').val($('#ccmailidmodal').val());
            setSaveType('S');
            //callLoading();
            document.getElementById('SIEditForm').action = "SubmitBL.do";
            document.getElementById('SIEditForm').submit();
        }
    }
    function addRow_New(sitable, contnum, obj, checkboxid, incclass, cotainercount, type, hscodeconfig, oogid) {
        //					console.log(">>>obj>>"+obj);
        var firstComdtyId = 'commodity' + contnum + "1";
        var firstComdtyVal = $('#' + firstComdtyId).val();
        if (firstComdtyVal != null && firstComdtyVal != '') {
            $('.selectcls').attr("disabled", false);
            $('.socslt').attr("disabled", false);
            var incarray = new Array();
            var incrementcheck = 0;
            //	console.log(">>incclass>>"+incclass);
            $('.' + incclass).each(function () {
                var incarr = $(this).attr('id').replace(incclass, "");
                incarray[incrementcheck] = incarr;
                incrementcheck++;
            });
            //	console.log("incarray>>"+incarray);
            var checkBoxTrue = "false";
            var largest = Math.max.apply(0, incarray);
            if (document.getElementById(checkboxid).checked) {
                checkBoxTrue = "true";
            }
            var nextRow;
            var rowcol = obj;
            rowcol = rowcol + "";
            //
            if (rowcol.indexOf('javasc') != -1) {
                nextRow = obj.parentNode.parentNode.parentNode.parentNode.rowIndex;
            }
            if (type == 'excel') {
                nextRow = obj;
            }
            var table = document.getElementById(sitable);
            //				console.log("nextRow>>"+nextRow);
            //var row = table.insertRow(nextRow + 1);
            var scpval = "";
            scpval = parseInt(largest) + 1;
            //	console.log("contnum>>>"+contnum);
            //	console.log("scpval>>>"+scpval);

            var cmdId = 'commodity' + contnum + scpval;
            var hiddencomid = 'hiddencomid' + contnum + scpval;
            var pkgId = "package" + contnum + scpval;
            var hiddenpackid = "hiddenpackid" + contnum + scpval;
            var hscodeid = "hscode" + contnum + scpval;
            var hiddenhsid = "hiddenhsid" + contnum + scpval;
            var unitvalue = '';
            var uniclassid = contnum.replace('sicontainerid', '');
            var eqpstatusval = document.getElementById(contnum + "#eqpstatus#").value;
            var oogvalue = (($('#' + oogid).prop('checked') == true) ? "Y" : "N");
            var addCargoURL = "SICargoDetails.jsp";
    <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
            addCargoURL = "SICargoDetailsNew.jsp";
    <%}%>
            $.ajax({
                type: "POST",
                cache: false,
                async: false,
                url: addCargoURL + "?id=" + scpval + "&containerNo=" + contnum + "&isRequired=" + checkBoxTrue + "&containercountid=" + cotainercount + "&incclass=" + incclass + "&checkboxid=" + checkboxid + "&hscodemandary=" + hscodeconfig + "&appversion=" + '<%=siAppVersion%>' + '&eqpstatusval=' + eqpstatusval + '&oogvalue=' + oogvalue,
                data: $(this).serializeArray(),
                success: function (data) {
                    //row.innerHTML = data;
                    $(obj).parents(".container-body-inner").find(".si_cmdty_accord").removeClass('show');
                    //$(data).insertAfter($(obj).parents('.si_cargo_pkg_des'));
                    $(data).insertAfter($(obj).parents('.container-body-inner').find('.si_cargo_pkg_des').last());
                    var firstcomdtycode;
                    if (firstComdtyVal != '') {
                        firstcomdtycode = firstComdtyVal.split("~~");
                        if (firstcomdtycode.length > 1) {
                            firstcomdtycode = trim(firstcomdtycode[1]);
                        }
                    }
                    $('#' + cmdId).val(firstComdtyVal);
                    commodityload(cmdId, hiddencomid, hscodeid, hiddenhsid, firstcomdtycode);
                    packageload(pkgId, hiddenpackid);
                    $('.' + 'unitclass' + uniclassid).each(function () {
                        console.log('entered in unit class');
                        unitvalue = $(this).val();
                        $('#unit' + contnum + scpval).val(unitvalue);
                        $('#newcmdty' + contnum + scpval).val(firstComdtyVal);
                    });
    <%if (is_SI_restriction) {%>
                    $(".SI_Process_Restriction").prop('disabled', true);
    <%}%>
                    loadtypeaheadtooltipmsg();
                    disableCustAddButton();
                    setSellerNameSelectOption();
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
            //commodityload(cmdId, hiddencomid,hscodeid,hiddenhsid);
            //packageload(pkgId, hiddenpackid);
            $('.selectcls').attr("disabled", true);
            $('.socslt').attr("disabled", true);
            $("#SIEditForm").validate();
        } else {
            alert("<bean:message key="ecom.msg.updatecomm" bundle="<%=clientcode%>"/>");
        }
    }
    function deleteRow_New(tableID, obj) {
        $(obj).parents('.si_cargo_pkg_des').remove();
//                                    var i = obj.parentNode.parentNode.parentNode.parentNode.rowIndex;
//                                    document.getElementById(tableID).deleteRow(i);
    }
    var isNS4 = (navigator.appName == "Netscape") ? 1 : 0;
    function keyCheckINT(e) {
        e = e || window.event;
        if (!isNS4) {
            if (!(event.keyCode > 47 && event.keyCode < 58)) {
                if (e.which != 0) {
                    if (e.which != 8) {
                        event.returnValue = false;
                    }
                }
            }
        } else {
            if ((!(e.which > 47 && e.which < 58))) {

                if (e.which != 0) {
                    if (e.which != 8) {
                        e.preventDefault();
                    }
                }
            }
        }
        return true;
    }

    function callsearchmethodsi(formName, searchType, con,custid) {
        if (con == 'shipper') {
            $('#shippersearch').html("<i class='fa fa-spinner fa-spin'></i>");
        } else if (con == 'consignee') {
            $('#consigneesearch').html("<i class='fa fa-spinner fa-spin'></i>");
        } else if (con == 'notify') {
            $('#notifysearch').html("<i class='fa fa-spinner fa-spin'></i>");
        } else if (con == 'forwarder') {
            $('#forwardersearch').html("<i class='fa fa-spinner fa-spin'></i>");
        } else if (con == 'warehouse') {
            $('#warehousesearch').html("<i class='fa fa-spinner fa-spin'></i>");
        } else if (con == 'buyer') {
            $('#buyersearch').html("<i class='fa fa-spinner fa-spin'></i>");
        } else if (con == 'seller') {
            $('#sellersearch').html("<i class='fa fa-spinner fa-spin'></i>");
        }else if (con == 'dyBuyer') {
        $('#'+con+'custsearch'+custid).html("<i class='fa fa-spinner fa-spin'></i>");
        }else if (con == 'dySeller') {
        $('#'+con+'custsearch'+custid).html("<i class='fa fa-spinner fa-spin'></i>");
        }
        var typeVal = "";
        var params = "";
        var remainingParameter = "";
         var dycustis = (custid && custid.length) ? custid : '';
        typeVal = searchType;
        remainingParameter = "&fieldName=" + con;
        params = "type=" + typeVal +"&FieldID=" + dycustis+ remainingParameter;
        var URLStr = "DynamicColumn.do?" + params;
        $.ajax({
            type: "POST",
            cache: false,
            url: URLStr,
            data: $(this).serializeArray(),
            success: function (data) {
                $("#myModalSearch .modal-body").html(data);
                $("#myModalSearch").modal("show");
                $("#myModalSearch .modal-backdrop").css({"position": "inherit"});
                $('#shippersearch').html("<i class='fa fa-search'></i>");
                $('#consigneesearch').html("<i class='fa fa-search'></i>");
                $('#notifysearch').html("<i class='fa fa-search'></i>");
                $('#forwardersearch').html("<i class='fa fa-search'></i>");
                $('#warehousesearch').html("<i class='fa fa-search'></i>");
                $('#buyersearch').html("<i class='fa fa-search'></i>");
                $('#sellersearch').html("<i class='fa fa-search'></i>");
                $('#'+con+'custsearch'+custid).html("<i class='fa fa-search'></i>");
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }



    function setnewvalues(target, source) {
        document.getElementById(target).value = document.getElementById(source).value;
    }
    function setnewvaluesComdty(target, source) {
        var val = document.getElementById(source).value;
        val.replace("\"", "&quot;");
        console.log('comdy>>' + val);
        document.getElementById(target).value = val;
    }
    $(document).ready(function () {
        $("#collapseAgreementParty,#collapseConsignee,#collapseNotify,#collapseForwarder,#collapseWarehouse").on('shown.bs.collapse', function () {
            $(this).parent().find(".fa-plus-square-o").removeClass("fa-plus-square-o").addClass("fa-minus-square-o");
        }).on('hidden.bs.collapse', function () {
            $(this).parent().find(".fa-minus-square-o").removeClass("fa-minus-square-o").addClass("fa-plus-square-o");
        });
    });
    var nAgt = navigator.userAgent;
    function checkvalue(elementRef, value1, fieldval) {
        document.getElementById(fieldval).value = value1.replace(/[^0-9.0-9]/g, '');
        value1 = document.getElementById(fieldval).value;
        var keyCodeEntered = (elementRef.which) ? elementRef.which : elementRef.keyCode;
        if (keyCodeEntered == 8 || keyCodeEntered == 37 || keyCodeEntered == 39 || keyCodeEntered == 46) {
            return true;
        }
        var val = value1;
        var afterdot;
        var beforedot;
        var bdot;
        if (val.indexOf(".") != -1)
        {
            afterdot = value1.split(".").pop();
            beforedot = isNaN(parseInt(value1, 10)) ? '0' : parseInt(value1, 10); //parseInt(value1, 10);
            bdot = beforedot.toString();
            if (afterdot.length > 2)
            {
                var aftercon = afterdot.slice(0, 3);
                var conval = bdot + "." + aftercon;
                document.getElementById(fieldval).value = conval;
                return false;
            }
        } else
        {
            if (val.length > 9 && keyCodeEntered != 190)
            {
                var newStr = val.slice(0, 8);
                document.getElementById(fieldval).value = newStr;
                return false;
            }
        }

    }

    function ValDigit(elementRef, val) {
        var keyCodeEntered = (elementRef.which) ? elementRef.which : elementRef.keyCode;
        if ((verOffset = nAgt.indexOf("Chrome")) != -1 || (verOffset = nAgt.indexOf("MSIE")) != -1 || (verOffset = nAgt.indexOf("Safari")) != -1)
        {
            if (keyCodeEntered == 46)
            {
                keyCodeEntered = 0;
            }
        } else
        {
            if (keyCodeEntered == 46)
            {
                keyCodeEntered = elementRef.keyCode;
            }
        }
        if (keyCodeEntered == 8 || keyCodeEntered == 9 || keyCodeEntered == 37 || keyCodeEntered == 39 || keyCodeEntered == 46 || (keyCodeEntered >= 48) && (keyCodeEntered <= 57)) {
            return true;
        } else if (keyCodeEntered == 0) {
            if ((val.value) && (val.value.indexOf('.') >= 0))
                return false;
            else
                return true;
        }
        return false;
    }
    function callAutocompleteintialize(id) {
        var ports = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: counlist
        });
        ports.initialize();
        $(id).typeahead(
                {
                    hint: true,
                    highlight: true,
                    minLength: 2
                },
                {
                    name: 'country',
                    limit: 5,
                    displayKey: function (counlist) {
                        return counlist.countrycode + " - " + counlist.countryname;
                    },
                    source: ports.ttAdapter()
                }).on('typeahead:render', function (obj, datum) {
            $("#typeheadhidden4").val("");
        }).on('typeahead:selected', function (obj, datum) {
            $("#typeheadhidden4").val(datum.countryname);
        }).on('typeahead:autocomplete', function (obj, datum) {
            $("#typeheadhidden4").val(datum.countryname);
        }).on("typeahead:change", function (obj, datum) {
            var hiddenValue = $("#typeheadhidden4").val();
            if (hiddenValue == null || hiddenValue == "") {
                $(id).typeahead('val', "");
                $(obj.currentTarget).val("");
            }
        });
    }
    function callAutocompleteforSIsearch(sor, tar) {

        $(sor).typeahead('destroy');
        $(sor).val(tar);
        var ports = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: counlist
        });
        ports.initialize();
        $(sor).typeahead(
                {
                    hint: true,
                    highlight: true,
                    minLength: 2
                },
                {
                    name: 'country',
                    limit: 5,
                    displayKey: function (counlist) {
                        return counlist.countrycode + " - " + counlist.countryname;
                    },
                    source: ports.ttAdapter()
                }).on('typeahead:render', function (obj, datum) {
            $("#typeheadhidden4").val("");
        }).on('typeahead:selected', function (obj, datum) {
            $("#typeheadhidden4").val(datum.countryname);
        }).on('typeahead:autocomplete', function (obj, datum) {
            $("#typeheadhidden4").val(datum.countryname);
        }).on("typeahead:change", function (obj, datum) {
            var hiddenValue = $("#typeheadhidden4").val();
            if (hiddenValue == null || hiddenValue == "") {
                $(sor).typeahead('val', "");
                $(obj.currentTarget).val("");
            }
        });
    }



    function socstatus(id, soc)
    {
        var checkid = id;
        var hiddensoc = "soc" + soc;
        var containerid = soc;
        if (document.getElementById(checkid).checked == true)
        {
            $('#containerno' + containerid + '-error').html("");
            $('#containerno' + containerid).removeClass('checkcontclass');
            document.getElementById(hiddensoc).value = 'Y';
        }
        if (document.getElementById(checkid).checked == false)
        {
            document.getElementById(hiddensoc).value = 'N';
            $('#containerno' + containerid + '-error').html("");
            $('#containerno' + containerid).removeClass('checkcontclass');
            $('#containerno' + containerid).addClass('checkcontclass');
        }
    }
    function calcSITareWeight(value, tareweightid, twt, cargowt, srs) {
        var tarwtunitid = tareweightid.replace('tareweight', '');
        var tarwtunitval = '';
        tarwtunitid = "tareweighthidunit" + tarwtunitid;
        if (srs == 'S') {
            $('#' + cargowt).val("");
        }
        var checkunitdiffcalc = true;
        var unitcntrid = tareweightid.replace("tareweightsicontainerid", "");
        var unitclass = ".unitclass" + unitcntrid;
        var currentunitid = cargowt.replace("weightsicontainerid", "");
        currentunitid = "unitsicontainerid" + currentunitid;
        $(unitclass).each(function () {
            if (currentunitid != $(this).attr('id')) {
                if (value == $(this).val()) {
                    checkunitdiffcalc = false;
                }
            }
        });
        console.log('tarwtunitid>>' + tarwtunitid);
        tarwtunitval = $('#' + tarwtunitid).val();
        if (value == 'T') {
            if (tarwtunitval == 'K') {
                var tare = parseFloat($('#' + tareweightid).val() / 1000);
                $('#' + tareweightid).val(tare);
                $('#' + tarwtunitid).val("T");
                $('#' + twt).html(tare);
            }

        } else {
            if (tarwtunitval == 'T') {
                if (srs == 'S') {
                    var tarewgt = parseFloat($('#' + tareweightid).val() * 1000);
                } else {
                    var tarewgt = parseFloat($('#' + tareweightid).val());
                }
                tarewt = tarewgt.toString();
                var tarewtsplit = tarewt.split(".");
                if (tarewtsplit.length == "1") {
                    tarewgt = tarewgt + ".0";
                }
                $('#' + tareweightid).val(tarewgt);
                $('#' + twt).html(tarewgt);
                $('#' + tarwtunitid).val("K");
            }
        }
    }
    function getCustDetail(blno, blstatus, bookno)
    {
        // callLoading();
        $('#custref').val("Y");
        document.forms[0].action = "BLEditAction.do?type=edit&blno=" + blno + "&bookno=" + bookno;
        document.forms[0].submit();
    }

    function checkBLContNo(contId, socId, tarewtid, wtunitid, eqpVal) {
        var twtidno = contId.replace('containerno', '');
        var twtid = "twt" + twtidno;
        var eqptypeval = "eqptypeval" + twtidno;
        var nor = document.getElementById(twtidno + "#nor#").value;
        var eqptype = $('#' + eqptypeval).html();
        if ($('#' + contId).val() != null && $('#' + contId).val() != "") {
            $.ajax({
                type: "POST",
                cache: false,
                url: "ecomAjax?type=gettareweight&container=" + encodeURIComponent($('#' + contId).val()) + "&eqptype=" + eqptype + "&nor=" + nor + "&soc=" + $('#' + socId).val(),
                async: false,
                data: $(this).serializeArray(),
                success: function (data) {
                    var str = data;
                    if ($('#' + socId).val() == 'Y') {
                        $('#' + tarewtid).val('');
                        $('#' + twtid).html('');
                    }
                    if (data != null && data != 'null' && data.length > 0) {
                        var cargoWtunit = $('#' + wtunitid).val();
                        str = convertWeightUnit(str, 'K', cargoWtunit);
                        $('#' + wtunitid + '_previous').val(cargoWtunit);
                        $('#' + tarewtid).val(str);
                        if (str != null && str != 'null' && str != '') {
                            $('#' + twtid).html($('#' + tarewtid).val());
                            $('#' + tarewtid).focus();
                            //  checkGrossWeight(twtidno);
                        }
                    }
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        }
    }

    function setContNo(id, classname, val, qtyclass, weigthclass, volumeclass, obj) {
        $("." + classname).addClass("required");
        var eqpstatusval = document.getElementById(id + "#eqpstatus#").value;
        console.log('eqpstatusval -  - ' + eqpstatusval);
        if (obj.checked) {
            $("." + classname).addClass("required");
            $("." + qtyclass).addClass("maxone");
            if (eqpstatusval == 'M') {
                console.log('inside m');
                $("." + weigthclass).addClass("valuezero");
            } else {
                $("." + weigthclass).addClass("validatezeropoint");
            }
            $("." + volumeclass).addClass("validatepoint");
            $('#containerno' + val).removeClass('checkcontclass');
            $('#containerno' + val).addClass('checkcontclass');
            $('#containerno' + val).addClass('required');
            document.getElementById(id).value = val;
        } else {
            $("." + classname).removeClass("required");
            $("." + qtyclass).removeClass("maxone");
            if (eqpstatusval == 'M') {
                console.log('inside m');
                $("." + weigthclass).removeClass("valuezero");
            } else {
                $("." + weigthclass).removeClass("validatezeropoint");
            }
            $("." + volumeclass).removeClass("validatepoint");
            $('#containerno' + val).removeClass('checkcontclass');
            $('#containerno' + val).removeClass('required');
            document.getElementById(id).value = "";
        }
    }
    function socstatuscheck(id, conid)
    {

        var checkid = 'select' + id;
        var containerid = conid;
        if (document.getElementById(checkid).checked == true)
        {
            $('#containerno' + containerid).addClass('checkcontclass');
        } else
        {
            $('#containerno' + containerid).removeClass('checkcontclass');
        }

    }
    function checkSIContNo(contId, socId, tarewtid, wtunitid, eqpVal) {
        var twtidno = contId.replace('containerno', '');
        var twtid = "twt" + twtidno;
        var eqptypeval = "eqptypeval" + twtidno;
        console.log("weolcome::::::" + eqptypeval);
        var eqptype = $('#' + eqptypeval).html();
        console.log("yes:::::" + eqptype);
        var nor = document.getElementById(twtidno + "#nor#").value;
        if ($('#' + contId).val() != null && $('#' + contId).val() != "") {
            $.ajax({
                type: "POST",
                cache: false,
                url: "ecomAjax?type=gettareweight&container=" + encodeURIComponent($('#' + contId).val()) + "&eqptype=" + eqptype + "&nor=" + nor + "&soc=" + $('#' + socId).val(),
                async: false,
                data: $(this).serializeArray(),
                success: function (data) {
                    var str = data;
                    if ($('#' + socId).val() == 'Y') {
                        $('#' + tarewtid).val('');
                        $('#' + twtid).html('');
                    }
                    if (data != null && data != 'null' && data.length > 0) {
                        var cargoWtunit = $('#' + wtunitid).val();
                        str = convertWeightUnit(str, 'K', cargoWtunit);
                        $('#' + wtunitid + '_previous').val(cargoWtunit);
                        $('#' + tarewtid).val(str);
                    }
                    if (str != null && str != 'null' && str != '') {
                        $('#' + twtid).html($('#' + tarewtid).val());
                        $('#' + tarewtid).focus();
                        //  checkGrossWeight(twtidno);
                    }

                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        }
    }

    function calcBLTareWeight(value, tareweightid, twt, cargowt, srs) {
        var tarwtunitid = tareweightid.replace('tareweight', '');
        var tarwtunitval = '';
        tarwtunitid = "tareweighthidunit" + tarwtunitid;
        if (srs == 'S') {
            $('#' + cargowt).val("");
        }
        var checkunitdiffcalc = true;
        var unitcntrid = tareweightid.replace("tareweightsicontainerid", "");
        var unitclass = ".unitclass" + unitcntrid;
        var currentunitid = cargowt.replace("weightsicontainerid", "");
        currentunitid = "unitsicontainerid" + currentunitid;
        $(unitclass).each(function () {
            if (currentunitid != $(this).attr('id')) {
                if (value == $(this).val()) {
                    checkunitdiffcalc = false;
                }
            }
        });
        console.log('tarwtunitid>>' + tarwtunitid);
        tarwtunitval = $('#' + tarwtunitid).val();
        if (value == 'T') {
            if (tarwtunitval == 'K') {
                var tare = parseFloat($('#' + tareweightid).val() / 1000);
                $('#' + tareweightid).val(tare);
                $('#' + tarwtunitid).val("T");
                $('#' + twt).html(tare);
            }

        } else {
            if (tarwtunitval == 'T') {
                if (srs == 'S') {
                    var tarewgt = parseFloat($('#' + tareweightid).val() * 1000);
                } else {
                    var tarewgt = parseFloat($('#' + tareweightid).val());
                }
                tarewt = tarewgt.toString();
                var tarewtsplit = tarewt.split(".");
                if (tarewtsplit.length == "1") {
                    tarewgt = tarewgt + ".0";
                }
                $('#' + tareweightid).val(tarewgt);
                $('#' + twt).html(tarewgt);
                $('#' + tarwtunitid).val("K");
            }
        }
    }
    $.fn.serializeObject = function ()
    {
        var o = {};
        var a = this.serializeArray();
        //a.MaxJsonLength = Int32.MaxValue;
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
    var alluncheckedVals = [];
    function ExportData()
    {
        $('.selectcls').attr("disabled", false);
        $('.socslt').attr("disabled", false);
        alluncheckedVals = [];
        var noncheckid = "";
        $('[name=select]:not(:checked)').each(function () {
            noncheckid = $(this).attr("id");
            document.getElementById(noncheckid).checked = true;
            alluncheckedVals.push($(this).attr("id"));
        });
        var jsonval = JSON.stringify($('form').serializeObject());
        $.ajax({
            url: 'ExcelExportImport?formval=sessionjson',
            type: 'POST',
            data: {"formjonval": jsonval},
            dataType: 'json',
            success: function (result) {
                getExcel();
            },
            error: function (result) {
                getExcel();
            }
        });
    }
    function getExcel()
    {
        var noncheckid = "";
        var j = 0;
        for (j = 0; j < alluncheckedVals.length; j++)
        {
            noncheckid = alluncheckedVals[j];
            document.getElementById(noncheckid).checked = false;
        }
        alluncheckedVals = [];
        var url = "ExcelExportImport?formval=downloadexcel";
        document.getElementById("SIEditForm").action = url;
        document.getElementById("SIEditForm").submit();
        $('.selectcls').attr("disabled", true);
        $('.socslt').attr("disabled", true);
    }

    function closeErrorModel()
    {
        // document.getElementById('#importerr').innerHTML="";
        $('#importerr').html("");
        $("#myModalImportErrors").modal("hide");
        $("#myImportModal").modal("show");
        document.getElementById('importexcel').value = "";
        return false;
    }

    function closeModel()
    {
        document.getElementById('importexcel').value = "";
        $("#myImportModal").modal("hide");
        return false;
    }

    function trim(value) {
        return LTrim(RTrim(value));
    }
    function LTrim(value) {
        var re = /\s*((\S+\s*)*)/;
        return value.replace(re, "$1");
    }
// Removes ending whitespaces
    function RTrim(value) {
        var re = /((\s*\S+)*)\s*/;
        return value.replace(re, "$1");
    }
    function checkGrossWeight(id, wid)
    {
        var cargoans = 0;
        var cargoval = 0;
        var unitval = "";
        var gross = 0;
        var totgross = 0;
        var socval = $('#soc' + id).val();
        var tareweightval = $('#tareweight' + id).val();
        if (socval != 'Y') {
            var unitname = id + "#unit#";
            var cargoname = id + "#newwt#";
            var cargovaluvalues = [];
            var unitvalues = [];
            $('input[name=' + cargoname + ']').each(function () {
                cargovaluvalues.push($(this).val());
            });
            $('select[name=' + unitname + ']').each(function () {
                unitvalues.push($(this).val());
            });
            var grossflag = (unitvalues.indexOf("K") > -1);
            var tonflag = (unitvalues.indexOf("T") > -1);
            if (grossflag == true && tonflag == true) {
                alert("<bean:message key="ecom.msg.cargoweighttype" bundle="<%=clientcode%>"/>");
                return false;
            }

            for (var i = 0; i < cargovaluvalues.length; i++)
            {
                if (cargovaluvalues[i] != "") {

                    unitval = unitvalues[i];
                    cargoval = cargovaluvalues[i];
                    if (unitval == 'T') {
                        cargoans = parseFloat(cargoval);
                        gross = parseFloat(cargoans);
                        totgross = parseFloat(gross) + parseFloat(tareweightval);
                        if (!(parseFloat(totgross) <= 50)) {
                            alert("<bean:message key="ecom.msg.grossweightton" bundle="<%=clientcode%>"/>");
                            $('#' + wid).val('');
                            //  $('#cargovaluecheckT').val('false');
                            return false;
                        } else {
                            //  $('#cargovaluecheckT').val('true');
                        }

                    } else {
                        cargoans = parseFloat(cargoval);
                        gross = parseFloat(gross) + parseFloat(cargoans);
                        totgross = parseFloat(gross) + parseFloat(tareweightval);
                        console.log("totgross:::::::::::" + totgross);
                        if (!(parseFloat(totgross) <= 50000)) {
                            alert("<bean:message key="ecom.msg.grossweightkilos" bundle="<%=clientcode%>"/>");
                            //	console.log("uuasdfasdf:::::::::::"+wid);
                            $('#' + wid).val('');
                            ///  $('#cargovaluecheckG').val('false');
                            return false;
                        } else {
                            //   $('#cargovaluecheckG').val('true');
                        }

                    }

                }

            }
        }
        return true;
    }

    function splitCCMailId(val) {
        var email = val;
        email = email.replace(",,", ",");
        email = email.replace(";,", ",");
        email = email.replace(";,", ",");
        email = email.replace(",;", ",");
        email = email.replace(/;/gi, ",");
        var result = email.replace(/\s/g, "").split(/,|;/);
        for (var i = 0; i < result.length; i++) {
            if (i == 0) {
                email = result[i];
            } else {
                email = email + "," + result[i];
            }
        }
        $('#ccMailId').val(email);
    }
    $(document).ready(function () {
        $('#headershipper,#headerconsignee,#headernotify').attr('maxlength', "450");
    <%
        if ("N".equalsIgnoreCase(carrierActualOptional)) {
    %>
        $(".carrierseallabel").html($(".carrierseallabel").html() + '<span class="mandatory">*</span>');
        $(".checkcarrierseal").addClass("required");
    <%}%>

        loadStateAutoComplete('ebook_notify2country_name', 'ebook_notify2state', 'typeheadhidden10', 'load');
        loadStateAutoComplete('ebook_notify3country_name', 'ebook_notify3state', 'typeheadhidden11', 'load');
        loadStateAutoComplete('ebook_forwardercountry_name', 'ebook_forwarderstate', 'typeheadhidden12', 'load');
        loadStateAutoComplete('newcustcountry', 'newcuststate', 'typeheadhidden13', 'load');
        if ($("#ebook_buyercountry_name").length > 0) {
            loadStateAutoComplete('ebook_buyercountry_name', 'ebook_buyerstate', 'typeheadhidden16', 'load');
        }
        if ($("#ebook_sellercountry_name").length > 0) {
            loadStateAutoComplete('ebook_sellercountry_name', 'ebook_sellerderstate', 'typeheadhidden17', 'load');
        }
        $('#hidden_bltype').val($('#bltype').val());
    });
    function enableContainer(id) {
        $('.container-body-inner').css('display', 'none');
        $('.box_bg').removeClass('active');
        $('.' + id + '_box_bg').addClass('active');
        console.log('id======> ' + id);
        $("#" + id).css("display", "block");
    }

    function acceptCommoOnlyEnglish(thisInput) {
    <% if ("Y".equalsIgnoreCase(CommoAcceptEngOnly)) {
        String commoEngRegex = (String) getServletContext().getAttribute(GeneralConf.CommodityDescInOnlyENG_Regex);
        if (commoEngRegex != null && !commoEngRegex.isEmpty()) { %>
        thisInput.value = thisInput.value.split(<%= commoEngRegex %>).join('');
    <%  } else { %>
        thisInput.value = thisInput.value.split(/[^a-zA-Z0-9\s!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~]/).join('');
    <%  }
    }%>
    }

</script>
<script type="text/javascript">
    var myNicEditor;
    bkLib.onDomLoaded(function () {
        //nicEditors.allTextAreas();
        myNicEditor = new nicEditor({fullPanel: true}).panelInstance('bodymail');
    });
    function showMailDialog() {
        $("#simailmodal").modal("show");
    }
    function mailDialogClose() {
        $('#simailmodal').modal('hide');
    }
    function sendSIMail() {
        $.ajax({
            type: "POST",
            cache: false,
            url: "SIAjax?type=SICHANGEREQMAIL&bodymail=" + encodeURIComponent($('#simailmodal').find('.nicEdit-main').html()) + "&tomail=" + $('#tomail').val() + "&subjectmail=" + $('#subjectmail').val() + "&bookno=<%=(String) session.getAttribute("edit_bookno")%>&blno=<%=(String) session.getAttribute("edit_blno")%>",
            async: false,
            data: $(this).serializeArray(),
            success: function (data) {
                alert(data);
                $('#simailmodal').modal('hide');
                window.location.href = "ViewBookingAll.do?BookingRequestType=oblprint";
            },
            error: function (xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
            }
        });
    }
    function checkEUCountry(countryid) {
        var countryValue = $("#" + countryid).val().trim();
        if (!countryValue) {
            return false;
        }
        var countrycode = countryValue.split('-')[0].trim().toUpperCase();
        return europeCountryJson.includes(countrycode);
    }
    function isNewCustomer(custcodeid) {
        if ($("#" + custcodeid).val() == "") {
            return true;
        }
        return false;
    }
 function addCustRowAjax(cust, type) {
console.log('Si booking '+type+'//cust//'+cust)
    const container = $('#'+cust+'Container');
    console.log(cust+'type'+type)
    let currentVal = parseInt($('#' + cust + 'Count').val(),10) || 0;
    let nextIndex = currentVal + 1;
  console.log('nextIndex: :::' + nextIndex);
   // AJAX call to get new buyer row from JSP template
    $.ajax({
        type: "POST",
        async: false,
        url: 'SICommonAddCust.jsp?id=' + nextIndex + '&custype=' + cust,
        data: $(this).serializeArray(),
        cache: false,
        success: function (data) {
        
            console.log('nextIndex: ' + nextIndex);
        
            container.append(data);
            applyRules(cust+'Container',cust, '<%=HideFaxNo%>' );
            // Update the buyer count
            $('#'+cust+'Count').val(nextIndex + 1);
            disableCustAddButton();
            setSellerNameSelectOption();
        },
        error: function () {
            alert('Failed to load buyer row.');
        }
    });
}
   function setCountry_New(field1, field2) {
        if (document.getElementById(field1).value == "") {
            document.getElementById(field2).value = "";
        }
        if (document.getElementById(field1).value != "") {
            document.getElementById(field2).value = trim(document.getElementById(field1).value.split("-")[0]);
        }
    }
    function setCityDesc_New(field1, field2) {
        if (document.getElementById(field1).value != "") {
            document.getElementById(field2).value = document.getElementById(field1).value;
        }
    }
</script>
<script type="text/javascript">
    $(document).ready(function () {
        $('#datatablebychargetab').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });


function removeBuyerRow(btn) {
    $(btn).closest('.buyer-row').remove();

    // Recalculate count after removal
    const count = $('#buyerContainer .buyer-row').length;
    $('#buyerCount').val(count);
}
function setENSType() {
    const ensDocType = $('#ebook_EnsDocType');
    const ensFiling = $('#ebook_EnsFiling');
    console.log('ens type ======> ' + ensFiling+'ens type '+ensDocType );
    if (ensDocType.length >0 ) {
    const divButtons = $('#ensDocType_btn').find('button');
    divButtons.removeClass('active').prop('disabled', true);

    let ensTypeVal = ensDocType.val() || 'S';  
    console.log('ens type ======> ' + ensTypeVal);

    ensDocType.val(ensTypeVal);

    if (ensTypeVal == 'S') {
        $('#ensDocType_btn > .sbl').addClass('active');
    } else if (ensTypeVal == 'C') {
        $('#ensDocType_btn > .cnd').addClass('active');
    }

}
    if (ensFiling.length >0 ) {
    const divButtons = $('#ensfilling_btn').find('button');
    divButtons.removeClass('active').prop('disabled', true);

    let ensFilingVal = ensFiling.val() || 'S';  
    console.log('ens Filing ======> ' + ensFilingVal);

    ensFiling.val(ensFilingVal);

     if (ensFilingVal == 'S') {
        $('#ensfilling_btn > .sng').addClass('active');
    } else if (ensFilingVal == 'P') {
        $('#ensfilling_btn > .prl').addClass('active');
    }else{
         $('#ensfilling_btn > .ena').addClass('active');
    }

}

}
function setENSFiling() {
    const ensDocType = $('#ebook_EnsDocType');
    if (ensDocType.length >0 ) {
     var pol = $("#originid").val();
     var pod = $("#finalportid").val();
 if (pol !== "" && pod !== "") {
            $.ajax({
                type: 'POST',
                cache: false,
                url: 'EnsFilingAjax?type=checkEUPort&origin=' + pol + '&delivary=' + pod,
                async: false,
                data: $(this).serializeArray(),
                success: function (response) {
                    var isEuPort = response;
                    if (isEuPort == 'Y') {
                  $('#euport').val('Y');
                    } else {
                  $('#euport').val('N');   
                    }
                },
                error: function (xhr) {
                    if (xhr.status === 403) {
                        window.location.href = 'SessionExpired.jsp';
                    }
                }
            });
        } 
    

}
}
  function disableCustAddButton() {
    const ensDocTypeElem = $('#ebook_EnsDocType');
    const addSellerBtn = $('#addSellerBtn');
    const addBuyerBtn = $('#addBuyerBtn');

    if (ensDocTypeElem.length > 0) {
        addSellerBtn.removeClass('disabled');
        addBuyerBtn.removeClass('disabled');
        if (ensDocTypeElem.val() === 'S') {
            addSellerBtn.addClass('disabled');
            addBuyerBtn.addClass('disabled');
        }
        $("input[name='select']").each(function () {
            const cargoId = this.value;
            const cmdtyhblno = $('#' + cargoId + '\\#cmdty_hblno\\#');

            if (cmdtyhblno.length > 0) {
                cmdtyhblno.prop('readonly', true);

                if (ensDocTypeElem.val() === 'C') {
                    cmdtyhblno.prop('readonly', false);
                } else {
                    cmdtyhblno.val('');
                }
            }
        });
    }
}
</script>
<style>
    .nicEdit-main {
        overflow: auto !important;
        height: 13em;
    }
    .jexcel > thead > tr > td {
        background-color: #009ace;color: #fff;
        font-family: 'Lato', sans-serif !important;
        font-weight: 600;
        font-size: 14px;
    } 
    .jexcel > thead > tr > td.selected {
        background-color: #026386;
    }
    .jexcel > tbody > tr > td {
        font-family: 'Lato', sans-serif !important;
        font-size: 14px;
    }
    .jexcel .highlight-selected {
        background-color: rgb(0 0 0 / 8%);
    }
</style>