<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="net.ecom.common.Validation,net.ecom.vo.*,java.util.ArrayList,java.util.List" errorPage="" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%
    ArrayList spllist = (ArrayList) session.getAttribute("SplPriviList");
    String custcode = (String) session.getAttribute("custcode");
    //System.out.println("[ECOM-WAR] SpecialPriviView cust code from strut session view ------ " + custcode);
    int tarsize = spllist.size();
    RegisterMenuVO registerVo = null;
    String menuId = "", name = "";
    String id = "", divid = "";
    int slno = 0;


%>

<div class="container-fluid">

    <div class="row">
        <div class="col-sm-6">

            <div class="card">
                <div class="card-body">
                    <h4 class="card-title"><span class="fa fa-sitemap"></span> Privileged</h4>

                    <%                               //System.out.println("[ECOM-WAR] SpecialPriviView list ------view--" + spllist);
                        String menuname = "";
                        if (spllist != null && spllist.size() > 0) {

                            for (int i = 0; i < tarsize; i++) {
                                slno++;
                                //System.out.println("[ECOM-WAR] SpecialPriviView size  " + tarsize);
                                registerVo = (RegisterMenuVO) spllist.get(i);
                                //System.out.println("[ECOM-WAR] SpecialPriviView Menu Category ----view--" + registerVo.getCategory());
                                //System.out.println("[ECOM-WAR] SpecialPriviView Menu Name ---view---" + registerVo.getMenuName());
                                menuId = Integer.toString(registerVo.getMenuId());
                                id = "privilege_" + i;
                                name = "privilege_" + i;
                                divid = "divid_" + i;
                                menuname = registerVo.getMenuName();
                                if ("OBLPrint".equalsIgnoreCase(menuname)) {
                                    menuname = "Bill of Lading";
                                }
                                if (registerVo.getCategory().equalsIgnoreCase("P")) {
                    %>

                    <div class="skin skin-flat">
                        <label class="col-sm-8 control-label" style="text-align: left;margin-left:30px">
                            <input type="checkbox" name="privilege" id="<%=registerVo.getCategory()%>" value="<%=menuId%>"/>
                            <div id="<%=divid%>" style="display:inline-block;"><%=menuname%></div> 
                        </label>
                    </div>

                    <%
                                }
                            }
                        }
                    %>


                    <div class="form-group row">
                        <div class="col-sm-5"></div>
                        <div class="col-sm-5">
                            <button type="submit" class="btn btn-success btn-lg" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="btnsearch" onclick="addPrivMenu();"> <span class="glyphicon glyphicon-search" ></span> Add</button>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <div class="col-sm-6">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title"><span class="fa fa-sitemap"></span> Default</h4>
                    <%
                        //System.out.println("[ECOM-WAR] SpecialPriviView ---view------" + spllist);
                        if (spllist != null && spllist.size() > 0) {
                            for (int j = 0; j < tarsize; j++) {
                                slno++;
                                registerVo = (RegisterMenuVO) spllist.get(j);
                                //System.out.println("[ECOM-WAR] SpecialPriviView Menu Category --view----" + registerVo.getCategory());
                                //System.out.println("[ECOM-WAR] SpecialPriviView Menu Name ---view---" + registerVo.getMenuName());
                                menuId = Integer.toString(registerVo.getMenuId());

                                id = "default_" + j;
                                name = "default_" + j;
                                //System.out.println("[ECOM-WAR] SpecialPriviView j value---view--" + id);
                                menuname = registerVo.getMenuName();
                                if ("OBLPrint".equalsIgnoreCase(menuname)) {
                                    menuname = "Bill of Lading";
                                }
                                if (registerVo.getCategory().equalsIgnoreCase("D") || registerVo.getCategory().equalsIgnoreCase("NP")) {
                                    if (registerVo.getCategory().equalsIgnoreCase("D")) {
                    %>

                    <div class="skin skin-flat"><label class="col-sm-8 control-label" style="text-align: left;margin-left:30px"> <input type="checkbox" name="default" id="<%=registerVo.getCategory()%>" disabled="disabled" value="<%=menuId%>"/> 
                            <%=menuname%> </label> </div> 
                            <%
                            } else if (registerVo.getCategory().equalsIgnoreCase("NP")) {
                            %>
                    <div class="skin skin-flat"> <label class="col-sm-8 control-label" style="text-align: left;margin-left:30px"> <input type="checkbox" name="newprivi" id="<%=registerVo.getCategory()%>" value="<%=menuId%>"/> 
                            <%=menuname%> </label></div> 

                    <%
                                    }
                                }
                            }
                        }
                    %>


                    <div class="form-group row">
                        <div class="col-sm-5"></div>
                        <div class="col-sm-5">
                            <button type="submit" class="btn btn-danger btn-lg" data-loading-text="<i class='fa fa-spinner fa-spin'></i> Loading..." autocomplete="off" id="btnsearch" onclick="removePrivMenu();"> <span class="glyphicon glyphicon-search" ></span> Remove</button>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="Design/app-assets/js/scripts/forms/checkbox-radio.min.js"></script>



