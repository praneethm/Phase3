package main.plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import main.core.Plugin;
import main.database.communication.Mysqlconn;

/**
 * 
 * @author Jon Cornado
 * 
 *         Singleton class that manages plugins
 *
 */
public class PluginService {

	private static PluginService service;
	private ServiceLoader<Plugin> loader;
	private static ServiceLoader<Plugin> Staticloader;
	public static HashMap<String, Plugin> plugins = new HashMap<>();
	private ListnerService listner = new ListnerService();
	public static String string = "hello mmmm";

	


	private PluginService() {
		loader = ServiceLoader.load(Plugin.class);
		loadPlugins();
	}

	// Depricated
	public static Plugin StaticPluginCalls(String system) {
		System.out.println("checking plugins");
		Staticloader = ServiceLoader.load(Plugin.class);
		Iterator<Plugin> pluginIterator = Staticloader.iterator();
		Plugin plugin;

		while (pluginIterator.hasNext()) {
			plugin = pluginIterator.next();
			System.out.println("plugin " + plugin.getPluginName());
			if (system.equalsIgnoreCase(plugin.getPluginName())) {
				// System.out.println("returning mip plugin");
				return plugin;

			}

		}

		return null;
	}

	private void loadPlugins() {
		// TODO Auto-generated method stub
		try {
			Iterator<Plugin> pluginIterator = loader.iterator();
			Plugin plugin;
			System.out.println("searching and running plugins");
			while (pluginIterator.hasNext()) {

				plugin = pluginIterator.next();
				System.out.println("Available Plugin " + plugin.getPluginName());

				plugins.put(plugin.getPluginName(), plugin.init(Mysqlconn.getConnection(), listner));
				// System.out.println("Available Plugin " + plugin.getPluginName());
			}
		} catch (ServiceConfigurationError serviceError) {

			serviceError.printStackTrace();

		}
	}

	public static void main(String[] args) {
		System.out.println("starting plugin service");
		PluginService ps = PluginService.getInstance();
		ps.loadPlugins();
	}

	public static synchronized PluginService getInstance() {
		if (service == null) {
			service = new PluginService();
		}
		return service;
	}

	public static String getStatus(String systemName) {
		return plugins.get(systemName).getStatus().toString();

	}

}
