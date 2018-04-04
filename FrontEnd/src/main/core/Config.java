package main.core;

import org.glassfish.jersey.server.ResourceConfig;

import main.plugin.PluginService;

public class Config extends ResourceConfig {

	  public Config() {
	        // Define the package which contains the service classes.
	        packages("com.javahelps.jerseydemo.services");
	        System.out.println("inside jersey config file");
	        PluginService.getInstance();
	    }
}
