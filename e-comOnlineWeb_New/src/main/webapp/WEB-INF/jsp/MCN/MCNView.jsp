<%-- 
    Document   : MCNView
    Created on : 10 Nov, 2023, 12:59:26 PM
    Author     : prakash.mani
--%>

<%@page contentType="text/html; charset=UTF-8" import="java.util.ArrayList, java.util.HashMap,java.util.Set,java.util.Hashtable,net.ecom.vo.SIDtableVO, net.ecom.vo.SIPartiesVO,net.ecom.vo.SIContainerVO,java.util.List,net.ecom.common.Validation,java.util.Map,net.ecom.vo.SIContainerDetailVO,net.ecom.vo.SIMasterVO,net.ecom.vo.SISealVO,net.ecom.vo.SIConatainerValueVO,net.ecom.common.utils.DateConversion_New,net.ecom.vo.ChargesVo,net.ecom.common.GeneralConf,net.ecom.common.NumberFormat"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
    session.setAttribute("SISEND", "SISend");
    String blno = (String) session.getAttribute("edit_blno") != null ? (String) session.getAttribute("edit_blno") : "";
    String clientcode = (String) session.getAttribute("clientcode");
    String agencyRefNo = (String) request.getAttribute("SIAGENCYREFNO") != null ? (String) request.getAttribute("SIAGENCYREFNO") : "";
    List custtypelist = (List) getServletContext().getAttribute(GeneralConf.CUSTTYPECONFIG);
    String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String backtodashboard = request.getParameter("backtodashboard");
    System.out.println("[ECOM-WAR] MCNView  BL backtodashboard ==> " + backtodashboard);
    String enableaesno = (String) getServletContext().getAttribute(GeneralConf.ENABLE_AES_NUMBER);
    String loadportcountry = (String) request.getAttribute("LoadportCountry");
    String SHOW_CHARGES_IN_SIBL = (String) getServletContext().getAttribute(GeneralConf.SHOW_CHARGES_IN_SIBL);
    List Chargedetails = (List) request.getAttribute("CHARGEDETAILS");
    String CommodityName_as_HS_Chapter = (String) getServletContext().getAttribute(GeneralConf.CommodityName_as_HS_Chapter);
    List EBK_pluginList = (List) getServletContext().getAttribute("EBK_PLUGIN_DETAILS");
    boolean is_SI_restriction = false;
    if (EBK_pluginList != null && EBK_pluginList.contains("ESP")) {
        is_SI_restriction = true;
    }
    String ENABLE_INSURANCE = (String) getServletContext().getAttribute(GeneralConf.ENABLE_INSURANCE);
    String insuranceaccess = (String) session.getAttribute("SESSION_InsuranceAccess");
    String isinsurance = null, quoteno = "", quotenostr = null;
    String Enable_BLType_Selection = (String) getServletContext().getAttribute(GeneralConf.Enable_BLType_Selection);

    String ENABLE_ECOMOOG_DETAILS = (String) getServletContext().getAttribute(GeneralConf.ENABLE_OOG_DETAILS);
    boolean is_SI_OOG = false;
    if (ENABLE_ECOMOOG_DETAILS != null && "Y".equalsIgnoreCase(ENABLE_ECOMOOG_DETAILS)) {
        is_SI_OOG = true;
    }
    List addInfoList = (List) getServletContext().getAttribute(GeneralConf.AdditionalInfoBkg);
    String hsnamecode = (String) getServletContext().getAttribute(GeneralConf.Display_HS_NAME_CODE);
    boolean is_SI_Addinfo = false;
    if (addInfoList != null && addInfoList.contains("ACD")) {
        is_SI_Addinfo = true;
    }
%>
<%! int tablerowcount = 0;%>
<style>
    /*    .tooltip-inner {
            background-color: #1FBBA6;
            color: #fff;
        }
    
        .tooltip.right .tooltip-arrow  {
            border-right-color: #1FBBA6;
            color: #fff;
        }
        textarea {
            resize: none;
        }*/
</style>
<style>
    /*    .form-control[readonly]{
            background-color: #fff;
        }
        textarea.form-control {
            height: auto !important;
        }*/
</style>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="bl.head.blview" bundle="<%=clientcode%>"/> <img class="back_btn" onclick="backtosiview()" src="Design/assets/images/page_icons/back_btn.svg" title="Back" /></div>

