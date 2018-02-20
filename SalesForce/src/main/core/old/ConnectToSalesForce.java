/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.core.old;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.message.BasicHeader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import main.core.salesForce;

import java.util.Timer;
import java.util.TimerTask;

class SessionChecker {

	private Timer timer;
	private long interval;

	public SessionChecker(long interval) {
		this.interval = interval;
	}

	/**
	 * 
	 */
	public void start() {
		this.timer = new Timer();
		TimerTask task = new TimerTask() {

			public void run() {
				ConnectToSalesForce.loginUsingOAuthForSalesForce();
			};
		};
		timer.schedule(task, interval, interval);
	}

	public void setInterval(long interval) {
		this.interval = interval;
		stop();
		start();
	}

	/**
	 * 
	 */
	public void stop() {
		timer.cancel();
	}

}

/**
 * Contains Functions that help to interact with the SalesForce account.
 * 
 * @author Saranya
 */
public class ConnectToSalesForce {

	private SessionChecker session;

	public ConnectToSalesForce() {

		loginUsingOAuthForSalesForce();
		// session = new SessionChecker(900000);
		// session.start();
	}

	/**
	 * Logs into the Sales Force account using OAuth 2.0 Fetches the Token from the
	 * response for subsequent requests
	 */
	@SuppressWarnings("static-access")
	public static void loginUsingOAuthForSalesForce() {
		try {

			// Constants.ReadCredsFromFile();
			UserCredentials.updateValues();
			String lstrPath = "https://" + UserCredentials.loginInstanceDomain + Constants.OAUTH_ENDPOINT;
			System.out.println("___________________Login for SalesForce_________________");
			System.out.println("Url For Login : " + lstrPath);

			// Login Auth details needs to be set in the URL
			// Note: Do not change to json, the request will not be
			// authenticated.
			StringBuilder lstrbldrRequestBody = new StringBuilder("grant_type=password");

			lstrbldrRequestBody.append("&username=");
			lstrbldrRequestBody.append(UserCredentials.userName);
			lstrbldrRequestBody.append("&password=");
			lstrbldrRequestBody.append(UserCredentials.password);
			lstrbldrRequestBody.append("&client_id=");
			lstrbldrRequestBody.append(UserCredentials.consumerKey);
			lstrbldrRequestBody.append("&client_secret=");
			lstrbldrRequestBody.append(UserCredentials.consumerSecret);
			System.out.println("Request : " + lstrbldrRequestBody.toString());

			JSONObject ljsonResponse = RestCalls.RestPostWithFormEncoding(lstrPath, lstrbldrRequestBody.toString());
			if (null == ljsonResponse) {
				Constants.ACCESS_TOKEN=null;
				return;
			}
			OauthResponse.SetGlobalVariablesFromResponse(ljsonResponse);

			Constants.BASE_URI = Constants.INSTANCE_URL + Constants.REST_ENDPOINT + "/v" + UserCredentials.apiVersion
					+ ".0";

			Constants.OAUTH_HEADER = new BasicHeader("Authorization", "OAuth " + Constants.ACCESS_TOKEN);

			System.out.println("\nSuccessfully logged in to instance: " + Constants.BASE_URI);
			System.out.println("___________________END________________");
		} catch (NullPointerException ioe) {
			Constants.OAUTH_HEADER = null;
			Constants.BASE_URI = null;
			ioe.printStackTrace();
		}
	}

