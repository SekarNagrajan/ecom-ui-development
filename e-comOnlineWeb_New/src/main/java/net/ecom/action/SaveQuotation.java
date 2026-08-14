/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.action;

import com.google.gson.Gson;

import net.ecom.common.DateConversion_New;
import net.ecom.common.GeneralConf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author gobinath.t
 */
public class SaveQuotation extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    String baseAPIURL = "http://localhost:8060/";// CRMConfig.baseQuoteAPIURL;
    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping  The ActionMapping used to select this instance.
     * @param form     The optional ActionForm bean for this request.
     * @param request  The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        System.out.println("SaveQuotation===> ");
        try {
            Map quoteMap = new HashMap();

            baseAPIURL = (String) request.getServletContext().getAttribute(GeneralConf.Quote_API_URL);
            baseAPIURL=baseAPIURL!=null?baseAPIURL:"http://localhost:8060/";
            System.out.println("baseAPIURL===> " + baseAPIURL);
            // Get company details
            String companyName = request.getParameter("companyName");
            String companyCode = request.getParameter("companyCode");

            // Get location details and split to get codes
            String origin = request.getParameter("origin");
            String destination = request.getParameter("destination");
            String commodity = request.getParameter("commodity");

            // Split and get first part (code) for origin, destination and commodity
            String originCode = origin != null ? origin.split("-")[0].trim() : "";
            String destinationCode = destination != null ? destination.split("-")[0].trim() : "";
            String commodityCode = commodity != null ? commodity.split("-")[0].trim() : "";

            String shipmentDate = request.getParameter("shipmentDate");
            shipmentDate = DateConversion_New.convertDefaultDBFormatwithoutTime(shipmentDate);
            // Get container and quantity details
            String containerType = request.getParameter("containerTypeDropdown");
            String quantity = request.getParameter("quantity");
            String weight = request.getParameter("weight");

            // Get carriage details
            String preCarriage = request.getParameter("preCarriage");
            String onCarriage = request.getParameter("onCarriage");
            String tos = request.getParameter("tos");
            String tservice = request.getParameter("tservice");
            String tservicename = request.getParameter("tservicename");

            // Get flags
            String dg = request.getParameter("dg");
            String soc = request.getParameter("soc");
            String nor = request.getParameter("nor");

            // Get request details
            String reqType = request.getParameter("reqType");
            String requestRate = request.getParameter("requestRate");
            String remarks = request.getParameter("remarks");
            String surchargeList = request.getParameter("surchargeList");
            // Store all values in quoteMap
            quoteMap.put("companyName", companyName);
            quoteMap.put("companyCode", companyCode);
            quoteMap.put("origin", originCode); // Store the code instead of full value
            quoteMap.put("destination", destinationCode); // Store the code instead of full value
            quoteMap.put("shipmentDate", shipmentDate);
            quoteMap.put("containerType", containerType);
            quoteMap.put("quantity", quantity);
            quoteMap.put("weight", weight);
            quoteMap.put("commodity", commodityCode); // Store the code instead of full value
            quoteMap.put("preCarriage", preCarriage);
            quoteMap.put("onCarriage", onCarriage);
            quoteMap.put("tos", tos);
            quoteMap.put("tservice", tservice);
            quoteMap.put("tservicename", tservicename);
            quoteMap.put("dg", dg);
            quoteMap.put("soc", soc);
            quoteMap.put("nor", nor);
            quoteMap.put("reqType", reqType);
            quoteMap.put("requestRate", requestRate);
            quoteMap.put("remarks", remarks);
            quoteMap.put("surchargeList", surchargeList);
            // Store the map in request for further processing
            // request.setAttribute("quoteMap", quoteMap);
            String quoteNo = getRateQuoteMap(quoteMap);
            request.setAttribute("quoteNo", quoteNo);
            
            if (quoteNo != null) {
                request.setAttribute("quotemsg", "Success");
            } else {
                request.setAttribute("quotemsg", "Failed");
            }
            updateQuoteStatus(quoteNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(SUCCESS);
    }

    

    public String getRateQuoteMap(Map quoteMap) {
        String quoteNo = null;
        try {
            Map<String, Object> rateQuote = new HashMap<>();
            Map<String, Object> innerQuoteMap = null;
            if (quoteMap != null) {

                rateQuote.put("TxtCustCode", quoteMap.get("companyCode"));//
                rateQuote.put("TxtCustName", quoteMap.get("companyName"));//
                rateQuote.put("origin", quoteMap.get("origin"));//
                rateQuote.put("delivery", quoteMap.get("destination"));//
                rateQuote.put("CmbCarrCode", quoteMap.get("tservice"));
                rateQuote.put("shiptype", "Containerized");
                rateQuote.put("Bgpprecarriage", "N");
                rateQuote.put("Bgponcarriage", "N");
                rateQuote.put("commodity", quoteMap.get("commodity"));// checkNull((String)
                                                                      // innerQuoteMap.get("commodity"))
                rateQuote.put("TxtEquipment", quoteMap.get("containerType"));//
                // rateQuote.put("eqpqty", innerQuoteMap.get("quantity"));//
                rateQuote.put("eqpqty", quoteMap.get("quantity"));//
                rateQuote.put("tos", quoteMap.get("tos"));
                rateQuote.put("fromdate", quoteMap.get("shipmentDate"));//
                rateQuote.put("todate", getTodate((String) quoteMap.get("shipmentDate")));//
                rateQuote.put("weight", quoteMap.get("weight"));
                rateQuote.put("DG", "N");
                rateQuote.put("unno", "");
                rateQuote.put("imdg", "");
                rateQuote.put("SOC", "N");
                rateQuote.put("NOR", "N");
                rateQuote.put("reqtype", "OFR");
                rateQuote.put("Allin", 1000);//
                rateQuote.put("remark", quoteMap.get("remarks"));//
                rateQuote.put("CmbCarr", quoteMap.get("tservicename"));
                rateQuote.put("ActualAmount", Integer.parseInt((String) quoteMap.get("requestRate")));//
                rateQuote.put("agencycode", getAgencyCode(checkNull((String) quoteMap.get("origin"))));
                // rateQuote.put("todate", "");//
                rateQuote.put("curr", "USD");
                rateQuote.put("leadid", "");
                rateQuote.put("custcode", "");
                rateQuote.put("allinstatus", "O");
                rateQuote.put("SH", "N");
                rateQuote.put("CN", "N");
                rateQuote.put("NF", "N");
                rateQuote.put("FW", "N");
                rateQuote.put("eqpgrp", "NO");
                rateQuote.put("impfreedays", "0");
                rateQuote.put("expfreedays", "0");
                rateQuote.put("imptype", "N");
                rateQuote.put("exptype", "N");
                rateQuote.put("carriercode", "SVM");
                rateQuote.put("loginname", "superuser");//
                rateQuote.put("dgdetails", "");
                rateQuote.put("dglist", new ArrayList<>());
                rateQuote.put("cbi", "N/A");
                rateQuote.put("cwa", "N/A");
                rateQuote.put("cai", "N/A");
                rateQuote.put("missingcost", "");
                rateQuote.put("oogFlag", "N");
                rateQuote.put("CounterBy", "C");
                rateQuote.put("counterno", "0");
                rateQuote.put("usdAmt", "0");
                rateQuote.put("ordNo", "0");
                rateQuote.put("itemNo", "0");
                rateQuote.put("quoteStatus", "P");
                rateQuote.put("contriCost", "N/A");
                rateQuote.put("contriRev", "N/A");
                rateQuote.put("contriCalc", "N");
                rateQuote.put("retcur", "0");
                rateQuote.put("orgRoe", "1");

                rateQuote.put("tariff", getTariffAmountForQuote(quoteMap));//
                Map routeMap = getRoutingDetails(quoteMap);
                rateQuote.put("routepol", checkNull((String) routeMap.get("routepol")));//
                rateQuote.put("service", checkNull((String) routeMap.get("service")));//
                rateQuote.put("routepodter", checkNull((String) routeMap.get("routepodter")));//
                rateQuote.put("routepolter", checkNull((String) routeMap.get("routepolter")));//
                rateQuote.put("routepod", checkNull((String) routeMap.get("routepod")));//
                /*
                 * Map<String, Object> surcharge = new HashMap<>();
                 * surcharge.put("pol", checkNull((String) quoteMap.get("origin")));
                 * surcharge.put("pod", checkNull((String) quoteMap.get("destination")));
                 * surcharge.put("eqp", checkNull((String) quoteMap.get("containerType")));
                 * surcharge.put("eqpval", "1");
                 * surcharge.put("eqpwt", "10.0");
                 * surcharge.put("yesNo", false);
                 * surcharge.put("chgs", "CSU");
                 * surcharge.put("tarr", 200);
                 * surcharge.put("curr", "SGD");
                 * surcharge.put("roe", "1.3295");
                 * surcharge.put("usd", 150.43);
                 * surcharge.put("loc", 200);
                 * surcharge.put("retcur", "150.43");
                 * surcharge.put("orgCur", "SGD");
                 * surcharge.put("orgTariff", "200.00");
                 * surcharge.put("orgRoe", "1.3295");
                 * surcharge.put("amtForm", "A");
                 * surcharge.put("id", 1);
                 * surcharge.put("seqno", "");
                 * surcharge.put("locamt", "200.00");
                 * surcharge.put("usdamt", "150.43");
                 * surcharge.put("chargetype", "B");
                 * surcharge.put("allin", false);
                 * surcharge.put("tosDis", false);
                 * surcharge.put("sur_allin", "");
                 * surcharge.put("sur_vatos", "");
                 * surcharge.put("sur_contri", "");
                 * surcharge.put("vatos", "Y");
                 * 
                 * rateQuote.put("surcharge", Collections.singletonList(surcharge));
                 */
                String surchargeListJson = (String) quoteMap.get("surchargeList");
                List surlList = null;
                Gson gson = new Gson();
                surlList = gson.fromJson(surchargeListJson, List.class);
                surlList = surlList == null ? new ArrayList() : surlList;
                rateQuote.put("surcharge", surlList);

                // rateQuote.put("surcharge","@surchargeList");
                // Print for verification
                System.out.println("Rate Quote Map: " + rateQuote);
                String jsonstr = (new Gson()).toJson(rateQuote);
                System.out.println("jsonstr===> " + jsonstr);
                // System.err.println("surchargeList===> "+(String)
                // quoteMap.get("surchargeList"));
                // jsonstr=jsonstr.replace("@surchargeList",(String)
                // quoteMap.get("surchargeList"));
                // System.out.println("jsonstr after replace===> "+jsonstr);
                Map quoteMapres = sendQuotation(jsonstr);
                quoteNo = (String) quoteMapres.get("quoteno");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return quoteNo;
    }

    public List getSurcharge(Map quoteMap) {
        Map<String, Object> rateQuote = new HashMap<>();

        rateQuote.put("Allin", "200");
        rateQuote.put("commodity", "999");
        rateQuote.put("delivery", "SGSIN");
        rateQuote.put("TxtEquipment", "20DC");
        rateQuote.put("carriercode", "SVM");
        rateQuote.put("origin", "INNSA");
        rateQuote.put("weight", "1");
        rateQuote.put("expfreedays", "1");
        rateQuote.put("CmbCarrCode", "CY / CY");
        rateQuote.put("CmbCarr", "MM");
        rateQuote.put("TxtCustCode", "CUS0000044");
        rateQuote.put("service", "INSG");
        rateQuote.put("impfreedays", "1");
        rateQuote.put("agencycode", "INDMUM");
        rateQuote.put("Bgpprecarriage", "N");
        rateQuote.put("Bgponcarriage", "N");
        rateQuote.put("dallsurcharge_enable", "Y");

        // Print for verification
        System.out.println("New Rate Quote Map: " + rateQuote);
        List surList = new ArrayList();
        getSurchargeListQuote("");
        return surList;
    }

    public String getSurchargeListQuote(String input) {
        HttpPost post = null;
        HttpResponse response = null;
        String jsonData = null;
        int responseCode = 0;
        String result = null;
        String line = "";
        String res = null;
        try {
            System.out.println("getsurchargelistquote==> " + input);
            // res = new ArrayList();
            // String restUrl =
            // "https://lrpv2-demo.solverminds.net/Lamp/SampleAppconfig/getsampleTVAPI/RRvalidation";
            String restUrl = baseAPIURL + "SRRSurcharge/Commercial/SRRGate/SRRSurcharge";
            jsonData = input;
            post = new HttpPost(restUrl);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Accept", "*/*");
            post.setEntity(new StringEntity(jsonData));
            HttpClient client = HttpClientBuilder.create().build();
            response = client.execute(post);

            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
            responseCode = response.getStatusLine().getStatusCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            // System.out.println("reader==> "+reader.toString());
            result = checkNull(reader.readLine());
            // System.out.println("reader readLine==> " + reader.readLine());
            /*
             * while ((line = reader.readLine()) != null) {
             * result.append(line);
             * }
             */
            //
            System.out.println("Output---->" + result);
            //
            if (responseCode == 200 && (!"0".equalsIgnoreCase(result))) {

                res = result;

            } else {
                res = "[]";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public Map sendQuotation(String inputmap) {
        HttpPost post = null;
        HttpResponse response = null;
        String jsonData = null;
        int responseCode = 0;
        String result = null;
        String line = "";
        String quoteno = "";
        Map outmap = null;
        try {
            outmap = new HashMap();
            // String restUrl =
            // "https://lrpv2-demo.solverminds.net/SRRSurcharge/Commercial/SRRGate/SRRSubmitAction";
            String restUrl = baseAPIURL + "SRRSurcharge/Commercial/SRRGate/SRRSubmitAction";
            // jsonData = getQuotationData(dataMap);
            jsonData = inputmap;
            post = new HttpPost(restUrl);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Accept", "*/*");
            post.setEntity(new StringEntity(jsonData));
            HttpClient client = HttpClientBuilder.create().build();
            response = client.execute(post);

            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
            responseCode = response.getStatusLine().getStatusCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            // System.out.println("reader==> "+reader.toString());
            result = checkNull(reader.readLine());
            // System.out.println("reader readLine==> " + reader.readLine());
            /*
             * while ((line = reader.readLine()) != null) {
             * result.append(line);
             * }
             */
            //
            System.out.println("Output---->" + result);
            //
            if (responseCode == 200) {
                // Gson gsonObj = new Gson();
                // Map outputObj = gsonObj.fromJson(result.toString(), Map.class);
                quoteno = result;
                System.out.println("QuoteNO-------------------> " + quoteno);
                // String valDuplicateQuote = quotebean.validateDuplicateQuote();

                outmap.put("quoteno", quoteno);
                // generateOpportunity(dataMap);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outmap;
    }

    private String checkNull(String str) {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String saveToQuote(String quoteno, String custcode) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = "failure";
        System.out.println("=======saveToQuote============>>>>");
        try {
            DataSource ds = (DataSource) (new InitialContext()).lookup("java:/CRMPRODDS");
            con = ds.getConnection();
            ;
            ps = con.prepareStatement("Insert Into crm_quote(quoteno,custcode) values (?,?);");
            ps.setString(1, quoteno);
            ps.setString(2, custcode);
            int res = ps.executeUpdate();
            if (res > 0) {
                result = "Success";
            }
            System.out.println("res=>" + res);
        } catch (Exception e) {
            System.out.println("==>" + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("===================>>>>" + result);
        return result;
    }

    public String getAgencyCode(String portcode) throws RemoteException {
        String agencycode = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        System.out.println("=======getAgencyCode============>>>>");
        try {
            DataSource ds = (DataSource) (new InitialContext()).lookup("java:/COMDS");
            con = ds.getConnection();
            ps = con.prepareStatement(
                    "select agencycode from agencylocations(nolock) where status='A' and agencylocations.portcode=?");
            ps.setString(1, portcode);

            rs = ps.executeQuery();
            if (rs.next()) {
                agencycode = checkNull(rs.getString("agencycode"));
            }
            System.out.println("res=>" + agencycode);
        } catch (Exception e) {
            System.out.println("==>" + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return agencycode;
    }

    public Map getRoutingDetails(Map dataMap) throws RemoteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map result = null;
        String outputJson = null;
        System.out.println("=======getRoutingDetails============>>>>");
        try {

            String shipmentdate = (String) dataMap.get("shipmentDate");
            String origin = (String) dataMap.get("origin");
            String delivery = (String) dataMap.get("destination");
            result = new HashMap<String, String>();
            System.out.println("origin=> " + origin + " &delivery=" + delivery + " @shipmentdate" + shipmentdate);
            // DataSource ds = (DataSource) (new InitialContext()).lookup("java:/COMDS");
            con = getCRMReportConnection();
            ps = con.prepareStatement(
                    "select top 1 blpol,blpolter,blpod,blpodter,mainsvc from routedetail(nolock) where status ='A' and ? between fromdate and todate \n"
                            + "and origin=? and delivery=? order by defroute desc");
            ps.setString(1, shipmentdate);
            ps.setString(2, origin);
            ps.setString(3, delivery);
            rs = ps.executeQuery();
            if (rs.next()) {

                System.out.println("Inside the getRoutingInfo");
                result.put("routepol", checkNull(rs.getString("blpol")));
                result.put("routepolter", checkNull(rs.getString("blpolter")));
                result.put("routepod", checkNull(rs.getString("blpod")));
                result.put("routepodter", checkNull(rs.getString("blpodter")));
                result.put("service", checkNull(rs.getString("mainsvc")));
            }

            System.out.println("res=>" + result);
            // System.out.println("res=>" + result);
        } catch (Exception e) {
            System.out.println("==>" + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // System.out.println("===================>>>>" + result);
        return result;
    }

    public double getTariffAmountForQuote(Map dataMap) throws RemoteException {
        String rturn = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map result = null;
        double ofrd = 0;
        System.out.println("=======getTariffAmount============>>>>" + dataMap);
        try {

            DataSource ds = (DataSource) (new InitialContext()).lookup("java:/COMDS");
            con = ds.getConnection();
            ps = con.prepareStatement(
                    " select top 1 loadport, dischport,eqptype,currency,localamount,validfrom,validto,'' as commodity,soc \n"
                            + "From bookingrates_tariff(nolock) where status='A' and bookingrates_tariff.eqptype=? 	 \n"
                            + " and bookingrates_tariff.validfrom<?\n"
                            + " and bookingrates_tariff.validto>? 	 \n"
                            + " and bookingrates_tariff.loadport=?\n"
                            + " and bookingrates_tariff.dischport=? ");
            ps.setString(1, ((String) dataMap.get("containerType")));
            ps.setString(2, ((String) dataMap.get("shipmentDate")));
            ps.setString(3, getTodate((String) dataMap.get("shipmentDate")));
            ps.setString(4, ((String) dataMap.get("origin")));
            ps.setString(5, ((String) dataMap.get("destination")));

            rs = ps.executeQuery();
            if (rs.next()) {
                rturn = rs.getString("localamount");
            }
            ofrd = (checkNull(rturn).length() > 0 ? Double.valueOf(rturn) : 0);
            result.put("OFR", ofrd);

            System.out.println("getTariffAmount==> " + ofrd);
        } catch (Exception e) {
            System.out.println("==>" + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ofrd;
    }

    private String getTodate(String fromdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String toDateStr = null;
        try {
            // Parse the input date string
            Date fromDate = sdf.parse(fromdate);

            // Create a Calendar instance and set the parsed date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fromDate);

            // Add 30 days to the date
            calendar.add(Calendar.DAY_OF_MONTH, 30);

            // Get the resulting date
            Date toDate = calendar.getTime();

            // Format the resulting date back to a string
            toDateStr = sdf.format(toDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return toDateStr;
    }

    private static Connection getCRMReportConnection() throws SQLException, NamingException {
        DataSource ds = (DataSource) new InitialContext().lookup("java:/REPORTDS");
        return ds.getConnection();
    }

    private static Connection getCRMComConnection() throws SQLException, NamingException {
        DataSource ds = (DataSource) new InitialContext().lookup("java:/COMDS");
        return ds.getConnection();
    }


    public boolean updateQuoteStatus(String quoteNo) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean success = false;
        
        try {
            con = getCRMComConnection();
            
            // Update srrmaster status
            String updateMasterQuery = "update sm set sm.Status='PE' from srrmaster sm where sm.srrno=?";
            ps = con.prepareStatement(updateMasterQuery);
            ps.setString(1, quoteNo);
            int masterUpdateCount = ps.executeUpdate();
            
            // Update srrdetail status
            String updateDetailQuery = "update sd set sd.quoteStatus='R' from srrdetail sd where sd.srrno=?";
            ps = con.prepareStatement(updateDetailQuery);
            ps.setString(1, quoteNo);
            int detailUpdateCount = ps.executeUpdate();
            
            // Consider successful if at least one row was updated in either table
            success = (masterUpdateCount > 0 || detailUpdateCount > 0);
            
        } catch (Exception e) {
            System.out.println("Error updating quote status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return success;
    }
}
