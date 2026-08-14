<%@page  contentType="text/html; charset=UTF-8" language="java" import="java.sql.*,net.ecom.vo.ColumnList,java.util.*,net.ecom.vo.CustomerSearch,net.ecom.vo.CustomerProfile,net.ecom.common.GeneralConf,net.ecom.common.Validation,net.ecom.common.utils.DateConversion_New"  %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<div id="dynamicForm">
    <form name="DBColumn" method="post"  id ="DBColumn" class="form-horizontal">
        <%
            String select1 = "";
            String syscode = "";
            String cusname = "";
            String caddr = "";
            String city = "";
            String trColor = "";
            String taxNo = "";
            ArrayList book = (ArrayList) session.getAttribute("ListInfo");
            CustomerProfile cuspro = null;
            List colList = (List) request.getAttribute("SearchInfo");
            ColumnList column = null;
            String criteria = (String) request.getAttribute("Criteria");
            String fieldName = (String) request.getAttribute("FieldName");
            fieldName = (fieldName != null && fieldName != "") ? fieldName : "";
             String fieldID = null;
            if("dyBuyer".equalsIgnoreCase(fieldName) ||"dySeller".equalsIgnoreCase(fieldName) ){
             fieldID = request.getParameter("FieldID");
               System.out.println("[ECOM-WAR] DynamicSearchColumn fieldName==>"+fieldName+fieldID);
            }
           ;
            String searchTitle = (String) request.getAttribute("SearchTitle");
            searchTitle = (searchTitle != null && searchTitle != "") ? searchTitle : "";
            String coulmnCount = (String) request.getAttribute("ColumnCount");
            int colCount = Integer.parseInt(coulmnCount);
            String constraintsValue = (String) request.getAttribute("CONSTRIANTS_VALUE");
            constraintsValue = (constraintsValue != null && constraintsValue != "") ? constraintsValue : "";
            String constraints = (String) request.getAttribute("CONSTRIANTS_NAME");
            constraints = (constraints != null && constraints != "") ? constraints : "";
            String columnName = (String) request.getAttribute("COLUMN_NAME");
            columnName = (columnName != null && columnName != "") ? columnName : "";
            String EntityType = (String) request.getAttribute("ENTITY_TYPE");
            EntityType = (EntityType != null && EntityType != "") ? EntityType : "0";
            String aggrID = (String) request.getAttribute("AGGR_ID");
            aggrID = (aggrID != null && aggrID != "") ? aggrID : "";
            String ebookingIntransitDeliveryPort = (String) request.getAttribute("INTRANSIT_DELIVERYPORT");
            String clientcode = (String) session.getAttribute("clientcode");
            ebookingIntransitDeliveryPort = (ebookingIntransitDeliveryPort != null && (!"".equalsIgnoreCase(ebookingIntransitDeliveryPort))) ? ebookingIntransitDeliveryPort.trim() : "";
            String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
            String printOnBlAdd = (String) getServletContext().getAttribute(GeneralConf.PrintOnBlAddMandatory);

            int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
            String APCustCodeSearch = (String) getServletContext().getAttribute(GeneralConf.ENABLE_AP_CUSTCODE_SEARCH);

            Hashtable contrylist = (Hashtable) getServletContext().getAttribute("SESSION_CONTRYLIST");

        %>

        <input type="hidden" name="Criteria" id="Criteria" value="<%=criteria%>" />
        <input type="hidden" name="fieldName" id="fieldName" value="<%=fieldName%>" />
        <input type="hidden" name="fieldID" id="fieldID" value="<%=fieldID%>" />
        <input type="hidden" name="entry_type" id="entry_type" value="1" />
        <input type="hidden" name="type" id="type" value="<%=criteria%>" />
        <input type="hidden" name="aggrID" id="aggrID" value="<%=aggrID%>" />
        <input type="hidden" name="deliveryPort" id="deliveryPort" value="<%=ebookingIntransitDeliveryPort%>" />

        <div class="empty_label_space"> </div>
        <div class="form-group row">

            <label class="col-sm-1 control-label_dlg"> <bean:message key="Searchcolumn.SearchBy" bundle="<%=clientcode%>" /> </label>
            <div class="col-xs-12 col-sm-2">
                <select  name="columnName" id="columnName"  class="form-control border_dlg required">
                    <%
                        if (!criteria.equalsIgnoreCase("Vendor") && !criteria.equalsIgnoreCase("ebookingSearch") && !criteria.equalsIgnoreCase("esiSearch")) {
                    %>

                    <%
                        if (colList != null && colList.size() != 0) {
                            String colName = "";
                            String colType = "", columnCode = "";
                            int colSize = colList.size();
                            for (int c = 0; c < colSize; c++) {
                                column = (ColumnList) colList.get(c);
                                colName = column.getColumnName().trim();
                                colType = column.getColumnType().trim();
                                columnCode = column.getCode().trim();
                                StringBuffer sb = new StringBuffer(colName);
                                String colTitle = "";
                                sb.setCharAt(0, Character.toUpperCase(colName.charAt(0)));
                                colTitle = sb.toString();
                                if (colTitle.equalsIgnoreCase("CustCode")) {
                                    colTitle = "SystemCode";
                                }
                                if (("InTransit".equalsIgnoreCase(criteria)) || ("TypeOfMovement".equalsIgnoreCase(criteria))
                                        || ("carriageTermCode".equalsIgnoreCase(criteria))) {
                    %>


                    <option value="<%=columnCode%>" <%=(columnCode.equalsIgnoreCase(columnName)) ? "selected" : ""%>><%=colTitle%></option>


                    <%
                    } else if ("registSearch".equalsIgnoreCase(criteria)) {

                        if ("Email".equalsIgnoreCase(colTitle)) {
                    %>


                    <option value="<%=colName%>" <%=(colName.equalsIgnoreCase(columnName)) ? "selected" : ""%>>Login Id</option>

                    <%

                    } else {%>
                    <option value="<%=colName%>" <%=(colName.equalsIgnoreCase(columnName)) ? "selected" : ""%>><%=colTitle%></option>
                    <%
                        }
                    } else {
                    %>
                    <option value="<%=colName%>" <%=(colName.equalsIgnoreCase(columnName)) ? "selected" : ""%>><%=colTitle%></option>
                    <%}
                            }

                        }


                    %>
                    <%                    } else if (criteria.equalsIgnoreCase("ebookingSearch") || criteria.equalsIgnoreCase("esiSearch")) {

                    %>
                    <option value="custname" <%=("custname".equalsIgnoreCase(columnName)) ? "selected" : ""%>>Customer Name</option>
                    <option value="address1" <%=("address1".equalsIgnoreCase(columnName)) ? "selected" : ""%>>Address</option>
                    <%if ("Y".equalsIgnoreCase(APCustCodeSearch) && "agree".equalsIgnoreCase(fieldName)) {%>
                    <option value="custcode" <%=("custcode".equalsIgnoreCase(columnName)) ? "selected" : ""%>>Customer Code</option>
                    <% } %>
                    <%
                    %>

                    <%                                                    } else {

                    %>
                    <option value="Companyname" <%=("companyname".equalsIgnoreCase(columnName)) ? "selected" : ""%>>Companyname</option>
                    <option value="Carriercode" <%=("Carriercode".equalsIgnoreCase(columnName)) ? "selected" : ""%>>Carriercode</option>
                    <%

                        }
                    %>

                </select>
            </div>

            <label class="col-sm-1 control-label_dlg" ><bean:message key="Searchcolumn.Constraint" bundle="<%=clientcode%>" /></label>
            <div class="col-xs-12 col-sm-2">
                <select name="constraints" id="constraints" class="form-control border_dlg required" >
                    <option value="Con"  <%="Con".equalsIgnoreCase(constraints) ? "selected" : ""%>>Contains</option>
                    <option value="equal" <%="equal".equalsIgnoreCase(constraints) ? "selected" : ""%>>Equal To</option>
                    <option value="start" <%="start".equalsIgnoreCase(constraints) ? "selected" : ""%>>Starts With</option>
                    <option value="End" <%="End".equalsIgnoreCase(constraints) ? "selected" : ""%>>End With</option>
                </select>
            </div>
            <!--        </div>-->
            <!--        <div class="form-group row">-->
            <label class="col-sm-1 control-label_dlg" ><bean:message key="Searchcolumn.EnterValue" bundle="<%=clientcode%>" /></label>
            <div class="col-xs-12 col-sm-3">

                <input type="text" name="consValue" id="consValue" value="<%=constraintsValue%>" class="form-control border_dlg required" onkeypress="return stopRKey(event)"/>
            </div>
            <div class="col-xs-12 col-sm-2">
                <a href="javascript:void(0)" onclick="searchResult_new('<%=criteria%>')" class="btn btn-info btn_search_dlg" id="dynamiccustsearch">  <bean:message key="ecom.Search" bundle="<%=clientcode%>"/></a>
            </div>
        </div>

        <%
            if ("1".equalsIgnoreCase(EntityType)) {

        %>

        <%            if (criteria.equalsIgnoreCase("ebookingSearch")) {
        %>
        <div class="col-xs-12 col-sm-12">
            <h5 class="modal-title text-left"><b><bean:message key="ebooking.cust.custsearch" bundle="<%=clientcode%>" /></b>
                <hr>
            </h5>
            <div class="table-responsive" style="max-height: 280px">
                <table class="table table-striped table-bordered text-center" id="searchdatatable">
                    <thead>
                        <tr>
                            <th><bean:message key='Schedule.View.select' bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/></th>                            
                            <th><bean:message key="Reg.State" bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/></th> 
                        </tr>
                    </thead>
                    <tbody >

                        <%
                            if (book != null && book.size() > 0) {
                                int size = book.size();
                                String strStyleName = "";
                                for (int i = 0; i < size; i++) {
                                    trColor = "";
                                    cuspro = (CustomerProfile) book.get(i);
                                    select1 = cuspro.getEbooksearchImg();
                                    syscode = cuspro.getCustId();
                                    cusname = cuspro.getCustName();
                                    caddr = cuspro.getAddr1();
                                    city = cuspro.getCity();
                                    if (i % 2 == 0) {
                                        strStyleName = "even";
                                    } else {
                                        strStyleName = "odd";
                                    }
                        %>
                        <tr>
                            <td><%=select1%></td>
                            <td><%=cusname%></td>
                            <td><%=caddr%></td>
                            <td><%=city%></td>
                            <td><%=Validation.checkNull(cuspro.getState())%></td>
                            <td><%=contrylist.get(Validation.checkNull(cuspro.getCountry()))%></td>
                            <%}
                                }%>
                        </tr>



                    </tbody></table>
            </div>  
        </div>
        <%                                        } else if (criteria.equalsIgnoreCase("esiSearch")) {
        %>
        <div class="col-xs-12 col-sm-12">
            <h5 class="modal-title text-left"><b>  <bean:message key="ecom.shippingins.cussearch" bundle="<%=clientcode%>"/> </b>
                <hr>
            </h5>
            <div class="table-responsive" style="max-height: 280px">
                <table class="table table-striped table-bordered text-center" id="searchdatatable">
                    <thead>
                        <tr>
                            <th><bean:message key='Schedule.View.select' bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='ecom.shippingins.syscode' bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='ebooking.customername.display' bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/></th>
                            <th><bean:message key="Reg.State" bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='ebooking.country.display' bundle="<%=clientcode%>"/></th> 
                            <th><bean:message key="vgm.zipcode" bundle="<%=clientcode%>"/></th>
                            <!--                            <th>Tax Id</th>-->
                        </tr>
                    </thead>
                    <tbody>

                        <%
                            if (book != null && book.size() > 0) {
                                System.out.println("[ECOM-WAR] DynamicSearchColumn size.." + book.size());
                                int size = book.size();
                                String strStyleName = "";
                                for (int i = 0; i < size; i++) {
                                    trColor = "";
                                    cuspro = (CustomerProfile) book.get(i);
                                    select1 = cuspro.getSisearchImg();
                                    syscode = cuspro.getCustId();
                                    cusname = cuspro.getCustName();
                                    caddr = cuspro.getAddr1();
                                    city = cuspro.getCity();
                                    taxNo = cuspro.getVatno();

                                    if (i % 2 == 0) {
                                        strStyleName = "even";
                                    } else {
                                        strStyleName = "odd";
                                    }


                        %>



                        <tr>
                            <td><%=select1%></td>
                            <td><%=syscode%></td>
                            <td ><%=cusname%></td>
                            <td><%=caddr%></td>
                            <td><%=city%></td>
                            <td><%=Validation.checkNull(cuspro.getState())%></td>
                            <td><%=contrylist.get(Validation.checkNull(cuspro.getCountry()))%></td>
                            <td><%=Validation.checkNull(cuspro.getZipcode())%></td>
                            <%}
                                }%>
                        </tr>



                    </tbody></table>
            </div>
        </div>
        <%                                            } else if (criteria.equalsIgnoreCase("registSearch")) {
        %>
        <div class="col-xs-12 col-sm-12">
            <h5 class="modal-title text-left"><b><bean:message key='ebooking.cust.custsearch' bundle="<%=clientcode%>"/> </b>
                <hr>
            </h5>
            <div class="table-responsive" style="max-height: 280px">
                <table class="table table-striped table-bordered text-center" id="searchdatatable">
                    <thead>
                        <tr>
                            <th><bean:message key='ecom.shippingins.syscode' bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='SI.fileUpload.custName' bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='ecom.reg.loginid' bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='ebooking.adresscust.display' bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='ebooking.city.display' bundle="<%=clientcode%>"/></th>
                            <th><bean:message key="Reg.State" bundle="<%=clientcode%>"/></th>
                            <th><bean:message key="bl.country.display" bundle="<%=clientcode%>"/></th>
                            <th><bean:message key="ecom.registered.date" bundle="<%=clientcode%>"/></th>
                            <th><bean:message key='Schedule.View.select' bundle="<%=clientcode%>"/></th>
                        </tr>
                    </thead>
                    <tbody >

                        <%
                            String setCustomerName = null;
                            if (book != null && book.size() > 0) {
                                int size = book.size();
                                String strStyleName = "";
                                for (int i = 0; i < size; i++) {
                                    trColor = "";
                                    cuspro = (CustomerProfile) book.get(i);
                                    select1 = cuspro.getRegistSearchImg();
                                    syscode = cuspro.getCustId();
                                    cusname = cuspro.getCustName();
                                    caddr = cuspro.getAddr1();
                                    city = cuspro.getCity();
                                    setCustomerName = cuspro.getCustomerName();
                                    if (i % 2 == 0) {
                                        strStyleName = "even";
                                    } else {
                                        strStyleName = "odd";
                                    }
                        %>
                        <tr class="<%=strStyleName%>">
                            <td class=" "><%=syscode%></td>
                            <td class=" "><%=setCustomerName%></td>
                            <td class=" "><%=cusname%></td>
                            <td class=" "><%=caddr%></td>
                            <td class="center"><%=city%></td>
                            <td><%=Validation.checkNull(cuspro.getState())%></td>
                            <td><%=contrylist.get(Validation.checkNull(cuspro.getCountry()))%></td>
                            <td><%=Validation.checkNull(DateConversion_New.convertDefaultFormatwithTime(cuspro.getRegistereddate()))%></td>
                            <td class=" "><%=select1%></td>
                            <%}
                                }%>
                        </tr>
                    </tbody>
                </table>

            </div>
        </div>
        <%
                }
            }
        %>
    </form>
