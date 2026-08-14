/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author gobinath.t
 */
public class CommonPasswordValidation {

    public static Map validatePassword(Map inputmap,String url) {
        String json = new Gson().toJson(inputmap);
        //
        System.out.println("[ECOM-WAR] CommonPasswordValidation url:::validatePassword ::::" + url + "Input --json ===> "+json);
        Client jaxrsClient = ClientBuilder.newClient();
        WebTarget webTarget = jaxrsClient.target(url);
        Invocation invocation = webTarget.request(MediaType.APPLICATION_JSON_TYPE).buildPost(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
        Response respon = invocation.invoke();
        String results = respon.readEntity(String.class);
        System.out.println("[ECOM-WAR] CommonPasswordValidation resultss::::"+results);
        Map<String, Object> hmdata = new Gson().fromJson(results, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        return hmdata;
    }
}
