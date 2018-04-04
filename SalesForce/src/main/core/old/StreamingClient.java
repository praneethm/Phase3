/**
 * Reference for Streaming API
 *  1. https://developer.salesforce.com/docs/atlas.en-us.api_streaming.meta/api_streaming/create_a_pushtopic.htm#create_a_pushtopic
 *  2. http://blog.terrasky.com/quick-tips-on-streaming-api-for-salesforce-developers
 *
 *
 *  Note: To Create a query in Push Topic, refer to Reference #1
 *  To find the fields for the query,
 *  1. Switch to lightening mode.
 *  2. Go to Setup Home
 *  3. Click on Objects and Fields.
 *  4. Go to Object Manager.
 *  5. Select the Label for which you need to create a Push Topic E.g.: Account
 *  6. Scroll down to "Fields and Relationships", the "Field Name"
 *     column is to be used in the query.
 *  7. "API Name" will be be the table name for the query.
 *
 *  To understand Bayeux Protocol:
 *  1. https://docs.cometd.org/current/reference/#_bayeux
 *
 */
package main.core.old;

import org.cometd.bayeux.Bayeux;
import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.client.ClientSessionChannel.MessageListener;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import main.core.salesForce;
import main.core.Plugin.statusType;
import main.core.mail.Format;
import main.core.mail.Mailing;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 *
 * @author Saranya
 */
public class StreamingClient {

	private static final boolean VERSION_22 = false;
	// private static final boolean USE_COOKIES = VERSION_22;

	// The channel to subscribe to. Same as the name of the PushTopic.
	// Be sure to create this topic before running this sample.
	private static final String PAID_INSERT = VERSION_22 ? "/PaidInsert" : "/topic/PaidInsert";
	private static final String STREAMING_ENDPOINT_URI = VERSION_22 ? "/cometd" : "/cometd/37.0";

	// The long poll duration.
	private static final int CONNECTION_TIMEOUT = 20 * 1000; // milliseconds
	private static final int READ_TIMEOUT = 120 * 1000; // milliseconds
	private Bayeux client;

