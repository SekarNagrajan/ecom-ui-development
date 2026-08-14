/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

import net.ecom.common.GeneralConf;

/**
 *
 * @author gobinath.t
 */
@WebServlet(name = "QuotationAjax", urlPatterns = {"/QuotationAjax"})
public class QuotationAjax extends HttpServlet {
    String baseAPIURL = "http://192.168.10.91:8060/";//CRMConfig.baseQuoteAPIURL;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            baseAPIURL = (String) request.getServletContext().getAttribute(GeneralConf.Quote_API_URL);
            baseAPIURL=baseAPIURL!=null?baseAPIURL:"http://localhost:8060/";
            String origin = request.getParameter("origin");
            String delivery = request.getParameter("delivery");
            String commodityCode = request.getParameter("commodityCode");
            String weight = request.getParameter("weight");
            String serviceName = request.getParameter("serviceName");
            String serviceCode = request.getParameter("serviceCode");
            String tservicename = request.getParameter("tservicename");
            String requestRate = request.getParameter("requestRate");   
            String containerType = request.getParameter("containerType");
            String custCode = request.getParameter("custCode");
            
            // Create a map with the parameters
            Map<String, Object> quoteMap = new HashMap<>();
            quoteMap.put("origin", origin);
            quoteMap.put("delivery", delivery);
            quoteMap.put("commodity", commodityCode);
            quoteMap.put("weight", weight);
            quoteMap.put("service", serviceCode);
            quoteMap.put("tservicename", tservicename);
            quoteMap.put("requestRate", requestRate);   
            quoteMap.put("containerType", containerType);
            quoteMap.put("custCode", custCode);
            
            // Get surcharge and calculate rate
            String surchargeListJson = getSurcharge(quoteMap);
            
           // Gson gson = new Gson();
           // String surchargeListJson = gson.toJson(surchargeList);
            // Return JSON response
            out.print("{\"status\":\"success\",\"surchargeList\":" + surchargeListJson + "}");
            
        } catch (Exception e) {
            e.printStackTrace();
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
            }
        }
    }

    public String getSurcharge(Map quoteMap) {
        String surList = null;
        try {
      
        Map<String, Object> rateQuote = new HashMap<>();

        rateQuote.put("Allin", quoteMap.get("requestRate"));
        rateQuote.put("commodity", quoteMap.get("commodity"));
        rateQuote.put("delivery", quoteMap.get("delivery"));
        rateQuote.put("TxtEquipment", quoteMap.get("containerType"));
        rateQuote.put("carriercode", "SVM");
        rateQuote.put("origin", quoteMap.get("origin"));
        rateQuote.put("weight", quoteMap.get("weight"));
        rateQuote.put("expfreedays", "1");
        rateQuote.put("CmbCarrCode", quoteMap.get("tservicename"));
        rateQuote.put("CmbCarr", quoteMap.get("service"));
        rateQuote.put("TxtCustCode", quoteMap.get("custCode"));
        rateQuote.put("service", "IME");
        rateQuote.put("impfreedays", "1");
        rateQuote.put("agencycode", getAgencyCode((String)quoteMap.get("origin")));
        rateQuote.put("Bgpprecarriage", "N");
        rateQuote.put("Bgponcarriage", "N");
        rateQuote.put("dallsurcharge_enable", "Y");

        // Print for verification
        System.out.println("New Rate Quote Map: " + rateQuote);
       Gson gson = new Gson();
       String rateQuoteJson = gson.toJson(rateQuote);
        surList= getSurchargeListQuote(rateQuoteJson);
         } catch (RemoteException e) {
            e.printStackTrace();
        }
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
            // String restUrl = "https://lrpv2-demo.solverminds.net/Lamp/SampleAppconfig/getsampleTVAPI/RRvalidation";
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
            //System.out.println("reader==> "+reader.toString());
            result = checkNull(reader.readLine());
            //System.out.println("reader readLine==> " + reader.readLine());
            /* while ((line = reader.readLine()) != null) {
                result.append(line);
            }*/
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

    public String getAgencyCode(String portcode) throws RemoteException {
        String agencycode = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        System.out.println("=======getAgencyCode============>>>>");
        try {
            con = getCRMReportConnection();
            ps = con.prepareStatement("select agencycode from agencylocations(nolock) where status='A' and agencylocations.portcode=?");
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
            ps = con.prepareStatement("select top 1 blpol,blpolter,blpod,blpodter,mainsvc from routedetail(nolock) where status ='A' and ? between fromdate and todate \n"
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
            //System.out.println("res=>" + result);
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
        //  System.out.println("===================>>>>" + result);
        return result;
    }
    private static Connection getCRMReportConnection() throws SQLException, NamingException {
        DataSource ds = (DataSource) new InitialContext().lookup("java:/REPORTDS");
        return ds.getConnection();
    }

    private String checkNull(String str) {
        try {
            str = (str != null && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
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
