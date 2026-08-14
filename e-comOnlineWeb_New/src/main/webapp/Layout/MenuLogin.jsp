<%@page contentType="text/html" import="java.util.ArrayList,java.util.List,net.ecom.vo.RegisterMenuVO,net.ecom.common.GeneralConf,java.util.Map"%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@page pageEncoding="UTF-8"%>

<%
    System.out.println("[ECOM-WAR] MenuLogin  Inside MneuLogin================> ");
    long jsdate = new java.util.Date().getTime();
    String imagepath = (String) getServletContext().getAttribute("Imagepath");
    String logo_name = (String) getServletContext().getAttribute(GeneralConf.Logo_Name);
    String logo = imagepath + logo_name;

    ArrayList RegMenus = (ArrayList) session.getAttribute("RegMenus"); // for user login
    String user = (String) session.getAttribute("sessionAdmin");
    String clientcode = (String) session.getAttribute("clientcode");
    RegisterMenuVO registerVo = null;
    String menuDisable = "", secureLink = "";
    ArrayList submenulis = null;
    String logintype = (String) session.getAttribute("SESSION_LOGIN_TYPE");
    ArrayList mainmenu = null;
    Map submenu = null;
    if ("U".equalsIgnoreCase(logintype) || "Y".equalsIgnoreCase(user)) {
        mainmenu = (ArrayList) getServletContext().getAttribute("usermenu");
        submenu = (Map) getServletContext().getAttribute("usersubmenu");
    } else {
        mainmenu = (ArrayList) session.getAttribute("VENDOR_MAIN_MENU");
        submenu = (Map) session.getAttribute("VENDOR_SUB_MENU");
    }
    boolean ishavesubmenu = false;
%>
<ul class="navigation navigation-main" id="main-menu-navigation" data-menu="menu-navigation">

    <%
        for (int i = 0; i < mainmenu.size(); i++) {
            registerVo = (RegisterMenuVO) mainmenu.get(i);
            menuDisable = "disabled";
            secureLink = "href='#'";
            if ((RegMenus != null && RegMenus.contains(registerVo.getRefNo())) || "D".equalsIgnoreCase(registerVo.getCategory()) || "Y".equalsIgnoreCase(user)) {
                menuDisable = "";
                secureLink = registerVo.getAttrValue();
            }
            ishavesubmenu = false;
            if (submenu != null && submenu.get(registerVo.getRefNo()) != null) {
                submenulis = (ArrayList) submenu.get(registerVo.getRefNo());
                ishavesubmenu = submenulis.size() > 0 ? true : false;
            }

    %>
    <li class="nav-item <%=menuDisable%>"> 
        <a <%=ishavesubmenu ? "href='#'" : secureLink%> > <i class="<%=registerVo.getClassValue()%>"></i> <span class="menu-title" data-i18n=""> <bean:message key="<%=registerVo.getLabelValue()%>" bundle="<%=clientcode%>"/> </span> </a>  
        <%if (ishavesubmenu) {%>
        <ul class="menu-content">
            <% 
                for (int j = 0; j < submenulis.size(); j++) {
                    registerVo = (RegisterMenuVO) submenulis.get(j);
                    menuDisable = "class='disabled'";
                    secureLink = "href='#'";
                    if ((RegMenus != null && RegMenus.contains(registerVo.getRefNo())) || "D".equalsIgnoreCase(registerVo.getCategory()) || "Y".equalsIgnoreCase(user)) {
                        menuDisable = "";
                        secureLink=registerVo.getAttrValue();                        
                    }
            %>
            <li <%=menuDisable%> ><a class="menu-item" <%=secureLink%> > <i class="<%=registerVo.getClassValue()%>"></i> <bean:message key="<%=registerVo.getLabelValue()%>" bundle="<%=clientcode%>"/> </a> </li>
                <%}%>
        </ul>
        <%}%>

    </li>
    <%}%>
</ul>

<script>
    function opencargoprotect() {
        var url = '/cargoprotect';
    <%  String SelectedLanguage = (String) session.getAttribute("SelectedLanguage");
        if ("CH".equalsIgnoreCase(SelectedLanguage)) { %>
        window.open(url + "/CH", "_blank");
    <%} else {%>
        window.open(url, "_blank");

    <%}%>

    }
    function openinsurancefaq(url) {
        var url = '/cargoprotect/FAQ';
    <%
        if ("CH".equalsIgnoreCase(SelectedLanguage)) { %>
        window.open(url + "?type=CH", "_blank");
    <%} else {%>
        window.open(url, "_blank");

    <%}%>
    }
</script>    
