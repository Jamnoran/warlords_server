package game;

import game.logging.Log;
import vo.Message;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Test {

	private static final String TAG = Test.class.getSimpleName();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Log.i(TAG, "Saving information to firebase!");
		String classType = "WARRIOR";
		String user = "Emil";

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setServiceAccount(new FileInputStream("firebase/warlords-10aa9a5c0e12.json"))
				.setDatabaseUrl("https://databaseName.firebaseio.com/")
				.build();
		FirebaseApp.initializeApp(options);

	}

}