	/**
	 * Queries SalesForce for Distribution Code based on the Opportunity Id received
	 * in the Push Notification
	 * 
	 * @param pstrOpportunityId
	 *            : Opportunity Id as received in the push notification
	 * @return : Distribution Code as fetched from SalesForce
	 */
	public static String getDistributionCode(String pstrOpportunityId) {
		String lstrDistributionCode = "";
		String lstrCampaignId = "";
		try {
			// Step 1 : Get Campaign Id using the Opportunity Id
			String lstrPath = Constants.BASE_URI + "/query?q=SELECT+CampaignId+FROM+Opportunity+WHERE+Id+=+'"
					+ pstrOpportunityId + "'";
			System.out.println("___________________Distribution Code_________________");
			System.out.println("Url For Campaign Id : " + lstrPath);

			JSONObject lobjJsonObj = RestCalls.RestGet(lstrPath, Constants.REQUESTING_CLASS.SALESFORCE);
			if (!lobjJsonObj.isEmpty()) {
				if (lobjJsonObj.containsKey("records")) {
					JSONArray larrRecords = (JSONArray) lobjJsonObj.get("records");
					if (!larrRecords.isEmpty() && larrRecords.size() > 0) {
						for (int count = 0; count < larrRecords.size();) {
							JSONObject lobjRecord = (JSONObject) larrRecords.get(count++);
							if (lobjRecord.containsKey("CampaignId")) {
								lstrCampaignId = lobjRecord.get("CampaignId").toString();
								break;
							} else {
								Logger.getLogger(ConnectToSalesForce.class.getName()).log(Level.SEVERE,
										"Campaign Id not found in the response for Opportunity Id : "
												+ pstrOpportunityId);
								return lstrDistributionCode;
							}
						}
					}
				}
			} else {
				Logger.getLogger(ConnectToSalesForce.class.getName()).log(Level.SEVERE,
						"Response for Campaign Id not received for Opportunity Id : " + pstrOpportunityId);
			}

			if (!lstrCampaignId.isEmpty()) {
				// Step 2 : Get Distribution Code using the Campaign Id
				lstrPath = Constants.BASE_URI
						+ "/query?q=SELECT+Distribution_Code_Lookup__r.Name+FROM+Campaign+WHERE+Id+=+'" + lstrCampaignId
						+ "'";
				System.out.println("Url For Dsctribution Code Name : " + lstrPath);

				lobjJsonObj = RestCalls.RestGet(lstrPath, Constants.REQUESTING_CLASS.SALESFORCE);
				if (!lobjJsonObj.isEmpty()) {
					if (lobjJsonObj.containsKey("records")) {
						JSONArray larrRecords = (JSONArray) lobjJsonObj.get("records");
						if (larrRecords.size() > 0) {
							for (int count = 0; count < larrRecords.size(); count++) {
								JSONObject lobjRecord = (JSONObject) larrRecords.get(count);
								if (lobjRecord.containsKey("Distribution_Code_Lookup__r")) {
									JSONObject lobjDistribution = (JSONObject) lobjRecord
											.get("Distribution_Code_Lookup__r");

									if (lobjDistribution.containsKey("Name")) {
										lstrDistributionCode = lobjDistribution.get("Name").toString();
									}
								} else {
									Logger.getLogger(ConnectToSalesForce.class.getName()).log(Level.SEVERE,
											"Distribution Code not found in the response for Campaign Id : "
													+ lstrCampaignId);
									return lstrDistributionCode;
								}
							}
						} else {
							Logger.getLogger(ConnectToSalesForce.class.getName()).log(Level.SEVERE,
									"Response for Distribution Code not received for Campaign Id : " + lstrCampaignId);
						}
					}
				}
			}
			System.out.println("___________________END________________");
		} catch (Exception ex) {
			Logger.getLogger(ConnectToSalesForce.class.getName()).log(Level.SEVERE, null, ex);
		}
		JSONObject obj = new JSONObject();
		obj.put("DistributionCode", lstrDistributionCode);

		salesForce.setInterfaceData("MIPExtra", obj, false);
		return lstrDistributionCode;
	}

