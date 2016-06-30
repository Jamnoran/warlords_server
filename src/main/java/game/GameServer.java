package game;

import com.google.gson.Gson;
import game.logging.Log;
import io.GameStatusResponse;
import vo.Hero;
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

	private ArrayList<Minion> minions = new ArrayList<>();
	private ArrayList<Hero> heros = new ArrayList<>();


	public GameServer(ServerDispatcher server) {
		this.server = server;
		// Start a thread that sends game status every second, this should be changed to when something happens in future
//		startStatusThread();
	}


	public void addHero(Hero hero) {
		heros.add(hero);
		Log.i(TAG, "Hero joined with this user id: " + hero.getUser_id() + " characters in game: " + heros.size());
//		server.dispatchMessage(new Message("{\"response_type\":\"GAME_INFO\", data:\"character joined\"}"));
		sendGameStatus();
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
		String jsonInString = gson.toJson(new GameStatusResponse(minions, heros));
		server.dispatchMessage(new Message(jsonInString));
	}

}
