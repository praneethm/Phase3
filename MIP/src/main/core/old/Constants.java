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

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.simple.JSONObject;

import main.core.MIP;

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
	public static String LOCATION;
	public static String APPTOKEN;
	public static String USERTOKEN;
	public static String ISSUED_AT;
	public static String INSTANCE_URL;
	public static String SIGNATURE;
	public static String ACCESS_TOKEN;
	public static String GLcode_CSH = "11000"; // default
	public static String OFFSET;
	public static boolean USEONLINE;
	public static String BASE_URI;
	public static Header PRETTY_PRINT_HEADER = new BasicHeader("X-PrettyPrint", "1");
	public static Header OAUTH_HEADER;
	static Connection conn;
	static PreparedStatement ps;
	static ResultSet rs = null;

	public enum TYPE_OF_REQUEST_IN_THE_BODY {
		FORM_URL_ENCODE, JSON
	};

	// Constants for Mip
	public static String MIP_BASE_URI;// =
										// "http://ec2-52-89-3-34.us-west-2.compute.amazonaws.com:9001";
	public static String MIP_TOKEN;
	public static String MIP_LOGIN;
	public static String MIP_PASSWORD;
	public static String MIP_ORG;
	public static Header MIP_AUTH_HEADER;
	public static BlockingQueue<JSONObject> sharedQueue;

	static {
		sharedQueue = new LinkedBlockingQueue<JSONObject>();
	}

	public enum REQUESTING_CLASS {
		SALESFORCE, MIP
	};

	public static void ReadFileForMipUrl() {
		System.out.println("Path" + System.getProperty("user.dir"));

		try {
			conn = MIP.conn;
			ps = conn.prepareStatement("select  * from view_cred(?)");
			ps.setString(1, "MIP");
			rs = ps.executeQuery();
			System.out.println("executd query on db");
			while (rs.next()) {

				switch (rs.getString(1)) {

				case "MIPINSTANCEURL": {
					Constants.MIP_BASE_URI = rs.getString(2);
					break;
				}
				case "USERNAME": {
					Constants.MIP_LOGIN = rs.getString(2);
					break;
				}
				case "PASSWORD": {
					Constants.MIP_PASSWORD = rs.getString(2);
					break;
				}
				case "DATABASE": {
					Constants.MIP_ORG = rs.getString(2);
					break;
				}
				case "CODE": {
					Constants.GLcode_CSH = rs.getString(2);
					break;
				}
				case "OFFSET": {
					if (null == rs.getString(2))
						Constants.OFFSET = "false";
					else
						Constants.OFFSET = rs.getString(2);
					break;
				}
				case "USEMIPONLINE":{
					if(null==rs.getString(2) || rs.getString(2).equalsIgnoreCase("false"))
						Constants.USEONLINE=false;
					else
						Constants.USEONLINE=true;
					break;
				}

				}

			}
			if(Constants.USEONLINE)
				Constants.MIP_BASE_URI="https://mipapi.abilaonline.com/";
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			conn = MIP.conn;
			ps = conn.prepareStatement("select  * from view_cred(?)");
			ps.setString(1, "AMPLIFUND");
			rs = ps.executeQuery();
			System.out.println("executd query on db");
			while (rs.next()) {

				switch (rs.getString(1).toUpperCase()) {

				case "LOCATION": {
					Constants.LOCATION = rs.getString(2);
					break;
				}
				case "APPTOKEN": {
					Constants.APPTOKEN = rs.getString(2);
					break;
				}
				case "USERTOKEN": {
					Constants.USERTOKEN = rs.getString(2);
					break;
				}

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
