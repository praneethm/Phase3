package main.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.plugin.PluginService;
/**
 * 
 * @author Jon Cornado
 * This class handles starting and stopping of plugins
 */
@WebServlet("/updateStatus")
public class UpdateApplicationStatus extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String system = req.getParameter("system");
		String status = req.getParameter("status");
		// PluginService.plugins.get(system)
		System.out.println("printing status");
		System.out.println(req.getParameter("status"));
		System.out.println(system);
		if (status.equalsIgnoreCase("stopped")) {
			System.out.println("starting application");
			PluginService.plugins.get(system).run("");

		} else if (status.equalsIgnoreCase("running")) {
			PluginService.plugins.get(system).stop();
		}
		resp.sendRedirect("/FrontEnd/DashBoard.jsp");
	}

}
