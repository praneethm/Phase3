package main.core;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import main.core.Plugin.statusType;
import main.core.old.ConnectToMip;
import main.core.old.MIPConstants;

/**
 * 
 * @author Jon Cornado 
 * @Description This is the entry to the MIP plugin. Interface details explained in the MipConnectorPlugin
 *
 */
public class MIP implements Plugin {
	private static final Logger LOGGER = Logger.getLogger(MIP.class.getName());
	public static Connection conn = null;
	private static Listner listner;
	public static HashMap<String, LinkedBlockingQueue<JSONObject>> processData = new HashMap<>();
	public static HashMap<String, Object> interfaceData = new HashMap<>();
	public static statusType status = statusType.loading;

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return "MIP";
	}

	@Override
	public HashMap<String, String> hasErrors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void run(Object processData) {
		// TODO Auto-generated method stub
		if (processData instanceof HashMap<?, ?>) {
			HashMap<String, JSONObject> holder = (HashMap<String, JSONObject>) processData;
			ConnectToMip.InsertDocument(holder);
		} 
	}

	@Override
	public Plugin init(Object DB, Object listner) {
		// TODO Auto-generated method stub
		conn = (Connection) DB;
		setListner(listner);
		MIPConstants.ReadFileForMipUrl();

		return this;
	}

	@Override
	public Object getInterfaceData(String system) {
		// TODO Auto-generated method stub
		return interfaceData.get(system);
	}

	@Override
	public void setListner(Object o) {
		// TODO Auto-generated method stub
		listner = (Listner) o;
	}

	public static void setinterfaceData(String system, ArrayList<ArrayList<String>> values) {
		interfaceData.put(system, values);
		listner.process("MIP", system);

	}

	@Override
	public Object extras(Object o) {
		// TODO Auto-generated method stub
		if (o instanceof HashMap<?, ?>) {
			HashMap<String, String> list = (HashMap<String, String>) o;
			ConnectToMip.LoginIntoMip(list.get("USERNAME"), list.get("PASSWORD"), list.get("MIPINSTANCEURL"));
			return ConnectToMip.getGeneralLedgerCodesBasedOnDescription();
		}

		else if (o instanceof String) {
			switch ((String) o) {

			case "FetchDistributionCode": {
				return ConnectToMip.FetchDistributionCode();
			}
			case "FetchCustomerID": {
				return ConnectToMip.FetchCustomerID();
			}
			case "FetchGeneralLedgerCodes":{
				return ConnectToMip.FetchGeneralLedgerCodes(); 
			}
			}

		}
		return null;

	}

	@Override
	public statusType getStatus() {
		// TODO Auto-generated method stub
		return status;
	}

	public static void setStatus(statusType status) {

		MIP.status = status;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return ConnectToMip.LoginIntoMip();
	}

}
