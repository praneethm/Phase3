package main.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/update")
public class UpdateEndApplication extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String system = req.getParameter("system");
		if (MasterBean.getHolder().containsKey(system.trim())) {
			System.out.println("saving information for system " + system);
			for (String string : MasterBean.getHolder().get(system).keySet()) {
				MasterBean.getHolder().get(system).get(string).setValue(req.getParameter(string));
			}
			
			

		}
		
		MasterBean.updateDatabase(system,"system");
		resp.flushBuffer();

	}

}
