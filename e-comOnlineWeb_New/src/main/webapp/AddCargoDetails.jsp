<%@page contentType="text/html"  pageEncoding="UTF-8" import="java.util.ArrayList,java.util.List,net.ecom.formbeans.EbookingValidatorFormBeanNew,java.util.Hashtable,net.ecom.common.GeneralConf,net.ecom.common.Validation,net.ecom.common.NumberFormat"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
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

<%
    String cacheVersion = (String) getServletContext().getAttribute("CACHEVERSION");
    String ENABLE_INSURANCE = (String) getServletContext().getAttribute(GeneralConf.ENABLE_INSURANCE);
    String insuranceaccess = (String) session.getAttribute("SESSION_InsuranceAccess");
    cacheVersion = cacheVersion != null && cacheVersion.length() > 0 ? "?" + cacheVersion : "";
    String clientcode = (String) session.getAttribute("clientcode");
    String tableRowID = request.getParameter("rowid");
    int count_row = 0;
    String str_row = "";
    String type = checkNull(request.getParameter("type"));
    Hashtable comlist = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
    Hashtable packagemap = (Hashtable) getServletContext().getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
    Hashtable dgclassmap = (Hashtable) getServletContext().getAttribute("SESSION_DGCLASS");
    String CommodityName_as_HS_Chapter = (String) getServletContext().getAttribute(GeneralConf.CommodityName_as_HS_Chapter);
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String enableActCommoName = (String) getServletContext().getAttribute(GeneralConf.EnableActualCommoName);
    String eqpType = null, eqpSize = null, eqpQty = null, soc = null, hzd = null, commodity = null, packagename = null, hscode = null, insurance = null;
    String packageQty = null, eqpStatus = null, tareWt = null, cargoWt = null, netWt = null, grossWt = null, volume = null, netWtUnit = null, cargoWtUnit = null, actCommoName = null,buyersName = null,sellersName = null,cmdtyhblno=null,buyersCode=null,sellerCode=null,sellerSelect=null,buyerSelect=null;
    String grossWtUnit = null, volUnit = null, othersCmdty = null, marinePollut = null, unno = null, flashPoint = null, flashUnit = null, commodityothers = null;
    String shippingName = null, dgClass = null, presetTemp = null, minTemp = null, maxTemp = null, tempUnit = null, nor = null;
    String dimUnit = null, owLeft = null, owRight = null, oh = null, olAft = null, olForward = null, hzdDiv = null, refDiv = null, oogDiv = null, eqptypeval = null;
    String commodityval = null, hscommodityval = null, packageval = null, refclass = null, oogclass = null, hzdclass = null, actualcmdtyval = null;
    String hidden_oog = null, hidden_lcl = null, hidden_hzd = null, hidden_rfr = null, hidden_soc = null, oogflag = null;
    String commcheck = "", commodityclass = null, hscodeclass = null, comid = null, conid = null, othercommodity = null, otherhscode = null;
    String cargoweightcheck = null, netweightcheck = null, volumeweightcheck = null;
    String commodityport = null, packagenameport = null, dgclassname = null, dgclassval = null;
    String hiddencomid = null, hiddenpackid = null, hiddenhscodeid = null, hiddenhscode = null, hdcommoidtycode = null;
    String humidityflag = null, humidity = null, drains = null, co2 = null, o2 = null, ventilation = null, isinsurance = null, quoteno = null, In_cust_type = null, suminsured = "", insuredemail = "";
    ArrayList ebooking_container_grid = null;
    String hideMinMaxTemp = (String) getServletContext().getAttribute(GeneralConf.Hide_Min_MaxTemp);
    String Ventilationlabel = (String) getServletContext().getAttribute("Ventilation");
    String userFruitCust = (String) session.getAttribute("SESSION_USER_FURIT_CUSTOMER");
    String Enable_Drains_Humidity = (String) getServletContext().getAttribute(GeneralConf.Enable_Drains_Humidity);
    String weight_unit_kilos_only = (String) getServletContext().getAttribute(GeneralConf.WEIGHT_UNIT_KILOS_ONLY);
    String fakCommodity = (String) getServletContext().getAttribute(GeneralConf.FAKCOMMODITY);
    String isMultipleDGEnabled = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.Enable_Multiple_DG_Details));
    String ENABLE_OOG_DETAILS = (String) getServletContext().getAttribute(GeneralConf.ENABLE_OOG_DETAILS);
    String hsnamecode = (String) getServletContext().getAttribute(GeneralConf.Display_HS_NAME_CODE);
    String bookingvolumeMandatory = (String) getServletContext().getAttribute(GeneralConf.BOOKING_VOLUME_MANDATORY);
    String HSCodeMandatory = (String) getServletContext().getAttribute(GeneralConf.HSCodeMandatory);
    String TENS_Documentation = (String) getServletContext().getAttribute(GeneralConf.TENS_Documentation);
    String enable_Reefer_MixTemp_Req = (String) getServletContext().getAttribute(GeneralConf.Enable_Reefer_MixTemp_Req);
    String reefer_MinTemp_Limit = (String) getServletContext().getAttribute("Reefer_MinTemp_Limit");
    String reefer_MaxTemp_Limit = (String) getServletContext().getAttribute("Reefer_MaxTemp_Limit");
    System.out.println("[ECOM-WAR] AddCargoDetails reefer_MinTemp_Limit>>>>" + reefer_MinTemp_Limit+"Reefer_MaxTemp_Limit/"+ reefer_MaxTemp_Limit);
    String fakComdtyCode = "";
    String fakComdtyName = "";
    System.out.println("[ECOM-WAR] AddCargoDetails fakCommodity>>>>" + fakCommodity);
    fakCommodity = fakCommodity != null ? fakCommodity : "";
    if (fakCommodity != null && fakCommodity.length() > 0) {
        String fakCommodityArr[] = fakCommodity.split("~~");
        if (fakCommodityArr.length == 2) {
            fakComdtyCode = fakCommodityArr[0].trim();
            fakComdtyName = fakCommodityArr[1].trim();
        }
    }
    EbookingValidatorFormBeanNew containergrid = null;
    ebooking_container_grid = (ArrayList) session.getAttribute("ebooking_cargo_grid");
    if (ebooking_container_grid != null && ebooking_container_grid.size() > 0) {
        for (int i = 0; i < ebooking_container_grid.size(); i++) {
            containergrid = (EbookingValidatorFormBeanNew) ebooking_container_grid.get(i);
            eqptypeval = checkNull(containergrid.getEbook_eqp_type());
            tableRowID = String.valueOf(i + 1);
            refclass = "refclass" + tableRowID;
            oogclass = "oogclass" + tableRowID;
            hzdclass = "hzdclass" + tableRowID;
            cargoweightcheck = "cargoweightcheck" + tableRowID;
            netweightcheck = "netweightcheck" + tableRowID;
            volumeweightcheck = "volumeweightcheck" + tableRowID;
            eqpType = "eqpType" + tableRowID;
            eqpSize = "eqpSize" + tableRowID;
            eqpQty = "eqpQty" + tableRowID;
            soc = "soc" + tableRowID;
            hzd = "hzd" + tableRowID;
            commodityclass = "commodityclass" + tableRowID;
            hscodeclass = "hscodeclass" + tableRowID;
            commodity = "commodity" + tableRowID;
            hiddencomid = "hiddencomid" + tableRowID;
            hdcommoidtycode = "hdcommoidtycode" + tableRowID;
            hiddenhscodeid = "hiddenhscodeid" + tableRowID;
            hiddenpackid = "hiddenpackid" + tableRowID;
            commodityport = "commodity" + tableRowID + "_port";
            packagename = "packagename" + tableRowID;
            packagenameport = "packagename" + tableRowID + "_port";
            hscode = "hscode" + tableRowID;
            packageQty = "packageQty" + tableRowID;
            actCommoName = "actCommoName" + tableRowID;
            sellersName = "sellersName" + tableRowID;
            buyersName = "buyersName" + tableRowID;
            buyersCode = "buyersCode" + tableRowID;
            sellerSelect = "sellerSelect" + tableRowID;
            buyerSelect = "buyerSelect" + tableRowID;
            sellerCode = "sellerCode" + tableRowID;
            cmdtyhblno = "cmdtyhblno" + tableRowID;
            eqpStatus = "eqpStatus" + tableRowID;
            tareWt = "tareWt" + tableRowID;
            cargoWt = "cargoWt" + tableRowID;
            netWt = "netWt" + tableRowID;
            grossWt = "grossWt" + tableRowID;
            volume = "volume" + tableRowID;
            comid = "comid" + tableRowID;
            conid = "conid" + tableRowID;
            othercommodity = "othercommodity" + tableRowID;
            otherhscode = "otherhscode" + tableRowID;
            cargoWtUnit = "cargoWtUnit" + tableRowID;
            netWtUnit = "netWtUnit" + tableRowID;
            grossWtUnit = "grossWtUnit" + tableRowID;
            volUnit = "volUnit" + tableRowID;
            othersCmdty = "othersCmdty" + tableRowID;
            commodityothers = "commodityothers" + tableRowID;
//
            marinePollut = "marinePollut" + tableRowID;
            unno = "unno" + tableRowID;
            flashPoint = "flashPoint" + tableRowID;
            flashUnit = "flashUnit" + tableRowID;
            shippingName = "shippingName" + tableRowID;
            dgClass = "dgClass" + tableRowID;
            presetTemp = "presetTemp" + tableRowID;
            minTemp = "minTemp" + tableRowID;
            maxTemp = "maxTemp" + tableRowID;
            tempUnit = "tempUnit" + tableRowID;
            nor = "nor" + tableRowID;
            owLeft = "owLeft" + tableRowID;
            dimUnit = "dimUnit" + tableRowID;
            owRight = "owRight" + tableRowID;
            oh = "oh" + tableRowID;
            olAft = "olAft" + tableRowID;
            olForward = "olForward" + tableRowID;
            hzdDiv = "hzdDiv" + tableRowID;
            refDiv = "refDiv" + tableRowID;
            oogDiv = "oogDiv" + tableRowID;
            oogflag = "oogflag" + tableRowID;

            hidden_oog = "hidden_oog" + tableRowID;
            hidden_hzd = "hidden_hzd" + tableRowID;
            hidden_rfr = "hidden_rfr" + tableRowID;
            hidden_soc = "hidden_soc" + tableRowID;

            humidityflag = "humidityflag" + tableRowID;
            humidity = "humidity" + tableRowID;
            drains = "drains" + tableRowID;
            co2 = "co2" + tableRowID;
            o2 = "o2" + tableRowID;
            ventilation = "ventilation" + tableRowID;
            insurance = "insurance" + tableRowID;

            commcheck = checkNull(containergrid.getCommodityothers());
            if (commcheck.equalsIgnoreCase("Y")) {
                commodityval = checkNull(containergrid.getEbook_commoditycode()).toUpperCase();
            } else {
                if (checkNull(containergrid.getEbook_commoditycode()).length() > 0 && checkNull(containergrid.getEbooking_commodity()).length() > 0) {
                    commodityval = (checkNull(containergrid.getEbooking_commodity()) + " ~~ " + checkNull(containergrid.getEbook_commoditycode())).toUpperCase();
                } else {
                    commodityval = "";
                }
            }

            hiddenhscode = checkNull(containergrid.getEbook_commoditycode());
            if (containergrid.getHscode() != null && checkNull(containergrid.getHscode()).length() > 0) {
                if (checkNull(containergrid.getEbook_commoditycode()).trim().equalsIgnoreCase(fakComdtyCode)) {
                    hscommodityval = checkNull(containergrid.getHscode());
                } else {
                    hscommodityval = (checkNull(containergrid.getHsCommodityVal()) + " ~~ " + checkNull(containergrid.getHscode())).toUpperCase();
                }
            } else {
                hscommodityval = "";
            }
            if (containergrid.getHscode() != null && checkNull(containergrid.getHscode()).length() > 0 && (commcheck.equalsIgnoreCase("Y") || "1".equalsIgnoreCase(containergrid.getAppVersion()))) {
                hscommodityval = checkNull(containergrid.getHscode());
            }
            packageval = (checkNull((String) packagemap.get(checkNull(containergrid.getEbook_packagecode()))) + " ~~ " + checkNull(containergrid.getEbook_packagecode())).toUpperCase();

            if (packageval != null && packageval.equalsIgnoreCase(" ~~ ")) {
                packageval = "";
            }

            if (containergrid.getEbook_unno() != null && checkNull(containergrid.getEbook_unno()).length() > 0) {
                dgclassname = (checkNull(containergrid.getEbook_unno()) + " ~~ " + (checkNull((String) dgclassmap.get(checkNull(containergrid.getEbook_unno()))))).toUpperCase();
                dgclassval = checkNull((String) dgclassmap.get(checkNull(containergrid.getEbook_unno())));
            } else {
                dgclassname = "";
                dgclassval = "";
            }

            count_row = Integer.parseInt(tableRowID);
            if (count_row % 2 == 0) {
                str_row = "info";
            } else {
                str_row = "active";
            }
            commodityval = commodityval.replaceAll("\"", "%22");
            commodityval = commodityval.replaceAll("'", "%27");
            hscommodityval = hscommodityval.replaceAll("\"", "%22");
            hscommodityval = hscommodityval.replaceAll("'", "%27");

            actualcmdtyval = checkNull(containergrid.getEbook_actCommoName());
            actualcmdtyval = actualcmdtyval.replace("\n", " ").replace("\r", " ").replace(" ", " ");
            actualcmdtyval = actualcmdtyval.replaceAll("\"", "%22");
            actualcmdtyval = actualcmdtyval.replaceAll("'", "%27");
            containergrid.setEbook_actCommoName(actualcmdtyval);

            String cargovol = containergrid.getEbook_cargovolume();
            if (cargovol == null || "0".equalsIgnoreCase(cargovol) || "0.0".equalsIgnoreCase(cargovol) || "".equalsIgnoreCase(cargovol) || Float.parseFloat(cargovol) <= 0) {
                containergrid.setEbook_cargovolume("");
            }
            String cargontwt = containergrid.getEbook_netweight();
            if (cargontwt == null || "0".equalsIgnoreCase(cargontwt) || "0.0".equalsIgnoreCase(cargontwt) || "".equalsIgnoreCase(cargontwt) || Float.parseFloat(cargontwt) <= 0) {
                containergrid.setEbook_netweight("");
            }
            cargontwt = containergrid.getEbook_cargoweight();
            if ((!"M".equalsIgnoreCase(containergrid.getEqpstatus())) && (cargontwt == null || "0".equalsIgnoreCase(cargontwt) || "0.0".equalsIgnoreCase(cargontwt) || "".equalsIgnoreCase(cargontwt) || Float.parseFloat(cargontwt) <= 0)) {
                containergrid.setEbook_cargoweight("");
            }
            isinsurance = Validation.checkNull(containergrid.getIsinsuranceavailable());
            insuredemail = Validation.checkNull(containergrid.getInsuredemail());
            quoteno = Validation.checkNull(containergrid.getQuoteno());
            System.out.println("[ECOM-WAR] AddCargoDetails isinsurance==> " + isinsurance + " quoteno=> " + quoteno);
            suminsured = "";
            if (quoteno.length() > 0 && quoteno.contains("~~")) {
                suminsured = Validation.checkNull(NumberFormat.decimalFormat(Validation.checkNull(quoteno).split("~~")[2]));
                In_cust_type = Validation.checkNull(quoteno).split("~~")[1];
                quoteno = Validation.checkNull(quoteno).split("~~")[0];
            }
%>
<div id="<%=tableRowID%>" class="preview <%=str_row%>" >
    <span  class="rowidchange" id="rowidchange<%=tableRowID%>" style="display: none"><%=tableRowID%></span>
    <input type="hidden" id="cargocount" name="cargocount" value="<%=tableRowID%>">
    <input type="hidden" id="<%=hidden_oog%>" name="<%=hidden_oog%>" value="N">
    <input type="hidden" id="<%=hidden_lcl%>" name="<%=hidden_lcl%>"  value="N">
    <input type="hidden" id="<%=hidden_hzd%>" name="<%=hidden_hzd%>"  value="N">
    <input type="hidden" id="<%=hidden_rfr%>" name="<%=hidden_rfr%>" value="N">
    <input type="hidden" id="<%=hiddencomid%>" name="<%=hiddencomid%>">
    <input type="hidden" id="<%=hdcommoidtycode%>" name="<%=hdcommoidtycode%>">
    <input type="hidden" id="<%=hiddenhscodeid%>" name="<%=hiddenhscodeid%>">
    <input type="hidden" id="<%=hiddenpackid%>" name="<%=hiddenpackid%>">
    <input type="hidden" id="<%=comid%>" name="<%=comid%>" value="<%=containergrid.getEbook_comid()%>">
    <input type="hidden" id="<%=conid%>" name="<%=conid%>" value="<%=containergrid.getEbook_conid()%>">
    <div class="txt_box_book">

        <a herf='javascript:void(0)' onclick="deletecargo('datatablebycargo', this)" class="delete_cargo float-right"> <i class="fa fa-times-circle fa-2x"></i>  </a>

        <div class="form-group">
            <div class="row">

                <div class="col-xs-12 col-sm-4 ecom_padding_right">
                    <%if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {%>
                    <label></label>
                    <%}%>
                    <select onchange="setEqptype('<%=eqpType%>', this);setDGValues('<%=tableRowID%>');
                            getTareweight(this.value, '<%=tableRowID%>');
                            changeEqpSizeToHaulage(this);" class="form-control required light_border eqpsize" id="<%=eqpSize%>" name="<%=eqpSize%>" >
                        <option value="">Select Eqp Size</option>
                    </select>
                    <input type="hidden" onchange="cargoselectmethod(this, '<%=tableRowID%>');
                            /*getEqpList_new(this.value, '<%=eqpSize%>');*/" id="<%=eqpType%>" name="<%=eqpType%>">
                </div>


                <div class="col-xs-12 col-sm-4 ">
                    <%if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {%>
                    <label></label>
                    <%}%>
                    <div class='inc_dec'>
                        <span style="position: relative; top: 8px">
                            <bean:message key='ebooking.trust.eqpmentqty' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                        </span>
                        <span class="float-right align_plus_minus" >
                            <div class="value-button" id="decrease" onclick="decreaseValue(this)" value="Decrease Value">-</div>
                            <input type="number" class="number required zerocheck numbervalpoint number_val_cls checkeqpquantity" value="<%=containergrid.getEbook_eqp_qty()%>" id="<%=eqpQty%>" name="<%=eqpQty%>" onchange="removeHaulageDetailsOnQuantity(this);">
                            <div class="value-button" id="increase" onclick="increaseValue(this)" value="Increase Value">+</div>
                        </span>	
                        <input type="hidden" id="eqpquantity" class="eqpquantity" value="0">
                    </div>
                </div>

                <%if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {%>

                <div class="col-xs-12 col-sm-1 text-center" style="margin-left: 40px;">
                    <label for="<%=soc%>" class="label_switch" style="margin-bottom: 4px;" > <bean:message key='ebooking.soc.text.display' bundle="<%=clientcode%>"/> </label>
                    <label class="switch">
                        <input type="checkbox" value="N" id="<%=soc%>" name="<%=soc%>" onchange="enableEqpStatus('<%=eqpStatus%>', this);" />
                        <span class="slider round"></span>
                    </label>

                </div>

                <div class="col-xs-12 col-sm-1 text-center" style="margin-left: 8px;">

                    <label  for="<%=oogflag%>" class="label_switch" style="margin-bottom: 4px;" > <bean:message key='ebooking.oog.text.display' bundle="<%=clientcode%>"/> </label>
                    <label class="switch" >
                        <input type="checkbox" value="N"   class="switch-button oogswitchbtn"  id="<%=oogflag%>" name="<%=oogflag%>" <%=("Y".equalsIgnoreCase(containergrid.getOog()) ? "checked" : "")%> disabled="true" onchange="enableMeasurement(this, '<%=tableRowID%>');" />
                        <span class="slider round"></span>
                    </label> 
                </div>

                <div class="col-xs-12 col-sm-1 text-center" style="margin-left: 8px;padding: 0;">
                    <label for="<%=hzd%>" class="label_switch" style="margin-bottom: 4px;margin-left: 10%;" > <bean:message key='ebooking.hazardous.display' bundle="<%=clientcode%>"/> </label>
                    <label class="switch">
                        <input type="checkbox" value="N"   onchange="cargoselectmethod_hzd(this, '<%=tableRowID%>');setDGValues('<%=tableRowID%>');" class="checkhazardous" id="<%=hzd%>" name="<%=hzd%>"  />
                        <span class="slider round"></span>
                    </label>
                </div>
                <%} else {%>
                <div class="col-xs-12 col-sm-2 text-center">
                    <label for="<%=soc%>" class="label_switch"> <bean:message key='ebooking.soc.text.display' bundle="<%=clientcode%>"/> </label>
                    <label class="switch">
                        <input type="checkbox" value="N" id="<%=soc%>" name="<%=soc%>" onchange="enableEqpStatus('<%=eqpStatus%>', this);" />
                        <span class="slider round"></span>
                    </label>

                </div>


                <div class="col-xs-12 col-sm-2 text-right">
                    <label for="<%=hzd%>" class="label_switch"> <bean:message key='ebooking.hazardous.display' bundle="<%=clientcode%>"/> </label>
                    <label class="switch">
                        <input type="checkbox" value="N"   onchange="cargoselectmethod_hzd(this, '<%=tableRowID%>');setDGValues('<%=tableRowID%>');" class="checkhazardous" id="<%=hzd%>" name="<%=hzd%>"  />
                        <span class="slider round"></span>
                    </label>
                </div>
                <%}%>

            </div>
        </div>
        <div class="form-group">
            <div class="row">

                <div class="col-xs-12 col-sm-4 ecom_padding_right">
                    <bean:message key='ebooking.eqpstatus.display' bundle="<%=clientcode%>"/>
                    <select class="form-control light_border eqpstatusclass" id="<%=eqpStatus%>" name="<%=eqpStatus%>" onchange="equipchange('<%=tableRowID%>', this.value)" disabled="true">
                        <option value="F">Laden</option>
                        <option value="M">Empty</option>
                    </select>
                    <!--<div class="inc_dec">
                        <span style="position: relative; top: 8px">
                            <bean1:message key='ebooking.eqpstatus.display' bundle="<%=clientcode%>"/>
                        </span>
                        <div class = "vertical_line"></div> 
                        <select class="form-control col-sm-6 txt_select_weight eqpstatusclass" id="<%=eqpStatus%>" name="<%=eqpStatus%>" onchange="equipchange('<%=tableRowID%>', this.value)" disabled="true">
                            <option value="F">Laden</option>
                            <option value="M">Empty</option>
                        </select>
                    </div>-->
                </div>

                <div class="col-xs-12 col-sm-4">
                    <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                    <bean:message key="shipping.type.display" bundle="<%=clientcode%>"/>
                    <%} else {%>
                    <bean:message key="shipping.packagetype.display" bundle="<%=clientcode%>"/>
                    <%}%>
                    <input type="text" class="form-control light_border  packtypehead autocompletetmsgfortype" id="<%=packagename%>" name="<%=packagename%>">
                    <label style="display:none;color:#a94442" class="comclass" id='<%=packagenameport%>'>Invalid Package</label>
                </div>

                <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                <div class="col-xs-12 col-sm-4 ">
                    <bean:message key="shipping.unit.display" bundle="<%=clientcode%>"/>
                    <input type="text" class="form-control light_border number zerocheck numbervalpoint" id="<%=packageQty%>" name="<%=packageQty%>" maxlength="8">
                </div>
                <%} else {%>
                <div class="col-xs-12 col-sm-4 ">
                    <bean:message key="shipping.quantity.display" bundle="<%=clientcode%>"/>
                    <input type="text" class="form-control light_border number zerocheck numbervalpoint" id="<%=packageQty%>" name="<%=packageQty%>" maxlength="8">
                </div>
                <%}%>

            </div>
        </div>
        <div class="form-group">
            <div class="row">

                <div class="col-xs-12 col-sm-4 ecom_padding_right">
                    <bean:message key='ebooking.tareweight.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                    <input type="text" class="form-control light_border required" readonly="readonly" id="<%=tareWt%>" name="<%=tareWt%>"  value="0"/>
                </div>

                <div class="col-xs-12 col-sm-4 ">
                    <bean:message key='ebooking.cargoweight.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span>
                    <div class="inc_dec">
                        <span style="position: relative; top: 8px">
                            <input type="text" class="cargo_weight number required grossWeightCheck <%=cargoweightcheck%> cargweightmsg" id="<%=cargoWt%>" name="<%=cargoWt%>" placeholder="" onblur="cargoweightcalc('<%=tableRowID%>');PayLoadValidation('<%=cargoWt%>', '<%=grossWt%>', '<%=cargoWtUnit%>', '<%=eqpQty%>', '<%=eqpSize%>');" maxlength="9" onkeypress="return ValDigit(event, this);" onkeyup="checkvalue(event, this.value, '<%=cargoWt%>');">
                        </span>

                        <div class = "vertical_line"></div> 
                        <input type="hidden" id="<%=cargoWtUnit%>_previous" name="<%=cargoWtUnit%>_previous" value="K" />
                        <select class="form-control col-sm-5 txt_select_weight" id="<%=cargoWtUnit%>" name="<%=cargoWtUnit%>" onchange="changeCargoWT('<%=tableRowID%>', this.value, 'new');grossweightValidation();">
                            <option value="K">Kilos</option>
                            <%if ("N".equalsIgnoreCase(weight_unit_kilos_only)) {%>
                            <!--                            <option value="T">Metric Tones</option>-->
                            <option value="P">Pounds</option>
                            <%}%>
                        </select>
                    </div>
                </div>

                <div class="col-xs-12 col-sm-4">
                    <bean:message key='ebooking.grossweight.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                    <input type="text" class="form-control number required light_border" readonly="readonly" id="<%=grossWt%>" name="<%=grossWt%>" value="0">
                    <input type="hidden" id="<%=grossWtUnit%>" name="<%=grossWtUnit%>">
                </div>

            </div>
        </div>
        <div class="form-group">
            <div class="row">

                <!--                       netweight-->
                <div class="col-xs-12 col-sm-4 ecom_padding_right">
                    <bean:message key='ecom.cargo.netweight' bundle="<%=clientcode%>"/>
                    <div class="inc_dec">
                        <span style="position: relative; top: 8px">
                            <input type="text" class="cargo_weight number  grossWeightCheck <%=netweightcheck%>" id="<%=netWt%>" name="<%=netWt%>" placeholder=""  maxlength="9" onkeypress="return ValDigit(event, this);" onkeyup="checkvalue(event, this.value, '<%=netWt%>');">
                        </span>
                    </div>
                </div>

                <div class="col-xs-12 col-sm-4">
                    <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                    <bean:message key='ebooking.trust.commoditygrp' bundle="<%=clientcode%>"/>
                    <%} else {%>
                    <bean:message key='ebooking.trust.commodity' bundle="<%=clientcode%>"/>
                    <%}%>
                    <%if ("Y".equalsIgnoreCase(CommodityName_as_HS_Chapter)) {%><bean:message key="ecom.si.hs.Chapter" bundle="<%=clientcode%>"/><%}%> <span class="mandatory">*</span>
                    <input type="hidden" id="<%=commodityothers%>" name="<%=commodityothers%>" value="N" />
                    <input type="text" class="form-control  <%=commodityclass%> commoditytypehead required light_border autocompletetmsg" aria-label="..." id="<%=commodity%>" name="<%=commodity%>" placeholder="<bean:message key="bl.commodity.display" bundle="<%=clientcode%>"/>" >
                    <label style="display:none;color:#a94442" class="comclass" id='<%=commodityport%>'>Invalid Commodity</label>
                    <input type="text" class="form-control  <%=othercommodity%> light_border"  id="<%=othercommodity%>"  name="<%=othercommodity%>" style="display:none" maxlength="100">
                </div> 

                <div class="col-xs-12 col-sm-4 input_type_width">
                    <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {
                        hscodeclass =hscodeclass+ " required";
                        %>
                    <bean:message key='ebooking.trust.commodity' bundle="<%=clientcode%>"/><span class="mandatory"> * </span>
                    <%} else if ("Y".equalsIgnoreCase(hsnamecode)) {%>
                    <bean:message key='ebooking.trust.hsnamecode' bundle="<%=clientcode%>"/>
                    <%} else {%>
                    <bean:message key='ebooking.trust.hscode' bundle="<%=clientcode%>"/>
                    <%}%>   
                    <div class="input-group">
                        <input type="text" class="form-control light_border <%=hscodeclass%> hscodetypehead hscodemandatoryclass autocompletetmsg" aria-label="..." id="<%=hscode%>" name="<%=hscode%>">
                        <input type="text" class="form-control light_border <%=otherhscode%> hscodemandatoryclass" id="<%=otherhscode%>" name="<%=otherhscode%>" style="display:none" maxlength="100">
                        <div class="input-group-addon group_search">
                            <span class="input-group-text"  onclick="callCommoditySearch('<%=commodity%>', '<%=hscode%>', '<%=tableRowID%>');" >
                                <i class="fa fa-search"></i>
                            </span>
                        </div>
                    </div>
                </div>

            </div>
        </div>


        <div class="form-group">
            <div class="row">
                <div class="col-sm-2 ecom_padding_right" >
                    <%if ("Y".equalsIgnoreCase(bookingvolumeMandatory)) {%>
                    <bean:message key='ebooking.containervolume.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span>
                    <input type="text" class="form-control light_border number required <%=volumeweightcheck%>" id="<%=volume%>" name="<%=volume%>" maxlength="8" onkeypress="return ValDigit(event, this);" onkeyup="checkvalue(event, this.value, '<%=volume%>');">
                    <%} else {%>
                    <bean:message key='ebooking.containervolume.display' bundle="<%=clientcode%>"/>
                    <input type="text" class="form-control light_border number <%=volumeweightcheck%>" id="<%=volume%>" name="<%=volume%>" maxlength="8" onkeypress="return ValDigit(event, this);" onkeyup="checkvalue(event, this.value, '<%=volume%>');">
                    <%}%>
                </div>
                <div class="col-sm-2 ecom_padding_left">
                    <label></label>
                    <select class="form-control light_border" id="<%=volUnit%>" name="<%=volUnit%>">
                        <option value="M">Cubic Meter</option>
                        <option value="F">Cubic Feet</option>
                    </select>
                </div>
                <%if ("Y".equalsIgnoreCase(enableActCommoName)) {%>
                <div class="col-xs-12 col-sm-4">
                    <bean:message key="shipping.actcommoname.display" bundle="<%=clientcode%>"/><span class="mandatory">*</span>
                    <input type="text" class="form-control light_border required" id="<%=actCommoName%>" name="<%=actCommoName%>" maxlength="100">
                </div>
                <%}%>
                  <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
                <div class="col-xs-12 col-sm-4">
                     <bean:message key='ebooking.cmdtyhblno.display' bundle="<%=clientcode%>"/>
                    <input type="text" class="form-control light_border " id="<%=cmdtyhblno%>" name="<%=cmdtyhblno%>" maxlength="20">
                </div>
                <%}%>
                <%if ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(insuranceaccess)) {%>
                <input type="hidden" class="inquotecusttype" id="<%=insurance + "_custtype"%>" value="<%=In_cust_type%>">
                <input type="hidden" class="inquoteno" id="<%=insurance + "_quoteno"%>" value="<%=quoteno%>">
                <input type="hidden" class="incargoval" id="<%=insurance + "_cargoval"%>" value="<%=suminsured%>" >
                <input type="hidden" class="inavailable" id="<%=insurance + "_available"%>" value="<%=(Validation.checkNull(quoteno).length() > 0 ? "Y" : "N")%>" >
                <input type="hidden" class="insuredemail" id="<%=insurance + "_insuredemail"%>" value="<%=insuredemail%>" >
                <!--                <div class="col-xs-12 col-sm-2 text-center">
                                    <a href="javascript:void(0)" onclick="loadinsurancepage($('#<=insurance + "_quoteno"%>').val(), $('#<=insurance + "_custtype"%>').val())"  class="btn btn-info btn-md addinsurance" id="addinsurance"><i class=""></i> <bean1:message key="booking.btn.cargo.insurance" bundle="<=clientcode%>"/> </a>
                                </div>
                                <div class="col-xs-12 col-sm-2 text-center">
                                    <label for="<=insurance%>" class="label_switch"> <bean1:message key="booking.cargo.insurance" bundle="<=clientcode%>"/> </label>
                                    <label class="switch">
                                        <input type="checkbox" value="N" id="<=insurance%>" name="<=insurance%>" class="insuranceswitchbtn"  disabled="disabled"/>
                                        <span class="slider round"></span>
                                    </label>
                                </div>-->
                <%}%>
            </div>
        </div>
          <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
        <div class="form-group">
            <div class="row">
                  <div class="col-xs-12 col-sm-4">
                  <bean:message key='ebooking.sellersName.display' bundle="<%=clientcode%>"/>
                 <select  class="form-control input-sm setdata  light_border" id="<%=sellerSelect%>" name="sellerNameList" onchange="setCustNameCode(this,'<%=sellerCode%>','<%=sellersName%>')">
                        <option value="">Select Customer</option>
                    </select>
                     <input type="hidden" id="<%=sellerCode%>" name="<%=sellerCode%>"  />
                     <input type="hidden" id="<%=sellersName%>" name="<%=sellersName%>" />
                </div>
                  <div class="col-xs-12 col-sm-4">
                  <bean:message key='ebooking.buyersName.display' bundle="<%=clientcode%>"/>
                   <select  class="form-control input-sm setdata light_border" id="<%=buyerSelect%>" name="buyerNameList"  onchange="setCustNameCode(this,'<%=buyersCode%>','<%=buyersName%>')" )>
                        <option value="">Select Customer</option>
                    </select>
                    <input type="hidden" id="<%=buyersCode%>" name="<%=buyersCode%>"  />
                    <input type="hidden" id="<%=buyersName%>" name="<%=buyersName%>" />
                </div>
                </div></div>
           <%}%>
        <div class="hazardous" id="<%=hzdDiv%>" style="display:none">
            <div class = "horizontal_line"></div>
            <!-- Hidden field for DG count -->
            <input type="hidden" id="dgCount<%=tableRowID%>" name="dgCount<%=tableRowID%>" value="1">
            
            <!-- DG Container - Always uses _1 suffix (unified for single & multiple) -->
            <div id="dgContainer<%=tableRowID%>">
                <div class="dg-entry" id="dgEntry<%=tableRowID%>_1">
                    <div class="form-group row">
                        <div class="col-sm-4">
                            <label><bean:message key='ebooking.marine.pollut' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                            <select  class="form-control input-sm setdata <%=hzdclass%> light_border" id="<%=marinePollut%>_1" name="<%=marinePollut%>_1">
                                <option value="N">No</option>
                                <option value="Y">Yes</option>
                            </select>
                        </div>

                        <div class="col-sm-4">
                            <label><bean:message key='ebooking.UN.no' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                            <input type="text" class="form-control input-sm light_border setdata <%=hzdclass%>" id="<%=unno%>_1" name="<%=unno%>_1" maxlength="600">
                        </div>

                        <div class="col-sm-2 ecom_padding_right">
                            <label><bean:message key='ebooking.flash.point' bundle="<%=clientcode%>"/></label>
                            <input type="text" maxlength="8" value="" class="form-control input-sm number setdata light_border" id="<%=flashPoint%>_1" name="<%=flashPoint%>_1">
                        </div>
                        <div class="col-sm-2 ecom_padding_left">
                            <label></label>
                            <select  class="form-control input-sm setdata light_border" id="<%=flashUnit%>_1" name="<%=flashUnit%>_1">
                                <option value="C">Celsius</option>
                                <option value="F">Fahrenheit</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-4">
                            <label><bean:message key='ebooking.DG.class' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                            <input type="text" class="form-control input-sm setdata light_border" id="<%=dgClass%>_1" name="<%=dgClass%>_1" readonly>
                        </div>

                        <div class="col-sm-4">
                            <label><bean:message key='ebooking.shipping.name' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                            <input type="text" class="form-control input-sm setdata light_border" id="<%=shippingName%>_1" name="<%=shippingName%>_1" readonly>
                        </div>
                        
                        <%
                        // Show Add/Remove buttons if plugin is enabled
                        if ("Y".equalsIgnoreCase(isMultipleDGEnabled)) {
                        %>
                        <div class="col-sm-3">
                            <button type="button" class="btn btn-sm btn-success" onclick="addDGRow(<%=tableRowID%>)" title="Add DG Entry" style="margin-top: 22px; margin-right: 5px;">
                                <i class="fa fa-plus"></i>
                            </button>
                            <button type="button" class="btn btn-sm btn-danger" onclick="removeDGRow(this, <%=tableRowID%>)" title="Remove DG Entry" style="margin-top: 22px;">
                                <i class="fa fa-minus"></i>
                            </button>
                        </div>
                        <%
                        }
                        %>
                    </div>
                </div>
            </div>
            
            <%
            // Populate DG entries if editing existing cargo with multiple DG
            if (containergrid != null && containergrid.getDgDetailsList() != null && !containergrid.getDgDetailsList().isEmpty()) {
            %>
            <script type="text/javascript">
                $(document).ready(function() {
                    var dgList = [
                        <%
                        List<net.ecom.vo.EbookingValidatorFormBeanNew> dgList = containergrid.getDgDetailsList();
                        for (int dgIdx = 0; dgIdx < dgList.size(); dgIdx++) {
                            net.ecom.vo.EbookingValidatorFormBeanNew dgEntry = dgList.get(dgIdx);
                            // Look up shipping name from dgclassmap using UN number
                            String shipName = "";
                            if (dgEntry.getEbook_unno() != null && checkNull(dgEntry.getEbook_unno()).length() > 0) {
                                shipName = checkNull((String) dgclassmap.get(checkNull(dgEntry.getEbook_unno())));
                            }
                            if (dgIdx > 0) { out.print(","); }
                        %>
                        {
                            ebook_poluttunit: '<%=checkNull(dgEntry.getEbook_poluttunit())%>',
                            ebook_unno: '<%=checkNull(dgEntry.getEbook_unno())%>',
                            ebook_flashpoint: '<%=checkNull(dgEntry.getEbook_flashpoint())%>',
                            ebook_flashunit: '<%=checkNull(dgEntry.getEbook_flashunit())%>',
                            ebook_dgclass: '<%=checkNull(dgEntry.getEbook_dgclass())%>',
                            ebook_shipname: '<%=shipName%>'
                        }
                        <%
                        }
                        %>
                    ];
                    populateDGEntries(<%=tableRowID%>, dgList);
                });
            </script>
            <%
            }
            %>
        </div>
        <div class="opencar" id="<%=oogDiv%>"  style="display:none">
            <div class = "horizontal_line"></div> 
            <div class="form-group row">
                <div class="col-sm-4">
                    <label><bean:message key='ebooking.dimensionunit.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <select  class="form-control input-sm setdata <%=oogclass%> light_border" id="<%=dimUnit%>" name="<%=dimUnit%>">
                        <option value="M">Meters</option>
                        <option value="F">Feet</option>
                        <option value="C">Centimeters</option>
                    </select>
                </div>



                <div class="col-sm-4">
                    <label><bean:message key='ebooking.dimensionleft.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <input type="text"  maxlength="6" value=""  class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border" id="<%=owLeft%>" name="<%=owLeft%>">
                </div>



                <div class="col-sm-4">
                    <label><bean:message key='ebooking.dimensionright.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <input type="text"  maxlength="6"  class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border" id="<%=owRight%>" name="<%=owRight%>">
                </div>
            </div>
            <div class="form-group row">

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.dimensionforward.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <input type="text"  maxlength="6" value=""  class="form-control number input-sm setdata validatezeropoint <%=oogclass%> light_border" id="<%=olForward%>" name="<%=olForward%>">
                </div>


                <div class="col-sm-4">
                    <label><bean:message key='ebooking.dimensionalt.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <input type="text"  maxlength="6" value=""  class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border" id="<%=olAft%>" name="<%=olAft%>">
                </div>


                <div class="col-sm-4">
                    <label><bean:message key='ebooking.dimensiontop.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <input type="text"  maxlength="6" value="" class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border" id="<%=oh%>" name="<%=oh%>">
                </div>
            </div>
        </div>
        <div class="reefer" id="<%=refDiv%>" style="display:none">
            <div class = "horizontal_line"></div> 
            <div class="form-group row">

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.NOR.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                    <select  onChange="setTempSetEnble('<%=tableRowID%>');"  class="form-control input-sm setdata <%=refclass%> light_border" id="<%=nor%>" name="<%=nor%>">
                        <option value="N">No</option>
                        <option value="Y">Yes</option>
                    </select>
                </div>

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.preset.temp' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <input type="text"  class="form-control input-sm setdata <%=refclass%> settempclass light_border" id="<%=presetTemp%>" name="<%=presetTemp%>" onkeypress="return ValDigittemp(event, this);" onblur="moneyFormat(this)">
                </div>
                <%if (!"Y".equalsIgnoreCase(hideMinMaxTemp)) {%>
                <div class="col-sm-4">
                    <label><bean:message key='ebooking.min.temp' bundle="<%=clientcode%>"/><%if("Y".equalsIgnoreCase(enable_Reefer_MixTemp_Req)){%><span class="mandatory">*</span><%}%></label>
                    <input type="text"  class="form-control input-sm setdata mintempclass light_border <%= "Y".equalsIgnoreCase(enable_Reefer_MixTemp_Req) ? refclass : "" %>" id="<%=minTemp%>" name="<%=minTemp%>"  <%= "Y".equalsIgnoreCase(enable_Reefer_MixTemp_Req) ? "min=\"" + reefer_MinTemp_Limit + "\"" : "" %> onkeypress="return ValDigittemp(event, this);" onblur="moneyFormat(this)">
                </div>
            </div>
            <div class="form-group row">

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.max.temp' bundle="<%=clientcode%>"/><%if("Y".equalsIgnoreCase(enable_Reefer_MixTemp_Req)){%><span class="mandatory">*</span><%}%></label>
                    <input type="text"  class="form-control input-sm setdata  maxtempclass light_border <%= "Y".equalsIgnoreCase(enable_Reefer_MixTemp_Req) ? refclass : "" %>" id="<%=maxTemp%>" name="<%=maxTemp%>"  <%= "Y".equalsIgnoreCase(enable_Reefer_MixTemp_Req) ? "max=\"" + reefer_MaxTemp_Limit + "\"" : "" %> onkeypress="return ValDigittemp(event, this);" onblur="moneyFormat(this)">
                </div>
                <%}%>
                <div class="col-sm-4">
                    <label><bean:message key='ebooking.temp.unit' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <select  class="form-control input-sm setdata <%=refclass%> light_border" id="<%=tempUnit%>" name="<%=tempUnit%>">
                        <option value="C">Celsius</option>
                        <%if (!"GWF".equalsIgnoreCase(carrierCode)) {%>
                        <option value="F">Fahrenheit</option>
                        <%}%>
                    </select>
                </div>

                <%if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {%>

                <div class="col-sm-4">
                    <label id='<%=humidity + "_label"%>'><bean:message key='ebooking.humidity' bundle="<%=clientcode%>"/></label>
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">
                                <input type="checkbox" id="<%=humidityflag%>" name="<%=humidityflag%>" onchange="enableHumidity('<%=humidityflag%>', '<%=humidity%>')" value="Y">
                            </span>
                        </div>
                        <input type="text"  class="form-control input-sm setdata light_border" id="<%=humidity%>" name="<%=humidity%>" onkeyup="return validateHumidityDigit(this);" maxlength="5" onblur="removeHyphen(this)" disabled />
                    </div>
                </div>
                <%}%>
            </div>
            <%if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {%>
            <div class="form-group row">

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.drains' bundle="<%=clientcode%>"/></label>
                    <select  class="form-control input-sm light_border" id="<%=drains%>" name="<%=drains%>">
                        <option value=""></option>
                        <option value="O">Open</option>
                        <option value="C">Closed</option>
                    </select>
                </div>

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.co2' bundle="<%=clientcode%>"/></label>
                    <input type="text"  class="form-control input-sm setdata light_border" id="<%=co2%>" name="<%=co2%>" onkeyup="return validateHumidityDigit(this);" maxlength="5" onblur="removeHyphen(this)"/>
                </div>

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.o2' bundle="<%=clientcode%>"/></label>
                    <input type="text"  class="form-control input-sm setdata light_border" id="<%=o2%>" name="<%=o2%>" onkeyup="return validateHumidityDigit(this);" maxlength="5" onblur="removeHyphen(this)"/>
                </div>
            </div>
            <%}%>
            <div class="form-group row">

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.vent' bundle="<%=clientcode%>"/><%= Ventilationlabel%></label>
                    <input type="text"  class="form-control input-sm setdata light_border" id="<%=ventilation%>" name="<%=ventilation%>" onkeyup="return validateHumidityDigit(this);" maxlength="5" onblur="removeHyphen(this)"/>
                </div>

            </div>
        </div>
    </div>
