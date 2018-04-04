/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.core.old;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.http.message.BasicHeader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * 
 * @author Saranya
 */

public class ConnectToMip {

	public static Response FetchDistributionCode() {
		try {
			LoginIntoMip();
			System.out.println("________________Distribution Code_______________");
			String lstrPath = MIPConstants.MIP_BASE_URI + "/api/dc";
			JSONObject ljsonDistributionCodes = MIPRestCalls.RestGet(lstrPath, MIPConstants.REQUESTING_CLASS.MIP);
			System.out.println("Distribution Code List :\n" + ljsonDistributionCodes.toJSONString());

			ResponseBuilder lresponseBuilder = Response.status(Status.OK);
			lresponseBuilder.header("Access-Control-Allow-Origin", "https://" + "login.salesforce.com");
			lresponseBuilder.header("Access-Control-Allow-Headers",
					"Authorization, Auth, Token, Access-Token, Access_Token, AccessToken, Code");

			System.out.println("________________END_______________");
			return lresponseBuilder.entity(ljsonDistributionCodes).build();
		} catch (Exception ex) {
			Logger.getLogger(ConnectToMip.class.getName()).log(Level.SEVERE, null, ex);
			return Response.status(Status.INTERNAL_SERVER_ERROR).header("FetchDistributionCode : ", ex.getMessage())
					.build();
		}
	}

	public static Response FetchCustomerID() {
		try {
			LoginIntoMip();

			System.out.println("________________Distribution Code_______________");
			String lstrPath = MIPConstants.MIP_BASE_URI + "/api/customers/lookups/CUSTOMER_ID";
			JSONObject ljsonDistributionCodes = MIPRestCalls.RestGet(lstrPath, MIPConstants.REQUESTING_CLASS.MIP);
			System.out.println("Distribution Code List :\n" + ljsonDistributionCodes.toJSONString());

			ResponseBuilder lresponseBuilder = Response.status(Status.OK);
			lresponseBuilder.header("Access-Control-Allow-Origin", "https://" + "login.salesforce.com");
			lresponseBuilder.header("Access-Control-Allow-Headers",
					"Authorization, Auth, Token, Access-Token, Access_Token, AccessToken, Code");

			System.out.println("________________END_______________");
			return lresponseBuilder.entity(ljsonDistributionCodes).build();
		} catch (Exception ex) {
			Logger.getLogger(ConnectToMip.class.getName()).log(Level.SEVERE, null, ex);
			return Response.status(Status.INTERNAL_SERVER_ERROR).header("FetchCustomerID : ", ex.getMessage()).build();
		}
	}

	public static Response FetchGeneralLedgerCodes() {
		LoginIntoMip();
		String lstrGLCodeNumber = "";
		HashMap<String, String> lstrCoaSegmentId = new HashMap<>();
		try {

			// Get the segment id for GL
			String lstrPath;
			JSONObject ljsonChartOfAccounts;

			// Get the COA code based on segment id and code description
			lstrPath = MIPConstants.MIP_BASE_URI + "/api/coa/segments/accounts";
			ljsonChartOfAccounts = MIPRestCalls.RestGet(lstrPath, MIPConstants.REQUESTING_CLASS.MIP);
			System.out.println("Chart Of Accounts :\n" + ljsonChartOfAccounts.toJSONString());

			JSONArray ljsonCoaArray = (JSONArray) ljsonChartOfAccounts.get("COA_SEGID");
			if (ljsonCoaArray != null && !ljsonCoaArray.isEmpty()) {
				for (int count = 0; count < ljsonCoaArray.size(); count++) {
					JSONObject ljsonSegment = (JSONObject) ljsonCoaArray.get(count);
					if (ljsonSegment.containsKey("COA_CODE")
							&& ljsonSegment.get("COA_TYPE").toString().equalsIgnoreCase("REV")) {
						System.out.println("adding to glcode array");
						System.out.println(ljsonSegment.get("COA_CODE").toString());
						lstrCoaSegmentId.put(ljsonSegment.get("COA_CODE").toString(),
								ljsonSegment.get("COA_CODE").toString() + "-"
										+ ljsonSegment.get("COA_TITLE").toString());
						// lstrCoaSegmentId.add(ljsonSegment.get("COA_CODE").toString());

					}
				}
			}
			ResponseBuilder lresponseBuilder = Response.status(Status.OK);
			lresponseBuilder.header("Access-Control-Allow-Origin", "https://" + "login.salesforce.com");
			lresponseBuilder.header("Access-Control-Allow-Headers",
					"Authorization, Auth, Token, Access-Token, Access_Token, AccessToken, Code");

			System.out.println("________________END_______________");
			return lresponseBuilder.entity(lstrCoaSegmentId).build();

		} catch (Exception ex) {
			Logger.getLogger(ConnectToMip.class.getName()).log(Level.SEVERE, null, ex);
			return Response.status(Status.INTERNAL_SERVER_ERROR).header("FetchCustomerID : ", ex.getMessage()).build();
		}
	}

