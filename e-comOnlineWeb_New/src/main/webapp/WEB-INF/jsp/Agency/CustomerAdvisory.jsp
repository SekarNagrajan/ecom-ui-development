<%-- 
    Document   : CustomerAdvisory
    Created on : Jun 14, 2018, 11:35:07 AM
    Author     : gobinath.t
--%>

<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.List,java.util.Hashtable" errorPage="" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Customer Advisory</div>
<div class="card card-body card-shadow page_bottom_content">
    <div class="container">
        <div class="details_search">
            <html:form styleId="CustomerAdvisoryForm" method="post" action="/CustomerAdvisory" styleClass="form-horizontal">
                <input type="hidden" name="Stype" id="Stype" value="Save"/>
                <html:hidden property="keyid" styleId="keyid" />

                <div class="form-group row">
                    <label class="col-sm-1 control-label">Advisory Name<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:text property="advisoryname" styleId="advisoryname" styleClass="form-control light_border required" maxlength="18"></html:text>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-1 control-label">Description<span class="mandatory">*</span></label>
                        <div class="col-sm-4">
                        <html:textarea property="description" styleId="description" styleClass="form-control  light_border required" style="height: 150px;overflow:auto;" ></html:textarea>
                        </div>
                    </div>

                    <div class="form-group row">
                        <div class="col-sm-5 col-sm-offset-2">
                            <button  type="submit" class="btn btn-primary btn-lg search_button"  id="submitID"> Submit</button>
                            <button type="button" class="btn btn-danger btn-lg search_button" id="resetID" onclick="resetConfigValues();"> Reset</button>
                        </div>
                    </div>
            </html:form>

        </div>
    </div>
</div>
<div id="cus_advis_tabledetails" style="display: none"></div>

<script type="text/javascript">
    var serializedData = "";
    $(document).ready(function () {
        $("#description").attr("maxlength", "190");
        $("#advisoryname").val("");
        $("#description").val("");
        //document.getElementById('processLoad').style.display="block";
        $('#cus_advis_tabledetails').hide();
        $.post("Accesslogin.do?type=getCustAdvisDetails", serializedData, function (data) {
            $("#cus_advis_tabledetails").html(data);
            //document.getElementById('processLoad').style.display="none";
            $("#cus_advis_tabledetails").show();
        });

        $("#CustomerAdvisoryForm").validate({
            rules: {
                advisoryname: {
                    required: true
                },
                description: {
                    required: true
                }
            },
            messages: {
                advisoryname: {
                    required: "This field is required"
                },
                description: {
                    required: "This field is required"
                }
            }

        });
    });

    function editConfigDetails(advisoryname, description, id) {
        description = description.replace(/<br>/g, "\n");
        $("#advisoryname").val(advisoryname);
        $("#description").val(unescape(description));
        $('#keyid').val(id);
        $('#Stype').val('edit');
    }
    function deleteCustAdvisoryDet(key) {
        var con = confirm('Do you want to delete?');
        if (con == true) {
            $.post("AgencyAjax?type=deleteCustAdvis&advisoryKey=" + key, serializedData, function (data1) {
                //document.getElementById('processLoad').style.display="block";
                $('#cus_advis_tabledetails').hide();
                $.post("Accesslogin.do?type=getCustAdvisDetails", serializedData, function (data) {
                    $("#cus_advis_tabledetails").html(data);
                    // document.getElementById('processLoad').style.display="none";
                    $("#cus_advis_tabledetails").show();
                });
            });
            resetConfigValues();
        }
    }

    function resetConfigValues() {
        $("#advisoryname").val("");
        $("#description").val("");
        $('#CustomerAdvisoryForm label.error').remove();
        $('#Stype').val('Save');
    }
</script>