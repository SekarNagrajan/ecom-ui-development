/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomAdminCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EcomPhase2BeanRemote;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.common.CryptoLibrary;
import net.ecom.common.Currencyformat;
import net.ecom.common.GeneralConf;
import net.ecom.common.LabelBeanConversion;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.formbeans.LoginForm;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.DropDownVO;
import net.ecom.vo.RegisterMenuVO;
import net.ecom.vo.UnclearedCargoVo;
import net.ecom.vo.UserData;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AdminAccess extends org.apache.struts.action.Action {

    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomRegistrationBeanRemote ecomreg = EJBLookUp.getRegistrationBeanLookUp();
    EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
    EcomAdminCntrlBeanRemote adminBeanObj = EJBLookUp.getAdminBeanLookUp();
    EcomPhase2BeanRemote phase2bean = EJBLookUp.getPhase2BeanLookUp();
    Validation validate = new Validation();
    CryptoLibrary crypt = new CryptoLibrary();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String forward = "invalid";
        try {
            PrintWriter out = response.getWriter();
            HttpSession session = request.getSession();
            ServletContext contexts = servlet.getServletContext();
            String v1DataEnableStatus = (String) contexts.getAttribute(GeneralConf.V1DATAENABLESTATUS);
            String custTypeConfig = Validation.checkNull((String) contexts.getAttribute(GeneralConf.APBPCONFIGBACKEND));
            String Dash_display = (String) request.getServletContext().getAttribute(GeneralConf.DASHBOARD_DISPLAY);
            String csfrominv = (String) request.getServletContext().getAttribute(GeneralConf.CSFROMINV);
            String custstmtsp = (String) request.getServletContext().getAttribute(GeneralConf.USE_CUSTOMETSTATEMENT_SP);
            String product_Dashboard = (String) request.getServletContext()
                    .getAttribute(GeneralConf.Enable_Product_Dashboard);
            String Enable_Dashboard_Filter = (String) request.getServletContext()
                    .getAttribute(GeneralConf.Enable_DashBoard_Filter);
            String type = request.getParameter("type");
            System.out.println("[ECOM-WAR] AdminAccess     type==>" + type);
            ArrayList templateno = new ArrayList();
            ArrayList backuptemplateno = new ArrayList();
            ArrayList tempmangetemplate = new ArrayList();
            StringBuffer mailtem = null;
            if ("emailtemplate".equalsIgnoreCase(type)) {
                session.setAttribute("IS_ACTIVEPAGE", "emailtemplate");
                forward = "emailtemplate";
                session.setAttribute("NEWDESIGNPRODUCT", "emailtemplate");
            } else if ("agencylogin".equalsIgnoreCase(type)) {
                LoginForm loginObj = (LoginForm) form;
                String loginName = loginObj.getUserName();
                loginName = validate.checkNull(loginName);
                String password = loginObj.getPassword();
                password = validate.checkNull(password);
                ArrayList userDataObj = (ArrayList) ecomCntrlObj.getAgencyLoginDetails(loginName, password);
                String usertype = (String) userDataObj.get(1);
                String userName = (String) userDataObj.get(0);
                if ("A".equalsIgnoreCase(usertype)) {
                    session.setAttribute("AdminAgency", "AdminLoing");
                    session.setAttribute("SESSION_USERNAME", userName);
                    session.setAttribute("IS_ACTIVEPAGE", "adminreg");

                    forward = "Agencylogin";
                } else if ("V".equalsIgnoreCase(usertype)) {
                    session.setAttribute("AdminAgency", "VendorLogin");
                } else {
                    forward = "Agencyloginfailed";
                    session.setAttribute("LOGINRESPONSE_DATA", "Invalid Username / Password");
                }
            } else if ("prvView".equalsIgnoreCase(type)) {
                List RegisterMenuList = null;
                String Message = " ";
                String usertype = request.getParameter("usertype");

                if (usertype.equalsIgnoreCase("user")) {
                    usertype = "U";
                } else if (usertype.equalsIgnoreCase("Vendor")) {
                    usertype = "V";
                }
                RegisterMenuList = ecomCntrlObj.getRegisteredMenus(Message, usertype);
                request.setAttribute("RegisterMenuList", RegisterMenuList);
                forward = "loadvalue";

            } else if ("prvSave".equalsIgnoreCase(type)) {
                List RegisterMenuList = null;
                String Message = " ";
                Validation validateObj = new Validation();
                String MenuName = request.getParameter("menuname");
                MenuName = validateObj.checkNull(MenuName);

                String DevelopedBy = request.getParameter("developedby");
                DevelopedBy = validateObj.checkNull(DevelopedBy);

                String Status = request.getParameter("status");
                Status = validateObj.checkNull(Status);

                String Date = request.getParameter("date");
                Date = validateObj.checkNull(Date);

                String RefNo = request.getParameter("refno");
                RefNo = validateObj.checkNull(RefNo);

                String CreatedBy = request.getParameter("createdby");
                CreatedBy = validateObj.checkNull(CreatedBy);

                String Category = request.getParameter("category");
                Category = validateObj.checkNull(Category);
                String usertype = request.getParameter("usertype");
                usertype = validateObj.checkNull(usertype);

                String classvalue = validateObj.checkNull(request.getParameter("classvalue"));
                String attrvalue = validateObj.checkNull(request.getParameter("attrvalue"));
                String labelvalue = validateObj.checkNull(request.getParameter("labelvalue"));
                String menutype = validateObj.checkNull(request.getParameter("menutype"));
                String parentmenu = validateObj.checkNull(request.getParameter("parentmenu"));

                if (usertype.equalsIgnoreCase("user")) {
                    usertype = "U";
                } else if (usertype.equalsIgnoreCase("Vendor")) {
                    usertype = "V";
                }
                String menuorder = request.getParameter("menuOrder");
                menuorder = validateObj.checkNull(menuorder);
                RegisterMenuList = ecomCntrlObj.saveRegisteredMenu(MenuName, DevelopedBy, Status, Date, RefNo,
                        CreatedBy, Category, usertype, menuorder, classvalue, attrvalue, labelvalue, menutype,
                        parentmenu);
                request.setAttribute("RegisterMenuList", RegisterMenuList);
                forward = "loadvaluesave";

            } else if ("prvEdit".equalsIgnoreCase(type)) {

                List RegisterMenuList = null;
                String Message = " ";

                Validation validateObj = new Validation();
                String MenuName = request.getParameter("menuname");
                MenuName = validateObj.checkNull(MenuName);

                String DevelopedBy = request.getParameter("developedby");
                DevelopedBy = validateObj.checkNull(DevelopedBy);

                String Status = request.getParameter("status");
                Status = validateObj.checkNull(Status);

                String Date = request.getParameter("date");
                Date = validateObj.checkNull(Date);

                String RefNo = request.getParameter("refno");
                RefNo = validateObj.checkNull(RefNo);

                String CreatedBy = request.getParameter("createdby");
                CreatedBy = validateObj.checkNull(CreatedBy);

                String Category = request.getParameter("category");
                Category = validateObj.checkNull(Category);
                String usertype = request.getParameter("usertype");
                usertype = validateObj.checkNull(usertype);
                if (usertype.equalsIgnoreCase("user")) {
                    usertype = "U";
                } else if (usertype.equalsIgnoreCase("Vendor")) {
                    usertype = "V";
                }
                String MenuId = (request.getParameter("menuid"));
                MenuId = validateObj.checkNull(MenuId);

                String classvalue = validateObj.checkNull(request.getParameter("classvalue"));
                String attrvalue = validateObj.checkNull(request.getParameter("attrvalue"));
                String labelvalue = validateObj.checkNull(request.getParameter("labelvalue"));
                String menutype = validateObj.checkNull(request.getParameter("menutype"));
                String parentmenu = validateObj.checkNull(request.getParameter("parentmenu"));

                int Menuid = 0;
                if (MenuId != "" && MenuId.trim().length() > 0) {
                    Menuid = Integer.parseInt(MenuId);
                }
                String menuorder = request.getParameter("menuOrder");
                menuorder = validateObj.checkNull(menuorder);
                RegisterMenuList = ecomCntrlObj.editRegisteredMenu(MenuName, DevelopedBy, Status, Date, RefNo,
                        CreatedBy, Category, Menuid, usertype, menuorder, classvalue, attrvalue, labelvalue, menutype,
                        parentmenu);
                request.setAttribute("RegisterMenuList", RegisterMenuList);
                forward = "loadvalueedit";

            } else if ("prvdelete".equalsIgnoreCase(type)) {
                List RegisterMenuList = null;
                String Message = " ";
                Validation validateObj = new Validation();
                String MenuId = (request.getParameter("menuid"));
                MenuId = validateObj.checkNull(MenuId);
                int Menuid = 0;
                if (MenuId != "" && MenuId.trim().length() > 0) {
                    Menuid = Integer.parseInt(MenuId);
                }
                String usertype = request.getParameter("usertype");
                usertype = validateObj.checkNull(usertype);
                if (usertype.equalsIgnoreCase("user")) {
                    usertype = "U";
                } else if (usertype.equalsIgnoreCase("Vendor")) {
                    usertype = "V";
                }
                RegisterMenuList = ecomCntrlObj.deleteRegisteredMenu(Menuid, usertype);
                request.setAttribute("RegisterMenuList", RegisterMenuList);
                forward = "loadvaluedelete";
            } else if ("IPfilter".equalsIgnoreCase(type)) {
                List alsIPFilter = ecomCntrlObj.getIPBlockedData();
                session.setAttribute("IS_ACTIVEPAGE", "IPfiter");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("IPFilteredData", alsIPFilter);
                forward = "IPFilter";
            } else if ("SaveIPfilter".equalsIgnoreCase(type)) {
                String strcarname = request.getParameter("Hiddencarrier");
                String savestate = request.getParameter("Hiddensavestate");
                String blockchar = request.getParameter("HiddenRange");
                String blockWords = request.getParameter("HiddenblockIP");
                String strSaveRes = ecomCntrlObj.saveIPblock(strcarname, blockWords, blockchar, savestate);
                List alsIPFilter = ecomCntrlObj.getIPBlockedData();
                session.setAttribute("IS_ACTIVEPAGE", "IPfiter");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("IPFilteredData", alsIPFilter);
                forward = "IPFilterView";
            } else if ("SavechangeIPStatus".equalsIgnoreCase(type)) {
                String strcarriername = request.getParameter("carriername");
                String strchangestatus = request.getParameter("Chkstatus");
                String strStatechange = ecomCntrlObj.SavechangeIPStatus(strcarriername, strchangestatus);
                List alsIPFilter = ecomCntrlObj.getIPBlockedData();
                session.setAttribute("IS_ACTIVEPAGE", "IPfiter");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("IPFilteredData", alsIPFilter);
                // forward = "IPFilter";
                forward = "IPFilterView";

            } else if ("adminreset".equalsIgnoreCase(type)) {
                ArrayList countryListObj = LabelBeanConversion
                        .labelListConversion(ecomCntrlObj.getCountryNameandCode());
                Hashtable authObj = ecomCntrlObj.getAuthenticationMail();
                // session.setAttribute("CAPIMAGEURL", "ecomonline");

                session.setAttribute("SESSION_AUTHENTICATION", authObj);
                session.setAttribute("COUNTRYNAME_COUNTRYCODE", countryListObj);
                forward = "resetadminpass";
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("IS_ACTIVEPAGE", "resetadminpassword");
                session.setAttribute("NEWDESIGNPRODUCT", "adminpwdreset");
                session.setAttribute("IS_VIA_LOGIN", "NO");
                request.setAttribute("IS_REGISTRATION", "YES");
                session.setAttribute("systemid", session.getId()); // session Id

            } else if (type.equalsIgnoreCase("registermenu")) {
                forward = "registerMenu";
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim()
                        : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";

                } else {
                    session.setAttribute("systemid", session.getId()); // session Id
                }
                session.setAttribute("IS_MENU_LOCK", strMenu);
                session.setAttribute("IS_ACTIVEPAGE", "adminreg");
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);

            } else if ("GlobalConfig".equalsIgnoreCase(type)) {
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim()
                        : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";

                } else {
                    session.setAttribute("systemid", session.getId()); // session Id
                }
                session.setAttribute("IS_ACTIVEPAGE", "GlobalConfig");
                session.setAttribute("adminmenupage", "cpanelmenupage");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                forward = "globalConfig";
            } else if ("V1V2ServiceConfig".equalsIgnoreCase(type)) {
                System.out.println("[ECOM-WAR] AdminAccess     >>V1V2ServiceConfig>>");
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim()
                        : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";

                } else {
                    session.setAttribute("systemid", session.getId()); // session Id
                }
                session.setAttribute("IS_ACTIVEPAGE", "V1V2ServiceConfig");
                session.setAttribute("adminmenupage", "cpanelmenupage");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                session.setAttribute("adminv1v2service", "no");
                forward = "v1v2serviceconfig";
            } else if ("adminv1v2serviceconfig".equalsIgnoreCase(type)) {
                // System.out.println("[ECOM-WAR] AdminAccess >>admin V1V2ServiceConfig>>");
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim()
                        : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";

                } else {
                    session.setAttribute("systemid", session.getId()); // session Id
                }
                session.setAttribute("IS_ACTIVEPAGE", "V1V2ServiceConfig");
                session.setAttribute("NEWDESIGNPRODUCT", "V1V2ServiceConfig");
                session.setAttribute("adminv1v2service", "yes");
                // session.setAttribute("adminmenupage", "cpanelmenupage");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                forward = "adminv1v2serviceconfig";
            } else if ("globalConfigAdmin".equalsIgnoreCase(type)) {
                // List alsSqlFilter = ecomCntrlObj.getSQLBlockedData();
                // System.out.println("[ECOM-WAR] AdminAccess this is global
                // configuration........");
                session.setAttribute("IS_ACTIVEPAGE", "globalconfig");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("NEWDESIGNPRODUCT", "globalconfig");
                // session.setAttribute("SQLFilteredData", alsSqlFilter);
                forward = "successconfigscreen";
                session.setAttribute("adminmenupage", "loginmenupage");

            } else if ("cutoffconfig".equalsIgnoreCase(type)) {
                // List alsSqlFilter = ecomCntrlObj.getSQLBlockedData();
                // System.out.println("[ECOM-WAR] AdminAccess this is global
                // configuration........");
                session.setAttribute("IS_ACTIVEPAGE", "cutoffconfig");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("NEWDESIGNPRODUCT", "cutoffconfig");
                // session.setAttribute("SQLFilteredData", alsSqlFilter);
                forward = "successcutoffconfig";
                session.setAttribute("adminmenupage", "loginmenupage");
            } else if ("fieldconfig".equalsIgnoreCase(type)) {
                // List alsSqlFilter = ecomCntrlObj.getSQLBlockedData();
                // System.out.println("[ECOM-WAR] AdminAccess this is global
                // configuration........");
                session.setAttribute("IS_ACTIVEPAGE", "fieldconfig");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("NEWDESIGNPRODUCT", "fieldconfig");
                // session.setAttribute("SQLFilteredData", alsSqlFilter);
                forward = "successfieldconfig";
                session.setAttribute("adminmenupage", "loginmenupage");
            } else if ("svrcrestrict".equalsIgnoreCase(type)) {
                // System.out.println("[ECOM-WAR] AdminAccess svrcrestrict>>");
                // List alsSqlFilter = ecomCntrlObj.getSQLBlockedData();
                // System.out.println("[ECOM-WAR] AdminAccess this is global
                // configuration........");
                session.setAttribute("IS_ACTIVEPAGE", "srvcrestriction");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("NEWDESIGNPRODUCT", "srvcrestriction");
                // session.setAttribute("SQLFilteredData", alsSqlFilter);
                forward = "successsrvcrestrictconfig";
                session.setAttribute("adminmenupage", "loginmenupage");
            } else if ("ImageUploadAdmin".equalsIgnoreCase(type)) {
                session.setAttribute("IS_ACTIVEPAGE", "imageupload");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("NEWDESIGNPRODUCT", "imageupload");
                forward = "imageupload";
                session.setAttribute("adminmenupage", "loginmenupage");
            } else if ("CustomerAdvisoryAdmin".equalsIgnoreCase(type)) {
                session.setAttribute("IS_ACTIVEPAGE", "customeradvisory");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("NEWDESIGNPRODUCT", "customeradvisory");
                forward = "customeradvisory";
                session.setAttribute("adminmenupage", "loginmenupage");
            } else if (type.equalsIgnoreCase("specialprivi")) {
                if (type.equalsIgnoreCase("specialprivi")) {
                    ArrayList SplPriviList = new ArrayList();
                    SplPriviList = ecomreg.getRegMenuDetails();
                    session.setAttribute("SplPriviList", SplPriviList);
                }
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim()
                        : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";

                } else {
                    session.setAttribute("systemid", session.getId()); // session Id
                }
                session.setAttribute("IS_MENU_LOCK", strMenu);
                forward = "vendor";
                session.setAttribute("IS_ACTIVEPAGE", "Vendormodulesmapping");
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                session.setAttribute("NEWDESIGNPRODUCT", "modulemapping");

            } else if ("useraudit".equalsIgnoreCase(type)) {
                forward = "useraudit";
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null) && (!"".equalsIgnoreCase(strIsViaLogin)) ? strIsViaLogin.trim()
                        : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";
                } else {
                    session.setAttribute("systemid", session.getId());
                }
                session.setAttribute("IS_MENU_LOCK", strMenu);
                session.setAttribute("IS_ACTIVEPAGE", "AuditLog");
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                session.setAttribute("NEWDESIGNPRODUCT", "useraudit");
            } else if ("getAssignedModule".equalsIgnoreCase(type)) {
                String strvendorname = request.getParameter("vendorname");
                ArrayList algetVendor = ecomCntrlObj.getAssignedModule(strvendorname);
                session.setAttribute("SelectedVendorName", strvendorname);
                session.setAttribute("ASSIGNEDModule", algetVendor);
                forward = "assignedJSP";

            } else if ("SaveAssigned".equalsIgnoreCase(type)) {
                ArrayList alssaveVAlue = new ArrayList();
                String strmodulename = request.getParameter("modulename");
                String strvendorname1 = request.getParameter("vendor");
                String strvendorname2 = (String) session.getAttribute("SelectedVendorName");
                String strArray[] = strmodulename.split("SGM");
                for (int ih = 0; ih < strArray.length; ih++) {
                    alssaveVAlue.add(strArray[ih].toString());
                }
                String VendorModuleSave = ecomCntrlObj.saveVendorModules(alssaveVAlue, strvendorname2);
                ArrayList algetVendor = ecomCntrlObj.getAssignedModule(strvendorname2);
                session.setAttribute("ASSIGNEDModule", algetVendor);
                forward = "assignedJSP";

            } else if ("DeleteAssigned".equalsIgnoreCase(type)) {
                ArrayList alssaveVAlue = new ArrayList();
                String strmodulename = request.getParameter("modulename");
                String strvendorname1 = request.getParameter("vendor");
                String strvendorname2 = (String) session.getAttribute("SelectedVendorName");
                String strArray[] = strmodulename.split("SGM");
                for (int ih = 0; ih < strArray.length; ih++) {
                    alssaveVAlue.add(strArray[ih].toString());
                }
                String VendorModuleSave = ecomCntrlObj.DeleteVendorModules(alssaveVAlue, strvendorname2);
                ArrayList algetVendor = ecomCntrlObj.getAssignedModule(strvendorname2);
                session.setAttribute("ASSIGNEDModule", algetVendor);
                forward = "assignedJSP";

            } else if ("vendormenu".equalsIgnoreCase(type)) {
                ArrayList alsgetassignableModule = ecomCntrlObj.getAssignableModules();
                forward = "specialPrivilege";
                session.setAttribute("IS_ACTIVEPAGE", "adminprivl");
                session.setAttribute("DefaultModules", alsgetassignableModule);
            } else if ("emailformate".equalsIgnoreCase(type)) {
                String strmodules = request.getParameter("strmodules");
                String stractivity = request.getParameter("stractivity");
                Map stremailformate = new HashMap();
                stremailformate = ecomCntrlObj.getemailformate(strmodules, stractivity);
                String mailFormet = (String) stremailformate.get("msg");
                String mailsubject = (String) stremailformate.get("sub");
                mailtem = new StringBuffer();
                mailtem.append(mailsubject).append("~~~");
                mailtem.append(mailFormet);
                session.setAttribute("emailmsg", mailtem);
                // forward = "emailmsgtemplate";
                // mailtem = mailtem == null ? "" : mailtem ;
                out.println(mailtem);
                // out.println(stremailformate);
            } else if ("dashboard".equalsIgnoreCase(type)) {
                String userid = (String) session.getAttribute("SESSION_USERCODE");
                // Map<String, String> dashboardMap = ecomCntrlObj.getDashboardCount(userid,
                // v1DataEnableStatus);
                // request.setAttribute("CUSTOMERDASHBOARDCOUNT", dashboardMap);
                if ("Y".equalsIgnoreCase(Dash_display)) {
                    List<UnclearedCargoVo> dashlis = adminBeanObj.getEcomUnclearedCargoDetails(userid);
                    int deliveredsum = dashlis.stream().filter(o -> o.getDelcontrcnt() != null)
                            .mapToInt(o -> Integer.parseInt(o.getDelcontrcnt())).sum();
                    int remainingsum = dashlis.stream().filter(o -> o.getRemcontrcnt() != null)
                            .mapToInt(o -> Integer.parseInt(o.getRemcontrcnt())).sum();
                    request.setAttribute("dashremainingsum", remainingsum);
                    request.setAttribute("dashdeliveredsum", deliveredsum);
                    request.setAttribute("UnclearedCargoDetails", dashlis);
                    String fromdate = DateConversion_New.getCurrentDateDbFormat();
                    String outbal = null;
                    if ("N".equalsIgnoreCase(csfrominv)) {
                        outbal = adminBeanObj.getDashboardOutstandingBalance(fromdate, userid);
                    } else if ("Y".equalsIgnoreCase(custstmtsp)) {
                        outbal = adminBeanObj.getDashboardOutstandingBalfromcuststmtsp(fromdate, userid);
                    } else {
                        Map<String, List> resmap = phase2bean.getOutstandingDetails("A", userid, "", "", "CO");
                        if (resmap != null) {
                            List<String> lis = (List) resmap.get("totalOutstandingBal");
                            if (lis != null && lis.size() > 0) {
                                outbal = lis.get(0);
                            }
                        }
                    }
                    if (outbal != null && outbal.length() > 0) {
                        outbal = Currencyformat.rupeeFormat(outbal);
                    }
                    request.setAttribute("DASH_OUTSTANDING_BALANCE", outbal);
                }
                if ("Y".equalsIgnoreCase(product_Dashboard)) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    String monthyr = month + "-" + year;
                    System.out.println("[ECOM-WAR] AdminAccess     month" + monthyr);
                    Map<String, Map> countMap = (Map<String, Map>) ecomCntrlObj.getDashboardCounts(userid, monthyr,
                            Enable_Dashboard_Filter);
                    // Map<String, String> count = (Map<String, String>) countMap.get("Booking");
                    Map<String, String> count = bookingObj.getBookingCount(userid, custTypeConfig, v1DataEnableStatus,
                            "");
                    Set s = count.entrySet();
                    Iterator it = s.iterator();
                    while (it.hasNext()) {
                        Map.Entry m = (Map.Entry) it.next();
                        String key = (String) m.getKey();
                        String value = (String) m.getValue();
                        if ("Draft".equalsIgnoreCase(key)) {
                            session.setAttribute("Draft", value);
                        } else if ("Send to Agency".equalsIgnoreCase(key)) {
                            session.setAttribute("Send to Agency", value);
                        } else if ("Approved".equalsIgnoreCase(key)) {
                            session.setAttribute("Approved", value);
                        } else if ("Declined".equalsIgnoreCase(key)) {
                            session.setAttribute("Declined", value);
                        } else if ("Confirmed".equalsIgnoreCase(key)) {
                            session.setAttribute("Confirmed", value);
                        }
                    }
                    /* bl count */
                    Map<String, String> blcount = (Map<String, String>) countMap.get("BL");
                    Set sval = blcount.entrySet();
                    Iterator it1 = sval.iterator();
                    while (it1.hasNext()) {
                        Map.Entry mm = (Map.Entry) it1.next();
                        String keye = (String) mm.getKey();
                        String values = (String) mm.getValue();
                        if ("Confirmed".equalsIgnoreCase(keye)) {
                            session.setAttribute("Confirmedbl", values);
                        } else if ("Issued".equalsIgnoreCase(keye)) {
                            session.setAttribute("Issued", values);
                        }
                    }
                    /* bl end count */
                    Map<String, String> sival = (Map<String, String>) countMap.get("SI");
                    Set sicount = sival.entrySet();
                    Iterator itsi = sicount.iterator();
                    while (itsi.hasNext()) {
                        Map.Entry mm = (Map.Entry) itsi.next();
                        String keye = (String) mm.getKey();
                        // int values = (Integer) mm.getValue();
                        String str = String.valueOf(mm.getValue());
                        if ("Submitted".equalsIgnoreCase(keye)) {
                            session.setAttribute("Submittedsi", str);
                        } else if ("Drafts".equalsIgnoreCase(keye)) {
                            session.setAttribute("Draftsi", str);
                        } else if ("csi".equalsIgnoreCase(keye)) {
                            session.setAttribute("SingleSI", str);
                        } else if ("cmsi".equalsIgnoreCase(keye)) {
                            session.setAttribute("MultipleSI", str);
                        } else if ("Accepted".equalsIgnoreCase(keye)) {
                            session.setAttribute("Accepted", str);
                        }
                    }
                }
                /**
                 * HashMap count = (HashMap) bookingObj.getBookingCount(userid,
                 * custTypeConfig, v1DataEnableStatus); Set s =
                 * count.entrySet(); Iterator it = s.iterator(); while
                 * (it.hasNext()) { Map.Entry m = (Map.Entry) it.next(); String
                 * key = (String) m.getKey(); String value = (String)
                 * m.getValue(); if ("Draft".equalsIgnoreCase(key)) {
                 * session.setAttribute("Draft", value); } else if ("Send to
                 * Agency".equalsIgnoreCase(key)) { session.setAttribute("Send
                 * to Agency", value); } else if
                 * ("Approved".equalsIgnoreCase(key)) {
                 * session.setAttribute("Approved", value); } else if
                 * ("Declined".equalsIgnoreCase(key)) {
                 * session.setAttribute("Declined", value); } else if
                 * ("Confirmed".equalsIgnoreCase(key)) {
                 * session.setAttribute("Confirmed", value); } } /* bl count *
                 * HashMap blcount = (HashMap) ecomCntrlObj.getBLCount(userid,
                 * custTypeConfig, v1DataEnableStatus); Set sval =
                 * blcount.entrySet(); Iterator it1 = sval.iterator(); while
                 * (it1.hasNext()) { Map.Entry mm = (Map.Entry) it1.next();
                 * String keye = (String) mm.getKey(); String values = (String)
                 * mm.getValue(); if ("Confirmed".equalsIgnoreCase(keye)) {
                 * session.setAttribute("Confirmedbl", values); } else if
                 * ("Issued".equalsIgnoreCase(keye)) {
                 * session.setAttribute("Issued", values); } } /* bl end count
                 * HashMap sival = (HashMap) ecomCntrlObj.getSiCount(userid,
                 * custTypeConfig, v1DataEnableStatus); Set sicount =
                 * sival.entrySet(); Iterator itsi = sicount.iterator(); while
                 * (itsi.hasNext()) { Map.Entry mm = (Map.Entry) itsi.next();
                 * String keye = (String) mm.getKey(); int values = (Integer)
                 * mm.getValue(); String str = String.valueOf(values); if
                 * ("Submitted".equalsIgnoreCase(keye)) {
                 * session.setAttribute("Submittedsi", str); } else if
                 * ("Drafts".equalsIgnoreCase(keye)) {
                 * session.setAttribute("Draftsi", str); } else if
                 * ("csi".equalsIgnoreCase(keye)) {
                 * session.setAttribute("SingleSI", str); } else if
                 * ("cmsi".equalsIgnoreCase(keye)) {
                 * session.setAttribute("MultipleSI", str); } }*
                 */
                String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin))) ? strIsViaLogin.trim()
                        : "NO";
                String strMenu = "YES";
                if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                    strMenu = "NO";

                } else {
                    session.setAttribute("systemid", session.getId()); // session Id
                }
                session.setAttribute("IS_MENU_LOCK", strMenu);
                session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                session.setAttribute("successPage", "Dash");
                forward = "dashboard";
                session.setAttribute("IS_ACTIVEPAGE", "dashboard");
                session.setAttribute("NEWDESIGNPRODUCT", "dashboard");
                System.out.println("[ECOM-WAR] AdminAccess     ###############################################"
                        + contexts.getAttribute("clientcode"));
            } else if ("SaveSqlfilter".equalsIgnoreCase(type)) {
                String strcarname = request.getParameter("Hiddencarrier");
                String savestate = request.getParameter("Hiddensavestate");
                String blockchar = request.getParameter("Hiddenblockchar");
                String blockWords = request.getParameter("Hiddenchidden");
                String strSaveRes = ecomCntrlObj.saveSQLblock(strcarname, blockchar, blockWords, savestate);
                List alsSqlFilter = ecomCntrlObj.getSQLBlockedData();
                session.setAttribute("SQLFilteredData", alsSqlFilter);
                session.setAttribute("IS_ACTIVEPAGE", "sqlfiter");
                // session.setAttribute("SQLFilteredData", alsSqlFilter);
                forward = "sqlFilter";

            } else if ("SavechangeStatus".equalsIgnoreCase(type)) {
                String strcarriername = request.getParameter("carriername");
                String strchangestatus = request.getParameter("Chkstatus");
                String strStatechange = ecomCntrlObj.SavechangeStatus(strcarriername, strchangestatus);
                List alsSqlFilter = ecomCntrlObj.getSQLBlockedData();
                session.setAttribute("SQLFilteredData", alsSqlFilter);
                forward = "sqlFilter";

            } else if ("sqlfilter".equalsIgnoreCase(type)) {
                List alsSqlFilter = ecomCntrlObj.getSQLBlockedData();
                session.setAttribute("IS_ACTIVEPAGE", "sqlfiter");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("SQLFilteredData", alsSqlFilter);
                forward = "sqlFilter";

            } else if ("useranalytics".equalsIgnoreCase(type)) {
                forward = "useranalytics";
                session.setAttribute("IS_ACTIVEPAGE", "useranalytics");
                session.setAttribute("IS_MENU_LOCK", "YES");
                session.setAttribute("IS_VIA_LOGIN", "NO");
            } else {
                LoginForm loginObj = (LoginForm) form;
                String loginName = loginObj.getUserName();
                loginName = validate.checkNull(loginName);

                String password = loginObj.getPassword();
                password = validate.checkNull(password);
                ArrayList userDataOb = (ArrayList) ecomreg.getAgencyLoginDet(loginName, password);
                password = crypt.encrypt(password);
                String styleCss = loginObj.getStyleName();
                styleCss = validate.checkNull(styleCss);
                String language = loginObj.getLanguage();
                language = validate.checkNull(language);
                if ("V".equalsIgnoreCase(userDataOb.get(1).toString())) {
                    ArrayList userDataObj = ecomreg.getvendormodules(userDataOb.get(0).toString());
                    ArrayList menulist = ecomreg.getvendormoduleslist(loginName);
                    session.setAttribute("MENULIST", menulist);
                    if (menulist.size() > 0) {
                        if ("Email Template".equalsIgnoreCase(menulist.get(0).toString())) {
                            forward = "successvendoremail";
                            session.setAttribute("IS_ACTIVEPAGE", "emailtemplate");
                        } else if ("Audit log".equalsIgnoreCase(menulist.get(0).toString())) {
                            forward = "successvendoraudit";
                            String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                            strIsViaLogin = (strIsViaLogin != null) && (!"".equalsIgnoreCase(strIsViaLogin))
                                    ? strIsViaLogin.trim()
                                    : "NO";
                            String strMenu = "YES";
                            if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                                strMenu = "NO";
                            } else {
                                session.setAttribute("systemid", session.getId());
                            }
                            session.setAttribute("IS_MENU_LOCK", strMenu);
                            session.setAttribute("IS_ACTIVEPAGE", "AuditLog");
                            session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                        } else if ("Password Reset".equalsIgnoreCase(menulist.get(0).toString())) {
                            forward = "successvendor";
                            session.setAttribute("IS_ACTIVEPAGE", "resetadminpassword");
                            session.setAttribute("IS_MENU_LOCK", "YES");
                            session.setAttribute("IS_VIA_LOGIN", "NO");
                            request.setAttribute("IS_REGISTRATION", "YES");
                        } else if ("Module Mapping".equalsIgnoreCase(menulist.get(0).toString())) {
                            ArrayList SplPriviList = new ArrayList();
                            SplPriviList = ecomreg.getRegMenuDetails();
                            session.setAttribute("SplPriviList", SplPriviList);
                            forward = "successvendormapping";
                            session.setAttribute("IS_ACTIVEPAGE", "Vendormodulesmapping");
                            String strIsViaLogin = (String) session.getAttribute("IS_VIA_LOGIN");
                            strIsViaLogin = (strIsViaLogin != null && (!"".equalsIgnoreCase(strIsViaLogin)))
                                    ? strIsViaLogin.trim()
                                    : "NO";
                            String strMenu = "YES";
                            if ("YES".equalsIgnoreCase(strIsViaLogin)) {
                                strMenu = "NO";

                            } else {
                                session.setAttribute("systemid", session.getId()); // session Id
                            }
                            session.setAttribute("IS_MENU_LOCK", strMenu);
                            session.setAttribute("IS_VIA_LOGIN", strIsViaLogin);
                        }
                    } else {
                        forward = "successvendorempty";
                        session.setAttribute("IS_ACTIVEPAGE", "vendor");
                    }
                    session.setAttribute("vendormenuList", userDataObj);
                    session.setAttribute("SESSION_USERNAME", userDataOb.get(0).toString());
                    session.setAttribute("SESSION_USERCODE", "vendor");
                    session.setAttribute("IS_MENU_LOCK", "YES");
                } else {
                    String enable_encryption = (String) request.getServletContext()
                            .getAttribute(GeneralConf.Enable_Login_Password_Encryption);
                    UserData userDataObj = ecomreg.getActiveDetails(loginName, password,
                            ("Y".equalsIgnoreCase(enable_encryption))); // checking from LRPDB 1st
                    String res2Data = "";
                    if (userDataObj != null) {
                        res2Data = userDataObj.getResData();
                        if ("VALID".equalsIgnoreCase(res2Data)) {
                            userDataObj = ecomreg.getUserDetailsFromCustomermaster(loginName, password);
                        } else {
                            session.setAttribute("IS_MENU_LOCK", "YES");
                            forward = "invalid";
                        }
                    }
                    String resData = "", loginname = "", email = "", loginexpire = "", usercode = "", usertype = "",
                            userName = "", preflanguage = "", admin = null, custtype = null;
                    ArrayList templateNoListObj = null;
                    if (userDataObj != null) {
                        resData = userDataObj.getResData();
                        if ("VALID".equalsIgnoreCase(resData)) {
                            session.setAttribute("systemid", session.getId());
                            loginname = userDataObj.getLoginName();
                            loginname = Validation.checkNull(loginname);
                            userName = userDataObj.getUserName();
                            email = userDataObj.getEmail();
                            email = Validation.checkNull(email);
                            usercode = userDataObj.getUsercode();
                            usercode = Validation.checkNull(usercode);
                            usertype = userDataObj.getUsertype();
                            usertype = Validation.checkNull(usertype);
                            loginexpire = userDataObj.getLoginexpire();
                            loginexpire = validate.checkNull(loginexpire);
                            preflanguage = userDataObj.getUserpreflanguage();
                            admin = userDataObj.getAdmin();
                            custtype = userDataObj.getCusttype();
                            // String result = ecomCntrlObj.updateUserOnlineStatus(loginname, "Y");
                            templateNoListObj = ecomCntrlObj.getTemplateNo(loginname, "T");
                            Hashtable authObj = ecomCntrlObj.getAuthenticationMail();
                            if (templateNoListObj != null && templateNoListObj.size() == 3) {
                                LabelBeanConversion.labelListConversion((List<DropDownVO>) templateNoListObj.get(0));
                                backuptemplateno = (ArrayList) templateNoListObj.get(1);
                                tempmangetemplate = (ArrayList) templateNoListObj.get(2); // vasanth

                            }
                            ArrayList RegMenus = ecomCntrlObj.getCustMenuDetailsList(usercode, "C");
                            RegisterMenuVO registerVo = null;
                            int b = 0, c = 0;
                            for (int j = 0; j < RegMenus.size(); j++) {
                                registerVo = (RegisterMenuVO) RegMenus.get(j);
                                if (registerVo.getMenuName().equalsIgnoreCase("Booking")
                                        && registerVo.getCategory().equalsIgnoreCase("NP")) {
                                    b++;
                                } else if (registerVo.getMenuName().equalsIgnoreCase("Dashboard")
                                        && registerVo.getCategory().equalsIgnoreCase("NP")) {
                                    c++;
                                }
                            }
                            if (c > 0) {
                                // Map<String, String> dashboardMap = ecomCntrlObj.getDashboardCount(usercode,
                                // v1DataEnableStatus);
                                // request.setAttribute("CUSTOMERDASHBOARDCOUNT", dashboardMap);
                                if ("Y".equalsIgnoreCase(Dash_display)) {
                                    List<UnclearedCargoVo> dashlis = adminBeanObj
                                            .getEcomUnclearedCargoDetails(usercode);
                                    int deliveredsum = dashlis.stream().filter(o -> o.getDelcontrcnt() != null)
                                            .mapToInt(o -> Integer.parseInt(o.getDelcontrcnt())).sum();
                                    int remainingsum = dashlis.stream().filter(o -> o.getRemcontrcnt() != null)
                                            .mapToInt(o -> Integer.parseInt(o.getRemcontrcnt())).sum();
                                    request.setAttribute("dashremainingsum", remainingsum);
                                    request.setAttribute("dashdeliveredsum", deliveredsum);
                                    request.setAttribute("UnclearedCargoDetails", dashlis);
                                    String fromdate = DateConversion_New.getCurrentDateDbFormat();
                                    String outbal = null;
                                    if ("N".equalsIgnoreCase(csfrominv)) {
                                        outbal = adminBeanObj.getDashboardOutstandingBalance(fromdate, usercode);
                                    } else if ("Y".equalsIgnoreCase(custstmtsp)) {
                                        outbal = adminBeanObj.getDashboardOutstandingBalfromcuststmtsp(fromdate, usercode);
                                    } else {
                                        Map<String, List> resmap = phase2bean.getOutstandingDetails("A", usercode, "",
                                                "", "CO");
                                        if (resmap != null) {
                                            List<String> lis = (List) resmap.get("totalOutstandingBal");
                                            if (lis != null && lis.size() > 0) {
                                                outbal = lis.get(0);
                                            }
                                        }
                                    }
                                    if (outbal != null && outbal.length() > 0) {
                                        outbal = Currencyformat.rupeeFormat(outbal);
                                    }
                                    request.setAttribute("DASH_OUTSTANDING_BALANCE", outbal);
                                }
                                if ("Y".equalsIgnoreCase(product_Dashboard)) {
                                    Calendar calendar = Calendar.getInstance();
                                    int year = calendar.get(Calendar.YEAR);
                                    int month = calendar.get(Calendar.MONTH) + 1;
                                    String monthyr = month + "-" + year;
                                    System.out.println("[ECOM-WAR] AdminAccess     month" + monthyr);
                                    Map<String, Map> countMap = (Map<String, Map>) ecomCntrlObj
                                            .getDashboardCounts(usercode, monthyr, Enable_Dashboard_Filter);
                                    // Map<String, String> count = (Map<String, String>) countMap.get("Booking");
                                    Map<String, String> count = bookingObj.getBookingCount(usercode, custTypeConfig,
                                            v1DataEnableStatus, "");
                                    Set s = count.entrySet();
                                    Iterator it = s.iterator();
                                    while (it.hasNext()) {
                                        Map.Entry m = (Map.Entry) it.next();
                                        String key = (String) m.getKey();
                                        String value = (String) m.getValue();
                                        if ("Draft".equalsIgnoreCase(key)) {
                                            session.setAttribute("Draft", value);
                                        } else if ("Send to Agency".equalsIgnoreCase(key)) {
                                            session.setAttribute("Send to Agency", value);
                                        } else if ("Approved".equalsIgnoreCase(key)) {
                                            session.setAttribute("Approved", value);
                                        } else if ("Declined".equalsIgnoreCase(key)) {
                                            session.setAttribute("Declined", value);
                                        } else if ("Confirmed".equalsIgnoreCase(key)) {
                                            session.setAttribute("Confirmed", value);
                                        }
                                    }
                                    /* bl count */
                                    Map<String, String> blcount = (Map<String, String>) countMap.get("BL");
                                    Set sval = blcount.entrySet();
                                    Iterator it1 = sval.iterator();
                                    while (it1.hasNext()) {
                                        Map.Entry mm = (Map.Entry) it1.next();
                                        String keye = (String) mm.getKey();
                                        String values = (String) mm.getValue();
                                        if ("Confirmed".equalsIgnoreCase(keye)) {
                                            session.setAttribute("Confirmedbl", values);
                                        } else if ("Issued".equalsIgnoreCase(keye)) {
                                            session.setAttribute("Issued", values);
                                        }
                                    }
                                    /* bl end count */
                                    Map<String, String> sival = (Map<String, String>) countMap.get("SI");
                                    Set sicount = sival.entrySet();
                                    Iterator itsi = sicount.iterator();
                                    while (itsi.hasNext()) {
                                        Map.Entry mm = (Map.Entry) itsi.next();
                                        String keye = (String) mm.getKey();
                                        // int values = (Integer) mm.getValue();
                                        String str = String.valueOf(mm.getValue());
                                        if ("Submitted".equalsIgnoreCase(keye)) {
                                            session.setAttribute("Submittedsi", str);
                                        } else if ("Drafts".equalsIgnoreCase(keye)) {
                                            session.setAttribute("Draftsi", str);
                                        } else if ("csi".equalsIgnoreCase(keye)) {
                                            session.setAttribute("SingleSI", str);
                                        } else if ("cmsi".equalsIgnoreCase(keye)) {
                                            session.setAttribute("MultipleSI", str);
                                        } else if ("Accepted".equalsIgnoreCase(keye)) {
                                            session.setAttribute("Accepted", str);
                                        }
                                    }
                                }
                                /**
                                 * HashMap count = (HashMap)
                                 * bookingObj.getBookingCount(usercode,
                                 * custTypeConfig, v1DataEnableStatus); Set s =
                                 * count.entrySet(); Iterator it = s.iterator();
                                 * while (it.hasNext()) { Map.Entry m =
                                 * (Map.Entry) it.next(); String key = (String)
                                 * m.getKey(); String value = (String)
                                 * m.getValue(); if
                                 * ("Draft".equalsIgnoreCase(key)) {
                                 * session.setAttribute("Draft", value); } else
                                 * if ("Send to Agency".equalsIgnoreCase(key)) {
                                 * session.setAttribute("Send to Agency",
                                 * value); } else if
                                 * ("Approved".equalsIgnoreCase(key)) {
                                 * session.setAttribute("Approved", value); }
                                 * else if ("Declined".equalsIgnoreCase(key)) {
                                 * session.setAttribute("Declined", value); }
                                 * else if ("Confirmed".equalsIgnoreCase(key)) {
                                 * session.setAttribute("Confirmed", value); } }
                                 * HashMap blcount = (HashMap)
                                 * ecomCntrlObj.getBLCount(usercode,
                                 * custTypeConfig, v1DataEnableStatus); Set sval
                                 * = blcount.entrySet(); Iterator it1 =
                                 * sval.iterator(); while (it1.hasNext()) {
                                 * Map.Entry mm = (Map.Entry) it1.next(); String
                                 * keye = (String) mm.getKey(); String values =
                                 * (String) mm.getValue(); if
                                 * ("Confirmed".equalsIgnoreCase(keye)) {
                                 * session.setAttribute("Confirmedbl", values);
                                 * } else if ("Issued".equalsIgnoreCase(keye)) {
                                 * session.setAttribute("Issued", values); } }
                                 * /* bl end count HashMap sival = (HashMap)
                                 * ecomCntrlObj.getSiCount(usercode,
                                 * custTypeConfig, v1DataEnableStatus); Set
                                 * sicount = sival.entrySet(); Iterator itsi =
                                 * sicount.iterator(); while (itsi.hasNext()) {
                                 * Map.Entry mm = (Map.Entry) itsi.next();
                                 * String keye = (String) mm.getKey(); int
                                 * values = (Integer) mm.getValue(); String str
                                 * = String.valueOf(values); if
                                 * ("Submitted".equalsIgnoreCase(keye)) {
                                 * session.setAttribute("Submittedsi", str); }
                                 * else if ("Drafts".equalsIgnoreCase(keye)) {
                                 * session.setAttribute("Draftsi", str); } else
                                 * if ("csi".equalsIgnoreCase(keye)) {
                                 * session.setAttribute("SingleSI", str); } else
                                 * if ("cmsi".equalsIgnoreCase(keye)) {
                                 * session.setAttribute("MultipleSI", str); } }*
                                 */
                                forward = "successdash";
                                session.setAttribute("successPage", "Dash");
                            } else if (b > 0) {
                                forward = "success";
                                session.setAttribute("successPage", "Book");
                            } else {
                                forward = "successport";
                                session.setAttribute("PORTSELECT", "Schedule");
                                session.setAttribute("successPage", "port");
                            }
                            session.setAttribute("RegMenus", RegMenus);
                            session.setAttribute("IS_VIA_LOGIN", "YES");
                            session.setAttribute("SESSION_LOGINNAME", loginname);
                            session.setAttribute("SESSION_USERNAME", userName);
                            session.setAttribute("SESSION_EMAIL", email);
                            session.setAttribute("SESSION_TEMPLATE_NO", templateNoListObj);
                            session.setAttribute("SESSION_TEMPLATE_NO", templateno);
                            session.setAttribute("SESSION_BACKUP_TEMPLATE_NO", backuptemplateno);
                            session.setAttribute("SESSION_MANAGE_TEMPLATE", tempmangetemplate); // vasanth
                            session.setAttribute("SESSION_LOGINEXPIRE", loginexpire);
                            session.setAttribute("SESSION_USERCODE", usercode);
                            session.setAttribute("SESSION_USERTYPE", usertype);
                            session.setAttribute("SESSION_AUTHENTICATION", authObj);
                            session.setAttribute("SESSION_LANGUAGE", preflanguage);
                            session.setAttribute("IS_MENU_LOCK", "NO");
                            session.setAttribute("USER_ROLL_ADMIN", admin);
                            session.setAttribute("SESSION_USER_CUST_TYPE", custtype);
                            if (custtype != null && custtype.contains("FC")) {
                                session.setAttribute("SESSION_USER_FURIT_CUSTOMER", "Y");
                            }
                        } else {
                            session.setAttribute("IS_MENU_LOCK", "YES");
                            forward = "invalid";
                        }
                    }
                    request.setAttribute("LOGINRESPONSE_DATA", resData);
                }
            }
            System.out.println("[ECOM-WAR] AdminAccess     forward==>" + forward);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(forward);
    }
}
