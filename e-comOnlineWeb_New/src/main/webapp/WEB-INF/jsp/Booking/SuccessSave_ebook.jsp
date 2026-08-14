<%@page import="net.ecom.common.GeneralConf, java.util.HashMap"%>
<%@page contentType="text/html"  pageEncoding="UTF-8" import="net.ecom.vo.ReturnVO,net.ecom.common.Validation"%>
<!-- booking Submitted msg start  -->

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%   String clientcode = (String) session.getAttribute("clientcode");
    String yemenBookingMsg = Validation.checkNull((String) request.getAttribute("yemenBookingMsg"));
    String insurancemsg = Validation.checkNull((String) request.getAttribute("InsuranceMSGMessage"));
    String restrictedFiled = Validation.checkNull((String) request.getAttribute("restrictedFile"));
    String restrictedFiledSize = Validation.checkNull((String) request.getAttribute("restrictedFileSize"));
    String fileSize = (String) getServletContext().getAttribute(GeneralConf.EcomFileUploadSize);
     String enableAmendmentComMail = (String) session.getAttribute("ENABLEAMENDMENTCOMMAIL");
     System.out.println("[ECOM-WAR] SuccessSave_ebook enableAmendmentComMail=>"+enableAmendmentComMail);
     HashMap bookingCache =(HashMap)session.getAttribute("AMENDMENT_BOOKING_MAIL_CACHE");
     String LRPBook_no="",EDIRead_ID="",EcomBookNo="",Type="",ToMailID="";
     if(bookingCache!=null){
      LRPBook_no=(String)bookingCache.get("LRPBookNo");
       EDIRead_ID = (String) bookingCache.get("EDIReaderID");
       EcomBookNo = (String) bookingCache.get("EcomBookNo");
      Type= (String)bookingCache.get("Type");
      ToMailID= (String)bookingCache.get("ToMail");
      }
%>
<script>
    	   function sendAmendmentMail(book_no,ediread_id,EcomBookNo,type) {
            $.ajax({
                type: "POST",
                cache: false,
                async: true,
                url: "AmendmentMailAjax?book_no=" + book_no + "&type="+type+"&EDIread_id=" + ediread_id+"&EcomBookNo="+EcomBookNo,
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
           <%if("Y".equalsIgnoreCase(enableAmendmentComMail)&&"Y".equalsIgnoreCase(ToMailID)){%>
            sendAmendmentMail('<%=LRPBook_no%>','<%=EDIRead_ID%>','<%=EcomBookNo%>','<%=Type%>');
            <%}%>
</script>
<div class="row">

    <div class="col-sm-2"> </div>
    <div class="col-sm-8">

        <div class="box_sess_clr" <%if(yemenBookingMsg.length() > 0){%> style="margin-top: 13% !important;" <%}%>>
            <div class=" text-center">

                <div> <img class="sub_icon_pages" src="Design/assets/images/page_icons/sub_page.svg" /> </div>
            </div>

            <div class="text-center" >
                <%
                    ReturnVO rvo = (ReturnVO) request.getAttribute("SaveResult");
                    if (rvo != null) {
                %>
                <%
                    if ("S".equalsIgnoreCase(rvo.getStrSaveType())) {
                %>
                <%  if (yemenBookingMsg.length() > 0) { %>
                <%=yemenBookingMsg%>
                <%  } else { %>
                <h3 style="color: #6C7077;font-size: 23px;"><%=rvo.getStrDisplayMsg()%></h3>
                <%  } %>

                <%
                } else if ("D".equalsIgnoreCase(rvo.getStrSaveType())) {
                %>
                <h3 style="color: #6C7077;font-size: 23px;"><%=rvo.getStrDisplayMsg()%></h3>

                <% }%>

                <div style="font-size: <%=yemenBookingMsg.length() > 0 ? "15px" : "18px"%>;"><bean:message key='ebooking.msg.sucbkgview' bundle="<%=clientcode%>"/> <a href="javascript:void(0)" onclick="backtobookview();" style="text-decoration: underline;">  <bean:message key='ebooking.msg.sucbkgpage' bundle="<%=clientcode%>"/>  </a> </div>
                <%if (restrictedFiled.length() > 0) {%>
                <div style="font-size: 18px;color: #6C7077;"><bean:message key='ebooking.msg.fileRestrict' bundle="<%=clientcode%>"/>(<%=restrictedFiled%>)</div> 
                <%}%>
                <%if (restrictedFiledSize.length() > 0) {%>
                <div style="font-size: 18px;color: #6C7077;"><bean:message key='ebooking.msg.fileRestrictsize' bundle="<%=clientcode%>"/><%=fileSize%> (<%=restrictedFiledSize%>)</div>
                <%}%>
                <%} else { %>
                <script type="text/javascript">
                    location.href = "ViewBookingAll.do?BookingRequestType=ViewBookingAll";
                </script>
                <%}%>
                <%if (insurancemsg.length() > 0) {%>
                <div style="font-size: 18px;color: #6C7077;" class="text-center"><%=insurancemsg%> </div>
                <%}%>
            </div>

        </div>

    </div>

    <div class="col-sm-2"> </div>

</div>

<!-- booking Submitted msg end  -->
<script language="javascript" type="text/javascript">
    function backtobookview() {
        location.href = "ViewBookingAll.do?BookingRequestType=ViewBookingAll";
    }
</script>

