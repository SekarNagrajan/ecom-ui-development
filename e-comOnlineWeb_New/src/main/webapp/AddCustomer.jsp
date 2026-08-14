<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ page import="java.util.*, org.apache.struts.util.MessageResources, org.apache.struts.action.ActionForm" %>
<%@ page import="net.ecom.formbeans.EbookingValidatorFormBeanNew, net.ecom.vo.*, net.ecom.common.*, net.ecom.common.utils.DateConversion_New" %>

<%
    try {
        List<CustomerDetailsVO> byList = null;
        List<String> custtypelist = (List<String>) application.getAttribute(GeneralConf.CUSTTYPECONFIG);
        String HideFaxNo = (String) application.getAttribute(GeneralConf.HideFaxNo);
        String GENS = (String) application.getAttribute(GeneralConf.GENS);
        ArrayList<EbookingValidatorFormBeanNew> ebooking_custtype_grid = null;
        String customerType = "", custTypeCode = "", searchCust = null;
        customerType = Validation.checkNull(request.getParameter("custype"));
        System.out.println("customerType=>" + customerType + custtypelist);
        ArrayList<EbookingValidatorFormBeanNew> ebooking_Buyertype_grid = null;
        ebooking_Buyertype_grid = (ArrayList<EbookingValidatorFormBeanNew>) session.getAttribute("ebooking_buyere_grid");
        ArrayList<EbookingValidatorFormBeanNew> ebooking_sellertype_grid = null;
        ebooking_sellertype_grid = (ArrayList<EbookingValidatorFormBeanNew>) session.getAttribute("ebooking_seller_grid");
        custTypeCode = "Buyer".equalsIgnoreCase(customerType) ? "BY" : "Seller".equalsIgnoreCase(customerType) ? "SL" : "";
        if (customerType != null && "Buyer".equalsIgnoreCase(customerType)) {
            custTypeCode = "BY";
            byList = (List<CustomerDetailsVO>) session.getAttribute("BYCUSTLIST");
        } else if (customerType != null && "Seller".equalsIgnoreCase(customerType)) {
            custTypeCode = "SL";
            byList = (List<CustomerDetailsVO>) session.getAttribute("SLCUSTLIST");
        }
        if (ebooking_Buyertype_grid != null && !ebooking_Buyertype_grid.isEmpty() && customerType != null && "Buyer".equalsIgnoreCase(customerType)) {
            ebooking_custtype_grid = ebooking_Buyertype_grid;
        } else if (ebooking_sellertype_grid != null && !ebooking_sellertype_grid.isEmpty() && customerType != null && "Seller".equalsIgnoreCase(customerType)) {
            ebooking_custtype_grid = ebooking_sellertype_grid;
        }
        searchCust = "Buyer".equalsIgnoreCase(customerType) ? "dyBuyer" : "Seller".equalsIgnoreCase(customerType) ? "dySeller" : "";
        CustomerDetailsVO custVO = null;
        String rowID = request.getParameter("id");
        System.out.println("rowID===>>>>" + rowID);
        System.out.println("ebooking_custtype_grid**********************************88" + ebooking_custtype_grid);
        if (ebooking_custtype_grid != null) {
            System.out.println("ebooking_custtype_grid==>" + ebooking_custtype_grid);

            for (int k = 0; k < ebooking_custtype_grid.size(); k++) {
                EbookingValidatorFormBeanNew customergrid = ebooking_custtype_grid.get(k);

                rowID = String.valueOf(k + 1);
                String prefix = customerType;
                String custSelect = "ebook_" + prefix + "Select" + rowID;
                String persontype = "ebook_" + prefix + "persontype" + rowID;
                String eori = "ebook_" + prefix + "eori" + rowID;
                String state = "ebook_" + prefix + "state" + rowID;
                String email = "ebook_" + prefix + "email" + rowID;
                System.out.println("email==>" + email);
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
                String code = "ebook_" + prefix + "code" + rowID;
                String country_desc = "ebook_" + prefix + "country_desc" + rowID;
                String countRowID = prefix + "countRowID";


%>

<!-- Hidden Fields -->
<input type="hidden" id="<%=country%>" name="<%=country%>" class="form-control txt_borderno" />
<input type="hidden" id="<%=city%>" name="<%=city%>" class="form-control txt_borderno" />
<input type="hidden" id="<%=cusid%>" name="<%=cusid%>" class="form-control txt_borderno" />
<input type="hidden" id="<%=code%>" name="<%=code%>" class="form-control txt_borderno" />
<input type="hidden" id="<%=typecustheadhidden%>" name="<%=typecustheadhidden%>" class="form-control txt_borderno" />
<input type="hidden" id="<%=typecustcountryheadhidden%>" name="<%=typecustcountryheadhidden%>" class="form-control txt_borderno" />
<input type="hidden" id="<%=countRowID + rowID%>" name="<%=countRowID%>" class="form-control txt_borderno" value="<%=rowID%>" />

<% if (custtypelist != null && custtypelist.contains(custTypeCode)) { %>
<div class="buyer-row position-relative p-2 mb-2 rounded">
    <% if (rowID != null && !"1".equalsIgnoreCase(rowID)) {%>
    <div class="section_header" style="padding: 14px;"> <%=customerType%>
    </div>
    <div class="remove-btn-wrapper position-absolute" style="top: 5px; right: 10px;">
        <span class="float-right">
            <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="<%=searchCust%>custsearch<%=rowID%>" onclick="callsearchmethod('ebooking', 'ebookingSearch', '<%=searchCust%>', '<%=rowID%>')" readonly="readonly"><i class="fa fa-search"></i></a>
            <a href="javascript:void(0)" onclick="removeBuyerRow(this)" class="delete_cargo" style="color: red;">
                <i class="fa fa-times-circle fa-2x"></i>
            </a>            </span>

    </div>
    <%}%>
    <div id="<%=rowID%>" class="previewCust <%=rowID%>"  >

        <div class="form-group">
            <div class="row">
                <div class="col-xs-12 col-sm-4">
                    <select id="<%=custSelect%>" name="<%=custSelect%>" class="form-control txt_borderno" onchange="setAddCustDetails(this.value, '<%=customerType%>', '<%=rowID%>')">
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

                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=name%>" name="<%=name%>" maxlength="150" placeholder="Name" class="form-control txt_borderno" />
                </div>

                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=address%>" name="<%=address%>" maxlength="150" placeholder="Address" class="form-control txt_borderno" />
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="row">
                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=address1%>" name="<%=address1%>" maxlength="150" placeholder="Address 1" class="form-control txt_borderno" />
                </div>

                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=cityDesc%>" name="<%=cityDesc%>" maxlength="100" placeholder="City" class="form-control txt_borderno city_new_shipper" onblur="setCityDesc_New('<%=cityDesc%>', 'ebook_<%=customerType%>city<%=rowID%>')" />
                </div>

                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=countryName%>" name="<%=countryName%>" maxlength="100" placeholder="Country" class="form-control txt_borderno country_new_<%=customerType%>" onchange="loadStateAutoComplete('<%=countryName%>', '<%=state%>', '<%=typecustheadhidden%>');" onblur="setCountry_New('<%=countryName%>', '<%=country%>');" />
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="row">
                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=phone%>" name="<%=phone%>" maxlength="20" placeholder="Phone" class="form-control txt_borderno" />
                </div>

                <% if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=fax%>" name="<%=fax%>" maxlength="20" placeholder="Fax" class="form-control txt_borderno" />
                </div>
                <% } else {%>
                <input type="hidden" id="<%=fax%>" name="<%=fax%>" class="form-control txt_borderno" />
                <% }%>

                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=email%>" name="<%=email%>" maxlength="75" placeholder="Email" class="form-control email txt_borderno" />
                </div> 
            </div>
        </div>

        <div class="form-group">
            <div class="row">
                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=state%>" name="<%=state%>" maxlength="100" placeholder="State" class="form-control txt_borderno ebooking_state" onclick="validateState('<%=countryName%>', this);" />
                </div>

                <% if ("Y".equalsIgnoreCase(GENS)) {%>
                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=eori%>" name="<%=eori%>" maxlength="17" placeholder="EORI" class="form-control txt_borderno ebooking_eori" style="display: block;" />
                </div>

                <div class="col-xs-12 col-sm-4">
                    <select id="<%=persontype%>" name="<%=persontype%>" class="form-control txt_borderno" style="display: block;">
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
<!-- Additional fields are unchanged, continue... -->

<script>
    $('#<%=custSelect%>').val('<%=customergrid.getEbook_CustSelect()%>');

    $('#<%=persontype%>').val('<%=customergrid.getEbook_Custpersontype()%>');
    $('#<%=eori%>').val('<%=customergrid.getEbook_Custeori()%>');
    $('#<%=state%>').val('<%=customergrid.getEbook_Custstate()%>');
    $('#<%=email%>').val('<%=customergrid.getEbook_Custemail()%>');
    $('#<%=fax%>').val('<%=customergrid.getEbook_Custfax()%>');
    $('#<%=phone%>').val('<%=customergrid.getEbook_Custphone()%>');
    $('#<%=countryName%>').val('<%=customergrid.getEbook_Custcountry_desc()%>');
    $('#<%=cityDesc%>').val('<%=customergrid.getEbook_Custcity_desc()%>');

    $('#<%=address1%>').val('<%= customergrid.getEbook_Custaddress1().replace("\"", "\\\"").replace("'", "\\'") %>');
    $('#<%=address%>').val('<%= customergrid.getEbook_Custaddress().replace("\"", "\\\"").replace("'", "\\'") %>');
    $('#<%=name%>').val('<%=customergrid.getEbook_Custname()%>');
    $('#<%=country%>').val('<%=customergrid.getEbook_Custcountry()%>');
    $('#<%=city%>').val('<%=customergrid.getEbook_Custcity_desc()%>');
    $('#<%=typecustheadhidden%>').val('<%=customergrid.getEbook_CustSelect()%>');<!-- Double-check this usage -->
    $('#<%=cusid%>').val('<%=customergrid.getEbook_Custid()%>');
    $('#<%=code%>').val('<%=customergrid.getEbook_Custcode()%>');
    $('#<%=country_desc%>').val('<%=customergrid.getEbook_Custcountry_desc()%>');
    $('#<%=custSelect%>').val('');



</script>
<%
        } // end if custtypelist != null && contains code
    } // end for loop
    if ("Seller".equalsIgnoreCase(customerType)) {
        session.setAttribute("ebooking_seller_grid", null);
    }
    if ("Buyer".equalsIgnoreCase(customerType)) {
        session.setAttribute("ebooking_buyere_grid", null);
    }
} else {

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
    String cusid = "ebook_" + prefix + "id" + rowID;
    String code = "ebook_" + prefix + "code" + rowID;
    String country_desc = "ebook_" + prefix + "country_desc" + rowID;
    String countRowID = prefix + "countRowID";
    String typecustcountryheadhidden = "typecustcountryheadhidden" + rowID;


%>

<!-- Include all hidden fields -->
<input type="hidden" id="<%=country%>" name="<%=country%>" class="form-control txt_borderno" />
<input type="hidden" id="<%=city%>" name="<%=city%>" class="form-control txt_borderno" />
<input type="hidden" id="<%=cusid%>" name="<%=cusid%>" class="form-control txt_borderno" />
<input type="hidden" id="<%=code%>" name="<%=code%>"  class="form-control txt_borderno" />
<input type="hidden" id="<%=typecustheadhidden%>" name="<%=typecustheadhidden%>" class="form-control txt_borderno" />
<input type="hidden" id="<%=typecustcountryheadhidden%>" name="<%=typecustcountryheadhidden%>" class="form-control txt_borderno" />
<input type="hidden" id="<%=countRowID + rowID%>" name="<%=countRowID%>" class="form-control txt_borderno" value="<%=rowID%>" />

<% if (custtypelist != null && custtypelist.contains(custTypeCode)) { %>
<!-- New Customer Input Form -->
<div class="buyer-row position-relative p-2 mb-2 rounded">
    <% if (rowID != null && !"1".equalsIgnoreCase(rowID)) {%>
    <div class="section_header" style="padding: 14px;"> <%=customerType%>
    </div>
    <div class="remove-btn-wrapper position-absolute" style="top: 5px; right: 10px;">
        <span class="float-right">
            <a href="javascript:void(0)"  class="btn btn-success btn-sm btn_search" id="<%=searchCust%>custsearch<%=rowID%>" onclick="callsearchmethod('ebooking', 'ebookingSearch', '<%=searchCust%>', '<%=rowID%>')" readonly="readonly"><i class="fa fa-search"></i></a>
            <a href="javascript:void(0)" onclick="removeBuyerRow(this)" class="delete_cargo" style="color: red;">
                <i class="fa fa-times-circle fa-2x"></i>
            </a>            </span>

    </div>
    <%}%>
    <div id="<%=rowID%>" class="previewCust <%=rowID%>"  >
        <div class="form-group">
            <div class="row">
                <div class="col-xs-12 col-sm-4">
                    <select id="<%=custSelect%>" name="<%=custSelect%>" class="form-control txt_borderno" onchange="setAddCustDetails(this.value, '<%=customerType%>', '<%=rowID%>')">
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

                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=name%>" name="<%=name%>" maxlength="150" placeholder="Name" class="form-control txt_borderno" />
                </div>

                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=address%>" name="<%=address%>" maxlength="150" placeholder="Address" class="form-control txt_borderno" />
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="row">
                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=address1%>" name="<%=address1%>" maxlength="150" placeholder="Address 1" class="form-control txt_borderno" />
                </div>

                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=cityDesc%>" name="<%=cityDesc%>" maxlength="100" placeholder="City" class="form-control txt_borderno city_new_shipper" onblur="setCityDesc_New('<%=cityDesc%>', 'ebook_<%=customerType%>city<%=rowID%>')" />
                </div>

                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=countryName%>" name="<%=countryName%>" maxlength="100" placeholder="Country" class="form-control txt_borderno country_new_<%=customerType%>" onchange="loadStateAutoComplete('<%=countryName%>', '<%=state%>', '<%=typecustheadhidden%>');" onblur="setCountry_New('<%=countryName%>', '<%=country%>');" />
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="row">
                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=phone%>" name="<%=phone%>" maxlength="20" placeholder="Phone" class="form-control txt_borderno" />
                </div>

                <% if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=fax%>" name="<%=fax%>" maxlength="20" placeholder="Fax" class="form-control txt_borderno" />
                </div>
                <% } else {%>
                <input type="hidden" id="<%=fax%>" name="<%=fax%>" class="form-control txt_borderno" />
                <% }%>

                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=email%>" name="<%=email%>" maxlength="75" placeholder="Email" class="form-control email txt_borderno" />
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="row">
                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=state%>" name="<%=state%>" maxlength="100" placeholder="State" class="form-control txt_borderno ebooking_state" onclick="validateState('<%=countryName%>', this);" />
                </div>

                <% if ("Y".equalsIgnoreCase(GENS)) {%>
                <div class="col-xs-12 col-sm-4">
                    <input type="text" id="<%=eori%>" name="<%=eori%>" maxlength="17" placeholder="EORI" class="form-control txt_borderno ebooking_eori" style="display: block;" />
                </div>

                <div class="col-xs-12 col-sm-4">
                    <select id="<%=persontype%>" name="<%=persontype%>" class="form-control txt_borderno" style="display: block;">
                        <option value="">Type of Person</option>
                        <option value="Legal">Legal</option>
                        <option value="Natural">Natural</option>
                        <option value="Association of persons">Association of persons</option>
                    </select>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</div>
<% } // close BY check %>
<% } // close else for null grid %>
<% } catch (Exception e) {
        e.printStackTrace();
    }%>



