package main.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Jon Cornado This class alone handles the setup phase of the
 *         application.
 * 
 */
@WebServlet("/setup")
public class ActionBarSetup extends HttpServlet {

	static HashMap<String, String> set;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub

		MasterBean mb = new MasterBean();

		String state;
		System.out.println("displaying request values from frontend" + req.getParameter("type"));
		// adding user details - presently not saving in database - has to be replaced
		// with functionality like plugins
		if (null == req.getParameter("type")) {

			state = "User Details";
			// System.out.println("present state " + state);

		} else {
			// get the name of the plugin name its processing now
			String now = req.getParameter("type").trim();
			if (mb.getHolder().containsKey(now.trim())) {
				// System.out.println("saving information for system " + now);
				for (String string : mb.getHolder().get(now).keySet()) {
					System.out.println("keys " + string + "value" + req.getParameter(string));
					mb.getHolder().get(now).put(string, req.getParameter(string));

				}

			}
			// state goes to the next plugin it has to process. This is when the user click
			// next in the front end
			// initially the frontend doesnot return any value for "type" so its null. for
			// this 1st case it send the user editing fields to front end.
			// User credentials --> next plugin --> next plugin --> till the last plugin
			//
			//
			//

			state = mb.getNext(req.getParameter("type"));
			// System.out.println("present state after first request" + state);

		}

		StringBuilder result = new StringBuilder();
		// the entire html page is built here
		result.append(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n"
						+ "<html>\r\n" + "<head>\r\n" + "\r\n"
						+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\r\n" + "\r\n"
						+ "\r\n" + "<!-- Bootstrap core CSS -->\r\n"
						+ "<link href=\"http://getbootstrap.com/dist/css/bootstrap.min.css\"\r\n"
						+ "	rel=\"stylesheet\">\r\n" + "\r\n" + "\r\n" + "<link\r\n"
						+ "	href=\"http://getbootstrap.com/docs/4.0/examples/dashboard/dashboard.css\"\r\n"
						+ "	rel=\"stylesheet\">\r\n" + "\r\n" + "<title>MIP Connector</title>\r\n" + "\r\n" + "\r\n"
						+ "\r\n" + "</head>");
		result.append("<nav class=\"navbar navbar-expand-md navbar-dark bg-dark fixed-top\">\r\n"
				+ "<a class=\"navbar-brand\" href=\"DashBoard.jsp\">Dashboard</a>\r\n"
				+ "<button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\"\r\n"
				+ "	data-target=\"#navbarsExampleDefault\"\r\n"
				+ "	aria-controls=\"navbarsExampleDefault\" aria-expanded=\"false\"\r\n"
				+ "	aria-label=\"Toggle navigation\">\r\n" + "	<span class=\"navbar-toggler-icon\"></span>\r\n"
				+ "</button>\r\n" + "\r\n" + "<div class=\"collapse navbar-collapse\" id=\"navbarsExampleDefault\">\r\n"
				+ "	<ul class=\"navbar-nav mr-auto\">\r\n"
				+ "		<li class=\"nav-item active\"><a class=\"nav-link\" href=\"#\">Edit\r\n"
				+ "				User Details <span class=\"sr-only\">(current)</span>\r\n" + "\r\n"
				+ "				<li class=\"nav-item dropdown\" disabled=\"disabled\"><a\r\n"
				+ "					class=\"nav-link dropdown-toggle\" href=\"http://example.com\"\r\n"
				+ "					id=\"dropdown01\" data-toggle=\"dropdown\" aria-haspopup=\"true\"\r\n"
				+ "					aria-expanded=\"false\">End Applications</a>\r\n"
				+ "					<div class=\"dropdown-menu\" aria-labelledby=\"dropdown01\">\r\n" + "\r\n" + "\r\n"
				+ "					</div></li>\r\n" + "	</ul>\r\n" + "\r\n" + "</div>\r\n" + "</nav>");
		result.append("<div class=\"container-fluid\">\r\n" + "	<div class=\"row\">");
		result.append("<nav class=\"col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar\">\r\n"
				+ "          <ul class=\"nav nav-pills flex-column\">");

		// Display list of systems on the left side(dashboard) eg. user, salesforce,
		// quickbook
		for (String string : mb.getHolder().keySet()) {
			if (string.equalsIgnoreCase(state)) {
				result.append("<li class=\"nav-item highlight\"   >\r\n" + "        <a class=\"nav-link\" href=\"#\">"
						+ string + "</a></li>");
			} else {

				result.append("<li class=\"nav-item\"   >\r\n" + "        <a class=\"nav-link\" href=\"#\">" + string
						+ "</a></li>");
			}

		}

		result.append("  </ul>\r\n" + "        </nav>");

		result.append("<main class=\"col-sm-9 ml-sm-auto col-md-10 pt-3\" role=\"main\">");

