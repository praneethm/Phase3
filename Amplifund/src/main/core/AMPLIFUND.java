package main.core;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import com.main.business.Constants;
import com.main.business.Process;

/**
 * 
 * @author Jon Cornado 
 * @Description This is the entry to the Amplifund plugin. Interface details explained in the MipConnectorPlugin
 *
 */
public class AMPLIFUND implements Plugin {

	private static final Logger LOGGER = Logger.getLogger(AMPLIFUND.class.getName());
	public static Connection conn = null;
	private static Listner listner;
	public static HashMap<String, Object> interfaceData = new HashMap<>();
	private static final String name = "AMPLIFUND";

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return name;
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
	public void run(Object interfaceData) {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<String>> holder = (ArrayList<ArrayList<String>>) interfaceData;
		expense(holder);
		// ArrayList<String> temp = holder.get(1);
		// Process.execute(temp.get(3), temp.get(4), temp.get(2), temp.get(5),
		// temp.get(6), temp.get(0));
	}

	@Override
	public Plugin init(Object DB, Object listner) {
		// TODO Auto-generated method stub
		conn = (Connection) DB;
		setListner(listner);
		Constants.ReadConstants();
		return this;
	}

	@Override
	public Object getInterfaceData(String d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setListner(Object o) {
		// TODO Auto-generated method stub
		listner = (Listner) o;
	}

	@Override
	public Object extras(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	public void expense(ArrayList<ArrayList<String>> holder) {
		int size = holder.size() - 1;
		ArrayList<String> temp;
		System.out.println("Printing expense in reverse order " + size);
		for (int i = size; i > 0; i--) {
			// System.out.print("at "+i+"---");
			temp = holder.get(i);

			System.out.println("inserting " + i + " record");
			System.out.println(temp);
			Process.execute(temp.get(3), temp.get(4), temp.get(2), temp.get(5), temp.get(6), temp.get(0));
		}

	}

	@Override
	public statusType getStatus() {
		// TODO Auto-generated method stub
		return statusType.passive;
	}

}
