package main.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import main.database.communication.Mysqlconn;
import main.plugin.PluginService;
/**
 * 
 * @author Jon Cornado
 *
 * This is used to get list of EndApplication list and also tells if the application is done or not
 */
@Path("/appList")
public class Dashboard {
	
	
	private static Boolean isSetupDone=false;
	public static boolean isSetupDone() {
		PreparedStatement ps;
		ResultSet rs = null;
		
		try {
			ps = Mysqlconn.getConnection().prepareStatement("select view_setupstatus()");
			rs = ps.executeQuery();
			rs.next();
			
			isSetupDone = rs.getBoolean(1);
			//if(null==isSetupDone)
				//isSetupDone=false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(isSetupDone && !CompleteSetup.started)
		{
			PluginService.getInstance();
		}
		
		return isSetupDone;
	}

	public static void setSetunDone(boolean isSetunDone) {
		isSetupDone = isSetunDone;
	}
/**
 * 
 * @return list of all the plugins available, on clicking this it shows credentials jsp
 */
	@GET
	@Path("/list")
	@Produces("text/html")
	public String getApplications() {

		
		StringBuilder builder=new StringBuilder();
		Connection conn = null;
		try {
			conn =Mysqlconn.getConnection();
			ResultSet rs=null;
			PreparedStatement ps;
		ps = conn.prepareStatement("select get_sysname()");
		
		rs = ps.executeQuery();
		
		while (rs.next()) {
			
		builder.append("<a class=\"dropdown-item\" onclick=\"here(this)\" href=\"#\">"+rs.getString(1)+"</a>");
		}
		conn.close();
		}
		catch(Exception e){
			
			// code to update error response servlet
			ActionBar.update("DB",e,true);
			System.out.println("caught error in dropdown");
			
		}
	
		if(isSetupDone)
		return builder.toString();
		else
			return "";
		
	}
	

}