		if (!state.equalsIgnoreCase("")) {
			result.append("<div class=\"container\">\r\n" + "		<div class=\"row\">\r\n"
					+ "			<div class=\"col-xs-6\"> <form id=\"myForm\" action=\"/FrontEnd/setup\" method=\"GET\"><table> ");
		} else {
			result.append("<div class=\"container\">\r\n" + "		<div class=\"row\">\r\n"
					+ "			<div class=\"col-xs-6\"> <form id=\"myForm\" action=\"/FrontEnd/MainApplication\" method=\"GET\"><table> ");

		}
		if (!state.equalsIgnoreCase("")) {
			// complex logic involved and has to simplified, system was initially built for
			// text fields. with the addition of check box and auto populating
			// values for drop down we see key from database for plugins like (OFFSET, CODE)
			// that have to be handled differently
			for (String string : mb.getHolder().get(state).keySet()) {

				if (!string.equalsIgnoreCase("OFFSET")) {
					// code is written to make CODE and field null if offset is enabled. this is
					// when we fall into this condition
					if (null != mb.getHolder().get(state).get(string)) {
						// Code is a key from MIP plugin that gets a value that comes from external
						// server
						if (string.equalsIgnoreCase("CODE")) {
							System.out.println("in the code section");
							result.append("			<tr>\r\n" + "				<td>Disable Offset:</td>\r\n"
									+ "				<td><input type=\"checkbox\" name=\"OFFSET\" id=\"OFFSET\" value=\"false\" ></td>\r\n"
									+ "			</tr>");
							result.append("			<tr>\r\n" + "				<td> Select Cash Account:</td>\r\n"
									+ "				<td> "
									+ "                <select class=\"quantity\" id=\"CODE\" name=\"CODE\" disabled=\"disabled\">\r\n"
									+ "					<option value=\"\">Disable offset for selection</option></div>\r\n"
									+ "                </select>\r\n" + "           </td>\r\n" + "			</tr>");
						} else if (string.equalsIgnoreCase("USEMIPONLINE")) {
							result.append("		<tr>\r\n" + "			<td>Use MIP Online Version:</td>\r\n"
									+ "			<td><input type=\"checkbox\" checked  name=\"USEMIPONLINE\" id=\"USEMIPONLINE\"\r\n"
									+ "				value=\"true\" ></td>\r\n" + "		</tr>");

						}
						// this is the actually part where the key and value pair of text field type are
						// generated
						else {
							result.append("			<tr>\r\n" + "				<td>" + string + ":</td>\r\n"
									+ "				<td><input type=\"text\" value=\""
									+ mb.getHolder().get(state).get(string) + "\" name=\"" + string + "\" id =\""
									+ string + "\"></td>\r\n" + "			</tr>");
						}
					} else {
						// when offset is disabled then the CODE should already have a value
						if (string.equalsIgnoreCase("CODE")) {
							System.out.println("in the code section");
							result.append("			<tr>\r\n" + "				<td>Disable Offset:</td>\r\n"
									+ "				<td><input type=\"checkbox\" name=\"OFFSET\" id=\"OFFSET\" value=\"false\" ></td>\r\n"
									+ "			</tr>");
							result.append("			<tr>\r\n" + "				<td> Select Cash Account:</td>\r\n"
									+ "				<td> "
									+ "                <select class=\"quantity\" id=\"CODE\" name=\"CODE\" disabled=\"disabled\">\r\n"
									+ "					<option value=\"0\">Disable offset for selection</option></div>\r\n"
									+ "                </select>\r\n" + "           </td>\r\n" + "			</tr>");
						} else if (string.equalsIgnoreCase("UseMIPOnline")) {
							result.append("		<tr>\r\n" + "			<td>Use MIP Online Version:</td>\r\n"
									+ "			<td><input type=\"checkbox\" name=\"USEMIPONLINE\" id=\"USEMIPONLINE\"\r\n"
									+ "				value=\"true\" ></td>\r\n" + "		</tr>");

						}
						// this is the actually part where the key and value pair of text field type are
						// generated
						else {

							result.append("			<tr>\r\n" + "				<td>" + string + ":</td>\r\n"
									+ "				<td><input type=\"text\"  name=\"" + string + "\" id =\"" + string
									+ "\"></td>\r\n" + "			</tr>");
						}
					}
				}
			}
			result.append("	<td><input type=\"hidden\" name=\"type\" value=\" " + state + " \"></td>\r\n"
					+ "			</tr>");
		}
		result.append("</table>");
		// this is the end of plugins state and goes to CompleteSetup class to insert
		// all the key values to database
		if (!state.equalsIgnoreCase("")) {
			result.append("<input type=\"submit\" value=\"Submit\" /></form>");
		} else {

			result.append("<input type=\"submit\" value=\"Update to database\" /></form>");

		}
		result.append("</div>");
		result.append("</div>");
		result.append("</main>	</div>\r\n" + "</div>\r\n" + "");
		result.append(
				"<script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\r\n"
						+ "<script>\r\n" + "	window.jQuery\r\n" + "			|| document\r\n"
						+ "					.write('<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"><\\/script>')\r\n"
						+ "</script>\r\n"
						+ "<script src=\"http://getbootstrap.com/assets/js/vendor/popper.min.js\"></script>\r\n"
						+ "<script src=\"http://getbootstrap.com/dist/js/bootstrap.min.js\"></script>\r\n"
						+ "<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->\r\n"
						+ "<script src=\"http://getbootstrap.com/assets/js/ie10-viewport-bug-workaround.js\"></script>\r\n"
						+ "<script src=\"http://code.jquery.com/jquery-latest.js\">\r\n" + "</script>\r\n"
						+ "<script src=\"http://malsup.github.com/jquery.form.js\"></script> \r\n"
						+ "<link\r\n href=\"Status.css\"\r\n rel=\"stylesheet\"> <link\r\n href=\"NewFile.css\"\r\n rel=\"stylesheet\"> ");

