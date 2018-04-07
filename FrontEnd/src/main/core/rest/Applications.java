package main.core.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import main.core.MasterBean;
import main.core.Plugin;
import main.core.beans.keysSet;
import main.database.communication.Mysqlconn;
import main.plugin.PluginService;

@Path("/appList/1.0")
public class Applications {

	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public String listApplications() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps;
		JSONArray list = new JSONArray();
		JSONObject holder;
		MasterBean.getSystems();
		try {
			conn = Mysqlconn.getConnection();
			ps = conn.prepareStatement("select get_sysname()");
			rs = ps.executeQuery();
			while (rs.next()) {
				holder = new JSONObject();
				holder.put("name", rs.getString(1));
				holder.put("status", PluginService.getStatus(rs.getString(1)));
				list.put(holder);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list.toString();

	}

	@GET
	@Path("/listSettings")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray listSettings() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps;
		JSONArray list = new JSONArray();
		try {
			conn = Mysqlconn.getConnection();
			ps = conn.prepareStatement("SELECT settings_name FROM public.systems_settings");
			rs = ps.executeQuery();
			while (rs.next()) {
				list.put(rs.getString(1));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return list;
	}

	/**
	 * 
	 * @return
	 */

	@SuppressWarnings("unchecked")
	@POST
	@Path("/codes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getGeneralLedgerCodes(String json) {
		String username = "", password = "", url = "";
		Plugin plugin = PluginService.StaticPluginCalls("MIP");
		HashMap<String, String> list = new HashMap<>();
		System.out.println(json);
		JSONObject arr = new JSONObject(json);

		username = arr.getString("USERNAME");
		password = arr.getString("PASSWORD");
		url = arr.getString("MIPINSTANCEURL");

		System.out.println("displaying extracted credentials" + username + url);

		list.put("USERNAME", username);
		list.put("PASSWORD", password);
		list.put("MIPINSTANCEURL", url);
		HashMap<String, String> map = (HashMap<String, String>) plugin.extras(list);
		JSONArray res = new JSONArray();
		for (Entry<String, String> set : map.entrySet()) {
			arr = new JSONObject();
			arr.put("key", set.getKey());
			arr.put("value", set.getValue());
			res.put(arr);
		}
		return res.toString();
		// return new JSONObject((HashMap<String, String>)
		// plugin.extras(list)).toString();

	}

	@GET
	@Path("/keyValuePairSettings")
	public String keyValue(@QueryParam("SYSTEM") String system) {
		System.out.println("settings name recieved:" + system);
		JSONArray res = new JSONArray();
		JSONObject holder;
		MasterBean.getSettings();
		if (!MasterBean.getSettingsHolder().containsKey(system))
			return "{Requested system does not exist}";
		for (Entry<String, keysSet> entry : MasterBean.getSettingsHolder().get(system).entrySet()) {
			holder = new JSONObject();
			holder.put("key", entry.getKey());
			String value = entry.getValue().getValue();
			value = null == value ? "" : value;
			holder.put("value", value);
			String type = entry.getValue().getType();
			type = null == type ? "" : type;
			holder.put("type", type);
			String effectedkey = entry.getValue().getEffectedKey();
			effectedkey = null == effectedkey ? "" : effectedkey;
			holder.put("effectedKey", effectedkey);
			String url = entry.getValue().getUrl();
			url = null == url ? "" : url;
			holder.put("url", url);
			String options = entry.getValue().getOptions();
			options = null == options ? "" : options;
			holder.put("options", options);
			String displayName = entry.getValue().getDisplayName();
			displayName = null == displayName ? "" : displayName;
			holder.put("DisplayName", displayName);
			String ToolTip = entry.getValue().getToolTip();
			ToolTip = null == ToolTip ? "" : ToolTip;
			holder.put("ToolTip", ToolTip);
			res.put(holder);
		}

		return res.toString();

	}

	@POST
	@Path("/updatekeyValuePair")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response UpdatekeyValue(String json) {
		MasterBean.getSystems();
		System.out.println("updated credentials service called");
		System.out.println(json);
		String system = "";
		JSONArray arr = new JSONArray(json);
		for (int i = 0; i < arr.length(); i++) {
			system = arr.getJSONObject(i).getString("system");
			String key = arr.getJSONObject(i).getString("key");
			String value = String.valueOf(arr.getJSONObject(i).get("value"));
			value = value.equalsIgnoreCase("") ? null : value;
			MasterBean.getHolder().get(arr.getJSONObject(i).getString("system")).get(key).setValue(value);

		}

		if (!MasterBean.updateDatabase())
			return Response.status(500).entity("failed values to database, please contact support").build();
		// return Response.status(200).build();

		Plugin plugin = PluginService.StaticPluginCalls(system);
		if (plugin.validate()) {
			return Response.status(200).entity("Credentials are validated successfully").build();
		}

		return Response.status(201).entity("credentials are invalid or the " + system + " seems to be offline").build();

	}

	@POST
	@Path("/updateSettingskeyValuePair")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response UpdateSettingskeyValue(String json) {
		MasterBean.getSettings();
		System.out.println("updated settings credentials service called");
		System.out.println(json);
		JSONArray arr = new JSONArray(json);
		String type = null;
		for (int i = 0; i < arr.length(); i++) {
			String key = arr.getJSONObject(i).getString("key");
			String value = String.valueOf(arr.getJSONObject(i).get("value"));
			value = value.equalsIgnoreCase("") ? null : value;
			MasterBean.getSettingsHolder().get(arr.getJSONObject(i).getString("system")).get(key).setValue(value);
			type = arr.getJSONObject(i).getString("system");
		}
		Boolean status = MasterBean.updateDatabase(type, "settings");
		if (status)
			return Response.status(200).build();
		return Response.status(500).build();

	}

	@GET
	@Path("/keyValuePair/2.0")
	public String keyValueNew(@QueryParam("SYSTEM") String system) {
		System.out.println("system name recieved:" + system);
		JSONArray res = new JSONArray();
		JSONObject holder;
		MasterBean.getSystems();
		if (!MasterBean.getHolder().containsKey(system))
			return "{Requested system does not exist}";
		for (Entry<String, keysSet> entry : MasterBean.getHolder().get(system).entrySet()) {
			holder = new JSONObject();
			holder.put("key", entry.getKey());
			String value = entry.getValue().getValue();
			value = null == value ? "" : value;
			holder.put("value", value);
			String type = entry.getValue().getType();
			type = null == type ? "" : type;
			holder.put("type", type);
			String effectedkey = entry.getValue().getEffectedKey();
			effectedkey = null == effectedkey ? "" : effectedkey;
			holder.put("effectedKey", effectedkey);
			String url = entry.getValue().getUrl();
			url = null == url ? "" : url;
			holder.put("url", url);
			String options = entry.getValue().getOptions();
			options = null == options ? "" : options;
			holder.put("options", options);
			String displayName = entry.getValue().getDisplayName();
			displayName = null == displayName ? "" : displayName;
			holder.put("DisplayName", displayName);
			String ToolTip = entry.getValue().getToolTip();
			ToolTip = null == ToolTip ? "" : ToolTip;
			holder.put("ToolTip", ToolTip);
			String visible = String.valueOf(entry.getValue().isVisible());
			visible = null == visible ? "" : visible;
			holder.put("visible", visible);
			res.put(holder);
		}
		System.out.println(res.toString());
		return res.toString();

	}

	@GET
	@Path("/status")
	public String Status(@QueryParam("SYSTEM") String system) {

		Plugin plugin = PluginService.StaticPluginCalls(system);
		return plugin == null ? "{requested system does not exist}" : plugin.getStatus().toString();
	}

	@POST
	@Path("/changeStatus")
	@Consumes(MediaType.APPLICATION_JSON)
	public String changeApplicationStatus(String json) {
		String system = "";
		String status = "";
		System.out.println(json);
		JSONObject obj = new JSONObject(json);
		system=obj.getString("name");
		status=obj.getString("status");
		
		if (status.equalsIgnoreCase("stopped")) {
			System.out.println("starting application");
			PluginService.plugins.get(system).run("");

		} else if (status.equalsIgnoreCase("running")) {
			PluginService.plugins.get(system).stop();
		}
		//{"name":"AMPLIFUND","status":"stopped"}
		obj= new JSONObject();
		obj.put("name", system);
		obj.put("status",PluginService.plugins.get(system).getStatus() );
		
		return new JSONArray().put(obj).toString();
	}

}
