<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.ArrayList,java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%
    String strDateFormat = (String) session.getAttribute("SESSION_DATEFROMAT");
    String clientcode = (String) session.getAttribute("clientcode");
%>
<style>
    /*    .modal-dialog {
            width: 800px;
            margin: 30px auto;
        }*/

</style>
<script>


    function setregistSearchInfo(custId, custName) {
        // try{

        var custCode = custId + " - " + custName;
        document.getElementById('txtvalue').value = custCode;
        $('#myModalSearch').modal('hide');


    }
    function inputField()
    {
        var serType = document.getElementById('searchType').value;
        if (document.getElementById('btn') != null) {
            document.getElementById('btn').style.paddingLeft = "0px";
        }

        if (serType == "DA")
        {
            document.getElementById('txtvalue').value = "";
            document.getElementById('fromdate').value = "";
            document.getElementById('todate').value = "";
            document.getElementById('fromdiv').style.display = "block";
            document.getElementById('todiv').style.display = "block";
            document.getElementById('valuediv').style.display = "none";
            document.getElementById('datediv').style.display = "block";
            document.getElementById('fromddiv').style.display = "block";
            document.getElementById('search_img').style.display = "none";
            //  document.getElementById('datediv').style.display="inline-block";
            document.getElementById('txtdiv').style.display = "none";
            document.getElementById('imgdiv').style.display = "none";
            var datFormat = jQuery.trim(document.getElementById("txtdateFormat").value);

        }
        if (serType == "UN")
        {
            document.getElementById('fromdate').value = "";
            document.getElementById('todate').value = "";
            document.getElementById("txtvalue").readOnly = true;
            document.getElementById('txtvalue').value = "";
            document.getElementById('valuediv').style.display = "block";
            document.getElementById('search_img').style.display = "block";
            document.getElementById('fromdiv').style.display = "none";
            document.getElementById('fromddiv').style.display = "none";
            document.getElementById('todiv').style.display = "none";
            document.getElementById('datediv').style.display = "none";
            document.getElementById('txtdiv').style.display = "inline-block";
            document.getElementById('imgdiv').style.display = "block";
        }
        if (serType == "RN")
        {
            document.getElementById('fromdate').value = "";
            document.getElementById('todate').value = "";
            document.getElementById("txtvalue").readOnly = false;
            document.getElementById('txtvalue').value = "";
            document.getElementById('valuediv').style.display = "block";
            document.getElementById('search_img').style.display = "none";
            document.getElementById('fromdiv').style.display = "none";
            document.getElementById('fromddiv').style.display = "none";
            document.getElementById('todiv').style.display = "none";
            document.getElementById('datediv').style.display = "none";
            document.getElementById('txtdiv').style.display = "inline-block";
            document.getElementById('imgdiv').style.display = "none";
        }
    }
    function resetFields()
    {
        document.getElementById('searchType').value = "S";
        document.getElementById('txtvalue').value = "";
        document.getElementById('fromdate').value = "";
        document.getElementById('todate').value = "";
        document.getElementById('search_img').style.display = "none";
        document.getElementById('txtvalue').disabled = false;
        document.getElementById('txtdiv').style.display = "block";
        document.getElementById('datediv').style.display = "none";
        document.getElementById('fromdiv').style.display = "none";
        document.getElementById('todiv').style.display = "none";
        document.getElementById('imgdiv').style.display = "none";
        document.getElementById('valuediv').style.display = "block";
        document.getElementById('fromddiv').style.display = "none";
        document.getElementById('searchType').disabled = false;
        document.getElementById('fromdate').disabled = false;
        document.getElementById('todate').disabled = false;
        document.getElementById('txtvalue').value = "";
        document.getElementById('searchType').value = "S";


    }
</script>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> User Audit</div>