	/**
	 *
	 */
	@SuppressWarnings("unused")
	public StreamingClient() {
		System.out.println("running streaming client");
		salesForce.setErrorMessage(
				new Exception("Failed to connect to BayeuxClient used for receiving push notifications"), false);
		try {

			ConnectToSalesForce lconnectToSalesForce = new ConnectToSalesForce();
			System.out.println("Running streaming client....");

			client = createBayeuxClient();

			// To understand the META CHANNEL concept
			// Refer :
			// http://cometdproject.dojotoolkit.org/documentation/cometd-javascript/subscription
			// Two tasks are performed during a META HANDSHAKE
			// 1. The client and Server negotiate the type of transport to use.
			// 2. The server informs the client with the detailed timings of the
			// requests.
			// Refer :
			// http://cometdproject.dojotoolkit.org/documentation/cometd-javascript/handshake
			((BayeuxClient) client).getChannel(Channel.META_HANDSHAKE)
					.addListener((ClientSessionChannel.MessageListener) new MessageListener() {
						@Override
						public void onMessage(ClientSessionChannel channel, Message message) {
							System.out.println("[CHANNEL:META_HANDSHAKE]: " + message);

							boolean success = message.isSuccessful();
							if (!success) {
								String error = (String) message.get("error");
								if (error != null) {
									System.out.println("Error during HANDSHAKE: " + error);
									// StreamingClient sc= new StreamingClient();
									// System.out.println("Started the streaming client again");
									// System.exit(1);

								}

								Exception exception = (Exception) message.get("exception");
								if (exception != null) {
									System.out.println("Exception during HANDSHAKE: ");
									exception.printStackTrace();
									System.out.println("Exiting...");
									//System.exit(1);

								}
								salesForce.setErrorMessage(new Exception(
										"Failed to connect to BayeuxClient used for receiving push notifications"),
										true);
							} else {

							}
						}
					});

			// Subcribe to the meta/connect channel,
			// which gives the status of the current connection with the Bayeux
			// server.
			// Refer :
			// http://cometdproject.dojotoolkit.org/documentation/cometd-javascript/subscription
			((BayeuxClient) client).getChannel(Channel.META_CONNECT)
					.addListener((ClientSessionChannel.MessageListener) new MessageListener() {
						@Override
						public void onMessage(ClientSessionChannel channel, Message message) {
							System.out.println("[CHANNEL:META_CONNECT]: " + message);

							boolean success = message.isSuccessful();
							if (!success) {
								String error = (String) message.get("error");
								if (error != null) {
									System.out.println("Error during CONNECT: " + error);
									StreamingClient sc = new StreamingClient();
									System.out.println("Started the streaming client again");
									// System.out.println("Exiting...");
									// System.exit(1);
								}
							}
						}
					});

			// Subscribe to the channel
			((BayeuxClient) client).getChannel(Channel.META_SUBSCRIBE)
					.addListener(new ClientSessionChannel.MessageListener() {
						public void onMessage(ClientSessionChannel channel, Message message) {

							System.out.println("[CHANNEL:META_SUBSCRIBE]: " + message);
							boolean success = message.isSuccessful();
							if (!success) {
								String error = (String) message.get("error");
								if (error != null) {
									System.out.println("Error during SUBSCRIBE: " + error);
									System.out.println("Exiting...");
									// System.exit(1);
								}
								salesForce.setErrorMessage(new Exception(
										"Failed to connect to BayeuxClient used for receiving push notifications"),
										true);
							}
						}

					});

			((BayeuxClient) client).handshake();
			System.out.println("Waiting for handshake");

			boolean handshaken = ((BayeuxClient) client).waitFor(10 * 1000, BayeuxClient.State.CONNECTED);
			if (!handshaken) {
				System.out.println("Failed to handshake: " + client);
				// System.exit(1);
				salesForce.setErrorMessage(
						new Exception("Failed to connect to BayeuxClient used for receiving push notifications"), true);
			}

			System.out.println("Waiting for streamed data from your organization ...");
			if (handshaken) {
				((BayeuxClient) client).getChannel(PAID_INSERT).subscribe(new MessageListener() {
					@Override
					public void onMessage(ClientSessionChannel channel, Message message) {
						System.out.println("Received Message: " + message);
						System.out.println("recieved message from salesforce push notification");
						// salesForce.setInterfaceData("MIP", new JSONObject(message), false);
						JSONObject obj = new JSONObject();
						obj = new JSONObject(message);
						// String[] valueArr = obj.toJSONString().split("sobject");
						// JSONObject sObject = null;
						JSONObject temp;
						try {
							temp = (JSONObject) ((new JSONParser().parse(obj.toJSONString())));
							JSONObject sObject = null;
							sObject = (JSONObject) temp.get("data");
							sObject = (JSONObject) sObject.get("sobject");
							String id = sObject.get("npe01__Opportunity__c").toString();
							ConnectToSalesForce.getDistributionCode(id);
							ConnectToSalesForce.getAccountNameAndGeneralLedgerCode(id);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						salesForce.setInterfaceData("MIP", new JSONObject(message), true);

					}
				});
			} else {
				salesForce.setStatus(statusType.stopped);
			}
			salesForce.setStatus(statusType.running);
		} catch (Exception ex) {
			Logger.getLogger(StreamingClient.class.getName()).log(Level.SEVERE, null, ex);
			salesForce.setStatus(statusType.stopped);
			salesForce.setErrorMessage(
					new Exception("Failed to connect to BayeuxClient used for receiving push notifications"), true);
		}

	}

	/**
	 * Creates an HttpClient for communicating with Bayeux Server. Refer :
	 * http://cometdproject.dojotoolkit.org/documentation/cometd-java/client/
	 * handshake
	 *
	 * @return lbClient: BayeuxClient that will receive communication from the
	 *         BayeuxServer.
	 */
	private BayeuxClient createBayeuxClient() {
		try {
			HttpClient lhttpClient = new HttpClient();
			lhttpClient.setConnectTimeout(CONNECTION_TIMEOUT);
			lhttpClient.setTimeout(READ_TIMEOUT);
			lhttpClient.start();

			/*
			 * // This sections was used to login into SalesForce using SOAP API // And
			 * display a message with session id. // This is not required now as we have
			 * logged into SalesForce // in LoginUsingOAuth Page. // But if need be, change
			 * this section to login into SalesForce // by refering the login process is
			 * LoginUsingOAuth.
			 * 
			 * String[] pair = new String[]{Constants.ACCESS_TOKEN, Constants.INSTANCE_URL};
			 * 
			 * //SoapLoginUtil.login(httpClient, UserCredentials.userName, PASSWORD);
			 * 
			 * if (pair == null) { System.exit(1); }
			 * 
			 * assert pair.length == 2; final String sessionid = pair[0]; String endpoint =
			 * pair[1]; System.out.println( "Login successful!\nServer URL: " + endpoint +
			 * "\nSession ID=" + sessionid);
			 * 
			 */
			Map<String, Object> lmOptions = new HashMap<String, Object>();
			lmOptions.put(ClientTransport.TIMEOUT_OPTION, READ_TIMEOUT);
			LongPollingTransport llpTransport = new LongPollingTransport(lmOptions, lhttpClient) {

				@Override
				protected void customize(ContentExchange pcExchange) {
					super.customize(pcExchange);
					pcExchange.addRequestHeader("Authorization", "OAuth " + Constants.ACCESS_TOKEN);
				}
			};

			BayeuxClient lbClient = new BayeuxClient(setSalesForceStreamingEndpoint(), llpTransport);

			// These lines are used to maintain cookies with usercreds on client
			// side.
			// if (USE_COOKIES)
			// establishCookies(client, UserCredentials.userName, sessionid);
			return lbClient;
		} catch (Exception ex) {
			Logger.getLogger(StreamingClient.class.getName()).log(Level.SEVERE, null, ex);
			salesForce.setStatus(statusType.stopped);
			return null;
		}
	}

	/**
	 * Sets the Endpoint URL for the BayeuxClient that points to the BayeuxServer.
	 *
	 * @return : URL of the BayeuxServer.
	 */
	private static String setSalesForceStreamingEndpoint() {
		try {
			return new URL(Constants.INSTANCE_URL + STREAMING_ENDPOINT_URI).toExternalForm();
		} catch (MalformedURLException ex) {
			Logger.getLogger(StreamingClient.class.getName()).log(Level.SEVERE, null, ex);
			salesForce.setStatus(statusType.stopped);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			Format format = Format.build().addSystem("SALESFORCE").addError("Failed to Authenticate").addStack(sw.toString()).addsubject(ex.toString());
			Mailing.sendMail(format, "2");
			return null;
		}
	}

	public static void main(String[] args) {

		StreamingClient sc = new StreamingClient();

	}

	public boolean stopClient() {
		((BayeuxClient) client).disconnect();
		// ((BayeuxClient) client).abort();
		return ((BayeuxClient) client).isDisconnected();
	}

	/**
	 * Currently not in use. Has been used in the example provided in SalesForce
	 * developer page for Streaming API.
	 *
	 * @param client
	 * @param user
	 * @param sid
	 */
	/*
	 * private void establishCookies(BayeuxClient client, String user, String sid) {
	 * client.setCookie("com.salesforce.LocaleInfo", "us", 24 * 60 * 60 * 1000);
	 * client.setCookie("login", user, 24 * 60 * 60 * 1000); client.setCookie("sid",
	 * sid, 24 * 60 * 60 * 1000); client.setCookie("language", "en_US", 24 * 60 * 60
	 * * 1000); }
	 */
}
