package main.core.old;


import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class AmplifundUploader extends Thread {
	String recentUpDate = "05/27/2015";
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	Date startDate;
	Date syncDate;
	ArrayList<String> SESSION_SESSIONNUMID = new ArrayList<>();
	HashMap<String, ArrayList<String>> docsWithSession = new HashMap<>();
	static JSONObject jsonobjectDocs= new JSONObject();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ConnectToMip.LoginIntoMip("admin", "api", "http://ec2-52-27-100-34.us-west-2.compute.amazonaws.com:9001");
		String lstrPath = Constants.MIP_BASE_URI + "/api/te/apinvoices/sessions?status=posted";
		JSONArray ljsonSessionIds = RestCalls.RestGetJsonArray(lstrPath, Constants.REQUESTING_CLASS.MIP);
		System.out.println(ljsonSessionIds);

		for (Object object : ljsonSessionIds) {
			JSONObject jobj = (JSONObject) object;

			try {
				syncDate = df.parse(recentUpDate);
				startDate = df.parse((String) jobj.get("SESSION_SESSIONDATE"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (startDate.compareTo(syncDate) > 0)
				SESSION_SESSIONNUMID.add((String) jobj.get("SESSION_SESSIONNUMID"));
		}

		for (String object : SESSION_SESSIONNUMID) {
			String lstrPath1 = Constants.MIP_BASE_URI + "/api/te/apinvoices/sessions/" + object + "/documents";
			JSONArray ljsonSessionIds1 = RestCalls.RestGetJsonArray(lstrPath1, Constants.REQUESTING_CLASS.MIP);
			for (Object object2 : ljsonSessionIds1) {
				JSONObject jobj = (JSONObject) object2;
				System.out.println(jobj.get("TEDOC_DOCNUM"));
				if (docsWithSession.containsKey(object)) {
					docsWithSession.get(object).add((String) jobj.get("TEDOC_DOCNUM"));

				} else {
					ArrayList<String> holder= new ArrayList<>();
					holder.add((String) jobj.get("TEDOC_DOCNUM"));
					docsWithSession.put(object, holder);

				}

			}

		}

		System.out.println(
				"-----------------------------------------------------------------------------------------------------------------------------");
		for (String session : docsWithSession.keySet()) {
			for (String docs : docsWithSession.get(session)) {
				String lstrPath2 = Constants.MIP_BASE_URI + "/api/te/apinvoices/sessions/"+session+"/documents/"+docs;
				RestCalls.RestGetJsonArray(lstrPath2, Constants.REQUESTING_CLASS.MIP);
				System.out.println(jsonobjectDocs);

			}

		}

	}

	public static void main(String[] args) {

		Thread thread = new AmplifundUploader();
		thread.start();

	}

}
