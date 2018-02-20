package com.main.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import main.core.AMPLIFUND;

/**
 * 
 * @author Jon Cornado
 * Constants class in all plugins is used for retrieving and storing credentials
 */
public class Constants {

	public static String USERTOKEN;
	public static String LOCATION;
	public static String APPTOKEN;
	public static String URL;   //https://amplifund-sandbox-api.azurewebsites.net
	static Connection conn;
	static PreparedStatement ps;
	static ResultSet rs = null;

	public static void ReadConstants() {

		try {
			conn = AMPLIFUND.conn;
			ps = conn.prepareStatement("select  * from view_cred(?)");
			ps.setString(1, "AMPLIFUND");
			rs = ps.executeQuery();
			System.out.println("executd query on db");
			while (rs.next()) {
				System.out.println("setting " + rs.getString(2));
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
				case "URL": {
					Constants.URL = rs.getString(2);
					break;
				}

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