</div>

<script>
    //edit
    var refDiv = "#refDiv" +<%=tableRowID%>;
    var oogDiv = "#oogDiv" +<%=tableRowID%>;
    var hzdDiv = "#hzdDiv" +<%=tableRowID%>;
    var refclass = ".refclass" +<%=tableRowID%>;
    var oogclass = ".oogclass" +<%=tableRowID%>;
    var hzdclass = ".hzdclass" +<%=tableRowID%>;
    var hscommodityval = '<%=hscommodityval%>';
    var commodityval = '<%=commodityval%>';
    var actualcmdtyval = '<%=actualcmdtyval%>';
    hscommodityval = unescape(hscommodityval);
    commodityval = unescape(commodityval);
    actualcmdtyval = unescape(actualcmdtyval);
    cargoweightcheck = ".cargoweightcheck" +<%=tableRowID%>;
    netweightcheck = ".netweightcheck" +<%=tableRowID%>;
    volumeweightcheck = ".volumeweightcheck" +<%=tableRowID%>;
    $('#' + '<%=eqpType%>').val('<%=containergrid.getEbook_eqp_type()%>').trigger('change');
    getEqpList_new('<%=eqptypeval%>', '<%=eqpSize%>');
    $('#' + '<%=eqpSize%>').val('<%=containergrid.getEbook_eqp()%>').trigger('change');
    var rfr = '<%=containergrid.getEbook_eqp_type()%>';
    console.log("reefervalue" + rfr);
    var hzd = '<%=containergrid.getHzd()%>';
    var oog = '<%=containergrid.getEbook_eqp_type()%>';
    var soc = '<%=containergrid.getSoc()%>';
    var othersCmdty = '<%=containergrid.getCommodityothers()%>';
    var eqpst = '<%=containergrid.getEqpstatus()%>';
    if (soc == 'Y') {
        $('#' + '<%=soc%>').prop('checked', true).trigger('change');
    }
    // enableEqpStatus('<%=eqpStatus%>', soc);
    if (hzd == 'Y') {
        $('#' + '<%=hzd%>').prop('checked', true).trigger('change');
    }
    if (hzd == 'Y') {
        $(hzdclass).addClass('required');
        $(hzdDiv).show();
        $('#' + '<%=hscode%>').addClass('required');
        // Multiple DG: populateDGEntries() in JSP script will handle data loading
        // No need to call setDGValues() or setShippingName() here
    }
    //alert(rfr)
    if (rfr == 'R') {
        $(refclass).addClass('required');
        $(refDiv).show();
    }
    if (oog == 'O') {
        if ($('#OOG_Plugin').val() == 'N') {
            $(oogclass).addClass('required');
            $(oogDiv).show();
        } else {
            $('#<%=oogflag%>').attr('disabled', false);
    <%if ("Y".equalsIgnoreCase(containergrid.getOog())) {%>
            console.log('inside oogvalue')
            $(oogclass).addClass('required');
            $(oogDiv).show();
            $('#<%=oogflag%>').attr('disabled', false);
            $('#<%=oogflag%>').prop('checked', true).trigger('change');
    <%}%>
        }
    }

    if (eqpst == 'F') {
        $(cargoweightcheck).removeClass('valuezero');
        $(netweightcheck).removeClass('validatezeropoint');
        $(volumeweightcheck).removeClass('validatezeropoint');
        $(cargoweightcheck).addClass('validatepoint');
        $(netweightcheck).addClass('validatepoint');
        $(volumeweightcheck).addClass('validatepoint');
    }
    if (eqpst == 'M') {
        $(cargoweightcheck).removeClass('validatepoint');
        $(volumeweightcheck).removeClass('validatepoint');
        $(netweightcheck).removeClass('validatepoint');
        $(cargoweightcheck).addClass('valuezero');
        $(volumeweightcheck).addClass('validatezeropoint');
        $(netweightcheck).addClass('validatezeropoint');

    }

    if (othersCmdty == 'Y')
    {
        $('#' + '<%=othersCmdty%>').attr('checked', true);
        $('#' + '<%=othercommodity%>').val(commodityval);
        $('#' + '<%=othercommodity%>').css("display", "block");
        $('#' + '<%=commodity%>').css("display", "none");
        $('#' + '<%=othercommodity%>').addClass('required');
        $('#' + '<%=commodity%>').removeClass('required');
        $('#' + '<%=othercommodity%>').attr('maxLength', '100');
        $('#' + '<%=commodityothers%>').val('Y');
        //
        $('#' + '<%=otherhscode%>').val(hscommodityval);
        $('#' + '<%=otherhscode%>').css("display", "block");
        $('#' + '<%=hscode%>').css("display", "none");
        $('#' + '<%=otherhscode%>').attr('maxLength', '100');
    } else
    {
        $('#' + '<%=commodity%>').val(commodityval);
        $('#' + '<%=othercommodity%>').css("display", "none");
        $('#' + '<%=commodity%>').css("display", "block");
        $('#' + '<%=othercommodity%>').removeClass('required');
        $('#' + '<%=commodity%>').addClass('required');
        $('#' + '<%=hscode%>').val(hscommodityval);
        $('#' + '<%=otherhscode%>').css("display", "none");
        $('#' + '<%=hscode%>').css("display", "block");

    }
    cargoselectmethod(document.getElementById('<%=eqpType%>'), '<%=tableRowID%>');
    $('#' + '<%=hdcommoidtycode%>').val('<%=hiddenhscode%>');
    //  $('#' + '<%=hscode%>').val(hscommodityval');
    $('#' + '<%=packagename%>').val("<%=packageval%>");
    $('#' + '<%=eqpStatus%>').val('<%=containergrid.getEqpstatus()%>');
    $('#' + '<%=tareWt%>').val('<%=containergrid.getEbook_tareweight()%>');
    <%if (containergrid.getEbook_cargoweight() != null && (!"".equalsIgnoreCase(containergrid.getEbook_cargoweight()))) {%>
    $('#' + '<%=cargoWt%>').val(parseFloat('<%=containergrid.getEbook_cargoweight()%>'));
    <%} else {%>
    $('#' + '<%=cargoWt%>').val('<%=containergrid.getEbook_cargoweight()%>');
    <%}%>
    $('#' + '<%=netWt%>').val('<%=Validation.checkNull(containergrid.getEbook_netweight())%>');
    $('#' + '<%=grossWt%>').val('<%=containergrid.getEbook_grossweight()%>');
    if ('<%=containergrid.getEbook_cargovolume()%>' == "" || '<%=containergrid.getEbook_cargovolume()%>' == null)
    {
        $('#' + '<%=volume%>').val('<%=containergrid.getEbook_cargovolume()%>');
    } else
    {
        $('#' + '<%=volume%>').val(parseFloat('<%=containergrid.getEbook_cargovolume()%>'));
    }
    $('#' + '<%=cargoWtUnit%>').val('<%=containergrid.getEbook_weightunit()%>');
    $('#' + '<%=cargoWtUnit%>_previous').val('<%=containergrid.getEbook_weightunit()%>');
    $('#' + '<%=netWtUnit%>').val('<%=containergrid.getEbook_netweightunit()%>');
    $('#' + '<%=netWtUnit%>_previous').val('<%=containergrid.getEbook_netweightunit()%>');
    $('#' + '<%=volUnit%>').val('<%=containergrid.getEbook_volumeunit()%>');
    $('#' + '<%=packageQty%>').val('<%=containergrid.getEbook_packquantity()%>');
    $('#' + '<%=actCommoName%>').val(actualcmdtyval);
    $('#' + '<%=cmdtyhblno%>').val('<%=containergrid.getEbook_cmdtyhblno()%>');
    $('#' + '<%=buyersName%>').val('<%=containergrid.getEbook_cmdtybuyerName()%>');
    $('#' + '<%=sellersName%>').val('<%=containergrid.getEbook_cmdtysellerName()%>');
    $('#' + '<%=sellerCode%>').val('<%=containergrid.getEbook_cmdtysellercode()%>');
    $('#' + '<%=buyersCode%>').val('<%=containergrid.getEbook_cmdtybuyercode()%>');
    setSellerNameSelectOption();
    $('#' + '<%=sellerSelect%>').val('<%=containergrid.getEbook_cmdtysellercode()%>'+'~~'+'<%=containergrid.getEbook_cmdtysellerName()%>');
    $('#' + '<%=buyerSelect%>').val('<%=containergrid.getEbook_cmdtybuyercode()%>'+'~~'+'<%=containergrid.getEbook_cmdtybuyerName()%>');
    $('#' + '<%=marinePollut%>').val('<%=containergrid.getEbook_poluttunit()%>');
    $('#' + '<%=unno%>').val('<%=dgclassname%>');
    $('#' + '<%=flashPoint%>').val('<%=containergrid.getEbook_flashpoint()%>');
    $('#' + '<%=flashUnit%>').val('<%=containergrid.getEbook_flashunit()%>');
    $('#' + '<%=shippingName%>').val('<%=dgclassval%>');
    $('#' + '<%=dgClass%>').val('<%=containergrid.getEbook_dgclass()%>');
    $('#' + '<%=presetTemp%>').val('<%=containergrid.getEbook_presetemp()%>');
    $('#' + '<%=minTemp%>').val('<%=containergrid.getEbook_mintemp()%>');
    $('#' + '<%=maxTemp%>').val('<%=containergrid.getEbook_maxtemp()%>');
    $('#' + '<%=tempUnit%>').val('<%=containergrid.getEbook_temperunit()%>');
    $('#' + '<%=nor%>').val('<%=containergrid.getEbook_nor()%>');
    $('#' + '<%=owLeft%>').val('<%=containergrid.getEbook_owleft()%>');
    $('#' + '<%=dimUnit%>').val('<%=containergrid.getEbook_dimensionunit()%>');
    $('#' + '<%=owRight%>').val('<%=containergrid.getEbook_owright()%>');
    $('#' + '<%=oh%>').val('<%=containergrid.getEbook_oh()%>');
    $('#' + '<%=olAft%>').val('<%=containergrid.getEbook_olaft()%>');
    $('#' + '<%=olForward%>').val('<%=containergrid.getEbook_olforward()%>');

    $('#' + '<%=hidden_oog%>').val('<%=containergrid.getOog()%>');
    $('#' + '<%=hidden_rfr%>').val('<%=containergrid.getRfr()%>');
    $('#' + '<%=hidden_hzd%>').val('<%=containergrid.getHzd()%>');

    $('#' + '<%=drains%>').val('<%=containergrid.getDrains()%>');
    $('#' + '<%=co2%>').val('<%=containergrid.getCo2()%>');
    $('#' + '<%=o2%>').val('<%=containergrid.getO2()%>');
    setTempSetEnble('<%=tableRowID%>');
    openOOGBlockOnLoad(oogclass, oogDiv, '<%=oogflag%>', '<%=containergrid.getOog()%>');
    <%if ("1".equalsIgnoreCase(containergrid.getHumidityflag())) {%>
    $('#' + '<%=humidityflag%>').prop('checked', true).trigger("onchange");
    $('#' + '<%=humidity%>').val('<%=containergrid.getHumidity()%>');
    <%}%>
    $('#' + '<%=ventilation%>').val('<%=containergrid.getVentilation()%>');
    if (othersCmdty != 'Y')
    {
        commodityload('<%=commodity%>', '<%=hiddencomid%>', '<%=tableRowID%>');
    }
    packageload('<%=packagename%>', '<%=hiddenpackid%>');
    // hscommodityload('<%=hscode%>', '<%=hiddenhscodeid%>');
    dgload('<%=unno%>_1', '<%=hiddencomid%>', '<%=dgClass%>_1', '<%=shippingName%>_1');
    hideV2OtherCommodity('<%=othersCmdty%>', 'othercmdtylable<%=tableRowID%>');
    if ($('#' +<%=soc%>).prop('checked') == true) {
        $('#' + '<%=tareWt%>').prop('readonly', false);
    }
    $('[data-toggle="tooltip"]').tooltip();
    <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
    $('#' + '<%=hscode%>').removeClass('required');
    $('#' + '<%=hscode%>').addClass('required');
    <%}%>

