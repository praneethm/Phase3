package main.core;

import java.sql.Connection;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONObject;

import main.core.old.StreamingClient;

/**
 * 
 * @author Jon Cornado 
 * @Description This is the entry to the salesForce plugin. Interface details explained in the MipConnectorPlugin
 *
 */
public class salesForce implements Plugin {

	private static HashMap<String, LinkedBlockingQueue<JSONObject>> interfaceData = new HashMap<>();
	private static Listner listner;
	public static statusType status = statusType.loading;
	public static Connection conn = null;
	private static String PluginName = "SALESFORCE";
	private static StreamingClient sc;

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return PluginName;
	}

	@Override
	public HashMap<String, String> hasErrors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		if (sc.stopClient()) {
			setStatus(statusType.stopped);
			return true;
		}
		return false;
	}

	@Override
	public void run(Object InterfaceData) {
		// TODO Auto-generated method stub
		if (status != statusType.running) {
			setStatus(statusType.loading);
			System.out.println("starting streaming client");
			sc = new StreamingClient();
			//setStatus(statusType.running);
		}

	}

	@Override
	public Plugin init(Object db, Object listner) {
		// TODO Auto-generated method stub
		conn = (Connection) db;
		setListner(listner);
		run(null);
		return this;
	}

	@Override
	public synchronized BlockingQueue<JSONObject> getInterfaceData(String system) {
		// TODO Auto-generated method stub
		BlockingQueue<JSONObject> data = interfaceData.get(system);
		interfaceData.remove(system);
		return data;
	}

	public static void setInterfaceData(String system, JSONObject data, boolean callListner) {
		LinkedBlockingQueue<JSONObject> queue;
		if (interfaceData.containsKey(system)) {
			queue = interfaceData.get(system);
		} else {
			queue = new LinkedBlockingQueue<>();
		}
		try {
			if (!interfaceData.containsKey(system)) {
				interfaceData.put(system, queue);
			}
			queue.put(data);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (callListner)
			callListner("SALESFORCE", system);

	}

	@Override
	public void setListner(Object o) {
		// TODO Auto-generated method stub
		listner = (Listner) o;
	}

	public static void callListner(String from, String to) {
		// TODO Auto-generated method stub
		listner.process(from, to);
	}

	public static void setErrorMessage(Exception e, boolean add) {

		listner.reportError(PluginName, e, add);

	}

	@Override
	public Object extras(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void setStatus(statusType status) {
		System.out.println("changing status to :" + status);
		salesForce.status = status;
	}

	@Override
	public statusType getStatus() {
		// TODO Auto-generated method stub
		return status;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
