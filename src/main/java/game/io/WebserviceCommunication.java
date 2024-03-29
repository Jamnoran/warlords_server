package game.io;

import com.google.gson.Gson;
import game.io.Responses.RegisterGameServerResponse;
import game.logging.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Eric on 2017-03-05.
 */
public class WebserviceCommunication {
	private static final String TAG = WebserviceCommunication.class.getSimpleName();
	private static String  registerServer = "http://jamnoran.se/warlords_webservice/register_server.php";
	private static final String USER_AGENT = "Mozilla/5.0";
	private static String ip = "127.0.0.1";
//		private static String ip = "2.248.122.35";
//	private static String ip = "192.168.0.207";
	private static String version = "1";

	public static RegisterGameServerResponse sendGameServerOnline(int portNumber) {
		String params;
		if(ip != null){
			params = "ip=" + ip + "&port=" + portNumber + "&version=" + version;
		}else{
			params = "port=" + portNumber + "&version=" + version;
		}
		try {

			return (RegisterGameServerResponse) sendRequest(params, registerServer, new RegisterGameServerResponse());
		} catch (Exception e) {
			Log.i(TAG, "Could not access webservice, check network status");
			e.printStackTrace();
		}
		return null;
	}

	private static JsonResponse sendRequest(String params, String url, JsonResponse responseObject) {
		try {
			URL obj = new URL(url);
			HttpURLConnection con;
			con = (HttpURLConnection) obj.openConnection();

			//add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			Log.i(TAG, "Sending 'POST' request to URL : " + url);
			Log.i(TAG, "Post parameters : " + params);
			Log.i(TAG, "Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			return new Gson().fromJson(response.toString(), responseObject.getClass());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
