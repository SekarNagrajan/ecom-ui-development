<%@page import="net.ecom.common.GeneralConf, java.util.HashMap"%>
<%@page contentType="text/html"  pageEncoding="UTF-8" import="net.ecom.vo.ReturnVO,net.ecom.common.Validation"%>
<!-- booking Submitted msg start  -->

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%   String clientcode = (String) session.getAttribute("clientcode");
    String quotemsg = (String) request.getAttribute("quotemsg");
    String quoteNo = (String) request.getAttribute("quoteNo");
%>
<div class="row">
    <div class="col-sm-2"> </div>
    <div class="col-sm-8">
        <%if(quoteNo !=null && quoteNo.length()> 0){%>
            <div class="box_sess_clr">
                <div class=" text-center">

                    <div> <img class="sub_icon_pages" src="Design/assets/images/page_icons/sub_page.svg" /> </div>
                </div>
                <div class="text-center">
                    <h3 style="color: #6C7077;font-size: 23px;">Quotation Request <span class=""
                            style="color:#62be82; font-weight: bold;"> (<%=quoteNo%>) </span> has been submitted to agency.
                    </h3>


                </div>

            </div>
            <%} else {%>
                <div class="box_sess_clr">
                    <div class=" text-center"></div>

                    <div> <img class="sub_icon_pages" src="Design/assets/images/page_icons/failure.svg" /> </div>
                </div>
                <div class="text-center">
                    <h3 style="color: #6C7077;font-size: 23px;">Quotation Request has failed to submit to the agency.</h3>


                </div>

    </div>
    <%}%>
</div>

<div class="col-sm-2"> </div>

</div>

