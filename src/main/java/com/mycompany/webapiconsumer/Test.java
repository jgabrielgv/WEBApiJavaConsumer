/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webapiconsumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 *
 * @author OPEN48
 */
public class Test {

    public static void main(String[] args) {
        CustomerConsumer consumer = new CustomerConsumer();
        try {
            System.out.println("Retrieving all customers...");
            System.out.println(consumer.getCustomers());
            System.out.println();

            System.out.println("Retrieving customer ALFKI...");
            System.out.println(consumer.getCustomer("ALFKI"));
            System.out.println();
            
            System.out.println("Retrieving queried customer ALFKI...");
            System.out.println(consumer.getCustomersQueried("$filter=CustomerID%20eq%20'ALFKI'&$select=CustomerID,CompanyName,ContactName,Address"));
            System.out.println();
            
            JSONObject test2 = null;
            try {
                test2 = consumer.getCustomer("TEST2");
            } catch (Exception ex) {
            }
            if (test2 == null) {
                System.out.println("Adding customer TEST2...");
                test2 = createDefaultCustomer("TEST2");
                consumer.createCustomer(test2);
                System.out.println();
            }
            
            System.out.println("Adding customer JGGV...");
            consumer.createCustomer(createDefaultCustomer("JGGV"));
            System.out.println();
            
            test2.put("CompanyName", "Test Company5");
            test2.put("Address", "Test Address5");
            System.out.println("Updating customer TEST2...");
            consumer.updateCustomer((String) test2.get("CustomerID"), test2);
            System.out.println();
            
            System.out.println("Retrieving customer TEST2...");
            System.out.println(consumer.getCustomer("TEST2"));
            System.out.println();
            
            System.out.println("Retrieving customer JGGV...");
            System.out.println(consumer.getCustomer("JGGV"));
            System.out.println();

            System.out.println("Deleting customer JGGV...");
            System.out.println(consumer.deleteCustomer("JGGV"));
        } catch (ParseException | IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static JSONObject createDefaultCustomer(String custId) {
        Map<String, Object> map = new HashMap<>();
        map.put("CustomerID", custId);
        map.put("CompanyName", "Test Company");
        map.put("ContactName", "Test Contact Name");
        map.put("ContactTitle", "Test Title");
        map.put("Address", "Test Address");
        map.put("City", "Test City");
        map.put("Region", "Test Region");
        map.put("PostalCode", "Test Code");
        map.put("Country", "Test Country");
        map.put("Phone", "Test Phone");
        map.put("Fax", "Test Fax");
        return new JSONObject(map);
    }

}
