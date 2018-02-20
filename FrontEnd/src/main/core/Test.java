package main.core;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("test")
public class Test {

	
	
	
	@GET
	@Path("/stat")
	@Produces("text/html")
	public String status() {
		HashMap<String, String> set = new HashMap<String, String>();
		set.put("SalesForce bla bla", "up");
		set.put("QuickBook", "up");
		StringBuilder response = new StringBuilder();

		for (String k : set.keySet()) {

			response.append("<li class=\"nav-item\">\r\n" + "        <a class=\"nav-link active\" href=\"#\">" + k
					+ "</a></li>");

		}
		return response.toString();
	}
}
