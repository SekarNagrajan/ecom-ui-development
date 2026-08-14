<%@page contentType="text/html"   pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@page import="java.util.Locale,java.util.ArrayList,java.util.LinkedHashMap,org.apache.struts.util.MessageResources,org.apache.struts.action.ActionForm" %>
<%@page import="net.ecom.formbeans.EbookingValidatorFormBeanNew,java.util.Date,net.ecom.vo.Route,net.ecom.common.Validation,net.ecom.common.GeneralConf,java.util.List,java.util.Map,net.ecom.vo.TransportServiceVO,net.ecom.vo.CustomerDetailsVO,net.ecom.vo.CutoffConfigVO,java.util.Hashtable,java.util.Set,net.ecom.vo.BookingHaulagedetailsVo,net.ecom.common.utils.DateConversion_New"%>
<%
    try {
        String custTypeConfig = Validation.checkNull((String) getServletContext().getAttribute(GeneralConf.APBPCONFIGFRONTEND));
        List bpList = (List) session.getAttribute("BPCUSTLIST");
        List apList = (List) session.getAttribute("APCUSTLIST");
        List shList = (List) session.getAttribute("SHCUSTLIST");
        List cnList = (List) session.getAttribute("CNCUSTLIST");
        List ntList = (List) session.getAttribute("NTCUSTLIST");
        List fwList = (List) session.getAttribute("FWCUSTLIST");
        List whList = (List) session.getAttribute("WHCUSTLIST");
        List ncList = (List) session.getAttribute("NCCUSTLIST");
        List slList = (List) session.getAttribute("SLCUSTLIST");
        List byList = (List) session.getAttribute("BYCUSTLIST");
        System.out.println("[ECOM-WAR] CommonCustDetails slList=>"+slList.size());
        List custtypelist = (List) getServletContext().getAttribute(GeneralConf.CUSTTYPECONFIG);
        String clientcode = (String) session.getAttribute("clientcode");
        CustomerDetailsVO custVO = null;
        String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
        //String consReq = ("GWF").equalsIgnoreCase(carrierCode) ? "required" : "";
        //System.out.println("[ECOM-WAR] CommonCustDetails consReqValue" + consReq);
        String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);
        String disableAP_Previous_BLtransactions = (String) getServletContext().getAttribute(GeneralConf.DISABLE_AP_PREVIOUS_BL_TRANSACTIONS);
        String DispConsigToOrder = (String) getServletContext().getAttribute(GeneralConf.DisplayConsigneeToOrder);
        String enableNamedCustomer = (String) getServletContext().getAttribute(GeneralConf.ENABLE_NAMED_CUSTOMER);
        String GENS = (String) getServletContext().getAttribute(GeneralConf.GENS);
        String TENS = (String) getServletContext().getAttribute(GeneralConf.TENS);
        String TENS_Documentation = (String) getServletContext().getAttribute(GeneralConf.TENS_Documentation);
        String Enable_Zip_Code = (String) getServletContext().getAttribute(GeneralConf.Enable_Zip_Code);


