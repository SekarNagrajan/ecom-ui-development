/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common.utils;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import net.ecom.common.GeneralConf;
import net.ecom.common.NumberFormat;
import static net.ecom.common.utils.DateConversion_New.strDbFormat;
import net.ecom.insurance.vo.InsuredGood;
import net.ecom.insurance.vo.Survey;
import net.ecom.vo.SurveyList;
import net.ecom.insurance.vo.SwissApiOutput;
import net.ecom.insurance.vo.TokenVo;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author gobinath.t
 */
public class InsuranceUtils {

    private ServletContext contexts;
    private HttpSession session;

    public List callAPI(String strAction, String bookno, ServletContext context, HttpSession sessions) {
        List outputObj = null;
        try {
            InsuranceUtils IU = new InsuranceUtils();
            contexts = context;
            session = sessions;
            String baseUrl = (String) context.getAttribute(GeneralConf.INSURANCE_BASEURL);
            if ("premium".equalsIgnoreCase(strAction)) {
                // callInsurancePermium(baseUrl);
            } else if ("quotecreate".equalsIgnoreCase(strAction)) {
                outputObj = createQuote(baseUrl, bookno);
            } else if ("quoteview".equalsIgnoreCase(strAction)) {
                //  getQuote(baseUrl, quoteNo);
            } else if ("quoteupdate".equalsIgnoreCase(strAction)) {
                // updateQuote(baseUrl, quoteNo);
            } else if ("buyinsurance".equalsIgnoreCase(strAction)) {
                // createInsurance(baseUrl, quoteNo);
            } else if ("getpolicy".equalsIgnoreCase(strAction)) {
                //  getPolicy(baseUrl, policyNo);
            } else if ("getpolicyattach".equalsIgnoreCase(strAction)) {
                //  getPolicyAttachment(baseUrl, policyNo);
            } else if ("downloadpolicy".equalsIgnoreCase(strAction)) {
                //  getPolicyDownload(baseUrl, policyNo, attachId);
            } else if ("optout".equalsIgnoreCase(strAction)) {
                callOptionalAPI(baseUrl, bookno);
                System.out.println("[ECOM-WAR] InsuranceUtils Inside Optout.......type......");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputObj;
    }

    private List createQuote(String baseUrl, String bookno) {
        HttpPost post = null;
        HttpPut put = null;
        String restUrl = null;
        String quoteUrl = null;
        HttpResponse response = null;
        String jsonData = null;
        StringBuffer result = null;
        String strToken = null;
        int responseCode = 0;
        Gson gsonObj = null;
        Map outputObj = null, typemap = null;
        List resultList = null;
        try {
            gsonObj = new Gson();
            strToken = getBearToken();
            //quoteUrl = "api/insurance/v1/marine/quotes/cargo";
            quoteUrl = "quotes/cargo";
            restUrl = baseUrl + quoteUrl;
            if ((restUrl != null) && (restUrl.trim().length() > 0)) {
                post = new HttpPost(restUrl);
                post.setHeader("Content-Type", "application/json");
                post.setHeader("Accept", "application/json");
                post.setHeader("Authorization", "Bearer " + strToken);
                //
                put = new HttpPut(restUrl);
                put.setHeader("Content-Type", "application/json");
                put.setHeader("Accept", "application/json");
                put.setHeader("Authorization", "Bearer " + strToken);
                //executeHttpRequest(jsonData, post);                
                String line = "";
                List inputjs = (List) session.getAttribute("CREATE_QUOTE_INPUT_JSON");
                if (inputjs != null) {
                    resultList = new ArrayList();
                    for (int i = 0; i < inputjs.size(); i++) {
                        outputObj = null;
                        jsonData = (String) inputjs.get(i);
                        jsonData = jsonData.replaceAll("[^\\x00-\\x7F]", " ");
                        System.out.println("[ECOM-WAR] InsuranceUtils jsonData==> " + jsonData);
                        jsonData = jsonData.replace("@bookno", bookno);
                        typemap = gsonObj.fromJson(jsonData.toString(), Map.class);
                        System.out.println("[ECOM-WAR] InsuranceUtils Resturl--->" + restUrl +" INput------->" + jsonData);
                        result = new StringBuffer();
                        if (typemap != null && checkNull((String) typemap.get("quoteNumber")).length() > 0) {
                            put.setEntity(new StringEntity(jsonData));
                            HttpClient client = HttpClientBuilder.create().build();
                            response = client.execute(put);
                        } else {
                            post.setEntity(new StringEntity(jsonData));
                            HttpClient client = HttpClientBuilder.create().build();
                            response = client.execute(post);
                        }

                        System.out.println("[ECOM-WAR] InsuranceUtils Response Code : " + response.getStatusLine().getStatusCode());
                        responseCode = response.getStatusLine().getStatusCode();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        System.out.println("[ECOM-WAR] InsuranceUtils result===> " + result);
                        //
                        if (responseCode == 200) {
                            outputObj = gsonObj.fromJson(result.toString(), Map.class);
                        }
                        //
                        //
                        if (responseCode == 204) {
                            session.setAttribute("INSURANCE_API_TOKEN", "");
                            resultList = createQuote(baseUrl, bookno);
                        }
                        //
                        System.out.println("[ECOM-WAR] InsuranceUtils Output---->" + outputObj);
                        resultList.add(outputObj);
                    }
                }
            } else {
                System.out.println("[ECOM-WAR] InsuranceUtils ====== Invalid Connectivity ======");
            }
            System.out.println("[ECOM-WAR] InsuranceUtils resultList==> " + resultList);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ECOM-WAR] InsuranceUtils Exception at createConnectivity >>>>" + e.getMessage());
        }
        return resultList;
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
            System.out.println("[ECOM-WAR] InsuranceUtils session_token_date==> " + session_token_date);
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
                System.out.println("[ECOM-WAR] InsuranceUtils Encode  " + encCode);
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
                System.out.println("[ECOM-WAR] InsuranceUtils Output 123---->" + result);
                strToken = tokenObj.getAccess_token();
                session.setAttribute("INSURANCE_API_TOKEN", strToken);
                session.setAttribute("INSURANCE_API_TOKEN_CREATED_DATE", new Date());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strToken;

    }

    public List arrangeInsuranceDetailsToInsert(List custtypelis, List insurancelis, String sourcetype, List insuredpartylist, List insuredpartyemail, List insuredpartyprimaryemail) {
        List result = null, templis = null;
        Map submap = null, tempmap = null, tempmap1 = null;
        String hscode = "", sumInsured = "";
        try {
            if (insurancelis != null) {
                result = new ArrayList();
                for (int i = 0; i < insurancelis.size(); i++) {
                    hscode = "";
                    sumInsured = "";
                    submap = new HashMap();
                    tempmap = (Map) insurancelis.get(i);
                    if (tempmap != null) {
                        submap.put("quoteNumber", checkNull((String) tempmap.get("quoteNumber")));
                        submap.put("onlineBookingNumber", checkNull((String) tempmap.get("onlineBookingNumber")));
                        submap.put("totalPremium", checkNull((String) (tempmap.get("totalPremium").toString())));
                        submap.put("currency", checkNull((String) tempmap.get("currency")));
                        submap.put("custtype", checkNull((String) custtypelis.get(i)));
                        if (!"BKG".equalsIgnoreCase(sourcetype)) {
                            submap.put("lrpbook_no", checkNull((String) session.getAttribute("edit_bookno")));
                            submap.put("lrpbl_no", checkNull((String) session.getAttribute("edit_blno")));
                        }
                        String username = checkNull((String) session.getAttribute("SESSION_USERNAME"));
                        String loginname = checkNull((String) session.getAttribute("SESSION_LOGINNAME"));
                        String insuredparty = checkNull((String) insuredpartylist.get(i));
                        if (loginname.length() > 150) {
                            loginname = loginname.substring(0, 150);
                        }
                        if (username.length() > 150) {
                            username = username.substring(0, 50);
                        }
                        if (insuredparty.length() > 150) {
                            insuredparty = insuredparty.substring(0, 150);
                        }
                        submap.put("contactname", username);
                        submap.put("loginname", loginname);
                        submap.put("insuredparty", insuredparty);
                        if (insuredpartyemail != null && insuredpartyemail.size() > 0) {
                            submap.put("email", checkNull((String) insuredpartyemail.get(i)));
                        }
                        System.out.println("[ECOM-WAR] InsuranceUtils insuredpartyprimaryemail=>" + insuredpartyprimaryemail);
                        if (insuredpartyprimaryemail != null && insuredpartyprimaryemail.size() > 0) {
                            submap.put("primaryemail", checkNull((String) insuredpartyprimaryemail.get(i)));
                        }
                        templis = (List) tempmap.get("insuredGoods");
                        for (int j = 0; j < templis.size(); j++) {
                            tempmap1 = (Map) templis.get(j);
                            hscode = hscode + (checkNull((String) tempmap1.get("harmonizedSystemCode")).replace("hsCode_", "") + "~~");
                            sumInsured = sumInsured + checkNull(NumberFormat.decimalFormat(((String) tempmap1.get("sumInsured").toString())) + "~~");
                        }
                        hscode = hscode.substring(0, hscode.length() - 2);
                        sumInsured = sumInsured.substring(0, sumInsured.length() - 2);
                        sumInsured = checkNull(sumInsured);
                        submap.put("HScode", checkNull(hscode));
                        submap.put("sumInsured", checkNull(sumInsured));
                        result.add(submap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String checkNull(String str) throws RemoteException {
        try {
            str = (str != null && !str.equalsIgnoreCase("null") && (!"".equalsIgnoreCase(str))) ? str.trim() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }

    public Map getSurveyList(String surveyjson) {
        LinkedHashMap finalmap = null;
        SurveyList surveyobj = null;
        try {
            finalmap = new LinkedHashMap();

            Gson gson = new Gson();
            Survey sur = gson.fromJson(surveyjson.toString(), Survey.class);
            if (sur != null) {
                int version = sur.getVersion();
                List surveylis = (ArrayList) sur.getSurveyOptionSelections();
                if (surveylis != null && surveylis.size() > 0) {
                    for (int i = 0; i < surveylis.size(); i++) {
                        surveyobj = (SurveyList) surveylis.get(i);
                        surveyobj.setVersion(version);
                        finalmap.put(surveyobj.getSurveyOptionCode(), surveyobj);
                    }
                }
            }
            System.out.println("[ECOM-WAR] InsuranceUtils finalmap==> " + finalmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalmap;
    }

    public Map mergeOldAndNewPremium(List<InsuredGood> currentpremium, List<InsuredGood> newpremium) {
        LinkedHashMap finalmap = null, innermap = null;
        SurveyList surveyobj = null;
        InsuredGood ig = null;
        try {
            finalmap = new LinkedHashMap();
            for (int i = 0; i < currentpremium.size(); i++) {
                ig = currentpremium.get(i);
                innermap = new LinkedHashMap();
                innermap.put("currentpremium", ig.getSumInsured());
                innermap.put("isIsCovered", ig.isIsCovered());
                finalmap.put(ig.getHarmonizedSystemCode(), innermap);
            }
            for (int i = 0; i < newpremium.size(); i++) {
                ig = newpremium.get(i);
                if (finalmap.containsKey(ig.getHarmonizedSystemCode())) {
                    innermap = (LinkedHashMap) finalmap.get(ig.getHarmonizedSystemCode());
                    innermap.put("newpremium", ig.getSumInsured());
                    innermap.put("isIsCovered", ig.isIsCovered());

                } else {
                    innermap = new LinkedHashMap();
                    innermap.put("newpremium", ig.getSumInsured());
                    innermap.put("isIsCovered", ig.isIsCovered());
                }
                finalmap.put(ig.getHarmonizedSystemCode(), innermap);
            }

            System.out.println("[ECOM-WAR] InsuranceUtils finalmap==> " + finalmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalmap;
    }

    public void callOptionalAPI(String baseUrl, String bookno) {
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
            System.out.println("[ECOM-WAR] InsuranceUtils Inside_InsuranceUtils_callOptionalAPI......");
            strToken = getBearToken();
            optoutUrl = "optout/cargo";
            // optoutUrl = "api/insurance/v2/marine/optout/cargo";
            restUrl = baseUrl + optoutUrl;
            if ((restUrl != null) && (restUrl.trim().length() > 0)) {
                post = new HttpPost(restUrl);
                post.setHeader("Content-Type", "application/json");
                post.setHeader("Accept", "application/json"); 
                post.setHeader("Authorization", "Bearer " + strToken);
                //executeHttpRequest(jsonData, post);                
                String line = "";
                //jsonData = getInputForAPICall(false, null);
                jsonData = (String) session.getAttribute("INSURANCE_OPTOUT_JSON");

                jsonData = jsonData.replaceAll("[^\\x00-\\x7F]", " ");
                System.out.println("[ECOM-WAR] InsuranceUtils jsonData==> " + jsonData);
                jsonData = jsonData.replace("@bookno", bookno); 

                System.out.println("[ECOM-WAR] InsuranceUtils Resturl--->" + restUrl+" INput------->" + jsonData);
                result = new StringBuffer();
                post.setEntity(new StringEntity(jsonData));
                HttpClient client = HttpClientBuilder.create().build();
                response = client.execute(post);
                responseCode = response.getStatusLine().getStatusCode();
                System.out.println("[ECOM-WAR] InsuranceUtils Response Code : " + response.getStatusLine().getStatusCode());
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                System.out.println("[ECOM-WAR] InsuranceUtils result==> " + result);
                //result.append("{ \"quoteNumber\": \"{{quoteNumber}}\", \"onlineBookingNumber\": \"TAR-123456\", \"totalPremium\": 15000, \"currency\": \"Usd\", \"insuredGoods\": [  {   \"harmonizedSystemCode\": \"hsCode_2\",   \"currency\": \"Usd\",   \"sumInsured\": 100000  },  {   \"harmonizedSystemCode\": \"hsCode_2\",   \"currency\": \"Usd\",   \"sumInsured\": 100000  } ]}");
                //
                if (responseCode == 200) {
                    gsonObj = new Gson();
                    outputObj = gsonObj.fromJson(result.toString(), SwissApiOutput.class);
                    System.out.println("[ECOM-WAR] InsuranceUtils outputObj==> " + outputObj);
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
                }
                //
                //
                /*   if (responseCode == 204) {
                    session.setAttribute("INSURANCE_API_TOKEN", "");
                    createQuote(baseUrl);
                }
                if (responseCode != 200 && responseCode != 204) {
                    gsonObj = new Gson();
                    errormap = gsonObj.fromJson(result.toString(), Map.class);
                    System.out.println("[ECOM-WAR] InsuranceUtils outputObj==> " + errormap);
                    I_request.setAttribute("ErrorMessage", errormap);
                    I_request.setAttribute("ErrorOccured", "Y");
                    RequestDispatcher rd = I_request.getRequestDispatcher("/InsurancePremiumDetails.jsp");
                    rd.forward(I_request, I_response);
                }*/
                System.out.println("[ECOM-WAR] InsuranceUtils Output---->" + result);
            } else {
                System.out.println("[ECOM-WAR] InsuranceUtils ====== Invalid Connectivity ======");
            }
        } catch (Exception e) {
            System.out.println("[ECOM-WAR] InsuranceUtils Exception at createConnectivity >>>>" + e.getMessage());
            e.printStackTrace();
        }
    }
}
