<%-- 
    Document   : SIBLCommonENS
    Created on : 14 Nov, 2024, 3:30:32 PM
    Author     : gobinath.t
--%>
<%@page contentType="text/html; charset=UTF-8" import="net.ecom.common.GeneralConf,java.util.List,net.ecom.vo.CustomerDetailsVO"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>


<%
    String GENS = (String) getServletContext().getAttribute(GeneralConf.GENS);
    String enableENSTab = (String) session.getAttribute("IsEnableENSTabInSIBL");
    if ("Y".equalsIgnoreCase(GENS) && "Y".equalsIgnoreCase(enableENSTab)) {
        String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);
        String Enable_Zip_Code = (String) getServletContext().getAttribute(GeneralConf.Enable_Zip_Code);
        String clientcode = (String) session.getAttribute("clientcode");
        List byList = (List) session.getAttribute("BYCUSTLIST_SI");
        List slList = (List) session.getAttribute("SLCUSTLIST_SI");
        CustomerDetailsVO custVO = null;
%>

<h6><i class="step-icon fa fa-file-text-o"></i> <bean:message key="shipping.ens.heading" bundle="<%=clientcode%>"/> </h6>

<fieldset> 
    <div class="details_search " id="GENS_TAB">

        <div class="header_tle">  <bean:message key="shipping.ens.heading" bundle="<%=clientcode%>"/>  </div>

        <div class="row form-group">

            <label class="col-sm-6 control-label single-row-radio"> <bean:message key="shipping.ens.eu.customs.zone" bundle="<%=clientcode%>"/> 
                <div class="col-sm-2 single-row-radio">
                    <label>
                        <html:radio property="euCustZone" value="Y" styleId="euCustZoneY" styleClass="form-control medium-radio" />
                        Yes
                    </label>
                    <label>
                        <html:radio property="euCustZone" value="N" styleId="euCustZoneN" styleClass="form-control medium-radio"/>
                        No
                    </label>
                </div>   
            </label>

        </div>
        <div class="row form-group">
            <label class="col-sm-1 control-label"> <bean:message key="shipping.ens.type.of.bl" bundle="<%=clientcode%>"/>  </label>

            <div class="col-sm-3 ">
                <div class="btn-group ens_btn" id="blTypeENS_btn">
                    <button type="button" class="btn btn-primary straightbl">Straight BL</button>
                    <button type="button" class="btn btn-primary masterbl">Master BL</button>
                </div>
                <html:hidden property="blTypeENS" styleId="blTypeENS"/>
                <!-- <html1:select property="blTypeENS" styleId="blTypeENS" styleClass="form-control light_border">
                     <html1:option value="Straight BL">Straight BL</html1:option>
                     <html1:option value="Master BL">Master BL</html1:option>
                 </html1:select>-->

            </div>

            <label class="col-sm-1 control-label"> <bean:message key="shipping.ens.type.ens.filling" bundle="<%=clientcode%>"/>  </label>

            <div class="col-sm-3 ">
                <div class="btn-group ens_btn" id="ensFillingType_btn">
                    <button type="button" class="btn btn-primary singlefiling">Single Filing</button>
                    <button type="button" class="btn btn-primary multiplefiling" >Multiple Filing</button>
                </div>
                <html:hidden property="ensFillingType" styleId="ensFillingType" onchange="enableMultiDeclarant('Y')" />
                <!--<html1:select property="ensFillingType" styleId="ensFillingType" styleClass="form-control light_border" onchange="enableMultiDeclarant('Y')">
                    <html1:option value="Single Filing">Single Filing</html1:option>
                    <html1:option value="Multiple Filing">Multiple Filing</html1:option>
                </html1:select>-->

            </div>

            <label class="col-sm-1 control-label"> <bean:message key="shipping.ens.method.of.payment" bundle="<%=clientcode%>"/>  </label>

            <div class="col-sm-3 ">
                <div class="btn-group ens_btn" id="paymentMethod_btn">
                    <button type="button" class="btn btn-primary wiretransfer">Wire Transfer</button>
                    <button type="button" class="btn btn-primary notprepaid">Not Prepaid</button>
                </div>
                <html:hidden property="paymentMethod" styleId="paymentMethod"/>
                <!--<html1:select property="paymentMethod" styleId="paymentMethod" styleClass="form-control light_border">
                    <html1:option value="Wire Transfer">Wire Transfer</html1:option>
                    <html1:option value="Not Prepaid">Not Prepaid</html1:option>
                </html1:select>-->

            </div>

        </div>
        <div id="supplementarydeclarant" style="display: none">
            <div class="header_tle">  <bean:message key="shipping.ens.supplementary.declarant" bundle="<%=clientcode%>"/> 
                <span class="pull-right btn btn-success search_symbol btn-xs" style="margin-top: -7px;" onclick='callsearchmultifilingmethodsi()' >
                    <i class="fa fa-search"></i>
                </span>
            </div>
            <div class="row form-group">
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.name' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                    <html:text property="declarantName" styleId="declarantName" styleClass="form-control setdata light_border declarantcomclass" maxlength="150" />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.address' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                    <html:text property="declarantAddress" styleId="declarantAddress" styleClass="form-control setdata light_border declarantcomclass" maxlength="150" />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.address1' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                    <html:text property="declarantAddress1" styleId="declarantAddress1" styleClass="form-control setdata light_border declarantcomclass" maxlength="150" />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.city' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                    <html:text property="declarantCity" styleId="declarantCity" styleClass="form-control setdata light_border declarantcomclass" maxlength="150" />
                </div>
                <%if("Y".equalsIgnoreCase(Enable_Zip_Code)){%>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.zipcode' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                    <html:text property="declarantZipcode" styleId="declarantZipcode" styleClass="form-control setdata light_border declarantcomclass" maxlength="15"  />
                </div>
                <%}%>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.country' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                    <html:text property="declarantCountry" styleId="declarantCountry" styleClass="form-control setdata light_border declarantcomclass" maxlength="50" />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.state' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                    <html:text property="declarantState" styleId="declarantState" styleClass="form-control setdata light_border declarantcomclass" maxlength="50" />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.telephone' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                    <html:text property="declarantTelephone" styleId="declarantTelephone" styleClass="form-control setdata light_border declarantcomclass" maxlength="25"  />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.eori' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                    <html:text property="declarantEORI" styleId="declarantEORI" styleClass="form-control setdata light_border declarantcomclass" maxlength="20"  />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.email' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                    <html:text property="declarantEmail" styleId="declarantEmail" styleClass="form-control setdata email light_border declarantcomclass" maxlength="100"  />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label class="control-label"><bean:message key='shipping.ens.declarant.fillingtype' bundle="<%=clientcode%>"/> <span class="mandatory">* </span> </label>
                    <div class="single-row-radio">
                        <label>
                            <html:radio property="declarantFillingType" value="House BL" styleId="declarantFillingTypeHBL" styleClass="form-control medium-radio light_border " />
                            House BL
                        </label>
                        <label>
                            <html:radio property="declarantFillingType" value="Sub-House BL" styleId="declarantFillingTypeSHBL" styleClass="form-control medium-radio light_border " />
                            Sub-House BL
                        </label>
                    </div>

                </div>
            </div>
        </div>

        <div id="buyerseller">
            <div class="header_tle">  Buyer and Seller  </div>
            <div class="row">

                <div class="col-md-12">

                    <div class="panel_right">

                        <div class="custom-panel-head"> <bean:message key="shipping.ens.declarant.buyer" bundle="<%=clientcode%>"/>

                            <span class="pull-right" style="margin-top: -7px;"> <a href="javascript:void(0)" onclick="callsearchmethodsi('ebooking', 'esiSearch', 'buyer')" class="btn btn-success search_symbol btn-xs" id="buyersearch" > <i class="fa fa-search"></i></a> 

                                <a data-toggle="collapse" class="toggle-btn"   href="javascript:void(0)" onclick="openCollapseFun('collapseBuyer');" data-target="#collapseBuyer" aria-expanded="false" aria-controls="collapseBuyer"> <i class="fa fa-minus-square-o"></i> </a>

                            </span>

                        </div>

                        <div class="collapse show" id="collapseBuyer">

                            <div class="row form-group">

                                <div class="col-sm-1"> </div>

                                <div class="col-sm-3">
                                    <select id="buyerSelect" name="buyerSelect" class="form-control input_si" onchange="setCustDetails(this.value, 'buyer')">
                                        <%
                                            if (byList != null) {
                                        %>
                                        <option value="">Select Customer</option>
                                        <%
                                            for (int i = 0; i < byList.size(); i++) {
                                                custVO = (CustomerDetailsVO) byList.get(i);

                                        %>

                                        <option value="<%=custVO.getName()%>~~<%=custVO.getCustCode()%>~~<%=custVO.getAddress()%>~~<%=custVO.getCity()%>~~<%=custVO.getCountry()%>~~<%=custVO.getCountryCode()%>~~<%=custVO.getEmail()%>~~<%=custVO.getPhoneNo()%>~~<%=custVO.getFax()%>~~<%=custVO.getAddress1()%>~~<%=custVO.getState()%>~~<%=custVO.getZipcode()%>~~<%=custVO.getTaxId()%>~~<%=custVO.getPerontype()%>~~<%=custVO.getEroi()%>~~<%=custVO.getZipcode()%>">
                                            <%=custVO.getName()%></option>
                                        <%                                                                    }
                                            }
                                        %>

                                        <option value="new">New Customer</option>
                                    </select>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:hidden property="byCustCode" styleId="buyerCustCode"/>
                                    <html:text  property="byname" styleId="ebook_buyername" styleClass="form-control  input_si byslmandatory SI_Process_Restriction_Cust byslmandatory" maxlength="150"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="byaddr" styleId="ebook_buyeraddress" styleClass="form-control  input_si SI_Process_Restriction_Cust byslmandatory" maxlength="150"/>

                                </div>

                            </div>

                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text  property="byaddr1" styleId="ebook_buyeraddress1" styleClass="form-control  input_si SI_Process_Restriction_Cust" maxlength="150"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text property="bycity" styleId="ebook_buyercity"  styleClass="form-control  input_si SI_Process_Restriction_Cust byslmandatory" maxlength="150"/>
                                    <html:hidden property="byCityName" styleId="ebook_buyercity_desc"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:hidden property="bycountry" styleId="ebook_buyercountry" />
                                    <html:text  property="byCountryName" styleId="ebook_buyercountry_name" styleClass="form-control  input_si SI_Process_Restriction_Cust byslmandatory" onchange="loadStateAutoComplete('ebook_buyercountry_name', 'ebook_buyerstate', 'typeheadhidden16');"/>
                                </div>

                            </div>


                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="byphone" styleId="ebook_buyerphone" styleClass="form-control input_si SI_Process_Restriction_Cust" maxlength="20"/>

                                </div>

                                <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="byfax" styleId="ebook_buyerfax" styleClass="form-control input_si SI_Process_Restriction_Cust" maxlength="20"/>

                                </div>
                                <%} else {%>

                                <html:hidden property="byfax" styleId="ebook_buyerfax"   styleClass="form-control input_si SI_Process_Restriction_Cust" />

                                <%}%>
                                <%if("Y".equalsIgnoreCase(Enable_Zip_Code)){%>
                                <label class="col-sm-1 control-label"><bean:message key='shipping.ens.declarant.zipcode' bundle="<%=clientcode%>"/> </label>
                                <div class="col-sm-3">
                                    <html:text property="byzipcode" styleId="ebook_buyerzipcode" styleClass="form-control input_si SI_Process_Restriction_Cust" maxlength="15"/>
                                </div>
                                <%}else{%>
                                    <html:hidden property="byzipcode" styleId="ebook_buyerzipcode" styleClass="form-control input_si SI_Process_Restriction_Cust" />
                                <%}%>
                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text size="43" property="byemail" styleId="ebook_buyeremail" styleClass="form-control email input_si SI_Process_Restriction_Cust" maxlength="75"/>

                                </div>

                            </div>
                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="bystate" styleId="ebook_buyerstate" styleClass="form-control input_si SIBL_state SI_Process_Restriction_Cust" onclick="validateState('ebook_buyercountry_name',this);" />

                                </div>
                                <%if ("Y".equalsIgnoreCase(GENS)) {%>
                                <label class="col-sm-1 control-label ebook_buyerdiv"  ><bean:message key="Reg.Eori" bundle="<%=clientcode%>"/> </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="byeori" styleId="ebook_buyereori" styleClass="form-control  input_si ebook_buyerdiv"  maxlength="17"  />
                                </div>
                                <label class="col-sm-1 control-label ebook_buyerdiv" ><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/> </label>

                                <div class="col-sm-3">
                                    <html:select property="bypersontype" styleId="buyertypeperson" styleClass="form-control  input_si ebook_buyerdiv"  >
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
            <div class="row">

                <div class="col-md-12">

                    <div class="panel_right">

                        <div class="custom-panel-head"> <bean:message key="shipping.ens.declarant.seller" bundle="<%=clientcode%>"/>

                            <span class="pull-right" style="margin-top: -7px;"> <a href="javascript:void(0)" onclick="callsearchmethodsi('ebooking', 'esiSearch', 'seller')" class="btn btn-success search_symbol btn-xs" id="sellersearch" > <i class="fa fa-search"></i></a> 

                                <a data-toggle="collapse" class="toggle-btn"   href="javascript:void(0)" onclick="openCollapseFun('collapseSeller');" data-target="#collapseSeller" aria-expanded="false" aria-controls="collapseSeller"> <i class="fa fa-minus-square-o"></i> </a>

                            </span>

                        </div>

                        <div class="collapse show" id="collapseSeller">

                            <div class="row form-group">

                                <div class="col-sm-1"> </div>

                                <div class="col-sm-3">
                                    <select id="sellerSelect" name="sellerSelect" class="form-control input_si" onchange="setCustDetails(this.value, 'seller')">
                                        <%
                                            if (slList != null) {
                                        %>
                                        <option value="">Select Customer</option>
                                        <%
                                            for (int i = 0; i < slList.size(); i++) {
                                                custVO = (CustomerDetailsVO) slList.get(i);

                                        %>
                                        <option value="<%=custVO.getName()%>~~<%=custVO.getCustCode()%>~~<%=custVO.getAddress()%>~~<%=custVO.getCity()%>~~<%=custVO.getCountry()%>~~<%=custVO.getCountryCode()%>~~<%=custVO.getEmail()%>~~<%=custVO.getPhoneNo()%>~~<%=custVO.getFax()%>~~<%=custVO.getAddress1()%>~~<%=custVO.getState()%>~~<%=custVO.getZipcode()%>~~<%=custVO.getTaxId()%>~~<%=custVO.getPerontype()%>~~<%=custVO.getEroi()%>~~<%=custVO.getZipcode()%>">
                                            <%=custVO.getName()%></option>

                                        <%                                                                    }
                                            }
                                        %>

                                        <option value="new">New Customer</option>
                                    </select>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:hidden property="slCustCode" styleId="sellerCustCode"/>
                                    <html:text  property="slname" styleId="ebook_sellername" styleClass="form-control  input_si SI_Process_Restriction_Cust byslmandatory" maxlength="150"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="sladdr" styleId="ebook_selleraddress" styleClass="form-control  input_si SI_Process_Restriction_Cust byslmandatory" maxlength="150"/>

                                </div>

                            </div>

                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text  property="sladdr1" styleId="ebook_selleraddress1" styleClass="form-control  input_si SI_Process_Restriction_Cust" maxlength="150"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:text property="slcity" styleId="ebook_sellercity"  styleClass="form-control  input_si SI_Process_Restriction_Cust byslmandatory" maxlength="150"/>
                                    <html:hidden property="slCityName" styleId="ebook_sellercity_desc"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>

                                <div class="col-sm-3 ">
                                    <html:hidden property="slcountry" styleId="ebook_sellercountry" />
                                    <html:text  property="slCountryName" styleId="ebook_sellercountry_name" styleClass="form-control  input_si SI_Process_Restriction_Cust byslmandatory" onchange="loadStateAutoComplete('ebook_sellercountry_name', 'ebook_sellerstate', 'typeheadhidden17');"/>
                                </div>

                            </div>


                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="slphone" styleId="ebook_sellerphone" styleClass="form-control input_si SI_Process_Restriction_Cust" maxlength="20"/>

                                </div>

                                <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="slfax" styleId="ebook_sellerfax" styleClass="form-control input_si SI_Process_Restriction_Cust" maxlength="20"/>

                                </div>
                                <%} else {%>

                                <html:hidden property="slfax" styleId="ebook_sellerfax"   styleClass="form-control input_si SI_Process_Restriction_Cust" />

                                <%}%>
                                <%if("Y".equalsIgnoreCase(Enable_Zip_Code)){%>
                                <label class="col-sm-1 control-label"><bean:message key='shipping.ens.declarant.zipcode' bundle="<%=clientcode%>"/> </label>
                                <div class="col-sm-3">
                                    <html:text property="slzipcode" styleId="ebook_sellerzipcode" styleClass="form-control input_si SI_Process_Restriction_Cust" maxlength="15"/>
                                </div>
                                <%}else{%>
                                    <html:hidden property="slzipcode" styleId="ebook_sellerzipcode" styleClass="form-control input_si SI_Process_Restriction_Cust" />
                                <%}%>
                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text size="43" property="slemail" styleId="ebook_selleremail" styleClass="form-control email input_si SI_Process_Restriction_Cust" maxlength="75"/>

                                </div>

                            </div>
                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="slstate" styleId="ebook_sellerstate" styleClass="form-control input_si SIBL_state SI_Process_Restriction_Cust" onclick="validateState('ebook_sellercountry_name',this);" />

                                </div>
                                <%if ("Y".equalsIgnoreCase(GENS)) {%>
                                <label class="col-sm-1 control-label ebook_sellerdiv" ><bean:message key="Reg.Eori" bundle="<%=clientcode%>"/> </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="sleori" styleId="ebook_sellereori" styleClass="form-control  input_si ebook_sellerdiv"  maxlength="17"  />
                                </div>
                                <label class="col-sm-1 control-label ebook_sellerdiv" ><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/> </label>

                                <div class="col-sm-3">
                                    <html:select property="slpersontype" styleId="sellertypeperson" styleClass="form-control  input_si ebook_sellerdiv"   >
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
        </div>
        <div class="col-lg-12"> 
            <span class="lbl_clr_txt font-weight-bold text-danger"> <bean:message key="point1.ens.text" bundle="<%=clientcode%>"/>
                <br>
                <bean:message key="point2.ens.text" bundle="<%=clientcode%>"/>
                <br>
                <bean:message key="point3.ens.text" bundle="<%=clientcode%>"/>
            </span> 
        </div>
    </div>
    <script>
        $(document).ready(function () {
            var isTriggering = false;
            $(".btn-group > button").click(function () {
                if (isTriggering) {
                    return;
                }
                console.log('paymentMethod click value==> ' + $('#paymentMethod').val());
                console.log('blTypeENS_btn click value==> ' + $('#blTypeENS').val());
                $(this).parent('.btn-group').find('button').removeClass("active");
                $(this).addClass("active");
                if ($(this).parent().is('#paymentMethod_btn')) {
                    if ($(this).is('.wiretransfer')) {
                        $('#paymentMethod').val('Wire Transfer');
                    } else if ($(this).is('.notprepaid')) {
                        $('#paymentMethod').val('Not Prepaid');
                    }
                } else if ($(this).parent().is('#blTypeENS_btn')) {
                    if ($(this).is('.straightbl')) {
                        $('#blTypeENS').val('Straight BL');
                        isTriggering = true;
                        clickENSFillingType('singlefiling');          
                        $('#ensFillingType').val('Single Filing').trigger('change');
                       isTriggering = false;
                    } else if ($(this).is('.masterbl')) {
                        $('#blTypeENS').val('Master BL');
                        isTriggering = true;
                        clickENSFillingType('multiplefiling');
                        $('#ensFillingType').val('Multiple Filing').trigger('change');
                      isTriggering = false;
                    }
                } else if ($(this).parent().is('#ensFillingType_btn')) {
                    if ($(this).is('.singlefiling')) {
                        $('#ensFillingType').val('Single Filing').trigger('change');
                        isTriggering = true;
                        clickBLTypeENS('straightbl');
                        $('#blTypeENS').val('Straight BL');
                       isTriggering = false; 
                    } else if ($(this).is('.multiplefiling')) {
                        $('#ensFillingType').val('Multiple Filing').trigger('change');
                        isTriggering = true;
                        clickBLTypeENS('masterbl');
                        $('#blTypeENS').val('Master BL');
                     isTriggering = false;
                    }
                }
                console.log('paymentMethod click==> ' + $('#paymentMethod').val());
                console.log('blTypeENS click==> ' + $('#blTypeENS').val());
            });

            if ($('#paymentMethod').val() == 'Wire Transfer') {
                $('#paymentMethod_btn > .wiretransfer').addClass("active");
            } else if ($('#paymentMethod').val() == 'Not Prepaid') {
                $('#paymentMethod_btn > .notprepaid').addClass("active");
            } else {
                $('#paymentMethod_btn > .wiretransfer').addClass("active");
                $('#paymentMethod').val('Wire Transfer');
            }

            if ($('#blTypeENS').val() == 'Straight BL') {
                $('#blTypeENS_btn > .straightbl').addClass("active");
            } else if ($('#blTypeENS').val() == 'Master BL') {
                $('#blTypeENS_btn > .masterbl').addClass("active");
            } else {
                $('#blTypeENS_btn > .straightbl').addClass("active");
                $('#blTypeENS').val('Straight BL');
            }
            console.log("ensFillingType**===>", $('#ensFillingType').val());
            if ($('#ensFillingType').val() == 'Single Filing') {
                $('#ensFillingType_btn > .singlefiling').addClass("active");
            } else if ($('#ensFillingType').val() == 'Multiple Filing') {
                $('#ensFillingType_btn > .multiplefiling').addClass("active");
            } else {
                $('#ensFillingType_btn > .singlefiling').addClass("active");
                $('#ensFillingType').val('Single Filing');
            }

        });
        function clickENSFillingType(type) {
            $('#ensFillingType_btn button').removeClass("active");
            $('#ensFillingType_btn > .' + type).addClass("active").trigger('click');
        }
        function clickBLTypeENS(type) {
            $('#blTypeENS_btn button').removeClass("active");
            $('#blTypeENS_btn > .' + type).addClass("active").trigger('click');
        }

    </script>
</fieldset>
<%}%>
