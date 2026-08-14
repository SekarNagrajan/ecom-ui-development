/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.captcha;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;


/**
 *
 * @author priyadharshinim
 */
public final class VerifyRecaptcha{

	public static final String url = "https://www.google.com/recaptcha/api/siteverify";
      
            

       
    /**
     *
     * @param gRecaptchaResponse
     * @param secretKey
     * @return
     * @throws IOException
     */
    public static  boolean verify(String gRecaptchaResponse,String secretKey) throws IOException {
        
                 
               if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			return false;
		}
		
		try{
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
             
		con.setRequestMethod("POST");
                
		String postParams = "secret=" + secretKey + "&response="
				+ gRecaptchaResponse;

	
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("[ECOM-WAR] VerifyRecaptcha \nSending 'POST' request to URL : " + url + " Post parameters : " + postParams +" Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

	
		System.out.println("[ECOM-WAR] VerifyRecaptcha CAPTCHARESPONSE"+response.toString());
		
		
		JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
		JsonObject jsonObject = jsonReader.readObject();
		jsonReader.close();
		
		return jsonObject.getBoolean("success");
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
    
  
    
    
}

