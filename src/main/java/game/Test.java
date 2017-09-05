package game;

import game.logging.Log;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Test {

	private static final String TAG = Test.class.getSimpleName();

	public static void main(String[] args) {
		//ArrayList<AbilityPosition> data = DatabaseUtil.getHeroAbilityPositions(16);
		//Log.i(TAG, "Data : " + data.toString());

		//DatabaseUtil.updateAbilityPosition(13, 8,2);
		//Log.i(TAG, "Database : " + DatabaseUtil.getTalents().toString());


		Log.i(TAG, getLobbys());

		//Log.i(TAG, "Logging logging");
		//Log.i("DatabaseHandler", "Testing");
		//Log.i("GameLogic", "Testing");

//		ArrayList<Talent> talents = DatabaseUtil.getHeroTalents(12);
//		for(Talent talent : talents){
//			Log.i(TAG, "Talent : " + talent.toString());
//		}

//		int highestLevel = 14;
//		int levelDivider = 5;
//
//		int levelToPlay = highestLevel - (highestLevel % levelDivider) + 1;
//		Log.i(TAG, "Level to play : " + levelToPlay);

	}








	private static String getLobbys() {
		BufferedReader reader = null;
		try {
			URL url = null;
			try {
				url = new URL("http://www.warlord.ga/warlords_webservice/lobbys.json");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}


}