</script>
<%
    }
    session.setAttribute("ebooking_cargo_grid", null);
} else {

    eqpType = "eqpType" + tableRowID;
    eqpSize = "eqpSize" + tableRowID;
    eqpQty = "eqpQty" + tableRowID;
    soc = "soc" + tableRowID;
    hzd = "hzd" + tableRowID;
    commodityclass = "commodityclass" + tableRowID;
    commodity = "commodity" + tableRowID;
    hiddencomid = "hiddencomid" + tableRowID;
    hdcommoidtycode = "hdcommoidtycode" + tableRowID;
    hiddenhscodeid = "hiddenhscodeid" + tableRowID;
    commodityport = "commodity" + tableRowID + "_port";
    packagename = "packagename" + tableRowID;
    hiddenpackid = "hiddenpackid" + tableRowID;
    packagenameport = "packagename" + tableRowID + "_port";
    hscode = "hscode" + tableRowID;
    packageQty = "packageQty" + tableRowID;
    actCommoName = "actCommoName" + tableRowID;
    sellersName = "sellersName" + tableRowID;
    buyersName = "buyersName" + tableRowID;
     sellerSelect = "sellerSelect" + tableRowID;
            buyerSelect = "buyerSelect" + tableRowID;
    cmdtyhblno = "cmdtyhblno" + tableRowID;
    buyersCode = "buyersCode" + tableRowID;
    sellerCode = "sellerCode" + tableRowID;
    eqpStatus = "eqpStatus" + tableRowID;
    tareWt = "tareWt" + tableRowID;
    cargoWt = "cargoWt" + tableRowID;
    netWt = "netWt" + tableRowID;
    grossWt = "grossWt" + tableRowID;
    volume = "volume" + tableRowID;
    cargoWtUnit = "cargoWtUnit" + tableRowID;
    netWtUnit = "netWtUnit" + tableRowID;
    grossWtUnit = "grossWtUnit" + tableRowID;
    volUnit = "volUnit" + tableRowID;
    othersCmdty = "othersCmdty" + tableRowID;
    commodityothers = "commodityothers" + tableRowID;
    othercommodity = "othercommodity" + tableRowID;
    cargoweightcheck = "cargoweightcheck" + tableRowID;
    netweightcheck = "netweightcheck" + tableRowID;
    volumeweightcheck = "volumeweightcheck" + tableRowID;
    hscodeclass = "hscodeclass" + tableRowID;
    otherhscode = "otherhscode" + tableRowID;
    //
    marinePollut = "marinePollut" + tableRowID;
    unno = "unno" + tableRowID;
    flashPoint = "flashPoint" + tableRowID;
    flashUnit = "flashUnit" + tableRowID;
    shippingName = "shippingName" + tableRowID;
    dgClass = "dgClass" + tableRowID;
    presetTemp = "presetTemp" + tableRowID;
    minTemp = "minTemp" + tableRowID;
    maxTemp = "maxTemp" + tableRowID;
    tempUnit = "tempUnit" + tableRowID;
    nor = "nor" + tableRowID;
    owLeft = "owLeft" + tableRowID;
    dimUnit = "dimUnit" + tableRowID;
    owRight = "owRight" + tableRowID;
    oh = "oh" + tableRowID;
    olAft = "olAft" + tableRowID;
    olForward = "olForward" + tableRowID;
    hzdDiv = "hzdDiv" + tableRowID;
    refDiv = "refDiv" + tableRowID;
    oogDiv = "oogDiv" + tableRowID;
    refclass = "refclass" + tableRowID;
    oogclass = "oogclass" + tableRowID;
    hzdclass = "hzdclass" + tableRowID;
    oogflag = "oogflag" + tableRowID;

    hidden_oog = "hidden_oog" + tableRowID;
    hidden_hzd = "hidden_hzd" + tableRowID;
    hidden_rfr = "hidden_rfr" + tableRowID;
    hidden_soc = "hidden_soc" + tableRowID;
    comid = "comid" + tableRowID;
    conid = "conid" + tableRowID;

    humidityflag = "humidityflag" + tableRowID;
    humidity = "humidity" + tableRowID;
    drains = "drains" + tableRowID;
    co2 = "co2" + tableRowID;
    o2 = "o2" + tableRowID;

    ventilation = "ventilation" + tableRowID;
    insurance = "insurance" + tableRowID;
    count_row = Integer.parseInt(tableRowID);
    if (count_row % 2 == 0) {
        str_row = "info";
    } else {
        str_row = "active";
    }


%>
<div id="<%=tableRowID%>" class="preview <%=str_row%>">
    <span  class="rowidchange" id="rowidchange<%=tableRowID%>" style="display: none"><%=tableRowID%></span>
    <input type="hidden" id="cargocount" name="cargocount" value="<%=tableRowID%>"/>
    <input type="hidden" id="<%=hidden_oog%>" name="<%=hidden_oog%>" value="N"/>
    <input type="hidden" id="<%=hidden_lcl%>" name="<%=hidden_lcl%>"  value="N"/>
    <input type="hidden" id="<%=hidden_hzd%>" name="<%=hidden_hzd%>"  value="N"/>
    <input type="hidden" id="<%=hidden_rfr%>" name="<%=hidden_rfr%>" value="N"/>
    <input type="hidden" id="<%=comid%>" name="<%=comid%>" value=""/>
    <input type="hidden" id="<%=conid%>" name="<%=conid%>" value=""/>
    <input type="hidden" id="<%=hiddencomid%>" name="<%=hiddencomid%>"/>
    <input type="hidden" id="<%=hiddenpackid%>" name="<%=hiddenpackid%>"/>
    <input type="hidden" id="<%=hiddenhscodeid%>" name="<%=hiddenhscodeid%>"/>
    <input type="hidden" id="<%=hdcommoidtycode%>" name="<%=hdcommoidtycode%>"/>
    <div class="txt_box_book">

        <a herf='javascript:void(0)' onclick="deletecargo('datatablebycargo', this)" class="delete_cargo float-right"> <i class="fa fa-times-circle fa-2x"></i>  </a>

        <div class="form-group">
            <div class="row">

                <div class="col-xs-12 col-sm-4 ecom_padding_right">
                    <%if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {%>
                    <label></label>
                    <%}%>
                    <select onchange="setEqptype('<%=eqpType%>', this);
                            setDGValues('<%=tableRowID%>');
                            getTareweight(this.value, '<%=tableRowID%>');
                            changeEqpSizeToHaulage(this);" class="form-control required eqpsize light_border" id="<%=eqpSize%>" name="<%=eqpSize%>" >
                        <option value="">Select Eqp Size</option>
                    </select>
                    <input type="hidden" onchange="cargoselectmethod(this, '<%=tableRowID%>');
                            /*getEqpList_new(this.value, '<%=eqpSize%>');*/" id="<%=eqpType%>" name="<%=eqpType%>">
                </div>

                <div class="col-xs-12 col-sm-4 ">
                    <%if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {%>
                    <label></label>
                    <%}%>
                    <div class="inc_dec">
                        <span style="position: relative; top: 8px">
                            <bean:message key='ebooking.trust.eqpmentqty' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                        </span>
                        <span class="float-right align_plus_minus" >
                            <div class="value-button" id="decrease" onclick="decreaseValue(this)" value="Decrease Value">-</div>
                            <input type="number" class="number required zerocheck numbervalpoint number_val_cls checkeqpquantity" value="0" id="<%=eqpQty%>" name="<%=eqpQty%>" onchange="removeHaulageDetailsOnQuantity(this);">
                            <div class="value-button" id="increase" onclick="increaseValue(this)" value="Increase Value">+</div>
                        </span>	
                        <input type="hidden" id="eqpquantity" class="eqpquantity" value="0">
                    </div>
                </div>

                <%if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {%>

                <div class="col-xs-12 col-sm-1 text-center" style="margin-left: 40px;">
                    <label for="<%=soc%>" class="label_switch" style="margin-bottom: 4px;"> <bean:message key='ebooking.soc.text.display' bundle="<%=clientcode%>"/> </label>
                    <label class="switch">
                        <input type="checkbox" value="N" id="<%=soc%>" name="<%=soc%>" onchange="enableEqpStatus('<%=eqpStatus%>', this);" />
                        <span class="slider round"></span>
                    </label>

                </div>

                <div class="col-xs-12 col-sm-1 text-center" style="margin-left: 8px;">

                    <label  for="<%=oogflag%>" class="label_switch" style="margin-bottom: 4px;" > <bean:message key='ebooking.oog.text.display' bundle="<%=clientcode%>"/> </label>
                    <label class="switch" >
                        <input type="checkbox" value="N"   class="switch-button oogswitchbtn"  id="<%=oogflag%>" name="<%=oogflag%>" disabled="true" onchange="enableMeasurement(this, '<%=tableRowID%>');" />
                        <span class="slider round"></span>
                    </label> 
                </div>

                <div class="col-xs-12 col-sm-1 text-center" style="margin-left: 8px;padding: 0;">
                    <label for="<%=hzd%>" class="label_switch" style="margin-bottom: 4px;margin-left: 10%;"> <bean:message key='ebooking.hazardous.display' bundle="<%=clientcode%>"/> </label>
                    <label class="switch">
                        <input type="checkbox" value="N"   onchange="cargoselectmethod_hzd(this, '<%=tableRowID%>');
                                setDGValues('<%=tableRowID%>');" class="checkhazardous" id="<%=hzd%>" name="<%=hzd%>"  />
                        <span class="slider round"></span>
                    </label>
                </div>
                <%} else {%>
                <div class="col-xs-12 col-sm-2 text-center">
                    <label for="<%=soc%>" class="label_switch" > <bean:message key='ebooking.soc.text.display' bundle="<%=clientcode%>"/> </label>
                    <label class="switch">
                        <input type="checkbox" value="N" id="<%=soc%>" name="<%=soc%>" onchange="enableEqpStatus('<%=eqpStatus%>', this);" />
                        <span class="slider round"></span>
                    </label>

                </div>

                <div class="col-xs-12 col-sm-2 text-right">
                    <label for="<%=hzd%>" class="label_switch" > <bean:message key='ebooking.hazardous.display' bundle="<%=clientcode%>"/> </label>
                    <label class="switch">
                        <input type="checkbox" value="N"  onchange="cargoselectmethod_hzd(this, '<%=tableRowID%>');
                                setDGValues('<%=tableRowID%>');" class="checkhazardous" id="<%=hzd%>" name="<%=hzd%>" />
                        <span class="slider round"></span>
                    </label>
                </div>
                <%}%>
            </div>
        </div>

        <div class="form-group">
            <div class="row">

                <div class="col-xs-12 col-sm-4 ecom_padding_right">
                    <bean:message key='ebooking.eqpstatus.display' bundle="<%=clientcode%>"/>
                    <select class="form-control light_border eqpstatusclass" id="<%=eqpStatus%>" name="<%=eqpStatus%>" onchange="equipchange('<%=tableRowID%>', this.value)" disabled="true">
                        <option value="F">Laden</option>
                        <option value="M">Empty</option>
                    </select>
                    <!--<div class="inc_dec">
                        <span style="position: relative; top: 8px">
                            <bean1:message key='ebooking.eqpstatus.display' bundle="<%=clientcode%>"/>
                        </span>
                        <div class = "vertical_line"></div> 
                        <select class="form-control col-sm-6 txt_select_weight eqpstatusclass" id="<%=eqpStatus%>" name="<%=eqpStatus%>" onchange="equipchange('<%=tableRowID%>', this.value)" disabled="true">
                            <option value="F">Laden</option>
                            <option value="M">Empty</option>
                        </select>
                    </div>-->
                </div>

                <div class="col-xs-12 col-sm-4">
                    <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                    <bean:message key="shipping.type.display" bundle="<%=clientcode%>"/>
                    <%} else {%>
                    <bean:message key="shipping.packagetype.display" bundle="<%=clientcode%>"/>
                    <%}%>
                    <input type="text" class="form-control light_border  packtypehead autocompletetmsgfortype" id="<%=packagename%>" name="<%=packagename%>">
                    <label style="display:none;color:#a94442" class="comclass" id='<%=packagenameport%>'>Invalid Package</label>
                </div>

                <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                <div class="col-xs-12 col-sm-4 ">
                    <bean:message key="shipping.unit.display" bundle="<%=clientcode%>"/>
                    <input type="text" class="form-control light_border number zerocheck numbervalpoint" id="<%=packageQty%>" name="<%=packageQty%>" maxlength="8">
                </div>
                <%} else {%>
                <div class="col-xs-12 col-sm-4 ">
                    <bean:message key="shipping.quantity.display" bundle="<%=clientcode%>"/>
                    <input type="text" class="form-control light_border number zerocheck numbervalpoint" id="<%=packageQty%>" name="<%=packageQty%>" maxlength="8">
                </div>
                <%}%>

            </div>
        </div>
        <div class="form-group">
            <div class="row">

                <div class="col-xs-12 col-sm-4 ecom_padding_right">
                    <bean:message key='ebooking.tareweight.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                    <input type="text" class="form-control light_border required" readonly="readonly" id="<%=tareWt%>" name="<%=tareWt%>"  value="0"/>
                </div>

                <div class="col-xs-12 col-sm-4 ">
                    <bean:message key='ebooking.cargoweight.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span>
                    <div class="inc_dec">
                        <span style="position: relative; top: 8px">
                            <input type="text" class="cargo_weight number required grossWeightCheck <%=cargoweightcheck%> cargweightmsg" id="<%=cargoWt%>" name="<%=cargoWt%>" onblur="cargoweightcalc('<%=tableRowID%>');
                                    PayLoadValidation('<%=cargoWt%>', '<%=grossWt%>', '<%=cargoWtUnit%>', '<%=eqpQty%>', '<%=eqpSize%>');" maxlength="9" onkeypress="return ValDigit(event, this);" onkeyup="checkvalue(event, this.value, '<%=cargoWt%>');">

                        </span>

                        <div class = "vertical_line"></div> 
                        <input type="hidden" id="<%=cargoWtUnit%>_previous" name="<%=cargoWtUnit%>_previous" value="K" />
                        <select class="form-control col-sm-5 txt_select_weight" id="<%=cargoWtUnit%>" name="<%=cargoWtUnit%>" onchange="changeCargoWT('<%=tableRowID%>', this.value, 'new');
                                grossweightValidation();">
                            <option value="K">Kilos</option>
                            <%if ("N".equalsIgnoreCase(weight_unit_kilos_only)) {%>
                            <!--                            <option value="T">Metric Tones</option>-->
                            <option value="P">Pounds</option>
                            <%}%>
                        </select>
                    </div>
                </div>

                <div class="col-xs-12 col-sm-4">
                    <bean:message key='ebooking.grossweight.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span>
                    <input type="text" class="form-control number required light_border" readonly="readonly" id="<%=grossWt%>" name="<%=grossWt%>" value="0">
                    <input type="hidden" id="<%=grossWtUnit%>" name="<%=grossWtUnit%>">
                </div>

            </div>
        </div>
        <div class="form-group">
            <div class="row">
                <div class="col-xs-12 col-sm-4 ecom_padding_right">
                    <bean:message key='ecom.cargo.netweight' bundle="<%=clientcode%>"/>
                    <div class="inc_dec">
                        <span style="position: relative; top: 8px">
                            <input type="text" class="cargo_weight number  grossWeightCheck <%=netweightcheck%>" id="<%=netWt%>" name="<%=netWt%>" placeholder=""  maxlength="9" onkeypress="return ValDigit(event, this);" onkeyup="checkvalue(event, this.value, '<%=netWt%>');">
                        </span>
                    </div>
                </div>

                <div class="col-xs-12 col-sm-4">
                    <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                    <bean:message key='ebooking.trust.commoditygrp' bundle="<%=clientcode%>"/>
                    <%} else {%>
                    <bean:message key='ebooking.trust.commodity' bundle="<%=clientcode%>"/>
                    <%}%>
                    <%if ("Y".equalsIgnoreCase(CommodityName_as_HS_Chapter)) {%><bean:message key="ecom.si.hs.Chapter" bundle="<%=clientcode%>"/><%}%> <span class="mandatory">*</span>
                    <input type="hidden" id="<%=commodityothers%>" name="<%=commodityothers%>" value="N" />
                    <input type="text" class="form-control  <%=commodityclass%> commoditytypehead required light_border autocompletetmsg" aria-label="..." id="<%=commodity%>" name="<%=commodity%>" placeholder="<bean:message key="bl.commodity.display" bundle="<%=clientcode%>"/>">
                    <label style="display:none;color:#a94442" class="comclass" id='<%=commodityport%>'>Invalid Commodity</label>
                    <input type="text" class="form-control  <%=othercommodity%> light_border"  id="<%=othercommodity%>"  name="<%=othercommodity%>" style="display:none" maxlength="100">
                </div>


                <div class="col-xs-12 col-sm-4 input_type_width">
                    <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {
                        hscodeclass =hscodeclass+ " required";
                        %>
                    <bean:message key='ebooking.trust.commodity' bundle="<%=clientcode%>"/><span class="mandatory"> * </span>
                    <%} else if ("Y".equalsIgnoreCase(hsnamecode)) {%>
                    <bean:message key='ebooking.trust.hsnamecode' bundle="<%=clientcode%>"/>
                    <%} else {%>
                    <bean:message key='ebooking.trust.hscode' bundle="<%=clientcode%>"/>
                    <%}%>
                    <div class="input-group">
                        <input type="text" class="form-control light_border <%=hscodeclass%> hscodetypehead hscodemandatoryclass autocompletetmsg"  aria-label="..." id="<%=hscode%>" name="<%=hscode%>">
                        <input type="text" class="form-control light_border <%=otherhscode%> hscodemandatoryclass" id="<%=otherhscode%>" name="<%=otherhscode%>" style="display:none" maxlength="100">
                        <div class="input-group-addon group_search ">
                            <span class="input-group-text"  onclick="callCommoditySearch('<%=commodity%>', '<%=hscode%>', '<%=tableRowID%>');" >
                                <i class="fa fa-search"></i>
                            </span>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <div class="form-group">
            <div class="row">
                <div class="col-sm-2 ecom_padding_right">
                    <%if ("Y".equalsIgnoreCase(bookingvolumeMandatory)) {%>
                    <bean:message key='ebooking.containervolume.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span>
                    <input type="text" class="form-control light_border number required <%=volumeweightcheck%>" id="<%=volume%>" name="<%=volume%>" maxlength="8" onkeypress="return ValDigit(event, this);" onkeyup="checkvalue(event, this.value, '<%=volume%>');">
                    <%} else {%>
                    <bean:message key='ebooking.containervolume.display' bundle="<%=clientcode%>"/>
                    <input type="text" class="form-control light_border number <%=volumeweightcheck%>" id="<%=volume%>" name="<%=volume%>" maxlength="8" onkeypress="return ValDigit(event, this);" onkeyup="checkvalue(event, this.value, '<%=volume%>');">
                    <%}%>
                </div>
                <div class="col-sm-2 ecom_padding_left">
                    <label></label>
                    <select class="form-control light_border" id="<%=volUnit%>" name="<%=volUnit%>">
                        <option value="M">Cubic Meter</option>
                        <option value="F">Cubic Feet</option>
                    </select>
                </div>
                <%if ("Y".equalsIgnoreCase(enableActCommoName)) {%>
                <div class="col-xs-12 col-sm-4">
                    <bean:message key="shipping.actcommoname.display" bundle="<%=clientcode%>"/><span class="mandatory">*</span>
                    <input type="text" class="form-control light_border required" id="<%=actCommoName%>" name="<%=actCommoName%>" maxlength="100">
                </div>
                <%}%>
                 <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
                <div class="col-xs-12 col-sm-4">
                   <bean:message key='ebooking.cmdtyhblno.display' bundle="<%=clientcode%>"/>
                    <input type="text" class="form-control light_border " id="<%=cmdtyhblno%>" name="<%=cmdtyhblno%>" maxlength="20">
                </div>
                <%}%>
                <%if ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(insuranceaccess)) {%>
                <input type="hidden" class="inquotecusttype" id="<%=insurance + "_custtype"%>" >
                <input type="hidden" class="inquoteno" id="<%=insurance + "_quoteno"%>" >
                <input type="hidden" class="incargoval" id="<%=insurance + "_cargoval"%>" >
                <input type="hidden" class="inavailable" id="<%=insurance + "_available"%>" value="<%=(Validation.checkNull(quoteno).length() > 0 ? "Y" : "N")%>" >

                <!--                <div class="col-xs-12 col-sm-2 text-center" style="top: 20px;">
                
                                    <a href="javascript:void(0)" onclick="loadinsurancepage($('#<=insurance + "_quoteno"%>').val(), $('#<=insurance + "_custtype"%>').val())"  class="btn btn-info btn-md addinsurance" id="addinsurance"><i class=""></i> <bea:message key="booking.btn.cargo.insurance" bundle="<=clientcode%>"/> </a>
                                </div>
                                <div class="col-xs-12 col-sm-2 text-center" style="top: 20px;">
                                    <label for="<=insurance%>" class="label_switch"> <bea:message key="booking.cargo.insurance" bundle="<clientcode%>"/> </label>
                                    <label class="switch">
                                        <input type="checkbox" value="N" id="<=insurance%>" name="<=insurance%>" class="insuranceswitchbtn" onchange="" disabled="disabled" />
                                        <span class="slider round"></span>
                                    </label>
                                </div>-->
                <%}%>
            </div>
        </div>
        <%if ("Y".equalsIgnoreCase(TENS_Documentation)) {%>
        <div class="form-group">
            <div class="row">
                  <div class="col-xs-12 col-sm-4">
               <bean:message key='ebooking.sellersName.display' bundle="<%=clientcode%>"/>
                 <select  class="form-control input-sm setdata  light_border" id="<%=sellerSelect%>" name="sellerNameList" onchange="setCustNameCode(this,'<%=sellerCode%>','<%=sellersName%>')">
                        <option value="">Select Customer</option>
                    </select>
                     <input type="hidden" id="<%=sellerCode%>" name="<%=sellerCode%>"  />
                     <input type="hidden" id="<%=sellersName%>" name="<%=sellersName%>"  />
                </div>
                  <div class="col-xs-12 col-sm-4">
                  <bean:message key='ebooking.buyersName.display' bundle="<%=clientcode%>"/>
                   <select  class="form-control input-sm setdata light_border" id="<%=buyerSelect%>" name="buyerNameList"  onchange="setCustNameCode(this,'<%=buyersCode%>','<%=buyersName%>')" )>
                        <option value="">Select Customer</option>
                    </select>
                    <input type="hidden" id="<%=buyersCode%>" name="<%=buyersCode%>" />
                    <input type="hidden" id="<%=buyersName%>" name="<%=buyersName%>"  />
                </div>
                </div></div>
           <%}%>
        <div class="hazardous" id="<%=hzdDiv%>" style="display:none">
            <div class = "horizontal_line"></div>
            <!-- Hidden field for DG count -->
            <input type="hidden" id="dgCount<%=tableRowID%>" name="dgCount<%=tableRowID%>" value="1">

            <!-- DG Container - Always uses _1 suffix (unified for single & multiple) -->
            <div id="dgContainer<%=tableRowID%>">
                <div class="dg-entry" id="dgEntry<%=tableRowID%>_1">
                    <div class="form-group row">
                        <div class="col-sm-4">
                            <label><bean:message key='ebooking.marine.pollut' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                            <select  class="form-control input-sm setdata <%=hzdclass%> light_border" id="<%=marinePollut%>_1" name="<%=marinePollut%>_1">
                                <option value="N">No</option>
                                <option value="Y">Yes</option>
                            </select>
                        </div>

                        <div class="col-sm-4">
                            <label><bean:message key='ebooking.UN.no' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                            <input type="text" class="form-control input-sm setdata <%=hzdclass%> light_border" id="<%=unno%>_1" name="<%=unno%>_1" maxlength="600">
                        </div>

                        <div class="col-sm-2 ecom_padding_right">
                            <label><bean:message key='ebooking.flash.point' bundle="<%=clientcode%>"/></label>
                            <input type="text" maxlength="8" value="" class="form-control input-sm number setdata light_border" id="<%=flashPoint%>_1" name="<%=flashPoint%>_1">
                        </div>
                        <div class="col-sm-2 ecom_padding_left">
                            <label></label>
                            <select  class="form-control input-sm setdata light_border" id="<%=flashUnit%>_1" name="<%=flashUnit%>_1">
                                <option value="C">Celsius</option>
                                <option value="F">Fahrenheit</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-4">
                            <label><bean:message key='ebooking.DG.class' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                            <input type="text" class="form-control input-sm setdata light_border" id="<%=dgClass%>_1" name="<%=dgClass%>_1" readonly>
                        </div>

                        <div class="col-sm-4">
                            <label><bean:message key='ebooking.shipping.name' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                            <input type="text" class="form-control input-sm setdata light_border" id="<%=shippingName%>_1" name="<%=shippingName%>_1" readonly>
                        </div>
                        
                        <%
                        // Show Add/Remove buttons if plugin is enabled
                        if ("Y".equalsIgnoreCase(isMultipleDGEnabled)) {
                        %>
                        <div class="col-sm-3">
                            <button type="button" class="btn btn-sm btn-success" onclick="addDGRow(<%=tableRowID%>)" title="Add DG Entry" style="margin-top: 22px; margin-right: 5px;">
                                <i class="fa fa-plus"></i>
                            </button>
                            <button type="button" class="btn btn-sm btn-danger" onclick="removeDGRow(this, <%=tableRowID%>)" title="Remove DG Entry" style="margin-top: 22px;">
                                <i class="fa fa-minus"></i>
                            </button>
                        </div>
                        <%
                        }
                        %>
                    </div>
                </div>
            </div>
        </div>
        <div class="opencar" id="<%=oogDiv%>"  style="display:none">
            <div class = "horizontal_line"></div> 
            <div class="form-group row">

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.dimensionunit.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <select  class="form-control input-sm setdata <%=oogclass%> light_border" id="<%=dimUnit%>" name="<%=dimUnit%>">
                        <option value="M">Meters</option>
                        <option value="F">Feet</option>
                        <option value="C">Centimeters</option>
                    </select>
                </div>

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.dimensionleft.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <input type="text"  maxlength="6" value=""  class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border" id="<%=owLeft%>" name="<%=owLeft%>">
                </div>

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.dimensionright.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <input type="text"  maxlength="6"  class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border" id="<%=owRight%>" name="<%=owRight%>">
                </div>
            </div>
            <div class="form-group row">

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.dimensionforward.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <input type="text"  maxlength="6" value=""  class="form-control number input-sm setdata validatezeropoint <%=oogclass%> light_border" id="<%=olForward%>" name="<%=olForward%>">
                </div>


                <div class="col-sm-4">
                    <label><bean:message key='ebooking.dimensionalt.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <input type="text"  maxlength="6" value=""  class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border" id="<%=olAft%>" name="<%=olAft%>">
                </div>


                <div class="col-sm-4">
                    <label><bean:message key='ebooking.dimensiontop.display' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <input type="text"  maxlength="6" value="" class="form-control input-sm number setdata validatezeropoint <%=oogclass%> light_border" id="<%=oh%>" name="<%=oh%>">
                </div>
            </div>
        </div>
        <div class="reefer" id="<%=refDiv%>" style="display:none">
            <div class = "horizontal_line"></div> 
            <div class="form-group row">

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.NOR.display' bundle="<%=clientcode%>"/> <span class="mandatory">*</span></label>
                    <select  onChange="setTempSetEnble('<%=tableRowID%>');"  class="form-control input-sm setdata <%=refclass%> light_border" id="<%=nor%>" name="<%=nor%>">
                        <option value="N">No</option>
                        <option value="Y">Yes</option>
                    </select>
                </div>

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.preset.temp' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <input type="text"  class="form-control input-sm setdata <%=refclass%> settempclass light_border" id="<%=presetTemp%>" name="<%=presetTemp%>" onkeypress="return ValDigittemp(event, this);" onblur="moneyFormat(this)">
                </div>

                <%if (!"Y".equalsIgnoreCase(hideMinMaxTemp)) {%>
                <div class="col-sm-4">
                    <label><bean:message key='ebooking.min.temp' bundle="<%=clientcode%>"/><%if("Y".equalsIgnoreCase(enable_Reefer_MixTemp_Req)){%><span class="mandatory">*</span><%}%></label>
                    <input type="text"  class="form-control input-sm setdata  mintempclass light_border <%= "Y".equalsIgnoreCase(enable_Reefer_MixTemp_Req) ? refclass : "" %>" id="<%=minTemp%>" name="<%=minTemp%>"  <%= "Y".equalsIgnoreCase(enable_Reefer_MixTemp_Req) ? "min=\"" + reefer_MinTemp_Limit + "\"" : "" %> onkeypress="return ValDigittemp(event, this);" onblur="moneyFormat(this)">
                </div>
            </div>
            <div class="form-group row">

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.max.temp' bundle="<%=clientcode%>"/> <%if("Y".equalsIgnoreCase(enable_Reefer_MixTemp_Req)){%><span class="mandatory">*</span><%}%></label>
                    <input type="text"  class="form-control input-sm setdata  maxtempclass light_border <%= "Y".equalsIgnoreCase(enable_Reefer_MixTemp_Req) ? refclass : "" %>" id="<%=maxTemp%>" name="<%=maxTemp%>"  <%= "Y".equalsIgnoreCase(enable_Reefer_MixTemp_Req) ? "max=\"" + reefer_MaxTemp_Limit + "\"" : "" %> onkeypress="return ValDigittemp(event, this);" onblur="moneyFormat(this)">
                </div>
                <%}%>
                <div class="col-sm-4">
                    <label><bean:message key='ebooking.temp.unit' bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                    <select  class="form-control input-sm setdata <%=refclass%> light_border" id="<%=tempUnit%>" name="<%=tempUnit%>">
                        <option value="C">Celsius</option>
                        <%if (!"GWF".equalsIgnoreCase(carrierCode)) {%>
                        <option value="F">Fahrenheit</option>
                        <%}%>
                    </select>
                </div>


                <%if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {%>

                <div class="col-sm-4">
                    <label id='<%=humidity + "_label"%>'><bean:message key='ebooking.humidity' bundle="<%=clientcode%>"/></label>
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">
                                <input type="checkbox" id="<%=humidityflag%>" name="<%=humidityflag%>" onchange="enableHumidity('<%=humidityflag%>', '<%=humidity%>')" value="Y">
                            </span>
                        </div>
                        <input type="text"  class="form-control input-sm setdata light_border" id="<%=humidity%>" name="<%=humidity%>" onkeyup="return validateHumidityDigit(this);" maxlength="5" onblur="removeHyphen(this)" disabled />
                    </div>
                </div>
                <%}%>
            </div>
            <%if ("Y".equalsIgnoreCase(Enable_Drains_Humidity)) {%>
            <div class="form-group row">

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.drains' bundle="<%=clientcode%>"/></label>
                    <select  class="form-control input-sm light_border" id="<%=drains%>" name="<%=drains%>">
                        <option value=""></option>
                        <option value="O">Open</option>
                        <option value="C">Closed</option>
                    </select>
                </div>

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.co2' bundle="<%=clientcode%>"/></label>
                    <input type="text"  class="form-control input-sm setdata light_border" id="<%=co2%>" name="<%=co2%>" onkeyup="return validateHumidityDigit(this);" maxlength="5" onblur="removeHyphen(this)"/>
                </div>

                <div class="col-sm-4">
                    <label><bean:message key='ebooking.o2' bundle="<%=clientcode%>"/></label>
                    <input type="text"  class="form-control input-sm setdata light_border" id="<%=o2%>" name="<%=o2%>" onkeyup="return validateHumidityDigit(this);" maxlength="5" onblur="removeHyphen(this)"/>
                </div>
            </div>

            <%}%>
            <div class="form-group row">
                <div class="col-sm-4">
                    <label><bean:message key='ebooking.vent' bundle="<%=clientcode%>"/><%= Ventilationlabel%></label>
                    <input type="text"  class="form-control input-sm setdata light_border" id="<%=ventilation%>" name="<%=ventilation%>" onkeyup="return validateHumidityDigit(this);" maxlength="5" onblur="removeHyphen(this)"/>
                </div>
            </div>
        </div>
    </div>

