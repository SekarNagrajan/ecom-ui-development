<%@page import="net.ecom.common.GeneralConf"%>
<%
    String Version = (String) getServletContext().getAttribute("Version");
    String footercopyright = (String) getServletContext().getAttribute("footercompanyname");
    String copyrightslink = (String) getServletContext().getAttribute(GeneralConf.AGENCYLINK);
    String carrierCode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String imprintLink =(String) getServletContext().getAttribute(GeneralConf.ImprintLink);
    String enable_TWImprint = (String) getServletContext().getAttribute(GeneralConf.Enable_TWImprint);
    String removeCopyrightsLink = (String) getServletContext().getAttribute(GeneralConf.REMOVE_COPYRIGHTS_LINK);
    String centralizeCopyrights = (String) getServletContext().getAttribute(GeneralConf.CENTRALIZE_COPYRIGHTS);
%>

<footer class="footer footer-static footer-light navbar-border">
    <p class="clearfix blue-grey lighten-2 text-sm-center mb-0 px-2">
        <%if(carrierCode != null && !carrierCode.equalsIgnoreCase("MIL")){%>
            <span class="float-md-left d-block d-md-inline-block"> Version:&nbsp;<%=Version%> </span> 
            <%}%>
            <span class="<%= "Y".equalsIgnoreCase(centralizeCopyrights) ? "" : "float-md-right " %>d-none d-lg-block">
            <%if("Y".equalsIgnoreCase(enable_TWImprint)){%>
                <a href="<%=imprintLink%>" target="_blank" style="color: white !important" class="btn btn-sm btn-primary mr-2">Imprint</a>
            <%}%>
            <%if("Y".equalsIgnoreCase(removeCopyrightsLink)){%>
                <span>Copyright &copy; <%=footercopyright%></span>
            <%}else{%>
                <a target="_blank" href="<%=copyrightslink%>">
                    <span>Copyright &copy; <%=footercopyright%></span></a>
            <%}%>
         </span>
    </p>
</footer>