	public static HashMap<String, String> getGeneralLedgerCodesBasedOnDescription() {
		String lstrGLCodeNumber = "";
		HashMap<String, String> lstrCoaSegmentId = new HashMap<>();
		try {

			// Get the segment id for GL
			String lstrPath;
			JSONObject ljsonChartOfAccounts;

			// Get the COA code based on segment id and code description
			lstrPath = MIPConstants.MIP_BASE_URI + "/api/coa/segments/accounts";
			ljsonChartOfAccounts = MIPRestCalls.RestGet(lstrPath, MIPConstants.REQUESTING_CLASS.MIP);
			System.out.println("Chart Of Accounts :\n" + ljsonChartOfAccounts.toJSONString());

			JSONArray ljsonCoaArray = (JSONArray) ljsonChartOfAccounts.get("COA_SEGID");
			if (ljsonCoaArray != null && !ljsonCoaArray.isEmpty()) {
				for (int count = 0; count < ljsonCoaArray.size(); count++) {
					JSONObject ljsonSegment = (JSONObject) ljsonCoaArray.get(count);
					if (ljsonSegment.containsKey("COA_CODE")
							&& ljsonSegment.get("COA_TYPE").toString().equalsIgnoreCase("CSH")) {
						System.out.println("adding to glcode array");
						System.out.println(ljsonSegment.get("COA_CODE").toString());
						lstrCoaSegmentId.put(ljsonSegment.get("COA_CODE").toString(),
								ljsonSegment.get("COA_CODE").toString() + "-" + ljsonSegment.get("COA_TITLE").toString()
										+ "-" + ljsonSegment.get("COA_SHORT_TITLE").toString());
						// lstrCoaSegmentId.add(ljsonSegment.get("COA_CODE").toString());

					}
				}

			}
		} catch (Exception ex) {
			Logger.getLogger(ConnectToMip.class.getName()).log(Level.SEVERE, null, ex);
		}
		return lstrCoaSegmentId;
	}