%>
<h6><i class="step-icon fa_cust"></i> <bean:message key='ebooking.custdetails.display' bundle="<%=clientcode%>"/> </h6>
<fieldset> 

    <div class="inner txt_box_book">

        <div class="p-b-20">
            <%if("Y".equalsIgnoreCase(GENS)){%>
            <span class="text-info font-weight-bold text-danger">The EORI number is required only for cargo destined for Europe.</span> 
            <%}%>
            <a onclick="bookingcustomerexpand(this)" class="btn-info btn-sm btn bkg_cust_expand expanded  float-right text-white" href="#"> <bean:message key='exp.cust.head' bundle="<%=clientcode%>"/>
            </a></div>

        <%if ("BP".equalsIgnoreCase(custTypeConfig)) {%>
        <div class="section_header"> <span class="txt_line_clr"> <bean:message key='ebooking.BookingParty.display' bundle="<%=clientcode%>"/> </span> <!--<span class="">Reliance industries limited - Mumbai, India </span>-->

            <span class="float-right">

                <button  type="button" class="toggle-btn collapseBookingParty" data-toggle="collapse" data-target="#collapseBookingParty"> <i class="fa fa-plus" > </i> </button>

            </span>		

        </div>

        <div class="col-sm-12 collapse " id="collapseBookingParty">

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <select id="bookingSelect" name="bookingSelect" class="form-control txt_borderno" onclick="setCustDetails(this.value, 'booking')">
                            <%
                                if (bpList != null) {
                            %>
                            <option value="">Select Customer</option>
                            <%
                                //for (int i = 0; i < bpList.size(); i++) {
                                for (Object custObj : bpList) {
                                    custVO = (CustomerDetailsVO) custObj;
                            %>

                            <option value="<%=custVO.getName() + "~~" + custVO.getCustCode() + "~~" + custVO.getAddress() + "~~" + custVO.getCity() + "~~" + custVO.getCountry() + "~~" + custVO.getCountryCode() + "~~" + custVO.getEmail() + "~~" + custVO.getPhoneNo() + "~~" + custVO.getFax() + "~~" + custVO.getAddress1() + "~~" + custVO.getState()+ "~~" + custVO.getPerontype()+ "~~" + custVO.getEroi()+ "~~" + custVO.getZipcode()%>">
                                <%=custVO.getName()%></option>
                                <%}
                                } else {
                                %>
                            <option value="">New Customer</option>
                            <%}%>
                            <option value="new">New Customer</option>
                        </select>

                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_bookingname" styleId="ebook_bookingname"  maxlength="200" styleClass="form-control txt_borderno required" />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_bookingaddress" styleId="ebook_bookingaddress"  styleClass="form-control txt_borderno required"/>
                    </div>


                </div>

            </div>

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_bookingaddress1" styleId="ebook_bookingaddress1"  styleClass="form-control txt_borderno"/>
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_bookingcity_desc" styleId="ebook_bookingcity_desc"  styleClass="form-control txt_borderno city_new_shipper required"  maxlength="100" onblur="setCityDesc_New('ebook_agreecity_desc','ebook_agreecity')"/>
                    </div>


                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_bookingcountry_name" styleId="ebook_bookingcountry_name"  styleClass="form-control txt_borderno country_new_booking required" onchange="loadStateAutoComplete('ebook_bookingcountry_name', 'ebook_bookingstate', 'typeheadhidden6');" maxlength="100" />
                    </div>

                </div>

            </div>


            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_bookingphone" styleId="ebook_bookingphone" styleClass="form-control txt_borderno" />
                    </div>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_bookingfax" styleId="ebook_bookingfax"  styleClass="form-control txt_borderno"/>
                    </div>
                    <%} else {%>

                    <html:hidden property="ebook_bookingfax" styleId="ebook_bookingfax"   styleClass="form-control txt_borderno" />

                    <%}%>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_bookingemail" styleId="ebook_bookingemail" styleClass="form-control email txt_borderno" maxlength="100" />
                    </div>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                        <div class="col-xs-12 col-sm-4">
                           <html:text property="ebook_bookingzipcode" styleId="ebook_bookingzipcode"  styleClass="form-control zip_code txt_borderno"  maxlength="15" />
                        </div>
                    <%}%>
                </div>

            </div>

            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_bookingstate" styleId="ebook_bookingstate"  styleClass="form-control txt_borderno ebooking_state"  maxlength="100" onclick="validateState('ebook_bookingcountry_name',this);"  />
                    </div>

                <%if("Y".equalsIgnoreCase(GENS)){%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_bookingeori" styleId="ebook_bookingeori"  styleClass="form-control txt_borderno ebooking_eori"  maxlength="17" />
                    </div>
                    <div class="col-xs-12 col-sm-4">
                        <html:select property="bookingtypeperson" styleId="bookingtypeperson" styleClass="form-control txt_borderno" style="display: block;" >
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

        <%}%>
        <div class="section_header"> <span class="txt_line_clr"> <bean:message key='shipping.booking.party.agreementType' bundle="<%=clientcode%>"/> </span> <!--<span class="">Reliance industries limited - Mumbai, India </span> -->
            <span class="float-right">
                <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="agreesearch" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'agree')" readonly="readonly"><i class="fa fa-search"></i></a>
                <button type="button" class="toggle-btn collapseAgree" data-toggle="collapse" data-target="#collapseAgree" > <i class="fa fa-plus" > </i> </button>
            </span>
            <div class="checkbox-inline" style="width: 30%;">
                <label style="margin-bottom:0px;padding-left:40px">
                    <input type="checkbox" id="agree" name="agree" onClick="setSameAsBookingforAgreementParty();" value="1" style="margin-top:0.5%">
                    <bean:message key='shipping.booking.party.sameasbooking' bundle="<%=clientcode%>"/> </label>
            </div>
        </div>

        <div class="col-sm-12 collapse " id="collapseAgree">

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">

                        <select id="agreeSelect" name="agreeSelect" class="form-control txt_borderno" onchange="setCustDetails(this.value, 'agree')">
                            <%
                                if (apList != null) {
                            %>
                            <option value="">Select Customer</option>
                            <%
                                for (int i = 0; i < apList.size(); i++) {
                                    custVO = (CustomerDetailsVO) apList.get(i);
                            %>
                            <%if (!"Y".equalsIgnoreCase(disableAP_Previous_BLtransactions)) {%> 
                            <option value="<%=custVO.getName() + "~~" + custVO.getCustCode() + "~~" + custVO.getAddress() + "~~" + custVO.getCity() + "~~" + custVO.getCountry() + "~~" + custVO.getCountryCode() + "~~" + custVO.getEmail() + "~~" + custVO.getPhoneNo() + "~~" + custVO.getFax() + "~~" + custVO.getAddress1() + "~~" + custVO.getState()+ "~~" + custVO.getPerontype()+ "~~" + custVO.getEroi()+ "~~" + custVO.getZipcode()%>">
                                <%=custVO.getName()%></option>
                                <% }%>
                                <%                                                                    }
                                } else {
                                %>
                            <option value="">New Customer</option>
                            <%                                                                }%>
                            <option value="new">New Customer</option>
                        </select>

                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_agreename" styleId="ebook_agreename"  maxlength="150" styleClass="form-control txt_borderno " />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_agreeaddress" styleId="ebook_agreeaddress"  styleClass="form-control txt_borderno " maxlength="150" />
                    </div>


                </div>

            </div>

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_agreeaddress1" styleId="ebook_agreeaddress1"  styleClass="form-control txt_borderno" maxlength="150" />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_agreecity_desc" styleId="ebook_agreecity_desc"  styleClass="form-control txt_borderno city_new_shipper"  maxlength="100"  onblur="setCityDesc_New('ebook_agreecity_desc','ebook_agreecity')"/>
                    </div>


                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_agreecountry_name" styleId="ebook_agreecountry_name"  styleClass="form-control txt_borderno  country_new_agree"  maxlength="100" onchange="loadStateAutoComplete('ebook_agreecountry_name', 'ebook_agreestate', 'typeheadhidden7');" onblur="setCountry_New('ebook_agreecountry_name','ebook_agreecountry');"/>
                    </div>

                </div>

            </div>


            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_agreephone" styleId="ebook_agreephone" styleClass="form-control txt_borderno"  maxlength="20" />
                    </div>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_agreefax" styleId="ebook_agreefax"  styleClass="form-control txt_borderno" maxlength="20" />

                    </div>
                    <%} else {%>

                    <html:hidden property="ebook_agreefax" styleId="ebook_agreefax"   styleClass="form-control txt_borderno" />

                    <%}%>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_agreeemail" styleId="ebook_agreeemail" styleClass="form-control email txt_borderno"  maxlength="75" />

                    </div>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                        <div class="col-xs-12 col-sm-4">
                           <html:text property="ebook_agreezipcode" styleId="ebook_agreezipcode"  styleClass="form-control zip_code txt_borderno"  maxlength="15" />
                        </div>
                    <%}%>
                </div>

            </div>

            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_agreestate" styleId="ebook_agreestate"  styleClass="form-control txt_borderno ebooking_state"  maxlength="100" onclick="validateState('ebook_agreecountry_name',this);" />
                    </div>
                    <%if("Y".equalsIgnoreCase(GENS)){%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_agreeeori" styleId="ebook_agreeeori"  styleClass="form-control txt_borderno ebooking_eori"  style="display: block;" maxlength="17" />
                    </div>
                    <div class="col-xs-12 col-sm-4">
                        <html:select property="agreetypeperson" styleId="agreetypeperson" styleClass="form-control txt_borderno" style="display: block;" >
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


        <div class="section_header"> <bean:message key='shipping.booking.party.shipperType' bundle="<%=clientcode%>"/>
            <div class="checkbox-inline" style="width: 22%;">
                <label style="margin-bottom:0px;padding-left:40px">
                    <input type="checkbox" id="agreeShipper" name="agreeShipper" onClick="setSameAsBooking();" value="1" style="margin-top:0.5%">
                    <bean:message key='shipping.booking.party.sameasbooking' bundle="<%=clientcode%>"/> </label>
            </div>
            <div class="checkbox-inline" style="width: 30%;">
                <label style="margin-bottom:0px;padding-left:20px">
                    <input type="checkbox" id="agreementShipper" name="agreementShipper" onClick="setSameAsAgree();" value="1" style="margin-top:0.5%">
                    <bean:message key="ecom.bkg.sameasap" bundle="<%=clientcode%>"/></label>
            </div>
            <span class="float-right">
                <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="shippersearch" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'shipper')" readonly="readonly"><i class="fa fa-search"></i></a>
                <button type="button" class="toggle-btn float-right collapseShipper" data-toggle="collapse" data-target="#collapseShipper" > <i class="fa fa-plus" > </i> </button>
            </span>
        </div>

        <div class="col-sm-12 collapse " id="collapseShipper">

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">

                        <select id="shipperSelect" name="shipperSelect" class="form-control txt_borderno" onchange="setCustDetails(this.value, 'shipper')">
                            <%

                                if (shList != null) {
                            %>
                            <option value="">Select Customer</option>
                            <%
                                for (int i = 0; i < shList.size(); i++) {
                                    custVO = (CustomerDetailsVO) shList.get(i);
                            %>

                                <option value="<%=custVO.getName() + "~~" + custVO.getCustCode() + "~~" + custVO.getAddress() + "~~" + custVO.getCity() + "~~" + custVO.getCountry() + "~~" + custVO.getCountryCode() + "~~" + custVO.getEmail() + "~~" + custVO.getPhoneNo() + "~~" + custVO.getFax() + "~~" + custVO.getAddress1() + "~~" + custVO.getState()+ "~~" + custVO.getPerontype()+ "~~" + custVO.getEroi()+ "~~" + custVO.getZipcode()%>">
                                <%=custVO.getName()%></option>

                            <%                                                                    }
                            } else {
                            %>
                            <option value="">New Customer</option>
                            <%                                                                }%>
                            <option value="new">New Customer</option>
                        </select>

                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_shippername" styleId="ebook_shippername" maxlength="150" styleClass="form-control txt_borderno"  />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_shipperaddress" styleId="ebook_shipperaddress" maxlength="150"  styleClass="form-control txt_borderno" />
                    </div>


                </div>

            </div>

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_shipperaddress1" styleId="ebook_shipperaddress1"  maxlength="150"  styleClass="form-control txt_borderno"/>
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_shippercity_desc" styleId="ebook_shippercity_desc"  styleClass="form-control txt_borderno city_new_shipper"  maxlength="100" onblur= "setCityDesc_New('ebook_shippercity_desc','ebook_shippercity')"/>
                    </div>


                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_shippercountry_name" styleId="ebook_shippercountry_name" maxlength="100" styleClass="form-control txt_borderno country_new_shipper" onchange="loadStateAutoComplete('ebook_shippercountry_name', 'ebook_shipperstate', 'typeheadhidden8');" onblur="setCountry_New('ebook_shippercountry_name','ebook_shippercountry');"/>
                    </div>

                </div>

            </div>


            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_shipperphone" styleId="ebook_shipperphone" maxlength="20" styleClass="form-control txt_borderno"  />
                    </div>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_shipperfax" styleId="ebook_shipperfax" maxlength="20"  styleClass="form-control txt_borderno" />

                    </div>
                    <%} else {%>

                    <html:hidden property="ebook_shipperfax" styleId="ebook_shipperfax"   styleClass="form-control txt_borderno"/>

                    <%}%>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_shipperemail" styleId="ebook_shipperemail" styleClass="form-control email txt_borderno" maxlength="75" />

                    </div>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                        <div class="col-xs-12 col-sm-4">
                           <html:text property="ebook_shipperzipcode" styleId="ebook_shipperzipcode"  styleClass="form-control zip_code txt_borderno"  maxlength="15" />
                        </div>
                        <%}%>
                </div>

            </div>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_shipperstate" styleId="ebook_shipperstate"  styleClass="form-control txt_borderno ebooking_state"  maxlength="100" onclick="validateState('ebook_shippercountry_name',this);" />
                    </div>
                     <%if("Y".equalsIgnoreCase(GENS)){%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_shippereori" styleId="ebook_shippereori"  styleClass="form-control txt_borderno ebooking_eori" style="display: block;"  maxlength="17" />
                    </div>
                    <div class="col-xs-12 col-sm-4">
                        <html:select property="shippertypeperson" styleId="shippertypeperson" styleClass="form-control txt_borderno" style="display: block;" >
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

        <div class="section_header"> <bean:message key='shipping.booking.party.consigneeType' bundle="<%=clientcode%>"/>
            <%if ("Y".equalsIgnoreCase(DispConsigToOrder)) {%>
            <div class="checkbox-inline" style="width: 30%;">
                <label style="margin-bottom:0px;padding-left:20px">
                    <input type="checkbox" id="consigneetoorder" name="consigneetoorder" onclick="checkToOrder('consignee', this);" value="1" style="margin-top:0.5%">
                    <bean:message key="shipping.booking.party.constoorder" bundle="<%=clientcode%>"/></label>
            </div>
            <%}%>
            <span class="float-right">
                <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="consigneesearch" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'consignee')"  readonly="readonly"><i class="fa fa-search"></i></a>
                <button type="button" class="toggle-btn float-right collapseConsignee" data-toggle="collapse" data-target="#collapseConsignee" > <i class="fa fa-plus" > </i> </button>
            </span>
        </div>

        <div class="col-sm-12 collapse " id="collapseConsignee">

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">

                        <select id="consigneeSelect" name="consigneeSelect" class="form-control txt_borderno"  onchange="setCustDetails(this.value, 'consignee')">
                            <%

                                if (cnList != null) {
                            %>
                            <option value="">Select Customer</option>
                            <%
                                for (int i = 0; i < cnList.size(); i++) {
                                    custVO = (CustomerDetailsVO) cnList.get(i);

                            %>


                                <option value="<%=custVO.getName() + "~~" + custVO.getCustCode() + "~~" + custVO.getAddress() + "~~" + custVO.getCity() + "~~" + custVO.getCountry() + "~~" + custVO.getCountryCode() + "~~" + custVO.getEmail() + "~~" + custVO.getPhoneNo() + "~~" + custVO.getFax() + "~~" + custVO.getAddress1() + "~~" + custVO.getState()+ "~~" + custVO.getPerontype()+ "~~" + custVO.getEroi()+ "~~" + custVO.getZipcode()%>">      
                                <%=custVO.getName()%></option>


                            <%                                                                    }
                            } else {
                            %>
                            <option value="">New Customer</option>
                            <%                                                                }%>
                            <option value="new">New Customer</option>
                        </select>

                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text size="36" property="ebook_consigneename" styleId="ebook_consigneename" styleClass="form-control txt_borderno"   maxlength="150"/>
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_consigneeaddress" styleId="ebook_consigneeaddress"  maxlength="150" styleClass="form-control txt_borderno "/>
                    </div>


                </div>

            </div>

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_consigneeaddress1" styleId="ebook_consigneeaddress1" maxlength="150"  styleClass="form-control txt_borderno"/>
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_consigneecity_desc" styleId="ebook_consigneecity_desc"  styleClass="form-control txt_borderno city_new_consignee "   maxlength="100" onblur= "setCityDesc_New('ebook_consigneecity_desc','ebook_consigneecity')"/>
                    </div>


                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_consigneecountry_name" styleId="ebook_consigneecountry_name" styleClass="form-control txt_borderno country_new_consignee"  maxlength="100"  onchange="loadStateAutoComplete('ebook_consigneecountry_name', 'ebook_consigneestate', 'typeheadhidden9');" onblur="setCountry_New('ebook_consigneecountry_name','ebook_consigneecountry');"/>
                    </div>

                </div>

            </div>


            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_consigneephone" styleId="ebook_consigneephone" maxlength="20"   styleClass="form-control txt_borderno" />
                    </div>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_consigneefax" styleId="ebook_consigneefax" maxlength="20"  styleClass="form-control txt_borderno" />
                    </div>
                    <%} else {%>

                    <html:hidden property="ebook_consigneefax" styleId="ebook_consigneefax"   styleClass="form-control txt_borderno" />

                    <%}%>


                    <div class="col-xs-12 col-sm-4">
                        <html:text size="43" property="ebook_consigneeemail" styleId="ebook_consigneeemail" styleClass="form-control txt_borderno email" maxlength="75" />
                    </div>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                        <div class="col-xs-12 col-sm-4">
                           <html:text property="ebook_consigneezipcode" styleId="ebook_consigneezipcode"  styleClass="form-control zip_code txt_borderno"  maxlength="15" />
                        </div>
                        <%}%>
                       
                </div>

            </div>

            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_consigneestate" styleId="ebook_consigneestate"  styleClass="form-control txt_borderno ebooking_state"  maxlength="100" onclick="validateState('ebook_consigneecountry_name',this);" />
                    </div>
                     <%if("Y".equalsIgnoreCase(GENS)){%>
                      <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_consigneeeori" styleId="ebook_consigneeeori"  styleClass="form-control txt_borderno ebooking_eori" style="display: block;"  maxlength="17" />
                    </div>
                    <div class="col-xs-12 col-sm-4">
                        <html:select property="consigneetypeperson" styleId="consigneetypeperson" styleClass="form-control txt_borderno" style="display: block;" >
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
        <div class="section_header"> <bean:message key='Booking.party.notify' bundle="<%=clientcode%>"/>
            <div class="checkbox-inline" style="width: 30%;">
                <label style="margin-bottom:1px;padding-left:52px">
                    <input type="checkbox" id="notifyconsig" name="notifyconsig" onClick="populateNotify();" value="1" style="margin-top:0.5%">
                    <bean:message key='shipping.booking.party.sameasConsignee' bundle="<%=clientcode%>"/></label>
            </div>
            <span class="float-right">
                <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="notifysearch" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'notify')"  readonly="readonly"><i class="fa fa-search"></i></a>
                <button type="button" class="toggle-btn float-right collapseNotify" data-toggle="collapse" data-target="#collapseNotify" > <i class="fa fa-plus" > </i> </button>
            </span>
        </div>

        <div class="col-sm-12 collapse " id="collapseNotify">

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">

                        <select id="notifySelect" name="notifySelect" class="form-control txt_borderno"  onchange="setCustDetails(this.value, 'notify')">
                            <%

                                if (ntList != null) {
                            %>
                            <option value="">Select Customer</option>
                            <%
                                for (int i = 0; i < ntList.size(); i++) {
                                    custVO = (CustomerDetailsVO) ntList.get(i);

                            %>


                            <option value="<%=custVO.getName() + "~~" + custVO.getCustCode() + "~~" + custVO.getAddress() + "~~" + custVO.getCity() + "~~" + custVO.getCountry() + "~~" + custVO.getCountryCode() + "~~" + custVO.getEmail() + "~~" + custVO.getPhoneNo() + "~~" + custVO.getFax() + "~~" + custVO.getAddress1() + "~~" + custVO.getState()+ "~~" + custVO.getPerontype()+ "~~" + custVO.getEroi()+ "~~" + custVO.getZipcode()%>">
                                <%=custVO.getName()%></option>

                            <%                                                                    }
                            } else {
                            %>
                            <option value="">New Customer</option>
                            <%                                                                }%>
                            <option value="new">New Customer</option>
                        </select>

                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifyname" styleId="ebook_notifyname" styleClass="form-control txt_borderno"   maxlength="150" />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_notifyaddress" styleId="ebook_notifyaddress" maxlength="150"   styleClass="form-control txt_borderno"/>
                    </div>


                </div>

            </div>

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_notifyaddress1" styleId="ebook_notifyaddress1" maxlength="150" styleClass="form-control txt_borderno"/>
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifycity_desc" styleId="ebook_notifycity_desc"   styleClass="form-control txt_borderno city_new_notify "  maxlength="100" onblur= "setCityDesc_New('ebook_notifycity_desc','ebook_notifycity')"/>
                    </div>


                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifycountry_name" styleId="ebook_notifycountry_name"  styleClass="form-control txt_borderno country_new_notify " maxlength="100"  onchange="loadStateAutoComplete('ebook_notifycountry_name', 'ebook_notifystate', 'typeheadhidden10');" onblur="setCountry_New('ebook_notifycountry_name','ebook_notifycountry');"/>
                    </div>

                </div>

            </div>


            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifyphone" styleId="ebook_notifyphone" maxlength="20"   styleClass="form-control txt_borderno" />
                    </div>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifyfax" styleId="ebook_notifyfax" maxlength="20"  styleClass="form-control txt_borderno" />

                    </div>

                    <%} else {%>

                    <html:hidden property="ebook_notifyfax" styleId="ebook_notifyfax"   styleClass="form-control txt_borderno" />

                    <%}%>

                    <div class="col-xs-12 col-sm-4">
                        <html:text size="43" property="ebook_notifyemail" styleId="ebook_notifyemail" styleClass="form-control txt_borderno email " maxlength="75"/>

                    </div>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                        <div class="col-xs-12 col-sm-4">
                           <html:text property="ebook_notifyzipcode" styleId="ebook_notifyzipcode"  styleClass="form-control zip_code txt_borderno"  maxlength="15" />
                        </div>
                            <%}%>
                        
                </div>

            </div>

            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifystate" styleId="ebook_notifystate"  styleClass="form-control txt_borderno ebooking_state"  maxlength="100" onclick="validateState('ebook_notifycountry_name',this);" />
                    </div>
                     <%if("Y".equalsIgnoreCase(GENS)){%>
                     <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifyeori" styleId="ebook_notifyeori"  styleClass="form-control txt_borderno ebooking_eori" style="display: block;"  maxlength="17" />
                    </div>
                    <div class="col-xs-12 col-sm-4">
                         <html:select property="notifytypeperson" styleId="notifytypeperson" styleClass="form-control txt_borderno" style="display: block;">
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
        <% if (custtypelist != null && custtypelist.contains("N1")) {%>
        <div class="section_header"> <bean:message key='Booking.party.notify1' bundle="<%=clientcode%>"/>
            <div class="checkbox-inline" style="width: 30%;">
                <label style="margin-bottom:1px;padding-left: 44px;">
                    <input type="checkbox" id="notifyconsig1" name="notifyconsig1" onClick="populateNotify1();" value="1" style="margin-top:0.5%">
                    <bean:message key='shipping.booking.party.sameasConsignee' bundle="<%=clientcode%>"/></label>
            </div>
            <span class="float-right">
                <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="notifysearch1" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'notify1')"  readonly="readonly"><i class="fa fa-search"></i></a>
                <button type="button" class="toggle-btn float-right collapseNotify1" data-toggle="collapse" data-target="#collapseNotify1" > <i class="fa fa-plus" > </i> </button>
            </span>
        </div>

        <div class="col-sm-12 collapse " id="collapseNotify1">

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">

                        <select id="notify1Select" name="notify1Select" class="form-control txt_borderno"  onchange="setCustDetails1(this.value, 'notify')">
                            <%

                                if (ntList != null) {
                            %>
                            <option value="">Select Customer</option>
                            <%
                                for (int i = 0; i < ntList.size(); i++) {
                                    custVO = (CustomerDetailsVO) ntList.get(i);

                            %>

                            <option value="<%=custVO.getName() + "~~" + custVO.getCustCode() + "~~" + custVO.getAddress() + "~~" + custVO.getCity() + "~~" + custVO.getCountry() + "~~" + custVO.getCountryCode() + "~~" + custVO.getEmail() + "~~" + custVO.getPhoneNo() + "~~" + custVO.getFax() + "~~" + custVO.getAddress1() + "~~" + custVO.getState()+ "~~" + custVO.getPerontype()+ "~~" + custVO.getEroi()+ "~~" + custVO.getZipcode()%>">
                                <%=custVO.getName()%></option>

                            <%                                                                    }
                            } else {
                            %>
                            <option value="">New Customer</option>
                            <%                                                                }%>
                            <option value="new">New Customer</option>
                        </select>

                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifyname_n1" styleId="ebook_notifyname_n1"  styleClass="form-control txt_borderno"   maxlength="150" />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_notifyaddress_n1" styleId="ebook_notifyaddress_n1" maxlength="150" styleClass="form-control txt_borderno"/>
                    </div>


                </div>

            </div>

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_notifyaddress1_n1" styleId="ebook_notifyaddress1_n1" maxlength="150" styleClass="form-control txt_borderno"/>
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifycity_desc_n1" styleId="ebook_notifycity_desc_n1"  styleClass="form-control txt_borderno city_new_notify "  maxlength="100" onblur= "setCityDesc_New('ebook_notifycity_desc','ebook_notifycity')"/>
                    </div>


                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifycountry_name_n1" styleId="ebook_notifycountry_name_n1"  styleClass="form-control txt_borderno country_new_notify" maxlength="100" onchange="loadStateAutoComplete('ebook_notifycountry_name_n1', 'ebook_notifystate_n1', 'typeheadhidden11');" onblur="setCountry_New('ebook_notifycountry_name_n1','ebook_notifycountry_n1');"/>
                    </div>

                </div>

            </div>


            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifyphone_n1" styleId="ebook_notifyphone_n1" maxlength="20"  styleClass="form-control txt_borderno" />
                    </div>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifyfax_n1" styleId="ebook_notifyfax_n1" maxlength="20"  styleClass="form-control txt_borderno" />

                    </div>
                    <%} else {%>

                    <html:hidden property="ebook_notifyfax_n1" styleId="ebook_notifyfax_n1"   styleClass="form-control txt_borderno" />

                    <%}%>

                    <div class="col-xs-12 col-sm-4">
                        <html:text size="43" property="ebook_notifyemail_n1" styleId="ebook_notifyemail_n1" styleClass="form-control txt_borderno email " maxlength="75"/>

                    </div>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                        <div class="col-xs-12 col-sm-4">
                           <html:text property="ebook_notifyzipcode_n1" styleId="ebook_notifyzipcode_n1"  styleClass="form-control zip_code txt_borderno"  maxlength="15" />
                        </div>
                            <%}%>
                </div>

            </div>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifystate_n1" styleId="ebook_notifystate_n1"  styleClass="form-control txt_borderno ebooking_state"  maxlength="100" onclick="validateState('ebook_notifycountry_name_n1',this);" />
                    </div>
                     <%if("Y".equalsIgnoreCase(GENS)){%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_notifyeori_n1" styleId="ebook_notifyeori_n1"  styleClass="form-control txt_borderno ebooking_eori" style="display: block;" maxlength="17" />
                    </div>
                    <div class="col-xs-12 col-sm-4">
                         <html:select property="notifytypeperson_n1" styleId="notifytypeperson_n1" styleClass="form-control txt_borderno " style="display: block;">
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
        <%}%>
        <% if (custtypelist != null && custtypelist.contains("FW")) {%>

        <div class="section_header"><bean:message key="ecom.bkg.custfw" bundle="<%=clientcode%>"/>
            <span class="float-right">
                <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="forwardersearch" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'forwarder')" readonly="readonly"><i class="fa fa-search"></i></a>
                <button type="button" class="toggle-btn collapseForwarder" data-toggle="collapse" data-target="#collapseForwarder" > <i class="fa fa-plus" > </i> </button>
            </span>
        </div>

        <div class="col-sm-12 collapse " id="collapseForwarder">

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">

                        <select id="forwarderSelect" name="forwarderSelect" class="form-control txt_borderno" onchange="setCustDetails(this.value, 'forwarder')">
                            <%
                                if (fwList != null) {
                            %>
                            <option value="">Select Customer</option>
                            <%
                                for (int i = 0; i < fwList.size(); i++) {
                                    custVO = (CustomerDetailsVO) fwList.get(i);
                            %>

                            <option value="<%=custVO.getName() + "~~" + custVO.getCustCode() + "~~" + custVO.getAddress() + "~~" + custVO.getCity() + "~~" + custVO.getCountry() + "~~" + custVO.getCountryCode() + "~~" + custVO.getEmail() + "~~" + custVO.getPhoneNo() + "~~" + custVO.getFax() + "~~" + custVO.getAddress1() + "~~" + custVO.getState()+ "~~" + custVO.getPerontype()+ "~~" + custVO.getEroi()+ "~~" + custVO.getZipcode()%>">
                                <%=custVO.getName()%></option>

                            <%                                                                    }
                            } else {
                            %>
                            <option value="">New Customer</option>
                            <%                                                                }%>
                            <option value="new">New Customer</option>
                        </select>

                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_forwardername" styleId="ebook_forwardername"  maxlength="150" styleClass="form-control txt_borderno " />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_forwarderaddress" styleId="ebook_forwarderaddress"  styleClass="form-control txt_borderno " maxlength="150" />
                    </div>


                </div>

            </div>

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_forwarderaddress1" styleId="ebook_forwarderaddress1"  styleClass="form-control txt_borderno" maxlength="150" />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_forwardercity_desc" styleId="ebook_forwardercity_desc"  styleClass="form-control txt_borderno city_new_forwarder"  maxlength="100"  onblur="setCityDesc_New('ebook_forwardercity_desc','ebook_forwardercity')"/>
                    </div>


                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_forwardercountry_name" styleId="ebook_forwardercountry_name"  styleClass="form-control txt_borderno  country_new_forwarder"  maxlength="100" onchange="loadStateAutoComplete('ebook_forwardercountry_name', 'ebook_forwarderstate', 'typeheadhidden12');" onblur="setCountry_New('ebook_forwardercountry_name','ebook_forwardercountry');"/>
                    </div>

                </div>

            </div>


            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_forwarderphone" styleId="ebook_forwarderphone" styleClass="form-control txt_borderno"  maxlength="20" />
                    </div>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_forwarderfax" styleId="ebook_forwarderfax"  styleClass="form-control txt_borderno" maxlength="20" />

                    </div>

                    <%} else {%>

                    <html:hidden property="ebook_forwarderfax" styleId="ebook_forwarderfax"   styleClass="form-control txt_borderno" />

                    <%}%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_forwarderemail" styleId="ebook_forwarderemail" styleClass="form-control email txt_borderno"  maxlength="75" />

                    </div>

                  <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                     <div class="col-xs-12 col-sm-4">
                       <html:text property="ebook_forwarderzipcode" styleId="ebook_forwarderzipcode"  styleClass="form-control zip_code txt_borderno"  maxlength="15" />
                    </div>
                  <%}%>


                </div>

            </div>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_forwarderstate" styleId="ebook_forwarderstate"  styleClass="form-control txt_borderno ebooking_state"  maxlength="100" onclick="validateState('ebook_forwardercountry_name',this);" />
                    </div>
                     <%if("Y".equalsIgnoreCase(GENS)){%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_forwardereori" styleId="ebook_forwardereori"  styleClass="form-control txt_borderno ebooking_eori" style="display: block;"  maxlength="17" />
                    </div>
                    <div class="col-xs-12 col-sm-4">
                        <html:select property="forwardertypeperson" styleId="forwardertypeperson" styleClass="form-control txt_borderno " style="display: block;" >
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
        <%}%>
        <% if ("Y".equalsIgnoreCase(enableNamedCustomer)) {%>
        <% if (custtypelist != null && custtypelist.contains("NC")) {%>
        <div class="section_header"> <bean:message key='ecom.bkg.custnc' bundle="<%=clientcode%>"/>
            <div class="checkbox-inline" style="width: 30%;">
                <label style="margin-bottom:1px;padding-left:52px">
                    <input type="checkbox" id="NamedCustomer" name="NamedCustomer" onClick="directBooking();" value="directBooking" style="margin-top:0.5%" checked>
                    <bean:message key="ecom.bkg.directbooking" bundle="<%=clientcode%>"/></label>
            </div>
            <span class="float-right">
                <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="NamedCustomersearch" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'NamedCustomer')" readonly="readonly"><i class="fa fa-search"></i></a>
                <button type="button" class="toggle-btn collapseNamedCustomer " data-toggle="collapse" data-target="#collapseNamedCustomer" collapsed=""> <i class="fa fa-plus"> </i> </button>
            </span>
        </div>

        <div class="col-sm-12 collapse " id="collapseNamedCustomer" >
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <select id="NamedCustomerSelect" name="NamedCustomerSelect" class="form-control txt_borderno" onchange="setCustDetails(this.value, 'NamedCustomer')">
                            <%
                                if (ncList != null) {
                            %>
                            <option value="">Select Customer</option>
                            <%
                                for (int i = 0; i < ncList.size(); i++) {
                                    custVO = (CustomerDetailsVO) ncList.get(i);
                            %>
                                <option value="<%=custVO.getName() + "~~" + custVO.getCustCode() + "~~" + custVO.getAddress() + "~~" + custVO.getCity() + "~~" + custVO.getCountry() + "~~" + custVO.getCountryCode() + "~~" + custVO.getEmail() + "~~" + custVO.getPhoneNo() + "~~" + custVO.getFax() + "~~" + custVO.getAddress1() + "~~" + custVO.getState()+ "~~" + custVO.getPerontype()+ "~~" + custVO.getEroi()+ "~~" + custVO.getZipcode()%>">
                                <%=custVO.getName()%></option>
<!--                            <option value="<=custVO.getName()%>~~<=custVO.getCustCode()%>~~<=custVO.getAddress()%>~~<=custVO.getCity()%>~~<=custVO.getCountry()%>~~<=custVO.getCountryCode()%>~~<=custVO.getEmail()%>~~<=custVO.getPhoneNo()%>~~<=custVO.getFax()%>~~<=custVO.getAddress1()%>~~<=custVO.getState()%>~~<=custVO.getZipcode()%>">
                                                    <=custVO.getName()%></option>-->
                                <%                                                                    }
                                } else {
                                %>
                            <option value="">New Customer</option>
                            <%}%>
                            <option value="new">New Customer</option>
                        </select>
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_NamedCustomername" styleId="ebook_NamedCustomername"  maxlength="150" styleClass="form-control txt_borderno " />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_NamedCustomeraddress" styleId="ebook_NamedCustomeraddress"  styleClass="form-control txt_borderno " maxlength="150" />
                    </div>

                </div>
            </div>

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_NamedCustomeraddress1" styleId="ebook_NamedCustomeraddress1"  styleClass="form-control txt_borderno" maxlength="150" />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_NamedCustomercity_desc" styleId="ebook_NamedCustomercity_desc"  styleClass="form-control txt_borderno city_new_shipper"  maxlength="100"  onblur="setCityDesc_New('ebook_NamedCustomercity_desc','ebook_NamedCustomercity')"/>
                    </div>


                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_NamedCustomercountry_name" styleId="ebook_NamedCustomercountry_name"  styleClass="form-control txt_borderno  country_new_NamedCustomer"  maxlength="100" onchange="loadStateAutoComplete('ebook_NamedCustomercountry_name', 'ebook_NamedCustomerstate', 'typeheadhidden14');" onblur="setCountry_New('ebook_NamedCustomercountry_name','ebook_NamedCustomercountry');"/>
                    </div>
                </div>    
            </div>  

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_NamedCustomerphone" styleId="ebook_NamedCustomerphone" styleClass="form-control txt_borderno"  maxlength="20" />
                    </div>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_NamedCustomerfax" styleId="ebook_NamedCustomerfax"  styleClass="form-control txt_borderno" maxlength="20" />

                    </div>


                    <%} else {%>

                    <html:hidden property="ebook_NamedCustomerfax" styleId="ebook_NamedCustomerfax"   styleClass="form-control txt_borderno" />

                    <%}%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_NamedCustomeremail" styleId="ebook_NamedCustomeremail" styleClass="form-control email txt_borderno"  maxlength="75" />
                    </div>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                        <div class="col-xs-12 col-sm-4">
                           <html:text property="ebook_NamedCustomerzipcode" styleId="ebook_NamedCustomerzipcode"  styleClass="form-control zip_code txt_borderno"  maxlength="15" />
                        </div>
                    <%}%>
                 
                </div>  
            </div>        

            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_NamedCustomerstate" styleId="ebook_NamedCustomerstate"  styleClass="form-control txt_borderno ebooking_state"  maxlength="100" onclick="validateState('ebook_NamedCustomercountry_name',this);" />
                    </div>
                     <%if("Y".equalsIgnoreCase(GENS)){%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_NamedCustomereori" styleId="ebook_NamedCustomereori"  styleClass="form-control txt_borderno ebooking_eori" style="display: block;" maxlength="17" />
                    </div>
                    <div class="col-xs-12 col-sm-4">
                         <html:select property="ebook_NamedCustomerpersontype" styleId="NamedCustomertypeperson" styleClass="form-control txt_borderno" style="display: block;">
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
        <%}%>
        <%}%>
        <% if (custtypelist != null && custtypelist.contains("WH") && false) {%>

        <div class="section_header"> <bean:message key="ecom.bkg.custwh" bundle="<%=clientcode%>"/>
            <span class="float-right">
                <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="agreesearch" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'Warehouse')" readonly="readonly"><i class="fa fa-search"></i></a>
                <button type="button" class="toggle-btn collapseWarehouse" data-toggle="collapse" data-target="#collapseWarehouse" > <i class="fa fa-plus" > </i> </button>
            </span>
        </div>

        <div class="col-sm-12 collapse " id="collapseWarehouse">

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">

                        <select id="WarehouseSelect" name="WarehouseSelect" class="form-control txt_borderno" onchange="setCustDetails(this.value, 'Warehouse')">
                            <%
                                if (whList != null) {
                            %>
                            <option value="">Select Customer</option>
                            <%
                                for (int i = 0; i < whList.size(); i++) {
                                    custVO = (CustomerDetailsVO) whList.get(i);
                            %>

                            <option value="<%=custVO.getName() + "~~" + custVO.getCustCode() + "~~" + custVO.getAddress() + "~~" + custVO.getCity() + "~~" + custVO.getCountry() + "~~" + custVO.getCountryCode() + "~~" + custVO.getEmail() + "~~" + custVO.getPhoneNo() + "~~" + custVO.getFax() + "~~" + custVO.getAddress1() + "~~" + custVO.getState()+ "~~" + custVO.getPerontype()+ "~~" + custVO.getEroi()+ "~~" + custVO.getZipcode()%>">
                                <%=custVO.getName()%></option>

                            <%                                                                    }
                            } else {
                            %>
                            <option value="">New Customer</option>
                            <%                                                                }%>
                            <option value="new">New Customer</option>
                        </select>

                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_Warehousename" styleId="ebook_Warehousename"  maxlength="150" styleClass="form-control txt_borderno " />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_Warehouseaddress" styleId="ebook_Warehouseaddress"  styleClass="form-control txt_borderno " maxlength="150" />
                    </div>


                </div>

            </div>

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_Warehouseaddress1" styleId="ebook_Warehouseaddress1"  styleClass="form-control txt_borderno" maxlength="150" />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Warehousecity_desc" styleId="ebook_Warehousecity_desc"  styleClass="form-control txt_borderno city_new_shipper"  maxlength="100"  onblur="setCityDesc_New('ebook_Warehousecity_desc','ebook_Warehousecity')"/>
                    </div>


                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Warehousecountry_name" styleId="ebook_Warehousecountry_name"  styleClass="form-control txt_borderno  country_new_Warehouse"  maxlength="100" onchange="loadStateAutoComplete('ebook_Warehousecountry_name', 'ebook_Warehousestate', 'typeheadhidden13');" onblur="setCountry_New('ebook_Warehousecountry_name','ebook_Warehousecountry');"/>
                    </div>

                </div>

            </div>


            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Warehousephone" styleId="ebook_Warehousephone" styleClass="form-control txt_borderno"  maxlength="20" />
                    </div>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Warehousefax" styleId="ebook_Warehousefax"  styleClass="form-control txt_borderno" maxlength="20" />

                    </div>


                    <%} else {%>

                    <html:hidden property="ebook_Warehousefax" styleId="ebook_Warehousefax"   styleClass="form-control txt_borderno" />

                    <%}%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Warehouseemail" styleId="ebook_Warehouseemail" styleClass="form-control email txt_borderno"  maxlength="75" />

                    </div>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                        <div class="col-xs-12 col-sm-4">
                           <html:text property="ebook_Warehousezipcode" styleId="ebook_Warehousezipcode"  styleClass="form-control zip_code txt_borderno"  maxlength="15" />
                        </div>
                        <%}%>
                       

                </div>

            </div>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Warehousestate" styleId="ebook_Warehousestate"  styleClass="form-control txt_borderno ebooking_state"  maxlength="100" onclick="validateState('ebook_Warehousecountry_name',this);" />
                    </div>
                     <%if("Y".equalsIgnoreCase(GENS)){%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Warehouseeori" styleId="ebook_Warehouseeori"  styleClass="form-control txt_borderno ebooking_eori" style="display: block;" maxlength="17" />
                    </div>
                    <div class="col-xs-12 col-sm-4">
                         <html:select property="Warehousetypeperson" styleId="Warehousetypeperson" styleClass="form-control txt_borderno" style="display: block;">
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

        <%}%>
          <% if ("Y".equalsIgnoreCase(TENS) && "N".equalsIgnoreCase(TENS_Documentation)) {%> 
        <% if (custtypelist != null && custtypelist.contains("SL")) {%>
        <div class="section_header"> <bean:message key='ecom.bkg.custsl' bundle="<%=clientcode%>"/>
            <span class="float-right">
                <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="Sellersearch" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'Seller')" readonly="readonly"><i class="fa fa-search"></i></a>
                <button type="button" class="toggle-btn collapseSeller " data-toggle="collapse" data-target="#collapseSeller" collapsed=""> <i class="fa fa-plus"> </i> </button>
            </span>
        </div>

        <div class="col-sm-12 collapse " id="collapseSeller" >
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <select id="SellerSelect" name="SellerSelect" class="form-control txt_borderno" onchange="setCustDetails(this.value, 'Seller')">
                            <%
                                if (slList != null) {
                            %>
                            <option value="">Select Customer</option>
                            <%
                                for (int i = 0; i < slList.size(); i++) {
                                    custVO = (CustomerDetailsVO) slList.get(i);
                            %>
                                <option value="<%=custVO.getName() + "~~" + custVO.getCustCode() + "~~" + custVO.getAddress() + "~~" + custVO.getCity() + "~~" + custVO.getCountry() + "~~" + custVO.getCountryCode() + "~~" + custVO.getEmail() + "~~" + custVO.getPhoneNo() + "~~" + custVO.getFax() + "~~" + custVO.getAddress1() + "~~" + custVO.getState()+ "~~" + custVO.getPerontype()+ "~~" + custVO.getEroi()+ "~~" + custVO.getZipcode()%>">
                                <%=custVO.getName()%></option>
                           
                                <%                                                                    }
                                } else {
                                %>
                            <option value="">New Customer</option>
                            <%}%>
                            <option value="new">New Customer</option>
                        </select>
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_Sellername" styleId="ebook_Sellername"  maxlength="150" styleClass="form-control txt_borderno " />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_Selleraddress" styleId="ebook_Selleraddress"  styleClass="form-control txt_borderno " maxlength="150" />
                    </div>

                </div>
            </div>

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_Selleraddress1" styleId="ebook_Selleraddress1"  styleClass="form-control txt_borderno" maxlength="150" />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Sellercity_desc" styleId="ebook_Sellercity_desc"  styleClass="form-control txt_borderno city_new_shipper"  maxlength="100"  onblur="setCityDesc_New('ebook_Sellercity_desc','ebook_Sellercity')"/>
                    </div>


                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Sellercountry_name" styleId="ebook_Sellercountry_name"  styleClass="form-control txt_borderno  country_new_Seller"  maxlength="100" onchange="loadStateAutoComplete('ebook_Sellercountry_name', 'ebook_Sellerstate', 'typeheadhidden15');" onblur="setCountry_New('ebook_Sellercountry_name','ebook_Sellercountry');"/>
                    </div>
                </div>    
            </div>  

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Sellerphone" styleId="ebook_Sellerphone" styleClass="form-control txt_borderno"  maxlength="20" />
                    </div>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Sellerfax" styleId="ebook_Sellerfax"  styleClass="form-control txt_borderno" maxlength="20" />

                    </div>


                    <%} else {%>

                    <html:hidden property="ebook_Sellerfax" styleId="ebook_Sellerfax"   styleClass="form-control txt_borderno" />

                    <%}%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Selleremail" styleId="ebook_Selleremail" styleClass="form-control email txt_borderno"  maxlength="75" />
                    </div>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                        <div class="col-xs-12 col-sm-4">
                           <html:text property="ebook_Sellerzipcode" styleId="ebook_Sellerzipcode"  styleClass="form-control zip_code txt_borderno"  maxlength="15" />
                        </div>
                    <%}%>
                      
                </div>  
            </div>        

            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Sellerstate" styleId="ebook_Sellerstate"  styleClass="form-control txt_borderno ebooking_state"  maxlength="100" onclick="validateState('ebook_Sellercountry_name',this);" />
                    </div>
                     <%if("Y".equalsIgnoreCase(GENS)){%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Sellereori" styleId="ebook_Sellereori"  styleClass="form-control txt_borderno ebooking_eori" style="display: block;" maxlength="17" />
                    </div>
                    <div class="col-xs-12 col-sm-4">
                         <html:select property="ebook_Sellerpersontype" styleId="Sellertypeperson" styleClass="form-control txt_borderno" style="display: block;">
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
        <%}%>
	<% if (custtypelist != null && custtypelist.contains("BY")) {%>
        <div class="section_header"> <bean:message key='ecom.bkg.custby' bundle="<%=clientcode%>"/>
            <span class="float-right">
                <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="Buyersearch" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'Buyer')" readonly="readonly"><i class="fa fa-search"></i></a>
                <button type="button" class="toggle-btn collapseBuyer " data-toggle="collapse" data-target="#collapseBuyer" collapsed=""> <i class="fa fa-plus"> </i> </button>
            </span>
        </div>

        <div class="col-sm-12 collapse " id="collapseBuyer"  >
        <div >
            <div class="form-group" >
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <select id="BuyerSelect" name="BuyerSelect" class="form-control txt_borderno" onchange="setCustDetails(this.value, 'Buyer')">
                            <%
                                if (byList != null) {
                            %>
                            <option value="">Select Customer</option>
                            <%
                                for (int i = 0; i < byList.size(); i++) {
                                    custVO = (CustomerDetailsVO) byList.get(i);
                            %>
                                <option value="<%=custVO.getName() + "~~" + custVO.getCustCode() + "~~" + custVO.getAddress() + "~~" + custVO.getCity() + "~~" + custVO.getCountry() + "~~" + custVO.getCountryCode() + "~~" + custVO.getEmail() + "~~" + custVO.getPhoneNo() + "~~" + custVO.getFax() + "~~" + custVO.getAddress1() + "~~" + custVO.getState()+ "~~" + custVO.getPerontype()+ "~~" + custVO.getEroi()+ "~~" + custVO.getZipcode()%>">
                                <%=custVO.getName()%></option>
                                <%  }
                                } else {
                                %>
                            <option value="">New Customer</option>
                            <%}%>
                            <option value="new">New Customer</option>
                        </select>
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_Buyername" styleId="ebook_Buyername"  maxlength="150" styleClass="form-control txt_borderno " />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_Buyeraddress" styleId="ebook_Buyeraddress"  styleClass="form-control txt_borderno " maxlength="150" />
                    </div>

                </div>
            </div>

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text  property="ebook_Buyeraddress1" styleId="ebook_Buyeraddress1"  styleClass="form-control txt_borderno" maxlength="150" />
                    </div>

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Buyercity_desc" styleId="ebook_Buyercity_desc"  styleClass="form-control txt_borderno city_new_shipper"  maxlength="100"  onblur="setCityDesc_New('ebook_Buyercity_desc','ebook_Buyercity')"/>
                    </div>


                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Buyercountry_name" styleId="ebook_Buyercountry_name"  styleClass="form-control txt_borderno  country_new_Buyer"  maxlength="100" onchange="loadStateAutoComplete('ebook_Buyercountry_name', 'ebook_Buyerstate', 'typeheadhidden16');" onblur="setCountry_New('ebook_Buyercountry_name','ebook_Buyercountry');"/>
                    </div>
                </div>    
            </div>  

            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Buyerphone" styleId="ebook_Buyerphone" styleClass="form-control txt_borderno"  maxlength="20" />
                    </div>
                    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Buyerfax" styleId="ebook_Buyerfax"  styleClass="form-control txt_borderno" maxlength="20" />

                    </div>


                    <%} else {%>

                    <html:hidden property="ebook_Buyerfax" styleId="ebook_Buyerfax"   styleClass="form-control txt_borderno" />

                    <%}%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Buyeremail" styleId="ebook_Buyeremail" styleClass="form-control email txt_borderno"  maxlength="75" />
                    </div>
                    <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                        <div class="col-xs-12 col-sm-4">
                           <html:text property="ebook_Buyerzipcode" styleId="ebook_Buyerzipcode"  styleClass="form-control zip_code txt_borderno"  maxlength="15" />
                        </div>
                    <%} %>
                      
                </div>  
            </div>        

            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Buyerstate" styleId="ebook_Buyerstate"  styleClass="form-control txt_borderno ebooking_state"  maxlength="100" onclick="validateState('ebook_Buyercountry_name',this);" />
                    </div>
                     <%if("Y".equalsIgnoreCase(GENS)){%>
                    <div class="col-xs-12 col-sm-4">
                        <html:text property="ebook_Buyereori" styleId="ebook_Buyereori"  styleClass="form-control txt_borderno ebooking_eori" style="display: block;" maxlength="17" />
                    </div>
                    <div class="col-xs-12 col-sm-4">
                         <html:select property="ebook_Buyerpersontype" styleId="Buyertypeperson" styleClass="form-control txt_borderno" style="display: block;">
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
        <%}%>
        <%} if("Y".equalsIgnoreCase(TENS_Documentation)){%> 
             <% if (custtypelist != null && custtypelist.contains("SL")) {%>
             <div class="section_header"> <bean:message key='ecom.bkg.custsl' bundle="<%=clientcode%>"/>
            <span class="float-right">
                <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="dySellercustsearch1" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'dySeller','1')" readonly="readonly"><i class="fa fa-search"></i></a>
                <button type="button" class="toggle-btn collapseSeller " data-toggle="collapse" data-target="#collapseSeller" collapsed=""> <i class="fa fa-plus"> </i> </button>
            </span>
           </div>

              <div class="col-sm-12 collapse " id="collapseSeller" >
            <div id="SellerContainer">
           
          </div>
        
          <input type="hidden" id="SellerCount" name="SellerCount" value="0" />
           <div class="col-12 col-sm-12 text-right">
            <a href='javascript:void(0)' onclick="addCustRowAjax('Seller','')" class="btn new_add" id="addSellerBtn" >
                <i class="fa fa-plus"></i>
                <bean:message key="ecom.bkg.but.add" bundle="<%=clientcode%>"/>
            </a>
          </div>
          </div>
        <%}%>
            <% if (custtypelist != null && custtypelist.contains("BY")) {%>
        <div class="section_header"> <bean:message key='ecom.bkg.custby' bundle="<%=clientcode%>"/>
            <span class="float-right">
                <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="dyBuyercustsearch1" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'dyBuyer','1')" readonly="readonly"><i class="fa fa-search"></i></a>
                <button type="button" class="toggle-btn collapseBuyer " data-toggle="collapse" data-target="#collapseBuyer" collapsed=""> <i class="fa fa-plus"> </i> </button>
            </span>
        </div>

        <div class="col-sm-12 collapse " id="collapseBuyer"  >
              <div id="BuyerContainer">
            <!-- Default first row can be injected here via JS on page load or server-side -->
        </div>
        
        <input type="hidden" id="BuyerCount" name="BuyerCount" value="0" />
        <div class="col-12 col-sm-12 text-right">
            <a href='javascript:void(0)' onclick="addCustRowAjax('Buyer','')" class="btn new_add" id="addBuyerBtn" >
                <i class="fa fa-plus"></i>
                <bean:message key="ecom.bkg.but.add" bundle="<%=clientcode%>"/>
            </a>
        </div>
        </div>
        <%}%>
            <%}%>
    </div>
</fieldset>

<%} catch (Exception e) {
        e.printStackTrace();
    }%>

