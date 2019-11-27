package myapp.payment.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.JsonObject;
import com.paypal.api.payments.Links;
import com.paypal.base.HttpConnection;

/**
 * Utility class to evaluate {@link JSONObject} for a given links and accesstoken
 * 
 * @author Nandan
 *
 */
public class JSonUtility {

	
/**
 * Parse the {@link JsonObject} and evaluates whether the transaction is succesfull by considering the input for the transaction
 * 	
 * @param links
 * @param accessToken
 * @return
 * true if the Transaction succeed and false if the transaction fails
 */
public boolean evaluateJSonResponse(Links links, String accessToken) {
		
		boolean transaction = true;
		
		try {
			Thread.sleep(2000);

			HttpURLConnection conn = getUrlConnection(links, accessToken);

			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String output;

			while ((output = br.readLine()) != null) {
				JSONParser parse = new JSONParser();
				JSONObject jobj = (JSONObject) parse.parse(output);

				JSONArray itemsJS = (JSONArray) jobj.get("items");

				String Transaction_Status = (String) jobj.get("transaction_status");
				if (Transaction_Status != null && "UNCLAIMED".equals(Transaction_Status)) {
					transaction = false;
					break;
				}

				for (int i = 0; i < itemsJS.size(); i++) {
					JSONObject jsonObject = (JSONObject) itemsJS.get(i);

					JSONArray linksInJSonObject = (JSONArray) jsonObject.get("links");

					for (Object link : linksInJSonObject) {
						JSONObject obje = (JSONObject) link;
						Links linkTest = new Links();
						linkTest.setHref((String) obje.get("href"));
						return evaluateJSonResponse(linkTest,accessToken );
					}
				}
			}
			conn.disconnect();

		} catch (Exception e) {
			System.out.println("Exception in NetClientGet:- " + e);
		}
		return transaction;
	}

	/**
	 * Gets the {@link HttpURLConnection} for a given {@link Links} and the accessToken
	 * 
	 * @param links
	 * which establishes the connection 
	 * @param accessToken
	 * The request token from the response to RequestPermissions
	 * @return {@link HttpConnection}
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 */
	private HttpURLConnection getUrlConnection(Links links, String accessToken)
			throws MalformedURLException, IOException, ProtocolException {
		URL url = new URL(links.getHref());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Authorization", accessToken );
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
		}
		return conn;
	}

	
}
