<%
    String clientcode = (String) session.getAttribute("clientcode");
    String message = (String) request.getAttribute("forwardmessage");
    String msgtype =(String) request.getAttribute("forwardmessagetype");
    String imgname ="";
    if("emailsent".equalsIgnoreCase(msgtype)){
        imgname="email_sent.svg";
    } else if ("success".equalsIgnoreCase(msgtype)){
        imgname="sub_page.svg";
    } else if ("duplicate".equalsIgnoreCase(msgtype)){ 
        imgname="email_sent.svg";
    }else {
        imgname="failure.svg";
    }
%>

<!-- Registration msg start  -->
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" />  <bean:message key="ecom.head.reg" bundle="<%=clientcode%>"/></div>
<div class="row">

    <div class="col-sm-2"> </div>
    <div class="col-sm-8">


        <div class="box_regi_clr"> 
            <div class=" text-center" style=" ">

                <div> <img class="sub_icon_pages" src="Design/assets/images/page_icons/<%=imgname%>" /> </div>
            </div> <br/>

            <div class="text-center" style=" ">

                <%=message%> 


            </div>

        </div>

    </div>

    <div class="col-sm-2"> </div>

</div>

<!--  Registration msg end  -->
