package main.core;

import org.glassfish.jersey.server.ResourceConfig;

public class Config extends ResourceConfig {

	  public Config() {
	        // Define the package which contains the service classes.
	        packages("com.javahelps.jerseydemo.services");
	    }
}
