<%-- 
    Document   : InsuranceSurvey
    Created on : Jan 5, 2022, 10:55:27 AM
    Author     : gobinath.t
--%>

<%@page import="net.ecom.common.Validation,java.util.Map,java.util.Iterator,net.ecom.vo.SurveyList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%
    String clientcode = (String) session.getAttribute("clientcode");
    String quoteno = Validation.checkNull((String) request.getParameter("quoteno"));
    String bookno = Validation.checkNull((String) request.getParameter("bookno"));
    String SelectedLanguage = (String) session.getAttribute("SelectedLanguage");
    Map Insurance_Survey_List = null;
    if ("CH".equalsIgnoreCase(SelectedLanguage)) {
        Insurance_Survey_List = (Map) getServletContext().getAttribute("Insurance_Survey_List_Lang");
    } else {
        Insurance_Survey_List = (Map) getServletContext().getAttribute("Insurance_Survey_List");
    }

%>
<div id="ecom_insurance_survey">
    <div class="card">
        <div class="card-header" style="box-shadow: 0px 2px 3px 0px #e0e0e0;height: 62px;background-color: white;">
            <div class="form-group row">
                <div class="col-sm-4 float-left">
                    <img class="brand-logo" alt="logo" src="/Ecommaster/SwissRe_Logo.png">
                </div>
                <div class="col-sm-8 float-right">
                    <h3 class="col-sm-11 text-right" style="margin-right: 20px;color: #00bcd4;margin-top: 8px;"><b> <bean:message key="ecom.insurance.spot.voyage" bundle="<%=clientcode%>" />  </b></h3>
                </div>
            </div>
        </div>
        <div class="card-body" style="font-size: 18px;">
            <div class="row">
                <div class="col-sm-12">
                    <input type="hidden" id="surveyquoteno" value="<%=quoteno%>">
                    <input type="hidden" id="surveybook_no" value="<%=bookno%>">
                    <div class="row col-sm-12" style="border-bottom: 1px solid #FFC107 !important; margin-bottom: 8px;"><h4><bean:message key="ecom.insurance.survey.text1" bundle="<%=clientcode%>" /></h4></div>
                    <div class="row col-sm-12"><bean:message key="ecom.insurance.survey.text2" bundle="<%=clientcode%>" /></div>
                    <div class="row col-sm-12"><bean:message key="ecom.insurance.survey.text3" bundle="<%=clientcode%>" /></div>
                    <div class="row col-sm-12"><bean:message key="ecom.insurance.survey.text4" bundle="<%=clientcode%>" /></div>

                    <div class="check-list">

                        <%
                            Iterator it = Insurance_Survey_List.entrySet().iterator();       //keyset is a method 
                            SurveyList surveyobj = null;
                            while (it.hasNext()) {
                                Map.Entry pairs = (Map.Entry) it.next();
                                surveyobj = (SurveyList) pairs.getValue();

                        %>
                        <div class="row col-sm-12"><input type="radio" name="insurance_survey" style="width: 18px;height: 18px;" value="<%=surveyobj.getSurveyOptionCode()%>" /> <span style="margin-top: 3px;"> <%=surveyobj.getSurveyOptionValue()%></span></div>
                            <%}%>
                        <div class="row col-sm-12 enable_manualoption" style="display: none;"> <bean:message key="ecom.insurance.survey.text5" bundle="<%=clientcode%>" /></div>
                        <div class="row col-sm-6 enable_manualoption" style="display: none;"><input type="text" class="form-control light_border" name="insurance_survey_input" id="insurance_survey_input"/> </div>
                    </div>


                    <div class="row col-sm-12"><input type="checkbox" name="insurance_survey_improve_msg" id="insurance_survey_improve_msg" value="Y" style="width: 18px;height: 18px;"/> <span style="line-height: 15px;margin-left: 8px; margin-bottom: 14px;margin-top: 3px;"><bean:message key="ecom.insurance.survey.text6" bundle="<%=clientcode%>" /></span></div>
                    <div class="row offset-md-9"><button type="button" class="btn btn-info" id="surveysubmit" onclick="validateSurvey($('#surveyquoteno').val());" ><bean:message key="ecom.submit" bundle="<%=clientcode%>" /></button> </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $("input[name='insurance_survey']").change(function (e) {
        if ($(this).val() == 'CUSTOM_FEEDBACK') {
            $('.enable_manualoption').show();
        } else {
            $('.enable_manualoption').hide();
            $('#insurance_survey_input').val('');
        }
    });
</script>