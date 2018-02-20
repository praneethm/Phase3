package main.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.plugin.PluginService;

@WebServlet("/MainApplication")
public class CompleteSetup extends HttpServlet{
	
	public static boolean started=false;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.getWriter().write("upload values to data base, set setup flag in database to \"true\" and then redirect to Dashboard.jsp");
		MasterBean.updateDatabase();
		PluginService pluginService=PluginService.getInstance();
		//resp.getWriter().flush();
		started=true;
		resp.sendRedirect("/FrontEnd/DashBoard.jsp");
	}
	
	
	
	

}