</div>

<script>
    <%if (!("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode))) {%>
    callHSCodeMandatoryScriptForId('<%=hscode%>', '<%=hzd%>');
    <%}%>
    var eqpval = $('#eqpType' +<%=tableRowID%>).val();
    getEqpList_new(eqpval, '<%=eqpSize%>');
    commodityload('<%=commodity%>', '<%=hiddencomid%>', '<%=tableRowID%>');
    //   hscommodityload('<%=hscode%>', '<%=hiddenhscodeid%>');
    packageload('<%=packagename%>', '<%=hiddenpackid%>');
    dgload('<%=unno%>_1', '<%=hiddencomid%>', '<%=dgClass%>_1', '<%=shippingName%>_1');
    hideV2OtherCommodity('<%=othersCmdty%>', 'othercmdtylable<%=tableRowID%>');
    $('[data-toggle="tooltip"]').tooltip();
    var eid = $('#eqpStatus' +<%=tableRowID%>).val();
    var cargoweightcheck = ".cargoweightcheck" +<%=tableRowID%>;
    var netweightcheck = ".netweightcheck" +<%=tableRowID%>;
    var volumeweightcheck = ".volumeweightcheck" +<%=tableRowID%>;
    if (eid == 'F') {
        $(cargoweightcheck).removeClass('valuezero');
        $(volumeweightcheck).removeClass('validatezeropoint');
        $(netweightcheck).removeClass('validatezeropoint');
        $(cargoweightcheck).addClass('validatepoint');
        $(volumeweightcheck).addClass('validatepoint');
        $(netweightcheck).addClass('validatepoint');
    }
    if (eid == 'M') {
        $(cargoweightcheck).removeClass('validatepoint');
        $(volumeweightcheck).removeClass('validatepoint');
        $(netweightcheck).removeClass('validatepoint');
        $(cargoweightcheck).addClass('valuezero');
        $(volumeweightcheck).addClass('validatezeropoint');
        $(netweightcheck).addClass('validatezeropoint');

    }
    // callHSCodeMandatoryScript();
    <%if ("Y".equalsIgnoreCase(HSCodeMandatory)) {%>
    $('#' + '<%=hscode%>').removeClass('required');
    $('#' + '<%=hscode%>').addClass('required');
    <%}%>
  setSellerNameSelectOption();
</script>
<%
    }
