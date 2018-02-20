package main.plugin;

import java.util.HashMap;

import org.json.simple.JSONObject;

import main.core.Plugin;


/**
 * 
 * @author Jon Cornado
 * This makes calls between plugins work parallel
 */
public class ProcessThread implements Runnable{
	
	
	Plugin plugin;
	Object interfaceData;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		plugin.run(interfaceData);
	}
	
	public ProcessThread(Plugin plugin,Object interfaceData ) {
		
		this.plugin=plugin;
		this.interfaceData=interfaceData;
	}

}
