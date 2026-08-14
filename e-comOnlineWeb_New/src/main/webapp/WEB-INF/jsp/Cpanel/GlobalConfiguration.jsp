<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<%
    String menupage = (String) session.getAttribute("adminmenupage");
    menupage = menupage != null ? menupage : "";
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Global Configuration</div>
<div class="card card-body card-shadow page_bottom_content">
    <div class="container">
        <div class="details_search">          
            <html:form styleId="GlobalConfigForm"  method="post" action="/GlobalConfigAction" styleClass="form-horizontal" >
                <input type="hidden" name="Stype" id="Stype" value="Save"/>
                <div class="form-group row">
                    <label class="col-sm-1 control-label">Key<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:text property="key"  styleId="key"  styleClass="form-control light_border required "   maxlength="100"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-1 control-label">Value<span class="mandatory">*</span></label>
                    <div class="col-sm-3 demo2" id="colorpick" style="padding-left: 15px !important;padding-right: 15px !important;float:left !important">
                        <div class="input-group">
                            <html:text property="value"  styleId="value"  styleClass="form-control light_border required"  />
                            <div class="input-group-addon">
                                <span class="input-group-text" title="colorpicker" id="chooseclr" style="cursor: pointer;min-height: 40px;margin-top: -2px;display: none;"></span>
                            </div>
                        </div>
                    </div>
                    <%if (menupage.equalsIgnoreCase("cpanelmenupage")) {%>
                    <label class="col-sm-2 control-label" style="text-align:left !important"><input type="checkbox" id="checkbtn" aria-label="..." onClick="activecolor()"/>Need colorpicker</label>
                        <%                                                            }%>
                    <div class="col-sm-5">
                        <button type="submit" class="btn btn-primary btn-lg search_button" id="searchID"> Submit</button>
                        <button type="button" class="btn btn-danger btn-lg search_button" id="resetID" onclick="cancelMenuReg();"> Reset</button>
                    </div>
                </div>


                <div class="form-group row">
                    <label class="col-sm-5 control-label" id="validid" style="display:none;color:#a94442;">Mandatory field(s) required</label>
                </div>


            </html:form>

        </div>
    </div>
</div>
<div id="menudetails" style="display:none;"></div>


<script type="text/javascript">

    var menupageval = '<%=menupage%>';
    $(document).ready(function () {
        $('#key').val("");
        $('#value').val("");
        $('#Stype').val("");
        var serializedData = "";
        //document.getElementById('processLoad').style.display="block";
        $("#menudetails").hide();
        $.post("Accesslogin.do?type=getConfigValues&adminpage=" + menupageval, serializedData, function (data) {
            $("#menudetails").html(data);

            //  document.getElementById('processLoad').style.display="none";
            $("#menudetails").show();
        });
        $("#GlobalConfigForm").validate({
        });

    });

    function editConfigDetails(key, value) {
        var retVal = false;
        retVal = confirm("Do you want to edit?");
        if (retVal == true) {
            $('#key').val(key);
            $('#value').val(value);
            $('#Stype').val('edit');
            $('#value').focus();
            $('html,body').scrollTop(0);
        } else
        {
            return false;
        }
        document.getElementById('validid').style.display = 'none';
    }

</script>
<script>

    function activecolor()
    {
        if (document.getElementById('checkbtn').checked == false)
        {
            $('.demo2').find('input').colorpicker('destroy');
            // $('#chooseclr').html("");
            $('#chooseclr').hide();
        }

        if (document.getElementById('checkbtn').checked == true)
        {
            $('.demo2').find('input').colorpicker();
            $('.demo2').find('input').on('colorpickerChange', function (event) {
                $('#chooseclr').css('background-color', event.color.toString());
            });
            //     $('#chooseclr').html("Choose Colour");
            $('#chooseclr').show();
            $("#value").focus();
        }

    }
    function cancelMenuReg()
    {
        document.getElementById('key').value = '';
        document.getElementById('value').value = '';
        document.getElementById('validid').style.display = 'none';
        $('#GlobalConfigForm label.error').remove();
    }

    function checkvalue()
    {
        if (document.getElementById('key').value == "" || document.getElementById('value').value == "")
        {
            document.getElementById('validid').style.display = 'block';
            return false;
        }
    }

    function keymessage()
    {
        document.getElementById('validid').style.display = 'none';
    }

    function deleteConfigDetails(key) {
        var serializedData = "";
        var con = confirm('Do you want to delete?');
        if (con == true) {
            $.post("UserProfileAjax?type=deleteconfig&configkey=" + key, serializedData, function (data1) {
                $.post("Accesslogin.do?type=getConfigValues&adminpage=" + menupageval, serializedData, function (data) {
                    $("#menudetails").html(data);

                    $("#menudetails").show();
                    /* auto generated menu id based on max of id */
                });
            });
        }

    }

</script>
</html>