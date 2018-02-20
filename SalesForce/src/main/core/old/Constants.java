/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.core.old;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.simple.JSONObject;

import main.core.salesForce;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Saranya
 */
public class Constants {

	public static final String OAUTH_ENDPOINT = "/services/oauth2/token";
	public static final String REST_ENDPOINT = "/services/data";
	public static final int NINETY_NINE = 99;

	public static String USER_ID;
	public static String ISSUED_AT;
	public static String INSTANCE_URL;
	public static String SIGNATURE;
	public static String ACCESS_TOKEN;

	public static String BASE_URI;
	public static Header PRETTY_PRINT_HEADER = new BasicHeader("X-PrettyPrint", "1");
	public static Header OAUTH_HEADER;

	public enum TYPE_OF_REQUEST_IN_THE_BODY {
		FORM_URL_ENCODE, JSON
	};



	public enum REQUESTING_CLASS {
		SALESFORCE, MIP
	};



	public static void ReadCredsFromFile() {



							Connection conn=salesForce.conn;
							 ResultSet rs = null;
							 PreparedStatement ps;
								try {
									ps = conn.prepareStatement("select * from public.\"CREDENTIALS\"");
									rs = ps.executeQuery();
									//UserCredentials.loginInstanceDomain ="login.salesforce.com";
									while(rs.next()) {
									if(rs.getString("CR_ID").equalsIgnoreCase("1"))
									UserCredentials.loginInstanceDomain =rs.getString("KEY_VALUE");
									if(rs.getString("CR_ID").equalsIgnoreCase("2"))
									UserCredentials.userName = rs.getString("KEY_VALUE");
									if(rs.getString("CR_ID").equalsIgnoreCase("3"))
									UserCredentials.password = rs.getString("KEY_VALUE");
									if(rs.getString("CR_ID").equalsIgnoreCase("4"))
									UserCredentials.consumerKey = rs.getString("KEY_VALUE");
									if(rs.getString("CR_ID").equalsIgnoreCase("5"))
									UserCredentials.consumerSecret = rs.getString("KEY_VALUE");
									}
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								


						
						
					
			


	}
}
