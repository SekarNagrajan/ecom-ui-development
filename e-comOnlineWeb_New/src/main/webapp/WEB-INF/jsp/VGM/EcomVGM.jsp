<%--
    Document   : EcomVGM
    Created on : Jul 7, 2016, 11:25:58 AM
    Author     : deepa.p
--%>
<%@page import="net.ecom.common.Validation"%>
<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8" import="net.ecom.common.GeneralConf" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!-- Fav and touch icons -->

<!--<style type="text/css">


    .table td{
        position:relative;
    }
    .input-sm+.form-control-feedback {
        width: 30px;
        height: 30px;
        line-height: 40px;
    }
</style>-->
<%
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String clientcode = (String) session.getAttribute("clientcode");
    String vgmFileUpload = (String) getServletContext().getAttribute("VGMFILEUPLOADSTATUS");
    String vgmDisclaimerMsg = (String) getServletContext().getAttribute("VGMDISCLAIMERMSG");
    String vgmDisclaimerMsglang = (String) getServletContext().getAttribute("VGMDISCLAIMERMSGLANG");
    String vgmDisclaimerMsgfinal = null, vgmDisclaimerMsglangfinal = null;
    vgmDisclaimerMsgfinal = vgmDisclaimerMsg.length() > 0 ? vgmDisclaimerMsg : "";
    vgmDisclaimerMsglangfinal = vgmDisclaimerMsglang.length() > 0 ? vgmDisclaimerMsglang : "";
    String useremail = Validation.checkNull((String) session.getAttribute("SESSION_EMAIL"));
    String user = (String) session.getAttribute("sessionAdmin");
    String HideFaxNo = (String) getServletContext().getAttribute(GeneralConf.HideFaxNo);
    String SESSION_LOGINNAME = Validation.checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));

%>