	public static void LoginIntoMip(String login, String password, String MIP_BASE_URI) {
		try {
			MIPConstants.ReadFileForMipUrl();
			System.out.println(MIP_BASE_URI);
			MIPConstants.MIP_BASE_URI = MIP_BASE_URI;
			String lstrPath = MIP_BASE_URI + "/api/security/login";
			System.out.println("________________Login into MIP_______________");
			System.out.println("Path for Login : " + lstrPath);
 
			// create the JSON object containing the new contacts details.
			JSONObject ljsonLoginCredentials = new JSONObject();
			ljsonLoginCredentials.put("login", login);
			ljsonLoginCredentials.put("org", "NTO");
			ljsonLoginCredentials.put("password", password);

			System.out.println("JSON for Login into MIP :\n" + ljsonLoginCredentials.toJSONString());

			JSONObject ljsonResponse = MIPRestCalls.RestPost(lstrPath, ljsonLoginCredentials,
					MIPConstants.REQUESTING_CLASS.MIP);

			if (ljsonResponse != null && ljsonResponse.containsKey("token")) {
				MIPConstants.MIP_TOKEN = ljsonResponse.get("token").toString();
			}
			else {
				MIPConstants.MIP_TOKEN="";
			}
			System.out.println("Token from response: " + MIPConstants.MIP_TOKEN);
			MIPConstants.MIP_AUTH_HEADER = new BasicHeader("Authorization-Token", MIPConstants.MIP_TOKEN);
			System.out.println("________________END_______________");
		} catch (Exception ex) {
			Logger.getLogger(ConnectToMip.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Logs into MIP using REST POST call.
	 */
	@SuppressWarnings("unchecked")
	public static Boolean LoginIntoMip() {
		boolean isValid=false;
		try {
			MIPConstants.ReadFileForMipUrl();
			String lstrPath;
			if (!MIPConstants.USEONLINE)
				lstrPath = MIPConstants.MIP_BASE_URI + "/api/security/login";
			else
				lstrPath = "https://login.abilaonline.com/api/v1/sso/mipadv/login";
			System.out.println("________________Login into MIP_______________");
			System.out.println("Path for Login : " + lstrPath);

			// create the JSON object containing the new contacts details.
			JSONObject ljsonLoginCredentials = new JSONObject();
			if (MIPConstants.USEONLINE)
				ljsonLoginCredentials.put("username", MIPConstants.MIP_LOGIN);
			else
				ljsonLoginCredentials.put("login", MIPConstants.MIP_LOGIN);
			if (!MIPConstants.USEONLINE)
				ljsonLoginCredentials.put("org", MIPConstants.MIP_ORG);
			ljsonLoginCredentials.put("password", MIPConstants.MIP_PASSWORD);

			System.out.println("JSON for Login into MIP :\n" + ljsonLoginCredentials.toJSONString());

			JSONObject ljsonResponse = MIPRestCalls.RestPost(lstrPath, ljsonLoginCredentials,
					MIPConstants.REQUESTING_CLASS.MIP);

			if (ljsonResponse != null) {
				if (MIPConstants.USEONLINE) {
					MIPConstants.MIP_TOKEN = ljsonResponse.get("accessToken").toString();
					System.out.println("successfully got token");
				}
				else {
					MIPConstants.MIP_TOKEN = ljsonResponse.get("token").toString();
				System.out.println("success in getting token from hosted mip server");
				}
				isValid=true;
			}
			System.out.println("Token from response: " + MIPConstants.MIP_TOKEN);
			MIPConstants.MIP_AUTH_HEADER = new BasicHeader("Authorization-Token", MIPConstants.MIP_TOKEN);
			System.out.println("________________END_______________");
			return isValid;
		} catch (Exception ex) {
			Logger.getLogger(ConnectToMip.class.getName()).log(Level.SEVERE, null, ex);
			isValid=false;
		}
		return isValid;
	}

	/**
	 * Checks if an unposted session exists with today's date If exists, fetches the
	 * session id Else creates a new session with today's date Returns the session
	 * id retrieved or generated
	 * 
	 * @return : Unposted session id for inserting a document
	 */
	@SuppressWarnings("unchecked")
	public static String getSessionId() {
		LoginIntoMip();
		String sessionId = "CR" + new SimpleDateFormat("yyyyMMdd").format(new Date());
		String lstrPath;
		JSONObject ljsonResponse;
		JSONArray ljsonSessionIds = null;
		Boolean blnSessionExists = false;

		try {
			// Step 1 : Get the list of existing session id's
			System.out.println("________________Session Id_______________");
			lstrPath = MIPConstants.MIP_BASE_URI + "/api/te/CR/sessions/ids";
			System.out.println("Path to fetch session id's : \n" + lstrPath);
			ljsonSessionIds = MIPRestCalls.RestGetJsonArray(lstrPath, MIPConstants.REQUESTING_CLASS.MIP);

			// Step 2: Check if it contains the session id with today's date
			for (int count = 0; count < ljsonSessionIds.size(); count++) {
				if (ljsonSessionIds.get(count).equals(sessionId)) {
					blnSessionExists = true;
					break;
				}
			}

			do {
				if (blnSessionExists) {
					// Step 3: If session id exists, Check if it is unposted
					System.out.println("________________Details of Unique Session_______________");
					lstrPath = MIPConstants.MIP_BASE_URI + "/api/te/CR/sessions/" + sessionId;
					System.out.println("Path for session details : \n" + lstrPath);
					ljsonResponse = MIPRestCalls.RestGet(lstrPath, MIPConstants.REQUESTING_CLASS.MIP);
					JSONArray jsonFieldsArray = (JSONArray) ljsonResponse.get("fields");
					for (int count = 0; count < jsonFieldsArray.size(); count++) {
						JSONObject ljsonObject = (JSONObject) jsonFieldsArray.get(count);
						if (ljsonObject.containsKey("SESSION_POSTED_STATUS")
								&& ljsonObject.get("SESSION_POSTED_STATUS").equals("Unposted")) {
							blnSessionExists = true;
							break;
						} else
							blnSessionExists = false;
					}

					if (!blnSessionExists) {
						String[] sessionArray = sessionId.split("-");
						if (sessionArray.length > 0)
							sessionId = sessionArray[0] + "-" + (int) (Math.random() * 10 + 1);
					}
					System.out.println("___________________________________");

				} else {
					try {
						System.out.println("________________Create Unique Session_______________");
						lstrPath = MIPConstants.MIP_BASE_URI + "/api/te/CR/sessions";
						System.out.println("Path for creation of session :\n " + lstrPath);

						JSONObject jsonRequest = new JSONObject();
						JSONArray jsonFieldsArray = new JSONArray();

						JSONObject ljsonSessionId = new JSONObject();
						ljsonSessionId.put("SESSION_SESSIONNUMID", sessionId);
						jsonFieldsArray.add(ljsonSessionId);

						JSONObject ljsonSessionStatus = new JSONObject();
						ljsonSessionStatus.put("SESSION_STATUS", "BP");
						jsonFieldsArray.add(ljsonSessionStatus);

						JSONObject ljsonSessionDesc = new JSONObject();
						ljsonSessionDesc.put("SESSION_DESCRIPTION", "SalesForce Cash Reciepts");
						jsonFieldsArray.add(ljsonSessionDesc);

						JSONObject ljsonSessionDate = new JSONObject();
						ljsonSessionDate.put("SESSION_SESSIONDATE",
								new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
						jsonFieldsArray.add(ljsonSessionDate);

						JSONObject ljsonSessionCurrency = new JSONObject();
						ljsonSessionCurrency.put("SESSION_CURRENCY_TYPE", "USD");
						jsonFieldsArray.add(ljsonSessionCurrency);

						jsonRequest.put("fields", jsonFieldsArray);
						System.out.println("Request :\n" + jsonRequest.toJSONString());
						ljsonResponse = MIPRestCalls.RestPost(lstrPath, jsonRequest, MIPConstants.REQUESTING_CLASS.MIP);
						blnSessionExists = true;
						System.out.println("________________END_______________");
					} catch (Exception ex) {
						Logger.getLogger(ConnectToMip.class.getName()).log(Level.SEVERE,
								"Exception in creating a session in MIP", ex);
					}
				}
			} while (!blnSessionExists);

		} catch (Exception ex) {
			Logger.getLogger(ConnectToMip.class.getName()).log(Level.SEVERE,
					"Exception in fetching Session Id's from MIP", ex);
		}
		return sessionId;
	}

	/**
	 * Inserts a document into MIP on payment notification received from Salesforce
	 * 
	 * @param obj
	 *            : Push notification received from Salesforce for Payment
	 */
	@SuppressWarnings("unchecked")
	public static void InsertDocument(HashMap<String, JSONObject> obj) {
		String lstrAccountName;
		String lstrGLCode;

		try {
			MIPConstants.ReadFileForMipUrl();
			String lstrPath = MIPConstants.MIP_BASE_URI + "/api/te/CR/dc";
			JSONObject temp;
			temp = (JSONObject) ((new JSONParser().parse(obj.get("message").toJSONString())));
			JSONObject sObject = null;

			sObject = (JSONObject) temp.get("data");
			sObject = (JSONObject) sObject.get("sobject");
			System.out.println("___________________Insert Document________________");
			System.out.println("Path for fetching distribution : " + lstrPath);
			// JSONObject dist =MIP.interfaceData.get("MIPExtra").take();
			System.out.println("printing sobject" + sObject.toJSONString());
			// String lstrdistributionCode = dist.get("DistributionCode").toString();
			String lstrdistributionCode = obj.get("DistributionCode").get("DistributionCode").toString();
			// JSONObject ledger =MIP.interfaceData.get("MIPExtra").take();
			JSONArray arr = (JSONArray) obj.get("AccountNameAndGeneralLedgerCode")
					.get("AccountNameAndGeneralLedgerCode");
			System.out.println("printing account " + arr.toJSONString());
			System.out.println("1" + arr.get(1));
			System.out.println("0" + arr.get(0));
			lstrAccountName = (String) arr.get(0);
			lstrGLCode = (String) arr.get(1);

			JSONArray jsonArrFields = new JSONArray();
			JSONObject jsonObjFields = new JSONObject();
			JSONArray larrFinalTransaction = new JSONArray();
			JSONObject OffsetTransaction = new JSONObject();
			JSONObject ljsonResponse = new JSONObject();

			// Balance with Cash transaction
			/*
			 * {"fields":[{"TEUSEDISTCODE_DISTCODEID" : "<valid code>"},
			 * {"TEUSEDISTCODE_LINEDESC" : "<DIST_CODE_FROM_SF>"}, {"TEUSEDISTCODE_LINEDATE"
			 * : "DATE_FROM_SF>"}, {"TEUSEDISTCODE_LINEGL":"11000"}, // hard coded for cash
			 * {"TEUSEDISTCODE_AMT":"<AMT_FROM_SF>"},
			 * {"TEUSEDISTCODE_CREDIT_OR_DEBIT":"<Debit>"}]}
			 */
			if (MIPConstants.OFFSET.equalsIgnoreCase("true")) {
				jsonArrFields = new JSONArray();
				JSONObject ljsonDistCodeId2 = new JSONObject();
				ljsonDistCodeId2.put("TEUSEDISTCODE_DISTCODEID", lstrdistributionCode);
				jsonArrFields.add(ljsonDistCodeId2); // dist code

				JSONObject ljsonLineDesc2 = new JSONObject();
				ljsonLineDesc2.put("TEUSEDISTCODE_LINEDESC", lstrAccountName);
				jsonArrFields.add(ljsonLineDesc2);

				JSONObject ljsonLineDate2 = new JSONObject();
				ljsonLineDate2.put("TEUSEDISTCODE_LINEDATE",
						sObject.get("npe01__Payment_Date__c").toString().substring(0, 10));
				jsonArrFields.add(ljsonLineDate2);

				JSONObject ljsonLineGLCode2 = new JSONObject();
				ljsonLineGLCode2.put("TEUSEDISTCODE_LINEGL", MIPConstants.GLcode_CSH);
				jsonArrFields.add(ljsonLineGLCode2); // GL Code

				JSONObject ljsonAmount2 = new JSONObject();
				ljsonAmount2.put("TEUSEDISTCODE_AMT", sObject.get("npe01__Payment_Amount__c"));
				jsonArrFields.add(ljsonAmount2);// Amount from Salesforce

				JSONObject ljsonCreditDebit2 = new JSONObject();
				ljsonCreditDebit2.put("TEUSEDISTCODE_CREDIT_OR_DEBIT", "Debit");
				jsonArrFields.add(ljsonCreditDebit2); // Credit or Debit

				// Fields object
				jsonObjFields = new JSONObject();
				jsonObjFields.put("fields", jsonArrFields);

				LoginIntoMip();
				// Post to MIP - Debit
				System.out.println("_____________________________Debit____________________________");
				System.out.println("Request for Debit :\n" + jsonObjFields.toString());
				ljsonResponse = MIPRestCalls.RestPost(lstrPath, jsonObjFields, MIPConstants.REQUESTING_CLASS.MIP);
				System.out.println("Response for Debit : \n" + ljsonResponse.toJSONString());
				System.out.println("_________________________________________________________");

				if (ljsonResponse.containsKey("transactions")) {
					JSONArray larrDebit = (JSONArray) ljsonResponse.get("transactions");
					if (larrDebit.size() > 0)
						for (int count = 0; count < larrDebit.size(); count++) {
							JSONObject lobjDebit = new JSONObject();
							lobjDebit = (JSONObject) larrDebit.get(count);
							larrFinalTransaction.add(lobjDebit);
						}
				}

			}
			/*
			 * {"fields":[{"TEUSEDISTCODE_DISTCODEID" : "<valid code>"},
			 * {"TEUSEDISTCODE_LINEDESC" : "<DIST_CODE_FROM_SF>"}, {"TEUSEDISTCODE_LINEDATE"
			 * : "DATE_FROM_SF>"}, {"TEUSEDISTCODE_LINEGL":"<GL_FROM_SF>"},
			 * {"TEUSEDISTCODE_AMT":"<AMT_FROM_SF>"},
			 * {"TEUSEDISTCODE_CREDIT_OR_DEBIT":"<Credit>"}]}
			 */
			jsonArrFields = new JSONArray();

			JSONObject ljsonDistCodeId = new JSONObject();
			ljsonDistCodeId.put("TEUSEDISTCODE_DISTCODEID", lstrdistributionCode);
			jsonArrFields.add(ljsonDistCodeId); // dist code

			JSONObject ljsonLineDesc = new JSONObject();
			ljsonLineDesc.put("TEUSEDISTCODE_LINEDESC", lstrAccountName);
			jsonArrFields.add(ljsonLineDesc);

			JSONObject ljsonLineDate = new JSONObject();
			ljsonLineDate.put("TEUSEDISTCODE_LINEDATE",
					sObject.get("npe01__Payment_Date__c").toString().substring(0, 10));
			jsonArrFields.add(ljsonLineDate);

			JSONObject ljsonLineGLCode = new JSONObject();
			ljsonLineGLCode.put("TEUSEDISTCODE_LINEGL", lstrGLCode);
			jsonArrFields.add(ljsonLineGLCode); // GL Code

			JSONObject ljsonAmount = new JSONObject();
			ljsonAmount.put("TEUSEDISTCODE_AMT", sObject.get("npe01__Payment_Amount__c"));
			jsonArrFields.add(ljsonAmount);// Amount from Salesforce

			JSONObject ljsonCreditDebit = new JSONObject();
			ljsonCreditDebit.put("TEUSEDISTCODE_CREDIT_OR_DEBIT", "Credit");
			jsonArrFields.add(ljsonCreditDebit); // Credit or Debit

			// Fields object
			jsonObjFields = new JSONObject();
			jsonObjFields.put("fields", jsonArrFields);

			LoginIntoMip();
			// Post to MIP - Credit
			System.out.println("_____________________________Credit____________________________");
			System.out.println("Request for Credit : \n" + jsonObjFields.toJSONString());
			ljsonResponse = MIPRestCalls.RestPost(lstrPath, jsonObjFields, MIPConstants.REQUESTING_CLASS.MIP);
			System.out.println("Response for Credit : \n" + ljsonResponse.toJSONString());
			System.out.println("_________________________________________________________");

			// larrFinalTransaction = new JSONArray();
			if (MIPConstants.OFFSET.equalsIgnoreCase("true")) {
				if (ljsonResponse.containsKey("transactions")) {
					JSONArray larrCredit = (JSONArray) ljsonResponse.get("transactions");
					if (larrCredit.size() > 0)
						for (int count = 0; count < larrCredit.size(); count++) {
							JSONObject lobjCredit = new JSONObject();
							lobjCredit = (JSONObject) larrCredit.get(count);
							larrFinalTransaction.add(lobjCredit);
						}
				}
			} else {
				OffsetTransaction = MIPRestCalls.RestPost(
						MIPConstants.MIP_BASE_URI + "/api/te/cr/sessions/" + getSessionId() + "/documents/offsets",
						ljsonResponse, MIPConstants.REQUESTING_CLASS.MIP);
				System.out.println("printing values from offset");
				System.out.println(OffsetTransaction);
				if (OffsetTransaction.containsKey("transactions")) {
					JSONArray larrCredit = (JSONArray) OffsetTransaction.get("transactions");
					if (larrCredit.size() > 0)
						for (int count = 0; count < larrCredit.size(); count++) {
							JSONObject lobjCredit = new JSONObject();
							lobjCredit = (JSONObject) larrCredit.get(count);
							larrFinalTransaction.add(lobjCredit);
						}
				}

			}

			// JSONArray larrTransaction = new JSONArray();
			// larrTransaction.add(lobjCredit);
			// larrTransaction.add(larrFinalDebit);

			JSONObject ljsonInsertDocument = new JSONObject();
			JSONArray larrReverseSetup = new JSONArray();
			JSONArray larrUserDefinedFields = new JSONArray();
			ljsonInsertDocument.put("reverseSetup", larrReverseSetup);
			ljsonInsertDocument.put("userDefinedFields", larrUserDefinedFields);
			ljsonInsertDocument.put("transactions", larrFinalTransaction);

			JSONObject lobjDocNumber = new JSONObject();
			lobjDocNumber.put("TEDOC_DOCNUM", sObject.get("npe01__Check_Reference_Number__c"));

			JSONObject lobjDescription = new JSONObject();
			lobjDescription.put("TEDOC_DESCRIPTION", lstrAccountName);

			JSONObject lobjDocDate = new JSONObject();
			lobjDocDate.put("TEDOC_DOCDATE", sObject.get("npe01__Payment_Date__c").toString().substring(0, 10));

			JSONObject lobjDocAmount = new JSONObject();
			lobjDocAmount.put("TEDOC_SRC_AMOUNT", sObject.get("npe01__Payment_Amount__c"));
			;

			JSONArray larrFields = new JSONArray();
			larrFields.add(lobjDocNumber);
			larrFields.add(lobjDescription);
			larrFields.add(lobjDocDate);
			larrFields.add(lobjDocAmount);
			ljsonInsertDocument.put("fields", larrFields);

			lstrPath = MIPConstants.MIP_BASE_URI + "/api/te/CR/sessions/" + getSessionId() + "/documents";

			LoginIntoMip();
			// Post Document to Session
			System.out.println("_____________________________Add Document to Session____________________________");
			System.out.println("Path for inserting document : \n" + lstrPath);
			System.out.println("Request for inserting document :\n" + ljsonInsertDocument.toString());
			ljsonResponse = MIPRestCalls.RestPost(lstrPath, ljsonInsertDocument, MIPConstants.REQUESTING_CLASS.MIP);
			System.out.println("_________________________________________________________");
			System.out.println("_____________________________END____________________________");

		} catch (Exception ex) {
			Logger.getLogger(ConnectToMip.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
