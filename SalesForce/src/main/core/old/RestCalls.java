package main.core.old;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Contains the GET, POST and PATCH functions which can be used to make RESTful
 * calls.
 *
 * @author Saranya
 */
public class RestCalls {

	private static final Logger LOGGER = Logger.getLogger(RestCalls.class.getName());

	/**
	 * Retrieves a JSON response from the give URL
	 *
	 * @param pstrPath:
	 *            Request URL from which data is to be retrieved.
	 * @param lenumRequestingClass
	 * @return : JSON response received from the REST URL.
	 */
	public static JSONObject RestGet(String pstrPath, Constants.REQUESTING_CLASS lenumRequestingClass) {

		try {
			JSONObject lobjJsonObject = null;
			HttpGet lobjHttpGet = new HttpGet(pstrPath);

				lobjHttpGet.addHeader(Constants.OAUTH_HEADER);
				lobjHttpGet.addHeader(Constants.PRETTY_PRINT_HEADER);


			HttpClient lobjclient = HttpClientBuilder.create().build();
			// Make the request.
			HttpResponse lobjHttpResponse = lobjclient.execute(lobjHttpGet);
			// Process the result
			int lintStatusCode = lobjHttpResponse.getStatusLine().getStatusCode();
			if (lintStatusCode == HttpStatus.SC_OK) {

				String lstrResponseString = EntityUtils.toString(lobjHttpResponse.getEntity());

				if (!lstrResponseString.trim().isEmpty() || lstrResponseString.trim().length() > 0) {
					try {
						Object obj = (new JSONParser()).parse(lstrResponseString);
						lobjJsonObject = (JSONObject) obj;
						System.out.println("JSON result of Query:\n" + lobjJsonObject.toJSONString());
					} catch (ParseException je) {
						je.printStackTrace();
					}
				}
			} else {
				System.out.println("Query was unsuccessful. Status code returned is " + lintStatusCode);
			}
			return lobjJsonObject;
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "IOException occurred in RestGet Method", ex);
			return null;
		}
	}

	/**
	 * Inserts values at the given REST URL using JSON object.
	 *
	 * @param pstrPath
	 *            : Request URL at which data is to be inserted.
	 * @param pjsonValuesToPost
	 *            : JSON object containing values to be inserted.
	 * @param lenumRequestingClass
	 * @return : JSON response received from the REST URL.
	 */
	public static JSONObject RestPost(String pstrPath, JSONObject pjsonValuesToPost,
			Constants.REQUESTING_CLASS lenumRequestingClass) {

		JSONObject ljsonResponse = null;
		try {
			// Construct the objects needed for the request
			HttpClient lhttpclientObject = HttpClientBuilder.create().build();
			HttpPost lhttppostObject = new HttpPost(pstrPath);


				lhttppostObject.addHeader(Constants.OAUTH_HEADER);
				lhttppostObject.addHeader(Constants.PRETTY_PRINT_HEADER);
				

			// The message we are going to post
			StringEntity lstrRequestBody = new StringEntity(pjsonValuesToPost.toJSONString());
			lstrRequestBody.setContentType("application/json");
			lhttppostObject.setEntity(lstrRequestBody);

			// Make the request
			HttpResponse lhttpresponseObject = lhttpclientObject.execute(lhttppostObject);

			// Process the results
			int lintResponseStatusCode = lhttpresponseObject.getStatusLine().getStatusCode();
			System.out.println("Response Status Code: "+ lintResponseStatusCode);
			switch (lintResponseStatusCode) {
			case HttpStatus.SC_OK:
			case HttpStatus.SC_CREATED:
				String response_string = EntityUtils.toString(lhttpresponseObject.getEntity());

				if (!response_string.trim().isEmpty() || response_string.trim().length() > 0) {
					try {
						Object obj = (new JSONParser()).parse(response_string);
						ljsonResponse = (JSONObject) obj;
					} catch (ParseException je) {
						je.printStackTrace();
					}
				}
				break;
			default:
				switch (lenumRequestingClass) {
				case SALESFORCE:
					System.out.println("Insertion unsuccessful. Status code returned is " + lintResponseStatusCode);
					break;
				case MIP:
					System.out.println("Login unsuccessful. Status code returned is " + lintResponseStatusCode);
					break;
				}
				break;
			}
		} catch (Exception ex) {
			Logger.getLogger(RestCalls.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ljsonResponse;
	}

	/**
	 * Inserts values at the given REST URL using Form URL Encoding. This method
	 * is used during the login OAuth process.
	 *
	 * @param pstrPath
	 *            : Request URL at which data is to be inserted.
	 * @param pstrValuesToPost
	 *            : Form encoding values needed for Authentication.
	 * @return : JSON response received from the REST URL.
	 */
	public static JSONObject RestPostWithFormEncoding(String pstrPath, String pstrValuesToPost) {
		JSONObject ljsonResponse = null;
		try {
			HttpClient lhttpclientObject = HttpClientBuilder.create().build();
			HttpPost lhttppostObject = new HttpPost(pstrPath);
			StringEntity lstrRequestBody = new StringEntity(pstrValuesToPost);

			lstrRequestBody.setContentType("application/x-www-form-urlencoded");
			lhttppostObject.setEntity(lstrRequestBody);

			lhttppostObject.addHeader(Constants.PRETTY_PRINT_HEADER);
			System.out.println("\n\n Before Execute " + lhttppostObject.toString());
			// Make the request and store the result
			HttpResponse lhttpresponseObject = lhttpclientObject.execute(lhttppostObject);

			if (lhttpresponseObject.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String lstrResponseString = EntityUtils.toString(lhttpresponseObject.getEntity());
				if (!lstrResponseString.trim().isEmpty() || lstrResponseString.trim().length() > 0) {
					try {
						Object obj = (new JSONParser()).parse(lstrResponseString);
						ljsonResponse = (JSONObject) obj;
						System.out.println("JSON returned by response: \n" + ljsonResponse.toJSONString());
					} catch (ParseException je) {
						je.printStackTrace();
					}
				}
			} else {
				System.out.println(
						"An error has occured. Http status: " + lhttpresponseObject.getStatusLine().getStatusCode());
				System.out.println(formatInputStream(lhttpresponseObject.getEntity().getContent()));
				//System.exit(-1);
			}
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(RestCalls.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(RestCalls.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ljsonResponse;
	}

	/**
	 * Updates values at the given REST URL using JSON object.
	 *
	 * @param pstrPath
	 *            : Request URL at which data is to be updated.
	 * @param pjsonValuesToUpdate
	 *            : JSON object containing values to be updated.
	 * @return : Status code which specifies if the update was successful.
	 */
	public static int RestPatch(String pstrPath, JSONObject pjsonValuesToUpdate) {
		try {
			// Set up the objects necessary to make the request.
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPatch httpPatch = new HttpPatch(pstrPath);
			httpPatch.addHeader(Constants.OAUTH_HEADER);
			httpPatch.addHeader(Constants.PRETTY_PRINT_HEADER);
			StringEntity body = new StringEntity(pjsonValuesToUpdate.toJSONString());
			body.setContentType("application/json");
			httpPatch.setEntity(body);

			// Make the request
			HttpResponse response = httpClient.execute(httpPatch);

			// Process the response
			return response.getStatusLine().getStatusCode();
		} catch (Exception ex) {
			Logger.getLogger(RestCalls.class.getName()).log(Level.SEVERE, null, ex);
		}
		return -1;
	}

	/**
	 * Reads a stream of input and converts it into displayable format by
	 * inserting line feed.
	 *
	 * @param pinputstreamReceived
	 *            : Stream of data received in a single line.
	 * @return lstrFinalInput : Formatted data with line feed.
	 */
	private static String formatInputStream(InputStream pinputstreamReceived) {
		String lstrFinalInput = "";
		try {
			try (BufferedReader lbuffReader = new BufferedReader(new InputStreamReader(pinputstreamReceived))) {
				String lstrInputLine;
				while ((lstrInputLine = lbuffReader.readLine()) != null) {
					lstrFinalInput += lstrInputLine;
					lstrFinalInput += "\n";
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return lstrFinalInput;
	}

	public static String RestGetDocument(String pstrPath, Constants.REQUESTING_CLASS lenumRequestingClass) {
		try {
			HttpGet lobjHttpGet = new HttpGet(pstrPath);

				lobjHttpGet.addHeader(Constants.OAUTH_HEADER);
				lobjHttpGet.addHeader(Constants.PRETTY_PRINT_HEADER);


			HttpClient lobjclient = HttpClientBuilder.create().build();
			// Make the request.
			HttpResponse lobjHttpResponse = lobjclient.execute(lobjHttpGet);

			// Process the result
			int lintStatusCode = lobjHttpResponse.getStatusLine().getStatusCode();
			if (lintStatusCode == HttpStatus.SC_OK) {
				return EntityUtils.toString(lobjHttpResponse.getEntity());

			} else {
				System.out.println("Query was unsuccessful. Status code returned is " + lintStatusCode);
			}
			return "";
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "IOException occurred in RestGet Method", ex);
			return "";
		}
	}

	public static JSONArray RestGetJsonArray(String pstrPath, Constants.REQUESTING_CLASS lenumRequestingClass) {

		try {
			JSONArray larrJsonObject = null;
			HttpGet lobjHttpGet = new HttpGet(pstrPath);

				lobjHttpGet.addHeader(Constants.OAUTH_HEADER);
				lobjHttpGet.addHeader(Constants.PRETTY_PRINT_HEADER);


			HttpClient lobjclient = HttpClientBuilder.create().build();
			// Make the request.
			HttpResponse lobjHttpResponse = lobjclient.execute(lobjHttpGet);
			// Process the result
			int lintStatusCode = lobjHttpResponse.getStatusLine().getStatusCode();
			if (lintStatusCode == HttpStatus.SC_OK) {

				String lstrResponseString = EntityUtils.toString(lobjHttpResponse.getEntity());

				if (!lstrResponseString.trim().isEmpty() || lstrResponseString.trim().length() > 0) {
					try {
						Object obj = (new JSONParser()).parse(lstrResponseString);
						larrJsonObject = (JSONArray) obj;
						System.out.println("JSON result of Query:\n" + larrJsonObject.toJSONString());
					} catch (ParseException je) {
						je.printStackTrace();
					}
				}
			} else {
				System.out.println("Query was unsuccessful. Status code returned is " + lintStatusCode);
			}
			return larrJsonObject;
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "IOException occurred in RestGet Method", ex);
			return null;
		}
	}

}
