<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<style>
    /*    .modal-dialog {
            width: 800px;
            margin: 30px auto;
        }*/
</style>

<script>
    var savetype_new;
    $(document).ready(function () {

        $("#specialprivi").validate({

            submitHandler: function (form) {
                if (savetype_new == 'add') {

                    var custid = document.getElementById('selectuser').value;
                    //alert(custid);
                    $("#custselect").val(custid);
                    var ids = custid.split("~~");
                    var customerid = ids[0];
                    var webid = ids[1];
                    var Pcboxes = document.getElementsByName('privilege');
                    var Dcboxes = document.getElementsByName('default');
                    var Ncboxes = document.getElementsByName('newprivi');
                    var serializedData = "";
                    var Plen = Pcboxes.length;
                    var Dlen = Dcboxes.length;
                    var Nlen = Ncboxes.length;
                    var checkboxvalues = [];
                    var j = 0;
                    var menus = "";
                    var Nmenus = "";
                    if (custid == "")
                    {
                        alert("Select Customer");
                        return false;
                    }

                    for (var i = 0; i < Plen; i++) {
                        if (Pcboxes[i].checked)
                        {
                            if (j == 0)
                            {
                                menus = menus + Pcboxes[i].value;
                            } else
                            {
                                menus = menus + "_" + Pcboxes[i].value;
                            }
                            j++;
                        }
                    }
                    if (j == 0)
                    {
                        alert("Select Any Privilege Menu to Add");
                        return false;
                    }

                    if (Nlen > 0)
                    {
                        for (i = 0; i < Nlen; i++)
                        {
                            menus = menus + "_" + Ncboxes[i].value;
                        }
                    }
                    // document.getElementById('processLoad').style.display = "block";
                    $.post("SpecialPrivilege.do?Type=addMenu&CustCode=" + customerid + "&webid=" + webid + "&menuid=" + menus, serializedData, function (data) {
                        //  document.getElementById('processLoad').style.display = "none";
                        $("#spldiv").html("");
                        $("#spldiv").html(data);
                        $("#spldiv").show();
                        var cusid = $("#custselect").val();
                        $("#selectuser").val(cusid);
                        $('#mmaddalert').show();
                        $('#mmremovealert').hide();
                    });
                } else if (savetype_new == 'remove') {
                    $('#mmaddalert').hide();
                    $('#mmremovealert').hide();
                    var custid = document.getElementById('selectuser').value;
                    $("#custselect").val(custid);
                    var ids = custid.split("~~");
                    var customerid = ids[0];
                    var webid = ids[1];
                    var Pcboxes = document.getElementsByName('privilege');
                    var Dcboxes = document.getElementsByName('default');
                    var Ncboxes = document.getElementsByName('newprivi');
                    var serializedData = "";
                    var Plen = Pcboxes.length;
                    var Dlen = Dcboxes.length;
                    var Nlen = Ncboxes.length;
                    var checkboxvalues = [];
                    var j = 0, k = 0;
                    var menus = "";
                    var Nmenus = "";
                    if (custid == "")
                    {
                        alert("Select Customer");
                        return false;
                    }
                    for (var i = 0; i < Nlen; i++) {
                        if (!Ncboxes[i].checked)
                        {
                            if (j == 0)
                            {
                                menus = menus + Ncboxes[i].value;
                            } else
                            {
                                menus = menus + "_" + Ncboxes[i].value;
                            }
                            j++;
                        } else
                        {
                            k = 1;
                        }
                    }
                    if (k == 0)
                    {
                        alert("Select Any Privilege Menu to Remove");
                        return false;
                    }
                    //  document.getElementById('processLoad').style.display = "block";
                    $.post("SpecialPrivilege.do?Type=removeMenu&CustCode=" + customerid + "&webid=" + webid + "&menuid=" + menus, serializedData, function (data) {
                        //   document.getElementById('processLoad').style.display = "none";
                        $("#spldiv").html("");
                        $("#spldiv").html(data);
                        $("#spldiv").show();
                        var cusid = $("#custselect").val();
                        $("#selectuser").val(cusid);
                        $('#mmremovealert').show();
                    });

                }
                //alert();
                //$("#searchbtn").html('<span class="glyphicon glyphicon-search" ></span>Search');
                return false;
            }
        });

    });

    function setregistSearchInfo(custId, custName) {
        var custCode = custId + " ~~ " + custName;
        var serializedData = "";
        document.getElementById('selectuser').value = custCode;
        // document.getElementById('processLoad').style.display = "block";
        console.log("custName " + custName);
        $.post("SpecialPrivilege.do?Type=customer&webid=" + custName + "&CustCode=" + custId, serializedData, function (data) {
            $("#spldiv").html("");
            $("#spldiv").html(data);
            $("#spldiv").show();
            // document.getElementById('processLoad').style.display = "none";
        });
        $('#myModalSearch').modal('hide');

    }



    function addPrivMenu()
    {
        //alert("add");

        savetype_new = "add";
    }
    function removePrivMenu()
    {
        //alert("remove");
        savetype_new = "remove";
    }