<div class="details_search page_bottom_content">
    <html:form method="post" action="SubmitSI.do" styleId="SIEditForm" styleClass="form-horizontal">
        <html:hidden   property="sino" styleId="sino" />
        <div class="card card-shadow">

            <div class="card-header preview_header"> <bean:message key='EBooking.customer' bundle="<%=clientcode%>"/></div>
            <div class="card-body">
                <div class="form-group">
                    <label class="col-sm-2 control-label"><bean:message key="bl.Bookingno" bundle="<%=clientcode%>"/> :   </label>

                    <label class="col-sm-2 control-label"> <%=(String) session.getAttribute("edit_bookno")%>  </label>

                    <%if (blno.length() > 0) {%>
                    <label class="col-sm-2 control-label"> <bean:message key="bl.blno" bundle="<%=clientcode%>"/> : </label>
                    <label class="col-sm-2 control-label"> <%=(String) session.getAttribute("edit_blno")%>   </label>
                    <%}%>
                </div>

                <div class="form-group">


                    <label class="col-sm-2 control-label"> <bean:message key="shipping.agency.refno" bundle="<%=clientcode%>"/> :  </label>

                    <label class="col-sm-2 control-label"><%=agencyRefNo%> </label>
                    <%if ("Y".equalsIgnoreCase(Enable_BLType_Selection)) {%>
                    <label class="col-sm-2 control-label"> <bean:message key="shipping.bl.type" bundle="<%=clientcode%>"/> : </label>
                    <label class="col-sm-3 control-label">
                        <html:text property="bltype" styleId="bltype" styleClass="form-control input-sm"  readonly="true"/>
                    </label>
                    <%}%>

                </div>
                <hr/>
                <div class="row">
                    <div class="col col-md-4">
                        <div id="shipper" class="panel_right">
                            <div class="custom-panel-head"><bean:message key="ebooking.shipper.display" bundle="<%=clientcode%>"/>  </div>     
                            <div class="row">
                                <label class="col-sm-8"> <bean:message key="shipping.printonbl" bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                            </div>

                            <div class="row">
                                <div class="col-sm-12 form-group">
                                    <html:text property="spnameheader" styleId="ebook_shippernameheader" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    <html:textarea styleId="headershipper" property="headerShipper"  styleClass="form-control required" rows="5" readonly="true">
                                    </html:textarea>
                                </div>
                            </div>
                        </div>
                    </div>



                    <div class="col-md-4">

                        <div class="custom-panel-head"><bean:message key="ebooking.consignee.display" bundle="<%=clientcode%>"/> </div>

                        <div class="row">

                            <label class="col-sm-8"> <bean:message key="shipping.printonbl" bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>

                        </div>

                        <div class="row">
                            <div class="col-sm-12 form-group">
                                <html:text  property="consnameheader" styleId="ebook_consigneenameheader" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-12">
                                <html:textarea styleId="headerconsignee" property="headerConsignee"  styleClass="form-control required" rows="5" readonly="true">
                                </html:textarea>
                            </div>
                        </div>


                    </div>


                    <div class="col-md-4 ">

                        <div class="custom-panel-head"> <bean:message key="ebooking.notify.display" bundle="<%=clientcode%>"/>
                        </div>

                        <div class="row">

                            <label class="col-sm-8"> <bean:message key="shipping.printonbl" bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>

                        </div>

                        <div class="row">
                            <div class="col-sm-12 form-group">
                                <html:text  property="npnameheader" styleId="ebook_notifynameheader" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-12">
                                <html:textarea styleId="headernotify" property="headerNotify"  styleClass="form-control required" rows="5" readonly="true">
                                </html:textarea>
                            </div>
                        </div>

                    </div>
                </div>
                <% if (custtypelist.contains("N1")) {%>

                <div class="row">

                    <div class="col-md-12">
                        <div class="normalpanel" id="nt_id2">

                            <div class="custom-panel-head"> <bean:message key="Booking.party.notify1" bundle="<%=clientcode%>"/>
                                <span class="pull-right"><a data-toggle="collapse" href="#collapseNotify2" aria-expanded="false" aria-controls="collapseNotify2"><i class="fa fa-plus-square-o"></i></a></span> </div>
                        </div>
                        <div class="collapse" id="collapseNotify2">
                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np2name" styleId="ebook_notifyname2" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np2addr" styleId="ebook_notifyaddress2" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                <div class="col-sm-3">

                                    <html:text  property="np2addr1" styleId="ebook_notifyaddress2" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>
                            </div>

                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text property="np2city" styleId="ebook_notifycity2"  styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np2CountryName" styleId="ebook_notifycountry_name2" styleClass="form-control required input-sm" readonly="true" />
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="np2phone" styleId="ebook_notifyphone2" styleClass="form-control input-sm" maxlength="20" readonly="true"/>

                                </div>
                            </div>


                            <div class="row form-group">


                                <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np2fax" styleId="ebook_notifyfax2" styleClass="form-control input-sm" maxlength="20" readonly="true"/>

                                </div>
                                <%}%>

                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text size="43" property="np2email" styleId="ebook_notifyemail2" styleClass="form-control email input-sm" maxlength="75" readonly="true"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text  property="np2state" styleId="ebook_notifystate2" styleClass="form-control email input-sm" maxlength="40" readonly="true"/>

                                </div>

                            </div>
                        </div>
                    </div>
                </div>
                <%}%>
                <% if (custtypelist.contains("N2")) {%>

                <div class="row">

                    <div class="col-md-12">
                        <div class="normalpanel" id="nt_id3">

                            <div class="custom-panel-head"> <bean:message key="Booking.party.notify2" bundle="<%=clientcode%>"/><span class="pull-right"><a data-toggle="collapse" href="#collapseNotify3" aria-expanded="false" aria-controls="collapseNotify3"><i class="fa fa-plus-square-o"></i></a></span> </div></div>
                        <div class="collapse" id="collapseNotify3">
                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np3name" styleId="ebook_notifyname3" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np3addr" styleId="ebook_notifyaddress3" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                <div class="col-sm-3">
                                    <html:text  property="np3addr1" styleId="ebook_notifyaddress3" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>

                            </div>

                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text property="np3city" styleId="ebook_notifycity3"  styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np3CountryName" styleId="ebook_notifycountry_name3" styleClass="form-control required input-sm" readonly="true" />
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="np3phone" styleId="ebook_notifyphone3" styleClass="form-control input-sm" maxlength="20" readonly="true"/>
                                </div>
                            </div>


                            <div class="row form-group">
                                <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="np3fax" styleId="ebook_notifyfax3" styleClass="form-control input-sm" maxlength="20" readonly="true"/>
                                </div>
                                <%}%>

                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text size="43" property="np3email" styleId="ebook_notifyemail3" styleClass="form-control email input-sm" maxlength="75" readonly="true"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text  property="np3state" styleId="ebook_notifystate3" styleClass="form-control email input-sm" maxlength="40" readonly="true"/>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%}%>
                <% if (custtypelist.contains("FW")) {%>

                <div class="row">

                    <div class="col-md-12">
                        <div class="normalpanel" id="fw_id">

                            <div class="custom-panel-head"> <bean:message key="ecom.bkg.custfw" bundle="<%=clientcode%>"/>
                                <span class="pull-right"><a data-toggle="collapse" href="#collapseForwarder" aria-expanded="false" aria-controls="collapseForwarder"><i class="fa fa-plus-square-o"></i></a></span> </div>
                        </div>
                        <div class="collapse" id="collapseForwarder">
                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="fwname" styleId="ebook_forwardername" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="fwaddr" styleId="ebook_forwarderaddress" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>

                                <div class="col-sm-3">

                                    <html:text  property="fwaddr1" styleId="ebook_forwarderaddress1" styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>
                            </div>

                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text property="fwcity" styleId="ebook_forwardercity"  styleClass="form-control required input-sm" maxlength="150" readonly="true"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="fwCountryName" styleId="ebook_forwardercountry_name" styleClass="form-control required input-sm" readonly="true" />
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="fwphone" styleId="ebook_forwarderphone2" styleClass="form-control input-sm" maxlength="20" readonly="true"/>

                                </div>

                            </div>


                            <div class="row form-group">

                                <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="fwfax" styleId="ebook_forwarderfax" styleClass="form-control input-sm" maxlength="20" readonly="true"/>

                                </div>
                                <%}%>

                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text size="43" property="fwemail" styleId="ebook_forwarderemail" styleClass="form-control email input-sm" maxlength="75" readonly="true"/>

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text  property="fwstate" styleId="ebook_forwarderstate3" styleClass="form-control email input-sm" maxlength="40" readonly="true"/>

                                </div>

                            </div>
                        </div>
                    </div>
                </div>
                <%}%>
            </div>

        </div>
    </div>
    <div class="card card-shadow">

        <div class="card-header preview_header"> <bean:message key="ecom.shippingins.headinfo" bundle="<%=clientcode%>"/>  </div>
        <div class="card-body">
            <div class="row form-group">

                <label class="col-sm-2 control-label"> <bean:message key="Tracking.origin" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                <div class="col-sm-4 ">
                    <html:text property="hid_pla_free_txt" styleId="hid_pla_free_txt" readonly="true"  styleClass="form-control required input-sm" maxlength="149"/>

                </div>

                <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                <div class="col-sm-4 ">
                    <html:text property="pla_free_txt" styleId="pla_free_txt"  styleClass="form-control required input-sm" maxlength="149" readonly="true"/>

                </div>

            </div>

            <div class="row form-group">

                <label class="col-sm-2 control-label"> <bean:message key="shipping.portofload.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                <div class="col-sm-4 ">
                    <html:text property="hid_pol_free_txt" styleId="hid_pol_free_txt" readonly="true"  styleClass="form-control required input-sm" maxlength="149"/>
                </div>

                <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                <div class="col-sm-4 ">
                    <html:text property="pol_free_txt" styleId="pol_free_txt"  styleClass="form-control required input-sm" maxlength="149" readonly="true"/>

                </div>

            </div>


            <div class="row form-group">

                <label class="col-sm-2 control-label"> <bean:message key="shipping.portofdischarge.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                <div class="col-sm-4 ">
                    <html:text property="hid_pod_free_txt" styleId="hid_pod_free_txt" readonly="true" styleClass="form-control required input-sm" maxlength="149"/>

                </div>

                <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                <div class="col-sm-4 ">

                    <html:text property="pod_free_txt" styleId="pod_free_txt" styleClass="form-control required input-sm" maxlength="149" readonly="true"/>

                </div>

            </div>


            <div class="row form-group">

                <label class="col-sm-2 control-label"> <bean:message key="Tracking.delivery" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                <div class="col-sm-4 ">
                    <html:text property="hid_pld_free_txt" styleId="hid_pld_free_txt" readonly="true" styleClass="form-control required input-sm" maxlength="149"/>
                </div>

                <label class="col-sm-2 control-label"> <bean:message key="shipping.printbl.display" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                <div class="col-sm-4 ">
                    <html:text property="pld_free_txt" styleId="pld_free_txt" styleClass="form-control required input-sm" maxlength="149" readonly="true"/>
                    <html:hidden property="vslvoyage_free_txt" styleId="vslvoyage_free_txt"></html:hidden>
                    </div>

                </div>
                <div class="row form-group">

                    <div class="heading_trainee_bottom_booking ">  </div>

                    <div class="heading_trainee_booking "> <bean:message key="Booking.head.vesselschddet" bundle="<%=clientcode%>"/> </div>

            </div>
            <div id="siroutingdetailspreview">
                <%
                    int transittime = 0;
                    HashMap hm_routing = (HashMap) session.getAttribute("bk_port_details");
                    SIPartiesVO parties_routing = (SIPartiesVO) hm_routing.get("routing");
                    if (parties_routing != null) {
                        transittime = DateConversion_New.daysDifferentBetweenTwoDays(parties_routing.getPolat(), parties_routing.getPodat());
                %>
                <div id="siroutingdetails">
                    <div id="routingdetails">
                        <div class="form-group">
                            <div class="row">

                                <div class="col-xs-12 col-sm-12">

                                    <div class="panel_inner_left txt_box">
                                        <div class="panel_inner_top">
                                            <div class="panel_inner_top_1">
                                                <table>
                                                    <tr>
                                                        <td><img class="header_icon" src="Design/assets/images/page_icons/marker.svg" /></td>
                                                        <td>
                                                    <dt>
                                                    <dd><bean:message key="ecom.schdl.dept" bundle="<%=clientcode%>"/></dd>
                                                    <dd><strong><%=parties_routing.getPolat()%></strong></dd>
                                                    <dd><%=parties_routing.getReceipt()%></dd>
                                                    </dt>
                                                    </td>
                                                    <td><img class="content_icon" src="Design/assets/images/page_icons/Rvessel.svg" /></td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="panel_inner_top_2">
                                                <table>
                                                    <tr>
                                                        <td><img class="header_icon" src="Design/assets/images/page_icons/marker.svg" /></td>
                                                        <td>
                                                    <dt>
                                                    <dd><bean:message key="ecom.schdl.arrv" bundle="<%=clientcode%>"/></dd>
                                                    <dd><strong><%=parties_routing.getPodat()%></strong></dd>
                                                    <dd><%=parties_routing.getDelivery()%></dd>
                                                    </dt>
                                                    </td>
                                                    <td><img class="content_icon" src="Design/assets/images/page_icons/Lvessel.svg" /></td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="panel_inner_top_3 vessel_info">
                                                <table>
                                                    <tr>
                                                        <td><img class="header_icon" src="Design/assets/images/page_icons/vessel.svg" /></td>
                                                        <td>
                                                    <dt>
                                                    <dd><bean:message key="Schd.vesselname" bundle="<%=clientcode%>"/></dd>
                                                    <dd><strong><%=parties_routing.getVesselName()%></strong></dd>
                                                    <dd><%=parties_routing.getVoyagecode()%><%=parties_routing.getBound()%></dd>
                                                    </dt>
                                                    </td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="panel_inner_top_4" style="width: 20%;">
                                                <table>
                                                    <tr>
                                                        <td><img class="header_icon" src="Design/assets/images/page_icons/clock.svg" /></td>
                                                        <td>
                                                    <dt>
                                                    <dd><bean:message key="ecom.schdl.ttime" bundle="<%=clientcode%>"/> : <strong><%=transittime%> days</strong></dd>
                                                    <dd><bean:message key="ebooking.Haulage.Origin" bundle="<%=clientcode%>"/> : <strong><%=parties_routing.getTransportOrigin()%></strong></dd>
                                                    <dd><bean:message key="ebooking.Haulage.Destination" bundle="<%=clientcode%>"/> : <strong><%=parties_routing.getTransportDestination()%></strong></dd>
                                                    </dt>
                                                    </td>
                                                    </tr>
                                                </table>

                                            </div>

                                        </div> 

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%}%>
            </div>
        </div>
    </div>
    <div class="card card-shadow">

        <div class="card-header preview_header">  <bean:message key="shipping.cargo.head" bundle="<%=clientcode%>"/> </div>
        <div class="card-body">
            <div id="cargopreview">
                <table class="table table-striped table-bordered remove_td_title" id="sitablepreview">
                    <thead>
                        <tr>
                            <th width="5"><bean:message key="ebooking.trust.Sno" bundle="<%=clientcode%>"/></th>
                            <th width="30%"><bean:message key="ecom.shippingins.cargo.contno" bundle="<%=clientcode%>"/></th>
                                <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                            <th width="80%"><bean:message key="adminscreen.description" bundle="<%=clientcode%>"/></th>
                                <%} else {%>
                            <th width="80%"><bean:message key="ecom.shippingins.cargo.packdesc" bundle="<%=clientcode%>"/></th>
                                <%}%> 
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            String requestAction = (String) request.getAttribute("SUBMISSION_EDIT");
                            requestAction = Validation.checkNull(requestAction);
                            String weigthunit = "", volumeunit = "";
                            Hashtable cntDetails = (Hashtable) session.getAttribute("SI_CONTAINER_DETAILS");
                            int cntDtIncr = 1;

                            String dtCmdName = "", dtCmdId = "", dtWtName = "", dtWtId = "", ntWtName = "", ntWtId = "", dtUnitName = "", dtUnitId = "", dtQtyName = "", dtQtyId = "", dtCidName = "", dtCidId = "";
                            String dtCmd = "", ntWt = "", dtWt = "", dtUnit = "", dtQty = "", dtCid = "", soc = "", socname = "", hscodevalue = "", nor = "", settemp = "", tempunit = "", setVentilation = "";
                            String comdId = "", cmdEvt = "", csId = "", csEvt = "", ssId = "", ssEvt = "", wtId = "", ntId = "", qtyId = "", selectOnClickFun = "", cidId = "";
                            String cntNo = "", shipperSealName = "", carrierSealName = "", shipperSeal = "", carrierSeal = "", tareweight = "", grossweight = "", eqpType = "", eqpTypeName = "", strIsSelect = "NO", strCargoId = "", strCargoIdName = "", strEqpType = "", strEqpTypeName = "";
                            //added by Shikin 1/11/2012 for PLARPENH-440
                            String dtPkgName = "", pkgId = "", dtPkg = "", dtVolName = "", volId = "", dtVol = "", dtVutName = "", vutId = "", dtVut = "", dtPkgId = "", dtVolId = "", dtVutId = "", pkgEvt = "";
                            String dtMrkName = "", mrkId = "", dtMrk = "", dtDesName = "", desId = "", dtDes = "", dtMrkId = "", dtDesId = "";
                            String oogflag = "", oogleft = "", oogright = "", oogforward = "", oogalt = "", oogtop = "", oogunit = "";
                            StringBuilder selectOnclick = new StringBuilder("");
                            tablerowcount = 0;
                            int alsize = 0;
                            int cntListSize = 0;
                            int selCnt = 0;
                            String strCntOrder = "";
                            String scriptVef = "";
                            int con = 0, aa = 1;
                            int headSize = 1;
                            String rowstyle = "even";
                            Hashtable comm_table = (Hashtable) session.getServletContext().getAttribute("SESSION_EBOOKINGVIEW_COMMODITYLIST");
                            Hashtable pack_table = (Hashtable) session.getServletContext().getAttribute("SESSION_EBOOKINGVIEW_PACKAGELIST");
                            HashMap<SIMasterVO, SIConatainerValueVO> cntMapObj = (HashMap<SIMasterVO, SIConatainerValueVO>) session.getAttribute("containerListForSI");

                            if (cntMapObj != null && cntMapObj.size() > 0) {
                                alsize = cntMapObj.size();
                                Set<Map.Entry<SIMasterVO, SIConatainerValueVO>> cntSet = cntMapObj.entrySet();
                                for (Map.Entry<SIMasterVO, SIConatainerValueVO> cntMap : cntSet) {
                                    cntDtIncr = 1;
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
                                    oogflag = "";
                                    strEqpType = "";
                                    strCargoId = "";
                                    selectOnClickFun = "";
                                    strCntOrder = "";
                                    scriptVef = "";
                                    strCargoIdName = "";
                                    strEqpTypeName = "";
                                    socname = "";
                                    SIMasterVO siMasterVO = (SIMasterVO) cntMap.getKey();
                                    SIConatainerValueVO siCNTValueVO = (SIConatainerValueVO) cntMap.getValue();
                                    SISealVO siSealVOObj = (SISealVO) siCNTValueVO.getSiSealObj();

                                    cntNo = siMasterVO.getStrCntNo();
                                    con = siMasterVO.getStrSelect();
                                    soc = siMasterVO.getStrsoc();
                                    strEqpType = siMasterVO.getStrEqpType();
                                    strCargoId = siSealVOObj.getStrCargoid();
                                    strIsSelect = siSealVOObj.getStrIsSelect();
                                    shipperSeal = siSealVOObj.getStrShipperSeal();
                                    carrierSeal = siSealVOObj.getStrCarrierSeal();
                                    tareweight = siMasterVO.getTareweight();
                                    grossweight = siMasterVO.getGrossweight();
                                    setVentilation = siMasterVO.getVentilation();
                                    settemp = siMasterVO.getSetTemp();
                                    tempunit = siMasterVO.getTempunit();
                                    oogflag = siMasterVO.getOog();
                                    nor = siMasterVO.getNor();
                                    if ("Y".equalsIgnoreCase(nor)) {
                                        settemp = "";
                                        tempunit = "";
                                        setVentilation = "";
                                    }
                                    csId = "carrierseal" + cntNo + "1";
                                    csEvt = "csChk('" + csId + "');";
                                    ssId = "shipperseal" + cntNo + "1";
                                    ssEvt = "ssChk('" + ssId + "');";
                                    selectOnclick = new StringBuilder("");
                                    selectOnclick.append("setContNo('containerNo");
                                    selectOnclick.append(cntNo);
                                    selectOnclick.append("','");
                                    selectOnclick.append(cntNo);
                                    selectOnclick.append("',this)");
                                    selectOnClickFun = selectOnclick.toString();
                                    eqpType = siMasterVO.getStrType();
                                    socname = cntNo + "#socID#";
                                    strCargoIdName = cntNo + "#cargoID#";
                                    shipperSealName = cntNo + "#shipper#";
                                    carrierSealName = cntNo + "#carrier#";
                                    eqpTypeName = cntNo + "#eqpType#";
                                    strCntOrder = cntNo + "#CNTORDER#";
                                    scriptVef = cntNo + "#Scipt#";
                                    strEqpTypeName = cntNo + "#typeID#";
                                    ArrayList cntList = (ArrayList) siCNTValueVO.getCntDeatilsObj();
                                    if (cntList != null && cntList.size() > 0) {
                                        cntListSize = 0;
                                        cntListSize = cntList.size();
                                        for (int j = 0; j < cntListSize; j++) {
                                            oogleft = "";
                                            oogright = "";
                                            oogforward = "";
                                            oogalt = "";
                                            oogtop = "";
                                            oogunit = "";
                                            SIContainerDetailVO siCntDetVo = (SIContainerDetailVO) cntList.get(j);
                                            comdId = "commodity" + cntNo + cntDtIncr;
                                            cmdEvt = "dynamicCommodity('" + comdId + "');";
                                            qtyId = "qty" + cntNo + cntDtIncr;
                                            wtId = "weight" + cntNo + cntDtIncr;
                                            ntId = "netweight" + cntNo + cntDtIncr;
                                            cidId = "cid" + cntNo + cntDtIncr;
                                            dtCmdName = cntNo + "#commodity#";
                                            dtCmdId = "commodity" + cntNo + cntDtIncr;
                                            dtCidName = cntNo + "#cid#";
                                            dtWtName = cntNo + "#weight#";
                                            dtWtId = "weight" + cntNo + cntDtIncr;
                                            ntWtName = cntNo + "#netweight#";
                                            ntWtId = "netweight" + cntNo + cntDtIncr;
                                            dtUnitName = cntNo + "#unit#";
                                            dtUnitId = "unit" + cntNo + cntDtIncr;
                                            dtQtyName = cntNo + "#qty#";
                                            dtQtyId = "qty" + cntNo + cntDtIncr;
                                            dtCmd = siCntDetVo.getCommodityName();
                                            hscodevalue = siCntDetVo.getHsCodeDesc();
                                            dtWt = siCntDetVo.getStrWgt();
                                            ntWt = siCntDetVo.getNettwt();
                                            dtUnit = siCntDetVo.getStrUnit();
                                            dtQty = siCntDetVo.getStrQty();
                                            dtCid = siCntDetVo.getStrCid();

                                            pkgId = "package" + cntNo + cntDtIncr;
                                            pkgEvt = "dynamicPackage('" + pkgId + "');";
                                            volId = "volume" + cntNo + cntDtIncr;
                                            vutId = "vunit" + cntNo + cntDtIncr;
                                            mrkId = "mark" + cntNo + cntDtIncr;
                                            desId = "descp" + cntNo + cntDtIncr;

                                            dtPkgName = cntNo + "#package#";
                                            dtPkgId = "package" + cntNo + cntDtIncr;
                                            dtVolName = cntNo + "#volume#";
                                            dtVolId = "volume" + cntNo + cntDtIncr;
                                            dtVutName = cntNo + "#vunit#";
                                            dtVutId = "vunit" + cntNo + cntDtIncr;
                                            dtMrkName = cntNo + "#mark#";
                                            dtMrkId = "mark" + cntNo + cntDtIncr;
                                            dtDesName = cntNo + "#descp#";
                                            dtDesId = "descp" + cntNo + cntDtIncr;
                                            dtPkg = Validation.checkNull(siCntDetVo.getStrPkg());
                                            dtVol = siCntDetVo.getStrVol();
                                            dtVut = siCntDetVo.getStrVut();
                                            dtMrk = siCntDetVo.getStrMrk();
                                            dtDes = siCntDetVo.getStrDes();

                                            if (dtUnit.equalsIgnoreCase("T")) {
                                                weigthunit = "Metric Tons";
                                            } else if (dtUnit.equalsIgnoreCase("P")) {
                                                weigthunit = "Pounds";
                                            } else if (dtUnit.equalsIgnoreCase("K")) {
                                                weigthunit = "Kilos";
                                            } else {
                                                weigthunit = dtUnit;
                                            }

                                            if (dtVut.equalsIgnoreCase("M")) {
                                                volumeunit = "Cubic Meters";
                                            } else if (dtVut.equalsIgnoreCase("F")) {
                                                volumeunit = "Cubic Feet";
                                            } else {
                                                volumeunit = dtVut;
                                            }

                                            dtCmd = dtCmd != null && dtCmd.length() > 0 ? dtCmd : "";
                                            hscodevalue = hscodevalue != null && hscodevalue.length() > 0 ? hscodevalue : "";
                                            if (hscodevalue == null || hscodevalue.length() == 0) {
                                                hscodevalue = siCntDetVo.getHsCode();
                                            }
                                            hscodevalue = hscodevalue != null && hscodevalue.length() > 0 ? hscodevalue : "";
                                            dtPkg = dtPkg != null && dtPkg.length() > 0 ? Validation.checkNull((String) pack_table.get(dtPkg)) : dtPkg;

                                            if (isinsurance == null || (!"Y".equalsIgnoreCase(isinsurance))) {
                                                isinsurance = Validation.checkNull(siCntDetVo.getIsinsuranceavailable());
                                            }
                                            quotenostr = Validation.checkNull(siCntDetVo.getQuoteno());
                                            System.out.println("[ECOM-WAR] MCNView isinsurance==> " + isinsurance + " quoteno=> " + quotenostr);
                                            if (quotenostr.length() > 0 && quotenostr.contains("~~")) {
                                                quoteno = Validation.checkNull(quotenostr).split("~~")[0];
                                            }
                                            oogleft = Validation.checkNull(siCntDetVo.getItemleft());
                                            oogright = Validation.checkNull(siCntDetVo.getItemright());
                                            oogforward = Validation.checkNull(siCntDetVo.getItemforward());
                                            oogalt = Validation.checkNull(siCntDetVo.getItemaft());
                                            oogtop = Validation.checkNull(siCntDetVo.getItemtop());
                                            oogunit = Validation.checkNull(siCntDetVo.getItemlengthUnit());
                        %>
                        <tr id="<%=cntNo%>">
                            <% if (cntDtIncr == 1) {%>
                            <td valign="top" align="left">
                                <div class="form-group form-group-sm">
                                    <label class="col-sm-1 "><%=aa%></label>
                                </div>

                            </td>
                            <td valign="top" align="left">
                                <div class="form-group row">
                                    <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.container.display" bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class="col-sm-5 text-info" id="sicontno_lable"><%=cntNo%></label>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-5 font-weight-bold text-left">SOC</label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class="col-sm-5 text-info" id="sisoc_lable"><%=soc%></label>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.eqpsize.display" bundle="<%=clientcode%>"/> </label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class="col-sm-5 text-info" id="sieqptyow_lable"><%=eqpType%></label>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.carrierseal.display" bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class="col-sm-5 text-info" id="sicarrierseal_lable"><%=carrierSeal%></label>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.shipperseal.display" bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class="col-sm-5 text-info" id="sishipperseal_lable" style="word-break: break-all;"><%=shipperSeal%></label>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-5 font-weight-bold text-left"><bean:message key="shipping.tareweight.display" bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class="col-sm-5 text-info" id="sitareweight_lable"><%=tareweight%></label>
                                </div>
                                <%if (is_SI_OOG) {%>
                                <div class="form-group row">
                                    <label class="col-sm-5 font-weight-bold text-left"><bean:message key="ebooking.oog.text.display" bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class="col-sm-5 text-info" id="oogflag_lable"><%=("Y".equalsIgnoreCase(oogflag) ? "Yes" : "No")%></label>
                                </div>
                                <%}%>
                                <%if ("R".equalsIgnoreCase(strEqpType) && is_SI_restriction) {%>
                                <div class="form-group row">
                                    <label class="col-sm-5 font-weight-bold text-left"><bean:message key='ebooking.NOR.display' bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class="col-sm-5 text-info" id="sinor_lable"><%=nor%></label>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-5 font-weight-bold text-left"><bean:message key='ebooking.preset.temp' bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class="col-sm-5 text-info" id="sisettemp_lable"><%=NumberFormat.numberFormat(Validation.checkNull(settemp))%></label>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-5 font-weight-bold text-left"><bean:message key='ebooking.temp.unit' bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class="col-sm-5 text-info" id="sitempunit_lable"><%=("C".equalsIgnoreCase(tempunit) ? "Celsius" : ("F".equalsIgnoreCase(tempunit) ? "Fahrenheit" : ""))%></label>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-5 font-weight-bold text-left"><bean:message key='ebooking.vent' bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class="col-sm-5 text-info" id="sivent_lable"><%=Validation.checkNull(setVentilation)%></label>
                                </div>
                                <%}%>
                            </td>
                            <%
                            } else {%>
                            <td></td><td></td>
                            <%}%>
                            <td valign="top" align="left">

                                <div class="form-group row">
                                    <label class="col-sm-2 font-weight-bold text-left">
                                        <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                                        <bean:message key="shipping.commoditygrp.display" bundle="<%=clientcode%>"/>
                                        <%} else {%>
                                        <bean:message key="shipping.commodity.display" bundle="<%=clientcode%>"/>
                                        <%}%>
                                        <%if ("Y".equalsIgnoreCase(CommodityName_as_HS_Chapter)) {%><bean:message key="ecom.si.hs.Chapter" bundle="<%=clientcode%>"/><%}%> </label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-4 text-info" id="sicmdtylabel"><%=dtCmd%></label>
                                    <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                                    <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.unit.display" bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-2 text-info" id="siqtylabel"><%=dtQty%></label>
                                    <%} else {%>
                                    <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.quantity.display" bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-2 text-info" id="siqtylabel"><%=dtQty%></label>
                                    <%}%>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-2 font-weight-bold text-left">
                                        <%if ("LSC".equalsIgnoreCase(carrierCode) || "NMC".equalsIgnoreCase(carrierCode)) {%>
                                        <bean:message key="shipping.commodity.display" bundle="<%=clientcode%>"/>
                                        <%} else if ("Y".equalsIgnoreCase(hsnamecode)) {%>
                                        <bean:message key='ebooking.trust.hsnamecode' bundle="<%=clientcode%>"/>
                                        <%} else {%>
                                        <bean:message key="shipping.hscode.display" bundle="<%=clientcode%>"/>
                                        <%}%>
                                    </label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-4 text-info" id="sihscodelabel"><%=hscodevalue%></label>
                                    <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.volume.display" bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-2 text-info" id="sivollabel" ><%=dtVol%> <%=volumeunit%></label>
                                    <!--                                    <label class=" col-sm-2 text-info" id="sivolunitlabel"></label>-->
                                </div>
                                <div class="form-group row">

                                    <label class=" col-sm-2 font-weight-bold text-left">
                                        <%if ("GWF".equalsIgnoreCase(carrierCode)) {%>
                                        <bean:message key="shipping.type.display" bundle="<%=clientcode%>"/>
                                        <%} else {%>
                                        <bean:message key="shipping.packagetype.display" bundle="<%=clientcode%>"/>
                                        <%}%>
                                    </label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-4 text-info" id="sipacklabel"><%=dtPkg%></label>

                                    <label class=" col-sm-2 font-weight-bold text-left"><bean:message key="shipping.cargoweight.display" bundle="<%=clientcode%>"/></label> <label class=""></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-2 text-info" id="siwtlabel"><%=dtWt%> <%=weigthunit%></label>
                                    <!--                                    <label class=" col-sm-2 text-info" id="siwtunitlabel"></label>-->
                                </div>
                                <!--                                        netweight-->
                                <div class="form-group row" >
                                    <label class="col-sm-2 font-weight-bold text-left"><bean:message key="ecom.cargo.netweight" bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-4 text-info" id="sinetweight_lable" ><%=ntWt%></label>
                                </div>
                                <div class="form-group row" >
                                    <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.marks.display" bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-9 text-info" id="<%=dtMrkId%>" ><%=dtMrk%></label>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-2 font-weight-bold text-left"><bean:message key="shipping.description.display" bundle="<%=clientcode%>"/></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-9 text-info" id="<%=dtDesId%>"  ><%=dtDes%></label>
                                </div>
                                <div class="form-group row">
                                    <div class="col-sm-12 text-right">
                                        <a onclick="markDescView('<%=dtMrkId%>', '<%=dtDesId%>');"  class='linkeffect mark_desc_cls' href="javascript:void(0)" title="Marks No. and Commodity Description View" ><bean:message key='ecom.shippingins.marksanddescview' bundle="<%=clientcode%>"/></a></div>
                                </div>  
                                <%if (is_SI_OOG && "Y".equalsIgnoreCase(oogflag)) {%>
                                <div class="form-group row">

                                    <label class=" col-sm-2 font-weight-bold text-left">
                                        <bean:message key='ebooking.dimensionunit.display' bundle="<%=clientcode%>"/>
                                    </label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-4 text-info" id="sioogunitlabel"><%= oogunit%></label>

                                    <label class=" col-sm-2 font-weight-bold text-left"><bean:message key='ebooking.dimensionleft.display' bundle="<%=clientcode%>"/></label> <label class=""></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-2 text-info" id="sioogleftlabel"><%=oogleft%></label>
                                </div>
                                <div class="form-group row">

                                    <label class=" col-sm-2 font-weight-bold text-left">
                                        <bean:message key='ebooking.dimensionright.display' bundle="<%=clientcode%>"/>
                                    </label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-4 text-info" id="sioogrightlabel"><%=oogright%></label>

                                    <label class=" col-sm-2 font-weight-bold text-left"><bean:message key='ebooking.dimensionforward.display' bundle="<%=clientcode%>"/></label> <label class=""></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-2 text-info" id="sioogforwardlabel"><%=oogforward%></label>
                                </div>
                                <div class="form-group row">

                                    <label class=" col-sm-2 font-weight-bold text-left">
                                        <bean:message key='ebooking.dimensionalt.display' bundle="<%=clientcode%>"/>
                                    </label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-4 text-info" id="sioogaltlabel"><%=oogalt%></label>

                                    <label class=" col-sm-2 font-weight-bold text-left"><bean:message key='ebooking.dimensiontop.display' bundle="<%=clientcode%>"/></label> <label class=""></label>
                                    <label class="col-sm-1 text-center">:</label>
                                    <label class=" col-sm-2 text-info" id="sioogtoplabel"><%=oogtop%></label>
                                </div>
                                <%}%>
                            </td>

                        </tr>
                        <%
                                            cntDtIncr = cntDtIncr + 1;
                                            tablerowcount++;
                                        }
                                    }
                                    aa++;
                                }
                            }%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <%if ("Y".equalsIgnoreCase(SHOW_CHARGES_IN_SIBL)) {%>
    <div class="card card-shadow">
        <div class="card-header preview_header"> <bean:message key="ecom.si.charges" bundle="<%=clientcode%>"/> </div>
        <div class="card-body">
            <div class="col-sm-12">
                <div id="previewdatatablebychargediv">
                    <table class="table table-striped table-bordered" id="previewdatatablebycharge">
                        <thead>
                            <tr>
                                <th title="sno">S.No</th>
                                <th title="chargeid" class='d-none'>Charge Code</th>
                                <th title="chargename" >Charge Description</th>
                                <th title="fprepaidcollect" >Frieght</th>
                                <th title="custtypeid" >Payor</th>
                                <th title="payagencyid" >Location</th>
                                <th title="eqptypeid" >Eqp.type</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                HashMap frieghtmap = new HashMap();
                                frieghtmap.put("P", "Prepaid");
                                frieghtmap.put("C", "Collect");
                                frieghtmap.put("E", "Third Party");
                                ChargesVo charge = null;
                                if (Chargedetails != null && Chargedetails.size() > 0) {
                                    for (int i = 0; i < Chargedetails.size(); i++) {
                                        charge = (ChargesVo) Chargedetails.get(i);

                            %>
                            <tr>
                                <td><%=i + 1%></td>
                                <td class='d-none'><%=charge.getChargeid()%></td>
                                <td><%=charge.getChargename()%></td>
                                <td class="prepaidcollect"><%=frieghtmap.get(charge.getFprepaidcollect().toUpperCase())%></td>
                                <td class="custtypeid"><%=charge.getCusttypeid()%></td>
                                <td class="payagencyid"><%=charge.getPayagencyid()%></td>
                                <td><%=charge.getEqptypeid()%></td>
                            </tr>
                            <%}
                                }
                            %>
                        </tbody>
                    </table>
                </div>
                <div class="row form-group">

                    <div class="col-xs-12 col-sm-3">
                        <label><bean:message key='ebooking.ref.type' bundle="<%=clientcode%>"/></label>
                        <html:select property="releasetype" styleId="releasetype" styleClass="form-control" disabled="true">
                            <html:option value="">Select</html:option>
                            <html:option value="Normal">Normal</html:option>
                            <html:option value="Express">Express</html:option>
                        </html:select>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%}%>             
    <%
        System.out.println("[ECOM-WAR] MCNView ENABLE_INSURANCE=> " + ENABLE_INSURANCE+ "  isinsurance=> " + isinsurance + " " + quoteno);
        if ("Y".equalsIgnoreCase(ENABLE_INSURANCE) && "Y".equalsIgnoreCase(isinsurance) && "Y".equalsIgnoreCase(insuranceaccess)) {%>
    <div class="card card-shadow">
        <div class="card-header preview_header"> <bean:message key="booking.cargo.insurance" bundle="<%=clientcode%>" />  </div>
        <div class="card-body" id="SIBLviewinsurance">

        </div>
    </div>
    <%}%>
    <div class="card">
        <div class="card-header preview_header"> <bean:message key="ecom.bkg.addrem" bundle="<%=clientcode%>"/> </div>
        <div class="card-body card-shadow">
            <div class="form-group row">
                <label class="col-sm-2 control-label">Declared Value</label>
                <div class="col-sm-6">
                    <html:text styleId="declaredValue" property="declaredValue"  styleClass="form-control light_border" readonly="true" maxlength="1500">
                    </html:text>
                </div>
            </div>
            <% if (is_SI_Addinfo) {%>
            <div class="form-group row">
                <label class="col-sm-2 control-label">ACID</label>
                <div class="col-sm-3">
                    <html:text styleId="acidValue" property="acidValue"  styleClass="form-control light_border" readonly="true" maxlength="20">
                    </html:text>
                </div>
            </div>
            <%}
                if ("Y".equalsIgnoreCase(enableaesno) && "US".equalsIgnoreCase(loadportcountry)) {%>
            <div class="form-group row">
                <label class="col-sm-2 control-label"><bean:message key='ecom.si.aesno' bundle="<%=clientcode%>"/></label>
                <div class="col-sm-10">
                    <html:text styleId="siAESnumber" property="siAESnumber"  styleClass="form-control input-si" maxlength="50" readonly="true"/>
                    <span class="text-info"><bean:message key='ecom.si.aesmsg' bundle="<%=clientcode%>"/></span>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-10">
                    <label class=" col-sm-8 control-label">
                        <bean:message key='ecom.si.aesdismsg' bundle="<%=clientcode%>"/>                   
                    </label>
                    <label class=" col-sm-8 control-label AESDisclaimer">
                        <html:radio property="AESDisclaimer" styleId="AESDisclaimer" value="No SED required – valued at $2,500 or less" disabled="true"/>  <bean:message key='ecom.si.aesdismsg1' bundle="<%=clientcode%>"/>
                    </label>
                    <label class=" col-sm-8 control-label AESDisclaimer">
                        <html:radio property="AESDisclaimer" styleId="AESDisclaimer" value="No SED required, section 30.39 FTSR, C.A.S" disabled="true"/>  <bean:message key='ecom.si.aesdismsg2' bundle="<%=clientcode%>"/>
                    </label>
                    <label class=" col-sm-8 control-label AESDisclaimer">
                        <html:radio property="AESDisclaimer" styleId="AESDisclaimer" value="No SED required, section 30.55 (H) FTSR" disabled="true"/>  <bean:message key='ecom.si.aesdismsg3' bundle="<%=clientcode%>"/>
                    </label>
                </div>
            </div>
            <%}%>

            <div class="form-group row">
                <label class="col-sm-2 control-label"><bean:message key='shipping.addremarks.display' bundle="<%=clientcode%>"/></label>
                <div class="col-sm-10">
                    <html:textarea styleId="sicustremarks" property="sicustremarks"  styleClass="form-control" rows="3" readonly="true"></html:textarea>
                    </div>
                </div>
                <div class="text-right">
                    <button type="button" class="btn btn-primary btn-sm" onclick="backtosiview()"> <bean:message key='common.back.button' bundle="<%=clientcode%>"/> </button>
            </div>
        </div>
    </div>
