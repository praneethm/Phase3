package main.plugin;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONObject;

import main.core.ActionBar;
import main.core.Listner;
import main.core.Plugin;
/**
 * 
 * @author Jon Cornado
 * This class handels calls between plugins
 */
public class ListnerService implements Listner{
	


	@Override
	public void process(String from, String to) {
		// TODO Auto-generated method stub
	
		Plugin fromPlugin=PluginService.plugins.get(from);
		if(to.equalsIgnoreCase("MIP")) {
		 HashMap<String,JSONObject> interfaceData =new HashMap<>();
		 System.out.println("printing plugin name for "+ from +"and to name "+to);
		 System.out.println("printing from plugin name "+ fromPlugin.getPluginName());
		 BlockingQueue<JSONObject> temp =(BlockingQueue<JSONObject>) fromPlugin.getInterfaceData("MIPExtra");
		 try {
			interfaceData.put("DistributionCode",temp.take());
			interfaceData.put("AccountNameAndGeneralLedgerCode", temp.take());
			temp = (BlockingQueue<JSONObject>) fromPlugin.getInterfaceData("MIP");
			 interfaceData.put("message", temp.take());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 new ProcessThread(PluginService.plugins.get(to), interfaceData).run();
		 
		//PluginService.plugins.get(to).run(interfaceData);
		}
		System.out.println("calling Amplifund service");
		new ProcessThread(PluginService.plugins.get(to), PluginService.plugins.get(from).getInterfaceData(to)).run();
	}

	@Override
	public void reportError(String arg0, Exception e,boolean add) {
		if(ActionBar.set.containsKey(arg0)) {
			ActionBar.update(arg0, e, add);
			
		}
		
	}



}
