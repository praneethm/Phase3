package main.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.plugin.PluginService;
/**
 * 
 * @author Jon Cornado
 *	This is used for MIP to get values from external server and populates it into the dropdown box
 */

@WebServlet("/Codevalues")
public class HandleSetupConfigurations extends HttpServlet{
	
	Plugin plugin;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String system=req.getParameter("system");
		plugin = PluginService.StaticPluginCalls(req.getParameter("system").toString());
		HashMap<String, String> list = (HashMap<String, String>) plugin.extras(MasterBean.getHolder().get(system));
		StringBuilder sb = new StringBuilder();
		sb.append("<select class=\"quantity\" id=\"parttwoqty\" name='qty2' disabled=\"disabled\">");
		for (String string : list.keySet()) {
			sb.append("<option value=\""+string+"\">"+list.get(string)+"</option>");
		System.out.println("adding to ropdown list");
		System.out.println(string);
		}
		sb.append("</select>");
		PrintWriter writer = resp.getWriter();
		writer.print(sb.toString());
		writer.flush();
	}

	
	
	
	
}
