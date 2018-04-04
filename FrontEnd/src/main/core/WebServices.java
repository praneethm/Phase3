package main.core;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import main.plugin.PluginService;

/**
 * 
 * @author Jon Cornado rest api calls to the server is handled here, since
 *         plugin cannot expose api due to the plugin nature, we use this class
 *         to get information from the plugins send response
 *
 */
@Path("/ConnectToMip")
public class WebServices {
	/**
	 * called by salesforce.
	 * 
	 * @return
	 */
	@GET
	@Path("/FetchCustomerID")
	@Produces(MediaType.APPLICATION_JSON)
	public Response FetchCustomerID() {
		Plugin plugin = PluginService.plugins.get("MIP");
		return (Response) plugin.extras("FetchCustomerID");
	}

	
	
	/**
	 * called by salesforce.
	 * 
	 * @return
	 */
	@GET
	@Path("/FetchDistributionCode")
	@Produces(MediaType.APPLICATION_JSON)
	public Response FetchDistributionCode() {
		Plugin plugin = PluginService.plugins.get("MIP");
		return (Response) plugin.extras("FetchDistributionCode");
	}

	/**
	 * called by salesforce.
	 * 
	 * @return
	 */
	@GET
	@Path("/FetchGeneralLedgerCodes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response FetchGeneralLedgerCodes() {
		Plugin plugin = PluginService.plugins.get("MIP");
		System.out.println("fetching FetchGeneralLedgerCodes");
		return (Response) plugin.extras("FetchGeneralLedgerCodes");
	}

}
