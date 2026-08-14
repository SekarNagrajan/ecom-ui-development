<%-- 
    Document   : SIBLCommonCargo
    Created on : Dec 17, 2020, 10:54:53 AM
    Author     : gobinath.t
--%>
<%@page contentType="text/html; charset=UTF-8" import="net.ecom.common.GeneralConf,java.util.ArrayList, java.util.HashMap,java.util.Set,java.util.Hashtable,net.ecom.vo.SIDtableVO, net.ecom.vo.SIPartiesVO,net.ecom.vo.SIContainerVO,java.util.List,net.ecom.common.Validation,java.util.Map,net.ecom.vo.SIContainerDetailVO,net.ecom.vo.SIMasterVO,net.ecom.vo.SISealVO,net.ecom.vo.SIConatainerValueVO,net.ecom.vo.CustomerDetailsVO,net.ecom.common.utils.DateConversion_New,net.ecom.common.NumberFormat,net.ecom.vo.ChargesVo"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
    session.setAttribute("SISEND", "SISend");
    String type12 = ((String) session.getAttribute("SItype"));
    String blno = (String) session.getAttribute("edit_blno") != null ? (String) session.getAttribute("edit_blno") : "";
    String sino_new = (String) session.getAttribute("edit_sino") != null ? (String) session.getAttribute("edit_sino") : "";
    String errors_new = (String) request.getAttribute("sierrors") != null ? (String) request.getAttribute("sierrors") : "";
    String agencyRefNo = (String) request.getAttribute("SIAGENCYREFNO") != null ? (String) request.getAttribute("SIAGENCYREFNO") : "";
    String user = (String) session.getAttribute("sessionAdmin");
    List apList = (List) session.getAttribute("APCUSTLIST");
    List shList = (List) session.getAttribute("SHCUSTLIST_SI");
    List cnList = (List) session.getAttribute("CNCUSTLIST_SI");
    List ntList = (List) session.getAttribute("NTCUSTLIST_SI");
    List fwList = (List) session.getAttribute("FWCUSTLIST_SI");
    List whList = (List) session.getAttribute("WHCUSTLIST_SI");
    String commodityDescDisclaimer = (String) getServletContext().getAttribute(GeneralConf.CommodityDescInOnlyENG_Disclaimer);
    String CommodityName_as_HS_Chapter = (String) getServletContext().getAttribute(GeneralConf.CommodityName_as_HS_Chapter);
    String Enable_cargodesc_mandatory = (String) getServletContext().getAttribute(GeneralConf.Enable_cargodesc_mandatory);
    System.out.println("[ECOM-WAR] SIBLCommonCargo commodityDescDisclaimer===>" + commodityDescDisclaimer);
    String printOnBlAdd = (String) getServletContext().getAttribute(GeneralConf.PrintOnBlAddMandatory);
    String ccMailId = Validation.checkNull((String) session.getAttribute("SESSION_USER_CC_MAILID"));
    String bookstatus = (String) session.getAttribute("bookstatus");
    //    System.out.println("[ECOM-WAR] SIBLCommonCargo welcome boodsts:::::" + bookstatus);
    String DispConsigToOrder = (String) getServletContext().getAttribute(GeneralConf.DisplayConsigneeToOrder);
    Hashtable comlist = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
    Hashtable packagemap = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
    String Enable_Customize_Import_Export_Excel = (String) getServletContext().getAttribute(GeneralConf.Enable_Customize_Import_Export_Excel);
    CustomerDetailsVO custVO = null;
    String clientcode = (String) session.getAttribute("clientcode");
    String Ventilationlabel = (String) getServletContext().getAttribute("Ventilation");
    String booknum = (String) session.getAttribute("edit_bookno");
    String snum = (String) session.getAttribute("edit_sino");
    String sino = "";
    if (snum != null && snum.length() > 0 && !"SION".equalsIgnoreCase(snum)) {
        sino = (String) session.getAttribute("edit_sino");
    }
    String weight_unit_kilos_only = (String) getServletContext().getAttribute(GeneralConf.WEIGHT_UNIT_KILOS_ONLY);
    String carrierActualOptional = (String) getServletContext().getAttribute(GeneralConf.CARRIER_SEAL_CONTAINER_OPTIONAL);
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String exceluploadpath = (String) getServletContext().getAttribute(GeneralConf.exceluploadpath);
    String enableaesno = (String) getServletContext().getAttribute(GeneralConf.ENABLE_AES_NUMBER);
    long jsdate = new java.util.Date().getTime();
    String headerConfig = (String) request.getAttribute("HEADERCONFIGURATION");
    String siAppVersion = (String) request.getAttribute("EDITSIAPPVERSION");
    String hscodeconfig = (String) request.getAttribute("HSCODECONFIGURATION");
    String shipperSealconfig = (String) session.getAttribute("SHIPPERSEALCONFIG");
    System.out.println("[ECOM-WAR] SIBLCommonCargo hscodeconfig::::::>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + hscodeconfig);
    String v1DataEnableStatus = (String) getServletContext().getAttribute(GeneralConf.V1DATAENABLESTATUS);
    String fakCommodity = (String) getServletContext().getAttribute(GeneralConf.FAKCOMMODITY);
    String pdfpath = (String) getServletContext().getAttribute("Imagepath");
    String fakComdtyCode = "";
    String fakComdtyName = "";
    System.out.println("[ECOM-WAR] SIBLCommonCargo fakCommodity>>>>" + fakCommodity);
    fakCommodity = fakCommodity != null ? fakCommodity : "";
    if (fakCommodity != null && fakCommodity.length() > 0) {
        String fakCommodityArr[] = fakCommodity.split("~~");
        if (fakCommodityArr.length == 2) {
            fakComdtyCode = fakCommodityArr[0].trim();
            fakComdtyName = fakCommodityArr[1].trim();
        }
    }
    List custtypelist = (List) getServletContext().getAttribute(GeneralConf.CUSTTYPECONFIG);
    String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);
    Map<String, String> mailAlertMap = (Map<String, String>) session.getAttribute("USERMAILALERTSESSIONMAP");
    String checkMailConfig = "N";
    Map<String, List<String>> map = (Map<String, List<String>>) getServletContext().getAttribute("MAILALERTACTIVITYLIST");
    String termsandconditions = (String) getServletContext().getAttribute("Terms_and_Conditions");
    if (map != null) {
        List<String> mailActList = map.get("BKG");
        System.out.println("[ECOM-WAR] SIBLCommonCargo mailActList>>>" + mailActList + "  mailAlertMap>>>" + mailAlertMap);
        if (mailActList != null && mailAlertMap != null) {
            for (int i = 0; i < mailActList.size(); i++) {
                System.out.println("[ECOM-WAR] SIBLCommonCargo mailAlertMap.get(mailActList.get(i))>>" + mailAlertMap.get(mailActList.get(i)));
                if ("Y".equalsIgnoreCase(mailAlertMap.get(Validation.checkNull(mailActList.get(i)).trim()))) {
                    checkMailConfig = "Y";
                }
            }
        }
    }
    List EBK_pluginList = (List) getServletContext().getAttribute("EBK_PLUGIN_DETAILS");
    boolean is_SI_restriction = false;
    if (EBK_pluginList != null && EBK_pluginList.contains("ESP")) {
        is_SI_restriction = true;
    }
    String agencyemailid = (String) request.getAttribute("agencyemailid");
    String loadportcountry = (String) request.getAttribute("LoadportCountry");
    String SHOW_CHARGES_IN_SIBL = (String) getServletContext().getAttribute(GeneralConf.SHOW_CHARGES_IN_SIBL);
    List Chargedetails = (List) request.getAttribute("CHARGEDETAILS");
    String ENABLE_INSURANCE = (String) getServletContext().getAttribute(GeneralConf.ENABLE_INSURANCE);
    String insuranceaccess = (String) session.getAttribute("SESSION_InsuranceAccess");
    String HIDE_MASKPORT_CONTAINER = (String) getServletContext().getAttribute(GeneralConf.HIDE_MASKPORT_CONTAINER);
    String shipperseallength = (String) getServletContext().getAttribute(GeneralConf.SI_Shipper_Seal_Length);
    if (shipperseallength == null || Validation.checkNull(shipperseallength).length() == 0) {
        shipperseallength = "17";
    }
    String ENABLE_ECOMOOG_DETAILS = (String) getServletContext().getAttribute(GeneralConf.ENABLE_OOG_DETAILS);
    boolean is_SI_OOG = false;
    if (ENABLE_ECOMOOG_DETAILS != null && "Y".equalsIgnoreCase(ENABLE_ECOMOOG_DETAILS)) {
        is_SI_OOG = true;
    }
    String EnableReeferDetails = (String) getServletContext().getAttribute(GeneralConf.EnableReeferDetails);
    String EditEnableReeferDetails = (String) getServletContext().getAttribute(GeneralConf.EditEnableReeferDetails);
    System.out.println("[ECOM-WAR] SIBLCommonCargo EnableReeferDetails>>>>>>>>" + EnableReeferDetails + "  EditEnableReeferDetails>>>>>>>>" + EditEnableReeferDetails);
    String cargoSIEdit = null;
    if ("N".equalsIgnoreCase(EditEnableReeferDetails)) {
        cargoSIEdit = "disabled='true'";
        System.out.println("[ECOM-WAR] SIBLCommonCargo cargoSIEdit>>>>>>>>" + cargoSIEdit);

    } else {

        cargoSIEdit = "";
        System.out.println("[ECOM-WAR] SIBLCommonCargo cargoSIEdit>>>>>>>>" + cargoSIEdit);
    }
    String enableapplyallcont = (String) getServletContext().getAttribute(GeneralConf.Enable_Apply_To_All_Customer);
    String hsnamecode = (String) getServletContext().getAttribute(GeneralConf.Display_HS_NAME_CODE);
    String enableSmartImport = (String) getServletContext().getAttribute(GeneralConf.Enable_SI_Smart_Import);
    String TENS_Documentation = (String) getServletContext().getAttribute(GeneralConf.TENS_Documentation);
    String hslencheck = (String) getServletContext().getAttribute(GeneralConf.ENABLE_HSCODE_LENGTH_VALIDATION);
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
<style>
    .switchToggle .tgl_round:active:after {
        width: 17px !important;
    }
