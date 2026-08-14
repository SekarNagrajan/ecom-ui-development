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
            String clientcode = (String) session.getAttribute("clientcode");
            String HideFaxNo = (String) application.getAttribute(GeneralConf.HideFaxNo);
            String GENS = (String) application.getAttribute(GeneralConf.GENS);
            String customerType = "", custTypeCode = "", searchCust = null;
            customerType = request.getParameter("custype");
            System.out.println("customerType=>" + customerType + custtypelist);
            custTypeCode = "Buyer".equalsIgnoreCase(customerType) ? "BY" : "Seller".equalsIgnoreCase(customerType) ? "SL" : "";
            if (customerType != null && "Buyer".equalsIgnoreCase(customerType)) {
                custTypeCode = "BY";
                byList = (List<CustomerDetailsVO>) session.getAttribute("BYCUSTLIST_SI");
            } else if (customerType != null && "Seller".equalsIgnoreCase(customerType)) {
                custTypeCode = "SL";
                byList = (List<CustomerDetailsVO>) session.getAttribute("SLCUSTLIST_SI");
            }
            searchCust = "Buyer".equalsIgnoreCase(customerType) ? "dyBuyer" : "Seller".equalsIgnoreCase(customerType) ? "dySeller" : "";
            CustomerDetailsVO custVO = null;
            String rowID = request.getParameter("id");
            System.out.println("rowID===>>>>" + rowID);
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
            String ContainerId = customerType + "Container";
            String count = customerType + "Count";
%>

<!-- Include all hidden fields -->
<input type="hidden" id="<%=country%>" name="<%=country%>" class="form-control " />
<input type="hidden" id="<%=city%>" name="<%=city%>" class="form-control " />
<input type="hidden" id="<%=cusid%>" name="<%=cusid%>" class="form-control " />
<input type="hidden" id="<%=code%>" name="<%=code%>" class="form-control " />
<input type="hidden" id="<%=typecustheadhidden%>" name="<%=typecustheadhidden%>" class="form-control " />
<input type="hidden" id="<%=countRowID + rowID%>" name="<%=countRowID%>" class="form-control " value="<%=rowID%>" />
<input type="hidden" id="<%=typecustcountryheadhidden%>" name="<%=typecustcountryheadhidden%>" class="form-control " value="<%=rowID%>" />
<script>
    var customerType = '<%=customerType%>';
    var rowID = '<%=rowID%>';
    var country_name = '<%=countryName%>';
    var typecustheadhidden = '<%=typecustheadhidden%>';
    var state = '<%=state%>';
    var typecustcountryheadhidden = '<%=typecustcountryheadhidden%>';
</script>
<% if (custtypelist != null && custtypelist.contains(custTypeCode)) {%>
<!-- New Customer Input Form -->
<div class="buyer-row position-relative p-2 mb-2 rounded">
    <div class="section_header" style="padding: 14px;"><%=prefix%>
    </div>

    <div class="remove-btn-wrapper position-absolute" style="top: 5px; right: 10px;">
        <a href="javascript:void(0)"  class="btn btn-success search_symbol btn-xs" id="<%=searchCust%>custsearch<%=rowID%>" onclick="callsearchmethodsi('ebooking', 'ebookingSearch', '<%=searchCust%>', '<%=rowID%>')" readonly="readonly"><i class="fa fa-search"></i></a>
        <a href="javascript:void(0)" onclick="removeBuyerRow(this)" class="delete_cargo" style="color: red;">
            <i class="fa fa-times-circle fa-2x"></i>
        </a>
    </div>


    <div id="<%=rowID%>" class="previewCust <%=rowID%>"  >

        <div class=" row form-group">

            <div class="col-sm-1"> </div>
            <div class="col-sm-3">
                <select id="<%=custSelect%>" name="<%=custSelect%>" class="form-control  input_si" onchange="setAddCustDetails(this.value, '<%=customerType%>', '<%=rowID%>')">
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
                <input type="text" id="<%=name%>" name="<%=name%>" maxlength="150" placeholder="Name" class="form-control  input_si" />
            </div>
            <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>
            <div class="col-sm-3" >
                <input type="text" id="<%=address%>" name="<%=address%>" maxlength="150" placeholder="Address" class="form-control  input_si" />
            </div>

        </div>

        <div class=" row form-group">

            <label class="col-sm-1 control-label"><bean:message key="shipping.adresscust1.display" bundle="<%=clientcode%>"/>   </label>
            <div class="col-sm-3" >
                <input type="text" id="<%=address1%>" name="<%=address1%>" maxlength="150" placeholder="Address 1" class="form-control input_si " />
            </div>
            <label class="col-sm-1 control-label"><bean:message key="shipping.city.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>
            <div class="col-sm-3" >
                <input type="text" id="<%=cityDesc%>" name="<%=cityDesc%>" maxlength="100" placeholder="City" class="form-control  input_si city_new_shipper" onblur="setCityDesc_New('<%=cityDesc%>', 'ebook_<%=customerType%>city<%=rowID%>')" />
            </div>
            <label class="col-sm-1 control-label"><bean:message key="shipping.country.display" bundle="<%=clientcode%>"/> <span class="mandatory">* </span>  </label>
            <div class="col-sm-3" >
                <input type="text" id="<%=countryName%>" name="<%=countryName%>" maxlength="100" placeholder="Country" class="form-control input_si  country_new_<%=customerType%>" onchange="loadStateAutoComplete('<%=countryName%>', '<%=state%>', '<%=typecustheadhidden%>');" onblur="setCountry_New('<%=countryName%>', '<%=country%>');" />
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
                <input type="text" id="<%=eori%>" name="<%=eori%>" maxlength="17" placeholder="EORI" class="form-control  input_si ebooking_eori" style="display: block;" />
            </div>
            <label class="col-sm-1 control-label ebook_forwarderdiv"  ><bean:message key="Reg.typeofperson" bundle="<%=clientcode%>"/> </label>
            <div class="col-sm-3" >
                <select id="<%=persontype%>" name="<%=persontype%>" class="form-control  input_si" style="display: block;">
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
  <script>
    setCountry_name(customerType, rowID, 'typecustcountryheadhidden' + rowID);
    loadStateAutoComplete('ebook_' + customerType + 'country_name' + rowID,
            'ebook_' + customerType + 'state' + rowID,
            'typecustheadhidden' + rowID,
            'load');
    setSellerNameSelectOption();
</script>
<% }
    }%>

<% } catch (Exception e) {
        e.printStackTrace();
    }%>



