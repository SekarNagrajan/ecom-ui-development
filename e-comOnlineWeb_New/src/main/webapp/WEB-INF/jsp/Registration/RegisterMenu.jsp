<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.List" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%
    String strDateFormat = (String) session.getAttribute("SESSION_DATEFROMAT");
    String clientcode = (String) session.getAttribute("clientcode");
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Menu Creation</div>
<div class="card card-body card-shadow page_bottom_content">
    <div class="container">
        <div class="details_search">    
            <form id="registermenu"  method="post" action="/AdminAccess.do" class="form-horizontal" autocomplete="off">
                <input type="hidden" name="Stype" id="Stype" value="Save"/>
                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key="register.menuid" bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-2">
                        <input type="text" name="menuid"  id="menuid"  class="form-control  light_border  required "   maxlength="100" />
                    </div>
                    <label class="col-sm-2 control-label">Menu order</label>
                    <div class="col-sm-2">
                        <input type="text" name="menuOrder"  id="menuOrder"  class="form-control  light_border  required "   maxlength="100" />
                    </div>
                    <label class="col-sm-2 control-label"><bean:message key="register.menuname" bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-2">
                        <input type="text" name="menuname" id="menuname" class="form-control  light_border  required"  />
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key="register.usertype" bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-2">
                        <select name="usertype" id="usertype" onfocus="" class="form-control  light_border  required " >
                            <option value="S">-- Select --</option>
                            <option value="U">User</option>
                            <option value="V">Vendor</option>
                        </select>
                    </div>
                    <label class="col-sm-2 control-label">Developed By</label>
                    <div class="col-sm-2">
                        <input type="text" name="developedby" id="developedby" class="form-control  light_border " maxlength="100"/>
                    </div>
                    <label class="col-sm-2 control-label"><bean:message key="register.refno" bundle="<%=clientcode%>" /></label>
                    <div class="col-sm-2">
                        <input type="text" name="refno" id="refno" maxlength="100" class="form-control  light_border "/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key="register.status" bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-2">
                        <select name="status" id="status"  class="form-control  light_border  required" >
                            <option value="D">-- Select --</option>
                            <option value="A">ACTIVE</option>
                            <option value="I">INACTIVE</option>
                        </select>
                    </div>
                    <label class="col-sm-2 control-label"><bean:message key="register.category" bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-2">
                        <select name="category" id="category" class="form-control  light_border  required" >
                            <option value="S">Select Category</option>
                            <option value="D">Default</option>
                            <option value="P">Privileged</option>
                        </select>
                    </div>
                    <label class="col-sm-2 control-label"><bean:message key="register.date" bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-2">
                        <input type="text" name="date" id="date" class="form-control  light_border " readonly="true" />
                        <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key="register.createdby" bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-2">
                        <input type="text" name="createdby" id="createdby"  class="form-control light_border" maxlength="100"/>
                    </div>
                    <label class="col-sm-2 control-label"><bean:message key="register.classvalue" bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-2">
                        <input type="text" name="classvalue" id="classvalue"  class="form-control light_border" maxlength="100"/>
                    </div>
                    <label class="col-sm-2 control-label"><bean:message key="register.attrvalue" bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-2">
                        <input type="text" name="attrvalue" id="attrvalue"  class="form-control light_border" maxlength="100"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 control-label"><bean:message key="register.labelvalue" bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-2">
                        <input type="text" name="labelvalue" id="labelvalue"  class="form-control light_border" maxlength="100"/>
                    </div>
                    <label class="col-sm-2 control-label"><bean:message key="register.menutype" bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-2">
                        <input type="text" name="menutype" id="menutype"  class="form-control light_border" maxlength="100"/>
                    </div>
                    <label class="col-sm-2 control-label"><bean:message key="register.parentmenu" bundle="<%=clientcode%>"/></label>
                    <div class="col-sm-2">
                        <input type="text" name="parentmenu" id="parentmenu"  class="form-control light_border" maxlength="100"/>
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-sm-3 offset-md-5">
                        <button type="submit" class="btn btn-primary btn-lg" id="btnsearch"> Submit</button>
                        <button type="reset" class="btn btn-danger btn-lg" id="btnreset" onclick="cancelMenuReg();"> Reset</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<div id="menudetails" style="display:none;"></div>
