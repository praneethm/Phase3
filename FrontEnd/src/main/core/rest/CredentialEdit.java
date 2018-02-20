package main.core.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import main.core.MasterBean;

@Path("/CredentialEdit")
public class CredentialEdit {

	@Path("/v1")
	@GET
	public String getFields(@QueryParam("system") String systemName) {
		systemName=systemName.toUpperCase();
		MasterBean mb = new MasterBean();
		StringBuilder response = new StringBuilder();
		response.append("<form action=\"/FrontEnd/update\" method=\"GET\"><table>");
		for (String string : mb.getHolder().get(systemName).keySet()) {
			if (!string.equalsIgnoreCase("OFFSET")) {
				if ((null != mb.getHolder().get(systemName).get(string))) {
					String value = mb.getHolder().get(systemName).get(string);
					if (string.equalsIgnoreCase("CODE")) {
						response.append(
								"<tr><td>Disable Offset:</td><td><input type=\"checkbox\" name=\"OFFSET\" id=\"OFFSET\" value=\"true\" checked></td></tr><tr><td>Select Cash Account:</td><td><select class=\"quantity\" id=\"CODE\" name=\"CODE\"><option value="
										+ value + "</option></select></td></tr>");
					} else if (string.equalsIgnoreCase("USEMIPONLINE")) {
						response.append(
								"<tr><td>Use MIP Online Version:</td><td><input type=\"checkbox\" checked name=\"USEMIPONLINE\" id=\"USEMIPONLINE\" value=\"true\"></td></tr>");
					} else {
						response.append("<tr><td>" + string + "</td><td><input type=\"text\" value=\"" + value + "\"");
						response.append(" name=\"" + string + "\" id=\"" + string + "\"</td></tr>");
					}
				} else {

					if (string.equalsIgnoreCase("CODE")) {
						response.append(
								"<tr><td>Disable Offset:</td><td><input type=\"checkbox\" name=\"OFFSET\" id=\"OFFSET\" value=\"false\"></td></tr><tr><td>Select Cash Account:</td><td><select class=\"quantity\" id=\"CODE\" name=\"CODE\" disabled=\"disabled\"><option value=\"11000\">Disable offset for selection</option></select></td></tr>");
					} else if (string.equalsIgnoreCase("USEMIPONLINE")) {
						response.append(
								"<tr><td>Use MIP Online Version:</td><td><input type=\"checkbox\" name=\"USEMIPONLINE\" id=\"USEMIPONLINE\" value=\"true\"></td></tr>");
					} else {
						response.append("<tr><td>" + string + "</td><td><input type=\"text\" name=\"" + string
								+ "\" id=\"" + string + "\" /></td></tr>");
					}

				}
			}

		}
		response.append("<tr><td><input type=\"hidden\" name=\"system\" value=\"" + systemName
				+ "\" /></td></tr></table><input type=\"submit\" value=\"Submit\" ' /></form>");
		return response.toString();
	}

}
