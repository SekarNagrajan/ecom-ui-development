<%-- 
    Document   : NewInvoiceDetails
    Created on : 8 Dec, 2020, 3:27:24 PM
    Author     : priyadharshinim
--%>

<%@page language="java" import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="net.ecom.common.GeneralConf,java.util.List,java.util.Map,net.ecom.phase2.service.vo.CSInvoiceSummaryVO,net.ecom.common.Validation,net.ecom.phase2.service.vo.AmountSummaryVO,java.util.Hashtable,net.ecom.common.utils.DateConversion_New,java.text.DecimalFormat"%>
<%
    String strDateFormat = (String) session.getServletContext().getAttribute("DATEFROMATfORDATEPICKER");
    String clientcode =  (String)session.getAttribute("clientcode");
    String csFromInv = (String) getServletContext().getAttribute(GeneralConf.CSFROMINV);
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" />  <bean:message key="ecom.head.inv" bundle="<%=clientcode%>"/>  </div>

<div class="details_search page_bottom_content">
    <div class="card card-shadow">
        <div class="card-body">
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
    </div>
                                <div id="invSumydiv1">
            </div>
</div>

<script type="text/javascript">
    $('#invFromDate,#invToDate').datepicker({autoclose: true, clearBtn: true, todayHighlight: true, format: '<%=strDateFormat%>'});
    var newdate = new Date();
    newdate.setDate(newdate.getDate() - 90); // minus the date
    var fdate = newdate;
    var newdate1 = new Date();
    var tdate = newdate1;
    var frdate, todate;
    //showInitailInvDet();
    $(document).ready(function () {
            
                var serializedData = "";
                $("#invSumydiv1").html("");
                $('#invFromDate').datepicker("setDate", fdate);
                $('#invToDate').datepicker("setDate", tdate);
                frdate = $('#invFromDate').val();
                todate = $('#invToDate').val();
                //document.getElementsByName("invType")[1].checked = true;
                $('#invdate').prop('checked', true).trigger("onclick");
                openLoader();
                $.post("NewInvoiceAction.do?invType=D&invFromDate=" + frdate + "&invToDate=" + todate + "&invOption=CI&type=inv", serializedData, function (data) {
                    $("#invSumydiv1").html(data);
                    closeLoader();
                });

            
//            e.preventDefault()
//            $(this).tab('show')
       // });

        //validate
        
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
                $.post("NewInvoiceAction.do?invType=" + invType + "&invFromDate=" + fromDate + "&invToDate=" + toDate + "&invOption=" + option + "&type=inv", serializedData, function (data) {
                    $("#invSumydiv1").html(data);
                    closeLoader();
                });

                return false;
            }
        });

       
    });

    
    function searchTypeMethod(value, type) {
          if (value == 'A' && type == 'inv') {
            document.getElementById('invFromDate').value = "";
            document.getElementById('invToDate').value = "";
            if ($('#invall').prop('checked') == true) {
                $('#invdate').prop('checked', false);
                $('#invFromDate,#invToDate').attr("disabled", true);
            }
        } 
        else if (value == 'D' && type == 'inv') {
            if ($('#invdate').prop('checked') == true) {
                $('#invall').prop('checked', false);
                $('#invFromDate,#invToDate').attr("disabled", false);
            }

        }
    }
    function showInitailInvDet() {
        
        var serializedData = "";
        $('#invFromDate').datepicker("setDate", fdate);
        $('#invToDate').datepicker("setDate", tdate);
        frdate = $('#invFromDate').val();
        todate = $('#invToDate').val();
        document.getElementsByName("invType")[1].checked = true;
        $("#invSumydiv1").html("");
        $.post("NewInvoiceAction.do?invType=D&invFromDate=" + frdate + "&invToDate=" + todate + "&invOption=CA&type=inv", serializedData, function (data) {
            $("#invSumydiv1").html(data);
        });
    }
    </script>
