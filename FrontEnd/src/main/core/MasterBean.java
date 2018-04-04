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

import main.core.beans.keysSet;
import main.database.communication.Mysqlconn;

/**
 * 
 * @author Jon Cornado This class holds all system names - keys - values
 */
public class MasterBean {

	// private static LinkedHashMap<String, ArrayList<String>> holder = new
	// LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, keysSet>> holder = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, keysSet>> Settingsholder = new LinkedHashMap<>();

	public static LinkedHashMap<String, LinkedHashMap<String, keysSet>> getHolder() {
		return holder;
	}

	public static LinkedHashMap<String, LinkedHashMap<String, keysSet>> getSettingsHolder() {
		return Settingsholder;
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

		try {
			conn = Mysqlconn.getConnection();

			ps = conn.prepareStatement("select get_sysname()");

			rs = ps.executeQuery();

			// System.out.println("executd query on db");
			while (rs.next()) {
				// System.out.println("system "+rs.getString(1));

				LinkedHashMap<String, keysSet> pair = new LinkedHashMap<>();
				ps1 = conn.prepareStatement("select * from view_cred(?)");
				ps1.setString(1, rs.getString(1));
				rs1 = ps1.executeQuery();

				while (rs1.next()) {
					keysSet keyset = new keysSet();
					// System.out.println("system "+rs.getString(1)+" key "+rs1.getString(1)+" value
					// "+rs1.getString(1));
					System.out.println("system:" + rs.getString(1) + " key:" + rs1.getString(1) + " value:"
							+ rs1.getString(2) + " type" + rs1.getString(3) + " effectedkey" + rs1.getString(4) + " url"
							+ rs1.getString(5) + " options" + rs1.getString(6));
					keyset.setValue(rs1.getString(2));
					keyset.setType(rs1.getString(3));
					keyset.setEffectedKey(rs1.getString(4));
					keyset.setUrl(rs1.getString(5));
					keyset.setOptions(rs1.getString(6));
					keyset.setDisplayName(rs1.getString(7));
					keyset.setToolTip(rs1.getString(8));
					keyset.setVisible(rs1.getBoolean(9));
					pair.put(rs1.getString(1), keyset);

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

	public static void getSettings() {
		try {
			conn = Mysqlconn.getConnection();
			ps = conn.prepareStatement("SELECT settings_name FROM public.systems_settings");
			rs = ps.executeQuery();
			LinkedHashMap<String, keysSet> pair = new LinkedHashMap<>();

			while (rs.next()) {

				ps1 = conn.prepareStatement("select * from view_settings_cred(?)");
				ps1.setString(1, rs.getString(1));
				rs1 = ps1.executeQuery();
				while (rs1.next()) {
					keysSet keyset = new keysSet();
					// System.out.println("system "+rs.getString(1)+" key "+rs1.getString(1)+" value
					// "+rs1.getString(1));
					System.out.println("system:" + rs.getString(1) + " key:" + rs1.getString(1) + " value:"
							+ rs1.getString(2) + " type" + rs1.getString(3) + " effectedkey" + rs1.getString(4) + " url"
							+ rs1.getString(5) + " options" + rs1.getString(6)+ " displayname" + rs1.getString(7));
					keyset.setValue(rs1.getString(2));
					keyset.setType(rs1.getString(3));
					keyset.setEffectedKey(rs1.getString(4));
					keyset.setUrl(rs1.getString(5));
					keyset.setOptions(rs1.getString(6));
					keyset.setDisplayName(rs1.getString(7));
					keyset.setToolTip(rs1.getString(8));
					pair.put(rs1.getString(1), keyset);
				}
				Settingsholder.put(rs.getString(1), pair);

			}
			conn.close();
		} catch (Exception e) {

		}

	}

	public String getNext(String value) {
		// System.out.println("searching index of " + value);
		getNext = new ArrayList(holder.keySet());
		int position = getNext.indexOf(value.trim());
		// System.out.println("comparing -" + value.trim() + "-");
		// System.out.println("comparing -" + getNext.get(0) + "-");
		// System.out.println("printing position " + position);
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
			if (!string.equalsIgnoreCase("User Details"))
				for (String key : holder.get(string).keySet()) {

					try {
						ps = conn.prepareStatement("select upd_keyvalue(?,?,?)");
						ps.setString(1, holder.get(string).get(key).getValue());
						ps.setString(2, string);
						ps.setString(3, key);
						rs = ps.executeQuery();

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}

				}

		}
		try {
			ps = conn.prepareStatement("select upd_setupstatus('true')");

			rs = ps.executeQuery();
			System.out.println("setting setup trigger to true");
			ps = conn.prepareStatement("select view_setupstatus()");
			rs = ps.executeQuery();
			rs.next();
			Dashboard.setSetunDone(rs.getBoolean(1));
			System.out.println("safter setting trigger value: " + rs.getBoolean(1));
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// update only key values for one system
	public static boolean updateDatabase(String system,String type) {

		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement ps;
		conn = Mysqlconn.getConnection();
		LinkedHashMap<String, LinkedHashMap<String, keysSet>> tmp = null;
		boolean forSystems = true;
		switch (type) {
		case "system": {
			tmp = holder;
			forSystems = true;
			break;

		}
		case "settings": {
			tmp = Settingsholder;
			forSystems = false;
			break;
		}
		}

		try {
			for (String key : tmp.get(system).keySet()) {
				if (forSystems)
					ps = conn.prepareStatement("select upd_keyvalue(?,?,?)");
				else
					ps = conn.prepareStatement("select upd_settings_keyvalue(?,?,?)");
				ps.setString(1, tmp.get(system).get(key).getValue());
				ps.setString(2, system);
				ps.setString(3, key);
				rs = ps.executeQuery();

			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
