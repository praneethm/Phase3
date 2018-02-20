package main.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Holder;

import main.database.communication.Mysqlconn;
/**
 * 
 * @author Jon Cornado
 * This class holds all system names - keys - values
 */
public class MasterBean {

	// private static LinkedHashMap<String, ArrayList<String>> holder = new
	// LinkedHashMap<>();
	private static LinkedHashMap<String, HashMap<String, String>> holder = new LinkedHashMap<>();

	public static LinkedHashMap<String, HashMap<String, String>> getHolder() {
		return holder;
	}
	static Connection conn = null;
	static ResultSet rs = null;
	static ResultSet rs1 = null;
	static ResultSet rs2 = null;
	static PreparedStatement ps;
	static PreparedStatement ps1;
	static PreparedStatement ps2;
	private static ArrayList<String> getNext;

	public static void getSystems() {
		HashMap<String, String> ar = new HashMap<String, String>(); 
		try {
			conn = Mysqlconn.getConnection();
			
			ps = conn.prepareStatement("select get_sysname()");
			
			rs = ps.executeQuery();
			
			
			//System.out.println("executd query on db");
			while (rs.next()) {
				//System.out.println("system "+rs.getString(1));
				HashMap<String, String> pair = new HashMap<>();
				ps1 = conn.prepareStatement("select * from view_cred(?)");
				ps1.setString(1, rs.getString(1));
				rs1 = ps1.executeQuery();
				
				while (rs1.next()) {
					//System.out.println("system "+rs.getString(1)+" key "+rs1.getString(1)+" value "+rs1.getString(1));
					System.out.println("key:"+rs1.getString(1)+" value:"+rs1.getString(2));
					pair.put(rs1.getString(1),rs1.getString(2));
				}
				holder.put(rs.getString(1), pair);

			}
			ps2 = conn.prepareStatement("select view_setupstatus()");
			rs2 = ps2.executeQuery();
			rs2.next();
			Dashboard.setSetunDone(rs.getBoolean(1));
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		/*
		 * ar = new HashMap<String, String>(); ar.put("user name", "");
		 * ar.put("address", ""); holder.put("User Details", ar); ar = new
		 * HashMap<String, String>(); ar.put("Credentials1 for sf", "");
		 * ar.put("cred 1", ""); holder.put("Sales Force", ar); ar = new HashMap<String,
		 * String>(); ar.put("credentials for MIP", ""); ar.put("credentials aesfsdfgs",
		 * ""); holder.put("MIP Abila", ar);
		 */
	}

	public String getNext(String value) {
		//System.out.println("searching index of " + value);
		getNext = new ArrayList(holder.keySet());
		int position = getNext.indexOf(value.trim());
		//System.out.println("comparing -" + value.trim() + "-");
		//System.out.println("comparing -" + getNext.get(0) + "-");
		//System.out.println("printing position " + position);
		if (holder.size() > position + 1)
			return getNext.get(++position);

		return "";
	}
// insert key values into database - used in setup phase to insert all values
	public static boolean updateDatabase() {

		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement ps;
		conn = Mysqlconn.getConnection();


		
		for (String string : holder.keySet()) {
			if(!string.equalsIgnoreCase("User Details"))
			for (String key : holder.get(string).keySet()) {

				
				try {
					ps = conn.prepareStatement("select upd_keyvalue(?,?,?)");
					System.out.println("setting value"+holder.get(string).get(key)+" system "+string+" key "+key);
					ps.setString(1, holder.get(string).get(key));
					ps.setString(2, string);
					ps.setString(3, key);
					rs = ps.executeQuery();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}

		}
		try {
			ps=conn.prepareStatement("select upd_setupstatus('true')");
			
			rs=ps.executeQuery();
			System.out.println("setting setup trigger to true");
			ps=conn.prepareStatement("select view_setupstatus()");
			rs=ps.executeQuery();
			rs.next();
			Dashboard.setSetunDone(rs.getBoolean(1));
			System.out.println("safter setting trigger value: "+rs.getBoolean(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	// update only key values for one system
	public static boolean updateDatabase(String system) {
		

		
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement ps;
		conn = Mysqlconn.getConnection();
		for (String key : holder.get(system).keySet()) {

			
			try {
				ps = conn.prepareStatement("select upd_keyvalue(?,?,?)");
				ps.setString(1, holder.get(system).get(key));
				ps.setString(2, system);
				ps.setString(3, key);
				rs = ps.executeQuery();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			
		}
		
		return true;
	}
	

}
