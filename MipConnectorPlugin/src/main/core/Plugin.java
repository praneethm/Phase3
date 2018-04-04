package main.core;

import java.util.HashMap;

public interface Plugin {


	public String getPluginName();
	//holds error and error details that is used by the main application (FrontEnd) to display in status page
	public HashMap<String,String> hasErrors();
	//stop plugin
	public boolean stop();
	//This method is usually called by other plugins, e.g. Salesforce plugin calls MIP Plugin to insert cash receipts.
	public void run(Object interfaceData);
	//Initilize plugin
	public Plugin init(Object DB, Object listner);
	//This method holds data that is used by other plugins
	public Object getInterfaceData(String d);
	public void setListner(Object o);
	//Additionality functionality that run() method cannot handle, e.g used in mip to get some values from MIP server even before setting up server
	public Object extras(Object o);
	public statusType getStatus();
	public boolean validate();
	public enum statusType {
		loading,
		running,
		stopped,
		passive
	}
	
}
