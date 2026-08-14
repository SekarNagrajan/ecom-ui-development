<%-- 
    Document   : CommonENS
    Created on : 16 June, 2025
    Author     : preethi.m
--%>
<%@page contentType="text/html; charset=UTF-8" import="net.ecom.common.GeneralConf,java.util.List,net.ecom.vo.CustomerDetailsVO"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>


<%
    String GENS = (String) getServletContext().getAttribute(GeneralConf.GENS);
    if ("Y".equalsIgnoreCase(GENS) ) {
        String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);
        String clientcode = (String) session.getAttribute("clientcode");
        List slList = (List) session.getAttribute("SLCUSTLIST");
        List byList = (List) session.getAttribute("BYCUSTLIST");
        CustomerDetailsVO custVO = null;
        long jsdate = new java.util.Date().getTime();
        String Enable_Zip_Code = (String) getServletContext().getAttribute(GeneralConf.Enable_Zip_Code);
%>
<script type='text/javascript' >

    function callsearchmultifilingmethodsi() {
        var apCustCode = $('#ebook_agreecode').val();
        $.ajax({
            type: "POST",
            cache: false,
            url: "SIAjax?type=esimultifilingSearch&apcustcode=" + encodeURIComponent(apCustCode),
            data: $(this).serializeArray(),
            success: function (data) {
                openFancyBox(data);
            },
            error: function (data, xhr) {
                if (xhr.status == 403) {
                    window.location.href = "SessionExpired.jsp";
                }
                alert(errorMsg);
            }
        });
    }
    function setMultifilingDetails(custName, address, address1, country, telephone, EORI, email, city, state, filingtype,zipcode) {
    $("#declarantName").val(custName || "");
    $("#declarantAddress").val(address || "");
    $("#declarantAddress1").val(address1 || "");
    $("#declarantCity").val(city || "");
    $("#declarantCountry").val(country || "");
    $("#declarantCountry").typeahead("val", country);
    $("#declarantState").val(state || "");
    $("#declarantTelephone").val(telephone || "");
    $("#declarantEORI").val(EORI || "");
    $("#declarantEmail").val(email || "");
    $("#declarantZipcode").val(zipcode || "");
    if (filingtype) {
        $(`input[name='declarantFillingType'][value='${filingtype}']`).prop("checked", true);
    }
    $.fancybox.close();
}
</script>

<h6><i class="step-icon fa fa-file-text-o"></i> <bean:message key="shipping.ens.heading" bundle="<%=clientcode%>"/> </h6>

<fieldset> 
    <div class="details_search details_search txt_box_book" id="GENS_TAB">
