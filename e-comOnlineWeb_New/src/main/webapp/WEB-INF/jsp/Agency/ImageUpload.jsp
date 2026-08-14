<%-- 
    Document   : ImageUpload
    Created on : Jun 14, 2018, 11:33:33 AM
    Author     : gobinath.t
--%>
<!DOCTYPE html>
<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.List,java.util.Hashtable" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%
    String check = (String) request.getAttribute("ImgupType");
%>
<div class="card-header page_header fixed-top">  <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Image Upload</div>
<div class="card card-shadow page_bottom_content">
    <div class="card-body">
        <div class="container">
            <div class="details_search">


                <html:form styleId="ImageUploadForm" method="post" action="/ImageUpload" styleClass="form-horizontal" enctype="multipart/form-data">
                    <input type="hidden" name="Stype" id="Stype" value="Save"/>
                    <html:hidden property="keyid" styleId="keyid" />

                    <div class="form-group row" >
                        <label class="col-sm-1 control-label">Name<span class="mandatory">*</span></label>
                        <div class="col-sm-3">
                            <html:text property="imagename" styleId="imagename" styleClass="form-control light_border required" maxlength="18"></html:text>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-1 control-label">Image<span class="mandatory">*</span></label>
                            <div class="col-sm-3">
                                <input type="text" id="imagepath" name="imagepath" class="form-control  light_border "  autocomplete="off" readonly/>
                            <html:file style="display: none;" styleId="image" onchange="setImagePath();" styleClass="form-control  light_border required" property="image" ></html:file>
                            <label style="color:#ee3942" id="pngerror">${pngerror}</label>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="btn btn-success btn-lg" id="resetID" onclick="openfile();"> <span class="fa fa-upload" ></span> Upload</button>
                        </div>
                    </div>


                    <div class="form-group row">
                        <div class="col-sm-offset-1 col-sm-5">
                            <button id="submitbtn" type="submit" class="btn btn-primary btn-lg search_button">  Submit</button>
                            <button type="button" class="btn btn-danger btn-lg search_button" id="resetID" onclick="resetConfigValues();"> <span class="glyphicon glyphicon-refresh" ></span> Reset</button>
                        </div>
                    </div>

                    <label class="col-sm-offset-1" style="color:#ee3942"><b>Note :</b> Upload JPG Image alone.<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Use Image Resolution : 1340 X 450 Pixels.</label><br>
                    <label class="col-sm-offset-1 control-label error">${limitexceedederror}</label>
                </html:form>



                <div class="modal fade" id="Rearrangedialog" role="dialog">
                    <div class="modal-dialog modal-lg"> 
                        <div class="modal-content">
                            <div class="modal-header bg-info text-white "> <h5 class="modal-title">Rearrange <i  id="topdialogspiner" idaria-hidden="true"></i></h5>  <div class="pull-right">
                                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                </div>
                            </div>
                            <div class="modal-body">
                                <div id="Imgrearrangeorder"></div>
                            </div>
                            <div class="modal-footer">
                                <div class="pull-right"> <a href="javascript:void(0)" title="Save"  class="btn btn-primary btn-xs" id="rearrangesavebtn" onclick="RearrangeOrder();"> Save</a></div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<div id="imguploadtabledetails" style="display: none"></div>

