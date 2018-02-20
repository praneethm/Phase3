package main.core.old;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.core.salesForce;

/**
 * Contains the credentials for connecting to SalesForce.
 *
 * @author Saranya
 */
public class UserCredentials {
	static Connection conn;
	static PreparedStatement ps;
	static ResultSet rs = null;
	
	public static String loginInstanceDomain; // "na30.salesforce.com";//
	// "test.salesforce.com";//
	public static String apiVersion = "37";
	public static String userName;// = "saranya1991@gmail.com";//
									// "jim@icanaz.org.nsp3";//
	public static String password;// = "Saranya_1991";// "2Virginia";//

	// 1. Login into SalesForce
	// 2. Switch to Lightening mode by clicking on "Switch to Lightening mode"
	// 3. After switching to lightening mode, click on the Gear symbol
	// and go to "Setup Home".
	// 4. Click on Apps > Apps > scroll down to the connected app created by
	// you.
	// 5. Click on the name of the connected app you created.
	// 6. Under "API (Enable OAuth Settings)",
	// You will find the Consumer Key and Secret.
	public static String consumerKey;// =
	// "3MVG9uudbyLbNPZPLnaJp3s.yZd1RW48WuLAMwBr_4TvsjkbwmfnifEZ43o.23ypH28BRL9JJ63IbkjlHpdh5";
	// //
	// "3MVG9qwrtt_SGpCtQg.l6zzyXkGIajFD80ZHMM6nmtKhTrUyAtPhDCKEbjc447xgqQqA0_3dWqQPcUYCPKAOw";//
	public static String consumerSecret;// = "5439281922189881772"; //
	// "6411396308310537101";//
	public static String grantType = "password";
	public static void updateValues(){
		
		
		
		try {
			conn = salesForce.conn;
			ps = conn.prepareStatement("select  * from view_cred(?)");
			ps.setString(1, "SALESFORCE");
			rs = ps.executeQuery();
			System.out.println("executd query on db");
			while (rs.next()) {
				
				switch(rs.getString(1)) {
				
				case "LOGININSTANCEDOMAIN":{
					loginInstanceDomain=rs.getString(2);
					break;
				}
				case "USERNAME":{
					userName=rs.getString(2);
					break;
				}
				case "PASSWORD":{
					password=rs.getString(2);
					break;
				}
				case "CONSUMERKEY":{
					consumerKey=rs.getString(2);
					break;
				}
				case "CONSUMERSECRET":{
					consumerSecret=rs.getString(2);
					break;
				}
				
				}
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