	/**
	 * 
	 * @param pstrOpportunityId
	 *            : Opportunity Id as received in the push notification
	 * @return
	 */
	public static String[] getAccountNameAndGeneralLedgerCode(String pstrOpportunityId) {
		String[] lstrarAccountAndGL = new String[2];

		String lstrPath = Constants.BASE_URI
				+ "/query?q=SELECT+o.Account.Name+,+o.Account.Account_Type__c+FROM+Opportunity+o+where+Id+=+'"
				+ pstrOpportunityId + "'";

		System.out.println("___________________Account Type & GL Code______________");
		System.out.println("Url For Account type and GL code : " + lstrPath);

		JSONObject lobjJsonObj = RestCalls.RestGet(lstrPath, Constants.REQUESTING_CLASS.SALESFORCE);
		if (!lobjJsonObj.isEmpty()) {
			if (lobjJsonObj.containsKey("records")) {
				JSONArray larrRecords = (JSONArray) lobjJsonObj.get("records");
				if (larrRecords != null && larrRecords.size() > 0) {
					for (int count = 0; count < larrRecords.size(); count++) {
						JSONObject lobjRecord = (JSONObject) larrRecords.get(count);
						if (lobjRecord.containsKey("Account")) {
							JSONObject lobjAccount = (JSONObject) lobjRecord.get("Account");
							if (lobjAccount.containsKey("Name"))
								lstrarAccountAndGL[0] = lobjAccount.get("Name").toString();

							if (lobjAccount.containsKey("Account_Type__c")) {

								String lstrAccountType = (String) lobjAccount.get("Account_Type__c");
								lstrPath = Constants.BASE_URI
										+ "/query?q=SELECT+GL_Code__c+FROM+Account_Type__c+WHERE+Id+=+'"
										+ lstrAccountType + "'";

								System.out.println("___________________Account Name______________");
								System.out.println("Url For Account Name : " + lstrPath);

								JSONObject lobjJsonAccountName = RestCalls.RestGet(lstrPath,
										Constants.REQUESTING_CLASS.SALESFORCE);
								if (!lobjJsonAccountName.isEmpty()) {
									if (lobjJsonAccountName.containsKey("records")) {
										JSONArray larrANRecords = (JSONArray) lobjJsonAccountName.get("records");
										if (larrANRecords != null && larrANRecords.size() > 0) {
											for (int ancount = 0; ancount < larrANRecords.size(); ancount++) {
												JSONObject lobjANRecord = (JSONObject) larrANRecords.get(ancount);
												if (lobjANRecord.containsKey("GL_Code__c"))
													lstrarAccountAndGL[1] = Integer.toString(
															((Double) lobjANRecord.get("GL_Code__c")).intValue());
											}
										} else {
											Logger.getLogger(ConnectToSalesForce.class.getName()).log(Level.SEVERE,
													"records is empty for Account Id : " + lstrAccountType);
										}
									} else {
										Logger.getLogger(ConnectToSalesForce.class.getName()).log(Level.SEVERE,
												"records not found for Account Id : " + lstrAccountType);
									}
								} else {
									Logger.getLogger(ConnectToSalesForce.class.getName()).log(Level.SEVERE,
											"Response for Account Name is empty");
								}
							}
						}
					}
				} else {
					Logger.getLogger(ConnectToSalesForce.class.getName()).log(Level.SEVERE,
							"records is empty for Opportunity Id : " + pstrOpportunityId);
				}
			} else {
				Logger.getLogger(ConnectToSalesForce.class.getName()).log(Level.SEVERE,
						"Response for GL Code does not contain key records for Opportunity Id : " + pstrOpportunityId);
			}
		} else {
			Logger.getLogger(ConnectToSalesForce.class.getName()).log(Level.SEVERE,
					"Response for GL Code not received for Opportunity Id : " + pstrOpportunityId);
		}
		System.out.println("___________________END________________");
		JSONObject obj = new JSONObject();
		JSONArray objArr = new JSONArray();
		objArr.add(lstrarAccountAndGL[0]);
		objArr.add(lstrarAccountAndGL[1]);
		obj.put("AccountNameAndGeneralLedgerCode", objArr);
		salesForce.setInterfaceData("MIPExtra", obj, false);
		return lstrarAccountAndGL;
	}
}