</div>
<script type="text/javascript" src="/Ecommaster/state.js?<%=jsno%>"></script>
<script type="text/javascript">
                    $(document).ready(function () {
                        $('[data-toggle="tooltip"]').tooltip();
                        $('#searchdatatable').DataTable({
                            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
                            "aaSorting": []
                        });
                    });
                    function searchResult_new(searchType) {
                        //$("#dynamiccustsearch").html("<i class='fa fa-spinner fa-spin'></i>");
                        var typeVal = "";
                        var params = "";
                        var remainingParameter = "";
                        typeVal = searchType;
                        remainingParameter = "&fieldName=" + escape(document.getElementById("fieldName").value) + "&entry_type=" + escape(document.getElementById("entry_type").value) + "&Criteria=" + escape(document.getElementById("Criteria").value) + "&columnName=" + escape(document.getElementById("columnName").value) + "&constraints=" + escape(document.getElementById("constraints").value) + "&consValue=" + escape(document.getElementById("consValue").value)+ "&FieldID=" + escape(document.getElementById("fieldID").value);
                        params = "type=" + typeVal + remainingParameter;
                        var URLStr = "DynamicColumn.do?";
                        $.ajax({
                            type: "POST",
                            url: URLStr + params,
                            success: function (response) {
                                $("#dynamicForm").html(response);
                            },
                            error: function (xhr) {
                                if (xhr.status == 403) {
                                    window.location.href = "SessionExpired.jsp";
                                }
                            }
                        });
                    }

                    var fname = "";
                    function seteBookingSearchInfo(custId, custName, addr, pobox, city, countryID, countryName, teleNo, fax, emailID, zipcode, citycode, addr1, state, eori, typeofperson) {
                        var fieldName1 = trim(document.getElementById('fieldName').value);
                        var fieldId = trim(document.getElementById('fieldID').value);
                        fname = fieldName1;
                        console.log(fieldName1);
                        if (state != null && state != 'null' && state.length > 0) {
                            var statejs = StateJson.filter(function (item) {
                                return (item["StateCode"] == state && item["CountryCode"] == trim(countryID))
                            });
                            state = state + ' - ' + statejs[0].StateName;
                        }
                        if (fieldName1 == 'notify1')
                        {
                            fieldName1 = "notify";
                            fname = fieldName1;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'name_n1').value = custName;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'code_n1').value = custId;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'name_n1').value = custName;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'name_n1').readOnly = true;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'address_n1').value = addr;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'address1_n1').value = addr1;
                            window.parent.document.getElementById(fieldName1 + 'Select').value = "";
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'email_n1').value = emailID;
                            //window.parent.document.getElementById('ebook_'+fieldName1+'country_name_n1').value=trim(trim(countryID)+' - '+trim(countryName));
                            window.parent.document.getElementById('ebook_notify1countryhdn').value = trim(trim(countryID) + ' - ' + trim(countryName));
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'country_n1').value = countryID;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'phone_n1').value = teleNo;
                            if ($('#ebook_' + fieldName1 + 'fax_n1', window.parent.document).is(":hidden")) {
                                window.parent.document.getElementById('ebook_' + fieldName1 + 'fax_n1').setAttribute("readonly", true);
                            } else {
                                window.parent.document.getElementById('ebook_' + fieldName1 + 'fax_n1').value = fax;
                            }
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'city_n1').value = trim(city);
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'city_desc_n1').value = trim(city);
                            if($('#ebook_' + fieldName1 + 'zipcode_n1', window.parent.document).length > 0) {
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'zipcode_n1').value = zipcode;
                            }
                               window.parent.callAutocompleteforsearch('#ebook_' + fieldName1 + 'country_name_n1', '#ebook_notify1countryhdn');
                            $('#ebook_' + fieldName1 + 'country_name_n1').trigger('change');
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'state_n1').value = state;
                            $('#ebook_' + fieldName1 + 'state').typeahead('val', state);
                            
                            if ($('#' + fieldName1 + 'typeperson_n1').length > 0) {
                                $('#' + fieldName1 + 'typeperson_n1').val(typeofperson);
                            }
                            if ($('#ebook_' + fieldName1 + 'eori_n1').length > 0) {
                                $('#ebook_' + fieldName1 + 'eori_n1').val(eori);
                            }
                        }else if(fieldName1 == 'dySeller' || fieldName1 == 'dyBuyer'){
                            console.log('fieldId==>'+fieldId);
                            var custType=fieldName1 == 'dySeller'?'Seller': fieldName1 == 'dyBuyer'?'Buyer':'';
                                window.parent.document.getElementById('ebook_' + custType + 'name'+fieldId).value = custName;
//                            window.parent.document.getElementById('ebook_' + custType + 'code'+fieldId).value = custId;
                              const codeElem = window.parent.document.getElementById('ebook_' + custType + 'code'+fieldId);
                            if (codeElem) {
                             codeElem.value = custId;
                                 }
                          const custCodeElem = window.parent.document.getElementById('ebook_' + custType + 'CustCode'+fieldId);
                          if (custCodeElem) {
                           custCodeElem.value = custId;
                           }
                            window.parent.document.getElementById('ebook_' + custType + 'name'+fieldId).value = custName;
                            window.parent.document.getElementById('ebook_' + custType + 'name'+fieldId).readOnly = true;
                            window.parent.document.getElementById('ebook_' + custType + 'address'+fieldId).value = addr;
                            window.parent.document.getElementById('ebook_' + custType + 'address1'+fieldId).value = addr1;
                            window.parent.document.getElementById('ebook_' +  custType + 'Select'+fieldId).value = "";
                            window.parent.document.getElementById('ebook_' + custType + 'email'+fieldId).value = emailID;
                            window.parent.document.getElementById('ebook_'+custType+'country_name'+fieldId).value=trim(trim(countryID)+' - '+trim(countryName));
//                            window.parent.document.getElementById('ebook_' + custType + 'countryhdn'+fieldId).value = trim(trim(countryID) + ' - ' + trim(countryName));
                            window.parent.document.getElementById('ebook_' + custType + 'country'+fieldId).value = countryID;
                            window.parent.document.getElementById('ebook_' + custType + 'phone'+fieldId).value = teleNo;
//                            if ($('#ebook_' + custType + 'fax', window.parent.document).is(":hidden")) {
//                                window.parent.document.getElementById('ebook_' + custType + 'fax'+fieldId).setAttribute("readonly", true);
//                            } else {
//                                window.parent.document.getElementById('ebook_' + custType + 'fax'+fieldId).value = fax;
//
//                            }
                            window.parent.document.getElementById('ebook_' + custType + 'city'+fieldId).value = trim(city);
                            window.parent.document.getElementById('ebook_' + custType + 'city_desc'+fieldId).value = trim(city);
                            window.parent.setCountry_name(custType,fieldId,'typecustcountryheadhidden'+fieldId);
                           
                            $('#ebook_' + custType + 'country_name'+fieldId).trigger('change');
                            window.parent.document.getElementById('ebook_' + custType + 'state'+fieldId).value = state;
                            $('#ebook_' + custType + 'state'+fieldId).typeahead('val', state);
//                            window.parent.clearCargoNameCode(custType);
                        } else
                        {
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'name').value = custName;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'code').value = custId;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'name').value = custName;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'name').readOnly = true;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'address').value = addr;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'address1').value = addr1;
                            window.parent.document.getElementById(fieldName1 + 'Select').value = "";
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'email').value = emailID;
                            //window.parent.document.getElementById('ebook_'+fieldName1+'country_name').value=trim(trim(countryID)+' - '+trim(countryName));
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'countryhdn').value = trim(trim(countryID) + ' - ' + trim(countryName));
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'country').value = countryID;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'phone').value = teleNo;
                            if ($('#ebook_' + fieldName1 + 'fax', window.parent.document).is(":hidden")) {
                                window.parent.document.getElementById('ebook_' + fieldName1 + 'fax').setAttribute("readonly", true);
                            } else {
                                window.parent.document.getElementById('ebook_' + fieldName1 + 'fax').value = fax;

                            }
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'city').value = trim(city);
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'city_desc').value = trim(city);
                            window.parent.callAutocompleteforsearch('#ebook_' + fieldName1 + 'country_name', '#ebook_' + fieldName1 + 'countryhdn');
                            window.parent.setagreefielddisabled(fieldName1);
                            $('#ebook_' + fieldName1 + 'country_name').trigger('change');
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'state').value = state;
                            if($('#ebook_' + fieldName1 + 'zipcode').length > 0){
                                window.parent.document.getElementById('ebook_' + fieldName1 + 'zipcode').value = zipcode;
                            }
                            $('#ebook_' + fieldName1 + 'state').typeahead('val', state);
                            if ('agree' == fieldName1) {
                                chkAPEnsFilingAgreement();
                            }

                            if ($('#' + fieldName1 + 'typeperson').length > 0) {
                                $('#' + fieldName1 + 'typeperson').val(typeofperson);
                            }
                            if ($('#ebook_' + fieldName1 + 'eori').length > 0) {
                                $('#ebook_' + fieldName1 + 'eori').val(eori);
                            }
                        }
                        $('#myModalSearch').modal('hide');
                    }

                    function trim(value) {
                        return LTrim(RTrim(value));
                    }
                    function LTrim(value) {
                        var re = /\s*((\S+\s*)*)/;
                        return value.replace(re, "$1");
                    }
                    function RTrim(value) {
                        var re = /((\s*\S+)*)\s*/;
                        return value.replace(re, "$1");
                    }

                    function setSISearchInfo(custId, custName, addr, addr1, pobox, city, countryID, countryName, teleNo, fax, emailID, zipcode, citycode, state, taxId, eori, typeofperson) {
    <%  String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);%>

                        var fieldName1 = trim(document.getElementById('fieldName').value);
                        fname = fieldName1;
                        var headerShipper = "";
                        if (fieldName1 == 'shipper' || fieldName1 == 'consignee' || fieldName1 == 'notify') {
                            $('#header' + fieldName1).attr('readonly', false);
                            $('#ebook_' + fieldName1 + 'nameheader').attr('readonly', true);
                            headerShipper = addr + " " + addr1 + " " + city + " " + state + " " + trim(zipcode) + " " + trim(countryName);
                            if (teleNo != '') {
                                headerShipper = headerShipper + " T: " + teleNo;
                            }
                            if (fax != '') {
    <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                                headerShipper = headerShipper + " F: " + fax;
    <%}%>
                            }
                            if (emailID != '') {
                                headerShipper = headerShipper + " Email:: " + emailID;
                            }
                            if (taxId != null && taxId != 'null' && taxId.length > 0 && taxId != '') {

                                headerShipper = headerShipper + " Tax Id: " + taxId;

                            }
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'nameheader').value = custName;
                            window.parent.document.getElementById('header' + fieldName1).value = headerShipper;
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'nameheader').setAttribute("readonly", true);
                            window.parent.document.getElementById('header' + fieldName1).setAttribute("readonly", false);
                        }
                        if (state != null && state != 'null' && state.length > 0) {
                            var statejs = StateJson.filter(function (item) {
                                return (item["StateCode"] == state && item["CountryCode"] == trim(countryID))
                            });
                            state = state + ' - ' + statejs[0].StateName;
                        }

                        window.parent.document.getElementById('ebook_' + fieldName1 + 'name').value = custName;
                        window.parent.document.getElementById(fieldName1 + 'CustCode').value = custId;
                        window.parent.document.getElementById('ebook_' + fieldName1 + 'name').value = custName;
                        window.parent.document.getElementById('ebook_' + fieldName1 + 'address').value = addr;
                        window.parent.document.getElementById('ebook_' + fieldName1 + 'address1').value = addr1;
                        window.parent.document.getElementById(fieldName1 + 'Select').value = "";
                        window.parent.document.getElementById('ebook_' + fieldName1 + 'email').value = emailID;
                        window.parent.document.getElementById('ebook_' + fieldName1 + 'country_name').value = trim(trim(countryID) + ' - ' + trim(countryName));
                        window.parent.document.getElementById('ebook_' + fieldName1 + 'country').value = countryID;
                        window.parent.document.getElementById('ebook_' + fieldName1 + 'phone').value = teleNo;
                        if ($('#ebook_' + fieldName1 + 'fax', window.parent.document).is(":hidden")) {
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'fax').setAttribute("readonly", true);
                        } else {
                            window.parent.document.getElementById('ebook_' + fieldName1 + 'fax').value = fax;
                        }
                        window.parent.document.getElementById('ebook_' + fieldName1 + 'city').value = trim(citycode);
                        window.parent.document.getElementById('ebook_' + fieldName1 + 'city').value = trim(citycode);
                        if ((fieldName1 == 'shipper' || fieldName1 == 'consignee' || fieldName1 == 'notify') && $('#' + fieldName1 + 'newcustimg').length > 0) {
                            window.parent.document.getElementById(fieldName1 + 'newcustimg').innerHTML = '<i class="fa fa-pencil-square" id="ccupdateimg"></i>  New';
                        }
                        window.parent.callAutocompleteforSIsearch('#ebook_' + fieldName1 + 'country_name', trim(trim(countryID) + ' - ' + trim(countryName)));
                        window.parent.enableHeaderInfo(fieldName1, '<%=printOnBlAdd%>');
                        $('#myModalSearch').modal('hide');
                        $('#ebook_' + fieldName1 + 'country_name').trigger('change');
                        window.parent.document.getElementById('ebook_' + fieldName1 + 'state').value = state;
                        $('#ebook_' + fieldName1 + 'state').typeahead('val', state);

                        if ($('#' + fieldName1 + 'typeperson').length > 0) {
                            $('#' + fieldName1 + 'typeperson').val(typeofperson);
                        }
                        if ($('#ebook_' + fieldName1 + 'eori').length > 0) {
                            $('#ebook_' + fieldName1 + 'eori').val(eori);
                        }
                        if($('#ebook_' + fieldName1 + 'zipcode').length > 0){
                            $('#ebook_' + fieldName1 + 'zipcode').val(zipcode);
                        }

                    }


</script>
<script type="text/javascript">

    function stopRKey(evt) {
        var evt = (evt) ? evt : ((event) ? event : null);
        var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
        if ((evt.keyCode == 13) && (node.type == "text")) {
            return false;
        }
    }
</script>
