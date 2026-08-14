/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomRegistrationBeanRemote;
import net.ecom.common.AjaxSplitter;
import net.ecom.common.LabelBeanConversion;
import net.ecom.common.Validation;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.DropDownVO;
import net.ecom.vo.UserData;

public class PortListAjax extends HttpServlet {

    AjaxSplitter ajaxObj = new AjaxSplitter();
    ServletContext contexts;
    EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
    EcomRegistrationBeanRemote ecomreg = EJBLookUp.getRegistrationBeanLookUp();
    EcomBookingBeanRemote ecombkg = EJBLookUp.getBookingBeanLookUp();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        contexts = config.getServletContext();
    }

    @SuppressWarnings("static-access")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        String type = request.getParameter("type");
        type = Validation.checkNull(type);
         System.out.println("[ECOM-WAR] PortListAjax  Coming in PortListAjax" + type);
        String query = request.getParameter("q");
        try {
            if ("atport".equalsIgnoreCase(type)) {
                List portList = (List) contexts.getAttribute("PORTLISTOBJ");
                if (portList != null && portList.size() > 0) {
                    int portListSize = portList.size();
                    List<String> portListObj = ajaxObj.getMatchedData(query, portList, portListSize);
                    if (portListObj != null && portListObj.size() > 0) {
                        int matchSize = portListObj.size();
                        String strMatch = "";
                        for (int i = 0; i < matchSize; i++) {
                            strMatch = Validation.checkNull(portListObj.get(i).toString());
                            out.println(strMatch);
                        }
                    }
                }
            } else if ("spterm".equalsIgnoreCase(type)) {

                List shTermList = (List) contexts.getAttribute("SHIPMENT_TERMS");
                if (shTermList != null && shTermList.size() > 0) {
                    int shTermListSize = shTermList.size();
                    String strMatch = null;
                    List<String> shptermListObj = ajaxObj.getMatchedData(query, shTermList, shTermListSize);
                    if (shptermListObj != null && shptermListObj.size() > 0) {
                        int matchSize = shptermListObj.size();
                        strMatch = "valid";
                        for (int i = 0; i < matchSize; i++) {
                            strMatch = strMatch + "~~" + Validation.checkNull(shptermListObj.get(i).toString());

                        }
                    }
                    out.print(strMatch);
                }
            } else if ("atvessel".equalsIgnoreCase(type)) {
                List vesselList = (List) contexts.getAttribute("VESSELLISTOBJ");
                String arr = "valid";
                if (vesselList != null && vesselList.size() > 0) {
                    int vesselListSize = vesselList.size();
                    List<String> vesselListObj = ajaxObj.getMatchedData(query, vesselList, vesselListSize);
                    if (vesselListObj != null && vesselListObj.size() > 0) {
                        int matchSize = vesselListObj.size();
                        String strMatch = "";
                        for (int i = 0; i < matchSize; i++) {
                            arr = arr + "~~" + Validation.checkNull(vesselListObj.get(i).toString());

                        }
                    }
                }
                out.print(arr);
            } else if ("commodity".equalsIgnoreCase(type)) {
                List comList = (List) contexts.getAttribute("SESSION_COMMODITYLIST");
                if (comList != null && comList.size() > 0) {
                    int comListSize = comList.size();
                    List<String> comListObj = ajaxObj.getMatchedData(query, comList, comListSize);
                    if (comListObj != null && comListObj.size() > 0) {
                        int matchSize = comListObj.size();
                        String strMatch = "valid";
                        for (int i = 0; i < matchSize; i++) {
                            strMatch = strMatch + "~~" + Validation.checkNull(comListObj.get(i).toString());

                        }
                        //System.out.println([ECOM-WAR] PortListAjax strMatch);
                        out.print(strMatch);
                    } else {
                        out.println("INVALID");
                    }
                }
            } else if ("customer".equalsIgnoreCase(type)) {
                List comList = (List) session.getAttribute("sessionAdmindetails");
                if (comList != null && comList.size() > 0) {
                    int comListSize = comList.size();
                    List<String> comListObj = ajaxObj.getMatchedData(query, comList, comListSize);
                    if (comListObj != null && comListObj.size() > 0) {
                        int matchSize = comListObj.size();
                        String strMatch = "valid~~";
                        for (int i = 0; i < matchSize; i++) {
                            strMatch = strMatch + "~~" + Validation.checkNull(comListObj.get(i).toString());

                        }
                        //System.out.println([ECOM-WAR] PortListAjax strMatch);
                        out.print(strMatch);
                    } else {
                        out.println("INVALID");
                    }
                }
            } else if ("checkcustomer".equalsIgnoreCase(type)) {
                 System.out.println("[ECOM-WAR] PortListAjax  ???query???" + query);
                UserData userDataObj = null;
                ArrayList templateNoListObj = null;
                ArrayList templateno = new ArrayList();
                ArrayList backuptemplateno = new ArrayList();
                ArrayList tempmangetemplate = new ArrayList();   // vasanth
                String resData = "", loginname = "", firstname = "", lastname = "", email = "", loginexpire = "", systemType = "", usercode = "", usertype = "", userName = "", preflanguage = "";
                List customerlist = (List) session.getAttribute("sessionAdmindetails");
                boolean arr = false;
                if (customerlist != null && customerlist.size() > 0) {
                     System.out.println("[ECOM-WAR] PortListAjax  >>>customerlist>>" + customerlist.size());
                    if (customerlist.contains(query)) {

                        String[] custvalue = query.split("-");
                        String custcode = custvalue[0].trim();
                         System.out.println("[ECOM-WAR] PortListAjax  >>cust code while selecting customer>>" + custcode);
                        session.setAttribute("SESSION_USERCODE", custcode);
                        userDataObj = ecomreg.getUserDetailsforAdmin(custcode);
                        loginname = userDataObj.getLoginName();
                        loginname = Validation.checkNull(loginname);
                        firstname = userDataObj.getFirstName();
                        firstname = Validation.checkNull(firstname);
                        lastname = userDataObj.getLastName();
                        lastname = Validation.checkNull(lastname);
                        userName = userDataObj.getUserName();
                        email = userDataObj.getEmail();
                        email = Validation.checkNull(email);
                        usercode = userDataObj.getUsercode();
                        usercode = Validation.checkNull(usercode);
                        usertype = userDataObj.getUsertype();
                        usertype = Validation.checkNull(usertype);
                        loginexpire = userDataObj.getLoginexpire();
                        loginexpire = Validation.checkNull(loginexpire);
                        systemType = userDataObj.getSystemType();
                        systemType = Validation.checkNull(systemType);
                        preflanguage = userDataObj.getUserpreflanguage();

                        templateNoListObj = ecomCntrlObj.getTemplateNo(loginname, "T");
                        if (templateNoListObj != null && templateNoListObj.size() == 3) {
                            templateno = LabelBeanConversion.labelListConversion((List<DropDownVO>) templateNoListObj.get(0));
                            backuptemplateno = (ArrayList) templateNoListObj.get(1);
                            tempmangetemplate = (ArrayList) templateNoListObj.get(2);  // vasanth
                        }
                        String timeZone = Validation.checkNull(userDataObj.getTimezone());
//                         System.out.println("[ECOM-WAR] PortListAjax  >>>user preferred timezone>>" + timeZone);
                        String firsttimesplit = null, secondparttimeslit = null;
                        if (timeZone != null && timeZone.length() > 0) {
                            String aftrTimesplit[] = timeZone.split("~~");
                            if (aftrTimesplit.length > 0) {
                                firsttimesplit = Validation.checkNull(aftrTimesplit[0]).trim();
                                secondparttimeslit = Validation.checkNull(aftrTimesplit[1]).trim();
                            }

                        }
                        session.setAttribute("USERPREFERREDTIMEZONE", firsttimesplit);
                         System.out.println("[ECOM-WAR] PortListAjax  firstslit" + firsttimesplit+",timeZone:"+timeZone);

                        session.setAttribute("SESSION_LOGIN_TYPE", "U");
                        session.setAttribute("IS_VIA_LOGIN", "YES");
                        session.setAttribute("SESSION_LOGINNAME", loginname);
                        session.setAttribute("SESSION_FIRSTNAME", firstname);
                        session.setAttribute("SESSION_LASTNAME", lastname);
                        session.setAttribute("SESSION_USERNAME", userName);
                        session.setAttribute("SESSION_EMAIL", email);
                        session.setAttribute("SESSION_LOGINEXPIRE", loginexpire);
                        session.setAttribute("SESSION_USERCODE", usercode);
                        session.setAttribute("SESSION_USERTYPE", usertype);
                        session.setAttribute("SESSION_LANGUAGE", preflanguage);
                        session.setAttribute("IS_MENU_LOCK", "NO");
                        session.setAttribute("SESSION_TEMPLATE_NO", templateNoListObj);
                        session.setAttribute("SESSION_TEMPLATE_NO", templateno);
                        session.setAttribute("SESSION_BACKUP_TEMPLATE_NO", backuptemplateno);
                        session.setAttribute("SESSION_MANAGE_TEMPLATE", tempmangetemplate); //vasanth

                        arr = true;
                    }
                }
                out.print(arr);
            } else if ("sicommodityvalidate".equalsIgnoreCase(type)) {
                List comList = (List) contexts.getAttribute("SESSION_COMMODITYLIST");
                String result = null;
                if (comList != null && (!comList.isEmpty())) {
                    if (comList.contains(query)) {
                        result = "valid";
                    } else {
                        result = "Invalid";
                    }
                }
                out.print(result);
            } else if ("UNODetails".equalsIgnoreCase(type)) {
                String unno = request.getParameter("eqpType");
                String result = ecombkg.getUNODetails(unno);
                out.print(result);
            } else if ("IMDGClassDetails".equalsIgnoreCase(type)) {
                String imdg = request.getParameter("unno");
                String result = ecombkg.getIMDGClassDetails(imdg);
                out.print(result);
            } else if ("getTareweight".equalsIgnoreCase(type)) {
                //  eqptype="+eqptype+"&equipdesc="+value
                String eqptype = request.getParameter("eqptype");
                String equipdesc = request.getParameter("equipdesc");
                String result = ecombkg.getTareweight(eqptype, equipdesc);
                out.print(result);
            } else if ("getTareweightAmend".equalsIgnoreCase(type)) {
                //  eqptype="+eqptype+"&equipdesc="+value
                String eqptype = request.getParameter("eqptype");
                String containerno = request.getParameter("containerno");
                String equipdesc = request.getParameter("equipdesc");
                String soc = request.getParameter("soc");
                String result = "";
                float result2 = ecombkg.getTareWtForAmendment(containerno, eqptype, equipdesc);
                if (result2 > 0) {
                    out.print(result2);
                } else if (!"Y".equalsIgnoreCase(soc) || (containerno.contains(eqptype + "TNB") || (containerno.contains(eqptype + "TBN")))) {
                    result = ecombkg.getTareweight(eqptype, equipdesc);
                    out.print(result);
                }

            } else if ("packageValidate".equalsIgnoreCase(type)) {
                List packageList = (List) contexts.getAttribute("SESSION_PACKAGELIST");
                String result = null;
                if (packageList != null && (!packageList.isEmpty())) {
                    if (packageList.contains(query)) {
                        result = "valid";
                    } else {
                        result = "Invalid";
                    }
                }
                out.print(result);
            } else if ("newcommodity".equalsIgnoreCase(type)) {
                List comList = (List) contexts.getAttribute("NEW_COMDTY_JSON");
                if (comList != null && comList.size() > 0) {
                    int comListSize = comList.size();
                    List<String> comListObj = ajaxObj.getMatchedData(query, comList, comListSize);
                    if (comListObj != null && comListObj.size() > 0) {
                        int matchSize = comListObj.size();
                        String strMatch = "valid";
                        for (int i = 0; i < matchSize; i++) {
                            strMatch = strMatch + "~~" + Validation.checkNull(comListObj.get(i).toString());
                        }
                        out.println(strMatch);
                    } else {
                        out.println("INVALID");
                    }

                }

            } else if ("neweqp".equalsIgnoreCase(type)) {
                List comList = (List) contexts.getAttribute("NEW_EQP_JSON");
                if (comList != null && comList.size() > 0) {
                    int comListSize = comList.size();
                    List<String> comListObj = ajaxObj.getMatchedData(query, comList, comListSize);
                    if (comListObj != null && comListObj.size() > 0) {
                        int matchSize = comListObj.size();
                        String strMatch = "";
                        for (int i = 0; i < matchSize; i++) {
                            strMatch = Validation.checkNull(comListObj.get(i).toString());
                            out.println(strMatch);
                        }
                    } else {
                        out.println("INVALID");
                    }

                }

            } else if ("pack".equalsIgnoreCase(type)) {
                List packageList = (List) contexts.getAttribute("SESSION_PACKAGELIST");
                if (packageList != null && packageList.size() > 0) {
                    int packageListSize = packageList.size();
                    List<String> packageListObj = ajaxObj.getMatchedData(query, packageList, packageListSize);
                    if (packageListObj != null && packageListObj.size() > 0) {
                        int matchSize = packageListObj.size();
                        String strMatch = "valid";
                        for (int i = 0; i < matchSize; i++) {
                            strMatch = strMatch + "~~" + Validation.checkNull(packageListObj.get(i).toString());

                        }
                        out.print(strMatch);
                    }
                }
            } else if ("competition".equalsIgnoreCase(type)) {
                List comList = (List) contexts.getAttribute("SESSION_COMPETITION");
                if (comList != null && comList.size() > 0) {
                    int comListSize = comList.size();
                    List<String> comListObj = ajaxObj.getMatchedData(query, comList, comListSize);
                    if (comListObj != null && comListObj.size() > 0) {
                        int matchSize = comListObj.size();
                        String strMatch = "";
                        for (int i = 0; i < matchSize; i++) {
                            strMatch = Validation.checkNull(comListObj.get(i).toString());
                            out.println(strMatch);
                        }
                    }

                }

            } else if ("country".equalsIgnoreCase(type)) {
                List conList = (List) contexts.getAttribute("COUNTRYLISTOBJ");
                String strMatch = "";
                if (conList != null && conList.size() > 0) {
                    int comListSize = conList.size();
                    List<String> conListObj = ajaxObj.getMatchedData(query, conList, comListSize);

                    if (conListObj != null && conListObj.size() > 0) {
                        int matchSize = conListObj.size();
                        strMatch = "valid";
                        for (int i = 0; i < matchSize; i++) {
                            strMatch = strMatch + "~~" + Validation.checkNull(conListObj.get(i).toString());

                        }
                    }
                    out.print(strMatch);

                }

            } else if ("city".equalsIgnoreCase(type)) {

                List cityList = (List) session.getAttribute("CITYLISTOBJ");
                String strMatch = "";
                if (cityList != null && cityList.size() > 0) {
                    int cityListSize = cityList.size();
                    List<String> cityListObj = ajaxObj.getMatchedData(query, cityList, cityListSize);

                    if (cityListObj != null && cityListObj.size() > 0) {
                        int matchSize = cityListObj.size();
                        strMatch = "valid~~";
                        for (int i = 0; i < matchSize; i++) {
                            strMatch = strMatch + "~~" + Validation.checkNull(cityListObj.get(i).toString());

                        }
                    }
                    out.print(strMatch);

                }

            } else if ("getLocation".equalsIgnoreCase(type)) {
                String countryCode = Validation.checkNull(request.getParameter("countryCode"));
                HashMap locationHash = (HashMap) contexts.getAttribute("CONTEXT_AGENCYLOCATION");
                if (locationHash != null && locationHash.size() > 0) {
                    List locationList = (List) locationHash.get(countryCode);
                    if (locationList != null && locationList.size() > 0) {
                        int locationListSize = locationList.size();
                        List<String> locationListObj = ajaxObj.getMatchedData(query, locationList, locationListSize);
                        if (locationListObj != null && locationListObj.size() > 0) {
                            int matchSize = locationListObj.size();
                            String strMatch = "valid~~";
                            for (int i = 0; i < matchSize; i++) {
                                strMatch = strMatch + "~~" + Validation.checkNull(locationListObj.get(i).toString());

                            }
                            out.print(strMatch);
                        }

                    }
                }
            } else if ("atAgency".equalsIgnoreCase(type)) {
                List agencyList = (List) contexts.getAttribute("AGENCYLISTOBJ");
                if (agencyList != null && agencyList.size() > 0) {
                    int agencyListSize = agencyList.size();
                    List<String> agencyListObj = ajaxObj.getMatchedData(query, agencyList, agencyListSize);
                    if (agencyListObj != null && agencyListObj.size() > 0) {
                        int matchSize = agencyListObj.size();
                        String strMatch = "valid";
                        for (int i = 0; i < matchSize; i++) {
                            strMatch = strMatch + "~~" + Validation.checkNull(agencyListObj.get(i).toString());

                        }
                        out.print(strMatch);
                    }
                }
            } else if ("atseaport".equalsIgnoreCase(type)) {
                List portList = (List) contexts.getAttribute("SEAPORTLISTOBJ");
                String arr = "valid";
                if (portList != null && portList.size() > 0) {
                    int portListSize = portList.size();
                    List<String> portListObj = ajaxObj.getMatchedData(query, portList, portListSize);
                    if (portListObj != null && portListObj.size() > 0) {
                        int matchSize = portListObj.size();
                        for (int i = 0; i < matchSize; i++) {
                            arr = arr + "~~" + Validation.checkNull(portListObj.get(i)).toString();
                        }
                        out.print(arr);
                    }
                }
            } else if ("checkPort_New".equalsIgnoreCase(type)) {
                List portList = (List) contexts.getAttribute("PORTLISTOBJ");
                boolean arr = false;
                if (portList != null && portList.size() > 0) {
                    if (portList.contains(query)) {
                        arr = true;
                    }
                }
                out.print(arr);
            } else if ("checkAgncy_Port".equalsIgnoreCase(type)) {
                boolean arr = false;
                String countryCode = Validation.checkNull(request.getParameter("countryCode"));
                HashMap locationHash = (HashMap) contexts.getAttribute("CONTEXT_AGENCYLOCATION");
                if (locationHash != null && locationHash.size() > 0) {
                    List locationList = (List) locationHash.get(countryCode);
                    if (locationList != null && locationList.size() > 0) {
                        int locationListSize = locationList.size();
                        List<String> portList = ajaxObj.getMatchedData(query, locationList, locationListSize);
                        if (portList != null && portList.size() > 0) {
                            if (portList.contains(query)) {
                                arr = true;
                            }
                        }
                    }
                    out.print(arr);
                }
            } else if ("checkPort_Schedule".equalsIgnoreCase(type)) {

                List portList = (List) contexts.getAttribute("PORTLISTOBJ");
                String splitport[] = query.split("~~");
                String arr = "";
                if (portList != null && portList.size() > 0) {
                    if (portList.contains(splitport[0])) {
                        arr = arr + "true~~";
                    } else {
                        arr = arr + "false~~";
                    }
                }
                if (portList != null && portList.size() > 0) {
                    if (portList.contains(splitport[1])) {
                        arr = arr + "true";
                    } else {
                        arr = arr + "false";
                    }
                }
                out.print(arr);
            } else if ("checkVessel_New".equalsIgnoreCase(type)) {

                List vesselList = (List) contexts.getAttribute("VESSELLISTOBJ");
                 System.out.println("[ECOM-WAR] PortListAjax  >>vesselList>>" + vesselList);
                boolean arr = false;
                if (vesselList != null && vesselList.size() > 0) {
                    if (vesselList.contains(query)) {
                        arr = true;
                    }
                }
                out.print(arr);
            } else if ("checkcountry_New".equalsIgnoreCase(type)) {
                List countryList = (List) contexts.getAttribute("COUNTRYLISTOBJ");
                boolean arr = false;
                if (countryList != null && countryList.size() > 0) {
                    if (countryList.contains(query)) {
                        arr = true;
                    }
                }
                out.print(arr);
            } else if ("checkcommodity_New".equalsIgnoreCase(type)) {

                List countryList = (List) contexts.getAttribute("NEW_COMDTY_JSON");
                boolean arr = false;
                if (countryList != null && countryList.size() > 0) {
                    if (countryList.contains(query)) {
                        arr = true;
                    }
                }
                out.print(arr);
            } else if ("checkcommodity_NewSI".equalsIgnoreCase(type)) {

                List countryList = (List) contexts.getAttribute("NEW_COMDTY_JSON");
                boolean arr = false;
                String value = request.getParameter(query);
                if (countryList != null && countryList.size() > 0) {
                    if (countryList.contains(value)) {
                        arr = true;
                    }
                }
                out.print(arr);
            } else if ("checkpack_NewSI".equalsIgnoreCase(type)) {
                //   System.out.println("[ECOM-WAR] PortListAjax  welcomeeeee"+query);
                List countryList = (List) contexts.getAttribute("SESSION_PACKAGELIST");
                //   System.out.println("[ECOM-WAR] PortListAjax  countrylist:::::::"+countryList);
                boolean arr = false;
                if (countryList != null && countryList.size() > 0) {
                    if (countryList.contains(query)) {
                        arr = true;
                    }
                }
                out.print(arr);
            } else if ("checkagency_New".equalsIgnoreCase(type)) {
                List agencyList = (List) contexts.getAttribute("AGENCYLISTOBJ");
                boolean arr = false;
                if (agencyList != null && agencyList.size() > 0) {
                    if (agencyList.contains(query)) {
                        arr = true;
                    }
                }
                out.print(arr);
            } else if ("packageajax".equalsIgnoreCase(type)) {
                List comList = (List) contexts.getAttribute("PACKAGEARRAY");
                if (comList != null && comList.size() > 0) {
                    int comListSize = comList.size();
                    List<String> comListObj = ajaxObj.getMatchedData(query, comList, comListSize);
                    if (comListObj != null && comListObj.size() > 0) {
                        int matchSize = comListObj.size();
                        String strMatch = "valid";
                        for (int i = 0; i < matchSize; i++) {
                            strMatch = strMatch + "~~" + Validation.checkNull(comListObj.get(i).toString());
                        }
                        out.println(strMatch);
                    } else {
                        out.println("INVALID");
                    }

                }

            }

        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
