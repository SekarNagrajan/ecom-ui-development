<%@ page contentType="text/html"  pageEncoding="UTF-8"  language="java" import="net.ecom.vo.ReturnVO" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@page import="java.util.*,net.ecom.common.GeneralConf,net.ecom.common.Validation"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script language="javascript">
    function backtosiview() {
        location.href = "ViewBookingAll.do?BookingRequestType=oblprint";
    }
</script>
  
<div class="row">

    <div class="col-sm-2"> </div>
    <div class="col-sm-8">

        <div class="box_sess_clr"> 
            <div class=" text-center">

                <div> <img class="sub_icon_pages" src="Design/assets/images/page_icons/sub_page.svg" /> </div>
            </div>

            <div class="text-center" >
                <% String insurancemsg = Validation.checkNull((String) request.getAttribute("InsuranceMSGMessage"));
                    String clientcode = (String) session.getAttribute("clientcode");
                    String restrictedFiled = Validation.checkNull((String) request.getAttribute("restrictedFile"));
                    String restrictedFiledSize = Validation.checkNull((String) request.getAttribute("restrictedFileSize"));
                    String fileSize = (String) getServletContext().getAttribute(GeneralConf.EcomFileUploadSize);
                    ReturnVO returnVO = (ReturnVO) request.getAttribute("SI_RESPONSE_VO");
                    String strResponseMsg = "";
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
                      System.out.println("[ECOM-WAR] BLDisplayMsg bookingCache===>"+bookingCache);
                    String enableAmendmentComMail = (String) session.getAttribute("ENABLE_BL_AMENDMENT_MAIL");
                    if (returnVO != null && (!"Bill Of Lading not saved.".equalsIgnoreCase(returnVO.getStrDisplayMsg()))) {
                        strResponseMsg = returnVO.getStrDisplayMsg();

                %>
                <h3 style="color: #6C7077;font-size: 23px;"><bean:message key="ecom.msg.bl" bundle="<%=clientcode%>" /></h3>

                <div style="font-size: 18px;"> <bean:message key="bl.msg.gotobl" bundle="<%=clientcode%>"/> <a href="javascript:void(0)" onclick="backtosiview();" style="text-decoration: underline;">  <bean:message key="si.msg.gotoviewpg" bundle="<%=clientcode%>"/> </a> .</div>
                <%} else if (returnVO != null && ("Bill Of Lading not saved.".equalsIgnoreCase(returnVO.getStrDisplayMsg()))) {%>

                <h3 style="color: #6C7077;font-size: 23px;"><%=returnVO.getStrDisplayMsg()%></h3>

                <div style="font-size: 18px;"> <bean:message key="bl.msg.gotobl" bundle="<%=clientcode%>"/> <a href="javascript:void(0)" onclick="backtosiview();" style="text-decoration: underline;">  <bean:message key="si.msg.gotoviewpg" bundle="<%=clientcode%>"/> </a> .</div>
                <%} else {%>
                <h3 style="color: #6C7077;font-size: 23px;"><bean:message key="ecom.msg.bl.error" bundle="<%=clientcode%>"/></h3>

                <div style="font-size: 18px;"> <bean:message key="bl.msg.gotobl" bundle="<%=clientcode%>"/> <a href="javascript:void(0)" onclick="backtosiview();" style="text-decoration: underline;">  <bean:message key="si.msg.gotoviewpg" bundle="<%=clientcode%>"/></a> <bean:message key=" bl.msg.latest" bundle="<%=clientcode%>"/></div>
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
    	   function sendAmendmentBLMail(book_no,ediread_id,SINo,BLNo,type) {
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
            sendAmendmentBLMail('<%=LRPBook_no%>','<%=EDIRead_ID%>','<%=SINo%>','<%=BLNo%>','<%=Type%>');
            <%}%>
         </script>