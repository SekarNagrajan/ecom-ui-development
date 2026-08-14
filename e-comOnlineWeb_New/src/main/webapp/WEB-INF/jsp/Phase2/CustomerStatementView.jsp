<%@page language="java" import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="net.ecom.common.GeneralConf"%>
<%
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String clientcode =  (String)session.getAttribute("clientcode");
    String csFromInv = (String) getServletContext().getAttribute(GeneralConf.CSFROMINV);
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" />  <bean:message key="ecom.cs.csview" bundle="<%=clientcode%>"/>  </div>
<div class="card-body page_bottom_content">
    <ul id="tabs" class="nav nav-tabs tab_custom" role="tablist">

        <li class="nav-item">
            <a class="nav-link active" data-toggle="tab" href="#acco_sum"><bean:message key="ecom.cs.accsum" bundle="<%=clientcode%>"/></a>
        </li>
         <%if ("Y".equalsIgnoreCase(csFromInv)) {%>

        <li class="nav-item">
            <a class="nav-link" data-toggle="tab" href="#invoice"><bean:message key="ecom.cs.invs" bundle="<%=clientcode%>"/></a>
        </li>

        <li class="nav-item">
            <a class="nav-link" data-toggle="tab" href="#payment"><bean:message key="ecom.cs.payments" bundle="<%=clientcode%>"/></a>
        </li>

        <li class="nav-item">
            <a class="nav-link" data-toggle="tab" href="#outsta"><bean:message key="ecom.cs.outst" bundle="<%=clientcode%>"/></a>
        </li>
        <%}%>
    </ul>
    <div class="tab-content">

        <div id="acco_sum" class="tab-pane active">

            <div class="card card-body card-shadow">

                <div class="container">
                    <form method="post" id="accform" class="form-horizontal">
                        <div class="form-group">
                            <div class="row">
                                <div class="col-xs-12 col-sm-4 offset-md-2">
                                    <input type="text" name="accFromDate" id="accFromDate" class="form-control inside_input remove_autocomplete required" placeholder="<bean:message key="ecom.cro.fromdate" bundle="<%=clientcode%>"/>"/>
                                    <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                    <img class="pages_icon" src="Design/assets/images/page_icons/date.png" />     

                                </div>
                                 <%if ("Y".equalsIgnoreCase(csFromInv)) {%>
                                <div class="col-xs-12 col-sm-4">
                                    <input type="text" name="accToDate" id="accToDate"  class="form-control inside_input remove_autocomplete required" placeholder="<bean:message key="ecom.cro.todate" bundle="<%=clientcode%>"/>"/>
                                    <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                    <img class="pages_icon" src="Design/assets/images/page_icons/date.png" />  

                                </div>
                                <%}else{%>
                                 <div class="col-xs-12 col-sm-4">
                                     <input type="text" name="accToDate" id="accToDate"  class="form-control inside_input remove_autocomplete required"  disabled="disabled" placeholder="<bean:message key="ecom.cro.todate" bundle="<%=clientcode%>"/>"/>
                                    <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                    <img class="pages_icon" src="Design/assets/images/page_icons/date.png" />  

                                </div>
                                    <%}%>
                                
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="row">
                               <%if ("Y".equalsIgnoreCase(csFromInv)) {%>
                                <div class="col-xs-12 col-sm-2 m-t-10 offset-md-2">

                                    <label for="accall" class="label_switch" ><bean:message key="rv.All" bundle="<%=clientcode%>"/> </label>
                                    <label class="switch">
                                        <input type="checkbox" name="accType" id="accall" value="A"  onclick="searchTypeMethod('A', 'acc');" />
                                        <span class="slider round"></span>
                                    </label>


                                </div>
                                   <%}else{%>
                                   <div class="col-xs-12 col-sm-2 m-t-10 offset-md-2">

                                    <label for="accall" class="label_switch"  ><bean:message key="rv.All" bundle="<%=clientcode%>"/> </label>
                                    <label class="switch">
                                        <input type="checkbox" name="accType" id="accall"  value="A" disabled="disabled" onclick="searchTypeMethod('A', 'acc');" />
                                        <span class="slider round"></span>
                                    </label>


                                </div>
                                     <%}%>
                                <div class="col-xs-12 col-sm-2 m-t-10">

                                    <label for="accdate" class="label_switch" > <bean:message key="ecom.cs.seldate" bundle="<%=clientcode%>"/> </label>

                                    <label class="switch">
                                        <input type="checkbox" name="accType" id="accdate" value="D" onclick="searchTypeMethod('D', 'acc');" />
                                        <span class="slider round"></span>
                                    </label>
                                </div>

                                <div class="col-xs-12 col-sm-4 m-t-10">
                                    <button class="btn btn-info btn_block_custom"><bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                                </div>

                            </div>

                        </div>
                    </form>
                </div>

            </div>

            <div id="actSumydiv1"> 

            </div>

        </div>



        <div id="invoice" class="tab-pane">

            <div class="card card-body card-shadow">

                <div class="container">
                    <form method="post" id="invform" class="form-horizontal">
                        <div class="form-group">
                            <div class="row">
                                <div class="col-xs-12 col-sm-4 offset-md-2">
                                    <input type="text" name="invFromDate" id="invFromDate" class="form-control inside_input remove_autocomplete required" placeholder="<bean:message key="ecom.cro.fromdate" bundle="<%=clientcode%>"/>"/>
                                    <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                    <img class="pages_icon" src="Design/assets/images/page_icons/date.png" />     

                                </div>
                               
                                <div class="col-xs-12 col-sm-4">
                                    <input type="text" name="invToDate" id="invToDate"  class="form-control inside_input remove_autocomplete required" placeholder="<bean:message key="ecom.cro.todate" bundle="<%=clientcode%>"/>"/>
                                    <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                    <img class="pages_icon" src="Design/assets/images/page_icons/date.png" />  

                                </div>
                              
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="row">

                                <div class="col-xs-12 col-sm-2 m-t-10 offset-md-2">

                                    <label for="invall" class="label_switch"> <bean:message key="rv.All" bundle="<%=clientcode%>"/> </label>
                                    <label class="switch">
                                        <input type="checkbox" name="invType" value="A" id="invall" onclick="searchTypeMethod('A', 'inv');" />
                                        <span class="slider round"></span>
                                    </label>
                                </div>

                                <div class="col-xs-12 col-sm-2 m-t-10">

                                    <label for="invdate" class="label_switch" > <bean:message key="ecom.cs.seldate" bundle="<%=clientcode%>"/> </label>

                                    <label class="switch">
                                        <input type="checkbox"  name="invType" value="D" id="invdate"  onclick="searchTypeMethod('D', 'inv');"/>
                                        <span class="slider round"></span>
                                    </label>
                                </div>

                                <div class="col-xs-12 col-sm-4 m-t-10">
                                    <button class="btn btn-info btn_block_custom"><bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                                </div>

                            </div>

                        </div>

                    </form>
                </div>

            </div>
            <div id="invSumydiv1">
            </div>
        </div>

        <div id="payment" class="tab-pane">

            <div class="card card-body card-shadow">

                <div class="container">
                    <form method="post" id="payform" class="form-horizontal">
                        <div class="form-group">
                            <div class="row">
                                <div class="col-xs-12 col-sm-4 offset-md-2">
                                    <input type="text" name="payFromDate" id="payFromDate" class="form-control inside_input remove_autocomplete required"  placeholder="<bean:message key="ecom.cro.fromdate" bundle="<%=clientcode%>"/>" />
                                    <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                    <img class="pages_icon" src="Design/assets/images/page_icons/date.png" />     

                                </div>
                                   
                                <div class="col-xs-12 col-sm-4">
                                    <input type="text" name="payToDate" id="payToDate"  class="form-control inside_input remove_autocomplete required"  placeholder="<bean:message key="ecom.cro.todate" bundle="<%=clientcode%>"/>"  />
                                    <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                    <img class="pages_icon" src="Design/assets/images/page_icons/date.png" />  

                                </div>
                                    
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="row">

                                <div class="col-xs-12 col-sm-2 m-t-10 offset-md-2">

                                    <label for="payall" class="label_switch"> <bean:message key="rv.All" bundle="<%=clientcode%>"/> </label>
                                    <label class="switch">
                                        <input type="checkbox" name="payType" id="payall" value="A" onclick="searchTypeMethod('A', 'pay');" />
                                        <span class="slider round"></span>
                                    </label>

                                </div>

                                <div class="col-xs-12 col-sm-2 m-t-10">

                                    <label for="paydate" class="label_switch"> <bean:message key="ecom.cs.seldate" bundle="<%=clientcode%>"/> </label>

                                    <label class="switch">
                                        <input type="checkbox" name="payType" id="paydate" value="D" onclick="searchTypeMethod('D', 'pay');" />
                                        <span class="slider round"></span>
                                    </label>
                                </div>

                                <div class="col-xs-12 col-sm-4 m-t-10">
                                    <button class="btn btn-info btn_block_custom"><bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                                </div>

                            </div>

                        </div>
                    </form>

                </div>

            </div>

            <div id="paySumydiv1">
            </div>

        </div>

        <div id="outsta" class=" tab-pane ">

            <div class="card card-body card-shadow">

                <div class="container">
                    <form method="post" id="outform" class="form-horizontal">
                        <div class="form-group">
                            <div class="row">
                                <div class="col-xs-12 col-sm-4 offset-md-2">
                                    <input type="text" name="outFromDate" id="outFromDate" class="form-control inside_input remove_autocomplete required" placeholder="<bean:message key="ecom.cro.fromdate" bundle="<%=clientcode%>"/>"
                                    <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                    <img class="pages_icon" src="Design/assets/images/page_icons/date.png" />     

                                </div>
                              
                                <div class="col-xs-12 col-sm-4">
                                    <input type="text" name="outToDate" id="outToDate"  class="form-control inside_input remove_autocomplete required" placeholder="<bean:message key="ecom.cro.todate" bundle="<%=clientcode%>"/>"
                                    <span class="glyphicon glyphicon-calendar form-control-feedback" aria-hidden="true"></span>
                                    <img class="pages_icon" src="Design/assets/images/page_icons/date.png" />  
                                </div>
                                 
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="row">

                                <div class="col-xs-12 col-sm-2 m-t-10 offset-md-2">

                                    <label for="outall" class="label_switch"> <bean:message key="rv.All" bundle="<%=clientcode%>"/>  </label>

                                    <label class="switch">
                                        <input type="checkbox" name="outType" value="A" id='outall' onclick="searchTypeMethod('A', 'out');"/>
                                        <span class="slider round"></span>
                                    </label>
                                </div>

                                <div class="col-xs-12 col-sm-2 m-t-10">

                                    <label for="outdate" class="label_switch"> <bean:message key="ecom.cs.seldate" bundle="<%=clientcode%>"/> </label>
                                    <label class="switch">
                                        <input type="checkbox" name="outType" value="D" id='outdate' onclick="searchTypeMethod('D', 'out');"/>
                                        <span class="slider round"></span>
                                    </label>                                 

                                </div>
                                <div class="col-xs-12 col-sm-4 m-t-10">
                                    <button class="btn btn-info btn_block_custom"><bean:message key="ecom.Search" bundle="<%=clientcode%>"/></button>
                                </div>

                            </div>

                        </div>

                    </form>
                </div>

            </div>
            <div id="outSumydiv1">
            </div>

        </div>

    </div>