<script type="text/javascript">

    var serializedData = "";
    $(document).ready(function () {
    <%if (!"error".equalsIgnoreCase(check)) {%>
        $("#imagename").val("");
        $("#imagepath").val("");
        $('#ImageUploadForm label.error').remove();
        $("#image").val("");
        $("#imagename").attr("readonly", false);
        $("#pngerror").hide();
    <%}%>
        $('#imguploadtabledetails').hide();
//        $.ajax({
//            type: "POST",
//            cache: false,
//            url: "Accesslogin.do?type=getImageUploadDetails&showactive=N",
//            success: function (response) {
//            console.log("inside Accesslogin");
//            $("#imguploadtabledetails").html(response);
//            $("#imguploadtabledetails").show();
//            }
//        });
        $.post("Accesslogin.do?type=getImageUploadDetails&showactive=N", serializedData, function (data) {
            $("#imguploadtabledetails").html(data);
            $("#imguploadtabledetails").show();
        });
        //validate display:none field
        $.validator.setDefaults({
            ignore: []
        });
        //
        $("#ImageUploadForm").validate({
            rules: {
                imagename: {
                    required: true
                },
                imagepath: {
                    required: true
                },
                image: {
                    required: true
                }
            },
            messages: {
                imagename: {
                    required: "This field is required"
                },
                imagepath: {
                    required: "This field is required"
                },
                image: {
                    required: "This field is required"
                }
            }

        });
    });

    function editConfigDetails(imagename, id) {
        $("#imagename").val(imagename);
        $("#imagepath").val("");
        $("#image").val("");
        $('#keyid').val(id);
        $('#Stype').val('edit');
        $("#imagename").attr("readonly", true);
    }
    function deleteImageUploadDetails(key, status) {
        var msg = "";
        if (status == 'A') {
            status = 'V';
            msg = 'hide'
        } else {
            status = 'A';
            msg = 'show';
        }
        var con = confirm('Do you want to ' + msg + '?');
        if (con == true) {
            $.post("AgencyAjax?type=viewImageUpload&configkey=" + key + "&Imgstatus=" + status, serializedData, function (data1) {
                $('#imguploadtabledetails').hide();
                $.post("Accesslogin.do?type=getImageUploadDetails&showactive=N", serializedData, function (data) {
                    $("#imguploadtabledetails").html(data);
                    $("#imguploadtabledetails").show();
                });
            });
            resetConfigValues();
        }
    }

    function resetConfigValues() {
        $("#imagename").val("");
        $("#imagepath").val("");
        $("#image").val("");
        $('#ImageUploadForm label.error').remove();
        $("#pngerror").hide();
        $('#Stype').val('Save');
        $("#imagename").attr("readonly", false);

    }

    function openfile() {
        $('#image').click();
    }
    function setImagePath() {
        var path = $("#image").val();
        var format = '.jpg';
        var format1 = '.jpeg';
        if ((path.substr(path.length - format.length, format.length).toLowerCase() == format.toLowerCase()) || (path.substr(path.length - format1.length, format1.length).toLowerCase() == format1.toLowerCase())) {
            $("#imagepath").val(path);
            $("#pngerror").hide();
        } else {
            $("#pngerror").text("Please upload jpg image");
            $("#pngerror").show();
            $("#imagepath").val("");
            $("#image").val("");
        }

    }
    function getRearrangedetails()
    {
        $("#topdialogspiner").addClass("fa fa-spinner fa-spin");
        $("#Imgrearrangeorder").hide();
        $.post("Accesslogin.do?type=getImageUploadDetails&showactive=Y", serializedData, function (data) {
            $("#Imgrearrangeorder").html(data);
            $("#topdialogspiner").removeClass("fa fa-spinner fa-spin");
            $("#Imgrearrangeorder").show();
        });
    }

    function RearrangeOrder() {
        $('#rearrangesavebtn').html("<i class='fa fa-spinner fa-spin'></i> Saving...");
        var tbl = $('#configdatatablerearrange')
        var tblhead = $(tbl).find('thead')
        var tblbody = $(tbl).find('tbody')
        var header = [];
        var JObjectArray = [];
        $.each($(tblhead).find('tr>th'), function (i, j) {
            header.push($(j).text().toLowerCase());
        })
        $.each($(tblbody).find('tr'), function (key, value)
        {
            var jObject = {};
            for (var x = 0; x < header.length; x++)
            {
                jObject[header[x]] = $(this).find('td').eq(x).text();
            }
            JObjectArray.push(jObject);

        });
        var jsonstr = JSON.stringify(JObjectArray);
        $.post("ImageUpload.do?Stype=Rearrange&jsonarraystr=" + jsonstr, serializedData, function (data) {
            $('#imguploadtabledetails').hide();
            $.post("Accesslogin.do?type=getImageUploadDetails&showactive=N", serializedData, function (data) {
                $("#imguploadtabledetails").html(data);
                $("#imguploadtabledetails").show();
            });
        });
        alert("Image Order Successfully Rearranged.");
        $('#rearrangesavebtn').html('<i class="fa fa-save"></i> Save');
        $("#Imgrearrangeorder").html('');
        $('#Rearrangedialog').modal('hide');
    }

    function closeBox()
    {
        if (rearrangeactive == true) {
            var con = confirm('Do you want to Save');
            if (con == true) {
                RearrangeOrder();
            }
            $("#Imgrearrangeorder").html('');
        }
        rearrangeactive = false;
    }
</script>