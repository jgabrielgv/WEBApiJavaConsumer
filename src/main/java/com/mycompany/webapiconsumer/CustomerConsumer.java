/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webapiconsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author OPEN48
 */
public class CustomerConsumer {

    private final String BASE_URL;

    public CustomerConsumer() {
        this.BASE_URL = "http://localhost:49890/api/";
    }
    
    protected URL createUrl(String requestUrl) throws MalformedURLException {
        return new URL(String.format("%s%s", this.BASE_URL, requestUrl));
    }
    
    protected String formatCustId(String custId) {
        return String.format("Customers/%s", custId);
    }
    
    //201 CREATED
    protected void createCustomer(JSONObject object) throws IOException {
       this.genericVoidRequest("POST", "Customers/", 201, object);
    }
    
    //200 OK
    protected JSONObject deleteCustomer(String custId) throws ParseException, IOException {
        return this.genericRetrieveRequest("DELETE", this.formatCustId(custId));
    }
    
    //200 OK
    public JSONObject getCustomer(String custId) throws ParseException, MalformedURLException, IOException {
        return this.genericRetrieveRequest("GET", this.formatCustId(custId));
    }

    //200 OK
    public JSONArray getCustomers() throws ParseException, MalformedURLException, IOException {
        return this.genericRetrieveRequest("GET", "Customers/");
    }
    
    public JSONArray getCustomersQueried(String query) throws ParseException, MalformedURLException, IOException {
        return this.genericRetrieveRequest("GET", String.format("Customers?%s", query));
    }
    
    //204 NO-CONTENT
    protected void updateCustomer(String custId, JSONObject object) throws IOException {
        this.genericVoidRequest("PUT", this.formatCustId(custId), 204, object);
    }

    /**
     * @param <T>
     * @param requestMethod
     * @param requestUrl
     * @return @throws org.json.simple.parser.ParseException
     * @throws java.net.MalformedURLException
     */
    public <T> T genericRetrieveRequest(String requestMethod, String requestUrl) throws ParseException, MalformedURLException, IOException {
        URL url = this.createUrl(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(requestMethod);
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String output, result = "";
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            result += output;
        }
        JSONParser parser = new JSONParser();
        T obj = (T) parser.parse(result);
        conn.disconnect();
        return obj;
    }

    public void genericVoidRequest(String requestMethod, String requestUrl, int statusCode, JSONObject object) throws MalformedURLException, IOException {
        URL url = this.createUrl(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(requestMethod);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(object.toString().getBytes("UTF-8"));
        }
        if (conn.getResponseCode() != statusCode) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode() + " " + conn.getResponseMessage());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String output, result = "";
        System.out.println("Output from Server ....");
        while ((output = br.readLine()) != null) {
            result += output;
        }
        System.out.println(result);
        conn.disconnect();
    }
}