</div>

<script type="text/javascript">
    $('#accFromDate,#accToDate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>'});
    $('#invFromDate,#invToDate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>'});
    $('#payFromDate,#payToDate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>'});
    $('#outFromDate,#outToDate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>'});
    var newdate = new Date();
    newdate.setDate(newdate.getDate() - 90); // minus the date
    var fdate = newdate;
    var newdate1 = new Date();
    var tdate = newdate1;
    var frdate, todate;
    showInitailsAccSumy();
    //alert(newdate1);
    $(document).ready(function () {
        $('#outFromDate,#outToDate').attr("disabled", true);
        $('#tabs a').click(function (e) {
            e.preventDefault();
            showInitailsAccSumy();
            var b = $($(this).attr('href')).index();
            if (b == 0) {
                var serializedData = "";
                $("#actSumydiv1").html("");
                $('#accFromDate').datepicker("setDate", fdate);
                $('#accToDate').datepicker("setDate", tdate);
                frdate = $('#accFromDate').val();
                todate = $('#accToDate').val();
                //document.getElementById('accFromDate').value =fdate;
                //document.getElementById('accToDate').value =tdate;
                //document.getElementsByName("accType")[1].checked = true;
                $('#accdate').prop('checked', true).trigger("onclick");
                openLoader();
                $.post("CustStmtSearchAction.do?accType=D&accFromDate=" + frdate + "&accToDate=" + todate + "&accOption=CA&type=acc", serializedData, function (data) {
                    $("#actSumydiv1").html(data);
                    closeLoader();
                });
            } else if (b == 1) {
                var serializedData = "";
                $("#invSumydiv1").html("");
                $('#invFromDate').datepicker("setDate", fdate);
                $('#invToDate').datepicker("setDate", tdate);
                frdate = $('#invFromDate').val();
                todate = $('#invToDate').val();
                //document.getElementsByName("invType")[1].checked = true;
                $('#invdate').prop('checked', true).trigger("onclick");
                openLoader();
                $.post("CustStmtSearchAction.do?invType=D&invFromDate=" + frdate + "&invToDate=" + todate + "&invOption=CI&type=inv", serializedData, function (data) {
                    $("#invSumydiv1").html(data);
                    closeLoader();
                });

            } else if (b == 2) {
                var serializedData = "";
                $("#paySumydiv1").html("");
                $('#payFromDate').datepicker("setDate", fdate);
                $('#payToDate').datepicker("setDate", tdate);
                frdate = $('#payFromDate').val();
                todate = $('#payToDate').val();
                //document.getElementsByName("payType")[1].checked = true;
                $('#paydate').prop('checked', true).trigger("onclick");
                openLoader();
                $.post("CustStmtSearchAction.do?payType=D&payFromDate=" + frdate + "&payToDate=" + todate + "&payOption=CP&type=pay", serializedData, function (data) {
                    $("#paySumydiv1").html(data);
                    closeLoader();
                });
            } else if (b == 3) {
                var serializedData = "";
                $("#outSumydiv1").html("");
                //document.getElementsByName("outType")[0].checked = true;
                $('#outall').prop('checked', true).trigger("onclick");
                openLoader();
                $.post("CustStmtSearchAction.do?outType=A&outFromDate=&outToDate=&outOption=CO&type=out", serializedData, function (data) {
                    $("#outSumydiv1").html(data);
                    closeLoader();
                });
            }
            e.preventDefault()
            $(this).tab('show')
        });

        //validate
        $("#accform").validate({
            rules: {
                accType: {
                    required: true
                },
                accFromDate: {
                    required: function (element) {
                        return  document.getElementsByName("accType")[1].checked
                    }
                },
                accToDate: {
                    required: function (element) {
                        return  document.getElementsByName("accType")[1].checked
                    }
                }
            },
            messages: {
                accType: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                accFromDate: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                accToDate: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                }
            },
            submitHandler: function (form) {
                var strType = document.getElementsByName("accType");
                var accType;
                for (var i = 0; i < strType.length; i++) {
                    if (strType[i].checked == true) {
                        accType = strType[i].value;
                    }
                }
                console.log('acc type = ' + accType);
                var fromDate = document.getElementById('accFromDate').value;
                var toDate = document.getElementById('accToDate').value;
                var option = "CA";
                var test = 'yes';
                var serializedData = "";
                openLoader();
                $("#actSumydiv1").html("");
                $.post("CustStmtSearchAction.do?accType=" + accType + "&accFromDate=" + fromDate + "&accToDate=" + toDate + "&accOption=" + option + "&type=acc", serializedData, function (data) {
                    $("#actSumydiv1").html(data);
                    closeLoader();
                });
                return false;
            }
        });

        $("#invform").validate({
            rules: {
                invType: {
                    required: true
                },
                invFromDate: {
                    required: function (element) {
                        return  document.getElementsByName("invType")[1].checked
                    }
                },
                invToDate: {
                    required: function (element) {
                        return  document.getElementsByName("invType")[1].checked
                    }
                }
            },
            messages: {
                invType: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                invFromDate: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                invToDate: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                }
            },
            submitHandler: function (form) {
                var strType = document.getElementsByName("invType");
                var invType = 'A';
                for (var i = 0; i < strType.length; i++) {
                    if (strType[i].checked == true) {
                        invType = strType[i].value;
                    }
                }
                var fromDate = document.getElementById('invFromDate').value;
                var toDate = document.getElementById('invToDate').value;
                var option = "CI";
                var test = 'yes';
                var serializedData = "";
                openLoader();
                $("#invSumydiv1").html("");
                $.post("CustStmtSearchAction.do?invType=" + invType + "&invFromDate=" + fromDate + "&invToDate=" + toDate + "&invOption=" + option + "&type=inv", serializedData, function (data) {
                    $("#invSumydiv1").html(data);
                    closeLoader();
                });

                return false;
            }
        });

        $("#payform").validate({
            rules: {
                payType: {
                    required: true
                },
                payFromDate: {
                    required: function (element) {
                        return  document.getElementsByName("payType")[1].checked
                    }
                },
                payToDate: {
                    required: function (element) {
                        return  document.getElementsByName("payType")[1].checked
                    }
                }
            },
            messages: {
                payType: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                payFromDate: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                payToDate: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                }
            },
            submitHandler: function (form) {
                var strType = document.getElementsByName("payType");
                var payType = 'A';
                for (var i = 0; i < strType.length; i++) {
                    if (strType[i].checked == true) {
                        payType = strType[i].value;
                    }
                }
                var fromDate = document.getElementById('payFromDate').value;
                var toDate = document.getElementById('payToDate').value;
                var option = "CP";
                var serializedData = "";
                var test = 'yes';
                openLoader();
                $("#paySumydiv1").html("");
                $.post("CustStmtSearchAction.do?payType=" + payType + "&payFromDate=" + fromDate + "&payToDate=" + toDate + "&payOption=" + option + "&type=pay", serializedData, function (data) {
                    $("#paySumydiv1").html(data);
                    closeLoader();
                });
                return false;
            }
        });

        $("#outform").validate({
            rules: {
                outType: {
                    required: true
                },
                outFromDate: {
                    required: function (element) {
                        return  document.getElementsByName("outType")[1].checked
                    }
                },
                outToDate: {
                    required: function (element) {
                        return  document.getElementsByName("outType")[1].checked
                    }
                }
            },
            messages: {
                outType: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                outFromDate: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                },
                outToDate: {
                    required: "<bean:message key="ecom.msg.fieldreq" bundle="<%=clientcode%>"/>"
                }
            },
            submitHandler: function (form) {
                var strType = document.getElementsByName("outType");
                var outType = 'A';
                for (var i = 0; i < strType.length; i++) {
                    if (strType[i].checked == true) {
                        outType = strType[i].value;
                    }
                }
                var fromDate = document.getElementById('outFromDate').value;
                var toDate = document.getElementById('outToDate').value;
                var option = "CO";
                var test = 'yes';
                var serializedData = "";
                openLoader();
                $("#outSumydiv1").html("");
                $.post("CustStmtSearchAction.do?outType=" + outType + "&outFromDate=" + fromDate + "&outToDate=" + toDate + "&outOption=" + option + "&type=out", serializedData, function (data) {
                    $("#outSumydiv1").html(data);
                    closeLoader();
                });
                return false;
            }
        });

    });

    function showInitailsAccSumy() {
        var serializedData = "";
        $('#accFromDate').datepicker("setDate", fdate);
        $('#accToDate').datepicker("setDate", tdate);
        frdate = $('#accFromDate').val();
        todate = $('#accToDate').val();
        document.getElementsByName("accType")[1].checked = true;
        $("#actSumydiv1").html("");
        $.post("CustStmtSearchAction.do?accType=D&accFromDate=" + frdate + "&accToDate=" + todate + "&accOption=CA&type=acc", serializedData, function (data) {
            $("#actSumydiv1").html(data);
        });
    }
    function searchTypeMethod(value, type) {
        if (value == 'A' && type == 'acc') {
            document.getElementById('accFromDate').value = "";
            document.getElementById('accToDate').value = "";
            if ($('#accall').prop('checked') == true) {
                $('#accdate').prop('checked', false);
                $('#accFromDate,#accToDate').attr("disabled", true);
            }

        } else if (value == 'A' && type == 'inv') {
            document.getElementById('invFromDate').value = "";
            document.getElementById('invToDate').value = "";
            if ($('#invall').prop('checked') == true) {
                $('#invdate').prop('checked', false);
                $('#invFromDate,#invToDate').attr("disabled", true);
            }
        } else if (value == 'A' && type == 'pay') {
            document.getElementById('payFromDate').value = "";
            document.getElementById('payToDate').value = "";
            if ($('#payall').prop('checked') == true) {
                $('#paydate').prop('checked', false);
                $('#payFromDate,#payToDate').attr("disabled", true);
            }
        } else if (value == 'A' && type == 'out') {
            document.getElementById('outFromDate').value = "";
            document.getElementById('outToDate').value = "";
            if ($('#outall').prop('checked') == true) {
                $('#outdate').prop('checked', false);
                $('#outFromDate,#outToDate').attr("disabled", true);
            }
        } else if (value == 'D' && type == 'acc') {
            if ($('#accdate').prop('checked') == true) {
                $('#accall').prop('checked', false);
                $('#accFromDate,#accToDate').attr("disabled", false);
            }
        } else if (value == 'D' && type == 'inv') {
            if ($('#invdate').prop('checked') == true) {
                $('#invall').prop('checked', false);
                $('#invFromDate,#invToDate').attr("disabled", false);
            }

        } else if (value == 'D' && type == 'pay') {
            if ($('#paydate').prop('checked') == true) {
                $('#payall').prop('checked', false);
                $('#payFromDate,#payToDate').attr("disabled", false);
            }
        } else if (value == 'D' && type == 'out') {
            if ($('#outdate').prop('checked') == true) {
                $('#outall').prop('checked', false);
                $('#outFromDate,#outToDate').attr("disabled", false);
            }
        }
    }
    /*   function searchTypeMethod(value, type) {
     if (value == 'A' && type == 'acc') {
     document.getElementById('accFromDate').value = "";
     document.getElementById('accToDate').value = "";
     $('#accFromDate,#accToDate').attr("disabled", true);
     } else if (value == 'A' && type == 'inv') {
     document.getElementById('invFromDate').value = "";
     document.getElementById('invToDate').value = "";
     $('#invFromDate,#invToDate').attr("disabled", true);
     } else if (value == 'A' && type == 'pay') {
     document.getElementById('payFromDate').value = "";
     document.getElementById('payToDate').value = "";
     $('#payFromDate,#payToDate').attr("disabled", true);
     } else if (value == 'A' && type == 'out') {
     document.getElementById('outFromDate').value = "";
     document.getElementById('outToDate').value = "";
     $('#outFromDate,#outToDate').attr("disabled", true);
     } else if (value == 'D' && type == 'acc') {
     $('#accFromDate,#accToDate').attr("disabled", false);
     } else if (value == 'D' && type == 'inv') {
     $('#invFromDate,#invToDate').attr("disabled", false);
     } else if (value == 'D' && type == 'pay') {
     $('#payFromDate,#payToDate').attr("disabled", false);
     } else if (value == 'D' && type == 'out') {
     $('#outFromDate,#outToDate').attr("disabled", false);
     }
     } */
</script>