<div class="card card-body card-shadow page_bottom_content">
    <div class="container">
        <div class="details_search">        

            <html:form  styleId="userAudit" method="post" action="UserAudit.do" onsubmit="" styleClass="form-horizontal">
                <input type="hidden" id="txtdateFormat" name="txtdateFormat" value="<%=strDateFormat%>"/>
                <input type="hidden" id="selectuser" name="selectuser" value=""/>
                <input type="hidden" id="tmptype" name="tmptype" value=""/>
                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key="usradt.searchtype" bundle="<%=clientcode%>"/>
                    </label>
                    <div class="col-sm-3">
                        <html:select property="searchType" styleId="searchType" styleClass="form-control light_border required" onchange="inputField();">
                            <html:option value="S">Select Type</html:option>
                            <html:option value="UN">UserName</html:option>
                            <html:option value="DA">Date</html:option>
                            <html:option value="RN">Reference No</html:option>
                        </html:select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 control-label" id="valuediv"><bean:message key="usradt.value" bundle="<%=clientcode%>"/></label>
                    <label class="col-sm-2 control-label" id="fromdiv" style="display:none;">From Date</label>
                    <div class="col-sm-3" id="txtdiv">
                        <html:text styleId="txtvalue" styleClass="form-control light_border required" property="txtvalue"    readonly="true"/>

                    </div>
                    <div class="col-sm-2" id="imgdiv" style="display:none;">
                        <a href="#" onclick="callsearchmethod_mm('menuReg', 'registSearch', 'customer')" id="search_img" class="btn btn-success btn-xs call_search"><i class="fa fa-search"></i></a>
                    </div>
                    <div class="col-sm-3" id="datediv" style="display: none;">
                        <html:text property="fromdate" styleId="fromdate" styleClass="form-control light_border " readonly="true"/>
                        <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                    </div>
                </div>
                <div class="form-group row">

                    <label class="col-sm-2 control-label" id="todiv" style="display:none;">To Date</label>
                    <div class="col-sm-3" id="fromddiv" style="display: none;">
                        <html:text property="todate" styleId="todate" styleClass="form-control light_border " readonly="true"/>
                        <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 control-label" ></label>
                  
                     <div class="col-sm-5">
                        <button type="submit" class="btn btn-primary btn-lg search_button" id="btnsearch"><span class="glyphicon glyphicon-search"></span> Search</button>
                        <button type="button" class="btn btn-danger btn-lg search_button" id="btnreset" onclick="resetFields();" > <span class="glyphicon glyphicon-refresh"></span> Reset</button>
                    </div>

            </html:form>

        </div>
    </div>
</div>
<div id="logdetails"></div>


<div class="modal fade" id="myModalSearch" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <div class="modal-header bg-info text-white "> <h5 class="modal-title"> Customer Search</h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">

            </div>
        </div>

    </div>

</div><!-- /.modal-dialog -->



<script type="text/javascript">
    resetFields();
    $(document).ready(function () {
        $('#fromdate').datepicker({autoclose: true, todayHighlight: true, format: "yyyy/mm/dd"});
        $('#todate').datepicker({autoclose: true, todayHighlight: true, format: "yyyy/mm/dd"});
        $("#userAudit").validate({

            submitHandler: function (form) {
                var value = "";
                var fromdate = "";
                var todate = "";
                var serType = document.getElementById('searchType').value;
                var datFormat = "yy/mm/dd";
                document.getElementById('logdetails').style.display = "none";
                if (serType == "UN")
                {
                    serType = "username";
                    value = document.getElementById('txtvalue').value;
                }
                if (serType == "DA")
                {
                    serType = "Logdate";
                    fromdate = document.getElementById('fromdate').value;
                    todate = document.getElementById('todate').value;
                    value = fromdate + "~" + todate;
                }
                if (serType == "RN")
                {
                    //
                    serType = "referenceno";
                    value = document.getElementById('txtvalue').value;
                }
                //alert(document.getElementById('searchType').value);
                $("#btnsearch").button('loading');
                $.ajax({
                    type: "POST",
                    url: "UserAudit.do?serType=" + serType + "&value=" + value + "&datFormat=" + datFormat
                }).done(function (response) {
                    $("#logdetails").html(response);
                    $("#btnsearch").button('reset');
                    document.getElementById('processLoad').style.display = "none";
                    document.getElementById('logdetails').style.display = "block";
                    document.getElementById('txtvalue').disabled = true;
                    document.getElementById('fromdate').disabled = true;
                    document.getElementById('todate').disabled = true;
                    document.getElementById('searchType').disabled = true;
                    document.getElementById('search_img').disabled = true;
                });


                return false;
            }
        });
    });
</script>

<script type="text/javascript">
    function callsearchmethod_mm(formName, searchType, con) {
        var typeVal = "";
        var params = "";
        var remainingParameter = "";
        typeVal = searchType;
        remainingParameter = "&fieldName=" + con;
        params = "type=" + typeVal + remainingParameter;
        var URLStr = "DynamicColumn.do?" + params;
        $("#search_img").html("<i class='fa fa-spinner fa-spin'></i>");
        $.ajax({
            type: "POST",
            cache: false,

            url: URLStr,
            data: $(this).serializeArray(),
            success: function (data) {
                $("#search_img").html("<i class='fa fa-search'></i>");
                $("#myModalSearch .modal-body").html(data);
                $("#myModalSearch").modal("show");
            }
        });
    }
</script>




