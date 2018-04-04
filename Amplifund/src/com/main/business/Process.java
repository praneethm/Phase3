package com.main.business;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import main.core.AMPLIFUND;

/**
 * 
 * @author Jon Cornado Class to insert data to Amplifund
 *
 */

public class Process {

	static String GL_CODE_ID = "";
	static String mipGlAccountId_id = "";
	static String targetGrantId = "";
	static String targetBudgetItemId = "";
	/*
	 * static String mipGrantId = "101"; static String mipGlAccountId = "57001";
	 */
	static String mipGrantId;
	static String mipGlAccountId;
	static HttpResponse lobjHttpResponse;
	static HttpGet lobjHttpGet;
	static HttpPost lobjHttpPost;
	static String[] docs;
	static String obj;
	static Connection conn;
	static PreparedStatement ps;
	static ResultSet rs = null;
	static String Description;
	static String DirectCost;
	static String ExpenseDateUtc;
	static String AppToken;
	static String UserToken;

	/**
	 * 
	 * @param mipGrantId
	 * @param mipGlAccountId
	 * @param Description
	 * @param DirectCost
	 * @param ExpenseDateUtc
	 * @param syncDate
	 * 
	 * 
	 *            Inserting a document involves multiple steps (which are explained
	 *            before each step). Every step involves get call to amplifund that
	 *            returns a json object. If we get desired id's from all the steps
	 *            then we insert the record into the amplifund, else we simply store
	 *            the expense details in database. At the end of each insert we
	 *            update the sync date in database. This date is later used my mip
	 *            plugin to read the expenses only till the sync date.
	 * 
	 */
	public static boolean execute(String name, String mipGrantId, String mipGlAccountId, String Description, String DirectCost,
			String ExpenseDateUtc, String syncDate, boolean batch) {

		Process.mipGrantId = mipGrantId;
		Process.mipGlAccountId = mipGlAccountId;
		Process.Description = Description;
		Process.DirectCost = DirectCost;
		Process.ExpenseDateUtc = ExpenseDateUtc;
		URI uri = null;
		conn = AMPLIFUND.conn;
		System.out.println("running amplifund expense code");

		// first step is to find mipGlAccountId_id from amplifund that has a field with
		// value "GL CODE"
		try {

			uri = new URI(Constants.URL + "/api/GeneralLedgerAccounts/GetAllFieldDefinitions");

			HttpClient lobjclient = HttpClientBuilder.create().build();
			lobjHttpGet = new HttpGet(uri);
			lobjHttpGet.addHeader(new BasicHeader("AppToken", Constants.APPTOKEN)); // "5F2BDA0A-4E32-4F50-B44F-A870945258XX"
			lobjHttpGet.addHeader(new BasicHeader("UserToken", Constants.USERTOKEN)); // "praneeth.manubolu1@marist.edu"
			System.out.println("Apptoken" + Constants.APPTOKEN);
			System.out.println("UserToken" + Constants.USERTOKEN);
			lobjHttpResponse = lobjclient.execute(lobjHttpGet);
			obj = EntityUtils.toString(lobjHttpResponse.getEntity());
			// System.out.println(obj);
			docs = stringToDocs(obj);
			for (String string : docs) {
				JSONObject jobj = (JSONObject) new JSONParser().parse(string);
				if (jobj.get("Name").toString().equalsIgnoreCase("GL CODE")) {
					GL_CODE_ID = (String) jobj.get("Id");
				}
			}
			System.out.println("GL_CODE_ID" + GL_CODE_ID);
			if (GL_CODE_ID.equalsIgnoreCase("")) {
				System.out.println("records insertion failed after GL CODE " + syncDate);
				if (!batch)
					saveFailedTransactions(name, Description, DirectCost, ExpenseDateUtc,mipGrantId,mipGlAccountId, conn);
				return false;
			}

			// second step is to find the mipGlAccountId_id that matches with mipGlAccountId

			uri = new URI(Constants.URL + "/api/generalledgeraccounts/getall");
			lobjHttpGet = new HttpGet(uri);
			lobjHttpGet.addHeader(new BasicHeader("AppToken", Constants.APPTOKEN));
			lobjHttpGet.addHeader(new BasicHeader("UserToken", Constants.USERTOKEN));
			lobjHttpResponse = lobjclient.execute(lobjHttpGet);

			obj = EntityUtils.toString(lobjHttpResponse.getEntity());
			// System.out.println(obj);
			docs = stringToDocs(obj);
			for (String string : docs) {
				// System.out.println(string);
				JSONObject jobj = (JSONObject) new JSONParser().parse(string);
				JSONObject temp = (JSONObject) new JSONParser().parse(jobj.get("FieldValues").toString());
				if (temp.get("Value").toString().equalsIgnoreCase(mipGlAccountId)) {
					mipGlAccountId_id = (String) jobj.get("Id");
				}

			}
			System.out.println("mipGlAccountId_id " + mipGlAccountId_id);
			if (mipGlAccountId_id.equalsIgnoreCase("")) {
				System.out.println("records insertion failed after MIPGLACCOUNT " + syncDate);
				if (!batch)
					saveFailedTransactions(name, Description, DirectCost, ExpenseDateUtc,mipGrantId,mipGlAccountId, conn);
				return false;
			}

			// Third step is to match mipGrantId and find the corresponding targetGrantId
			uri = new URI(Constants.URL + "/api/grants/getall");
			lobjHttpGet = new HttpGet(uri);
			lobjHttpGet.addHeader(new BasicHeader("AppToken", Constants.APPTOKEN));
			lobjHttpGet.addHeader(new BasicHeader("UserToken", Constants.USERTOKEN));
			lobjHttpResponse = lobjclient.execute(lobjHttpGet);

			obj = EntityUtils.toString(lobjHttpResponse.getEntity());
			// System.out.println(obj);
			docs = stringToDocs(obj);

			for (String string : docs) {

				// System.out.println(string);
				String temp = "{" + string.substring(1, string.length() - 1).replace("\\{\\}", "\"\"")
						.replace("{", "\"").replace("}", "\"") + "}";
				// System.out.println(temp);
				JSONObject jobj = (JSONObject) new JSONParser().parse(temp);
				String holder1 = (String) jobj.get("AdditionalInformation");
				System.out.println("holing value of additionalinformation " + holder1);
				// System.out.println(holder1 +" is the value of additional information--
				// "+jobj.get("AdditionalInformation")==null);
				if (jobj.get("AdditionalInformation") == null) {
					continue;
				}
				if (jobj.get("AdditionalInformation").toString().equalsIgnoreCase(mipGrantId)) {
					String holder = String.valueOf(jobj.get("Id"));
					if (holder.equalsIgnoreCase("null")) {
						System.out.println("GOING TO NEXT ITERATION");
						continue;
					}
					targetGrantId = String.valueOf(jobj.get("Id"));
				} else {
					continue;
				}

			}
			// {"Name":"Health
			// Appeals","Description":null,"AwardDetails":null,"EligibilityRequirements":null,"AdditionalInformation":"101","AwardType":"Grant","AwardStatus":"Approved","GrantorId":18293,"GrantManagerId":42522,"RequestForProposalIdentificationNumber":null,"FundingOpportunityIdentificationNumber":null,"CatalogOfFederalDomesticAssistanceNumber":null,"LetterOfIntentRequired":"False","LetterOfIntentDueDate":"","ProposedLengthOfAwardYears":"1","ProposedLengthOfAwardMonths":null,"ProposalOpenDate":"1/1/2017","ProposalCloseDate":"3/31/2017","ProjectedReceiptDate":"6/1/2017","RequestedAmount":"200000.00000","CashMatchRequirementAmount":"0.00000","InKindMatchRequirementAmount":"0.00000","AwardedAmount":"200000.00000","CashMatchAmount":"0.00000","InKindMatchAmount":"0.00000","DepartmentIds":{10218},"GrantWriterIds":{42523},"SubjectIds":{},"AwardedDateUtc":"6/1/2017","LengthOfAwardYears":"1","LengthOfAwardMonths":"0","StartDateUtc":"6/5/2017","EndDateUtc":"6/4/2018","CloseOutDateUtc":"11/28/2018","ExtensionApprovedDateUtc":"","ExtendedEndDateUtc":"","DeniedDateUtc":"","FederalGrantIdentificationNumber":null,"ActivityCode":null,"FederalAgency":null,"FederalAgencyIdentificationNumber":null,"RecipientAccountNumber":null,"Id":21638}
			System.out.println("targetGrantId " + targetGrantId);
			if (targetGrantId.equalsIgnoreCase("")) {
				System.out.println("records insertion failed after targetgrantid " + syncDate);
				if (!batch)
					saveFailedTransactions(name, Description, DirectCost, ExpenseDateUtc,mipGrantId,mipGlAccountId, conn);
				return false;
			}

			// Final step is to find targetBudgetItemId which is used to insert the expense
			uri = new URI(Constants.URL + "/api/budgetitems/GetByGeneralLedgerAccount/" + mipGlAccountId_id);
			lobjHttpGet = new HttpGet(uri);
			lobjHttpGet.addHeader(new BasicHeader("AppToken", Constants.APPTOKEN));
			lobjHttpGet.addHeader(new BasicHeader("UserToken", Constants.USERTOKEN));
			System.out.println("before calling api");
			lobjHttpResponse = lobjclient.execute(lobjHttpGet);
			System.out.println("status from uri " + lobjHttpResponse.getStatusLine());
			obj = EntityUtils.toString(lobjHttpResponse.getEntity());
			// System.out.println(obj);
			System.out.println("newly added sysout before string to doc" + docs);
			docs = stringToDocs(obj);
			System.out.println("number of docs" + docs.length);
			if (docs.length <= 1) {
				System.out.println("records insertion as no records found for  " + mipGlAccountId_id);
				if (!batch)
					saveFailedTransactions(name, Description, DirectCost, ExpenseDateUtc,mipGrantId,mipGlAccountId, conn);
				return false;
			}
			for (String string : docs) {

				System.out.println(string);
				String temp = "{" + string.substring(1, string.length() - 1).replace("\\{\\}", "\"\"")
						.replace("{", "\"").replace("}", "\"") + "}";
				// System.out.println(temp);
				JSONObject jobj = (JSONObject) new JSONParser().parse(temp);
				// JSONObject temp = (JSONObject) new
				// JSONParser().parse(jobj.get("FieldValues").toString());
				if (jobj.get("GrantId") == null) {
					continue;
				}
				if (jobj.get("GrantId").toString().equalsIgnoreCase(targetGrantId)) {
					targetBudgetItemId = String.valueOf(jobj.get("Id"));
				} else {
					continue;
				}
			}
			// {"Name":"Health
			// Appeals","Description":null,"AwardDetails":null,"EligibilityRequirements":null,"AdditionalInformation":"101","AwardType":"Grant","AwardStatus":"Approved","GrantorId":18293,"GrantManagerId":42522,"RequestForProposalIdentificationNumber":null,"FundingOpportunityIdentificationNumber":null,"CatalogOfFederalDomesticAssistanceNumber":null,"LetterOfIntentRequired":"False","LetterOfIntentDueDate":"","ProposedLengthOfAwardYears":"1","ProposedLengthOfAwardMonths":null,"ProposalOpenDate":"1/1/2017","ProposalCloseDate":"3/31/2017","ProjectedReceiptDate":"6/1/2017","RequestedAmount":"200000.00000","CashMatchRequirementAmount":"0.00000","InKindMatchRequirementAmount":"0.00000","AwardedAmount":"200000.00000","CashMatchAmount":"0.00000","InKindMatchAmount":"0.00000","DepartmentIds":{10218},"GrantWriterIds":{42523},"SubjectIds":{},"AwardedDateUtc":"6/1/2017","LengthOfAwardYears":"1","LengthOfAwardMonths":"0","StartDateUtc":"6/5/2017","EndDateUtc":"6/4/2018","CloseOutDateUtc":"11/28/2018","ExtensionApprovedDateUtc":"","ExtendedEndDateUtc":"","DeniedDateUtc":"","FederalGrantIdentificationNumber":null,"ActivityCode":null,"FederalAgency":null,"FederalAgencyIdentificationNumber":null,"RecipientAccountNumber":null,"Id":21638}
			System.out.println("targetBudgetItemId" + targetBudgetItemId);
			if (targetBudgetItemId.equalsIgnoreCase("")) {
				System.out.println("records insertion failed after targetbudgetid " + syncDate);
				if (!batch)
					saveFailedTransactions(name, Description, DirectCost, ExpenseDateUtc,mipGrantId,mipGlAccountId, conn);
				return false;
			}

			// This is where an expense is added to Amplifund
			uri = new URI(Constants.URL + "/api/expenses/add");
			lobjHttpPost = new HttpPost(uri);
			lobjHttpPost.addHeader(new BasicHeader("AppToken", Constants.APPTOKEN));
			lobjHttpPost.addHeader(new BasicHeader("UserToken", Constants.USERTOKEN));
			JSONObject ljsonExpense = new JSONObject();
			ljsonExpense.put("BudgetItemId", targetBudgetItemId);
			ljsonExpense.put("Description", Description);
			ljsonExpense.put("DirectCost", DirectCost);
			ljsonExpense.put("ExpenseDateUtc", ExpenseDateUtc);
			StringEntity lstrRequestBody = new StringEntity(ljsonExpense.toJSONString());
			lstrRequestBody.setContentType("application/json");
			lobjHttpPost.setEntity(lstrRequestBody);
			lobjHttpResponse = lobjclient.execute(lobjHttpPost);
			System.out.println("amplifund post completed: displaying amplifund result");
			System.out.println(EntityUtils.toString(lobjHttpResponse.getEntity()));
			// System.out.println(ljsonExpense.toJSONString());
			// donot save sync date if its for batch process
			if (!batch) {
				ps = conn.prepareStatement("select * from upd_string(?)");
				ps.setString(1, syncDate);
				rs = ps.executeQuery();
			}
		} catch (SQLException e) { // TODO Auto-generated catch block e.printStackTrace(); }

		} catch (ClientProtocolException e) {
			// add code here if transaction fails due to system level errors. add to the
			// other catch statements as well
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * 
	 * @param BudgetItemId
	 * @param Description
	 * @param DirectCost
	 * @param ExpenseDateUtc
	 * @param conn
	 * 
	 *            These values are stored in database as failed transactions
	 */
	public static void saveFailedTransactions(String name, String Description, String DirectCost,
			String ExpenseDateUtc,String grant,String gl, Connection conn) {
		PreparedStatement ps;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("select * from ins_af_failed_trans(?,?,?,?,?,?)");
			ps.setString(1, name);
			ps.setString(2, Description);
			ps.setString(3, DirectCost);
			ps.setString(4, ExpenseDateUtc);
			ps.setString(5, grant);
			ps.setString(6, gl);
			ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("could not save failed transactions in DB");
		}

	}

	/**
	 * 
	 * @param fullDoc
	 * @return
	 * 
	 * 		This method splits multiple json records and puts them in a string
	 *         array. It replaces few special characters that normally break when
	 *         tried to convert it to a JSONObject
	 * 
	 * 
	 */
	public static String[] stringToDocs(String fullDoc) {
		String temp = fullDoc.substring(1, fullDoc.length() - 1);
		// System.out.println(temp);
		String[] docs = temp.split("\\},\\{");
		// JSONObject jobj= (JSONObject) new JSONParser().parse(obj);
		int length = docs.length;
		for (int i = 0; i < length; i++) {
			if (length > 1) {
				if (i == 0)
					docs[i] = docs[i] + "}";
				else if (i == length - 1) {
					docs[i] = "{" + docs[i];
				} else {
					docs[i] = "{" + docs[i] + "}";
				}
			}

			docs[i] = docs[i].replace("[{", "{");
			docs[i] = docs[i].replace("}]", "}");
			docs[i] = docs[i].replace("[", "{");
			docs[i] = docs[i].replace("]", "}");
			docs[i] = docs[i].replace("{}", "\"\"");
			docs[i] = docs[i].replace("[]", "\"\"");
			// System.out.println(docs[i]);
		}

		return docs;

	}

}
