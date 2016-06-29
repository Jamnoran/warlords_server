package game;

import com.google.gson.Gson;
import game.logging.Log;
import io.GameStatusResponse;
import util.GameCommunicationUtil;
import vo.Champion;
import vo.Message;
import vo.Minion;

import java.util.ArrayList;


/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class GameServer {
	private static final String TAG = GameServer.class.getSimpleName();
	private boolean gameRunning = true;
	private ServerDispatcher server;
	private GameCommunicationUtil gameCommunicationUtil = new GameCommunicationUtil(this);

	private ArrayList<Minion> minions = new ArrayList<>();
	private ArrayList<Champion> champions = new ArrayList<>();


	public GameServer(ServerDispatcher server) {
		this.server = server;
		// Start a thread that sends game status every second, this should be changed to when something happens in future
//		startStatusThread();
	}


	public GameCommunicationUtil getGameCommunicationUtil() {
		return gameCommunicationUtil;
	}

	public void characterJoined(String character_id) {
		champions.add(new Champion(character_id));
		Log.i(TAG, "Character joined with this characterID: " + character_id + " characters in game: " + champions.size());
		server.dispatchMessage(new Message("{data:\"character joined\"}"));
	}



















	public void sendStatusToAllClients() {
		sendGameStatus();
	}

	private void startStatusThread() {
		Thread thread = new Thread(){
			public void run(){
				while(gameRunning){
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Log.i(TAG, "Sending game status");
					sendGameStatus();
				}
			}
		};

		thread.start();
	}
	public void sendGameStatus(){
		Gson gson = new Gson();
		String jsonInString = gson.toJson(new GameStatusResponse(minions, champions));
		server.dispatchMessage(new Message(jsonInString));
	}

}
