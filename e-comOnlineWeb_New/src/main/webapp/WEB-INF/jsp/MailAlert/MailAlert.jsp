<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.List,net.ecom.vo.MailAlertVO" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%
    String successmsg = (String) request.getAttribute("SUCCESSMAILALERTRESULT");
    String clientcode = (String) session.getAttribute("clientcode");

%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> <bean:message key="ecom.myalerts" bundle="<%=clientcode%>" /></div>
<div class="card card-shadow page_bottom_content">
    <div class="details_search">
        <html:form method="post" action="SaveMailAlert.do" styleId="MailAlertForm" styleClass="form-horizontal">

            <div class="col-lg-12 text-center mt-3">
                <div class="form-group alert-error" style="display:none" id='alertResetSuccess'>
                    <div class="alert alert-success alert-dismissible" role="alert" >
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <strong><%=successmsg%></strong> 
                    </div>
                </div>
            </div>
            <input type="hidden" name="checkid" value="checkval">
            <div class="col-sm-12">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th><bean:message key="ebooking.trust.Sno" bundle="<%=clientcode%>" /></th>
                                    <th><bean:message key="ecom.myalerts.Module" bundle="<%=clientcode%>" /></th>
                                    <th><bean:message key="ecom.myalerts.Activity" bundle="<%=clientcode%>" /></th>
                                    <th><bean:message key="ecom.myalerts.MailAlert" bundle="<%=clientcode%>" /></th>

                            </tr>
                        </thead>
                        <tbody >
                            <%
                                String user = (String) session.getAttribute("sessionAdmin");
                                List<MailAlertVO> listAlertVO = (List<MailAlertVO>) request.getAttribute("MAILALERTVIEWDETAILS");
                                MailAlertVO alertVO = null;
                                if (listAlertVO != null && listAlertVO.size() > 0) {
                                    for (int i = 0; i < listAlertVO.size(); i++) {
                                        alertVO = listAlertVO.get(i);

                            %>
                            <tr>
                                <td><%=(i + 1)%></td>
                                <td><%=alertVO.getModule()%></td>
                                <td><%=alertVO.getActivity()%></td>
                                <td>
                                    <div class="skin skin-flat">
                                        <input type="checkbox" name="mailalert" onclick="setHiddentAlertVal(this, 'hidalervalue<%=i%>')" id="mailalertid<%=i%>" value="<%=alertVO.getMailAlert()%>" <%=("Y".equalsIgnoreCase(alertVO.getMailAlert()) ? "checked" : "")%> />
                                    </div>
                                    <input type="hidden" name="hidmailalertcode" value="<%=alertVO.getActivityCode()%>">
                                    <input type="hidden" name="hidmailalertvalue" value="<%=alertVO.getMailAlert()%>" id="hidalervalue<%=i%>">
                                    <input type="hidden" name="mailalreadyexist" value="<%=alertVO.getAlreadyExist()%>">
                                </td>
                            </tr>
                            <%                                            }
                                }%>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-5 "></div>
                <div class="col-sm-2 ">
                    <button type="submit" class="btn btn-primary btn-lg btn_block_custom " onclick="openLoader();" id="submitbtn" <%="Y".equalsIgnoreCase(user) ? "disabled" : ""%>> <bean:message key="ecom.Save" bundle="<%=clientcode%>" /></button>
                </div>
            </div>
        </html:form>
    </div>
</div>


<script type="text/javascript">

    $(document).ready(function () {
        $("#MailAlertForm").validate({
            submitHandler: function (form) {
                $("input[name ='mailalert']").trigger('onclick');
                return true;
            }
        });
    });

    function setHiddentAlertVal(obj, id) {
        if ($(obj).prop('checked') == true) {
            $('#' + id).val("Y");
        } else {
            $('#' + id).val("N");
        }
    }
    <%if (successmsg != null && successmsg.length() > 0) {
            if (successmsg.contains("Failure")) {
    %>
    $('#alertResetSuccess').find('.alert').removeClass('alert-success');
    $('#alertResetSuccess').find('.alert').addClass('alert-danger');
    <%}%>
    $('#alertResetSuccess').show();
    <%}%>
</script>