%>
<style>
    /*    #comdtydropdown .tt-dropdown-menu {
            max-height: 140px;
            overflow-y: auto;
        }
        table {
            border-spacing: 0px;
            border-collapse: inherit;
        }*/
    /**.table > tbody > tr.active > td, .table > tbody > tr.active > th, .table > tbody > tr > td.active, .table > tbody > tr > th.active, .table > tfoot > tr.active > td, .table > tfoot > tr.active > th, .table > tfoot > tr > td.active, .table > tfoot > tr > th.active, .table > thead > tr.active > td, .table > thead > tr.active > th, .table > thead > tr > td.active, .table > thead > tr > th.active {
        background-color: #F5F5F5 !important;
        border: 1px solid #CCC !important;
    }
    .table>tbody>tr.info>td, .table>tbody>tr.info>th, .table>tbody>tr>td.info, .table>tbody>tr>th.info, .table>tfoot>tr.info>td, .table>tfoot>tr.info>th, .table>tfoot>tr>td.info, .table>tfoot>tr>th.info, .table>thead>tr.info>td, .table>thead>tr.info>th, .table>thead>tr>td.info, .table>thead>tr>th.info {
        background-color: #d9edf7 !important;
        border: 1px solid #CCC !important;
    }**/
    <%if ("Y".equalsIgnoreCase(ENABLE_OOG_DETAILS)) {%>
    .label_switch {
        display: block !important;
    }
    <%}%>
</style>