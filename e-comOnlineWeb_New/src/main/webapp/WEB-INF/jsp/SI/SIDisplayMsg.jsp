<%@ page contentType="text/html" pageEncoding="UTF-8"  language="java" import="net.ecom.vo.ReturnVO" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@page import="java.util.*,net.ecom.common.GeneralConf,net.ecom.common.Validation"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<div class="row">

    <div class="col-sm-2"> </div>
    <div class="col-sm-8">

        <div class="box_sess_clr"> 
            <div class=" text-center">

                <div> <img class="sub_icon_pages" src="Design/assets/images/page_icons/sub_page.svg" /> </div>
            </div>

            <div class="text-center" >
                <%
                    String insurancemsg = Validation.checkNull((String) request.getAttribute("InsuranceMSGMessage"));
                    String clientcode = (String) session.getAttribute("clientcode");
                    String restrictedFiled = Validation.checkNull((String) request.getAttribute("restrictedFile"));
                    String restrictedFiledSize = Validation.checkNull((String) request.getAttribute("restrictedFileSize"));
                    String fileSize = (String) getServletContext().getAttribute(GeneralConf.EcomFileUploadSize);
                    String enableAmendmentComMail = (String) session.getAttribute("ENABLE_SI_AMENDMENT_MAIL");
                    System.out.println("[ECOM-WAR] SIDisplayMsg enableAmendmentComMail=>"+enableAmendmentComMail);
                    ReturnVO returnVO = (ReturnVO) request.getAttribute("SI_RESPONSE_VO");
                     HashMap bookingCache =(HashMap)session.getAttribute("AMENDMENT_BOOKING_MAIL_CACHE");
                      String LRPBook_no="",EDIRead_ID="",SINo="",Type="",BLNo="",ToMailId="";
                      if(bookingCache!=null){
                      LRPBook_no=(String)bookingCache.get("LRPBookNo");
                      EDIRead_ID = (String) bookingCache.get("EDIReaderID");
                      SINo = (String) bookingCache.get("SI_No");
                      BLNo = (String) bookingCache.get("BL_No");
                      Type= (String)bookingCache.get("Type");
                      ToMailId= (String)bookingCache.get("ToMail");
                      }
                      System.out.println("[ECOM-WAR] SIDisplayMsg bookingCache===>"+bookingCache);
                    String strResponseMsg = "";
                    if (returnVO != null) {
                        strResponseMsg = returnVO.getStrDisplayMsg();


                %>
                <h3 style="color: #6C7077;font-size: 23px;"><%=strResponseMsg%></h3>

                <div style="font-size: 18px;"> <bean:message key="si.msg.gotosi" bundle="<%=clientcode%>"/> <a href="javascript:void(0)" onclick="backtosiview();" style="text-decoration: underline;">    <bean:message key="si.msg.gotoviewpg" bundle="<%=clientcode%>"/> </a> .</div>
                <%} else {%>
                <h3 style="color: #6C7077;font-size: 23px;"><bean:message key="ecom.msg.bl.error" bundle="<%=clientcode%>" /></h3>

                <div style="font-size: 18px;"> <bean:message key="si.msg.gotosi" bundle="<%=clientcode%>"/> <a href="javascript:void(0)" onclick="backtosiview();" style="text-decoration: underline;">    <bean:message key="si.msg.gotoviewpg" bundle="<%=clientcode%>"/> </a>   <bean:message key="si.msg.forlatestsi" bundle="<%=clientcode%>"/> </div>
                <%}%>
                <%if (restrictedFiled.length() > 0) {%>
                <div style="font-size: 18px;color: #6C7077;"><bean:message key='ebooking.msg.fileRestrict' bundle="<%=clientcode%>"/>(<%=restrictedFiled%>)</div>
                <%}%>
                <%if (restrictedFiledSize.length() > 0) {%>
                <div style="font-size: 18px;color: #6C7077;"><bean:message key='ebooking.msg.fileRestrictsize' bundle="<%=clientcode%>"/><%=fileSize%> (<%=restrictedFiledSize%>)</div>
                <%}%>
                <%if (insurancemsg.length() > 0) {%>
                <div style="font-size: 18px;color: #6C7077;" class="text-center"><%=insurancemsg%> </div>
                <%}%>

            </div>

        </div>

    </div>

    <div class="col-sm-2"> </div>

</div>
                <script>
    	   function sendAmendmentSIMail(book_no,ediread_id,SINo,BLNo,type) {
            $.ajax({
                type: "POST",
                cache: false,
                async: true,
                url: "AmendmentMailAjax?book_no=" + book_no + "&type="+type+"&EDIread_id=" + ediread_id+"&SINo="+SINo+"&BLNo="+BLNo,
                data: $(this).serializeArray(),
                success: function (data) {
                    var response = data;
                   
                },
                error: function (xhr) {
                    if (xhr.status == 403) {
                        window.location.href = "SessionExpired.jsp";
                    }
                }
            });
          }
           <%if("Y".equalsIgnoreCase(enableAmendmentComMail)&&"Y".equalsIgnoreCase(ToMailId)){%>
            sendAmendmentSIMail('<%=LRPBook_no%>','<%=EDIRead_ID%>','<%=SINo%>','<%=BLNo%>','<%=Type%>');
            <%}%>
</script>
<script type="text/javascript">
    function backtosiview() {
        location.href = "ShippingInstruction.do";
    }
</script>