<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="ecom.VGM.head" bundle="<%=clientcode%>"/>  </div>
<div class="container-fluid page_bottom_content">

    <div class="card card-body card-shadow">
        <form  id="vgmForm">
            <%                session.setAttribute("VGESUBMITSESSION", "send");
                String invalid = (String) session.getAttribute("VGMFAILURE");
                session.setAttribute("VGMFAILURE", "");
                if (invalid != null && invalid.length() > 0) {

            %>
            <div class="col-md-12 column" style="padding-bottom: 20px">
                <label class="error"><%=invalid%></label>
            </div>
            <%}%>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4">

                        <label> <bean:message key="vgm.submissionby" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span> </label>
                        <select id="submitby" name="submitBy" class="form-control inside_input required" onchange="hideError();">
                            <option value="bookno"><bean:message key="ebooking.bookno.display" bundle="<%=clientcode%>"/></option>
                            <option value="blno"><bean:message key="bl.blnum" bundle="<%=clientcode%>"/></option>
                        </select>

                    </div>
                    <div class="col-xs-12 col-sm-4">
                        <label></label>
                        <input type="text" name="referenceNo"  id="referenceNo" class="form-control inside_input required alphanumeric booknumbercheck"  maxlength="20" onblur="deleteemptydiv(this.value)"/>
                        <label class="error" id="loadconfirmerror" style="display:none"></label>
                    </div>

                    <div class="col-xs-12 col-sm-4 ">
                        <label></label>
                        <button   type="submit" autocomplete="off" id="searchbtn" class="btn btn-info btn_block_custom"><bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                    </div>

                </div>
            </div>
        </form>
    </div>
    <div class="card card-body card-shadow">
        <html:form  method="post" action="SubmitVGM.do" styleId="vgmViewForm"  styleClass="form-horizontal" enctype="multipart/form-data">
            <html:hidden property="vgmSubmission" styleId="vgmsubmission"/>
            <html:hidden property="bookNo" styleId="bookNo"/>
            <html:hidden property="blNo" styleId="blNo"/>
            <html:hidden property="vgmStatus" styleId="vgmStatus"/>
            <input type="hidden" id="SESSION_LOGINNAME" value="<%=SESSION_LOGINNAME%>"/>
            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-12">

                        <div class="header_tle"> <b><bean:message key="vgm.bookingblheading" bundle="<%=clientcode%>"/> </b> </div>


                        <div class="row form-group">

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.apname" bundle="<%=clientcode%>"/>  </label>

                            <div class="col-sm-4 "> 

                                <html:text property="vgmAPName"  styleId="vgmapname" styleClass="form-control light_border required  " readonly="true"/>

                            </div>

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.shippername" bundle="<%=clientcode%>"/> </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmSHName"  styleId="vgmshname" styleClass="form-control light_border required"  readonly="true"/>

                            </div>

                        </div>

                        <div class="row form-group">

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.origin" bundle="<%=clientcode%>"/>  </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmOrigin"  styleId="vgmorigin" styleClass="form-control light_border required  " readonly="true"/>

                            </div>

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.destination" bundle="<%=clientcode%>"/> </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmDelivery"  styleId="vgmdelivery" styleClass="form-control light_border required  " readonly="true"/>

                            </div>

                        </div>


                        <div class="row form-group">

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.vgmpol" bundle="<%=clientcode%>"/>  </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmBLPOL"  styleId="vgmblpol" styleClass="form-control light_border required  "  readonly="true"/>

                            </div>

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.vgmpod" bundle="<%=clientcode%>"/> </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmBLPOD"  styleId="vgmblpod" styleClass="form-control light_border required  "  readonly="true"/>

                            </div>

                        </div>

                    </div>

                </div>
            </div>


            <div class="form-group">
                <div class="row">

                    <div class="col-xs-12 col-sm-12">

                        <div class="header_tle"> <b><bean:message key="vgm.vgmdetailsheading" bundle="<%=clientcode%>"/> </b> </div>

                        <div class="row form-group">

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.reference" bundle="<%=clientcode%>"/>  </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmReference"  styleId="vgmref" styleClass="form-control light_border vgmrefcheck"  maxlength="50"/>

                            </div>

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.companyname" bundle="<%=clientcode%>"/> </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmCompanyNmae"  styleId="vgmcmpnyname" styleClass="form-control light_border"  maxlength="200"/>

                            </div>

                        </div>

                        <div class="row form-group">

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.orderno" bundle="<%=clientcode%>"/>  </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmOrderNo"  styleId="vgmorderno" styleClass="form-control light_border vgmrefcheck"  maxlength="50"/>

                            </div>

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.addr1" bundle="<%=clientcode%>"/> </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmAddr1"  styleId="vgmaddr1" styleClass="form-control light_border"  maxlength="200"/>

                            </div>

                        </div>

                        <div class="row form-group">

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.obtaineddate" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmObtainDate"  styleId="vgmobtaindate" styleClass="form-control light_border required datecheck" />
                                <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>

                            </div>

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.addr2" bundle="<%=clientcode%>"/> </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmAddr2"  styleId="vgmaddr2" styleClass="form-control light_border"  maxlength="200"/>

                            </div>

                        </div>

                        <div class="row form-group">

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.obtainedmethod" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span>  </label>

                            <div class="col-sm-4 ">

                                <html:select property="vgmObtainMethod"  styleId="vgmobtainmeth" styleClass="form-control light_border required" >
                                    <html:option value="SM1">SM1</html:option>
                                    <html:option value="SM2">SM2</html:option>
                                </html:select>

                            </div>

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.authorizedperson" bundle="<%=clientcode%>"/> <span class="mandatory"> * </span> </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmAuthPerson"  styleId="vgmauthperson" styleClass="form-control light_border required alphanumericwithspace"  maxlength="100"/>

                            </div>

                        </div>

                        <div class="row form-group">

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.country" bundle="<%=clientcode%>"/>  </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmCountry"  styleId="vgmcountry" styleClass="form-control light_border"  maxlength="100"/>

                            </div>

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.city" bundle="<%=clientcode%>"/> </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmCity"  styleId="vgmcity" styleClass="form-control light_border"  maxlength="150"/>

                            </div>

                        </div>


                        <div class="row form-group">

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.telephone" bundle="<%=clientcode%>"/>  </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmPhone"  styleId="vgmphone" styleClass="form-control light_border telephonefaxcheck"  maxlength="50"/>

                            </div>
                            <label class="col-sm-2 control-label"> <bean:message key="vgm.email" bundle="<%=clientcode%>"/> </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmEmail"  styleId="vgmemail" styleClass="form-control light_border email"  maxlength="100"/> 

                            </div>

                        </div>

                        <div class="row ">

                            <label class="col-sm-2 control-label"> <bean:message key="vgm.zipcode" bundle="<%=clientcode%>"/>  </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmZipcode"  styleId="vgmzipcode" styleClass="form-control light_border zipcodecheck number "  maxlength="15"/>

                            </div>


                            <%if (!"Y".equalsIgnoreCase(HideFaxNo)) {%>
                            <label class="col-sm-2 control-label"> <bean:message key="vgm.fax" bundle="<%=clientcode%>"/> </label>

                            <div class="col-sm-4 ">

                                <html:text property="vgmFax"  styleId="vgmfax" styleClass="form-control light_border telephonefaxcheck"  maxlength="25"/>

                            </div>
                            <%}%>

                        </div>


                    </div>

                </div>

            </div>
            <div class="col-xs-12 col-sm-12">
                <div class="form-group">
                    <div id="vgmviewdiv"></div>
                </div>
                <div class="form-group">
                    <label class="col-sm-12 control-label " style="text-align:left;color: #ee3942;" id="containercheckerror"></label>
                    <label class="col-sm-12 control-label " style="text-align:left;color: #ee3942;" id="containercheckerrorload"></label>
                </div>
            </div>

            <!--File Upload  -->
            <div id="vgmfilediv" style="display: none">

                <div class="row form-group">
                    <%if ("Y".equalsIgnoreCase(vgmFileUpload)) {%>
                    <div class="col-sm-4 txt_upload">
                        <label></label>
                        <div class="input-group">

                            <div class="input-group-prepend">
                                <span class="input-group-text"><bean:message key="ecom.VGM.file" bundle="<%=clientcode%>"/>  </span>
                            </div>
                            <div class="custom-file">
                                <html:file property="vgmFile" styleId="vgmFile" value="" title="" styleClass="custom-file-input" onchange="ValidateSingleInput(this);"/>
                                <label class="custom-file-label" for="vgmFile">  <bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                            </div>
                        </div>

                    </div>

                    <div class="col-sm-4 m-t-30">
                        <div class="skin skin-flat">
                            <input type="checkbox"  id="agree" name="agree" class="required">
                            <label for="agree">  <bean:message key="vgm.msg.vgmvaliddetails" bundle="<%=clientcode%>"/><span class="mandatory"> * </span> </label>
                        </div>
                    </div>
                    <%}%>
                    <div class="col-sm-4 ">
                        <label class="control-label"><bean:message key="ecom.VGM.ackmailid" bundle="<%=clientcode%>"/><span class="mandatory"> * </span></label>

                        <input type="text" id="sendemailid" name="sendemailid"  value="<%=useremail%>" class="form-control light_border autooffclass required"  maxlength="100"/>
                        <span style="font-size: 12px;"><bean:message key="ebooking.msg.commasepmutiplemail" bundle="<%=clientcode%>"/> </span>
                        <label id="emailvalidation"  class="req" style="display:none"><bean:message key="vgm.msg.invalidmail" bundle="<%=clientcode%>"/></label>
                    </div>

                </div>

                <div class="row form-group">

                    <div class="col-sm-9 note-info">

                        <div class="sm_method"> <bean:message key="vgm.msg.contmet1" bundle="<%=clientcode%>"/>  </div>
                        <div class="sm_method"> <bean:message key="vgm.msg.contmet2" bundle="<%=clientcode%>"/> </div>
                        <%if ("zh".equalsIgnoreCase(clientcode)) {%>
                        <div class="form-group">

                            <label class="col-sm-12 vgmdisclaimertext"><%=vgmDisclaimerMsglangfinal%></label>

                        </div>
                        <%} else {%>
                        <div class="form-group">

                            <label class="col-sm-12 vgmdisclaimertext"><%=vgmDisclaimerMsgfinal%></label>

                        </div>


                        <%}%>   
                    </div>

                    <div class="col-sm-3 float-right m-t-30">

                        <button type="submit" class="btn btn-primary btn-lg w-100 float-right" name="submit"autocomplete="off" id="submitbtn" value="Submit" <%="Y".equalsIgnoreCase(user) ? "disabled" : ""%>>  <bean:message key="ecom.submit" bundle="<%=clientcode%>"/></button>

                    </div>
                </div>
            </div>
            <input type="hidden" id="typeheadhidden2" />
            <input type="hidden" id="appversion" name="appversion"/>
        </html:form>
    </div>