</html:form>
</div>
<!--marks and desc view modal-->
<div class="modal fade" id="markdescmodal" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="row">
                <div class="col-md-12 column">
                    <div class="modal-header bg-info text-white">  <h5 class="modal-title"><bean:message key='ecom.shippingins.marksanddescview' bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                            <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <label class="col-sm-12"><bean:message key='shipping.marks.display' bundle="<%=clientcode%>"/></label>
                        </div>
                        <hr/>
                        <div class="form-group">
                            <div class="col-sm-12">
                                <textarea name="modalmarkid" id="modalmarkid" value="" class="form-control" rows="3" readonly="true" style="background-color: #fff;"></textarea>
                            </div>
                        </div>
                        <div class="row">
                            <label class="col-sm-12"><bean:message key='ecom.shippingins.commdescview' bundle="<%=clientcode%>"/></label>
                        </div>
                        <hr/>
                        <div class="form-group">
                            <div class="col-sm-12">
                                <textarea name="modaldescid" id="modaldescid" value="" class="form-control" rows="9" readonly="true" style="background-color: #fff;"></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="pull-right"><a href="javascript:void(0)" title="Close"  class="btn btn-info btn-xs" onclick="markDescClose()" id="mailbtn"> <bean:message key="ecom.close" bundle="<%=clientcode%>"/></a></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--Marks and desc modal view end-->
