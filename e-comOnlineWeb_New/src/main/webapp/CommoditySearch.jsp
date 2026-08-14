<%-- 
    Document   : CommoditySearch
    Created on : Mar 29, 2018, 2:24:48 PM
    Author     : deepa.p
--%>

<%@page import="net.ecom.common.GeneralConf"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%
    String comId = (String) request.getAttribute("BKGcomid");
    String hsCodeId = (String) request.getAttribute("BKGhscodeid");
    String rowid = (String) request.getAttribute("BKGrowid");
    String screenname = (String) request.getAttribute("screenname");
    System.out.println("[ECOM-WAR] CommoditySearch comId>> " + comId+" hsCodeId>>" + hsCodeId+" rowid>>" + rowid);
    String clientcode = (String) session.getAttribute("clientcode");
    String openCommodityJson = (String) getServletContext().getAttribute("OpenCommodityJson");
    String frontcmdcode = (String) getServletContext().getAttribute(GeneralConf.Display_Commodity_Code_Front);
%>
<div id="commodityModalFormdiv">
    <div class="empty_label_space"> </div>
    <div class="form-group row">
        <label class="col-sm-1 control-label_dlg"> <bean:message key="Searchcolumn.SearchBy" bundle="<%=clientcode%>" /> </label>
        <div class="col-xs-12 col-sm-2">
            <select  name="columnName" id="columnName"  class="form-control border_dlg required">
                <option value="comdtyname">Commodity Name</option>
                <option value="comdtycode">Commodity Code</option>
            </select>
        </div>

        <label class="col-sm-1 control-label_dlg"> <bean:message key="Searchcolumn.Constraint" bundle="<%=clientcode%>" /> </label>
        <div class="col-xs-12 col-sm-2">
            <select  name="constraint" id="constraint"  class="form-control border_dlg required">
                <option value="contains">Contains</option>
                <option value="equalto">Equal To</option>
                <option value="startwith">Start With</option>
                <option value="endwith">End With</option>
            </select>
        </div>
        <!--        </div>
                <div class="form-group row">-->
        <label class="col-sm-1 control-label_dlg"><bean:message key="Searchcolumn.EnterValue" bundle="<%=clientcode%>" /> </label>
        <div class="col-xs-12 col-sm-3">
            <input type="text" name="searchvalue" id="searchvalue" value="" class="form-control border_dlg required"  onkeydown="handleEnterKey(event)"/>
        </div>
        <div class="col-xs-12 col-sm-2">
            <a href="javascript:void(0)" onclick="searchMatchCommodity();" class="btn btn-info btn_search_dlg" id="shippersearch"><bean:message key="ecom.Search" bundle="<%=clientcode%>" /></a> 
        </div>
    </div>
    <div id="commoditySearchView" style="display: none">

    </div>
    <script type="text/javascript">
        var comId = '<%=comId%>';
        var hsCodeId = '<%=hsCodeId%>';
        var rowid = '<%=rowid%>';
        var screenname = '<%=screenname%>';
        var openCommodityJson = '<%=openCommodityJson%>';
        var frontcmdcode = '<%=frontcmdcode%>';
        var openCommodityJsonArray = JSON.parse(openCommodityJson);
        function handleEnterKey(event) {
            if (event.key === "Enter") {
                event.preventDefault(); // Prevent form submission if inside a form
                searchMatchCommodity();
            }
        }
        function searchMatchCommodity() {
            var value = $('#searchvalue').val();

            if (value != '') {
                var cmdval = '';
                if ($('#' + comId).val() != null && $('#' + comId).val() != '') {
                    var cmdval = $('#' + comId).val().split('~~')[1].trim();
                    if (openCommodityJsonArray.includes(cmdval)) {
                        isopencmdt = 'Y';
                        cmdval = '';
                    }
                }
                var columnName = $('#columnName').val();
                var constraint = $('#constraint').val();
                $.ajax({
                    type: "POST",
                    cache: false,
                    url: "BookingAjax?type=seachcommodity&value=" + encodeURIComponent(value) + "&columnName=" + encodeURIComponent(columnName) + "&constraint=" + encodeURIComponent(constraint) + "&groupcode=" + cmdval,
                    data: $(this).serializeArray(),
                    success: function (data) {
                        $("#commoditySearchView").html(data);
                        $('#commoditySearchView').show();
                    },
                    error: function (xhr) {
                        if (xhr.status == 403) {
                            window.location.href = "SessionExpired.jsp";
                        }
                    }
                });
            } else {
                alert('<bean:message key="ecom.msg.entvaltosearch" bundle="<%=clientcode%>"/>');
            }
        }
        function setComdtyForParent(comdtyCode, hscode, hsname) {
            var comdtyName;
            $.ajax({
                type: "POST",
                cache: false,
                url: "BookingAjax?type=getCommodityName&code=" + encodeURIComponent(comdtyCode),
                async: false,
                data: $(this).serializeArray(),
                success: function (data) {
                    comdtyName = data;
                    parent.setComdty(comdtyCode, comdtyName, hscode, hsname);
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });

        }
        function setComdty(comdtyCode, comdtyName, hscode, hsname) {
            var isopencmdt = 'N';
            if ($('#' + comId).val() != null && $('#' + comId).val() != '') {
                var cmdval = $('#' + comId).val().split('~~')[1].trim();
                if (openCommodityJsonArray.includes(cmdval)) {
                    isopencmdt = 'Y';
                }
            }
            if (isopencmdt == 'N' && screenname == 'BKG') {
                $('#' + comId).typeahead('destroy');
                $('#' + hsCodeId).typeahead('destroy');
                $('#hdcommoidtycode' + rowid).val(comdtyCode);
                commodityload(comId, 'hdcommoidtycode' + rowid, rowid);
                $('#' + comId).typeahead('val', comdtyName + " ~~ " + comdtyCode);
            }
            if (frontcmdcode == 'Y') {
                $('#' + hsCodeId).val("(" + hscode + ") " + hsname + " ~~ " + hscode).trigger("change").trigger("blur");
                $('#' + hsCodeId).typeahead('val', "(" + hscode + ") " + hsname + " ~~ " + hscode);
            } else {
                $('#' + hsCodeId).val(hsname + " ~~ " + hscode).trigger("change").trigger("blur");
                $('#' + hsCodeId).typeahead('val', hsname + " ~~ " + hscode);
            }
            if (screenname == 'SI') {
                var hidhscodeid = hsCodeId.replace("hscode", "hidhscodeval");
                $('#' + hidhscodeid).val(hsname + " ~~ " + hscode);
            }
            $('#comdtySearchModal').modal('hide');
        }
    </script>
</div>