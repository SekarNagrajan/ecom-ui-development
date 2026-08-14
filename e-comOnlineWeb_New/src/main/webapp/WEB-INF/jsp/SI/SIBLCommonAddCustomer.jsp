<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ page import="java.util.*, org.apache.struts.util.MessageResources, org.apache.struts.action.ActionForm" %>
<%@ page import=" net.ecom.vo.*, net.ecom.common.*, net.ecom.common.utils.DateConversion_New" %>

<%
    try {
        if ("Y".equalsIgnoreCase((String) application.getAttribute(GeneralConf.TENS_Documentation))) {
            List<CustomerDetailsVO> byList = null;
            List<String> custtypelist = (List<String>) application.getAttribute(GeneralConf.CUSTTYPECONFIG);
            long jsdate = new Date().getTime();
            String clientcode = (String) session.getAttribute("clientcode");
            String HideFaxNo = (String) application.getAttribute(GeneralConf.HideFaxNo);
            String GENS = (String) application.getAttribute(GeneralConf.GENS);
            ArrayList<CustomerDetailsVO> ebooking_custtype_grid = null;
            String customerType = "", custTypeCode = "", readonlyflag = "", selectDisable = "", addBtnId = "", searchCust = null;
            customerType = request.getParameter("custype");
            readonlyflag = request.getParameter("flag");
            addBtnId = "Buyer".equalsIgnoreCase(customerType) ? "addBuyerBtn" : "Seller".equalsIgnoreCase(customerType) ? "addSellerBtn" : "";
            selectDisable = "true".equalsIgnoreCase(readonlyflag) ? "disabled" : "";
            ArrayList<CustomerDetailsVO> ebooking_Buyertype_grid = null;
            ebooking_Buyertype_grid = (ArrayList<CustomerDetailsVO>) session.getAttribute("ebooking_buyere_grid");
            ArrayList<CustomerDetailsVO> ebooking_sellertype_grid = null;
            ebooking_sellertype_grid = (ArrayList<CustomerDetailsVO>) session.getAttribute("ebooking_seller_grid");
            custTypeCode = "Buyer".equalsIgnoreCase(customerType) ? "BY" : "Seller".equalsIgnoreCase(customerType) ? "SL" : "";
            searchCust = "Buyer".equalsIgnoreCase(customerType) ? "dyBuyer" : "Seller".equalsIgnoreCase(customerType) ? "dySeller" : "";
            if (customerType != null && "Buyer".equalsIgnoreCase(customerType)) {
                custTypeCode = "BY";
                byList = (List<CustomerDetailsVO>) session.getAttribute("BYCUSTLIST_SI");
            } else if (customerType != null && "Seller".equalsIgnoreCase(customerType)) {
                custTypeCode = "SL";
                byList = (List<CustomerDetailsVO>) session.getAttribute("SLCUSTLIST_SI");
            }
            if (ebooking_Buyertype_grid != null && !ebooking_Buyertype_grid.isEmpty() && customerType != null && "Buyer".equalsIgnoreCase(customerType)) {
                ebooking_custtype_grid = ebooking_Buyertype_grid;
            } else if (ebooking_sellertype_grid != null && !ebooking_sellertype_grid.isEmpty() && customerType != null && "Seller".equalsIgnoreCase(customerType)) {
                ebooking_custtype_grid = ebooking_sellertype_grid;
            }
            CustomerDetailsVO custVO = null;
            String rowID = request.getParameter("id");
            if (ebooking_custtype_grid != null && customerType != null) {
                String searchid = customerType + "search";
                String collapse = customerType + "collapse";
                String ContainerId = customerType + "Container";
                String count = customerType + "Count";
                String indexSize = String.valueOf(ebooking_custtype_grid.size());%>
<script type='text/javascript' src='js/SIBLCommonJS.js?<%=jsdate%>'></script>
<div class="row">
    <div class="col-md-12">
        <div class="<%= "true".equalsIgnoreCase(readonlyflag) ? "" : "panel_right"%>">
            <div class="custom-panel-head">
                <%if ("Seller".equalsIgnoreCase(customerType)) {%>
                <bean:message key="ecom.bkg.custsl" bundle="<%=clientcode%>" />
                <%} else {%>
                <bean:message key="ecom.bkg.custby" bundle="<%=clientcode%>" />
                <%}%>
                <span class="pull-right" style="margin-top: -7px;">
                    <a href="javascript:void(0)"  class="btn btn-success search_symbol btn-xs" id="<%=searchCust%>custsearch<%=rowID%>"  <%= "true".equalsIgnoreCase(readonlyflag) ? "style=\"display: none\"" : ""%> onclick="callsearchmethodsi('ebooking', 'ebookingSearch', '<%=searchCust%>', '<%=rowID%>')" readonly="readonly"><i class="fa fa-search"></i></a>
                    <a data-toggle="collapse" 
                       href="javascript:void(0)" 
                       onclick="openCollapseFun('<%=collapse%>');" 
                       aria-expanded="false" 
                       aria-controls="<%=collapse%>">
                        <i class="fa fa-plus-square-o"></i>
                    </a>
                </span>
            </div>

            <div class="collapse" id="<%=collapse%>">
                <div id="<%=ContainerId%>">

                    <% for (int k = 0; k < ebooking_custtype_grid.size(); k++) {
                            CustomerDetailsVO customergrid = ebooking_custtype_grid.get(k);

                            rowID = String.valueOf(k + 1);
                            String prefix = customerType;
                            String custSelect = "ebook_" + prefix + "Select" + rowID;
                            String persontype = "ebook_" + prefix + "persontype" + rowID;
                            String eori = "ebook_" + prefix + "eori" + rowID;
                            String state = "ebook_" + prefix + "state" + rowID;
                            String email = "ebook_" + prefix + "email" + rowID;
                            String fax = "ebook_" + prefix + "fax" + rowID;
                            String phone = "ebook_" + prefix + "phone" + rowID;
                            String countryName = "ebook_" + prefix + "country_name" + rowID;
                            String cityDesc = "ebook_" + prefix + "city_desc" + rowID;
                            String address1 = "ebook_" + prefix + "address1" + rowID;
                            String address = "ebook_" + prefix + "address" + rowID;
                            String name = "ebook_" + prefix + "name" + rowID;
                            String country = "ebook_" + prefix + "country" + rowID;
                            String city = "ebook_" + prefix + "city" + rowID;
                            String typecustheadhidden = "typecustheadhidden" + rowID;
                            String typecustcountryheadhidden = "typecustcountryheadhidden" + rowID;
                            String cusid = "ebook_" + prefix + "id" + rowID;
                            String code = "ebook_" + prefix + "CustCode" + rowID;
                            String country_desc = "ebook_" + prefix + "country_desc" + rowID;
                            String countRowID = prefix + "countRowID";


                    %>
                    <script>
                        var customerType = '<%=customerType%>';
                        var rowID = '<%=rowID%>';
                        var country_name = '<%=countryName%>';
                        var typecustheadhidden = '<%=typecustheadhidden%>';
                        var state = '<%=state%>';
                        var typecustcountryheadhidden = '<%=typecustcountryheadhidden%>';
                    </script>
                    <!-- Hidden Fields -->
                    <input type="hidden" id="<%=country%>" name="<%=country%>" class="form-control " />
                    <input type="hidden" id="<%=city%>" name="<%=city%>" class="form-control " />
                    <input type="hidden" id="<%=cusid%>" name="<%=cusid%>" class="form-control " />
                    <input type="hidden" id="<%=code%>" name="<%=code%>"  class="form-control " />
                    <input type="hidden" id="<%=typecustheadhidden%>" name="<%=typecustheadhidden%>" class="form-control " />
                    <input type="hidden" id="<%=typecustcountryheadhidden%>" name="<%=typecustcountryheadhidden%>" class="form-control " />
                    <input type="hidden" id="<%=countRowID + rowID%>" name="<%=countRowID%>" class="form-control " value="<%=rowID%>" />

                    <% if (custtypelist != null && custtypelist.contains(custTypeCode)) { %>
                    <div class="buyer-row position-relative p-2 mb-2 rounded">
                        <% if (rowID != null && !"1".equalsIgnoreCase(rowID)) {%>
                        <div class="section_header" style="padding: 14px;"><%=prefix%>
                        </div>
                        <div class="remove-btn-wrapper position-absolute"  style="top: 5px; right: 10px;<%= "true".equalsIgnoreCase(readonlyflag) ? "; display: none;" : ""%>">
                            <a href="javascript:void(0)"  class="btn btn-success search_symbol btn-xs" id="<%=searchCust%>custsearch<%=rowID%>" onclick="callsearchmethodsi('ebooking', 'ebookingSearch', '<%=searchCust%>', '<%=rowID%>')" readonly="readonly"><i class="fa fa-search"></i></a>
                            <a href="javascript:void(0)" onclick="removeBuyerRow(this)" class="delete_cargo" style="color: red;">
                                <i class="fa fa-times-circle fa-2x"></i>
                            </a>
                        </div>

                        <%}%>
                        <div id="<%=rowID%>" class="previewCust <%=rowID%>"  >

                            <div class=" row form-group">

                                <div class="col-sm-1"> </div>
                                <div class="col-sm-3">
                                    <select id="<%=custSelect%>" name="<%=custSelect%>" <%=selectDisable%>  class="form-control  input_si" onchange="setAddCustDetails(this.value, '<%=customerType%>', '<%=rowID%>')">
                                        <% if (byList != null) { %>
                                        <option value="">Select Customer</option>
                                        <% for (int i = 0; i < byList.size(); i++) {
                                                custVO = (CustomerDetailsVO) byList.get(i);%>
                                        <option value="<%=custVO.getName() + "~~" + custVO.getCustCode() + "~~" + custVO.getAddress() + "~~" + custVO.getCity() + "~~" + custVO.getCountry() + "~~" + custVO.getCountryCode() + "~~" + custVO.getEmail() + "~~" + custVO.getPhoneNo() + "~~" + custVO.getFax() + "~~" + custVO.getAddress1() + "~~" + custVO.getState() + "~~" + custVO.getPerontype() + "~~" + custVO.getEroi()%>">
                                            <%=custVO.getName()%>
                                        </option>
                                        <% }
                                        } else { %>
                                        <option value="">New Customer</option>
                                        <% }%>
                                        <option value="new">New Customer</option>
                                    </select>

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>
                                <div class="col-sm-3">
                                    <input type="text" id="<%=name%>" name="<%=name%>" <%=selectDisable%> maxlength="150" placeholder="Name" class="form-control  input_si" />
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=address%>" name="<%=address%>" <%=selectDisable%> maxlength="150" placeholder="Address" class="form-control  input_si" />
                                </div>

                            </div>

                            <div class=" row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=address1%>" name="<%=address1%>"  <%=selectDisable%>  maxlength="150" placeholder="Address 1" class="form-control input_si " />
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=cityDesc%>" name="<%=cityDesc%>" <%=selectDisable%>  maxlength="100" placeholder="City" class="form-control  input_si city_new_shipper" onblur="setCityDesc_New('<%=cityDesc%>', 'ebook_<%=customerType%>city<%=rowID%>')" />
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=countryName%>" name="<%=countryName%>" <%=selectDisable%>  maxlength="100" placeholder="Country" class="form-control input_si  country_new_<%=customerType%>" onchange="loadStateAutoComplete('<%=countryName%>', '<%=state%>', '<%=typecustheadhidden%>');" onblur="setCountry_New('<%=countryName%>', '<%=country%>');" />
                                </div>

                            </div>

                            <div class=" row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=phone%>" name="<%=phone%>" maxlength="20" <%=selectDisable%>  placeholder="Phone" class="form-control  input_si" />
                                </div>

                                <% if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=fax%>" name="<%=fax%>" maxlength="20" <%=selectDisable%>  placeholder="Fax" class="form-control  input_si" />
                                </div>
                                <% } else {%>
                                <input type="hidden" id="<%=fax%>" name="<%=fax%>" class="form-control " />
                                <% }%>
                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=email%>" name="<%=email%>" maxlength="75" <%=selectDisable%>  placeholder="Email" class="form-control email  input_si" />
                                </div> 

                            </div>

                            <div class=" row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/> </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=state%>" name="<%=state%>" maxlength="100" <%=selectDisable%> placeholder="State" class="form-control  input_si ebooking_state" onclick="validateState('<%=countryName%>', this);" />
                                </div>

                                <% if ("Y".equalsIgnoreCase(GENS)) {%>
                                <label class="col-sm-1 control-label ebook_forwarderdiv"  ><bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=eori%>" name="<%=eori%>" maxlength="17" <%=selectDisable%> placeholder="EORI" class="form-control  input_si ebooking_eori" style="display: block;" />
                                </div>
                                <label class="col-sm-1 control-label ebook_forwarderdiv"  ><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/> </label>
                                <div class="col-sm-3" >
                                    <select id="<%=persontype%>" name="<%=persontype%>" <%=selectDisable%> class="form-control  input_si" <%=selectDisable%>  style="display: block;">
                                        <option value="">Type of Person</option>
                                        <option value="Legal">Legal</option>
                                        <option value="Natural">Natural</option>
                                        <option value="Association of persons">Association of persons</option>
                                    </select>
                                </div>
                                <% }%>

                            </div>
                        </div>
                    </div>
                    <!-- Additional fields are unchanged, continue... -->

                    <script>
                                        $('#<%=custSelect%>').val('<%=customergrid.getCustType()%>');

                                        $('#<%=persontype%>').val('<%=customergrid.getPerontype()%>');
                                        $('#<%=eori%>').val('<%=customergrid.getEroi()%>');
                                        $('#<%=state%>').val('<%=customergrid.getState()%>');
                                        $('#<%=email%>').val('<%=customergrid.getEmail()%>');
                                        $('#<%=fax%>').val('<%=customergrid.getFax()%>');
                                        $('#<%=phone%>').val('<%=customergrid.getPhoneNo()%>');
                                        $('#<%=countryName%>').val('<%=customergrid.getCountryCode()%>');
                                        $('#<%=cityDesc%>').val('<%=customergrid.getCity()%>');

                                         $('#<%=address1%>').val('<%= customergrid.getAddress1().replace("\"", "\\\"").replace("'", "\\'") %>');
                                         $('#<%=address%>').val('<%= customergrid.getAddress().replace("\"", "\\\"").replace("'", "\\'") %>');
                                        $('#<%=name%>').val('<%=customergrid.getName()%>');
                                        $('#<%=country%>').val('<%=customergrid.getCountry()%>');
                                        $('#<%=city%>').val('<%=customergrid.getCity()%>');
                                        $('#<%=typecustheadhidden%>').val('<%=customergrid.getCustType()%>');
                        $('#<%=cusid%>').val('<%=customergrid.getCustCode()%>');
                        $('#<%=code%>').val('<%=customergrid.getCustCode()%>');
                        $('#<%=country_desc%>').val('<%=customergrid.getCountryCode()%>');
                        $('#<%=custSelect%>').val('');


                        setCountry_name(customerType, rowID, 'typecustcountryheadhidden' + rowID);
                        loadStateAutoComplete('ebook_' + customerType + 'country_name' + rowID,
                                'ebook_' + customerType + 'state' + rowID,
                                'typecustheadhidden' + rowID,
                                'load');
                         setCityDesc_New('<%=cityDesc%>', 'ebook_<%=customerType%>city<%=rowID%>');
                         
                         applyRules('<%=ContainerId%>',customerType, '<%=HideFaxNo%>' );
                   
                    </script>
                    <%
                            } // end if custtypelist != null && contains code
                        }
                        if ("Seller".equalsIgnoreCase(customerType)) {
                            session.setAttribute("ebooking_seller_grid", null);
                        }
                        if ("Buyer".equalsIgnoreCase(customerType)) {
                            session.setAttribute("ebooking_buyere_grid", null);
                        }
                    %>



                </div>
                <input type="hidden" id="<%=count%>" name="<%=count%>" value="<%=indexSize%>" />

                <div class="col-12 col-sm-12 text-right" id="<%=addBtnId%>"  <%= "true".equalsIgnoreCase(readonlyflag) ? "style=\"display: none\"" : ""%> >
                    <a href="javascript:void(0)" 
                       onclick="addCustRowAjax('<%=customerType%>', '')" 
                       class="btn new_add">
                        <i class="fa fa-plus"></i>
                        <bean:message key="ecom.bkg.but.add" bundle="<%=clientcode%>" />
                    </a>
                </div>
            </div>

        </div> 

    </div>

</div> 

<%} else if ("false".equalsIgnoreCase(readonlyflag)) {

    String prefix = customerType;
    String custSelect = "ebook_" + prefix + "Select" + rowID;
    String persontype = "ebook_" + prefix + "persontype" + rowID;
    String eori = "ebook_" + prefix + "eori" + rowID;
    String state = "ebook_" + prefix + "state" + rowID;
    String email = "ebook_" + prefix + "email" + rowID;
    String fax = "ebook_" + prefix + "fax" + rowID;
    String phone = "ebook_" + prefix + "phone" + rowID;
    String countryName = "ebook_" + prefix + "country_name" + rowID;
    String cityDesc = "ebook_" + prefix + "city_desc" + rowID;
    String address1 = "ebook_" + prefix + "address1" + rowID;
    String address = "ebook_" + prefix + "address" + rowID;
    String name = "ebook_" + prefix + "name" + rowID;
    String country = "ebook_" + prefix + "country" + rowID;
    String city = "ebook_" + prefix + "city" + rowID;
    String typecustheadhidden = "typecustheadhidden" + rowID;
    String typecustcountryheadhidden = "typecustcountryheadhidden" + rowID;
    String cusid = "ebook_" + prefix + "id" + rowID;
    String code = "ebook_" + prefix + "CustCode" + rowID;
    String country_desc = "ebook_" + prefix + "country_desc" + rowID;
    String countRowID = prefix + "countRowID";
    String searchid = customerType + "search";
    String collapse = customerType + "collapse";
    String ContainerId = customerType + "Container";
    String count = customerType + "Count";
%>

<script>
    var customerType = '<%=customerType%>';
    var rowID = '<%=rowID%>';
    var country_name = '<%=countryName%>';
    var typecustheadhidden = '<%=typecustheadhidden%>';
    var state = '<%=state%>';
    var typecustcountryheadhidden = '<%=typecustcountryheadhidden%>';

</script>
<div class="row">
    <div class="col-md-12">
        <div class="<%= "true".equalsIgnoreCase(readonlyflag) ? "" : "panel_right"%>">
            <div class="custom-panel-head">
                <%if ("Seller".equalsIgnoreCase(customerType)) {%>
                <bean:message key="ecom.bkg.custsl" bundle="<%=clientcode%>" />
                <%} else {%>
                <bean:message key="ecom.bkg.custby" bundle="<%=clientcode%>" />
                <%}%>

                <span class="pull-right" style="margin-top: -7px;">
                    <a href="javascript:void(0)" 
                       onclick="callsearchmethodsi('ebooking', 'esiSearch', 'Buyer')" 
                       class="btn btn-success search_symbol btn-xs" 
                       id="<%=searchid%>"  <%= "true".equalsIgnoreCase(readonlyflag) ? "style=\"display: none\"" : ""%> >
                        <i class="fa fa-search"></i>
                    </a>

                    <a data-toggle="collapse" 
                       href="javascript:void(0)" 
                       onclick="openCollapseFun('<%=collapse%>');" 
                       aria-expanded="false" 
                       aria-controls="<%=collapse%>">
                        <i class="fa fa-plus-square-o"></i>
                    </a>
                </span>
            </div>

            <div class="collapse" id="<%=collapse%>">
                <div id="<%=ContainerId%>">
                    <!-- Include all hidden fields -->
                    <input type="hidden" id="<%=country%>" name="<%=country%>" class="form-control " />
                    <input type="hidden" id="<%=city%>" name="<%=city%>" class="form-control " />
                    <input type="hidden" id="<%=cusid%>" name="<%=cusid%>" class="form-control " />
                    <input type="hidden" id="<%=code%>" name="<%=code%>" class="form-control " />
                    <input type="hidden" id="<%=typecustheadhidden%>" name="<%=typecustheadhidden%>" class="form-control " />
                    <input type="hidden" id="<%=typecustcountryheadhidden%>" name="<%=typecustcountryheadhidden%>" class="form-control " />
                    <input type="hidden" id="<%=countRowID + rowID%>" name="<%=countRowID%>" class="form-control " value="<%=rowID%>" />

                    <% if (custtypelist != null && custtypelist.contains(custTypeCode)) { %>
                    <!-- New Customer Input Form -->
                    <div class="buyer-row position-relative p-2 mb-2 rounded">
                        <% if (rowID != null && !"1".equalsIgnoreCase(rowID)) {%>
                        <div class="section_header" style="padding: 14px;"><%=prefix%>
                        </div>
                        <div class="remove-btn-wrapper position-absolute" style="top: 5px; right: 10px;" >
                            <a href="javascript:void(0)"  class="btn btn-success search_symbol btn-xs" id="<%=searchCust%>custsearch<%=rowID%>" onclick="callsearchmethodsi('ebooking', 'ebookingSearch', '<%=searchCust%>', '<%=rowID%>')" readonly="readonly"><i class="fa fa-search"></i></a>
                            <a href="javascript:void(0)" onclick="removeBuyerRow(this)" class="delete_cargo" style="color: red;">
                                <i class="fa fa-times-circle fa-2x"></i>
                            </a>
                        </div>

                        <%}%>
                        <div id="<%=rowID%>" class="previewCust <%=rowID%>"  >

                            <div class=" row form-group">

                                <div class="col-sm-1"> </div>
                                <div class="col-sm-3">
                                    <select id="<%=custSelect%>" name="<%=custSelect%>"  class="form-control  input_si" onchange="setAddCustDetails(this.value, '<%=customerType%>', '<%=rowID%>')">
                                        <% if (byList != null) { %>
                                        <option value="">Select Customer</option>
                                        <% for (int i = 0; i < byList.size(); i++) {
                                                custVO = (CustomerDetailsVO) byList.get(i);%>
                                        <option value="<%=custVO.getName() + "~~" + custVO.getCustCode() + "~~" + custVO.getAddress() + "~~" + custVO.getCity() + "~~" + custVO.getCountry() + "~~" + custVO.getCountryCode() + "~~" + custVO.getEmail() + "~~" + custVO.getPhoneNo() + "~~" + custVO.getFax() + "~~" + custVO.getAddress1() + "~~" + custVO.getState() + "~~" + custVO.getPerontype() + "~~" + custVO.getEroi()%>">
                                            <%=custVO.getName()%>
                                        </option>
                                        <% }
                                        } else { %>
                                        <option value="">New Customer</option>
                                        <% }%>
                                        <option value="new">New Customer</option>
                                    </select>

                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.customername.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>
                                <div class="col-sm-3">
                                    <input type="text" id="<%=name%>" name="<%=name%>"  maxlength="150" placeholder="Name" class="form-control  input_si" />
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=address%>" name="<%=address%>" maxlength="150" placeholder="Address" class="form-control  input_si" />
                                </div>

                            </div>

                            <div class=" row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=address1%>" name="<%=address1%>"  maxlength="150" placeholder="Address 1" class="form-control input_si " />
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=cityDesc%>" name="<%=cityDesc%>"  maxlength="100" placeholder="City" class="form-control  input_si city_new_shipper" onblur="setCityDesc_New('<%=cityDesc%>', 'ebook_<%=customerType%>city<%=rowID%>')" />
                                </div>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=countryName%>" name="<%=countryName%>"  maxlength="100" placeholder="Country" class="form-control input_si  country_new_<%=customerType%>" onchange="loadStateAutoComplete('<%=countryName%>', '<%=state%>', '<%=typecustheadhidden%>');" onblur="setCountry_New('<%=countryName%>', '<%=country%>');" />
                                </div>

                            </div>

                            <div class=" row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="shipping.telephone.display" bundle="<%=clientcode%>"/>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=phone%>" name="<%=phone%>" maxlength="20" placeholder="Phone" class="form-control  input_si" />
                                </div>

                                <% if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                <label class="col-sm-1 control-label"><bean:message key="shipping.fax.display" bundle="<%=clientcode%>"/>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=fax%>" name="<%=fax%>" maxlength="20" placeholder="Fax" class="form-control  input_si" />
                                </div>
                                <% } else {%>
                                <input type="hidden" id="<%=fax%>" name="<%=fax%>" class="form-control " />
                                <% }%>
                                <label class="col-sm-1 control-label"> <bean:message key="shipping.email.display" bundle="<%=clientcode%>"/>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=email%>" name="<%=email%>" maxlength="75" placeholder="Email" class="form-control email  input_si" />
                                </div> 

                            </div>

                            <div class=" row form-group">

                                <label class="col-sm-1 control-label"><bean:message key="Reg.State" bundle="<%=clientcode%>"/> </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=state%>" name="<%=state%>" maxlength="100" placeholder="State" class="form-control  input_si ebooking_state" onclick="validateState('<%=countryName%>', this);" />
                                </div>

                                <% if ("Y".equalsIgnoreCase(GENS)) {%>
                                <label class="col-sm-1 control-label ebook_forwarderdiv"  ><bean:message key="Reg.Eori" bundle="<%=clientcode%>"/>  </label>
                                <div class="col-sm-3" >
                                    <input type="text" id="<%=eori%>" name="<%=eori%>" maxlength="17"  placeholder="EORI" class="form-control  input_si ebooking_eori" style="display: block;" />
                                </div>
                                <label class="col-sm-1 control-label ebook_forwarderdiv"  ><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/> </label>
                                <div class="col-sm-3" >
                                    <select id="<%=persontype%>" name="<%=persontype%>"  class="form-control  input_si" style="display: block;">
                                        <option value="">Type of Person</option>
                                        <option value="Legal">Legal</option>
                                        <option value="Natural">Natural</option>
                                        <option value="Association of persons">Association of persons</option>
                                    </select>
                                </div>
                                <% }%>

                            </div>

                        </div> 
                    </div>
                </div>
                <input type="hidden" id="<%=count%>" name="<%=count%>" value="1" />

                <div class="col-12 col-sm-12 text-right"  id="<%=addBtnId%>"  >
                    <a href="javascript:void(0)" 
                       onclick="addCustRowAjax('<%=customerType%>', '')" 
                       class="btn new_add">
                        <i class="fa fa-plus"></i>
                        <bean:message key="ecom.bkg.but.add" bundle="<%=clientcode%>" />
                    </a>
                </div>
            </div>

        </div> 

    </div>  
</div>
 <script type='text/javascript' src='js/SIBLCommonJS.js?<%=jsdate%>'></script>
<script>

    setCountry_name(customerType, rowID, 'typecustcountryheadhidden' + rowID);
    loadStateAutoComplete('ebook_' + customerType + 'country_name' + rowID,
            'ebook_' + customerType + 'state' + rowID,
            'typecustheadhidden' + rowID,
            'load');
    setSellerNameSelectOption();
    applyRules('<%=ContainerId%>',customerType, '<%=HideFaxNo%>' );

</script>
<% } // close BY check %>
<% }%> 

   <%}%>

<% }catch (Exception e) {
    e.printStackTrace();
}%>