</style>

<h6><i class="step-icon fa_cargo"></i> <bean:message key="shipping.cargo.head" bundle="<%=clientcode%>"/> </h6>


<fieldset>  	
    <div class="details_search">
        <div class="header_tle">  <bean:message key="shipping.cargo.head" bundle="<%=clientcode%>"/>
            <%if (exceluploadpath != null && exceluploadpath.length() > 0) {%>
            <div class="si_imp_exp_excel pull-right">
                <%if (is_SI_restriction) {%>
                <span class="pull-right"><a href="javascript:void(0);" onclick="showMailDialog();" class="btn btn-info btn-sm si_imp_exp_excel" id="genmailbtn" style="background: #ffc107 !important;border-color: #ffc107 !important;"><i class="fa fa-envelope-o"></i> <bean:message key='ecom.si.generateemail' bundle="<%=clientcode%>"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                <%}%>
                <span class="pull-right"> <a href="javascript:void(0)" onclick='<%=(("Y".equalsIgnoreCase(Enable_Customize_Import_Export_Excel)) ? "ExportDataCustomize()" : "ExportData()")%>' title="Download Template" class="btn btn-info btn-sm si_imp_exp_excel" id="exportsearch"><i class="fa fa-download"></i> <%if ("Y".equalsIgnoreCase(Enable_Customize_Import_Export_Excel)) {%> Download Excel <%} else {%><bean:message key="ebooking.cargo.expexcel" bundle="<%=clientcode%>"/><%}%> </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                <span class="pull-right"> <a href="javascript:void(0)"  data-toggle="modal" data-target="#myImportModal" class="btn btn-info btn-sm si_imp_exp_excel" id="importsearch"><i class="fa fa-upload"></i>  <%if ("Y".equalsIgnoreCase(Enable_Customize_Import_Export_Excel)) {%> Upload  Excel <%} else {%><bean:message key="ebooking.cargo.impexcel" bundle="<%=clientcode%>"/><%}%> </a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                <%if ("Y".equalsIgnoreCase(enableSmartImport)) {%>
                <span class="pull-right"><a href="javascript:void(0);" onclick="openSmartImportBox()" class="btn btn-info btn-sm si_imp_exp_excel" id="sismartimportbtn" ><i class="fa fa-table"></i> <bean:message key='ecom.si.smart.import' bundle="<%=clientcode%>"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                <%}%>
            </div>
            <%}%> 
            <input type="hidden" id="EnableReeferDetails" value="<%=Validation.checkNull(EnableReeferDetails).toUpperCase()%>">
        </div>
        <%if (is_SI_restriction) {%>
        <div class="row"><span class="col-sm-12"><span class="float-right" style="margin-right: 4%;"><span class="text-info font-weight-bold" style="color: #2196f3 !important;"> * To change the non-editable fields. Please click on 'Request Booking update' button. </span></span></span></div>
        <%}%>
        <div id="cargo_details">
            <div class="row">

                <% String sicontainerid = "sicontainerid", selectOnClickFun = "", addreq = "", addreq1 = "", qtyclass = "", weigthclass = "", volumeclass = "", strIsSelect = "NO", isinsurance = null, quoteno = null, insurance = null, In_cust_type = null, suminsured = "", insuredemail = "";
                    int aa = 1, con = 0;
                    StringBuilder selectOnclick = new StringBuilder("");
                    HashMap<SIMasterVO, SIConatainerValueVO> cntMapObj = (HashMap<SIMasterVO, SIConatainerValueVO>) session.getAttribute("containerListForSI");
                    System.out.println("[ECOM-WAR] SIBLCommonCargo cntMapObj.size() is=====-----------------=====>" + cntMapObj.size());
                    if (cntMapObj != null && cntMapObj.size() > 0) {%>
                <div class="col-sm-3" style="max-width: 21%;">
                    <%Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = cntMapObj.entrySet();
                        for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                            sicontainerid = "sicontainerid" + aa;
                            qtyclass = "qtyclass" + aa;
                            weigthclass = "weigthclass" + aa;
                            volumeclass = "volumeclass" + aa;
                            SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                            SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                            SISealVO siSealVOObj = (SISealVO) siCNTValueVO.getSiSealObj();
                            strIsSelect = siSealVOObj.getStrIsSelect();
                            con = siMasterVO.getStrSelect();
                            selectOnclick = new StringBuilder("");
                            selectOnclick.append("setContNo('containersNo");
                            selectOnclick.append(sicontainerid);
                            selectOnclick.append("','");
                            selectOnclick.append(addreq);
                            selectOnclick.append("','");
                            selectOnclick.append(sicontainerid);
                            selectOnclick.append("','");
                            selectOnclick.append(qtyclass);
                            selectOnclick.append("','");
                            selectOnclick.append(weigthclass);
                            selectOnclick.append("','");
                            selectOnclick.append(volumeclass);
                            selectOnclick.append("',this)");
                            selectOnClickFun = selectOnclick.toString();
                            strIsSelect = "YES";

                    %>
                    <a href="javascript:void(0);" onclick="enableContainer('<%=sicontainerid%>')"> <div class="box_bg <%=sicontainerid + "_box_bg"%> <%=aa == 1 ? " active" : ""%>" >

                            <span class="Eqp_size p-l-10 eqpclassall " id="eqptypeval<%=sicontainerid%>"> <%=siMasterVO.getStrType()%>  </span> <span class="vertical_divider"> </span> <span class="container_code p-l-10 "> <%if("Y".equalsIgnoreCase(HIDE_MASKPORT_CONTAINER)){%>CONTAINER <%=aa%><%} else {%><%=siMasterVO.getStrCntNo()%><%}%> </span>  
                            <span class="float-right"><input type="checkbox" name="select" id="select<%=con%>" value="<%=sicontainerid%>" data-size="xs" onClick="<%=selectOnClickFun%>, socstatuscheck('<%=con%>', '<%=sicontainerid%>')" <%=("YES".equalsIgnoreCase(strIsSelect) ? "checked" : "")%> class ="required selectcls switchery" disabled="disabled"/>
                                <label for="select<%=con%>" class="font-medium-2 text-bold-600 ml-1"> </label></span>

                        </div>

                    </a>
                    <% aa++;
                        }

                    %>
                </div>
                <%}%>
                <div class="col-sm-9">
                    <%
                        String requestAction = (String) request.getAttribute("SUBMISSION_EDIT");
                        requestAction = Validation.checkNull(requestAction);
                        Hashtable cntDetails = (Hashtable) session.getAttribute("SI_CONTAINER_DETAILS");
                        int cntDtIncr = 1;
                        String dtCmdName = "", dtCmdId = "", dtWtName = "", ntWtName = "", dtWtId = "", ntWtId = "", dtUnitName = "", ntUnitName = "", dtUnitId = "", dtQtyName = "", dtQtyId = "", dtCidName = "", dtCidId = "";
                        String dtCmd = "", dtCmd1, dtWt = "", ntWt = "", dtUnit = "", dtQty = "", dtCid = "",byc="",byn="",slc="",sln="",hbln="";
                        String comdId = "", cmdEvt = "", csId = "", csEvt = "", ssId = "", ssEvt = "", wtId = "", ntId = "", qtyId = "", cidId = "";
                        String cntNo = "", containerName = "", socname = "", shipperSealName = "", carrierSealName = "", shipperSeal = "", carrierSeal = "", eqpType = "", eqpTypeName = "", strCargoId = "", strCargoIdName = "", strEqpType = "", strEqpTypeName = "", strRateEqpTypeName = "", strRateEqpType = "";
                        String dtPkgName = "", pkgId = "", dtPkg = "", dtVolName = "", volId = "", dtVol = "", dtVutName = "", vutId = "", dtVut = "", dtPkgId = "", dtVolId = "", dtVutId = "", pkgEvt = "";
                        String dtMrkName = "", mrkId = "", dtMrk = "", dtDesName = "", desId = "", dtDes = "", dtMrkId = "", dtDesId = "";
                        String newcomdtyname = "", newcomdtyid = "", newpackid = "", newpackname = "", newwtid = "", newntid = "", newntname = "", newwtname = "", newvolid = "", soc = "", newvolname = "", newqtyid = "", newqtyname = "",buyerName = null,sellerName = null,cmdtyhblno=null,buyerCode=null,sellerCode=null,sellerSelect=null,buyerSelect=null;
                        String buyerNameid = null,sellerNameid = null,cmdtyhblnoid=null,buyerCodeid=null,sellerCodeid=null,sellerSelectid=null,buyerSelectid=null;
                        String hiddencomid = "", markdescclass = "", hiddenpackid = "", unitclass = "", qtyclass1 = "", netweightclass = "", weigthclass1 = "", volumeclass1 = "", containerclass = "", cargodescclass = "";
                        String cargono = "", imocontact = "", imophone = "";
                        String itemforward = "", itemtop = "", itemright = "", itemaft = "", itemleft = "", itemlengthUnit = "";
                        String tempmax = "", settemp = "", tempmin = "", tempunit = "";
                        String hazardous = "", cargoweight = "", netweight = "", tareweight = "", grossweight = "", eqpStatusId = "";
                        String nor = "", oog = "", refer = "";
                        String cargononame = "", imocontactname = "", imophonename = "", hidhscodename = "", hidhscodeid = "", hssuffixcode = "";
                        String itemforwardname = "", itemtopname = "", itemrightname = "", itemaftname = "", itemleftname = "", itemlengthUnitname = "";
                        String tempitemforwardname = "", tempoogid = "", tempitemtopname = "", tempitemrightname = "", tempitemaftname = "", tempitemleftname = "", tempitemlengthUnitname = "";
                        String tempmaxname = "", settempname = "", tempminname = "", tempunitname = "";
                        String hazardousname = "", netweightname = "", hazardousid = "", cargoweightname = "", tareweightname = "", grossweightname = "", hscodeid = "";
                        String norname = "", oogname = "", refername = "", hiddenhsid = "", hscodevalue = "", hscodevalue1 = "", incclass = "", incclassid = "", oogid = "", oogclass = "";
                        String humidityId = null, humidityFlagId = null, drainsId = null, co2Id = null, o2Id = null, setHumidity = null, setHumidityFlag = null, setDrains = null, setCo2 = null, setO2 = null;
                        String ventilationId = null, setVentilation = null;
                        String hscodecls = "", dgflagName = "", dgFlagVal = "";
                        String dimension = "";
                        ArrayList importexcellist = new ArrayList();
                        HashMap importexcelmap = null;
                        tablerowcount = 0;
                        int alsize = 0;
                        int cntListSize = 0;
                        int selCnt = 0, containerCount = 1, ccc = 0;
                        aa = 1;
                        String strCntOrder = "";
                        String scriptVef = "";
                        int bb = 1;
                        int headSize = 1;

                        String rowstyle = "even";
                        // HashMap<SIMasterVO, SIConatainerValueVO> cntMapObj = (HashMap<SIMasterVO, SIConatainerValueVO>) session.getAttribute("containerListForSI");
                        System.out.println("[ECOM-WAR] SIBLCommonCargo cntMapObj.size() is=====-----------------=====>" + cntMapObj.size());
                        if (cntMapObj != null && cntMapObj.size() > 0) {
                            System.out.println("[ECOM-WAR] SIBLCommonCargo ===============inside if=============");
                            alsize = cntMapObj.size();
                            Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = cntMapObj.entrySet();
                            for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                                cntDtIncr = 1;
                                sicontainerid = "sicontainerid" + aa;
                                con = 0;
                                comdId = "";
                                cmdEvt = "";
                                csId = "";
                                csEvt = "";
                                ssId = "";
                                ssEvt = "";
                                wtId = "";
                                ntId = "";
                                qtyId = "";
                                cidId = "";
                                strEqpType = "";
                                strRateEqpType = "";
                                strCargoId = "";
                                selectOnClickFun = "";
                                strCntOrder = "";
                                scriptVef = "";
                                strCargoIdName = "";
                                strEqpTypeName = "";
                                strRateEqpTypeName = "";
                                SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                                SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                                SISealVO siSealVOObj = (SISealVO) siCNTValueVO.getSiSealObj();
                                cntNo = siMasterVO.getStrCntNo();
                                con = siMasterVO.getStrSelect();
                                soc = siMasterVO.getStrsoc();
                                strRateEqpType = siMasterVO.getRateeqptypeid();
                                //
                                cargono = siMasterVO.getCargoNo();
                                cargoweight = siMasterVO.getCargoweight();
                                System.out.println("[ECOM-WAR] SIBLCommonCargo cargoweight sout" + cargoweight);
                                tareweight = siMasterVO.getTareweight();
                                cargoweight = siMasterVO.getCargoweight();

                                System.out.println("[ECOM-WAR] SIBLCommonCargo cargoweight sout 2" + cargoweight);
                                netweight = siMasterVO.getNetweight();
                                grossweight = siMasterVO.getGrossweight();
                                tempmin = siMasterVO.getTempMin();
                                tempmax = siMasterVO.getTempMax();
                                settemp = siMasterVO.getSetTemp();
                                oog = siMasterVO.getOog();
                                nor = siMasterVO.getNor();
                                refer = siMasterVO.getRefer();
                                tempunit = siMasterVO.getTempunit();
                                dgFlagVal = siMasterVO.getDgFlag();
                                setHumidity = siMasterVO.getHumidity();
                                setHumidityFlag = siMasterVO.getHumidity_flag();
                                setDrains = siMasterVO.getDrains();
                                setCo2 = siMasterVO.getCo2value();
                                setO2 = siMasterVO.getO2value();
                                setVentilation = siMasterVO.getVentilation();
                                if ("Y".equalsIgnoreCase(dgFlagVal)) {
                                    dgFlagVal = "Y";
                                } else {
                                    dgFlagVal = "N";
                                }

                                strEqpType = siMasterVO.getStrEqpType();
                                strCargoId = siSealVOObj.getStrCargoid();
                                strIsSelect = siSealVOObj.getStrIsSelect();
                                shipperSeal = siSealVOObj.getStrShipperSeal();
                                carrierSeal = siSealVOObj.getStrCarrierSeal();
                                addreq = "reqclass" + aa;
                                qtyclass = "qtyclass" + aa;
                                weigthclass = "weigthclass" + aa;
                                netweightclass = "netweightclass" + aa;
                                volumeclass = "volumeclass" + aa;
                                netweightclass = "netweightclass" + aa;
                                unitclass = "unitclass" + aa;
                                markdescclass = "markdescclass" + aa;
                                qtyclass1 = qtyclass;
                                weigthclass1 = weigthclass;
                                volumeclass1 = volumeclass;
                                csId = "carrierseal" + sicontainerid + "1";
                                csEvt = "csChk('" + csId + "');";
                                ssId = "shipperseal" + sicontainerid + "1";
                                ssEvt = "ssChk('" + ssId + "');";
                                selectOnclick = new StringBuilder("");
                                selectOnclick.append("setContNo('containersNo");
                                selectOnclick.append(sicontainerid);
                                selectOnclick.append("','");
                                selectOnclick.append(addreq);
                                selectOnclick.append("','");
                                selectOnclick.append(sicontainerid);
                                selectOnclick.append("','");
                                selectOnclick.append(qtyclass);
                                selectOnclick.append("','");
                                selectOnclick.append(weigthclass);
                                selectOnclick.append("','");
                                selectOnclick.append(volumeclass);
                                selectOnclick.append("','");
                                selectOnclick.append(netweightclass);

                                selectOnclick.append("',this)");
                                selectOnClickFun = selectOnclick.toString();
                                eqpType = siMasterVO.getStrType();
                                containerName = sicontainerid + "#containerID#";
                                socname = sicontainerid + "#socID#";
                                strCargoIdName = sicontainerid + "#cargoID#";
                                shipperSealName = sicontainerid + "#shipper#";
                                carrierSealName = sicontainerid + "#carrier#";
                                eqpTypeName = sicontainerid + "#eqpType#";
                                strCntOrder = sicontainerid + "#CNTORDER#";
                                scriptVef = sicontainerid + "#Scipt#";
                                strEqpTypeName = sicontainerid + "#typeID#";
                                strRateEqpTypeName = sicontainerid + "rateeqptypeid";
                                newcomdtyname = sicontainerid + "#newcmdty#";
                                newpackname = sicontainerid + "#newpack#";
                                newqtyname = sicontainerid + "#newqty#";
                                newwtname = sicontainerid + "#newwt#";
                                newvolname = sicontainerid + "#newvol#";
                                newntname = sicontainerid + "#newnt#";
                                eqpStatusId = sicontainerid + "#eqpstatus#";
                                dgflagName = sicontainerid + "#dgflagName#";
                                //
                                cargononame = sicontainerid + "#cargono#";
                                cargoweightname = sicontainerid + "#cargoweight#";
                                System.out.println("[ECOM-WAR] SIBLCommonCargo cargoweightname sout" + cargoweightname);
                                netweightname = sicontainerid + "#nettweight#";
                                tareweightname = sicontainerid + "#tareweight#";
                                grossweightname = sicontainerid + "#grossweight#";
                                tempminname = sicontainerid + "#tempmin#";
                                tempmaxname = sicontainerid + "#tempmax#";
                                settempname = sicontainerid + "#settemp#";
                                oogname = sicontainerid + "#oog#";
                                norname = sicontainerid + "#nor#";
                                refername = sicontainerid + "#refer#";
                                tempunitname = sicontainerid + "#tempunit#";
                                oogid = sicontainerid + "oogcheckbox";
                                tempoogid = sicontainerid + "tempoogcheckbox";
                                oogclass = "oogclass" + sicontainerid;
                                //
                                humidityId = sicontainerid + "#humidity#";
                                humidityFlagId = sicontainerid + "#humidityflag#";
                                drainsId = sicontainerid + "#drains#";
                                co2Id = sicontainerid + "#co2#";
                                o2Id = sicontainerid + "#o2#";
                                ventilationId = sicontainerid + "#ventilation#";
                                //
                                dimension = sicontainerid + "dimension";
                                ArrayList cntList = (ArrayList) siCNTValueVO.getCntDeatilsObj();
                                addreq1 = addreq;
                                System.out.println("[ECOM-WAR] SIBLCommonCargo ========cntList.size is============>" + cntList.size());
                                ccc = 1;

                                importexcelmap = new HashMap();
                                importexcelmap.put("nor", nor);
                                importexcelmap.put("eqpsize", eqpType);
                                importexcelmap.put("containerno", cntNo);
                                importexcelmap.put("soc", soc);
                                importexcelmap.put("type", strEqpType);
                                importexcellist.add(importexcelmap);
                                //

                                if ("Y".equalsIgnoreCase(nor)) {
                                    settemp = "";
                                    tempunit = "";
                                    setVentilation = "";
                                }
                    %>


                    <div class="container-body-inner" id="<%=sicontainerid%>" <%if (aa != 1) {%> style="display: none"<%}%>>
                        <div class="container_herader">   <bean:message key="ecom.shippingins.cargo.contno" bundle="<%=clientcode%>"/></div>
                        <div class="row box_height" >
                            <% if (cntDtIncr == 1) {%>
                            <input type="hidden" name="containersNo" id="containersNo<%=sicontainerid%>" class="docgrosswtcheck"/><input type="hidden" name="<%=strCntOrder%>" id="CntOrder" value="<%=con%>"/>
                            <input type="hidden" name="<%=strCargoIdName%>" id="<%=strCargoIdName%>" value="<%=strCargoId%>"/><input type="hidden" name="<%=strEqpTypeName%>" id="<%=strEqpTypeName%>" value="<%=strEqpType%>"/>
                            <input type="hidden" name="<%=strRateEqpTypeName%>" id="<%=strRateEqpTypeName%>" value="<%=strRateEqpType%>"/>
                            <div class="col-sm-3 form-group">
                                <label> <bean:message key="shipping.container.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span> </label>
                                <%if("Y".equalsIgnoreCase(HIDE_MASKPORT_CONTAINER)){%>
                                <input type="text" class="form-control light_border" maxlength="11" id="containerno<%=sicontainerid%>_display" name="containerno<%=sicontainerid%>_display" value="Container <%=aa%>" style="background-color: #e9ecef;" readonly>
                                <input type="hidden" name="<%=containerName%>" id="containerno<%=sicontainerid%>" value="<%=cntNo%>" class="form-control checkcntrdupclass skip-container-validation <%=containerclass%>"/>
                                <%} else {%>
                                <input type="text" name="<%=containerName%>" id="containerno<%=sicontainerid%>" maxlength="11" value="<%=cntNo%>"  class="form-control checkcntrdupclass copypasteclass validatebkgcontno light_border <%=containerclass%>" onblur="checkSIContNo('containerno<%=sicontainerid%>', 'soc<%=sicontainerid%>', 'tareweight<%=sicontainerid%>', 'unit<%=sicontainerid%>1', '<%=eqpType%>');
                                        calculateGrossWeight('<%=sicontainerid%>', '<%=sicontainerid + cntDtIncr%>');PayLoadValidation('containerno<%=sicontainerid%>', 'eqptypeval<%=sicontainerid%>');"/>
                                <%}%>
                                <div class="checkbox-inline" style="margin:7px 0px 0px -3px;">
                                    <label> <bean:message key="ebooking.soc.text.display" bundle="<%=clientcode%>"/> </label>
                                    <input type="checkbox" onClick="socstatus(this.id, '<%=sicontainerid%>');
                                            checkSIContNo('containerno<%=sicontainerid%>', 'soc<%=sicontainerid%>', 'tareweight<%=sicontainerid%>', 'unit<%=sicontainerid%>1', '<%=eqpType%>');" data-size="xs" id="selectsoc<%=sicontainerid%>" <%=("Y".equalsIgnoreCase(soc) ? "checked" : "")%>  disabled="disabled" class="socslt switchery"/>
                                    <label for="selectsoc<%=sicontainerid%>" class="font-medium-2 text-bold-600 ml-1"> </label>
                                </div>
                                <%if (is_SI_OOG) {%>
                                <div class="switchToggle checkbox-inline " style="margin:7px 0px 0px -3px;display: inline-flex;" >
                                    <span class="toggle_label" style="padding-right: 5px;"> <bean:message key="ebooking.oog.text.display" bundle="<%=clientcode%>"/> </span>
                                    <input type="checkbox"  id="<%=oogid%>" name="<%=oogid%>" <%=("Y".equalsIgnoreCase(oog) ? "checked" : "disabled")%>  value="<%=oog%>" onchange="oogEnable(this, '<%=sicontainerid%>', '<%=oogclass%>')" />
                                    <label class="tgl_round" for="<%=oogid%>"> </label>
                                    <input type="hidden"  id="<%=tempoogid%>" value="<%=oog%>"/>
                                </div>
                                <%}%>
                            </div>

                            <input type="hidden" name="<%=socname%>" id="soc<%=sicontainerid%>" value="<%=soc%>"/>
                            <input type="hidden" name ="<%=eqpTypeName%>" id="<%=eqpTypeName%>" value="<%=eqpType%>" readonly/>

                            <div class="col-sm-3 form-group">
                                <label class="carrierseallabel"> <bean:message key="shipping.carrierseal.display" bundle="<%=clientcode%>"/> </label>
                                <input type="text" name ="<%=carrierSealName%>" id="<%=csId%>"  maxlength="17" value="<%=carrierSeal%>"  class="form-control light_border checkcarrierseal" />
                            </div>

                            <div class="col-sm-3 form-group">
                                <label class="shipperseallabelclass"> <bean:message key="shipping.shipperseal.display" bundle="<%=clientcode%>"/>  </label>
                                <input type="text" name="<%=shipperSealName%>" Id="<%=ssId%>" value="<%=shipperSeal%>" maxlength="<%=shipperseallength%>" class="form-control light_border shippersealinputclass" rows="2" style="resize:none"/>
                            </div>

                            <div class="col-sm-3 form-group">

                                <label> <bean:message key="shipping.tareweight.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span> <label class="col-sm-2 p-l-15 d-none">:</label><span class="tare2_wg d-none" id="twt<%=sicontainerid%>" ><%=tareweight%>   </span> </label>
                                <input type="text" id="tareweight<%=sicontainerid%>" name="<%=tareweightname%>" value="<%=tareweight%>" class='form-control light_border tareweightinputclass  number validatepoint required ' onchange="$('#twt<%=sicontainerid%>').text(this.value)" <%=("Y".equalsIgnoreCase(soc) ? "" : "readonly='readonly'")%>/>
                                <label class='d-none'> <bean:message key="ebooking.grossweight.display" bundle="<%=clientcode%>"/><label class="col-sm-1">:</label><span class="tare2_wg" id="gwt<%=sicontainerid%>" ><%=grossweight%> </span>  </label>

                            </div>
                            <%if ("R".equalsIgnoreCase(strEqpType) && "Y".equalsIgnoreCase(EnableReeferDetails)) {%>
                            <div class="col-sm-3 form-group">
                                <label class="col-sm-12" ><bean:message key='ebooking.NOR.display' bundle="<%=clientcode%>"/> : </label>
                                <select  name ="<%=norname%>" id="<%=norname%>" value="<%=nor%>" onchange="nor(this.value, '<%=sicontainerid%>')" class="form-control light_border si_reefer_option" <%=cargoSIEdit%> >
                                    <option value="Y" <%=("Y".equalsIgnoreCase(nor)) ? "selected" : ""%>>Yes</option>                                   
                                    <option value="N" <%=("N".equalsIgnoreCase(nor)) ? "selected" : ""%>>No</option>
                                </select>
                            </div>
                            <div class="col-sm-3 form-group">
                                <label class="col-sm-12" > <bean:message key='ebooking.preset.temp' bundle="<%=clientcode%>"/> : </label>
                                <input type="text" name ="<%=settempname%>" id="<%=settempname%>" value="<%=NumberFormat.numberFormat(checkNull(settemp))%>"  class="form-control light_border si_reefer_option" <%=cargoSIEdit%>/>
                            </div>
                            <div class="col-sm-3 form-group">
                                <label class="col-sm-12" ><bean:message key='ebooking.temp.unit' bundle="<%=clientcode%>"/> : </label>
                                <select  name ="<%=tempunitname%>" id="<%=tempunitname%>" value="<%=tempunit%>"  class="form-control light_border si_reefer_option" <%=cargoSIEdit%>>
                                    <option value="C" <%=("C".equalsIgnoreCase(tempunit)) ? "selected" : ""%>>Celsius</option>
                                    <%if (!"GWF".equalsIgnoreCase(carrierCode)) {%>
                                    <option value="F" <%=("F".equalsIgnoreCase(tempunit)) ? "selected" : ""%>>Fahrenheit</option>
                                    <%}%>
                                </select>
                            </div>
                            <div class="col-sm-3 form-group">
                                <label class="col-sm-12" > <bean:message key='ebooking.vent' bundle="<%=clientcode%>"/><%= Ventilationlabel%> : </label>
                                <input type="text" name ="<%=ventilationId%>" id="<%=ventilationId%>" value="<%=checkNull(setVentilation)%>"  class="form-control light_border si_reefer_option" <%=cargoSIEdit%>/>
                            </div>
                            <%} else {%>
                            <input type="hidden" id="<%=norname%>" name="<%=norname%>" value="<%=nor%>"/>
                            <input type="hidden" id="<%=settempname%>" name="<%=settempname%>" value="<%=checkNull(settemp)%>"/>
                            <input type="hidden" id="<%=tempunitname%>" name="<%=tempunitname%>" value="<%=checkNull(tempunit)%>"/>
                            <input type="hidden" id="<%=ventilationId%>" name="<%=ventilationId%>" value="<%=checkNull(setVentilation)%>"/>
                            <%}%>
                            <%}
                                if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                            <label class="lbl_pkcg">   <bean:message key="adminscreen.description" bundle="<%=clientcode%>"/> </label>
                            <%} else {%>
                            <label class="lbl_pkcg">   <bean:message key="si.cargo.packdesc" bundle="<%=clientcode%>"/> </label>
                            <%}
                                if (cntList != null && cntList.size() > 0) {
                                    cntListSize = 0;
                                    cntListSize = cntList.size();
                                    for (int j = 0; j < cntListSize; j++) {
                                        SIContainerDetailVO siCntDetVo = (SIContainerDetailVO) cntList.get(j);
                                        comdId = "commodity" + sicontainerid + cntDtIncr;
                                        hiddencomid = "hiddencomid" + sicontainerid + cntDtIncr;
                                        newcomdtyid = "newcmdty" + sicontainerid + cntDtIncr;
                                        newpackid = "newpackid" + sicontainerid + cntDtIncr;

                                        newwtid = "newwtid" + sicontainerid + cntDtIncr;
                                        newntid = "newntid" + sicontainerid + cntDtIncr;
                                         buyerCodeid= "bycode"+ sicontainerid + cntDtIncr;
                                         buyerNameid= "byname"+ sicontainerid + cntDtIncr;
                                         sellerCodeid=  "slcode"+ sicontainerid + cntDtIncr;
                                         sellerNameid=  "slname"+ sicontainerid + cntDtIncr;
	                                 sellerSelectid= "slselect"+ sicontainerid + cntDtIncr;
	                                 buyerSelectid=  "byselect"+ sicontainerid + cntDtIncr;
                                         cmdtyhblnoid= "cmdty_hblno"+ sicontainerid + cntDtIncr;
//
                                         buyerCode= sicontainerid + "#bycode#";
                                         buyerName= sicontainerid + "#byname#";
                                         sellerCode= sicontainerid + "#slcode#";
                                         sellerName= sicontainerid + "#slname#";
	                                  sellerSelect= sicontainerid + "#slselect#";
	                                  buyerSelect= sicontainerid + "#byselect#";
                                       cmdtyhblno= sicontainerid + "#cmdty_hblno#";
                                        newvolid = "newvolid" + sicontainerid + cntDtIncr;
                                        newqtyid = "newqtyid" + sicontainerid + cntDtIncr;
                                        cmdEvt = "dynamicCommodity('" + comdId + "');";
                                        qtyId = "qty" + sicontainerid + cntDtIncr;
                                        hscodeid = "hscode" + sicontainerid + cntDtIncr;
                                        hiddenhsid = "hiddenhsid" + sicontainerid + cntDtIncr;
                                        wtId = "weight" + sicontainerid + cntDtIncr;
                                        ntId = "netweight" + sicontainerid + cntDtIncr;
                                        cidId = "cid" + sicontainerid + cntDtIncr;
                                        dtCmdName = sicontainerid + "#commodity#";
                                        dtCmdId = "commodity" + sicontainerid + cntDtIncr;
                                        dtCidName = sicontainerid + "#cid#";
                                        dtWtName = sicontainerid + "#weight#";
                                        ntWtName = sicontainerid + "#netweight#";
                                        dtWtId = "weight" + sicontainerid + cntDtIncr;
                                        ntWtId = "netweight" + sicontainerid + cntDtIncr;
                                        dtUnitName = sicontainerid + "#unit#";
                                        dtUnitId = "unit" + sicontainerid + cntDtIncr;
                                        dtQtyName = sicontainerid + "#qty#";
                                        dtQtyId = "qty" + sicontainerid + cntDtIncr;
                                        insurance = "insurance" + sicontainerid + cntDtIncr;
                                        System.out.println("[ECOM-WAR] SIBLCommonCargo =====comlist.size()================>" + comlist.size() + ""
                                                + " siCntDetVo.getStrCommodity()>>>" + siCntDetVo.getStrCommodity() + "  fakComdtyCode>>>" + fakComdtyCode
                                                + " siCntDetVo.getHsCode()>>" + siCntDetVo.getHsCode());
                                        if (comlist != null && comlist.size() > 0 && checkNull(siCntDetVo.getStrCommodity()).length() > 0) {
                                            dtCmd = checkNull(siCntDetVo.getCommodityName()).length() > 0 ? (checkNull(siCntDetVo.getCommodityName()) + " ~~ " + checkNull(siCntDetVo.getStrCommodity())).toUpperCase() : "";
                                            dtCmd1 = dtCmd;
                                            System.out.println("[ECOM-WAR] SIBLCommonCargo dtCmd>>>>" + dtCmd);
                                            dtCmd = dtCmd.replaceAll("\"", "&quot;");
                                            dtCmd = dtCmd.replaceAll("\\\\", "");
                                            dtCmd = dtCmd.replaceAll("\\r|\\n", "");

                                            System.out.println("[ECOM-WAR] SIBLCommonCargo dtCmd>>1111>" + dtCmd);
                                            if ("1".equalsIgnoreCase(siCntDetVo.getAppVersion())) {
                                                hscodevalue = checkNull(siCntDetVo.getHsCode()).length() > 0 ? checkNull(siCntDetVo.getHsCode()) : "";
                                            } else if (checkNull(siCntDetVo.getStrCommodity()).equalsIgnoreCase(fakComdtyCode)) {
                                                System.out.println("[ECOM-WAR] SIBLCommonCargo entafjlasd ");
                                                hscodevalue = checkNull(siCntDetVo.getHsCode()).length() > 0 ? checkNull(siCntDetVo.getHsCode()) : "";

                                            } else {
                                                hscodevalue = checkNull(siCntDetVo.getHsCode()).length() > 0 ? (checkNull(siCntDetVo.getHsCodeDesc()) + " ~~ " + checkNull(siCntDetVo.getHsCode())).toUpperCase() : "";
                                            }
                                            /* if (is_SI_restriction) {
                                                hscodevalue = dtCmd1;
                                            } */
                                            hscodevalue1 = hscodevalue;
                                            hscodevalue1 = hscodevalue1.replaceAll("\"", "&quot;");
                                            hscodevalue1 = hscodevalue1.replaceAll("\\\\", "");
                                            hscodevalue1 = hscodevalue1.replaceAll("\\r|\\n", "");
                                        } else {
                                            dtCmd = checkNull(siCntDetVo.getStrCommodity());
                                            dtCmd1 = dtCmd;
                                            hscodevalue = checkNull(siCntDetVo.getHsCode());
                                            hscodevalue1 = hscodevalue;
                                            hscodevalue1 = hscodevalue1.replaceAll("\"", "&quot;");
                                            hscodevalue1 = hscodevalue1.replaceAll("\\\\", "");
                                            hscodevalue1 = hscodevalue1.replaceAll("\\r|\\n", "");
                                        }
                                        System.out.println("[ECOM-WAR] SIBLCommonCargo >>jscode>>" + hscodevalue+"  hscodevalue1>>" + hscodevalue1);
                                        dtWt = siCntDetVo.getStrWgt();
                                        System.out.println("[ECOM-WAR] SIBLCommonCargo dtwt-----" + dtWt);
                                        ntWt = siCntDetVo.getNettwt();
                                        dtUnit = siCntDetVo.getStrUnit();
                                        byc= checkNull(siCntDetVo.getByscode());
                                        byn= checkNull(siCntDetVo.getBysname());
                                        slc= checkNull(siCntDetVo.getSlscode());
                                        sln= checkNull(siCntDetVo.getSlsname());
                                        hbln= checkNull(siCntDetVo.getCmdty_hblno());
                                        dtQty = siCntDetVo.getStrQty();
                                        dtCid = siCntDetVo.getStrCid();
                                        System.out.println(sln+"//"+hbln+"//"+byn+"//"+byc);
                                        pkgId = "package" + sicontainerid + cntDtIncr;
                                        hiddenpackid = "hidpackage" + sicontainerid + cntDtIncr;
                                        pkgEvt = "dynamicPackage('" + pkgId + "');";
                                        volId = "volume" + sicontainerid + cntDtIncr;
                                        vutId = "vunit" + sicontainerid + cntDtIncr;
                                        mrkId = "mark" + sicontainerid + cntDtIncr;
                                        desId = "descp" + sicontainerid + cntDtIncr;

                                        dtPkgName = sicontainerid + "#package#";
                                        dtPkgId = "package" + sicontainerid + cntDtIncr;
                                        dtVolName = sicontainerid + "#volume#";
                                        hidhscodename = sicontainerid + "#hscode#";
                                        hidhscodeid = "hidhscodeval" + sicontainerid + cntDtIncr;
                                        dtVolId = "volume" + sicontainerid + cntDtIncr;
                                        dtVutName = sicontainerid + "#vunit#";
                                        dtVutId = "vunit" + sicontainerid + cntDtIncr;
                                        dtMrkName = sicontainerid + "#mark#";
                                        dtMrkId = "mark" + sicontainerid + cntDtIncr;
                                        dtDesName = sicontainerid + "#descp#";
                                        dtDesId = "descp" + sicontainerid + cntDtIncr;

                                        imocontactname = sicontainerid + "#imocontact#";
                                        imocontact = siCntDetVo.getImocontact();
                                        imophonename = sicontainerid + "#imophone#";
                                        imophone = siCntDetVo.getImophone();
                                        itemforwardname = sicontainerid + "#itemforward#";
                                        itemforward = checkNull(siCntDetVo.getItemforward());
                                        itemtopname = sicontainerid + "#itemtop#";
                                        itemtop = checkNull(siCntDetVo.getItemtop());
                                        itemrightname = sicontainerid + "#itemright#";
                                        itemright = checkNull(siCntDetVo.getItemright());
                                        itemaftname = sicontainerid + "#itemaft#";
                                        itemaft = checkNull(siCntDetVo.getItemaft());
                                        tempitemforwardname = sicontainerid + "#tempitemforward#";
                                        tempitemtopname = sicontainerid + "#tempitemtop#";
                                        tempitemrightname = sicontainerid + "#tempitemright#";
                                        tempitemaftname = sicontainerid + "#tempitemaft#";
                                        tempitemleftname = sicontainerid + "#tempitemleft#";
                                        tempitemlengthUnitname = sicontainerid + "#tempitemlengthUnit#";
                                        hazardousname = sicontainerid + "#hazardous#";
                                        hazardousid = sicontainerid + "#hazardous#" + cntDtIncr;
                                        hazardous = siCntDetVo.getHazardous();
                                        itemlengthUnitname = sicontainerid + "#itemlengthUnit#";
                                        itemlengthUnit = ((siCntDetVo.getItemlengthUnit() == null || "null".equalsIgnoreCase(siCntDetVo.getItemlengthUnit())) ? "M" : siCntDetVo.getItemlengthUnit());
                                        itemleft = checkNull(siCntDetVo.getItemleft());
                                        itemleftname = sicontainerid + "#itemleft#";
                                        hssuffixcode = sicontainerid + "hssuffixcode";
                                        //
                                        //
                                        strIsSelect = "YES";

                                        if (packagemap != null && packagemap.size() > 0 && checkNull(siCntDetVo.getStrPkg()).length() > 0) {
                                            dtPkg = checkNull((String) packagemap.get(checkNull(siCntDetVo.getStrPkg()))).length() > 0 ? (checkNull((String) packagemap.get(checkNull(siCntDetVo.getStrPkg()))) + " ~~ " + checkNull(siCntDetVo.getStrPkg())).toUpperCase() : "";
                                        } else {
                                            dtPkg = checkNull(siCntDetVo.getStrPkg());
                                        }
                                        dtVol = siCntDetVo.getStrVol();
                                        dtVut = siCntDetVo.getStrVut();
                                        dtMrk = siCntDetVo.getStrMrk();
                                        dtDes = siCntDetVo.getStrDes();
                                        dtDes = checkNull(dtDes).replace("\"", "''");
                                        if ("YES".equalsIgnoreCase(strIsSelect)) {
                                            addreq = addreq + " required";
                                            qtyclass = qtyclass + " maxone";
                                            if (!"M".equalsIgnoreCase(siMasterVO.getEqpStatus())) {
                                                weigthclass = weigthclass + " validatezeropoint";
                                                weigthclass = weigthclass + " validatepoint ";
                                            }
                                            if ("M".equalsIgnoreCase(siMasterVO.getEqpStatus())) {
                                                weigthclass = weigthclass + " valuezero ";
                                                weigthclass = weigthclass + " validatezeropoint ";
                                            }
                                            volumeclass = volumeclass + " validatepoint";
                                            containerclass = addreq + " required";
                                        }
                                        if ("2".equalsIgnoreCase(siAppVersion)) {
                                            hscodecls += " required";
                                        }
                                        if ("Y".equalsIgnoreCase(hslencheck)) {
                                            hscodecls += " hscodelengthcheck";
                                        }
                                        incclass = "incclass" + aa;
                                        incclassid = "incclass" + aa + ccc;
                                        if (dtVol == null || "0".equalsIgnoreCase(dtVol) || "0.0".equalsIgnoreCase(dtVol) || "".equalsIgnoreCase(dtVol) || Float.parseFloat(dtVol) <= 0) {
                                            dtVol = "";
                                        }
                                        if (ntWt == null || "0".equalsIgnoreCase(ntWt) || "0.0".equalsIgnoreCase(ntWt) || "".equalsIgnoreCase(ntWt) || Float.parseFloat(ntWt) <= 0) {
                                            ntWt = "";
                                        }

                                        System.out.println("[ECOM-WAR] SIBLCommonCargo getEqpStatus - - " + siMasterVO.getEqpStatus());
                                        if ((!"M".equalsIgnoreCase(siMasterVO.getEqpStatus())) && (dtWt == null || "0".equalsIgnoreCase(dtWt) || "0.0".equalsIgnoreCase(dtWt) || "".equalsIgnoreCase(dtWt) || Float.parseFloat(dtWt) <= 0)) {
                                            dtWt = "";
                                            System.out.println("[ECOM-WAR] SIBLCommonCargo getEqpStatus - " + siMasterVO.getEqpStatus());
                                        }
                                        if ("Y".equalsIgnoreCase(Enable_cargodesc_mandatory)) {
                                            cargodescclass = " required";
                                        }
                                        isinsurance = Validation.checkNull(siCntDetVo.getIsinsuranceavailable());
                                        insuredemail = Validation.checkNull(siCntDetVo.getInsuredemail());
                                        quoteno = Validation.checkNull(siCntDetVo.getQuoteno());
                                        System.out.println("[ECOM-WAR] SIBLCommonCargo isinsurance==> " + isinsurance + " quoteno=> " + quoteno);
                                        suminsured = "";
                                        if (quoteno.length() > 0 && quoteno.contains("~~")) {
                                            suminsured = Validation.checkNull(NumberFormat.decimalFormat(Validation.checkNull(quoteno).split("~~")[2]));
                                            In_cust_type = Validation.checkNull(quoteno).split("~~")[1];
                                            quoteno = Validation.checkNull(quoteno).split("~~")[0];
                                        }
                            %>
                            <div class="si_cargo_pkg_des w-100 form-group row">
                                <div class="col-sm-10">		

                                    <div class="row box_height_gray <%=incclass%>" id="<%=incclassid%>">

                                        <div class="col-sm-4 form-group">
                                            <label>
                                                <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                                                <bean:message key="shipping.commoditygrp.display" bundle="<%=clientcode%>"/>
                                                <%} else {%>
                                                <bean:message key="shipping.commodity.display" bundle="<%=clientcode%>"/>
                                                <%}%>
                                                <%if ("Y".equalsIgnoreCase(CommodityName_as_HS_Chapter)) {%><bean:message key="ecom.si.hs.Chapter" bundle="<%=clientcode%>"/><%}%> <span class="mandatory"> * </span> </label>
                                            <input type="text"  name="<%=comdId%>"  id="<%=comdId%>"  class="form-control light_border commodity_new cp_commoditygroup" onchange="setnewvaluesComdty('<%=newcomdtyid%>', '<%=comdId%>');" readonly="true"/>

                                            <input type="hidden" name="<%=scriptVef%>" id="<%=scriptVef%>" value="<%=cntDtIncr%>"/>
                                            <input type="hidden" name="<%=dtCidName%>" id="<%=cidId%>" value="<%=dtCid%>"/>
                                            <input type="hidden" name="<%=newcomdtyname%>" id="<%=newcomdtyid%>" value="<%=dtCmd%>" class="checkcommodityvalue"/>
                                            <input type="hidden" id="<%=hiddencomid%>" name="<%=hiddencomid%>">

                                            <input type="hidden" id="<%=cargononame%>" name="<%=cargononame%>" value="<%=cargono%>"/>
                                            <input type="hidden" id="<%=cargoweightname%>" name="<%=cargoweightname%>" value="<%=cargoweight%>"/>
                                            <input type="hidden" id="tareweighthidunit<%=sicontainerid%>" value="<%=dtUnit%>"/>
                                            <input type="hidden" id="<%=netweightname%>" name="<%=netweightname%>" value="<%=netweight%>"/>
                                            <input type="hidden" id="<%=tempmaxname%>" name="<%=tempmaxname%>" value="<%=tempmax%>"/>
                                            <input type="hidden" id="grossweight<%=sicontainerid%>" name="<%=grossweightname%>" value="<%=grossweight%>"/>
                                            <input type="hidden" id="<%=grossweightname%>" name="<%=grossweightname%>" value="<%=grossweight%>"/>
                                            <input type="hidden" id="<%=tempminname%>" name="<%=tempminname%>" value="<%=tempmin%>"/>                                      
                                            <input type="hidden" id="<%=oogname%>" name="<%=oogname%>" value="<%=oog%>"/>                                            
                                            <input type="hidden" id="<%=refername%>" name="<%=refername%>" value="<%=refer%>"/>                                            
                                            <input type="hidden" id="<%=dgflagName%>" name="<%=dgflagName%>" value="<%=dgFlagVal%>"/>
                                            <input type="hidden" id="<%=eqpStatusId%>" name="<%=eqpStatusId%>" value="<%=siMasterVO.getEqpStatus()%>"/>

                                            <input type="hidden" id="<%=humidityId%>" name="<%=humidityId%>" value="<%=setHumidity%>"/>
                                            <input type="hidden" id="<%=humidityFlagId%>" name="<%=humidityFlagId%>" value="<%=setHumidityFlag%>"/>
                                            <input type="hidden" id="<%=drainsId%>" name="<%=drainsId%>" value="<%=setDrains%>"/>
                                            <input type="hidden" id="<%=co2Id%>" name="<%=co2Id%>" value="<%=setCo2%>"/>
                                            <input type="hidden" id="<%=o2Id%>" name="<%=o2Id%>" value="<%=setO2%>"/>                                            
                                            <input type="hidden" id="<%=imocontactname%>" name="<%=imocontactname%>" value="<%=imocontact%>"/>
                                            <input type="hidden" id="<%=imophonename%>" name="<%=imophonename%>" value="<%=imophone%>"/>
                                            <%if (!is_SI_OOG) {%>
                                            <input type="hidden" id="<%=itemforwardname%>" name="<%=itemforwardname%>" value="<%=itemforward%>"/>
                                            <input type="hidden" id="<%=itemtopname%>" name="<%=itemtopname%>" value="<%=itemtop%>"/>
                                            <input type="hidden" id="<%=itemrightname%>" name="<%=itemrightname%>" value="<%=itemright%>"/>
                                            <input type="hidden" id="<%=itemaftname%>" name="<%=itemaftname%>" value="<%=itemaft%>"/>
                                            <input type="hidden" id="<%=itemlengthUnitname%>" name="<%=itemlengthUnitname%>" value="<%=itemlengthUnit%>"/>
                                            <input type="hidden" id="<%=itemleftname%>" name="<%=itemleftname%>" value="<%=itemleft%>"/>
                                            <%}%>
                                            <input type="hidden" id="<%=hazardousid%>" name="<%=hazardousname%>" class="cp_hazardous" value="<%=hazardous%>"/>
                                            <input type="hidden" id="<%=hssuffixcode%>" name="<%=hssuffixcode%>" value="<%=Validation.checkNull(siCntDetVo.getHssuffixcode())%>"/>
                                        </div>

                                        <div class="col-sm-4 form-group">
                                            <label>
                                                <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                                                <bean:message key="shipping.commodity.display" bundle="<%=clientcode%>"/>
                                                <%} else if ("Y".equalsIgnoreCase(hsnamecode)) {%>
                                                <bean:message key='ebooking.trust.hsnamecode' bundle="<%=clientcode%>"/>
                                                <%} else {%>
                                                <bean:message key="shipping.hscode.display" bundle="<%=clientcode%>"/>
                                                <%}%>
                                                <%if (("2").equalsIgnoreCase(siAppVersion)) {%><span class="mandatory"> * </span><%}%> </label>
                                            <input type="text" name="<%=hscodeid%>"  Id="<%=hscodeid%>"  class="form-control light_border <%=hscodecls%> v1hscommodity cp_hscode" onblur="setnewvaluesComdty('<%=hidhscodeid%>', '<%=hscodeid%>');" onchange="clearCustHsCode('<%=hssuffixcode%>');<%if ("Y".equalsIgnoreCase(Enable_cargodesc_mandatory)) {%> hscodevalidation(this);<%}%>"/>
                                            <input type="hidden" id="<%=hiddenhsid%>" name="<%=hiddenhsid%>">
                                            <input type="hidden" name="<%=hidhscodename%>" id="<%=hidhscodeid%>" value="<%=hscodevalue1%>">

                                        </div>

                                        <div class="col-sm-4 form-group">
                                            <label> 
                                                <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                                                <bean:message key="shipping.type.display" bundle="<%=clientcode%>"/>
                                                <%} else {%>
                                                <bean:message key="shipping.packagetype.display" bundle="<%=clientcode%>"/>
                                                <%}%>
                                                <span class="mandatory"> * </span> </label>
                                            <input type="text"  name="<%=pkgId%>"  Id="<%=pkgId%>"  class="form-control <%=addreq%> light_border package_new autocompletetmsgfortype cp_packagetype" onblur="setnewvalues('<%=newpackid%>', '<%=pkgId%>');"/>
                                            <input type="hidden" name="<%=scriptVef%>" id="<%=scriptVef%>" value="<%=cntDtIncr%>"/>
                                            <input type="hidden" name="<%=newpackname%>" id="<%=newpackid%>" value="<%=dtPkg%>"/>
                                            <input type="hidden" id="<%=hiddenpackid%>" name="<%=hiddenpackid%>">
                                        </div>

                                        <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                                        <div class="col-sm-4 form-group">
                                            <label><bean:message key="shipping.unit.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span> </label> 
                                            <input type="text" name="<%=qtyId%>" maxlength="8"  Id="<%=qtyId%>" value="<%=dtQty%>" class="form-control <%=addreq%> number light_border <%=qtyclass%> cp_noofpackage" onblur="setnewvalues('<%=newqtyid%>', '<%=qtyId%>');" onkeypress="keyCheckINT(event)"/>
                                            <input type="hidden" name="<%=newqtyname%>" id="<%=newqtyid%>" value="<%=dtQty%>"/>
                                        </div>
                                        <%} else {%>
                                        <div class="col-sm-4 form-group">
                                            <label><bean:message key="shipping.quantity.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span> </label> 
                                            <input type="text" name="<%=qtyId%>" maxlength="8"  Id="<%=qtyId%>" value="<%=dtQty%>" class="form-control <%=addreq%> number light_border <%=qtyclass%> cp_noofpackage" onblur="setnewvalues('<%=newqtyid%>', '<%=qtyId%>');" onkeypress="keyCheckINT(event)"/>
                                            <input type="hidden" name="<%=newqtyname%>" id="<%=newqtyid%>" value="<%=dtQty%>"/>
                                        </div>
                                        <%}%>

                                        <div class="col-xs-12 col-sm-2 ecom_padding_right">	
                                            <label>	<bean:message key="shipping.cargoweight.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span></label>

                                            <input type="text" maxlength="9" name="<%=wtId%>"  Id="<%=wtId%>" value="<%=dtWt%>" class="form-control <%=addreq%> number light_border <%=weigthclass%> cp_cargoweight cargweightmsg" onblur="setnewvalues('<%=newwtid%>', '<%=wtId%>');
                                                    calculateGrossWeight('<%=sicontainerid%>', '<%=sicontainerid + cntDtIncr%>');
                                                    PayLoadValidation('containerno<%=sicontainerid%>', 'eqptypeval<%=sicontainerid%>');" 
                                                   onkeyup="checkvalue(event, this.value, '<%=wtId%>');"/>
                                            <input type="hidden" name="<%=newwtname%>" id="<%=newwtid%>" value="<%=dtWt%>"/>

                                        </div>

                                        <div class="col-xs-12 col-sm-2 ecom_padding_left">
                                            <label> </label>
                                            <input type="hidden" id="<%=dtUnitId%>_previous" name="<%=dtUnitId%>_previous" value="<%=dtUnit%>" />
                                            <select name="<%=dtUnitName%>" Id="<%=dtUnitId%>" class="form-control light_border  no-left-border <%=unitclass%> SI_Process_Restriction cp_cargoweightunit" onchange="/*calcSITareWeight(this.value, 'tareweight<%=sicontainerid%>', 'twt<%=sicontainerid%>', '<%=wtId%>', 'S');*/convertWeight('<%=sicontainerid%>', '<%=sicontainerid + cntDtIncr%>')">
                                                <option value="K" <%=(dtUnit.equalsIgnoreCase("K")) ? "selected" : ""%> >Kilos</option>
                                                <%if ("N".equalsIgnoreCase(weight_unit_kilos_only)) {%>
<!--                                                            <option value="T" <%=(dtUnit.equalsIgnoreCase("T")) ? "selected" : ""%> >Metric Tones</option>-->
                                                <option value="P" <%=(dtUnit.equalsIgnoreCase("P")) ? "selected" : ""%> >Pounds</option>
                                                <%}%>   
                                            </select>

                                        </div>
                                        <!--netweight-->
                                        <div class="col-sm-4 form-group">
                                            <label>	<bean:message key="ecom.cargo.netweight" bundle="<%=clientcode%>"/> </label>

                                            <input type="text" maxlength="9" name="<%=ntId%>"  Id="<%=ntId%>" value="<%=ntWt%>" class="form-control  number light_border <%=netweightclass%> SI_Process_Restriction" onblur="setnewvalues('<%=newntid%>', '<%=ntId%>');" 
                                                   onkeyup="checkvalue(event, this.value, '<%=ntId%>');"/>
                                            <input type="hidden" name="<%=newntname%>" id="<%=newntid%>" value="<%=ntWt%>"/>

                                        </div>