<script type="text/javascript">
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();

//                                var form = $("#SIEditForm").show();
//                                form.steps({
//                                    headerTag: "h3",
//                                    bodyTag: "fieldset",
//                                    autoFocus: true,
//                                    enableAllSteps: true,
//                                    enablePagination: false
//                                });
//                                $(".steps li").addClass("done");
//                                $(".steps li.first").removeClass("done");
//                                $(".steps li a span.number").addClass("glyphicon glyphicon-ok").text("");
//
    });

    function backtosiview() {
        //  callLoading();
    <%if ("YES".equalsIgnoreCase(backtodashboard)) {%>
        location.href = "Welcome.do";
    <%} else {%>
        location.href = "mcn.do";
    <%}%>
    }
    function markDescView(markval, descval) {
        // var markval = $('#' + marksNoId).val();
        // var descval = $('#' + descId).val();
        $('#modalmarkid').val($('#' + markval).text());
        $('#modaldescid').val($('#' + descval).text());
        $("#markdescmodal").modal("show");
    }
    function markDescClose() {
        $("#markdescmodal").modal("hide");
    }

    viewinsurance('<%=quoteno%>');
    function viewinsurance(quote) {
        if (quote != '' && quote.length > 0) {
            //openLoader();
            $.ajax({
                type: "POST",
                cache: false,
                url: "InsuranceAjax?type=quoteview&quoteno=" + quote,
                async: false,
                data: $(this).serializeArray(),
                success: function (data) {
                    $('#SIBLviewinsurance').html(data);
                    // closeLoader();
                    /*$.fancybox.open(data, {
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
                     
                     });*/
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
        }
    }
</script>