</div>
<div class="d-none">
    <form id="vgmexportexcel" method="POST">
        <input type="hidden" id="vgmvalue" name="vgmvalue" />
        <input type="hidden" id="vgmRefNo" name="vgmRefNo" />
        <input type="hidden" id="vgmcount" name="vgmcount" />
    </form>
</div>

<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title">   <bean:message key="vgm.msg.cpyto" bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
                <div class="form-group row">
                    <label class="col-sm-12"><bean:message key="vgm.availablecontainers" bundle="<%=clientcode%>"/> </label>
                    <div class="offset-md-0 col-sm-11">
                        <select multiple="true" id="availablecontainerList" class="availablecontainerList" name="availablecontainerList" size="6" style="height:150px; width:200px;">
                        </select>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <div class="pull-right"><a href="javascript:void(0)" title="OK"  class="btn btn-info btn-xs" onclick="copyValues()" id="mailbtn">OK</a></div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myImportModal" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="row">
                <div class="col-md-12 column">
                    <div class="modal-header bg-info text-white">  <h5 class="modal-title">  <bean:message key='ebooking.cargo.impexcel' bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                            <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div class="form-group row">
                            <div class="col-sm-offset-0 col-sm-11">

                                <div class="custom-file">
                                    <input type="file" name="importexcel" id="importexcel" class='custom-file-input'>
                                    <label class="custom-file-label" for="">  <bean:message key="ecom.bkg.choosefile" bundle="<%=clientcode%>"/></label>
                                </div>
                            </div>
                            <div class="col-sm-offset-0 col-sm-11">
                                <span class="pull-left"> <label class="error" style="color:red"><bean:message key='ecom.bkg.xlfileformat' bundle="<%=clientcode%>"/></label> </span>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <div class="text-right">
                            <a href="javascript:void(0)" title="Import Excel"  class="btn btn-info btn-xs" onclick="ImportData()" id="importsearch"><i class="fa fa-download"></i> <bean:message key='ebooking.cargo.impexcel' bundle="<%=clientcode%>"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModalImportErrors" role="dialog">
    <div class="modal-dialog modal-lg" style="width:100%">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">  <h5 class="modal-title"> <bean:message key='ebooking.cargo.impexcel' bundle="<%=clientcode%>"/></h5>  <div class="pull-right">
                    <button type="button" class="close dialouge_close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
            </div>
            <div class="modal-body">
                <div class="col-sm-12" id="importerr" style="height: 450px;overflow: auto;"></div>
            </div>
        </div>
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script type="text/javascript" src="/Ecommaster/country.js?<%=jsno%>"></script>
<script type="text/javascript">
                                var counlist = countryJson;
                                $(window).one("load", function () {
                                    var ports = new Bloodhound({
                                        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('countryname', 'countrycode'),
                                        queryTokenizer: Bloodhound.tokenizers.whitespace,
                                        local: counlist
                                    });
                                    ports.initialize();
                                    $('#vgmcountry').typeahead(
                                            {
                                                hint: true,
                                                highlight: true,
                                                minLength: 2
                                            },
                                            {
                                                name: 'country',
                                                limit: 5,
                                                displayKey: function (counlist) {
                                                    return counlist.countrycode + " - " + counlist.countryname;
                                                },
                                                source: ports.ttAdapter()
                                            }).on('typeahead:render', function (obj, datum) {
                                        $("#typeheadhidden2").val("");
                                    }).on('typeahead:selected', function (obj, datum) {
                                        $("#typeheadhidden2").val(datum.countryname);
                                    }).on('typeahead:autocomplete', function (obj, datum) {
                                        $("#typeheadhidden2").val(datum.countryname);
                                    }).on("typeahead:change", function (obj, datum) {
                                        var hiddenValue = $("#typeheadhidden2").val();
                                        if (hiddenValue == null || hiddenValue == "") {
                                            $('#vgmcountry').typeahead('val', "");
                                            $(obj.currentTarget).val("");
                                        }
                                    });
                                });
                                function dateCheck() {
                                    return false;
                                }
                                function hideError() {
                                    $('#loadconfirmerror').hide();
                                    $('#containercheckerror').html("");
                                }
                                function deleteemptydiv(value) {
                                    if (value == "" || value.length == 0 || value == null) {
                                        $('#loadconfirmerror').hide();
                                        $('#containercheckerror').html("");
                                        $('#containercheckerrorload').html("");
                                        //$("#searchbtn").button('loading');
                                        $('#emailvalidation').hide();
                                        $("#vgmviewdiv").html("");
                                    }
                                }

                                function  changeLabelValue(value) {
                                    if (value == 'C') {
                                        $("#typeLabel").html("Contain No<font class='req'>*</font>");
                                    } else if (value == 'B') {
                                        $("#typeLabel").html("Book No<font class='req'>*</font>");
                                    } else if (value == 'L') {
                                        $("#typeLabel").html("BL No<font class='req'>*</font>");
                                    }
                                }
                                function deleteRow(id) {
                                    var splitval;
                                    var arrindex;
                                    var confirmfun = true;
                                    if ($('#datatablebyeslvgm >tbody >tr').length == 1) {
                                        alert("<bean:message key="ecom.msg.vgm.atleastonecontmust" bundle="<%=clientcode%>"/>");
                                    } else {
                                        splitval = id.split("tablerowid");
                                        arrindex = splitval[1];
                                        if ($('#vgmStatus').val() == 'D') {
                                            if ($('#containerno' + arrindex).val() != "" || $('#vgmtd1' + arrindex).val() != "") {
                                                confirmfun = confirm("<bean:message key="ecom.msg.vgm.delrow" bundle="<%=clientcode%>"/>");
                                            }
                                        } else {
                                            if ($('#vgmtd1' + arrindex).val() != "") {
                                                confirmfun = confirm("<bean:message key="ecom.msg.vgm.delrow" bundle="<%=clientcode%>"/>");
                                            }
                                        }
                                        if (confirmfun == true) {
                                            $('#' + id).remove();
                                        }
                                    }
                                }
                                var selectedid;
                                function editTable(currentBookno) {
                                    var cur = $('#' + currentBookno).val();
                                    currentBookno = cur;
                                    var check = "true";
                                    var j = 0;
                                    $('[name=containernohid]').each(function (index) {
                                        if (this.value == '') {
                                            check = "false";
                                        }
                                        j++;
                                    });
                                    var bookid;
                                    var arr;
                                    var option;
                                    option = "";
                                    var i = 0;
                                    if (j > 1) {
                                        if (check == 'true') {
                                            $('[name=containernohid]').each(function (index) {
                                                bookid = this.id;
                                                arr = bookid.split("containernohid");
                                                if (currentBookno == this.value) {
                                                    selectedid = arr[1];
                                                }
                                                if (currentBookno != this.value) {
                                                    option += "<option value = '" + arr[1] + "'>" + this.value + "</option>";
                                                }
                                                $('#availablecontainerList').html(option);
                                                //document.getElementById('availablecontainerList').options[i] = new Option(this.value,arr[1]);
                                                i++;
                                            });
                                            if ($('#vgmtd1' + selectedid).val() != "") {
                                                $('#myModal').modal('show');
                                            } else {
                                                alert("<bean:message key="ecom.msg.vgm.contdet" bundle="<%=clientcode%>"/> " + $('#containerno' + selectedid).val());
                                            }

                                        } else {
                                            alert("<bean:message key="ecom.msg.vgm.entallcontdet" bundle="<%=clientcode%>"/>");
                                        }
                                    } else {
                                        alert("<bean:message key="ecom.msg.vgm.onlyonecont" bundle="<%=clientcode%>"/>");
                                    }
                                }
                                function copyValues() {
                                    $('#availablecontainerList :selected').each(function (i, selected) {
                                        $("#weighthid" + $(selected).val()).val($("#weighthid" + selectedid).val());
                                        $("#vgmtd1" + $(selected).val()).val($("#weighthid" + selectedid).val());
                                        $("#vgmtd" + $(selected).val()).html($("#weighthid" + selectedid).val());
                                        $("#unithid" + $(selected).val()).val($("#unithid" + selectedid).val());
                                        $("#unitselect" + $(selected).val()).val($("#unithid" + selectedid).val());
                                        $("#unittd" + $(selected).val()).html($("#unittd" + selectedid).html());
                                    });
                                    $('#myModal').modal('hide');
                                }
                                function editVGMWeight(id, id1) {
                                    $('#' + id).css({"display": "none"});
                                    $('#' + id1).css({"display": "block"});
                                    $('#' + id1).focus();
                                }
                                function setVGMWeight(value, id, id1, weightid) {
                                    $('#' + id).html(value);
                                    $('#' + weightid).val(value);
                                    //$('#'+id).css({"display":"block"});
                                    //$('#'+id1).css({"display":"none"});
                                }
                                function editVGMUnit(id, id1) {
                                    $('#' + id).css({"display": "none"});
                                    $('#' + id1).css({"display": "block"});
                                    $('#' + id1).focus();
                                }
                                function setVGMUnit(value, id, id1, weightid) {
                                    if (value == 'T') {
                                        $('#' + id).html("Tons");
                                    } else {
                                        $('#' + id).html("Kgs");
                                    }
                                    $('#' + weightid).val(value);
                                    //$('#'+id).css({"display":"block"});
                                    //$('#'+id1).css({"display":"none"});
                                }
                                function setValues(value, id, errorid, bookNo) {
                                    $('#' + id).val(value);
                                    /** $.ajax({
                                     type: "POST",
                                     cache:false,
                                     async:false,
                                     url: "VGMServlet?type=validatecontainer&bookno="+encodeURIComponent(bookNo)+"&containerno="+encodeURIComponent(value),
                                     success: function (response) {
                                     if(response!='SessionExpired'){
                                     if(response!='valid'){
                                     $("#"+errorid).show();
                                     }
                                     }else{
                                     window.location.href = "SessionExpired.jsp";
                                     }
                                     },
                                     error: function (xhr) {
                                     if(xhr.status==403){
                                     window.location.href = "SessionExpired.jsp";
                                     }
                                     }                                  });**/
                                }

                                var booknoslist = "";
                                var ret = false;
                                $(document).ready(function () {

                                    $('#vgmobtaindate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: "<%=strDateFormat%>", endDate: 'today'});
                                    $(".datecheck").keypress(function (event) {
                                        event.preventDefault();
                                    });
                                    $('.alphanumericwithoutspace').keyup(function () {
                                        if (this.value.match(/[^a-zA-Z0-9 ]/g)) {
                                            this.value = this.value.replace(/[^a-zA-Z0-9 ]/g, '');
                                        }
                                    });
                                    $('.vgmrefcheck').keyup(function () {
                                        if (this.value.match(/[^a-zA-Z0-9-_() ]/g)) {
                                            this.value = this.value.replace(/[^a-zA-Z0-9-_() ]/g, '');
                                        }
                                    });
                                    $('.telephonefaxcheck').keyup(function () {
                                        if (this.value.match(/[^0-9-]/g)) {
                                            this.value = this.value.replace(/[^0-9-]/g, '');
                                        }
                                    });
                                    $('.zipcodecheck').keyup(function () {
                                        if (this.value.match(/[^0-9]/g)) {
                                            this.value = this.value.replace(/[^0-9]/g, '');
                                        }
                                    });
                                    $('.booknumbercheck').keyup(function () {
                                        if (this.value.match(/[^a-zA-Z0-9]/g)) {
                                            this.value = this.value.replace(/[^a-zA-Z0-9]/g, '');
                                        }
                                    });
                                    $('#vgmForm').attr('autocomplete', 'off');
                                    $('.autooffclass').attr('autocomplete', 'off');
                                    $("#vgmViewForm").validate({
                                        rules: {
                                            vgmPhone: {
                                                validnumbers: "#vgmphone",
                                                ZeroTest: "#vgmphone",
                                                phonefax: ["#vgmphone"]
                                            },
                                            vgmFax: {
                                                validnumbers: "#vgmfax",
                                                ZeroTest: "#vgmfax",
                                                phonefax: ["#vgmfax"]
                                            }
                                        },
                                        messages: {
                                            vgmPhone: {validnumbers: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                                                ZeroTest: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>",
                                                phonefax: "<bean:message key="error.reg.phone_Number" bundle="<%=clientcode%>"/>"
                                            },
                                            vgmFax: {
                                                validnumbers: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                                                ZeroTest: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>",
                                                phonefax: "<bean:message key="ecom.msg.invalidfax" bundle="<%=clientcode%>"/>"
                                            }
                                        },
                                        submitHandler: function (form) {
                                            var contalis = "";
                                            var inti = 0;
                                            var contarr;
                                            var wtContainerList = '';
                                            var wtContCount = 0;
                                            ret = true;
                                            $('#containercheckerror').html("");
                                            $('#containercheckerrorload').html("");
                                            $('#emailvalidation').hide();
                                            //$("#submitbtn").button('loading');
                                            $('.weightnocheck').each(function (index) {
                                                var id = $(this).attr('id');
                                                id = id.replace('vgmtd1', '');
                                                var unitid = "unitselect" + id;
                                                var weightval = $(this).val();
                                                var unitval = $('#' + unitid).val();
                                                if (unitval == 'T') {
                                                    if (parseFloat(weightval) > 50) {
                                                        ret = false;
                                                    }
                                                } else {
                                                    if (parseFloat(weightval) > 50000) {
                                                        ret = false;
                                                    }
                                                }
                                                if (!ret) {
                                                    if (wtContCount == 0) {
                                                        wtContainerList = $('#containerno' + id).val();
                                                        wtContCount++;
                                                    } else {
                                                        wtContainerList = wtContainerList + "," + $('#containerno' + id).val();
                                                    }

                                                }
                                            });
                                            if (ret) {
                                                if (validEmailCheckToCommon('sendemailid')) {
                                                    ret = true;
                                                } else {
                                                    $("#submitbtn").button('reset');
                                                    $('#emailvalidation').show();
                                                    ret = false;
                                                }
                                                if (ret) {
                                                    var eqpList = "", socList = "";
                                                    var eqpInc = 0;
                                                    var socInc = 0;
                                                    $('[name=booknohid]').each(function (index) {
                                                        booknoslist = this.value;
                                                    });
                                                    $('[name=eqptypehid]').each(function (index) {
                                                        if (eqpInc == 0) {
                                                            eqpList = this.value;
                                                        } else {
                                                            eqpList = eqpList + "~~" + this.value;
                                                        }
                                                        eqpInc++;
                                                    });
                                                    $('[name=hidsoc]').each(function (index) {
                                                        if (socInc == 0) {
                                                            socList = this.value;
                                                        } else {
                                                            socList = socList + "~~" + this.value;
                                                        }
                                                        socInc++;
                                                    });
                                                    $('[name=containernohid]').each(function (index) {
                                                        booknoslist = booknoslist + "~~" + this.value;
                                                        if (inti == 0) {
                                                            contalis = this.value;
                                                        } else {
                                                            contalis = contalis + "~~" + this.value;
                                                        }
                                                        inti++;
                                                    });
                                                    contarr = contalis.split("~~");
                                                    /*for (var i = 0; i < contarr.length; i++) {
                                                     if (i != 0 && (contarr[i] == contarr[i - 1])) {
                                                     //alert("ii");
                                                     ret = false;
                                                     }
                                                     }*/
                                                    //new duplicate checking logic
                                                    const letters = new Set(contarr);
                                                    console.log("contarr.length=>" + contarr.length);
                                                    console.log("letters.size=>" + letters.size);
                                                    //alert("Enter");
                                                    if (contarr.length != letters.size) {
                                                        ret = false;
                                                    }
                                                    var containerslistval;
                                                    var msg;
                                                    if (ret) {
                                                        if (ret) {
                                                            //ret = false;
                                                            $.ajax({
                                                                type: "POST",
                                                                async: false,
                                                                url: "VGMServlet?type=containernocheck&reference=" + booknoslist + "&submissionBy=" + encodeURIComponent($('#submitby').val()) + "&eqpList=" + encodeURIComponent(eqpList) + "&socList=" + socList + "&appversion=" + $('#appversion').val(),
                                                                success: function (response) {
                                                                    $("#submitbtn").button('reset');
                                                                    $('#containercheckerror').html("");
                                                                    $('#containercheckerrorload').html("");
                                                                    //alert(response);
                                                                    if (response != 'SessionExpired') {
                                                                        if (response != 'Valid') {
                                                                            msg = "";
                                                                            containerslistval = response.split("-");
                                                                            if (containerslistval[0] == 'already submitted') {
                                                                                $('#containercheckerror').html(containerslistval[1]);
                                                                            } else if (containerslistval[0] == 'Invalid Container Nos') {
                                                                                $('#containercheckerror').html("Invalid Container Nos : " + containerslistval[1]);
                                                                            } else {
                                                                                if (containerslistval[0].length > 0) {
                                                                                    if ($('#submitby').val() == 'blno') {
                                                                                        $('#containercheckerror').html("<bean:message key="ecom.msg.vgm.contirrelevanttobl" bundle="<%=clientcode%>"/> " + containerslistval[0]);
                                                                                    } else {
                                                                                        $('#containercheckerror').html("<bean:message key="ecom.msg.vgm.contirrelevanttobookno" bundle="<%=clientcode%>"/>" + containerslistval[0]);
                                                                                    }
                                                                                }
                                                                                if (containerslistval.length == 2 && containerslistval[1].length > 0) {
                                                                                    $('#containercheckerrorload').html("<bean:message key="ecom.msg.vgm.cnfrmcont" bundle="<%=clientcode%>"/> " + containerslistval[1]);
                                                                                }
                                                                            }
                                                                            //$('#containercheckerror').html(msg);
                                                                            ret = false;
                                                                        } else {
                                                                            // alert(response);
                                                                            ret = true;
                                                                        }
                                                                    } else {
                                                                        window.location.href = "SessionExpired.jsp";
                                                                    }
                                                                },
                                                                error: function (xhr) {
                                                                    if (xhr.status == 403) {
                                                                        window.location.href = "SessionExpired.jsp";
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        ret = false;
                                                        $("#submitbtn").button('reset');
                                                        $('#containercheckerror').html("<bean:message key="ecom.msg.vgm.remdupcnt" bundle="<%=clientcode%>"/>");
                                                    }
                                                }
                                            } else {
                                                $("#submitbtn").button('reset');
                                                alert('<bean:message key="ecom.msg.vgm.contweight" bundle="<%=clientcode%>"/> - ' + wtContainerList);
                                            }
                                            if (ret) {
                                                openLoader();
                                            }
                                            return ret;
                                        }
                                    });
                                    $("#vgmForm").validate({
                                        rules: {
                                            vgmSubmission: {
                                                required: true
                                            }
                                        },
                                        messages: {
                                            vgmSubmission: {
                                                required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                                            }
                                        },
                                        submitHandler: function (form) {
                                            $("#searchbtn").button('loading');
                                            $('.containererrorclass').hide();
                                            $('#loadconfirmerror').hide();
                                            $('#containercheckerror').html("");
                                            $('#containercheckerrorload').html("");
                                            $('#emailvalidation').hide();
                                            $('#bookNo').val("");
                                            $('#blNo').val("");
                                            $('#vgmStatus').val("");
                                            $('#vgmapname').val("");
                                            $('#submittype').val("");
                                            $('#vgmshname').val("");
                                            $('#vgmorigin').val("");
                                            $('#vgmdelivery').val("");
                                            $('#vgmblpol').val("");
                                            $('#vgmblpod').val("");
                                            $('#vgmfilediv').hide();
                                            var vgmSubmission = $('#vgmSubmission').val();
                                            var referenceno = $('#referenceNo').val();
                                            var result;
                                            $.ajax({
                                                type: "POST",
                                                cache: false,
                                                async: false,
                                                url: "VGMServlet?type=getversionno&referenceno=" + encodeURIComponent(referenceno) + "&submissionBy=" + encodeURIComponent($('#submitby').val()),
                                                success: function (response) {
                                                    $('#appversion').val(response);
                                                },
                                                error: function (xhr) {
                                                    if (xhr.status == 403) {
                                                        window.location.href = "SessionExpired.jsp";
                                                    }
                                                }
                                            });
                                            $.ajax({
                                                type: "POST",
                                                cache: false,
                                                async: false,
                                                url: "VGMServlet?type=loadconfirm&bookno=" + encodeURIComponent(referenceno) + "&submissionBy=" + encodeURIComponent($('#submitby').val()) + "&blno=" + encodeURIComponent(referenceno) + "&appversion=" + $('#appversion').val(),
                                                success: function (response) {
                                                    if (response != 'SessionExpired') {
                                                        result = response;
                                                        if (result == 'success') {
                                                            $.ajax({
                                                                type: "POST",
                                                                cache: false,
                                                                async: false,
                                                                url: "VGMServlet?type=search&booknos=" + encodeURIComponent(referenceno) + "&submissionBy=" + encodeURIComponent($('#submitby').val()) + "&appversion=" + $('#appversion').val(),
                                                                success: function (response) {
                                                                    if (response != 'SessionExpired') {
                                                                        if (response != 'Invalid Booking Number.') {
                                                                            $("#vgmviewdiv").html(response);
                                                                            $('#vgmviewdiv').show();
                                                                            $('#vgmfilediv').show();
                                                                            $("#searchbtn").button('reset');
                                                                            setHiddenValues();
                                                                            $('.checkvgmcontclass').each(function () {
                                                                                $(this).rules('add', {
                                                                                    checkvgmcontclass: true,
                                                                                    messages: {
                                                                                        checkvgmcontclass: "<bean:message key="ecom.msg.invalidcontnum" bundle="<%=clientcode%>"/>"
                                                                                    }
                                                                                });
                                                                            });
                                                                            $('.checkvgmwtwithtarewtclass').each(function () {
                                                                                $(this).rules('add', {
                                                                                    checkvgmwtwithtarewtclass: true,
//                                                                                    messages: {
//                                                                                        checkvgmwtwithtarewtclass: "<bean:message key="ecom.msg.invalidcontnum" bundle="<%=clientcode%>"/>"
//                                                                                    }
                                                                                });
                                                                            });
                                                                        } else {
                                                                            $("#vgmviewdiv").html("");
                                                                            if ($('#submitby').val() == 'blno') {
                                                                                $('#loadconfirmerror').html("<bean:message key="ecom.msg.vgm.invalidblno" bundle="<%=clientcode%>"/>");
                                                                            } else {
                                                                                $('#loadconfirmerror').html("<bean:message key="ecom.msg.vgm.invalidbookno" bundle="<%=clientcode%>"/>");
                                                                            }
                                                                            $('#loadconfirmerror').show();
                                                                            $("#searchbtn").button('reset');
                                                                        }
                                                                    } else {
                                                                        window.location.href = "SessionExpired.jsp";
                                                                    }
                                                                },
                                                                error: function (xhr) {
                                                                    if (xhr.status == 403) {
                                                                        window.location.href = "SessionExpired.jsp";
                                                                    }
                                                                }

                                                            });
                                                        } else if (result == 'notvalid') {
                                                            $("#vgmviewdiv").html("");
                                                            if ($('#submitby').val() == 'blno') {
                                                                $('#loadconfirmerror').html("<bean:message key="ecom.msg.vgm.invalidblno" bundle="<%=clientcode%>"/>");
                                                            } else {
                                                                $('#loadconfirmerror').html("<bean:message key="ecom.msg.vgm.invalidbookno" bundle="<%=clientcode%>"/>");
                                                            }
                                                            $('#loadconfirmerror').show();
                                                            $("#searchbtn").button('reset');
                                                        } else {
                                                            $("#vgmviewdiv").html("");
                                                            $('#loadconfirmerror').html("<bean:message key="ecom.msg.vgm.updatefailed" bundle="<%=clientcode%>"/>");
                                                            $('#loadconfirmerror').show();
                                                            $("#searchbtn").button('reset');
                                                        }
                                                    } else {
                                                        window.location.href = "SessionExpired.jsp";
                                                    }
                                                },
                                                error: function (xhr) {
                                                    if (xhr.status == 403) {
                                                        window.location.href = "SessionExpired.jsp";
                                                    }
                                                }
                                            });
                                            return false;
                                        }
                                    });
                                });
                                $(document).ready(function () {
                                    var date1 = new Date();
                                    date1.setDate(date1.getDate());
                                    $('#vgmobtaindate').datepicker("setDate", date1);
                                });
                                function setTareWeight(value, tareweightid, eqpType) {
                                    if (value != "") {
                                        $.ajax({
                                            type: "POST",
                                            cache: false,
                                            async: false,
                                            url: "VGMServlet?type=tareWT&containerNo=" + encodeURIComponent(value) + "&eqpType=" + encodeURIComponent(eqpType),
                                            success: function (response) {
                                                if (response != "") {
                                                    $('#' + tareweightid).html(response);
                                                }
                                            },
                                            error: function (xhr) {
                                                if (xhr.status == 403) {
                                                    window.location.href = "SessionExpired.jsp";
                                                }
                                            }

                                        });
                                    } else {
                                        $('#' + tareweightid).html("");
                                    }
                                }

                                function setHiddenValues() {
                                    $('#bookNo').val($('#hidbookno').val());
                                    $('#blNo').val($('#hidblno').val());
                                    $('#vgmsubmission').val($('#submittype').val());
                                    $('#vgmapname').val($('#hiddenapname').val());
                                    $('#vgmshname').val($('#hiddenshname').val());
                                    $('#vgmorigin').val($('#hiddenorigin').val());
                                    $('#vgmdelivery').val($('#hiddendestination').val());
                                    $('#vgmblpol').val($('#hiddenloadport').val());
                                    $('#vgmblpod').val($('#hiddendischarge').val());
                                    $('#vgmStatus').val($('#hiddenvgmstatus').val());
                                }
                                function validEmailCheckToCommon(idname) {
                                    var email = document.getElementById(idname).value;
                                    /*email=email.replace(",,",",");
                                     email=email.replace(";,",",");
                                     email=email.replace(";,",",");
                                     email=email.replace(",;",",");
                                     email = email.replace(/;/gi,",");*/
                                    //var result = email.replace(/\s/g, "").split(/,|;/);
                                    if (email.length > 0) {
                                        var result = email.replace(/\s/g, "").split(/,/);
                                        for (var i = 0; i < result.length; i++) {
                                            if (!validateEmailToCommon(result[i])) {
                                                return false;
                                            }
                                        }
                                    }
                                    //document.getElementById('email_text').value=result;
                                    return true;
                                }
                                function validateEmailToCommon(value) {
                                    var regex = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
                                    return (regex.test(value)) ? true : false;
                                }
                                $(document).ready(function () {
                                    $('#sendemailid').keyup(function () {
                                        $('#emailvalidation').hide();
                                    });
                                    $('.containernovalidation').keyup(function () {
                                        if (this.value.match(/[^a-zA-Z0-9]/g)) {
                                            this.value = this.value.replace(/[^a-zA-Z0-9]/g, '');
                                        }
                                    });
                                    $('#referenceNo').keyup(function () {
                                        $('#loadconfirmerror').hide();
                                    });
                                });
    <%
        if ("zh".equalsIgnoreCase(clientcode) || "ma".equalsIgnoreCase(clientcode) || "es".equalsIgnoreCase(clientcode)) {
    %>
                                toastr.info("<bean:message key="ecom.msg.formsineng" bundle="<%=clientcode%>"/>", "INFO");
    <%}%>

</script>
<script type="text/javascript">
    function ImportData()
    {
        var totresponse, counttotresponse, countresponse, cargowtresponse, concont, cwtresponse, errresponse, response, error;
        var importexcel = document.getElementById('importexcel').value;
        var extn = importexcel.substring(importexcel.lastIndexOf("."), importexcel.length).toLowerCase();
        var comdityflag = true;
        var appconsize = $('#datatablebyeslvgm').find('tbody').find('tr').length;
        if (extn == "")
        {
            alert(selfile);
            document.getElementById('importexcel').value = "";
            document.getElementById('importexcel').focus();
            return false;
        }

        if (extn != ".xls")
        {
            alert(invfileformat);
            document.getElementById('importexcel').value = "";
            document.getElementById('importexcel').focus();
            return false;
        } else {
            openLoader();
            var exceldata;
            exceldata = new FormData();
            var checkcontianerordering = true;
            exceldata.append('action', 'VGMImportExcel');
            exceldata.append('importexcel', $('#importexcel')[0].files[0]);
            $.ajax({url: 'VGMImportExcel',
                type: 'POST',
                cache: false,
                contentType: false,
                processData: false,
                data: exceldata,
                success: function (result) {
                    closeLoader();
                    console.log(result);
                    if (result == "true")
                    {
                        alert(invxltemp);
                        return false;
                    }
                    if (result.indexOf("~*~~~~~~~~~~~*~") !== -1) {
                        counttotresponse = result.split("~*~~~~~~~~~~~*~");
                        totresponse = counttotresponse[0];
                        countresponse = counttotresponse[1];
                        cargowtresponse = counttotresponse[2];
                        concont = countresponse.replace(/~/g, "");
                        concont = trim(concont);
                        //	console.log(":::>>>>>>>>>cwtresponse::::::::>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+cwtresponse);
                        if (cargowtresponse != null && cargowtresponse != 'undefined' && cargowtresponse != undefined && cargowtresponse != "") {
                            cwtresponse = cargowtresponse.replace(/~/g, "");
                            cwtresponse = trim(cwtresponse);
                        }

                    } else {
                        totresponse = result;
                    }
                    if (totresponse.indexOf("~~~~~~~~~~") !== -1) {
                        errresponse = totresponse.split("~~~~~~~~~~");
                        response = errresponse[0];
                        error = errresponse[1];
                    } else
                    {
                        response = totresponse;
                    }

                    if (parseInt(concont) > parseInt(appconsize))
                    {
                        //alert(concont+":::"+selsize);
                        alert(vgmmorecontcountbl);
                        return false;
                    }
                    if (parseInt(concont) < parseInt(appconsize))
                    {
                        //alert(concont+":::"+selsize);
                        alert(vgmlesscontcountbl);
                        return false;
                    } else if (error != null && error != 'undefined')
                    {
                        $("#myImportModal").modal("hide");
                        setTimeout(function () {
                            $("#myModalImportErrors").modal("show");
                        }, 500);
                        error = error.replace(/~/g, "");
                        // alert(error);
                        $('#importerr').html(error);
                        $('#datatableexcel').DataTable();
                        return false;
                    } else if (cwtresponse != null && cwtresponse != 'undefined' && cwtresponse != undefined && cwtresponse != "")
                    {
                        $("#myImportModal").modal("hide");
                        setTimeout(function () {
                            $("#myModalImportErrors").modal("show");
                        }, 500);
                        error = cwtresponse.replace(/~/g, "");
                        $('#importerr').html(error);
                        $('#datatableexcel1').DataTable();
                        return false;
                    } else if (response != null && response != "")
                    {
                        var dup = response.split("~~~~~~~");
                        var duparr = new Array();
                        var dupcont = "";
                        var precheck = "", cuurcheck = "";
                        var dupboolean = false;
                        var dupival = 0;
                        //console.log("dup len>>"+dup.length);
                        for (var dupinc = 0; dupinc < dup.length - 1; dupinc++)
                        {
                            dupboolean = false;
                            var dupres = dup[dupinc];
                            dupres = dupres.split("~~~");
                            dupcont = checkEmpty(dupres[0]);
                            if (dupcont != checkEmpty($('#containerno' + dupinc).val())) {
                                alert(contorderbl);
                                return false;
                            }
                            for (var ari = 0; ari < duparr.length; ari++) {
                                //console.log("check>>"+duparr[ari]+">>11"+dupcont);
                                if (duparr[ari] == dupcont) {
                                    dupboolean = true;
                                    break;
                                }
                            }


                            if (!dupboolean) {
                                duparr[dupival] = dupcont;
                                dupival++;
                                precheck = dupcont;
                            } else if (dupcont != precheck) {
                                alert(contorderbl);
                                return false;
                            }
                        }

                    }
                    if (checkcontianerordering)
                    {
                        var req1 = response.split("~~~~~~~");
                        var i = 0;
                        var contno = '', vgmwgt = '';
                        //console.log(req1.length-1);
                        for (i = 0; i < req1.length - 1; i++)
                        {
                            var reqresonse = req1[i];
                            var req = reqresonse.split("~~~");
                            contno = checkEmpty(req[0]);
                            vgmwgt = checkEmpty(req[1]);

                            //
                            $('#vgmtd1' + i).val(vgmwgt).trigger('blur');

                        }
                        document.getElementById('importexcel').value = "";
                        $("#myImportModal").modal("hide");
                        return true;
                    } else
                    {
                        alert(entmandatoryfields);
                        document.getElementById('importexcel').value = "";
                        $("#myImportModal").modal("hide");
                        return true;
                    }
                }, error: function (result) {
                    closeLoader();
                    alert("Something went wrong please try again !!");
                }
            });
        }
    }
    function checkEmpty(strval)
    {
        if (strval == "no~records" || strval == undefined || strval == "undefined")
        {
            strval = "";
        }
        return strval;
    }
</script> 

