package main.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import main.database.communication.Mysqlconn;

/**
 * 
 * @author Jon Cornado
 * Used for getting status of a plugin
 *
 */
@Path("/status")
public class ActionBar extends HttpServlet {

	public static HashMap<String, String> set = new HashMap<>();

	public ActionBar() {
		set = new HashMap<String, String>();
		set.put("DB", "UP");
		Connection conn = null;
		try {
			conn = Mysqlconn.getConnection();
			ResultSet rs = null;
			PreparedStatement ps;
			ps = conn.prepareStatement("select get_sysname()");

			rs = ps.executeQuery();
			while (rs.next()) {

				set.put(rs.getString(1), "up");
			}
			conn.close();
		} catch (Exception e) {

			// code to update error response servlet
			update("DB", e, true);

		}
	}

	@GET
	@Path("/update")
	@Produces("text/html")
	public String status() {
		StringBuilder response = new StringBuilder();

		for (String k : set.keySet()) {
			// System.out.println("comparing "+ k);
			// System.out.println("Value is "+ set.get(k));
			if (set.get(k).equalsIgnoreCase("up")) {
				response.append("<li class=\"nav-item\">\r\n "
						+ "        <a class=\"nav-link\" onclick=\"status(this)\" href=\"#\">" + k + "</a></li>");
			} else {
				response.append("<li class=\"nav-item hello\">\r\n "
						+ "        <a class=\"nav-link\" onclick=\"status(this)\" href=\"#\" >" + k
						+ "<span class=\"sr-only\">(current)</span></a></li>");
			}

		}
		return response.toString();
	}

	@GET
	@Path("/details")
	@Produces("text/html")
	public String details() {
		StringBuilder response = new StringBuilder();

		return "hello";
	}

	public static void update(String sys, Exception e, boolean add) {

		if (add) {
			set.put(sys, e.toString());
		} else {
			if (set.containsKey(sys))
				if (set.get(sys).equalsIgnoreCase(e.getMessage()))
					set.remove(sys);
			set.put(sys, "UP");

		}

	}

}
