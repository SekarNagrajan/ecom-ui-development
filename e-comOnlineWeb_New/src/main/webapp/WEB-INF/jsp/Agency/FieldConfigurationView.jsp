<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.FieldConfigVO,java.util.List,java.util.Hashtable,java.util.Map" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<hr/>
<div class="table-responsive card card-body card-shadow">
    <table class="table table-striped table-bordered" id="fieldconfigtable">
        <thead>
            <tr>
                <th>S.No</th>
                <th>Field Name</th>
                <th>Search Type</th>
                <th>Search Value</th>
                <th>Shipment Type</th>
                <th>Input Type</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <%
                Hashtable portlist = (Hashtable) getServletContext().getAttribute("SESSION_PORTLIST");
                Map<String, String> fieldConfigMap = (Map<String, String>) getServletContext().getAttribute("FIELDNAMECONFIGMAP");
                Map<String, String> countrymap = (Map<String, String>) getServletContext().getAttribute("SESSION_CONTRYLIST");
                List<FieldConfigVO> fieldConfigList = (List<FieldConfigVO>) request.getAttribute("FIELDCONFIGLIST");
                FieldConfigVO configVO = null;
                String searchValue = null, searchType = null, shipmentType = null, inputType = null, status = null, fieldName = null;
                if (fieldConfigList != null && fieldConfigList.size() > 0) {
                    for (int i = 0; i < fieldConfigList.size(); i++) {
                        configVO = fieldConfigList.get(i);
                        searchType = configVO.getSearchType();
                        if ("P".equalsIgnoreCase(searchType)) {
                            searchValue = configVO.getSearchCode() + " - " + Validation.checkNull((String) portlist.get(configVO.getSearchCode()));
                            searchType = "By Port";
                        } else {
                            searchValue = configVO.getSearchCode() + " - " + Validation.checkNull((String) countrymap.get(configVO.getSearchCode()));
                            searchType = "By Country";
                        }
                        shipmentType = configVO.getShipmentType();
                        if ("E".equalsIgnoreCase(shipmentType)) {
                            shipmentType = "Export";
                        } else {
                            shipmentType = "Import";
                        }
                        inputType = configVO.getInputType();
                        if ("M".equalsIgnoreCase(inputType)) {
                            inputType = "Mandatory";
                        } else if ("N".equalsIgnoreCase(inputType)) {
                            inputType = "Non Mandatory";
                        } else if ("E".equalsIgnoreCase(inputType)) {
                            inputType = "Enable";
                        } else if ("D".equalsIgnoreCase(inputType)) {
                            inputType = "Disable";
                        }
                        status = configVO.getStatus();
                        if ("I".equalsIgnoreCase(status)) {
                            status = "Inactive";
                        } else {
                            status = "Active";
                        }
                        fieldName = fieldConfigMap.get(configVO.getFieldCode());
            %>
            <tr id="rowid<%=i%>">
                <td><%=(i + 1)%></td>
                <td><%=fieldName%></td>
                <td><%=searchType%></td>
                <td><%=searchValue%></td>
                <td><%=shipmentType%></td>
                <td><%=inputType%></td>
                <td><%=status%></td>
                <td>
                    <a href="javascript:void(0)" onclick="editFieldConfigDetails('<%=configVO.getFieldCode()%>', '<%=configVO.getSearchType()%>', '<%=searchValue%>', '<%=configVO.getShipmentType()%>', '<%=configVO.getInputType()%>', '<%=configVO.getStatus()%>', '<%=configVO.getId()%>')"  title="Edit" class="btn btn-lg text-warning">
                        <i class="fa fa-edit"></i></a>
                    <a href="javascript:void(0)" onclick="deleteFieldConfigDetails('<%=configVO.getId()%>')"  title="Delete" class="btn btn-lg text-danger">
                        <i class="fa fa-trash-o"></i></a>
                </td>
            </tr>
            <%                            }
                }%>
        </tbody>
    </table>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        var ptable = $('#fieldconfigtable').DataTable({
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            "aaSorting": []
        });
    });
</script>