<span id="GENS_TAB_Fields">
        <div class="header_tle">  <bean:message key="shipping.ens.heading" bundle="<%=clientcode%>"/>  </div>

        <div class="row form-group">

            <label class="col-sm-6 control-label single-row-radio"> <bean:message key="shipping.ens.eu.customs.zone" bundle="<%=clientcode%>"/> 
                <div class="col-sm-2 single-row-radio">
                    <label>
                        <html:radio property="euCustZone" value="Y" styleId="euCustZoneY" styleClass="form-control medium-radio ensbuyersellerclass"  />
                        Yes
                    </label>
                    <label>
                        <html:radio property="euCustZone" value="N" styleId="euCustZoneN" styleClass="form-control medium-radio ensbuyersellerclass"/>
                        No
                    </label>
                </div>   
            </label>

        </div>
        <div class="row form-group">
            <label class="col-sm-1 control-label"> <bean:message key="shipping.ens.type.of.bl" bundle="<%=clientcode%>"/>  </label>

            <div class="col-sm-3 ">
                <div class="btn-group ens_btn" id="blTypeENS_btn">
                    <button type="button" class="btn btn-primary straightbl ensbuyersellerclass">Straight BL</button>
                    <button type="button" class="btn btn-primary masterbl ensbuyersellerclass">Master BL</button>
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
                    <button type="button" class="btn btn-primary singlefiling ensbuyersellerclass">Single Filing</button>
                    <button type="button" class="btn btn-primary multiplefiling ensbuyersellerclass" >Multiple Filing</button>
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
                    <button type="button" class="btn btn-primary wiretransfer  ensbuyersellerclass">Wire Transfer</button>
                    <button type="button" class="btn btn-primary notprepaid ensbuyersellerclass">Not Prepaid</button>
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
                    <label><bean:message key='shipping.ens.declarant.name' bundle="<%=clientcode%>"/> </label>
                    <html:text property="declarantName" styleId="declarantName" styleClass="form-control setdata light_border declarantcomclass" maxlength="150" />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.address' bundle="<%=clientcode%>"/> </label>
                    <html:text property="declarantAddress" styleId="declarantAddress" styleClass="form-control setdata light_border declarantcomclass" maxlength="150" />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.address1' bundle="<%=clientcode%>"/> </label>
                    <html:text property="declarantAddress1" styleId="declarantAddress1" styleClass="form-control setdata light_border declarantcomclass" maxlength="150" />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.city' bundle="<%=clientcode%>"/> </label>
                    <html:text property="declarantCity" styleId="declarantCity" styleClass="form-control setdata light_border declarantcomclass" maxlength="150" />
                </div>
                <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.zipcode' bundle="<%=clientcode%>"/> </label>
                    <html:text property="declarantZipcode" styleId="declarantZipcode" styleClass="form-control setdata light_border declarantcomclass" maxlength="15" />
                 </div>
                 <%}else{%>
                 <html:hidden property="declarantZipcode" styleId="declarantZipcode"   styleClass="form-control" />
                 <%}%>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.country' bundle="<%=clientcode%>"/> </label>
                        <html:text property="declarantCountry" styleId="declarantCountry" styleClass="form-control setdata light_border declarantcomclass" maxlength="50" />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.state' bundle="<%=clientcode%>"/> </label>
                    <html:text property="declarantState" styleId="declarantState" styleClass="form-control setdata light_border declarantcomclass" maxlength="50" />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.telephone' bundle="<%=clientcode%>"/> </label>
                    <html:text property="declarantTelephone" styleId="declarantTelephone" styleClass="form-control setdata light_border declarantcomclass" maxlength="25"  />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.eori' bundle="<%=clientcode%>"/> </label>
                    <html:text property="declarantEORI" styleId="declarantEORI" styleClass="form-control setdata light_border declarantcomclass" maxlength="20"  />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label><bean:message key='shipping.ens.declarant.email' bundle="<%=clientcode%>"/> </label>
                    <html:text property="declarantEmail" styleId="declarantEmail" styleClass="form-control setdata email light_border declarantcomclass" maxlength="100"  />
                </div>
                <div class="col-xs-12 col-sm-3">
                    <label class="control-label"><bean:message key='shipping.ens.declarant.fillingtype' bundle="<%=clientcode%>"/> </label>
                    <div class="single-row-radio">
                        <label>
                            <html:radio property="declarantFillingType"  value="House BL" styleId="declarantFillingTypeHBL" styleClass="form-control medium-radio light_border" />
                            House BL
                        </label>
                        <label>
                            <html:radio property="declarantFillingType" value="Sub-House BL" styleId="declarantFillingTypeSHBL" styleClass="form-control medium-radio light_border" />
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

                        <div class="section_header"> <bean:message key='ecom.bkg.custby' bundle="<%=clientcode%>"/>
                            <span class="float-right">
                                <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search declarantcomclass" id="Buyersearch" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'Buyer')" readonly="readonly"><i class="fa fa-search"></i></a>
                                <button type="button" class="toggle-btn  ensCollapseBuyer ensbuyersellerclass " data-toggle="collapse" data-target="#ensCollapseBuyer" collapsed=""> <i class="fa fa-plus"> </i> </button>
                            </span>
                        </div>

                        <div class="collapse show" id="ensCollapseBuyer">

                            <div class="row form-group">

                                <div class="col-sm-1"> </div>

                                <div class="col-sm-3">
                                    <select id="BuyerSelect" name="BuyerSelect" class="form-control ensbuyersellerclass light_border" onchange="setCustDetails(this.value, 'Buyer')">
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

                                        <%                                                                    }
                                            }
                                        %>

                                        <option value="new">New Customer</option>
                                    </select>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:hidden property="ebook_Buyercode" styleId="ebook_Buyercode"/>
                                    <html:text  property="ebook_Buyername" styleId="ebook_Buyername" styleClass="form-control  light_border byslmandatory " maxlength="150"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="ebook_Buyeraddress" styleId="ebook_Buyeraddress" styleClass="form-control ensbuyersellerclass light_border   byslmandatory" maxlength="150"/>

                                </div>

                            </div>

                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text  property="ebook_Buyeraddress1" styleId="ebook_Buyeraddress1" styleClass="form-control ensbuyersellerclass light_border  " maxlength="150"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text property="ebook_Buyercity_desc" styleId="ebook_Buyercity_desc"  styleClass="form-control ensbuyersellerclass light_border   byslmandatory" maxlength="150"   onblur="setCityDesc_New('ebook_Buyercity_desc','ebook_Buyercity')"  />
                                    <html:hidden property="ebook_Buyercity" styleId="ebook_Buyercity"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:hidden property="ebook_Buyercountry" styleId="ebook_Buyercountry" />
                                    <html:text  property="ebook_Buyercountry_name" styleId="ebook_Buyercountry_name" styleClass="form-control ensbuyersellerclass light_border   byslmandatory" onchange="loadStateAutoComplete('ebook_Buyercountry_name', 'ebook_buyerstate', 'typeheadhidden16');"/>
                                </div>

                            </div>


                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="ebook_Buyerphone" styleId="ebook_Buyerphone" styleClass="form-control ensbuyersellerclass light_border" maxlength="20"/>

                                </div>

                                <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="ebook_Buyerfax" styleId="ebook_Buyerfax" styleClass="form-control ensbuyersellerclass light_border" maxlength="20"/>

                                </div>
                                <%} else {%>

                                <html:hidden property="ebook_Buyerfax" styleId="ebook_Buyerfax"   styleClass="form-control  " />

                                <%}%>

                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text size="43" property="ebook_Buyeremail" styleId="ebook_Buyeremail" styleClass="form-control email ensbuyersellerclass light_border" maxlength="75"/>

                                </div>
                                <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                    <label class="col-sm-1 control-label"> <bean:message key="shipping.ens.declarant.zipcode" bundle="<%=clientcode%>"/>  </label>

                                    <div class="col-sm-3">
                                       <html:text property="ebook_Buyerzipcode" styleId="ebook_Buyerzipcode"  styleClass="form-control zip_code ensbuyersellerclass light_border"  maxlength="15" />
                                    </div>
                                <%}else{%>
                                    <html:hidden property="ebook_Buyerzipcode" styleId="ebook_Buyerzipcode"   styleClass="form-control " />
                               <%}%>
                            </div>
                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="ebook_Buyerstate" styleId="ebook_Buyerstate" styleClass="form-control ensbuyersellerclass light_border  SIBL_state ebooking_state SI_Process_Restriction_Cust" onclick="validateState('ebook_Buyercountry_name',this);" />

                                </div>
                                <%if ("Y".equalsIgnoreCase(GENS)) {%>
                                <label class="col-sm-1 control-label ebook_buyerdiv"  ><bean:message key="Reg.Eori" bundle="<%=clientcode%>"/> </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="ebook_Buyereori" styleId="ebook_Buyereori" styleClass="form-control ensbuyersellerclass  ebooking_eori light_border ebook_buyerdiv"  maxlength="17"  />
                                </div>
                                <label class="col-sm-1 control-label ebook_buyerdiv" ><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/> </label>

                                <div class="col-sm-3">
                                    <html:select property="ebook_Buyerpersontype" styleId="Buyertypeperson" styleClass="form-control ensbuyersellerclass  light_border ebook_buyerdiv"  >
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
            <div class="row">

                <div class="col-md-12">
                    <div class="section_header"> <bean:message key='ecom.bkg.custsl' bundle="<%=clientcode%>"/>
                        <span class="float-right">
                            <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search declarantcomclass" id="Sellersearch" onclick="callsearchmethod('ebooking', 'ebookingSearch', 'Seller')" readonly="readonly"><i class="fa fa-search"></i></a>
                            <button type="button" class="toggle-btn ensCollapseSeller ensbuyersellerclass" data-toggle="collapse" data-target="#ensCollapseSeller" collapsed=""> <i class="fa fa-plus"> </i> </button>
                        </span>
                    </div>

                        <div class="collapse show" id="ensCollapseSeller">

                            <div class="row form-group">

                                <div class="col-sm-1"> </div>

                                <div class="col-sm-3">
                                    <select id="SellerSelect" name="SellerSelect" class="form-control ensbuyersellerclass light_border" onchange="setCustDetails(this.value, 'Seller')">
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
                                            }
                                        %>

                                        <option value="new">New Customer</option>
                                    </select>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:hidden property="ebook_Sellercode" styleId="ebook_Sellercode"/>
                                    <html:text  property="ebook_Sellername" styleId="ebook_Sellername" styleClass="form-control ensbuyersellerclass light_border  byslmandatory" maxlength="150"/>
                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="ebook_Selleraddress" styleId="ebook_Selleraddress" styleClass="form-control ensbuyersellerclass light_border  byslmandatory" maxlength="150"/>

                                </div>

                            </div>

                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text  property="ebook_Selleraddress1" styleId="ebook_Selleraddress1" styleClass="form-control light_border  ensbuyersellerclass" maxlength="150"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text property="ebook_Sellercity_desc" styleId="ebook_Sellercity_desc"  styleClass="form-control ensbuyersellerclass light_border  byslmandatory" maxlength="150"   onblur="setCityDesc_New('ebook_Sellercity_desc','ebook_Sellercity')"  />
                                    <html:hidden property="ebook_Sellercity" styleId="ebook_Sellercity"/>

                                </div>

                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:hidden property="ebook_Sellercountry" styleId="ebook_Sellercountry" />
                                    <html:text  property="ebook_Sellercountry_name" styleId="ebook_Sellercountry_name" styleClass="form-control light_border ensbuyersellerclass byslmandatory" onchange="loadStateAutoComplete('ebook_Sellercountry_name', 'ebook_Sellerstate', 'typeheadhidden15');" onblur="setCountry_New('ebook_Sellercountry_name','ebook_Sellercountry');" />
                                </div>

                            </div>


                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="ebook_Sellerphone" styleId="ebook_Sellerphone" styleClass="form-control light_border ensbuyersellerclass" maxlength="20"/>

                                </div>

                                <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="ebook_Sellerfax" styleId="ebook_Sellerfax" styleClass="form-control light_border ensbuyersellerclass" maxlength="20"/>

                                </div>
                                <%} else {%>

                                <html:hidden property="ebook_Sellerfax" styleId="ebook_Sellerfax"   styleClass="form-control  " />

                                <%}%>

                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text size="43" property="ebook_Selleremail" styleId="ebook_Selleremail" styleClass="form-control light_border email ensbuyersellerclass" maxlength="75"/>

                                </div>
                                <%if ("Y".equalsIgnoreCase(Enable_Zip_Code)) {%>
                                    <label class="col-sm-1 control-label"> <bean:message key="shipping.ens.declarant.zipcode" bundle="<%=clientcode%>"/>  </label>

                                    <div class="col-sm-3">
                                       <html:text property="ebook_Sellerzipcode" styleId="ebook_Sellerzipcode"  styleClass="form-control zip_code light_border ensbuyersellerclass"  maxlength="15" />
                                    </div>
                                <%}else{%>
                                    <html:hidden property="ebook_Sellerzipcode" styleId="ebook_Sellerzipcode"   styleClass="form-control " />
                               <%}%>
                            </div>
                            <div class="row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/>  </label>

                                <div class="col-sm-3">
                                    <html:text property="ebook_Sellerstate" styleId="ebook_Sellerstate" styleClass="form-control ensbuyersellerclass ebooking_state light_border SIBL_state SI_Process_Restriction_Cust" onclick="validateState('ebook_Sellercountry_name',this);" />

                                </div>
                                <%if ("Y".equalsIgnoreCase(GENS)) {%>
                                <label class="col-sm-1 control-label ebook_sellerdiv" ><bean:message key="Reg.Eori" bundle="<%=clientcode%>"/> </label>

                                <div class="col-sm-3 ">
                                    <html:text  property="ebook_Sellereori" styleId="ebook_Sellereori" styleClass="form-control  ebooking_eori light_border ensbuyersellerclass ebook_sellerdiv"  maxlength="17"  />
                                </div>
                                <label class="col-sm-1 control-label ebook_sellerdiv" ><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/> </label>

                                <div class="col-sm-3">
                                    <html:select property="ebook_Sellerpersontype" styleId="Sellertypeperson" styleClass="form-control ensbuyersellerclass  light_border ebook_sellerdiv"   >
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
    </span>
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
       
//            // Call chkEU_ENSDeclaration on page load
//            chkEU_ENSDeclaration();
//            
//            // Call chkEU_ENSDeclaration when origin/destination countries change
//            $('#ebook_origin_country, #ebook_delivery_country').on('change blur', function() {
//                chkEU_ENSDeclaration();
//            });
//            
//            // Override the getPort function to include ENS check
//            var originalGetPort = window.getPort;
//            window.getPort = function(port, setport, code, defOriDesHaulage) {
//                originalGetPort(port, setport, code, defOriDesHaulage);
//                chkEU_ENSDeclaration();
//            };
        // placeholder
            $('#ebook_Buyercity').attr('placeholder', '<bean:message key="Reg.City" bundle="<%=clientcode%>"/>');
           $('#ebook_Sellercity').attr('placeholder', '<bean:message key="Reg.City" bundle="<%=clientcode%>"/>');
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
                        $('#ensFillingType_btn > .singlefiling').trigger('click');
                        $('#ensFillingType').val('Single Filing').trigger('change');
                        chkEU_ENSFilingType();
                        isTriggering = false;
                       
                    } else if ($(this).is('.masterbl')) {
                        $('#blTypeENS').val('Master BL');
                        isTriggering = true;
                        $('#ensFillingType_btn > .multiplefiling').trigger('click');
                        $('#ensFillingType').val('Multiple Filing').trigger('change');  
                        chkEU_ENSFilingType();
                        isTriggering = false;
                       
                    }
                } else if ($(this).parent().is('#ensFillingType_btn')) {
                    if ($(this).is('.singlefiling')) {
                        $('#ensFillingType').val('Single Filing').trigger('change'); 
                        isTriggering = true;
                        $('#blTypeENS_btn > .straightbl').trigger('click');
                        $('#blTypeENS').val('Straight BL');
                        chkEU_ENSFilingType();
                        isTriggering = false;
                        
                       
                        // loadStateAutoComplete('ebook_Sellercountry_name', 'ebook_Sellerstate', 'typeheadhidden15', 'load');
                        // loadStateAutoComplete('ebook_Buyercountry_name', 'ebook_Buyerstate', 'typeheadhidden16', 'load');
                    } else if ($(this).is('.multiplefiling')) {
                        $('#ensFillingType').val('Multiple Filing').trigger('change');
                        isTriggering = true;
                        $('#blTypeENS_btn > .masterbl').trigger('click');
                        $('#blTypeENS').val('Master BL');           
                        chkEU_ENSFilingType();  

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


            if ($('#ensFillingType').val() == 'Single Filing') {
                $('#ensFillingType_btn > .singlefiling').addClass("active");
                chkEU_ENSFilingType();
            } else if ($('#ensFillingType').val() == 'Multiple Filing') {
                $('#ensFillingType_btn > .multiplefiling').addClass("active");
                chkEU_ENSFilingType();
            } else {

                $('#ensFillingType_btn > .singlefiling').addClass("active");
                $('#ensFillingType').val('Single Filing');
                chkEU_ENSFilingType();
            }
          
        });

    </script>
</fieldset>
<%}%>
