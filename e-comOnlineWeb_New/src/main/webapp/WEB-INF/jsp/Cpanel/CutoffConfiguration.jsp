<%--
    Document   : CutoffConfiguration
    Created on : Feb 14, 2017, 2:56:52 PM
    Author     : deepa.p
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="net.ecom.common.GeneralConf" %>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%
int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Cutoff Configuration</div>
<div class="card card-body card-shadow page_bottom_content">
    <div class="container">
        <div class="details_search">

            <html:form styleId="CutoffConfigForm"  method="post" action="/savecutoffconfig" styleClass="form-horizontal" >
                <html:hidden styleId="saveType" property="saveType" />
                <input type="hidden" id="typeheadhidden" name="typeheadhidden">
                <div class="form-group row">
                    <label class="col-sm-2 control-label">Port<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:text property="port"  styleId="port"  styleClass="form-control light_border required portsnotes"   maxlength="100" onblur="setCutoffTerminal(this.value);"/>
                    </div>
                    <label class="col-sm-2 control-label">Terminal<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:select property="terminal"  styleId="terminal"  styleClass="form-control light_border required ">
                            <html:option value="">Select Terminal</html:option>
                        </html:select>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 control-label">CFS Full Container Gate-in Closing<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:text property="cfsClosing"  styleId="cfsClosing"  styleClass="form-control number light_border required "   maxlength="3"/>
                    </div>
                    <label class="col-sm-2 control-label">VGM Closing<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:text property="vgmClosing"  styleId="vgmClosing"  styleClass="form-control number light_border required "   maxlength="3"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 control-label">Document Closing<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:text property="documentClosing"  styleId="documentClosing"  styleClass="form-control light_border number required "   maxlength="3"/>
                    </div>
                    <label class="col-sm-2 control-label">EDI Declaration Closing<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:text property="ediDecClosing"  styleId="ediDecClosing"  styleClass="form-control light_border required number"   maxlength="3"/>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-sm-2 control-label">Full Container Gate-In Closing<span class="mandatory">*</span></label>
                    <div class="col-sm-3">
                        <html:text property="fullCntrGateClosing"  styleId="fullCntrGateClosing"  styleClass="form-control light_border required number"   maxlength="3"/>
                    </div>
                    <label class="col-sm-2 control-label">Exclude Weekends</label>
                    <div class="col-sm-3">
                        <input type="checkbox" id="excludeWeekEnds" name="excludeWeekEnds" value="Y" style="width:25px; height:25px;" />
                    </div>
                </div>
                <br/>
                <div class="form-group row">
                    <div class="col-sm-5">
                    </div>
                    <div class="col-sm-5">
                        <button type="submit" class="btn btn-primary btn-lg search_button" id="searchID"> Submit</button>
                        <button type="button" class="btn btn-danger btn-lg search_button" onclick="resetCutOffVal();"> Reset</button>
                    </div>
                </div>
            </html:form>

        </div>
    </div>
</div>
<div id="cutoffdetails" style="display:none;"></div>

