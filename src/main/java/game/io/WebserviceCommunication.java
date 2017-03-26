package game.io;

import com.google.gson.Gson;
import game.Test;
import game.vo.Server;
import game.vo.ServerList;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Eric on 2017-03-05.
 */
public class WebserviceCommunication {
	private static final String TAG = Test.class.getSimpleName();
	private static String  registerServer = "http://localhost:8888/warlords_webservice/register_server.php";
	private static final String USER_AGENT = "Mozilla/5.0";
	private static String ip;
	private static String version = "1";

	public static RegisterGameServerResponse sendGameServerOnline(int portNumber) {
		String params;
		if(ip != null){
			params = "ip=" + ip + "&port=" + portNumber + "&version=" + version;
		}else{
			params = "port=" + portNumber + "&version=" + version;
		}
		return (RegisterGameServerResponse) sendRequest(params, registerServer, new RegisterGameServerResponse());
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
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + params);
			System.out.println("Response Code : " + responseCode);

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