<script>
    $(document).ready(function () {
    cancelMenuReg();
    $("#date").datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "yyyy/mm/dd"});
    document.getElementById('menuid').disabled = true;
    $("#menudetails").hide();
    openLoader();
    $.ajax({
    type: "POST",
            url: "Accesslogin.do?&type=prvView&usertype=Load"
    }).done(function (response) {
    $("#menudetails").html(response);
    $("#menudetails").show();
    /* auto generated menu id based on max of id */
    var maxid = document.getElementById('maxiid').value;
    document.getElementById('menuid').value = parseInt(maxid) + 1;
    closeLoader();
    });
    var errMsg = $("#maxiid").val();
    $("#err").html(errMsg);
    $("#registermenu").validate({
    submitHandler: function (form) {
    var menuId = document.getElementById('menuid').value;
    document.getElementById('menuid').disabled = true;
    //alert(menuId);
    var menuName = document.getElementById('menuname').value;
    var devlpdBy = document.getElementById('developedby').value;
    var status = document.getElementById('status').value;
    var date = document.getElementById('date').value;
    var refNo = document.getElementById('refno').value;
    var cretdBy = document.getElementById('createdby').value;
    var category = document.getElementById('category').value;
    var usertype = document.getElementById('usertype').value;
    var menuOrder = document.getElementById('menuOrder').value;
    var Stype = $("#Stype").val();
    var Type = Stype;
    $("#btnsearch").button('loading');
    if (Type == "Save") {
    Type = "prvSave";
    $.ajax({
    type: "POST",
            url: "AdminAccess.do?developedby=" + devlpdBy + "&menuname=" + menuName + "&date=" + date + "&refno=" + refNo + "&createdby=" + cretdBy + "&category=" + category + "&type=" + Type + "&status=" + status + "&usertype=" + usertype + "&menuOrder=" + menuOrder + "&classvalue=" + $('#classvalue').val() + "&attrvalue=" + $('#attrvalue').val() + "&labelvalue=" + $('#labelvalue').val() + "&menutype=" + $('#menutype').val() + "&parentmenu=" + $('#parentmenu').val()
    }).done(function (response) {
    $("#menudetails").html(response);
    cancelMenuReg();
    var maxid = document.getElementById('maxiid').value;
    document.getElementById('menuid').value = parseInt(maxid) + 1;
    $("#btnsearch").button('reset');
    });
    } else if (Type == "Edit") {
    Type = "prvEdit";
    $.ajax({
    type: "POST",
            url: "AdminAccess.do?developedby=" + devlpdBy + "&menuname=" + menuName + "&date=" + date + "&refno=" + refNo + "&createdby=" + cretdBy + "&category=" + category + "&type=" + Type + "&status=" + status + "&menuid=" + menuId + "&usertype=" + usertype + "&menuOrder=" + menuOrder + "&classvalue=" + $('#classvalue').val() + "&attrvalue=" + $('#attrvalue').val() + "&labelvalue=" + $('#labelvalue').val() + "&menutype=" + $('#menutype').val() + "&parentmenu=" + $('#parentmenu').val()
    }).done(function (response) {
    $("#menudetails").html(response);
    cancelMenuReg();
    var maxid = document.getElementById('maxiid').value;
    document.getElementById('menuid').value = parseInt(maxid) + 1;
    $("#btnsearch").button('reset');
    });
    }
    return false;
    }
    });
    });
    function editMenuReg(Menuid, Menuname, Devlpdby, Status, Mdate, Refno, Category, Cretdby, Maxid, usertype, menuorder,classvalue,attrvalue,labelvalue,menutype,parentmenu)
    {
    $('#Stype').val("Edit");
    $('#menuid').val(Menuid);
    $('#menuname').val(Menuname);
    if (Devlpdby != "")
    {
    $('#developedby').val(Devlpdby);
    } else
    {
    $('#developedby').val("");
    }

    $('#status').val(Status);
    if (Mdate != "")
    {

    $('#date').datepicker("setDate", new Date(Mdate));
    } else {
    $('#date').datepicker("setDate", "");
    }
    //  document.getElementById('date').value="";
    if (Refno != "")
    {
    $('#refno').val(Refno);
    } else
    {
    $('#refno').val("");
    }
    if (Cretdby != "")
    {
    $('#createdby').val(Cretdby);
    } else
    {
    $('#createdby').val("");
    }

    $('#category').val(Category);
    if (usertype == "User")
    {
    usertype = "U";
    } else {
    usertype = "V";
    }
    $('#usertype').val(usertype);
    if (menuorder != "")
    {
    $('#menuOrder').val(menuorder);
    } else
    {
    $('#menuOrder').val("");
    }
    $('#classvalue').val(classvalue);
    $('#attrvalue').val(unescape(attrvalue));
    $('#labelvalue').val(labelvalue);
    $('#menutype').val(menutype);
    $('#parentmenu').val(parentmenu);
    }

    function deleteMenuReg(MenuId, usertype)
    {

    var Type = "Delete";
    var serializedData = "";
    var menuId = MenuId;
    var con = confirm("Do you want to delete the Menu?");
    if (con)
    {
    document.getElementById('processLoad').style.display = "block";
    document.getElementById('menudetails').style.display = "none";
    Type = "prvdelete";
    $.post("AdminAccess.do?menuid=" + menuId + "&type=" + Type + "&usertype=" + usertype, serializedData, function (data) {
    $("#menudetails").html(data);
    cancelMenuReg();
    var maxid = document.getElementById('maxiid').value;
    document.getElementById('menuid').value = parseInt(maxid) + 1;
    document.getElementById('processLoad').style.display = "none";
    document.getElementById('menudetails').style.display = "block";
    });
    } else
    {
    return false;
    }

    }

    function cancelMenuReg()
    {
    // $("#registermenu").reset();

    document.getElementById('menuid').value = "";
    document.getElementById('menuname').value = "";
    document.getElementById('developedby').value = "";
    document.getElementById('status').value = "D";
    document.getElementById('date').value = "";
    document.getElementById('refno').value = "";
    document.getElementById('createdby').value = "";
    document.getElementById('category').value = "S";
    document.getElementById('usertype').value = "S";
    document.getElementById('menuOrder').value = "";
    $('#classvalue,#attrvalue,#labelvalue,#menutype,#parentmenu').val('');
    /* auto generated menu id based on max of id */
    //console.log('maxiid=========> '+document.getElementById('maxiid').value);
    if (document.getElementById('maxiid') != null) {
    var maxid = document.getElementById('maxiid').value;
    document.getElementById('menuid').value = parseInt(maxid) + 1;
    }


    }

    function fieldValidation()
    {

    var menuName = document.getElementById('menuname').value;
    var status = document.getElementById('status').value;
    var category = document.getElementById('category').value;
    var usertype = document.getElementById('usertype').value;
    var table = document.getElementById("RegisterMenu");
    var rows = table.rows;
    var rowCount = rows.length;
    var Stype = $("#Stype").val();
    if (Stype != "Edit") {
    if (rowCount > 2)
    {
    for (var i = 1; i < rowCount; i++)
    {
    cellval = rows[i].cells[1].innerHTML;
    if (menuName.toLowerCase() == cellval.toLowerCase())
    {
    alert("This Menu is already Registered");
    document.getElementById('menuname').focus();
    return false;
    }

    }
    }
    }

    if (menuName == "")
    {
    alert("Enter Menu Name");
    document.getElementById('menuname').focus();
    return false;
    }
    if (status == "D")
    {
    alert("Select Menu Status");
    return false;
    }
    if (category == "S")
    {
    alert("Select Menu Category");
    return false;
    }
    if (usertype == "S")
    {
    alert("Select User Type");
    return false;
    }
    return true;
    }
    function textLimit(field, maxlimit) {
    if (field.value.length > maxlimit) {
    field.value = field.value.substring(0, maxlimit);
    }
    }
</script>