<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/terminal.js?<%=jsno%>"></script>
<script type="text/javascript">
                            $(document).ready(function () {
                                $('#cfsClosing,#vgmClosing,#documentClosing,#ediDecClosing,#fullCntrGateClosing').attr("placeholder", "In hours");
                                var portJson = portJson;
                                var TerminalJson = TerminalJson;
                                $('#saveType').val('save');
                                $("#CutoffConfigForm").validate({
                                    submitHandler: function (form) {

                                        $('#port,#terminal').attr("disabled", false);
                                        var check = true;
                                        // callLoading();
                                        var port = $('#port').val().split("-");
                                        port = trim(port[0]);
                                        terminal = $('#terminal').val()
                                        if ($('#saveType').val() == 'save') {
                                            $.ajax({
                                                type: "POST",
                                                async: false,
                                                cache: false,
                                                url: "ecomAjax?type=checkcutoffconfig&port=" + encodeURIComponent(port) + "&terminal=" + encodeURIComponent(terminal),
                                                success: function (response) {
                                                    if (response == 'Exist') {
                                                        //  destroyLoading();
                                                        check = false;
                                                        alert('Duplicate port and terminal');
                                                        return false;
                                                    } else {
                                                        return true;
                                                    }
                                                }
                                            });
                                        } else {
                                            return true;
                                        }
                                        if (check) {
                                            return true;
                                        }

                                    }
                                });
                                var form = $("#CutoffConfigForm").show();
                                form.validate().settings.ignore = ":disabled,:hidden,[readonly=readonly]";
                            });
                            var cutportJson = portJson;
                            cutportJson.push({"PortCode": "All", "PortName": "All Port"});
                            $(window).one("load", function () {
                                var ports = new Bloodhound({
                                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
                                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                                    local: cutportJson
                                });
                                ports.initialize();
                                $('#port').typeahead(
                                        {
                                            hint: true,
                                            highlight: true,
                                            minLength: 2
                                        },
                                        {
                                            name: 'ports',
                                            limit: 20,
                                            displayKey: function (portJson) {
                                                return portJson.PortCode + " - " + portJson.PortName;
                                            },
                                            source: ports.ttAdapter()
                                        }).on('typeahead:render', function (obj, datum) {
                                    $("#typeheadhidden").val("");
                                }).on('typeahead:selected', function (obj, datum) {
                                    $("#typeheadhidden").val(datum.PortName);
                                }).on('typeahead:autocomplete', function (obj, datum) {
                                    $("#typeheadhidden").val(datum.PortName);
                                }).on("typeahead:change", function (obj, datum) {
                                    var hiddenValue = $("#typeheadhidden").val();
                                    if (hiddenValue == null || hiddenValue == "") {
                                        $('#portcode').typeahead('val', "");
                                        $(obj.currentTarget).val("");
                                        $('#terminal').html('');
                                        $('#terminal').append($('<option>', {
                                            value: '',
                                            text: 'Select Terminal'
                                        }));
                                    }
                                });
                            });
                            setCutoffTable();
                            function setCutoffTable() {
                                $.ajax({
                                    type: "POST",
                                    cache: false,
                                    url: "cutoffconfigview.do",
                                    success: function (response) {
                                        $('#cutoffdetails').html(response);
                                        $('#cutoffdetails').show();
                                    }
                                });
                            }
                            function setCutoffTerminal(val) {
                                if (val != '') {
                                    var code = val.split("-");
                                    $('#terminal').html('');
                                    code = trim(code[0]);
                                    $('#terminal').append($('<option>', {
                                        value: '',
                                        text: 'Select Terminal'
                                    }));
                                    $('#terminal').append($('<option>', {
                                        value: 'All',
                                        text: 'All Terminal'
                                    }));
                                    $.each(TerminalJson, function (i, obj) {
                                        if (obj.portCode == code) {
                                            $('#terminal').append($('<option>', {
                                                value: obj.terminalCode,
                                                text: obj.terminalName
                                            }));
                                        }
                                    });
                                }
                            }
                            function editCutOffDetails(port, terminal, cfs, vgm, doc, edi, full, excluweek) {
                                resetCutOffVal();
                                setCutoffTerminal(port);
                                $('#port').val(port);
                                $('#cfsClosing').val(cfs);
                                $('#vgmClosing').val(vgm);
                                $('#documentClosing').val(doc);
                                $('#ediDecClosing').val(edi);
                                $('#fullCntrGateClosing').val(full);
                                if(excluweek == 'Y' || excluweek == 'y' ){
                                    $('#excludeWeekEnds').prop('checked', true);
                                }
                                $('#saveType').val('update');
                                $('#terminal').val(terminal);
                                $('#port,#terminal').attr("disabled", true);
                                $('#port').css("background-color", "#eee");

                            }
                            function resetCutOffVal() {
                                $('#port').val("");
                                $('#cfsClosing').val("");
                                $('#vgmClosing').val("");
                                $('#documentClosing').val("");
                                $('#ediDecClosing').val("");
                                $('#fullCntrGateClosing').val("");
                                $('#excludeWeekEnds').prop('checked', false);
                                $('#saveType').val('update');
                                $('#terminal').val("");
                                $('#port,#terminal').attr("disabled", false);
                                $('#port').css("background-color", "white");
                            }
                            function deleteCutoffDetails(port, terminal, rowid) {
                                var retVal = false;
                                retVal = confirm("Do you want to delete?");
                                if (retVal) {
                                    $.ajax({
                                        type: "POST",
                                        cache: false,
                                        url: "ecomAjax?type=deletecutoffconfig&port=" + encodeURIComponent(port) + "&terminal=" + encodeURIComponent(terminal),
                                        success: function (response) {
                                            $('#' + rowid).remove();
                                        }
                                    });
                                }
                            }
                            function trim(value) {
                                return LTrim(RTrim(value));
                            }
                            function LTrim(value) {
                                var re = /\s*((\S+\s*)*)/;
                                return value.replace(re, "$1");
                            }
                            // Removes ending whitespaces
                            function RTrim(value) {
                                var re = /((\s*\S+)*)\s*/;
                                return value.replace(re, "$1");
                            }
                            
</script>