		result.append("<script type=\"text/javascript\">\r\n" + "\r\n" + "var $checkBox = $('#OFFSET');\r\n"
				+ "$select = $('#CODE');\r\n" + "\r\n" + "$checkBox.on('change',function(e){\r\n" + "\r\n"
				+ "if ($(this).is(':checked')){$checkBox.attr('value','true');\r\n"
				+ "    $.get('/FrontEnd/Codevalues', {");
		if (null != MasterBean.getHolder().get(state))
			for (String string : MasterBean.getHolder().get(state).keySet()) {
				result.append(string + " : $('#" + string + "').val(),");
			}
		result.append("system" + " : '" + state + "'");

		result.append("}, function(responseText) {\r\n" + "		// var $response = $(responseText);\r\n"
				+ "        $('#CODE').html(responseText);\r\n alert(responseText);" + "$(\".overlay\").show();"
				+ "        $select.removeAttr('disabled');\r\n" + "	});\r\n" + "}else{\r\n"
				+ "   $select.attr('disabled','disabled');$checkBox.attr('value','false');\r\n $('#CODE').html(\"<option value='11000'>disable offset to enable this</option>\");\r\n"
				+ "}\r\n" + "});\r\n" +

				"var $checkBox1 = $('#USEMIPONLINE');\r\n" + "$select1 = $('#MIPINSTANCEURL');\r\n"
				+ "$checkBox1.on('change',function(e){\r\n" + "if ($(this).is(':checked')){\r\n"
				+ "	alert(\"Using MIP Online version\");\r\n" + "	$checkBox1.attr('value','true');\r\n"
				+ "$select1.attr('readonly','readonly');\r\n" + "}else{\r\n"
				+ "	alert(\"Please enter MIPINSTANCEURL\");\r\n" + "	$checkBox1.attr('value','false');\r\n"
				+ "$select1.removeAttr('readonly');\r\n" + "}\r\n" + "});" +

				"</script></html>");
		PrintWriter writer = resp.getWriter();
		writer.print(result.toString());
		writer.flush();

	}

	/*
	 * public String ActionBarItems() { set = new HashMap<String, String>();
	 * set.put("DB", "UP");
	 * 
	 * return "<li class=\"nav-item\"   onclick=\"display(this)\" >\r\n" +
	 * "        <a class=\"nav-link\" href=\"#\">" +"user" + "</a></li>";
	 * ar.add("name"); ar.add("address"); ar.add("contact");
	 * 
	 * StringBuilder result=new StringBuilder(); result.
	 * append("<nav class=\"col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar\">\r\n"
	 * + "          <ul class=\"nav nav-pills flex-column\">");
	 * result.append("<li class=\"nav-item\"   >\r\n" +
	 * "        <a class=\"nav-link\" href=\"#\">" +"user" + "</a></li>");
	 * result.append("  </ul>\r\n" + "        </nav>");
	 * 
	 * result.
	 * append("<main class=\"col-sm-9 ml-sm-auto col-md-10 pt-3\" role=\"main\">");
	 * result.append("<div class=\"container\">\r\n" +
	 * "		<div class=\"row\">\r\n" +
	 * "			<div class=\"col-xs-6\"> <form id=\"myForm\" action=\"/FrontEnd/rest/setupCredentials/save\" method=\"GET\"><table> "
	 * ); for (String string : ar) {
	 * 
	 * result.append("			<tr>\r\n" +
	 * "				<td>"+string+":</td>\r\n" +
	 * "				<td><input type=\"text\" name=\""+string+"\"></td>\r\n" +
	 * "			</tr>"); } result.append("</table>");
	 * result.append("<input type=\"submit\" value=\"Submit\" /></form>");
	 * result.append("</div>"); result.append("</div>"); result.append("</main>");
	 * return result.toString(); }
	 */

}
