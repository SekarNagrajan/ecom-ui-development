/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.ajax;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.AjaxSplitter;
import net.ecom.common.GeneralConf;
import net.ecom.common.NumberFormat;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.common.utils.InsuranceUtils;
import net.ecom.insurance.vo.Address;
import net.ecom.insurance.vo.BookingParty;
import net.ecom.insurance.vo.InsuredGood;
import net.ecom.insurance.vo.InsuredGoodInput;
import net.ecom.insurance.vo.PolicyAttachOutput;
import net.ecom.insurance.vo.PolicyCreateOutput;
import net.ecom.insurance.vo.PolicyGetOutput;
import net.ecom.insurance.vo.PolicyHolder;
import net.ecom.insurance.vo.SwissApiInput;
import net.ecom.insurance.vo.SwissApiOutput;
import net.ecom.insurance.vo.TokenVo;
import net.ecom.insurance.vo.Transportation;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.SurveyList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author gobinath.t
 */
@WebServlet(name = "InsuranceAjax", urlPatterns = {"/InsuranceAjax"})
public class InsuranceAjax extends HttpServlet {

    private ServletContext contexts;
    private HttpSession session;
    private HttpServletRequest I_request;
    private HttpServletResponse I_response;
    private PrintWriter out = null;
    EcomBookingBeanRemote bookbean = EJBLookUp.getBookingBeanLookUp();
    EComCntrlBeanRemote locobj = EJBLookUp.getEcomCntrlBeanLookUp();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        contexts = config.getServletContext();
    }

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
        response.setContentType("text/html;charset=UTF-8");
        out = response.getWriter();
        session = request.getSession();
        try {
             System.out.println("[ECOM-WAR] InsuranceAjax         Inside InsuranceAjax");
            I_request = request;
            I_response = response;
            String baseUrl = (String) contexts.getAttribute(GeneralConf.INSURANCE_BASEURL);
            String attachId = null, type = null, quoteNo = null, policyNo = null;
            type = (String) request.getParameter("type");
            quoteNo = checkNull((String) request.getParameter("quoteno"));
            policyNo = (String) request.getParameter("policyno");
            if ("premium".equalsIgnoreCase(type)) {
                callInsurancePermium(baseUrl);
            } else if ("quotecreate".equalsIgnoreCase(type)) {
                createQuote(baseUrl);
            } else if ("quoteview".equalsIgnoreCase(type)) {
                getQuote(baseUrl, quoteNo);
            } else if ("quoteupdate".equalsIgnoreCase(type)) {
                updateQuote(baseUrl, quoteNo);
            } else if ("buyinsurance".equalsIgnoreCase(type)) {
                createInsurance(baseUrl, quoteNo);
            } else if ("getpolicy".equalsIgnoreCase(type)) {
                getPolicy(baseUrl, policyNo);
            } else if ("getpolicyattach".equalsIgnoreCase(type)) {
                getPolicyAttachment(baseUrl, policyNo);
            } else if ("downloadpolicy".equalsIgnoreCase(type)) {
                getPolicyDownload(baseUrl, policyNo, attachId);
            } else if ("removeInsurance".equalsIgnoreCase(type)) {
                removeInsurance(quoteNo, baseUrl);
            } else if ("getserveylist".equalsIgnoreCase(type)) {
                getServeyList(baseUrl);
            } else if ("optout".equalsIgnoreCase(type)) {
                callOptionalAPI(baseUrl);
            }else if ("removeoptout".equalsIgnoreCase(type)) {
                removeoptout();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createQuote(String baseUrl) {
        HttpPost post = null;
        String restUrl = null;
        String quoteUrl = null;
        HttpResponse response = null;
        String jsonData = null;
        StringBuffer result = null;
        String strToken = null;
        int responseCode = 0;
        Gson gsonObj = null;
        SwissApiOutput outputObj = null;
        try {
            strToken = getBearToken();
            //quoteUrl = "api/insurance/v1/marine/quotes/cargo";
            quoteUrl = "quotes/cargo";
            restUrl = baseUrl + quoteUrl;
            if ((restUrl != null) && (restUrl.trim().length() > 0)) {
                post = new HttpPost(restUrl);
                post.setHeader("Content-Type", "application/json");
                post.setHeader("Accept", "application/json");
                post.setHeader("Authorization", "Bearer " + strToken);
                //executeHttpRequest(jsonData, post);                
                String line = "";
                jsonData = getInputForAPICall(false, null);
                List inputjs = (List) session.getAttribute("CREATE_QUOTE_INPUT_JSON");
                List custtype = (List) session.getAttribute("CREATE_QUOTE_CUST_TYPE");
                List insuredparty = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY");
                List insuredpartyemail = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY_EMAIL");
                List insuredpartyprimaryemail = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY_PRIMARY_EMAIL");
                /*if (inputjs != null) {
                    inputjs.add(jsonData);
                    custtype.add(checkNull(I_request.getParameter("custtype")));
                } else {*/
                inputjs = new ArrayList();
                inputjs.add(jsonData);
                custtype = new ArrayList();
                custtype.add(checkNull(I_request.getParameter("custtype")));
                insuredparty = new ArrayList();
                insuredparty.add(checkNull(I_request.getParameter("custname")).replace("~~", "&"));
                insuredpartyemail = new ArrayList();
                insuredpartyemail.add(checkNull(I_request.getParameter("email")));
                insuredpartyprimaryemail = new ArrayList();
                insuredpartyprimaryemail.add(checkNull(I_request.getParameter("Pemail")));

                 System.out.println("[ECOM-WAR] InsuranceAjax         inputjs=>" + inputjs);
                // }
                if (checkNull(jsonData).length() > 0) {
                    session.setAttribute("CREATE_QUOTE_INPUT_JSON", inputjs);
                    session.setAttribute("CREATE_QUOTE_CUST_TYPE", custtype);
                    session.setAttribute("CREATE_QUOTE_INSURED_PARTY", insuredparty);
                    session.setAttribute("CREATE_QUOTE_INSURED_PARTY_EMAIL", insuredpartyemail);
                    session.setAttribute("CREATE_QUOTE_INSURED_PARTY_PRIMARY_EMAIL", insuredpartyprimaryemail);
                    session.setAttribute("CREATE_QUOTE_NEW", "Y");
                    out.print("success");
                } else {
                    out.print("failure");
                    session.setAttribute("CREATE_QUOTE_INPUT_JSON", null);
                    session.setAttribute("CREATE_QUOTE_CUST_TYPE", null);
                    session.setAttribute("CREATE_QUOTE_INSURED_PARTY", null);
                    session.setAttribute("CREATE_QUOTE_INSURED_PARTY_EMAIL", null);
                    session.setAttribute("CREATE_QUOTE_INSURED_PARTY_PRIMARY_EMAIL", null);
                    session.setAttribute("CREATE_QUOTE_NEW", "N");
                }
                /* System.out.println("[ECOM-WAR] InsuranceAjax         Resturl--->" + restUrl);
                 System.out.println("[ECOM-WAR] InsuranceAjax         INput------->" + jsonData);
                result = new StringBuffer();
                post.setEntity(new StringEntity(jsonData));
                HttpClient client = HttpClientBuilder.create().build();
                response = client.execute(post);

                 System.out.println("[ECOM-WAR] InsuranceAjax         Response Code : " + response.getStatusLine().getStatusCode());
                responseCode = response.getStatusLine().getStatusCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                //
                if (responseCode == 200) {
                    gsonObj = new Gson();
                    outputObj = gsonObj.fromJson(result.toString(), SwissApiOutput.class);
                    I_request.setAttribute("CreateQuoteDetails", outputObj);
                    RequestDispatcher rd = I_request.getRequestDispatcher("/InsurancePremiumDetails.jsp");
                    rd.forward(I_request, I_response);
                }
                //
                //
                if (responseCode == 204) {
                    session.setAttribute("INSURANCE_API_TOKEN", "");
                    createQuote(baseUrl);
                }
                //
                 System.out.println("[ECOM-WAR] InsuranceAjax         Output---->" + result);*/
            } else {
                 System.out.println("[ECOM-WAR] InsuranceAjax         ====== Invalid Connectivity ======");
            }
        } catch (Exception e) {
             System.out.println("[ECOM-WAR] InsuranceAjax         Exception at createConnectivity >>>>" + e.getMessage());
        }
    }

    private String getBearToken() {
        String strToken = null;
        //NPD
        String strClientSecretCode = (String) contexts.getAttribute(GeneralConf.INSURANCE_TOKENSECRETCODE);
        String tokenUrl = (String) contexts.getAttribute(GeneralConf.INSURANCE_TOKENURL);
        String encCode = null;

        String scopes = "category_marinecargo";
        ArrayList<NameValuePair> postParameters = null;
        TokenVo tokenObj = null;
        Gson gson = null;
        //
        HttpPost post = null;
        HttpResponse response = null;
        try {
            String session_token = (String) session.getAttribute("INSURANCE_API_TOKEN");
            Date session_token_date = (Date) session.getAttribute("INSURANCE_API_TOKEN_CREATED_DATE");
             System.out.println("[ECOM-WAR] InsuranceAjax         session_token_date==> " + session_token_date);
            Date current_date = new Date();
            long token_diffMinutes = 60;
            if (session_token_date != null) {
                long date_diff = current_date.getTime() - session_token_date.getTime();
                token_diffMinutes = date_diff / (60 * 1000) % 60;
            }
            if (session_token != null && session_token.length() > 0 && token_diffMinutes < 4 && false) {
                strToken = session_token;
            } else {
                encCode = new String(Base64.getEncoder().encode(strClientSecretCode.getBytes()));
                //
                 System.out.println("[ECOM-WAR] InsuranceAjax         Encode  " + encCode);
                post = new HttpPost(tokenUrl);
                post.setHeader("Authorization", "Basic " + encCode);
                post.setHeader("Content-Type", "application/x-www-form-urlencoded");
                //post.setHeader("Accept", "application/json");
                // 
                postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
                postParameters.add(new BasicNameValuePair("scope", "category_marinecargo"));

                //post.setHeader("X-Stream", "true");
                //executeHttpRequest(jsonData, post);     
                post.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
                HttpClient client = HttpClientBuilder.create().build();
                response = client.execute(post);
                //
                String line = "";
                StringBuffer result = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                //
                gson = new Gson();
                tokenObj = gson.fromJson(result.toString(), TokenVo.class);
                 System.out.println("[ECOM-WAR] InsuranceAjax         Output 123---->" + result);
                strToken = tokenObj.getAccess_token();
                session.setAttribute("INSURANCE_API_TOKEN", strToken);
                session.setAttribute("INSURANCE_API_TOKEN_CREATED_DATE", new Date());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strToken;
    }

    private String getInputForAPICall(boolean isQuoteNoAvail, String quoteNo) {
        String jsonValue = null;
        SwissApiInput rootObj = null;
        PolicyHolder policyObj = null;
        Address addObj = null;
        Transportation tranObj = null;
        List<InsuredGoodInput> insuredGoods = null;
        InsuredGoodInput cargoObj = null;
        Gson gson = new Gson();
        try {
            Map vesselIMO = (Map) contexts.getAttribute("VesselIMODetails");
            //String ADD_INURANCEVALUE_PREFIX = checkNull((String) contexts.getAttribute("ADD_INURANCEVALUE_PREFIX"));
            String state = checkNull(I_request.getParameter("state"));
             System.out.println("[ECOM-WAR] InsuranceAjax         state==>" + state);
            state = ((state.length() > 0 && state.split("-").length > 1) ? (state.split("-")[1]) : "");
            String country = checkNull(I_request.getParameter("country"));
            country = checkNull(country.length() > 0 ? (country.split("-")[0]) : "");
            String origin = checkNull(I_request.getParameter("origin"));
            origin = checkNull(origin.length() > 0 ? (origin.split("-")[0]) : "");
            String delivery = checkNull(I_request.getParameter("delivery"));
            delivery = checkNull(delivery.length() > 0 ? (delivery.split("-")[0]) : "");
            Date shipmentdate = DateConversion_New.convertStringToDate(checkNull(I_request.getParameter("shipmentdate")));
            String onlinebookno = checkNull(I_request.getParameter("onlinebookno"));
            if (onlinebookno.length() == 0) {
                onlinebookno = "@bookno";
            }
            //
            List<String> addEmailLis = Arrays.asList(checkNull(I_request.getParameter("email")).split(","));
            //
            //policyHolder
             System.out.println("[ECOM-WAR] InsuranceAjax         add=> " + checkNull(I_request.getParameter("address")));
            policyObj = new PolicyHolder();
            addObj = new Address();
            addObj.setStreetLine1(checkNull(I_request.getParameter("address")));
            addObj.setStreetLine2(checkNull(I_request.getParameter("address1")));
            addObj.setCity(checkNull(I_request.getParameter("city")));
            addObj.setState(state);
            addObj.setCountry(country);
            addObj.setPostalCode("-");
            addObj.setType("PrincipalAddress");
            //
            policyObj.setInsuredName(checkNull(I_request.getParameter("custname")).replace("~~", "&"));
            policyObj.setAddress(addObj);
            //policyObj.setCompanyId("ESL");
            policyObj.setPrimaryEmailAddress(checkNull(I_request.getParameter("Pemail")));
            policyObj.setAdditionalEmailAddresses(addEmailLis);
            //Transportation
            tranObj = new Transportation();
            tranObj.setPortOfOrigin(origin);
            tranObj.setPortOfDestination(delivery);

            tranObj.setVesselName(checkNull(I_request.getParameter("vessel")));
            tranObj.setImoNumber(checkNull((String) vesselIMO.get(checkNull(I_request.getParameter("vesselcode")))));
            tranObj.setShipmentDateTime(shipmentdate.toInstant().toString());
            tranObj.setShipmentServiceCode(checkNull(I_request.getParameter("shipmentservicecode")));

            BookingParty bookingParty = new BookingParty();
            bookingParty.setContactType(checkNull(I_request.getParameter("custtype")));
            bookingParty.setContactName(checkNull((String) session.getAttribute("SESSION_USERNAME")));
            bookingParty.setAddress(addObj);
            bookingParty.setPrimaryEmailAddress(checkNull(I_request.getParameter("Pemail")));
            policyObj.setAdditionalEmailAddresses(addEmailLis);

            String commoditystr = checkNull(I_request.getParameter("commodityvalue"));
            ArrayList comlis = new ArrayList(Arrays.asList(commoditystr.split("@@@@@")));
            //Goods
            if (comlis != null && comlis.size() > 0) {
                String hscode = null, cargoval = null;
                String[] hsarr = null;
                insuredGoods = new ArrayList<InsuredGoodInput>();
                for (int i = 0; i < comlis.size(); i++) {
                    cargoval = null;
                    hscode = (String) comlis.get(i);
//                     System.out.println("[ECOM-WAR] InsuranceAjax         hscode==> " + hscode);
                    hsarr = hscode.split("~~~");
                    hscode = checkNull(hsarr[0]);
                    if (hsarr.length > 1) {
                        cargoval = checkNull(NumberFormat.decimalFormat(checkNull(hsarr[1])));
                    }
                    if (cargoval == null || cargoval.length() == 0) {
                        cargoval = "0";
                    }
//                     System.out.println("[ECOM-WAR] InsuranceAjax         hscode==> " + hscode);
                    if (hscode.split("~~").length > 1) {
                        hscode = checkNull(hscode.split("~~")[1]);
                    } else {
                        hscode = null;
                    }
//                     System.out.println("[ECOM-WAR] InsuranceAjax         hscode==> " + hscode);
                    cargoObj = new InsuredGoodInput();
                    cargoObj.setCurrency("USD");
                    cargoObj.setHarmonizedSystemCode(hscode);
                    cargoObj.setSumInsured(cargoval);
                    insuredGoods.add(cargoObj);
                }
            }
            //root
            rootObj = new SwissApiInput();
            //rootObj.setInceptionDate(new Date().toInstant().toString());
            rootObj.setOnlineBookingNumber(onlinebookno);
            rootObj.setInsuredGoods(insuredGoods);
            rootObj.setPolicyHolder(policyObj);
            rootObj.setTransportation(tranObj);
            rootObj.setBookingParty(bookingParty);
            //
            if (isQuoteNoAvail) {
                rootObj.setQuoteNumber(quoteNo);
            }
            //
            jsonValue = gson.toJson(rootObj);
             System.out.println("[ECOM-WAR] InsuranceAjax         INPUT====> " + jsonValue);
            jsonValue = jsonValue.replaceAll("[^\\x00-\\x7F]", " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonValue;
    }

    public void createInsurance(String baseUrl, String quoteNo) {
        HttpPost post = null;
        String restUrl = null;
        String insuranceUrl = null;
        HttpResponse response = null;
        String jsonData = null;
        StringBuffer result = null;
        String strToken = null;
        int responseCode = 0;
        Gson gsonObj = null;
        PolicyCreateOutput outputObj = null;
        //
        try {
            strToken = getBearToken();
            //insuranceUrl = "api/insurance/v1/marine/policies/cargo";
            insuranceUrl = "policies/cargo";
            restUrl = baseUrl + insuranceUrl;
            if ((restUrl != null) && (restUrl.trim().length() > 0)) {
                post = new HttpPost(restUrl);
                post.setHeader("Content-Type", "application/json");
                post.setHeader("Accept", "application/json");
                post.setHeader("Authorization", "Bearer " + strToken);
                //executeHttpRequest(jsonData, post);                
                String line = "";
                jsonData = getInputForAPICall(true, quoteNo);
//                 System.out.println("[ECOM-WAR] InsuranceAjax         Resturl--->" + restUrl);
                 System.out.println("[ECOM-WAR] InsuranceAjax         INput------->" + jsonData+",restUrl:"+restUrl);
                result = new StringBuffer();
                post.setEntity(new StringEntity(jsonData));
                HttpClient client = HttpClientBuilder.create().build();
                response = client.execute(post);

                 System.out.println("[ECOM-WAR] InsuranceAjax         Response Code : " + response.getStatusLine().getStatusCode());
                responseCode = response.getStatusLine().getStatusCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                //
                if (responseCode == 200) {
                    gsonObj = new Gson();
                    outputObj = gsonObj.fromJson(result.toString(), PolicyCreateOutput.class);
                }
                 System.out.println("[ECOM-WAR] InsuranceAjax         Output---->" + result);
            } else {
                 System.out.println("[ECOM-WAR] InsuranceAjax         ====== Invalid Connectivity ======");
            }
        } catch (Exception e) {
             System.out.println("[ECOM-WAR] InsuranceAjax         Exception at createConnectivity >>>>" + e.getMessage());
        }
    }

    public void callInsurancePermium(String baseUrl) {
        HttpPost post = null;
        String restUrl = null;
        String premiumUrl = null;
        String quoteUrl = null;
        HttpResponse response = null;
        String jsonData = null;
        StringBuffer result = null;
        String strToken = null;
        int responseCode = 0;
        Gson gsonObj = null;
        SwissApiOutput outputObj = null, currentoutputObj = null;
        Map errormap = null;
        try {
            strToken = getBearToken();
            premiumUrl = "rating/cargo";
            // quoteUrl = "api/insurance/v1/marine/quotes/cargo";
            restUrl = baseUrl + premiumUrl;
            if ((restUrl != null) && (restUrl.trim().length() > 0)) {
                post = new HttpPost(restUrl);
                post.setHeader("Content-Type", "application/json");
                post.setHeader("Accept", "application/json");
                post.setHeader("Authorization", "Bearer " + strToken);
                //executeHttpRequest(jsonData, post);                
                String line = "";
                jsonData = getInputForAPICall(false, null);
                 System.out.println("[ECOM-WAR] InsuranceAjax         Resturl--->" + restUrl+",jsonData:"+jsonData);
//                 System.out.println("[ECOM-WAR] InsuranceAjax         INput------->" + jsonData);
                result = new StringBuffer();
                post.setEntity(new StringEntity(jsonData));
                HttpClient client = HttpClientBuilder.create().build();
                response = client.execute(post);
                responseCode = response.getStatusLine().getStatusCode();
                 System.out.println("[ECOM-WAR] InsuranceAjax         Response Code : " + response.getStatusLine().getStatusCode());
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                 System.out.println("[ECOM-WAR] InsuranceAjax         result==> " + result);
                //result.append("{ \"quoteNumber\": \"{{quoteNumber}}\", \"onlineBookingNumber\": \"TAR-123456\", \"totalPremium\": 15000, \"currency\": \"Usd\", \"insuredGoods\": [  {   \"harmonizedSystemCode\": \"hsCode_2\",   \"currency\": \"Usd\",   \"sumInsured\": 100000  },  {   \"harmonizedSystemCode\": \"hsCode_2\",   \"currency\": \"Usd\",   \"sumInsured\": 100000  } ]}");
                //
                if (responseCode == 200) {
                    gsonObj = new Gson();
                    outputObj = gsonObj.fromJson(result.toString(), SwissApiOutput.class);
                     System.out.println("[ECOM-WAR] InsuranceAjax         outputObj==> " + outputObj);
                    I_request.setAttribute("quoteno", I_request.getParameter("quoteno"));
                    if (checkNull(I_request.getParameter("quoteno")).length() > 0) {
                        currentoutputObj = (SwissApiOutput) session.getAttribute("EditedQuoteDetails");
                        I_request.setAttribute("isrecalculated", "Y");
                        I_request.setAttribute("newpremiumtotal", outputObj.getTotalPremium());
                        I_request.setAttribute("currentpremiumtotal", currentoutputObj.getTotalPremium());

                        I_request.setAttribute("newpremiumcurrency", outputObj.getCurrency());
                        I_request.setAttribute("currentpremiumcurrency", currentoutputObj.getCurrency());
                        InsuranceUtils insoj = new InsuranceUtils();
                        Map mergepremium = insoj.mergeOldAndNewPremium(currentoutputObj.getInsuredGoods(), outputObj.getInsuredGoods());
                        I_request.setAttribute("recalculatedpremium", mergepremium);
                    }
                    I_request.setAttribute("CreateQuoteDetails", outputObj);
                    RequestDispatcher rd = I_request.getRequestDispatcher("/InsurancePremiumDetails.jsp");
                    rd.forward(I_request, I_response);

                }
                //
                //
                if (responseCode == 204) {
                    session.setAttribute("INSURANCE_API_TOKEN", "");
                    createQuote(baseUrl);
                }
                if (responseCode != 200 && responseCode != 204) {
                    gsonObj = new Gson();
                    errormap = gsonObj.fromJson(result.toString(), Map.class);
                     System.out.println("[ECOM-WAR] InsuranceAjax         outputObj==> " + errormap);
                    I_request.setAttribute("ErrorMessage", errormap);
                    I_request.setAttribute("ErrorOccured", "Y");
                    RequestDispatcher rd = I_request.getRequestDispatcher("/InsurancePremiumDetails.jsp");
                    rd.forward(I_request, I_response);
                }
                 System.out.println("[ECOM-WAR] InsuranceAjax         Output---->" + result);
            } else {
                 System.out.println("[ECOM-WAR] InsuranceAjax         ====== Invalid Connectivity ======");
            }
        } catch (Exception e) {
             System.out.println("[ECOM-WAR] InsuranceAjax         Exception at createConnectivity >>>>" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void getQuote(String baseUrl, String quoteNo) {
        HttpGet get = null;
        String restUrl = null;
        String getquoteUrl = null;
        HttpResponse response = null;
        StringBuffer result = null;
        String strToken = null;
        int responseCode = 0;
        Gson gsonObj = null;
        SwissApiOutput outputObj = null;
        try {
            strToken = getBearToken();
            getquoteUrl = "quotes/cargo/@quoteNumber";
            //getquoteUrl = "api/insurance/v1/marine/quotes/cargo/@quoteNumber";
            getquoteUrl = getquoteUrl.replaceAll("@quoteNumber", quoteNo);
            restUrl = baseUrl + getquoteUrl;
            if ((restUrl != null) && (restUrl.trim().length() > 0)) {
                get = new HttpGet(restUrl);
                get.setHeader("Content-Type", "application/x-www-form-urlencoded");
                get.setHeader("Authorization", "Bearer " + strToken);
                //executeHttpRequest(jsonData, post);                
                String line = "";
                 System.out.println("[ECOM-WAR] InsuranceAjax         Resturl--->" + restUrl);
                result = new StringBuffer();
                HttpClient client = HttpClientBuilder.create().build();
                response = client.execute(get);

                 System.out.println("[ECOM-WAR] InsuranceAjax         Response Code : " + response.getStatusLine().getStatusCode());
                responseCode = response.getStatusLine().getStatusCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                //
                if (responseCode == 200) {
                    gsonObj = new Gson();
                    outputObj = gsonObj.fromJson(result.toString(), SwissApiOutput.class);
                     System.out.println("[ECOM-WAR] InsuranceAjax         outputObj==> " + outputObj);
                    I_request.setAttribute("CreateQuoteDetails", outputObj);
                    if ("Y".equalsIgnoreCase(checkNull(I_request.getParameter("iseditload")))) {
                        session.setAttribute("EditedQuoteDetails", outputObj);
                    }
                    I_request.setAttribute("quoteno", quoteNo);
                    I_request.setAttribute("iseditload", I_request.getParameter("iseditload"));
                    if (!"N".equalsIgnoreCase(checkNull(I_request.getParameter("logotype")))) {
                        I_request.setAttribute("ViewType", "view");
                    }
                    RequestDispatcher rd = I_request.getRequestDispatcher("/InsurancePremiumDetails.jsp");
                    rd.forward(I_request, I_response);
                }
                 System.out.println("[ECOM-WAR] InsuranceAjax         Output---->" + result);
            } else {
                 System.out.println("[ECOM-WAR] InsuranceAjax         ====== Invalid Connectivity ======");
            }
        } catch (Exception e) {
             System.out.println("[ECOM-WAR] InsuranceAjax         Exception at createConnectivity >>>>" + e.getMessage());
        }
    }

    public void updateQuote(String baseUrl, String quoteNo) {
        HttpPut put = null;
        String restUrl = null;
        String quoteUrl = null;
        HttpResponse response = null;
        String jsonData = null;
        StringBuffer result = null;
        String strToken = null;
        int responseCode = 0;
        Gson gsonObj = null;
        SwissApiOutput outputObj = null;
        try {
            strToken = getBearToken();
            //quoteUrl = "api/insurance/v1/marine/quotes/cargo";
            quoteUrl = "quotes/cargo";
            restUrl = baseUrl + quoteUrl;
            if ((restUrl != null) && (restUrl.trim().length() > 0)) {
                put = new HttpPut(restUrl);
                put.setHeader("Content-Type", "application/json");
                put.setHeader("Accept", "application/json");
                put.setHeader("Authorization", "Bearer " + strToken);
                //executeHttpRequest(jsonData, post);                
                String line = "";
                jsonData = getInputForAPICall(true, quoteNo);
                List inputjs = (List) session.getAttribute("CREATE_QUOTE_INPUT_JSON");
                List custtype = (List) session.getAttribute("CREATE_QUOTE_CUST_TYPE");
                List insuredparty = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY");
                List insuredpartyemail = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY_EMAIL");
                List insuredpartyprimaryemail = (List) session.getAttribute("CREATE_QUOTE_INSURED_PARTY_PRIMARY_EMAIL");
                /*if (inputjs != null) {
                    inputjs.add(jsonData);
                    custtype.add(checkNull(I_request.getParameter("custtype")));
                } else {*/
                inputjs = new ArrayList();
                inputjs.add(jsonData);
                custtype = new ArrayList();
                custtype.add(checkNull(I_request.getParameter("custtype")));
                insuredparty = new ArrayList();
                insuredparty.add(checkNull(I_request.getParameter("custname")).replace("~~", "&"));
                insuredpartyemail = new ArrayList();
                insuredpartyemail.add(checkNull(I_request.getParameter("email")));
                insuredpartyprimaryemail = new ArrayList();
                insuredpartyprimaryemail.add(checkNull(I_request.getParameter("Pemail")));
                 System.out.println("[ECOM-WAR] InsuranceAjax         inputjs=>" + inputjs);
                // }
                if (checkNull(jsonData).length() > 0) {
                    session.setAttribute("CREATE_QUOTE_INPUT_JSON", inputjs);
                    session.setAttribute("CREATE_QUOTE_CUST_TYPE", custtype);
                    session.setAttribute("CREATE_QUOTE_INSURED_PARTY", insuredparty);
                    session.setAttribute("CREATE_QUOTE_INSURED_PARTY_EMAIL", insuredpartyemail);
                    session.setAttribute("CREATE_QUOTE_INSURED_PARTY_PRIMARY_EMAIL", insuredpartyprimaryemail);
                    session.setAttribute("CREATE_QUOTE_NEW", "Y");
                    out.print("success");
                } else {
                    out.print("failure");
                    session.setAttribute("CREATE_QUOTE_INPUT_JSON", null);
                    session.setAttribute("CREATE_QUOTE_CUST_TYPE", null);
                    session.setAttribute("CREATE_QUOTE_INSURED_PARTY", null);
                    session.setAttribute("CREATE_QUOTE_INSURED_PARTY_EMAIL", null);
                    session.setAttribute("CREATE_QUOTE_INSURED_PARTY_PRIMARY_EMAIL", null);
                    session.setAttribute("CREATE_QUOTE_NEW", "N");
                }
                /*
                 System.out.println("[ECOM-WAR] InsuranceAjax         Resturl--->" + restUrl);
                 System.out.println("[ECOM-WAR] InsuranceAjax         INput------->" + jsonData);
                result = new StringBuffer();
                put.setEntity(new StringEntity(jsonData));
                HttpClient client = HttpClientBuilder.create().build();
                response = client.execute(put);
                //
                 System.out.println("[ECOM-WAR] InsuranceAjax         Response Code : " + response.getStatusLine().getStatusCode());
                responseCode = response.getStatusLine().getStatusCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                //
                if (responseCode == 200) {
                    gsonObj = new Gson();
                    outputObj = gsonObj.fromJson(result.toString(), SwissApiOutput.class);
                }
                 System.out.println("[ECOM-WAR] InsuranceAjax         Output---->" + result);*/
            } else {
                 System.out.println("[ECOM-WAR] InsuranceAjax         ====== Invalid Connectivity ======");
            }
        } catch (Exception e) {
             System.out.println("[ECOM-WAR] InsuranceAjax         Exception at createConnectivity >>>>" + e.getMessage());
        }
    }

    public void getPolicy(String baseUrl, String policyNo) {
        HttpGet get = null;
        String restUrl = null;
        String getquoteUrl = null;
        HttpResponse response = null;
        StringBuffer result = null;
        String strToken = null;
        //
        int responseCode = 0;
        Gson gsonObj = null;
        PolicyGetOutput outputObj = null;
        try {
            strToken = getBearToken();
            //getquoteUrl = "api/insurance/v1/marine/policies/cargo/@policyNumber";
            getquoteUrl = "policies/cargo/@policyNumber";
            getquoteUrl = getquoteUrl.replaceAll("@policyNumber", policyNo);
            restUrl = baseUrl + getquoteUrl;
            if ((restUrl != null) && (restUrl.trim().length() > 0)) {
                get = new HttpGet(restUrl);
                get.setHeader("Content-Type", "application/x-www-form-urlencoded");
                get.setHeader("Authorization", "Bearer " + strToken);
                //executeHttpRequest(jsonData, post);                
                String line = "";
                 System.out.println("[ECOM-WAR] InsuranceAjax         Resturl--->" + restUrl);
                result = new StringBuffer();
                HttpClient client = HttpClientBuilder.create().build();
                response = client.execute(get);
                //
                responseCode = response.getStatusLine().getStatusCode();
                 System.out.println("[ECOM-WAR] InsuranceAjax         Response Code : " + response.getStatusLine().getStatusCode());
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                if (responseCode == 200) {
                    gsonObj = new Gson();
                    outputObj = gsonObj.fromJson(result.toString(), PolicyGetOutput.class);
                     System.out.println("[ECOM-WAR] InsuranceAjax         outputObj==> " + outputObj);
                    I_request.setAttribute("PolicyViewDetails", outputObj);
                    RequestDispatcher rd = I_request.getRequestDispatcher("/InsurancePolicyView.jsp");
                    rd.forward(I_request, I_response);
                }
                 System.out.println("[ECOM-WAR] InsuranceAjax         Output---->" + result);
            } else {
                 System.out.println("[ECOM-WAR] InsuranceAjax         ====== Invalid Connectivity ======");
            }
        } catch (Exception e) {
             System.out.println("[ECOM-WAR] InsuranceAjax         Exception at createConnectivity >>>>" + e.getMessage());
        }
    }

    public void getPolicyAttachment(String baseUrl, String policyNo) {
        HttpGet get = null;
        String restUrl = null;
        String getquoteUrl = null;
        HttpResponse response = null;
        StringBuffer result = null;
        String strToken = null;
        int responseCode = 0;
        Gson gsonObj = null;
        PolicyAttachOutput outputObj = null;
        try {
            strToken = getBearToken();
            getquoteUrl = "policies/cargo/@policyNumber/attachments";
            //getquoteUrl = "api/insurance/v1/marine/policies/cargo/@policyNumber/attachments";
            getquoteUrl = getquoteUrl.replaceAll("@policyNumber", policyNo);
            restUrl = baseUrl + getquoteUrl;
            if ((restUrl != null) && (restUrl.trim().length() > 0)) {
                get = new HttpGet(restUrl);
                get.setHeader("Content-Type", "application/x-www-form-urlencoded");
                get.setHeader("Authorization", "Bearer " + strToken);
                //executeHttpRequest(jsonData, post);                
                String line = "";
                 System.out.println("[ECOM-WAR] InsuranceAjax         Resturl--->" + restUrl);
                result = new StringBuffer();
                HttpClient client = HttpClientBuilder.create().build();
                response = client.execute(get);
                //
                responseCode = response.getStatusLine().getStatusCode();
                 System.out.println("[ECOM-WAR] InsuranceAjax         Response Code : " + response.getStatusLine().getStatusCode());
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                //
                if (responseCode == 200) {
                    gsonObj = new Gson();
                    outputObj = gsonObj.fromJson(result.toString(), PolicyAttachOutput.class);
                }
                 System.out.println("[ECOM-WAR] InsuranceAjax         Output---->" + result);
            } else {
                 System.out.println("[ECOM-WAR] InsuranceAjax         ====== Invalid Connectivity ======");
            }
        } catch (Exception e) {
             System.out.println("[ECOM-WAR] InsuranceAjax         Exception at createConnectivity >>>>" + e.getMessage());
        }
    }

    public void getPolicyDownload(String baseUrl, String policyNo, String attachId) {
        HttpGet get = null;
        String restUrl = null;
        String getquoteUrl = null;
        HttpResponse response = null;
        StringBuffer result = null;
        String strToken = null;
        String strattachId = null;
        try {
            strToken = getBearToken();
            //getquoteUrl = "api/insurance/v1/marine/policies/cargo/@policyNumber/attachments/:attachmentId/download";
            getquoteUrl = "policies/cargo/@policyNumber/attachments/:attachmentId/download";
            getquoteUrl = getquoteUrl.replaceAll("@policyNumber", policyNo);
            getquoteUrl = getquoteUrl.replaceAll(":attachmentId", attachId);
            restUrl = baseUrl + getquoteUrl;
            if ((restUrl != null) && (restUrl.trim().length() > 0)) {
                get = new HttpGet(restUrl);
                get.setHeader("Content-Type", "application/x-www-form-urlencoded");
                get.setHeader("Authorization", "Bearer " + strToken);
                //executeHttpRequest(jsonData, post);                
                String line = "";
                 System.out.println("[ECOM-WAR] InsuranceAjax         Resturl--->" + restUrl);
                result = new StringBuffer();
                HttpClient client = HttpClientBuilder.create().build();
                response = client.execute(get);

                 System.out.println("[ECOM-WAR] InsuranceAjax         Response Code : " + response.getStatusLine().getStatusCode());
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                 System.out.println("[ECOM-WAR] InsuranceAjax         Output---->" + result);
            } else {
                 System.out.println("[ECOM-WAR] InsuranceAjax         ====== Invalid Connectivity ======");
            }
        } catch (Exception e) {
             System.out.println("[ECOM-WAR] InsuranceAjax         Exception at createConnectivity >>>>" + e.getMessage());
        }
    }

    private void removeInsurance(String quoteno, String baseUrl) {

        HttpPost post = null;
        String restUrl = null;
        String surveyUrl = null;
        HttpResponse response = null;
        String jsonData = null;
        StringBuffer result = null;
        String strToken = null;
        int responseCode = 0;
        Gson gsonObj = null;
        SwissApiOutput outputObj = null;
        Map inputmap = null;
        try {
            gsonObj = new Gson();
            session.setAttribute("CREATE_QUOTE_INPUT_JSON", null);
            session.setAttribute("CREATE_QUOTE_CUST_TYPE", null);
            session.setAttribute("CREATE_QUOTE_INSURED_PARTY", null);
            session.setAttribute("CREATE_QUOTE_INSURED_PARTY_EMAIL", null);
            session.setAttribute("CREATE_QUOTE_INSURED_PARTY_PRIMARY_EMAIL", null);
            session.setAttribute("CREATE_QUOTE_NEW", null);

            strToken = getBearToken();
            //surveyUrl = "api/insurance/v1/marine/quotes/survey";
            surveyUrl = "quotes/survey";
            restUrl = baseUrl + surveyUrl;
            if ((restUrl != null) && (restUrl.trim().length() > 0)) {
                post = new HttpPost(restUrl);
                post.setHeader("Content-Type", "application/json");
                post.setHeader("Accept", "application/json");
                post.setHeader("Authorization", "Bearer " + strToken);
                //executeHttpRequest(jsonData, post);                
                String line = "";
                //
                String book_no = checkNull(I_request.getParameter("surveybook_no"));
                String surveystr = checkNull(I_request.getParameter("surveymsg"));
                String customsurvey = checkNull(I_request.getParameter("surveycustominput"));
                String contactme = checkNull(I_request.getParameter("contactme"));
                 System.out.println("[ECOM-WAR] InsuranceAjax         book_no=" + book_no + "&surveystr=" + surveystr + "&customsurvey=" + customsurvey + "&contactme=" + contactme + "&quoteno=" + quoteno);
                List surveylist = null, surveyinsertlis = null;
                SurveyList surobj = null;
                if (surveystr != null && surveystr.length() > 0) {
                    surveyinsertlis = new ArrayList<SurveyList>();
                    Map Insurance_Survey_List = (Map) contexts.getAttribute("Insurance_Survey_List");
                    surveylist = new ArrayList<String>(Arrays.asList(surveystr.split(",")));
                    for (int i = 0; i < surveylist.size(); i++) {
                        surobj = (SurveyList) Insurance_Survey_List.get((String) surveylist.get(i));
                        surveyinsertlis.add(surobj);
                    }
                    //
                    inputmap = new HashMap();
                    inputmap.put("version", surobj.getVersion());
                    inputmap.put("quoteId", quoteno);
                    inputmap.put("surveyOptionCode", surobj.getSurveyOptionCode());
                    inputmap.put("isContactMe", ("Y".equalsIgnoreCase(contactme) ? true : false));
                    inputmap.put("customFeedback", customsurvey);
                    jsonData = gsonObj.toJson(inputmap);
                    jsonData = jsonData.replaceAll("[^\\x00-\\x7F]", " ");
                     System.out.println("[ECOM-WAR] InsuranceAjax         Resturl--->" + restUrl);
                     System.out.println("[ECOM-WAR] InsuranceAjax         INput------->" + jsonData);
                    result = new StringBuffer();
                    post.setEntity(new StringEntity(jsonData));
                    HttpClient client = HttpClientBuilder.create().build();
                    response = client.execute(post);
                    responseCode = response.getStatusLine().getStatusCode();
                     System.out.println("[ECOM-WAR] InsuranceAjax         Response Code : " + response.getStatusLine().getStatusCode());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                     System.out.println("[ECOM-WAR] InsuranceAjax         result==> " + result);
                    if (responseCode == 200) {
                        if (checkNull(quoteno).length() > 0) {
                            bookbean.removeInsuranceDetails(quoteno, book_no, surveyinsertlis);
                        }
                        out.print("success");
                    } else {
                        gsonObj = new Gson();
                        Map errormap = gsonObj.fromJson(result.toString(), Map.class);
                        String error = "";
                         System.out.println("[ECOM-WAR] InsuranceAjax         outputObj==> " + errormap);
                        if (errormap != null) {
                            List errlis = (List) errormap.get("errors");
                            if (errlis != null && errlis.size() > 0) {
                                for (int i = 0; i < errlis.size(); i++) {
                                    errormap = (Map) errlis.get(i);
                                    if (errormap != null) {
                                        error = error + checkNull((String) errormap.get("message")) + " \n";
                                    }
                                }
                            }
                            out.print(error);
                        }
                    }
                    //
                     System.out.println("[ECOM-WAR] InsuranceAjax         Output---->" + result);
                } else {
                    if (checkNull(quoteno).length() == 0) {
                        out.print("success");
                    }
                }
            } else {
                 System.out.println("[ECOM-WAR] InsuranceAjax         ====== Invalid Connectivity ======");
            }
        } catch (Exception e) {
            out.print("Error!!");
             System.out.println("[ECOM-WAR] InsuranceAjax         Exception at createConnectivity >>>>" + e.getMessage());
        }
    }

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    public void getServeyList(String baseUrl) {
        HttpGet get = null;
        String restUrl = null;
        String getserveyUrl = null;
        HttpResponse response = null;
        StringBuffer result = null;
        String strToken = null;
        int responseCode = 0;
        try {
             System.out.println("[ECOM-WAR] InsuranceAjax         inside getServeyList");
            strToken = getBearToken();
            //getserveyUrl = "api/insurance/v1/marine/quotes/surveyoptions";
            getserveyUrl = "quotes/surveyoptions";
            restUrl = baseUrl + getserveyUrl;
            if ((restUrl != null) && (restUrl.trim().length() > 0)) {
                get = new HttpGet(restUrl);
                get.setHeader("Content-Type", "application/x-www-form-urlencoded");
                get.setHeader("Authorization", "Bearer " + strToken);
                String line = "";
                 System.out.println("[ECOM-WAR] InsuranceAjax         Resturl--->" + restUrl);
                result = new StringBuffer();
                HttpClient client = HttpClientBuilder.create().build();
                response = client.execute(get);

                 System.out.println("[ECOM-WAR] InsuranceAjax         Response Code : " + response.getStatusLine().getStatusCode());
                responseCode = response.getStatusLine().getStatusCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                //
                 System.out.println("[ECOM-WAR] InsuranceAjax         Survey List=>  " + result.toString());
                if (responseCode == 200) {
                    InsuranceUtils insoj = new InsuranceUtils();
                    contexts.setAttribute("Insurance_Survey_List", insoj.getSurveyList(result.toString()));
                    out.print(locobj.putInsuranceSurveyList(result.toString()));
                }
            } else {
                 System.out.println("[ECOM-WAR] InsuranceAjax         ====== Invalid Connectivity ======");
                out.print("ERROR");
            }
        } catch (Exception e) {
            out.print("ERROR");
             System.out.println("[ECOM-WAR] InsuranceAjax         Exception at createConnectivity >>>>" + e.getMessage());
        }
    }

    public void callOptionalAPI(String baseUrl) {
        HttpPost post = null;
        String restUrl = null;
        String optoutUrl = null;
        HttpResponse response = null;
        String jsonData = null;
        StringBuffer result = null;
        String strToken = null;
        int responseCode = 0;
        Gson gsonObj = null;
        SwissApiOutput outputObj = null, currentoutputObj = null;
        Map errormap = null;
        try {
            strToken = getBearToken();
            optoutUrl = "optout/cargo";
            // optoutUrl = "api/insurance/v2/marine/optout/cargo";
            restUrl = baseUrl + optoutUrl;
            if ((restUrl != null) && (restUrl.trim().length() > 0)) {
//                post = new HttpPost(restUrl);
//                post.setHeader("Content-Type", "application/json"); 
//                post.setHeader("Accept", "application/json");
//                post.setHeader("Authorization", "Bearer " + strToken);
                //executeHttpRequest(jsonData, post);

                jsonData = getInputForAPICall(false, null);

                 System.out.println("[ECOM-WAR] InsuranceAjax         jsonData=>" + jsonData);

                if (checkNull(jsonData).length() > 0) {
                    session.setAttribute("INSURANCE_OPTOUT_JSON", jsonData);
                    out.print("success");
                } 
                /*                String line = "";         
                 jsonData = getInputForAPICall(false, null);
                 System.out.println("[ECOM-WAR] InsuranceAjax         Resturl--->" + restUrl);
                 System.out.println("[ECOM-WAR] InsuranceAjax         INput------->" + jsonData);
                result = new StringBuffer();
                post.setEntity(new StringEntity(jsonData));
                HttpClient client = HttpClientBuilder.create().build();
                response = client.execute(post);
                responseCode = response.getStatusLine().getStatusCode();
                 System.out.println("[ECOM-WAR] InsuranceAjax         Response Code : " + response.getStatusLine().getStatusCode());
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                 System.out.println("[ECOM-WAR] InsuranceAjax         result==> " + result);
                //result.append("{ \"quoteNumber\": \"{{quoteNumber}}\", \"onlineBookingNumber\": \"TAR-123456\", \"totalPremium\": 15000, \"currency\": \"Usd\", \"insuredGoods\": [  {   \"harmonizedSystemCode\": \"hsCode_2\",   \"currency\": \"Usd\",   \"sumInsured\": 100000  },  {   \"harmonizedSystemCode\": \"hsCode_2\",   \"currency\": \"Usd\",   \"sumInsured\": 100000  } ]}");
                //
                if (responseCode == 200) {
                    gsonObj = new Gson();
                    outputObj = gsonObj.fromJson(result.toString(), SwissApiOutput.class);
                     System.out.println("[ECOM-WAR] InsuranceAjax         outputObj==> " + outputObj); */
 /* I_request.setAttribute("quoteno", I_request.getParameter("quoteno"));
                    if (checkNull(I_request.getParameter("quoteno")).length() > 0) {
                        currentoutputObj = (SwissApiOutput) session.getAttribute("EditedQuoteDetails");
                        I_request.setAttribute("isrecalculated", "Y");
                        I_request.setAttribute("newpremiumtotal", outputObj.getTotalPremium());
                        I_request.setAttribute("currentpremiumtotal", currentoutputObj.getTotalPremium());

                        I_request.setAttribute("newpremiumcurrency", outputObj.getCurrency());
                        I_request.setAttribute("currentpremiumcurrency", currentoutputObj.getCurrency());
                        InsuranceUtils insoj = new InsuranceUtils();
                        Map mergepremium = insoj.mergeOldAndNewPremium(currentoutputObj.getInsuredGoods(), outputObj.getInsuredGoods());
                        I_request.setAttribute("recalculatedpremium", mergepremium);
                    }
                    I_request.setAttribute("CreateQuoteDetails", outputObj);
                    RequestDispatcher rd = I_request.getRequestDispatcher("/InsurancePremiumDetails.jsp");
                    rd.forward(I_request, I_response);
                 */
                // }
                //
                //
                /*   if (responseCode == 204) {
                    session.setAttribute("INSURANCE_API_TOKEN", "");
                    createQuote(baseUrl);
                }
                if (responseCode != 200 && responseCode != 204) {
                    gsonObj = new Gson();
                    errormap = gsonObj.fromJson(result.toString(), Map.class);
                     System.out.println("[ECOM-WAR] InsuranceAjax         outputObj==> " + errormap);
                    I_request.setAttribute("ErrorMessage", errormap);
                    I_request.setAttribute("ErrorOccured", "Y");
                    RequestDispatcher rd = I_request.getRequestDispatcher("/InsurancePremiumDetails.jsp");
                    rd.forward(I_request, I_response);
                }*/
                //  System.out.println("[ECOM-WAR] InsuranceAjax         Output---->" + result);
            } else {
                 System.out.println("[ECOM-WAR] InsuranceAjax         ====== Invalid Connectivity ======");
            }
        } catch (Exception e) {
             System.out.println("[ECOM-WAR] InsuranceAjax         Exception at createConnectivity >>>>" + e.getMessage());
            e.printStackTrace();
        }
    }
    private void removeoptout(){
    
    try{
         session.setAttribute("INSURANCE_OPTOUT_JSON", null);
         out.print("success");
          System.out.println("[ECOM-WAR] InsuranceAjax         Inside.....remove....optout....");
    
    }
    catch (Exception e) {
            out.print("Error!!");
             System.out.println("[ECOM-WAR] InsuranceAjax         Exception at removeOptout >>>>" + e.getMessage());
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
