<%-- 
    Document   : Notification
    Created on : Nov 28, 2022, 4:38:20 PM
    Author     : gobinath.t
--%>

<%@page import="net.ecom.vo.NotificationVo,java.util.List,net.ecom.common.utils.DateConversion_New"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="customizer border-left-blue-grey border-left-lighten-4 d-xl-block notification_box" >
    <a class="customizer-close notificationbox_close  m-r-15" href="javascript:;">
        <i class="feather icon-x font-medium-3 fa fa-times"></i>
    </a>
    <!--<div class="customizer-content p-2 ps ps--active-y"></div>-->
    <div class="customizer-content p-2 ps ps--active-y overflow-auto">
        <div class="chat-sidebar">

            <div class="chat-sidebar-list-wrapper ps ps--active-y">

                <h4 class="px-2 pt-2 pb-25 mb-0" style="text-align: center !important;"><span class="notification_header_icon" ><i class="ficon feather icon-bell"></i></span> Notification</h4>
                <div class="container" id="EcomNotificationBox">
                    <%
                        int notificationcount = 0;
                        String activeClass = "";
                        List<NotificationVo> nolis = (List) session.getAttribute("ECOM_NOTIFICATION_DETAILS");
                        if (nolis != null && nolis.size() > 0) {
                            for (int i = 0; i < nolis.size(); i++) {
                                activeClass = "";
                                NotificationVo nvo = (NotificationVo) nolis.get(i);
                                if (!nvo.isIsviewed()) {
                                    notificationcount = notificationcount + 1;
                                    activeClass = "active";
                                }
                    %>
                    <div class="card <%=activeClass%>" id="<%=nvo.getMsgid()%>" <%if (!nvo.isIsviewed()) {%>onclick="onNotificationMessageViewAction('<%=nvo.getMsgid()%>')" <%}%> >
                        <div class="card-content">
                            <div class="card-body">
                                <p class="card-text"><%=nvo.getMessage()%></p>
                                <span class="float-right m-b-5 enotification_time" ><%=DateConversion_New.convertDefaultFormatwithTime(nvo.getNotificationdate())%></span>
                            </div>
                        </div>
                    </div>


                    <% }
                    } else {%>
                    <p class="text-center">No record available</p>
                    <%}%>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $('#notificationcount').text('<%=notificationcount%>');
</script>