<%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
           <div class="col-sm-4 form-group">
                     <label><bean:message key='ebooking.cmdtyhblno.display' bundle="<%=clientcode%>"/></label>
                    <input type="text" class="form-control light_border si_cargo_cmd_hblno" id="<%=cmdtyhblnoid%>" name="<%=cmdtyhblno%>"  value="<%=hbln%>" maxlength="20">
          </div>           
		   <div class="col-sm-4 form-group">	
                   <label><bean:message key='ebooking.sellersName.display' bundle="<%=clientcode%>"/></label>
                 <select  class="form-control input-sm setdata  light_border" id="<%=sellerSelectid%>" name="sellerNameList" onchange="setCustNameCode(this,'<%=sellerCodeid%>','<%=sellerNameid%>')">
                        <option value="">Select Customer</option>
                    </select>
                     <input type="hidden" id="<%=sellerCodeid%>" name="<%=sellerCode%>" value="<%=slc%>" />
                     <input type="hidden" id="<%=sellerNameid%>" name="<%=sellerName%>" value="<%=sln%>" />
                </div>
				
                  <div class="col-sm-4 form-group">	
                   <label><bean:message key='ebooking.buyersName.display' bundle="<%=clientcode%>"/></label>
                   <select  class="form-control input-sm setdata light_border" id="<%=buyerSelectid%>" name="buyerNameList"  onchange="setCustNameCode(this,'<%=buyerCodeid%>','<%=buyerNameid%>')" )>
                        <option value="">Select Customer</option>
                    </select>
                    <input type="hidden" id="<%=buyerCodeid%>" name="<%=buyerCode%>" value="<%=byc%>" />
                    <input type="hidden" id="<%=buyerNameid%>" name="<%=buyerName%>" value="<%=byn%>" />
                </div>
				
				
              
           <%}%>
                                        <div class="col-xs-12 col-sm-2 ecom_padding_right">

                                            <label>	<bean:message key="shipping.volume.display" bundle="<%=clientcode%>"/></label>

                                            <input type="text" maxlength="9" name="<%=volId%>"  Id="<%=volId%>" value="<%=dtVol%>" class="form-control number light_border <%=volumeclass%> SI_Process_Restriction cp_volume" onblur="setnewvalues('<%=newvolid%>', '<%=volId%>');" onkeypress="return ValDigit(event, this);" onkeyup="checkvalue(event, this.value, '<%=volId%>');"/>
                                            <input type="hidden" name="<%=newvolname%>" id="<%=newvolid%>" value="<%=dtVol%>"/>

                                        </div>

                                        <div class="col-xs-12 col-sm-2 ecom_padding_left">

                                            <label> </label>
                                            <select name="<%=dtVutName%>" Id="<%=dtVutId%>" class="form-control light_border  no-left-border SI_Process_Restriction cp_volumeunit" >
                                                <option value="M" <%=(dtVut.equalsIgnoreCase("M")) ? "selected" : ""%>>Cubic Meter</option>
                                                <option value="F" <%=(dtVut.equalsIgnoreCase("F")) ? "selected" : ""%>>Cubic Feet</option>
                                            </select>

                                        </div>

                                        <div class="col-xs-12 col-sm-4">
                                            <label> <bean:message key="shipping.marks.display" bundle="<%=clientcode%>"/></label>
                                            <textarea name="<%=dtMrkName%>" Id="<%=mrkId%>" value="<%=dtMrk%>"  class="form-control <%=markdescclass%> cp_marksno" rows="3"><%=dtMrk%></textarea>
                                        </div>
                                        <%if ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(insuranceaccess)) {%>
                                        <input type="hidden" class="inquotecusttype" id="<%=insurance + "_custtype"%>" value="<%=In_cust_type%>">
                                        <input type="hidden" class="inquoteno" id="<%=insurance + "_quoteno"%>" value="<%=quoteno%>">
                                        <input type="hidden" class="incargoval" id="<%=insurance + "_cargoval"%>" value="<%=suminsured%>" >
                                        <input type="hidden" class="inavailable" id="<%=insurance + "_available"%>" value="<%=(Validation.checkNull(quoteno).length() > 0 ? "Y" : "N")%>" >
                                        <input type="hidden" class="insuredemail" id="<%=insurance + "_insuredemail"%>" value="<%=insuredemail%>" >
                                        <!--                                        <div class="col-xs-12 col-sm-3 text-center">
                                                                                    <a href="javascript:void(0)" onclick="loadinsurancepage($('#<=insurance + "_quoteno"%>').val(), $('#<=insurance + "_custtype"%>').val())"  class="btn btn-info btn-md addinsurance" style="margin-top: 18px;"><i class=""></i> <bea:message key="booking.btn.cargo.insurance" bundle="<=clientcode%>"/> </a>
                                                                                    <div class="text-center" style="margin-top: 10px;">
                                                                                        <label for="<=insurance%>" class="label_switch"> <bea:message key="booking.cargo.insurance" bundle="<=clientcode%>"/> </label>
                                                                                        <label class="switch">
                                                                                            <input type="checkbox" value="N" id="<=insurance%>" name="<=insurance%>" class="insuranceswitchbtn"  disabled="disabled" <=("Y".equalsIgnoreCase(isinsurance) ? "checked" : "")%>/>
                                                                                            <span class="slider round"></span>
                                                                                        </label>
                                                                                    </div>
                                                                                </div>-->
                                        <%}%>
                                        <%if ("Y".equalsIgnoreCase(commodityDescDisclaimer)) {%>

                                        <div class="col-xs-12 col-sm-12">
                                            <label> <bean:message key="shipping.description.display" bundle="<%=clientcode%>"/> <%if ("Y".equalsIgnoreCase(Enable_cargodesc_mandatory)) {%><span class="mandatory"> * </span> <%}%></label>

                                            <textarea name="<%=dtDesName%>" Id="<%=desId%>" value="<%=dtDes%>" class="form-control <%=cargodescclass%> cp_cargodesc" rows="10" onkeyup="acceptCommoOnlyEnglish(this)" <%if ("Y".equalsIgnoreCase(Enable_cargodesc_mandatory)) {%> onchange="cargodescvalidation(this)"<%}%>><%=dtDes%></textarea>
                                            <span class='text-info'> <bean:message key="ecom.cargo.commlangeng" bundle="<%=clientcode%>"/></span>
                                        </div>             
                                        <%} else {%>
                                        <div class="col-xs-12 col-sm-12">
                                            <label> <bean:message key="shipping.description.display" bundle="<%=clientcode%>"/><%if ("Y".equalsIgnoreCase(Enable_cargodesc_mandatory)) {%><span class="mandatory"> * </span> <%}%> </label>

                                            <textarea name="<%=dtDesName%>" Id="<%=desId%>" value="<%=dtDes%>" class="form-control <%=cargodescclass%> cp_cargodesc" rows="10" <%if ("Y".equalsIgnoreCase(Enable_cargodesc_mandatory)) {%> onchange="cargodescvalidation(this)"<%}%>><%=dtDes%></textarea>
                                            <span class='text-info'> <bean:message key='ecom.coomdesc.lang' bundle="<%=clientcode%>"/></span>
                                        </div>
                                        <%}%>                                          

                                        <div class="col-xs-12 col-sm-12  m-t-5 p-r-20 text-right">
                                            <a onclick="markDescView('<%=mrkId%>', '<%=desId%>');" class='linkeffect mark_desc_cls' href="javascript:void(0)" title="Marks No. and Commodity Description View" ><bean:message key="ecom.shippingins.marksanddescview" bundle="<%=clientcode%>"/></a>
                                        </div>

                                        <div class="col-xs-12 col-sm-12 <%=dimension%>" style="display: none;" id="oogblock<%= sicontainerid%>">
                                            <%if (is_SI_OOG) {%>
                                            <!--<label class="lbl_pkcg">   <beanmessage key="ebooking.oog.text.display" bundle="<clientcode>"/> </label>-->
                                            <hr/>
                                            <div class=" row">

                                                <div class="col-sm-4">
                                                    <label><bean:message key='ebooking.dimensionunit.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                                    <select class="form-control input-sm setdata <%=oogclass%> light_border "   id="<%=itemlengthUnitname + cntDtIncr%>" name="<%=itemlengthUnitname%>"  >
                                                        <option value="M" <%=("M".equalsIgnoreCase(itemlengthUnit)) ? "selected" : ""%>>Meters</option>
                                                        <option value="F" <%=("F".equalsIgnoreCase(itemlengthUnit)) ? "selected" : ""%>>Feet</option>
                                                        <option value="C" <%=("C".equalsIgnoreCase(itemlengthUnit)) ? "selected" : ""%>>Centimeters</option>
                                                    </select>
                                                    <input type="hidden" id="<%=tempitemlengthUnitname + cntDtIncr%>"  value="<%=itemlengthUnit%>"/>
                                                </div>

                                                <div class="col-sm-4">
                                                    <label><bean:message key='ebooking.dimensionleft.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                                    <input type="text"  maxlength="6" class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border " id="<%=itemleftname + cntDtIncr%>"   name="<%=itemleftname%>" value="<%=itemleft%>" />
                                                    <input type="hidden" id="<%=tempitemleftname + cntDtIncr%>" value="<%=itemleft%>"/>
                                                </div>

                                                <div class="col-sm-4">
                                                    <label><bean:message key='ebooking.dimensionright.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                                    <input type="text"  maxlength="6" class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border "  id="<%=itemrightname + cntDtIncr%>" name="<%=itemrightname%>" value="<%=itemright%>" />
                                                    <input type="hidden" id="<%=tempitemrightname + cntDtIncr%>"  value="<%=itemright%>"/>
                                                </div>
                                            </div>
                                            <div class="form-group row">

                                                <div class="col-sm-4">
                                                    <label><bean:message key='ebooking.dimensionforward.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                                    <input type="text"  maxlength="6" class="form-control number input-sm setdata validatezeropoint <%=oogclass%> light_border " id="<%=itemforwardname + cntDtIncr%>"   name="<%=itemforwardname%>" value="<%=itemforward%>" />
                                                    <input type="hidden" id="<%=tempitemforwardname + cntDtIncr%>"  value="<%=itemforward%>"/>
                                                </div>


                                                <div class="col-sm-4">
                                                    <label><bean:message key='ebooking.dimensionalt.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                                    <input type="text"  maxlength="6" class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border "  id="<%=itemaftname + cntDtIncr%>"   name="<%=itemaftname%>" value="<%=itemaft%>" />
                                                    <input type="hidden" id="<%=tempitemaftname + cntDtIncr%>"  value="<%=itemaft%>"/>
                                                </div>


                                                <div class="col-sm-4">
                                                    <label><bean:message key='ebooking.dimensiontop.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                                                    <input type="text"  maxlength="6" class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border " id="<%=itemtopname + cntDtIncr%>"   name="<%=itemtopname%>" value="<%=itemtop%>" />
                                                    <input type="hidden" id="<%=tempitemtopname + cntDtIncr%>"  value="<%=itemtop%>"/>
                                                </div>
                                            </div>
                                            <%}%>
                                        </div>
                                    </div>

                                </div>
                                <div class="col-sm-2 m-t-120"> 
                                    <%
                                        if (!is_SI_restriction) {
                                            if (cntDtIncr == 1) {
                                    %>

                                    <button type="button" onclick="addRow_New('sitable', '<%=sicontainerid%>', this, 'select<%=con%>', '<%=incclass%>', '<%=containerCount%>', '', '<%=hscodeconfig%>', '<%=oogid%>')" title="Add cargo details" class="btn btn_add_si si_cmdty_add_btn"> <i class="fa fa-plus"></i>   <bean:message key="ecom.bkg.but.add" bundle="<%=clientcode%>"/> </button>

                                    <%} else {
                                    %>
                                    <button type="button" onclick="deleteRow_New('sitable', this);" title="Delete Cargo Details"  class="btn btn_add_si si_cmdty_remove_btn"> <i class="fa fa-minus"></i>   <bean:message key="specialprivi.remove" bundle="<%=clientcode%>"/> </button>
                                    <%}
                                        }%>
                                    <% if ("Y".equalsIgnoreCase(enableapplyallcont)) {%>
                                    <button type="button" onclick="applyToAllContainers(this, '<%=sicontainerid + cntDtIncr%>')" title="Apply to all containers" class="btn apply_container_btn"> <i class="fa fa-copy"></i>   Apply to all containers</button>
                                    <%}%>
                                </div>
                            </div>


                            <script type="text/javascript">
                                $(document).ready(function () {
                                    $("#" + "<%=comdId%>").typeahead('val', "<%=dtCmd1%>");
                                    $("#" + "<%=pkgId%>").typeahead('val', "<%=dtPkg%>");
                                <%if (checkNull(siCntDetVo.getStrCommodity()).equalsIgnoreCase(fakComdtyCode)) {%>
                                    $("#" + "<%=hscodeid%>").val("<%=hscodevalue%>");
                                    $("#" + "<%=hscodeid%>").attr('maxlength', '150');
                                <%} else {%>
                                    $("#" + "<%=hscodeid%>").typeahead('val', "<%=hscodevalue%>");
                                <%}%>
                                <%if (is_SI_OOG) {%>
                                    oogEnableLoad('<%=sicontainerid%>', '<%=oogclass%>', '<%=oogid%>', '<%=oog%>', '<%=eqpType%>');
                                <%}%>
                                <%if ("R".equalsIgnoreCase(strEqpType) && "Y".equalsIgnoreCase(EnableReeferDetails) && "Y".equalsIgnoreCase(EditEnableReeferDetails)) {%>
                                    nor('<%=nor%>', '<%=sicontainerid%>');
                                <%}%>
                                });
//                                 setSellerNameSelectOption();
                                commodityload('<%=comdId%>', '<%=hiddencomid%>', '<%=hscodeid%>', '<%=hiddenhsid%>', '<%=checkNull(siCntDetVo.getStrCommodity())%>');
                                packageload('<%=pkgId%>', '<%=hiddenpackid%>');</script>
                                <%
                                            cntDtIncr = cntDtIncr + 1;
                                            tablerowcount++;
                                            bb++;
                                            ccc++;
                                        }

                                    }
                                %>

                        </div>
                    </div>

                    <script type="text/javascript">
                        setContNo('containersNo<%=sicontainerid%>', '<%=addreq%>', '<%=sicontainerid%>', '<%=qtyclass%>', '<%=weigthclass%>', '<%=volumeclass%>', document.getElementById('select<%=con%>'));
                        socstatuscheck('<%=con%>', '<%=sicontainerid%>');</script>
                        <%
                                    con++;
                                    aa++;
                                    containerCount++;

                                }
                            }

                            session.setAttribute(
                                    "SI_Import_Excel_Cargo_Details", importexcellist);
                        %>
                </div>
            </div>
        </div>
    </div>

</fieldset> 