</script>
<%
    ArrayList spllist = (ArrayList) session.getAttribute("SplPriviList");
    String custcode = (String) session.getAttribute("custcode");
    int tarsize = spllist.size();
    RegisterMenuVO registerVo = null;
    String menuId = "", name = "";
    String id = "", divid = "";
    int slno = 0;
    if (custcode == null) {
        custcode = "";
    }
    String clientcode = (String) session.getAttribute("clientcode");
%>
<div class="card-header page_header fixed-top"><img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Module Mapping</div>
<div class="card card-shadow page_bottom_content">
    <div class="card-body">
        <div class="container">
            <div class="details_search">
                <html:form styleId="specialprivi"  method="post" action="/SpecialPrivilege" styleClass="form-horizontal">
                    <div class="form-group alert-error" id="mmremovealert" style="display:none;margin-left: 15px;margin-right: 15px;" >
                        <br/>
                        <div class="alert alert-success alert-dismissible validatebox" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <strong>Success!</strong> Access has been removed to the below user.</div>
                    </div>
                    <div class="form-group alert-error" id="mmaddalert" style="display:none;margin-left: 15px;margin-right: 15px;" >
                        <br/>
                        <div class="alert alert-success alert-dismissible validatebox" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <strong>Success!</strong> Access has been given to the below user.</div>
                    </div>
                    <input type="hidden" id="custselect" name="checklist" value=""/>
                    <%
                        if (custcode != null) {

                    %>

                    <div class="form-group row">
                        <label class="col-sm-2 control-label"><bean:message key="specialprivi.seluser" bundle="<%=clientcode%>"/></label>

                        <div class="col-sm-4">
                            <html:text property="selectuser"  styleId="selectuser"  styleClass="form-control light_border required "  value=''  maxlength="100"  readonly="true"/>
                        </div>
                        <div class="col-sm-2">
                            <a href="#" onclick="callsearchmethod_mm('menuReg', 'registSearch', 'customer')" class="btn btn-success btn-xs call_search" id="modulemapsearch" title="Search"><i class="fa fa-search"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="resetfn()" class="btn btn-success btn-xs call_search" title="Reset"> <i class="fa fa-refresh"></i></a>
                        </div>
                    </div>

                    <%                                }
                    %>
                    <div id="spldiv">
                        <div class="container-fluid">

                            <div class="row">
                                <div class="col-sm-6">

                                    <div class="card">
                                        <div class="card-body">
                                            <h4 class="card-title"><span class="fa fa-sitemap"></span> Privileged</h4>



                                            <%
                                                //System.out.println("[ECOM-WAR] SpecialPrivilege  list --------" + spllist);
                                                String menuname = "";
                                                if (spllist != null && spllist.size() > 0) {
                                                    for (int i = 0; i < tarsize; i++) {
                                                        slno++;
                                                        //System.out.println("[ECOM-WAR] SpecialPrivilege  size  " + tarsize);
                                                        registerVo = (RegisterMenuVO) spllist.get(i);
                                                        //System.out.println("[ECOM-WAR] SpecialPrivilege  Menu Category ------" + registerVo.getCategory());
                                                        //System.out.println("[ECOM-WAR] SpecialPrivilege  Menu Name ------" + registerVo.getMenuName());
                                                        menuId = Integer.toString(registerVo.getMenuId());
                                                        id = "privilege_" + i;
                                                        name = "privilege_" + i;
                                                        divid = "divid_" + i;
                                                        menuname = registerVo.getMenuName();
                                                        if ("OBLPrint".equalsIgnoreCase(menuname)) {
                                                            menuname = "Bill of Lading";
                                                        }
                                                        if (registerVo.getCategory().equalsIgnoreCase("P")) {
                                            %>
                                            <div class="skin skin-flat">
                                                <label class="col-sm-8 control-label " style="text-align: left;margin-left:30px">

                                                    <input type="checkbox" name="privilege" id="<%=registerVo.getCategory()%>" value="<%=menuId%>"/> <div id="<%=divid%>" style="display:inline-block;"><%=menuname%></div> </label></div>


                                            <%
                                                        }
                                                    }
                                                }
                                            %>


                                            <div class="form-group row">
                                                <div class="col-sm-5"></div>
                                                <div class="col-sm-5">
                                                    <button type="submit" class="btn btn-success btn-lg" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="btnsearch" onclick="addPrivMenu();"> <span class="glyphicon glyphicon-search" ></span> Add</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </div>

                                <div class="col-sm-6">
                                    <div class="card">
                                        <div class="card-body">
                                            <h4 class="card-title"><span class="fa fa-sitemap"></span> Default </h4>

                                            <%
                                                //System.out.println("[ECOM-WAR] SpecialPrivilege  ---------" + spllist);
                                                if (spllist != null && spllist.size() > 0) {
                                                    for (int j = 0; j < tarsize; j++) {
                                                        slno++;
                                                        registerVo = (RegisterMenuVO) spllist.get(j);
                                                        //System.out.println("[ECOM-WAR] SpecialPrivilege  Menu Category ------" + registerVo.getCategory());
                                                        //System.out.println("[ECOM-WAR] SpecialPrivilege  Menu Name ------" + registerVo.getMenuName());
                                                        menuId = Integer.toString(registerVo.getMenuId());

                                                        id = "default_" + j;
                                                        name = "default_" + j;

                                                        menuname = registerVo.getMenuName();
                                                        if ("OBLPrint".equalsIgnoreCase(menuname)) {
                                                            menuname = "Bill of Lading";
                                                        }

                                                        if (registerVo.getCategory().equalsIgnoreCase("D") || registerVo.getCategory().equalsIgnoreCase("NP")) {
                                                            if (registerVo.getCategory().equalsIgnoreCase("D")) {
                                            %>
                                            <div class="skin skin-flat"> <label class="col-sm-8 control-label " style="text-align: left;margin-left:30px">
                                                    <input type="checkbox" name="default" id="<%=registerVo.getCategory()%>" disabled="disabled" value="<%=menuId%>"/> <%=menuname%></label> </div>
                                                    <%
                                                    } else if (registerVo.getCategory().equalsIgnoreCase("NP")) {
                                                    %>
                                            <div class="skin skin-flat"><label class="col-sm-8 control-label" style="text-align: left;margin-left:30px">
                                                    <input type="checkbox" name="newprivi" id="<%=registerVo.getCategory()%>" value="<%=menuId%>"/> <%=menuname%></label> </div>
                                                    <%
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    %>


                                            <div class="form-group row">
                                                <div class="col-sm-5"></div>
                                                <div class="col-sm-2">
                                                    <button type="submit" class="btn btn-danger btn-lg" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="btnremove" onclick="removePrivMenu();"> <span class="glyphicon glyphicon-search" ></span> Remove</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </html:form>
            </div>
        </div>
    </div>
</div>


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

</div>



<script type="text/javascript">
    function callsearchmethod_mm(formName, searchType, con) {
        var typeVal = "";
        var params = "";
        var remainingParameter = "";
        typeVal = searchType;
        remainingParameter = "&fieldName=" + con;
        params = "type=" + typeVal + remainingParameter;
        var URLStr = "DynamicColumn.do?" + params;
        $("#modulemapsearch").html("<i class='fa fa-spinner fa-spin'></i>");
        $.ajax({
            type: "POST",
            cache: false,
            url: URLStr,
            data: $(this).serializeArray(),
            success: function (data) {
                $("#modulemapsearch").html("<i class='fa fa-search'></i>");
                $("#myModalSearch .modal-body").html(data);
                $("#myModalSearch").modal("show");
                $("#myModalSearch .modal-backdrop").css({"position": "inherit"});
            }
        });
    }
</script>
<script type="text/javascript">
//    $(function () {
//        $(".modal-draggable .modal-dialog").draggable({
//            handle: ".panel-heading"
//        });
//    });
    function resetfn()
    {
        location.href = "AdminAccess.do?type=specialprivi";
        // $('#selectuser').val('');
        // $('.validatebox').css('display','none');
    }

</script>
<style>
    /*    .modal-draggable .panel-heading:hover {
            cursor: move;
        }*/
</style>

